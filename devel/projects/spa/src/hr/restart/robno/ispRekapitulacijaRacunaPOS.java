/****license*****************************************************************
**   file: ispRekapitulacijaRacunaPOS.java
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
package hr.restart.robno;

import hr.restart.baza.Condition;
import hr.restart.baza.Sklad;
import hr.restart.baza.Smjene;
import hr.restart.baza.dM;
import hr.restart.pos.presBlag;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raButtonGroup;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raUpitLite;
import hr.restart.util.sysoutTEST;

import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class ispRekapitulacijaRacunaPOS extends raUpitLite {
  static ispRekapitulacijaRacunaPOS instanceOfMe;

//  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  dM dm = hr.restart.baza.dM.getDataModule();
  lookupData ld = lookupData.getlookupData();

  java.sql.Timestamp minDat, maxDat;
  int minDok, maxDok;

//  QueryDataSet tempDs;
//  QueryDataSet tempAs;
//  QueryDataSet reportQDS = new QueryDataSet();
  QueryDataSet artikliReportQDS = new QueryDataSet();

  QueryDataSet finalSet = new QueryDataSet();
  QueryDataSet grupeSet = new QueryDataSet();
  QueryDataSet grupePorSet = new QueryDataSet();
  QueryDataSet porezSet = new QueryDataSet();

  JPanel mainPanel = new JPanel();
  XYLayout mainXYLayout = new XYLayout();
  JLabel jlSkladiste = new JLabel();
  JLabel jlBlagajna = new JLabel();
  JLabel jllagajnik = new JLabel();
  JLabel jlSmjena = new JLabel();
  JraCheckBox jcbPoArtiklima = new JraCheckBox();
  JraCheckBox jcbPoSmjenama = new JraCheckBox();
  JraCheckBox jcbPoKonobarima = new JraCheckBox();
  JraTextField jtfPocDatum = new JraTextField();
  JraTextField jtfZavDatum = new JraTextField();
  JraTextField jtfPocBroj = new JraTextField();
  JraTextField jtfZavBroj = new JraTextField();

  JlrNavField jrfNAZ = new JlrNavField();
  JraButton jbSM = new JraButton();
  JlrNavField jrfSM = new JlrNavField();
  
  JlrNavField jrfNAZSKL = new JlrNavField();
  JraButton jbCSKL = new JraButton();
  JlrNavField jrfCSKL = new JlrNavField();
  JlrNavField jrfNAZBLAG = new JlrNavField();
  JraButton jbCBLAG = new JraButton();
  JlrNavField jrfCBLAG = new JlrNavField();
  JlrNavField jrfNazivBlagajnika = new JlrNavField();
  JraButton jbSelBlagajnik = new JraButton();
  JlrNavField jrfCBlagajnik = new JlrNavField();
  TableDataSet tds = new TableDataSet();
  JraRadioButton jrbDatum = new JraRadioButton();
  JraRadioButton jrbRacun = new JraRadioButton();
  raButtonGroup ispisChoozer = new raButtonGroup();
  
  JLabel jlStol = new JLabel();
  JlrNavField jlrStol = new JlrNavField();
  JlrNavField jlrNazStol = new JlrNavField();
  JraButton jbSelStol = new JraButton();
  
  boolean stolovi = false;
  
  public ispRekapitulacijaRacunaPOS() {
    try {
      jbInit();
      instanceOfMe = this;
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static ispRekapitulacijaRacunaPOS getInstance(){
    if (instanceOfMe == null) instanceOfMe = new ispRekapitulacijaRacunaPOS();
    return instanceOfMe;
  }

  public void componentShow() {
    tds.open();
    tds.setTimestamp("pocDatum", vl.getToday());
    tds.setTimestamp("zavDatum", vl.getToday());
    tds.setInt("POCBROJ",0);
    tds.setInt("ZAVBROJ",0);
    ispisChoozer.setSelected(jrbDatum);
    jrfCBLAG.setText("");
    jrfNAZBLAG.setText("");
    jrfCBlagajnik.setText("");
    jrfNazivBlagajnika.setText("");
    jcbPoArtiklima.setSelected(true);
    jcbPoSmjenama.setSelected(true);
    jcbPoKonobarima.setSelected(false);
    if (presBlag.isUserOriented() && !raUser.getInstance().isSuper()) {
      jrfCBlagajnik.setText(raUser.getInstance().getUser());
      jrfCBlagajnik.forceFocLost();
      rcc.setLabelLaF(jrfCBlagajnik,false);
      rcc.setLabelLaF(jrfNazivBlagajnika,false);
      rcc.setLabelLaF(jbSelBlagajnik,false);
    } else {
      rcc.setLabelLaF(jrfCBlagajnik,true);
      rcc.setLabelLaF(jrfNazivBlagajnika,true);
      rcc.setLabelLaF(jbSelBlagajnik,true);
    }
    tds.setString("IspisRek","D");
    tds.setString("IspisSmje","D");
    tds.setString("IspisKon","N");
    //findMpSkl();
    findSklad();
    jrfCBLAG.requestFocus();
  }

  public boolean isIspis(){
    return false;
  }

  public boolean ispisNow(){
    return true;
  }

  public boolean Validacija(){
    if (vl.isEmpty(jrfCSKL)) return false;
    //if (vl.isEmpty(jrfCBLAG)) return false;
    if (jrbRacun.isSelected() && (tds.getInt("POCBROJ") == tds.getInt("ZAVBROJ") && tds.getInt("ZAVBROJ") == 0) ||
        tds.getInt("POCBROJ") > tds.getInt("ZAVBROJ")) {
      jtfPocBroj.requestFocus();
//      System.out.println("Neispravan range ili oba nula");
      javax.swing.JOptionPane.showMessageDialog(jtfPocBroj,"Sljed raèuna nije ispravan","Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
      return false;
    } else {
//      System.out.println(jtfPocDatum + " - " + jtfZavDatum);
      if (!vl.isValidRange(jtfPocDatum,jtfZavDatum))return false;
    }
    return true;
  }

  private boolean isOk = false;
  
  public BigDecimal izpov = Aus.zero0;

  public void okPress() {
//    System.out.println("CSKL        - " + tds.getString("CSKL"));
//    System.out.println("CPRODMJ     - " + tds.getString("CPRODMJ"));
//    System.out.println("CBLAGAJNIK  - " + tds.getString("CBLAGAJNIK"));
//    System.out.println("POC DATUM   - " + tds.getTimestamp("POCDATUM"));
//    System.out.println("ZAV DATUM   - " + tds.getTimestamp("ZAVDATUM"));
//    System.out.println("POC BROJ    - " + tds.getInt("POCBROJ"));
//    System.out.println("ZAV BROJ    - " + tds.getInt("ZAVBROJ"));
//    System.out.println("??????      - " + tds.getString("ISPISREK"));
//    System.out.println("Datum range - " + jrbDatum.isSelected());
//    System.out.println("Broj range  - " + jrbRacun.isSelected());
//    sysoutTEST syst = new sysoutTEST(false);


    this.killAllReports();
//    /*if (!jcbPoArtiklima.isSelected()) */this.addReport("hr.restart.robno.repIspPOS_Total", "hr.restart.robno.repIspPOS_Total", "RekapitulacijaUplata",  "Ispis stanja - vrijednosni");
//    else this.addReport("hr.restart.robno.repIspPOS_Total_Kolicinski", "Ispis stanja - koli\u010Dinski", 2);
    this.addReport("hr.restart.pos.repRekapitulacijaPOS", "Ispis stanja - matri\u010Dni");

    boolean isPdv = frmParam.getParam("pos", "pdvRekap", "N",
        "Ispis poreza na rekapitulaciji uplata (D,N)").equalsIgnoreCase("D");

    porezSet.open();
    porezSet.empty();
    
    izpov = Aus.zero2;
    
    if (isPdv) {
      String porezString = 
      "SELECT pos.cnacpl, stpos.cart, stpos.kol, stpos.neto, stpos.por1, stpos.por2, stpos.por3 " +
      "FROM pos, Stpos "+
      "WHERE pos.cskl = stpos.cskl "+
      "AND pos.vrdok = stpos.vrdok "+
      "AND pos.god = stpos.god "+
      "AND pos.brdok = stpos.brdok "+
      "AND pos.cprodmj = stpos.cprodmj "+
      "and pos.vrdok = 'GRC' "+ kondishnBlag() +
      "and pos.cskl = '"+tds.getString("CSKL")+"' "+
      "and "+kondishnDatumOrBroj()+kondishnOperater();
      
      BigDecimal pov = Aus.getDecNumber(frmParam.getParam("robno", "iznosPov", "0.5",
        "Iznos povratne naknade"));
      
      
      
      System.out.println(porezString);
      QueryDataSet qdsp = Aus.q(porezString);
      qdsp.open();
      for (qdsp.first(); qdsp.inBounds(); qdsp.next()) {
        
        ld.raLocate(dm.getArtikli(), new String[]{"CART"}, new String[]{String.valueOf(qdsp.getInt("CART"))});
        ld.raLocate(dm.getPorezi(), new String[]{"CPOR"}, new String[]{dm.getArtikli().getString("CPOR")});
        
        
        for (int i = 1; i <= 3; i++) {
          if (qdsp.getBigDecimal("POR" + i).signum() != 0) {
            String naz = dm.getPorezi().getString("NAZPOR" + i);
            
            if (!ld.raLocate(porezSet, new String[] {"CNACPL", "NAZIV"},
                new String[] {qdsp.getString("CNACPL"), naz})) {
              porezSet.insertRow(false);
              porezSet.setString("CNACPL", qdsp.getString("CNACPL"));
              porezSet.setString("NAZIV", naz);
            }
            Aus.add(porezSet, "PROMET", qdsp, "NETO");
            porezSet.setBigDecimal("STOPA", dm.getPorezi().getBigDecimal("POR" + i));
            Aus.add(porezSet, "IZNOS", qdsp, "POR" + i);
            Aus.add(porezSet, "OSNOVICA", qdsp, "NETO");
            Aus.sub(porezSet, "OSNOVICA", qdsp, "POR1");
            Aus.sub(porezSet, "OSNOVICA", qdsp, "POR2");
            Aus.sub(porezSet, "OSNOVICA", qdsp, "POR3");
            if ("D".equals(dm.getArtikli().getString("POV"))) {
              BigDecimal ip = pov.multiply(qdsp.getBigDecimal("KOL")).
                  setScale(2, BigDecimal.ROUND_HALF_UP);
              Aus.sub(porezSet, "OSNOVICA", ip);
              izpov = izpov.add(ip);
            }
          }
        }
        
        /*if (qdsp.getBigDecimal("POR1").signum() != 0) {
          if (!ld.raLocate(porezSet, new String[] {"CNACPL", "NAZIV"},
              new String[] {qdsp.getString("CNACPL"), "PDV"})) {
            porezSet.insertRow(false);
            porezSet.setString("CNACPL", qdsp.getString("CNACPL"));
            porezSet.setString("NAZIV", "PDV");
          }
          Aus.add(porezSet, "PROMET", qdsp, "NETO");
          porezSet.setBigDecimal("STOPA", new BigDecimal(23));
          Aus.add(porezSet, "IZNOS", qdsp, "POR1");
          Aus.add(porezSet, "OSNOVICA", qdsp, "NETO");
          Aus.sub(porezSet, "OSNOVICA", qdsp, "POR1");
          Aus.sub(porezSet, "OSNOVICA", qdsp, "POR2");
        }
        if (qdsp.getBigDecimal("POR2").signum() != 0) {
          if (!ld.raLocate(porezSet, new String[] {"CNACPL", "NAZIV"},
              new String[] {qdsp.getString("CNACPL"), "PNP"})) {
            porezSet.insertRow(false);
            porezSet.setString("CNACPL", qdsp.getString("CNACPL"));
            porezSet.setString("NAZIV", "PNP");
          }
          Aus.add(porezSet, "PROMET", qdsp, "NETO");
          porezSet.setBigDecimal("STOPA", new BigDecimal(3));
          Aus.add(porezSet, "IZNOS", qdsp, "POR2");
          Aus.add(porezSet, "OSNOVICA", qdsp, "NETO");
          Aus.sub(porezSet, "OSNOVICA", qdsp, "POR1");
          Aus.sub(porezSet, "OSNOVICA", qdsp, "POR2");
        }*/
      }
    }
    
//    System.out.println("\n"+Condition.between("DATDOK",tds.getTimestamp("POCDATUM"),tds.getTimestamp("ZAVDATUM")).toString()+"\n");
//    System.out.println(Condition.between("BRDOK",tds.getInt("POCBROJ"),tds.getInt("ZAVBROJ")).toString()+"\n");

    String upitString = "SELECT (RATE.irata) as irata, (RATE.cbanka) as cbanaka, (RATE.cnacpl) as cnacpl "+
                        /*"(pos.brdok) as mindok, (pos.brdok) as maxdok, (pos.datdok) as mindat, (pos.datdok) as maxdat "+*/
                        "FROM pos, rate "+
                        "WHERE pos.cskl = rate.cskl "+
                        "AND pos.vrdok = rate.vrdok "+
                        "AND pos.god = rate.god "+
                        "AND pos.brdok = rate.brdok "+
                        "AND pos.cprodmj = rate.cprodmj "+
                        "and pos.vrdok = 'GRC' "+kondishnBlag()+
                        "and pos.cskl = '"+tds.getString("CSKL")+"' "+
                        "and "+kondishnDatumOrBroj()+ kondishnOperater()/*+*/ // kondishnOperator diprektid...
                        /*" group by cnacpl"*/;

    System.out.println("\n"+upitString);

    QueryDataSet qds = getSumQdsPoCnacpl(ut.getNewQueryDataSet(upitString));
    
//    System.out.println("qds");
//    syst.prn(qds);
    if (qds.isEmpty()) setNoDataAndReturnImmediately();
    
    isOk = true;

    String upitString2 = "SELECT min(pos.brdok) as mindok, max(pos.brdok) as maxdok, min(pos.datdok) as mindat, max(pos.datdok) as maxdat "+
                         "FROM pos, rate "+
                         "WHERE pos.cskl = rate.cskl "+
                         "AND pos.vrdok = rate.vrdok "+
                         "AND pos.god = rate.god "+
                         "AND pos.brdok = rate.brdok "+
                         "AND pos.cprodmj = rate.cprodmj "+
                         "and pos.vrdok = 'GRC' "+ kondishnBlag() +
                         "and pos.cskl = '"+tds.getString("CSKL")+"' "+
                         "and "+kondishnDatumOrBroj()+ kondishnOperater();

    QueryDataSet qds2 = ut.getNewQueryDataSet(upitString2);
    
//    System.out.println("qds2");
//    syst.prn(qds2);

    minDat = qds2.getTimestamp("MINDAT");
    maxDat = qds2.getTimestamp("MAXDAT");
    minDok = qds2.getInt("MINDOK");
    maxDok = qds2.getInt("MAXDOK");



    String artikliString = "SELECT Stpos.cart, max(Stpos.cart1) as cart1, max(Stpos.bc) as bc, Stpos.nazart, sum(Stpos.kol) as kol, sum(stpos.neto) as neto FROM pos, Stpos "+
                           "WHERE pos.cskl = stpos.cskl "+
                           "AND pos.vrdok = stpos.vrdok "+
                           "AND pos.god = stpos.god "+
                           "AND pos.brdok = stpos.brdok "+
                           "AND pos.cprodmj = stpos.cprodmj "+
                           "and stpos.kol >= 0 "+
                           "and pos.vrdok = 'GRC' "+kondishnBlag()+
                           "and pos.cskl = '"+tds.getString("CSKL")+"' "+
                           "and "+kondishnDatumOrBroj()+kondishnOperater()+ // kondishnOperator diprektid...
                           "group by cart, nazart UNION ALL " + 
                           "SELECT Stpos.cart, max(Stpos.cart1) as cart1, max(Stpos.bc) as bc, Stpos.nazart, sum(Stpos.kol) as kol, sum(stpos.neto) as neto FROM pos, Stpos "+
                           "WHERE pos.cskl = stpos.cskl "+
                           "AND pos.vrdok = stpos.vrdok "+
                           "AND pos.god = stpos.god "+
                           "AND pos.brdok = stpos.brdok "+
                           "AND pos.cprodmj = stpos.cprodmj "+
                           "and stpos.kol < 0 "+
                           "and pos.vrdok = 'GRC' "+kondishnBlag()+
                           "and pos.cskl = '"+tds.getString("CSKL")+"' "+
                           "and "+kondishnDatumOrBroj()+kondishnOperater()+ // kondishnOperator diprektid...
                           "group by cart, nazart";

//    System.out.println("\n"+artikliString);


    calculateAndPrepare(qds,artikliString);

//    hr.restart.util.sysoutTEST syst = new hr.restart.util.sysoutTEST(false);
//    syst.prn(qds);
  }
  
  private QueryDataSet getSumQdsPoCnacpl(QueryDataSet tmpset){
    QueryDataSet sumSet = new QueryDataSet();
    sumSet.setColumns(tmpset.cloneColumns());
    sumSet.open();
    tmpset.first();
    
    do {
      if (!lookupData.getlookupData().raLocate(sumSet,new String[] {"CNACPL","CBANAKA"},new String[] {tmpset.getString("CNACPL"), tmpset.getString("CBANAKA")})){
        sumSet.insertRow(false);
        tmpset.copyTo(sumSet);
      } else {
        sumSet.setBigDecimal("IRATA",sumSet.getBigDecimal("IRATA").add(tmpset.getBigDecimal("IRATA")));
        sumSet.setString("CNACPL",tmpset.getString("CNACPL"));
        sumSet.setString("CBANAKA",tmpset.getString("CBANAKA"));
      }
    } while (tmpset.next());
    sumSet.close();
    sumSet.setSort(new SortDescriptor(new String[] {"CBANAKA","CNACPL"}));
    sumSet.open();
//    sysoutTEST syst = new sysoutTEST(false);
//    System.out.println("sumSet");
//    syst.prn(sumSet);
   
    return sumSet;
  }
  
  public QueryDataSet getGrupeArt(){
//  sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
//  st.showInFrame(finalSet,"Dataset Test Frame");
    return grupeSet;
  }
  
  public QueryDataSet getGrupePor(){
//  sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
//  st.showInFrame(finalSet,"Dataset Test Frame");
    return grupePorSet;
  }
  
  public QueryDataSet getPorezi() {
    return porezSet;
  }

  public QueryDataSet getWhatSoEver(){
//    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
//    st.showInFrame(finalSet,"Dataset Test Frame");
    return finalSet;
  }

  public QueryDataSet getWhatSoEverArtikli(){
//    sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
//    st.showInFrame(artikliReportQDS,"Dataset Test Frame");
    return artikliReportQDS;
  }

  public boolean getRekapitulacijaPoAretiklima(){
    return jcbPoArtiklima.isSelected();
  }

  public String getPocetniDatum(){
    if (!jrbDatum.isSelected()) return raDateUtil.getraDateUtil().dataFormatter(minDat);
    return raDateUtil.getraDateUtil().dataFormatter(tds.getTimestamp("POCDATUM"));
  }

  public String getZavrsniDatum(){
    if (!jrbDatum.isSelected()) return raDateUtil.getraDateUtil().dataFormatter(maxDat);
    return raDateUtil.getraDateUtil().dataFormatter(tds.getTimestamp("ZAVDATUM"));
  }

  public String getPrviBroj(){
    if (jrbDatum.isSelected()) return tds.getString("CSKL")+"-"+ vl.maskZeroInteger(Integer.decode(minDok+""),6);
    return tds.getString("CSKL")+"-"+ vl.maskZeroInteger(Integer.decode(tds.getInt("POCBROJ")+""),6);
  }

  public String getZadnjiBroj(){
    if (jrbDatum.isSelected()) return tds.getString("CSKL")+"-"+ vl.maskZeroInteger(Integer.decode(maxDok+""),6);
    return tds.getString("CSKL")+"-"+ vl.maskZeroInteger(Integer.decode(tds.getInt("ZAVBROJ")+""),6);
  }

  public boolean datumskiPeriod(){
    return jrbDatum.isSelected();
  }
  
  public String getBlagajnik() {
    return jrfNazivBlagajnika.getText();
  }
  
  public String getCSKL() {
    return tds.getString("CSKL");
  }

  private void calculateAndPrepare(QueryDataSet qst, String artStr){
    qst.first();
    finalSet.open();
    finalSet.empty();
    do {
      finalSet.insertRow(false);
      finalSet.setBigDecimal("IZNOS", qst.getBigDecimal("IRATA"));
      finalSet.setString("CNACPL",qst.getString("CNACPL"));
      if (lookupData.getlookupData().raLocate(dm.getNacpl(),"CNACPL",qst.getString("CNACPL"))){
        finalSet.setString("NACPL",dm.getNacpl().getString("NAZNACPL"));
      }
      finalSet.setString("CBANKA",qst.getString("CBANAKA"));
    } while (qst.next());
    
    finalSet.setSort(new SortDescriptor (new String[] {"CNACPL"}));
    
    
    findRekapGrupe();
    
    findRekapPorezGrupe();
    
//    System.out.println("finalSet");
//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(finalSet);

    if (getRekapitulacijaPoAretiklima()){
//      artikliReportQDS.open();
//      artikliReportQDS.deleteAllRows();
      artikliReportQDS = ut.getNewQueryDataSet(artStr);
      artikliReportQDS.getColumn("NETO").setDisplayMask("###,###,##0.00");
      artikliReportQDS.getColumn("KOL").setDisplayMask("###,###,##0.###");
      
      artikliReportQDS.setRowId("CART",true);
      
      QueryDataSet artikli = ut.getNewQueryDataSet("select cart, cart1, bc from artikli");
      for (artikliReportQDS.first();artikliReportQDS.inBounds();artikliReportQDS.next()){
        if (lookupData.getlookupData().raLocate(artikli,"CART",artikliReportQDS.getInt("CART")+"")){
          artikliReportQDS.setString("CART1",artikli.getString("CART1"));
          artikliReportQDS.setString("BC",artikli.getString("BC"));
        }
      }
    }
  }
  
  private void findRekapGrupe() {
    String grupe = frmParam.getParam("pos", "rekapGrupe", "",
    "Naèin raspodjele utrška po grupama artikla");
    
    grupeSet.open();
    grupeSet.empty();
    if (grupe.length() > 0) try {
      Map topgr = new HashMap();
      String[] parts = new VarStr(grupe).split('|');
      for (int i = 0; i < parts.length; i++) {
        String[] def = new VarStr(parts[i]).splitTrimmed('=');
        topgr.put(def[1], Aus.zero2);
      }
      
      Map allgr = Aut.getAut().getPodgrupe(topgr.keySet());
    
      Map allart = new HashMap();
      DataSet art = dm.getArtikli();
      art.open();
      for (art.first(); art.inBounds(); art.next())
        allart.put(new Integer(art.getInt("CART")),
            allgr.get(art.getString("CGRART")));
      
      String grString = "SELECT stpos.cart, stpos.neto FROM pos, Stpos "+
        "WHERE pos.cskl = stpos.cskl "+
        "AND pos.vrdok = stpos.vrdok "+
        "AND pos.god = stpos.god "+
        "AND pos.brdok = stpos.brdok "+
        "AND pos.cprodmj = stpos.cprodmj "+
        "and pos.vrdok = 'GRC' "+kondishnBlag()+
        "and pos.cskl = '"+tds.getString("CSKL")+"' "+
        "and "+kondishnDatumOrBroj()+kondishnOperater();
      
      DataSet allStav = Aus.q(grString);
      for (allStav.first(); allStav.inBounds(); allStav.next()) {
        String cgr = (String) allart.get(new Integer(allStav.getInt("CART")));
        if (cgr == null) 
          throw new Exception("Nepostojeæi artikl "+allStav.getInt("CART"));
        BigDecimal old = (BigDecimal) topgr.get(cgr);
        if (old == null)
          throw new Exception("Nedefinirana grupa artikala "+cgr);
        topgr.put(cgr, old.add(allStav.getBigDecimal("NETO")));
      }
      
      for (int i = 0; i < parts.length; i++) {
        String[] def = new VarStr(parts[i]).splitTrimmed('=');
        grupeSet.insertRow(false);
        grupeSet.setString("CGRART", def[1]);
        grupeSet.setString("OPIS", def[0]);
        grupeSet.setBigDecimal("IZNOS", (BigDecimal) topgr.get(def[1]));
        grupeSet.post();
      }
    
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private void findRekapPorezGrupe() {
    String grpor = frmParam.getParam("pos", "grupePorez", "",
      "Popis grupa artikala za rekapitulaciju poreza");
  
    grupePorSet.open();
    grupePorSet.empty();
    if (grpor.length() > 0) try {
      List groups = grpor.indexOf(',') < 0 ? new VarStr(grpor).splitAsList()
            : new VarStr(grpor).splitAsListTrimmed(',');
      Set tops = new HashSet(groups);
      
      Map allgr = new HashMap();
      Map grnam = new HashMap();
      DataSet gr = dm.getGrupart();
      gr.open();
      for (gr.first(); gr.inBounds(); gr.next()) {
        allgr.put(gr.getString("CGRART"), gr.getString("CGRARTPRIP"));
        grnam.put(gr.getString("CGRART"), gr.getString("NAZGRART"));
      }
      
      Map allart = new HashMap();
      DataSet art = dm.getArtikli();
      art.open();
      
      for (art.first(); art.inBounds(); art.next())
        allart.put(new Integer(art.getInt("CART")),
            art.getString("CGRART"));
      
      String grpString = "SELECT stpos.cart, stpos.neto, " +
            "stpos.por1, stpos.por2 FROM pos, Stpos "+
            "WHERE pos.cskl = stpos.cskl "+
            "AND pos.vrdok = stpos.vrdok "+
            "AND pos.god = stpos.god "+
            "AND pos.brdok = stpos.brdok "+
            "AND pos.cprodmj = stpos.cprodmj "+
            "and pos.vrdok = 'GRC' "+kondishnBlag()+
            "and pos.cskl = '"+tds.getString("CSKL")+"' "+
            "and "+kondishnDatumOrBroj()+kondishnOperater();
      
      Map topgr = new HashMap();
      DataSet allStav = Aus.q(grpString);
      PorData total = new PorData();
      for (allStav.first(); allStav.inBounds(); allStav.next()) {
        String cgr = (String) allart.get(new Integer(allStav.getInt("CART")));
        if (cgr == null) 
          throw new Exception("Nepostojeæi artikl "+allStav.getInt("CART"));

        String tc = cgr;
        
        while (cgr != null) {
          if (tops.contains(cgr)) {
            PorData pd = (PorData) topgr.get(cgr);
            if (pd != null) pd.add(allStav);
            else topgr.put(cgr, new PorData(allStav));
            tc = null;
          }
          if (cgr == allgr.get(cgr)) cgr = null;
          else cgr = (String) allgr.get(cgr);
        }
        if (tc != null)
          throw new Exception("Nedefinirana grupa artikala "+tc);
        
        total.add(allStav);
      }
      
      int minlev = -1;
      for (int i = 0; i < groups.size(); i++) {
        String cgr = (String) groups.get(i);
        PorData pd = (PorData) topgr.get(cgr);
        if (pd == null) continue;
        
        String lcg = cgr;
        int lev = 0;
        while (lcg != null && allgr.get(lcg) != lcg) {
          ++lev;
          lcg = (String) allgr.get(lcg);
        }
        if (lev < minlev || minlev < 0) minlev = lev;
        
        grupePorSet.insertRow(false);
        grupePorSet.setString("CGRART", cgr);
        grupePorSet.setString("NAZGRART", Aus.spc(lev * 2) + grnam.get(cgr));
        pd.update(grupePorSet);
        grupePorSet.post();
      }
      if (grupePorSet.rowCount() > 1) {
        if (minlev > 0)
          for (grupePorSet.first(); grupePorSet.inBounds(); grupePorSet.next())
            grupePorSet.setString("NAZGRART", 
                grupePorSet.getString("NAZGRART").substring(minlev * 2));
        
        grupePorSet.insertRow(false);
        grupePorSet.setString("CGRART", "");
        grupePorSet.setString("NAZGRART", "Ukupno");
        total.update(grupePorSet);
        grupePorSet.post();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  static class PorData {
    BigDecimal osn, pdv, pnp, ukup;
    public PorData() {
      osn = pdv = pnp = ukup = Aus.zero2;
    }
    public PorData(DataSet ds) {
      ukup = ds.getBigDecimal("NETO");
      pdv = ds.getBigDecimal("POR1");
      pnp = ds.getBigDecimal("POR2");
      osn = ukup.subtract(pdv).subtract(pnp);
    }
    public void add(DataSet ds) {
      ukup = ukup.add(ds.getBigDecimal("NETO"));
      pdv = pdv.add(ds.getBigDecimal("POR1"));
      pnp = pnp.add(ds.getBigDecimal("POR2"));
      osn = ukup.subtract(pdv).subtract(pnp);
    }
    public void update(DataSet ds) {
      ds.setBigDecimal("OSNOVICA", osn);
      ds.setBigDecimal("PDV", pdv);
      ds.setBigDecimal("PNP", pnp);
      ds.setBigDecimal("IZNOS", ukup);
    }
  }

  private String kondishnDatumOrBroj(){
    Condition akt = presBlag.stolovi && !presBlag.isUserOriented() ? 
        Condition.equal("AKTIV", "N") : Condition.none;
    akt = akt.and(Condition.equal("RDOK", "arh").andNotNull().not());
    if (jcbPoSmjenama.isSelected())
      akt = akt.and(Condition.equal("FOK", "D"));
    
    if (stolovi && tds.getString("STOL").length() > 0)
      akt = akt.and(Condition.equal("STOL", tds));
    
    if (jrbDatum.isSelected()) {
      
      Timestamp poc = tds.getTimestamp("POCDATUM");
      Timestamp zav = tds.getTimestamp("ZAVDATUM");
      
      Calendar c = Calendar.getInstance();
      c.setTimeInMillis(poc.getTime());
      c.set(c.MILLISECOND, 0);
      c.set(c.SECOND, 0);
      c.set(c.MINUTE, 0);
      c.set(c.HOUR_OF_DAY, 6);
      poc = new Timestamp(c.getTime().getTime());
      
      c.setTimeInMillis(zav.getTime());
      c.set(c.MILLISECOND, 0);
      c.set(c.SECOND, 59);
      c.set(c.MINUTE, 59);
      c.set(c.HOUR_OF_DAY, 29);
      zav = new Timestamp(c.getTime().getTime());
      
      
      return "pos.datdok between '" + poc + "' and '" + zav + "' and " + akt.qualified("pos");
    }
    return Condition.between("BRDOK",tds, "POCBROJ", "ZAVBROJ").
                and(akt).qualified("pos").toString();
  }
  
  private String kondishnBlag(){
    if (tds.getString("CPRODMJ").length() == 0) return "";
    return "and pos.cprodmj = '"+tds.getString("CPRODMJ")+"' ";
  }

  private String kondishnOperater(){
    /** @todo razmisliti o root-u i operaterima */
    if (jrfCBlagajnik.getText().trim().equals("")) return "";
    else if (presBlag.isUserOriented())
      return "and pos.cuser='" + tds.getString("CUSER") + "' ";
    else return "and pos.cblagajnik='" + tds.getString("CBLAGAJNIK") + "' ";
  }

  public void firstESC() {
    
    if (isOk) {
      if (!presBlag.isUserOriented() || raUser.getInstance().isSuper()) {
        rcc.setLabelLaF(jrfCBlagajnik,true);
        rcc.setLabelLaF(jrfNazivBlagajnika,true);
        rcc.setLabelLaF(jbSelBlagajnik,true);
      }
      if (stolovi) {
        rcc.setLabelLaF(jlrStol,true);
        rcc.setLabelLaF(jlrNazStol,true);
        rcc.setLabelLaF(jbSelStol,true);
      } else {
        rcc.setLabelLaF(jrfSM,true);
        rcc.setLabelLaF(jrfNAZ,true);
        rcc.setLabelLaF(jbSM,true);
      }
      rcc.setLabelLaF(jrfCBLAG,true);
      rcc.setLabelLaF(jrfNAZBLAG,true);
      rcc.setLabelLaF(jbCBLAG,true);
      rcc.setLabelLaF(jcbPoArtiklima, true);
      rcc.setLabelLaF(jcbPoKonobarima, true);
      rcc.setLabelLaF(jcbPoSmjenama, true);
      rcc.setLabelLaF(jtfPocDatum, jrbDatum.isSelected());
      rcc.setLabelLaF(jtfZavDatum, jrbDatum.isSelected());
      rcc.setLabelLaF(jtfPocBroj, jrbRacun.isSelected());
      rcc.setLabelLaF(jtfZavBroj, jrbRacun.isSelected());
      rcc.setLabelLaF(jrbDatum, true);
      rcc.setLabelLaF(jrbRacun, true);
      if (jrbDatum.isSelected()) jtfPocDatum.requestFocus();
      else jtfPocBroj.requestFocus();
      isOk = false;
      return;
    }
    
    if (!jcbPoArtiklima.isEnabled()){
    rcc.setLabelLaF(jcbPoArtiklima, true);
    rcc.setLabelLaF(jtfPocDatum, true);
    rcc.setLabelLaF(jtfZavDatum, true);
    rcc.setLabelLaF(jtfPocBroj, true);
    rcc.setLabelLaF(jtfZavBroj, true);
    rcc.setLabelLaF(jrbDatum, true);
    rcc.setLabelLaF(jrbRacun, true);
    tds.setTimestamp("pocDatum", vl.getToday());
    tds.setTimestamp("zavDatum", vl.getToday());
    tds.setInt("POCBROJ",0);
    tds.setInt("ZAVBROJ",0);
    tds.setString("ISPISREK","N");
//    jcbPoArtiklima.setSelected(false);
    ispisChoozer.setSelected(jrbDatum);
    }
    if (!tds.getString("CPRODMJ").equals("")) {
      if (!presBlag.isUserOriented() || raUser.getInstance().isSuper()) {
        rcc.setLabelLaF(jrfCBlagajnik,true);
        rcc.setLabelLaF(jrfNazivBlagajnika,true);
        rcc.setLabelLaF(jbSelBlagajnik,true);
        jrfCBlagajnik.setText("");
        jrfCBlagajnik.emptyTextFields();
      }
      if (stolovi) {
        rcc.setLabelLaF(jlrStol,true);
        rcc.setLabelLaF(jlrNazStol,true);
        rcc.setLabelLaF(jbSelStol,true);
        jlrStol.setText("");
        jlrStol.emptyTextFields();
      } else {
        rcc.setLabelLaF(jrfSM,true);
        rcc.setLabelLaF(jrfNAZ,true);
        rcc.setLabelLaF(jbSM,true);
        jrfSM.setText("");
        jrfSM.emptyTextFields();
      }
      rcc.setLabelLaF(jrfCBLAG,true);
      rcc.setLabelLaF(jrfNAZBLAG,true);
      rcc.setLabelLaF(jbCBLAG,true);
      jrfCBLAG.setText("");
      jrfCBLAG.emptyTextFields();
      jrfCBLAG.requestFocus();
    } else if (!tds.getString("CSKL").equals("")){
      rcc.setLabelLaF(jrfCSKL,true);
      rcc.setLabelLaF(jrfNAZSKL,true);
      rcc.setLabelLaF(jbCSKL,true);
      jrfCSKL.setText("");
      jrfCSKL.emptyTextFields();
      jrfCSKL.requestFocus();
    }
  }

  public boolean runFirstESC() {
    if (!tds.getString("CSKL").equals("")) return true;
    else return false;
  }

  private void jbInit() throws Exception {
    mainXYLayout.setWidth(590);
    mainXYLayout.setHeight(260);
    this.setJPan(mainPanel);
    mainPanel.setLayout(mainXYLayout);

    finalSet.setColumns(new Column[]{dm.createStringColumn("NACPL", 30),
        				dm.createStringColumn("CNACPL",10),
        				dm.createStringColumn("CBANKA",10),
                        dm.createBigDecimalColumn("IZNOS")});
    
    grupeSet.setColumns(new Column[] {
       dm.createStringColumn("CGRART", 10),
       dm.createStringColumn("OPIS", 30),
       dm.createBigDecimalColumn("IZNOS")
    });
    
    grupePorSet.setColumns(new Column[] {
        dm.createStringColumn("CGRART", 10),
        dm.createStringColumn("NAZGRART", 50),
        dm.createBigDecimalColumn("OSNOVICA"),
        dm.createBigDecimalColumn("PDV"),
        dm.createBigDecimalColumn("PNP"),
        dm.createBigDecimalColumn("IZNOS")
     });
    
    porezSet.setColumns(new Column[] {
        dm.createStringColumn("CNACPL", 12),
        dm.createStringColumn("NAZIV", 30),
        dm.createBigDecimalColumn("PROMET"),
        dm.createBigDecimalColumn("OSNOVICA"),
        dm.createBigDecimalColumn("STOPA"),
        dm.createBigDecimalColumn("IZNOS")
    });

    artikliReportQDS.setColumns(new Column[]{(Column)dm.getArtikli().getColumn("CART").clone(),
                                (Column)dm.getArtikli().getColumn("CART1").clone(),
                                (Column)dm.getArtikli().getColumn("BC").clone(),
                                (Column)dm.getArtikli().getColumn("NAZART").clone(),
                                dm.createBigDecimalColumn("KOL")});

//    finalSet.open();

    tds.setColumns(new Column[] {dm.createStringColumn("CSKL","Skladište",10),
                                 dm.createStringColumn("CPRODMJ","Prodajno mjesto",10),
                                 dm.createStringColumn("CBLAGAJNIK","Blagajnik",10),
                                 dm.createStringColumn("CUSER","Korisnik",15),
                                 dm.createStringColumn("STOL","Stol",20),
                                 dm.createTimestampColumn("pocDatum", "Poèetni datum"),
                                 dm.createTimestampColumn("zavDatum", "Krajnji datum"),
                                 dm.createIntColumn("pocBroj", "Poèetni broj"),
                                 dm.createIntColumn("zavBroj", "Krajnji Broj"),
                                 dm.createStringColumn("IspisRek","Ispis rekapitulacije",1),
                                 dm.createStringColumn("IspisSmje","Ispis po smjenama",1),
                                 dm.createStringColumn("IspisKon","Ispis po konobarima",1),
                                 });

    jlSkladiste.setText("Skladište");
    jlBlagajna.setText("Blagajna");
    jllagajnik.setText("Operater");
    jlSmjena.setText("Smjena");
    jlStol.setText("Stol");

    jcbPoArtiklima.setText("Ispis rekapitulacije po artiklima");
    jcbPoArtiklima.setSelectedDataValue("D");
    jcbPoArtiklima.setUnselectedDataValue("N");
    jcbPoArtiklima.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbPoArtiklima.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbPoArtiklima.setDataSet(tds);
    jcbPoArtiklima.setColumnName("IspisRek");

    jcbPoSmjenama.setText("Samo raèuni");
    jcbPoSmjenama.setSelectedDataValue("D");
    jcbPoSmjenama.setUnselectedDataValue("N");
    jcbPoSmjenama.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbPoSmjenama.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbPoSmjenama.setDataSet(tds);
    jcbPoSmjenama.setColumnName("IspisSmje");

    jcbPoKonobarima.setText("Ispis rekapitulacije po konobarima");
    jcbPoKonobarima.setSelectedDataValue("D");
    jcbPoKonobarima.setUnselectedDataValue("N");
    jcbPoKonobarima.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbPoKonobarima.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbPoKonobarima.setDataSet(tds);
    jcbPoKonobarima.setColumnName("IspisKon");
    
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setDataSet(tds);

    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setDataSet(tds);

    jtfPocBroj.setColumnName("pocBroj");
    jtfPocBroj.setDataSet(tds);

    jtfZavBroj.setColumnName("zavBroj");
    jtfZavBroj.setDataSet(tds);
    
    if (frmParam.getParam("pos", "tinaStol", "N", "Stolovi iz Tinapos na rekapitulaciji (D,N)").equalsIgnoreCase("D")) {
      jlrStol.setColumnName("STOL");
      jlrStol.setNavColumnName("ID");
      jlrStol.setVisCols(new int[]{0,1});
      jlrStol.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazStol});
      jlrStol.setColNames(new String[] {"NAME"});
      jlrStol.setDataSet(tds);
      jlrStol.setRaDataSet(Aus.q("SELECT ID,NAME FROM places"));
      jlrStol.setSearchMode(0);
      jlrStol.setNavButton(jbSelStol);
      
      jlrNazStol.setColumnName("NAME");
      jlrNazStol.setNavProperties(jlrStol);
      jlrNazStol.setSearchMode(1);
    }
    

    jrfCSKL.setColumnName("CSKL");
    jrfCSKL.setColNames(new String[] {"NAZSKL"});
    jrfCSKL.setVisCols(new int[]{2,3});
    jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZSKL});
    jrfCSKL.setRaDataSet(Sklad.getDataModule().getTempSet(Aus.getKnjigCond()));
    jrfCSKL.setDataSet(tds);
    jrfCSKL.setSearchMode(0);
    jrfCSKL.setNavButton(jbCSKL);
    jrfCSKL.setNavProperties(null);
    jrfCSKL.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jrfCSKL_focusLost(e);
      }
    });

    jrfNAZSKL.setColumnName("NAZSKL");
    jrfNAZSKL.setNavProperties(jrfCSKL);
    jrfNAZSKL.setSearchMode(1);
    
    jrfSM.setColumnName("CSMJENA");
    jrfSM.setColNames(new String[] {"NAZIV"});
    jrfSM.setVisCols(new int[]{0,1});
    jrfSM.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZ});
    jrfSM.setRaDataSet(Smjene.getDataModule().getFilteredDataSet(""));
    jrfSM.setDataSet(tds);
    jrfSM.setSearchMode(0);
    jrfSM.setNavButton(jbSM);
    jrfSM.setNavProperties(null);
    /*jrfSM.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jrfCSKL_focusLost(e);
      }
    });*/

    jrfNAZ.setColumnName("NAZIV");
    jrfNAZ.setNavProperties(jrfSM);
    jrfNAZ.setSearchMode(1);

    jrfCBLAG.setRaDataSet(getProdMjZaSklad());
    jrfCBLAG.setDataSet(tds);
    jrfCBLAG.setNavButton(jbCBLAG);
    jrfCBLAG.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZBLAG});
    jrfCBLAG.setVisCols(new int[]{2,3});
    jrfCBLAG.setColNames(new String[] {"NAZPRODMJ"});
    jrfCBLAG.setColumnName("CPRODMJ");

    jrfNAZBLAG.setColumnName("NAZPRODMJ");
    jrfNAZBLAG.setSearchMode(1);
    jrfNAZBLAG.setNavProperties(jrfCBLAG);

    jrfCBlagajnik.setRaDataSet(presBlag.isUserOriented() ?
        dm.getUseri() : dm.getBlagajnici());
    jrfCBlagajnik.setDataSet(tds);
    jrfCBlagajnik.setTextFields(new javax.swing.text.JTextComponent[] {jrfNazivBlagajnika});
    jrfCBlagajnik.setVisCols(new int[]{0,1});
    if (presBlag.isUserOriented()) {
      jrfCBlagajnik.setColNames(new String[] {"NAZIV"});
      jrfCBlagajnik.setColumnName("CUSER");
    } else {
      jrfCBlagajnik.setColNames(new String[] {"NAZBLAG"});
      jrfCBlagajnik.setColumnName("CBLAGAJNIK");
    }
    jrfCBlagajnik.setNavButton(jbSelBlagajnik);

    jrfNazivBlagajnika.setColumnName(presBlag.isUserOriented() ? "NAZIV" : "NAZBLAG");
    jrfNazivBlagajnika.setSearchMode(1);
    jrfNazivBlagajnika.setNavProperties(jrfCBlagajnik);

    ispisChoozer.add(jrbDatum,"Datum (od-do)");
    ispisChoozer.add(jrbRacun,"Ra\u010Dun (od-do)");
    ispisChoozer.setSelected(jrbDatum);
    jrbDatum.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        radio_actionPerformed();
      }
    });
    jrbRacun.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        radio_actionPerformed();
      }
    });
    mainPanel.add(jlSkladiste, new XYConstraints(15, 20, -1, -1));
    mainPanel.add(jlBlagajna, new XYConstraints(15, 45, -1, -1));
    
    mainPanel.add(jllagajnik, new XYConstraints(15, 95, -1, -1));

    mainPanel.add(jrfCSKL, new XYConstraints(150, 15, 100, -1));
    mainPanel.add(jrfNAZSKL,  new XYConstraints(255, 15, 295, -1));
    mainPanel.add(jbCSKL,   new XYConstraints(555, 15, 21, 21));

    mainPanel.add(jrfCBLAG, new XYConstraints(150, 45, 100, -1));
    mainPanel.add(jrfNAZBLAG, new XYConstraints(255, 45, 295, -1));
    mainPanel.add(jbCBLAG, new XYConstraints(555, 45, 21, 21));

    if (frmParam.getParam("pos", "tinaStol", "D", "Stolovi iz Tinapos na rekapitulaciji (D,N)").equalsIgnoreCase("D")) {
      stolovi = true;
      mainPanel.add(jlStol, new XYConstraints(15, 70, -1, -1));
      mainPanel.add(jlrStol, new XYConstraints(150, 70, 100, -1));
      mainPanel.add(jlrNazStol, new XYConstraints(255, 70, 295, -1));
      mainPanel.add(jbSelStol, new XYConstraints(555, 70, 21, 21));
    } else {
      stolovi = false;
      mainPanel.add(jlSmjena, new XYConstraints(15, 70, -1, -1));
      mainPanel.add(jrfSM, new XYConstraints(150, 70, 100, -1));
      mainPanel.add(jrfNAZ, new XYConstraints(255, 70, 295, -1));
      mainPanel.add(jbSM, new XYConstraints(555, 70, 21, 21));
    }
    
    mainPanel.add(jrfCBlagajnik, new XYConstraints(150, 95, 100, -1));
    mainPanel.add(jrfNazivBlagajnika, new XYConstraints(255, 95, 295, -1));
    mainPanel.add(jbSelBlagajnik, new XYConstraints(555, 95, 21, 21));

    mainPanel.add(jtfPocDatum, new XYConstraints(150, 120/*70*/, 100, -1));
    mainPanel.add(jtfZavDatum, new XYConstraints(255, 120/*70*/, 100, -1));

    mainPanel.add(jtfPocBroj, new XYConstraints(150, 145/*95*/, 100, -1));
    mainPanel.add(jtfZavBroj, new XYConstraints(255, 145/*95*/, 100, -1));

    mainPanel.add(jrbDatum,   new XYConstraints(15, 120/*70*/, -1, -1));
    mainPanel.add(jrbRacun,   new XYConstraints(15, 145/*95*/, -1, -1));

    mainPanel.add(jcbPoArtiklima, new XYConstraints(250, 170/*120*/, 300, -1));
    mainPanel.add(jcbPoSmjenama, new XYConstraints(250, 195/*120*/, 300, -1));
    mainPanel.add(jcbPoKonobarima, new XYConstraints(250, 220/*120*/, 300, -1));

    rcc.setLabelLaF(jtfPocBroj,false);
    rcc.setLabelLaF(jtfZavBroj,false);

    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnj, String newKnj){
        jrfCSKL.setRaDataSet(Sklad.getDataModule().getTempSet(Aus.getKnjigCond()));
      }
    });
  }

  public com.borland.dx.sql.dataset.QueryDataSet getProdMjZaSklad(){
    String stringQDS = "select * from prod_mj";
    com.borland.dx.sql.dataset.QueryDataSet prodMjZaSklad = new com.borland.dx.sql.dataset.QueryDataSet();
    prodMjZaSklad.setQuery(new com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(),stringQDS));
    prodMjZaSklad.open();
    return prodMjZaSklad;
  }

  void findMpSkl() {
    if (hr.restart.robno.Util.getUtil().getMPSklDataset().rowCount()==0) {
      JOptionPane.showConfirmDialog(null,"Nema definiranog maloprodajnog skladišta !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      this.setVisible(false);
    } else {
      jrfCSKL.setText(hr.restart.robno.Util.getUtil().getMPSklDataset().getString("CSKL"));
      jrfNAZSKL.setText(hr.restart.robno.Util.getUtil().getMPSklDataset().getString("NAZSKL"));
      rcc.setLabelLaF(jrfCSKL,false);
      rcc.setLabelLaF(jrfNAZSKL,false);
      rcc.setLabelLaF(jbCSKL,false);
      jrfCBLAG.requestFocus();
    }
  }
  
  void findSklad() {
    jrfCSKL.setText(raUser.getInstance().getDefSklad());
    jrfCSKL.forceFocLost();
    if (jrfCSKL.getText().length() > 0) {
      rcc.setLabelLaF(jrfCSKL,false);
      rcc.setLabelLaF(jrfNAZSKL,false);
      rcc.setLabelLaF(jbCSKL,false);
      jrfCBLAG.requestFocusLater();
    } else jrfCSKL.requestFocusLater();
  }

  void radio_actionPerformed() {
    rcc.setLabelLaF(jtfPocDatum,jrbDatum.isSelected());
    rcc.setLabelLaF(jtfZavDatum,jrbDatum.isSelected());
    rcc.setLabelLaF(jtfPocBroj,!jrbDatum.isSelected());
    rcc.setLabelLaF(jtfZavBroj,!jrbDatum.isSelected());
  }

  void jrfCSKL_focusLost(FocusEvent e) {
    if(!jrfCSKL.getText().trim().equals("")){
      rcc.setLabelLaF(jrfCSKL,false);
      rcc.setLabelLaF(jrfNAZSKL,false);
      rcc.setLabelLaF(jbCSKL,false);
      jrfCBLAG.requestFocus();
    }
  }
}
