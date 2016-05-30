package au.gov.ga.geodesy.domain.service;

import static java.time.temporal.ChronoUnit.DAYS;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import au.gov.ga.geodesy.domain.model.WeeklySolution;
import au.gov.ga.geodesy.domain.model.WeeklySolutionRepository;
import au.gov.ga.geodesy.domain.model.event.EventPublisher;
import au.gov.ga.geodesy.domain.model.event.WeeklySolutionAvailable;
import au.gov.ga.geodesy.support.utils.GMLDateUtils;

@Component
@Transactional("geodesyTransactionManager")
public class WeeklySolutionService {

    @Autowired
    private WeeklySolutionRepository solutions;

    @Autowired
    private EventPublisher eventPublisher;

    public void uploadSolution(String sinexFileName) {
        try (
            Scanner scanner = new Scanner(new File(sinexFileName));
        ) {
            String firstLine = scanner.nextLine();
            // firstLine might look like this: %=SNX 2.00 AUS 13:074:43829 IGS 01:014:00000 01:020:86370 P 00384 0 S

            StringTokenizer tokenizer = new StringTokenizer(firstLine);
            drop(tokenizer, 3);
            Instant asAt = parseGpsEpoch(tokenizer.nextToken());
            drop(tokenizer, 1);
            Instant firstObservation = parseGpsEpoch(tokenizer.nextToken());

            @SuppressWarnings("unused")
            Instant lastObservation = parseGpsEpoch(tokenizer.nextToken());

            Instant epoch = firstObservation.plus(3, DAYS);

            WeeklySolution solution = new WeeklySolution(asAt, epoch, sinexFileName);
            solutions.saveAndFlush(solution);
            eventPublisher.publish(new WeeklySolutionAvailable(solution.getId()));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * YY:DOY:SECOD (YY = year, DOY = day of year, SECOD = second of day)
     * ignore seconds
     */
    private Instant parseGpsEpoch(String gpsEpoch) throws ParseException {
        String dayAndYear = gpsEpoch.substring(0, gpsEpoch.lastIndexOf(':'));
        return GMLDateUtils.stringToDate(dayAndYear, "yy':'DDD");
    }

    private void drop(StringTokenizer tokenizer, int n) {
        for (int i = 0; i < n; i++) {
            tokenizer.nextToken();
        }
    }
}
