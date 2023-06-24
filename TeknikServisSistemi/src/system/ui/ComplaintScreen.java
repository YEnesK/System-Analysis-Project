package system.ui;

import system.model.Complaint;
import system.model.table.ComplaintTableModel;
import system.model.document.SingleDocumentListener;
import system.ui.dialog.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static system.util.TableRendererUtils.setRenderer;

public class ComplaintScreen extends JFrame implements PayForComplaintDialog.PayForComplaintListener, RefundForComplaintDialog.RefundForComplaintListener, MakeOperationForComplaintDialog.MakeOperationListener, RequestRefundForComplaintDialog.RequestRefundForComplaintListener, FinishOperationForComplaintDialog.FinishOperationListener {
    private static final String TITLE = "Şikayet Sorgula";

    private JTextField filedCustomerName;
    private JTable tableComplaints;
    private JPanel panel;
    private JButton buttonRefund;
    private JButton buttonPay;
    private JButton buttonFinishOperation;
    private JButton buttonMakeOperation;
    private JButton buttonRequestRefund;
    private JButton buttonDetails;

    private Complaint selectedItem;
    private ComplaintTableModel tableModel;

    public ComplaintScreen() {
        super(TITLE);
        setContentPane(panel);
        setPreferredSize(new Dimension(900, 550));
        pack();
        setLocationRelativeTo(null);

        setRenderer(tableComplaints);

        List<Complaint> complaints = fetchAllComplaints();
        listAll(complaints);

        setSelectedItem(null);

        buttonDetails.addActionListener(e -> {
            new ComplaintDetailsDialog(selectedItem).setVisible(true);
        });
        buttonPay.addActionListener(e -> {
            // müşteriden para alınır
            // cihaz müşteriye teslim edilir
            // şikayet durumu TAMAMLANDI olur
            // payments tablosuna kaydedilir
            new PayForComplaintDialog(selectedItem, this).setVisible(true);
        });
        buttonRefund.addActionListener(e -> {
            // müşteriden para alınmaz
            // cihaz müşteriye iade edilir
            // şikayet durumu TAMAMLANDI olur
            new RefundForComplaintDialog(selectedItem, this).setVisible(true);
        });
        buttonMakeOperation.addActionListener(e -> {
            // teknisyen; gerekli ise
            // cihaz tamiri için gerekli parçaları seçer
            // parçalardan stoğu yetersiz olan varsa şikayet durumu PARÇA BEKLİYOR olur
            // yoksa İŞLEM GÖRÜYOR olur
            new MakeOperationForComplaintDialog(selectedItem, this).setVisible(true);
        });
        buttonRequestRefund.addActionListener(e -> {
            // teknisyen; arızayı çözemediği için
            // ürünün iadesini talep eder
            // şikayet durumu İADE BEKLİYOR olur
            new RequestRefundForComplaintDialog(selectedItem, this).setVisible(true);
        });
        buttonFinishOperation.addActionListener(e -> {
            // şikayet teknisyen tarafından çözülmüştür
            // teknisyen işçilik ücretini ve yapılan işlemeleri girer
            // şikayet durumu ÖDEME BEKLENİYOR olur
            new FinishOperationForComplaintDialog(selectedItem, this).setVisible(true);
        });

        filedCustomerName.getDocument().addDocumentListener((SingleDocumentListener) e -> {
            tableModel.filter(filedCustomerName.getText());
        });
    }

    private List<Complaint> fetchAllComplaints() {
        return App.getDatabaseHelper().getComplaints();
    }

    private void listAll(List<Complaint> complaints) {
        tableModel = new ComplaintTableModel(complaints);
        tableComplaints.setModel(tableModel);
        tableComplaints.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableComplaints.getSelectionModel().addListSelectionListener(e -> {
            int selectedIndex = tableComplaints.getSelectedRow();
            if (selectedIndex < 0) {
                setSelectedItem(null);
                return;
            }
            setSelectedItem(tableModel.getItem(selectedIndex));
        });
    }

    private void setSelectedItem(Complaint complaint) {
        this.selectedItem = complaint;

        buttonMakeOperation.setEnabled(false);
        buttonFinishOperation.setEnabled(false);
        buttonPay.setEnabled(false);
        buttonRefund.setEnabled(false);
        buttonRequestRefund.setEnabled(false);
        buttonDetails.setEnabled(true);

        if (complaint == null) {
            buttonDetails.setEnabled(false);
            return;
        }

        switch (complaint.getStatus()) {
            case WAITING_OPERATION:
                buttonMakeOperation.setEnabled(true);
                buttonRequestRefund.setEnabled(true);
                break;
            case WAITING_COMPONENT:
                // parça gelene dek...
                // sadece şikayet görüntülenir, herhangi bir işlem yapılamaz
                break;
            case IN_OPERATION:
                buttonFinishOperation.setEnabled(true);
                break;
            case WAITING_PAYMENT:
                buttonPay.setEnabled(true);
                break;
            case WAITING_REFUND:
                buttonRefund.setEnabled(true);
                break;
            case COMPLETED:
                // sadece şikayet görüntülenir, herhangi bir işlem yapılamaz
                break;
        }
    }

    @Override
    public void onPay(Complaint complaint) {
        setSelectedItem(selectedItem);
        tableModel.update(complaint);
        JOptionPane.showMessageDialog(null, complaint.getCustomer().getName() + " adlı müşteriden " + complaint.getTotalFee() + " miktarında ödeme alındı!");
    }

    @Override
    public void onRefund(Complaint complaint) {
        setSelectedItem(selectedItem);
        tableModel.update(complaint);
        JOptionPane.showMessageDialog(null, complaint.getCustomer().getName() + " adlı müşteriye cihaz iade edildi!");
    }

    @Override
    public void onMakeOperation(Complaint complaint) {
        setSelectedItem(selectedItem);
        tableModel.update(complaint);
    }

    @Override
    public void onRequestRefund(Complaint complaint) {
        setSelectedItem(selectedItem);
        tableModel.update(complaint);
        JOptionPane.showMessageDialog(null, complaint.getId() + " numaralı şikayet için, " + complaint.getCustomer().getName() + " adlı müşteriye iade talebi oluşturuldu!");
    }

    @Override
    public void onFinishOperation(Complaint complaint) {
        setSelectedItem(selectedItem);
        tableModel.update(complaint);
        JOptionPane.showMessageDialog(null, complaint.getId() + " numaralı şikayet için " + complaint.getTotalFee() + " tutarında ödeme yapılması için bekleniyor!");
    }
}
