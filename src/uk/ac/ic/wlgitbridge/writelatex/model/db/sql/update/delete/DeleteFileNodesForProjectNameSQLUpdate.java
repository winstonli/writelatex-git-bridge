package uk.ac.ic.wlgitbridge.writelatex.model.db.sql.update.delete;

import uk.ac.ic.wlgitbridge.writelatex.model.db.sql.SQLUpdate;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Winston on 20/11/14.
 */
public class DeleteFileNodesForProjectNameSQLUpdate implements SQLUpdate {

    private static final String DELETE_FILE_NODES_FOR_PROJECT_NAME =
            "DELETE FROM `file_node_table` WHERE `project_name` = ?";

    private final String projectName;

    public DeleteFileNodesForProjectNameSQLUpdate(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String getSQL() {
        return DELETE_FILE_NODES_FOR_PROJECT_NAME;
    }

    @Override
    public void addParametersToStatement(PreparedStatement statement) throws SQLException {
        statement.setString(1, projectName);
    }

}
