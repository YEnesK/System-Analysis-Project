package system.ui.dialog;

import system.model.Customer;
import system.ui.App;

import javax.swing.*;
import java.awt.event.*;
import java.util.regex.Pattern;

public class NewCustomerDialog extends JDialog {
    private static final String TITLE = "Yeni Müşteri";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldName;
    private JTextField fieldPhone;
    private JTextField fieldAddress;

    private final AddCustomerListener listener;

    public NewCustomerDialog(AddCustomerListener listener) {
        this.listener = listener;

        setTitle(TITLE);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        setLocationRelativeTo(null);

        buttonOK.addActionListener(e -> {
            String name = fieldName.getText().trim();
            if(name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "İsim kısmı boş bırakılamaz!");
                return;
            }
            String phone = fieldPhone.getText().trim();
            String regexPhone = "5\\d{2} \\d{3}-\\d{4}";
            if(!Pattern.matches(regexPhone, phone)) {
                JOptionPane.showMessageDialog(null, "Lütfen geçerli bir telefon giriniz!");
                return;
            }
            String address = fieldAddress.getText().trim();
            if(address.length() < 5) {
                JOptionPane.showMessageDialog(null, "Adres çok kısa!");
                return;
            }
            onOK(name, phone, address);
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

    private int insertCustomer(Customer customer) {
        return App.getDatabaseHelper().insertCustomer(customer);
    }

    private void onOK(String name, String phone, String address) {
        Customer c = new Customer(name, phone, address);
        c.setId(insertCustomer(c));
        listener.onAddCustomer(c);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public interface AddCustomerListener {
        void onAddCustomer(Customer newCustomer);
    }

}
