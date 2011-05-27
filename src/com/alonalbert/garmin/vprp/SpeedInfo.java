package com.alonalbert.garmin.vprp;

public class SpeedInfo {

  private final double speed;
  private final int minGrade;
  private final int maxGrade;

  public SpeedInfo(double speed, int minGrade, int maxGrade) {
    this.speed = speed;
    this.minGrade = minGrade;
    this.maxGrade = maxGrade;
  }

  public SpeedInfo(double speed, int grade) {
    this(speed, grade, grade);
  }

  public double getSpeed() {
    return speed;
  }

  public int getMinGrade() {
    return minGrade;
  }

  public int getMaxGrade() {
    return maxGrade;
  }
}
