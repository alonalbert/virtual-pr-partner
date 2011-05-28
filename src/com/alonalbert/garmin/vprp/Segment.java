package com.alonalbert.garmin.vprp;

/**
 * Created by IntelliJ IDEA. User: aalbert Date: 5/27/11 Time: 10:37 AM To change this template use
 * File | Settings | File Templates.
 */
public class Segment {

  private final Position start;
  private final Position end;

  private final int startIndex;
  private final int endIndex;

  public Segment(Position start, Position end, int startIndex, int endIndex) {
    this.start = start;
    this.end = end;
    this.startIndex = startIndex;
    this.endIndex = endIndex;
  }

  public Position getStart() {
    return start;
  }

  public Position getEnd() {
    return end;
  }

  public int getStartIndex() {
    return startIndex;
  }

  public int getEndIndex() {
    return endIndex;
  }

  @Override
  public String toString() {
    return "Segment{" +
           "start=" + start +
           ", end=" + end +
           ", startIndex=" + startIndex +
           ", endIndex=" + endIndex +
           '}';
  }

  public String toCoordinateString() {
    return start.toCoordinatesString() + "\n" + end.toCoordinatesString();
  }
}
