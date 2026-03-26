package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class BadQualitySessionsFunction implements AnalysisFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long badCount = sessions.stream()
                .filter(session -> session.getQuality() == SleepQuality.BAD)
                .count();
        return new SleepAnalysisResult("Количество сессий с плохим качеством сна", (int) badCount);
    }
}