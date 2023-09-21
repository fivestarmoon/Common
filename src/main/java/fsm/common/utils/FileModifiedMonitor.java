package fsm.common.utils;

import java.io.File;

import fsm.common.Log;

public class FileModifiedMonitor implements Runnable
{
   public FileModifiedMonitor(File file, FileModifiedListener l)
   {
      file_ = file;
      fileModifiedListener_ = l;
      if ( fileModifiedListener_ != null )
      {
         Thread thread = new Thread(this);
         thread.setDaemon(true);
         thread.start();
      }
   }
   @Override
   public void run()
   {
      long timeUpdated = file_.lastModified();
      Log.info("File " + file_.getName() + " modified monitor started");
      while ( true)
      {
         if ( fileModifiedListener_ == null )
         {
            break;
         }
         long newModified = file_.lastModified();
         if ( newModified != timeUpdated )
         {
            break;
         }
         try
         {
            Thread.sleep(1000);
         }
         catch (InterruptedException e)
         {
         }
      }
      
      // Attempt to call the listener
      FileModifiedListener fileModifiedListener = null;
      synchronized(file_)
      {
         fileModifiedListener = fileModifiedListener_;
      }
      if ( fileModifiedListener != null )
      {
         fileModifiedListener.fileModified();
         Log.info("File " + file_.getName() + " modified, listener called and stopped");
      }
      else
      {
         Log.info("File " + file_.getName() + " modified monitor aborted"); 
      }
      
      // Cleanup
      fileModifiedListener_ = null;
      file_ = null;
   }  
   public void stop()
   {
      File file = file_;
      if ( file == null )
      {
         return;
      }
      synchronized(file)
      {
         fileModifiedListener_ = null;
      }
   }
   private File file_;
   private FileModifiedListener fileModifiedListener_;
}
