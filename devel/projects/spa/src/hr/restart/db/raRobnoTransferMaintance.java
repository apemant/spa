/****license*****************************************************************
**   file: raRobnoTransferMaintance.java
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
package hr.restart.db;

import hr.restart.baza.dM;
import hr.restart.util.FileHandler;
import hr.restart.util.Valid;
import hr.restart.util.raCompress;
import hr.restart.util.raTransaction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.borland.dx.sql.dataset.QueryDataSet;

public class raRobnoTransferMaintance  {

  private QueryDataSet repldef ;
  private raCompress rC = raCompress.getraCompress();
  private ftpTransfer fT = ftpTransfer.getftpTransfer(false);

  private String ID_trans = hr.restart.sisfun.frmParam.getParam("robno","ID_trans","","ID onoga koji transferira");
  private String addr = hr.restart.sisfun.frmParam.getParam("robno","serverAddr","161.53.200.99","Adresa servera za transfer podataka");
  private int port = Integer.valueOf(hr.restart.sisfun.frmParam.getParam("robno","serverPort","21","Port ftp-servera za prijenos podataka")).intValue();
  private String username = hr.restart.sisfun.frmParam.getParam("robno","serverUser","restart","Korisnicko ime za spajanje na ftp server kod prijenosa podataka");
  private String passwd = hr.restart.sisfun.frmParam.getParam("robno","serverPasswd","restart","Lozinka za spajanje na ftp server kod prijenosa podataka");
  private String folder = hr.restart.sisfun.frmParam.getParam("robno","server","/sda1/fileserver/restart/trans","Folder na serveru gdje krcamo transfere");
  private String lastfile = hr.restart.sisfun.frmParam.getParam("robno","lastfile","","Zadnji naziv filea za prijenos");
  private String isUrlFtp = hr.restart.sisfun.frmParam.getParam("robno","UrlFtp","N","Koristenje ftpTransfer-a (opcija N) brze i nepouzdanije ili preko URL-a");

  private int lastversion =  Integer.valueOf(hr.restart.sisfun.frmParam.getParam
                         ("robno","lastversion","1","Zadnja verzija filea za prijenos")).intValue();
  private String localfolder = hr.restart.sisfun.frmParam.getParam
                                ("robno","replDirLocal","c:/restart/trans",
                              "Folder na klientu za prihvat potvrde transfera");

  private dM dm = dM.getDataModule();
  private String fileToSend;
  private java.sql.Timestamp datumdo ;


  public void transfereData(String batch_index){
  
  }

  public String getIDFileName(){

    dm.getParametri().open();
    dm.getParametri().refresh();
    lastfile = hr.restart.sisfun.frmParam.getParam("robno","lastfile","","Zadnji naziv filea za prijenos");
    lastversion =  Integer.valueOf(hr.restart.sisfun.frmParam.getParam
                           ("robno","lastversion","1","Zadnja verzija filea za prijenos")).intValue();

//    Calendar cal = Calendar.getInstance();
//    cal.setTime(Valid.getValid().getToday());
//    String datapart = ""+cal.get(cal.YEAR)+cal.get(cal.MONTH)+cal.get(cal.DATE);
    String datapart = new java.text.SimpleDateFormat("yyyyMMdd").format(Valid.getValid().getToday());
    if (lastfile.equalsIgnoreCase(datapart)) {
      datapart=datapart+"_"+lastversion;
      lastversion++;
      try {
        raTransaction.runSQL("UPDATE parametri set vrijednost='"+lastversion+"' " +
         " where param='lastversion' and app='robno'");
      }
      catch (Exception ex) {
      }
    } else {
      try {
        raTransaction.runSQL("UPDATE parametri set vrijednost='"+datapart+"' " +
               " where param='lastfile' and app='robno'");
        raTransaction.runSQL("UPDATE parametri set vrijednost='1' " +
               " where param='lastversion' and app='robno'");
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }

    }
    File dir = new File(localfolder);
    return dir.getAbsoluteFile()+File.separator+ID_trans+datapart+".zip";
  }

  public boolean prepareData(String batch_index){
    repldef = hr.restart.util.Util.getNewQueryDataSet(
        "select * from repldef where batch_index='"+batch_index+"'",true);

    ArrayList al = new ArrayList();

    for (repldef.first();repldef.inBounds();repldef.next()){
      try {
      	Class myclass = Class.forName(repldef.getString("classname"));
        Object mo = myclass.newInstance();
        myclass.getMethod("setDatumdo",new Class[]{java.sql.Timestamp.class}).invoke(mo,new Object[] {getDatumdo()});
        myclass.getMethod("setTable_name",new Class[]{String.class}).invoke(mo,new Object[] {repldef.getString("imetab")});
        myclass.getMethod("prepareData",null).invoke(mo,null);
        myclass.getMethod("dataToFile",null).invoke(mo,null);
        String files = (String )myclass.getMethod("getFileName",null).invoke(mo,null);
        if (!(files == null || files .equalsIgnoreCase(""))){
          al.add(files);
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
        slijed(0);
        return false;
      }
    }
    if (al.size()!=0) {
      String[] array = new String[al.size()*2];
      String dir = (new File(localfolder)).getAbsoluteFile()+File.separator;
      for (int i=0;i< al.size();i++){
        array[2*i] = dir+(String) al.get(i)+".dat";
        array[2*i+1] = dir+(String) al.get(i)+".def";
      }
      fileToSend=getIDFileName();
      rC.add(fileToSend,array);
      deleteAlls(array);
    } else {
      slijed(0);
      return false;
    }
    slijed(1);
    return true;
  }
  private void slijed(int broj){

    String upit2 ="";
    if (broj==0) {
      upit2 = "update replinfo set rep_flag = 0 where rbr_url=1";
    } else if (broj==1) {
      upit2 = "update replinfo set rep_flag='P',rbr_url = 0 where rbr_url=1";
    } else {
      System.out.println("Krivi poziv slijed("+broj+")");
      return;
    }

    try {
      raTransaction.runSQL(upit2);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void deleteAlls(String[] array){
    for (int i=0;i< array.length;i++){
      new File(array[i]).delete();
    }
  }

  public boolean fileToDestination() {

    if (addr == null || port == -97 ||username== null ||passwd==null||folder==null) {
        throw new java.lang.IllegalArgumentException("Neispravni parametri addr == null || port == -97 ||username== null ||passwd==null||folder==null");
    }

    File ffileToSend = new File(fileToSend);

    System.out.println("ffileToSend.canRead() "+ffileToSend.canRead());
    System.out.println("ffileToSend.isFile() "+ffileToSend.isFile());
    System.out.println("ffileToSend.getAbsolutePath() "+ffileToSend.getAbsolutePath());
    System.out.println("ffileToSend.length() "+ffileToSend.length());
    System.out.println("adrr "+ addr);
    System.out.println("port "+ port);
    System.out.println("username "+username);
    System.out.println("passwd "+passwd);
    System.out.println("folder "+folder);

//    fT.stor(addr,port,username,passwd,folder,new File(fileToSend));

    if ("D".equalsIgnoreCase(isUrlFtp)) {
      System.out.println("URL FTP is activated");
      return urlftpGet(ffileToSend);
    } else {
      fT.stor(addr,port,username,passwd,folder,ffileToSend);
    }
    return true;
  }

  /**
   * Vraæa obraðene fileove
   * @return Array svih fileova koje treba pokupiti
   */

  public String[] getFileNamesForReturn(){
    QueryDataSet qds = hr.restart.util.Util.getNewQueryDataSet("select * from transhistory where "+
        "status='N'",true);
    if (qds == null ||qds.getRowCount()==0) return null;
    String[] FileNames = new String[qds.getRowCount()];
    int i = 0;
    qds.first();
    for (;;){
      FileNames[i] = qds.getString("FileNameServer");
      if (!qds.next()) break;
      i++;
    }
    return FileNames;
  }

  /**
   *
   * @param filename Ime filea za prijenos
   * @return true ako je file uspješno prenesen
   */

  public boolean getFileFromServer(String filename){
    return fT.get(addr,port,username,passwd,folder,new File(filename));
  }
  public boolean getFileFromServer(String filename,String lfolder) {
    if (fT.get(addr,port,username,passwd,folder,new File(filename),lfolder)) {
//      try {
//        raTransaction.runSQL("update TransHistory set status = 'D',datpotvrde='"+Valid.getValid().getToday()+
//                             "' where filenameserver='"+filename+"'");
//      }
//      catch (Exception ex) {
//        return false;
//      }
    } else {
      return false;
    }
    return true;
  }


  public void setArraListFromFile(ArrayList al,String file) {
    FileHandler fh = new FileHandler(file);
    fh.openWrite();

    for (int i=0;i<al.size();i++) {
      fh.writeln((String) al.get(i));
    }
    fh.close();
  }
  public ArrayList getArraListFromFile(String file) {

    ArrayList hm = new ArrayList();
    FileHandler fh = new FileHandler(file);
    String line="";
    fh.openRead();
    try {
      BufferedReader bis = new BufferedReader(new InputStreamReader(fh.fileInputStream));
      if (bis.ready()) {
        while ((line=bis.readLine())!=null) {
          hm.add(line);
        }
      }
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
    fh.close();
    if (hm.isEmpty()) return null;
    return hm;
  }
  public java.sql.Timestamp getDatumdo() {
    return datumdo;
  }
  public void setDatumdo(java.sql.Timestamp datumdo) {
    this.datumdo = datumdo;
  }
  public String getFileToSend() {
    return fileToSend;
  }
  public HashMap getSeparateKeyAndTablename(String allinone){
    HashMap hm = new HashMap();
    StringTokenizer st = new StringTokenizer(allinone,"$");

    if (st.countTokens()!=2) throw new RuntimeException("Ne valja dati kljuc "+allinone);
    hm.put("table",st.nextToken());
    hm.put("key",st.nextToken());
    return hm;
  }

  public void azuriranjeReplInfoPovratnimFileom(String filename,String flag){

    if (new File(filename).length()==0L) {
      System.out.println(filename+ " length = 0");
    return;}
    ArrayList al = getArraListFromFile(filename);
    HashMap hm = new HashMap();
    String work = "";
    for (int i = 0;i<al.size();i++){
      hm = getSeparateKeyAndTablename((String) al.get(i));
      try {
        work = "update replinfo set rep_flag='"+flag+"' where imetab='"+((String)hm.get("table")).toLowerCase()+
               "' and keytab='"+(String)hm.get("key")+"'";
        raTransaction.runSQL(work);
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  /**
   *  Ekstremna metoda jer sun clase nisu baš najbolje za rad !!!
   * @param file
   * @return
   */

  public boolean urlftpGet(File file){

    boolean result = false;
    com.oroinc.net.ftp.FTPClient ftpc = new com.oroinc.net.ftp.FTPClient();

    try {
      ftpc.connect(addr,port);
      System.out.println(ftpc.getReplyString());
      ftpc.login(username,passwd);
      System.out.println(ftpc.getReplyString());
      ftpc.cwd(folder);
      System.out.println(ftpc.getReplyString());
      result = ftpc.storeFile(file.getName(),new FileInputStream(file));
      System.out.println(ftpc.getReplyString());
      ftpc.disconnect();
//      System.out.println(ftpc.getReplyString());
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
    return result;
  }
}