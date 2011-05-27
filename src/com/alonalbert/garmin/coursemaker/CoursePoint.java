package com.alonalbert.garmin.coursemaker;

import org.w3c.dom.Element;

import java.io.PrintStream;
import java.text.ParseException;
import java.util.Date;

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
public class CoursePoint {
    private String name;
    private Date time;
    private Position position;
    private String type;
    private String notes;

    public static CoursePoint fromElement(Element point) throws ParseException {
        final Element position = (Element) XmlUtils.getElement(point, "Position");
        return new CoursePoint(
                XmlUtils.getElement(point, "Name").getTextContent(),
                XmlUtils.getDateFormat().parse(XmlUtils.getElement(point, "Time").getTextContent()),
                Double.parseDouble(XmlUtils.getElement(position, "LatitudeDegrees").getTextContent()),
                Double.parseDouble(XmlUtils.getElement(position, "LongitudeDegrees").getTextContent()),
                XmlUtils.getElement(point, "PointType").getTextContent(),
                XmlUtils.getElement(point, "Notes").getTextContent()
        );
    }

    public CoursePoint(String name, Date time, double latitude, double longitude, String type, String notes) {
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

    public void serialize(PrintStream printStream) {
        printStream.print("      <CoursePoint>\n");
        printStream.printf("        <Name>%s</Name>\n", name);
        printStream.printf("        <Time>%s</Time>\n", XmlUtils.getDateFormat().format(time));
        printStream.printf("        <Position>\n");
        printStream.printf("          <LatitudeDegrees>%.6f</LatitudeDegrees>\n", position.getLatitude());
        printStream.printf("          <LongitudeDegrees>%.6f</LongitudeDegrees>\n", position.getLongitude());
        printStream.printf("        </Position>\n");
        printStream.printf("        <PointType>%s</PointType>\n", type);
        printStream.printf("        <Notes>%s</Notes>\n", notes);
        printStream.print("      </CoursePoint>\n");
    }
}
