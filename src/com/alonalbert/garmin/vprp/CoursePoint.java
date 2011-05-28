package com.alonalbert.garmin.vprp;

import org.w3c.dom.Element;

import java.io.PrintStream;
import java.text.ParseException;

/*
      <CoursePoint>
        <Name>Right</Name>
        <Time>2011-05-25T20:08:44Z</Time>
        <Position>
          <LatitudeDegrees>37.28181</LatitudeDegrees>
          <LongitudeDegrees>-121.97946</LongitudeDegrees>
        </Position>
        <PointType>Right</PointType>
        <Notes>Rincon Ave</Notes>
      </CoursePoint>

 */
@SuppressWarnings({"UnusedDeclaration"})
public class CoursePoint {

  public static final String CATEGORY_SUFFIX = " Category";

  private String name;
  private long time;
  private Position position;
  private String type;
  private String notes;

  public static CoursePoint fromElement(Element point) throws ParseException {
    final Element position = (Element) XmlUtils.getElement(point, "Position");
    return new CoursePoint(
        XmlUtils.getElement(point, "Name").getTextContent(),
        XmlUtils.parseTime(XmlUtils.getElement(point, "Time").getTextContent()),
        Double.parseDouble(XmlUtils.getElement(position, "LatitudeDegrees").getTextContent()),
        Double.parseDouble(XmlUtils.getElement(position, "LongitudeDegrees").getTextContent()),
        XmlUtils.getElement(point, "PointType").getTextContent(),
        XmlUtils.getText(XmlUtils.getElement(point, "Notes"))
    );
  }

  public CoursePoint(String name, long time, double latitude, double longitude, String type,
                     String notes) {
    this.name = name;
    this.time = time;
    this.position = new Position(latitude, longitude);
    this.type = type;
    this.notes = notes;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void serialize(PrintStream printStream, String prefix) throws ParseException {
    printStream.print(prefix + "<CoursePoint>\n");
    printStream.printf(prefix + "  <Name>%s</Name>\n", name);
    printStream.printf(prefix + "  <Time>%s</Time>\n", XmlUtils.formatTime(time));
    printStream.printf(prefix + "  <Position>\n");
    printStream
        .printf(prefix + "    <LatitudeDegrees>%.6f</LatitudeDegrees>\n", position.getLatitude());
    printStream
        .printf(prefix + "    <LongitudeDegrees>%.6f</LongitudeDegrees>\n",
                position.getLongitude());
    printStream.printf(prefix + "  </Position>\n");
    printStream.printf(prefix + "  <PointType>%s</PointType>\n", type);
    if (notes != null) {
      printStream.printf(prefix + "  <Notes>%s</Notes>\n", notes);
    }
    printStream.print(prefix + "</CoursePoint>\n");
  }

  @Override
  public String toString() {
    return "CoursePoint{" +
           "name='" + name + '\'' +
           ", time=" + time +
           ", position=" + position +
           ", type='" + type + '\'' +
           ", notes='" + notes + '\'' +
           '}';
  }
}
