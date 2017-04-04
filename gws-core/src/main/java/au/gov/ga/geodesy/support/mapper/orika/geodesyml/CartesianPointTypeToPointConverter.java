package au.gov.ga.geodesy.support.mapper.orika.geodesyml;

import static au.gov.ga.geodesy.support.mapper.orika.geodesyml.SiteLocationMapper.CARTESIAN_COORDINATES;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequenceFactory;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import net.opengis.gml.v_3_2_1.DirectPositionType;
import net.opengis.gml.v_3_2_1.PointType;

public class CartesianPointTypeToPointConverter extends BidirectionalConverter<Point, PointType> {
	
	public CartesianPointTypeToPointConverter() {
		
	}
	
    @Override
    public Point convertFrom(PointType pointType, Type<Point> targetType, MappingContext ctx) {  	
    	
    	List <Double> positionValues = pointType.getPos().getValue();

    	Coordinate coordinate = new Coordinate(
            positionValues.get(0).doubleValue(), 
            positionValues.get(1).doubleValue(), 
            positionValues.get(2).doubleValue()
    	);

    	Point point = new GeometryFactory(
    			new PrecisionModel(PrecisionModel.FLOATING), CARTESIAN_COORDINATES).createPoint(coordinate);

    	/* TODO 
    	// figure out the srid of the point, in the xml we allow strings like "EPSG:7789"
    	String srsNameNumbersOnly = pointType.getSrsName().replaceAll("[^0-9]+", "");
    	int srID = Integer.parseInt(srsNameNumbersOnly);
    	*/
    	point.setSRID(CARTESIAN_COORDINATES);
    	
    	return point;
    }

    @Override
    public PointType convertTo(Point point, Type<PointType> targetType, MappingContext ctx) {
    	PointType pointType = new PointType();
    	
    	// for converting back to xml, prepend the String EPSG:" to the sr id
    	// TODO get the value from the entity 
    	// int srID = point.getSRID();
    	
    	String epsgCode = "EPSG:" + CARTESIAN_COORDINATES;
    	
    	Double [] values = {
            point.getCoordinate().getOrdinate(0),
            point.getCoordinate().getOrdinate(1),
            point.getCoordinate().getOrdinate(2)
    	};

        pointType.setPos(new DirectPositionType().withValue(values));
        pointType.setSrsName(epsgCode);
    	return pointType;
        
    }
}
