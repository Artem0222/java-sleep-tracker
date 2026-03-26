package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SleeplessNightsFunction implements AnalysisFunction {

    private static final LocalTime NIGHT_START = LocalTime.of(0, 0);
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);
    private static final LocalTime NOON = LocalTime.of(12, 0);

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


        LocalDate startDate = getFirstNight(firstSessionStart);
        LocalDate endDate = getLastNight(lastSessionEnd);


        long totalNights = Period.between(startDate, endDate).getDays() + 1;


        Set<LocalDate> nightsWithSleep = sessions.stream()
                .filter(this::isNightSleep)
                .map(session -> getNightDate(session))
                .collect(Collectors.toSet());

        long sleeplessNights = totalNights - nightsWithSleep.size();


        return new SleepAnalysisResult("Количество бессонных ночей", (int) sleeplessNights);
    }


    private boolean isNightSleep(SleepingSession session) {
        LocalDateTime start = session.getStartTime();
        LocalDateTime end = session.getEndTime();
        LocalDate startDate = start.toLocalDate();
        LocalDate endDate = end.toLocalDate();


        if (startDate.isBefore(endDate)) {
            return true;
        }


        if (startDate.equals(endDate)) {
            LocalTime startTime = start.toLocalTime();
            LocalTime endTime = end.toLocalTime();


            if (startTime.isAfter(NIGHT_START) && endTime.isBefore(NIGHT_END)) {
                return true;
            }

            if (startTime.isBefore(NIGHT_START) && endTime.isAfter(NIGHT_START)) {
                return true;
            }
        }

        return false;
    }


    private LocalDate getNightDate(SleepingSession session) {
        LocalDateTime start = session.getStartTime();
        LocalDateTime end = session.getEndTime();
        LocalDate startDate = start.toLocalDate();
        LocalDate endDate = end.toLocalDate();

        if (startDate.isBefore(endDate)) {
            return startDate;
        }


        LocalTime startTime = start.toLocalTime();
        if (startTime.isBefore(NIGHT_END)) {
            return startDate.minusDays(1);
        }

        return startDate;
    }


    private LocalDate getFirstNight(LocalDateTime firstSessionStart) {
        LocalTime time = firstSessionStart.toLocalTime();
        LocalDate date = firstSessionStart.toLocalDate();

        if (time.isAfter(NOON)) {
            return date;
        } else {
            return date.minusDays(1);
        }
    }


    private LocalDate getLastNight(LocalDateTime lastSessionEnd) {
        LocalTime time = lastSessionEnd.toLocalTime();
        LocalDate date = lastSessionEnd.toLocalDate();


        if (time.isAfter(NOON)) {
            return date;
        } else {
            return date.minusDays(1);
        }
    }
}