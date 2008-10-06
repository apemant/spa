/****license*****************************************************************
**   file: repIspVir2.java
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
import hr.restart.util.reports.mxRM;
import hr.restart.util.reports.mxReport;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.StringTokenizer;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

public class repIspVir2 extends mxReport {
  String[] PK = frmVirmani.getInstance().PKs;
  public QueryDataSet virmanDS = new QueryDataSet();
  private QueryDataSet qds = new QueryDataSet();
  Valid vl= Valid.getValid();
  LinkedList svrha = new LinkedList();
  public repIspVir2() {
    init();
  }

  public void init() {
//    mxRM matIsp = mxRM.getDefaultMxRM();
//    String prntcmd = "cmd /c C:\\util\\hrconv 2 4 < \""+System.getProperty("user.dir")+"\\#\" > \"\\\\Grga\\EPSON\"";
//    matIsp.setPrintCommand(prntcmd);
    QueryDataSet printerRM = hr.restart.baza.dM.getDataModule().getMxPrinterRM();
    printerRM.open();
    printerRM.first();

    mxRM matIsp = new mxRM();
    try {
      matIsp.init(printerRM);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    setRM(matIsp);
//    System.out.println("mxR: " + matIsp.getPrintCommand());
    virmanDS.addColumn(new Column("ROW1","Row1",Variant.STRING));
    virmanDS.addColumn(new Column("ROW2","Row2",Variant.STRING));
    virmanDS.addColumn(new Column("ROW3","Row3",Variant.STRING));
    virmanDS.addColumn(new Column("ROW4","Row4",Variant.STRING));
    virmanDS.addColumn(new Column("ROW5","Row5",Variant.STRING));
    virmanDS.addColumn(new Column("ROW6","Row6",Variant.STRING));
    virmanDS.addColumn(new Column("ROW7","Row7",Variant.STRING));
    virmanDS.addColumn(new Column("ROW7_1","Row7_1",Variant.STRING));
    virmanDS.addColumn(new Column("ROW8","Row8",Variant.STRING));
    virmanDS.addColumn(new Column("ROW9","Row9",Variant.STRING));
    virmanDS.addColumn(new Column("ROW10","Row10",Variant.STRING));
    virmanDS.addColumn(new Column("ROW11","Row11",Variant.STRING));
    virmanDS.addColumn(new Column("ROW12","Row12",Variant.STRING));
    virmanDS.addColumn(new Column("ROW13","Row13",Variant.STRING));
    virmanDS.addColumn(new Column("ROW14","Row14",Variant.STRING));
    virmanDS.addColumn(new Column("DUMMY","Dummy",Variant.STRING));

    setDataSet(virmanDS);
    String[] detail = new String[] {"<$newline$>"+
    "<#DUMMY,1,left#><#ROW1,10,left#>"+
    "<$newline$><$newline$>"+
    "<#DUMMY,1,left#><#DUMMY,18,left#><#ROW2,23,left#>"+
    "<$newline$>"+
    "<#DUMMY,1,left#><#ROW3,63,right#>"+
    "<$newline$>"+
    "<#DUMMY,1,left#><#ROW4,35,left#>"+
    "<$newline$>"+
    "<#DUMMY,1,left#><#ROW5,63,right#>"+
    "<$newline$><$newline$>"+
    "<#DUMMY,1,left#><#DUMMY,10,left#><#ROW6,31,left#>"+
    "<$newline$>"+
//    "<#DUMMY,1,left#><#ROW7,64,left#>"+
    "<#DUMMY,1,left#><#ROW7,54,left#>"+
    "<#ROW7_1,10,right#>"+
    "<$newline$>"+
    "<#DUMMY,1,left#><#ROW8,34,left#>"+
    "<$newline$>"+
    "<#DUMMY,1,left#><#ROW9,63,right#>"+
    "<$newline$><$newline$>"+
    "<#DUMMY,1,left#><#DUMMY,14,left#><#ROW10,27,left#>"+
    "<$newline$>"+
    "<#DUMMY,1,left#><#ROW11,63,right#>"+
    "<$newline$>"+
    "<#DUMMY,1,left#><#ROW12,63,left#>"+
    "<$newline$>"+
    "<#DUMMY,1,left#><#ROW13,63,right#>"+
    "<$newline$><$newline$><$newline$>"+
    "<#DUMMY,1,left#><#ROW14,60,right#>"+
    "<$newline$><$newline$><$newline$><$newline$>"};
    setDetail(detail);
    fill();
  }

  public void fill()
  {
    qds = (QueryDataSet)frmVirmani.getInstance().getRaQueryDataSet();
    qds.open();
    qds.first();
    while(qds.inBounds())
    {
      virmanDS.open();
      virmanDS.insertRow(false);
      virmanDS.setString("ROW1", qds.getString("JEDZAV"));
      svrha.clear();
      handleSvrha(replaceReturn(qds.getString("NATERET").trim()),14);
      checkLL();
      virmanDS.setString("ROW2", svrha.get(0).toString());
      virmanDS.setString("ROW3", handleGlobal(svrha.get(1).toString(),qds.getString("BRRACNT"),qds.getString("NACIZV"),36));
      virmanDS.setString("ROW4", svrha.get(2).toString());
      virmanDS.setString("ROW5", handlePNB(qds.getString("PNBZ1"), qds.getString("PNBZ2")));
      svrha.clear();
      handleSvrha(replaceReturn(qds.getString("SVRHA")),22);
      checkLL();
      virmanDS.setString("ROW6", svrha.get(0).toString());
//      virmanDS.setString("ROW7", svrha.get(1).toString() + handleSifra(qds.getString("SIF1"), qds.getString("SIF2"),qds.getString("SIF3"),svrha.get(1).toString().length() ));
      String svr = svrha.get(1).toString();
      if(svr.equals("")) svr=" ";
      virmanDS.setString("ROW7", svr);
      virmanDS.setString("ROW7_1", handleSifra(qds.getString("SIF1"), qds.getString("SIF2"),qds.getString("SIF3")));
      virmanDS.setString("ROW8", svrha.get(2).toString());
      virmanDS.setString("ROW9", formatIznos(qds.getBigDecimal("IZNOS"), 63));
      svrha.clear();
      handleSvrha(replaceReturn(qds.getString("UKORIST")),20);
      checkLL();

      virmanDS.setString("ROW10", svrha.get(0).toString());
      virmanDS.setString("ROW11", handleGlobal(svrha.get(1).toString(),qds.getString("BRRACUK"),"",36));
      virmanDS.setString("ROW12", svrha.get(2).toString());
      virmanDS.setString("ROW13", handlePNB2(qds.getString("PNBO1"), qds.getString("PNBO2"), svrha.get(2).toString().length()));
      virmanDS.setString("ROW14", getMjDat());

      virmanDS.setString("DUMMY", " ");
      virmanDS.post();
      qds.next();
    }
  }

  private void handleSvrha(String str, int duzina)
  {
    String temp = "";
    if(str.indexOf(" ")<0)
    {
      if(str.length()>22)
      {
        svrha.add(" ");
        parseStr(str, 33);
      }
      else
      {
        svrha.add(str);
      }



      return;

    }

    StringTokenizer st = new StringTokenizer(str, " ");

    while(st.hasMoreTokens())

    {

      String nastavak = st.nextToken();

      temp += nastavak + " ";

      if(temp.substring(0, temp.length()-1).length()>duzina)

      {

        temp = temp.substring(0, temp.length()-nastavak.length()-2);

        svrha.add(temp);



        if(temp.length()<str.length())

        {

          String newStr = str.substring(temp.length()+1, str.length());

          handleSvrha(newStr,30);



          return;

        }

      }

    }

    svrha.add(temp);

  }



  private String handlePNB(String pn1, String pn2)

  {

    String drugi = vl.maskString(pn2,' ',22);

    return pn1+" "+drugi;

  }



  private String handlePNB2(String pn1, String pn2, int len)

  {

//    System.out.println("len = "+len);

    len = len == 1?0:len;//andrej: nije mi jasno zašto ali bez ovog ne radi

    String drugi = vl.maskString(pn2,' ',23);

    String finalStr = pn1+drugi;

    return vl.maskString(finalStr,' ', 63-len);

  }



//  private String handleSifra(String s1, String s2, String s3, int svrLen)

//  {

//    System.out.println("svrLen: " + svrLen);

//    svrLen = svrLen == 1?0:svrLen;

//    String finalStr = "";

//    String sif2 = vl.maskString(s2.trim(),' ',4);

//    String sif3 = vl.maskString(s3.trim(),' ',4);

//    finalStr = s1+sif2+sif3;

//    System.out.println("'"+vl.maskString(finalStr,' ', 63-svrLen)+"'");

//    return vl.maskString(finalStr,' ', 63-svrLen);

//  }



  private String handleSifra(String s1, String s2, String s3)

  {

    String finalStr = "";

    String sif2 = vl.maskString(s2.trim(),' ',4);

    String sif3 = vl.maskString(s3.trim(),' ',4);

    finalStr = s1+sif2+sif3;

    return finalStr;

  }



  private String handleGlobal(String s1, String s2, String s3, int len)

  {

    String nacIz = vl.maskString(s3,' ', 27-s2.length());

    String brRac = vl.maskString(s2,' ', (len+s2.length())-s1.length());

    return s1+brRac+nacIz;

  }



  private String getMjDat()

  {

    String mj = qds.getString("MJESTO");

    String dat = datumParser(qds.getTimestamp("DATUMIZV").toString());

    return mj+", "+dat;

  }



  private String datumParser(String datum)

  {

      return datum.substring(8,10)+"."+datum.substring(5,7)+"."+datum.substring(0,4)+".";

  }



  private String formatIznos(BigDecimal izn, int len)

  {

    String finalStr ="";

    int idx=0;

    String temp="";

    String iznos = izn.toString();

    String iznosInt=iznos.substring(0,iznos.indexOf("."));

    String iznosDec=iznos.substring(iznos.indexOf(".")+1, iznos.length());

    for(int i= 0; i<iznosInt.length();i++)

    {

      temp = iznosInt.substring((iznosInt.length()-(i+1)),(iznosInt.length()-(i)))+temp;

      idx++;

      if(idx==3 && iznosInt.length()>3)

      {

        temp="."+temp;

        idx=0;

      }

    }

    finalStr = "*"+temp+","+iznosDec;

    return vl.maskString(finalStr,' ', len);

  }





  private void checkLL()

  {

    if(svrha.size()<3)

      {

        for(int i = svrha.size();i<3;i++)

          svrha.add("");

      }

  }



//  private String replaceEndRowReturn(String str)

//   {

//    int i = 0;

//     try {

//       System.out.println("li: " + str.lastIndexOf("\n"));

//       System.out.println("len: " + str.length());

//       while(str.lastIndexOf("\n") == str.length()-1)

//       {

//         System.out.println("'" + str+"'");

//         str = str.substring(0, str.lastIndexOf("\n"));

//       }

//     }

//     catch (Exception ex) {

//       ex.printStackTrace();

//     }

//     return str;

//  }



  private String replaceReturn(String initStr)

  {

    initStr = initStr.trim();

    int i = initStr.indexOf("\n");



    if(i>0 && i!= (initStr.length()-1))

      return replaceReturn(initStr.substring(0, i)+", "+ initStr.substring(i+1, initStr.length()));

    else if(i>0)

      return replaceReturn(initStr.substring(0, i)+" "+ initStr.substring(i+1, initStr.length()));

    return initStr;

  }



  private void parseStr(String str, int duzina)

  {

    int pointer = 0;

    while(pointer < str.length())

    {

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