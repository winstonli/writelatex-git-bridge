package uk.ac.ic.wlgitbridge.data.filestore;

import uk.ac.ic.wlgitbridge.git.exception.SizeLimitExceededException;

import java.util.Map;
import java.util.Optional;

/**
 * Created by Winston on 16/11/14.
 */
public class RawDirectory {

    private final Map<String, RawFile> fileTable;

    public RawDirectory(Map<String, RawFile> fileTable) {
        this.fileTable = fileTable;
    }

    public void checkSize(long maxFileSize) throws SizeLimitExceededException {
        for (RawFile file : fileTable.values()) {
            long size = file.size();
            if (size <= maxFileSize) continue;
            throw new SizeLimitExceededException(
                    Optional.of(file.getPath()), size, maxFileSize);
        }
    }

    public Map<String, RawFile> getFileTable() {
        return fileTable;
    }

}
