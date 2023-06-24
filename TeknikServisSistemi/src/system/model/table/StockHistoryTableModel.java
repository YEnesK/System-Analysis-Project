package system.model.table;

import system.model.StockHistoryItem;

import java.util.List;
import java.util.regex.Pattern;

public class StockHistoryTableModel extends BaseTableModel<StockHistoryItem> {
    public StockHistoryTableModel(List<StockHistoryItem> stockHistory) {
        super(stockHistory);
    }

    @Override
    String[] getColumnNames() {
        return new String[]{
                "Tarih",
                "İşlem",
                "Parça Kodu",
                "Parça Adı",
                "Önceki Stok",
                "Sonraki Stok"
        };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 2:
            case 3:
                return String.class;
            default:
                return Integer.class;
        }
    }

    @Override
    public Object getValue(int columnIndex, StockHistoryItem item) {
        switch (columnIndex) {
            case 0:
                return item.getDate();
            case 1:
                return item.getOperation();
            case 2:
                return item.getComponent().getCode();
            case 3:
                return item.getComponent().getName();
            case 4:
                return item.getPreviousStock();
            case 5:
                return item.getCurrentStock();
        }
        return null;
    }

    @Override
    public boolean getFilter(StockHistoryItem item, String query) {
        String date = item.getDate();
        String regex = date.substring(0, date.length() - 3);
        return Pattern.matches(regex, query);
    }
}
