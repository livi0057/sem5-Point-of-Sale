package se.kth.iv1350.pointofsale.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import se.kth.iv1350.pointofsale.model.*;
import se.kth.iv1350.pointofsale.integration.*;

public class ControllerTest {
    private Controller instance;
    private SystemCreator systemCreator;
    private Sale sale;
    private InventorySystem inventorySystem;
    
    @BeforeEach
    public void setUp() {
        systemCreator = new SystemCreator();
        inventorySystem = systemCreator.getInventorySystem();
        Printer printer = new Printer();
        instance = new Controller(systemCreator, printer);
        sale = new Sale();
    }
    
    @AfterEach
    public void tearDown() {
        instance = null;
        systemCreator = null;
        inventorySystem = null;
        sale = null;
    }
    
    @Test
    public void testEnterItem() {
        instance.startNewSale();
        ItemDTO result = inventorySystem.getItemInfo(1234, 1);
        int resultItemID = result.getItemID();
        Amount resultPrice = result.getPrice();
        int resultQuantity = result.getQuantity();
        int resultVat = result.getVatRate();
        String resultItemDescription = result.getItemDescription();
        
        int expItemID = 1234;
        Amount expPrice = new Amount(20);
        int expQuantity = 1;
        int expVat = 25;
        String expItemDescription = "Pasta";
        
        assertEquals(resultItemID, expItemID, "Wrong item ID.");
        assertEquals(resultPrice, expPrice, "Wrong item price.");
        assertEquals(resultQuantity, expQuantity, "Wrong item quantity.");
        assertEquals(resultVat, expVat, "Wrong item VAT rate.");
        assertEquals(resultItemDescription, expItemDescription, "Wrong item ID.");
    }
    
    @Test
    public void testEnterNonExcistingItem() {
        instance.startNewSale();
        ItemDTO result = inventorySystem.getItemInfo(7867, 1);
        assertNull(result, "An item that does not exist in the inventory system was registered.");
    }
    
    @Test
    public void testEndSale() {
        instance.startNewSale();
        ItemDTO saleInfo = inventorySystem.getItemInfo(1234, 1);
        sale.registerItem(saleInfo);
        Amount result = sale.getTotalInclVat();
        Amount expResult = new Amount(25);
        assertEquals(result, expResult, "The correct total price was not shown.");
    }
    
    @Test
    public void testEnterPaidAmount() {
        instance.startNewSale();
        ItemDTO saleInfo = inventorySystem.getItemInfo(1234, 1);
        sale.registerItem(saleInfo);
        Amount paidAmount = new Amount(40);
        CashPayment payment = new CashPayment(paidAmount);
        Amount result = sale.completeSale(payment);
        Amount expResult = new Amount(15);
        assertEquals(result, expResult, "Wrong change amount.");
    }
}
