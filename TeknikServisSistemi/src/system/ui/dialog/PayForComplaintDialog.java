package system.ui.dialog;

import system.model.Complaint;
import system.model.ComplaintStatus;
import system.ui.App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PayForComplaintDialog extends JDialog {
    private static final String TITLE = "Ödeme Yap";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldId;
    private JTextField fieldCustomerName;
    private JTextField fieldComplaint;
    private JTextField fieldDeviceSerialNumber;
    private JTextField fieldTotalFee;

    private final PayForComplaintListener listener;

    public PayForComplaintDialog(Complaint complaint, PayForComplaintListener listener) {
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
        fieldTotalFee.setText(String.valueOf(complaint.getTotalFee()));
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
        listener.onPay(complaint);
    }

    private void pay(Complaint complaint) {
        App.getDatabaseHelper().updateComplaint(complaint);
        App.getDatabaseHelper().insertPayment(complaint);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public interface PayForComplaintListener {
        void onPay(Complaint complaint);
    }

}
