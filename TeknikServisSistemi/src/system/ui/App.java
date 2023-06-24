package system.ui;

import system.DatabaseHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class App extends JFrame {

    private static final String TITLE = "Teknik Servis Sistemi";

    private JPanel panelMain;
    private JButton buttonReportComplaint;
    private JButton buttonListComplaints;
    private JButton buttonStock;
    private JButton buttonCustomers;
    private JButton buttonTechnicians;
    private JButton buttonStockHistory;
    private JButton buttonPayments;

    private static final DatabaseHelper databaseHelper = new DatabaseHelper().connect();

    public App() {
        super(TITLE);

        setContentPane(panelMain);
        setPreferredSize(new Dimension(1080, 720));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        buttonReportComplaint.addActionListener(e -> {
            new ReportComplaintScreen().setVisible(true);
        });
        buttonListComplaints.addActionListener(e -> {
            new ComplaintScreen().setVisible(true);
        });
        buttonStock.addActionListener(e -> {
            new ComponentScreen().setVisible(true);
        });
        buttonCustomers.addActionListener(e -> {
            new CustomerScreen().setVisible(true);
        });
        buttonTechnicians.addActionListener(e -> {
            new TechnicianScreen().setVisible(true);
        });
        buttonStockHistory.addActionListener(e -> {
            new StockHistoryScreen().setVisible(true);
        });
        buttonPayments.addActionListener(e -> {
            new PaymentHistoryScreen().setVisible(true);
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                databaseHelper.disconnect();
            }
        });
    }

    public static void main(String[] args) {
        new App().setVisible(true);
    }

    public static DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

}