package system.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StockHistoryItem {
    private final String date;
    private final Component component;
    private final String operation;
    private final int previousStock;
    private final int currentStock;

    public StockHistoryItem(String date, Component component, String operation, int previousStock, int currentStock) {
        this.date = date;
        this.component = component;
        this.operation = operation;
        this.previousStock = previousStock;
        this.currentStock = currentStock;
    }

    public static StockHistoryItem fromResultSet(ResultSet rs) throws SQLException {
        return new StockHistoryItem(
                rs.getString(1),
                new Component(
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(6),
                        rs.getInt(5)
                ),
                rs.getString(7),
                rs.getInt(8),
                rs.getInt(9)
        );
    }

    public String getDate() {
        return date;
    }

    public Component getComponent() {
        return component;
    }

    public String getOperation() {
        return operation;
    }

    public int getPreviousStock() {
        return previousStock;
    }

    public int getCurrentStock() {
        return currentStock;
    }
}
