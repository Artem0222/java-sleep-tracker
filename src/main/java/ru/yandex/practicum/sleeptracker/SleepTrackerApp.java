package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SleepTrackerApp {

    private static final List<AnalysisFunction> analysisFunctions = new ArrayList<>();

    static {

        analysisFunctions.add(new TotalSessionsFunction());
        analysisFunctions.add(new MinDurationFunction());
        analysisFunctions.add(new MaxDurationFunction());
        analysisFunctions.add(new AverageDurationFunction());
        analysisFunctions.add(new BadQualitySessionsFunction());
        analysisFunctions.add(new SleeplessNightsFunction());
        analysisFunctions.add(new ChronoTypeFunction());
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Пожалуйста, укажите путь к файлу с логом сна");
            return;
        }

        String filePath = args[0];

        try {
            List<SleepingSession> sessions = loadSessions(filePath);
            System.out.println("Загружено сессий: " + sessions.size());


            for (AnalysisFunction function : analysisFunctions) {
                SleepAnalysisResult result = function.apply(sessions);
                System.out.println(result);
            }

        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static List<SleepingSession> loadSessions(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        try (Stream<String> lines = Files.lines(path)) {
            return lines
                    .filter(line -> !line.trim().isEmpty())
                    .map(SleepingSession::parse)
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
    }
}