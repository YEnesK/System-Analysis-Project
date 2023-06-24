package system.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Complaint {

    private int id;
    private final Customer customer;
    private final Technician technician;
    private final String complaint;
    private final DeviceType deviceType;
    private final DeviceBrand brand;
    private final String serialNumber;
    private ComplaintStatus status;
    private int laborFee;
    private int totalFee;
    private String description;
    private List<UsedComponent> usedComponents;

    public Complaint(int id, Customer customer, Technician technician, String complaint, DeviceType deviceType, DeviceBrand brand, String serialNumber, ComplaintStatus status, int laborFee, int totalFee, String description, List<UsedComponent> usedComponents) {
        this.id = id;
        this.customer = customer;
        this.technician = technician;
        this.complaint = complaint;
        this.deviceType = deviceType;
        this.brand = brand;
        this.serialNumber = serialNumber;
        this.status = status;
        this.laborFee = laborFee;
        this.totalFee = totalFee;
        this.description = description;
        this.usedComponents = usedComponents;
    }

    public Complaint(Customer customer, Technician technician, String complaint, DeviceType deviceType, DeviceBrand brand, String serialNumber, ComplaintStatus status, int laborFee, int totalFee, String description, List<UsedComponent> usedComponents) {
        this.customer = customer;
        this.technician = technician;
        this.complaint = complaint;
        this.deviceType = deviceType;
        this.brand = brand;
        this.serialNumber = serialNumber;
        this.status = status;
        this.laborFee = laborFee;
        this.totalFee = totalFee;
        this.description = description;
        this.usedComponents = usedComponents;
    }

    public static Complaint fromResultSet(ResultSet rs) throws SQLException {
        return new Complaint(
                rs.getInt(1),
                new Customer(
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5)
                ),
                new Technician(
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getString(8)
                ),
                rs.getString(13),
                new DeviceType(
                        rs.getInt(11),
                        rs.getString(12)
                ),
                new DeviceBrand(
                        rs.getInt(9),
                        rs.getString(10)
                ),
                rs.getString(14),
                ComplaintStatus.values()[rs.getInt(15)],
                rs.getInt(16),
                rs.getInt(17),
                rs.getString(18),
                rs.getString(20) == null ?
                        Collections.emptyList() :
                        Collections.singletonList(new UsedComponent(new Component(
                                rs.getInt(19),
                                rs.getString(20),
                                rs.getString(21),
                                rs.getInt(23),
                                rs.getInt(22)
                        ), rs.getInt(24)))
        );
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Technician getTechnician() {
        return technician;
    }

    public String getComplaint() {
        return complaint;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public DeviceBrand getBrand() {
        return brand;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public int getLaborFee() {
        return laborFee;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public String getDescription() {
        return description;
    }

    public List<UsedComponent> getUsedComponents() {
        return usedComponents;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(ComplaintStatus status) {
        this.status = status;
    }

    public void setUsedComponents(List<UsedComponent> usedComponents) {
        this.usedComponents = usedComponents;
    }

    public void setLaborFee(int laborFee) {
        this.laborFee = laborFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Complaint{" +
                "id=" + id +
                ", customer=" + customer +
                ", technician=" + technician +
                ", complaint='" + complaint + '\'' +
                ", deviceType=" + deviceType +
                ", brand=" + brand +
                ", serialNumber='" + serialNumber + '\'' +
                ", status=" + status +
                ", laborFee=" + laborFee +
                ", totalFee=" + totalFee +
                ", description='" + description + '\'' +
                ", usedComponents=" + usedComponents +
                '}';
    }
}
