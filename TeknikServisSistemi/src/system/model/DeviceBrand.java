package system.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DeviceBrand {

    private final int id;
    private final String name;

    public DeviceBrand(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static DeviceBrand fromResultSet(ResultSet rs) throws SQLException {
        return new DeviceBrand(rs.getInt(1), rs.getString(2));
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
