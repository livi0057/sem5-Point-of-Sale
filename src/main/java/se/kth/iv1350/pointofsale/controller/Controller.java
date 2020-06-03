package se.kth.iv1350.pointofsale.controller;

import se.kth.iv1350.pointofsale.model.Sale;
import se.kth.iv1350.pointofsale.model.ItemDTO;
import se.kth.iv1350.pointofsale.model.SaleInfoDTO;
import se.kth.iv1350.pointofsale.model.SaleDTO;
import se.kth.iv1350.pointofsale.model.CashRegister;
import se.kth.iv1350.pointofsale.model.Amount;
import se.kth.iv1350.pointofsale.model.CashPayment;
import se.kth.iv1350.pointofsale.integration.SystemCreator;
import se.kth.iv1350.pointofsale.integration.InventorySystem;
import se.kth.iv1350.pointofsale.integration.AccountingSystem;
import se.kth.iv1350.pointofsale.integration.Printer;

/**
 * This is the application's only controller. All calls to the model pass through this class.
 */
public class Controller {
    private Sale sale;
    private InventorySystem inventorySystem;
    private AccountingSystem accountingSystem;
    private CashRegister cashRegister;
    private Printer printer;
    
    /**
     * Creates a new instance.
     * 
     * @param creator   Used to get all systems that handle database calls.
     * @param printer   Interface to printer.
     */
    public Controller (SystemCreator creator, Printer printer) {
        this.inventorySystem = creator.getInventorySystem();
        this.accountingSystem = creator.getAccountingSystem();
        this.cashRegister = new CashRegister();
        this.printer = printer;
    }
    
    /**
     * Starts a new sale. This method must be called before doing anything else during a sale. 
     */
    public void startNewSale() {
        sale = new Sale();
    }
    
    /**
     * Enters a item in the current sale. If the item to be registered is already 
     * present in the current sale, the items quantity is updated.
     * 
     * @param itemID        This is the item identifier for the scanned item.
     * @param quantity      This is the quantity of the item scanned.
     * @return The entered item's item information.
     */
    public SaleInfoDTO enterItem(int itemID, int quantity) {
        ItemDTO itemAlreadyScanned = sale.findItem(itemID);
        SaleInfoDTO infoToPresent;
        
        if(itemAlreadyScanned != null) {
            infoToPresent = registerExistingItem(itemAlreadyScanned, quantity);
        }
        else {
            infoToPresent = registerNewItem(itemID, quantity);
        }
        return infoToPresent;
    }
    
    /**
     * Updates the quantity of the already scanned item in the current sale.
     * 
     * @param itemAlreadyScanned    This is the item info of the already scanned item.
     * @param quantity              This is the quantity to update the already scanned item with.
     * @return The updated information of the already scanned item.
     */
    private SaleInfoDTO registerExistingItem (ItemDTO itemAlreadyScanned, int quantity) {
        sale.updateQuantity(itemAlreadyScanned, quantity);
        SaleInfoDTO itemInfo = new SaleInfoDTO(itemAlreadyScanned.getItemDescription(), 
                    itemAlreadyScanned.getPrice(), sale.getTotalInclVat(), quantity);
        return itemInfo;
    }
    
    /**
     * Registers a new item to the current sale.
     * 
     * @param itemID        This is the item ID of the scanned item.
     * @param quantity      This is the quantity of the scanned item.
     * @return The information of the new registered item.
     */
    private SaleInfoDTO registerNewItem (int itemID, int quantity) {
        ItemDTO newItemInfo = inventorySystem.getItemInfo(itemID, quantity);
        sale.registerItem(newItemInfo);
        SaleInfoDTO itemInfo = new SaleInfoDTO(newItemInfo.getItemDescription(), 
                    newItemInfo.getPrice(), sale.getTotalInclVat(), quantity);
        return itemInfo;
    }
    
    /**
     * Ends the current sale. No new items can be added after this.
     * 
     * @return The total price including VAT for entire sale.
     */
    public Amount endSale() {
        return sale.getTotalInclVat();
    }
    
    /**
     * Handles sale payment. Updates cash register, inventory and accounting.
     * Calculates change. Prints receipt.
     * 
     * @param paidAmount    The cash payment payed by customer.
     * @return The calculated change to give to customer.
     */
    public Amount enterPaidAmount(Amount paidAmount) {
        CashPayment payment = new CashPayment (paidAmount);
        cashRegister.increaseBalance(payment);
        Amount change = sale.completeSale(payment);
        cashRegister.updateBalance(change);
        
        SaleDTO saleInfo = sale.getSaleInfo();
        inventorySystem.updateInventory(saleInfo);
        accountingSystem.updateAccounting(saleInfo);
        
        sale.printReceipt(printer, payment, change);
        
        return change;
    }
}
