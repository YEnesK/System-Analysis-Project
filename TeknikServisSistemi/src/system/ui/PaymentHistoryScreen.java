package system.ui;

import system.model.Payment;
import system.model.StockHistoryItem;
import system.model.table.PaymentHistoryTableModel;
import system.model.table.StockHistoryTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.List;

import static system.util.TableRendererUtils.setRenderer;

public class PaymentHistoryScreen extends JFrame {
    private static final String TITLE = "Ödeme Geçmişi";

    private JTable tablePaymentHistory;
    private JPanel panel;
    private JComboBox comboMonth;
    private JComboBox comboYear;

    private PaymentHistoryTableModel tableModel;

    public PaymentHistoryScreen() {
        super(TITLE);
        setContentPane(panel);
        setPreferredSize(new Dimension(900, 550));
        pack();
        setLocationRelativeTo(null);

        setRenderer(tablePaymentHistory);

        List<Payment> payments = fetchAllPaymentHistory();
        listAll(payments);

        comboYear.setModel(new DefaultComboBoxModel<>(generateYears()));
        comboYear.setSelectedItem(null);

        comboMonth.setModel(new DefaultComboBoxModel<>(generateMonths()));
        comboMonth.setSelectedItem(null);

        comboMonth.addItemListener(e -> {
            onChangeDate();
        });
        comboYear.addItemListener(e -> {
            onChangeDate();
        });
    }

    private String[] generateYears() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        String[] years = new String[5];
        for (int i = 0; i < 5; i++) {
            years[i] = String.valueOf(currentYear - 4 + i);
        }
        return years;
    }

    private String[] generateMonths() {
        return new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    }

    private void onChangeDate() {
        String query = getSelectedDate();
        tableModel.filter(query);
    }

    private String getSelectedDate() {
        Object month = comboMonth.getSelectedItem();
        Object year = comboYear.getSelectedItem();
        if (month == null || year == null) {
            return null;
        }
        return year + "-" + month;
    }

    private List<Payment> fetchAllPaymentHistory() {
        return App.getDatabaseHelper().getPaymentHistory();
    }

    private void listAll(List<Payment> payments) {
        tableModel = new PaymentHistoryTableModel(payments);
        tablePaymentHistory.setModel(tableModel);
        tablePaymentHistory.setCellSelectionEnabled(false);
    }

}
