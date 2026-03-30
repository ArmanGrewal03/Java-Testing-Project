/*
 *  LocalDate Manual Test Suite
 *  
 *  Tests for:
 *  - LocalDate.plusMonths(int) [TI-03]
 *  - LocalDate.minusDays(int) [TI-04]
 *  
 *  Testing Approaches:
 *  - Input Space Partitioning (ISP)
 *  - Boundary Value Analysis (BVA)
 *  - Immutability verification
 *  - Month-end day clamping edge cases
 */
package org.joda.time;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LocalDateManualTest {

    // ========================================================================
    // LocalDate.plusMonths(int) Tests [TI-03]
    // ========================================================================
    // Testing abstraction: ISP with BVA
    // Partitions:
    //  - Zero months (identity)
    //  - Positive months (same year)
    //  - Positive months (year boundary crossing)
    //  - Negative months
    //  - Month-end clamping (31 to 28/30)
    //  - Leap-year February handling
    // ========================================================================

    @Test
    public void test_plusMonths_zeroMonths() {
        // Partition: Zero months (identity)
        // ISP: Zero is boundary
        LocalDate ld = new LocalDate(2020, 6, 15);
        LocalDate result = ld.plusMonths(0);
        
        assertEquals(ld, result);
        assertEquals(2020, result.getYear());
        assertEquals(6, result.getMonthOfYear());
        assertEquals(15, result.getDayOfMonth());
    }

    @Test
    public void test_plusMonths_positiveWithinYear() {
        // Partition: Positive months within same year
        LocalDate ld = new LocalDate(2020, 6, 15);
        LocalDate result = ld.plusMonths(3);
        
        assertEquals(2020, result.getYear());
        assertEquals(9, result.getMonthOfYear());
        assertEquals(15, result.getDayOfMonth());
    }

    @Test
    public void test_plusMonths_positiveYearBoundaryCrossing() {
        // Partition: Year boundary crossing (forward)
        // BVA: December + months
        LocalDate ld = new LocalDate(2020, 11, 15);
        LocalDate result = ld.plusMonths(3);  // Crosses to March 2021
        
        assertEquals(2021, result.getYear());
        assertEquals(2, result.getMonthOfYear());
        assertEquals(15, result.getDayOfMonth());
    }

    @Test
    public void test_plusMonths_negativeMonths() {
        // Partition: Negative months (subtraction)
        LocalDate ld = new LocalDate(2020, 6, 15);
        LocalDate result = ld.plusMonths(-3);
        
        assertEquals(2020, result.getYear());
        assertEquals(3, result.getMonthOfYear());
        assertEquals(15, result.getDayOfMonth());
    }

    @Test
    public void test_plusMonths_negativeYearBoundaryCrossing() {
        // Partition: Year boundary crossing (backward)
        LocalDate ld = new LocalDate(2020, 3, 15);
        LocalDate result = ld.plusMonths(-5);  // Crosses back to Oct 2019
        
        assertEquals(2019, result.getYear());
        assertEquals(10, result.getMonthOfYear());
        assertEquals(15, result.getDayOfMonth());
    }

    @Test
    public void test_plusMonths_monthEndClampingTo28() {
        // Partition: Month-end day clamping (31 to 28/30)
        // BVA: Jan 31 + months → Feb (only 28 days)
        LocalDate ld = new LocalDate(2021, 1, 31);  // 2021 is NOT leap year
        LocalDate result = ld.plusMonths(1);
        
        assertEquals(2021, result.getYear());
        assertEquals(2, result.getMonthOfYear());
        assertEquals(28, result.getDayOfMonth());  // Clamped to Feb 28
    }

    @Test
    public void test_plusMonths_monthEndClampingTo29() {
        // Partition: Month-end day clamping to Feb 29 in leap year
        LocalDate ld = new LocalDate(2020, 1, 31);  // 2020 is leap year
        LocalDate result = ld.plusMonths(1);
        
        assertEquals(2020, result.getYear());
        assertEquals(2, result.getMonthOfYear());
        assertEquals(29, result.getDayOfMonth());  // Clamped to Feb 29
    }

    @Test
    public void test_plusMonths_monthEndClampingJan31ToApr30() {
        // Partition: Day clamping from 31 days to 30 days
        LocalDate ld = new LocalDate(2020, 3, 31);
        LocalDate result = ld.plusMonths(1);  // March → April (only 30 days)
        
        assertEquals(2020, result.getYear());
        assertEquals(4, result.getMonthOfYear());
        assertEquals(30, result.getDayOfMonth());
    }

    @Test
    public void test_plusMonths_largePositive() {
        // BVA: Large positive value
        LocalDate ld = new LocalDate(2020, 6, 15);
        LocalDate result = ld.plusMonths(12);  // One year
        
        assertEquals(2021, result.getYear());
        assertEquals(6, result.getMonthOfYear());
        assertEquals(15, result.getDayOfMonth());
    }

    @Test
    public void test_plusMonths_largeNegative() {
        // BVA: Large negative value
        LocalDate ld = new LocalDate(2020, 6, 15);
        LocalDate result = ld.plusMonths(-12);  // One year back
        
        assertEquals(2019, result.getYear());
        assertEquals(6, result.getMonthOfYear());
        assertEquals(15, result.getDayOfMonth());
    }

    @Test
    public void test_plusMonths_janToDecember() {
        // Edge case: Jan 1 plus 11 months → Dec 1
        LocalDate ld = new LocalDate(2020, 1, 1);
        LocalDate result = ld.plusMonths(11);
        
        assertEquals(2020, result.getYear());
        assertEquals(12, result.getMonthOfYear());
        assertEquals(1, result.getDayOfMonth());
    }

    @Test
    public void test_plusMonths_immutability() {
        // Verify immutability
        LocalDate ld = new LocalDate(2020, 6, 15);
        LocalDate original_month = new LocalDate(ld.getYear(), ld.getMonthOfYear(), ld.getDayOfMonth());
        
        LocalDate result = ld.plusMonths(10);
        
        assertEquals(original_month.getMonthOfYear(), ld.getMonthOfYear());
        assertNotEquals(result.getMonthOfYear(), ld.getMonthOfYear());
    }

    // ========================================================================
    // LocalDate.minusDays(int) Tests [TI-04]
    // ========================================================================
    // Testing abstraction: ISP with BVA
    // Partitions:
    //  - Zero days (identity)
    //  - Positive subtraction (within month)
    //  - Positive subtraction (month boundary crossing)
    //  - Positive subtraction (year boundary crossing)
    //  - Negative subtraction (effectively addition)
    //  - Large values
    //  - Leap-year boundary
    // ========================================================================

    @Test
    public void test_minusDays_zeroDays() {
        // Partition: Zero days (identity)
        LocalDate ld = new LocalDate(2020, 6, 15);
        LocalDate result = ld.minusDays(0);
        
        assertEquals(ld, result);
        assertEquals(2020, result.getYear());
        assertEquals(6, result.getMonthOfYear());
        assertEquals(15, result.getDayOfMonth());
    }

    @Test
    public void test_minusDays_positiveWithinMonth() {
        // Partition: Positive subtraction within month
        LocalDate ld = new LocalDate(2020, 6, 20);
        LocalDate result = ld.minusDays(5);
        
        assertEquals(2020, result.getYear());
        assertEquals(6, result.getMonthOfYear());
        assertEquals(15, result.getDayOfMonth());
    }

    @Test
    public void test_minusDays_positiveMonthBoundaryCrossing() {
        // Partition: Month boundary crossing (backward)
        // BVA: Beginning of month
        LocalDate ld = new LocalDate(2020, 6, 5);
        LocalDate result = ld.minusDays(10);  // Should cross back to May
        
        assertEquals(2020, result.getYear());
        assertEquals(5, result.getMonthOfYear());
        assertEquals(26, result.getDayOfMonth());
    }

    @Test
    public void test_minusDays_positiveYearBoundaryCrossing() {
        // Partition: Year boundary crossing (backward)
        LocalDate ld = new LocalDate(2020, 1, 10);
        LocalDate result = ld.minusDays(15);  // Should cross back to previous year
        
        assertEquals(2019, result.getYear());
        assertEquals(12, result.getMonthOfYear());
        assertEquals(26, result.getDayOfMonth());
    }

    @Test
    public void test_minusDays_negativeSubtraction() {
        // Partition: Negative subtraction (effectively addition)
        LocalDate ld = new LocalDate(2020, 6, 15);
        LocalDate result = ld.minusDays(-5);  // Subtract negative = add
        
        assertEquals(2020, result.getYear());
        assertEquals(6, result.getMonthOfYear());
        assertEquals(20, result.getDayOfMonth());
    }

    @Test
    public void test_minusDays_leapYearBoundary() {
        // Partition: Leap year boundary
        // BVA: Feb 29 in leap year
        LocalDate ld = new LocalDate(2020, 3, 5);  // 2020 is leap year
        LocalDate result = ld.minusDays(5);  // Should cross Feb 29
        
        assertEquals(2020, result.getYear());
        assertEquals(2, result.getMonthOfYear());
        assertEquals(29, result.getDayOfMonth());
    }

    @Test
    public void test_minusDays_nonLeapYearFeb() {
        // Partition: February 28 in non-leap year
        LocalDate ld = new LocalDate(2021, 3, 1);  // 2021 is NOT leap year
        LocalDate result = ld.minusDays(1);
        
        assertEquals(2021, result.getYear());
        assertEquals(2, result.getMonthOfYear());
        assertEquals(28, result.getDayOfMonth());
    }

    @Test
    public void test_minusDays_largePositive() {
        // BVA: Large positive subtraction
        LocalDate ld = new LocalDate(2020, 6, 15);
        LocalDate result = ld.minusDays(365);  // One year back
        
        assertEquals(2019, result.getYear());
        assertEquals(6, result.getMonthOfYear());
        assertEquals(16, result.getDayOfMonth());
    }

    @Test
    public void test_minusDays_largeNegative() {
        // BVA: Large negative subtraction (effectively addition)
        LocalDate ld = new LocalDate(2020, 6, 15);
        LocalDate result = ld.minusDays(-365);  // Effectively add 365 days
        
        assertEquals(2021, result.getYear());
        assertEquals(6, result.getMonthOfYear());
        assertEquals(15, result.getDayOfMonth());
    }

    @Test
    public void test_minusDays_january1st() {
        // Edge case: January 1 minus days
        LocalDate ld = new LocalDate(2020, 1, 1);
        LocalDate result = ld.minusDays(1);
        
        assertEquals(2019, result.getYear());
        assertEquals(12, result.getMonthOfYear());
        assertEquals(31, result.getDayOfMonth());
    }

    @Test
    public void test_minusDays_december31st() {
        // Edge case: December 31 plus negative days (subtraction)
        LocalDate ld = new LocalDate(2020, 12, 31);
        LocalDate result = ld.minusDays(-1);  // Add 1 day
        
        assertEquals(2021, result.getYear());
        assertEquals(1, result.getMonthOfYear());
        assertEquals(1, result.getDayOfMonth());
    }

    @Test
    public void test_minusDays_immutability() {
        // Verify immutability
        LocalDate ld = new LocalDate(2020, 6, 15);
        LocalDate original_day = new LocalDate(ld.getYear(), ld.getMonthOfYear(), ld.getDayOfMonth());
        
        LocalDate result = ld.minusDays(20);
        
        assertEquals(original_day.getDayOfMonth(), ld.getDayOfMonth());
        assertNotEquals(result.getDayOfMonth(), ld.getDayOfMonth());
    }


}
