package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SleepTrackerAppTest {

    private List<SleepingSession> testSessions;

    @BeforeEach
    void setUp() {
        testSessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 23, 15),
                        LocalDateTime.of(2025, 10, 2, 7, 30),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 23, 50),
                        LocalDateTime.of(2025, 10, 3, 6, 40),
                        SleepQuality.NORMAL
                ),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 3, 14, 10),
                        LocalDateTime.of(2025, 10, 3, 15, 0),
                        SleepQuality.NORMAL
                ),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 3, 23, 40),
                        LocalDateTime.of(2025, 10, 4, 8, 0),
                        SleepQuality.BAD
                )
        );
    }


    @Test
    void testTotalSessions() {
        TotalSessionsFunction function = new TotalSessionsFunction();
        SleepAnalysisResult result = function.apply(testSessions);
        assertEquals(4, result.getValue());

        assertEquals("Общее количество сессий сна", result.getDescription());
    }

    @Test
    void testTotalSessionsEmpty() {
        TotalSessionsFunction function = new TotalSessionsFunction();
        SleepAnalysisResult result = function.apply(Arrays.asList());
        assertEquals(0, result.getValue());
    }


    @Test
    void testMinDuration() {
        MinDurationFunction function = new MinDurationFunction();
        SleepAnalysisResult result = function.apply(testSessions);

        assertEquals(Integer.valueOf(50), result.getValue());
    }

    @Test
    void testMinDurationSingleSession() {
        List<SleepingSession> single = Arrays.asList(testSessions.get(0));
        MinDurationFunction function = new MinDurationFunction();
        SleepAnalysisResult result = function.apply(single);
        assertEquals(Integer.valueOf(495), result.getValue());
    }


    @Test
    void testMaxDuration() {
        MaxDurationFunction function = new MaxDurationFunction();
        SleepAnalysisResult result = function.apply(testSessions);
        assertEquals(Integer.valueOf(500), result.getValue());
    }

    @Test
    void testMaxDurationWithDaytime() {
        MaxDurationFunction function = new MaxDurationFunction();
        SleepAnalysisResult result = function.apply(testSessions);
        assertEquals(Integer.valueOf(500), result.getValue());
    }


    @Test
    void testAverageDuration() {
        AverageDurationFunction function = new AverageDurationFunction();
        SleepAnalysisResult result = function.apply(testSessions);
        assertEquals(Integer.valueOf(364), result.getValue());
    }

    @Test
    void testAverageDurationTwoSessions() {
        List<SleepingSession> twoSessions = Arrays.asList(testSessions.get(0), testSessions.get(1));
        AverageDurationFunction function = new AverageDurationFunction();
        SleepAnalysisResult result = function.apply(twoSessions);
        assertEquals(Integer.valueOf(453), result.getValue());
    }

    @Test
    void testBadQualityCount() {
        BadQualitySessionsFunction function = new BadQualitySessionsFunction();
        SleepAnalysisResult result = function.apply(testSessions);
        assertEquals(Integer.valueOf(1), result.getValue());
    }

    @Test
    void testNoBadQuality() {
        List<SleepingSession> goodSessions = Arrays.asList(testSessions.get(0), testSessions.get(1));
        BadQualitySessionsFunction function = new BadQualitySessionsFunction();
        SleepAnalysisResult result = function.apply(goodSessions);
        assertEquals(Integer.valueOf(0), result.getValue());
    }

    @Test
    void testSleeplessNights() {
        SleeplessNightsFunction function = new SleeplessNightsFunction();
        SleepAnalysisResult result = function.apply(testSessions);
        assertNotNull(result.getValue());
        assertTrue(result.getValue() instanceof Integer);
    }

    @Test
    void testSleeplessNightsEmptyList() {
        SleeplessNightsFunction function = new SleeplessNightsFunction();
        SleepAnalysisResult result = function.apply(Arrays.asList());
        assertEquals(0, result.getValue());
    }

    @Test
    void testNightSleepOnly() {
        List<SleepingSession> nightOnly = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 23, 0),
                        LocalDateTime.of(2025, 10, 2, 7, 0),
                        SleepQuality.GOOD
                )
        );
        SleeplessNightsFunction function = new SleeplessNightsFunction();
        SleepAnalysisResult result = function.apply(nightOnly);
        assertEquals(0, result.getValue());
    }

    @Test
    void testSleeplessNightCase() {
        List<SleepingSession> onlyDaytime = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 7, 0),
                        LocalDateTime.of(2025, 10, 1, 11, 0),
                        SleepQuality.GOOD
                )
        );
        SleeplessNightsFunction function = new SleeplessNightsFunction();
        SleepAnalysisResult result = function.apply(onlyDaytime);
        assertEquals(1, result.getValue());
    }


    @Test
    void testChronotypeFunction() {
        ChronoTypeFunction function = new ChronoTypeFunction();
        SleepAnalysisResult result = function.apply(testSessions);
        assertNotNull(result.getValue());
        assertTrue(result.getValue() instanceof String);
    }

    @Test
    void testChronotypeWithDifferentPatterns() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 23, 30),
                        LocalDateTime.of(2025, 10, 2, 9, 30),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 21, 30),
                        LocalDateTime.of(2025, 10, 3, 6, 30),
                        SleepQuality.GOOD
                )
        );

        ChronoTypeFunction function = new ChronoTypeFunction();
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals("Голубь", result.getValue());
    }
}