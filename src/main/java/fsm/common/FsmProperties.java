package fsm.common;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FsmProperties
{
   public FsmProperties()
   {
      properties_ = new ArrayList<LocalProperty>();
   }
   
   public String getProperty(String key)
   {
      for ( int ii=0; ii<properties_.size(); ii++ )
      {
         if ( key.equals(properties_.get(ii).key) )
         {
            return properties_.get(ii).value;
         }
      }
      return null;
   }
   
   public String getProperty(String key, String defaultValue)
   {
      for ( int ii=0; ii<properties_.size(); ii++ )
      {
         if ( key.equals(properties_.get(ii).key) )
         {
            return properties_.get(ii).value;
         }
      }
      return defaultValue;
   }
   
   public void setProperty(String key, String value)
   {
      for ( int ii=0; ii<properties_.size(); ii++ )
      {
         if ( key.equals(properties_.get(ii).key) )
         {
            properties_.get(ii).value = value;
            return;
         }
      }
      
      LocalProperty newProperty = new LocalProperty();
      newProperty.key = key;
      newProperty.value = value;
      properties_.add(newProperty);
      return;
   }
   
   public void load(File file) throws IOException
   {
      properties_.clear();
      
      BufferedReader reader = null;
      try
      {
         reader = new BufferedReader(new FileReader(file));
         
         String line = null;
         while ((line = reader.readLine()) != null)
         {
            // Attempt to parse the line
            try
            {
               // Find the first "="
               int equalPos = -1;
               if ( (equalPos = line.indexOf('=')) > 0 )
               {
                  // Split the line into a key and a value based on the first "="
                  LocalProperty newProperty = new LocalProperty();
                  newProperty.key = line.substring(0, equalPos);
                  if ( equalPos < line.length() )
                  {
                     newProperty.value = line.substring(equalPos+1, line.length());
                  }
                  else
                  {
                     newProperty.value = "";
                  }
                  
                  // Add the property to the list
                  properties_.add(newProperty);
               }
            }
            catch ( Exception e )
            {
               e.printStackTrace();
               System.out.println(line);
            }
         }
      }
      catch (IOException e)
      {
         throw e;
      }
      finally
      {
         if ( reader != null )
         {
            reader.close();
         }
      }
   }
   
   public void store(File file) throws IOException
   {      
      // Open the writer
      BufferedWriter writer = null;
      try
      {
         writer = new BufferedWriter(new FileWriter(file));
         
         // Write each property to the file
         for ( int ii=0; ii<properties_.size(); ii++ )
         {
            writer.write(
                  properties_.get(ii).key 
                  + "=" + properties_.get(ii).value 
                  + "\r\n");  // make it windows friendly
         }
      }
      catch (IOException e)
      {
         throw e;
      }
      finally
      {
         if ( writer != null )
         {
            writer.close();
         }
      }
   }

   private ArrayList<LocalProperty> properties_;
   
   private static class LocalProperty
   {
      public String key;
      public String value;
   }

   public void setProperty(String string, int width)
   {
      setProperty(string, Integer.toString(width));
   }

   public int getProperty(String string, int width)
   {
      return Integer.parseInt(getProperty(string, Integer.toString(width)));
   }
}
