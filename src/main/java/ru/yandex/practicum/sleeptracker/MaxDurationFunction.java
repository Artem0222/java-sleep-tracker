package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class MaxDurationFunction implements AnalysisFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long maxDuration = sessions.stream()
                .mapToLong(SleepingSession::getDurationMinutes)
                .max()
                .orElse(0);
        return new SleepAnalysisResult("Максимальная продолжительность сессии (минуты)", (int) maxDuration);
    }
}