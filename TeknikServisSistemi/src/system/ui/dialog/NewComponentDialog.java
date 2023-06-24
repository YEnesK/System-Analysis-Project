package system.ui.dialog;

import system.model.Component;
import system.ui.App;

import javax.swing.*;
import java.awt.event.*;
import java.util.regex.Pattern;

public class NewComponentDialog extends JDialog {
    private static final String TITLE = "Yeni Parça";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldCode;
    private JTextField fieldName;
    private JTextField fieldPrice;
    private JTextField fieldStock;
    private JCheckBox checkBoxGenerateCode;

    private final AddComponentListener listener;

    public NewComponentDialog(AddComponentListener listener) {
        this.listener = listener;

        setTitle(TITLE);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        setLocationRelativeTo(null);

        checkBoxGenerateCode.addChangeListener(e -> {
            if (checkBoxGenerateCode.isSelected()) {
                fieldCode.setEnabled(false);
                fieldCode.setText("");
            } else {
                fieldCode.setEnabled(true);
            }
        });

        buttonOK.addActionListener(e -> {
            boolean generateCode = checkBoxGenerateCode.isSelected();
            String code = generateCode ? null : fieldCode.getText().trim();
            if (!generateCode && code.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Kod kısmı boş bırakılamaz!");
                return;
            }
            String name = fieldName.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "İsim kısmı boş bırakılamaz!");
                return;
            }
            String price = fieldPrice.getText().trim();
            String regexPrice = "\\d{1,9}";
            if (!Pattern.matches(regexPrice, price)) {
                JOptionPane.showMessageDialog(null, "Lütfen geçerli bir fiyat giriniz!");
                return;
            }
            String stock = fieldStock.getText().trim();
            if (!Pattern.matches(regexPrice, stock)) {
                JOptionPane.showMessageDialog(null, "Lütfen geçerli bir stok giriniz!");
                return;
            }
            onOK(code, name, Integer.parseInt(stock), Integer.parseInt(price));
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

    private String insertComponent(Component c) {
        return App.getDatabaseHelper().insertComponent(c);
    }

    private void onOK(String code, String name, int stock, int price) {
        Component c = new Component(code, name, stock, price);
        c.setCode(insertComponent(c));
        listener.onAddComponent(c);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public interface AddComponentListener {
        void onAddComponent(Component component);
    }

}
