package au.gov.ga.geodesy.domain.service;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import au.gov.ga.geodesy.domain.model.WeeklySolutionRepository;

@Component
@Transactional("geodesyTransactionManager")
public class WeeklySolutionService {

    @Autowired
    private WeeklySolutionRepository solutions;

    public void uploadSolution(String sinexFileName) {
        try (
            Scanner scanner = new Scanner(new File(sinexFileName));
        ) {
            String firstLine = scanner.nextLine();
            // firstLine might look like this: %=SNX 2.00 AUS 13:074:43829 IGS 01:014:00000 01:020:86370 P 00384 0 S
            System.out.println(firstLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
