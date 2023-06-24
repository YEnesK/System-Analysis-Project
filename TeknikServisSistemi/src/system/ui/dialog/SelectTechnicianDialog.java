package system.ui.dialog;

import system.model.Technician;
import system.model.document.SingleDocumentListener;
import system.model.table.TechnicianTableModel;
import system.ui.App;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;

import static system.util.TableRendererUtils.setRenderer;

public class SelectTechnicianDialog extends JDialog {
    private static final String TITLE = "Teknisyen Seç";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tableTechnicians;
    private JTextField fieldName;


    private final SelectTechnicianListener listener;
    private Technician selectedCustomer;
    private TechnicianTableModel tableModel;

    public SelectTechnicianDialog(SelectTechnicianListener listener) {
        this.listener = listener;

        setTitle(TITLE);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        setLocationRelativeTo(null);

        setRenderer(tableTechnicians);

        List<Technician> technicians = fetchAllTechnicians();
        listAll(technicians);

        setSelectedCustomer(null);

        fieldName.getDocument().addDocumentListener((SingleDocumentListener) e -> {
            tableModel.filter(fieldName.getText());
        });

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
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
            if (selectedIndex < 0) {
                setSelectedCustomer(null);
                return;
            }
            setSelectedCustomer(tableModel.getItem(selectedIndex));
        });
    }

    private void onOK() {
        listener.onSelectTechnician(selectedCustomer);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public interface SelectTechnicianListener {
        void onSelectTechnician(Technician technician);
    }

    public void setSelectedCustomer(Technician selectedTechnician) {
        this.selectedCustomer = selectedTechnician;
        if(selectedCustomer == null) {
            buttonOK.setEnabled(false);
        } else {
            buttonOK.setEnabled(true);
        }
    }
}
