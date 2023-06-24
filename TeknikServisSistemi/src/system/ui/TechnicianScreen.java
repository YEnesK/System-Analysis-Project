package system.ui;

import system.model.*;
import system.model.document.SingleDocumentListener;
import system.model.table.TechnicianTableModel;
import system.ui.dialog.NewTechnicianDialog;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static system.util.TableRendererUtils.setRenderer;

public class TechnicianScreen extends JFrame implements NewTechnicianDialog.AddTechnicianListener {

    private static final String TITLE = "Teknisyen İşlemleri";

    private JTable tableTechnicians;
    private JTextField fieldTechnicianName;
    private JButton buttonAdd;
    private JButton buttonRemove;
    private JPanel panel;

    private Technician selectedItem;
    private TechnicianTableModel tableModel;

    public TechnicianScreen() {
        super(TITLE);

        setContentPane(panel);
        setPreferredSize(new Dimension(900, 550));
        pack();
        setLocationRelativeTo(null);

        setRenderer(tableTechnicians);

        List<Technician> technicians = fetchAllTechnicians();
        listAll(technicians);

        setSelectedItem(null);

        buttonAdd.addActionListener(e -> {
            new NewTechnicianDialog(this).setVisible(true);
        });

        buttonRemove.addActionListener(e -> {
            int n = JOptionPane.showConfirmDialog(
                    null,
                    selectedItem.getName() + " adlı teknisyeni silmek üzeresiniz. Onaylıyor musunuz?",
                    "Teknisyen Silme Onayı",
                    JOptionPane.YES_NO_OPTION
            );
            if (n == JOptionPane.YES_OPTION) {
                removeTechnician();
            }
        });

        fieldTechnicianName.getDocument().addDocumentListener((SingleDocumentListener) e -> {
            tableModel.filter(fieldTechnicianName.getText());
        });
    }

    private void removeTechnician() {
        App.getDatabaseHelper().deleteTechnicianById(selectedItem.getId());
        tableModel.remove(selectedItem);
        fieldTechnicianName.setText("");
    }

    private List<Technician> fetchAllTechnicians() {
        return App.getDatabaseHelper().getTechnicians();
    }

    private void listAll(List<Technician> technicians) {
        tableModel = new TechnicianTableModel(technicians);
        tableTechnicians.setModel(tableModel);
        tableTechnicians.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableTechnicians.getSelectionModel().addListSelectionListener(e -> {
            int selectedIndex = tableTechnicians.getSelectedRow();
            if(selectedIndex < 0) {
                setSelectedItem(null);
                return;
            }
            setSelectedItem(tableModel.getItem(selectedIndex));
        });
    }

    public void setSelectedItem(Technician selectedItem) {
        this.selectedItem = selectedItem;
        if(selectedItem == null) {
            buttonRemove.setEnabled(false);
        } else {
            buttonRemove.setEnabled(true);
        }
    }

    @Override
    public void onAddTechnician(Technician newTechnician) {
        tableModel.add(newTechnician);
        fieldTechnicianName.setText("");
    }
}
