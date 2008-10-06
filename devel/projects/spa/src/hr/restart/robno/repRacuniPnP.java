/****license*****************************************************************
**   file: repRacuniPnP.java
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
import hr.restart.baza.Kosobe;
import hr.restart.sisfun.frmParam;
import hr.restart.util.lookupData;

import java.math.BigDecimal;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * @author S.G.
 *
 * Started 2005.04.07
 * 
 */

public class repRacuniPnP extends repIzlazni {

  String afix;
  String sifdob;
  String sifdosko;
  String sifracko;
  
  public repRacuniPnP() {
    this(true);
  }

  public repRacuniPnP(boolean init) {
    super(init);
    if (init) {
      vrsteRabata();
      artikliParkiranja();
      stranaShifra();
      
      afix = frmParam.getParam("robno", "akcijaPrefiks", "",
      "Prefiks naziva artikla za artikle na akciji (prazno za onemogucavanje)");
      sifdob = frmParam.getParam("robno", "sifGetro", "", "Šifra dobavljaèa za Getro");
      sifdosko = frmParam.getParam("robno", "sifGetroKO", 
          "", "Šifra kontakt osobe za dostavnicu za Getro");
      sifracko = frmParam.getParam("robno", "sifGetroKORac", 
          "", "Šifra kontakt osobe za raèun za Getro");
    }
  }
  
  protected void dokChanged() {
    vrsteRabata();
    stranaShifra();
  }
  
  public String getVTRABNAZRAB(){ // colname = NRAB
    String fr = "";
    try {
      fr = getValue("NRAB",(short)ds.getInt("RBSID")); 
    } catch (Exception e) {
      e.printStackTrace();
    }
   return fr; 
  }
  
  public String getVTRABPOSTO(){ // colname = PRAB
    String fr = "";
    try {
      fr = getValue("PRAB",(short)ds.getInt("RBSID")); 
    } catch (Exception e) {
      e.printStackTrace();
    }
   return fr; 
  }
  
  public String getVTRABIRAB(){ // colname = IRAB
    String fr = "";
    try {
      fr = getValue("IRAB",(short)ds.getInt("RBSID")); 
    } catch (Exception e) {
      e.printStackTrace();
    }
   return fr; 
  }
  
  private static QueryDataSet setPopusta = null;
  private static QueryDataSet setArtikala = null;
  private static QueryDataSet setSifri = null;
  
  private void vrsteRabata(){
    String upit = "SELECT rabati.nrab, vtrabat.cskl, vtrabat.vrdok, vtrabat.god, vtrabat.brdok, vtrabat.rbr, vtrabat.lrbr, vtrabat.crab, vtrabat.prab, vtrabat.irab, vtrabat.rabnarab FROM rabati, vtrabat "+
    			  "WHERE vtrabat.crab = rabati.crab "+
    			  "and cskl = '"+ds.getString("CSKL")+"' and vrdok = '"+ds.getString("VRDOK")+"' and god = '"+ds.getString("GOD")+"' and brdok = "+ds.getInt("BRDOK")+" and rbr != 0 order by rbr, lrbr";
    
    setPopusta = ut.getNewQueryDataSet(upit);
  }
  
  private void artikliParkiranja(){
    String upit = "SELECT cart, jmpak, brjed FROM Artikli WHERE brjed > 0.00";
    
    setArtikala = ut.getNewQueryDataSet(upit);
  }
  
  private void stranaShifra(){
    String upit = "SELECT cart, ccpar, bcpar, pnazart FROM VTCartPart where cpar = "+ds.getInt("CPAR");
    
    System.out.println(upit); //XDEBUG delete when no more needed
    
    setSifri = ut.getNewQueryDataSet(upit);
  }
  
  private String getValue (String colname, short rbr){
    String value = "";
    setPopusta.first();
    do {
      if (setPopusta.getShort("RBR") == rbr){
        if (colname.equalsIgnoreCase("NRAB"))
          value += setPopusta.getString(colname)+"\n";
        else
          value += sgQuerys.getSgQuerys().format(setPopusta, colname)+"\n";
      }
    } while (setPopusta.next());
    
    return value;
  }
  
  
  public String getJMPAK(){
    if (lookupData.getlookupData().raLocate(setArtikala,"CART",ds.getInt("CART")+""))
      return setArtikala.getString("JMPAK");
    return "";
  }
  
  public String getKOLPAK(){
    if (lookupData.getlookupData().raLocate(setArtikala,"CART",ds.getInt("CART")+"")){
      return sgQuerys.getSgQuerys().format(ds.getBigDecimal("KOL").divide(setArtikala.getBigDecimal("BRJED"),3,BigDecimal.ROUND_HALF_UP),3);
    } else return "";
    
  }
  
  public double getVNAK() {
    return 0;
  }
  
  public double getTNAK() {
    return 0;
  }
  
  public String getNAZARTPAR() {
    if (lookupData.getlookupData().raLocate(setSifri, "CART", ds.getInt("CART")+""))
      if (setSifri.getString("PNAZART").trim().length() > 0)
        return setSifri.getString("PNAZART");

    return getNAZART();
  }
  
  public String getAKT() {
    return afix.length() > 0 &&
      super.getNAZART().startsWith(afix) ? "X" : "";
  }
  
  public String getBCART() {
    return getBC();
  }
  
  public String getBCPAK() {
    if (lookupData.getlookupData().raLocate(setSifri, "CART", ds.getInt("CART")+""))
      if (setSifri.getString("BCPAR").trim().length() > 0)
        return setSifri.getString("BCPAR");
    return "";
  }
  
  public String getCARTPAR() {
    if (lookupData.getlookupData().raLocate(setSifri, "CART", ds.getInt("CART")+""))
      if (setSifri.getString("CCPAR").trim().length() > 0)
        return setSifri.getString("CCPAR");
    return "";
  }
  
  public String getSTRANASIFRA(){
   if (lookupData.getlookupData().raLocate(setSifri, "CART", ds.getInt("CART")+"")){
     if (setSifri.getString("BCPAR").trim().length() > 0)
       return setSifri.getString("CCPAR") + "\n" + setSifri.getString("BCPAR");
     return setSifri.getString("CCPAR");
   } else return "";
  }
  
  public String getNAZPARDOS() {    
    String cached = cache.getValue("NAZPARDOS", Integer.toString(ds.getInt("CPAR")));
    if (cached != null) return cached;
    lookupData.getlookupData().raLocate(dm.getPartneri(), 
        "CPAR", Integer.toString(ds.getInt("CPAR")));
    String np = "\n" + dm.getPartneri().getString("NAZPAR") +
                "\n" + dm.getPartneri().getString("ADR") +
                "\n" + dm.getPartneri().getInt("PBR") +
                " " + dm.getPartneri().getString("MJ") +
                "\nMB  " + dm.getPartneri().getString("MB") +
                "\nTel:  " + dm.getPartneri().getString("TEL");
    
    return cache.returnValue(np);
  }
  
  public String getISPORUKADOS(){    
    String cached = cache.getValue("ISPORUKADOS", ds.getInt("CPAR") + "-" + ds.getInt("PJ"));
    if (cached != null) return cached;
      if (ds.getInt("PJ") > 0) {
        lookupData.getlookupData().raLocate(dm.getPjpar(),
            new String[] {"CPAR","PJ"},
            new String[] {ds.getInt("CPAR")+"",ds.getInt("PJ")+""});
        if (dm.getPjpar().getInt("PBRPJ") > 0) 
          return cache.returnValue(dm.getPjpar().getString("NAZPJ")+"\n"+dm.getPjpar().getString("ADRPJ") + "\n" + dm.getPjpar().getInt("PBRPJ") + " " + dm.getPjpar().getString("MJPJ"));
        return cache.returnValue(dm.getPjpar().getString("NAZPJ")+"\n"+dm.getPjpar().getString("ADRPJ"));
    }
    return cache.returnValue("");
  }
  
  public String getSIFDOB() {
    return sifdob;
  }
  
  public String getKKOSDOS() {
    String cached = cache.getValue("KKOSDOS", ds.getInt("CPAR") + "-" + ds.getInt("PJ"));
    if (cached != null) return cached;
    
    Condition cko = Condition.none;
    if (ds.getInt("PJ") > 0 && lookupData.getlookupData().raLocate(dm.getPjpar(),
          new String[] {"CPAR","PJ"},
          new String[] {ds.getInt("CPAR")+"",ds.getInt("PJ")+""})) {
      cko = Condition.equal("CKO", dm.getPjpar().getInt("CKO"));
    } else {
      cko = Condition.equal("CKO", ds.getInt("CKO"));
    }
    
    return cache.returnValue(getKoString(cko));
  }
  
  public String getDKOSDOS() {
    String cached = cache.getValue("DKOSDOS", Integer.toString(ds.getInt("CPAR")));
    if (cached != null) return cached;
    
    return cache.returnValue(getKoString(
        Condition.equal("CKO", Integer.parseInt(sifdosko))));
  }
  
  private String getKoString(Condition cko) {
    DataSet kos = Kosobe.getDataModule().getTempSet(
        Condition.equal("CPAR", ds.getInt("CPAR")).and(cko));
    kos.open();
    if (kos.rowCount() == 0) return "";
    
    return kos.getString("IME") +
        "\nTel: " + kos.getString("TEL") +
        "\nFax: " + kos.getString("FAX") +
        "\n" + kos.getString("EMAIL");
  }
  
  public String getGETRODOB() {
    String cached = cache.getValue("GETRODOB", Integer.toString(ds.getInt("CPAR")));
    if (cached != null) return cached;
    
    return cache.returnValue(getKoString(
        Condition.equal("CKO", Integer.parseInt(sifracko))));
  }
  
  public String getLABGETRODOB() {
    String getro = getGETRODOB();
    if (getro == null || getro.trim().length() == 0) return "";
    
    return "Kontakt osoba dobavlja\u010Da";
  }
  
  
  public String getTEMP(){
    return "";
  }
}
