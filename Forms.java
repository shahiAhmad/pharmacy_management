import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Forms{
    JFrame orderForm=new JFrame("Order Form");
    JFrame productForm=new JFrame("Product Form");

    JFrame orderRecordWindow=new JFrame();
    private JButton placeOrderButton;
    private JTextField orderIdField;
    private JTextField numberOfProductsField;
    private JTextField deliveryDateField;

    public void OrderForm() {


        orderForm.setSize(300, 300);
        orderForm.setLayout(null);

        JLabel orderIdLabel = new JLabel("Order ID:");
        orderIdLabel.setBounds(20, 30, 100, 25);
        orderIdField = new JTextField();
        orderIdField.setBounds(120, 30, 150, 25);

        JLabel deliveryDateLabel = new JLabel("Delivery Date:");
        deliveryDateLabel.setBounds(20, 70, 100, 25);
        deliveryDateField = new JTextField();
        deliveryDateField.setBounds(120, 70, 150, 25);

        JLabel formatLabel = new JLabel("YYYY-MM-DD");
        formatLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        formatLabel.setBounds(20, 90, 100, 25);

        JLabel numberOfProductsLabel = new JLabel("Products:");
        numberOfProductsLabel.setBounds(20, 120, 100, 25);
        numberOfProductsField = new JTextField();
        numberOfProductsField.setBounds(120, 120, 150, 25);

        placeOrderButton = new JButton("Place Order");
        placeOrderButton.setBounds(80, 170, 140, 30);
        placeOrderButton.setFocusable(false);

        orderForm.add(orderIdLabel);
        orderForm.add(orderIdField);
        orderForm.add(deliveryDateLabel);
        orderForm.add(deliveryDateField);
        orderForm.add(formatLabel);
        orderForm.add(numberOfProductsLabel);
        orderForm.add(numberOfProductsField);
        orderForm.add(placeOrderButton);
        orderForm.setVisible(true);
        getPlaceOrderButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int orderId = Integer.parseInt(getOrderIdField());
                String deliveryDate = getDeliveryDateField();
                boolean hasArrived = false;
                int productCount= Integer.parseInt(getNumberOfProductsField());
                orderForm.setVisible(false);
                for (int i=0; i<productCount; i++){
                    OrderProductsForm(orderId);
                }
                Order newOrder = new Order();
                newOrder.placeOrder(orderId, deliveryDate, hasArrived);
            }
        });
        // Center the frame on the screen
        orderForm.setLocationRelativeTo(null);
    }


    public JButton getPlaceOrderButton() {
        return placeOrderButton;
    }

    public String getOrderIdField() {
        return orderIdField.getText();
    }

    public String getDeliveryDateField() {
        return deliveryDateField.getText();
    }

    public String getNumberOfProductsField() {
        return numberOfProductsField.getText();
    }


    private JTextField productNameField, priceField, expField, quantityField;

    public void setProductNameField(String productNameField) {
        this.productNameField.setText(productNameField);
    }

    public void setPriceField(double priceField) {
        this.priceField.setText(String.valueOf(priceField));
    }

    public void setExpField(String expDate) {
        this.expField.setText(expDate);
    }

    public void setQuantityField(int quantityField) {
        this.quantityField.setText(String.valueOf(quantityField));
    }

    public void setCategory(String category) {
        if(category.equals("Tablet"))
            this.category.setSelectedIndex(0);
        if(category.equals("Syrup"))
            this.category.setSelectedIndex(1);
        if(category.equals("Cosmetic"))
            this.category.setSelectedIndex(2);
        if(category.equals("Injection"))
            this.category.setSelectedIndex(3);
        if(category.equals("Drug"))
            this.category.setSelectedIndex(4);
    }

    public JButton addButton;
    private String[] categories;


    private JComboBox category;

    public void ProductForm() {
        productForm.setSize(400, 400);
        productForm.setLayout(null);

        JLabel productNameLabel = new JLabel("Product Name:");
        productNameLabel.setBounds(20, 20, 100, 25);
        productNameField = new JTextField();
        productNameField.setBounds(120, 20, 250, 25);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setBounds(20, 60, 100, 25);
        priceField = new JTextField();
        priceField.setBounds(120, 60, 250, 25);

        JLabel expLabel = new JLabel("Expiry Date:");
        expLabel.setBounds(20, 100, 100, 25);
        JLabel formatLabel = new JLabel("YYYY-MM-DD");
        formatLabel.setFont(new Font("Ariel",Font.PLAIN, 10));
        formatLabel.setBounds(20, 115, 100, 25);
        expField = new JTextField();
        expField.setBounds(120, 100, 250, 25);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(20, 140, 100, 25);
        quantityField = new JTextField();
        quantityField.setBounds(120, 140, 250, 25);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setBounds(20, 180, 100, 25);
        categories = new String[]{"Tablet", "Syrup", "Cosmetic", "Injection", "Drug"};
        category = new JComboBox<>(categories);
        category.setBounds(120, 180, 250, 25);

        addButton = new JButton("Add");
        addButton.setBounds(150, 240, 100, 30);
        addButton.setFocusable(false);

        productForm.add(productNameLabel);
        productForm.add(productNameField);
        productForm.add(priceLabel);
        productForm.add(priceField);
        productForm.add(expLabel);
        productForm.add(expField);
        productForm.add(formatLabel);
        productForm.add(quantityLabel);
        productForm.add(quantityField);
        productForm.add(categoryLabel);
        productForm.add(category);
        productForm.add(addButton);
        productForm.setVisible(true);

        // Center the frame on the screen
        productForm.setLocationRelativeTo(null);
    }

    public String getProductNameField() {
        return productNameField.getText();
    }
    public void makeProductNameFieldFinal(){
        productNameField.setEditable(false);
    }

    public double getPriceField() {
        return Double.parseDouble(priceField.getText());
    }

    public String getExpField() {
        return expField.getText();
    }

    public int getQuantityField() {
        return Integer.parseInt(quantityField.getText());
    }

    public String getCategory() {
        System.out.println(categories[category.getSelectedIndex()]);
        return (String) category.getSelectedItem();
    }

    public void OrderRecordWindow(int orderID) {
        orderRecordWindow.setTitle("Order Record");
        orderRecordWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        orderRecordWindow.setPreferredSize(new Dimension(400, 300));

        JPanel mainPanel = new JPanel(new BorderLayout());
        JTable productsTable = new JTable();

        // Disable editing of table fields
        productsTable.setDefaultEditor(Object.class, null);

        // Fetch and populate the table model with data
        DefaultTableModel tableModel = new DefaultTableModel();
        OrderDAM orders=new OrderDAM();
        orders.showOrderDetails(orderID, tableModel);

        // Set the table model to the JTable
        productsTable.setModel(tableModel);

        JScrollPane scrollPane = new JScrollPane(productsTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        orderRecordWindow.setContentPane(mainPanel);
        orderRecordWindow.pack();
        orderRecordWindow.setLocationRelativeTo(null); // Center the frame on the screen
        orderRecordWindow.setVisible(true);
    }

    public void OrderProductsForm(int order) {
        JFrame orderProductsForm=new JFrame();
        JTextField productNameField, orderIDField, quantityField;
        JButton nextButton;
        orderProductsForm.setTitle("Product Form");
        orderProductsForm.setSize(400, 300);
        orderProductsForm.setLayout(null);

        JLabel orderIDLabel = new JLabel("Order ID:");
        orderIDLabel.setBounds(20, 20, 100, 25);

        orderIDField = new JTextField();
        orderIDField.setBounds(120, 20, 250, 25);
        orderIDField.setText(String.valueOf(order));
        orderIDField.setEditable(false);

        JLabel productNameLabel = new JLabel("Product Name:");
        productNameLabel.setBounds(20, 60, 100, 25);

        productNameField = new JTextField();
        productNameField.setBounds(120, 60, 250, 25);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(20, 100, 100, 25);

        quantityField = new JTextField();
        quantityField.setBounds(120, 100, 250, 25);

        nextButton = new JButton("Next");
        nextButton.setBounds(150, 200, 100, 30);
        nextButton.setFocusable(false);
        nextButton.addActionListener(e -> {
            String productName = productNameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            ProductDAM orderProducts=new ProductDAM();
            orderProducts.addProduct(productName, order, quantity);
            productNameField.setText("");
            quantityField.setText("");
            orderProductsForm.setVisible(false);
        });
        orderProductsForm.add(quantityLabel);
        orderProductsForm.add(productNameField);
        orderProductsForm.add(orderIDField);
        orderProductsForm.add(productNameLabel);
        orderProductsForm.add(orderIDLabel);
        orderProductsForm.add(quantityField);
        orderProductsForm.add(nextButton);
        orderProductsForm.setVisible(true);
        orderProductsForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        orderProductsForm.setLocationRelativeTo(null);
    }
}

