package au.gov.ga.geodesy.support.testng;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.orm.jpa.JpaSystemException;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class TestListener extends TestListenerAdapter implements ITestListener {
    public void onTestFailure(ITestResult failure) {
        Throwable x = failure.getThrowable();
        // TODO: can we get the root cause polymorphicaly?
        if (x instanceof DataIntegrityViolationException) {
            ((DataIntegrityViolationException) x).getRootCause().printStackTrace();
        } else if (x instanceof JpaSystemException) {
            ((JpaSystemException) x).getRootCause().printStackTrace();
        } else if (x instanceof InvalidDataAccessResourceUsageException) {
            ((InvalidDataAccessResourceUsageException) x).getRootCause().printStackTrace();
        }
        super.onTestFailure(failure);
    }
}
