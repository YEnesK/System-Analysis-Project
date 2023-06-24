package system.ui;

import system.model.*;
import system.ui.dialog.SelectCustomerDialog;
import system.ui.dialog.SelectTechnicianDialog;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReportComplaintScreen extends JFrame implements SelectCustomerDialog.SelectCustomerListener, SelectTechnicianDialog.SelectTechnicianListener {
    private static final String TITLE = "Yeni Şikayet";

    private JPanel panel;
    private JButton buttonSelectTechnician;
    private JButton buttonOK;
    private JComboBox comboDeviceType;
    private JTextArea textAreaComplaint;
    private JButton buttonSelectCustomer;
    private JTextField fieldCustomerName;
    private JTextField fieldCustomerPhone;
    private JTextField fieldCustomerAddress;
    private JTextField fieldTechnicianName;
    private JTextField fieldTechnicianPhone;
    private JComboBox comboDeviceBrand;
    private JTextField fieldDeviceSerialNumber;

    private Customer selectedCustomer;
    private Technician selectedTechnician;

    public ReportComplaintScreen() {
        super(TITLE);

        setContentPane(panel);
        setPreferredSize(new Dimension(900, 550));
        pack();
        setLocationRelativeTo(null);

        comboDeviceType.setModel(new DefaultComboBoxModel<>(fetchDeviceTypes().toArray(new DeviceType[0])));
        comboDeviceType.setSelectedItem(null);
        comboDeviceBrand.setModel(new DefaultComboBoxModel<>(fetchDeviceBrands().toArray(new DeviceBrand[0])));
        comboDeviceBrand.setSelectedItem(null);

        buttonSelectCustomer.addActionListener(e -> {
            new SelectCustomerDialog(this).setVisible(true);
        });

        buttonSelectTechnician.addActionListener(e -> {
            new SelectTechnicianDialog(this).setVisible(true);
        });

        buttonOK.addActionListener(e -> {
            if (selectedCustomer == null) {
                JOptionPane.showMessageDialog(null, "Müşteri seçmeniz gerekmekte!");
                return;
            }
            if (selectedTechnician == null) {
                JOptionPane.showMessageDialog(null, "Teknisyen seçmeniz gerekmekte!");
                return;
            }
            DeviceType deviceType = getSelectedDeviceType();
            if (deviceType == null) {
                JOptionPane.showMessageDialog(null, "Cihaz türünü seçmeniz gerekmekte!");
                return;
            }
            DeviceBrand brand = getSelectedDeviceBrand();
            if (brand == null) {
                JOptionPane.showMessageDialog(null, "Cihaz markasını seçmeniz gerekmekte!");
                return;
            }
            String serialNumber = fieldDeviceSerialNumber.getText().trim();
            if (serialNumber.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Cihaz seri numarası boş bırakılamaz!");
                return;
            }
            String complaint = textAreaComplaint.getText().trim();
            if (complaint.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Şikayet metni boş bırakılamaz!");
                return;
            }
            onOK(selectedCustomer, selectedTechnician, deviceType, brand, serialNumber, complaint);
        });
    }

    private List<DeviceType> fetchDeviceTypes() {
        return App.getDatabaseHelper().getDeviceTypes();
    }

    private List<DeviceBrand> fetchDeviceBrands() {
        return App.getDatabaseHelper().getDeviceBrands();
    }

    private DeviceBrand getSelectedDeviceBrand() {
        return (DeviceBrand) comboDeviceBrand.getSelectedItem();
    }

    private DeviceType getSelectedDeviceType() {
        return (DeviceType) comboDeviceType.getSelectedItem();
    }

    private void onOK(Customer customer, Technician technician, DeviceType deviceType, DeviceBrand brand, String serialNumber, String complaint) {
        Complaint c = new Complaint(customer, technician, complaint, deviceType, brand, serialNumber, ComplaintStatus.WAITING_OPERATION, 0, 0, "", null);
        int id = App.getDatabaseHelper().insertComplaint(c);
        dispose();
        JOptionPane.showMessageDialog(null, id + " nolu şikayet alındı!");
    }

    @Override
    public void onSelectCustomer(Customer customer) {
        selectedCustomer = customer;
        fieldCustomerName.setText(customer.getName());
        fieldCustomerPhone.setText(customer.getPhone());
        fieldCustomerAddress.setText(customer.getAddress());
    }

    @Override
    public void onSelectTechnician(Technician technician) {
        selectedTechnician = technician;
        fieldTechnicianName.setText(technician.getName());
        fieldTechnicianPhone.setText(technician.getPhone());
    }
}
