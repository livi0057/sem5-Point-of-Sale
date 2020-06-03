package se.kth.iv1350.pointofsale.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.kth.iv1350.pointofsale.integration.Printer;

import java.time.LocalTime;


public class SaleTest {
    private Sale sale;
    private Printer printer;
    private ItemDTO pasta;
    private ItemDTO milk;
    private LocalTime saleTime;
    
    @BeforeEach
    public void setUp() {
        sale = new Sale();
        Amount pricePasta = new Amount(20);
        pasta = new ItemDTO(1234, pricePasta, 1, 25, "Pasta");
        Amount priceMilk = new Amount(10);
        milk = new ItemDTO(4321, priceMilk, 1, 6, "Milk");
    }
    
    @AfterEach
    public void tearDown() {
        sale = null;
        pasta = null;
        milk = null;
    }


    @Test
    public void testRegisterAndFindItem() {
        sale.registerItem(pasta);
        sale.registerItem(pasta);
        
        ItemDTO result = sale.findItem(pasta.getItemID());
        assertEquals(pasta.toString(), result.toString(), "test");
        assertEquals(pasta.getItemDescription(), result.getItemDescription(), "The item found has the wrong item description.");
        assertEquals(pasta.getItemID(), result.getItemID(), "The item found has the wrong item ID.");
        assertEquals(pasta.getPrice(), result.getPrice(), "The item found has the wrong item price.");
        assertEquals(pasta.getQuantity(), result.getQuantity(), "The item found has the wrong item quantity.");
        assertEquals(pasta.getVatRate(), result.getVatRate(), "The item found has the wrong item VAT rate.");
    }

    
    @Test
    public void testUpdateQuantity() {
        sale.registerItem(pasta);
        sale.updateQuantity(pasta, 2);
        ItemDTO foundItem = sale.findItem(pasta.getItemID());
        int result = foundItem.getQuantity();
        int expResult = 3;
        assertEquals(expResult,result, "The given items quantity was not updated correctly.");
    }
    

   @Test
    public void testGetTotalInclVat() {
        sale.registerItem(pasta);
        sale.registerItem(milk);
        
        Amount expResult = new Amount(35.6);
        Amount result = sale.getTotalInclVat();
        assertEquals(expResult, result, "Incorrect total incl. VAT for the sale.");
    }


    @Test
    public void testGetVatForEntireSale() {
        sale.registerItem(pasta);
        sale.registerItem(milk);
        
        Amount expResult = new Amount(5.6);
        Amount result = sale.getVatForEntireSale();
        assertEquals(expResult, result, "The correct total VAT was not given for entire sale.");
    }


    @Test
    public void testCompleteSale() {
        sale.registerItem(pasta);
        sale.registerItem(milk);
        Amount pay = new Amount(40);
        CashPayment payment = new CashPayment(pay);
        
        Amount result = sale.completeSale(payment);
        double d = Double.parseDouble(result.toString());
        Amount resultRounded = result.oneDecimal(d);
        Amount expResult = new Amount(4.4);

        assertEquals(expResult, resultRounded, "The correct change was not calulated for the sale.");
    }


    @Test
    public void testGetSaleInfo() {
        sale.registerItem(pasta);
        sale.registerItem(milk);
        
        SaleDTO result = sale.getSaleInfo();
        Amount resultTotalVat = result.getTotalVat();
        Amount resultTotalInclVat = result.getTotalPrice();
        
        Amount expTotalInclVat = sale.getTotalInclVat();
        Amount expTotalVat = sale.getVatForEntireSale();
        
        assertEquals(expTotalInclVat, resultTotalInclVat, "Wrong total incl. VAT.");
        assertEquals(expTotalVat, resultTotalVat, "Wrong VAT.");
    }

    
   @Test
    public void testPrintReceipt() {
        sale.registerItem(pasta);
        sale.registerItem(milk);
        Amount pay = new Amount(40);
        CashPayment payment = new CashPayment(pay);
        Amount change = sale.completeSale(payment);
        SaleDTO saleInfo = sale.getSaleInfo();
    
        Receipt receipt = new Receipt(saleTime, saleInfo, payment, change);
        String result = receipt.receiptToString();
        String expTotalInclVat = saleInfo.getTotalPrice().toString();
        String expTotalVat = saleInfo.getTotalVat().toString();
        String expItemDescriptionPasta = pasta.getItemDescription();
        String expItemDescriptionMilk = milk.getItemDescription();
        
        assertTrue(result.contains(expTotalInclVat), "Receipt contains the wrong total incl. VAT.");
        assertTrue(result.contains(expTotalVat), "Receipt contains the wrong total VAT.");
        assertTrue(result.contains(expItemDescriptionPasta), "Receipt contains the wrong item description.");
        assertTrue(result.contains(expItemDescriptionMilk), "Receipt contains the wrong item description.");
    }
    
}
