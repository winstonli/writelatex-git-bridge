package uk.ac.ic.wlgitbridge.writelatex;

import uk.ac.ic.wlgitbridge.writelatex.api.request.exception.FailedConnectionException;
import uk.ac.ic.wlgitbridge.writelatex.api.request.getdoc.SnapshotGetDocRequest;
import uk.ac.ic.wlgitbridge.writelatex.api.request.getdoc.SnapshotGetDocResult;
import uk.ac.ic.wlgitbridge.writelatex.api.request.getdoc.exception.InvalidProjectException;
import uk.ac.ic.wlgitbridge.writelatex.api.request.getforversion.SnapshotData;
import uk.ac.ic.wlgitbridge.writelatex.api.request.getforversion.SnapshotGetForVersionRequest;
import uk.ac.ic.wlgitbridge.writelatex.api.request.getforversion.SnapshotGetForVersionResult;
import uk.ac.ic.wlgitbridge.writelatex.api.request.getsavedvers.SnapshotGetSavedVersRequest;
import uk.ac.ic.wlgitbridge.writelatex.api.request.getsavedvers.SnapshotInfo;
import uk.ac.ic.wlgitbridge.writelatex.model.Snapshot;
import uk.ac.ic.wlgitbridge.writelatex.model.db.PersistentStoreAPI;
import uk.ac.ic.wlgitbridge.writelatex.model.db.PersistentStoreSource;

import java.util.*;

/**
 * Created by Winston on 07/11/14.
 */
public class SnapshotFetcher implements PersistentStoreSource {

    private PersistentStoreAPI persistentStore;

    private final String projectName;
    private final Map<Integer, Snapshot> snapshots;
    private final SortedSet<Integer> versions;

    public SnapshotFetcher(String projectName, Map<Integer, Snapshot> snapshots) {
        this.projectName = projectName;
        this.snapshots = snapshots;
        versions = new TreeSet<Integer>();
    }

    public SortedSet<Snapshot> fetchNewSnapshots() throws FailedConnectionException, InvalidProjectException {
        SortedSet<Snapshot> newSnapshots = new TreeSet<Snapshot>();
        while (getNew(newSnapshots));
        for (Snapshot snapshot : newSnapshots) {
            persistentStore.addSnapshot(projectName, snapshot.getVersionID());
        }
        System.out.println("Fetched snapshots: " + newSnapshots);
        return newSnapshots;
    }

    public Snapshot getLatestSnapshot() {
        if (versions.isEmpty()) {
            return null;
        }
        return snapshots.get(versions.last());
    }

    public void putLatestVersion(int versionID) {
        versions.add(versionID);
    }

    @Override
    public void initFromPersistentStore(PersistentStoreAPI persistentStore) {
        this.persistentStore = persistentStore;
        for (Integer savedVersionID : persistentStore.getVersionIDsForProjectName(projectName)) {
            snapshots.put(savedVersionID, new Snapshot(savedVersionID));
            versions.add(savedVersionID);
        }
    }

    private boolean getNew(SortedSet<Snapshot> newSnapshots) throws FailedConnectionException, InvalidProjectException {
        SnapshotGetDocRequest getDoc = new SnapshotGetDocRequest(projectName);
        SnapshotGetSavedVersRequest getSavedVers = new SnapshotGetSavedVersRequest(projectName);

        getDoc.request();
        getSavedVers.request();

        Set<Integer> fetchedIDs = new HashSet<Integer>();
        Map<Integer, SnapshotInfo> fetchedSnapshotInfos = new HashMap<Integer, SnapshotInfo>();

        int latestVersionID = putLatestDoc(getDoc, fetchedIDs, fetchedSnapshotInfos);

        putSavedVers(getSavedVers, fetchedIDs, fetchedSnapshotInfos);

        List<Integer> idsToUpdate = getIDsToUpdate(fetchedIDs);

        versions.addAll(fetchedIDs);
        versions.add(latestVersionID);

        return updateIDs(idsToUpdate, fetchedSnapshotInfos, newSnapshots);
    }

    private void putFetchedResult(SnapshotInfo snapshotInfo, Set<Integer> ids, Map<Integer, SnapshotInfo> snapshotInfos) {
        int versionID = snapshotInfo.getVersionId();
        snapshotInfos.put(versionID, snapshotInfo);
        ids.add(versionID);
    }

    private int putLatestDoc(SnapshotGetDocRequest getDoc, Set<Integer> fetchedIDs, Map<Integer, SnapshotInfo> fetchedSnapshotInfos) throws FailedConnectionException, InvalidProjectException {
        SnapshotGetDocResult result = getDoc.getResult();
        int latestVersionID = result.getVersionID();
        putFetchedResult(new SnapshotInfo(latestVersionID, result.getCreatedAt(), result.getName(), result.getEmail()), fetchedIDs, fetchedSnapshotInfos);
        return latestVersionID;
    }

    private void putSavedVers(SnapshotGetSavedVersRequest getSavedVers, Set<Integer> fetchedIDs, Map<Integer, SnapshotInfo> fetchedSnapshotInfos) throws FailedConnectionException {
        for (SnapshotInfo snapshotInfo : getSavedVers.getResult().getSavedVers()) {
            putFetchedResult(snapshotInfo, fetchedIDs, fetchedSnapshotInfos);
        }
    }

    private List<Integer> getIDsToUpdate(Set<Integer> fetchedIDs) {
        List<Integer> idsToUpdate = new LinkedList<Integer>();
        for (Integer id : fetchedIDs) {
            if (!versions.contains(id)) {
                idsToUpdate.add(id);
            }
        }
        return idsToUpdate;
    }

    private boolean updateIDs(List<Integer> idsToUpdate, Map<Integer, SnapshotInfo> fetchedSnapshotInfos, SortedSet<Snapshot> newSnapshots) throws FailedConnectionException {
        if (idsToUpdate.isEmpty()) {
            return false;
        }
        fetchVersions(idsToUpdate, fetchedSnapshotInfos, newSnapshots);
        return true;
    }

    private void fetchVersions(List<Integer> idsToUpdate, Map<Integer, SnapshotInfo> fetchedSnapshotInfos, SortedSet<Snapshot> newSnapshots) throws FailedConnectionException {
        List<SnapshotGetForVersionRequest> requests = createFiredRequests(idsToUpdate);
        processResults(fetchedSnapshotInfos, newSnapshots, requests);
    }

    private List<SnapshotGetForVersionRequest> createFiredRequests(List<Integer> idsToUpdate) {
        List<SnapshotGetForVersionRequest> requests = new LinkedList<SnapshotGetForVersionRequest>();
        for (int id : idsToUpdate) {
            SnapshotGetForVersionRequest request = new SnapshotGetForVersionRequest(projectName, id);
            requests.add(request);
            request.request();
        }
        return requests;
    }

    private void processResults(Map<Integer, SnapshotInfo> fetchedSnapshotInfos, SortedSet<Snapshot> newSnapshots, List<SnapshotGetForVersionRequest> requests) throws FailedConnectionException {
        for (SnapshotGetForVersionRequest request : requests) {
            processResult(fetchedSnapshotInfos, newSnapshots, request);
        }
    }

    private void processResult(Map<Integer, SnapshotInfo> fetchedSnapshotInfos, SortedSet<Snapshot> newSnapshots, SnapshotGetForVersionRequest request) throws FailedConnectionException {
        SnapshotGetForVersionResult result = request.getResult();
        SnapshotData data = result.getSnapshotData();
        Snapshot snapshot = new Snapshot(fetchedSnapshotInfos.get(request.getVersionID()), data);
        snapshots.put(request.getVersionID(), snapshot);
        newSnapshots.add(snapshot);
    }

    public SortedSet<Integer> getVersions() {
        return versions;
    }
}