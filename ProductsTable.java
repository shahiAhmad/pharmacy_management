import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class ProductsTable {

    // Declaring three panels
    private final JPanel leftPanel;
    DefaultTableModel model;
    private final Stock conn = new Stock();
    private JTable stockTable;
    // Declaring menu items
    private JMenuItem updateItem;
    private JMenuItem deleteItem;
    private JMenuItem showStockLimitsItem;
    private JPanel menuPanel;
    private JComboBox<String> reportDropdown;
    private JComboBox<String> categoryDropdown;
    private JButton addNewProductButton;

    // Left Panel where stock table will be displayed
    public ProductsTable() {
        leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.RED);
        leftPanel.setPreferredSize(new Dimension(450, getScreenHeight()));
    }

    // Get screen height
    public int getScreenHeight() {
        // Get the default toolkit
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        // Get the screen size
        Dimension screenSize = toolkit.getScreenSize();

        // Get the screen height
        int screenHeight = screenSize.height;

        return screenHeight;
    }

    // Setting up left panel
    public JPanel settingUpProductTablePanel() {

        // Disable editing of table fields
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };


        // Adding menu panel
        menuPanel = new JPanel(new GridLayout(0, 4));
        // Adding Combo box
        String[] reportOptions = {"Save stock report as pdf", "Save order report as pdf", "Export to CSV"};
        reportDropdown = new JComboBox<>(reportOptions);
        reportDropdown.setFocusable(false);
        reportDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
                String selectedOption = (String) comboBox.getSelectedItem();

                if (selectedOption.equals(reportOptions[1])) {
                    Order orderReport = new Order();
                    orderReport.generatePurchaseReport();
                } else if (selectedOption.equals(reportOptions[2])) {
                    Stock stockReport = new Stock();
                    stockReport.exportToCSV();
                } else if (selectedOption.equals(reportOptions[0])) {
                    Product productReport = new Product();
                    productReport.generateStockReport();
                }
            }
        });


        // Drop down to select category
        String[] categoryOptions = {"All", "Tablet", "Syrup", "Cosmetic", "Injection", "Drug"};
        categoryDropdown = new JComboBox<>(categoryOptions);
        categoryDropdown.setFocusable(false);
        conn.showData(model, "All");
        categoryDropdown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedOption = (String) categoryDropdown.getSelectedItem();
                    if (selectedOption.equals("All")) {
                        model.setColumnCount(0);
                        conn.showData(model, "All");
                    } else {
                        model.setColumnCount(0);
                        conn.showData(model, selectedOption.toLowerCase());
                    }
                }
            }
        });


        // Stock Table
        stockTable = new JTable(model);
        stockTable.setFocusable(false);
        //Add new product in table
        addNewProductButton = createButton("Add New Product");
        addNewProductButton.setFont(new Font("Arial", Font.BOLD, 10));
        addNewProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Forms addProductFrom = new Forms();
                addProductFrom.ProductForm();

                addProductFrom.addButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (addProductFrom.getProductNameField().isEmpty() || addProductFrom.getPriceField()<=0 || addProductFrom.getExpField().isEmpty() || addProductFrom.getQuantityField()<=0) {
                            JOptionPane.showMessageDialog(null, "Valid input is required for all fields.", "Message", JOptionPane.INFORMATION_MESSAGE);
                        }else {

                            conn.addProductToStock(addProductFrom.getProductNameField(), addProductFrom.getCategory(), addProductFrom.getExpField(), addProductFrom.getQuantityField(), addProductFrom.getPriceField());
                            addProductFrom.productForm.setVisible(false);
                            resetTable();
                            conn.showData(model, "All");
                        }
                    }
                });
            }
        });

        // Setting up popup menu
        JPopupMenu popupMenu = new JPopupMenu();
        updateItem = new JMenuItem("Update");
        deleteItem = new JMenuItem("Delete");
        showStockLimitsItem = new JMenuItem("Show Stock Limits");
        //Update product
        updateItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = stockTable.getSelectedRow();
                if (selectedRow != -1) {
//                    String name, String category, String expiryDate, int quantity, double price
                    Forms updateProduct = new Forms();
                    updateProduct.ProductForm();
                    updateProduct.setProductNameField((String) stockTable.getValueAt(selectedRow, 0));
                    updateProduct.setCategory(stockTable.getValueAt(selectedRow, 1).toString());
                    updateProduct.setExpField(stockTable.getValueAt(selectedRow, 2).toString());
                    updateProduct.setQuantityField((Integer) stockTable.getValueAt(selectedRow, 3));
                    updateProduct.setPriceField((Double) stockTable.getValueAt(selectedRow, 4));
                    updateProduct.addButton.setText("Update");
                    updateProduct.makeProductNameFieldFinal();
                    updateProduct.addButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (updateProduct.getPriceField() <= 0 || updateProduct.getQuantityField() <= 0 || updateProduct.getExpField().equals(""))
                                JOptionPane.showMessageDialog(null, "Valid input is required for all fields.", "Message", JOptionPane.INFORMATION_MESSAGE);
                            else {
                                updateProduct.productForm.setVisible(false);
                            conn.updateProduct(updateProduct.getProductNameField(), updateProduct.getCategory(), updateProduct.getExpField(), updateProduct.getQuantityField(), updateProduct.getPriceField());
                            resetTable();
                            conn.showData(model, "All");
                            System.out.println("Updated");
                        }
                        }
                    });
                }
            }
        });
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem source = (JMenuItem) e.getSource();
                String menuItemText = source.getText();
                if (menuItemText.equals("Delete")) {
                    int selectedRow = stockTable.getSelectedRow();
                    String name = (String) stockTable.getModel().getValueAt(selectedRow, 0);
                    String category = (String) stockTable.getModel().getValueAt(selectedRow, 1);
                    conn.deleteProductFromStock(name, category);
                    resetTable();
                    conn.showData(model, categoryDropdown.getSelectedItem().toString());
                }
            }
        });

        popupMenu.add(updateItem);
        popupMenu.add(deleteItem);
        popupMenu.add(showStockLimitsItem);
        // Add a MouseListener to the panel
        stockTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                showPopupMenu(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                showPopupMenu(e);
            }

            private void showPopupMenu(MouseEvent e) {
                if (stockTable.getSelectedRow() != -1) {
                    if (e.isPopupTrigger()) {
                        if (stockTable.getSelectedRow() > 1) {
                            int selectedRowCount = stockTable.getSelectedRowCount();
                            popupMenu.getComponent(0).setEnabled(selectedRowCount == 1);
                            popupMenu.getComponent(2).setEnabled(selectedRowCount == 1);
                        }
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });

        // Add components to the panel
        menuPanel.add(reportDropdown);
        menuPanel.add(categoryDropdown);
        menuPanel.add(new JLabel());
        menuPanel.add(addNewProductButton);
        // Set the layout manager for leftPanel
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(menuPanel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(stockTable), BorderLayout.CENTER);

        // Set the preferred size of the table to match the panel's size
        leftPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension panelSize = leftPanel.getSize();
                stockTable.setPreferredScrollableViewportSize(panelSize);
            }
        });

        leftPanel.setComponentPopupMenu(popupMenu);
        return leftPanel;
    }


    // Create Button
    public JButton createButton(String title) {
        JButton button = new JButton(title);
        button.setFocusable(false);
        return button;
    }

    private void resetTable() {
        model.setRowCount(0);
        model.setColumnCount(0);
    }

}
