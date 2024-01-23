import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductDAM {


    public void addProduct(String name, int orderID, int quantity) {
        try{
            String url = "jdbc:mysql://localhost:3306/pharmacy";
            String username = "root";
            String password = "newpassword";
            Connection connection = DriverManager.getConnection(url, username, password);
            String query = "INSERT INTO products (name, quantity, order_id) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setInt(2, quantity);
            statement.setInt(3, orderID);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product added successfully.");
            } else {
                System.out.println("Failed to add product.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void generateStockReport() {

        Connection connection = null;

        int stockCount = 0;
        int reorderQuantity = 50;
        int belowLimitStock = 0;
        double inventoryValuation = 0;
        ArrayList<String> expiredProducts = new ArrayList<>(10);


        try {
            // create a connection
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/pharmacy";
            String username = "root";
            String password = "newpassword";

            connection = DriverManager.getConnection(url, username, password);
            String selectQuery = "SELECT * FROM stock";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                stockCount++;
                inventoryValuation += resultSet.getDouble("price");
                if (resultSet.getInt("quantity") < 10) {
                    belowLimitStock++;
                }
                if ((resultSet.getDate("expiry_date").compareTo(new java.util.Date())) < 0) {
                    expiredProducts.add("Product Name: " + resultSet.getString("name") + ", Category: " + resultSet.getString("category"));
                }
            }


            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);

            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            Path fontPath = Paths.get("fonts", "Calibri.ttf");
            PDType0Font font = PDType0Font.load(document, fontPath.toFile());
            int fontSize = 12;
            contentStream.setFont(font, fontSize);
            float lineHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

            float startX = 25;
            float startY = 800;

            contentStream.beginText();
            contentStream.newLineAtOffset(startX, startY);

            contentStream.showText("Total no of products: " + stockCount);
            contentStream.newLine();
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Products below stock limit: " + belowLimitStock);
            contentStream.newLine();
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Reorder Quantity: " + reorderQuantity);
            contentStream.newLine();
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Inventory Valuation: " + Double.parseDouble(new DecimalFormat("#.##").format(inventoryValuation)));
            contentStream.newLine();

            if (expiredProducts.size() == 0) {
                contentStream.newLineAtOffset(0, 15);
                contentStream.showText("Expired products: N/A");
                contentStream.newLine();
            } else {
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Expired products: " + expiredProducts.size());
            }

            for (String expiredProduct : expiredProducts) {
                contentStream.newLineAtOffset(0, -15);
                contentStream.newLine();
                contentStream.showText(expiredProduct);
            }


            contentStream.endText();
            contentStream.close();

            document.save("Stock report.pdf");
            document.close();


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //Search Product
    public void searchProduct(String productName, DefaultTableModel tableModel) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/pharmacy";
            String username = "root";
            String password = "newpassword";
            connection = DriverManager.getConnection(url, username, password);

            // Create the SQL query
            String searchQuery = "SELECT name, price, quantity, expiry_date FROM stock WHERE name LIKE ?";

            // Clear the existing table data
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);

            // Prepare the SQL statement
            statement = connection.prepareStatement(searchQuery);
            statement.setString(1, "%" + productName + "%");

            // Add the column labels to the table model
            tableModel.addColumn("Name");
            tableModel.addColumn("Expiry Date");
            tableModel.addColumn("Quantity");
            tableModel.addColumn("Price");

            // Execute the query
            resultSet = statement.executeQuery();

            // Iterate through the result set
            while (resultSet.next()) {
                // Retrieve the product information from the result set
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                String expiry = resultSet.getString("expiry_date");

                // Add the data to the table model
                tableModel.addRow(new Object[]{name, expiry, quantity, price});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }




}
