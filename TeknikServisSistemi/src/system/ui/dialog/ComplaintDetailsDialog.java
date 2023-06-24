package system.ui.dialog;

import system.model.Complaint;
import system.model.ComplaintStatus;
import system.model.UsedComponent;
import system.model.document.SingleDocumentListener;
import system.model.table.UsedComponentTableModel;
import system.ui.App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Pattern;

import static system.util.TableRendererUtils.setRenderer;

public class ComplaintDetailsDialog extends JDialog {
    private static final String TITLE = "Şikayet Detayları";

    private JPanel contentPane;
    private JTextField fieldId;
    private JTable tableComponents;
    private JTextField fieldComponentFee;
    private JTextField fieldCustomerName;
    private JTextField fieldComplaint;
    private JTextField fieldDeviceSerialNumber;
    private JTextArea textAreaDescription;
    private JTextField fieldLaborFee;
    private JTextField fieldTotalFee;

    public ComplaintDetailsDialog(Complaint complaint) {
        setTitle(TITLE);

        setContentPane(contentPane);
        setModal(true);
        setPreferredSize(new Dimension(700, 500));
        pack();
        setLocationRelativeTo(null);

        int componentFee = calculateComponentFee(complaint);

        fieldId.setText(String.valueOf(complaint.getId()));
        fieldComplaint.setText(complaint.getComplaint());
        fieldComponentFee.setText(String.valueOf(componentFee));
        fieldCustomerName.setText(complaint.getCustomer().getName());
        fieldDeviceSerialNumber.setText(complaint.getSerialNumber());
        fieldLaborFee.setText(String.valueOf(complaint.getLaborFee()));
        fieldTotalFee.setText(String.valueOf(complaint.getTotalFee()));
        textAreaDescription.setText(complaint.getDescription());

        setRenderer(tableComponents);

        setupComponentsTable(complaint);

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

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
