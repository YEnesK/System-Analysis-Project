package system.ui.dialog;

import system.model.Customer;
import system.model.document.SingleDocumentListener;
import system.model.table.CustomerTableModel;
import system.ui.App;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;

import static system.util.TableRendererUtils.setRenderer;

public class SelectCustomerDialog extends JDialog {
    private static final String TITLE = "Müşteri Seç";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldName;
    private JTable tableCustomers;

    private final SelectCustomerListener listener;
    private Customer selectedCustomer;
    private CustomerTableModel tableModel;

    public SelectCustomerDialog(SelectCustomerListener listener) {
        this.listener = listener;

        setTitle(TITLE);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        setLocationRelativeTo(null);

        setRenderer(tableCustomers);

        List<Customer> customers = fetchAllCustomers();
        listAll(customers);

        setSelectedCustomer(null);

        fieldName.getDocument().addDocumentListener((SingleDocumentListener) e -> {
            tableModel.filter(fieldName.getText());
        });

        buttonOK.addActionListener(e -> onOK());

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
                setSelectedCustomer(null);
                return;
            }
            setSelectedCustomer(tableModel.getItem(selectedIndex));
        });
    }

    private void onOK() {
        listener.onSelectCustomer(selectedCustomer);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public interface SelectCustomerListener {
        void onSelectCustomer(Customer customer);
    }

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
        if(selectedCustomer == null) {
            buttonOK.setEnabled(false);
        } else {
            buttonOK.setEnabled(true);
        }
    }
}
