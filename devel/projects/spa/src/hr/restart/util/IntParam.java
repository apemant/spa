/****license*****************************************************************
**   file: IntParam.java
**   Copyright 2006 Rest Art
**
**   Licensed under the Apache License, Version 2.0 (the "License");
**   you may not use this file except in compliance with the License.
**   You may obtain a copy of the License at
**
**       http://www.apache.org/licenses/LICENSE-2.0
**
**   Unless required by applicable law or agreed to in writing, software
**   distributed under the License is distributed on an "AS IS" BASIS,
**   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
**   See the License for the specific language governing permissions and
**   limitations under the License.
**
****************************************************************************/
package hr.restart.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Properties;

/**
 * Title:        Robno knjigovodstvo
 * Description:  Projekt je zamišljen kao multiuser-ska verzija robnog knjigovodstva
 * Copyright:    Copyright (c) 2001
 * Company:      Rest Art d.o.o.
 * @author Tomislav Vidakovi\u0107
 * @version 1.0
 * na ovo sam vrlo vrlo ponosan ovo je prvi JAVA program koji nije banalni kao ostali
 *
 * AI: robno.ini -> restart.properties
 */

public class IntParam {

  static public String  URL ;
  static public String  PASSWORD ;
  static public String TIP ;
  static public String USER ;
  static private String IME_PARAMETARSKOG_FAJLA = "robno.ini" ;
  public static String PROPSFILENAME = "restart.properties";

  public IntParam() {
      PostojiFile();
      SetajParametre();
  }

  public static void SetajParametre() {
       URL = VratiSadrzajTaga("url");
       PASSWORD = VratiSadrzajTaga("pass");
       TIP = VratiSadrzajTaga("tip");
       USER = VratiSadrzajTaga("user");
  }
  
  public static void setBaseParams(String url, String pass, String tip, String user, String dbdialect) {
    URL = url;
    PASSWORD = pass;
    TIP = tip;
    USER = user;
    Properties raProps = FileHandler.getProperties(PROPSFILENAME,false);
    raProps.setProperty("url", url);
    raProps.setProperty("pass", pass);
    raProps.setProperty("tip", tip);
    raProps.setProperty("user", user);
    raProps.setProperty("dbdialect", dbdialect);
    FileHandler.storeProperties(PROPSFILENAME,raProps,false);
  }

  public static void setImeFajle(String ime_fajla) {
    IME_PARAMETARSKOG_FAJLA=ime_fajla;
  }

  public static String getImeFajle() {
    return IME_PARAMETARSKOG_FAJLA;
  }

  public static String getPropsFileName() {
    return PROPSFILENAME;
  }

  private static void initializeProps(boolean paramapExists) {
    java.util.Properties raProps = new java.util.Properties();
    raProps.setProperty("url1","jdbc:interbase://161.53.200.99/home/interbase/bazara.gdb");
    raProps.setProperty("url2","jdbc:postgresql://161.53.200.99/bazara");
    raProps.setProperty("url3","jdbc:odbc:Robno");
    raProps.setProperty("tip1","interbase.interclient.Driver");
    raProps.setProperty("tip2","org.postgresql.Driver");
    raProps.setProperty("tip3","sun.jdbc.odbc.JdbcOdbcDriver");
    if (paramapExists) {
      raProps.setProperty("tip",VratiSadrzajTaga_old("tip"));
      raProps.setProperty("url",VratiSadrzajTaga_old("url"));
      raProps.setProperty("user",VratiSadrzajTaga_old("user"));
      raProps.setProperty("pass",VratiSadrzajTaga_old("pass"));
      raProps.setProperty("framemode",VratiSadrzajTaga_old("framemode"));
      raProps.setProperty("lookandfeel",VratiSadrzajTaga_old("lookandfeel"));
      raProps.setProperty("backimage",VratiSadrzajTaga_old("backimage"));
javax.swing.JOptionPane.showMessageDialog(null,"Zbog nadogradnje aplikacije startaj ponovo!!",
"Obavijest",javax.swing.JOptionPane.INFORMATION_MESSAGE,raImages.getImageIcon(raImages.DEFAULTERRICON));
      FileHandler.storeProperties(PROPSFILENAME,raProps,false);
      System.exit(0);
    }
    FileHandler.storeProperties(PROPSFILENAME,raProps,false);
  }

  private static boolean PostojiFile() {
    File propsfile = new File(PROPSFILENAME);
    if (propsfile.length()==0) {
      File paramap= new File(IME_PARAMETARSKOG_FAJLA);
      initializeProps(paramap.length()>0);
    }
/*
    if ( ! paramap.exists()) {
      try {
        paramap.createNewFile();
        } catch (IOException ioex) {
          return false ;
          }
        UpisiUIni("jdbc:interbase://161.53.200.99/home/interbase/bazara.gdb","url1");
        UpisiUIni("jdbc:odbc:Robno","url2");
        UpisiUIni("interbase.interclient.Driver","tip1");
        UpisiUIni("sun.jdbc.odbc.JdbcOdbcDriver","tip2");
    }
*/
    return true;
  }

  private static boolean PostojiFile_old() {
    return true;
  }

  public static void setTag(String tag,String value) {
    java.util.Properties raProps = FileHandler.getProperties(PROPSFILENAME,false);
    raProps.setProperty(tag,value);
    FileHandler.storeProperties(PROPSFILENAME,raProps,false);
  }

  public static String getTag(String tag) {
    java.util.Properties raProps = FileHandler.getProperties(PROPSFILENAME,false);
    return raProps.getProperty(tag,"");
  }

  public static String VratiSadrzajTaga(String tag) {
    PostojiFile();
    return getTag(tag);
  }

  public static String VratiSadrzajTaga_old(String tag) {

    try{
      PostojiFile_old();
      RandomAccessFile raf =new RandomAccessFile(IME_PARAMETARSKOG_FAJLA,"r");
      long poc= PocetakTagaSadrzaja(raf, tag);
      long kraj = KrajTagaSadrzaja(raf,tag);
      if (poc== -1)
        return "";
      if (kraj-poc < 0) return "";
      byte[] bSadrzajTaga= new byte[(int) (kraj-poc) ];
      raf.seek(poc);
      raf.read(bSadrzajTaga);

      return new String(bSadrzajTaga);

      } catch (IOException iox){
      return ""; }
  }

  public static boolean UpisiUIni(String zaupis,String tag) {
    try {
      setTag(tag,zaupis);
      return true;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

  public static boolean UpisiUIni_Old(String zaupis,String tag){

    try{

      RandomAccessFile raf =new RandomAccessFile(IME_PARAMETARSKOG_FAJLA,"rw");
      long duzina=raf.length();
      long poc=PocetakTaga(raf,tag);
      long kraj=KrajTaga(raf,tag);

      if (poc!=-1) {
        byte[] prije= new byte[(int) poc ];
        byte[] poslije =new byte[(int)(duzina-kraj)];

        raf.seek(0);
        raf.read(prije);
        raf.seek(kraj);
        raf.read(poslije);
        raf.close();

        FileOutputStream fos = new FileOutputStream(IME_PARAMETARSKOG_FAJLA);
        fos.write(prije);
        fos.write(poslije);
        fos.close();
      }
    } catch (IOException ioex){
        System.out.println("Greška" + ioex);
        return false ;
        }

    try {

      RandomAccessFile raf =new RandomAccessFile(IME_PARAMETARSKOG_FAJLA,"rw");
      long duzina=raf.length();
      duzina=raf.length();
      raf.seek(duzina);
      raf.writeBytes("<"+tag+">"+zaupis+"</"+tag+">");
      raf.close();

  } catch (IOException ioex){
      System.out.println("Greška" + ioex);
      return false ;
      }

  SetajParametre();
  return true ;

  }

  private static long DajTag(RandomAccessFile raf,String tag,long start){

    String temptag ;

    int duzinatag=tag.length();
    byte[] btag = tag.getBytes();

    try {
    for (long li= start; li< raf.length();li++) {

       raf.seek(li);
       raf.read(btag);
       temptag=new String(btag);
       if (tag.equals(temptag))
          return li;
    }
    } catch (IOException ioex) {
       return -1;
    }
    return -1;

 }
   private static long PocetakTaga(RandomAccessFile raf,String tag){

    return DajTag(raf,"<"+tag+">",0);

 }
   private static long PocetakTagaSadrzaja(RandomAccessFile raf,String tag){

    return DajTag(raf,"<"+tag+">",0)+tag.length()+2;

 }
   private static long KrajTaga(RandomAccessFile raf,String tag){

    return DajTag(raf,"</"+tag+">",0)+tag.length()+3;
 }

  private static long KrajTagaSadrzaja(RandomAccessFile raf,String tag){
    return DajTag(raf,"</"+tag+">",0);
  }
}