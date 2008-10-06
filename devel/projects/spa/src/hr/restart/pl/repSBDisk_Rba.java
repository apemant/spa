/****license*****************************************************************
**   file: repSBDisk_Rba.java
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
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.reports.mxPrinter;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.repDisk;

import java.io.File;
import java.math.BigDecimal;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class repSBDisk_Rba extends repDisk{
  StorageDataSet qds;
  QueryDataSet rbaDS = new QueryDataSet();
  frmBankSpec fBS = frmBankSpec.getInstance();
  hr.restart.baza.dM dm;
  BigDecimal naRukeUK = Aus.zero2;
  Valid vl;
  lookupData ld = lookupData.getlookupData();
  int cBanke;
  String isplMj ="";
  String tipFile;
  String knjigovodstvo  = OrgStr.getKNJCORG();

  public repSBDisk_Rba() {
    super(23);
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
      this.setPrint("SB_Rba.txt");
      fill();
    }
    catch (Exception ex) {
    }
  }

  public void fill() throws Exception
  {
    vl = Valid.getValid();
    dm = hr.restart.baza.dM.getDataModule();
    rbaDS.setColumns(qds.cloneColumns());
    tipFile = fBS.tipFile;
    cBanke = getCBanke();
    fillRbaDS();
    rbaDS.open();
    rbaDS.first();
    qds.first();
    diskzap.open();
    this.setHeader(new String[]{insertVodeciSlog()});
//    insertVodeciSlog();
    while(rbaDS.inBounds())
    {
      insertSlogPodataka();

      rbaDS.next();
    }
  }

  private String insertVodeciSlog()
  {
    StringBuffer sb = getNullSB();
    String date = getDate();
    String brS = getBrSl();
    String sifPos = vl.maskString(knjigovodstvo,'0',6);
    String ukupno = formatIznos(naRukeUK,17);
    System.out.println("date: " + date);

    sb.replace(0, 0+"19101".length(),"19101");
    sb.replace(5, 5+sifPos.length(),sifPos);
    sb.replace(11, 11+date.length(),date);
    sb.replace(19, 19+brS.length(),brS);
    sb.replace(25, 25+ukupno.length(),ukupno);

    return sb.toString();
  }


  private void insertSlogPodataka()
  {
    StringBuffer sb = getNullSBProm();
    String sifKom = vl.maskString(rbaDS.getString("CRADNIK"),'0',6);
    String iznos = formatIznos(rbaDS.getBigDecimal("NARUKE"), 17);

    sb.replace(0, 0+sifKom.length(),sifKom);
    sb.replace(6, 6+iznos.length(),iznos);

    diskzap.insertRow(false);
    diskzap.setString("REDAK", new String(sb));
    diskzap.post();
  }

  private StringBuffer getNullSB()
  {
    String dummy = "";
    for (int i = 0; i<42;i++)
    {
      dummy+=" ";
    };
    return new StringBuffer(dummy);
  }

  private StringBuffer getNullSBProm()
  {
    String dummy = "";
    for (int i = 0; i<23;i++)
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
    return vl.maskString(izn+"",' ',len);
  }

  private String getDate()
  {
    //String date = vl.getToday().toString();
    String date;
    if (raIniciranje.getInstance().posOrgsPl(OrgStr.getKNJCORG())) {
      date = dm.getOrgpl().getTimestamp("DATUMISPL").toString();
    } else {
      date = vl.getToday().toString();
    }
    String dd = date.substring(8,10);
    String mm = date.substring(5,7);
    String yyyy = date.substring(0,4);

    return yyyy+mm+dd;
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
  private void fillRbaDS()
  {
    rbaDS.open();
    qds.first();
    isplMj = qds.getShort("CISPLMJ")+"";
    while(qds.inBounds())
    {
      if(cBanke== qds.getInt("CBANKE"))
      {
        rbaDS.insertRow(false);
        naRukeUK = naRukeUK.add(qds.getBigDecimal("NARUKE"));
        qds.copyTo(rbaDS);
      }
      qds.next();
    }

    rbaDS.post();
  }

  private int getCBanke()
  {
    ld.raLocate(dm.getIsplMJ(), new String[]{"TIPFILE"}, new String[]{tipFile});
    return dm.getIsplMJ().getInt("CBANKE");
  }

  private String getBrBank()
  {
    ld.raLocate(dm.getBankepl(), new String [] {"CBANKE"}, new String [] {cBanke+""});
    return dm.getBankepl().getString("BRPOSL");
  }

  private String getIBPB()
  {
    ld.raLocate(dm.getBankepl(), new String[]{"CBANKE"}, new String[]{cBanke+""});
    return dm.getBankepl().getString("BRPOSL");
  }

  private String getMatBr()
  {
    return repDiskRS.getMatBr();
    /*
    ld.raLocate(dm.getLogotipovi(), new String[]{"CORG"}, new String[]{knjigovodstvo});
    return "0" + dm.getLogotipovi().getString("MATBROJ");
     */
  }

  private String getBrSl()
  {
    String brS = rbaDS.getRowCount()+"";
    return vl.maskString(brS,' ',6);
  }

  private String getZR()
  {
    String vratiZiro="";
    ld.raLocate(dm.getOrgstruktura(), new String[]{"CORG"}, new String[]{knjigovodstvo});
    String zr = dm.getOrgstruktura().getString("ZIRO");
    VarStr vs = new VarStr(zr);
    String[] poljeZR = vs.split('-');

    if(poljeZR.length==2)
    {
      vratiZiro = poljeZR[0]+poljeZR[1].substring(0,2)+
                  vl.maskString(poljeZR[1].substring(2,poljeZR[1].length()),'0',(17-poljeZR[0].length()-2));
    }
    else if(poljeZR.length==3)
    {
      try {
        vratiZiro = poljeZR[0]+poljeZR[1]+vl.maskString(poljeZR[2],'0', 15 -(poljeZR[0]+poljeZR[1]).length());
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return vratiZiro;
  }

  private String getOznApp()
  {
    ld.raLocate(dm.getIsplMJ(), new String[]{"CISPLMJ"}, new String[]{ rbaDS.getShort("ISPLMJ")+""});
    if(dm.getIsplMJ().getString("TIPISPLMJ").equals("T"))
      return "4";
    if(dm.getIsplMJ().getString("TIPISPLMJ").equals("S"))
      return "1";
    return "";
  }
}


