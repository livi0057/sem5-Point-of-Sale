package se.kth.iv1350.pointofsale.integration;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.kth.iv1350.pointofsale.model.Amount;
import se.kth.iv1350.pointofsale.model.ItemDTO;
import se.kth.iv1350.pointofsale.model.Sale;
import se.kth.iv1350.pointofsale.model.SaleDTO;


public class InventorySystemTest {
    private InventorySystem inventorySystem;
    private SaleDTO currentSale;
    private List<ItemDTO> itemDescriptions;
    private Sale sale;
    
    @BeforeEach
    public void setUp() {
        inventorySystem = new InventorySystem();
    }
    
    @AfterEach
    public void tearDown() {
        inventorySystem = null;
    }

    @Test
    public void testGetItemInfo() {
        String expResult = new ItemDTO(1234, new Amount(20), 1, 25, "Pasta").toString();
        String result = inventorySystem.getItemInfo(1234, 1).toString();
        
        assertEquals(expResult, result, "Wrong item info collected from the inventory system.");
    }

    @Test
    public void testUpdateInventory() {
        sale = new Sale();
        itemDescriptions = new ArrayList<>();
        
        itemDescriptions.add(new ItemDTO(1234, new Amount(20), 1, 25, "Pasta"));
        itemDescriptions.add(new ItemDTO(4321, new Amount(10), 1, 6, "Milk"));
        Amount totalInclVat = sale.getTotalInclVat();
        Amount totalVat = sale.getVatForEntireSale();
        currentSale = new SaleDTO(itemDescriptions, totalInclVat, totalVat);
        
        inventorySystem.updateInventory(currentSale);
    }
    
}
