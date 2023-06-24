package system.model.table;

import system.model.Complaint;

import java.util.List;

public class ComplaintTableModel extends BaseTableModel<Complaint> {

    public ComplaintTableModel(List<Complaint> complaintList) {
        super(complaintList);
    }

    @Override
    String[] getColumnNames() {
        return new String[]{
                "Şikayet No",
                "Müşteri Adı",
                "Şikayet",
                "Cihaz Türü",
                "Marka",
                "Seri No",
                "Durum",
                "Toplam"
        };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 7:
                return Integer.class;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return String.class;
        }
        return super.getColumnClass(columnIndex);
    }

    @Override
    public Object getValue(int columnIndex, Complaint item) {
        switch (columnIndex) {
            case 0:
                return item.getId();
            case 1:
                return item.getCustomer().getName();
            case 2:
                return item.getComplaint();
            case 3:
                return item.getDeviceType().getName();
            case 4:
                return item.getBrand().getName();
            case 5:
                return item.getSerialNumber();
            case 6:
                return item.getStatus().getName();
            case 7:
                return item.getTotalFee();
        }
        return null;
    }

    @Override
    public boolean getFilter(Complaint item, String query) {
        return item.getCustomer().getName().toLowerCase().contains(query.toLowerCase());
    }
}
