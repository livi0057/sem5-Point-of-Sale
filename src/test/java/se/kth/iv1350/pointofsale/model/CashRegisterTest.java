package se.kth.iv1350.pointofsale.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CashRegisterTest {
    private CashRegister cashRegister;
    private Amount amount;
    private CashPayment payment;
    
    @BeforeEach
    public void setUp() {
        cashRegister = new CashRegister();
        amount = new Amount(100);
        payment = new CashPayment(amount);
    }
    
    @AfterEach
    public void tearDown() {
        amount = null;
        payment = null;
    }

    @Test
    public void testIncreaseBalance() {
        cashRegister.increaseBalance(payment);
    }

  
   @Test
    public void testUpdateBalance() {
        cashRegister.updateBalance(amount);
    }
    
}
