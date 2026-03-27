package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChronoTypeFunction implements AnalysisFunction {

    private static final LocalTime OWL_BED_START = LocalTime.of(23, 0);
    private static final LocalTime OWL_WAKE_START = LocalTime.of(9, 0);
    private static final LocalTime LARK_BED_END = LocalTime.of(22, 0);
    private static final LocalTime LARK_WAKE_END = LocalTime.of(7, 0);
    private static final LocalTime NOON = LocalTime.of(12, 0);
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        List<SleepingSession> nightSessions = sessions.stream()
                .filter(this::isNightSleep)
                .filter(session -> !isDaytimeSleep(session))
                .collect(Collectors.toList());

        Map<Chronotype, Long> counts = nightSessions.stream()
                .map(this::getChronotypeForNight)
                .collect(Collectors.groupingBy(ct -> ct, Collectors.counting()));

        Chronotype result = determineChronotype(counts);

        return new SleepAnalysisResult("Хронотип пользователя", result.getDisplayName());
    }

    private boolean isNightSleep(SleepingSession session) {
        LocalDate startDate = session.getStartTime().toLocalDate();
        LocalDate endDate = session.getEndTime().toLocalDate();


        if (startDate.isBefore(endDate)) {
            return true;
        }


        if (startDate.equals(endDate)) {
            LocalTime startTime = session.getStartTime().toLocalTime();
            LocalTime endTime = session.getEndTime().toLocalTime();
            return startTime.isBefore(NIGHT_END) || endTime.isBefore(NIGHT_END);
        }

        return false;
    }

    private boolean isDaytimeSleep(SleepingSession session) {
        LocalTime startTime = session.getStartTime().toLocalTime();
        LocalTime endTime = session.getEndTime().toLocalTime();
        LocalDate startDate = session.getStartTime().toLocalDate();
        LocalDate endDate = session.getEndTime().toLocalDate();


        return startDate.equals(endDate) &&
                startTime.isAfter(NIGHT_END) &&
                startTime.isBefore(NOON);
    }

    private Chronotype getChronotypeForNight(SleepingSession session) {
        LocalTime bedTime = session.getStartTime().toLocalTime();
        LocalTime wakeTime = session.getEndTime().toLocalTime();


        if (session.getStartTime().toLocalDate().isBefore(session.getEndTime().toLocalDate())) {
            // Если проснулся после 9 утра, значит сова
            if (wakeTime.isAfter(OWL_WAKE_START)) {
                return Chronotype.OWL;
            }
        }


        if (bedTime.isAfter(OWL_BED_START) && wakeTime.isAfter(OWL_WAKE_START)) {
            return Chronotype.OWL;
        }


        if (bedTime.isBefore(LARK_BED_END) && wakeTime.isBefore(LARK_WAKE_END)) {
            return Chronotype.LARK;
        }


        return Chronotype.PIGEON;
    }

    private Chronotype determineChronotype(Map<Chronotype, Long> counts) {
        long owlCount = counts.getOrDefault(Chronotype.OWL, 0L);
        long larkCount = counts.getOrDefault(Chronotype.LARK, 0L);
        long pigeonCount = counts.getOrDefault(Chronotype.PIGEON, 0L);

        if (owlCount > larkCount && owlCount > pigeonCount) {
            return Chronotype.OWL;
        } else if (larkCount > owlCount && larkCount > pigeonCount) {
            return Chronotype.LARK;
        } else {
            return Chronotype.PIGEON;
        }
    }

    private enum Chronotype {
        OWL("Сова"),
        LARK("Жаворонок"),
        PIGEON("Голубь");

        private final String displayName;

        Chronotype(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}