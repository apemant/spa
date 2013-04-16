/****license*****************************************************************
**   file: raIZD.java
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
import hr.restart.baza.RN;
import hr.restart.baza.VTRnl;
import hr.restart.baza.dM;
import hr.restart.baza.stdoki;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raTransaction;
import hr.restart.util.startFrame;

import java.awt.Frame;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.ReadWriteRow;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jb.util.TriStateProperty;

public class raIZD extends raIzlazTemplate  {

  private QueryDataSet realStavke ;
  private QueryDataSet realStavkeGreska;
  private Column cart ;
  private Column cartzam ;
  private Column kol;
  private Column kolzam;
  private Column status ;
  private Column rbsid ;
  private Column branch ;
  private boolean indikator = true;
  private hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  private boolean okSnimiti = false;

  private void initRealStavke(){
    realStavke = new QueryDataSet();
    realStavkeGreska = new QueryDataSet();
    cart = dm.getStdoki().getColumn("CART").cloneColumn();
    cart.setCaption("Artikl");
    cartzam = dm.getStdoki().getColumn("CART").cloneColumn();
    cartzam.setColumnName("CARTZAM");
    cartzam.setCaption("Zamjenski artikl");
    kol = dm.getStdoki().getColumn("KOL").cloneColumn();
    kolzam = dm.getStdoki().getColumn("KOL").cloneColumn();
    kolzam.setColumnName("KOLZAM");
    kolzam.setCaption("Zamjenska koli\u010Dina");
    status = dm.getStdoki().getColumn("STATUS").cloneColumn();
    status.setVisible(dm.getStdoki().getColumn("LOKK").getVisible());
    rbsid = dm.getStdoki().getColumn("RBSID").cloneColumn();
    rbsid.setVisible(dm.getStdoki().getColumn("LOKK").getVisible());
    branch = dM.createStringColumn("BRANCH", 120);
    branch.setVisible(0);
    realStavke.setColumns(new Column[] {cart,cartzam,kol,kolzam,status,rbsid,branch});
    realStavkeGreska.setColumns(realStavke.cloneColumns());
    realStavke.open();
    realStavkeGreska.open();
  }

  public void initialiser() {
    what_kind_of_dokument = "IZD";
    initRealStavke();
  }

  public void MyaddIspisMaster(){
    raMaster.getRepRunner().addReport("hr.restart.robno.repIzdatnica","hr.restart.robno.repIzlazni","Izdatnica","Izdatnice");
    raMaster.getRepRunner().addReport("hr.restart.robno.repIzdatnicaExtendedVersion","hr.restart.robno.repIzlazni","IzdatnicaExtendedVersion","Izdatnice s vrijednostima");
    raMaster.getRepRunner().addReport("hr.restart.robno.repIzdatnicaIntRacun","hr.restart.robno.repIzlazni","IzdatnicaIntRacun","Izdatnica - interni raèun");
  }

  public void MyaddIspisDetail(){
    raDetail.getRepRunner().addReport("hr.restart.robno.repIzdatnica","hr.restart.robno.repIzlazni","Izdatnica","Izdatnica");
    raDetail.getRepRunner().addReport("hr.restart.robno.repIzdatnicaExtendedVersion","hr.restart.robno.repIzlazni","IzdatnicaExtendedVersion","Izdatnica s vrijednostima");
    raDetail.getRepRunner().addReport("hr.restart.robno.repIzdatnicaIntRacun","hr.restart.robno.repIzlazni","IzdatnicaIntRacun","Izdatnica - interni raèun");
  }

  public void zamraciMaster(DataSet ds){}
  public void zamraciDetail(DataSet ds){

	ds.getColumn("CRADNAL").setVisible(TriStateProperty.TRUE);
	ds.getColumn("UPRAB").setVisible(TriStateProperty.TRUE);
	ds.getColumn("UIRAB").setVisible(TriStateProperty.TRUE);
	ds.getColumn("UPZT").setVisible(TriStateProperty.TRUE);
	ds.getColumn("UIZT").setVisible(TriStateProperty.TRUE);
	ds.getColumn("FC").setVisible(TriStateProperty.TRUE);
	ds.getColumn("INETO").setVisible(TriStateProperty.TRUE);
	ds.getColumn("FVC").setVisible(TriStateProperty.TRUE);
	ds.getColumn("IPRODBP").setVisible(TriStateProperty.TRUE);
	ds.getColumn("POR1").setVisible(TriStateProperty.TRUE);
	ds.getColumn("POR2").setVisible(TriStateProperty.TRUE);
	ds.getColumn("POR3").setVisible(TriStateProperty.TRUE);
	ds.getColumn("FMC").setVisible(TriStateProperty.TRUE);
	ds.getColumn("IPRODSP").setVisible(TriStateProperty.TRUE);
	ds.getColumn("NC").setVisible(TriStateProperty.TRUE);
	ds.getColumn("INAB").setVisible(TriStateProperty.TRUE);
	ds.getColumn("IMAR").setVisible(TriStateProperty.TRUE);
	ds.getColumn("VC").setVisible(TriStateProperty.TRUE);
	ds.getColumn("IBP").setVisible(TriStateProperty.TRUE);
	ds.getColumn("IPOR").setVisible(TriStateProperty.TRUE);
	ds.getColumn("MC").setVisible(TriStateProperty.TRUE);
	ds.getColumn("ZC").setVisible(TriStateProperty.TRUE);
	ds.getColumn("ISP").setVisible(TriStateProperty.TRUE);
	ds.getColumn("IRAZ").setVisible(TriStateProperty.TRUE);
	ds.getColumn("BRPRI").setVisible(TriStateProperty.TRUE);
	ds.getColumn("RBRPRI").setVisible(TriStateProperty.TRUE);
	ds.getColumn("PPOR1").setVisible(TriStateProperty.TRUE);
	ds.getColumn("PPOR2").setVisible(TriStateProperty.TRUE);
	ds.getColumn("PPOR3").setVisible(TriStateProperty.TRUE);
	ds.getColumn("CARTNOR").setVisible(TriStateProperty.TRUE);
	ds.getColumn("FMCPRP").setVisible(TriStateProperty.TRUE);
	ds.getColumn("REZKOL").setVisible(TriStateProperty.TRUE);
	ds.getColumn("VEZA").setVisible(TriStateProperty.TRUE);
	ds.getColumn("ID_STAVKA")
			.setVisible(TriStateProperty.TRUE);

}
  
  
  
  public raIZD() {

    setPreSel((jpPreselectDoc) presIZD.getPres());
    raMaster.addOption(rnvFisk, 5, false);
    master_titel = "Izdatnice";
    detail_titel_mno = "Stavke izdatnice";
    detail_titel_jed = "Stavka izdatnice";
    zamraciMaster(dm.getZagIzd());
    zamraciDetail(dm.getStIzd());    
    setMasterSet(dm.getZagIzd());
    setDetailSet(dm.getStIzd());
    setMasterDeleteMode(DELDETAIL);
    MP.BindComp();
    DP.BindComp();
    ConfigViewOnTable();
    //raMaster.addOption(rnvFisk, 5, false);
  }
	public void ConfigViewOnTable() {
	    setVisibleColsMaster(new int[] {4,5,12,13,29});
	    setVisibleColsDetail(new int[] {4,Aut.getAut().getCARTdependable(5,6,7),8,9,11,33,34});
	}
  public boolean DodatnaValidacijaDetail() {
    getDetailSet().setString("CRADNAL", getMasterSet().getString("CRADNAL"));
    return true;
  }
  public void RestPanelSetup(){
    DP.addRestOTP();
  }
  public void RestPanelMPSetup(){
//    MP.setupDrai();
  }
  public void SetFocusMasterBefore() {
    if ("D".equalsIgnoreCase(hr.restart.sisfun.frmParam.getParam("robno","useRadniNalozi",
                 "D","D ako se koriste radni nalozi inaèe slobodan upis"))){
      MP.panelBasic.jpRN.init(getMasterSet().getString("CRADNAL"));      
    }

  }
  public void CORGafter_lookUp(){
//    if (!MP.jrfCORG.getText().equals(""))
    MP.panelBasic.jpRN.setDefaultCORG(MP.panelBasic.jrfCORG.getText());
  }


  public void AfterAfterSaveMaster(char mode) {

    if (mode=='N' && !MP.panelBasic.jpRN.getCRADNAL().equals("")) {
      raMaster.setMode('I');
      raMaster.AfterAfterSave('I');
      jBStavke_actionPerformed(null);
      raMaster.jeprazno();
    }
    else {
      superAfterAfterSaveMaster(mode);
    }
  }

  public boolean isStanjeExist4allDS(){
    return isStanjeExist4allDS(realStavke, realStavkeGreska, getMasterSet().getString("GOD"), getMasterSet().getString("CSKL"), false);
  }
  public static boolean isStanjeExist4allDS(QueryDataSet s_realStavke, QueryDataSet s_realStavkeGreska, String god, String cskl, boolean sumcheck) {
    return isStanjeExist4allDS(s_realStavke, s_realStavkeGreska, god, cskl, sumcheck, true);
  }
  /**
   * Metoda je losa ali koristi kod prijema podataka iz vana u skladiste koje se vodi po NC-u 
   * Poziva se u isStanjeExist4allDS(ds, ds, String, String, boolean, boolean, boolean) ako je 3. boolean (fillstvalues) true
   * @param stavka
   * @param stanje
   */
  public static void add_stotp_values(ReadWriteRow stavka, ReadRow stanje) {
    stavka.setBigDecimal("NC",stanje.getBigDecimal("NC"));
    stavka.setBigDecimal("INAB",stavka.getBigDecimal("NC").multiply(stavka.getBigDecimal("KOL")));
    stavka.setBigDecimal("VC",stanje.getBigDecimal("VC"));
    stavka.setBigDecimal("IBP",stavka.getBigDecimal("VC").multiply(stavka.getBigDecimal("KOL")));
    stavka.setBigDecimal("MC",stanje.getBigDecimal("MC"));
    stavka.setBigDecimal("ISP",stavka.getBigDecimal("MC").multiply(stavka.getBigDecimal("KOL")));
    stavka.setBigDecimal("ZC",stavka.getBigDecimal("NC"));//!!! samo ako je po prosjecnoj nabavnoj !!!!!!
    stavka.setBigDecimal("IRAZ",stavka.getBigDecimal("ZC").multiply(stavka.getBigDecimal("KOL")));
    stavka.setString("CSKLART",stavka.getString("CSKL"));
  }
  
  public static boolean isStanjeExist4allDS(QueryDataSet s_realStavke, QueryDataSet s_realStavkeGreska, String god, String cskl, boolean sumcheck, boolean deterr) {
    return isStanjeExist4allDS(s_realStavke, s_realStavkeGreska, god, cskl, sumcheck, deterr, false);
  }
  public static boolean isStanjeExist4allDS(QueryDataSet s_realStavke, QueryDataSet s_realStavkeGreska, String god, String cskl, boolean sumcheck, boolean deterr, boolean fillstvalues) {
    allStanje s_AST = allStanje.getallStanje();
    HashMap hm= new HashMap() {
//      public Object put(Object key, Object value) {
//        System.out.println("*** hm.put("+key+","+value+")");
//        return super.put(key, value);
//      }
    };
    boolean returnValue = true;
    s_realStavkeGreska.emptyAllRows();
//    StorageDataSet tmp_realStavkeGreska = s_realStavkeGreska.cloneDataSetStructure();
//    tmp_realStavkeGreska.open();
    for (s_realStavke.first();s_realStavke.inBounds();s_realStavke.next()){

      if (raVart.isUsluga(s_realStavke.getInt("CART"))) continue;
      
      s_AST.findStanjeUnconditional(god/*getMasterSet().getString("GOD")*/,
      cskl /*getMasterSet().getString("CSKL")*/,
      s_realStavke.getInt("CART"));
      
      if(hm.containsKey(String.valueOf(s_realStavke.getInt("CART")))){
      	BigDecimal tmpBD = (BigDecimal)hm.get(String.valueOf(s_realStavke.getInt("CART"))); 
      	tmpBD = tmpBD.add(s_realStavke.getBigDecimal("KOL"));
      	hm.put(String.valueOf(s_realStavke.getInt("CART")),tmpBD);
      } else {
      	hm.put(String.valueOf(s_realStavke.getInt("CART")),s_realStavke.getBigDecimal("KOL"));
      }
      BigDecimal kolicinaBD = 
      	(BigDecimal)hm.get(String.valueOf(s_realStavke.getInt("CART")));
      
      if (fillstvalues && s_AST.gettrenSTANJE().getRowCount()>0) {
        add_stotp_values(s_realStavke, s_AST.gettrenSTANJE());
      }
      
      if (s_AST.gettrenSTANJE().getRowCount()==0)  {
        s_realStavke.setString("STATUS","B");
        if (!sumcheck) {
          s_realStavkeGreska.insertRow(true);
          dM.copyColumns(s_realStavke,s_realStavkeGreska);
        }
        returnValue = false;
//      }else if (AST.gettrenSTANJE().getBigDecimal("KOL").subtract(realStavke.getBigDecimal("KOL")).doubleValue()<0 ) {
      }	else if (s_AST.gettrenSTANJE().getBigDecimal("KOL").subtract(kolicinaBD).doubleValue()<0 ) {        
      	s_realStavke.setString("STATUS","M");
        if (!sumcheck) {
          s_realStavkeGreska.insertRow(true);
          dM.copyColumns(s_realStavke,s_realStavkeGreska);
        }
        returnValue = false;
      }
    }
    if (!returnValue && sumcheck) {//check suma jer mozda je samo trenutno otislo stanje u minus pa se poslije vratilo (storno)
      for (Iterator iterator = hm.keySet().iterator(); iterator.hasNext();) {
        returnValue = true;
        String cart = (String) iterator.next();
        BigDecimal kol = (BigDecimal)hm.get(cart);
        int icart = Integer.parseInt(cart);
        s_AST.findStanjeUnconditional(god,cskl,icart);
        if (s_AST.gettrenSTANJE().getRowCount()==0 && kol.signum() != 0)  {
          System.out.println("isStanjeExist4allDS.sumcheck :: Nema stanja za "+god+"-"+cskl+" artikl "+cart);
          returnValue = false;
        } else if (s_AST.gettrenSTANJE().getBigDecimal("KOL").subtract(kol).doubleValue()<0) {
          System.out.println("isStanjeExist4allDS.sumcheck :: Ipak ide u minus "+god+"-"+cskl+" artikl "+cart+
              "\n           stanjeKOL = "+s_AST.gettrenSTANJE().getBigDecimal("KOL")+";" +
              "\n           trazenKOl = "+kol);
          returnValue = false;          
        }
        if (!returnValue) {
          if (deterr) {
            for (s_realStavke.first(); s_realStavke.inBounds(); s_realStavke.next()) {
              if (s_realStavke.getInt("CART") == icart) {
                s_realStavkeGreska.insertRow(false);
                dM.copyColumns(s_realStavke, s_realStavkeGreska);
                s_realStavkeGreska.post();
              }
            }
          } else {
            //kumulativno
            lookupData.getlookupData().raLocate(s_realStavke, "CART", cart);
            s_realStavkeGreska.insertRow(false);
            dM.copyColumns(s_realStavke, s_realStavkeGreska);
            s_realStavkeGreska.setInt("BRDOK", 0);
            s_realStavkeGreska.setShort("RBR", (short)0);
            s_realStavkeGreska.setBigDecimal("KOL", kol);
            s_realStavkeGreska.post();
          }
        }
      }
    } else if (!returnValue){
//      for (tmp_realStavkeGreska.first(); tmp_realStavkeGreska.inBounds(); tmp_realStavkeGreska.next()) {
//        s_realStavkeGreska.insertRow(false);
//        dM.copyColumns(tmp_realStavkeGreska, s_realStavkeGreska);
//        s_realStavkeGreska.post();
//      }
    }
    return s_realStavkeGreska.getRowCount() == 0;
  }

  public boolean isStanjeExist4allDSwithMSG(){
    // Postoje artikli koji nepostoje na skladištu želite li nastaviti
    return (javax.swing.JOptionPane.showConfirmDialog(null,"Za neke odabrane artikle iz RN-a "+
        "ne postoje zalihe na skladištu. "+
        "Ako nastavite biti \u0107e ponu\u0111eno da odaberete zamijenske artikle. Želite li nastaviti ? ","Upozorenje",
        javax.swing.JOptionPane.YES_NO_OPTION,javax.swing.JOptionPane.WARNING_MESSAGE)==0);
  }

  public boolean rastaviSve(DataSet ds){

    realStavke.open();
    realStavke.emptyAllRows();
    for (ds.first();ds.inBounds();ds.next()) {
      realStavke.insertRow(true);
      realStavke.setInt("CART",ds.getInt("CART"));
      realStavke.setInt("CARTZAM",ds.getInt("CART"));
      realStavke.setBigDecimal("KOL",ds.getBigDecimal("KOL"));
      realStavke.setBigDecimal("KOLZAM",ds.getBigDecimal("KOL"));
      realStavke.setInt("RBSID",ds.getInt("RBSID"));
      realStavke.setString("BRANCH",ds.getString("BRANCH"));
    }
    return true;
  }

  public boolean testandreplaceStanje() {

    indikator = true;
    if (isStanjeExist4allDS()) return true;
    else {
      if (isStanjeExist4allDSwithMSG()) {
        raZamjenaArtikla pero =  new raZamjenaArtikla(
        		(Frame) raMaster.getFrameOwner(), realStavkeGreska,realStavke,getMasterSet().getString("CSKL")){
          public void setIndikator(boolean indi) {
            indikator = indi;
          }
          public boolean testStanja(int cart,java.math.BigDecimal kolicina){
            AST.findStanjeUnconditional(getMasterSet().getString("GOD"),
                                        getMasterSet().getString("CSKL"), cart);
            if (AST.gettrenSTANJE().getRowCount()==0)  {
              return false;
            }
            else if (AST.gettrenSTANJE().getBigDecimal("KOL").subtract(kolicina).doubleValue()<0 ) {
              return false;
            }
            return true;
          }
        };
        startFrame.getStartFrame().centerFrame(pero,0,"Zamjenski artikli");
        pero.setVisible(true);

        return indikator;
      }
      else return false;
    }
  }

  public String sviArtikli(){
    String zarez="";
    String upit = "and cart in (";

    for (realStavke.first();realStavke.inBounds();realStavke.next()) {
      zarez = realStavke.getRow()==realStavke.getRowCount()-1?"'":"',";
      upit = upit +"'"+realStavke.getInt("CARTZAM") +zarez;
    }
    upit = upit+") ";
    return upit;
  }

  public boolean unesiStavke() {
    if (realStavke==null || realStavke.getRowCount()==0) return false;
    try {
      short myrbr = 1;
      realStavke.first();
      getDetailSet().open();
      dm.getSklad().open();

      QueryDataSet svastanja = hr.restart.util.Util.getNewQueryDataSet("SELECT * from stanje where "+
          "god='"+getMasterSet().getString("GOD")+"' and cskl='"+getMasterSet().getString("CSKL")+"' "+
          sviArtikli(),true);

      QueryDataSet skladiste = hr.restart.util.Util.getNewQueryDataSet("SELECT * from sklad where "+
          "cskl='"+getMasterSet().getString("CSKL")+"' ",true);

      // (ab.f) vezna tablica, te stavke radnog naloga
      QueryDataSet vti = VTRnl.getDataModule().getTempSet(Condition.equal("CRADNAL",
                                               MP.panelBasic.jpRN.getCRADNAL()));
      vti.open();
      
      QueryDataSet stavkeRNL = stdoki.getDataModule().getTempSet(Condition.equal("CRADNAL",
          MP.panelBasic.jpRN.getCRADNAL()).and(Condition.equal("VRDOK", "RNL")));
      stavkeRNL.open();
      

      for (realStavke.first();realStavke.inBounds();realStavke.next()) {

        if (realStavke.getString("STATUS").equalsIgnoreCase("Z") || 
            raVart.isUsluga(realStavke.getInt("CART"))) {
          if (lookupData.getlookupData().raLocate(vti, new String[] {"RBSRNL", "BRANCH"},
              new String[] {String.valueOf(realStavke.getInt("RBSID")), realStavke.getString("BRANCH")}))
            vti.setInt("RBSIZD", 0);
          continue;
        }

        if (!lD.raLocate(svastanja,new String[] {"CART"},
                                   new String[]{String.valueOf(realStavke.getInt("CARTZAM"))})){
System.out.println("nisam nasao "+realStavke.getInt("CARTZAM"));
        return false;
      }

      rKD.stanje.Init();
      lc.TransferFromDB2Class(svastanja,rKD.stanje);
      rKD.stanje.sVrSklad=skladiste.getString("VRZAL");
      rKD.stavkaold.Init();
      rKD.stavka.Init();
      rKD.stavka.kol = realStavke.getBigDecimal("KOLZAM");
      getDetailSet().insertRow(false);
      getDetailSet().setString("CSKL",getMasterSet().getString("CSKL"));
      getDetailSet().setString("CSKLART",getMasterSet().getString("CSKL"));
      getDetailSet().setString("GOD",getMasterSet().getString("GOD"));
      getDetailSet().setString("VRDOK",getMasterSet().getString("VRDOK"));
      getDetailSet().setInt("BRDOK",getMasterSet().getInt("BRDOK"));
      getDetailSet().setShort("RBR",(short) myrbr++ );
      getDetailSet().setInt("RBSID", (int) getDetailSet().getShort("RBR"));

      getDetailSet().setInt("CART",realStavke.getInt("CARTZAM"));
      if (hr.restart.util.lookupData.getlookupData().
          raLocate(dm.getAllArtikli(),new String[] {"CART"},
      new String[] {String.valueOf(realStavke.getInt("CARTZAM"))})){
System.out.println(dm.getAllArtikli().getString("NAZART"));      	
      	
        getDetailSet().setString("CART1",dm.getAllArtikli().getString("CART1"));
        getDetailSet().setString("BC",dm.getAllArtikli().getString("BC"));
        getDetailSet().setString("NAZART",dm.getAllArtikli().getString("NAZART"));
        getDetailSet().setString("JM",dm.getAllArtikli().getString("JM"));
      }
      getDetailSet().setBigDecimal("KOL",realStavke.getBigDecimal("KOLZAM"));
      getDetailSet().setString("CRADNAL",MP.panelBasic.jpRN.getCRADNAL());
//      getDetailSet().setTimestamp("CRADNAL",MP.panelBasic.jpRN.g.getCRADNAL());
//      getDetailSet().setInt("CARTNOR",realStavke.getInt("CARTNOR"));  // cart iz radnog
      getDetailSet().setInt("RBSRN",realStavke.getInt("RBSID"));
      
      // napuni id stavke (ab.f)
      getDetailSet().setString("ID_STAVKA",
          raControlDocs.getKey(getDetailSet(), new String[] { "cskl",
              "vrdok", "god", "brdok", "rbsid" }, "stdoki"));
      // napuni vezu u veznoj tablici
      if (lookupData.getlookupData().raLocate(vti, new String[] {"RBSRNL", "BRANCH"},
          new String[] {String.valueOf(realStavke.getInt("RBSID")), realStavke.getString("BRANCH")})) {
        vti.setInt("RBSIZD", getDetailSet().getInt("RBSID"));
        vti.setString("VEZA", getDetailSet().getString("ID_STAVKA"));
      }

      
      // napuni polje veza sa id_stavkom pripadajuce stavke radnog naloga.
      // uz pomoc CRADNAL i RBSID. DataSet je pripremljen prethodno za sve stavke ovog RNL.
      if (lookupData.getlookupData().raLocate(stavkeRNL, "RBSID", 
          Integer.toString(realStavke.getInt("RBSID")))) {
        if (stavkeRNL.getString("ID_STAVKA").length() > 0)
          getDetailSet().setString("VEZA", stavkeRNL.getString("ID_STAVKA"));
        else getDetailSet().setString("VEZA",
            raControlDocs.getKey(stavkeRNL, new String[] { "cskl",
                "vrdok", "god", "brdok", "rbsid" }, "stdoki"));
      } else System.err.println("Ne mogu pronaci stavku RNL!! "+
          getDetailSet().getString("CRADNAL")+ ": "+getDetailSet().getInt("RBSRN"));
      
      
      
      lc.TransferFromDB2Class(svastanja,rKD.stanje);
//    rKD.setVrzal(vrzal);
      rKD.KalkulacijaStavke(what_kind_of_dokument,"KOL",raDetail.getMode(),
                            getMasterSet().getString("CSKL"),false);
      rKD.KalkulacijaStanje(what_kind_of_dokument);
      lc.TransferFromClass2DB(getDetailSet(),rKD.stavka);

//      Kalkulacija("KOL");
//      if (rKD.TestStanje()==0) {
      lc.TransferFromClass2DB(svastanja,rKD.stanje);
      lc.TransferFromClass2DB(getDetailSet(),rKD.stavka);
      if (svastanja.getBigDecimal("KOL").doubleValue()<0){
//      if (svastanja.getBigDecimal("KOL").subtract(svastanja.getBigDecimal("KOLREZ")).doubleValue()<0){
          // ko hebe rezervaciju !!!
//        javax.swing.JOptionPane.showMessageDialog(null,
//                "Sranje zbog stanja nema ga dovoljno ! "
//        		+svastanja.getInt("CART")+"-"+svastanja.getString("CSKL"),
//                "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
System.out.println("Greška jer nema stanja "+svastanja.getInt("CART")+"-"+svastanja.getString("CSKL"));      	
      	
      	return false;
      }
      rCD.unosIzlaz(getDetailSet(),svastanja);
      }
//        ST.prn(qdsrn);
/*      QueryDataSet myRN  = hr.restart.util.Util.getNewQueryDataSet("SELECT * from stdoki where vrdok='RNL' and "+
          "cradnal = '"+MP.panelBasic.jpRN.getCRADNAL()+"'",true);

      for (myRN.first();myRN.inBounds();myRN.next()) {
        QueryDataSet tmpRastav = Aut.getAut().expandArt(myRN.getInt("CART"),myRN.getBigDecimal("KOL"),true);
        QueryDataSet izdatnice =
        hr.restart.util.Util.getNewQueryDataSet("select * from stdoki where cradnal='"+
                MP.panelBasic.jpRN.getCRADNAL()+"' and cartnor="+myRN.getInt("CART")+" and vrdok='IZD' "+
                 " and rbsrn = "+myRN.getInt("RBSRN"),true);

        for (tmpRastav.first();tmpRastav.inBounds();tmpRastav.next()){
          if (!hr.restart.util.lookupData.getlookupData().raLocate(izdatnice,new String[] {"CART"},
              new String[] {String.valueOf(tmpRastav.getInt("CART"))})){
              break;
           }
           myRN.setString("STATUS","P");
        }
      } */
      if (autoStatus) {
        boolean doClose = true;
        for (vti.first(); vti.inBounds(); vti.next())
          if (vti.getInt("RBSIZD") < 0) doClose = false;
        if (doClose) {
          QueryDataSet rnt = RN.getDataModule().getTempSet(
              Condition.equal("CRADNAL", MP.panelBasic.jpRN.getCRADNAL()));
          rnt.open();
          if (rnt.rowCount() == 1) {
            if (rnt.getString("STATUS").equals("P")) {
              rnt.setString("STATUS", "O");
              rnt.setString("CUSEROBRAC", raUser.getInstance().getUser());
              rnt.setTimestamp("DATUMO", Valid.getValid().getToday());
            } else if (rnt.getString("STATUS").equals("F")) {
              rnt.setString("STATUS", "Z");
              rnt.setTimestamp("DATUMZ", Valid.getValid().getToday());
            }
            raTransaction.saveChanges(rnt);
          }
        }
      }
System.out.println("Savam sranja "+svastanja.getString("CSKL")+"-"+svastanja.getInt("CART"));      
      raTransaction.saveChanges(svastanja);
      raTransaction.saveChanges(getDetailSet());
      raTransaction.saveChanges(vti);
//      raTransaction.saveChanges(myRN);
      return true;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

  // ab.f  azuriranje stavke RNL
  protected boolean AdditionalDeleteDetail() {
    try {
      QueryDataSet vti = VTRnl.getDataModule().getTempSet(Condition.equal("CRADNAL", delCradnal)
         .and(Condition.equal("RBSRNL", delRbsrn)));
      vti.open();
      if (lookupData.getlookupData().raLocate(vti, "RBSIZD", String.valueOf(delRbsid))) {
        vti.setInt("RBSIZD", -1);
        raTransaction.saveChanges(vti);
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean extrasave() {

    if (okSnimiti){ return unesiStavke();}
      return true;
  }

  DataSet qdsrn = null;

  public void afterOK(){
    int i=-1;
    qdsrn = MP.panelBasic.jpRN.getStavkeRNL();
    if (qdsrn.getRowCount()==0){
      return;
    }

    if (rastaviSve(qdsrn)) {
      if (testandreplaceStanje()) {
        okSnimiti = true;
      }
      else {
        javax.swing.JOptionPane.showMessageDialog(null,
            "Radni nalog nije prenesen !",
            "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
        okSnimiti = false;
      }
    } else {
      javax.swing.JOptionPane.showMessageDialog(null,
      "Radni nalog nije prenesen ! Postoje artikli oznaèeni kao proizvod a nemaju normativ !!!",
      "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
      okSnimiti = false;
    }
  }

  public void SetFocusNoviExtends(){
    okSnimiti = false;
    if (MP.panelBasic.jrfCORG.getText().equals("")){
      MP.panelBasic.jrfCORG.requestFocus();
    }
    else {
      MP.panelBasic.jrfCORG.forceFocLost();
      MP.panelBasic.jrfCVRTR.requestFocus();
    }
    rcc.setLabelLaF(MP.panelBasic.jrfCORG,true);
    rcc.setLabelLaF(MP.panelBasic.jrfNAZORG,true);
    rcc.setLabelLaF(MP.panelBasic.jbCORG,true);
    rcc.EnabDisabAll(MP.panelBasic.jpRN,true);
    rcc.setLabelLaF(MP.panelBasic.jrfCVRTR,true);
    rcc.setLabelLaF(MP.panelBasic.jrfNAZVRTR,true);
    rcc.setLabelLaF(MP.panelBasic.jbCVRTR,true);
    MP.panelBasic.jpRN.setGod(getMasterSet().getString("GOD"));
  }

  public void SetFocusIzmjena(){
    MP.EnabSetup();
//    rcc.setLabelLaF(MP.panelBasic.jrfCVRTR,false);
//    rcc.setLabelLaF(MP.panelBasic.jrfNAZVRTR,false);
//    rcc.setLabelLaF(MP.panelBasic.jbCVRTR,false);    
    if (MP.panelBasic.jpRN.getCRADNAL().equalsIgnoreCase("")) {
//      rcc.EnabDisabAll(MP.panelBasic.jpRN,false);
      MP.panelBasic.jrfCORG.requestFocus();
    } else {
      rcc.setLabelLaF(MP.panelBasic.jrfCORG,false);
      rcc.setLabelLaF(MP.panelBasic.jrfNAZORG,false);
      rcc.setLabelLaF(MP.panelBasic.jbCORG,false);
      rcc.EnabDisabAll(MP.panelBasic.jpRN,false);
      MP.panelBasic.jrfCVRTR.requestFocus();
    }
  }

  public boolean validRealRN(){

    getMasterSet().setString("CRADNAL", MP.panelBasic.jpRN.getCRADNAL());
    QueryDataSet rn_tmp = hr.restart.util.Util.getNewQueryDataSet(
                    "select * from rn where cradnal='"+MP.panelBasic.jpRN.getCRADNAL()+"'",true);
    getMasterSet().setTimestamp("DATRADNAL", rn_tmp.getTimestamp("DATDOK"));
    rn_tmp = null;
    if (!MP.panelBasic.jpRN.getCRADNAL().equals("")) {
      MP.panelBasic.jpRN.setCSKL(getMasterSet().getString("CSKL"));
      if (!MP.panelBasic.jpRN.copyRNL()){
        javax.swing.JOptionPane.showMessageDialog(null,
            "Za ovaj radni nalog sve su stavke prenešene, nemam što prenijeti !",
            "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    return true;
  }

  public boolean LocalDeleteCheckDetail() {
    if (getDetailSet().getString("VEZA").trim().length() == 0) return true;
    DataSet rn = RN.getDataModule().getTempSet(Condition.equal("CRADNAL", getDetailSet()));
    rn.open();
    if (rn.rowCount() > 0 && !rn.getString("STATUS").equals("P")) {
      JOptionPane.showMessageDialog(raDetail.getWindow(),
          "Stavka je nastala iz radnog naloga koji je "+
          (rn.getString("STATUS").equals("Z") ? "zatvoren!" : "obraðen!"), 
              "Brisanje nije moguæe", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }
  
  public boolean LocalValidacijaMaster(){
//     okSnimiti = false;
    if (hr.restart.util.Valid.getValid().isEmpty(MP.panelBasic.jrfCORG)) return false;
    if (hr.restart.sisfun.frmParam.getParam("robno","useVRtros",
            "D","Provjera vrste troška obavezna").equalsIgnoreCase("D")){
System.out.println("asdad");    	
       if (hr.restart.util.Valid.getValid().isEmpty(MP.panelBasic.jrfCVRTR)) return false;
    }
    if (!MP.panelBasic.jpRN.Validacija()) {
      return false;
    }
    
    if ("D".equalsIgnoreCase(hr.restart.sisfun.frmParam.getParam("robno","useRadniNalozi",
                 "D","D ako se koriste radni nalozi inaèe slobodan upis"))){
      return validRealRN();
    }
    
    return true;
  }

  // ab.f handlanje automatskog prebacivanja iz radnih naloga

  boolean autoStatus = false;
  public void beforeShowMaster() {
    super.beforeShowMaster();
    autoStatus = frmParam.getParam("rn", "autoRnStatusIzd", "D", "Automatsko zatvaranje " +
            "radnog naloga za servis kod kreiranja izdatnice (D,N)?").equalsIgnoreCase("D");
  }
  
  public void defaultIzdFinancPart(){
	getDetailSet().setBigDecimal("UPZT",Nula);
	getDetailSet().setBigDecimal("UIZT",Nula);
	getDetailSet().setBigDecimal("IPRODBP",Nula);
	getDetailSet().setBigDecimal("UIRAB",Nula);
	getDetailSet().setBigDecimal("POR1",Nula);
	getDetailSet().setBigDecimal("POR2",Nula);
	getDetailSet().setBigDecimal("POR3",Nula);
	getDetailSet().setBigDecimal("IPRODSP",Nula);
/*			
			getDetailSet().getBigDecimal("UIZT").
			add(getDetailSet().getBigDecimal("UIRAB")).
			add(getDetailSet().getBigDecimal("POR1")).
			add(getDetailSet().getBigDecimal("POR2")).
			add(getDetailSet().getBigDecimal("POR3")));
*/			
  	}
  
  public void kalkulacijaPorezaZaInternuFakturu(){
  	
	// kalkulacija poreza
	// upzt  ---> posto oporezive osnovice
	// uirab ---> neoporeziva osnovica
	// uizt  ---> oporeziva osnovica
	// iprodbp  ---> oporeziva+neoporeziva osnovica
	// por1  ---> iznos poreza1 na oporezivu osnovicu
	// por2  ---> iznos poreza1 na oporezivu osnovicu
	// por3  ---> iznos poreza1 na oporezivu osnovicu
	// iprodsp ---> uirab+uizt+por1+por2+por3
	
//System.out.println("kalkulacijaPorezaZaInternuFakturu()");
	String sqltros = "SELECT * FROM VRTROS WHERE CVRTR='"+
						getMasterSet().getString("CVRTR")+"'"; 
		QueryDataSet qdsvrtroska =hr.restart.util.Util.getNewQueryDataSet(
				sqltros,true); 
		if (qdsvrtroska.getRowCount()==0) {
			defaultIzdFinancPart();
			return;
		}		
		BigDecimal postoosnovice = qdsvrtroska.getBigDecimal("OSNOVICA");
		QueryDataSet qdsporez =hr.restart.util.Util.getNewQueryDataSet(
				"select * from artikli,porezi WHERE artikli.cpor = porezi.cpor and cart="
				+getDetailSet().getInt("CART"),true);
		BigDecimal postoporeza1 = 
			qdsporez.getBigDecimal("POR1").divide(Sto,2,BigDecimal.ROUND_HALF_UP);
		BigDecimal postoporeza2 = 
			qdsporez.getBigDecimal("POR2").divide(Sto,2,BigDecimal.ROUND_HALF_UP);
		BigDecimal postoporeza3 = 
			qdsporez.getBigDecimal("POR3").divide(Sto,2,BigDecimal.ROUND_HALF_UP);

		if (postoosnovice.compareTo(Nula)==0) {
			defaultIzdFinancPart();
			return;
		}
		getDetailSet().setBigDecimal("UPZT",postoosnovice);
		getDetailSet().setBigDecimal("UIZT",
				(getDetailSet().getBigDecimal("INAB").
						multiply(postoosnovice)).divide(Sto,2,BigDecimal.ROUND_HALF_UP));
		getDetailSet().setBigDecimal("UIRAB",
				getDetailSet().getBigDecimal("INAB").
					subtract(getDetailSet().getBigDecimal("UIZT")));
		getDetailSet().setBigDecimal("IPRODBP",getDetailSet().getBigDecimal("INAB"));
		getDetailSet().setBigDecimal("POR1",
				(getDetailSet().getBigDecimal("UIZT").
						multiply(postoporeza1)).setScale(2,BigDecimal.ROUND_HALF_UP));
		getDetailSet().setBigDecimal("POR2",
				(getDetailSet().getBigDecimal("UIZT").
							multiply(postoporeza2)).setScale(2,BigDecimal.ROUND_HALF_UP));
		getDetailSet().setBigDecimal("POR3",
				(getDetailSet().getBigDecimal("UIZT").
						multiply(postoporeza3)).setScale(2,BigDecimal.ROUND_HALF_UP));
		getDetailSet().setBigDecimal("IPRODSP",
				getDetailSet().getBigDecimal("UIZT").
				add(getDetailSet().getBigDecimal("UIRAB")).
				add(getDetailSet().getBigDecimal("POR1")).
				add(getDetailSet().getBigDecimal("POR2")).
				add(getDetailSet().getBigDecimal("POR3")));

  }
  
	public boolean doBeforeSaveDetail(char mode) {
		if (mode !='B'){
			kalkulacijaPorezaZaInternuFakturu();
		}
		return super.doBeforeSaveDetail(mode);
	}
  
}
