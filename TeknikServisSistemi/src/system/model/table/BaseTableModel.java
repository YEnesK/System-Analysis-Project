package system.model.table;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseTableModel<T> extends AbstractTableModel {
    private final List<T> fullList;
    private final List<T> filteredList;

    abstract String[] getColumnNames();

    public BaseTableModel(List<T> itemList) {
        fullList = new ArrayList<>(itemList);
        filteredList = new ArrayList<>(itemList);
    }

    @Override
    public final int getRowCount() {
        return filteredList.size();
    }

    @Override
    public final int getColumnCount() {
        return getColumnNames().length;
    }

    @Override
    public final String getColumnName(int column) {
        return getColumnNames()[column];
    }

    @Override
    public final Object getValueAt(int rowIndex, int columnIndex) {
        return getValue(columnIndex, filteredList.get(rowIndex));
    }

    public abstract Object getValue(int columnIndex, T item);

    public void add(T newItem) {
        fullList.add(newItem);
        filter(null);
    }

    public void remove(T item) {
        int i = filteredList.indexOf(item);
        if (i < 0) {
            return;
        }
        fullList.remove(item);
        filter(null);
    }

    public void update(T item) {
        int i = filteredList.indexOf(item);
        fireTableRowsUpdated(i, i);
    }

    public void filter(String query) {
        filteredList.clear();
        if(query == null || query.trim().isEmpty()) {
            filteredList.addAll(fullList);
            fireTableDataChanged();
            return;
        }
        for(T item: fullList) {
            if(getFilter(item, query)) {
                filteredList.add(item);
            }
        }
        fireTableDataChanged();
    }

    public abstract boolean getFilter(T item, String query);

    public T getItem(int index) {
        return filteredList.get(index);
    }

    public List<T> getAllItems() {
        return fullList;
    }
}
