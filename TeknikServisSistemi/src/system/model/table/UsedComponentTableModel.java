package system.model.table;

import system.model.UsedComponent;

import java.util.List;

public class UsedComponentTableModel extends BaseTableModel<UsedComponent> {

    public UsedComponentTableModel(List<UsedComponent> usedComponents) {
        super(usedComponents);
    }

    @Override
    String[] getColumnNames() {
        return new String[]{
                "Parça Kodu",
                "Parça adı",
                "Fiyat",
                "Stok Adet",
                "Kullanılan Adet"
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
            case 4:
                return Integer.class;
        }
        return super.getColumnClass(columnIndex);
    }

    @Override
    public Object getValue(int columnIndex, UsedComponent item) {
        switch (columnIndex) {
            case 0:
                return item.getComponent().getCode();
            case 1:
                return item.getComponent().getName();
            case 2:
                return item.getComponent().getPrice();
            case 3:
                return item.getComponent().getStock();
            case 4:
                return item.getPiece();
        }
        return null;
    }

    @Override
    public boolean getFilter(UsedComponent item, String query) {
        return false;
    }
}
