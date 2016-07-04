package au.gov.ga.geodesy.domain.model.sitelog;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * http://sopac.ucsd.edu/ns/geodesy/doc/igsSiteLog/equipment/2004/surveyedLocalTies.xsd:surveyedLocalTiesType.
 * differentialComponentsGNSSMarkerToTiedMonumebtsITRS
 */
@Embeddable
public class DifferentialFromMarker {

    @Column(name = "DX")
    protected @MonotonicNonNull Double dx;

    @Column(name = "DY")
    protected @MonotonicNonNull Double dy;

    @Column(name = "DZ")
    protected @MonotonicNonNull Double dz;

    /**
     * Return differential component in x-direction.
     */
    public @Nullable Double getDx() {
        return dx;
    }

    /**
     * Set differential component in x-direction.
     */
    public void setDx(Double value) {
        this.dx = value;
    }

    /**
     * Return differential component in y-direction.
     */
    public @Nullable Double getDy() {
        return dy;
    }

    /**
     * Set differential component in y-direction.
     *
     */
    public void setDy(Double value) {
        this.dy = value;
    }

    /**
     * Return differential component in z-direction.
     */
    public @Nullable Double getDz() {
        return dz;
    }

    /**
     * Set differential component in z-direction.
     *
     */
    public void setDz(Double value) {
        this.dz = value;
    }
}
