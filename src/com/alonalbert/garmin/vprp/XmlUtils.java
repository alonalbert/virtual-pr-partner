package com.alonalbert.garmin.vprp;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class XmlUtils {

  private static DateFormat dateFormat;

  public static Node getElement(Element point, String name) {
    return point.getElementsByTagName(name).item(0);
  }

  public static DateFormat getDateFormat() {
    if (dateFormat == null) {
      dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    }

    return dateFormat;
  }
}
