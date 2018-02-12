package csm.common.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import fsm.common.Log;

/**
 * Execute a independent process from the current java process instance.
 */
public class ProcessWrapper implements Runnable
{
   /**
    * @param args command line arguments as per Runtime.getRuntime().exec()
    */
   public ProcessWrapper(String[] args)
   {
      args_ = new String[args.length];
      for ( int ii=0; ii<args.length; ii++ )
      {
         args_[ii] = new String(args[ii]);
      }
      handle_ = null;
   }
   
   /**
    * Run a process in a background thread and capture stdout and stderr to
    * prevent conflicts with calling java process instance
    */
   public void start()
   {
      // Wait for the previous process instance background thread to stop
      waitFor();
      
      // Create and run a thread instance to run in the background
      handle_ = new Thread(this);
      handle_.start();
   }
   
   /**
    * Wait for the process in the background thread to stop
    */
   public void waitFor()
   {
      while ( handle_ != null )
      {
         try
         {
            Thread.sleep(100);
         }
         catch (InterruptedException e)
         {
         }
      }
   }
   
   /**
    * Shutdown the background thread forcibly
    */
   public void shutdown()
   {
      final Process processInstance = process_;
      if ( processInstance != null )
      {
         processInstance.destroyForcibly();
      }
   }
   
   /**
    * Run the thread in the background
    */
   @Override
   public void run()
   {            
     try
     {
        // Log the command
        String cmdString = "";
        for ( String arg : args_ )
        {
           cmdString += arg + " ";
        }
        Log.fine(String.format("UTIL_Process.run(%s)\n", cmdString));
        
        // Start the process
        process_ = Runtime.getRuntime().exec(args_);
        
        // Consume the streams in separate background threads
        new ReadStream("stdout", process_.getInputStream());
        new ReadStream("stderr", process_.getErrorStream());
        
        // Wait for the process to stop
        process_.waitFor();
     }
     catch(IOException | InterruptedException e)
     {   
        Log.severe("Fatal error running process", e);
     }
     finally
     {
        if ( process_ != null )
        {
           process_.destroy();
        }
        process_ = null;
        handle_ = null;
     }
   }

   // --- PRIVATE
   
   private String[] args_;
   private Thread  handle_;
   private Process process_;

   /**
    * Private threads to consume a stream from a process
    *
    */
   private class ReadStream implements Runnable 
   {
      public ReadStream(String name, InputStream is) 
      {
         this.streamName_ = name;
         this.inputStream_ = is;
         new Thread(this).start();
      }  
      
      public void run() 
      {
         try 
         {
            InputStreamReader isr = new InputStreamReader(inputStream_);
            BufferedReader br = new BufferedReader(isr);   
            while (true) 
            {
               String s = br.readLine();
               if (s == null)
               {
                  break;
               }
               Log.fine("[" + streamName_ + "] " + s);
            }
            inputStream_.close ();    
         } 
         catch (Exception e) 
         {
            Log.severe("Problem reading stream " + streamName_, e);
         }
      }
      
      // --- PRIVATE
      
      private String streamName_;
      private InputStream inputStream_;
      
   } // class ReadStream
}

