package com.alonalbert.garmin.vprp;

/**
 * Created by IntelliJ IDEA. User: aalbert Date: 5/27/11 Time: 10:37 AM To change this template use
 * File | Settings | File Templates.
 */
public class Segment {

  private final CoursePoint start;
  private final CoursePoint end;

  public Segment(CoursePoint start, CoursePoint end) {

    this.start = start;
    this.end = end;
  }

  public CoursePoint getStart() {
    return start;
  }

  public CoursePoint getEnd() {
    return end;
  }

  @Override
  public String toString() {
    return "Segment{" +
           "start=" + start +
           ", end=" + end +
           '}';
  }
}
