package au.gov.ga.geodesy.support.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import au.gov.ga.geodesy.support.spring.EntityId;

@SuppressWarnings("serial")
public class EntityIdUserType implements UserType, Serializable {

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    public Object deepCopy(Object value) throws HibernateException {
        return ((EntityId<?>) value).clone();
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    public boolean equals(Object a, Object b) throws HibernateException {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }

    public int hashCode(Object a) throws HibernateException {
        return a.hashCode();
    }

    public boolean isMutable() {
        return false;
    }

    @SuppressWarnings("rawtypes")
    public Object nullSafeGet(ResultSet result, String[] names, SessionImplementor s, Object owner)
            throws HibernateException, SQLException {

        String colName = names[0];
        int colNum = result.findColumn(colName);
        return new EntityId(result.getString(colNum));
    }

    @SuppressWarnings("rawtypes")
    public void nullSafeSet(PreparedStatement statement, Object value, int index, SessionImplementor s)
            throws HibernateException, SQLException {

        if (value == null) {
            statement.setNull(index, Types.VARCHAR);
        } else {
            statement.setString(index, ((EntityId) value).id());
        }
    }

    public Object replace(Object original, Object target, Object owner) {
        return original;
    }

    @SuppressWarnings("rawtypes")
    public Class<EntityId> returnedClass() {
        return EntityId.class;
    }

    public int[] sqlTypes() {
        return new int[] {Types.VARCHAR};
    }
}
