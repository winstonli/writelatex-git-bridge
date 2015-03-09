package uk.ac.ic.wlgitbridge.snapshot.getdoc;

import com.google.gson.JsonElement;
import uk.ac.ic.wlgitbridge.snapshot.base.HTTPMethod;
import uk.ac.ic.wlgitbridge.snapshot.base.SnapshotAPIRequest;
import uk.ac.ic.wlgitbridge.snapshot.exception.FailedConnectionException;

/**
 * Created by Winston on 06/11/14.
 */
public class GetDocRequest extends SnapshotAPIRequest<GetDocResult> {

    public static final String API_CALL = "";

    public GetDocRequest(String projectName) {
        super(projectName, API_CALL);
    }

    @Override
    protected HTTPMethod httpMethod() {
        return HTTPMethod.GET;
    }

    @Override
    protected GetDocResult parseResponse(JsonElement json) throws FailedConnectionException {
        return new GetDocResult(this, json);
    }

}