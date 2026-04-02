package org.joda.time;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Interval_abuts_5_0_Test {

    private final Interval base = new Interval(0, 1000);

    @Test
    void adjacentBeforeTrue() {
        assertTrue(base.abuts(new Interval(-500, 0)));
    }

    @Test
    void adjacentAfterTrue() {
        assertTrue(base.abuts(new Interval(1000, 1500)));
    }

    @Test
    void overlapFalse() {
        assertFalse(base.abuts(new Interval(500, 1500)));
    }

    @Test
    void separatedGapFalse() {
        assertFalse(base.abuts(new Interval(2000, 3000)));
    }

    @Test
    void zeroLengthAtStartTrue() {
        assertTrue(base.abuts(new Interval(0, 0)));
    }

    @Test
    void zeroLengthAtEndTrue() {
        assertTrue(base.abuts(new Interval(1000, 1000)));
    }

    @Test
    void nullIntervalDeterministicNowCases() {
        try {
            DateTimeUtils.setCurrentMillisFixed(0);
            assertTrue(base.abuts(null));

            DateTimeUtils.setCurrentMillisFixed(1000);
            assertTrue(base.abuts(null));

            DateTimeUtils.setCurrentMillisFixed(500);
            assertFalse(base.abuts(null));
        } finally {
            DateTimeUtils.setCurrentMillisSystem();
        }
    }
}
