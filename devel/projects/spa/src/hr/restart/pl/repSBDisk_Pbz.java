/****license*****************************************************************
**   file: repSBDisk_Pbz.java
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

import hr.restart.sisfun.frmParam;
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

public class repSBDisk_Pbz extends repDisk{
  StorageDataSet qds;
  QueryDataSet pbzDS = new QueryDataSet();
  frmBankSpec fBS = frmBankSpec.getInstance();
  hr.restart.baza.dM dm;
  BigDecimal naRukeUK = Aus.zero2;
  Valid vl;
  lookupData ld = lookupData.getlookupData();
  int cBanke;
  String isplMj ="";
  String tipFile;
  String knjigovodstvo  = OrgStr.getKNJCORG();

  public repSBDisk_Pbz() {
    super(128);
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
      this.setPrint("Pbz.txt");
      fill();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void fill() throws Exception
  {
    vl = Valid.getValid();
    dm = hr.restart.baza.dM.getDataModule();
    pbzDS.setColumns(qds.cloneColumns());
    tipFile = fBS.tipFile;
    cBanke = getCBanke();
    fillPbzDS();
    pbzDS.open();
    pbzDS.first();
    qds.first();
    diskzap.open();
    insertSlog0001();
    insertSlog0970();
    while(pbzDS.inBounds())
    {
      insertPromslog();
      pbzDS.next();
    }
  }

  private void insertSlog0001()
  {
    StringBuffer sb = getNullSB();
    String date = getDate();
    String brSl = getBrSl();
    String pjPosRm = getBrBank()+"89";
    String nuleijedinice = frmParam.getParam("pl","pbz001broj","000010011100010","Jedan od brojeva na disketi za tekuci PBZ u slogu 001");   
    sb.replace(0, 0+nuleijedinice.length(),nuleijedinice);
    sb.replace(15, 15+pjPosRm.length(),pjPosRm);
    sb.replace(22, 22+date.length(),date);
    sb.replace(30, 30+"99".length(),"99");
    sb.replace(111, 111+"0000".length(),"0000");
    sb.replace(121, 121+"W".length(),"W");
    sb.replace(122, 122 + brSl.length(), brSl);

    diskzap.insertRow(false);
    diskzap.setString("REDAK", new String(sb));
    diskzap.post();
  }

  private void insertSlog0970()
  {
    StringBuffer sb = getNullSB();
    String date = getDate();
    String brSl = getBrSl();
    String pjPosRm = getBrDom()+"00";
    String knjzirorac = "2490404";
    String zirorac = getZR();
    String iznDug = formatIznos(naRukeUK,15);
    String slog970broj = frmParam.getParam("pl","pbz970broj","000020021109705","Jedan od brojeva na disketi za tekuci PBZ u slogu 970");
    sb.replace(0, 0+slog970broj.length(),slog970broj);
    sb.replace(30, 30+knjzirorac.length(),knjzirorac);
    sb.replace(41, 41+pjPosRm.length(),pjPosRm);
    sb.replace(48, 48+zirorac.length(),zirorac);
    sb.replace(70, 70+date.length(),date);
    sb.replace(78, 78 + iznDug.length(), iznDug);

    diskzap.insertRow(false);
    diskzap.setString("REDAK", new String(sb));
    diskzap.post();
  }

  private void insertPromslog()
  {
    StringBuffer sb = getNullSB();
    String rbr = vl.maskString(pbzDS.getRow()+3+"",'0',5);
    String brK = "003";
    String ind ="11";
    String ozApp = getOznApp();
    String vrS = "2";
    String ozSlo="622";
    String izn = formatIznos(pbzDS.getBigDecimal("NARUKE"),15);
    String brTek = pbzDS.getString("BROJTEK");

    String knjRac ="";

    if(ozApp.equals("4"))
      knjRac = "804045";
    else if(ozApp.equals("1"))
      knjRac = "814008";

    sb.replace(0, 0+rbr.length(),rbr);
    sb.replace(5, 5+brK.length(),brK);
    sb.replace(8, 8+ind.length(),ind);
    sb.replace(10, 10+ozApp.length(),ozApp);
    sb.replace(11, 11+ozSlo.length(),ozSlo);
    sb.replace(11, 11+ozSlo.length(),ozSlo);
    sb.replace(14, 14+vrS.length(),vrS);
    sb.replace(15, 15+brTek.length(),brTek);
    sb.replace(26, 26+knjRac.length(),knjRac);
    sb.replace(37, 37+izn.length(),izn);


    diskzap.insertRow(false);
    diskzap.setString("REDAK", new String(sb));
    diskzap.post();
  }

  private StringBuffer getNullSB()
  {
    String dummy = "";
    for (int i = 0; i<128;i++)
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
    String date;
    if (raIniciranje.getInstance().posOrgsPl(OrgStr.getKNJCORG())) {
      date = dm.getOrgpl().getTimestamp("DATUMISPL").toString();
    } else {
      date = vl.getToday().toString();
    }
    String dd = date.substring(8,10);
    String mm = date.substring(5,7);
    String yyyy = date.substring(0,4);

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
  private void fillPbzDS()
  {
    pbzDS.open();
    qds.first();
    isplMj = qds.getShort("CISPLMJ")+"";
    while(qds.inBounds())
    {
      if(cBanke== qds.getInt("CBANKE"))
      {
        pbzDS.insertRow(false);
        naRukeUK = naRukeUK.add(qds.getBigDecimal("NARUKE"));
        qds.copyTo(pbzDS);
      }
      qds.next();
    }

    pbzDS.post();
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
  private String getBrDom() {
    ld.raLocate(dm.getBankepl(), new String [] {"CBANKE"}, new String [] {cBanke+""});
    return dm.getBankepl().getString("BRDOM");
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
    String brS = pbzDS.getRowCount()+2+"";
    return vl.maskString(brS,'0',5);
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
    ld.raLocate(dm.getIsplMJ(), new String[]{"CISPLMJ"}, new String[]{ pbzDS.getShort("ISPLMJ")+""});
    if(dm.getIsplMJ().getString("TIPISPLMJ").equals("T"))
      return "4";
    if(dm.getIsplMJ().getString("TIPISPLMJ").equals("S"))
      return "1";
    return "";
  }
}


