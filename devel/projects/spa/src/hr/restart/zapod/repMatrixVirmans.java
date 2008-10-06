/****license*****************************************************************
**   file: repMatrixVirmans.java
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
package hr.restart.zapod;

import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.reports.mxRM;
import hr.restart.util.reports.mxReport;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.StringTokenizer;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

public abstract class repMatrixVirmans extends mxReport {
  String[] PK = frmVirmani.getInstance().PKs;
  public QueryDataSet virmanDS = new QueryDataSet();
  protected QueryDataSet qds = new QueryDataSet();
  Valid vl= Valid.getValid();
  LinkedList svrha = new LinkedList();

  public repMatrixVirmans() {
  }

  protected void init() {
    QueryDataSet printerRM = hr.restart.baza.dM.getDataModule().getMxPrinterRM();
    printerRM.open();
    mxRM matIsp = new mxRM();
    try {
      String rMjest = hr.restart.sisfun.frmParam.getParam("sisfun","printerRMcmnd","1","Radno mjesto",true);
      if (lookupData.getlookupData().raLocate(printerRM,"CRM",rMjest)){
        System.out.println("radno mjesto "+ rMjest +" pronadjeno :)");
      } else {
        printerRM.first();
      }
      matIsp.init(printerRM);

      System.out.println("print command " + matIsp.getPrintCommand());

    } catch (Exception ex) {
      ex.printStackTrace();
    }
    setRM(matIsp);
    virmanDS.addColumn(new Column("ROW1","Row1",Variant.STRING));
    virmanDS.addColumn(new Column("ROW2","Row2",Variant.STRING));
    virmanDS.addColumn(new Column("ROW3","Row3",Variant.STRING));
    virmanDS.addColumn(new Column("ROW4","Row4",Variant.STRING));
    virmanDS.addColumn(new Column("ROW5","Row5",Variant.STRING));
    virmanDS.addColumn(new Column("ROW6","Row6",Variant.STRING));
    virmanDS.addColumn(new Column("ROW7","Row7",Variant.STRING));
    virmanDS.addColumn(new Column("ROW8","Row8",Variant.STRING));
    virmanDS.addColumn(new Column("ROW9","Row9",Variant.STRING));
    virmanDS.addColumn(new Column("ROW10","Row10",Variant.STRING));
    virmanDS.addColumn(new Column("ROW11","Row11",Variant.STRING));
    virmanDS.addColumn(new Column("DUMMY","Dummy",Variant.STRING));

    setDataSet(virmanDS);
    setDetail(getDetail());
    fill();
  }

  public abstract String[] getDetail();

  protected String getSOPiOP(String sop, String op){
    VarStr temp;
    if (!sop.equals("")) temp = new VarStr(sop);
    else temp = new VarStr(".");
    temp.leftJustify(6);
    return temp.toString()+"<$CondensedON$>"+op;
  }

  public abstract void fill();

  protected String handleHHPUI(String str){

    VarStr temp = new VarStr("                                 ");
    try {
      char h1 = str.substring(0,1).equals("D")?'X':' ';
      char h2 = str.substring(1,2).equals("D")?'X':' ';
      char p = str.substring(2,3).equals("D")?'X':' ';
      char u = str.substring(3,4).equals("D")?'X':' ';
      char i = str.substring(4,5).equals("D")?'X':' ';

      temp.setCharAt(0,h1);
      temp.setCharAt(2,h2);
      temp.setCharAt(9,p);
      temp.setCharAt(27,u);
      temp.setCharAt(32,i);
    }
    catch (Exception ex) {
      System.err.println("Nema nacina...");
      ex.printStackTrace();
      temp.setCharAt(9,'X');
    }
    return temp.toString();
  }

  protected void handleSvrha(String str) {
    try {
      String temp = "";
      if(str.indexOf(" ")<0) {
        svrha.add(str);
        return;
      }

      StringTokenizer st = new StringTokenizer(str, " ");

      while(st.hasMoreTokens()) {
        String nastavak = st.nextToken();
        temp += nastavak + " ";
        if(temp.substring(0, temp.length()-1).length()>20) {
          temp = temp.substring(0, temp.length()-nastavak.length()-2);
          svrha.add(temp);
          if(temp.length()<str.length()) {
            String newStr = str.substring(temp.length()+1, str.length());
            handleSvrha(newStr);
            return;
          }
        }
      }
      svrha.add(temp);
    } catch (RuntimeException e) {
      svrha.add(str);
      System.out.println("HandleSvrha Exception: str = "+str);
      e.printStackTrace();
    }
  }

  protected String handlePNB(String s1, String s2, String s3) {
    String returned = "<$CondensedON$>"+vl.maskStringTrailing(s1,' ',30)+"<$CondensedOFF$>"+
                      vl.maskStringTrailing(" "+s2,' ' ,40)+
                      "<$CondensedON$>"+vl.maskStringTrailing(s3+" "+s2,' ',25);
    return returned;
  }

  protected String handleIznos(BigDecimal _iznos){
    String iznos = formatIznos(_iznos,23);
    return iznos+" "+iznos;
  }

  protected String handleSifra(String s1, String s2, String s3) {
    String finalStr = "";
    String sif2 = vl.maskString(s2.trim(),' ',4);
    String sif3 = vl.maskString(s3.trim(),' ',4);
    finalStr = s1+sif2+sif3;
    return finalStr;
  }

  protected String handleGlobal(String s1, String s2, String s3, String s4) {
    String returned = "<$CondensedON$>"+vl.maskStringTrailing(s1,' ',30)+"<$CondensedOFF$>"+
                      vl.maskStringTrailing(s2,' ',7)+
                      vl.maskStringTrailing(" "+s3,' ',33)+"<$CondensedON$>"+
                      vl.maskStringTrailing(s4,' ',48);

    return returned;
  }

  protected String getDatum() {
    String dat = datumParser(qds.getTimestamp("DATUMIZV").toString());
    if (hr.restart.sisfun.frmParam.getParam("zapod", "DatumValute", "D", "Ispis datuma valute na virmanu",true).equalsIgnoreCase("N")) return "";
    return dat;
  }

  protected String getDatumPod() {
    String dat = datumParser(qds.getTimestamp("DATUMPR").toString());
    if (hr.restart.sisfun.frmParam.getParam("zapod", "DatumPodnosenja", "D", "Ispis datuma podnosenja na virmanu",true).equalsIgnoreCase("N")) return "";
    return dat;
  }

  protected String datumParser(String datum) {
    return " "+datum.substring(8,10)+"."+datum.substring(5,7)+"."+datum.substring(0,4)+".";
  }

  protected static String formatIznos(BigDecimal izn, int len) {
    Valid vl = Valid.getValid();
    if (izn.signum() == 0) return vl.maskString("",' ', len);
    String finalStr ="";
    int idx=0;
    String temp="";
    String iznos = izn.toString();
    String iznosInt=iznos.substring(0,iznos.indexOf("."));
    String iznosDec=iznos.substring(iznos.indexOf(".")+1, iznos.length());
    for(int i= 0; i<iznosInt.length();i++) {
      temp = iznosInt.substring((iznosInt.length()-(i+1)),(iznosInt.length()-(i)))+temp;
      idx++;
      System.out.println("idx = " + idx + " iznos.length " + iznosInt.length() + " i = " + i + " iznosInt.length()-1 " + (iznosInt.length()-1));
      if(idx==3 && iznosInt.length()>3 && (iznosInt.length()-1) > i)  {
        temp="."+temp;
        idx=0;
      }
    }
    finalStr = "***"+temp+","+iznosDec;
    return vl.maskString(finalStr,' ', len);
  }

  protected void checkLL() {
    if(svrha.size()<3) {
      for(int i = svrha.size();i<3;i++)
        svrha.add("");
    }
  }

  protected String replaceReturn(String initStr) {
    initStr = initStr.trim();
    int i = initStr.indexOf("\n");
    if(i>0 && i!= (initStr.length()-1))
      return replaceReturn(initStr.substring(0, i)+" "+ initStr.substring(i+1, initStr.length()));
    else if(i>0)
      return replaceReturn(initStr.substring(0, i)+" "+ initStr.substring(i+1, initStr.length()));
    return initStr;
  }

  protected String justName(String initStr){
    try {
      return initStr.substring(0,initStr.indexOf("\n"));
    }
    catch (Exception ex) {
      return initStr;
    }
  }

  protected void parseStr(String str, int duzina) {
    int pointer = 0;
    while(pointer < str.length()) {
      try {
        svrha.add(str.substring(pointer, pointer+33));
        pointer+=33;
      }
      catch (Exception ex) {
        svrha.add(str.substring(pointer, str.length()));
        pointer = str.length();
      }
    }
  }
}

/*

    ZIM PRIMJER

    'X'                                         :newline 1 column 33: \
    $mask(fodrvir.lova,'**,***,***,**9.99')     :newline 2 column 40: \
    $mask(fodrvir.lova,'**,***,***,**9.99')     :column 64: \%talon
    fodrvir.nazivp                              :newline 2 column 4 width 17: \
    fodrvir.poziv1                              :column 22 width 3: \%model
    fodrvir.broj1                               :column 29: \
    fodrvir.nazivp                              :column 61 width 20: \%talon
    fodrvir.adresap                             :newline 1 column 4 width 17: \
    $toalpha($substring(adresap,18,18),17)      :newline 1 column 4 width 17: \
    fodrvir.poziv2                              :column 22: \
    $toalpha($concat($trim(fodrvir.poziv1),' ',$trim(fodrvir.poziv2)),20) :column 61 width 20: \%talon
    \
    fodrvir.naziv                               :newline 2 column 4 width 17: \
    fodrvir.poziv3                              :column 22 width 3: \%model
    fodrvir.broj2                               :column 29: \
    fodrvir.broj2                               :column 61 width 20: \%talon
    fodrvir.adresa                              :newline 1 column 4 width 17: \
    $toalpha($substring(adresa,18,18),17)       :newline 1 column 4 width 17: \
    fodrvir.poziv4                              :column 22: \
    $toalpha($concat($trim(fodrvir.poziv3),' ',$trim(fodrvir.poziv4)),20) :column 61 width 20: \%talon
    \
    $toalpha($concat($trim(fodrvir.svrha1),' ',$trim(fodrvir.svrha2)),40) :newline 2 column 17 width 40: \
    $mask(fodrvir.datum,'DD-MM-YYYY') :newline 2 column 4:
*/
