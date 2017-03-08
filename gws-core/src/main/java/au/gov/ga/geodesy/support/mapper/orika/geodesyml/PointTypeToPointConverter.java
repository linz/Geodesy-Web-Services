package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import net.opengis.gml.v_3_2_1.DirectPositionType;
import net.opengis.gml.v_3_2_1.PointType;

public class PointTypeToPointConverter extends BidirectionalConverter<Point, PointType> {

	public PointTypeToPointConverter() {
		super();
	}
	
    @Override
    public Point convertFrom(PointType pointType, Type<Point> targetType, MappingContext ctx) {  	
    	
    	List <Double> positionValues = pointType.getPos().getValue();

    	Coordinate coordinate = new Coordinate(
    		positionValues.get(0).doubleValue(), 
    		positionValues.get(1).doubleValue(), 
    		positionValues.get(2).doubleValue()
    	);
    	
    	// figure out the srid of the point, in the xml we allow strings like "EPSG:7789"
    	String srsNameNumbersOnly = pointType.getSrsName().replaceAll("[^0-9]+", "");
    	int srID = Integer.parseInt(srsNameNumbersOnly);
    	
    	Point point = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), srID).createPoint(coordinate);
    	
    	point.setSRID(srID);

    	return point;
    }

    @Override
    public PointType convertTo(Point point, Type<PointType> targetType, MappingContext ctx) {
    	PointType pointType = new PointType();
    	
    	// for converting back to xml, prepend the String EPSG:" to the sr id
    	String epsgCode = "EPSG:" + point.getSRID();
    	
    	Double [] values = {
			point.getCoordinate().getOrdinate(0),
			point.getCoordinate().getOrdinate(1),
			point.getCoordinate().getOrdinate(2)
    	};
    	    	
        pointType.setPos(new DirectPositionType().withSrsName(epsgCode).withValue(values));
        
    	return pointType;
        
    }
}
