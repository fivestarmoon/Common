package fsm.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Log extends Formatter
{
   static Logger LOGGER = Logger.getLogger(Log.class.getName());
   
   public static void Init()
   {
      LOGGER.addHandler(new ConsoleHandler());
      Handler[] handlers = LOGGER.getHandlers();
      for ( Handler hand : handlers )
      {
         hand.setFormatter(new Log());
      }
      System.out.println("Logger initialized");
   }
   
   public static void fine(String msg)
   {
      logMessage(Level.FINE, msg, null);
   }
   
   public static void info(String msg)
   {
      logMessage(Level.INFO, msg, null);
   }
   
   public static void severe(String msg)
   {
      logMessage(Level.SEVERE, msg, null);
   }
   
   public static void fine(String msg, Throwable t)
   {
      logMessage(Level.FINE, msg, t);
   }
   
   public static void info(String msg, Throwable t)
   {
      logMessage(Level.INFO, msg, t);
   }
   
   public static void severe(String msg, Throwable t)
   {
      logMessage(Level.SEVERE, msg, t);
   }
   
   private static void logMessage(Level level, String message, Throwable t)
   {
      String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
      String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
      String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
      int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
      
      LogRecord record = new LogRecord(level, 
                                       className + "." + methodName + "()[" + lineNumber + "] " + message);
      record.setThrown(t);

      for ( Handler hand : LOGGER.getHandlers() )
      {
         hand.publish(record);
      }
   }

   @Override
   public String format(LogRecord record)
   {
      StringBuilder sb = new StringBuilder();

      sb.append(new Date(record.getMillis()))
      .append(" ")
      .append(record.getLevel().getLocalizedName())
      .append(": ")
      .append(formatMessage(record))
      .append("\n");

      if (record.getThrown() != null) 
      {
         try 
         {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            record.getThrown().printStackTrace(pw);
            pw.close();
            sb.append(sw.toString());
         } 
         catch (Exception ex) 
         {
            // ignore
         }
      }   

      return sb.toString();
   }
}
