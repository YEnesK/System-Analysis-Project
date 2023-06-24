package system.ui;

import system.model.Complaint;
import system.model.DeviceType;
import system.model.StockHistoryItem;
import system.model.document.SingleDocumentListener;
import system.model.table.ComplaintTableModel;
import system.model.table.StockHistoryTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static system.util.TableRendererUtils.setRenderer;

public class StockHistoryScreen extends JFrame {
    private static final String TITLE = "Stok Hareketleri";

    private JTable tableStockHistory;
    private JPanel panel;
    private JComboBox comboMonth;
    private JComboBox comboYear;

    private StockHistoryTableModel tableModel;

    public StockHistoryScreen() {
        super(TITLE);
        setContentPane(panel);
        setPreferredSize(new Dimension(900, 550));
        pack();
        setLocationRelativeTo(null);

        setRenderer(tableStockHistory);

        List<StockHistoryItem> stockHistory = fetchAllStockHistory();
        listAll(stockHistory);

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

    private List<StockHistoryItem> fetchAllStockHistory() {
        return App.getDatabaseHelper().getStockHistory();
    }

    private void listAll(List<StockHistoryItem> stockHistory) {
        tableModel = new StockHistoryTableModel(stockHistory);
        tableStockHistory.setModel(tableModel);
        tableStockHistory.setCellSelectionEnabled(false);
    }

}
