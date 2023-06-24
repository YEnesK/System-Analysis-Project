package system.model.table;

import system.model.Component;

import java.util.List;

public class ComponentTableModel extends BaseTableModel<Component> {

    public ComponentTableModel(List<Component> componentList) {
        super(componentList);
    }

    @Override
    String[] getColumnNames() {
        return new String[]{
                "Parça Kodu",
                "Parça adı",
                "Fiyat",
                "Stok Adet"
        };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 1:
                return String.class;
            case 2:
            case 3:
                return Integer.class;
        }
        return super.getColumnClass(columnIndex);
    }

    @Override
    public Object getValue(int columnIndex, Component item) {
        switch (columnIndex) {
            case 0:
                return item.getCode();
            case 1:
                return item.getName();
            case 2:
                return item.getPrice();
            case 3:
                return item.getStock();
        }
        return null;
    }

    @Override
    public boolean getFilter(Component item, String query) {
        return item.getCode().toLowerCase().contains(query.toLowerCase());
    }
}
