package fsm.common.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Logger
{
   public static void init_s()
   {
      
   }
   
   public static void info(String format, Object... args)
   {
      localLogOut("INF", String.format(format, args));
   }
   
   public static void warning(String format, Object... args)
   {
      localLogOut("WAR", String.format(format, args));
   }
   
   public static void error(String format, Object... args)
   {
      localLogErr("ERR", String.format(format, args));
   }
   
   public static void error(Exception e)
   {
      localLogErr("ERR", String.format("%s [%d]: %s", 
         e.getStackTrace()[0].getFileName(),
         e.getStackTrace()[0].getLineNumber(),
         e.getMessage()));
   }
   
   // --- PRIVATE
   
   private static void localLogOut(String level, String message)
   {
      LocalDateTime timeNow = LocalDateTime.now();
      System.out.format("%s [%3s] %s\n", timeNow.format(formatter_s), level, message);
   }
   
   private static void localLogErr(String level, String message)
   {
      LocalDateTime timeNow = LocalDateTime.now();
      System.err.format("%s [%3s] %s\n", timeNow.format(formatter_s), level, message);
   }
   
   private static DateTimeFormatter formatter_s = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
   
   

}
