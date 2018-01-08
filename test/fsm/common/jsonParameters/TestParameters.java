package fsm.common.jsonParameters;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import fsm.common.Log;

class TestParameters
{
   @Test
   void testReader()
   {
      Log.Init();
      System.out.println("Starting testReader ...");
      validate("parameters.txt");
      System.out.println("Done.");
   }
   
   @Test
   void testWriter()
   {
      System.out.println("Starting testWriter ...");
      Parameters params = new Parameters("temp.txt", false);
      params.write();
      
      // Ensure the file is empty
      Parameters paramsTemp = new Parameters("temp.txt", true);
      assertEquals(0, paramsTemp.getReader().getKeySet().length);
      
      System.out.println("  writing temp.txt ...");
      Writer writer = params.getWriter();
      writer.setValue("long", 1);
      writer.setValue("double", 3.1456789124);
      writer.setValue("boolean", true);
      writer.setValue("string", "string");
      writer.setArrayOfValues("intarray", new long[]{ 0, 1, 2 });
      writer.setArrayOfValues("boolarray", new boolean[]{ true, false });
      
      writer = params.getWriter().struct("struct");
      writer.setValue("string", "string2");
      writer.setArrayOfValues("doublearray", new double[]{ 0.1, 0.2 });
      
      writer = params.getWriter();
      writer.allocateArrayOfStructs("structarray", 2);
      
      writer = params.getWriter().struct(Parameters.GetKeyForArrayIndex("structarray", 0));
      writer.setValue("long", 3);
      writer.setValue("double", 2.1);
      
      writer = params.getWriter().struct(Parameters.GetKeyForArrayIndex("structarray", 1));
      writer.setValue("boolean", true);
      writer.setArrayOfValues("stringarray", new String[]{ "a", "b" });
      
      params.write();
      System.out.println("  write completed.");
      validate("temp.txt");
      System.out.println("Done.");
   }
   
   void validate(String file)
   {
      System.out.println("  reading " + file + "...");
      Parameters params = new Parameters(file);
      System.out.println("  read completed.");

      System.out.println("  validating ...");
      assertEquals(true, params.getReader().isKeyForValue("long"));
      assertEquals(false, params.getReader().isKeyForArrayOfValues("long"));
      assertEquals(false, params.getReader().isKeyForStruct("long"));
      assertEquals(1, params.getReader().getValue("long", 0));

      assertEquals(true, params.getReader().isKeyForValue("double"));
      assertEquals(3.1456789124, params.getReader().getValue("double", 0.0));

      assertEquals(true, params.getReader().isKeyForValue("boolean"));
      assertEquals(true, params.getReader().getValue("boolean", false));

      assertEquals(true, params.getReader().isKeyForValue("string"));
      assertEquals("string", params.getReader().getValue("string", ""));

      assertEquals(true, params.getReader().isKeyForArrayOfValues("intarray"));
      assertEquals(false, params.getReader().isKeyForArrayOfStructs("intarray"));
      assertEquals(false, params.getReader().isKeyForValue("intarray"));
      assertEquals(false, params.getReader().isKeyForStruct("intarray"));
      assertEquals(3, params.getReader().getArrayLength("intarray"));
      assertEquals(0, params.getReader().getValue(Parameters.GetKeyForArrayIndex("intarray", 0), -1));
      assertEquals(1, params.getReader().getValue(Parameters.GetKeyForArrayIndex("intarray", 1), -1));
      assertEquals(2, params.getReader().getValue(Parameters.GetKeyForArrayIndex("intarray", 2), -1));

      assertEquals(true, params.getReader().isKeyForArrayOfValues("boolarray"));
      assertEquals(2, params.getReader().getArrayLength("boolarray"));
      assertEquals(true, params.getReader().getValue(Parameters.GetKeyForArrayIndex("boolarray", 0), false));
      assertEquals(false, params.getReader().getValue(Parameters.GetKeyForArrayIndex("boolarray", 1), true));

      assertEquals(true, params.getReader().isKeyForArrayOfValues("boolarray"));
      assertEquals(2, params.getReader().getArrayLength("boolarray"));
      assertEquals(true, params.getReader().getValue(Parameters.GetKeyForArrayIndex("boolarray", 0), false));
      assertEquals(false, params.getReader().getValue(Parameters.GetKeyForArrayIndex("boolarray", 1), true));

      assertEquals(true, params.getReader().isKeyForStruct("struct"));
      assertEquals(false, params.getReader().isKeyForValue("struct"));
      assertEquals(false, params.getReader().isKeyForArrayOfValues("struct"));
      Reader structReader = params.getReader().struct("struct");
      assertEquals("string2", structReader.getValue("string", ""));
      assertEquals(2, structReader.getArrayLength("doublearray"));
      assertEquals(0.1, structReader.getValue(Parameters.GetKeyForArrayIndex("doublearray", 0), -1.0));
      assertEquals(0.2, structReader.getValue(Parameters.GetKeyForArrayIndex("doublearray", 1), -1.0));

      assertEquals(true, params.getReader().isKeyForArrayOfStructs("structarray"));
      assertEquals(false, params.getReader().isKeyForArrayOfValues("structarray"));
      assertEquals(2,  params.getReader().getArrayLength("structarray"));
      
      structReader = params.getReader().struct(Parameters.GetKeyForArrayIndex("structarray", 0));
      assertEquals(3, structReader.getValue("long", 0));
      assertEquals(2.1, structReader.getValue("double", 0.0));
      
      structReader = params.getReader().struct(Parameters.GetKeyForArrayIndex("structarray", 1));
      assertEquals(true, structReader.getValue("boolean", false));
      assertEquals(2, structReader.getArrayLength("stringarray"));
      assertEquals("a", structReader.getValue(Parameters.GetKeyForArrayIndex("stringarray", 0), ""));
      assertEquals("b", structReader.getValue(Parameters.GetKeyForArrayIndex("stringarray", 1), ""));
      System.out.println("  validating completed.");
   }

}
