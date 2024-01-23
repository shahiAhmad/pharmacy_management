import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame {

    // Right panel for billing

    ProductsTable leftPanel = new ProductsTable();
    Product centerPanel = new Product();
    ProductBilling rightPanel=new ProductBilling();
    // Constructor
    public Gui() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1500,700)); // Full screen
        setResizable(false);
        setLayout(new BorderLayout());
        setVisible(true);

        // Adding panels to the frame

        // Add left panel to frame
        add(leftPanel.settingUpProductTablePanel(), BorderLayout.WEST);
        add(centerPanel.settingUpProductSearchPanel(), BorderLayout.CENTER);
        add(rightPanel.settingUpBillingPanel(), BorderLayout.EAST);

        // Disable the resizable button
        pack();
    }

    // Right Panel for bill


    public static void main(String[] args) {
        new Gui();


    }
}
