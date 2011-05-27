package com.alonalbert.garmin.vprp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class TrainingCenterDatabase {

  private static DocumentBuilder documentBuilder;
  private static XPathFactory xpathFactory = XPathFactory.newInstance();

  private String name;
  private final List<TrackPoint> trackPoints = new ArrayList<TrackPoint>();
  private final List<CoursePoint> coursePoints = new ArrayList<CoursePoint>();

  public static TrainingCenterDatabase parse(InputStream in)
      throws IOException, SAXException, XPathExpressionException, ParseException {
    final Document document = getDocumentBuilder().parse(in);

    final XPathFactory xPathFactory = getXPathFactory();

    String name = (String) xPathFactory.newXPath()
        .compile("/TrainingCenterDatabase/Courses/Course/Name/text()")
        .evaluate(document, XPathConstants.STRING);

    final TrainingCenterDatabase database = new TrainingCenterDatabase(name);

    final NodeList trackPoints = (NodeList) xPathFactory.newXPath()
        .compile("/TrainingCenterDatabase/Courses/Course/Track/Trackpoint")
        .evaluate(document, XPathConstants.NODESET);
    for (int i = 0, length = trackPoints.getLength(); i < length; ++i) {
      database.getTrackPoints().add(TrackPoint.fromElement((Element) trackPoints.item(i)));
    }

    final NodeList coursePoints = (NodeList) xPathFactory.newXPath()
        .compile("/TrainingCenterDatabase/Courses/Course/CoursePoint")
        .evaluate(document, XPathConstants.NODESET);
    for (int i = 0, length = coursePoints.getLength(); i < length; ++i) {
      database.getCoursePoints().add(CoursePoint.fromElement((Element) coursePoints.item(i)));
    }
    return database;
  }

  /*
<?xml version="1.0" encoding="UTF-8"?>
<TrainingCenterDatabase
 xmlns="http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xsi:schemaLocation="http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2
 http://www.garmin.com/xmlschemas/TrainingCenterDatabasev2.xsd">
<Folders/>
<Courses>
 <Course>
   <Name>Eden Southbound</Name>
   <Lap>
     <TotalTimeSeconds>487</TotalTimeSeconds>
     <DistanceMeters>32085.8</DistanceMeters>
     <BeginPosition>
       <LatitudeDegrees>37.419571</LatitudeDegrees>
       <LongitudeDegrees>-122.082958</LongitudeDegrees>
     </BeginPosition>
     <EndPosition>
       <LatitudeDegrees>37.28191</LatitudeDegrees>
       <LongitudeDegrees>-121.96902</LongitudeDegrees>
     </EndPosition>
     <Intensity>Active</Intensity>
   </Lap>
   <Track>
     <Trackpoint>
     </Trackpoint>
   </Track>
   <CoursePoint>
   </CoursePoint>
 </Course>
</Courses>
</TrainingCenterDatabase>
  */
  public void serialize(OutputStream out) {
    final PrintStream printStream = new PrintStream(out);

    printStream.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    printStream.print("<TrainingCenterDatabase ");
    printStream.print("    xmlns=\"http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2\" ");
    printStream.print("    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
    printStream.print(
        "    xsi:schemaLocation=\"http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2 ");
    printStream.print("    http://www.garmin.com/xmlschemas/TrainingCenterDatabasev2.xsd\">\n");
    printStream.print("  <Folders/>\n");
    printStream.print("  <Courses>\n");
    printStream.print("    <Course>\n");
    printStream.print("      <Name>" + name + "</Name>\n");
    printStream.print("      <Lap>\n");

    final TrackPoint firstPoint = trackPoints.get(0);
    final TrackPoint lastPoint = trackPoints.get(trackPoints.size() - 1);

    printStream.printf("        <TotalTimeSeconds>%d</TotalTimeSeconds>\n",
                       (lastPoint.getTime().getTime() - firstPoint.getTime().getTime()) / 1000);

    printStream.printf("        <DistanceMeters>%.1f</DistanceMeters>\n",
                       lastPoint.getDistance() - firstPoint.getDistance());

    printStream.print("        <BeginPosition>\n");
    printStream.printf("          <LatitudeDegrees>%.6f</LatitudeDegrees>\n",
                       firstPoint.getPosition().getLatitude());
    printStream.printf("          <LongitudeDegrees>%.6f</LongitudeDegrees>\n",
                       firstPoint.getPosition().getLongitude());
    printStream.print("        </BeginPosition>\n");
    printStream.print("        <EndPosition>\n");
    printStream.printf("          <LatitudeDegrees>%.6f</LatitudeDegrees>\n",
                       lastPoint.getPosition().getLatitude());
    printStream.printf("          <LongitudeDegrees>%.6f</LongitudeDegrees>\n",
                       lastPoint.getPosition().getLongitude());
    printStream.print("        </EndPosition>\n");
    printStream.print("        <Intensity>Active</Intensity>\n");
    printStream.print("      </Lap>\n");
    printStream.print("      <Track>\n");
    for (TrackPoint point : trackPoints) {
      point.serialize(printStream);
    }
    printStream.print("      </Track>\n");
    for (CoursePoint point : coursePoints) {
      point.serialize(printStream);
    }
    printStream.print("    </Course>\n");
    printStream.print("  </Courses>\n");
    printStream.print("</TrainingCenterDatabase>\n");
  }

  private static XPathFactory getXPathFactory() {
    return xpathFactory;
  }

  private static DocumentBuilder getDocumentBuilder() {
    if (documentBuilder == null) {
      try {
        documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      } catch (ParserConfigurationException e) {
        throw new RuntimeException("Failed to create DocumentBuilder", e);
      }
    }
    return documentBuilder;
  }

  public TrainingCenterDatabase(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<TrackPoint> getTrackPoints() {
    return trackPoints;
  }

  public List<CoursePoint> getCoursePoints() {
    return coursePoints;
  }


}
