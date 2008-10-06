/****license*****************************************************************
**   file: repPKDisk.java
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

import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;

public class repPKDisk extends repDisk{
  StorageDataSet qds;
  StorageDataSet kumulDS = new StorageDataSet();
  frmPK frPK = frmPK.getPKInstance();
  hr.restart.baza.dM dm;
  Valid vl;
  int mjeseci[] = new int []{0,0,0,0,0,0,0,0,0,0,0,0};
  lookupData ld = lookupData.getlookupData();

  public repPKDisk() {
    super(200);
    try {
      try {
        this.setPrinter(mxPrinter.getDefaultMxPrinter());
        this.getPrinter().setNewline(System.getProperty("line.separator"));
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      qds = (StorageDataSet)frPK.getRepSet();
      qds.open();
      qds.first();
      this.setPrint("PK1-"+qds.getShort("GODOBR")+".txt");
      fill();
    }
    catch (Exception ex) {
    }
  }

  public void fill() throws Exception
  {
    vl = Valid.getValid();
    dm = hr.restart.baza.dM.getDataModule();
    qds.first();


      kumulDS.setColumns(qds.cloneColumns());

    kumulDS.open();
    diskzap.open();
    sumIznosi();
    insertSlog0();
    kumulDS.first();
    while(kumulDS.inBounds())
    {
      insertSlog3();
      kumulDS.next();
    }
  }

  private void insertSlog0()
  {
    StringBuffer sb = getNullSB();
    String matBr = frPK.getKnjMatbroj();
    String naziv = frPK.getKnjNaziv();
    String godObr = getGodObr();
    String pu = getPUZiroNasUl(0);
    String ziro = getPUZiroNasUl(1);
    String date = getDate();
    String brSlog = addZero(frPK.getInstance().getBrojRadnika()+"",6);
    String naselje = getPUZiroNasUl(2);
    String ulica = parseUlica(0);
    String broj = addZero(parseBroj(0),3);
    String dodatakKB = parseBroj(1);

    if(matBr.length()<13)
      matBr = "0"+matBr;
    sb.replace(0, matBr.length(), matBr);
    sb.replace(13, 13+godObr.length(),godObr);
    sb.replace(17, 17+date.length(),date);
    sb.replace(25, 25+pu.length(),pu);
    sb.replace(50, 50+naziv.length(),naziv);
    sb.replace(100, 100+ziro.length(),ziro);
    sb.replace(120, 120+naselje.length(),naselje);
    sb.replace(145, 145+ulica.length(),ulica);
    sb.replace(167, 167+broj.length(),broj);
    sb.replace(170, 170+dodatakKB.length(),dodatakKB);
    sb.replace(172, 172+brSlog.length(),brSlog);

    diskzap.insertRow(false);
    diskzap.setString("REDAK", new String(sb));
    diskzap.post();
  }

  private void insertSlog3()
  {
    String jmbgPP = kumulDS.getString("JMBG");
    String prezime = kumulDS.getString("PREZIME");
    String ime = kumulDS.getString("IME");
    String cOpc = addZero(getCOpc(kumulDS.getString("CRADNIK")),3);
    String nazOpc = getNazOpc(cOpc);
    String pu = "PU"+nazOpc;
    String mj = getMjeseciStr();
    String bruto = formatIznos(kumulDS.getBigDecimal("BRUTO"),11);
    String doprinosi = formatIznos(kumulDS.getBigDecimal("DOPRINOSI"),10);
    String osOdb = formatIznos(kumulDS.getBigDecimal("ISKNEOP"),10);
    String porez = formatIznos(kumulDS.getBigDecimal("PORUK"),10);
    String prirez = formatIznos(kumulDS.getBigDecimal("PRIR"),10);
    String dopOs = formatIznos(Aus.zero2,10);// za sad nula, jer ga nema u srkijevom datasetu
    String bezIsplate = checkMjStr();


    StringBuffer sb = getNullSB();
    sb.replace(0,jmbgPP.length(), jmbgPP);
    sb.replace(13,13+prezime.length(), prezime);
    sb.replace(33,33+ime.length(), ime);
    sb.replace(48,48+cOpc.length(), cOpc);
    sb.replace(53,53+nazOpc.length(), nazOpc);
    sb.replace(78,78+pu.length(), pu);
    sb.replace(113,113+mj.length(), mj);
    sb.replace(125,125+bruto.length(), bruto);
    sb.replace(136,136+doprinosi.length(), doprinosi);
    sb.replace(146,146+osOdb.length(), osOdb);
    sb.replace(156,156+doprinosi.length(), doprinosi);
    sb.replace(166,166+porez.length(), porez);
    sb.replace(176,176+prirez.length(), prirez);
    sb.replace(186,186+dopOs.length(), dopOs);
    sb.replace(196,196+bezIsplate.length(), bezIsplate);
    sb.replace(197,197+"PK1".length(), "PK1");

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

  private String getTelEmail(int i)
  {
    String cKnjig = OrgStr.getKNJCORG();
    ld.raLocate(dm.getLogotipovi(), new String[]{"CORG"}, new String[]{cKnjig});
    if(i == 0)
      return removeNonDigit(dm.getLogotipovi().getString("TEL1"));
    return dm.getLogotipovi().getString("EMAIL");
  }

  private String getDate()
  {
    String date = vl.getToday().toString();
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

  private void sumIznosi()
  {
    String cRadnik = "";
    qds.setSort(new SortDescriptor(new String[]{"CRADNIK"}));
    qds.first();
    while(qds.inBounds())
    {
      if(!cRadnik.equals(qds.getString("CRADNIK")))
      {
        cRadnik = qds.getString("CRADNIK");
        kumulDS.insertRow(false);
        kumulDS.setString("CORG", qds.getString("CORG"));
        kumulDS.setString("CVRO", qds.getString("CVRO"));
        kumulDS.setShort("GODOBR", qds.getShort("GODOBR"));
        kumulDS.setShort("MJOBR", qds.getShort("MJOBR"));
        kumulDS.setShort("RBROBR", qds.getShort("RBROBR"));
        kumulDS.setString("CRADNIK", qds.getString("CRADNIK"));
        kumulDS.setString("IME", qds.getString("IME"));
        kumulDS.setString("PREZIME", qds.getString("PREZIME"));
        kumulDS.setString("JMBG", qds.getString("JMBG"));
        kumulDS.setString("ADRESA", qds.getString("ADRESA"));
      }
      setMonthObr(qds.getShort("MJOBR"));
      kumulDS.setBigDecimal("BRUTO", kumulDS.getBigDecimal("BRUTO").add(qds.getBigDecimal("BRUTO")));
      kumulDS.setBigDecimal("DOPRINOSI", kumulDS.getBigDecimal("DOPRINOSI").add(qds.getBigDecimal("DOPRINOSI")));
      kumulDS.setBigDecimal("ISKNEOP", kumulDS.getBigDecimal("ISKNEOP").add(qds.getBigDecimal("ISKNEOP")));
      kumulDS.setBigDecimal("POROSN", kumulDS.getBigDecimal("POROSN").add(qds.getBigDecimal("POROSN")));
      kumulDS.setBigDecimal("PORUK", kumulDS.getBigDecimal("PORUK").add(qds.getBigDecimal("PORUK")));
      kumulDS.setBigDecimal("PRIR", kumulDS.getBigDecimal("PRIR").add(qds.getBigDecimal("PRIR")));
      kumulDS.setBigDecimal("PORIPRIR", kumulDS.getBigDecimal("PORIPRIR").add(qds.getBigDecimal("PORIPRIR")));
      qds.next();
    }
    kumulDS.post();
  }

  private void setMonthObr(short mjObr)
  {
    int idx = (int)mjObr;
    mjeseci[idx-1] = 1;
  }
  private String getCOpc(String crad)
  {
    ld.raLocate(dm.getAllRadnicipl(), new String[]{"CRADNIK"}, new String[]{crad});
    return dm.getAllRadnicipl().getString("COPCINE");
  }

  private String getNazOpc(String cOp)
  {
    ld.raLocate(dm.getOpcine(), new String[]{"COPCINE"}, new String[]{cOp});
    return dm.getOpcine().getString("NAZIVOP");
  }

  private String getMjeseciStr()
  {
    String temp="";
    for (int t = 0; t<mjeseci.length;t++)
    {
      temp +=mjeseci[t];
    }
    return temp;
  }
  private String addZero(String s, int len)
  {
    return vl.maskString(s,'0',len);
  }

  private String checkMjStr()
  {
    for(int i = 0; i<mjeseci.length;i++)
    {
      if(mjeseci[i]==1) return "1";
    }
    return "0";
  }
  /*


  */
}

