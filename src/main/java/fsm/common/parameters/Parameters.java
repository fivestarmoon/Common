package fsm.common.parameters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fsm.common.Log;
import fsm.common.parameters.InternalDef.Struct;

public class Parameters
{ 
   public Parameters(String filename)
   {
      this(filename, true);
   }

   public Parameters(String filename, boolean readFile)
   {
      filename_ = filename;
      if ( readFile )
      {
         read();
      }
      else
      {
         jsonStruct_ = new Struct();
      }
   }

   public Reader getReader() 
   {
      return new Reader(jsonStruct_);
   }

   public Writer getWriter() 
   {
      return new Writer(jsonStruct_);
   }

   public void read()
   {
      File file = new File(filename_); 
      JSONParser jsonParser = new JSONParser();

      try (FileReader reader = new FileReader(file))
      {
         jsonStruct_ = new Struct((JSONObject) jsonParser.parse(reader));
      }
      catch (FileNotFoundException e)
      {
         Log.severe("File " + filename_+ " not found");
      }
      catch (IOException e)
      {
         Log.severe("File " + filename_+ " io exception " + e);
      }
      catch (ParseException e)
      {
         Log.severe("File " + filename_+ " JSON parsing exception " + e);
      }
   }

   public void write() 
   {
      File file = new File(filename_); 
      try (FileWriter writer = new FileWriter(file))
      {
         StringBuilder string = new StringBuilder();
         jsonStruct_.toJSONString(string);
         writer.write(string.toString());
         writer.flush();
      }
      catch (FileNotFoundException e)
      {
         e.printStackTrace();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   public void dump()
   {
      Reader reader = getReader();
      String[] keySet = reader.getKeySet();
      Arrays.sort(keySet);
      for ( String key : keySet )
      {
         System.out.println(key + "=" + jsonStruct_.struct.get(key));
      }      
   }

   // --- PRIVATE

   private String filename_;
   private Struct jsonStruct_;

}
