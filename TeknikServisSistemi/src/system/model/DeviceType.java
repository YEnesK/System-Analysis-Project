package system.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DeviceType {

    private final int id;
    private final String name;

    public DeviceType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static DeviceType fromResultSet(ResultSet rs) throws SQLException {
        return new DeviceType(rs.getInt(1), rs.getString(2));
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
