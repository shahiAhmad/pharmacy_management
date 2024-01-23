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

public class StockDAM {
    //dummy arguments
    public void addProduct(String name, String category, String expiryDate, int quantity, double price) {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/pharmacy";
            String username = "root";
            String password = "newpassword";
            connection = DriverManager.getConnection(url, username, password);
            String query = "INSERT INTO stock (name, category, expiry_date, quantity, price) VALUES (?, ?, ?, ?, ?)";

            // Prepare the SQL statement
            PreparedStatement statement = connection.prepareStatement(query);

            // Set the parameter values for the query
            statement.setString(1, name);
            statement.setString(2, category);
            statement.setDate(3, java.sql.Date.valueOf(expiryDate));
            statement.setInt(4, quantity);
            statement.setDouble(5, price);

            // Execute the query and get the number of rows affected
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Data inserted successfully!");
            } else {
                System.out.println("Failed to insert data.");
            }

            System.out.println("Connection established successfully!");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
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

    public void update(String name, String category, String expiryDate, int quantity, double price) {
        String url = "jdbc:mysql://localhost:3306/pharmacy";
        String username = "root";
        String password = "newpassword";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "UPDATE stock SET quantity = ?, price = ? ,category =? ,expiry_date =?, name =?  WHERE name = ?";

            // Prepare the SQL statement
            PreparedStatement statement = connection.prepareStatement(query);

            // Set the parameter values for the query
            statement.setInt(1, quantity); // New quantity value
            statement.setDouble(2, price); // New price value
            statement.setString(3, category);
            statement.setString(4, expiryDate);
            statement.setString(5, name); // new value of name
            statement.setString(6, name); // Name of the item to update

            // Execute the query
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Tuple updated successfully!");
            } else {
                System.out.println("No tuples were updated.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String name, String category) {
        String url = "jdbc:mysql://localhost:3306/pharmacy";
        String username = "root";
        String password = "newpassword";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "DELETE FROM stock WHERE name = ? and category = ?";

            // Prepare the SQL statement
            PreparedStatement statement = connection.prepareStatement(query);

            // Set the parameter value for the query
            statement.setString(1, name); // Name of the item to delete
            statement.setString(2, category);
            // Execute the query
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Tuple deleted successfully!");
            } else {
                System.out.println("No tuples were deleted.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void readAndInsertData(DefaultTableModel tableModel, String type) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/pharmacy";
            String username = "root";
            String password = "newpassword";
            connection = DriverManager.getConnection(url, username, password);

            // Create the SQL query
            String selectQuery = "SELECT * FROM stock";
            if (!type.equals("All")) selectQuery += " where category ='" + type + "'";

            // Clear the existing table data and column names
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);

            // Add the column names to the table model
            tableModel.addColumn("Name");
            tableModel.addColumn("Category");
            tableModel.addColumn("Expiry Date");
            tableModel.addColumn("Quantity");
            tableModel.addColumn("Price");

            // Create the SQL statement
            statement = connection.createStatement();

            // Execute the query
            resultSet = statement.executeQuery(selectQuery);

            // Iterate through the result set
            while (resultSet.next()) {
                // Retrieve the product information from the result set
                String name = resultSet.getString("name");
                String category = resultSet.getString("category");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                String expiry = resultSet.getString("expiry_date");

                // Add the data to the table model with default color
                tableModel.addRow(new Object[]{name, category, expiry, quantity, price});

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            // Close the database resources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


   }
