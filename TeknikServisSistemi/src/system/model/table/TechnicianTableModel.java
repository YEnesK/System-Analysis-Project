package system.model.table;

import system.model.Technician;

import java.util.List;

public class TechnicianTableModel extends BaseTableModel<Technician> {

    public TechnicianTableModel(List<Technician> technicianList) {
        super(technicianList);
    }

    @Override
    String[] getColumnNames() {
        return new String[]{
                "Teknisyen Adı",
                "Telefon Numarası",
        };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return Integer.class;
        }
        return super.getColumnClass(columnIndex);
    }

    @Override
    public Object getValue(int columnIndex, Technician item) {
        switch (columnIndex) {
            case 0:
                return item.getName();
            case 1:
                return item.getPhone();
        }
        return null;
    }

    @Override
    public boolean getFilter(Technician item, String query) {
        return item.getName().toLowerCase().contains(query.toLowerCase());
    }

}
