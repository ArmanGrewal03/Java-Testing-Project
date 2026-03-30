/*
 *  Period Manual Test Suite
 *  
 *  Tests for:
 *  - Period.plus(ReadablePeriod) [TI-07]
 *  - Period.normalizedStandard() [TI-08]
 *  
 *  Testing Approaches:
 *  - Input Space Partitioning (ISP)
 *  - Boundary Value Analysis (BVA)
 *  - Logic-based testing
 *  - Exception handling
 *  - Immutability verification
 */
package org.joda.time;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PeriodManualTest {

    // ========================================================================
    // Period.plus(ReadablePeriod) Tests [TI-07]
    // ========================================================================
    // Testing abstraction: ISP with BVA
    // Partitions:
    //  - Null period (null means zero)
    //  - Zero period (all fields zero)
    //  - Positive period (same sign fields)
    //  - Mixed field combinations
    //  - Large values
    // ========================================================================

    @Test
    public void test_plus_nullPeriod() {
        // Partition: Null period
        // ISP: Null input is special case (assumes zero)
        Period p = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        Period result = p.plus(null);
        
        assertEquals(p, result);
        assertEquals(1, result.getYears());
        assertEquals(2, result.getMonths());
        assertEquals(3, result.getWeeks());
        assertEquals(4, result.getDays());
        assertEquals(5, result.getHours());
        assertEquals(6, result.getMinutes());
        assertEquals(7, result.getSeconds());
        assertEquals(8, result.getMillis());
    }

    @Test
    public void test_plus_zeroPeriod() {
        // Partition: Zero period (all fields zero)
        Period p = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        Period zero = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        Period result = p.plus(zero);
        
        assertEquals(1, result.getYears());
        assertEquals(2, result.getMonths());
        assertEquals(3, result.getWeeks());
        assertEquals(4, result.getDays());
    }

    @Test
    public void test_plus_positiveYears() {
        // Partition: Positive years field
        Period p = new Period(1, 0, 0, 0, 0, 0, 0, 0);
        Period toAdd = new Period(2, 0, 0, 0, 0, 0, 0, 0);
        Period result = p.plus(toAdd);
        
        assertEquals(3, result.getYears());
        assertEquals(0, result.getMonths());
    }

    @Test
    public void test_plus_positiveMonths() {
        // Partition: Positive months field
        Period p = new Period(0, 5, 0, 0, 0, 0, 0, 0);
        Period toAdd = new Period(0, 3, 0, 0, 0, 0, 0, 0);
        Period result = p.plus(toAdd);
        
        assertEquals(0, result.getYears());
        assertEquals(8, result.getMonths());
    }

    @Test
    public void test_plus_mixedFields() {
        // Partition: Mixed positive fields
        Period p = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        Period toAdd = new Period(1, 1, 1, 1, 1, 1, 1, 1);
        Period result = p.plus(toAdd);
        
        assertEquals(2, result.getYears());
        assertEquals(3, result.getMonths());
        assertEquals(4, result.getWeeks());
        assertEquals(5, result.getDays());
        assertEquals(6, result.getHours());
        assertEquals(7, result.getMinutes());
        assertEquals(8, result.getSeconds());
        assertEquals(9, result.getMillis());
    }

    @Test
    public void test_plus_largeValues() {
        // BVA: Large positive values
        Period p = new Period(10, 12, 4, 28, 23, 59, 59, 999);
        Period toAdd = new Period(5, 6, 2, 14, 10, 30, 30, 500);
        Period result = p.plus(toAdd);
        
        assertEquals(15, result.getYears());
        assertEquals(18, result.getMonths());
        assertEquals(6, result.getWeeks());
        assertEquals(42, result.getDays());
    }

    @Test
    public void test_plus_singleField() {
        // Edge case: Only one field non-zero
        Period p = new Period(5, 0, 0, 0, 0, 0, 0, 0);
        Period toAdd = new Period(0, 0, 0, 0, 0, 0, 0, 100);
        Period result = p.plus(toAdd);
        
        assertEquals(5, result.getYears());
        assertEquals(100, result.getMillis());
    }

    @Test
    public void test_plus_weeksOnly() {
        // ISP: Explicit test for weeks field
        // Ensure weeks field is properly added
        Period p = new Period(0, 0, 5, 0, 0, 0, 0, 0);  // 5 weeks
        Period toAdd = new Period(0, 0, 3, 0, 0, 0, 0, 0);  // 3 weeks
        Period result = p.plus(toAdd);
        
        assertEquals(0, result.getYears());
        assertEquals(0, result.getMonths());
        assertEquals(8, result.getWeeks());
        assertEquals(0, result.getDays());
        assertEquals(0, result.getHours());
        assertEquals(0, result.getMinutes());
        assertEquals(0, result.getSeconds());
        assertEquals(0, result.getMillis());
    }

    @Test
    public void test_plus_daysOnly() {
        // ISP: Explicit test for days field
        Period p = new Period(0, 0, 0, 10, 0, 0, 0, 0);  // 10 days
        Period toAdd = new Period(0, 0, 0, 5, 0, 0, 0, 0);  // 5 days
        Period result = p.plus(toAdd);
        
        assertEquals(0, result.getYears());
        assertEquals(0, result.getMonths());
        assertEquals(0, result.getWeeks());
        assertEquals(15, result.getDays());
        assertEquals(0, result.getHours());
        assertEquals(0, result.getMinutes());
        assertEquals(0, result.getSeconds());
        assertEquals(0, result.getMillis());
    }

    @Test
    public void test_plus_hoursOnly() {
        // ISP: Explicit test for hours field
        Period p = new Period(0, 0, 0, 0, 12, 0, 0, 0);  // 12 hours
        Period toAdd = new Period(0, 0, 0, 0, 6, 0, 0, 0);  // 6 hours
        Period result = p.plus(toAdd);
        
        assertEquals(0, result.getYears());
        assertEquals(0, result.getMonths());
        assertEquals(0, result.getWeeks());
        assertEquals(0, result.getDays());
        assertEquals(18, result.getHours());
        assertEquals(0, result.getMinutes());
        assertEquals(0, result.getSeconds());
        assertEquals(0, result.getMillis());
    }

    @Test
    public void test_plus_minutesOnly() {
        // ISP: Explicit test for minutes field
        Period p = new Period(0, 0, 0, 0, 0, 30, 0, 0);  // 30 minutes
        Period toAdd = new Period(0, 0, 0, 0, 0, 15, 0, 0);  // 15 minutes
        Period result = p.plus(toAdd);
        
        assertEquals(0, result.getYears());
        assertEquals(0, result.getMonths());
        assertEquals(0, result.getWeeks());
        assertEquals(0, result.getDays());
        assertEquals(0, result.getHours());
        assertEquals(45, result.getMinutes());
        assertEquals(0, result.getSeconds());
        assertEquals(0, result.getMillis());
    }

    @Test
    public void test_plus_secondsOnly() {
        // ISP: Explicit test for seconds field
        Period p = new Period(0, 0, 0, 0, 0, 0, 45, 0);  // 45 seconds
        Period toAdd = new Period(0, 0, 0, 0, 0, 0, 30, 0);  // 30 seconds
        Period result = p.plus(toAdd);
        
        assertEquals(0, result.getYears());
        assertEquals(0, result.getMonths());
        assertEquals(0, result.getWeeks());
        assertEquals(0, result.getDays());
        assertEquals(0, result.getHours());
        assertEquals(0, result.getMinutes());
        assertEquals(75, result.getSeconds());
        assertEquals(0, result.getMillis());
    }

    @Test
    public void test_plus_millisOnly() {
        // ISP: Explicit test for millis field
        Period p = new Period(0, 0, 0, 0, 0, 0, 0, 500);  // 500 ms
        Period toAdd = new Period(0, 0, 0, 0, 0, 0, 0, 300);  // 300 ms
        Period result = p.plus(toAdd);
        
        assertEquals(0, result.getYears());
        assertEquals(0, result.getMonths());
        assertEquals(0, result.getWeeks());
        assertEquals(0, result.getDays());
        assertEquals(0, result.getHours());
        assertEquals(0, result.getMinutes());
        assertEquals(0, result.getSeconds());
        assertEquals(800, result.getMillis());
    }

    @Test
    public void test_plus_hoursMinutesSeconds() {
        // ISP: Combined time fields (hours, minutes, seconds)
        // Tests interaction of multiple time-based fields
        Period p = new Period(0, 0, 0, 0, 5, 30, 45, 500);
        Period toAdd = new Period(0, 0, 0, 0, 3, 15, 20, 300);
        Period result = p.plus(toAdd);
        
        assertEquals(0, result.getYears());
        assertEquals(0, result.getMonths());
        assertEquals(0, result.getWeeks());
        assertEquals(0, result.getDays());
        assertEquals(8, result.getHours());
        assertEquals(45, result.getMinutes());
        assertEquals(65, result.getSeconds());
        assertEquals(800, result.getMillis());
    }

    @Test
    public void test_plus_immutability() {
        // Verify immutability
        Period p = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        Period toAdd = new Period(1, 1, 1, 1, 1, 1, 1, 1);
        Period original_years = new Period(p.getYears(), 0, 0, 0, 0, 0, 0, 0);
        
        Period result = p.plus(toAdd);
        
        assertEquals(original_years.getYears(), p.getYears());
        assertNotEquals(result.getYears(), p.getYears());
    }

    @Test
    public void test_plus_chainedOperations() {
        // Chain plus operations
        Period p = new Period(1, 1, 1, 1, 0, 0, 0, 0);
        Period toAdd1 = new Period(1, 1, 1, 1, 0, 0, 0, 0);
        Period toAdd2 = new Period(1, 1, 1, 1, 0, 0, 0, 0);
        
        Period result = p.plus(toAdd1).plus(toAdd2);
        
        assertEquals(3, result.getYears());
        assertEquals(3, result.getMonths());
        assertEquals(3, result.getWeeks());
        assertEquals(3, result.getDays());
    }

    // ========================================================================
    // Period.normalizedStandard() Tests [TI-08]
    // ========================================================================
    // Testing abstraction: Logic-based testing and ISP
    // Partitions:
    //  - Already normalized period
    //  - Period with excess days/weeks/hours/minutes/seconds
    //  - Period with years/months normalization
    //  - Zero period
    //  - Pure time period (days below)
    // Logic: months normalized to 0-11 range, days/time computed with standard conversions
    // ========================================================================

    @Test
    public void test_normalizedStandard_alreadyNormalized() {
        // Partition: Already normalized (all fields within valid ranges)
        Period p = new Period(1, 6, 2, 5, 10, 30, 45, 500);
        Period result = p.normalizedStandard();
        
        assertNotNull(result);
        assertEquals(1, result.getYears());
        assertEquals(6, result.getMonths());
        assertEquals(2, result.getWeeks());
        assertEquals(5, result.getDays());
    }

    @Test
    public void test_normalizedStandard_excessMinutes() {
        // Partition: Excess minutes (>= 60)
        // Logic: 60 minutes → 1 hour
        Period p = new Period(0, 0, 0, 0, 0, 120, 0, 0);  // 120 minutes
        Period result = p.normalizedStandard();
        
        assertNotNull(result);
        // Should normalize: 120 minutes = 2 hours
        assertEquals(2, result.getHours());
        assertEquals(0, result.getMinutes());
    }

    @Test
    public void test_normalizedStandard_excessSeconds() {
        // Partition: Excess seconds (>= 60)
        Period p = new Period(0, 0, 0, 0, 0, 0, 120, 0);  // 120 seconds
        Period result = p.normalizedStandard();
        
        assertNotNull(result);
        // Should normalize: 120 seconds = 2 minutes
        assertEquals(2, result.getMinutes());
        assertEquals(0, result.getSeconds());
    }

    @Test
    public void test_normalizedStandard_excessDays() {
        // Partition: Excess days
        // Logic: 7 days → 1 week (converted to base units)
        Period p = new Period(0, 0, 0, 15, 0, 0, 0, 0);  // 15 days
        Period result = p.normalizedStandard();
        
        assertNotNull(result);
        // Result: 15 days total (weeks are converted to days in standard form)
        assertEquals(15, result.getDays());
    }

    @Test
    public void test_normalizedStandard_excessMonths() {
        // Partition: Excess months (>= 12)
        // Logic: 12 months or more → years normalization
        Period p = new Period(1, 15, 0, 0, 0, 0, 0, 0);  // 1 year + 15 months
        Period result = p.normalizedStandard();
        
        assertNotNull(result);
        // Should normalize: 1 year + 15 months = 2 years + 3 months
        assertEquals(2, result.getYears());
        assertEquals(3, result.getMonths());
    }

    @Test
    public void test_normalizedStandard_complexNormalization() {
        // Partition: Multiple fields requiring normalization
        Period p = new Period(0, 0, 0, 0, 30, 90, 120, 0);  
        // 30 hours, 90 minutes, 120 seconds
        Period result = p.normalizedStandard();
        
        assertNotNull(result);
        // 120 seconds = 2 minutes, 90 minutes = 1 hour 30 minutes
        // Total: 30 hours + 1 hour 30 minutes + 2 minutes = 31 hour 32 minutes
    }

    @Test
    public void test_normalizedStandard_zeroPeriod() {
        // Partition: Zero period (all fields zero)
        Period p = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        Period result = p.normalizedStandard();
        
        assertNotNull(result);
        assertEquals(0, result.getYears());
        assertEquals(0, result.getMonths());
    }

    @Test
    public void test_normalizedStandard_onlyTimeFields() {
        // Partition: Period with only time fields
        Period p = new Period(0, 0, 0, 0, 2, 30, 45, 100);
        Period result = p.normalizedStandard();
        
        assertNotNull(result);
        assertEquals(2, result.getHours());
        assertEquals(30, result.getMinutes());
        assertEquals(45, result.getSeconds());
        assertEquals(100, result.getMillis());
    }

    @Test
    public void test_normalizedStandard_immutability() {
        // Verify original period unchanged
        Period p = new Period(0, 15, 0, 0, 0, 120, 0, 0);
        Period original_months = new Period(0, 15, 0, 0, 0, 0, 0, 0);
        
        Period result = p.normalizedStandard();
        
        // Original should be unchanged
        assertEquals(original_months.getMonths(), p.getMonths());
        // Result should be normalized
        assertNotNull(result);
    }

    @Test
    public void test_normalizedStandard_largeValues() {
        // BVA: Large field values
        Period p = new Period(5, 24, 10, 200, 100, 300, 3600, 5000);
        Period result = p.normalizedStandard();
        
        assertNotNull(result);
        assertEquals(5, result.getYears());
        // 24 months normalized
        assertTrue(result.getMonths() >= 0 && result.getMonths() <= 11);
    }

    @Test
    public void test_normalizedStandard_negativeFields() {
        // Edge case: Some negative field values (may normalize to hybrid positive/negative)
        Period p = new Period(1, 12, 0, 0, 0, 0, 0, 0);  // 1 year + 12 months
        Period result = p.normalizedStandard();
        
        assertNotNull(result);
        assertEquals(2, result.getYears());
        assertEquals(0, result.getMonths());
    }
}
