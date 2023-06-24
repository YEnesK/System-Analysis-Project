package system.ui.dialog;

import system.model.Component;
import system.model.StockHistoryItem;
import system.ui.App;

import javax.swing.*;
import java.awt.event.*;
import java.util.regex.Pattern;

public class IncreaseComponentStockDialog extends JDialog {
    private static final String TITLE = "Parça Girişi";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldCode;
    private JTextField fieldName;
    private JTextField fieldPrice;
    private JTextField fieldStock;
    private JTextField fieldInput;

    private final IncreaseComponentStockListener listener;

    public IncreaseComponentStockDialog(Component c, IncreaseComponentStockListener listener) {
        this.listener = listener;

        setTitle(TITLE);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        setLocationRelativeTo(null);

        fieldCode.setText(c.getCode());
        fieldName.setText(c.getName());
        fieldStock.setText(String.valueOf(c.getStock()));
        fieldPrice.setText(String.valueOf(c.getPrice()));

        buttonOK.addActionListener(e -> {
            String stockInput = fieldInput.getText().trim();
            String regexInput = "\\d{1,9}";
            if (!Pattern.matches(regexInput, stockInput)) {
                JOptionPane.showMessageDialog(null, "Lütfen geçerli bir parça giriş adedi giriniz!");
                return;
            }
            if (Integer.parseInt(stockInput) == 0) {
                JOptionPane.showMessageDialog(null, "Parça giriş adedi 0 olamaz!");
                return;
            }
            onOK(c, Integer.parseInt(stockInput));
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

    private void onOK(Component c, int input) {
        App.getDatabaseHelper().increaseComponentStockById(c.getId(), input);
        StockHistoryItem stockHistoryItem = new StockHistoryItem(null, c, "PARÇA GİRİŞİ", c.getStock(), c.getStock() + input);
        // stok kaydı tut
        App.getDatabaseHelper().insertStockHistoryItem(stockHistoryItem);
        int newStock = c.getStock() + input;
        listener.onIncreaseComponentStock(newStock);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public interface IncreaseComponentStockListener {
        void onIncreaseComponentStock(int newStock);
    }
}
