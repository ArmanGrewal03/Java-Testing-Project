/*
 *  Duration Manual Test Suite
 *  
 *  Tests for:
 *  - Duration.plus(long) [TI-05]
 *  - Duration.dividedBy(long) [TI-06]
 *  
 *  Testing Approaches:
 *  - Input Space Partitioning (ISP)
 *  - Boundary Value Analysis (BVA)
 *  - Logic-based testing (predicate analysis for dividedBy)
 *  - Exception handling
 *  - Immutability verification
 */
package org.joda.time;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DurationManualTest {

    // ========================================================================
    // Duration.plus(long) Tests [TI-05]
    // ========================================================================
    // Testing abstraction: ISP with BVA
    // Partitions:
    //  - Zero milliseconds (identity)
    //  - Positive milliseconds
    //  - Negative milliseconds
    //  - Large positive/negative values
    // ========================================================================

    @Test
    public void test_plus_zeroMilliseconds() {
        // Partition: Zero milliseconds (identity)
        // ISP: Zero is boundary
        Duration d = new Duration(5000);  // 5 seconds
        Duration result = d.plus(0);
        
        assertEquals(d, result);
        assertEquals(5000, result.getMillis());
    }

    @Test
    public void test_plus_positiveMilliseconds() {
        // Partition: Positive milliseconds
        Duration d = new Duration(5000);  // 5 seconds
        Duration result = d.plus(3000);   // Add 3 seconds
        
        assertEquals(8000, result.getMillis());
    }

    @Test
    public void test_plus_negativeMilliseconds() {
        // Partition: Negative milliseconds
        Duration d = new Duration(5000);  // 5 seconds
        Duration result = d.plus(-3000);  // Add -3 seconds (subtract)
        
        assertEquals(2000, result.getMillis());
    }

    @Test
    public void test_plus_resultInZero() {
        // Edge case: Add negative to reach zero
        Duration d = new Duration(5000);
        Duration result = d.plus(-5000);
        
        assertEquals(0, result.getMillis());
    }

    @Test
    public void test_plus_resultInNegative() {
        // Edge case: Result becomes negative
        Duration d = new Duration(5000);
        Duration result = d.plus(-10000);
        
        assertEquals(-5000, result.getMillis());
    }

    @Test
    public void test_plus_largePositiveValue() {
        // BVA: Large positive value (many seconds, minutes, hours)
        Duration d = new Duration(1000);
        Duration result = d.plus(86400000);  // Add 1 day in milliseconds
        
        assertEquals(86401000, result.getMillis());
    }

    @Test
    public void test_plus_largeNegativeValue() {
        // BVA: Large negative value
        Duration d = new Duration(86400000);  // 1 day
        Duration result = d.plus(-86401000); // Subtract more than duration
        
        assertEquals(-1000, result.getMillis());
    }

    @Test
    public void test_plus_immutability() {
        // Verify original duration unchanged
        Duration d = new Duration(5000);
        Duration original_millis = new Duration(5000);
        
        Duration result = d.plus(3000);
        
        assertEquals(original_millis.getMillis(), d.getMillis());
        assertNotEquals(result.getMillis(), d.getMillis());
    }

    @Test
    public void test_plus_multipleTimes() {
        // Chain add operations
        Duration d = new Duration(1000);
        Duration result = d.plus(2000).plus(3000);
        
        assertEquals(6000, result.getMillis());
    }

    @Test
    public void test_plus_longMaxValue() {
        // BVA: Long.MAX_VALUE boundary
        // Partition: Extreme positive boundary value
        Duration d = new Duration(1000);
        Duration result = d.plus(Long.MAX_VALUE - 1000);
        
        assertEquals(Long.MAX_VALUE, result.getMillis());
    }

    @Test
    public void test_plus_longMinValue() {
        // BVA: Long.MIN_VALUE boundary
        // Partition: Extreme negative boundary value
        Duration d = new Duration(Long.MIN_VALUE + 1000);
        Duration result = d.plus(-1000);
        
        assertEquals(Long.MIN_VALUE, result.getMillis());
    }

    @Test
    public void test_plus_immutabilityOriginalUnchanged() {
        // Strengthen immutability verification
        // Ensure original object is never modified
        Duration d = new Duration(5000);
        long originalMillis = d.getMillis();
        
        Duration result = d.plus(3000);
        
        // Original must be unchanged
        assertSame(5000, d.getMillis());
        assertEquals(originalMillis, d.getMillis());
        assertNotSame(d, result);
        // Result is new object with different value
        assertNotEquals(d.getMillis(), result.getMillis());
    }

    // ========================================================================
    // Duration.dividedBy(long) Tests [TI-06]
    // ========================================================================
    // Testing abstraction: ISP with BVA and Logic-based testing
    // Partitions:
    //  - Divisor = 1 (identity)
    //  - Divisor = -1 (negation)
    //  - Divisor > 1
    //  - Divisor < -1 (negative division)
    //  - Divisor = 0 (exception expected - boundary)
    // Logic predicate: if (divisor == 1) return this; else new Duration(...)
    // ========================================================================

    @Test
    public void test_dividedBy_divisorOne() {
        // Partition: Divisor = 1 (identity)
        // Logic: if (divisor == 1) return this;
        // BVA: Divisor boundary value
        Duration d = new Duration(5000);
        Duration result = d.dividedBy(1);
        
        // Should return same instance
        assertSame(d, result);
        assertEquals(5000, result.getMillis());
    }

    @Test
    public void test_dividedBy_divisorNegativeOne() {
        // Partition: Divisor = -1
        Duration d = new Duration(5000);
        Duration result = d.dividedBy(-1);
        
        assertEquals(-5000, result.getMillis());
    }

    @Test
    public void test_dividedBy_positiveDivisorTwoOrGreater() {
        // Partition: Divisor > 1
        // Logic-based: divisor != 1 → create new object
        Duration d = new Duration(10000);
        Duration result = d.dividedBy(2);
        
        assertEquals(5000, result.getMillis());
        // Verify new object created when divisor != 1
        assertNotSame(d, result);
    }

    @Test
    public void test_dividedBy_positiveDivisorWithRemainder() {
        // Partition: Positive divisor with remainder
        // BVA: Test truncation behavior
        Duration d = new Duration(10000);
        Duration result = d.dividedBy(3);
        
        assertEquals(3333, result.getMillis());  // Truncated
    }

    @Test
    public void test_dividedBy_negativeDivisor() {
        // Partition: Divisor < -1 (negative division)
        Duration d = new Duration(10000);
        Duration result = d.dividedBy(-2);
        
        assertEquals(-5000, result.getMillis());
    }

    @Test
    public void test_dividedBy_negativeDivisorWithRemainder() {
        // Partition: Negative divisor with remainder
        Duration d = new Duration(10000);
        Duration result = d.dividedBy(-3);
        
        assertEquals(-3333, result.getMillis());
    }

    @Test
    public void test_dividedBy_negativeDuration() {
        // Partition: Negative duration divided
        Duration d = new Duration(-10000);
        Duration result = d.dividedBy(2);
        
        assertEquals(-5000, result.getMillis());
    }

    @Test
    public void test_dividedBy_zeroDurationDivisor() {
        // Partition: Zero duration divided (special case)
        Duration d = new Duration(0);
        Duration result = d.dividedBy(5);
        
        assertEquals(0, result.getMillis());
    }

    @Test
    public void test_dividedBy_zeroDivisor() {
        // Partition: Divisor = 0 (logic boundary - exception case)
        // Logic: This should throw exception (ArithmeticException)
        Duration d = new Duration(5000);
        
        assertThrows(ArithmeticException.class, () -> {
            d.dividedBy(0);
        });
    }

    @Test
    public void test_dividedBy_largeDivisor() {
        // BVA: Large positive divisor
        Duration d = new Duration(10000);
        Duration result = d.dividedBy(1000);
        
        assertEquals(10, result.getMillis());
    }

    @Test
    public void test_dividedBy_resultZero() {
        // Edge case: Divisor larger than dividend
        Duration d = new Duration(5);
        Duration result = d.dividedBy(10);
        
        assertEquals(0, result.getMillis());  // Truncated to 0
    }

    @Test
    public void test_dividedBy_immutability() {
        // Verify original duration unchanged
        Duration d = new Duration(10000);
        Duration original_millis = new Duration(10000);
        
        Duration result = d.dividedBy(2);
        
        assertEquals(original_millis.getMillis(), d.getMillis());
        assertNotEquals(result.getMillis(), d.getMillis());
    }

    @Test
    public void test_dividedBy_chainedOperations() {
        // Chain division operations
        Duration d = new Duration(1000);
        Duration result = d.dividedBy(2).dividedBy(5);
        
        assertEquals(100, result.getMillis());
    }

    @Test
    public void test_dividedBy_verySmallDuration() {
        // Edge case: Small duration precision
        Duration d = new Duration(1);
        Duration result = d.dividedBy(2);
        
        assertEquals(0, result.getMillis());  // 1/2 = 0 (truncated)
    }

    @Test
    public void test_plus_then_dividedBy() {
        // Combined operations: plus then divide
        Duration d = new Duration(1000);
        Duration result = d.plus(2000).dividedBy(3);
        
        assertEquals(1000, result.getMillis());  // (1000+2000)/3 = 1000
    }
}
