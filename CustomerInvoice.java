import java.util.ArrayList;

public class CustomerInvoice {
    private ArrayList<Product> products;

    public CustomerInvoice()
    {
        //initialization with ten is just random and can be changed when needed
        products = new ArrayList<>(1);
    }

    public double calculateBills(Product[] products)
    {
        Double count=0.0;
        for(Product i: products)
        {
            count += i.getProduct_price();
            count += calculateTax(i);
        }

        return count;
    }

    public double calculateTax(Product product)
    {
        if(product.getProduct_price()>500)
        {
            return 75;
        }
        else if (product.getProduct_price()>100)
        {
            return 12;
        }
        else if (product.getProduct_price()>50)
        {
            return 5;
        }
        else if (product.getProduct_price()>20)
        {
            return 1.5;
        }
        return 0.0;
    }

    public void addProduct(Product p)
    {
        this.products.add(p);
    }
}
