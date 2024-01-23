import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

public class Order {
    private String stocks;
    private String orderId;
    private boolean orderStatus;

    public Order() {
        stocks = null;
        orderId = null;
        orderStatus = false;
    }

    public Order(String stocks, String orderId, boolean orderStatus) {
        this.stocks = stocks;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }

    public String getStocks() {
        return stocks;
    }

    public void setStocks(String stocks) {
        this.stocks = stocks;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public boolean checkOrderStatus() {
        return this.orderStatus;
    }

    public void setOrderStatus(int orderID) {
        OrderDAM setOrder=new OrderDAM();
        setOrder.markOrderAsArrived(orderID);
    }
    public void placeOrder(int orderID, String arrivalDate, boolean hasArrived)
    {
        OrderDAM order=new OrderDAM();
        order.placeOrder(orderID,arrivalDate,hasArrived);
    }



    public void generatePurchaseReport()
    {
        Connection connection = null;
        int arrivedOrders=0;
        ArrayList<String> orders = new ArrayList<>(1);

        try {
            // create a connection
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/pharmacy";
            String username = "root";
            String password = "newpassword";

            connection = DriverManager.getConnection(url, username, password);
            String selectQuery = "SELECT * FROM orders";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

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

            while (resultSet.next()) {
                orders.add("Order ID: "+resultSet.getInt("order_id")+" , "+"Arrival Status: "+resultSet.getBoolean("has_arrived")+" , "+"Delivery Date: "+ resultSet.getDate("delivery_date"));
                if(resultSet.getBoolean("has_arrived"))
                {
                    arrivedOrders++;
                }
            }

            contentStream.beginText();
            contentStream.newLineAtOffset(startX, startY);

            contentStream.showText("No: of orders: "+orders.size());
            contentStream.newLine();
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("No of orders arrived: "+arrivedOrders);
            contentStream.newLine();

            for (String order : orders) {
                contentStream.newLineAtOffset(0, -15);
                contentStream.newLine();
                contentStream.showText(order);
            }


            contentStream.endText();
            contentStream.close();

            document.save("Purchase Report.pdf");
            document.close();


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
}
