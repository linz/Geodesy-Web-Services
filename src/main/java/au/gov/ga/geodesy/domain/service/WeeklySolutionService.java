package au.gov.ga.geodesy.domain.service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import au.gov.ga.geodesy.domain.model.EventPublisher;
import au.gov.ga.geodesy.domain.model.WeeklySolution;
import au.gov.ga.geodesy.domain.model.WeeklySolutionAvailable;
import au.gov.ga.geodesy.domain.model.WeeklySolutionRepository;

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
            System.out.println(firstLine);

            StringTokenizer tokenizer = new StringTokenizer(firstLine);
            drop(tokenizer, 3);
            Date asAt = parseGpsEpoch(tokenizer.nextToken());
            drop(tokenizer, 1);
            Date firstObservation = parseGpsEpoch(tokenizer.nextToken());
            Date lastObservation = parseGpsEpoch(tokenizer.nextToken());

            Calendar epoch = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            epoch.setTime(firstObservation);
            epoch.add(Calendar.DAY_OF_WEEK, 3);

            WeeklySolution solution = new WeeklySolution(epoch.getTime(), sinexFileName);
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
    private Date parseGpsEpoch(String gpsEpoch) throws ParseException {
        String dayAndYear = gpsEpoch.substring(0, gpsEpoch.lastIndexOf(':'));
        SimpleDateFormat gpsEpochFormat = new SimpleDateFormat("yy':'DDD");
        gpsEpochFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return gpsEpochFormat.parse(dayAndYear);
    }

    @SuppressWarnings("unused")
    private String formatDate(Date d) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(d);
    }

    private void drop(StringTokenizer tokenizer, int n) {
        for (int i = 0; i < n; i++) {
            tokenizer.nextToken();
        }
    }
}
