/****license*****************************************************************
**   file: creatorPRD.java
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
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class creatorPRD {

  private raenginePRD reP = new raenginePRD();
  private dM dm = dM.getDataModule();
  private lookupData ld = lookupData.getlookupData();
  private int rbr = 0;//Rbr.getRbr();
  private String god;
  private String cskl;
  private String zaliha;
  private int brdok;
  private QueryDataSet stanje;
  private QueryDataSet artikl;
  private QueryDataSet stavkeRN;
  private HashMap hm = new HashMap();
  private boolean forceArtCjenik = false;
  
  private ArrayList greska = new ArrayList();
  private raControlDocs rCD = new raControlDocs();
  private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  private Timestamp datdok= null;
  private String cradnal = "";
  private BigDecimal randman, koef;


//  final PreparedStatement p1 = raTransaction.getPreparedStatement
//                              ("INSERT INTO doku (cskl,vrdok,god,datdok,brdok) ");
//  final private java.sql.PreparedStatement insertStanje = raTransaction.getPreparedStatement
//  ("INSERT INTO Stanje(cskl,cart,god,kolul,kolmat,kol) Values (?,?,?,?,?,?)");

  public creatorPRD() {
  }

  public void findajStanjaAndArtikl(DataSet ds,String cskl,Timestamp datdok) {

    ArrayList ls = new ArrayList();
    ArrayList lsrn = new ArrayList();

    VarStr dodatak =new VarStr();
    VarStr dodatakRN = new VarStr();

    for (ds.first();ds.inBounds();ds.next()) {
      ls.add(String.valueOf(ds.getInt("CART")));
      lsrn.add(String.valueOf(ds.getInt("RBSID")));
    }
    
    DataSet nus = Aus.q("SELECT * FROM rnus WHERE "+Condition.in("CARTNOR", ds, "CART"));
    for (nus.first(); nus.inBounds(); nus.next()) {
      ls.add(String.valueOf(nus.getInt("CART")));
    }
    
//    dodatak = VarStr.join(ls, ',');

    for (int i = 0;i<ls.size();i++) {
//      if (i==ls.size()-1)dodatak = dodatak.append(ls.get(i));
//      else
        dodatak.append(ls.get(i)).append(",");
    }
    dodatak.chopRight(1);

    for (int i = 0;i<lsrn.size();i++) {
//      if (i==lsrn.size()-1)dodatakRN = dodatakRN+(String)lsrn.get(i);
//      else dodatakRN = dodatakRN+(String)lsrn.get(i)+",";
      dodatakRN.append(lsrn.get(i)).append(",");
    }
    dodatakRN.chopRight(1);

    artikl = hr.restart.util.Util.getNewQueryDataSet("SELECT * from Artikli where "+
       "cart in ("+dodatak.toString()+")",true);

    stanje = hr.restart.util.Util.getNewQueryDataSet("SELECT * from stanje where "+
        "cskl = '"+cskl+"' and god ='"+god+"' and cart in ("+dodatak+")",true);

    stavkeRN = hr.restart.util.Util.getNewQueryDataSet("SELECT * from stdoki where vrdok='RNL' and "+
        "cradnal='"+cradnal+"' and rbsid in ("+dodatakRN.toString()+")",true);
  }

  public boolean creatPRD(DataSet ds,String cskl,Timestamp datdok,String cradnal,BigDecimal randman,int brdok,QueryDataSet detail){

  	this.datdok= datdok;
    greska.clear();

    if (!ld.raLocate(dm.getSklad(),new String[] {"CSKL"},new String[] {cskl})) {
      greska.add("Ne postoji odabrano skladište !!!");
      return false;
    }

    zaliha = dm.getSklad().getString("VRZAL");

    DataSet radninalog =  ds;

    if (radninalog == null || radninalog.getRowCount()==0) {
      greska.add("Nema stavaka !!!");
      return false;
    }

    god = hr.restart.util.Valid.getValid().findYear(datdok);
    this.cskl = cskl;
    this.cradnal = cradnal;
    this.brdok = brdok;
    this.randman = randman;
    rbr = 1;
    
    forceArtCjenik = frmParam.getParam("robno", "forcePredArt", "N", "Forsirati cijene " +
            "s artikla kod predatnica iz radnih naloga (D,N)?").equalsIgnoreCase("D");
    
    findajStanjaAndArtikl(radninalog,cskl,datdok);
    for (radninalog.first();radninalog.inBounds();radninalog.next()) {
      if (!creatStavke(radninalog,detail)) {
        greska.add("Greška u creatStavke");
        return false;
      }
    }
    return true;
  }

  public void ImaCijene(DataSet ds){

    hm.put("IBP",getKalkulIznos("VC").multiply(ds.getBigDecimal("KOL")));
    hm.put("ISP",getKalkulIznos("MC").multiply(ds.getBigDecimal("KOL")));

    hm.put("MAR",getKalkulIznos("IBP").subtract(getKalkulIznos("INAB")));
    BigDecimal tempBD = getKalkulIznos("INAB").divide(new BigDecimal("100.00"),2,BigDecimal.ROUND_HALF_UP);

    if (tempBD.floatValue()!= 0.0) {
      hm.put("PMAR",getKalkulIznos("MAR").divide(tempBD,2,BigDecimal.ROUND_HALF_UP));
    }

    hm.put("POR1",getKalkulIznos("IBP").multiply(
        dm.getPorezi().getBigDecimal("POR1")).divide(new BigDecimal("100.00"),2,BigDecimal.ROUND_HALF_UP));
    if (dm.getPorezi().getString("PORNAPOR2").equalsIgnoreCase("D")) {
      tempBD = getKalkulIznos("IBP").add(getKalkulIznos("POR1"));
      hm.put("POR2",tempBD.multiply(
                                    dm.getPorezi().getBigDecimal("POR2")).divide(new BigDecimal("100.00"),2,BigDecimal.ROUND_HALF_UP));
    } else {
      hm.put("POR2",getKalkulIznos("IBP").multiply(
          dm.getPorezi().getBigDecimal("POR2")).divide(new BigDecimal("100.00"),2,BigDecimal.ROUND_HALF_UP));
    }
    if (dm.getPorezi().getString("PORNAPOR3").equalsIgnoreCase("D")) {
      tempBD = getKalkulIznos("IBP").add(getKalkulIznos("POR1")).add(getKalkulIznos("POR2"));
      hm.put("POR3",tempBD.multiply(
                                    dm.getPorezi().getBigDecimal("POR3")).divide(new BigDecimal("100.00"),2,BigDecimal.ROUND_HALF_UP));
    }else {
      hm.put("POR3",getKalkulIznos("IBP").multiply(
          dm.getPorezi().getBigDecimal("POR3")).divide(new BigDecimal("100.00"),2,BigDecimal.ROUND_HALF_UP));
    }

    tempBD=getKalkulIznos("ISP").subtract(getKalkulIznos("ISP")).
           subtract(getKalkulIznos("POR1")).
           subtract(getKalkulIznos("POR2")).
           subtract(getKalkulIznos("POR3"));

    if (tempBD.floatValue()!=0.0){
      hm.put("POR1",getKalkulIznos("POR1").add(tempBD));
    }
  }

  public void NemaCijene(DataSet ds){

    BigDecimal tempBD;
    hm.put("IBP",getKalkulIznos("INAB"));
    hm.put("POR1",getKalkulIznos("IBP").multiply(
        dm.getPorezi().getBigDecimal("POR1")).divide(new BigDecimal("100.00"),2,BigDecimal.ROUND_HALF_UP));
    if (dm.getPorezi().getString("PORNAPOR2").equalsIgnoreCase("D")) {
      tempBD = getKalkulIznos("IBP").add(getKalkulIznos("POR1"));
      hm.put("POR2",tempBD.multiply(
                                    dm.getPorezi().getBigDecimal("POR2")).divide(new BigDecimal("100.00"),2,BigDecimal.ROUND_HALF_UP));
    } else {
      hm.put("POR2",getKalkulIznos("IBP").multiply(
          dm.getPorezi().getBigDecimal("POR2")).divide(new BigDecimal("100.00"),2,BigDecimal.ROUND_HALF_UP));
    }
    if (dm.getPorezi().getString("PORNAPOR3").equalsIgnoreCase("D")) {
      tempBD = getKalkulIznos("IBP").add(getKalkulIznos("POR1")).add(getKalkulIznos("POR2"));
      hm.put("POR3",tempBD.multiply(
                                    dm.getPorezi().getBigDecimal("POR3")).divide(new BigDecimal("100.00"),2,BigDecimal.ROUND_HALF_UP));
    }else {
      hm.put("POR3",getKalkulIznos("IBP").multiply(
          dm.getPorezi().getBigDecimal("POR3")).divide(new BigDecimal("100.00"),2,BigDecimal.ROUND_HALF_UP));
    }

    hm.put("ISP",getKalkulIznos("ISP").add(getKalkulIznos("IBP")).
           add(getKalkulIznos("POR1")).
           add(getKalkulIznos("POR2")).
           add(getKalkulIznos("POR3")));

    try {
      hm.put("NC",getKalkulIznos("INAB").divide(ds.getBigDecimal("KOL"),2,BigDecimal.ROUND_HALF_UP));
    }
    catch (Exception ex) {
      hm.put("NC",Aus.zero2);
    }

    try {
      hm.put("VC",getKalkulIznos("IBP").divide(ds.getBigDecimal("KOL"),2,BigDecimal.ROUND_HALF_UP));
    }
    catch (Exception ex) {
      hm.put("VC",Aus.zero2);
    }

    try {
      hm.put("MC",getKalkulIznos("ISP").divide(ds.getBigDecimal("KOL"),2,BigDecimal.ROUND_HALF_UP));
    }
    catch (Exception ex) {
      hm.put("MC",Aus.zero2);
    }
  }
  
  public void insertVtPred(StorageDataSet sds,String id_stavka){
  	
  	dm.getVTPred().insertRow(true);
  	dm.getVTPred().setString("id_stavka",id_stavka);
  	dm.getVTPred().setBigDecimal("MAT_I",sds.getBigDecimal("MAT_I"));
  	dm.getVTPred().setBigDecimal("MAT_F",sds.getBigDecimal("MAT_F"));
  	dm.getVTPred().setBigDecimal("MAT_FI",sds.getBigDecimal("MAT_FI"));
  	dm.getVTPred().setBigDecimal("PRO_I",sds.getBigDecimal("PRO_I"));
  	dm.getVTPred().setBigDecimal("PRO_F",sds.getBigDecimal("PRO_F"));
  	dm.getVTPred().setBigDecimal("PRO_FI",sds.getBigDecimal("PRO_FI"));
  	dm.getVTPred().setBigDecimal("ROB_I",sds.getBigDecimal("ROB_I"));
  	dm.getVTPred().setBigDecimal("ROB_F",sds.getBigDecimal("ROB_F"));
  	dm.getVTPred().setBigDecimal("ROB_FI",sds.getBigDecimal("ROB_FI"));  	
  	dm.getVTPred().setBigDecimal("USL_I",sds.getBigDecimal("USL_I"));
  	dm.getVTPred().setBigDecimal("USL_F",sds.getBigDecimal("USL_F"));
  	dm.getVTPred().setBigDecimal("USL_FI",sds.getBigDecimal("USL_FI"));
  	dm.getVTPred().setBigDecimal("POL_I",sds.getBigDecimal("POL_I"));
  	dm.getVTPred().setBigDecimal("POL_F",sds.getBigDecimal("POL_F"));
  	dm.getVTPred().setBigDecimal("POL_FI",sds.getBigDecimal("POL_FI"));
  	dm.getVTPred().setBigDecimal("TOTAL",sds.getBigDecimal("TOTAL"));
  
  }
  
  
  boolean fillHm(DataSet ds, BigDecimal tempBD) {
  	hm.clear();
    boolean isFindStanje = ld.raLocate(stanje,new String[] {"CART","GOD"},new String[] {String.valueOf(ds.getInt("CART")),
    		hr.restart.util.Valid.getValid().findYear(datdok)});
    
    
    hm.put("INAB",tempBD);
//  insertVtPred(reP.getVtPred(),ds.getString("ID_STAVKA"));
 

	  try {
	    tempBD = tempBD.divide(ds.getBigDecimal("KOL"),2,BigDecimal.ROUND_HALF_UP);
	  }
	  catch (Exception ex) {
	    tempBD = Aus.zero2;
	  }
	
	  hm.put("NC",tempBD);
	
	
	  if (isFindStanje) {
	    hm.put("VC",stanje.getBigDecimal("VC"));
	    hm.put("MC",stanje.getBigDecimal("MC"));
	    hm.put("ZC",stanje.getBigDecimal("ZC"));
	  }
	  else {
	    hm.put("VC",artikl.getBigDecimal("VC"));
	    hm.put("MC",artikl.getBigDecimal("MC"));
	  }
	  
	  if (forceArtCjenik) {
	    hm.put("NC",artikl.getBigDecimal("NC"));
	    hm.put("VC",artikl.getBigDecimal("VC"));
	    hm.put("MC",artikl.getBigDecimal("MC"));
	    hm.put("INAB", artikl.getBigDecimal("NC").multiply(ds.getBigDecimal("KOL")).
	          setScale(2, BigDecimal.ROUND_HALF_UP));
	  }
	
	  if (getKalkulIznos("VC").doubleValue()==0 ) {
	    NemaCijene(ds);
	  }
	  else {
	    ImaCijene(ds);
	  }
	
	  if (zaliha.equalsIgnoreCase("N")) {
	    hm.put("IMAR",Aus.zero2);
	    hm.put("IPOR",Aus.zero2);
	    hm.put("IZAD",getKalkulIznos("INAB"));
	//    if (!isFindStanje)
	    hm.put("ZC",getKalkulIznos("NC"));
	  }else if (zaliha.equalsIgnoreCase("V")) {
	    hm.put("IMAR",getKalkulIznos("IBP").subtract(getKalkulIznos("INAB")));
	    hm.put("IPOR",Aus.zero2);
	    hm.put("IZAD",getKalkulIznos("IBP"));
	//    if (!isFindStanje)
	    hm.put("ZC",getKalkulIznos("VC"));
	
	  }else if (zaliha.equalsIgnoreCase("M")) {
	    hm.put("IMAR",getKalkulIznos("IBP").subtract(getKalkulIznos("INAB")));
	    hm.put("IPOR",getKalkulIznos("ISP").subtract(getKalkulIznos("IBP")));
	    hm.put("IZAD",getKalkulIznos("IBP"));
	//    if (!isFindStanje)
	    hm.put("ZC",getKalkulIznos("MC"));
	
	  }
	  if (isFindStanje) {
	    hm.put("SKOL", stanje.getBigDecimal("KOL"));
	    hm.put("SVC", stanje.getBigDecimal("VC"));
	    hm.put("SMC", stanje.getBigDecimal("MC"));
	    hm.put("TKAL", stanje.getString("TKAL"));
	/*
	    if (stanje.getBigDecimal("KOL").floatValue()!=0 &&
	        (stanje.getBigDecimal("VC").compareTo(getKalkulIznos("VC")) !=0 ||
	         stanje.getBigDecimal("MC").compareTo(getKalkulIznos("MC")) !=0)){
	
	      if (zaliha.equalsIgnoreCase("V")) {
	        hm.put("DIOPORMAR",
	      }
	      else if (zaliha.equalsIgnoreCase("V")) {
	      }
	    }
	*/
	  }
    return isFindStanje;
  }


  public boolean kakulacija(DataSet ds) {

    hm.clear();
    boolean isFindStanje = ld.raLocate(stanje,new String[] {"CART","GOD"},new String[] {String.valueOf(ds.getInt("CART")),
    		hr.restart.util.Valid.getValid().findYear(datdok)});		
    
    boolean isFindArtikl = ld.raLocate(artikl,new String[] {"CART"},new String[] {String.valueOf(ds.getInt("CART"))});
    boolean isFindRN = ld.raLocate(stavkeRN,new String[] {"RBSID"},new String[] {String.valueOf(ds.getInt("RBSID"))});

    if (!isFindArtikl) {
      greska.add("Ne postoji artikl s šifrom "+ds.getInt("CART"));
      return false;
    }

    if (!ld.raLocate(dm.getPorezi(),new String[] {"CPOR"},new String[] {artikl.getString("CPOR")})) {
      greska.add("Ne postoje porezi za artikl "+ds.getInt("CART")+" !!!");
      return false;
    }
    if (!isFindRN) {
      greska.add("Ne postoji artikl s šifrom "+ds.getInt("CART"));
      return false;
    }
/*
    BigDecimal tempBD = reP.iznosTroskovaMaterijala(cradnal,ds.getInt("RBSID"));
    tempBD = tempBD.add(reP.iznosTroskovaUsluge(cradnal,ds.getInt("RBSID")));

*/
    Aus.mul(ds, "KOL", randman);
    
    reP.prepareVtPred(cradnal, ds.getInt("RBSID"), ds.rowCount() == 1);
    
    BigDecimal tempBD = reP.getProIzn();
    
    koef = Aus.one0;
    DataSet nus = Aus.q("SELECT * FROM rnus WHERE cartnor = " + ds.getInt("CART"));
    for (nus.first(); nus.inBounds(); nus.next()) {
      koef = koef.add(nus.getBigDecimal("KOEFKOL").multiply(nus.getBigDecimal("KOEFZC")));
    }
    
    tempBD = tempBD.divide(koef, 2, BigDecimal.ROUND_HALF_UP);
    
    fillHm(ds, tempBD);
    
    stavkeRN.setString("STATUS","Z");
    return updateStanje(isFindStanje,ds);
  }

  public boolean updateStanje(boolean isFind,DataSet ds) {

      try {
        if (!isFind) {
          stanje.insertRow(true);
          stanje.setString("CSKL",cskl);
          stanje.setString("GOD",god);
          stanje.setInt("CART",ds.getInt("CART"));

          stanje.setBigDecimal("NC",getKalkulIznos("NC"));
          stanje.setBigDecimal("VC",getKalkulIznos("VC"));
          stanje.setBigDecimal("MC",getKalkulIznos("MC"));

          stanje.setBigDecimal("KOLUL",ds.getBigDecimal("KOL"));
          stanje.setBigDecimal("VUL",getKalkulIznos("IZAD"));
          stanje.setBigDecimal("NABUL",getKalkulIznos("INAB"));
          stanje.setBigDecimal("MARUL",getKalkulIznos("IMAR"));
          stanje.setBigDecimal("PORUL",getKalkulIznos("IPOR"));
          stanje.setBigDecimal("KOL",ds.getBigDecimal("KOL"));
          stanje.setBigDecimal("VRI",stanje.getBigDecimal("VUL").subtract(stanje.getBigDecimal("VIZ")));
          stanje.setString("SKAL",stanje.getString("TKAL"));
        } else {
          stanje.setBigDecimal("KOLUL",
                               stanje.getBigDecimal("KOLUL").add(ds.
              getBigDecimal("KOL")));
          stanje.setBigDecimal("VUL",
                               stanje.getBigDecimal("VUL").add(
                               getKalkulIznos("IZAD")));
          stanje.setBigDecimal("NABUL",
                               stanje.getBigDecimal("NABUL").add(
                               getKalkulIznos("INAB")));
          stanje.setBigDecimal("MARUL",
                               stanje.getBigDecimal("MARUL").add(
                               getKalkulIznos("IMAR")));
          stanje.setBigDecimal("PORUL",
                               stanje.getBigDecimal("PORUL").add(
                               getKalkulIznos("IPOR")));
          stanje.setBigDecimal("KOL",
                               stanje.getBigDecimal("KOLUL").subtract(stanje.
              getBigDecimal("KOLIZ")));
          stanje.setBigDecimal("VRI",
                               stanje.getBigDecimal("VUL").subtract(stanje.
              getBigDecimal("VIZ")));
          stanje.setString("SKAL", stanje.getString("TKAL"));
        }

        /*if (stanje.getBigDecimal("KOLUL").intValue() !=0) {
          stanje.setBigDecimal("NC",stanje.getBigDecimal("VUL").divide
                               (stanje.getBigDecimal("KOLUL"),2,BigDecimal.ROUND_HALF_UP));
        } else {
          stanje.setBigDecimal("NC",Aus.zero2);
        }*/
        
        if (zaliha.equalsIgnoreCase("N")) {
          if (stanje.getBigDecimal("KOL").intValue() != 0) {
            stanje.setBigDecimal("ZC", stanje.getBigDecimal("VRI").divide
                                 (stanje.getBigDecimal("KOL"), 2,
                                  BigDecimal.ROUND_HALF_UP));
            stanje.setBigDecimal("NC", stanje.getBigDecimal("ZC"));
          }
          stanje.setBigDecimal("ZC", stanje.getBigDecimal("NC"));
        }
        else if (zaliha.equalsIgnoreCase("V")) {
          stanje.setBigDecimal("ZC", getKalkulIznos("VC"));
        }
        else if (zaliha.equalsIgnoreCase("M")) {
          stanje.setBigDecimal("ZC", getKalkulIznos("MC"));
        }

        return true;
      }
      catch (Exception ex) {
        ex.printStackTrace();
        return false;
      }
  }

  public BigDecimal getKalkulIznos(String polje) {

    if (hm.containsKey(polje)) return (BigDecimal) hm.get(polje);
    return Aus.zero2;

  }
  
  void fillDs(DataSet ds,QueryDataSet detalji) {
    detalji.setString("CSKL",cskl);
    detalji.setString("GOD",god);
    detalji.setString("VRDOK","PRE");
    detalji.setInt("BRDOK",brdok);
    detalji.setShort("RBR",(short) rbr++);
    detalji.setInt("RBSID",(int) detalji.getShort("RBR"));
    detalji.setInt("CART",ds.getInt("CART"));
    detalji.setString("CART1",ds.getString("CART1"));
    detalji.setString("BC",ds.getString("BC"));
    detalji.setString("NAZART",ds.getString("NAZART"));
    detalji.setString("JM",ds.getString("JM"));
    detalji.setBigDecimal("KOL",ds.getBigDecimal("KOL"));
    detalji.setBigDecimal("DC",getKalkulIznos("DC")); // ovo mjenjati
    detalji.setBigDecimal("DC_VAL",getKalkulIznos("DC_VAL"));
    detalji.setBigDecimal("IDOB",getKalkulIznos("IDOB"));  // ovo mijenjati
    detalji.setBigDecimal("IDOB_VAL",getKalkulIznos("IDOB_VAL"));
    detalji.setBigDecimal("PRAB",getKalkulIznos("PRAB"));
    detalji.setBigDecimal("IRAB",getKalkulIznos("IRAB"));
    detalji.setBigDecimal("PZT",getKalkulIznos("PZT"));
    detalji.setBigDecimal("IZT",getKalkulIznos("IZT"));
    detalji.setBigDecimal("NC",getKalkulIznos("NC"));    // promjeniti
    detalji.setBigDecimal("PMAR",getKalkulIznos("PMAR"));
    detalji.setBigDecimal("MAR",getKalkulIznos("MAR"));
    detalji.setBigDecimal("VC",getKalkulIznos("VC"));
    detalji.setBigDecimal("POR1",getKalkulIznos("POR1"));
    detalji.setBigDecimal("POR2",getKalkulIznos("POR2"));
    detalji.setBigDecimal("POR3",getKalkulIznos("POR3"));
    detalji.setBigDecimal("MC",getKalkulIznos("MC"));
    detalji.setBigDecimal("INAB",getKalkulIznos("INAB"));
    detalji.setBigDecimal("IMAR",getKalkulIznos("IMAR"));
    detalji.setBigDecimal("IBP",getKalkulIznos("IBP"));
    detalji.setBigDecimal("IPOR",getKalkulIznos("IPOR"));
    detalji.setBigDecimal("ISP",getKalkulIznos("ISP"));
    detalji.setBigDecimal("ZC",getKalkulIznos("ZC"));
    detalji.setBigDecimal("IZAD",getKalkulIznos("IZAD"));
    detalji.setBigDecimal("SKOL",getKalkulIznos("SKOL"));
    detalji.setBigDecimal("SVC",getKalkulIznos("SVC"));
    detalji.setBigDecimal("SMC",getKalkulIznos("SMC"));
    detalji.setBigDecimal("DIOPORMAR",getKalkulIznos("DIOPORMAR"));
    detalji.setBigDecimal("DIOPORPOR",getKalkulIznos("DIOPORPOR"));
    detalji.setBigDecimal("PORAV",getKalkulIznos("PORAV"));
    detalji.setString("SKAL",hm.containsKey("SKAL")? (String) hm.get("TKAL"):"");
    detalji.setString("ID_STAVKA",rCD.getKey(detalji));
    stanje.setString("TKAL",rCD.getKey(detalji));
  }

  public boolean creatStavke(DataSet ds,QueryDataSet detalji){

     if (!kakulacija(ds)) return false;
     try {
       detalji.insertRow(true);
       fillDs(ds, detalji);
       BigDecimal total = detalji.getBigDecimal("INAB");
       detalji.setString("VEZA",stavkeRN.getString("ID_STAVKA"));
       insertVtPred(reP.getVtPred(),detalji.getString("ID_STAVKA"));
       BigDecimal kol = detalji.getBigDecimal("KOL");
       BigDecimal nc = detalji.getBigDecimal("NC");
       
       DataSet nus = Aus.q("SELECT * FROM rnus WHERE cartnor = " + ds.getInt("CART"));
       for (nus.first(); nus.inBounds(); nus.next()) {
      	 detalji.insertRow(true);
      	 Aut.getAut().copyArtFields(detalji, nus);
      	 detalji.setBigDecimal("KOL", kol);
      	 Aus.mul(detalji, "KOL", nus, "KOEFKOL");
      	 detalji.setBigDecimal("NC", nc);
      	 Aus.mul(detalji, "NC", nus, "KOEFZC");
      	 Aus.mul(detalji, "INAB", "NC", "KOL");
      	 total = total.add(detalji.getBigDecimal("INAB"));
      	 if (nus.atLast()) 
      	   Aus.add(detalji, "INAB", reP.getProIzn().subtract(total));
      	 updateStanje(fillHm(detalji, detalji.getBigDecimal("INAB")),detalji);
      	 fillDs(detalji, detalji);
       }       
       return true;
     }
     catch (Exception ex) {
       ex.printStackTrace();
       return false;
     }
  }
  public QueryDataSet getStanje() {
    return stanje;
  }
  public QueryDataSet getStavkeRN() {
    return stavkeRN;
  }
}
