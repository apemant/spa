package hr.restart;

import hr.restart.util.reports.mxReport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.andiv.print.Main;


public class Matrix {
  
  static {
    fillTables();
  }
  
  private Matrix() {
    // no
  }
  
  private static File getFile() {
    return new File(System.getProperty("user.dir"), mxReport.TMPPRINTFILE);
  }
  
  public static void parse(String comm) {
    if (comm.startsWith("printCro"))
      printCro(comm.substring(9).trim());
    else if (comm.startsWith("printLat"))
      printLat(comm.substring(9).trim());
    else print(comm.substring(6).trim());
  }
  
  public static void printCro(String printer) {
    convert(cro);
    print(printer);
  }
  
  public static void printLat(String printer) {
    convert(lat);
    print(printer);
  }
  
  public static void print(String printer) {
    Main.PRINTER = printer;
    new org.andiv.print.Print(getFile());
  }
  
  private static void convert(byte[] dest) {    
    File f = getFile();
    System.out.println("Converting " + f);
    int len = (int) f.length();
    
    byte[] buf = readFile();
    if (buf == null) {
      System.out.println("No file " + f.getAbsolutePath());
      return;
    }
    System.out.println("... " + buf.length + " bytes");
    
    for (int i = 0; i < buf.length; i++)
      buf[i] = dest[buf[i] & 0xFF];

    System.out.println("Writing back to " + f);
    writeFile(buf);
  }
  
  private static byte[] readFile() {
    File f = getFile();
    int len = (int) f.length();
    
    byte[] buf = new byte[len];
    
    try {
      FileInputStream fis = new FileInputStream(f);
      try {
        int read = fis.read(buf);
        if (read < 1) return null;
        if (read == buf.length) return buf;
        System.out.println("Wrong length! " + read + " != " + buf.length);
        byte[] nb = new byte[read];
        System.arraycopy(buf, 0, nb, 0, read);
        return nb;
      } finally {
        try {
          fis.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  private static void writeFile(byte[] buf) {
    try {
      FileOutputStream fos = new FileOutputStream(getFile());
      try {
        fos.write(buf);
        fos.flush();
      } finally {
        try {
          fos.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static byte[] lat, cro; 
  
  private static void fillTables() {
    lat = new byte[256];
    cro = new byte[256];
    for (int i = 0; i < 256; i++) 
      lat[i] = cro[i] = (byte) i;
    
    lat[154] = (byte) 185;
    lat[158] = (byte) 190;
    lat[138] = (byte) 169;
    lat[142] = (byte) 174;
    
    cro[232] = (byte) 126;
    cro[230] = (byte) 125;
    cro[240] = (byte) 124;
    cro[154] = (byte) 123;
    cro[158] = (byte) 96;
    
    cro[200] = (byte) 94;
    cro[198] = (byte) 93;
    cro[208] = (byte) 92;
    cro[138] = (byte) 91;
    cro[142] = (byte) 64;
  }
}
