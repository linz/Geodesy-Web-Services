package au.gov.ga.geodesy.domain.model.sitelog;

public abstract class EquipmentLogItem<E extends EquipmentLogItem<E>> extends LogItem<E> {
    public abstract String getType();
    public abstract String getSerialNumber();
}

