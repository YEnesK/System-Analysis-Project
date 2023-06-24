package system.ui.dialog;

import system.model.Technician;
import system.ui.App;

import javax.swing.*;
import java.awt.event.*;
import java.util.regex.Pattern;

public class NewTechnicianDialog extends JDialog {
    private static final String TITLE = "Yeni Teknisyen";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldName;
    private JTextField fieldPhone;

    private final AddTechnicianListener listener;

    public NewTechnicianDialog(AddTechnicianListener listener) {
        this.listener = listener;

        setTitle(TITLE);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        pack();
        setLocationRelativeTo(null);

        buttonOK.addActionListener(e -> {
            String name = fieldName.getText().trim();
            if(name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "İsim kısmı boş bırakılamaz!");
                return;
            }
            String phone = fieldPhone.getText().trim();
            String regexPhone = "5\\d{2} \\d{3}-\\d{4}";
            if(!Pattern.matches(regexPhone, phone)) {
                JOptionPane.showMessageDialog(null, "Lütfen geçerli bir telefon giriniz!");
                return;
            }
            onOK(name, phone);
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

    private int insertTechnician(Technician technician) {
        return App.getDatabaseHelper().insertTechnician(technician);
    }

    private void onOK(String name, String phone) {
        Technician t = new Technician(name, phone);
        t.setId(insertTechnician(t));
        listener.onAddTechnician(t);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public interface AddTechnicianListener {
        void onAddTechnician(Technician newTechnician);
    }

}
