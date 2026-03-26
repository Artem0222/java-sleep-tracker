package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

public class SleeplessNightsFunction implements AnalysisFunction {

    private static final LocalTime NIGHT_START = LocalTime.of(0, 0);
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Количество бессонных ночей", 0);
        }


        LocalDateTime firstSessionStart = sessions.stream()
                .map(SleepingSession::getStartTime)
                .min(LocalDateTime::compareTo)
                .get();

        LocalDateTime lastSessionEnd = sessions.stream()
                .map(SleepingSession::getEndTime)
                .max(LocalDateTime::compareTo)
                .get();


        LocalDate startDate = adjustStartDate(firstSessionStart);
        LocalDate endDate = lastSessionEnd.toLocalDate();

        long totalNights = Period.between(startDate, endDate).getDays();


        List<LocalDate> nightsWithSleep = sessions.stream()
                .filter(this::isNightSleep)
                .map(session -> getNightDate(session.getStartTime()))
                .distinct()
                .collect(Collectors.toList());

        long sleeplessNights = totalNights - nightsWithSleep.size();

        return new SleepAnalysisResult("Количество бессонных ночей", sleeplessNights);
    }

    private boolean isNightSleep(SleepingSession session) {
        LocalTime startTime = session.getStartTime().toLocalTime();
        LocalTime endTime = session.getEndTime().toLocalTime();
        LocalDate startDate = session.getStartTime().toLocalDate();
        LocalDate endDate = session.getEndTime().toLocalDate();


        if (startDate.isBefore(endDate)) {
            return true;
        }


        if (startDate.equals(endDate)) {
            return (startTime.isBefore(NIGHT_END) && endTime.isAfter(NIGHT_START)) ||
                    (startTime.isBefore(NIGHT_START) && endTime.isAfter(NIGHT_START));
        }

        return false;
    }

    private LocalDate getNightDate(LocalDateTime sessionStart) {
        LocalTime time = sessionStart.toLocalTime();
        LocalDate date = sessionStart.toLocalDate();


        if (time.isAfter(LocalTime.NOON)) {
            return date.plusDays(1);
        }
        return date;
    }

    private LocalDate adjustStartDate(LocalDateTime firstSessionStart) {
        LocalTime time = firstSessionStart.toLocalTime();
        LocalDate date = firstSessionStart.toLocalDate();


        if (time.isAfter(LocalTime.NOON)) {
            return date.plusDays(1);
        }
        return date;
    }
}