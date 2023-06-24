package system.model.table;

import system.model.Customer;

import java.util.List;

public class CustomerTableModel extends BaseTableModel<Customer> {

    public CustomerTableModel(List<Customer> customerList) {
        super(customerList);
    }

    @Override
    String[] getColumnNames() {
        return new String[]{
                "Müşteri Adı",
                "Telefon Numarası",
                "Adres"
        };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 2:
                return String.class;
            case 1:
                return Integer.class;
        }
        return super.getColumnClass(columnIndex);
    }

    @Override
    public Object getValue(int columnIndex, Customer item) {
        switch (columnIndex) {
            case 0:
                return item.getName();
            case 1:
                return item.getPhone();
            case 2:
                return item.getAddress();
        }
        return null;
    }

    @Override
    public boolean getFilter(Customer item, String query) {
        return item.getName().toLowerCase().contains(query.toLowerCase());
    }
}
