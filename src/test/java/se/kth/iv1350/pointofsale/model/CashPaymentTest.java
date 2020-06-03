package se.kth.iv1350.pointofsale.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class CashPaymentTest {
    private CashPayment cashPaymentNoArg;
    private CashPayment cashPaymentWithFive;
    private CashPayment cashPaymentWithThree;
    private final Amount FIVE = new Amount(5);

    @BeforeEach
    public void setUp() {
        cashPaymentNoArg = new CashPayment();
        cashPaymentWithFive = new CashPayment(FIVE);
    }
  
    @AfterEach
    public void tearDown() {
        cashPaymentNoArg = null;
        cashPaymentWithFive = null;
    }

    @Test
    public void testNotEqualsNull() {
        Object other = null;
        boolean expResult = false;
        boolean result = cashPaymentNoArg.equals(other);
        assertEquals(expResult, result, "CashPayment instance equal to null.");
    }
    
    @Test
    public void testNotEqualsJavaLangObject() {
        Object other = new Object();
        boolean expResult = false;
        boolean result = cashPaymentNoArg.equals(other);
        assertEquals(expResult, result, "CashPayment instance equal to java.lang.Object instance.");
    }
    
    @Test
    public void testNotEqualsUsingNoArg() {
        boolean expResult = false;
        boolean result = cashPaymentNoArg.equals(cashPaymentWithFive);
        assertEquals(expResult, result, "CashPayment instance with different states are equal.");
    }
    
    @Test
    public void testNotEqual() {
        Amount otherNumber = new Amount(3);
        CashPayment other = new CashPayment(otherNumber);
        boolean expResult = false;
        boolean result = cashPaymentWithFive.equals(other);
        assertEquals(expResult, result, "CashPayment instance with different states are equal.");
    }
    
    @Test
    public void testEqual() {
        Amount otherNumber = new Amount(5);
        boolean expResult = true;
        boolean result = otherNumber.equals(cashPaymentWithFive.toAmount());
        assertEquals(expResult, result, "CashPayment instance with same states are not equal.");
    }
    
    @Test
    public void testToAmount() {
        Amount expResult = new Amount(5);
        Amount result = cashPaymentWithFive.toAmount();
        assertEquals(expResult, result, "Not correct conversion from CashPayment to Amount.");
    }
}
