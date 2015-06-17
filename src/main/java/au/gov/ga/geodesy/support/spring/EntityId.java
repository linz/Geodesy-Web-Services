package au.gov.ga.geodesy.support.spring;

public class EntityId<T> implements Cloneable {

    private String id;

    public EntityId(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public Object clone() {
        return new EntityId<T>(id());
    }
}
