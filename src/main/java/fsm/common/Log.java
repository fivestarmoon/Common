package fsm.common;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Log extends Formatter
{
   static Logger LOGGER = Logger.getLogger(Log.class.getName());  
   
   public static void Init(File logFile)
   {
      LOGGER.addHandler(new LocalConsole());
      System.out.println("Console logger initialized ...");
      try
      {
         LOGGER.addHandler(new FileHandler(logFile.getAbsolutePath(), true));
         System.out.println("File logger initialized [" + logFile.getAbsolutePath() + "] ...");
      }
      catch(Exception e)
      {
         System.out.println("File logger failed [" + logFile.getAbsolutePath() + "] ...");
      }
      Handler[] handlers = LOGGER.getHandlers();
      for ( Handler hand : handlers )
      {
         hand.setFormatter(new Log());
      }
   }
   
   public static void fine(String msg)
   {
      logMessage(Level.FINE, msg, null);
   }
   
   public static void fine(String format, Object... args)
   {
      logMessage(Level.FINE, String.format(format, args), null);
   }
   
   public static void info(String msg)
   {
      logMessage(Level.INFO, msg, null);
   }
   
   public static void info(String format, Object... args)
   {
      logMessage(Level.INFO, String.format(format, args), null);
   }
   
   public static void severe(String msg)
   {
      logMessage(Level.SEVERE, msg, null);
   }
   
   public static void severe(String format, Object... args)
   {
      logMessage(Level.SEVERE, String.format(format, args), null);
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
   
   // --- PRIVATE
   
   private static void logMessage(Level level, String message, Throwable t)
   {
      String fileName = Thread.currentThread().getStackTrace()[3].getFileName();
      int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
      
      LogRecord record;
      if ( t != null || level == Level.WARNING || level == Level.SEVERE )
      {
         record = new LogRecord(level, fileName + " line " + lineNumber + ": " + message);
      }
      else
      {
         record = new LogRecord(level, message);
      }
      record.setThrown(t);

      for ( Handler hand : LOGGER.getHandlers() )
      {
         hand.publish(record);
      }
   }

   @Override
   public String format(LogRecord record)
   {
      String logLevel = String.format("%-3.3s", record.getLevel().getLocalizedName());
      StringBuilder sb = new StringBuilder();

      sb.append(LocalDateTime.now().format(formatter_s))
      .append(" [")
      .append(logLevel)
      .append("] ")
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
   
   private static class LocalConsole extends Handler
   {

      @Override
      public void publish(LogRecord record)
      {
         System.out.print(getFormatter().format(record));         
      }

      @Override
      public void flush()
      {
      }

      @Override
      public void close() throws SecurityException
      {
         System.out.println("Console logger done.");
      }      
   }
   
   private static DateTimeFormatter formatter_s = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
}
