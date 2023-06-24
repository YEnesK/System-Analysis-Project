package system.ui.dialog;

import system.model.Complaint;
import system.model.ComplaintStatus;
import system.ui.App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RequestRefundForComplaintDialog extends JDialog {
    private static final String TITLE = "İade İstemi Oluştur";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldId;
    private JTextField fieldCustomerName;
    private JTextField fieldComplaint;
    private JTextField fieldDeviceSerialNumber;
    private JTextArea textAreaDescription;

    private final RequestRefundForComplaintListener listener;

    public RequestRefundForComplaintDialog(Complaint complaint, RequestRefundForComplaintListener listener) {
        this.listener = listener;

        setTitle(TITLE);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setPreferredSize(new Dimension(400, 250));
        pack();
        setLocationRelativeTo(null);

        fieldId.setText(String.valueOf(complaint.getId()));
        fieldComplaint.setText(complaint.getComplaint());
        fieldCustomerName.setText(complaint.getCustomer().getName());
        fieldDeviceSerialNumber.setText(complaint.getSerialNumber());

        buttonOK.addActionListener(e -> {
            String description = textAreaDescription.getText().trim();
            if (description.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Açıklama kısmı boş bırakılamaz!");
                return;
            }
            onOK(complaint, description);
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

    private void onOK(Complaint complaint, String description) {
        complaint.setDescription(description);
        complaint.setStatus(ComplaintStatus.WAITING_REFUND);
        App.getDatabaseHelper().updateComplaint(complaint);
        dispose();
        listener.onRequestRefund(complaint);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public interface RequestRefundForComplaintListener {
        void onRequestRefund(Complaint complaint);
    }

}
