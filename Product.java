import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class Product {

    // Attributes of product

    private String product_ID;
    private String product_name;
    private double product_price;
    private int product_available_quantity;
    private Date product_manufacture_date;
    private Date product_expiry_date;

    private DefaultTableModel model;


// Search method
    public void searchProduct(String name){
        ProductDAM search=new ProductDAM();
        search.searchProduct(name, model);
    }


    public void generateStockReport() {
        ProductDAM stockReport=new ProductDAM();
        stockReport.generateStockReport();
    }




    // Central Panel for search
    public JPanel settingUpProductSearchPanel() {
        // Centre panel for searching product
        JPanel centerPanel;
        JPanel searchFieldPanel;
        // Declaring search field, button and border
        JButton searchButton;
        JTextField searchField;
        CompoundBorder border;
        JTable searchTable;

        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        // Creating new panel to add text field and search button
        searchFieldPanel = new JPanel(new FlowLayout());
        searchButton = (new ProductsTable().createButton("Search"));
        searchButton.setPreferredSize(new Dimension(100, 30));
        model= new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        searchTable = new JTable(model);
        searchTable.setFocusable(false);

        searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(150, 30));
        searchFieldPanel.add(searchField, FlowLayout.LEFT);
        searchFieldPanel.add(searchButton);

        //Table to show result


        // Left and right Border
        border = BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.BLACK),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Inner padding
        centerPanel.setBorder(border);

        // Adding search field panel to centre
        centerPanel.add(searchFieldPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(searchTable));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProduct(searchField.getText());

            }
        });
        return centerPanel;
    }


    //Getter Setter
    public String getProduct_ID() {
        return product_ID;
    }

    public double getProduct_price() {
        return product_price;
    }


    //Get expiry date of a product
    public Date getExpiryDate(String product_name){
        return product_expiry_date;
    }

}
