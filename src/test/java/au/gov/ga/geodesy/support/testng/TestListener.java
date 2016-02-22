package au.gov.ga.geodesy.support.testng;

import org.springframework.dao.DataIntegrityViolationException;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class TestListener extends TestListenerAdapter implements ITestListener {
    public void onTestFailure(ITestResult failure) {
        Throwable x = failure.getThrowable();
        if (x instanceof DataIntegrityViolationException) {
            ((DataIntegrityViolationException) x).getRootCause().printStackTrace();
        }
        super.onTestFailure(failure);
    }
}

