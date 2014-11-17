package uk.ac.ic.wlgitbridge.application;

import com.google.gson.JsonObject;
import uk.ac.ic.wlgitbridge.writelatex.InvalidFilesException;
import uk.ac.ic.wlgitbridge.writelatex.OutOfDateException;
import uk.ac.ic.wlgitbridge.writelatex.SnapshotPostException;
import uk.ac.ic.wlgitbridge.writelatex.UnexpectedErrorException;
import uk.ac.ic.wlgitbridge.writelatex.api.request.getdoc.exception.InvalidProjectException;
import uk.ac.ic.wlgitbridge.writelatex.api.request.push.UnexpectedPostbackException;

/**
 * Created by Winston on 17/11/14.
 */
public class SnapshotPostExceptionBuilder {

    private static final String CODE_ERROR_OUT_OF_DATE = "outOfDate";
    private static final String CODE_ERROR_INVALID_FILES = "invalidFiles";
    private static final String CODE_ERROR_INVALID_PROJECT = "invalidProject";
    private static final String CODE_ERROR_UNKNOWN = "error";

    public SnapshotPostException build(String errorCode, JsonObject json) throws UnexpectedPostbackException {
        if (errorCode.equals(CODE_ERROR_OUT_OF_DATE)) {
            return new OutOfDateException(json);
        } else if (errorCode.equals(CODE_ERROR_INVALID_FILES)) {
            return new InvalidFilesException(json);
        } else if (errorCode.equals(CODE_ERROR_INVALID_PROJECT)) {
            return new InvalidProjectException(json);
        } else if (errorCode.equals(CODE_ERROR_UNKNOWN)) {
            return new UnexpectedErrorException(json);
        } else {
            throw new UnexpectedPostbackException();
        }
    }

}
