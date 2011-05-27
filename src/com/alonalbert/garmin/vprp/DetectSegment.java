package com.alonalbert.garmin.vprp;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: aalbert Date: 5/27/11 Time: 10:12 AM To change this template use
 * File | Settings | File Templates.
 */
public class DetectSegment {

  public static void main(String[] args) {
    try {
      final TCX course = TCX.parse("eden.tcx");
      final TCX track = TCX.parse("eden-ride.tcx");

      final List<Segment> segments = getSegments(course);
//      for (Segment segment : segments) {
//        detectSegment(segment, track.getTrackPoints());
//      }
      findSegmentMatches(segments, track.getTrackPoints());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static int findSegmentMatches(List<Segment> segments, List<TrackPoint> points) {
    final int size = points.size();
    for (int i = 0; i < size; i += 50) {
      TrackPoint point = points.get(i);
      for (Segment segment : segments) {
        final double distance = segment.getStart().getPosition().distanceFrom(point.getPosition());
        if (distance > 100) {
          continue;
        }
        System.out.println("Found start");
        final int first = findClosestPoint(segment.getStart().getPosition(), points, i);
        final int last = findClosestPoint(segment.getEnd().getPosition(), points, first + 1);
        i = last + 1;
      }
    }
    return size;
  }

  private static void detectSegment(Segment segment, List<TrackPoint> points)
      throws ParseException {
    final int first = findClosestPoint(segment.getStart().getPosition(), points, 0);
    final int last = findClosestPoint(segment.getEnd().getPosition(), points, first + 1);

    System.out.println(last - first);
//    for (int i = first; i < last; ++i) {
//      System.out.println(points.get(i).toCoordinatesString());
//    }
  }


  private static int findClosestPoint(Position position, List<TrackPoint> points, int start) {
    double minDistance = Double.MAX_VALUE;
    final int size = points.size();
    for (int i = start; i < size; i++) {
      final double distance = position.distanceFrom(points.get(i).getPosition());
      if (distance > minDistance) {
        return i - 1;
      }
      minDistance = distance;
      i++;
    }
    return size;
  }

  private static List<Segment> getSegments(TCX course) throws Exception {
    final List<Segment> segments = new ArrayList<Segment>();
    final List<CoursePoint> coursePoints = course.getCoursePoints();
    for (int i = 0, size = coursePoints.size(); i < size; i++) {
      CoursePoint point = coursePoints.get(i);

      if (point.getType().equals(CoursePoint.TYPE_CAT_4)) {
        final CoursePoint start = point;
        point = coursePoints.get(++i);
        while (!point.getType().equals(start.getType())) {
          i++;
          if (i >= size) {
            throw new Exception("No match found for " + start);
          }
          point = coursePoints.get(i);
        }
        segments.add(new Segment(start, point));
      }
    }
    return segments;
  }
}
