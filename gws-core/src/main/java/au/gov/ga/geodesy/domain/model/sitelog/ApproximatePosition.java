package au.gov.ga.geodesy.domain.model.sitelog;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.vividsolutions.jts.geom.Point;

/**
 * http://sopac.ucsd.edu/ns/geodesy/doc/igsSiteLog/monumentInfo/2004/siteLocation.xsd:siteLocationType.approximatePositionITRF
 */
@Embeddable
public class ApproximatePosition {

	/**
	 * Position in EPSG:7789 reference system - x,y,z
	 */
    @Column(name = "CARTESIAN_POSITION", columnDefinition = "geometry")
    private Point cartesianPosition;

	/**
	 * Position in EPSG:7912 reference system - lat, long, elevation 
	 */
    @Column(name = "GEODETIC_POSITION", columnDefinition = "geometry")
    private Point geodeticPosition;

	public Point getCartesianPosition() {
		return cartesianPosition;
	}

	public void setCartesianPosition(Point cartesianPosition) {
		this.cartesianPosition = cartesianPosition;
	}

	public Point getGeodeticPosition() {
		return geodeticPosition;
	}

	public void setGeodeticPosition(Point geodeticPosition) {
		this.geodeticPosition = geodeticPosition;
	}
}
