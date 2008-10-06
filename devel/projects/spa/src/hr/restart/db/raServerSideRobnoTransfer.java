/****license*****************************************************************
**   file: raServerSideRobnoTransfer.java
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
import hr.restart.baza.KreirDrop;
import hr.restart.baza.dM;
import hr.restart.baza.raTransferNotifier;
import hr.restart.util.Valid;
import hr.restart.util.raCompress;
import hr.restart.util.raTransaction;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import com.borland.dx.sql.dataset.QueryDataSet;

public class raServerSideRobnoTransfer implements raServerSideRobnoInterface {

  private raCompress rCP = raCompress.getraCompress();
  private ArrayList raspakiraniFileovi = new ArrayList();
  private ArrayList badTransfer = new ArrayList();
  private ArrayList goodTransfer  = new ArrayList();
  private String[] allzip;
  private File destf = new File(
      hr.restart.sisfun.frmParam.getParam
        ("robno","replDirServer","/opt/restart/trans","Folder na serveru za prihvat transfera"));

  public raServerSideRobnoTransfer() {
    badTransfer = new ArrayList();
    goodTransfer  = new ArrayList();
    allzip= null;
  }

  public void potvrdaTransHistory(String[] aa){
    if (aa==null || aa.length==0) return;
    String work="";
    for (int i = 0;i<aa.length;i++) {
      try {
        work = "update TransHistory set status='D', datprijenos ='"+
                 Valid.getValid().getToday()+"' where filenameclient='"+aa[i]+"' ";
        raTransaction.runSQL(work);
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  public String[] getZipFiles4Batch(){
    QueryDataSet qds;
    ArrayList al = new ArrayList();
    allzip = getZipFiles();
    if (allzip==null || allzip.length==0) return null;
    for (int i = 0;i<allzip.length;i++) {
      qds = hr.restart.util.Util.getNewQueryDataSet(
      "Select * from TransHistory where filenameclient='"+allzip[i]+"'",true);
      if (qds.getRowCount()==0 && !allzip[i].substring(0,4).equalsIgnoreCase("ret_")){
        qds.insertRow(true);
        qds.setString("filenameclient",allzip[i]);
        qds.setTimestamp("datprijenos",Valid.getValid().getToday());
        qds.setString("STATUS","N");
        al.add(allzip[i]);
        qds.saveChanges();
     } else if (qds.getString("STATUS").equalsIgnoreCase("N")) {
       al.add(allzip[i]);
     }

    }

    if (al.size()==0) return null;
    String[] ss = new String[al.size()];
    for(int i = 0;i<ss.length;i++){
      ss[i] = (String) al.get(i);
    }
    return ss;
  }

  public boolean isNewZipFilesExist(){

    String[] ss = getZipFiles4Batch();
    if (ss == null || ss.length==0) return false;
    return true;

  }

  public String[] getZipFiles(){

    if (!destf.exists()) throw new RuntimeException(destf.getAbsolutePath()+" is not exist");
    if (!destf.isDirectory()) throw new RuntimeException(destf.getName()+" is not Directory");

    File[] fdir = destf.listFiles(new FileFilter(){
        public boolean accept(File pathname){
          String name = pathname.getName();
          if (name.length()<4) return false;
          if (name.substring(name.length()-4,name.length()).equalsIgnoreCase(".zip")){
            return true;
          }
          return false;
        }
    });
    if (fdir.length==0) return null ;
    String[] zipFiles = new String[fdir.length];
    for (int i = 0;i<fdir.length;i++){
      zipFiles[i] = fdir[i].getName();
    }
    return zipFiles;
  }

  public void decompressZipFile(String zipFile){

//System.out.println(destf.getAbsolutePath());
    decompressZipFile(zipFile,destf.getAbsolutePath());
  }

  public void decompressZipFile(String[] zipFiles,String dir){
    File dirA = new File(dir);
    String path;

    raspakiraniFileovi.clear();
    for (int i=0;i<zipFiles.length;i++){
      try {
        path = dirA.getAbsolutePath()+File.separator+zipFiles[i].substring(0,zipFiles[i].length()-4);
//System.out.println(path);
//System.out.println(dir+File.separator+zipFiles[i]);
        raspakiraniFileovi = hr.restart.util.Valid.ObjectArray2ArrayList(rCP.unzip(
            dir+File.separator+zipFiles[i],path));
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  public void decompressZipFile(String zipFile,String dir){
    File dirA = new File(dir);
    String path;

    raspakiraniFileovi.clear();
//    for (int i=0;i<zipFiles.length;i++){
      try {
        path = dirA.getAbsolutePath()+File.separator+zipFile.substring(0,zipFile.length()-4);
        raspakiraniFileovi = hr.restart.util.Valid.ObjectArray2ArrayList(rCP.unzip(
            dir+File.separator+zipFile,path));
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
//    }
  }

  String table_before="";
  String[] keys;
  HashMap hm  ;

  public void fromFileToDatabase(){
    if(raspakiraniFileovi==null) return;

    KreirDrop.installNotifier(new raTransferNotifier() {
      public void rowTransfered(String table, int action, int row,Object obj ) {
        try {
          if (action == raTransferNotifier.ROW_INSERT_FAILED) {
//            hm = (HashMap) obj;
//            if (!table_before.equalsIgnoreCase(table)) {
//              keys = hr.restart.db.raConnectionFactory.getKeyColumns(hr.restart.baza.dM.getDataModule().getDatabaseConnection(),table);
//            }
//            badTransfer.add(table+"$"+raControlDocs.getUniversalKey(hm,keys,table));
            table_before = table;
          } else if (action == raTransferNotifier.ROW_INSERTED) {
            hm = (HashMap) obj;
//            if (!table_before.equalsIgnoreCase(table)) {
//              keys = hr.restart.db.raConnectionFactory.getKeyColumns(hr.restart.baza.dM.getDataModule().getDatabaseConnection(),table);
//            }
//            goodTransfer.add(table+"$"+raControlDocs.getUniversalKey(hm,keys,table));
            table_before = table;
          }
        } catch (Exception ex){
          System.out.println("Exception "+table);
          ex.printStackTrace();
        }
      }
    });

    String modul_name="";
    String name="";
    String dir = "";

    for (int i = 0;i<raspakiraniFileovi.size();i++) {
      name = (String)raspakiraniFileovi.get(i);
      if (name.substring(name.length()-4,name.length()).equalsIgnoreCase(".def")) {
        continue;
      }
      dir = name.substring(0,name.lastIndexOf(File.separator));
      modul_name=name.substring(name.lastIndexOf(File.separator)+1,name.length()-4);
      if (!dM.modulesLoaded){
        dM.getDataModule().loadModules();
      }
      System.out.println(modul_name+" begining ...");
      try {
        KreirDrop.getModuleByName(modul_name).insertData(new File(dir),modul_name);
      }
      catch (FileNotFoundException ex) {
        ex.printStackTrace();
      }
      System.out.println(modul_name+" end ...");
    }
    KreirDrop.removeNotifier();
  }

  public void returnTransferKeys(){
/*
    spustiti u datove bad i good array
    zapakirati ih u zip odgovarajuæeg imena
    */
    if (badTransfer.size()>0){
      (new raRobnoTransferMaintance()).setArraListFromFile(badTransfer,destf.getAbsolutePath()+
          File.separator+"badList.dat");
      badTransfer.clear();
    }
    if (goodTransfer.size()>0) {
      (new raRobnoTransferMaintance()).setArraListFromFile(goodTransfer,destf.getAbsolutePath()+
          File.separator+"goodList.dat");
      goodTransfer.clear();
    }
  }

  public void makeReturnZipFile(String zipFile){

    String retFile = "ret_"+zipFile;
    ArrayList al = new ArrayList();
    File bad = new File(destf.getAbsolutePath()+File.separator+"badList.dat");
    File good = new File(destf.getAbsolutePath()+File.separator+"goodList.dat");

    if (bad.length()!=0L){
      al.add(destf.getAbsolutePath()+File.separator+"badList.dat");
    }
    if (good.length()!=0L){
      al.add(destf.getAbsolutePath()+File.separator+"goodList.dat");
    }

    if (al.size()>0){
      rCP.add(destf.getAbsolutePath()+File.separator+retFile,hr.restart.util.Valid.ArrayList2StringArray(al));
    }

    if (bad.length()!=0){
      bad.delete();
    }
    if (good.length()!=0){
      good.delete();
    }
  }

  public ArrayList getRaspakiraniFileovi() {
    return raspakiraniFileovi;
  }


}