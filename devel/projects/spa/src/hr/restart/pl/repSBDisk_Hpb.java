/****license*****************************************************************
**   file: repSBDisk_Hpb.java
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
package hr.restart.pl;

import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.reports.mxPrinter;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.repDisk;

import java.io.File;
import java.math.BigDecimal;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class repSBDisk_Hpb extends repDisk{
  StorageDataSet qds;
  QueryDataSet zabaDS = new QueryDataSet();
  frmBankSpec fBS = frmBankSpec.getInstance();
  hr.restart.baza.dM dm;
  BigDecimal naRukeUK = Aus.zero2;
  Valid vl;
  lookupData ld = lookupData.getlookupData();
  int cBanke;
  String tipFile;
  String knjigovodstvo  = OrgStr.getKNJCORG();

  public repSBDisk_Hpb() {
    super(79);
    try {
      try {
        this.setPrinter(mxPrinter.getDefaultMxPrinter());
        this.getPrinter().setNewline(System.getProperty("line.separator"));
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      qds = (StorageDataSet)fBS.getSpecBankDS(new String[] {"CISPLMJ","CORG","PREZIME","IME"});
      qds.open();
      this.setPrint("SB_Zaba.txt");

      fill();
    }
    catch (Exception ex) {
    }
  }

  public void fill() throws Exception
  {
    vl = Valid.getValid();
    dm = hr.restart.baza.dM.getDataModule();
    zabaDS.setColumns(qds.cloneColumns());
    tipFile = fBS.tipFile;
    cBanke = getCBanke();
    fillZabaDS();
    zabaDS.open();
    zabaDS.first();
    qds.first();
    diskzap.open();
    insertSlog0_Zaba();
    while(zabaDS.inBounds())
    {
      insertSlog3_Zaba();
      zabaDS.next();
    }
  }

  private void insertSlog0_Zaba()
  {

    StringBuffer sb = getNullSB();
    String date = getDate();
    String iznos = formatIznos(naRukeUK,13);
    String brSl = getBrSl();
    String mBr = getMatBr()+"000";
    String ibPB = getIBPB()+"*   ";

    sb.replace(0, 0+"03000000003234495".length(),"03000000003234495");
    sb.replace(17, 17+date.length(),date);
    sb.replace(23, 23+"0000000000000".length(),"0000000000000");
    sb.replace(36, 36+iznos.length(),iznos);
    sb.replace(49, 49+brSl.length(),brSl);
    sb.replace(53, 53+mBr.length(),mBr);
    sb.replace(64, 64+"000000".length(),"000000");
    sb.replace(70, 70+ibPB.length(),ibPB);

    diskzap.insertRow(false);
    diskzap.setString("REDAK", new String(sb));
    diskzap.post();
  }

  private void insertSlog3_Zaba()
  {
    StringBuffer sb = getNullSB();
    String date = getDate();
    String iznos = formatIznos(zabaDS.getBigDecimal("NARUKE"),11);
    String brRac = vl.maskString(zabaDS.getString("BROJTEK")+"000000",'0',16);
    String mBr = getMatBr()+"000";
    String ibPB = getIBPB()+"*   ";

    sb.replace(0, 0+"03100000000000000".length(),"03100000000000000");
    sb.replace(17, 17+date.length(),date);
    sb.replace(23, 23+"0150".length(),"0150");
    sb.replace(27, 27+iznos.length(),iznos);
    sb.replace(38, 38+brRac.length(),brRac);
    sb.replace(53, 53+mBr.length(),mBr);
    sb.replace(64, 64+"000000000000*   ".length(),"000000000000*   ");
    diskzap.insertRow(false);
    diskzap.setString("REDAK", new String(sb));
    diskzap.post();

  }

  private StringBuffer getNullSB()
  {
    String dummy = "";
    for (int i = 0; i<200;i++)
    {
      dummy+=" ";
    };
    return new StringBuffer(dummy);
  }
  private String getImePrez(String cRad)
  {
    ld.raLocate(dm.getAllRadnici(), new String[]{"CRADNIK"}, new String[]{cRad});
    return dm.getAllRadnici().getString("IME")+" "+dm.getAllRadnici().getString("PREZIME");
  }

  private String formatIznos(BigDecimal izn, int len)
  {
    String intPart="";
    String decPart="";
    int idx = izn.toString().indexOf(".");
    if(idx>-1)
    {
      intPart= izn.toString().substring(0,idx);
      decPart= izn.toString().substring(idx+1, izn.toString().length());
    }
    return vl.maskString(intPart+decPart,'0',len);
  }

  private String getDate()
  {
    String date = vl.getToday().toString();
    String dd = date.substring(8,10);
    String mm = date.substring(5,7);
    String yyyy = date.substring(2,4);

    return dd+mm+yyyy;
  }

  private String removeNonDigit(String s)
  {
    String ret="";
    char[] initC = s.toCharArray();
    for(int z = 0; z<initC.length; z++)
    {
     if(Character.isDigit(initC[z]))
     {
       ret += initC[z];
     }
    }
    return ret;
  }

  private String setFileName()
  {
    int counter = 0;
    try {
      File currDir = new File("a:\\");
      String[] fileList = currDir.list();
      for(int i=0; i<fileList.length;i++)
      {
        if(fileList[i].substring(0,8).toUpperCase().equals("Regos.rs".toUpperCase()))
          counter++;
      }
    }
    catch (Exception ex) {}
    return "Regos.rs"+counter;
  }

  private String getGodObr()
  {
    int rowPointer = qds.getRow();
    qds.first();
    short gObr = qds.getShort("GODOBR");
    qds.goToRow(rowPointer);
    return gObr+"";
  }

  private String getPUZiroNasUl(int i)
  {
    int rowPointer = qds.getRow();
    qds.first();
    String corg = qds.getString("CORG");
    ld.raLocate(dm.getLogotipovi(), new String[]{"CORG"}, new String[]{corg});
    qds.goToRow(rowPointer);
    if (i == 0)
      return dm.getLogotipovi().getString("PORISP");
    else if (i == 1)
      return dm.getLogotipovi().getString("ZIRO");
    else if (i == 2)
      return dm.getLogotipovi().getString("MJESTO");
    return dm.getLogotipovi().getString("ADRESA");
  }

  private String parseUlica(int i)
  {
    String adresa = getPUZiroNasUl(3);
    int idx = adresa.lastIndexOf(" ");
    if(i==0)
      return adresa.substring(0, idx);
    return adresa.substring(idx, adresa.length());
  }

  private String parseBroj(int i)
  {
    String returnStr = "";
    String adresa = getPUZiroNasUl(3);
    int idx = adresa.lastIndexOf(" ");
    String broj = adresa.substring(idx+1, adresa.length());
    try {
      Integer integ = new Integer(broj);
      if(i==0)
        return broj;
      return "";
    }
    catch (Exception ex) {
      int idx2 =0;
      char[] initC = broj.toCharArray();
      for (int ind = 0; ind<initC.length;ind++)
      {
        if(Character.isDigit(initC[ind]))
          idx2 ++;
        else
          break;
      }
      if(i == 0)
        return broj.substring(0,idx2);
      if(broj.substring(idx2, broj.length()).length()>2)
        return broj.substring(idx2, idx2+2);
      return broj.substring(idx2, broj.length());

    }
  }
  private void fillZabaDS()
  {
    zabaDS.open();
    qds.first();
    while(qds.inBounds())
    {
      if(cBanke== qds.getInt("CBANKE"))
      {
        zabaDS.insertRow(false);
        naRukeUK = naRukeUK.add(qds.getBigDecimal("NARUKE"));
        qds.copyTo(zabaDS);
      }
      qds.next();
    }
    zabaDS.post();
    System.out.println("uk: " + naRukeUK);
  }

  private int getCBanke()
  {
    ld.raLocate(dm.getIsplMJ(), new String[]{"TIPFILE"}, new String[]{tipFile});
    return dm.getIsplMJ().getInt("CBANKE");
  }

  private String getIBPB()
  {
    ld.raLocate(dm.getBankepl(), new String[]{"CBANKE"}, new String[]{cBanke+""});
    return dm.getBankepl().getString("BRPOSL");
  }

  private String getMatBr()
  {
    ld.raLocate(dm.getLogotipovi(), new String[]{"CORG"}, new String[]{knjigovodstvo});
    return "0" + dm.getLogotipovi().getString("MATBROJ");
  }

  private String getBrSl()
  {
    String brS = zabaDS.getRowCount()+"";
    return vl.maskString(brS,'0',4);
  }
}


