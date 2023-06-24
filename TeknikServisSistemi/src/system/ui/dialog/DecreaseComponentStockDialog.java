package system.ui.dialog;

import system.model.Component;
import system.model.StockHistoryItem;
import system.ui.App;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Pattern;

public class DecreaseComponentStockDialog extends JDialog {
    private static final String TITLE = "Parça Çıkışı";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldCode;
    private JTextField fieldName;
    private JTextField fieldPrice;
    private JTextField fieldStock;
    private JTextField fieldOutput;

    private final DecreaseComponentStockListener listener;

    public DecreaseComponentStockDialog(Component c, DecreaseComponentStockListener listener) {
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
            String stockOutput = fieldOutput.getText().trim();
            String regexOutput = "\\d{1,9}";
            if (!Pattern.matches(regexOutput, stockOutput)) {
                JOptionPane.showMessageDialog(null, "Lütfen geçerli bir parça çıkış adedi giriniz!");
                return;
            }
            if (Integer.parseInt(stockOutput) == 0) {
                JOptionPane.showMessageDialog(null, "Parça çıkış adedi 0 olamaz!");
                return;
            }
            if (c.getStock() - Integer.parseInt(stockOutput) < 0) {
                JOptionPane.showMessageDialog(null, "Stok adedinden fazla parça çıkışı yapılamaz!");
                return;
            }
            onOK(c, Integer.parseInt(stockOutput));
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

    private void onOK(Component c, int output) {
        App.getDatabaseHelper().decreaseComponentStockById(c.getId(), output);
        StockHistoryItem stockHistoryItem = new StockHistoryItem(null, c, "PARÇA ÇIKIŞI", c.getStock(), c.getStock() - output);
        // stok kaydı tut
        App.getDatabaseHelper().insertStockHistoryItem(stockHistoryItem);
        int newStock = c.getStock() - output;
        listener.onDecreaseComponentStock(newStock);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public interface DecreaseComponentStockListener {
        void onDecreaseComponentStock(int newStock);
    }
}
