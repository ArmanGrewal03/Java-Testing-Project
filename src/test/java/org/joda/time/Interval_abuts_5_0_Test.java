package org.joda.time;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInterval;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.tz.FixedDateTimeZone;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.Serializable;
import org.joda.time.base.BaseInterval;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

public class Interval_abuts_5_0_Test {

    @Test
    void testAbuts() throws Exception {
        // Test with null interval
        Interval abuts = new Interval(0, 1000);
        ReadableInterval interval = null;
        boolean result = (Boolean) abuts.getClass().getMethod("abuts", ReadableInterval.class).invoke(abuts, interval);
        assertTrue(result);
        // Test with intervals that abut at the start
        interval = new Interval(500, 1500);
        result = (Boolean) abuts.getClass().getMethod("abuts", ReadableInterval.class).invoke(abuts, interval);
        assertFalse(result);
        // Test with intervals that abut at the end
        interval = new Interval(-500, 500);
        result = (Boolean) abuts.getClass().getMethod("abuts", ReadableInterval.class).invoke(abuts, interval);
        assertFalse(result);
        // Test with intervals that do not abut
        interval = new Interval(2000, 3000);
        result = (Boolean) abuts.getClass().getMethod("abuts", ReadableInterval.class).invoke(abuts, interval);
        assertFalse(result);
    }
}
