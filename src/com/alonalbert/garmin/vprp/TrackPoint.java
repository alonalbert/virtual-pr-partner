package com.alonalbert.garmin.vprp;

import org.w3c.dom.Element;

import java.io.PrintStream;
import java.text.ParseException;

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
@SuppressWarnings({"UnusedDeclaration"})
public class TrackPoint {

  private long time;
  private Position position;
  private double altitude;
  private double distance;

  public static TrackPoint fromElement(Element point) throws ParseException {
    final Element position = (Element) XmlUtils.getElement(point, "Position");
    return new TrackPoint(
        XmlUtils.parseTime(XmlUtils.getElement(point, "Time").getTextContent()),
        Double.parseDouble(XmlUtils.getElement(position, "LatitudeDegrees").getTextContent()),
        Double.parseDouble(XmlUtils.getElement(position, "LongitudeDegrees").getTextContent()),
        Double.parseDouble(XmlUtils.getElement(point, "AltitudeMeters").getTextContent()),
        Double.parseDouble(XmlUtils.getElement(point, "DistanceMeters").getTextContent())
    );
  }

  public TrackPoint(long time, double latitude, double longitude, double altitude,
                    double distance) {
    this.time = time;
    position = new Position(latitude, longitude);
    this.altitude = altitude;
    this.distance = distance;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
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

  public void serialize(PrintStream printStream, String prefix) throws ParseException {
    printStream.print(prefix + "<Trackpoint>\n");
    printStream.printf(prefix + "  <Time>%s</Time>\n", XmlUtils.formatTime(time));
    printStream.print(prefix + "  <Position>\n");
    printStream.printf(
        prefix + "    <LatitudeDegrees>%.6f</LatitudeDegrees>\n", position.getLatitude());
    printStream.printf(
        prefix + "    <LongitudeDegrees>%.6f</LongitudeDegrees>\n", position.getLongitude());
    printStream.print(prefix + "  </Position>\n");
    printStream.printf(prefix + "  <AltitudeMeters>%.1f</AltitudeMeters>\n", altitude);
    printStream.printf(prefix + "  <DistanceMeters>%.12f</DistanceMeters>\n", distance);
    printStream.print(prefix + "</Trackpoint>\n");
  }

  @Override
  public String toString() {
    return "TrackPoint{" +
           "time=" + time +
           ", position=" + position +
           ", altitude=" + altitude +
           ", distance=" + distance +
           '}';
  }

  public String toCoordinatesString() {
    return position.toCoordinatesString();
  }
}
