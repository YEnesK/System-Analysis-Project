package system.ui.dialog;

import system.model.Component;
import system.model.*;
import system.model.document.SingleDocumentListener;
import system.model.table.UsedComponentTableModel;
import system.ui.App;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static system.util.TableRendererUtils.setRenderer;

public class FinishOperationForComplaintDialog extends JDialog {
    private static final String TITLE = "İşlem Ekle";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldId;
    private JTable tableComponents;
    private JTextField fieldComponentFee;
    private JTextField fieldCustomerName;
    private JTextField fieldComplaint;
    private JTextField fieldDeviceSerialNumber;
    private JTextArea textAreaDescription;
    private JTextField fieldLaborFee;
    private JTextField fieldTotalFee;

    private final FinishOperationListener listener;
    private int totalFee;
    private int componentFee;

    public FinishOperationForComplaintDialog(Complaint complaint, FinishOperationListener listener) {
        this.listener = listener;

        setTitle(TITLE);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setPreferredSize(new Dimension(700, 500));
        pack();
        setLocationRelativeTo(null);

        componentFee = calculateComponentFee(complaint);

        fieldId.setText(String.valueOf(complaint.getId()));
        fieldComplaint.setText(complaint.getComplaint());
        fieldComponentFee.setText(String.valueOf(componentFee));
        fieldCustomerName.setText(complaint.getCustomer().getName());
        fieldDeviceSerialNumber.setText(complaint.getSerialNumber());

        setRenderer(tableComponents);

        setupComponentsTable(complaint);

        fieldLaborFee.getDocument().addDocumentListener((SingleDocumentListener) e -> {
            try {
                String laborFee = fieldLaborFee.getText().trim();
                setTotalFee(componentFee + Integer.parseInt(laborFee));
            } catch (Exception ignored) {
            }
        });

        fieldLaborFee.setText("0");

        buttonOK.addActionListener(e -> {
            String laborFee = fieldLaborFee.getText().trim();
            String regexFee = "\\d{1,9}";
            if (!Pattern.matches(regexFee, laborFee)) {
                JOptionPane.showMessageDialog(null, "Lütfen geçerli bir işçilik ücreti giriniz!");
                return;
            }
            if(Integer.parseInt(laborFee) == 0) {
                JOptionPane.showMessageDialog(null, "İşçilik ücreti 0 olamaz!");
                return;
            }
            String description = textAreaDescription.getText().trim();
            if (description.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Yapılan işlemler kısmı boş bırakılamaz!");
                return;
            }
            onOK(complaint, Integer.parseInt(laborFee), totalFee, description);
        });

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private int calculateComponentFee(Complaint complaint) {
        int sum = 0;
        for (UsedComponent usedComponent : complaint.getUsedComponents()) {
            sum += usedComponent.getComponent().getPrice() * usedComponent.getPiece();
        }
        return sum;
    }

    private void setupComponentsTable(Complaint complaint) {
        UsedComponentTableModel tableModel = new UsedComponentTableModel(complaint.getUsedComponents());
        tableComponents.setModel(tableModel);
        tableComponents.setCellSelectionEnabled(false);
    }

    private void onOK(Complaint complaint, int laborFee, int totalFee, String description) {
        complaint.setLaborFee(laborFee);
        complaint.setTotalFee(totalFee);
        complaint.setDescription(description);
        complaint.setStatus(ComplaintStatus.WAITING_PAYMENT);
        App.getDatabaseHelper().updateComplaint(complaint);
        dispose();
        listener.onFinishOperation(complaint);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
        fieldTotalFee.setText(String.valueOf(totalFee));
    }

    public interface FinishOperationListener {
        void onFinishOperation(Complaint complaint);
    }

}
