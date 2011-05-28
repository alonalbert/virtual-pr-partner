package com.alonalbert.garmin.vprp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: aalbert Date: 5/27/11 Time: 10:12 AM To change this template use
 * File | Settings | File Templates.
 */
public class DetectSegment {

  private final TCX course;
  private final List<Segment> segments;

  public static void main(String[] args) {
    try {
      final TCX course = TCX.parse("eden.tcx");
      final TCX track = TCX.parse("eden-ride.tcx");

      final DetectSegment detector = new DetectSegment(course);
      detector.findSegmentMatches(track.getTrackPoints());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public DetectSegment(TCX course) throws Exception {
    this.course = course;

    segments = findSegments();
    for (Segment segment : segments) {
      System.out.println(segment.toCoordinateString());
    }
  }


  private int findSegmentMatches(List<TrackPoint> points) {
    final int size = points.size();
    for (int i = 0; i < size; i += 1) {
      TrackPoint point = points.get(i);
      for (Segment segment : segments) {
        //Segment segment = segments.get(0);
        final Position startPosition = segment.getStart();
        final double distance = startPosition.distanceFrom(point.getPosition());
        if (distance > 100) {
          continue;
        }

        final int first = findClosestPoint(startPosition, points, i);
        final int last = findClosestPoint(segment.getEnd(), points, first + 1);
        System.out.printf("Found segment from point %d to %d\n", first, last);
        i = last;
      }
    }
    return size;
  }


  private void detectSegment(Segment segment, List<TrackPoint> points)
      throws ParseException {
    final int first = findClosestPoint(segment.getStart(), points, 0);
    final int last = findClosestPoint(segment.getEnd(), points, first + 1);

    System.out.println(last - first);
//    for (int i = first; i < last; ++i) {
//      System.out.println(points.get(i).toCoordinatesString());
//    }
  }


  private static int findClosestPoint(Position position, List<TrackPoint> points, int start) {
    double minDistance = Double.MAX_VALUE;
    final int size = points.size();
    int i = start;
    while (i < size && position.distanceFrom(points.get(i).getPosition()) > 100) {
      i++;
    }
    while (i < size) {
      final double distance = position.distanceFrom(points.get(i).getPosition());
      if (distance > minDistance) {
        return i - 1;
      }
      minDistance = distance;
      i++;
    }
    return -1;
  }

  private List<Segment> findSegments() throws Exception {
    final List<Segment> segments = new ArrayList<Segment>();
    final List<CoursePoint> coursePoints = course.getCoursePoints();
    final List<TrackPoint> trackPoints = course.getTrackPoints();

    int firstIndex = -1;
    Position firstPosition = trackPoints.get(0).getPosition();

    for (int i = 0, size = coursePoints.size(); i < size; i++) {
      CoursePoint point = coursePoints.get(i);

      if (point.getType().endsWith(CoursePoint.CATEGORY_SUFFIX)) {
        segments.add(new Segment(firstPosition, point.getPosition(), firstIndex, i));
        firstIndex = i;
        final CoursePoint firstPoint = point;
        point = coursePoints.get(++i);
        while (!point.getType().equals(firstPoint.getType())) {
          i++;
          if (i >= size) {
            throw new Exception("No match found for " + firstPoint);
          }
          point = coursePoints.get(i);
        }
        segments.add(new Segment(
            coursePoints.get(firstIndex).getPosition(), point.getPosition(), firstIndex, i));
        firstIndex = i;
        firstPosition = coursePoints.get(firstIndex).getPosition();
      }
    }
    segments.add(new Segment(
        firstPosition, trackPoints.get(trackPoints.size() - 1).getPosition(), firstIndex, -1));
    return segments;
  }

//  private static List<Segment> getSegments2(TCX course) throws Exception {
//    final List<Segment> segments = new ArrayList<Segment>();
//    final List<CoursePoint> coursePoints = course.getCoursePoints();
//
//    for (int i = 0, size = coursePoints.size(); i < size; i++) {
//      CoursePoint point = coursePoints.get(i);
//
//      if (point.getType().equals(CoursePoint.CATEGORY_SUFFIX)) {
//        final int first = i;
//        final CoursePoint start = point;
//        point = coursePoints.get(++i);
//        while (!point.getType().equals(start.getType())) {
//          i++;
//          if (i >= size) {
//            throw new Exception("No match found for " + start);
//          }
//          point = coursePoints.get(i);
//        }
//        segments.add(new Segment(first, i));
//      }
//    }
//    return segments;
//  }
}
