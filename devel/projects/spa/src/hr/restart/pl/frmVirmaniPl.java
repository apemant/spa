/****license*****************************************************************
**   file: frmVirmaniPl.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Orgpl;
import hr.restart.baza.Orgstruktura;
import hr.restart.baza.Povjerioci;
import hr.restart.baza.Virmani;
import hr.restart.baza.dM;
import hr.restart.sisfun.raDelayWindow;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raMnemonics;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.frmVirmani;
import hr.restart.zapod.raKnjigChangeListener;
import hr.restart.zapod.repDiskZapUN;

import java.sql.Timestamp;
import java.util.HashSet;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;

public class frmVirmaniPl extends frmVirmani{

  HashSet vars = new HashSet();
  raDelayWindow proc;
  dM dm = dM.getDataModule();
  QueryDataSet mainDS = new QueryDataSet();
  QueryDataSet kumulDS = new QueryDataSet();

  QueryDataSet naTerDS;// = Orgstruktura.getDataModule().getFilteredDataSet("corg='"+OrgStr.getKNJCORG()+"'");
  private static QueryDataSet pvrodbcorg;
  
  lookupData ld = lookupData.getlookupData();
  Column column1 = new Column();

//  String strVrOdb, strPov, strRad; -> raVirPlMnWorker
//  QueryDataSet povDS; -> raVirPlMnWorker
  raVirPlMnWorker worker;
  
  String cKey="";
  short rbrOdb=-1;
  boolean delete;
  boolean create;

  public frmVirmaniPl() {
    super("pl", true);
    worker = raVirPlMnWorker.getInstance();
//    super("pl");
//    System.out.println("frmVirmaniPl - aktivejtid ;)");
//    hr.restart.util.reports.mxReport m;
  }

  private void checkObracun() {
    //addVars();
    column1.setColumnName("PARAM");
    column1.setDataType(com.borland.dx.dataset.Variant.STRING);
    column1.setDefault("0");
    column1.setResolvable(false);
    column1.setSqlType(0);
    column1.setCaption("Param");
    initMainDS();
    initKumulDS();
    cKey = getCKey();
    raIniciranje in = raIniciranje.getInstance();
    in.checkObr(OrgStr.getKNJCORG());
    if(in.obrCount>0) {
      if(imaVirmana()) {
        if(JOptionPane.showConfirmDialog(null,"Postoje virmani sa identifikatorom \""+cKey+"\" !\n Želite li ih obrisati ?",
            "Upozorenje !", JOptionPane.YES_NO_OPTION)==0) {
          delete=true;
          create = true;
        }
      } else {
        create = true;
      }
      procStart();
    } else {
      JOptionPane.showConfirmDialog(null,"Obra\u010Dun nije izvršen !", "Greška !", JOptionPane.DEFAULT_OPTION);
    }
  }

  private boolean imaVirmana() {
    QueryDataSet q = Virmani.getDataModule().getFilteredDataSet("ckey='"+cKey+"'");
    q.open();
    if(q.getRowCount()>0)
      return true;
    return false;
  }

  private String getCKey() {
    //QueryDataSet q = Orgpl.getDataModule().getFilteredDataSet("corg="+ OrgStr.getKNJCORG());
    QueryDataSet q = Orgpl.getDataModule().getFilteredDataSet(Condition.equal("CORG", OrgStr.getKNJCORG()));
    q.open();
    q.first();
    rbrOdb = q.getShort("RBROBR");
    return q.getString("CORG")+"-"+q.getShort("GODOBR")+"-"+q.getShort("MJOBR")+"-"+q.getShort("RBROBR");
  }

  private void delVir() {
/* ????????????????????????????    
    Valid.getValid().runSQL("delete from virmani where app='"+frmVirmani.getInstance().app+"' and ckey='"+cKey+"'");
    frmVirmani.getInstance().getRaQueryDataSet().refresh();
 */
    Valid.getValid().runSQL("delete from virmani where app='"+this.app+"' and ckey='"+cKey+"'");
    this.getRaQueryDataSet().refresh();

  }

  public static String getCVRODB(int cPov) {
    getPvrodbcorg();
    String temp="";
    QueryDataSet qds = new QueryDataSet();
    
    String qStr = "select distinct cvrodb from vrsteodb where cpov ="+cPov;

    qds.setQuery(new QueryDescriptor(dM.getDataModule().getDatabase1(),qStr));
    qds.open();
    for (qds.first(); qds.inBounds(); qds.next()) {
      // ako ima za to knjigovodstvo i taj odbitak slog pvrodbcorg 
      // koji upucuje na nekog drugog povjerioca, nemoj dodati u in string
      // nego u petlji po pvrodbcorg
      if (!lookupData.getlookupData().raLocate(pvrodbcorg, "CVRODB", qds.getShort("CVRODB")+"")) {
        temp+=qds.getShort("CVRODB")+",";
      }
    }
    // tu dodati pvrodbcorg
    for (pvrodbcorg.first(); pvrodbcorg.inBounds(); pvrodbcorg.next()) {
      if (pvrodbcorg.getInt("CPOV") == cPov) {
        temp+=pvrodbcorg.getShort("CVRODB")+",";
      }
    }
    return new VarStr(temp).chop().toString();
  }
  private static void getPvrodbcorg() {
    if (pvrodbcorg!=null) return;
    OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
      public void knjigChanged(String oldKnjig, String newKnjig) {
        pvrodbcorg = null;
        getPvrodbcorg();
      }
    });
    pvrodbcorg = hr.restart.baza.PVROdbCorg.getDataModule().getFilteredDataSet(Condition.equal("CORG",OrgStr.getKNJCORG()));
    pvrodbcorg.open();
  }
  private void createVir() {
    String inCvrodb="";
    String qStr="";

    QueryDataSet sumeDS = new QueryDataSet();
    worker.povDS= Povjerioci.getDataModule().copyDataSet();//getFilteredDataSet("'CPOV'='CPOV'");
    naTerDS = Orgstruktura.getDataModule().getFilteredDataSet(Condition.equal("CORG",OrgStr.getKNJCORG()));
    getPvrodbcorg();
    worker.povDS.open();
    worker.povDS.first();
    while(worker.povDS.inBounds()) {
      qStr ="";
      inCvrodb = getCVRODB(worker.povDS.getInt("CPOV"));
      if(worker.povDS.getString("NACISP").equals("2")) {
        if(!inCvrodb.equals("")) {
          qStr = "select sum(odbiciobr.obriznos) as obriznos,max(odbiciobr.cvrodb) as cvrodb, max(radnici.cradnik)as cradnik, max('2')as param from "+
                 "odbiciobr,radnici where odbiciobr.cradnik = radnici.cradnik and radnici.corg in "+getKnj()+" "+
                 "and odbiciobr.cvrodb in("+inCvrodb+") group by odbiciobr.cvrodb, radnici.cradnik";
          sumeDS.close();
          sumeDS.setQuery(new QueryDescriptor(dm.getDatabase1(),qStr));
          if(!sumeDS.isOpen()) sumeDS.open();
          insertInMain(sumeDS);
        }
      } else if (worker.povDS.getString("NACISP").equals("3")) {//pojedinaèno
        if(!inCvrodb.equals("")) {
          qStr = "select sum(odbiciobr.obriznos) as obriznos, max(odbiciobr.cvrodb) as cvrodb, " + 
          "max(radnici.cradnik) as cradnik, max('3')as param, odbiciobr.rbrodb as rbrodb from "+
                 "odbiciobr,radnici where odbiciobr.cradnik = radnici.cradnik and radnici.corg in "+getKnj()+" "+
                 "and odbiciobr.cvrodb in("+inCvrodb+") group by odbiciobr.cradnik, odbiciobr.rbrodb";
          sumeDS.close();
          sumeDS.setQuery(new QueryDescriptor(dm.getDatabase1(),qStr));
          if(!sumeDS.isOpen()) sumeDS.open();
          insertInMain(sumeDS);
        }
      }
      worker.povDS.next();
    }
  }

  private void insertInMain(QueryDataSet sume) {
    raIniciranje.getInstance().posOrgsPl(OrgStr.getOrgStr().getKNJCORG());
    Timestamp datTS = dm.getOrgpl().getTimestamp("DATUMISPL");

    if(!naTerDS.isOpen())naTerDS.open();

    naTerDS.first();
    sume.open();
    sume.first();
    while(sume.inBounds()) {
      worker.strVrOdb = sume.getShort("CVRODB")+"";
      worker.strPov = worker.povDS.getInt("CPOV")+"";
      worker.strRad = sume.getString("CRADNIK");
      worker.strRbrOdb = (sume.hasColumn("RBRODB")==null)?"":sume.getShort("RBRODB")+"";
      mainDS.insertRow(false);
      if (isNewVir()) mainDS.setString("JEDZAV", "NNDNN");
      else mainDS.setString("JEDZAV", naTerDS.getString("ZIRO").substring(0,naTerDS.getString("ZIRO").indexOf("-")));
//      mainDS.setString("NATERET", naTerDS.getString("NAZIV")+"\n"+naTerDS.getString("ADRESA")+
//                       ", "+naTerDS.getString("HPBROJ")+" "+naTerDS.getString("MJESTO"));
      String nazT = handleEmptyStr(naTerDS.getString("NAZIV"),0);
      String adrT = handleEmptyStr(naTerDS.getString("ADRESA"),1);
      String mjT = handleEmptyStr(naTerDS.getString("MJESTO"),0);
      String pbrT = handleEmptyStr(naTerDS.getString("HPBROJ") ,0);
      mainDS.setString("NATERET", nazT + "\n"+adrT+pbrT+mjT);
      mainDS.setString("SVRHA", replaceVars(worker.povDS.getString("SVRHA")));
      mainDS.setString("PNBZ1", worker.povDS.getString("PNBZ1"));
      mainDS.setString("PNBZ2", replaceVars(worker.povDS.getString("PNBZ2")));

      if(worker.povDS.getString("NACISP").equals("2")) {
        mainDS.setString("PNBO1", worker.povDS.getString("PNBO1"));
        mainDS.setString("PNBO2", replaceVars(worker.povDS.getString("PNBO2")));
      } else {
        short cvrodb = sume.getShort("CVRODB");
        String pnb2 = getPNBOPoj(2,cvrodb);//uvijek ""
        if(pnb2.equals("")) {
          mainDS.setString("PNBO1", worker.povDS.getString("PNBO1"));
          mainDS.setString("PNBO2", replaceVars(worker.povDS.getString("PNBO2")));
        } else {
          mainDS.setString("PNBO1", getPNBOPoj(1,cvrodb));
          mainDS.setString("PNBO2", replaceVars(pnb2));
        }
      }
      mainDS.setString("BRRACUK", getAppr(replaceVars(worker.povDS.getString("ZIRO")),"BRRACUK"));
      mainDS.setString("BRRACNT", naTerDS.getString("ZIRO"));
//      mainDS.setString("UKORIST", replaceVars(povDS.getString("NAZPOV")+"\n"+povDS.getString("ADRESA")+
//                       ", "+povDS.getInt("PBR")+" "+povDS.getString("MJESTO")));
      String nazPov = handleEmptyStr(worker.povDS.getString("NAZPOV"),0);
      String adr = handleEmptyStr(worker.povDS.getString("ADRESA"),1);
      String mj = handleEmptyStr(worker.povDS.getString("MJESTO"),0);
      String pbr = handleEmptyStr(worker.povDS.getInt("PBR") + "",0);
      mainDS.setString("UKORIST", replaceVars(nazPov+"\n"+adr+pbr+" "+mj));
      mainDS.setString("SIF1", (worker.povDS.getString("SIF1")+"   ").substring(0,2));
      mainDS.setString("SIF2", (worker.povDS.getString("SIF2")+"   ").substring(0,2));
      mainDS.setString("SIF3", (worker.povDS.getString("SIF3")+"   ").substring(0,2));
      mainDS.setString("MJESTO", naTerDS.getString("MJESTO"));
      mainDS.setBigDecimal("IZNOS", sume.getBigDecimal("OBRIZNOS"));
      mainDS.setTimestamp("DATUMIZV", datTS);
      mainDS.setTimestamp("DATUMPR", Valid.getValid().getToday());
      mainDS.setString("PARAM", worker.povDS.getString("NACISP"));
      mainDS.post();
      sume.next();
    }
  }

  private void initMainDS() {
    if(mainDS.isOpen()) mainDS.close();
    mainDS.setColumns(new Column[]{
      (Column)dm.getVirmani().getColumn("JEDZAV").clone(),
      (Column)dm.getVirmani().getColumn("NATERET").clone(),
      (Column)dm.getVirmani().getColumn("SVRHA").clone(),
      (Column)dm.getVirmani().getColumn("UKORIST").clone(),
      (Column)dm.getVirmani().getColumn("BRRACNT").clone(),
      (Column)dm.getVirmani().getColumn("NACIZV").clone(),
      (Column)dm.getVirmani().getColumn("PNBZ1").clone(),
      (Column)dm.getVirmani().getColumn("PNBZ2").clone(),
      (Column)dm.getVirmani().getColumn("SIF1").clone(),
      (Column)dm.getVirmani().getColumn("SIF2").clone(),
      (Column)dm.getVirmani().getColumn("SIF3").clone(),
      (Column)dm.getVirmani().getColumn("BRRACUK").clone(),
      (Column)dm.getVirmani().getColumn("PNBO1").clone(),
      (Column)dm.getVirmani().getColumn("PNBO2").clone(),
      (Column)dm.getVirmani().getColumn("IZNOS").clone(),
      (Column)dm.getVirmani().getColumn("MJESTO").clone(),
      (Column)dm.getVirmani().getColumn("DATUMIZV").clone(),
      (Column)dm.getVirmani().getColumn("DATUMPR").clone(),
      (Column)column1.clone()
    });
    mainDS.open();
  }

  private void initKumulDS() {
      if(kumulDS.isOpen()) kumulDS.close();
      kumulDS.setColumns(new Column[]{
        (Column)dm.getVirmani().getColumn("JEDZAV").clone(),
        (Column)dm.getVirmani().getColumn("NATERET").clone(),
        (Column)dm.getVirmani().getColumn("SVRHA").clone(),
        (Column)dm.getVirmani().getColumn("UKORIST").clone(),
        (Column)dm.getVirmani().getColumn("BRRACNT").clone(),
        (Column)dm.getVirmani().getColumn("NACIZV").clone(),
        (Column)dm.getVirmani().getColumn("PNBZ1").clone(),
        (Column)dm.getVirmani().getColumn("PNBZ2").clone(),
        (Column)dm.getVirmani().getColumn("SIF1").clone(),
        (Column)dm.getVirmani().getColumn("SIF2").clone(),
        (Column)dm.getVirmani().getColumn("SIF3").clone(),
        (Column)dm.getVirmani().getColumn("BRRACUK").clone(),
        (Column)dm.getVirmani().getColumn("PNBO1").clone(),
        (Column)dm.getVirmani().getColumn("PNBO2").clone(),
        (Column)dm.getVirmani().getColumn("IZNOS").clone(),
        (Column)dm.getVirmani().getColumn("MJESTO").clone(),
        (Column)dm.getVirmani().getColumn("DATUMIZV").clone(),
        (Column)dm.getVirmani().getColumn("DATUMPR").clone(),
        (Column)column1.clone()
      });
      kumulDS.open();
  }

  private String getKnj() {
    String cVrati = "(";
    int i=0;
    com.borland.dx.dataset.StorageDataSet tds =  hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(OrgStr.getKNJCORG());
      tds.first();
      do {
        if (i>0) {
          cVrati=cVrati+',';
        }
        i++;
        cVrati=cVrati+"'"+tds.getString("CORG")+"'";
        tds.next();
      } while (tds.inBounds());
      cVrati=cVrati+")";
      return cVrati;
  }

  private void saveCreatedVir() {
    // ???????? frmVirmani.getInstance().setKeys("pl", OrgStr.getKNJCORG(),cKey);
    this.setKeys("pl", OrgStr.getKNJCORG(),cKey);
    kumulDS = kumulMainDS();
    kumulDS.setSort(new SortDescriptor(new String[]{"UKORIST"}));
    kumulDS.first();
    while(kumulDS.inBounds()) {
      // ?????? frmVirmani.getInstance().
      this.add(kumulDS.getString("JEDZAV"),kumulDS.getString("NATERET"),kumulDS.getString("SVRHA"),
                                   kumulDS.getString("UKORIST"),kumulDS.getString("BRRACNT"),kumulDS.getString("NACIZV"),
                                   kumulDS.getString("PNBZ1"),kumulDS.getString("PNBZ2"),kumulDS.getString("SIF1"),
                                   kumulDS.getString("SIF2"),kumulDS.getString("SIF3"),kumulDS.getString("BRRACUK"),
                                   kumulDS.getString("PNBO1"),kumulDS.getString("PNBO2"),kumulDS.getBigDecimal("IZNOS"),
                                   kumulDS.getString("MJESTO"), kumulDS.getTimestamp("DATUMIZV"), kumulDS.getTimestamp("DATUMPR"));
      kumulDS.next();
    }
    // ?????? frmVirmani.getInstance().save();
    this.save();
    repDiskZapUN.setVrstaNalogaUDatoteci("1");
  }

  private String getPNBOPoj(int c, short cvrodb) {
    /* 
    //sto god da je autor htio ovime to je rijeseno kroz mnemonike
    String qStr="";
    if(c==1)
      qStr = " select distinct pnb1 as pnb from odbici where cvrodb = "+cvrodb;
    else
      qStr = " select distinct pnb2 as pnb from odbici where cvrodb = "+cvrodb;
    QueryDataSet q = new QueryDataSet();
    q.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    q.open();
    if(q.getRowCount()>0)
      return q.getString("pnb");
    */
    return "";
  }

  public void show() {
    delete = false;
    create = false;
    ckey=getCKey();
    checkObracun();
//    super.show();
  }

  private void procStart() {
    Thread th = new Thread() {
      public void run() {
        mainDS.empty();
        if(delete) delVir();
        if(create) {
          proc = raDelayWindow.show(frmVirmaniPl.this.getWindow()).setModal(true);
          createVir();
          saveCreatedVir();
          proc.close();
        }
        frmVirmaniPl.super.show();
      }
    };
    th.start();
  }

  private QueryDataSet kumulMainDS() {
    kumulDS.empty();
    mainDS.setSort(new SortDescriptor(new String[]{"BRRACUK", "PNBO1", "PNBO2"}));
    String brRacUk ="";
    String pnb="";
    mainDS.first();
    while(mainDS.inBounds()) {
      if(!brRacUk.equals(mainDS.getString("BRRACUK")) || !(pnb.equals(mainDS.getString("PNBO1").concat(mainDS.getString("PNBO2"))))
         || mainDS.getString("PARAM").equals("3")) {
        kumulDS.insertRow(false);
        brRacUk = mainDS.getString("BRRACUK");
        pnb = mainDS.getString("PNBO1").concat(mainDS.getString("PNBO2"));
      }
      kumulDS.setString("JEDZAV", mainDS.getString("JEDZAV"));
      kumulDS.setString("NATERET", mainDS.getString("NATERET"));
      kumulDS.setString("SVRHA", mainDS.getString("SVRHA"));
      kumulDS.setString("PNBZ1", mainDS.getString("PNBZ1"));
      kumulDS.setString("PNBZ2", mainDS.getString("PNBZ2"));
      kumulDS.setString("PNBO1", mainDS.getString("PNBO1"));
      kumulDS.setString("PNBO2", mainDS.getString("PNBO2"));
      kumulDS.setString("BRRACUK", mainDS.getString("BRRACUK"));
      kumulDS.setString("BRRACNT", mainDS.getString("BRRACNT"));
      kumulDS.setString("UKORIST", mainDS.getString("UKORIST"));
      kumulDS.setString("SIF1", mainDS.getString("SIF1"));
      kumulDS.setString("SIF2", mainDS.getString("SIF2"));
      kumulDS.setString("SIF3", mainDS.getString("SIF3"));
      kumulDS.setString("MJESTO", mainDS.getString("MJESTO"));
      kumulDS.setBigDecimal("IZNOS", kumulDS.getBigDecimal("IZNOS").add(mainDS.getBigDecimal("IZNOS")));
      kumulDS.setTimestamp("DATUMIZV", mainDS.getTimestamp("DATUMIZV"));
      kumulDS.setTimestamp("DATUMPR", mainDS.getTimestamp("DATUMIZV"));
      mainDS.next();
    }
    kumulDS.post();
    return kumulDS;
  }
  public String replaceVars(String text) {
    return raMnemonics.replaceMnemonics(text,worker.getID(),raMnemonics.DEFAULTMNMODE);
  }

//-> raMnemonics odavdje pa nadanje
/*
  void addVars() {
    vars.clear();
    vars.add(vCOpc);
    vars.add(vMjObr);
    vars.add(vGodObr);
    vars.add(vStopa);
    vars.add(vOsn);
    vars.add(vRsInd);
    vars.add(vRegBrMIO);
    vars.add(vRegBrZO);
    vars.add(vMatBr);
    vars.add(vDatIsp);
    vars.add(vJMBG);
    vars.add(vIme);
    vars.add(vPrez);
    vars.add(vMjIsp);
    vars.add(vGodIsp);
    vars.add(vNazPov);
    vars.add(vOpcZiro);
    vars.add(vOpcMjesto);
  }
//-> raMnemonics iskoristiti raMnemonics.replaceMnemonics
  public String replaceVars(String text) {
    String text2 = text;
    int idx = 0;
    String s1="";
    String s2="";
    String s3="";
    for (Iterator i = vars.iterator(); i.hasNext(); ) {
      raVirVar item = (raVirVar)i.next();
      String var = item.getVar();
      idx = text.indexOf(var);
      if(idx > -1) {
        s1=text2.substring(0,idx);
        s2 = item.getReplaceStr();
        s3 = text2.substring(idx+var.length(), text.length());
        text2 = s1+s2+s3;
        text = text2;
      }
    }
    return text2.toString();
  }

  raVirVar vMatBr = new raVirVar("$matbr","Matièni broj poduzeæa") {
    public String getReplaceStr() {
      ld.raLocate(dm.getLogotipovi(),new String[] {"CORG"},new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
      return dm.getLogotipovi().getString("MATBROJ");
    }
  };

  raVirVar vCOpc = new raVirVar("$cop","Oznaka opæine") {
    public String getReplaceStr() {
      String ckeySif="";
      String cKeyVal="";
      ld.raLocate(dm.getVrsteodb(),new String[] {"CVRODB", "CPOV"},new String[] {strVrOdb, strPov});
      String nivoOdb = dm.getVrsteodb().getString("NIVOODB");
      if(nivoOdb.length()==2 && nivoOdb.equals("OP"))
        ckeySif = "CKEY";
      else if(nivoOdb.length()==4) {
        if(nivoOdb.substring(0,2).equals("OP"))
          ckeySif = "CKEY";
        else if(nivoOdb.substring(2,4).equals("OP"))
          ckeySif = "CKEY2";
        else
          return "";
      } else  return "";
      ld.raLocate(dm.getOdbiciobr(), new String[]{"CVRODB", "CRADNIK"}, new String[]{strVrOdb, strRad});
      return dm.getOdbiciobr().getString(ckeySif);
    }
  };

  raVirVar vMjObr = new raVirVar("$mo","Mjesec obrade") {
    public String getReplaceStr() {
     ld.raLocate(dm.getOrgpl(),new String[] {"CORG"},new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
     return dm.getOrgpl().getShort("MJOBR") + "";
    }
  };

  raVirVar vGodObr = new raVirVar("$godo","Godina obrade") {
    public String getReplaceStr() {
     ld.raLocate(dm.getOrgpl(),new String[] {"CORG"},new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
     return dm.getOrgpl().getShort("GODOBR") + "";
    }
  };

  raVirVar vStopa = new raVirVar("$stopaobr","Stopa odbitka") {
    public String getReplaceStr() {
      ld.raLocate(dm.getOdbiciobr(), new String[]{"CVRODB", "CRADNIK"}, new String[]{strVrOdb, strRad});
      return dm.getOdbiciobr().getBigDecimal("OBRSTOPA").toString();
    }
  };

  raVirVar vOsn = new raVirVar("$osnobr","Osnovica za odbitak") {
    public String getReplaceStr() {
      ld.raLocate(dm.getOdbiciobr(), new String[]{"CVRODB", "CRADNIK"}, new String[]{strVrOdb, strRad});
      return dm.getOdbiciobr().getBigDecimal("OBROSN").toString();
    }
  };

  raVirVar vRsInd = new raVirVar("$rsind","Identifikator RS") {
    public String getReplaceStr() {
     ld.raLocate(dm.getOrgpl(),new String[] {"CORG"},new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
     return dm.getOrgpl().getString("RSIND");
    }
  };

  raVirVar vNazPov = new raVirVar("$nazpov", "Naziv povjerioca") {
    public String getReplaceStr(){
      String cPov = worker.povDS.getInt("CPOV") + "";
      ld.raLocate(dm.getPovjerioci(), new String[]{"CPOV"}, new String[]{cPov});
      return dm.getPovjerioci().getString("NAZPOV");
    }
  };

  raVirVar vRegBrMIO = new raVirVar("$regbrmio","Reg. br. MIO") {
    public String getReplaceStr() {
    ld.raLocate(dm.getOrgpl(),new String[] {"CORG"},new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
    return dm.getOrgpl().getString("REGBRMIO");
    }
  };

  raVirVar vRegBrZO = new raVirVar("$regbrzo","Reg. br. ZO") {
    public String getReplaceStr() {
     ld.raLocate(dm.getOrgpl(),new String[] {"CORG"},new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
     return dm.getOrgpl().getString("REGBRZO");
    }
  };

  raVirVar vDatIsp = new raVirVar("$datisp","Datum isplate") {
    public String getReplaceStr() {
      ld.raLocate(dm.getOrgpl(),new String[] {"CORG"},new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
      return DateFormat(dm.getOrgpl().getTimestamp("DATUMISPL"));
    }
  };

  raVirVar vJMBG = new raVirVar("$jmbg","JMBG radnika") {
    public String getReplaceStr() {
      ld.raLocate(dm.getRadnicipl(), new String[]{"CRADNIK"}, new String[]{strRad});
      return dm.getRadnicipl().getString("JMBG");
    }
  };

  raVirVar vIme = new raVirVar("$ime","Ime radnika") {
    public String getReplaceStr() {
      ld.raLocate(dm.getRadnici(), new String[]{"CRADNIK"}, new String[]{strRad});
      return dm.getRadnici().getString("IME");
    }
  };

  raVirVar vPrez = new raVirVar("$prez","Prezime radnika") {
    public String getReplaceStr() {
      ld.raLocate(dm.getRadnici(), new String[]{"CRADNIK"}, new String[]{strRad});
      return dm.getRadnici().getString("PREZIME");
    }
  };

  raVirVar vMjIsp = new raVirVar("$mi","Mjesec isplate") {
    public String getReplaceStr() {
      ld.raLocate(dm.getOrgpl(),new String[] {"CORG"},new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
      return DateFormat(dm.getOrgpl().getTimestamp("DATUMISPL")).substring(3,5);
    }
  };

  raVirVar vGodIsp = new raVirVar("$godi","Godina isplate") {
    public String getReplaceStr() {
      ld.raLocate(dm.getOrgpl(),new String[] {"CORG"},new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
      return DateFormat(dm.getOrgpl().getTimestamp("DATUMISPL")).substring(6,10);
    }
  };

  //optimizacija za opcinu

  private String lastSelectedCopcine;
  private String lastCradnikForSelectedCopcine;
  private String lastSelectedMjestoOpcine;

  void fillSelectedCopcine() {
    QueryDataSet qds = Util.getNewQueryDataSet("SELECT copcine FROM radnicipl WHERE cradnik = '"+strRad+"'");
    lastSelectedCopcine = qds.getString("COPCINE");
    lastCradnikForSelectedCopcine = strRad;
  }

  raVirVar vOpcZiro = new raVirVar("$opz","Žiro opæine") {
    public String getReplaceStr() {
      if (!(lastCradnikForSelectedCopcine!=null && lastCradnikForSelectedCopcine.equals(strRad))) {
        fillSelectedCopcine();
      }
      return getZiroOpc(lastSelectedCopcine);
    }
  };

  raVirVar vOpcMjesto = new raVirVar("$opmj","Mjesto opæine") {
    public String getReplaceStr() {
      if (!(lastCradnikForSelectedCopcine!=null && lastCradnikForSelectedCopcine.equals(strRad))) {
        fillSelectedCopcine();
      }
      return locateOpcine();
    }
    String locateOpcine() {
      ld.raLocate(dm.getOpcine(),"COPCINE",lastSelectedCopcine);
      return dm.getOpcine().getString("NAZIVOP");
    }
  };

  private String DateFormat(Timestamp _date) {
    String tempDate = _date.toString();
    String year =  tempDate.substring(0,4);
    String month = tempDate.substring(5,7);
    String day = tempDate.substring(8,10);
    return day+"."+month+"."+year+".";
  }

  private String handleEmptyStr(String str, int i) {
    if(str.equals("-") || str.equals(".") || str.equals("0"))
      return"";
    if(i==1)
      return str+", ";
    return str;
  }
*/
/*
NN 1/2003: Raèuni se sastoje od dva dijela koji su meðusobno odvojeni: vodeæeg broja banke i
broja raèuna u banci. Vodeæi broj banke je 1001005 -
Hrvatska narodna banka. Drugi dio raèuna se sastoji od deset (10) znamenaka od kojih prve dvije znamenke
imaju oznaku "17" - oznaka raèuna za naplatu javnih prihoda, iduæe tri znamenke su oznaka grada/opæine iz
dosadašnjeg sadržaja raèuna, slijede èetiri znamenke - oznaka dosadašnje individualne partije raèuna,
posljednja znamenka je kontrolni broj prethodnih devet izraèunana po meðunarodnoj normi ISO 7064 Modul 11, 10.
*/
/*
  public static String getZiroOpc(String copc) {
    int HNBVB=1001005; //Vodeci broj HNB
    int RN1=17000; //broj kom se doda copc i konkatinira RN2
    int RN2=1200; //concata se na RN1+copc i racuna kontrolni broj
    int CB;//kontrolni broj
    int RN = Integer.parseInt((RN1+Integer.parseInt(copc))+""+RN2);
    String zr = (HNBVB+"-"+RN).concat(getMod11(RN+"")+"");
    return zr;
  }

  static boolean trace = false;

  public static int getMod11(String br) {
    int rez=0;
    char[] chrs = br.toCharArray();
    int resto = 10;
    for (int i = 0; i < chrs.length; i++) {
      int digit = Integer.parseInt(String.valueOf(chrs[i]));
      int s1 = resto+digit;
      if (trace) System.out.print((i+1)+".  "+resto+"+"+digit+"="+s1);
      if (s1>10) s1=s1-10;
      if (trace) System.out.print("  ==> "+s1);
      s1 = s1*2;
      if (trace) System.out.print(" x 2 = "+s1);
      if (s1>11) resto = s1-11;
//      else if (s1 == 0) resto = 1;
      else resto = s1;
      if (trace) System.out.println(" ==> "+resto);
    }
    if (trace) System.out.println("resto = "+resto);
    if (resto==0)
      rez = 1;
    else if (resto==1)
      rez = 0;
    else
      rez = 11-resto;
    if (trace) System.out.println("rez....."+rez);
    return rez;
  }
*/
  public static void main(String[] args) {
    String copc="";
//    frmVirmaniPl.worker.trace=true;
    while (copc!=null) {
      copc = JOptionPane.showInputDialog(null,"Opcina");
      if (copc != null) JOptionPane.showMessageDialog(null,raVirPlMnWorker.getZiroOpc(copc));
    };
  }
}