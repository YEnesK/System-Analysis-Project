package system.ui;

import system.model.Component;
import system.model.document.SingleDocumentListener;
import system.model.table.ComponentTableModel;
import system.ui.dialog.DecreaseComponentStockDialog;
import system.ui.dialog.IncreaseComponentStockDialog;
import system.ui.dialog.NewComponentDialog;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static system.util.TableRendererUtils.setRenderer;

public class ComponentScreen extends JFrame implements NewComponentDialog.AddComponentListener, IncreaseComponentStockDialog.IncreaseComponentStockListener, DecreaseComponentStockDialog.DecreaseComponentStockListener {
    private static final String TITLE = "Parça İşlemleri";

    private JTable tableComponents;
    private JButton buttonRemove;
    private JButton buttonInput;
    private JButton buttonOutput;
    private JButton buttonAdd;
    private JPanel panel;
    private JTextField fieldComponentCode;

    private Component selectedItem;
    private ComponentTableModel tableModel;

    public ComponentScreen() {
        super(TITLE);

        setContentPane(panel);
        setPreferredSize(new Dimension(900, 550));
        pack();
        setLocationRelativeTo(null);

        setRenderer(tableComponents);

        List<Component> components = fetchAllComponents();
        listAll(components);

        setSelectedItem(null);

        buttonAdd.addActionListener(e -> {
            new NewComponentDialog(this).setVisible(true);
        });

        buttonRemove.addActionListener(e -> {
            int n = JOptionPane.showConfirmDialog(
                    null,
                    selectedItem.getName() + " adlı parçayı silmek üzeresiniz. Onaylıyor musunuz?",
                    "Parça Silme Onayı",
                    JOptionPane.YES_NO_OPTION
            );
            if (n == JOptionPane.YES_OPTION) {
                removeComponent();
            }
        });

        buttonInput.addActionListener(e -> {
            new IncreaseComponentStockDialog(selectedItem, this).setVisible(true);
        });

        buttonOutput.addActionListener(e -> {
            new DecreaseComponentStockDialog(selectedItem, this).setVisible(true);
        });

        fieldComponentCode.getDocument().addDocumentListener((SingleDocumentListener) e -> {
            tableModel.filter(fieldComponentCode.getText());
        });
    }

    private void removeComponent() {
        App.getDatabaseHelper().deleteComponentById(selectedItem.getId());
        tableModel.remove(selectedItem);
        fieldComponentCode.setText("");
    }

    private List<Component> fetchAllComponents() {
        return App.getDatabaseHelper().getComponents();
    }

    private void listAll(List<Component> components) {
        tableModel = new ComponentTableModel(components);
        tableComponents.setModel(tableModel);
        tableComponents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableComponents.getSelectionModel().addListSelectionListener(e -> {
            int selectedIndex = tableComponents.getSelectedRow();
            if (selectedIndex < 0) {
                setSelectedItem(null);
                return;
            }
            setSelectedItem(tableModel.getItem(selectedIndex));
        });
    }

    public void setSelectedItem(Component selectedItem) {
        this.selectedItem = selectedItem;
        if (selectedItem == null) {
            buttonRemove.setEnabled(false);
            buttonInput.setEnabled(false);
            buttonOutput.setEnabled(false);
        } else {
            buttonRemove.setEnabled(true);
            buttonInput.setEnabled(true);
            buttonOutput.setEnabled(true);

        }
    }

    @Override
    public void onAddComponent(Component component) {
        tableModel.add(component);
        fieldComponentCode.setText("");
    }

    @Override
    public void onIncreaseComponentStock(int newStock) {
        selectedItem.setStock(newStock);
        tableModel.update(selectedItem);
    }

    @Override
    public void onDecreaseComponentStock(int newStock) {
        selectedItem.setStock(newStock);
        tableModel.update(selectedItem);
    }
}
