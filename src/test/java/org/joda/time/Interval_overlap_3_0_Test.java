package org.joda.time;

import org.mockito.junit.jupiter.MockitoExtension;
import java.lang.reflect.Method;
import org.joda.time.ReadableInterval;
import org.joda.time.Interval;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import java.io.Serializable;
import org.joda.time.base.BaseInterval;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

@ExtendWith(MockitoExtension.class)
public class Interval_overlap_3_0_Test {

    @Mock
    private ReadableInterval mockInterval;

    private Interval interval = new Interval(100, 200);

    @Test
    public void testOverlap() throws Exception {
        // Arrange
        Method overlapMethod = Interval.class.getDeclaredMethod("overlap", ReadableInterval.class);
        overlapMethod.setAccessible(true);
        ReadableInterval mockReadableInterval = DateTimeUtils.getReadableInterval(mockInterval);
        // Act
        Object result = overlapMethod.invoke(interval, mockReadableInterval);
        // Assert
        if (result == null) {
            assertEquals(-1, interval.overlap(mockInterval));
        } else {
            assertEquals(((Interval) result).getStartMillis(), interval.overlap(mockInterval));
        }
    }

    @Test
    public void testOverlapNull() throws Exception {
        // Arrange
        Method overlapMethod = Interval.class.getDeclaredMethod("overlap", ReadableInterval.class);
        overlapMethod.setAccessible(true);
        ReadableInterval mockReadableInterval = null;
        // Act
        Object result = overlapMethod.invoke(interval, mockReadableInterval);
        // Assert
        assertEquals(-1, interval.overlap(mockInterval));
    }
}
