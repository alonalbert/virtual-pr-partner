package com.alonalbert.garmin.coursemaker;

import org.w3c.dom.Element;

import java.io.PrintStream;
import java.text.ParseException;
import java.util.Date;

/*
       <Trackpoint>
         <Time>2011-05-25T18:48:34Z</Time>
         <Position>
           <LatitudeDegrees>37.419571</LatitudeDegrees>
           <LongitudeDegrees>-122.082958</LongitudeDegrees>
         </Position>
         <AltitudeMeters>4.8</AltitudeMeters>
         <DistanceMeters>0</DistanceMeters>
       </Trackpoint>
*/
public class TrackPoint {
    private Date time;
    private Position position;
    private double altitude;
    private double distance;

    public static TrackPoint fromElement(Element point) throws ParseException {
        final Element position = (Element) XmlUtils.getElement(point, "Position");
        return new TrackPoint(
                XmlUtils.getDateFormat().parse(XmlUtils.getElement(point, "Time").getTextContent()),
                Double.parseDouble(XmlUtils.getElement(position, "LatitudeDegrees").getTextContent()),
                Double.parseDouble(XmlUtils.getElement(position, "LongitudeDegrees").getTextContent()),
                Double.parseDouble(XmlUtils.getElement(point, "AltitudeMeters").getTextContent()),
                Double.parseDouble(XmlUtils.getElement(point, "DistanceMeters").getTextContent())
        );
    }

    public TrackPoint(Date time, double latitude, double longitude, double altitude, double distance) {
        this.time = time;
        position = new Position(latitude, longitude);
        this.altitude = altitude;
        this.distance = distance;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void serialize(PrintStream printStream) {
        printStream.print("       <Trackpoint>\n");
        printStream.printf("         <Time>%s</Time>\n", XmlUtils.getDateFormat().format(time));
        printStream.print("         <Position>\n");
        printStream.printf("           <LatitudeDegrees>%.6f</LatitudeDegrees>\n", position.getLatitude());
        printStream.printf("           <LongitudeDegrees>%.6f</LongitudeDegrees>\n", position.getLongitude());
        printStream.print("         </Position>\n");
        printStream.printf("         <AltitudeMeters>%.1f</AltitudeMeters>\n", altitude);
        printStream.printf("         <DistanceMeters>%.12f</DistanceMeters>\n", distance);
        printStream.print("       </Trackpoint>\n");
    }
}
