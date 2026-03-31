package org.joda.time;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Section 5.2 manual baseline suite (before ChatUniTest generation).
 *
 * Selected class: Interval
 * Selected methods (3):
 * 1) Interval.overlap(ReadableInterval)
 * 2) Interval.gap(ReadableInterval)
 * 3) Interval.abuts(ReadableInterval)
 *
 * Rationale:
 * - All three methods are from a single class and contain branching/predicate logic.
 * - They are part of the project's manually tested method set.
 * - They support later structural + mutation comparison against LLM-generated tests.
 */
public class LLMComponentManualBaselineTest {

    // ---------------------------------------------------------------------
    // Method 1: Interval.overlap(ReadableInterval)
    // ISP partitions: {overlap}, {abutting}, {disjoint}, {null input}
    // CFG: overlaps(interval)==false branch and true branch.
    // DFG: start/end definitions flow to Math.max/Math.min and returned interval.
    // ---------------------------------------------------------------------

    @Test
    public void baseline_overlap_returnsExpectedIntersection() {
        Interval base = new Interval(10L, 30L);
        Interval other = new Interval(20L, 40L);

        Interval result = base.overlap(other);

        assertNotNull(result);
        assertEquals(20L, result.getStartMillis());
        assertEquals(30L, result.getEndMillis());
    }

    @Test
    public void baseline_overlap_abuttingIntervals_returnsNull() {
        Interval base = new Interval(10L, 20L);
        Interval other = new Interval(20L, 30L);

        assertNull(base.overlap(other));
    }

    @Test
    public void baseline_overlap_disjointIntervals_returnsNull() {
        Interval base = new Interval(10L, 20L);
        Interval other = new Interval(25L, 30L);

        assertNull(base.overlap(other));
    }

    @Test
    public void baseline_overlap_nullInterval_nowInside_returnsZeroLengthAtNow() {
        long now = DateTimeUtils.currentTimeMillis();
        Interval aroundNow = new Interval(now - 5_000L, now + 5_000L);

        Interval result = aroundNow.overlap(null);

        assertNotNull(result);
        assertEquals(result.getStartMillis(), result.getEndMillis());
    }

    // ---------------------------------------------------------------------
    // Method 2: Interval.gap(ReadableInterval)
    // ISP partitions: {this after other}, {other after this}, {overlap}, {abutting}, {null input}
    // CFG: if(thisStart>otherEnd), else-if(otherStart>thisEnd), else-null.
    // DFG: thisStart/thisEnd/otherStart/otherEnd definitions feed branch decisions and gap bounds.
    // ---------------------------------------------------------------------

    @Test
    public void baseline_gap_thisAfterOther_returnsGap() {
        Interval base = new Interval(20L, 30L);
        Interval other = new Interval(5L, 10L);

        Interval gap = base.gap(other);

        assertNotNull(gap);
        assertEquals(10L, gap.getStartMillis());
        assertEquals(20L, gap.getEndMillis());
    }

    @Test
    public void baseline_gap_otherAfterThis_returnsGap() {
        Interval base = new Interval(10L, 20L);
        Interval other = new Interval(30L, 40L);

        Interval gap = base.gap(other);

        assertNotNull(gap);
        assertEquals(20L, gap.getStartMillis());
        assertEquals(30L, gap.getEndMillis());
    }

    @Test
    public void baseline_gap_overlapping_returnsNull() {
        Interval base = new Interval(10L, 25L);
        Interval other = new Interval(20L, 30L);

        assertNull(base.gap(other));
    }

    @Test
    public void baseline_gap_abutting_returnsNull() {
        Interval base = new Interval(10L, 20L);
        Interval other = new Interval(20L, 30L);

        assertNull(base.gap(other));
    }

    // ---------------------------------------------------------------------
    // Method 3: Interval.abuts(ReadableInterval)
    // ISP partitions: {null input}, {left-abut}, {right-abut}, {overlap}, {disjoint}
    // Logic/Clause coverage:
    // - null branch: (start==now || end==now)
    // - non-null branch: (otherEnd==thisStart || thisEnd==otherStart)
    // ---------------------------------------------------------------------

    @Test
    public void baseline_abuts_leftAbut_true() {
        Interval base = new Interval(10L, 20L);
        Interval other = new Interval(5L, 10L);

        assertTrue(base.abuts(other));
    }

    @Test
    public void baseline_abuts_rightAbut_true() {
        Interval base = new Interval(10L, 20L);
        Interval other = new Interval(20L, 25L);

        assertTrue(base.abuts(other));
    }

    @Test
    public void baseline_abuts_overlap_false() {
        Interval base = new Interval(10L, 20L);
        Interval other = new Interval(15L, 25L);

        assertFalse(base.abuts(other));
    }

    @Test
    public void baseline_abuts_disjoint_false() {
        Interval base = new Interval(10L, 20L);
        Interval other = new Interval(30L, 40L);

        assertFalse(base.abuts(other));
    }

    @Test
    public void baseline_abuts_nullInput_falseWhenNowOutside() {
        long now = DateTimeUtils.currentTimeMillis();
        Interval beforeNow = new Interval(now - 5_000L, now - 1_000L);

        assertFalse(beforeNow.abuts(null));
    }
}
