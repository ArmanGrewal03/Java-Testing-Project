/*
 *  DateTime Manual Test Suite
 *  
 *  Tests for:
 *  - DateTime.withYear(int) [TI-01]
 *  - DateTime.plusDays(int) [TI-02]
 *  
 *  Testing Approaches:
 *  - Input Space Partitioning (ISP)
 *  - Boundary Value Analysis (BVA)
 *  - Immutability verification
 *  - Edge case and exception handling
 */
package org.joda.time;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class DateTimeManualTest {

    private static final DateTimeZone UTC = DateTimeZone.UTC;

    // ========================================================================
    // DateTime.withYear(int) Tests [TI-01]
    // ========================================================================
    // Testing abstraction: ISP with BVA
    // Partitions:
    //  - Normal valid year (non-boundary)
    //  - Leap year causing valid date transition
    //  - Year causing invalid date (Feb 29 on non-leap year)
    //  - Extreme positive year
    //  - Year with day-of-month preservation
    //  - Year with month preservation
    // ========================================================================

    @Test
    public void test_withYear_normalValidYear() {
        // Partition: Normal valid year, non-boundary
        // ISP: valid year input
        DateTime dt = new DateTime(2020, 6, 15, 10, 30, 45, UTC);
        DateTime result = dt.withYear(2021);
        
        assertEquals(2021, result.getYear());
        assertEquals(6, result.getMonthOfYear());
        assertEquals(15, result.getDayOfMonth());
        assertEquals(10, result.getHourOfDay());
        assertEquals(30, result.getMinuteOfHour());
        assertEquals(45, result.getSecondOfMinute());
        
        // Verify immutability: original unchanged
        assertEquals(2020, dt.getYear());
    }

    @Test
    public void test_withYear_sameYear() {
        // Edge case: Setting year to same value
        DateTime dt = new DateTime(2020, 3, 20, 14, 0, 0, UTC);
        DateTime result = dt.withYear(2020);
        
        assertEquals(2020, result.getYear());
        assertEquals(3, result.getMonthOfYear());
        assertEquals(20, result.getDayOfMonth());
    }

    @Test
    public void test_withYear_leapYearToLeapYear() {
        // Partition: Leap year date preservation
        // BVA: Feb 29 on leap year, transitioning between leap years
        DateTime dt = new DateTime(2020, 2, 29, 12, 0, 0, UTC);  // 2020 is leap year
        DateTime result = dt.withYear(2024);  // 2024 is also leap year
        
        assertEquals(2024, result.getYear());
        assertEquals(2, result.getMonthOfYear());
        assertEquals(29, result.getDayOfMonth());
    }

    @Test
    public void test_withYear_leapYearToNonLeapYear() {
        // Partition: Leap year date handling on non-leap year
        // BVA: Feb 29 transitioning to non-leap year
        // Expected: Day clamped to Feb 28
        DateTime dt = new DateTime(2020, 2, 29, 12, 0, 0, UTC);  // 2020 is leap year
        DateTime result = dt.withYear(2021);  // 2021 is NOT a leap year
        
        assertEquals(2021, result.getYear());
        assertEquals(2, result.getMonthOfYear());
        assertEquals(28, result.getDayOfMonth());  // Day clamped
    }

    @Test
    public void test_withYear_nonLeapYearToLeapYear() {
        // Partition: Non-leap year date transitioning to leap year
        DateTime dt = new DateTime(2021, 2, 28, 12, 0, 0, UTC);  // 2021 is NOT a leap year
        DateTime result = dt.withYear(2020);  // 2020 is a leap year
        
        assertEquals(2020, result.getYear());
        assertEquals(2, result.getMonthOfYear());
        assertEquals(28, result.getDayOfMonth());
    }

    @Test
    public void test_withYear_minimumYear() {
        // BVA: Minimum year boundary (negative years / year 1)
        DateTime dt = new DateTime(2020, 6, 15, 10, 0, 0, UTC);
        DateTime result = dt.withYear(1);
        
        assertEquals(1, result.getYear());
        assertEquals(6, result.getMonthOfYear());
        assertEquals(15, result.getDayOfMonth());
    }

    @Test
    public void test_withYear_largePositiveYear() {
        // BVA: Large positive year
        DateTime dt = new DateTime(2020, 6, 15, 10, 0, 0, UTC);
        DateTime result = dt.withYear(9999);
        
        assertEquals(9999, result.getYear());
        assertEquals(6, result.getMonthOfYear());
        assertEquals(15, result.getDayOfMonth());
    }

    @Test
    public void test_withYear_timePreserved() {
        // Verify time components preserved
        DateTime dt = new DateTime(2020, 8, 15, 23, 59, 59, UTC);
        DateTime result = dt.withYear(2019);
        
        assertEquals(2019, result.getYear());
        assertEquals(23, result.getHourOfDay());
        assertEquals(59, result.getMinuteOfHour());
        assertEquals(59, result.getSecondOfMinute());
    }

    @Test
    public void test_withYear_januaryFirst() {
        // Edge case: January 1st
        DateTime dt = new DateTime(2020, 1, 1, 0, 0, 0, UTC);
        DateTime result = dt.withYear(2022);
        
        assertEquals(2022, result.getYear());
        assertEquals(1, result.getMonthOfYear());
        assertEquals(1, result.getDayOfMonth());
    }

    @Test
    public void test_withYear_decemberThirtyFirst() {
        // Edge case: December 31st
        DateTime dt = new DateTime(2020, 12, 31, 23, 59, 59, UTC);
        DateTime result = dt.withYear(2025);
        
        assertEquals(2025, result.getYear());
        assertEquals(12, result.getMonthOfYear());
        assertEquals(31, result.getDayOfMonth());
    }

    // ========================================================================
    // DateTime.plusDays(int) Tests [TI-02]
    // ========================================================================
    // Testing abstraction: ISP with BVA
    // Partitions:
    //  - Zero days (identity)
    //  - Positive days (within month)
    //  - Positive days (month boundary crossing)
    //  - Positive days (year boundary crossing)
    //  - Negative days (subtraction)
    //  - Large positive/negative values
    //  - Leap year boundary crossing
    // ========================================================================

    @Test
    public void test_plusDays_zeroDays() {
        // Partition: Zero days (identity)
        // ISP: Zero is a boundary value
        DateTime dt = new DateTime(2020, 6, 15, 10, 30, 45, UTC);
        DateTime result = dt.plusDays(0);
        
        assertEquals(dt, result);
        assertEquals(2020, result.getYear());
        assertEquals(6, result.getMonthOfYear());
        assertEquals(15, result.getDayOfMonth());
    }

    @Test
    public void test_plusDays_positiveDaysWithinMonth() {
        // Partition: Positive days within month
        DateTime dt = new DateTime(2020, 6, 10, 12, 0, 0, UTC);
        DateTime result = dt.plusDays(5);
        
        assertEquals(2020, result.getYear());
        assertEquals(6, result.getMonthOfYear());
        assertEquals(15, result.getDayOfMonth());
        assertEquals(12, result.getHourOfDay());
    }

    @Test
    public void test_plusDays_positiveMonthBoundaryCrossing() {
        // Partition: Month boundary crossing (forward)
        // BVA: End of month
        DateTime dt = new DateTime(2020, 6, 28, 12, 0, 0, UTC);
        DateTime result = dt.plusDays(5);  // Should cross into July
        
        assertEquals(2020, result.getYear());
        assertEquals(7, result.getMonthOfYear());
        assertEquals(3, result.getDayOfMonth());
    }

    @Test
    public void test_plusDays_positiveYearBoundaryCrossing() {
        // Partition: Year boundary crossing (forward)
        // BVA: End of year
        DateTime dt = new DateTime(2020, 12, 28, 12, 0, 0, UTC);
        DateTime result = dt.plusDays(5);  // Should cross into next year
        
        assertEquals(2021, result.getYear());
        assertEquals(1, result.getMonthOfYear());
        assertEquals(2, result.getDayOfMonth());
    }

    @Test
    public void test_plusDays_leapYearBoundaryCrossing() {
        // Partition: Leap year boundary crossing
        // BVA: Feb 28/29 transition in leap year
        DateTime dt = new DateTime(2020, 2, 25, 12, 0, 0, UTC);  // 2020 is leap year
        DateTime result = dt.plusDays(5);  // Should include Feb 29
        
        assertEquals(2020, result.getYear());
        assertEquals(3, result.getMonthOfYear());
        assertEquals(1, result.getDayOfMonth());
    }

    @Test
    public void test_plusDays_negativeDays() {
        // Partition: Negative days (subtraction)
        DateTime dt = new DateTime(2020, 6, 15, 12, 0, 0, UTC);
        DateTime result = dt.plusDays(-5);
        
        assertEquals(2020, result.getYear());
        assertEquals(6, result.getMonthOfYear());
        assertEquals(10, result.getDayOfMonth());
    }

    @Test
    public void test_plusDays_negativeMonthBoundaryCrossing() {
        // Partition: Month boundary crossing (backward)
        DateTime dt = new DateTime(2020, 6, 5, 12, 0, 0, UTC);
        DateTime result = dt.plusDays(-10);  // Should cross back to May
        
        assertEquals(2020, result.getYear());
        assertEquals(5, result.getMonthOfYear());
        assertEquals(26, result.getDayOfMonth());
    }

    @Test
    public void test_plusDays_negativeYearBoundaryCrossing() {
        // Partition: Year boundary crossing (backward)
        DateTime dt = new DateTime(2020, 1, 10, 12, 0, 0, UTC);
        DateTime result = dt.plusDays(-15);  // Should cross back to previous year
        
        assertEquals(2019, result.getYear());
        assertEquals(12, result.getMonthOfYear());
        assertEquals(26, result.getDayOfMonth());
    }

    @Test
    public void test_plusDays_largePositive() {
        // BVA: Large positive value
        DateTime dt = new DateTime(2020, 6, 15, 12, 0, 0, UTC);
        DateTime result = dt.plusDays(365);
        
        assertEquals(2021, result.getYear());
        assertEquals(6, result.getMonthOfYear());
        assertEquals(15, result.getDayOfMonth());
    }

    @Test
    public void test_plusDays_largeNegative() {
        // BVA: Large negative value
        DateTime dt = new DateTime(2020, 6, 15, 12, 0, 0, UTC);
        DateTime result = dt.plusDays(-365);
        
        assertEquals(2019, result.getYear());
        assertEquals(6, result.getMonthOfYear());
        assertEquals(16, result.getDayOfMonth());
    }

    @Test
    public void test_plusDays_timePreserved() {
        // Verify time component preservation
        DateTime dt = new DateTime(2020, 6, 15, 23, 45, 30, UTC);
        DateTime result = dt.plusDays(10);
        
        assertEquals(23, result.getHourOfDay());
        assertEquals(45, result.getMinuteOfHour());
        assertEquals(30, result.getSecondOfMinute());
    }

    @Test
    public void test_plusDays_januaryFirstBoundary() {
        // Edge case: January 1st plus negative days
        DateTime dt = new DateTime(2020, 1, 1, 12, 0, 0, UTC);
        DateTime result = dt.plusDays(-1);
        
        assertEquals(2019, result.getYear());
        assertEquals(12, result.getMonthOfYear());
        assertEquals(31, result.getDayOfMonth());
    }

    @Test
    public void test_plusDays_immutability() {
        // Verify original object is unchanged
        DateTime dt = new DateTime(2020, 6, 15, 10, 30, 0, UTC);
        DateTime original_year = new DateTime(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth(), 
                                            dt.getHourOfDay(), dt.getMinuteOfHour(), dt.getSecondOfMinute(), UTC);
        
        DateTime result = dt.plusDays(100);
        
        assertEquals(original_year.getYear(), dt.getYear());
        assertEquals(original_year.getMonthOfYear(), dt.getMonthOfYear());
        assertEquals(original_year.getDayOfMonth(), dt.getDayOfMonth());
        assertNotEquals(result.getDayOfMonth(), dt.getDayOfMonth());
    }
}
