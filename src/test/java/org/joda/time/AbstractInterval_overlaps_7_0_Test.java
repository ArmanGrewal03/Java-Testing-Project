package org.joda.time;

import org.joda.time.Interval;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbstractInterval_overlaps_7_0_Test {

    private final Interval interval = new Interval(0, 10);

    @Test
    void overlapsTrue() {
        Interval other = new Interval(5, 15);
        assertTrue(interval.overlaps(other));
    }

    @Test
    void abutsFalse() {
        Interval other = new Interval(10, 20);
        assertFalse(interval.overlaps(other));
    }

    @Test
    void disjointBeforeFalse() {
        Interval other = new Interval(-5, 0);
        assertFalse(interval.overlaps(other));
    }

    @Test
    void disjointAfterFalse() {
        Interval other = new Interval(15, 25);
        assertFalse(interval.overlaps(other));
    }

    @Test
    void identicalTrue() {
        Interval other = new Interval(0, 10);
        assertTrue(interval.overlaps(other));
    }

    @Test
    void zeroLengthAtBoundaryFalse() {
        Interval other = new Interval(10, 10);
        assertFalse(interval.overlaps(other));
    }
}
