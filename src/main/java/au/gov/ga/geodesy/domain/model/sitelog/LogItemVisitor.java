package au.gov.ga.geodesy.domain.model.sitelog;

public interface LogItemVisitor<T> {
    T visit(GnssReceiverLogItem logItem);
    T visit(GnssAntennaLogItem logItem);
    T visit(HumiditySensorLogItem logItem);
    T visit(WaterVaporSensorLogItem logItem);
    T visit(TemperatureSensorLogItem logItem);
    T visit(PressureSensorLogItem logItem);
    T visit(FrequencyStandardLogItem logItem);
    T visit(OtherInstrumentationLogItem logItem);
}

