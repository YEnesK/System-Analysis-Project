package system.model.table;

import system.model.Payment;
import system.model.StockHistoryItem;

import java.util.List;
import java.util.regex.Pattern;

public class PaymentHistoryTableModel extends BaseTableModel<Payment> {
    public PaymentHistoryTableModel(List<Payment> payments) {
        super(payments);
    }

    @Override
    String[] getColumnNames() {
        return new String[]{
                "Tarih",
                "Şikayet No",
                "Müşteri Adı",
                "Müşteri Telefonu",
                "Toplam Ücret"
        };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 2:
                return String.class;
            default:
                return Integer.class;
        }
    }

    @Override
    public Object getValue(int columnIndex, Payment item) {
        switch (columnIndex) {
            case 0:
                return item.getDate();
            case 1:
                return item.getComplaint().getId();
            case 2:
                return item.getComplaint().getCustomer().getName();
            case 3:
                return item.getComplaint().getCustomer().getPhone();
            case 4:
                return item.getComplaint().getTotalFee();
        }
        return null;
    }

    @Override
    public boolean getFilter(Payment item, String query) {
        String date = item.getDate();
        String regex = date.substring(0, date.length() - 3);
        return Pattern.matches(regex, query);
    }
}
