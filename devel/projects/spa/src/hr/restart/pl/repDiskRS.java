/****license*****************************************************************
**   file: repDiskRS.java
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
import hr.restart.sisfun.raUser;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.reports.mxPrinter;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.repDisk;

import java.io.File;
import java.math.BigDecimal;

import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class repDiskRS extends repDisk{
  QueryDataSet qds;
  StorageDataSet kumulDS;
  StorageDataSet qdsIdent;
  QueryDataSet cjelineDS = new QueryDataSet();
  hr.restart.baza.dM dm;
  Valid vl;
  int brSlog5=0;
  lookupData ld = lookupData.getlookupData();

  public repDiskRS() {
    super(400);
    do_job();
  }
  public repDiskRS(int sirina) {
    super(sirina);
    do_job();
  }
  private void do_job() {
    try {
      try {
        this.setPrinter(mxPrinter.getDefaultMxPrinter());
        //this.getPrinter().setNewline(System.getProperty("line.separator"));
        this.getPrinter().setNewline("\r\n"); //samo DOS rs spasava :)
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      this.setPrint("Regos.rs0");
      fill();
    }
    catch (Exception ex) {
    }
  }
  public static String getOvlCLog() {
    //za slog 0 i 9
    if (frmParam.getParam("pl", "useOvlRSL", "N", "Koristiti posebne podatke o ovl. osobi na RSm datoteci?").equalsIgnoreCase("D")) {
      return frmParam.getParam("pl", "ovlRSLog", OrgStr.getKNJCORG(), "Oznaka logotipa ovlastene osobe za predaju rs obrazaca");
    }
    return OrgStr.getKNJCORG();
  }
  public void fill() throws Exception
  {
    vl = Valid.getValid();
    dm = hr.restart.baza.dM.getDataModule();
    qds = frmRS.getInstance().detailRS;
    qdsIdent=(StorageDataSet)frmRS.getInstance().getHead();
    qdsIdent.open();
    qdsIdent.first();

    kumulDS = frmRS.getInstance().getKumulRS();
    kumulDS.open();
    kumulDS.first();

    qds.open();
    diskzap.open();
    addRowDisk(insertSlog0());
    while(qdsIdent.inBounds())
    {
      addRowDisk(insertSlog3());
      insertSlog5(qdsIdent.getString("IDENTIFIKATOR"));
      addRowDisk(insertSlog7());
      qdsIdent.next();
    }
    addRowDisk(insertSlog9());
  }

  public StringBuffer insertSlog0()
  {
    StringBuffer sb = getNullSB();
    String matBr = getMatBr(getOvlCLog());
    String jmbg = getJMBG(getOvlCLog());
    String naziv = getNaziv(getOvlCLog(),0);
    String adresa = getNaziv(getOvlCLog(),1);
    String user = raUser.getInstance().getImeUsera();
    String tel = getTelEmail(getOvlCLog(),0);
    String email = getTelEmail(getOvlCLog(),1);
    String date = getDate();

    if(matBr.equals(""))
      matBr = vl.maskString(matBr,'0',12);
    else
      matBr = addZeroMB(matBr);

    if(jmbg.equals(""))
      jmbg = vl.maskString(jmbg,'0',13);

    sb.replace(0, matBr.length(), matBr);
    sb.replace(12, 12+jmbg.length(),jmbg);
    sb.replace(25, 25+naziv.length(),naziv);
    sb.replace(75, 75 + adresa.length(), adresa);
    sb.replace(125, 125+user.length(), user);
    sb.replace(175, 175 + tel.length(), tel);
    sb.replace(185, 185+email.length(), email);
    sb.replace(215, 215 + date.length(), date);
    sb.replace(223, 223 + "RS400".length(), "RS400" );
    sb.replace(399, 400, "0");

    return sb;
  }

  private void addRowDisk(StringBuffer sb) {
    diskzap.insertRow(false);
    diskzap.setString("REDAK", new String(sb));
    diskzap.post();
  }

  public StringBuffer insertSlog3()
  {
    String sbd10,sbd11,sbd12,sbd13,sbd14,sbd15,sbd16,sbd17,sbd18,sbd19,sbd20,sbd21,sbd22,sbd23,sbd24;

    sbd10 = formatIznos(kumulDS.getBigDecimal("BRUTOMJ"),14);
    sbd11 = formatIznos(kumulDS.getBigDecimal("BRUTO"),14);
    sbd12 = formatIznos(kumulDS.getBigDecimal("MIO1MJ"),14);
    sbd13 = formatIznos(kumulDS.getBigDecimal("MIO1"),14);
    sbd14 = formatIznos(kumulDS.getBigDecimal("MIO2MJ"),14);
    sbd15 = formatIznos(kumulDS.getBigDecimal("MIO2"),14);
    sbd16 = formatIznos(kumulDS.getBigDecimal("ZOMJ"),14);
    sbd17 = formatIznos(kumulDS.getBigDecimal("ZO"),14);
    sbd18 = formatIznos(kumulDS.getBigDecimal("ZAPOSMJ"),14);
    sbd19 = formatIznos(kumulDS.getBigDecimal("ZAPOS"),14);
    sbd20 = formatIznos(kumulDS.getBigDecimal("POREZMJ"),14);
    sbd21 = formatIznos(kumulDS.getBigDecimal("POREZ"),14);
    sbd22 = formatIznos(kumulDS.getBigDecimal("PRIREZMJ"),14);
    sbd23 = formatIznos(kumulDS.getBigDecimal("PRIREZ"),14);
    sbd24 = formatIznos(kumulDS.getBigDecimal("NETOPK"),14);

    StringBuffer sb = getNullSB();
    String matBr = getMatBr();
    String jmbg = getJMBG();
    String id = getIdentifikator();
    String date = getDate();
    String naziv = getNaziv(0);
    String adresa = getNaziv(1);
    String _mjobr = null;
    if (qdsIdent.getString("CSIF").equals("03") ||qdsIdent.getString("CSIF").equals("05")||qdsIdent.getString("CSIF").equals("08")){
      _mjobr = "00";
    } else {
      _mjobr = vl.maskZeroInteger(new Integer(qdsIdent.getShort("MJESEC")+""),2);
    }
    String mjGod = ""+_mjobr+qdsIdent.getShort("GODINA");
    String brOsig = vl.maskString(frmRS.getInstance().getBrojRadnika()+"",'0',5);
    String vrUpl = vl.maskString(qdsIdent.getString("CSIF")+"",'0',2);
    String vrObv = getVrstaObv();

    if(matBr.equals(""))
      matBr = vl.maskString(matBr,'0',12);
    if(jmbg.equals(""))
      jmbg = vl.maskString(jmbg,'0',13);
    
    matBr = addZeroMB(matBr);
    sb.replace(0,matBr.length(),matBr);
    sb.replace(12,12+jmbg.length(),jmbg);
    sb.replace(25,25+id.length(),id);
    sb.replace(29, 29+naziv.length(), naziv);
    sb.replace(79, 79+adresa.length(), adresa);
    sb.replace(129, 129+vrObv.length(), vrObv);
    sb.replace(131, 131+mjGod.length(), mjGod);
    sb.replace(137, 137+vrUpl.length(), vrUpl);
    sb.replace(139, 139+brOsig.length(),brOsig);
    sb.replace(144, 144+sbd10.length(), sbd10);
    sb.replace(159, 159+sbd11.length(), sbd11);
    sb.replace(174, 174+sbd12.length(), sbd12);
    sb.replace(189, 189+sbd13.length(), sbd13);
    sb.replace(204, 204+sbd14.length(), sbd14);
    sb.replace(219, 219+sbd15.length(), sbd15);
    sb.replace(234, 234+sbd16.length(), sbd16);
    sb.replace(249, 249+sbd17.length(), sbd17);
    sb.replace(264, 264+sbd18.length(), sbd18);
    sb.replace(279, 279+sbd19.length(), sbd19);
    sb.replace(294, 294+sbd20.length(), sbd20);
    sb.replace(309, 309+sbd21.length(), sbd21);
    sb.replace(324, 324+sbd22.length(), sbd22);
    sb.replace(339, 339+sbd23.length(), sbd23);
    sb.replace(354, 354+sbd24.length(), sbd24);
    sb.replace(399,400,"3");
    return sb;
  }

  public void insertSlog5(String idt)
  {

    qds.setSort(new SortDescriptor(new String []{"IDENTIFIKATOR","JMBG","ODDANA"}));
    qds.first();
    brSlog5 = qds.getRowCount();
    int rbr = 0;
    String jm = "###";
    while(qds.inBounds()) {
      if(qds.getString("IDENTIFIKATOR").equals(idt)) {
      	if (!jm.equals(qds.getString("JMBG"))) {
      		rbr++;
      		jm = qds.getString("JMBG");
      	}
        addRowDisk(getSlog5(rbr));
      }
        qds.next();
    }
    //diskzap.post();
  }

  public StringBuffer getSlog5(int rbr) {
    StringBuffer sb;
    String jmbg, imePrezime,copc,osnObr,razdoblje,obrMjIzn, obim, obDopr1Stup,
    obDopr2Stup, obDoprZO, obDoprZap, uplPrOs, osOd, porDoh, prirPor, ispl, tipSloga;
    sb = getNullSB();
    jmbg = qds.getString("JMBG");
    imePrezime = getImePrez(qds.getString("CRADNIK"));
    copc = raIzvjestaji.convertCopcineToRS(qds.getString("COPCINE"));
    osnObr=qds.getString("RSOO")+qds.getString("RSINV")+
           qds.getString("RSB")+qds.getString("RSZ")+getSati(qds.getBigDecimal("SATI"));
    razdoblje = getDani(qds.getShort("ODDANA")+"",qds.getShort("DODANA")+"");

    obrMjIzn = formatIznos(qds.getBigDecimal("BRUTOMJ"),14);
    obim=formatIznos(qds.getBigDecimal("BRUTO"),14);
    obDopr1Stup=formatIznos(qds.getBigDecimal("MIO1"),14);
    obDopr2Stup=formatIznos(qds.getBigDecimal("MIO2"),14);
    obDoprZO=formatIznos(qds.getBigDecimal("ZO"),14);
    obDoprZap=formatIznos(qds.getBigDecimal("ZAPOS"),14);
    uplPrOs=formatIznos(qds.getBigDecimal("PREMOS"),14);
    osOd=formatIznos(qds.getBigDecimal("OSODB"),14);
    porDoh=formatIznos(qds.getBigDecimal("POREZ"),14);
    prirPor=formatIznos(qds.getBigDecimal("PRIREZ"),14);
    ispl=formatIznos(qds.getBigDecimal("NETOPK"),14);

    sb.replace(0,jmbg.length(),jmbg);
    sb.replace(13,13+imePrezime.length(),imePrezime);
    sb.replace(43,43+copc.length(),copc);
    sb.replace(47,47+osnObr.length(),osnObr);
    sb.replace(56,56+razdoblje.length(),razdoblje);
    sb.replace(60,60+obrMjIzn.length(),obrMjIzn);
    sb.replace(75,75+obim.length(),obim);
    sb.replace(90,90+obDopr1Stup.length(),obDopr1Stup);
    sb.replace(105,105+obDopr2Stup.length(),obDopr2Stup);
    sb.replace(120,120+obDoprZO.length(),obDoprZO);
    sb.replace(135,135+obDoprZap.length(),obDoprZap);
    sb.replace(150,150+uplPrOs.length(),uplPrOs);
    sb.replace(165,165+osOd.length(),osOd);
    sb.replace(180,180+porDoh.length(),porDoh);
    sb.replace(195,195+prirPor.length(),prirPor);
    sb.replace(210,210+ispl.length(),ispl);
    sb.replace(399,400,"5");
    return sb;
  }

  public StringBuffer insertSlog7()
  {
    StringBuffer sb = getNullSB();
    String matBr = getMatBr();
    String jmbg = getJMBG();
    String id = getIdentifikator();
    String date = getDate();
    String br = vl.maskString(brSlog5+"",'0',5);

    if(matBr.equals(""))
      matBr = vl.maskString(matBr,'0',12);
    if(jmbg.equals(""))
      jmbg = vl.maskString(jmbg,'0',13);

    matBr = addZeroMB(matBr);
    sb.replace(0,matBr.length(),matBr);
    sb.replace(12,12+jmbg.length(),jmbg);
    sb.replace(25,25+id.length(),id);
    sb.replace(29,29+br.length(),br);
    sb.replace(34,34+date.length(),date);
    sb.replace(399,400,"7");

    return sb;
  }

  public StringBuffer insertSlog9()
  {
    StringBuffer sb = getNullSB();
    String matBr = getMatBr(getOvlCLog());
    String jmbg = getJMBG(getOvlCLog());
    String date = getDate();
    String brRS = vl.maskString(qdsIdent.getRowCount()+"",'0',3);
    if(matBr.equals(""))
      matBr = vl.maskString(matBr,'0',12);

    if(jmbg.equals(""))
      jmbg = vl.maskString(jmbg,'0',13);
    matBr = addZeroMB(matBr);
    sb.replace(0,matBr.length(),matBr);
    sb.replace(12,12+jmbg.length(),jmbg);
    sb.replace(25,25+date.length(),date);
    sb.replace(33,33+brRS.length(),brRS);
    sb.replace(399,400,"9");

    return sb;
  }

  public StringBuffer getNullSB()
  {
    String dummy = "";
    for (int i = 0; i<400;i++)
    {
      dummy+=" ";
    };
    return new StringBuffer(dummy);
  }
  String getImePrez(String cRad)
  {
    ld.raLocate(dm.getAllRadnici(), new String[]{"CRADNIK"}, new String[]{cRad});
    return dm.getAllRadnici().getString("IME")+" "+dm.getAllRadnici().getString("PREZIME");
  }

  private String getNaziv(int i) {
    return getNaziv(OrgStr.getKNJCORG(), i);
  }
  private String getNaziv(String clog, int i)
 {
   String cKnjig = clog;
   ld.raLocate(dm.getLogotipovi(), new String[]{"CORG"}, new String[]{cKnjig});
   if(i==0) return dm.getLogotipovi().getString("NAZIVLOG");
   else     return dm.getLogotipovi().getString("ADRESA") + ", " + dm.getLogotipovi().getInt("PBR")+" " + dm.getLogotipovi().getString("MJESTO");
  }

  String getSati(BigDecimal s)
  {
    BigDecimal tempSati = s.multiply(new BigDecimal(10));
    String tmpSati = tempSati.toString();
    String sati = "";
    int idx = tmpSati.indexOf(".");
    if(idx>-1)
    {
      sati= tmpSati.substring(0,idx);
    }
    else
      sati = tmpSati;
    return vl.maskString(sati,'0',4);
  }

  String getDani(String danOd , String danDo)
  {
    return vl.maskString(danOd,'0',2) + vl.maskString(danDo,'0',2);
  }

  String formatIznos(BigDecimal izn, int len)
  {
    String prefix="+";
    String intPart="";
    String decPart="";
    int idx = izn.toString().indexOf(".");
    if(idx>-1)
    {
      intPart= izn.toString().substring(0,idx);
      decPart= izn.toString().substring(idx+1, izn.toString().length());
      if(izn.compareTo(new BigDecimal(0))<0)
        prefix="-";
    }
    return prefix + vl.maskString(intPart+decPart,'0',len);
  }

  public static String getMatBr() {
    return getMatBr(OrgStr.getKNJCORG());
  }
  public static String getMatBr(String clog) //koristi se u repSBDisk_*
  {
    String cKnjig = clog;
    String mb = "";
    boolean nasao = lookupData.getlookupData().raLocate(hr.restart.baza.dM.getDataModule().getLogotipovi(), new String[]{"CORG"}, new String[]{cKnjig});
    mb=hr.restart.baza.dM.getDataModule().getLogotipovi().getString("MATBROJ");
    System.err.println("Logotip "+clog+" ... "+(nasao?"nasao!":"NEMA!!!")+" mb="+mb);
    if(mb.length()<9) {
      //return "0" + dm.getLogotipovi().getString("MATBROJ"); //uzas 
      try {
        return Valid.getValid().maskZeroInteger(new Integer(mb.trim()), 8);  
      } catch (Exception ex) {
        return mb;
      }
    } else {
      return "";
    }
  }

  private String getTelEmail(int i) {
    return getTelEmail(OrgStr.getKNJCORG(), i);
  }
  
  private String getTelEmail(String clog, int i)
  {
    String cKnjig = clog;
    ld.raLocate(dm.getLogotipovi(), new String[]{"CORG"}, new String[]{cKnjig});
    if(i == 0)
      return removeNonDigit(dm.getLogotipovi().getString("TEL1"));
    return dm.getLogotipovi().getString("EMAIL");
  }

  private String getJMBG() {
    return getJMBG(OrgStr.getKNJCORG());
  }
  private String getJMBG(String clog)
  {
    String cKnjig = clog;
    String jmbg = "";
    boolean nasao = ld.raLocate(dm.getLogotipovi(), new String[]{"CORG"}, new String[]{cKnjig});
    jmbg=dm.getLogotipovi().getString("MATBROJ");
    System.err.println("Logotip "+clog+" ... "+(nasao?"nasao!":"NEMA!!!")+" jmbg="+jmbg);
    if(jmbg.length()<10)
      return"";
    else
      return dm.getLogotipovi().getString("MATBROJ");
  }

  private String getVrstaObv()
  {
    return qdsIdent.getString("CVROB").trim();
    /*
    String cKnjig = OrgStr.getKNJCORG();
    String mb = "";
    ld.raLocate(dm.getLogotipovi(), new String[]{"CORG"}, new String[]{cKnjig});
    mb=dm.getLogotipovi().getString("MATBROJ");
    if(mb.length()<10)
      return "01";
    return "02";*/
  }

  private String getIdentifikator()
  {
    return vl.maskString(qdsIdent.getString("IDENTIFIKATOR"),'0',4) ;
  }

  private String getDate()
  {
    String date = vl.getToday().toString();
    String dd = date.substring(8,10);
    String mm = date.substring(5,7);
    String yyyy = date.substring(0,4);

    return dd+mm+yyyy;
  }

  private String addZeroMB(String s)
  {
    for (int i = 0; i<12;i++)
    {
      s+="0";
    }
    return s;
  }

  public String removeNonDigit(String s)
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
}