import javax.swing.table.DefaultTableModel;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class Stock {
    StockDAM stockItems = new StockDAM();
    private ArrayList<Product> stock_products;

    //Add product to Stock
    public void addProductToStock(String name, String category, String expiryDate, int quantity, double price) {
        stockItems.addProduct(name, category, expiryDate, quantity, price);
    }

    //delete product from stock
    public void deleteProductFromStock(String name, String category) {
        stockItems.delete(name, category);
    }

    //update a product
    public void updateProduct(String name, String category, String expiryDate, int quantity, double price) {
        stockItems.update(name, category, expiryDate, quantity, price);
    }

    public void showData(DefaultTableModel tableModel, String type) {
        stockItems.readAndInsertData(tableModel, type);
    }

    //stock limit setting
    public void stockLimitSetting() {
        //set stock limit setting
    }

    //export to CSV

    public void exportToCSV() {
        Connection connection = null;
        BufferedWriter fileWriter = null;

        try {
            // Create a connection
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/pharmacy";
            String username = "root";
            String password = "newpassword";

            connection = DriverManager.getConnection(url, username, password);
            String selectQuery = "SELECT * FROM stock";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            fileWriter = new BufferedWriter(new FileWriter("D:\\Data Backup.csv"));

            // Write header line containing column names
            fileWriter.write("product_id,name,quantity,expiry_date,category,price");
            fileWriter.newLine();


            //fetching data from database
            while (resultSet.next()) {
                int id = resultSet.getInt("product_id");
                String name = resultSet.getString("name");
                int quantity = resultSet.getInt("quantity");
                Date expiryDate = resultSet.getDate("expiry_date");
                String category = resultSet.getString("category");
                double price = resultSet.getDouble("price");

                //adding data to csv
                String line = String.format("%d,\"%s\",%d,%tF,\"%s\",%.2f", id, name, quantity, expiryDate, category, price);

                fileWriter.write(line);
                fileWriter.newLine();
            }

            //System.out.println("Data exported successfully.");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
