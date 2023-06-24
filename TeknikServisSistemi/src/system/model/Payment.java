package system.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Payment {

    private final String date;
    private final Complaint complaint;

    public Payment(String date, Complaint complaint) {
        this.date = date;
        this.complaint = complaint;
    }

    public static Payment fromResultSet(ResultSet rs) throws SQLException {
        return new Payment(
                rs.getString(1),
                new Complaint(
                        rs.getInt(2),
                        new Customer(
                                rs.getInt(3),
                                rs.getString(4),
                                rs.getString(5),
                                rs.getString(6)
                        ),
                        new Technician(
                                rs.getInt(7),
                                rs.getString(8),
                                rs.getString(9)
                        ),
                        rs.getString(14),
                        new DeviceType(
                                rs.getInt(12),
                                rs.getString(13)
                        ),
                        new DeviceBrand(
                                rs.getInt(10),
                                rs.getString(11)
                        ),
                        rs.getString(15),
                        ComplaintStatus.values()[rs.getInt(16)],
                        rs.getInt(17),
                        rs.getInt(18),
                        rs.getString(19),
                        new ArrayList<>()
                )
        );
    }

    public String getDate() {
        return date;
    }

    public Complaint getComplaint() {
        return complaint;
    }
}
