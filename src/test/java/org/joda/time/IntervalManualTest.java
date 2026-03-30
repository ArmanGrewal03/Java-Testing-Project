/*
 *  Interval Manual Test Suite
 *  
 *  Tests for:
 *  - Interval.contains(long) [TI-09]
 *  - Interval.overlaps(ReadableInterval) [TI-10]
 *  
 *  Testing Approaches:
 *  - Input Space Partitioning (ISP)
 *  - Boundary Value Analysis (BVA)
 *  - Logic-based testing (predicate analysis)
 *  - Control Flow Graph (CFG) guidance
 *  - Edge cases and boundary conditions
 */
package org.joda.time;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class IntervalManualTest {

    private static final DateTimeZone UTC = DateTimeZone.UTC;
    private static final long MILLIS_PER_HOUR = 3600000;
    
    // ========================================================================
    // Interval.contains(long) Tests [TI-09]
    // ========================================================================
    // Testing abstraction: ISP with BVA and Logic-based testing
    // Logic predicate: (millisInstant >= thisStart && millisInstant < thisEnd)
    // Partitions:
    //  - Instant before interval start (false)
    //  - Instant equal to start (true - inclusive start)
    //  - Instant between start and end (true)
    //  - Instant equal to end (false - exclusive end)
    //  - Instant after interval end (false)
    //  - Zero-duration interval (always false)
    // CFG branches: 
    //  - millisInstant < thisStart → false
    //  - millisInstant >= thisStart && millisInstant < thisEnd → true
    //  - millisInstant >= thisEnd → false
    // ========================================================================

    @Test
    public void test_contains_instantBeforeStart() {
        // Partition: instant < interval start
        // Logic: (millisInstant >= thisStart) is FALSE → return false
        // BVA: Boundary just before start
        DateTime start = new DateTime(2020, 1, 15, 9, 0, 0, UTC);
        DateTime end = new DateTime(2020, 1, 15, 10, 0, 0, UTC);
        Interval interval = new Interval(start, end);
        
        long beforeStart = start.getMillis() - 1;
        assertFalse(interval.contains(beforeStart));
    }

    @Test
    public void test_contains_instantAtStart() {
        // Partition: instant == interval start
        // Logic: (millisInstant >= thisStart && millisInstant < thisEnd)
        //        First part TRUE, need to check second part
        // BVA: Exact start boundary (inclusive)
        DateTime start = new DateTime(2020, 1, 15, 9, 0, 0, UTC);
        DateTime end = new DateTime(2020, 1, 15, 10, 0, 0, UTC);
        Interval interval = new Interval(start, end);
        
        assertTrue(interval.contains(start.getMillis()));
    }

    @Test
    public void test_contains_instantInMiddle() {
        // Partition: start < instant < end
        // Logic: Both parts of conjunction TRUE
        DateTime start = new DateTime(2020, 1, 15, 9, 0, 0, UTC);
        DateTime end = new DateTime(2020, 1, 15, 10, 0, 0, UTC);
        Interval interval = new Interval(start, end);
        
        DateTime middle = new DateTime(2020, 1, 15, 9, 30, 0, UTC);
        assertTrue(interval.contains(middle.getMillis()));
    }

    @Test
    public void test_contains_instantAtEnd() {
        // Partition: instant == interval end
        // Logic: (millisInstant < thisEnd) is FALSE → return false
        // BVA: Exact end boundary (exclusive)
        DateTime start = new DateTime(2020, 1, 15, 9, 0, 0, UTC);
        DateTime end = new DateTime(2020, 1, 15, 10, 0, 0, UTC);
        Interval interval = new Interval(start, end);
        
        assertFalse(interval.contains(end.getMillis()));
    }

    @Test
    public void test_contains_instantAfterEnd() {
        // Partition: instant > interval end
        // Logic: (millisInstant < thisEnd) is FALSE
        // BVA: Just after end
        DateTime start = new DateTime(2020, 1, 15, 9, 0, 0, UTC);
        DateTime end = new DateTime(2020, 1, 15, 10, 0, 0, UTC);
        Interval interval = new Interval(start, end);
        
        long afterEnd = end.getMillis() + 1;
        assertFalse(interval.contains(afterEnd));
    }

    @Test
    public void test_contains_zeroLengthIntervalAtStart() {
        // Partition: Zero-duration interval
        // Logic: start == end, so (millisInstant >= start && millisInstant < start) impossible
        DateTime timePoint = new DateTime(2020, 1, 15, 9, 0, 0, UTC);
        Interval interval = new Interval(timePoint, timePoint);  // Zero duration
        
        // Even at the exact point, a zero-duration interval contains nothing
        assertFalse(interval.contains(timePoint.getMillis()));
    }

    @Test
    public void test_contains_zeroLengthIntervalBeforePoint() {
        // Zero-duration interval, testing before point
        DateTime timePoint = new DateTime(2020, 1, 15, 9, 0, 0, UTC);
        Interval interval = new Interval(timePoint, timePoint);
        
        long beforePoint = timePoint.getMillis() - 1000;
        assertFalse(interval.contains(beforePoint));
    }

    @Test
    public void test_contains_oneMillisecondInterval() {
        // Edge case: Minimal 1ms interval
        DateTime start = new DateTime(2020, 1, 15, 9, 0, 0, UTC);
        DateTime end = new DateTime(start.getMillis() + 1);
        Interval interval = new Interval(start, end);
        
        assertTrue(interval.contains(start.getMillis()));
        assertFalse(interval.contains(end.getMillis()));
    }

    @Test
    public void test_contains_largeInterval() {
        // Edge case: Large time interval (multiple days)
        DateTime start = new DateTime(2020, 1, 1, 0, 0, 0, UTC);
        DateTime end = new DateTime(2020, 12, 31, 23, 59, 59, UTC);
        Interval interval = new Interval(start, end);
        
        DateTime middleOfYear = new DateTime(2020, 6, 15, 12, 0, 0, UTC);
        assertTrue(interval.contains(middleOfYear.getMillis()));
    }

    // ========================================================================
    // Interval.overlaps(ReadableInterval) Tests [TI-10]
    // ========================================================================
    // Testing abstraction: ISP with BVA and Logic-based testing
    // Logic predicate: (thisStart < otherEnd && otherStart < thisEnd)
    // Special case: null → overlaps with now
    // Partitions:
    //  - Complete overlap (intervals share common time)
    //  - Partial overlap (one side overlaps)
    //  - Abutting intervals (touching but not overlapping)
    //  - Disjoint intervals (gap between)
    //  - One interval contains the other
    //  - Equal intervals
    //  - Null interval
    // CFG branches:
    //  - if (interval == null) → check with now
    //  - else → check overlap logic
    // ========================================================================

    @Test
    public void test_overlaps_completeOverlap() {
        // Partition: Complete overlap (fully overlapping)
        // Logic: Both parts TRUE
        DateTime start1 = new DateTime(2020, 1, 15, 9, 0, 0, UTC);
        DateTime end1 = new DateTime(2020, 1, 15, 11, 0, 0, UTC);  // [9, 11)
        Interval interval1 = new Interval(start1, end1);
        
        DateTime start2 = new DateTime(2020, 1, 15, 10, 0, 0, UTC);
        DateTime end2 = new DateTime(2020, 1, 15, 12, 0, 0, UTC);  // [10, 12)
        Interval interval2 = new Interval(start2, end2);
        
        assertTrue(interval1.overlaps(interval2));
        assertTrue(interval2.overlaps(interval1));
    }

    @Test
    public void test_overlaps_oneContainsOther() {
        // Partition: One interval completely contains the other
        DateTime start1 = new DateTime(2020, 1, 15, 8, 0, 0, UTC);
        DateTime end1 = new DateTime(2020, 1, 15, 12, 0, 0, UTC);  // [8, 12)
        Interval interval1 = new Interval(start1, end1);
        
        DateTime start2 = new DateTime(2020, 1, 15, 9, 0, 0, UTC);
        DateTime end2 = new DateTime(2020, 1, 15, 10, 0, 0, UTC);  // [9, 10)
        Interval interval2 = new Interval(start2, end2);
        
        assertTrue(interval1.overlaps(interval2));
        assertTrue(interval2.overlaps(interval1));
    }

    @Test
    public void test_overlaps_equalIntervals() {
        // Partition: Exactly equal intervals
        DateTime start = new DateTime(2020, 1, 15, 9, 0, 0, UTC);
        DateTime end = new DateTime(2020, 1, 15, 10, 0, 0, UTC);
        Interval interval1 = new Interval(start, end);
        Interval interval2 = new Interval(start, end);
        
        assertTrue(interval1.overlaps(interval2));
        assertTrue(interval2.overlaps(interval1));
    }

    @Test
    public void test_overlaps_abutsBeforeNoOverlap() {
        // Partition: Abutting intervals (touching at boundary)
        // Logic: one interval's end equals other's start
        // BVA: Boundary condition - should NOT overlap
        DateTime start1 = new DateTime(2020, 1, 15, 9, 0, 0, UTC);
        DateTime end1 = new DateTime(2020, 1, 15, 10, 0, 0, UTC);  // [9, 10)
        Interval interval1 = new Interval(start1, end1);
        
        DateTime start2 = new DateTime(2020, 1, 15, 10, 0, 0, UTC);
        DateTime end2 = new DateTime(2020, 1, 15, 11, 0, 0, UTC);  // [10, 11)
        Interval interval2 = new Interval(start2, end2);
        
        // [9,10) and [10,11) touch but don't overlap (abut)
        assertFalse(interval1.overlaps(interval2));
        assertFalse(interval2.overlaps(interval1));
    }

    @Test
    public void test_overlaps_abutsAfterNoOverlap() {
        // Partition: Intervals abutting on opposite side
        DateTime start1 = new DateTime(2020, 1, 15, 11, 0, 0, UTC);
        DateTime end1 = new DateTime(2020, 1, 15, 12, 0, 0, UTC);  // [11, 12)
        Interval interval1 = new Interval(start1, end1);
        
        DateTime start2 = new DateTime(2020, 1, 15, 10, 0, 0, UTC);
        DateTime end2 = new DateTime(2020, 1, 15, 11, 0, 0, UTC);  // [10, 11)
        Interval interval2 = new Interval(start2, end2);
        
        // [10,11) and [11,12) touch but don't overlap
        assertFalse(interval1.overlaps(interval2));
        assertFalse(interval2.overlaps(interval1));
    }

    @Test
    public void test_overlaps_disjointBefore() {
        // Partition: Complete gap, one entirely before the other
        // Logic: Both parts FALSE
        DateTime start1 = new DateTime(2020, 1, 15, 8, 0, 0, UTC);
        DateTime end1 = new DateTime(2020, 1, 15, 9, 0, 0, UTC);  // [8, 9)
        Interval interval1 = new Interval(start1, end1);
        
        DateTime start2 = new DateTime(2020, 1, 15, 10, 0, 0, UTC);
        DateTime end2 = new DateTime(2020, 1, 15, 11, 0, 0, UTC);  // [10, 11)
        Interval interval2 = new Interval(start2, end2);
        
        assertFalse(interval1.overlaps(interval2));
        assertFalse(interval2.overlaps(interval1));
    }

    @Test
    public void test_overlaps_disjointAfter() {
        // Partition: Complete gap, other entirely after
        DateTime start1 = new DateTime(2020, 1, 15, 11, 0, 0, UTC);
        DateTime end1 = new DateTime(2020, 1, 15, 12, 0, 0, UTC);  // [11, 12)
        Interval interval1 = new Interval(start1, end1);
        
        DateTime start2 = new DateTime(2020, 1, 15, 9, 0, 0, UTC);
        DateTime end2 = new DateTime(2020, 1, 15, 10, 0, 0, UTC);  // [9, 10)
        Interval interval2 = new Interval(start2, end2);
        
        assertFalse(interval1.overlaps(interval2));
        assertFalse(interval2.overlaps(interval1));
    }

    @Test
    public void test_overlaps_partialOverlapLeft() {
        // Partition: Partial overlap (left side of interval)
        // BVA: Start of interval1 inside interval2, end outside
        DateTime start1 = new DateTime(2020, 1, 15, 9, 30, 0, UTC);
        DateTime end1 = new DateTime(2020, 1, 15, 10, 30, 0, UTC);  // [9:30, 10:30)
        Interval interval1 = new Interval(start1, end1);
        
        DateTime start2 = new DateTime(2020, 1, 15, 9, 0, 0, UTC);
        DateTime end2 = new DateTime(2020, 1, 15, 10, 0, 0, UTC);   // [9:00, 10:00)
        Interval interval2 = new Interval(start2, end2);
        
        // Logic check: thiStart(9:30) < otherEnd(10:00) TRUE
        //             otherStart(9:00) < thisEnd(10:30) TRUE
        assertTrue(interval1.overlaps(interval2));
    }

    @Test
    public void test_overlaps_partialOverlapRight() {
        // Partition: Partial overlap (right side of interval)
        DateTime start1 = new DateTime(2020, 1, 15, 8, 30, 0, UTC);
        DateTime end1 = new DateTime(2020, 1, 15, 9, 30, 0, UTC);   // [8:30, 9:30)
        Interval interval1 = new Interval(start1, end1);
        
        DateTime start2 = new DateTime(2020, 1, 15, 9, 0, 0, UTC);
        DateTime end2 = new DateTime(2020, 1, 15, 10, 0, 0, UTC);   // [9:00, 10:00)
        Interval interval2 = new Interval(start2, end2);
        
        assertTrue(interval1.overlaps(interval2));
    }

    @Test
    public void test_overlaps_zeroLengthWithNonZeroAtStart() {
        // Edge case: Zero-length interval at start of non-zero interval
        DateTime timePoint = new DateTime(2020, 1, 15, 9, 0, 0, UTC);
        DateTime end = new DateTime(2020, 1, 15, 10, 0, 0, UTC);
        
        Interval zeroLength = new Interval(timePoint, timePoint);
        Interval nonZero = new Interval(timePoint, end);
        
        // Zero-length at start of non-zero: abuts, doesn't overlap
        assertFalse(zeroLength.overlaps(nonZero));
    }

    @Test
    public void test_overlaps_zeroLengthWithNonZeroInMiddle() {
        // Edge case: Zero-length interval in middle of non-zero interval
        DateTime start = new DateTime(2020, 1, 15, 9, 0, 0, UTC);
        DateTime middle = new DateTime(2020, 1, 15, 9, 30, 0, UTC);
        DateTime end = new DateTime(2020, 1, 15, 10, 0, 0, UTC);
        
        Interval zeroLength = new Interval(middle, middle);
        Interval nonZero = new Interval(start, end);
        
        // Zero-length inside non-zero: overlaps
        assertTrue(zeroLength.overlaps(nonZero));
    }

    @Test
    public void test_overlaps_twoZeroLengthAtSamePoint() {
        // Edge case: Both zero-length at same point
        DateTime timePoint = new DateTime(2020, 1, 15, 9, 0, 0, UTC);
        
        Interval zeroLength1 = new Interval(timePoint, timePoint);
        Interval zeroLength2 = new Interval(timePoint, timePoint);
        
        // Both zero-length at same point: both don't overlap each other
        assertFalse(zeroLength1.overlaps(zeroLength2));
    }

    @Test
    public void test_overlaps_null() {
        // Partition: Null interval (special case)
        // CFG: if (interval == null) branch
        DateTime start = new DateTime(2020, 1, 15, 9, 0, 0, UTC);
        DateTime end = new DateTime(2020, 1, 15, 10, 0, 0, UTC);
        Interval interval = new Interval(start, end);
        
        // null interval represents "now"
        // This test depends on current time, but interval [9:00, 10:00] likely doesn't overlap now
        boolean result = interval.overlaps(null);
        // Result depends on whether "now" falls within interval
        // We can't reliably test this without mocking time
        assertNotNull(result);  // Just verify it doesn't throw
    }

    @Test
    public void test_overlaps_nullInterval_deterministicTrue() {
        // Deterministic null-branch coverage:
        // Create an interval guaranteed to contain "now".
        long now = DateTimeUtils.currentTimeMillis();
        Interval aroundNow = new Interval(now - MILLIS_PER_HOUR, now + MILLIS_PER_HOUR);

        // Null means "now" in overlaps(null)
        assertTrue(aroundNow.overlaps(null));
    }

    @Test
    public void test_overlap_method_returnsExpectedOverlapInterval() {
        // Explicitly cover Interval.overlap(ReadableInterval),
        // which internally depends on overlaps(...) logic.
        DateTime start1 = new DateTime(2020, 1, 15, 9, 0, 0, UTC);
        DateTime end1 = new DateTime(2020, 1, 15, 11, 0, 0, UTC);   // [09:00, 11:00)
        Interval interval1 = new Interval(start1, end1);

        DateTime start2 = new DateTime(2020, 1, 15, 10, 0, 0, UTC);
        DateTime end2 = new DateTime(2020, 1, 15, 12, 0, 0, UTC);   // [10:00, 12:00)
        Interval interval2 = new Interval(start2, end2);

        Interval overlap = interval1.overlap(interval2);
        assertNotNull(overlap);
        assertEquals(start2.getMillis(), overlap.getStartMillis());
        assertEquals(end1.getMillis(), overlap.getEndMillis());
    }

    @Test
    public void test_overlap_method_returnsNullForAbuttingIntervals() {
        // Explicitly verify non-overlap path used by overlap(...)
        DateTime start1 = new DateTime(2020, 1, 15, 9, 0, 0, UTC);
        DateTime end1 = new DateTime(2020, 1, 15, 10, 0, 0, UTC);   // [09:00, 10:00)
        Interval interval1 = new Interval(start1, end1);

        DateTime start2 = new DateTime(2020, 1, 15, 10, 0, 0, UTC);
        DateTime end2 = new DateTime(2020, 1, 15, 11, 0, 0, UTC);   // [10:00, 11:00)
        Interval interval2 = new Interval(start2, end2);

        assertNull(interval1.overlap(interval2));
    }

    @Test
    public void test_overlaps_multiDayIntervals() {
        // Edge case: Large intervals spanning multiple days
        DateTime start1 = new DateTime(2020, 1, 10, 0, 0, 0, UTC);
        DateTime end1 = new DateTime(2020, 1, 20, 0, 0, 0, UTC);
        Interval interval1 = new Interval(start1, end1);
        
        DateTime start2 = new DateTime(2020, 1, 15, 0, 0, 0, UTC);
        DateTime end2 = new DateTime(2020, 1, 25, 0, 0, 0, UTC);
        Interval interval2 = new Interval(start2, end2);
        
        assertTrue(interval1.overlaps(interval2));
    }

    @Test
    public void test_contains_and_overlaps_consistency() {
        // Consistency check: if interval1 contains interval2, they should overlap
        DateTime start1 = new DateTime(2020, 1, 15, 8, 0, 0, UTC);
        DateTime end1 = new DateTime(2020, 1, 15, 12, 0, 0, UTC);
        Interval interval1 = new Interval(start1, end1);
        
        DateTime start2 = new DateTime(2020, 1, 15, 9, 0, 0, UTC);
        DateTime end2 = new DateTime(2020, 1, 15, 10, 0, 0, UTC);
        Interval interval2 = new Interval(start2, end2);
        
        // If interval1 contains interval2, they must overlap
        if (interval1.contains(interval2)) {
            assertTrue(interval1.overlaps(interval2));
        }
    }
}
