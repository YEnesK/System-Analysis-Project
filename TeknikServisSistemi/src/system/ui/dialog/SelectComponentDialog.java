package system.ui.dialog;

import system.model.Component;
import system.model.document.SingleDocumentListener;
import system.model.table.ComponentTableModel;
import system.ui.App;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.regex.Pattern;

import static system.util.TableRendererUtils.setRenderer;

public class SelectComponentDialog extends JDialog {
    private static final String TITLE = "Parça Seç";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldCode;
    private JTable tableComponents;
    private JTextField fieldPiece;

    private final SelectComponentListener listener;
    private Component selectedComponent;
    private ComponentTableModel tableModel;

    public SelectComponentDialog(SelectComponentListener listener) {
        this.listener = listener;

        setTitle(TITLE);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        setLocationRelativeTo(null);

        setRenderer(tableComponents);

        List<Component> components = fetchAllComponents();
        listAll(components);

        setSelectedComponent(null);

        fieldPiece.setText("1");

        fieldCode.getDocument().addDocumentListener((SingleDocumentListener) e -> {
            tableModel.filter(fieldCode.getText());
        });

        buttonOK.addActionListener(e -> {
            String piece = fieldPiece.getText().trim();
            String regexPiece = "\\d{1,9}";
            if (!Pattern.matches(regexPiece, piece)) {
                JOptionPane.showMessageDialog(null, "Lütfen geçerli bir adet giriniz!");
                return;
            }
            if (Integer.parseInt(piece) == 0) {
                JOptionPane.showMessageDialog(null, "Parça adedi 0 olamaz!");
                return;
            }
            onOK(Integer.parseInt(piece));
        });

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
                setSelectedComponent(null);
                return;
            }
            setSelectedComponent(tableModel.getItem(selectedIndex));
        });
    }

    private void onOK(int piece) {
        listener.onSelectComponent(selectedComponent, piece);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public interface SelectComponentListener {
        void onSelectComponent(Component component, int piece);
    }

    public void setSelectedComponent(Component selectedComponent) {
        this.selectedComponent = selectedComponent;
        if(selectedComponent == null) {
            buttonOK.setEnabled(false);
        } else {
            buttonOK.setEnabled(true);
        }
    }
}
