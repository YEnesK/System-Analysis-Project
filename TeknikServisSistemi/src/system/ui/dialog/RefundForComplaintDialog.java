package system.ui.dialog;

import system.model.Complaint;
import system.model.ComplaintStatus;
import system.ui.App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RefundForComplaintDialog extends JDialog {
    private static final String TITLE = "İade İşlemi Yap";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldId;
    private JTextField fieldCustomerName;
    private JTextField fieldComplaint;
    private JTextField fieldDeviceSerialNumber;
    private JTextArea textAreaDescription;

    private final RefundForComplaintListener listener;

    public RefundForComplaintDialog(Complaint complaint, RefundForComplaintListener listener) {
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
        textAreaDescription.setText(String.valueOf(complaint.getDescription()));
        fieldDeviceSerialNumber.setText(complaint.getSerialNumber());

        buttonOK.addActionListener(e -> onOK(complaint));

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

    private void onOK(Complaint complaint) {
        complaint.setStatus(ComplaintStatus.COMPLETED);
        pay(complaint);
        dispose();
        listener.onRefund(complaint);
    }

    private void pay(Complaint complaint) {
        App.getDatabaseHelper().updateComplaint(complaint);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public interface RefundForComplaintListener {
        void onRefund(Complaint complaint);
    }

}
