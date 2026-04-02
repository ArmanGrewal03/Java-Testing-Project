package org.joda.time;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Interval_gap_4_0_Test {

    @Test
    void gapAfter() {
        Interval base = new Interval(0, 10000);
        Interval later = new Interval(15000, 20000);
        assertEquals(new Interval(10000, 15000), base.gap(later));
    }

    @Test
    void gapBefore() {
        Interval base = new Interval(0, 10000);
        Interval earlier = new Interval(-10000, -5000);
        assertEquals(new Interval(-5000, 0), base.gap(earlier));
    }

    @Test
    void overlapNull() {
        Interval base = new Interval(0, 10000);
        Interval overlap = new Interval(5000, 15000);
        assertNull(base.gap(overlap));
    }

    @Test
    void abuttingNull() {
        Interval base = new Interval(0, 10000);
        Interval abutting = new Interval(10000, 15000);
        assertNull(base.gap(abutting));
    }

    @Test
    void identicalNull() {
        Interval base = new Interval(0, 10000);
        Interval identical = new Interval(0, 10000);
        assertNull(base.gap(identical));
    }

    @Test
    void zeroLengthBoundaryNull() {
        Interval base = new Interval(0, 10000);
        Interval zeroLength = new Interval(10000, 10000);
        assertNull(base.gap(zeroLength));
    }
}
