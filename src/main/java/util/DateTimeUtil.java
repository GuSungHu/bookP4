package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
  public static String ldtToString(LocalDateTime ldt) {
    return ldt.format(DateTimeFormatter.ofPattern("yy-MM-dd"));
  }
}
