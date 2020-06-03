package se.kth.iv1350.pointofsale.integration;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.kth.iv1350.pointofsale.model.SaleDTO;
import se.kth.iv1350.pointofsale.model.ItemDTO;
import se.kth.iv1350.pointofsale.model.Sale;
import se.kth.iv1350.pointofsale.model.Amount;
import java.util.List;


public class AccountingSystemTest {
    private SaleDTO currentSale;
    private AccountingSystem accountingSystem;
    private List<ItemDTO> itemDescriptions;
    private Sale sale;

    
    @Test
    public void testUpdateAccounting() {
        sale = new Sale();
        itemDescriptions = new ArrayList<>();
        accountingSystem = new AccountingSystem();
        
        itemDescriptions.add(new ItemDTO(1234, new Amount(20), 1, 25, "Pasta"));
        itemDescriptions.add(new ItemDTO(4321, new Amount(10), 1, 6, "Milk"));
        Amount totalInclVat = sale.getTotalInclVat();
        Amount totalVat = sale.getVatForEntireSale();
        currentSale = new SaleDTO(itemDescriptions, totalInclVat, totalVat);
        
        accountingSystem.updateAccounting(currentSale);
    }
    
}
