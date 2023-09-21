package fsm.common.jsonParameters;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import fsm.common.Log;
import fsm.common.parameters.Parameters;
import fsm.common.parameters.Reader;
import fsm.common.parameters.Writer;

class JsonParametersTest
{
   @Test
   void testReader() throws IOException
   {
      Log.Init(File.createTempFile("JsonParametersTest-", ".log"));
      System.out.println("Starting testReader ...");
      validate(".\\target\\test-classes\\fsm\\common\\jsonParameters\\parameters.txt");
      System.out.println("Done.");
   }
   
   @Test
   void testWriter() throws IOException
   {
      System.out.println("Starting testWriter ...");
      String tempFile = File.createTempFile("JsonParametersTest-", ".txt").getAbsolutePath();
      Parameters params = new Parameters(tempFile, false);
      params.write();
      
      // Ensure the file is empty
      Parameters paramsTemp = new Parameters(tempFile, true);
      assertEquals(0, paramsTemp.getReader().getKeySet().length);
      
      System.out.println("  writing "+ tempFile + " ...");
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
      
      Writer[] arraywriters = params.getWriter().allocateArrayOfStructs(2);      
      arraywriters[0].setValue("long", 3);
      arraywriters[0].setValue("double", 2.1);      
      arraywriters[1].setValue("boolean", true);
      arraywriters[1].setArrayOfValues("stringarray", new String[]{ "a", "b" });
      params.getWriter().setArrayOfStructs("structarray", arraywriters);
      
      params.write();
      System.out.println("  write completed.");
      validate(tempFile);
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
      assertEquals(1, params.getReader().getLongValue("long", 0));

      assertEquals(true, params.getReader().isKeyForValue("double"));
      assertEquals(3.1456789124, params.getReader().getDoubleValue("double", 0.0));

      assertEquals(true, params.getReader().isKeyForValue("boolean"));
      assertEquals(true, params.getReader().getBooleanValue("boolean", false));

      assertEquals(true, params.getReader().isKeyForValue("string"));
      assertEquals("string", params.getReader().getStringValue("string", ""));

      assertEquals(true, params.getReader().isKeyForArrayOfValues("intarray"));
      assertEquals(false, params.getReader().isKeyForArrayOfStructs("intarray"));
      assertEquals(false, params.getReader().isKeyForValue("intarray"));
      assertEquals(false, params.getReader().isKeyForStruct("intarray"));
      long[] intarray = params.getReader().getLongArray("intarray");
      assertEquals(3, intarray.length);
      assertEquals(0, intarray[0]);
      assertEquals(1, intarray[1]);
      assertEquals(2, intarray[2]);

      assertEquals(true, params.getReader().isKeyForArrayOfValues("boolarray"));
      assertEquals(2, params.getReader().getBooleanArray("boolarray").length);
      assertEquals(true, params.getReader().getBooleanArray("boolarray")[0]);
      assertEquals(false, params.getReader().getBooleanArray("boolarray")[1]);

      assertEquals(true, params.getReader().isKeyForStruct("struct"));
      assertEquals(false, params.getReader().isKeyForValue("struct"));
      assertEquals(false, params.getReader().isKeyForArrayOfValues("struct"));
      Reader structReader = params.getReader().struct("struct");
      assertEquals("string2", structReader.getStringValue("string", ""));
      assertEquals(2, structReader.getDoubleArray("doublearray").length);
      assertEquals(0.1, structReader.getDoubleArray("doublearray")[0]);
      assertEquals(0.2, structReader.getDoubleArray("doublearray")[1]);

      assertEquals(true, params.getReader().isKeyForArrayOfStructs("structarray"));
      assertEquals(false, params.getReader().isKeyForArrayOfValues("structarray"));
      assertEquals(2,  params.getReader().structArray("structarray").length);
      
      structReader = params.getReader().structArray("structarray")[0];
      assertEquals(3, structReader.getLongValue("long", 0));
      assertEquals(2.1, structReader.getDoubleValue("double", 0.0));
      
      structReader = params.getReader().structArray("structarray")[1];
      assertEquals(true, structReader.getBooleanValue("boolean", false));
      assertEquals(2, structReader.getStringArray("stringarray").length);
      assertEquals("a", structReader.getStringArray("stringarray")[0]);
      assertEquals("b", structReader.getStringArray("stringarray")[1]);
      System.out.println("  validating completed.");
   }

}
