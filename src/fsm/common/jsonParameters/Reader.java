package fsm.common.jsonParameters;

import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import fsm.common.Log;

public class Reader
{
   @SuppressWarnings("unchecked")
   public Reader struct(String key)
   {
      Object object = currMap_.get(key);
      if ( object == null || object instanceof Map )
      {
         currMap_ = (Map<String, Object>) object;
      }
      else
      {
         Log.severe("Failed to enter struct [" + key + "]");
      }
      return this;      
   }
   
   public String[] getKeySet() 
   {
      Set<String> set = currMap_.keySet();
      LinkedList<String> setCopy = new  LinkedList<String>();
      setCopy.addAll(set);
      for ( String key : set )
      {
         Object value = currMap_.get(key);
         
         // Handle arrays
         if ( value instanceof Integer )
         {
            // Remove the length key
            int length = (Integer) value;
            setCopy.remove(key);
            int lastBracket = key.lastIndexOf('[');
            
            // Add the key for the array
            key = key.substring(0, lastBracket);
            setCopy.add(key);
            
            // Remove all array value keys
            for ( int ii=0; ii<length; ii++ )
            {
               setCopy.remove(Parameters.GetKeyForArrayIndex(key, ii));
            }
         }
      }
      return setCopy.toArray(new String[setCopy.size()]);
   }
   
   public boolean isKeyForStruct(String key)
   {
      Object object = currMap_.get(key);
      if ( object != null && object instanceof Map )
      {
         return true;
      }
      return false;
   }
   
   public boolean isKeyForArrayOfValues(String key)
   {
      Object object = currMap_.get(key + "[0]");
      if ( object != null && object instanceof Integer )
      {
         return !isKeyForStruct(key + "[1]");
      }
      return false;
   }
   
   public boolean isKeyForArrayOfStructs(String key)
   {
      Object object = currMap_.get(key + "[0]");
      if ( object != null && object instanceof Integer )
      {
         return isKeyForStruct(key + "[1]");
      }
      return false;
   }
   
   public boolean isKeyForValue(String key)
   {
      if ( isKeyForStruct(key) || isKeyForArrayOfValues(key) )
      {
         return false;
      }
      return true;
   }
   
   public int getArrayLength(String key)
   {
      Object object = currMap_.get(key + "[0]");
      if ( object == null || !(object instanceof Integer) )
      {
         Log.severe("Failed to get array length for [" + key + "]");
         return 0;
      }
      return (Integer) object;
   }
   
   public String getValue(String key, String defaultValue) 
   {
      Object object = currMap_.get(key);
      if ( object == null || !(object instanceof String) )
      {
         Log.severe("Failed to get string value for [" + key + "]");
         return defaultValue;
      }
      return (String) object;
   }
   
   public boolean getValue(String key, boolean defaultValue) 
   {
      Object object = currMap_.get(key);
      if ( object == null || !(object instanceof Boolean) )
      {
         Log.severe("Failed to get boolean value for [" + key + "]");
         return defaultValue;
      }
      return (Boolean) object;
   }
   
   public long getValue(String key, long defaultValue) 
   {
      Object object = currMap_.get(key);
      if ( object == null || !(object instanceof Long) )
      {
         Log.severe("Failed to get long value for [" + key + "]");
         return defaultValue;
      }
      return (Long) object;
   }
   
   public double getValue(String key, double defaultValue) 
   {
      Object object = currMap_.get(key);
      if ( object == null || !(object instanceof Double) )
      {
         Log.severe("Failed to get double value for [" + key + "]");
         return defaultValue;
      }
      return (Double) object;
   }
   
   // --- PACKAGE PROTECTED
   
   Reader(Map<String, Object> baseMap)
   {
      currMap_ = baseMap;
   }
   
   // --- PRIVATE
   
   private Map<String, Object> currMap_;

}
