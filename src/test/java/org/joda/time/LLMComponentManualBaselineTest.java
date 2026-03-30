package org.joda.time;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Section 5.2 manual baseline suite (before ChatUniTest generation).
 *
 * Selected methods (3):
 * 1) DateTime.plusDays(int)
 * 2) LocalDate.plusMonths(int)
 * 3) Interval.overlaps(ReadableInterval)
 *
 * Rationale:
 * - All three methods contain meaningful branching/logic and boundary-sensitive behavior.
 * - They are part of the project's manually tested method set.
 * - They support later structural + mutation comparison against LLM-generated tests.
 */
public class LLMComponentManualBaselineTest {

    private static final DateTimeZone UTC = DateTimeZone.UTC;

    // ---------------------------------------------------------------------
    // Method 1: DateTime.plusDays(int)
    // ISP partitions: {0}, {positive}, {negative}, {year boundary}, {leap boundary}
    // CFG/Logic: decision days == 0 with both true/false outcomes.
    // DFG focus: parameter days reaches chronology.add(...) on non-zero path.
    // ---------------------------------------------------------------------

    @Test
    public void baseline_plusDays_zero_identityPath() {
        DateTime dt = new DateTime(2020, 6, 15, 10, 30, 0, UTC);
        DateTime result = dt.plusDays(0);

        // Logic/decision check for the explicit guard branch.
        assertSame(dt, result);
    }

    @Test
    public void baseline_plusDays_nonZero_newInstancePath() {
        DateTime dt = new DateTime(2020, 6, 15, 10, 30, 0, UTC);
        DateTime result = dt.plusDays(1);

        // CFG false branch of (days == 0).
        assertNotSame(dt, result);
        assertEquals(16, result.getDayOfMonth());
    }

    @Test
    public void baseline_plusDays_positive_yearBoundary() {
        DateTime dt = new DateTime(2020, 12, 30, 12, 0, 0, UTC);
        DateTime result = dt.plusDays(3);

        assertEquals(2021, result.getYear());
        assertEquals(1, result.getMonthOfYear());
        assertEquals(2, result.getDayOfMonth());
    }

    @Test
    public void baseline_plusDays_negative_monthBoundary() {
        DateTime dt = new DateTime(2020, 6, 2, 12, 0, 0, UTC);
        DateTime result = dt.plusDays(-4);

        assertEquals(2020, result.getYear());
        assertEquals(5, result.getMonthOfYear());
        assertEquals(29, result.getDayOfMonth());
    }

    @Test
    public void baseline_plusDays_leapYear_transition() {
        DateTime dt = new DateTime(2020, 2, 28, 12, 0, 0, UTC);
        DateTime result = dt.plusDays(1);

        assertEquals(2020, result.getYear());
        assertEquals(2, result.getMonthOfYear());
        assertEquals(29, result.getDayOfMonth());
    }

    @Test
    public void baseline_plusDays_nonLeap_transition() {
        DateTime dt = new DateTime(2021, 2, 28, 12, 0, 0, UTC);
        DateTime result = dt.plusDays(1);

        assertEquals(2021, result.getYear());
        assertEquals(3, result.getMonthOfYear());
        assertEquals(1, result.getDayOfMonth());
    }

    // ---------------------------------------------------------------------
    // Method 2: LocalDate.plusMonths(int)
    // ISP partitions: {0}, {positive}, {negative}, {year boundary}, {month-end clamp}
    // CFG/Logic: decision months == 0 with both true/false outcomes.
    // DFG focus: parameter months reaches chronology.add(...) on non-zero path.
    // BVA focus: Jan 31 in leap/non-leap scenarios.
    // ---------------------------------------------------------------------

    @Test
    public void baseline_plusMonths_zero_identityByValue() {
        LocalDate d = new LocalDate(2020, 6, 15);
        LocalDate result = d.plusMonths(0);

        assertSame(d, result);
    }

    @Test
    public void baseline_plusMonths_nonZero_newInstancePath() {
        LocalDate d = new LocalDate(2020, 6, 15);
        LocalDate result = d.plusMonths(1);

        // CFG false branch of (months == 0).
        assertNotSame(d, result);
        assertEquals(7, result.getMonthOfYear());
    }

    @Test
    public void baseline_plusMonths_yearBoundary_forward() {
        LocalDate d = new LocalDate(2020, 11, 15);
        LocalDate result = d.plusMonths(3);

        assertEquals(2021, result.getYear());
        assertEquals(2, result.getMonthOfYear());
        assertEquals(15, result.getDayOfMonth());
    }

    @Test
    public void baseline_plusMonths_monthEndClamp_nonLeap() {
        LocalDate d = new LocalDate(2021, 1, 31);
        LocalDate result = d.plusMonths(1);

        assertEquals(2021, result.getYear());
        assertEquals(2, result.getMonthOfYear());
        assertEquals(28, result.getDayOfMonth());
    }

    @Test
    public void baseline_plusMonths_monthEndClamp_leapYear() {
        LocalDate d = new LocalDate(2020, 1, 31);
        LocalDate result = d.plusMonths(1);

        assertEquals(2020, result.getYear());
        assertEquals(2, result.getMonthOfYear());
        assertEquals(29, result.getDayOfMonth());
    }

    @Test
    public void baseline_plusMonths_negative_yearBoundary() {
        LocalDate d = new LocalDate(2020, 3, 31);
        LocalDate result = d.plusMonths(-4);

        assertEquals(2019, result.getYear());
        assertEquals(11, result.getMonthOfYear());
        assertEquals(30, result.getDayOfMonth());
    }

    // ---------------------------------------------------------------------
    // Method 3: Interval.overlaps(ReadableInterval)
    // ISP partitions: {overlap}, {abutting}, {disjoint}, {null=true}, {null=false}
    // Logic/Clause coverage:
    // - null branch predicate: (thisStart < now && now < thisEnd)
    // - non-null predicate: (thisStart < otherEnd && otherStart < thisEnd)
    // Includes clause-level true/false combinations to exercise decision behavior.
    // ---------------------------------------------------------------------

    @Test
    public void baseline_overlaps_nonNull_TT_true() {
        Interval base = new Interval(10L, 20L);
        Interval other = new Interval(15L, 25L);

        // C1: thisStart < otherEnd  => 10 < 25 (T)
        // C2: otherStart < thisEnd  => 15 < 20 (T)
        assertTrue(base.overlaps(other));
    }

    @Test
    public void baseline_overlaps_nonNull_TF_false() {
        Interval base = new Interval(10L, 20L);
        Interval other = new Interval(20L, 30L);

        // C1: 10 < 30 (T)
        // C2: 20 < 20 (F) => abutting right boundary
        assertFalse(base.overlaps(other));
    }

    @Test
    public void baseline_overlaps_nonNull_FT_false() {
        Interval base = new Interval(10L, 20L);
        Interval other = new Interval(5L, 10L);

        // C1: 10 < 10 (F)
        // C2: 5 < 20  (T) => abutting left boundary
        assertFalse(base.overlaps(other));
    }

    @Test
    public void baseline_overlaps_nullInput_branch() {
        long now = DateTimeUtils.currentTimeMillis();
        Interval aroundNow = new Interval(now - 1_000L, now + 1_000L);

        // null input follows the dedicated null-path semantics in Interval.overlaps.
        assertTrue(aroundNow.overlaps(null));
    }

    @Test
    public void baseline_overlaps_nullInput_false() {
        long now = DateTimeUtils.currentTimeMillis();
        Interval beforeNow = new Interval(now - 2_000L, now - 1_000L);

        // null branch predicate false when now is outside interval.
        assertFalse(beforeNow.overlaps(null));
    }
}
