/****license*****************************************************************
**   file: raGOT.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.raInputDialog;
import hr.restart.util.JlrNavField;
import hr.restart.util.raImages;
import hr.restart.util.raNavAction;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class raGOT extends raIzlazTemplate  {
  
  

  public void initialiser(){
    what_kind_of_dokument = "GOT";
//    this.raMaster.getNavBar().getColBean().setSaveName("hr.restart.robno.raGOT_m");
//    this.raDetail.getNavBar().getColBean().setSaveName("hr.restart.robno.raGOT_d");

//    (+4)
  }
  public void ConfigViewOnTable(){
//    this.setVisibleColsMaster(new int[] {4,5,6});
    this.setVisibleColsDetail(new int[]
      {4,Aut.getAut().getCARTdependable(5,6,7),8,11,42,12,23,24});
  }



  public void MyaddIspisMaster(){
    raMaster.getRepRunner().addReport("hr.restart.robno.repGotRac","hr.restart.robno.repIzlazni","GotRac","Gotovinski raèun 1 red");
    raMaster.getRepRunner().addReport("hr.restart.robno.repGotRac2","hr.restart.robno.repIzlazni","GotRac2","Gotovinski raèuni 2 red");
    raMaster.getRepRunner().addReport("hr.restart.robno.repGotRacBP","hr.restart.robno.repIzlazni","GotRacBP","Gotovinski raèun s cijenom bez poreza");
    
    raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniSKL","hr.restart.robno.repIzlazni","OTPGOT","Otpremnica");
    raMaster.getRepRunner().addReport("hr.restart.robno.repRacuniSKLVri","hr.restart.robno.repIzlazni","OTPGORvri","Otpremnica vrijednosna");
    
    raMaster.getRepRunner().addReport("hr.restart.robno.repMxRacun","Matrièni ispis raèuna");
    raMaster.getRepRunner().addReport("hr.restart.robno.repMxRacunPop","Matrièni ispis raèuna s više popusta");
    raMaster.getRepRunner().addReport("hr.restart.robno.repMxGRN", "Matrièni ispis raèuna Pos printer");
    if (repFISBIH.isFISBIH()) {
      if (getMasterSet().getInt("FBR")>0) {
        raMaster.getRepRunner().addReport("hr.restart.robno.repFISBIHDupli","Ispis DUPLIKATA FISKALNOG ra\u010Duna");
        raMaster.getRepRunner().addReport("hr.restart.robno.repFISBIHRekRN","REKLAMIRANJE FISKALNOG ra\u010Duna");
      } else raMaster.getRepRunner().addReport("hr.restart.robno.repFISBIHRN","FISKALNI ispis ra\u010Duna");
    }
    isMasterInitIspis = true;
  }

  public void MyaddIspisDetail(){
    raDetail.getRepRunner().addReport("hr.restart.robno.repGotRac","hr.restart.robno.repIzlazni","GotRac","Gotovinski raèun 1 red");
    raDetail.getRepRunner().addReport("hr.restart.robno.repGotRac2","hr.restart.robno.repIzlazni","GotRac2","Gotovinski raèuni 2 red");
    raDetail.getRepRunner().addReport("hr.restart.robno.repGotRacBP","hr.restart.robno.repIzlazni","GotRacBP","Gotovinski raèun s cijenom bez poreza");
    
    raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniSKL","hr.restart.robno.repIzlazni","OTPGOT","Otpremnica");
    raDetail.getRepRunner().addReport("hr.restart.robno.repRacuniSKLVri","hr.restart.robno.repIzlazni","OTPGORvri","Otpremnica vrijednosna");
    
    raDetail.getRepRunner().addReport("hr.restart.robno.repMxRacun","Matrièni ispis raèuna");
    raDetail.getRepRunner().addReport("hr.restart.robno.repMxRacunPop","Matrièni ispis raèuna s više popusta");
    raDetail.getRepRunner().addReport("hr.restart.robno.repMxGRN", "Matrièni ispis raèuna Pos printer");
//    if (repFISBIH.isFISBIH()) raDetail.getRepRunner().addReport("hr.restart.robno.repFISBIHRN","FISKALNI ispis ra\u010Duna");
    isDetailInitIspis = true;
  }
  
  KupacDialog kup = new KupacDialog();

  raNavAction rnvNacinPlac = new raNavAction("Na\u010Din pla\u0107anja",raImages.IMGEXPORT,java.awt.event.KeyEvent.VK_F7) {
      public void actionPerformed(ActionEvent e) {
          keyNacinPlac();
      }
  };
  
  raNavAction rnvKupHack = new raNavAction("Promijeni kupca",
      raImages.IMGPREFERENCES, java.awt.event.KeyEvent.VK_F11) {
    public void actionPerformed(ActionEvent e) {
      keyKupHack();
    }
  };

  public void keyNacinPlac(){
    frmPlacanje.entryRate(this);
  }
  
  public void keyKupHack() {
    if (getMasterSet().getRowCount() == 0) return;
    
    kup.setData(getMasterSet());
    if (kup.show(raMaster.getWindow())) {
      kup.update(getMasterSet());
      getMasterSet().saveChanges();
      raMaster.getJpTableView().fireTableDataChanged();
    }
  }

  public void ExitPointDetail(){

  }
/*
  public boolean checkAccess(){
    if (isKnjigen()) {setUserCheckMsg("Korisnik ne može promijeniti dokument jer je proknjižen !", false);
       return false;
    }
    if (isPrenesen()) {setUserCheckMsg("Korisnik ne može promijeniti dokument jer je prenesen u ili iz druge baze !", false);
       return false;
    }
    if (isKPR()) {setUserCheckMsg("Dokument je ušao u knjigu popisa i ne smije se mijenjati !!!", false);
       return false;
    }
    if (Aut.getAut().isWrongKnjigYear(this)) return false;

    restoreUserCheckMessage();
    return true;
  }
*/

  public raGOT() {

    isMaloprodajnaKalkulacija = true;
    setPreSel((jpPreselectDoc) presGOT.getPres());

    master_titel = "Gotovinski raèuni - otpremnice";
    detail_titel_mno = "Stavke gotovinskog raèuna";
    detail_titel_jed = "Stavka gotovinskog raèuna";
    setMasterSet(dm.getZagGot());
    setDetailSet(dm.getStGot());
    MP.BindComp();
    DP.BindComp();
    MP.panelBasicExt.jlrCNACPL.setRaDataSet(dm.getNacplG());
    setVisibleColsMaster(new int[] {4,5,9});
    set_kum_detail(true);
    stozbrojiti_detail(new String[] {"IPRODSP"});
    raDetail.addOption(rnvNacinPlac,4);
    raDetail.addOption(rnvKartica, 5, false);
    raMaster.addOption(rnvFisk, 5, false);
    defNacpl = hr.restart.sisfun.frmParam.getParam("robno","gotNacPl");
    
    if (hideKup) raMaster.addOption(rnvKupHack, 6, false);
  }

  public boolean LocalValidacijaMaster(){
    return isDatumToday();
  }

  public void RestPanelMPSetup(){
//    MP.setupOneA();
  }

  public boolean ValidacijaMasterExtend(){
/*    int i = MP.panelBasic.rpku.manipulateKupci();
    if (i!=-1){
      getMasterSet().setInt("CKUPAC",i);
    }*/

    /* Andrej:
     * ne znam jel treba,
     * ovisi sto i kada radi rpku.setDataSet(fDI.getMasterSet())
     * u rajpIzlazMPTemplate.BindComp();
     */
    //MP.panelBasic.rpku.setDataSet(getMasterSet());
    /* Andrej:
     * isto sto i ono gore zakomentirano
     */
    MP.panelBasic.rpku.updateRecords();
    return true;
  }


  public void detailSet_navigated(com.borland.dx.dataset.NavigationEvent ne){

    super.detailSet_navigated(ne);
    if (isKPR()){
      oslobodi();
    }
 }

  public void oslobodi(){
     raDetail.getEditNavActions()[4].setLockEnabled(false);
     raDetail.getEditNavActions()[4].setEnabled(true);
     raDetail.getEditNavActions()[4].setLockEnabled(true);
  }

  public void SetFocusIzmjenaExtends() {
    MP.rcc.setLabelLaF(MP.panelBasic.rpku.jraCkupac,true);
    MP.panelBasic.rpku.jraCkupac.requestFocus();
  }

  public void SetFocusNoviExtends(){

    if (MP.panelBasic.jrfCPAR.getText().equals("")){
      MP.panelBasic.rpku.jraCkupac.requestFocus();
    }
    else {
      ((JlrNavField)MP.panelBasic.rpku.jraCkupac).forceFocLost();
      MP.panelBasic.jtfDATDOK.requestFocus();
    }
  }
  
  public boolean afterWish(){
    return true;
  }

  public void Funkcija_ispisa_master(){
    if (frmPlacanje.justCheckRate(getMasterSet())) { // ovdje dolazi sinišina provjera rata
      raMaster.getRepRunner().clearAllCustomReports();
      isMasterInitIspis = false;
      super.Funkcija_ispisa_master();
    }
    else {
      JOptionPane.showConfirmDialog(this.raMaster,"Iznos pla\u0107anja je nejednak iznosu ra\u010Duna !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
    }
  }

   public void Funkcija_ispisa_detail(){
     if (frmPlacanje.justCheckRate(getMasterSet())) { // ovdje dolazi sinišina provjera rata
     raDetail.getRepRunner().clearAllCustomReports();
     isDetailInitIspis = false;
     super.Funkcija_ispisa_detail();
     }
     else {
        JOptionPane.showConfirmDialog(this.raDetail,"Iznos pla\u0107anja je nejednak iznosu ra\u010Duna !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
     }

   }

   public boolean ValidacijaPrijeIzlazaDetail() {
     return frmPlacanje.checkRate(this);
   }

   public void RestPanelSetup(){
     DP.addRestGRNGOT();
   }

  public boolean DodatnaValidacijaDetail() {
     if (val.isEmpty(DP.jtfKOL)) return false;
     if (val.isEmpty(DP.jraFMC)) return false;
     if (manjeNula()) return false;
     return isPriceToBig(true);
   }
   public void prepareQuery(String odabrano) {
     String ss = "select * from doki where "+
                     "aktiv='D' and god = '"+
               val.findYear(pressel.getSelRow().getTimestamp("DATDOK-to"))
               +"' and cskl='" +pressel.getSelRow().getString("CSKL") +"' "+dodatak+
                 (odabrano.equals("SGT") ? "and vrdok= 'GOT'" :
                     "and vrdok= '"+odabrano+"' and param like '%K%'") ; //samo trenutno
      qDS =  hr.restart.util.Util.getNewQueryDataSet(ss,true);
   }
   
   class KupacDialog extends raInputDialog {
     StorageDataSet sds = new StorageDataSet();
     
     
     JlrNavField jlrKup = new JlrNavField();
     JlrNavField jlrNaziv = new JlrNavField();
     JraButton jbSelKup = new JraButton();
     JraCheckBox jcbAktiv = new JraCheckBox();
     JPanel jpMain;
     
     public KupacDialog() {
       sds.setColumns(new Column[] {
           dM.createIntColumn("CKUPAC", "Kupac")
       });
       sds.open();
       sds.insertRow(false);
       
       jpMain = new JPanel();
       jpMain.setLayout(new XYLayout(545, 85));
       
       jlrKup.setColumnName("CKUPAC");
       jlrKup.setDataSet(sds);
       jlrKup.setColNames(new String[] {"IME"});
       jlrKup.setVisCols(new int[] {0, 1, 2});
       jlrKup.setTextFields(new javax.swing.text.JTextComponent[] {jlrNaziv});
       jlrKup.setRaDataSet(dm.getKupci());
       jlrKup.setNavButton(jbSelKup);
       jlrNaziv.setColumnName("IME");
       jlrNaziv.setNavProperties(jlrKup);
       jlrNaziv.setSearchMode(1);
       
       jcbAktiv.setHorizontalTextPosition(JLabel.LEADING);
       jcbAktiv.setHorizontalAlignment(JLabel.TRAILING);
       jcbAktiv.setText("Sakrij kupca");
       
       jpMain.add(new JLabel("Kupac"), new XYConstraints(15, 20, -1, -1));
       jpMain.add(jlrKup, new XYConstraints(100, 20, 100, -1));
       jpMain.add(jlrNaziv, new XYConstraints(205, 20, 300, -1));
       jpMain.add(jbSelKup, new XYConstraints(510, 20, 21, 21));
       jpMain.add(jcbAktiv, new XYConstraints(305, 50, 200, -1));
     }
     
     public void setData(DataSet ds) {
       jlrKup.emptyTextFields();
       if (ds.isNull("CKUPAC")) sds.setUnassignedNull("CKUPAC");
       else sds.setInt("CKUPAC", ds.getInt("CKUPAC"));
       jcbAktiv.setSelected(ds.getString("AKTIV").equalsIgnoreCase("N"));
     }
     
     public boolean show(Container parent) {
       return super.show(parent, jpMain, "Promjena kupca");
     }
     
     public void update(DataSet ds) {
       if (sds.isNull("CKUPAC")) ds.setAssignedNull("CKUPAC");
       else ds.setInt("CKUPAC", sds.getInt("CKUPAC"));
       ds.setString("AKTIV", jcbAktiv.isSelected() ? "N" : "D");
     }
   }
}
