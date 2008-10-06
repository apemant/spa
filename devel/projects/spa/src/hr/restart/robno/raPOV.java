/****license*****************************************************************
**   file: raPOV.java
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

  import hr.restart.util.raTransaction;
import hr.restart.util.startFrame;

import java.awt.Frame;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

  public class raPOV extends raIzlazTemplate  {

    private QueryDataSet realStavke ;
    private QueryDataSet realStavkeGreska;
    private Column cart ;
    private Column cartnor ;
    private Column cartzam ;
    private Column kol;
    private Column kolzam;
    private Column status ;
    private Column rbsid ;
    private boolean indikator = true;
    private hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
    private boolean okSnimiti = false;

    private void initRealStavke(){
      realStavke = new QueryDataSet();
      realStavkeGreska = new QueryDataSet();
      cart = dm.getStdoki().getColumn("CART").cloneColumn();
      cart.setCaption("Artikl");
      cartnor = dm.getStdoki().getColumn("CART").cloneColumn();
      cartnor.setCaption("Proizvod");
      cartnor.setColumnName("CARTNOR");
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
      realStavke.setColumns(new Column[] {cart,cartnor,cartzam,kol,kolzam,status,rbsid});
      realStavkeGreska.setColumns(new Column[] {cart.cloneColumn(),
          cartnor.cloneColumn(),cartzam.cloneColumn(),kol.cloneColumn(),kolzam.cloneColumn(), status.cloneColumn(),rbsid.cloneColumn()});
      realStavke.open();
      realStavkeGreska.open();
    }

    public void initialiser() {
      what_kind_of_dokument = "POV";
      initRealStavke();
    }

    public void MyaddIspisMaster(){
      raMaster.getRepRunner().addReport("hr.restart.robno.repPovratnica","hr.restart.robno.repIzlazni","Povratnica","Povratnica");
      raMaster.getRepRunner().addReport("hr.restart.robno.repPovratnicaExtendedVersion","hr.restart.robno.repIzlazni","PovratnicaExtendedVersion","Povratnica s vrijednostima");
    }
    public void MyaddIspisDetail(){
      raDetail.getRepRunner().addReport("hr.restart.robno.repPovratnica","hr.restart.robno.repIzlazni","Povratnica","Povratnica");
      raDetail.getRepRunner().addReport("hr.restart.robno.repPovratnicaExtendedVersion","hr.restart.robno.repIzlazni","PovratnicaExtendedVersion","Povratnica s vrijednostima");
    }

    public raPOV() {
      setPreSel((jpPreselectDoc) presPOV.getPres());
      master_titel = "Povratnice";
      detail_titel_mno = "Stavke povratnice";
      detail_titel_jed = "Stavka povratnice";
      setMasterSet(dm.getZagPov());
      setDetailSet(dm.getStPov());
      MP.BindComp();
      DP.BindComp();
//      set_kum_detail(false);
      setVisibleColsMaster(new int[] {4,5,12,13,29});
      setVisibleColsDetail(new int[] {4,Aut.getAut().getCARTdependable(5,6,7),8,9,11,33,34});
    }

    public boolean DodatnaValidacijaDetail() {
//  Ante 17-07-2002  kopiram CRADNAL iz zaglavlja u stavke
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
     MP.panelBasic.jpRN.init(getMasterSet().getString("CRADNAL"));
    }
    public void CORGafter_lookUp(){
//    if (!MP.jrfCORG.getText().equals(""))
      MP.panelBasic.jpRN.setDefaultCORG(MP.panelBasic.jrfCORG.getText());
    }


    public void AfterAfterSaveMaster(char mode) {

      if (mode=='N' && !MP.panelBasic.jpRN.getCRADNAL().equals("")) {
        raMaster.setLockedMode('I');
        jBStavke_actionPerformed(null);
      }
      else {
        superAfterAfterSaveMaster(mode);
      }
    }

    public boolean isStanjeExist4allDS(){
      boolean returnValue = true;
      realStavkeGreska.emptyAllRows();
      for (realStavke.first();realStavke.inBounds();realStavke.next()){

        AST.findStanjeUnconditional(getMasterSet().getString("GOD"),
        getMasterSet().getString("CSKL"),
        realStavke.getInt("CART"));
        if (AST.gettrenSTANJE().getRowCount()==0)  {
          realStavke.setString("STATUS","B");
          realStavkeGreska.insertRow(true);
          dm.copyColumns(realStavke,realStavkeGreska);
          returnValue = false;
        }
        else if (AST.gettrenSTANJE().getBigDecimal("KOL").subtract(realStavke.getBigDecimal("KOL")).doubleValue()<0 ) {
          realStavke.setString("STATUS","M");
          realStavkeGreska.insertRow(true);
          dm.copyColumns(realStavke,realStavkeGreska);
          returnValue = false;
        }
      }
      return returnValue;
    }

    public boolean isStanjeExist4allDSwithMSG(){
      // Postoje artikli koji nepostoje na skladištu želite li nastaviti
      return (javax.swing.JOptionPane.showConfirmDialog(null,"Za neke odabrane artikle iz RN-a "+
          "ne postoje zalihe na skladištu. "+
          "Ako nastavite biti \u0107e ponu\u0111eno da odaberete zamijenske artikle. Želite li nastaviti ? ","Upozorenje",
          javax.swing.JOptionPane.YES_NO_OPTION,javax.swing.JOptionPane.WARNING_MESSAGE)==0);
    }

    public void rastaviSve(DataSet ds){

//    realStavke= null;
//    realStavke = new QueryDataSet();
      realStavke.open();
      realStavke.emptyAllRows();
      for (ds.first();ds.inBounds();ds.next()) {
        if (raVart.isStanje(ds.getInt("CART")) &&
            !raVart.isNorma(ds.getInt("CART"))) {
            //Aut.getAut().artTipa(ds.getInt("CART"),"RM")){
          realStavke.insertRow(true);
          realStavke.setInt("CART",ds.getInt("CART"));
          realStavke.setInt("CARTZAM",ds.getInt("CART"));
          realStavke.setInt("CARTNOR",ds.getInt("CART"));
          realStavke.setBigDecimal("KOL",ds.getBigDecimal("KOL"));
          realStavke.setBigDecimal("KOLZAM",ds.getBigDecimal("KOL"));
          realStavke.setInt("RBSID",ds.getInt("RBSID"));
        }
        else if (raVart.isNorma(ds.getInt("CART"))) {
            //Aut.getAut().artTipa(ds.getInt("CART"),"P")){
          QueryDataSet tmpRastav = Aut.getAut().expandArt(ds.getInt("CART"),ds.getBigDecimal("KOL"),false);
          for (tmpRastav.first();tmpRastav.inBounds();tmpRastav.next()){
            if (raVart.isStanje(ds.getInt("CART"))) {
                //Aut.getAut().artTipa(tmpRastav.getInt("CART"),"PRM")) {
              realStavke.insertRow(true);
              realStavke.setInt("CART",tmpRastav.getInt("CART"));
              realStavke.setInt("CARTZAM",tmpRastav.getInt("CART"));
              realStavke.setInt("CARTNOR",ds.getInt("CART"));
              realStavke.setBigDecimal("KOL",tmpRastav.getBigDecimal("KOL"));
              realStavke.setBigDecimal("KOLZAM",tmpRastav.getBigDecimal("KOL"));
              realStavke.setInt("RBSID",ds.getInt("RBSID"));
            }
          }
        }
      }
    }

    public boolean testandreplaceStanje() {

      indikator = true;
      if (isStanjeExist4allDS()) return true;
      else {
        if (isStanjeExist4allDSwithMSG()) {
          raZamjenaArtikla pero =  new raZamjenaArtikla((Frame) raMaster.getFrameOwner(), realStavkeGreska,realStavke,getMasterSet().getString("CSKL")){
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
      try {
        short myrbr = 1;
        realStavke.first();
        getDetailSet().open();
        dm.getSklad().open();
        QueryDataSet svastanja = hr.restart.util.Util.getNewQueryDataSet("SELECT * from sklad, stanje where "+
            "sklad.cskl = stanje.cskl and god='"+getMasterSet().getString("GOD")+"' and cskl='"+getMasterSet().getString("CSKL")+"' "+
            sviArtikli(),true);

        for (realStavke.first();realStavke.inBounds();realStavke.next()) {

          if (!lD.raLocate(svastanja,new String[] {"CART"},
        new String[]{String.valueOf(realStavke.getInt("CARTZAM"))})){
          System.out.println("nisam nasao "+realStavke.getInt("CARTZAM"));
          ST.prn(svastanja);
          return false;
        }
        rKD.stanje.Init();
        lc.TransferFromDB2Class(svastanja,rKD.stanje);
        rKD.stanje.sVrSklad=svastanja.getString("VRZAL");
        rKD.stavkaold.Init();
        rKD.stavka.Init();
        rKD.stavka.kol = realStavke.getBigDecimal("KOL");
        getDetailSet().insertRow(true);
        getDetailSet().setString("CSKL",getMasterSet().getString("CSKL"));
        getDetailSet().setString("GOD",getMasterSet().getString("GOD"));
        getDetailSet().setString("VRDOK",getMasterSet().getString("VRDOK"));
        getDetailSet().setInt("BRDOK",getMasterSet().getInt("BRDOK"));
        getDetailSet().setShort("RBR",(short) myrbr++ );
        getDetailSet().setInt("RBSID", (int) getDetailSet().getShort("RBR"));
        getDetailSet().setInt("CART",realStavke.getInt("CARTZAM"));
        if (hr.restart.util.lookupData.getlookupData().
            raLocate(dm.getAllArtikli(),new String[] {"CART"},
        new String[] {String.valueOf(realStavke.getInt("CARTZAM"))})){
          getDetailSet().setString("CART1",dm.getAllArtikli().getString("CART1"));
          getDetailSet().setString("BC",dm.getAllArtikli().getString("BC"));
          getDetailSet().setString("NAZART",dm.getAllArtikli().getString("NAZART"));
          getDetailSet().setString("JM",dm.getAllArtikli().getString("JM"));
        }
        getDetailSet().setBigDecimal("KOL",realStavke.getBigDecimal("KOLZAM"));
        getDetailSet().setString("CRADNAL",MP.panelBasic.jpRN.getCRADNAL());
        getDetailSet().setInt("CARTNOR",realStavke.getInt("CART"));  // cart iz radnog
        getDetailSet().setInt("RBSRN",realStavke.getInt("RBSID"));
        Kalkulacija("KOL");

        if (rKD.TestStanje()==0) {
          lc.TransferFromClass2DB(AST.gettrenSTANJE(),rKD.stanje);
          lc.TransferFromClass2DB(getDetailSet(),rKD.stavka);
        }
        }
//        ST.prn(qdsrn);
        QueryDataSet myRN  = hr.restart.util.Util.getNewQueryDataSet("SELECT * from stdoki where vrdok='RNL' and "+
            "cradnal = '"+MP.panelBasic.jpRN.getCRADNAL()+"'",true);

        for (qdsrn.first();qdsrn.inBounds();qdsrn.next()) {
          if (hr.restart.util.lookupData.getlookupData().raLocate(myRN,new String[] {"RBSID"},
                      new String[] {String.valueOf(qdsrn.getInt("RBSID"))})){
                myRN.setString("STATUS","P");
          }
        }
        raTransaction.saveChanges(svastanja);
        raTransaction.saveChanges(getDetailSet());
        raTransaction.saveChanges(myRN);
        return true;
      }
      catch (Exception ex) {
        ex.printStackTrace();
        return false;
      }
    }

    public boolean extrasave() {

      if (okSnimiti){ unesiStavke();}
        return true;
    }

    DataSet qdsrn = null;
    public void afterOK(){
      int i=-1;
      qdsrn = MP.panelBasic.jpRN.getStavkeRNL();
      if (qdsrn.getRowCount()==0){
        return;
      }

      rastaviSve(qdsrn);

      if (testandreplaceStanje()) {
        okSnimiti = true;
      }
      else {
        javax.swing.JOptionPane.showMessageDialog(null,
            "Radni nalog nije prenesen !",
            "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
        okSnimiti = false;
      }
    }
    public void SetFocusNoviExtends(){
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
    }

    public void SetFocusIzmjena(){
      MP.EnabSetup();
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

    public boolean LocalValidacijaMaster(){
       okSnimiti = false;
      if (hr.restart.util.Valid.getValid().isEmpty(MP.panelBasic.jrfCORG)) return false;
      if (!MP.panelBasic.jpRN.Validacija()) {
        return false;
      }
      getMasterSet().setString("CRADNAL", MP.panelBasic.jpRN.getCRADNAL());
      if (!MP.panelBasic.jpRN.getCRADNAL().equals("")) {
        MP.panelBasic.jpRN.setCSKL(getMasterSet().getString("CSKL"));
        if (!MP.panelBasic.jpRN.copyRNL()){
          javax.swing.JOptionPane.showMessageDialog(null,
              "Za ovaj radni nalog sve su stavke prenešene, nemam što prenijeti !",
              "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
          return false;
        }

//      MP.panelBasic.jpRN.copyRNL();
        return okSnimiti;
      }
      return true;
    }
}
