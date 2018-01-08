package fsm.common.jsonParameters;

import java.util.HashMap;
import java.util.Map;

public class Writer
{
   @SuppressWarnings("unchecked")
   public Writer struct(String key)
   {
      Object object = currMap_.get(key);
      if ( object == null )
      {
         Map<String, Object> newMap = new HashMap<String, Object>();
         currMap_.put(key, newMap);
         currMap_ = newMap;
      }
      else
      {
         currMap_ = (Map<String, Object>) object;
      }
      return this;      
   }
   
   public void setValue(String key, String value) 
   {
      currMap_.put(key, new String(value));
   }
   
   public void setValue(String key, boolean value) 
   {
      currMap_.put(key, new Boolean(value));
   }
   
   public void setValue(String key, long value) 
   {
      currMap_.put(key, new Long(value));
   }
   
   public void setValue(String key, double value) 
   {
      currMap_.put(key, new Double(value));
   }
   
   public void setArrayOfValues(String key, String[] value) 
   {
      deleteArray(key);
      if ( value.length <= 0 )
      {
         return;
      }
      currMap_.put(key +"[0]", new Integer(value.length));
      for ( int ii=0; ii<value.length; ii++ )
      {
         currMap_.put(key +"[" + (ii+1) + "]", new String(value[ii]));
      }
   }
   
   public void setArrayOfValues(String key, boolean[] value) 
   {
      deleteArray(key);
      if ( value.length <= 0 )
      {
         return;
      }
      currMap_.put(key +"[0]", new Integer(value.length));
      for ( int ii=0; ii<value.length; ii++ )
      {
         currMap_.put(key +"[" + (ii+1) + "]", new Boolean(value[ii]));
      }
   }
   
   public void setArrayOfValues(String key, long[] value) 
   {
      deleteArray(key);
      if ( value.length <= 0 )
      {
         return;
      }
      currMap_.put(key +"[0]", new Integer(value.length));
      for ( int ii=0; ii<value.length; ii++ )
      {
         currMap_.put(key +"[" + (ii+1) + "]", new Long(value[ii]));
      }
   }
   
   public void setArrayOfValues(String key, double[] value) 
   {
      deleteArray(key);
      if ( value.length <= 0 )
      {
         return;
      }
      currMap_.put(key +"[0]", new Integer(value.length));
      for ( int ii=0; ii<value.length; ii++ )
      {
         currMap_.put(key +"[" + (ii+1) + "]", new Double(value[ii]));
      }
   }
   
   public void allocateArrayOfStructs(String key, int length) 
   {
      deleteArray(key);
      currMap_.put(key +"[0]", new Integer(length));
      for ( int ii=0; ii<length; ii++ )
      {
         Map<String, Object> struct = new HashMap<String, Object>();
         currMap_.put(key +"[" + (ii+1) + "]", struct);
      }
   }
   
   // --- PACKAGE PROTECTED
   
   Writer(Map<String, Object> baseMap)
   {
      currMap_ = baseMap;
   }
   
   // --- PRIVATE
   
   private void deleteArray(String key)
   {
      Object object = currMap_.get(key + "[0]");
      if ( object == null )
      {
         return;
      }
      int length = 0;
      if ( object instanceof Integer )
      {
         length = (Integer) object;
      }
      currMap_.remove(key + "[0]");
      for ( int ii=0; ii<length; ii++ )
      {
         currMap_.remove(key + "[" + ii + "]");         
      }
   }
   
   private Map<String, Object> currMap_;

}
