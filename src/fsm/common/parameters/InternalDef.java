package fsm.common.parameters;

import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import fsm.common.Log;

class InternalDef
{   
   static class Struct
   {
      Struct()
      {
         struct = new HashMap<String, Object>();  
      }
      Struct(JSONObject jsonObject)
      {
         this();
         @SuppressWarnings("rawtypes")
         Set keys = jsonObject.keySet();
         for ( Object key : keys )
         {
            Object value = jsonObject.get(key);
            if ( value instanceof JSONObject )
            {
               struct.put(key.toString(), new Struct((JSONObject) value));
            }
            else if ( value instanceof JSONArray )
            {
               JSONArray array = (JSONArray) value;
               if ( StructArray.BuildFrom(array) )
               {
                  struct.put(key.toString(), new StructArray(array));
               }
               else if ( StringArray.BuildFrom(array) )
               {
                  struct.put(key.toString(), new StringArray(array));
               }
               else if ( BooleanArray.BuildFrom(array) )
               {
                  struct.put(key.toString(), new BooleanArray(array));
               }
               else if ( LongArray.BuildFrom(array) )
               {
                  struct.put(key.toString(), new LongArray(array));
               }
               else if ( DoubleArray.BuildFrom(array) )
               {
                  struct.put(key.toString(), new DoubleArray(array));
               }
               else
               {
                  Log.info("key[" + key + "] had an unknown array type [" + value.getClass().getName() + "]");
               }
            }
            else if ( value instanceof Boolean 
                     || value instanceof Long 
                     || value instanceof Double 
                     || value instanceof String )
            {
               struct.put(key.toString(), value);
            }
            else
            {
               Log.info("key[" + key + "] had an unknown type [" + value.getClass().getName() + "]");
            }
         }
         
      }
      public void toJSONString(StringBuilder stringbuilder)
      {
         stringbuilder.append("{\n");
         for ( String key : struct.keySet() )
         {
            stringbuilder.append("\"" + key + "\":");
            Object value = struct.get(key);
            if ( value instanceof Struct )
            {
               stringbuilder.append("\n");
               ((Struct)value).toJSONString(stringbuilder);
            }
            else if ( value instanceof StructArray )
            {
               stringbuilder.append(" [\n");
               ((StructArray)value).toJSONString(stringbuilder);
               stringbuilder.append("],");
            }
            else if ( value instanceof StringArray )
            {
               stringbuilder.append(" [");
               ((StringArray)value).toJSONString(stringbuilder);
               stringbuilder.append("],");
            }
            else if ( value instanceof BooleanArray )
            {
               stringbuilder.append(" [");
               ((BooleanArray)value).toJSONString(stringbuilder);
               stringbuilder.append("],");
            }
            else if ( value instanceof LongArray )
            {
               stringbuilder.append(" [");
               ((LongArray)value).toJSONString(stringbuilder);
               stringbuilder.append("],");
            }
            else if ( value instanceof DoubleArray )
            {  
               stringbuilder.append(" [");             
               ((DoubleArray)value).toJSONString(stringbuilder);
               stringbuilder.append("],");
            }
            else if ( value instanceof String )
            {
               stringbuilder.append(" \"" + value.toString() + "\",");   
            }
            else
            {
               stringbuilder.append(" " + value.toString() + ",");   
            }
            stringbuilder.append("\n");
         }
         stringbuilder.append("}");         
      }      
      
      HashMap<String, Object> struct;
   }
   
   static class StructArray
   {
      static boolean BuildFrom(JSONArray array)
      {
         if ( array.size() <= 0 )
         {
            return false;
         }
         if ( array.get(0) instanceof JSONObject )
         {
            return true;
         }
         return false;         
      }
      public void toJSONString(StringBuilder string)
      {
         for ( Struct struct : array_ )
         {
            struct.toJSONString(string);
            string.append(",\n");
         }
      }
      StructArray(JSONArray jsonArray)
      {
         array_ = new Struct[jsonArray.size()];
         for ( int ii=0; ii<jsonArray.size(); ii++ )
         {
            array_[ii] = new Struct((JSONObject) jsonArray.get(ii));
         }
         
      }
      StructArray(Writer[] array)
      {
         array_ = new Struct[array.length];
         for ( int ii=0; ii<array.length; ii++ )
         {
            array_[ii] = array[ii].getStruct();
         }         
      }
      
      Struct[] array_;
   }   
   
   static class StringArray
   {
      static boolean BuildFrom(JSONArray array)
      {
         if ( array.size() <= 0 )
         {
            return false;
         }
         if ( array.get(0) instanceof String )
         {
            return true;
         }
         return false;         
      }
      StringArray(JSONArray jsonArray)
      {
         array_ = new String[jsonArray.size()];
         for ( int ii=0; ii<jsonArray.size(); ii++ )
         {
            array_[ii] = new String((String) jsonArray.get(ii));
         }         
      }
      StringArray(String[] array)
      {
         array_ = array.clone();
         
      }
      public void toJSONString(StringBuilder string)
      {
         for ( int ii=0; ii<array_.length; ii++ )
         {
            string.append("\"" + array_[ii] + "\"");
            if ( ii < (array_.length-1) )
            {
               string.append(',');
            }
         }           
      }
      String[] array_;
   } 
   
   static class BooleanArray
   {
      static boolean BuildFrom(JSONArray array)
      {
         if ( array.size() <= 0 )
         {
            return false;
         }
         if ( array.get(0) instanceof Boolean )
         {
            return true;
         }
         return false;         
      }
      BooleanArray(JSONArray jsonArray)
      {
         array_ = new boolean[jsonArray.size()];
         for ( int ii=0; ii<jsonArray.size(); ii++ )
         {
            array_[ii] = (Boolean) jsonArray.get(ii);
         }
         
      }
      BooleanArray(boolean[] array)
      {
         array_ = array.clone();
         
      }
      public void toJSONString(StringBuilder string)
      {
         for ( int ii=0; ii<array_.length; ii++ )
         {
            string.append(array_[ii]);
            if ( ii < (array_.length-1) )
            {
               string.append(',');
            }
         }           
      }
      boolean[] array_;
   } 
   
   static class LongArray
   {
      static boolean BuildFrom(JSONArray array)
      {
         if ( array.size() <= 0 )
         {
            return false;
         }
         if ( array.get(0) instanceof Long )
         {
            return true;
         }
         return false;         
      }
      LongArray(JSONArray jsonArray)
      {
         array_ = new long[jsonArray.size()];
         for ( int ii=0; ii<jsonArray.size(); ii++ )
         {
            array_[ii] = (Long) jsonArray.get(ii);
         }
         
      }
      LongArray(long[] array)
      {
         array_ = array.clone();
         
      }
      public void toJSONString(StringBuilder string)
      {
         for ( int ii=0; ii<array_.length; ii++ )
         {
            string.append(array_[ii]);
            if ( ii < (array_.length-1) )
            {
               string.append(',');
            }
         }           
      }
      long[] array_;
   } 
   
   static class DoubleArray
   {
      static boolean BuildFrom(JSONArray array)
      {
         if ( array.size() <= 0 )
         {
            return false;
         }
         if ( array.get(0) instanceof Double )
         {
            return true;
         }
         return false;         
      }
      DoubleArray(JSONArray jsonArray)
      {
         array_ = new double[jsonArray.size()];
         for ( int ii=0; ii<jsonArray.size(); ii++ )
         {
            array_[ii] = (Double) jsonArray.get(ii);
         }
         
      }
      DoubleArray(double[] array)
      {
         array_ = array.clone();
         
      }
      public void toJSONString(StringBuilder string)
      {
         for ( int ii=0; ii<array_.length; ii++ )
         {
            string.append(array_[ii]);
            if ( ii < (array_.length-1) )
            {
               string.append(',');
            }
         }           
      }
      double[] array_;
   }

}
