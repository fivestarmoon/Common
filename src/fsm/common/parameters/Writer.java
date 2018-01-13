package fsm.common.parameters;

import fsm.common.parameters.InternalDef.*;

public class Writer
{
   public Writer struct(String key)
   {
      Object object = currStruct_.struct.get(key);
      if ( object == null || !(object instanceof Struct) )
      {
         Struct newStruct = new Struct();
         currStruct_.struct.put(key, newStruct);
         return new Writer(newStruct);
      }
      else
      {
         return new Writer((Struct) object);
      }  
   }
   
   public void setValue(String key, String value) 
   {
      if ( value == null )
      {
         return;
      }
      currStruct_.struct.put(key, new String(value));
   }
   
   public void setValue(String key, boolean value) 
   {
      currStruct_.struct.put(key, new Boolean(value));
   }
   
   public void setValue(String key, long value) 
   {
      currStruct_.struct.put(key, new Long(value));
   }
   
   public void setValue(String key, double value) 
   {
      currStruct_.struct.put(key, new Double(value));
   }
   
   public void setArrayOfValues(String key, String[] value) 
   {
      if ( value == null )
      {
         return;
      }
      currStruct_.struct.put(key, new StringArray(value));
   }
   
   public void setArrayOfValues(String key, boolean[] value) 
   {
      if ( value == null )
      {
         return;
      }
      currStruct_.struct.put(key, new BooleanArray(value));
   }
   
   public void setArrayOfValues(String key, long[] value) 
   {
      if ( value == null )
      {
         return;
      }
      currStruct_.struct.put(key, new LongArray(value));
   }
   
   public void setArrayOfValues(String key, double[] value) 
   {
      if ( value == null )
      {
         return;
      }
      currStruct_.struct.put(key, new DoubleArray(value));
   }
   
   public Writer[] allocateArrayOfStructs(int length)
   {
      Writer[] writers = new Writer[length];
      for ( int ii=0; ii<writers.length; ii++ )
      {
         writers[ii] = new Writer();
      }
      return writers;
   }
   
   public void setArrayOfStructs(String key, Writer[] value) 
   {
      if ( value == null )
      {
         return;
      }
      currStruct_.struct.put(key, new StructArray(value));
   }
   
   // --- PACKAGE PROTECTED
   
   Writer(Struct struct)
   {
      currStruct_ = struct;
   }
   
   Writer()
   {
      currStruct_ = new Struct();
   }
   
   Struct getStruct()
   {
      return currStruct_;
   }
   
   // --- PRIVATE

   private Struct currStruct_;

}
