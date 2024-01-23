import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class OrderDAM{

    public void deleteOrder(int orderId) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/pharmacy";
            String username = "root";
            String password = "newpassword";
            connection = DriverManager.getConnection(url, username, password);

            // Disable auto-commit to perform a transaction
            connection.setAutoCommit(false);

            // Create the SQL query to delete from the products table
            String deleteProductsQuery = "DELETE FROM products WHERE order_id = ?";

            // Prepare the SQL statement for products deletion
            statement = connection.prepareStatement(deleteProductsQuery);
            statement.setInt(1, orderId);

            // Execute the products deletion query
            statement.executeUpdate();

            // Create the SQL query to delete from the orders table
            String deleteOrderQuery = "DELETE FROM orders WHERE order_id = ?";

            // Prepare the SQL statement for orders deletion
            statement = connection.prepareStatement(deleteOrderQuery);
            statement.setInt(1, orderId);

            // Execute the orders deletion query
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Order deleted successfully.");
                // Commit the transaction if both deletions were successful
                connection.commit();
            } else {
                System.out.println("No order found with the specified ID.");
                // Rollback the transaction if no order was found
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                // Rollback the transaction if an exception occurred
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            // Close the database resources and enable auto-commit
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    public void showOrders(DefaultTableModel tableModel) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/pharmacy";
            String username = "root";
            String password = "newpassword";
            Connection connection = DriverManager.getConnection(url, username, password);
            tableModel.setColumnCount(0);
            tableModel.setRowCount(0);
            String query = "SELECT * FROM orders";
            tableModel.addColumn("Order No.");
            tableModel.addColumn("Delivery Date");
            tableModel.addColumn("Arrival Status");


            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int orderID = resultSet.getInt("order_id");
                java.sql.Date arrivalDate = resultSet.getDate("delivery_date");
                boolean hasArrived = resultSet.getBoolean("has_arrived");

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = dateFormat.format(arrivalDate);

                tableModel.addRow(new Object[]{orderID, formattedDate, hasArrived});
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    public void showOrderDetails(int orderId, DefaultTableModel tableModel) {
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
            String searchQuery = "SELECT product_id, name, quantity FROM products WHERE order_id = ?";

            // Prepare the SQL statement
            statement = connection.prepareStatement(searchQuery);
            statement.setInt(1, orderId);

            // Execute the query
            resultSet = statement.executeQuery();

            // Clear the existing table data
            tableModel.setRowCount(0);
            // Add the columns to the table model
            tableModel.addColumn("Product ID");
            tableModel.addColumn("Name");
            tableModel.addColumn("Quantity");
            // Iterate through the result set
            while (resultSet.next()) {
                // Retrieve the product details from the result set
                int productId = resultSet.getInt("product_id");
                String name = resultSet.getString("name");
                int quantity = resultSet.getInt("quantity");

                // Add the data to the table model
                tableModel.addRow(new Object[]{productId, name, quantity});
                System.out.print("ID: "+productId);
                System.out.println("Name: "+name);
                System.out.println("Quantity: "+quantity);
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

    public void placeOrder(int orderID, String delivery_date, boolean hasArrived) {
        try {
            String url = "jdbc:mysql://localhost:3306/pharmacy";
            String username = "root";
            String password = "newpassword";
            Connection connection = DriverManager.getConnection(url, username, password);
            String query = "INSERT INTO orders (order_id, delivery_date, has_arrived) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, orderID);
            statement.setString(2, delivery_date);
            statement.setBoolean(3, hasArrived);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Order added successfully.");
            } else {
                System.out.println("Failed to add order.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void markOrderAsArrived(int orderId) {
        try{
            String url = "jdbc:mysql://localhost:3306/pharmacy";
            String username = "root";
            String password = "newpassword";
            Connection connection = DriverManager.getConnection(url, username, password);
            String updateQuery = "UPDATE orders SET has_arrived = true WHERE order_id = ?";
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setInt(1, orderId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Order marked as arrived successfully.");
            } else {
                System.out.println("Failed to mark the order as arrived.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}