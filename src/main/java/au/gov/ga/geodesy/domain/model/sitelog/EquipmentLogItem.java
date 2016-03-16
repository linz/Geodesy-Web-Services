package au.gov.ga.geodesy.domain.model.sitelog;

public interface EquipmentLogItem {
    String getType();
    String getSerialNumber();
    EffectiveDates getEffectiveDates();
    <T> T accept(LogItemVisitor<T> v);
}

