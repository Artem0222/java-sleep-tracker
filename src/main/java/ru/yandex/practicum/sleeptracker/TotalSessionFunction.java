package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class TotalSessionFunction implements AnalysisFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        return new SleepAnalysisResult("Общее количество сессий", sessions.size());
    }
}
