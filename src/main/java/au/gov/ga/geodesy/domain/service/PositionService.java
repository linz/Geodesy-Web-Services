package au.gov.ga.geodesy.domain.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import au.gov.ga.geodesy.domain.model.Position;
import au.gov.ga.geodesy.domain.model.PositionRepository;
import au.gov.ga.geodesy.domain.model.WeeklySolution;
import au.gov.ga.geodesy.domain.model.WeeklySolutionRepository;
import au.gov.ga.geodesy.domain.model.event.Event;
import au.gov.ga.geodesy.domain.model.event.EventPublisher;
import au.gov.ga.geodesy.domain.model.event.EventSubscriber;
import au.gov.ga.geodesy.domain.model.event.WeeklySolutionAvailable;

@Component
@Transactional("geodesyTransactionManager")
public class PositionService implements EventSubscriber<WeeklySolutionAvailable> {

    @Autowired
    private WeeklySolutionRepository solutions;

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private PositionRepository positions;

    @PostConstruct
    public void subscribe() {
        eventPublisher.subscribe(this);
    }

    public boolean canHandle(Event event) {
        return event instanceof WeeklySolutionAvailable;
    }

    public void handle(WeeklySolutionAvailable event) {
        WeeklySolution solution = solutions.findOne(event.getWeeklySolutionId());

        File sinex = new File(solution.getSinexFile());
        try (
            Scanner scanner = new Scanner(sinex, "iso-8859-1");
        ) {
            while (!scanner.nextLine().startsWith("+SOLUTION/ESTIMATE"));
            scanner.nextLine();

            String line = null;
            while (!(line = scanner.nextLine()).startsWith("-SOLUTION/ESTIMATE")) {

                Pair<String, BigDecimal> xLine = parseEstimatedValue(line);
                Pair<String, BigDecimal> yLine = parseEstimatedValue(scanner.nextLine());

                @SuppressWarnings("unused")
                Pair<String, BigDecimal> zLine = parseEstimatedValue(scanner.nextLine());

                double x = xLine.getRight().doubleValue();
                double y = yLine.getRight().doubleValue();

                Position p = new Position(xLine.getLeft(), null, 5332, x, y, solution.getEpoch(), solution.getAsAt(), solution.getId());
                positions.save(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        eventPublisher.handled(event);
    }

    private Pair<String, BigDecimal> parseEstimatedValue(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line);

        drop(tokenizer, 2);
        String fourCharacterId = tokenizer.nextToken();

        drop(tokenizer, 5);
        BigDecimal value = new BigDecimal(tokenizer.nextToken());

        return Pair.of(fourCharacterId, value);
    }

    private void drop(StringTokenizer tokenizer, int n) {
        for (int i = 0; i < n; i++) {
            tokenizer.nextToken();
        }
    }
}
