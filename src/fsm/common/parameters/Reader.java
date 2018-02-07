package fsm.common.parameters;

import java.util.Set;

import fsm.common.Log;
import fsm.common.parameters.InternalDef.BooleanArray;
import fsm.common.parameters.InternalDef.DoubleArray;
import fsm.common.parameters.InternalDef.LongArray;
import fsm.common.parameters.InternalDef.StringArray;
import fsm.common.parameters.InternalDef.Struct;
import fsm.common.parameters.InternalDef.StructArray;

public class Reader
{
   public Reader struct(String key)
   {
      verbose_ = true;
      Object object = currStruct_.struct.get(key);
      if ( object == null || object instanceof Struct )
      {
         currStruct_ = (Struct) object;
      }
      else
      {
         if (verbose_) Log.severe("Failed to enter struct [" + key + "]");
      }
      return this;      
   }
   
   public Reader[] structArray(String key)
   {
      Object object = currStruct_.struct.get(key);
      if ( object != null && object instanceof StructArray )
      {
         StructArray array = (StructArray) object;
         Reader[] readers = new Reader[array.array_.length];
         for ( int ii=0; ii<readers.length; ii++ )
         {
            readers[ii] = new Reader(array.array_[ii]);
         }
         return readers;
      }
      else
      {
         if (verbose_) Log.severe("Failed to enter structarray [" + key + "]");
         return new Reader[0];
      }   
   }
   
   public String[] getKeySet() 
   {
      Set<String> set = currStruct_.struct.keySet();
      return set.toArray(new String[set.size()]);
   }
   
   public boolean isKeyForStruct(String key)
   {
      Object object = currStruct_.struct.get(key);
      if ( object != null && object instanceof Struct )
      {
         return true;
      }
      return false;
   }
   
   public boolean isKeyForArrayOfValues(String key)
   {
      Object object = currStruct_.struct.get(key);
      if ( object != null 
               && ( object instanceof StringArray
                    || object instanceof BooleanArray 
                    || object instanceof LongArray
                    || object instanceof DoubleArray) )
      {
         return true;
      }
      return false;
   }
   
   public boolean isKeyForArrayOfStructs(String key)
   {
      Object object = currStruct_.struct.get(key);
      if ( object != null && object instanceof StructArray )
      {
         return true;
      }
      return false;
   }
   
   public boolean isKeyForValue(String key)
   {
      Object object = currStruct_.struct.get(key);
      if ( object != null 
               && ( object instanceof String
                    || object instanceof Boolean 
                    || object instanceof Long
                    || object instanceof Double) )
      {
         return true;
      }
      return false;
   }
   
   public String getStringValue(String key, String defaultValue) 
   {
      Object object = currStruct_.struct.get(key);
      if ( object == null || !(object instanceof String) )
      {
         if (verbose_) Log.severe("Failed to get string value for [" + key + "]");
         return defaultValue;
      }
      return (String) object;
   }
   
   public boolean getBooleanValue(String key, boolean defaultValue) 
   {
      Object object = currStruct_.struct.get(key);
      if ( object == null || !(object instanceof Boolean) )
      {
         if (verbose_) Log.severe("Failed to get boolean value for [" + key + "]");
         return defaultValue;
      }
      return (Boolean) object;
   }
   
   public long getLongValue(String key, long defaultValue) 
   {
      Object object = currStruct_.struct.get(key);
      if ( object == null || !(object instanceof Long) )
      {
         if (verbose_) Log.severe("Failed to get long value for [" + key + "]");
         return defaultValue;
      }
      return (Long) object;
   }
   
   public double getDoubleValue(String key, double defaultValue) 
   {
      Object object = currStruct_.struct.get(key);
      if ( object == null || !(object instanceof Double) )
      {
         if (verbose_) Log.severe("Failed to get double value for [" + key + "]");
         return defaultValue;
      }
      return (Double) object;
   }
   
   public String[] getStringArray(String key) 
   {
      Object object = currStruct_.struct.get(key);
      if ( object == null || !(object instanceof StringArray) )
      {
         if (verbose_) Log.severe("Failed to get string array for [" + key + "]");
         return new String[0];
      }
      else
      {
         StringArray array = (StringArray) object;
         return array.array_.clone();
      }
   }
   
   public boolean[] getBooleanArray(String key) 
   {
      Object object = currStruct_.struct.get(key);
      if ( object == null || !(object instanceof BooleanArray) )
      {
         if (verbose_) Log.severe("Failed to get boolean array for [" + key + "]");
         return new boolean[0];
      }
      else
      {
         BooleanArray array = (BooleanArray) object;
         return array.array_.clone();
      }
   }
   
   public long[] getLongArray(String key) 
   {
      Object object = currStruct_.struct.get(key);
      if ( object == null || !(object instanceof LongArray) )
      {
         if (verbose_) Log.severe("Failed to get long array for [" + key + "]");
         return new long[0];
      }
      else
      {
         LongArray array = (LongArray) object;
         return array.array_.clone();
      }
   }
   
   public double[] getDoubleArray(String key) 
   {
      Object object = currStruct_.struct.get(key);
      if ( object == null || !(object instanceof DoubleArray) )
      {
         if (verbose_) Log.severe("Failed to get double array for [" + key + "]");
         return new double[0];
      }
      else
      {
         DoubleArray array = (DoubleArray) object;
         return array.array_.clone();
      }
   }
   
   public void setVerbose(boolean verbose)
   {
      verbose_ = verbose;
   }
   
   // --- PACKAGE PROTECTED
   
   Reader(Struct struct)
   {
      currStruct_ = struct;
   }
   
   // --- PRIVATE
   
   private Struct currStruct_;
   private boolean verbose_;

}
