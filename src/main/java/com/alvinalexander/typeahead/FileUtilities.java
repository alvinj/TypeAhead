package com.alvinalexander.typeahead;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.io.*;

public class FileUtilities
{
  public static List getFileAsListOfStrings(String filename)
  throws IOException
  {
    if (filename==null) return null;
    List listOfStrings = new LinkedList();

    BufferedReader br = null;
    try
    {
      br = new BufferedReader(new FileReader(filename));
      String s = null;
      while ( (s = br.readLine()) != null)
      {
        listOfStrings.add(s);
      }
    }
    catch (FileNotFoundException fnfe)
    {
      System.err.println("FileNotFoundException occurred in FileUtilities.getFileAsListOfStrings");
      System.err.println(fnfe.getMessage());
      throw fnfe;
    }
    catch (IOException ioe)
    {
      System.err.println("FileNotFoundException occurred in FileUtilities.getFileAsListOfStrings");
      System.err.println(ioe.getMessage());
      throw ioe;
    }
    finally
    {
      if (br!=null)
      {
        try
        {
          br.close();
        }
        catch (IOException e)
        {
          // do nothing
        }
      }
    }
    return listOfStrings;
  }

  public static String getFileAsString(String filename)
  throws IOException
  {
    if (filename==null) return null;

    BufferedReader br = null;
    StringBuffer sb = new StringBuffer();
    String fileAsString = null;
    try
    {
      br = new BufferedReader(new FileReader(filename));
      String s = null;
      while ( (s = br.readLine()) != null)
      {
        sb.append(s);
      }
      fileAsString = sb.toString();
    }
    catch (FileNotFoundException fnfe)
    {
      System.err.println("FileNotFoundException occurred in FileUtilities.getFileAsListOfStrings");
      System.err.println(fnfe.getMessage());
      throw fnfe;
    }
    catch (IOException ioe)
    {
      System.err.println("FileNotFoundException occurred in FileUtilities.getFileAsListOfStrings");
      System.err.println(ioe.getMessage());
      throw ioe;
    }
    finally
    {
      if (br!=null)
      {
        try
        {
          br.close();
        }
        catch (IOException e)
        {
          // do nothing
        }
      }
    }
    return fileAsString;
  }
  
  public static void overwriteFile(String filename, String text)
  throws IOException
  {
    if (filename==null) return;
    if (text==null) return;
    try
    {
      File file = new File(filename);
      FileWriter out = new FileWriter(file);
      out.write(text);
      out.close();
    }
    catch (IOException ioe) 
    {
      System.err.println("IOException occurred trying to WRITE to a file.");
      System.err.println(ioe.getMessage());
      throw ioe;
    }
  }
  
  public static void appendToFile (String filename, String text) 
  throws IOException
  {
    BufferedWriter bw = null;
    try 
    {
       bw = new BufferedWriter(new FileWriter(filename, true));
       bw.write(text);
       bw.newLine();
       bw.flush();
    } 
    catch (IOException ioe) 
    {
      System.err.println("IOException occurred trying to APPEND to a file.");
      System.err.println(ioe.getMessage());
      throw ioe;
    } 
    finally 
    {
      if (bw != null)
      {
        try 
        {
          bw.close();
        }
        catch (IOException ioe2) 
        {
          //ignore it
        }
      }
    } 
  }

  public static void main(String[] args)
  {
    try
    {
      List strings = getFileAsListOfStrings("C:\\1_Al\\CVSProjects\\DevDaily\\ddbookmarks\\data\\categories.db");
      Iterator it = strings.iterator();
      while (it.hasNext())
      {
        String s = (String)it.next();
        System.out.println(s);
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
