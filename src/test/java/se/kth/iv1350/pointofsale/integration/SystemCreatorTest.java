package se.kth.iv1350.pointofsale.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class SystemCreatorTest {
    private SystemCreator systemCreator;
    private InventorySystem inventorySystem;
    private AccountingSystem accountingSystem;
    
    @BeforeEach
    public void setUp() {
        systemCreator = new SystemCreator();
    }
    
    @AfterEach
    public void tearDown() {
        systemCreator = null;
    }

    @Test
    public void testGetInventorySystem() {
        inventorySystem = systemCreator.getInventorySystem();
        assertTrue(inventorySystem instanceof InventorySystem, "The inventory system was not created as expected.");
    }


    @Test
    public void testGetAccountingSystem() {
        accountingSystem = systemCreator.getAccountingSystem();
        assertTrue(accountingSystem instanceof AccountingSystem, "The accounting system was not created as expected.");
    }
    
}
