import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProductBilling extends JFrame {

    private final JPanel rightPanel;
    OrderDAM orders = new OrderDAM();
    Order order=new Order();
    Forms orderForm = new Forms();
    private JTable ordersTable;

    public ProductBilling() {
        rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(450, new ProductsTable().getScreenHeight()));
        rightPanel.setLayout(new FlowLayout());
        int padding = 100;
        rightPanel.setBorder(new EmptyBorder(0, padding, 0, padding));

    }

    public static JPanel createPricePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.insets = new Insets(5, 5, 5, 5);

        // Add total price label on left and price label on right
        JLabel totalPriceLabel = new JLabel("Total Price:");
        JLabel totalPriceValueLabel = new JLabel("5100.00");
        panel.add(totalPriceLabel, constraints);
        constraints.gridx++;
        panel.add(totalPriceValueLabel, constraints);

        // Add line separator
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        constraints.gridx = 0;
        constraints.gridwidth = 1;
        panel.add(separator, constraints);
        constraints.gridy++;

        // Add tax label on left and price label on right
        JLabel taxLabel = new JLabel("Tax:");
        JLabel taxValueLabel = new JLabel("210.00");
        panel.add(taxLabel, constraints);
        constraints.gridx++;
        panel.add(taxValueLabel, constraints);

        // Add line separator
        separator = new JSeparator(SwingConstants.HORIZONTAL);
        constraints.gridx = 0;
        panel.add(separator, constraints);
        constraints.gridy++;

        // Add "Total Price" label on left and price label on right
        JLabel discountLabel = new JLabel("Discount:");
        JLabel discountValueLabel = new JLabel("65.00");
        panel.add(discountLabel, constraints);
        constraints.gridx++;
        panel.add(discountValueLabel, constraints);

        // Add line separator
        separator = new JSeparator(SwingConstants.HORIZONTAL);
        constraints.gridx = 0;
        panel.add(separator, constraints);
        constraints.gridy++;

        // Add "Total Price" label on left and price label on right
        JLabel grandTotalLabel = new JLabel("Total Price:");
        JLabel grandTotalValueLabel = new JLabel("5710.00");
        panel.add(grandTotalLabel, constraints);
        constraints.gridx++;
        panel.add(grandTotalValueLabel, constraints);

        return panel;
    }

    public JPanel settingUpBillingPanel() {
        ordersTable = new JTable();
        DefaultTableModel ordersTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        // Set the width of "Arrival Status" column

        orders.showOrders(ordersTableModel);

        ordersTable.setModel(ordersTableModel);
        ordersTable.setFocusable(false);

        // Set the table model
        rightPanel.add(new JScrollPane(ordersTable)); // Add the table to the rightPanel with a scroll pane

        // Setting up popup menu
        JPopupMenu popupMenu = new JPopupMenu();
        // Declaring menu items
        JMenuItem showDetails = new JMenuItem("Show Detail");
        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem arrived = new JMenuItem("Arrived");
        showDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = ordersTable.getSelectedRow();
                int orderId = -1; // Default value if no row is selected or if ID is not found

                if (selectedRow != -1) {
                    orderId = (int) ordersTable.getValueAt(selectedRow, 0);
                }
                orderForm.OrderRecordWindow(orderId);
            }
        });
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = ordersTable.getSelectedRow();
                int orderId = -1; // Default value if no row is selected or if ID is not found

                if (selectedRow != -1) {
                    orderId = (int) ordersTable.getValueAt(selectedRow, 0);
                }

                orders.deleteOrder(orderId);
                ordersTableModel.setRowCount(0);
                ordersTableModel.setColumnCount(0);
                orders.showOrders(ordersTableModel);
            }
        });

        arrived.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = ordersTable.getSelectedRow();
                int orderId = -1; // Default value if no row is selected or if ID is not found

                if (selectedRow != -1) {
                    orderId = (int) ordersTable.getValueAt(selectedRow, 0);
                }

                order.setOrderStatus(orderId);
                ordersTableModel.setRowCount(0);
                ordersTableModel.setColumnCount(0);
                orders.showOrders(ordersTableModel);
            }
        });
        popupMenu.add(showDetails);
        popupMenu.add(deleteItem);
        popupMenu.add(arrived);

        ordersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = ordersTable.rowAtPoint(e.getPoint());

                if (selectedRow != -1) {
                    ordersTable.setRowSelectionInterval(selectedRow, selectedRow);
                    if (e.isPopupTrigger()) {
                        popupMenu.show(ordersTable, e.getX(), e.getY());
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int selectedRow = ordersTable.rowAtPoint(e.getPoint());

                if (selectedRow != -1) {
                    ordersTable.setRowSelectionInterval(selectedRow, selectedRow);
                    if (e.isPopupTrigger()) {
                        popupMenu.show(ordersTable, e.getX(), e.getY());
                    }
                }
            }
        });
        JButton placeNewOrder = new JButton("New Order");
        placeNewOrder.setFocusable(false);
        placeNewOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                orderForm.OrderForm();

                ordersTableModel.setRowCount(0);
                ordersTableModel.setColumnCount(0);
                orders.showOrders(ordersTableModel);
            }
        });
        rightPanel.add(placeNewOrder, BorderLayout.SOUTH);
        ordersTable.setComponentPopupMenu(popupMenu);
        return rightPanel;
    }


}