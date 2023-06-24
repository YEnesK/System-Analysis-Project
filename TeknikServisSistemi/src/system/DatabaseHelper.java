package system;

import system.model.*;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseHelper {

    private Connection conn = null;

    public DatabaseHelper connect() {
        try {
            String url = "jdbc:sqlite:src/assets/service.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return this;
    }

    public void disconnect() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public List<Customer> getCustomers() {
        List<Customer> users = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, name, number, address FROM customer");
            while (rs.next()) {
                users.add(Customer.fromResultSet(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public int insertCustomer(Customer customer) {
        int generatedId = -1;
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO customer(name, number, address) VALUES(?, ?, ?)");
            st.setString(1, customer.getName());
            st.setString(2, customer.getPhone());
            st.setString(3, customer.getAddress());
            int affectedRows = st.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
            }

            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    public void deleteCustomerById(int id) {
        try {
            PreparedStatement st = conn.prepareStatement("DELETE FROM customer WHERE id = ?");
            st.setInt(1, id);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Technician> getTechnicians() {
        List<Technician> users = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, name, number FROM technician");
            while (rs.next()) {
                users.add(Technician.fromResultSet(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public int insertTechnician(Technician technician) {
        int generatedId = -1;
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO technician(name, number) VALUES(?, ?)");
            st.setString(1, technician.getName());
            st.setString(2, technician.getPhone());
            int affectedRows = st.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
            }

            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    public void deleteTechnicianById(int id) {
        try {
            PreparedStatement st = conn.prepareStatement("DELETE FROM technician WHERE id = ?");
            st.setInt(1, id);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Complaint> getComplaints() {
        List<Complaint> complaints = new ArrayList<>();
        List<Complaint> complaintsResult = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT cr.id," +
                            "c.id, c.name, c.number, c.address," +
                            "t.id, t.name, t.number," +
                            "b.id, b.name," +
                            "dt.id, dt.name," +
                            "complaint," +
                            "serial_no," +
                            "situation_no," +
                            "labor_cost," +
                            "total_cost," +
                            "operation_made," +
                            "cm.id, cm.code, cm.name, cm.price, cm.stock," +
                            "uc.piece" +
                            " FROM complaint_record cr" +
                            " JOIN customer c on c.ID = cr.customer_id" +
                            " JOIN technician t on t.ID = cr.technician_id" +
                            " JOIN brand b on cr.brand_id = b.ID" +
                            " JOIN device_type dt on dt.ID = cr.device_type_id" +
                            " LEFT JOIN used_component uc ON uc.complaint_id = cr.id" +
                            " LEFT JOIN component cm on uc.component_id = cm.ID"
            );
            while (rs.next()) {
                complaints.add(Complaint.fromResultSet(rs));
            }

            Map<Object, List<Complaint>> grouped = complaints.stream().collect(Collectors.groupingBy(u -> u.getId()));
            for (Object key : grouped.keySet()) {
                List<Complaint> list = grouped.get(key);
                List<UsedComponent> usedComponents = list.stream().map(Complaint::getUsedComponents).flatMap(Collection::stream).collect(Collectors.toList());
                Complaint c = list.get(0);
                c.setUsedComponents(usedComponents);
                complaintsResult.add(c);
            }

            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return complaintsResult;
    }

    public int insertComplaint(Complaint complaint) {
        int generatedId = -1;
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO complaint_record(" +
                    "customer_id," +
                    "technician_id," +
                    "device_type_id," +
                    "brand_id," +
                    "complaint," +
                    "serial_no," +
                    "situation_no," +
                    "labor_cost," +
                    "total_cost," +
                    "operation_made)" +
                    " VALUES(?,?,?,?,?,?,?,?,?,?)");
            st.setInt(1, complaint.getCustomer().getId());
            st.setInt(2, complaint.getTechnician().getId());
            st.setInt(3, complaint.getDeviceType().getId());
            st.setInt(4, complaint.getBrand().getId());
            st.setString(5, complaint.getComplaint());
            st.setString(6, complaint.getSerialNumber());
            st.setInt(7, complaint.getStatus().ordinal());
            st.setInt(8, complaint.getLaborFee());
            st.setInt(9, complaint.getTotalFee());
            st.setString(10, complaint.getDescription());
            int affectedRows = st.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
            }

            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    public void updateComplaint(Complaint complaint) {
        try {
            PreparedStatement st = conn.prepareStatement("UPDATE complaint_record SET " +
                    "situation_no = ?," +
                    "labor_cost = ?," +
                    "total_cost = ?," +
                    "operation_made = ?" +
                    " WHERE id = ?");
            st.setInt(1, complaint.getStatus().ordinal());
            st.setInt(2, complaint.getLaborFee());
            st.setInt(3, complaint.getTotalFee());
            st.setString(4, complaint.getDescription());
            st.setInt(5, complaint.getId());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<DeviceType> getDeviceTypes() {
        List<DeviceType> deviceTypes = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, name FROM device_type");
            while (rs.next()) {
                deviceTypes.add(DeviceType.fromResultSet(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deviceTypes;
    }

    public List<DeviceBrand> getDeviceBrands() {
        List<DeviceBrand> deviceBrands = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, name FROM brand");
            while (rs.next()) {
                deviceBrands.add(DeviceBrand.fromResultSet(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deviceBrands;
    }

    public List<Component> getComponents() {
        List<Component> components = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT ID, code, name, price, stock FROM component");
            while (rs.next()) {
                components.add(Component.fromResultSet(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return components;
    }

    public String insertComponent(Component c) {
        int generatedId = -1;
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO component(code, name, price, stock) VALUES(?, ?, ?, ?)");
            st.setString(1, c.getCode());
            st.setString(2, c.getName());
            st.setInt(3, c.getPrice());
            st.setInt(4, c.getStock());
            int affectedRows = st.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
            }
            st.close();

            if (c.getCode() == null) {
                // update
                String generatedCode = "PRC-" + generatedId;
                PreparedStatement st2 = conn.prepareStatement("UPDATE component SET code = ? WHERE id = ?");
                st2.setString(1, generatedCode);
                st2.setInt(2, generatedId);
                st2.executeUpdate();
                st2.close();
                return generatedCode;
            }
            return c.getCode();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void deleteComponentById(int id) {
        try {
            PreparedStatement st = conn.prepareStatement("DELETE FROM component WHERE id = ?");
            st.setInt(1, id);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void increaseComponentStockById(int id, int input) {
        try {
            PreparedStatement st = conn.prepareStatement("UPDATE component SET " +
                    "stock = stock + ?" +
                    " WHERE id = ?");
            st.setInt(1, input);
            st.setInt(2, id);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void decreaseComponentStockById(int id, int output) {
        try {
            PreparedStatement st = conn.prepareStatement("UPDATE component SET " +
                    "stock = stock - ?" +
                    " WHERE id = ?");
            st.setInt(1, output);
            st.setInt(2, id);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<StockHistoryItem> getStockHistory() {
        List<StockHistoryItem> stockHistory = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT ca.date, c.ID, c.code, c.name, c.price, c.stock, ca.operation_made, ca.previous_stock, ca.future_stock FROM component_actions ca" +
                    " JOIN component c on c.ID = ca.component_id");
            while (rs.next()) {
                stockHistory.add(StockHistoryItem.fromResultSet(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stockHistory;
    }

    public List<Payment> getPaymentHistory() {
        List<Payment> payments = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT p.date," +
                            "cr.id," +
                            "c.id, c.name, c.number, c.address," +
                            "t.id, t.name, t.number," +
                            "b.id, b.name," +
                            "dt.id, dt.name," +
                            "complaint," +
                            "serial_no," +
                            "situation_no," +
                            "labor_cost," +
                            "total_cost," +
                            "operation_made" +
                            " FROM payment p" +
                            " JOIN complaint_record cr on cr.ID = p.complaint_id" +
                            " JOIN customer c on c.ID = cr.customer_id" +
                            " JOIN technician t on t.ID = cr.technician_id" +
                            " JOIN brand b on cr.brand_id = b.ID" +
                            " JOIN device_type dt on dt.ID = cr.device_type_id"
            );
            while (rs.next()) {
                payments.add(Payment.fromResultSet(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    public void insertUsedComponentsToComplaint(int complaintId, List<UsedComponent> usedComponents) {
        try (PreparedStatement st = conn.prepareStatement("INSERT INTO used_component(complaint_id, component_id, piece) VALUES(?, ?, ?)")) {
            for (UsedComponent usedComponent : usedComponents) {
                st.setInt(1, complaintId);
                st.setInt(2, usedComponent.getComponent().getId());
                st.setInt(3, usedComponent.getPiece());
                st.addBatch();
            }
            st.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertPayment(Complaint complaint) {
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO payment(date, complaint_id) VALUES(date('now'), ?)");
            st.setInt(1, complaint.getId());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertStockHistoryItem(StockHistoryItem stockHistoryItem) {
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO component_actions(date, component_id, operation_made, previous_stock, future_stock) VALUES(date('now'), ?, ?, ?, ?)");
            st.setInt(1, stockHistoryItem.getComponent().getId());
            st.setString(2, stockHistoryItem.getOperation());
            st.setInt(3, stockHistoryItem.getPreviousStock());
            st.setInt(4, stockHistoryItem.getCurrentStock());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
