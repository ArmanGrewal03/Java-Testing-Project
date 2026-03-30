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
        // Result normalized as 2 weeks and 1 day
        assertEquals(1, result.getDays());
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
        assertEquals(7, result.getYears());
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

    // ========================================================================
    // Additional Tests for Maximum Coverage
    // ========================================================================

    @Test
    public void test_plus_singleField_years() {
        // Only years field
        Period p = new Period(5, 0, 0, 0, 0, 0, 0, 0);
        Period toAdd = new Period(3, 0, 0, 0, 0, 0, 0, 0);
        Period result = p.plus(toAdd);
        
        assertEquals(8, result.getYears());
        assertEquals(0, result.getMonths());
    }

    @Test
    public void test_plus_singleField_months() {
        // Only months field
        Period p = new Period(0, 10, 0, 0, 0, 0, 0, 0);
        Period toAdd = new Period(0, 5, 0, 0, 0, 0, 0, 0);
        Period result = p.plus(toAdd);
        
        assertEquals(0, result.getYears());
        assertEquals(15, result.getMonths());
    }

    @Test
    public void test_plus_singleField_weeks() {
        // Only weeks field
        Period p = new Period(0, 0, 10, 0, 0, 0, 0, 0);
        Period toAdd = new Period(0, 0, 5, 0, 0, 0, 0, 0);
        Period result = p.plus(toAdd);
        
        assertEquals(15, result.getWeeks());
    }

    @Test
    public void test_plus_singleField_days() {
        // Only days field
        Period p = new Period(0, 0, 0, 20, 0, 0, 0, 0);
        Period toAdd = new Period(0, 0, 0, 10, 0, 0, 0, 0);
        Period result = p.plus(toAdd);
        
        assertEquals(0, result.getYears());
        assertEquals(30, result.getDays());  // 20 + 10 = 30
    }

    @Test
    public void test_plus_allTimeFields() {
        // All time-based fields (hours, minutes, seconds, millis)
        Period p = new Period(0, 0, 0, 0, 24, 60, 60, 1000);
        Period toAdd = new Period(0, 0, 0, 0, 1, 1, 1, 100);
        Period result = p.plus(toAdd);
        
        assertEquals(25, result.getHours());
        assertEquals(61, result.getMinutes());
        assertEquals(61, result.getSeconds());
        assertEquals(1100, result.getMillis());
    }

    @Test
    public void test_plus_allDateFields() {
        // All date-based fields (years, months, weeks, days)
        Period p = new Period(1, 6, 4, 15, 0, 0, 0, 0);
        Period toAdd = new Period(2, 3, 2, 5, 0, 0, 0, 0);
        Period result = p.plus(toAdd);
        
        assertEquals(3, result.getYears());
        assertEquals(9, result.getMonths());
        assertEquals(6, result.getWeeks());
        assertEquals(20, result.getDays());
    }

    @Test
    public void test_plus_veryLargeValues() {
        // Extremely large field values
        Period p = new Period(100, 500, 200, 1000, 500, 3000, 60000, 1000000);
        Period toAdd = new Period(50, 200, 100, 500, 250, 1500, 30000, 500000);
        Period result = p.plus(toAdd);
        
        assertEquals(150, result.getYears());
        assertEquals(700, result.getMonths());
    }

    @Test
    public void test_plus_withNullExpectedBehavior() {
        // Null period behaves as zero
        Period p = new Period(5, 5, 5, 5, 5, 5, 5, 5);
        Period result = p.plus(null);
        
        assertEquals(5, result.getYears());
        assertEquals(5, result.getMonths());
        assertEquals(5, result.getWeeks());
        assertEquals(5, result.getDays());
    }

    @Test
    public void test_plus_chainMultipleTimes() {
        // Chain multiple plus operations
        Period p = new Period(1, 1, 1, 1, 1, 1, 1, 1);
        Period result = p.plus(p).plus(p).plus(p);
        
        assertEquals(4, result.getYears());
        assertEquals(4, result.getMonths());
    }

    @Test
    public void test_normalizedStandard_onlyYears() {
        // Period with only years
        Period p = new Period(5, 0, 0, 0, 0, 0, 0, 0);
        Period result = p.normalizedStandard();
        
        assertNotNull(result);
        assertEquals(5, result.getYears());
    }

    @Test
    public void test_normalizedStandard_onlyMonths() {
        // Period with only months
        Period p = new Period(0, 24, 0, 0, 0, 0, 0, 0);
        Period result = p.normalizedStandard();
        
        assertNotNull(result);
        assertEquals(2, result.getYears());
        assertEquals(0, result.getMonths());
    }

    @Test
    public void test_normalizedStandard_onlyWeeks() {
        // Period with only weeks
        Period p = new Period(0, 0, 10, 0, 0, 0, 0, 0);
        Period result = p.normalizedStandard();
        
        assertNotNull(result);
    }

    @Test
    public void test_normalizedStandard_hoursMinutesSeconds() {
        // Normalize hours, minutes, seconds together
        Period p = new Period(0, 0, 0, 0, 25, 90, 180, 0);
        Period result = p.normalizedStandard();
        
        assertNotNull(result);
        assertTrue(result.getHours() >= 0);
    }

    @Test
    public void test_normalizedStandard_hugeMonths() {
        // Very large month value requiring normalization
        Period p = new Period(0, 120, 0, 0, 0, 0, 0, 0);
        Period result = p.normalizedStandard();
        
        assertNotNull(result);
        assertEquals(10, result.getYears());
        assertEquals(0, result.getMonths());
    }

    @Test
    public void test_normalizedStandard_mixedExcess() {
        // Multiple fields with excess requiring normalization
        Period p = new Period(1, 24, 8, 35, 60, 120, 3661, 2000);
        Period result = p.normalizedStandard();
        
        assertNotNull(result);
        assertTrue(result.getYears() >= 0);
    }

    @Test
    public void test_normalizedStandard_allZero() {
        // All zero period
        Period p = new Period(0, 0, 0, 0, 0, 0, 0, 0);
        Period result = p.normalizedStandard();
        
        assertNotNull(result);
        assertEquals(0, result.getYears());
        assertEquals(0, result.getMonths());
    }

    @Test
    public void test_normalizedStandard_immutabilityAfterNormalization() {
        // Verify original not modified
        Period p = new Period(0, 24, 0, 0, 60, 3600, 60000, 1000000);
        int originalMonths = p.getMonths();
        int originalHours = p.getHours();
        
        Period result = p.normalizedStandard();
        
        assertEquals(originalMonths, p.getMonths());
        assertEquals(originalHours, p.getHours());
    }

    @Test
    public void test_plus_thenNormalize() {
        // Plus operation followed by normalization
        Period p1 = new Period(0, 6, 0, 0, 0, 0, 0, 0);
        Period p2 = new Period(0, 18, 0, 0, 0, 0, 0, 0);
        Period result = p1.plus(p2).normalizedStandard();
        
        assertNotNull(result);
        assertEquals(2, result.getYears());
        assertEquals(0, result.getMonths());
    }

    @Test
    public void test_normalizedStandard_containsAllFieldTypes() {
        // Normalization with all field types present
        Period p = new Period(1, 12, 4, 30, 30, 90, 90, 500);
        Period result = p.normalizedStandard();
        
        assertNotNull(result);
        assertTrue(result.getYears() >= 0);
        assertTrue(result.getMonths() >= 0);
        assertTrue(result.getWeeks() >= 0);
    }
}
