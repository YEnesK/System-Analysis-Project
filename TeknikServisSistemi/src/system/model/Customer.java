package system.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer extends User {

    private final String address;

    public Customer(int id, String name, String phone, String address) {
        super(id, name, phone);
        this.address = address;
    }

    public Customer(String name, String phone, String address) {
        super(name, phone);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public static Customer fromResultSet(ResultSet rs) throws SQLException {
        return new Customer(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4)
        );
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + getId() + '\'' +
                "name='" + getName() + '\'' +
                "phone='" + getPhone() + '\'' +
                "address='" + address + '\'' +
                '}';
    }
}
