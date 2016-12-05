package au.gov.ga.geodesy.domain.model.sitelog;

public interface LogItem {
    EffectiveDates getEffectiveDates();
    <T> T accept(LogItemVisitor<T> v);
}

