package system.ui;

import system.model.Customer;
import system.model.table.CustomerTableModel;
import system.model.document.SingleDocumentListener;
import system.ui.dialog.NewCustomerDialog;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import static system.util.TableRendererUtils.setRenderer;

public class CustomerScreen extends JFrame implements NewCustomerDialog.AddCustomerListener {

    private static final String TITLE = "Müşteri İşlemleri";

    private JPanel panel;
    private JTable tableCustomers;
    private JButton buttonAdd;
    private JButton buttonRemove;
    private JTextField fieldCustomerName;

    private Customer selectedItem;
    private CustomerTableModel tableModel;

    public CustomerScreen() {
        super(TITLE);
        setContentPane(panel);
        setPreferredSize(new Dimension(900, 550));
        pack();
        setLocationRelativeTo(null);

        setRenderer(tableCustomers);

        List<Customer> customers = fetchAllCustomers();
        listAll(customers);

        setSelectedItem(null);

        buttonAdd.addActionListener(e -> {
            new NewCustomerDialog(this).setVisible(true);
        });

        buttonRemove.addActionListener(e -> {
            int n = JOptionPane.showConfirmDialog(
                    null,
                    selectedItem.getName() + " adlı müşteriyi silmek üzeresiniz. Onaylıyor musunuz?",
                    "Müşteri Silme Onayı",
                    JOptionPane.YES_NO_OPTION
            );
            if (n == JOptionPane.YES_OPTION) {
                removeCustomer();
            }
        });

        fieldCustomerName.getDocument().addDocumentListener((SingleDocumentListener) e -> {
            tableModel.filter(fieldCustomerName.getText());
        });
    }

    private void removeCustomer() {
        App.getDatabaseHelper().deleteCustomerById(selectedItem.getId());
        tableModel.remove(selectedItem);
        fieldCustomerName.setText("");
    }

    private List<Customer> fetchAllCustomers() {
        return App.getDatabaseHelper().getCustomers();
    }

    private void listAll(List<Customer> customers) {
        tableModel = new CustomerTableModel(customers);
        tableCustomers.setModel(tableModel);
        tableCustomers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableCustomers.getSelectionModel().addListSelectionListener(e -> {
            int selectedIndex = tableCustomers.getSelectedRow();
            if (selectedIndex < 0) {
                setSelectedItem(null);
                return;
            }
            setSelectedItem(tableModel.getItem(selectedIndex));
        });
    }

    public void setSelectedItem(Customer selectedItem) {
        this.selectedItem = selectedItem;
        if (selectedItem == null) {
            buttonRemove.setEnabled(false);
        } else {
            buttonRemove.setEnabled(true);
        }
    }

    @Override
    public void onAddCustomer(Customer newCustomer) {
        tableModel.add(newCustomer);
        fieldCustomerName.setText("");
    }
}
