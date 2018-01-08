package fsm.common.jsonParameters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fsm.common.Log;

public class Parameters
{    
   static public String GetKeyForArrayIndex(String key, int indexFromZero)
   {
      return key + "[" + (indexFromZero+1) + "]";
   }

   public Parameters(String filename)
   {
      this(filename, true);
   }

   public Parameters(String filename, boolean readFile)
   {
      filename_ = filename;
      paramSet_ = new HashMap<String, Object>();
      if ( readFile )
      {
         read();
      }
   }

   public Reader getReader() 
   {
      return new Reader(paramSet_);
   }

   public Writer getWriter() 
   {
      return new Writer(paramSet_);
   }

   public void read()
   {
      File file = new File(filename_); 
      JSONParser jsonParser = new JSONParser();

      try (FileReader reader = new FileReader(file))
      {
         recursiveRead(paramSet_, (JSONObject) jsonParser.parse(reader));
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
         JSONObject jsonObject = recursiveWrite(paramSet_);
         writer.write(jsonObject.toJSONString());
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
         System.out.println(key + "=" + paramSet_.get(key));
      }      
   }

   // --- PRIVATE

   private void recursiveRead(Map<String, Object> paramSet, 
                              JSONObject jsonObject)
   {  
      @SuppressWarnings("rawtypes")
      Set keys = jsonObject.keySet();
      for ( Object key : keys )
      {
         Object value = jsonObject.get(key);
         if ( value == null )
         {
            paramSet.put(key.toString(), null);
         }
         else if ( value instanceof JSONObject )
         {
            Map<String, Object> subParamsSet = new HashMap<String, Object>();
            paramSet.put(key.toString(), subParamsSet);
            recursiveRead(subParamsSet, (JSONObject) value);
         }
         else if ( value instanceof JSONArray )
         {
            JSONArray array = (JSONArray) value;
            if ( array.size() <= 0 )
            {
               continue;
            }
            paramSet.put(key + "[0]", new Integer(array.size()));
            for ( int ii=0; ii<array.size(); ii++ )
            {
               if ( array.get(ii) instanceof JSONObject )
               {
                  Map<String, Object> subParamsSet = new HashMap<String, Object>();
                  paramSet.put(key + "[" + (ii+1) + "]", subParamsSet);
                  recursiveRead(subParamsSet,
                                (JSONObject) array.get(ii));
               }
               else
               {
                  paramSet.put(key + "[" + (ii+1) + "]", array.get(ii));
               }
            }
         }
         else if ( value instanceof Boolean 
                  || value instanceof Long 
                  || value instanceof Double 
                  || value instanceof String )
         {
            paramSet.put(key.toString(), value);
         }
         else if ( value instanceof Double )
         {
            paramSet.put(key.toString(), value);
         }
         else if ( value instanceof String )
         {
            paramSet.put(key.toString(), (String) value);
         }
         else
         {
            Log.info("key[" + key + "] had an unknown type [" + value.getClass().getName() + "]");
         }
      }

   }

   @SuppressWarnings("unchecked")
   private JSONObject recursiveWrite(HashMap<String, Object> paramSet)
   {
      Reader reader = new Reader(paramSet);
      JSONObject jsonObject = new JSONObject();
      
      // Handle arrays
      for ( String key : reader.getKeySet() )
      {
         if ( reader.isKeyForArrayOfStructs(key) )
         {
            JSONArray list = new JSONArray();
            for ( int ii=0; ii<reader.getArrayLength(key); ii++ )
            {
               list.add(recursiveWrite((HashMap<String, Object>)
                                       paramSet.get(GetKeyForArrayIndex(key, ii))));         
            }
            jsonObject.put(key, list);
         }
         else if ( reader.isKeyForArrayOfValues(key) )
         {
            JSONArray list = new JSONArray();
            for ( int ii=0; ii<reader.getArrayLength(key); ii++ )
            {
               list.add(paramSet.get(GetKeyForArrayIndex(key, ii)));
            }
            jsonObject.put(key, list);
         }  
         else if ( reader.isKeyForStruct(key) )
         {
            jsonObject.put(key, recursiveWrite((HashMap<String, Object>) paramSet.get(key)));
         }
         else 
         {
            jsonObject.put(key, paramSet.get(key));
         }
      }
      return jsonObject;
   }

   private String filename_;
   private HashMap<String, Object> paramSet_;

}
