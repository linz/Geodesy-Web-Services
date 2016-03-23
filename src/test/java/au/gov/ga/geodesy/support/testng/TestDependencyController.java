package au.gov.ga.geodesy.support.testng;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

public class TestDependencyController implements IAnnotationTransformer {

    @SuppressWarnings("rawtypes")
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor,
            Method testMethod) {

        if (shouldIgnoreDependencies()) {
            annotation.setDependsOnMethods(null);
            annotation.setDependsOnGroups(null);
        }
    }

    private boolean shouldIgnoreDependencies() {
        // TODO: compare to yes or no
        return System.getProperty("ignoreDependencies") != null;
    }
}

