package au.gov.ga.geodesy;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSuiteListener implements ISuiteListener {

    private static final Logger log = LoggerFactory.getLogger(TestSuiteListener.class);

    public void onStart(ISuite suite) {
    }

    public void onFinish(ISuite suite) {
        if (System.getProperty("keepAlive") != null) {
            log.info("Keeping the JVM alive, so you can inspect the in-memory database at http://localhost:8082/.");
            log.info("Type ctrl-c to exit.");
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException ok) {
            }
        }
    }
}
