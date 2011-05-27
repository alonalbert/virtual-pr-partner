package com.alonalbert.garmin.vprp;

public class Position {

  private final double latitude;
  private final double longitude;

  public Position(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Position position = (Position) o;

    if (Double.compare(position.latitude, latitude) != 0) {
      return false;
    }
    //noinspection RedundantIfStatement
    if (Double.compare(position.longitude, longitude) != 0) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = latitude != +0.0d ? Double.doubleToLongBits(latitude) : 0L;
    result = (int) (temp ^ (temp >>> 32));
    temp = longitude != +0.0d ? Double.doubleToLongBits(longitude) : 0L;
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "Position{" +
           "latitude=" + latitude +
           ", longitude=" + longitude +
           '}';
  }

  public double distanceFrom(Position other) {
    final double theta = longitude - other.longitude;
    double distance =
        Math.sin(toRadians(latitude)) * Math.sin(toRadians(other.latitude))
        + Math.cos(toRadians(latitude)) * Math.cos(toRadians(other.latitude)) * Math.cos(
            toRadians(theta));
    distance = Math.acos(distance);
    distance = toDegrees(distance);
    distance = distance * 60 * 1.1515 * 1609.344;
    return (distance);
  }

  public double getBearing(Position other) {
    final double deltaLongitude = longitude - other.longitude;
    final double y = Math.sin(deltaLongitude) * Math.cos(other.latitude);
    final double x = Math.cos(latitude) * Math.sin(other.latitude) -
                     Math.sin(latitude) * Math.cos(other.latitude) * Math.cos(deltaLongitude);
    return toDegrees(Math.atan2(y, x));
  }

  private double toRadians(double deg) {
    return (deg * Math.PI / 180.0);
  }

  private double toDegrees(double rad) {
    return (rad * 180.0 / Math.PI);
  }

  public String toCoordinatesString() {
    return latitude + ", " + longitude;
  }
}
