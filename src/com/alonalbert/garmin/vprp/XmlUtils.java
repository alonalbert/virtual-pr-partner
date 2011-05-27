package com.alonalbert.garmin.vprp;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class XmlUtils {

  private static DateFormat dateFormat;

  public static Node getElement(Element element, String name) {
    return element.getElementsByTagName(name).item(0);
  }

  public static String getText(Node node) {
    return node != null ? node.getTextContent() : null;
  }

  public static long parseTime(String time) throws ParseException {
    return getDateFormat().parse(time).getTime();
  }

  public static String formatTime(long time) throws ParseException {
    return getDateFormat().format(new Date(time));
  }

  private static DateFormat getDateFormat() {
    if (dateFormat == null) {
      dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    }

    return dateFormat;
  }

}
