package au.gov.ga.geodesy;

import java.security.Permission;

import org.testng.IExecutionListener;

public class TestSuiteListener implements IExecutionListener {

    @SuppressWarnings("serial")
    public class ExitInterceptedException extends SecurityException {
        public ExitInterceptedException() {
            super("Keeping the JVM alive, so you can inspect the in-memory database at http://localhost:8082/."
                  + " Connection details: jdbc:h2:./h2-test-db, username: \"\", password: \"\". Type ctrl-c to exit.");
        }
    }

    public void onExecutionStart() {
        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable x) {
                if (x.getClass() == ExitInterceptedException.class) {
                    new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(Long.MAX_VALUE);
                            } catch (InterruptedException ok) {
                            }
                        }
                    }.start();
                }
            }
        });
    }

    public void onExecutionFinish() {
        if (System.getProperty("keepAlive") != null) {
            final SecurityManager securityManager = new SecurityManager() {
                public void checkPermission(Permission permission) {
                    if (permission.getName().startsWith("exitVM.")) {
                        throw new ExitInterceptedException();
                    }
                }
            };
            System.setSecurityManager(securityManager);
        }
    }
}
