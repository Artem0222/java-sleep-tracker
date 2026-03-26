package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class AverageDurationFunction implements AnalysisFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {

        double avgDuration = sessions.stream()
                .mapToLong(SleepingSession::getDurationMinutes)
                .average()
                .orElse(0);
        return new SleepAnalysisResult("Средняя продолжительность сессии (минуты)", (int) Math.round(avgDuration));
    }
}