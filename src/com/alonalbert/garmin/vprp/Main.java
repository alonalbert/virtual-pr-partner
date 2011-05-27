package com.alonalbert.garmin.vprp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

  static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
  private static final int SMOOTH = 3;
  private static SpeedInfo[] speedInfos = new SpeedInfo[]{
      new SpeedInfo(25l, -100, -3),
      new SpeedInfo(24l, -2),
      new SpeedInfo(22l, -1),
      new SpeedInfo(17l, 0),
      new SpeedInfo(15l, 1),
      new SpeedInfo(12l, 2),
      new SpeedInfo(11l, 3),
      new SpeedInfo(10l, 4),
      new SpeedInfo(9l, 5),
      new SpeedInfo(8l, 6),
      new SpeedInfo(7l, 7),
      new SpeedInfo(5l, 8, 9),
      new SpeedInfo(4l, 10, 15),
      new SpeedInfo(3l, 16, 100),
  };

  public static void main(String[] args) {
    try {
      // read file
      final String inputFilename = args[0];
      final FileInputStream in = new FileInputStream(inputFilename);
      final TrainingCenterDatabase database;
      try {
        database = TrainingCenterDatabase.parse(in);
      } finally {
        in.close();
      }

      // correct course points name
      // also add course points to map for later on
      final Map<Position, CoursePoint> coursePointByPosition = new HashMap<Position, CoursePoint>();
      for (CoursePoint point : database.getCoursePoints()) {
        point.setName(point.getNotes().trim());
        coursePointByPosition.put(point.getPosition(), point);
      }

      // correct track points time
      final List<TrackPoint> trackPoints = database.getTrackPoints();

      final TrackPoint first = trackPoints.get(0);
      final Date start = first.getTime();
      first.setTime(start);
      long totalTime = start.getTime();

      // correct first SMOOTH points first using first average speed
      int grade = getGrade(trackPoints.get(SMOOTH), first);
      double speed = getSpeedForGrade(grade);
      for (int i = 1; i <= SMOOTH; i++) {
        final TrackPoint point = trackPoints.get(i);
        totalTime =
            correctTimeForPoint(point, trackPoints.get(i - 1), speed, totalTime,
                                coursePointByPosition);
      }

      //
      for (int i = SMOOTH + 1, size = trackPoints.size(); i < size; ++i) {
        final TrackPoint point = trackPoints.get(i);
        grade = getGrade(point, trackPoints.get(i - SMOOTH));
        speed = getSpeedForGrade(grade);

        totalTime =
            correctTimeForPoint(point, trackPoints.get(i - 1), speed, totalTime,
                                coursePointByPosition);
      }

      if (!coursePointByPosition.isEmpty()) {
        throw new IllegalStateException("Not all Course Points corrected.");
      }

      // Write file
      final String outputFilename = inputFilename.replace(".tcx", ".out.tcx");
      final FileOutputStream out = new FileOutputStream(outputFilename);
      try {
        database.serialize(out);
      } finally {
        out.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static long correctTimeForPoint(
      TrackPoint point,
      TrackPoint previous,
      double speed,
      long totalTime,
      Map<Position, CoursePoint> coursePointByPosition) {
    final double distance = getDistance(point, previous);
    final long time = (long) (distance / speed);
    totalTime += time * 1000;
    final Date newTime = new Date(totalTime);
    point.setTime(newTime);
    final Position position = point.getPosition();
    final CoursePoint coursePoint = coursePointByPosition.get(position);
    if (coursePoint != null) {
      coursePoint.setTime(newTime);
      coursePointByPosition.remove(position);
    }
    return totalTime;
  }

  private static int getGrade(TrackPoint current, TrackPoint previous) {
    final double distance = getDistance(current, previous);
    final double altitude = current.getAltitude() - previous.getAltitude();
    return (int) (altitude / distance * 100);
  }

  private static double getDistance(TrackPoint current, TrackPoint previous) {
    return current.getDistance() - previous.getDistance();
  }

  private static double getSpeedForGrade(int grade) {
    for (SpeedInfo speedInfo : speedInfos) {
      if (grade >= speedInfo.getMinGrade() && grade <= speedInfo.getMaxGrade()) {
        return speedInfo.getSpeed() * 1609 / 3600;
      }
    }
    throw new IllegalStateException("No speed found for grade " + grade);
  }

}
