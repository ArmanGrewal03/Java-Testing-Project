package org.joda.time;

import org.joda.time.DateTimeUtils;
import org.joda.time.Interval;
import org.joda.time.ReadableInterval;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.Serializable;
import org.joda.time.base.BaseInterval;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

public class Interval_gap_4_0_Test {

    private Interval interval;

    @BeforeEach
    public void setUp() {
        interval = new Interval(0, 10000, ISOChronology.getInstanceUTC());
    }

    @Test
    public void testGapWithNullInterval() {
        ReadableInterval nullInterval = null;
        assertNull(interval.gap(nullInterval));
    }

    @Test
    public void testGapWithEmptyInterval() {
        Interval emptyInterval = new Interval(10000, 10000, ISOChronology.getInstanceUTC());
        assertEquals(emptyInterval, interval.gap(emptyInterval));
    }

    @Test
    public void testGapWithOverlappingIntervals() {
        Interval overlappingInterval = new Interval(5000, 15000, ISOChronology.getInstanceUTC());
        assertNull(interval.gap(overlappingInterval));
    }

    @Test
    public void testGapWithBeforeIntervals() {
        Interval beforeInterval = new Interval(-10000, -5000, ISOChronology.getInstanceUTC());
        assertEquals(beforeInterval, interval.gap(beforeInterval));
    }

    @Test
    public void testGapWithAfterIntervals() {
        Interval afterInterval = new Interval(15000, 20000, ISOChronology.getInstanceUTC());
        assertEquals(afterInterval, interval.gap(afterInterval));
    }
}
