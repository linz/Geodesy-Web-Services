package au.gov.ga.geodesy.domain.model.sitelog;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 * http://sopac.ucsd.edu/ns/geodesy/doc/igsSiteLog/localInterferences/2004/baseLocalInterferencesLib.xsd:basePossibleProblemSourcesType
 */
@MappedSuperclass
public abstract class PossibleProblemSourceLogItem extends LogItem {

    @Size(max = 4000)
    @Column(name = "POSSIBLE_PROBLEM_SOURCE", length = 4000)
    protected String possibleProblemSource;

    @Valid
    @Embedded
    protected EffectiveDates effectiveDates;

    @Size(max = 4000)
    @Column(name = "NOTES", length = 4000)
    protected String notes;

    /**
     * Return possible problem source.
     */
    public String getPossibleProblemSource() {
        return possibleProblemSource;
    }

    /**
     * Set possible problem source.
     */
    public void setPossibleProblemSource(String value) {
        this.possibleProblemSource = value;
    }

    /**
     * Return effective dates.
     */
    public EffectiveDates getEffectiveDates() {
        return effectiveDates;
    }

    /**
     * Set effective dates.
     */
    public void setEffectiveDates(EffectiveDates value) {
        this.effectiveDates = value;
    }

    /**
     * Return notes.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Set notes.
     */
    public void setNotes(String value) {
        this.notes = value;
    }
    public <T> T accept(LogItemVisitor<T> v) {
        return v.visit(this);
    }
}
