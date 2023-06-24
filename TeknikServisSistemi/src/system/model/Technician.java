package system.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Technician extends User {
    public Technician(int id, String name, String phone) {
        super(id, name, phone);
    }

    public Technician(String name, String phone) {
        super(name, phone);
    }

    public static Technician fromResultSet(ResultSet rs) throws SQLException {
        return new Technician(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3)
        );
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + getId() + '\'' +
                "name='" + getName() + '\'' +
                "phone='" + getPhone() + '\'' +
                "}";
    }
}
