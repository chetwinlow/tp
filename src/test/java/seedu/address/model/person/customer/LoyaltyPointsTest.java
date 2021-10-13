package seedu.address.model.person.customer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class LoyaltyPointsTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new LoyaltyPoints(null));
    }

    @Test
    public void constructor_invalidLoyaltyPoints_throwsIllegalArgumentException() {
        String invalidLoyaltyPoints = "";
        assertThrows(IllegalArgumentException.class, () -> new LoyaltyPoints(invalidLoyaltyPoints));
    }

    @Test
    public void isValidLoyaltyPoints() {
        // null LoyaltyPoints number
        assertThrows(NullPointerException.class, () -> LoyaltyPoints.isValidLoyaltyPoints(null));

        // invalid LoyaltyPoints numbers
        assertFalse(LoyaltyPoints.isValidLoyaltyPoints("")); // empty string
        assertFalse(LoyaltyPoints.isValidLoyaltyPoints(" ")); // spaces only
        assertFalse(LoyaltyPoints.isValidLoyaltyPoints("911")); // less than 3 numbers
        assertFalse(LoyaltyPoints.isValidLoyaltyPoints("LoyaltyPoints")); // non-numeric
        assertFalse(LoyaltyPoints.isValidLoyaltyPoints("9011p041")); // alphabets within digits
        assertFalse(LoyaltyPoints.isValidLoyaltyPoints("9312 1534")); // spaces within digits

        // valid LoyaltyPoints numbers
        assertTrue(LoyaltyPoints.isValidLoyaltyPoints("9110")); // exactly 3 numbers
        assertTrue(LoyaltyPoints.isValidLoyaltyPoints("10000"));
        assertTrue(LoyaltyPoints.isValidLoyaltyPoints("124293842033123")); // long phone numbers
    }
}