package system.ui.dialog;

import system.model.*;
import system.model.Component;
import system.model.table.UsedComponentTableModel;
import system.ui.App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import static system.util.TableRendererUtils.setRenderer;

public class MakeOperationForComplaintDialog extends JDialog implements SelectComponentDialog.SelectComponentListener {
    private static final String TITLE = "İşlem Ekle";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldId;
    private JTable tableComponents;
    private JButton buttonAddComponent;
    private JTextField fieldComponentFee;
    private JTextField fieldCustomerName;
    private JTextField fieldComplaint;
    private JTextField fieldDeviceSerialNumber;
    private JButton buttonRemoveComponent;

    private final MakeOperationListener listener;
    private UsedComponentTableModel tableModel;
    private UsedComponent selectedItem;
    private int totalFee;

    public MakeOperationForComplaintDialog(Complaint complaint, MakeOperationListener listener) {
        this.listener = listener;

        setTitle(TITLE);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setPreferredSize(new Dimension(700, 500));
        pack();
        setLocationRelativeTo(null);

        fieldId.setText(String.valueOf(complaint.getId()));
        fieldComplaint.setText(complaint.getComplaint());
        fieldComponentFee.setText("0");
        fieldCustomerName.setText(complaint.getCustomer().getName());
        fieldDeviceSerialNumber.setText(complaint.getSerialNumber());

        setRenderer(tableComponents);

        setupComponentsTable();

        setSelectedItem(null);

        buttonAddComponent.addActionListener(e -> {
            new SelectComponentDialog(this).setVisible(true);
        });

        buttonRemoveComponent.addActionListener(e -> {
            removeComponent();
        });

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

    private void setupComponentsTable() {
        tableModel = new UsedComponentTableModel(new ArrayList<>());
        tableComponents.setModel(tableModel);
        tableComponents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableComponents.getSelectionModel().addListSelectionListener(e -> {
            int selectedIndex = tableComponents.getSelectedRow();
            if (selectedIndex < 0) {
                setSelectedItem(null);
                return;
            }
            setSelectedItem(tableModel.getItem(selectedIndex));
        });
    }

    private void removeComponent() {
        setTotalFee(totalFee - selectedItem.getComponent().getPrice() * selectedItem.getPiece());
        tableModel.remove(selectedItem);
    }

    public void setSelectedItem(UsedComponent selectedItem) {
        this.selectedItem = selectedItem;
        if (selectedItem == null) {
            buttonRemoveComponent.setEnabled(false);
        } else {
            buttonRemoveComponent.setEnabled(true);
        }
    }

    private void onOK(Complaint complaint) {
        List<UsedComponent> usedComponents = tableModel.getAllItems();
        if (checkIfSufficientStock(usedComponents)) {
            update(complaint, usedComponents);
        } else {
            insufficientStock(complaint, usedComponents);
        }
    }

    private void insufficientStock(Complaint complaint, List<UsedComponent> usedComponents) {
        int result = JOptionPane.showConfirmDialog(
                null,
                "Kullanılan parçalarda stoğu yeterli olmayan parça/parçalar mevcut! Parçaları beklemeye alabilirsiniz. Onaylıyor musunuz?",
                "Stok Yetersizliği",
                JOptionPane.YES_NO_OPTION
        );
        if (result == JOptionPane.OK_OPTION) {
            complaint.setStatus(ComplaintStatus.WAITING_COMPONENT);
            /**/
            App.getDatabaseHelper().updateComplaint(complaint);
            insertUsedComponents(complaint, usedComponents);
            /**/
            listener.onMakeOperation(complaint);
            dispose();
            JOptionPane.showMessageDialog(null, complaint.getId() + " nolu şikayet için stoğu yetersiz parçalar beklenmekte!");
        }
    }

    private void update(Complaint complaint, List<UsedComponent> usedComponents) {
        complaint.setStatus(ComplaintStatus.IN_OPERATION);
        /**/
        App.getDatabaseHelper().updateComplaint(complaint);
        insertUsedComponents(complaint, usedComponents);
        for (UsedComponent usedComponent : usedComponents) {
            Component c = usedComponent.getComponent();
            // stok azalt
            App.getDatabaseHelper().decreaseComponentStockById(c.getId(), usedComponent.getPiece());
            StockHistoryItem stockHistoryItem = new StockHistoryItem(null, c, "PARÇA ÇIKIŞI", c.getStock(), c.getStock() - usedComponent.getPiece());
            // stok kaydı tut
            App.getDatabaseHelper().insertStockHistoryItem(stockHistoryItem);
        }
        /**/
        listener.onMakeOperation(complaint);
        dispose();
        JOptionPane.showMessageDialog(null, complaint.getId() + " nolu şikayet işleme alındı!");
    }

    private void insertUsedComponents(Complaint complaint, List<UsedComponent> usedComponents) {
        App.getDatabaseHelper().insertUsedComponentsToComplaint(complaint.getId(), usedComponents);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    @Override
    public void onSelectComponent(Component component, int piece) {
        List<UsedComponent> items = tableModel.getAllItems();
        UsedComponent usedComponent = findComponent(items, component);
        if (usedComponent == null) {
            tableModel.add(new UsedComponent(component, piece));
        } else {
            usedComponent.setPiece(usedComponent.getPiece() + piece);
            tableModel.update(usedComponent);
        }
        setTotalFee(totalFee + component.getPrice() * piece);
    }

    private UsedComponent findComponent(List<UsedComponent> items, Component component) {
        for (UsedComponent item : items) {
            if (item.getComponent().getCode().equals(component.getCode())) {
                return item;
            }
        }
        return null;
    }

    private boolean checkIfSufficientStock(List<UsedComponent> items) {
        for (UsedComponent item : items) {
            if (item.getComponent().getStock() < item.getPiece()) {
                return false;
            }
        }
        return true;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
        fieldComponentFee.setText(String.valueOf(totalFee));
    }

    public interface MakeOperationListener {
        void onMakeOperation(Complaint complaint);
    }

}
