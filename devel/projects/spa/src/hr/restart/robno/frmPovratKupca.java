/****license*****************************************************************
**   file: frmPovratKupca.java
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

public class frmPovratKupca extends raIzlazTemplate {

  public void initialiser(){
    what_kind_of_dokument = "POD" ;
  }
  public void MyaddIspisMaster(){
//    raMaster.getRepRunner().addReport("hr.restart.robno.repPovratnicaOdobrenje",
//                                           "Ispis povratnice - odobrenja", 2);

    raMaster.getRepRunner().addReport("hr.restart.robno.repPovratnicaOdobrenje",
                                      "hr.restart.robno.repIzlazni",
                                      "PovratnicaOdobrenje",
                                      "Ispis povratnice - odobrenja");
    raMaster.getRepRunner().addReport("hr.restart.robno.repOdobrenjaV",
        "hr.restart.robno.repIzlazni",
        "PovratnicaOdobrenje",
        "Ispis povratnice - odobrenja u valuti");
    raMaster.getRepRunner().addReport("hr.restart.robno.repPODSifKup",
        "hr.restart.robno.repRacuniPnP",
        "PODSifKup",
        "Ispis povratnice - odobrenja sa šifrom kupca");
    raMaster.getRepRunner().addReport("hr.restart.robno.repPovratnicaOdobrenjePnP",
        "hr.restart.robno.repRacuniPnP",
        "PovratnicaOdobrenjePnP",
        "Ispis povratnice - odobrenja s popustima");
    raMaster.getRepRunner().addReport("hr.restart.robno.repPovratnicaOdobrenjeNoc",
        "hr.restart.robno.repIzlazni",
        "Povrat",
        "Ispis povratnice - odobrenja bez cijena");

  }

  public void MyaddIspisDetail(){
//    raDetail.getRepRunner().addReport("hr.restart.robno.repTerecenja", "Ispis odobrenja", 2);
//    raDetail.getRepRunner().addReport("hr.restart.robno.repPovratnicaOdobrenje",
//                                           "Ispis povratnice - odobrenja", 2);

    raDetail.getRepRunner().addReport("hr.restart.robno.repPovratnicaOdobrenje",
                                      "hr.restart.robno.repIzlazni",
                                      "PovratnicaOdobrenje",
                                      "Ispis povratnice - odobrenja");
    raDetail.getRepRunner().addReport("hr.restart.robno.repOdobrenjaV",
        "hr.restart.robno.repIzlazni",
        "PovratnicaOdobrenje",
        "Ispis povratnice - odobrenja u valuti");
    raDetail.getRepRunner().addReport("hr.restart.robno.repPODSifKup",
        "hr.restart.robno.repRacuniPnP",
        "PODSifKup",
        "Ispis povratnice - odobrenja sa šifrom kupca");
    raDetail.getRepRunner().addReport("hr.restart.robno.repPovratnicaOdobrenjePnP",
        "hr.restart.robno.repRacuniPnP",
        "PovratnicaOdobrenjePnP",
        "Ispis povratnice - odobrenja s popustima");
    raDetail.getRepRunner().addReport("hr.restart.robno.repPovratnicaOdobrenjeNoc",
        "hr.restart.robno.repIzlazni",
        "Povrat",
        "Ispis povratnice - odobrenja bez cijena");
  }

  public frmPovratKupca() {

    setPreSel((jpPreselectDoc) presPOD.getPres());
//    this.setNaslovMaster("Povratnice kupca");
    setVisibleColsMaster(new int[] {4,5,6});

//

//    this.setNaslovDetail("Artikli povratnice");
//    this.setVisibleColsDetail(new int[] {4,Aut.getAut().getCARTdependable(5,6,7),8,11,33,34});
//    this.setDetailKey(key);
    raMaster.addOption(rnvFisk, 5, false);

    master_titel = "Povratnice kupca";
    detail_titel_mno = "Stavke povratnice kupca";
    detail_titel_jed = "Stavka povratnice kupca";
    setMasterSet(dm.getZagPod());
    setDetailSet(dm.getStPod());
    MP.BindComp();
    DP.BindComp();
    
    raDetail.addOption(rnvKartica, 4, false);

  }

  public boolean ValidacijaStanje(){
    return true ;
  }

}
//
//import javax.swing.*;
//import javax.swing.text.*;
//import com.borland.jbcl.layout.*;
//import java.awt.*;
//import java.awt.event.*;
//import hr.restart.util.*;
//import hr.restart.baza.*;
//import hr.restart.swing.*;
//import com.borland.dx.sql.dataset.*;
//import hr.restart.sisfun.Asql;
//
//public class frmPovratKupca extends raMasterDetail {
//  _Main ma;
//  raCommonClass rcc = raCommonClass.getraCommonClass();
//  lookupData ld = lookupData.getlookupData();
//  Valid vl = Valid.getValid();
//  dM dm = dM.getDataModule();
//  Rbr rbr = Rbr.getRbr();
//  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
//
//
/*  jpRadniNalog jpr = new jpRadniNalog();*/
//  rajpBrDok jpMasterHeader = new rajpBrDok();
//  JPanel jpMasterMain = new JPanel();
//  JPanel jpMaster = new JPanel();
//  XYLayout xYLayout1 = new XYLayout();
//  JLabel jlPartner = new JLabel();
//  JlrNavField jlrNazPart = new JlrNavField() {
//    public void after_lookUp() {
//      jraDatum.requestFocus();
//    }
//  };
//  JraButton jbSelPart = new JraButton();
//  JlrNavField jlrPart = new JlrNavField() {
//    public void after_lookUp() {
//      jraDatum.requestFocus();
//    }
//  };
//  JLabel jlDatum = new JLabel();
//  JraTextField jraDatum = new JraTextField();
//  JPanel jpDetail = new JPanel();
//  JPanel jpDetailMain = new JPanel();
//  XYLayout xYLayout2 = new XYLayout();
//  JLabel jlKol = new JLabel();
//  JLabel jlCijena = new JLabel();
//  JLabel jlIznos = new JLabel();
//  JraTextField jraKol = new JraTextField();
//  JraTextField jraCijena = new JraTextField();
//  JraTextField jraIznos = new JraTextField();
//  rapancart rpc = new rapancart() {
//    public void metToDo_after_lookUp() {
//       if (!rpcLostFocus && raDetail.getMode() == 'N') {
//         rpcLostFocus = true;
//         updateCijene();
//         /*enableKol();
//         SwingUtilities.invokeLater(new Runnable() {
//           public void run() {
//             SwingUtilities.invokeLater(new Runnable() {
//               public void run() {
//                 if (!cijeneUpdated && raDetail.getMode() == 'N') {
//                   cijeneUpdated = true;
//                   updateCijene();
//                 }
//               }
//             });
//           }
//         });*/
//       }
//    }
//  };
//
//  boolean rpcLostFocus/*, cijeneUpdated*/;
//
//  QueryDataSet repQDS = new QueryDataSet();
//  static frmPovratKupca frmpovkup;
//
//  String[] key = new String[] {"CSKL", "VRDOK", "GOD", "BRDOK"};
//  short oldRbr;
//
//  public frmPovratKupca() {
//    try {
//      frmpovkup = this;
//      jbInit();
//    }
//    catch(Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  public static frmPovratKupca getInstance() {
//    return frmpovkup;
//  }
//
//  public QueryDataSet getrepQDS() {
//    return repQDS;
//  }
//
//  private void enableKol() {
//    rcc.setLabelLaF(jraKol, true);
//  }
//
//  public void beforeShowMaster() {
/*    getMasterSet().refresh();*/
/*    refilterDetailSet();*/
//    this.getMasterSet().open();
//    this.getDetailSet().open();
//    initRpcart();
//  }
//
//  public void EntryPointMaster(char mode) {
//    if (mode == 'N') {
/*      this.getPreSelect().copySelValues();*/
//      initNewMaster();
/*      jlrPart.setText(jpSelectDoc.getJpSelectDoc().jrfCPAR.getText());*/
/*      jlrPart.forceFocLost();*/
//    } //else
//      jpMasterHeader.SetDefTextDOK(mode);
//
///*    if (!jpSelectDoc.getJpSelectDoc().jrfCPAR.getText().trim().equals("")) { */
//    if (this.getPreSelect().getSelRow().getInt("CPAR") > 0) {
//      rcc.setLabelLaF(jlrPart, false);
//      rcc.setLabelLaF(jlrNazPart, false);
//      rcc.setLabelLaF(jbSelPart, false);
//    }
/*    jpr.init(this.getMasterSet().getString("CRADNAL"));*/
//  }
//
//  public void SetFokusMaster(char mode) {
/*    System.out.println("fokus master ");*/
/*    jpr.init(getMasterSet().getString("CRADNAL"));*/
//
//    if (mode == 'N') {
/*      this.getMasterSet().setString("CSKL", jpSelectDoc.getJpSelectDoc().jrfCSKL.getText());*/
/*      jlrPart.setText(jpSelectDoc.getJpSelectDoc().jrfCPAR.getText());*/
/*      jlrPart.forceFocLost();*/
//    } else
//      jpMasterHeader.SetDefTextDOK(mode);
//    if (mode != 'B') {
//      if (this.getPreSelect().getSelRow().getInt("CPAR") == 0)
//        jlrPart.requestFocus();
//      else jraDatum.requestFocus();
//    }
//  }
//
//  public boolean ValidacijaMaster(char mode) {
//    if (vl.isEmpty(jlrPart))
//      return false;
//    if (vl.isEmpty(jraDatum))
//      return false;
/*    if (!jpr.Validacija())*/
/*      return false;*/
//    if (mode == 'N') {
//      getBrojDokumenta(this.getMasterSet());
//    }
//    //jpr.copyRNL();
//    return true;
//  }
//
/*  public void AfterAfterSaveMaster(char mode) {*/
/*    if (mode == 'N')*/
/*      jpr.copyRNL();*/
///*    super.AfterAfterSaveMaster(mode);
//    raDetail.setLockedMode('0');
//    raDetail.getOKpanel().jPrekid_actionPerformed();
//    */
/*  }*/
//
/*  public boolean DeleteCheckMaster() {*/
/*    return Aut.getAut().standardDeleteCheckMaster(this);*/
/*  }*/
//
//  public void refilterDetailSet() {
//    super.refilterDetailSet();
//    rpc.setGodina(vl.findYear(this.getMasterSet().getTimestamp("DATDOK")));
//    rpc.setCskl(this.getMasterSet().getString("CSKL"));
//  }
//
//
//  private void eraseFields() {
//    rpcLostFocus = false;
/*    cijeneUpdated = false;*/
//    /*jraKol.setText("");
//    jraCijena.setText("");
//    jraIznos.setText("");*/
//    this.getDetailSet().setBigDecimal("KOL", _Main.nul);
//    this.getDetailSet().setBigDecimal("ZC", _Main.nul);
//    this.getDetailSet().setBigDecimal("IRAZ", _Main.nul);
//    rcc.EnabDisabAll(jpDetail, false);
//  }
//
//  public void EntryPointDetail(char mode) {
//    if (mode == 'N') {
//      eraseFields();
//      rpc.setCART();
//    } else if (mode == 'I') {
//      rcc.setLabelLaF(jraCijena, false);
//      rcc.setLabelLaF(jraIznos, false);
//    }
//  }
//
//  public void SetFokusDetail(char mode) {
//    if (mode == 'N') {
//      eraseFields();
//      rpc.setCART();
//    } else if (mode == 'I') {
//      jraKol.requestFocus();
//    }
//  }
//
//  private boolean artNotUnique(String art) {
//    /*vl.execSQL("select * from stdoki where " +
//               "cskl = '" + this.getMasterSet().getString("CSKL") + "' and " +
//               "vrdok = '" + this.getMasterSet().getString("VRDOK") + "' and " +
//               "god = '" + this.getMasterSet().getString("GOD") + "' and " +
//               "brdok = " + this.getMasterSet().getInt("BRDOK") +   " and cart = " + art);
//    vl.RezSet.open();
//    return (vl.RezSet.rowCount() > 0);*/
//    return false;
//  }
//
//  public boolean ValidacijaDetail(char mode) {
//
//    if (rpc.getCART().equals("")) {
//      eraseFields();
//      JOptionPane.showMessageDialog(this.jpDetail,"Obavezan unos Artikla!","Greška",
//        JOptionPane.ERROR_MESSAGE);
//      rpc.EnabDisab(true);
//      rpc.setCART();
//      eraseFields();
//      return false;
//    }
/*    if (!cijeneUpdated && mode == 'N') return false;*/
//    if (mode == 'N' && artNotUnique(rpc.getCART())) {
//      JOptionPane.showMessageDialog(this.jpDetail,"Artikl ve\u0107 promijenjen!","Greška",
//        JOptionPane.ERROR_MESSAGE);
//      rpc.EnabDisab(true);
//      rpc.setCART();
//      eraseFields();
//      return false;
//    }
//    if (!rpc.AST.findStanje(this.getMasterSet().getString("GOD"),
//         this.getMasterSet().getString("CSKL"),this.getDetailSet().getInt("CART"))) {
//      JOptionPane.showMessageDialog(this.jpDetail,"Nema artikla na stanju!","Greška",
//        JOptionPane.ERROR_MESSAGE);
//      rpc.EnabDisab(true);
//      rpc.setCART();
//      eraseFields();
//      return false;
//    }
//    if (mode == 'N')
//      fillHeader();
/*    if (this.getDetailSet().getBigDecimal("KOL").compareTo(ma.nul) > 0) {*/
//      KolUpdated();
/*    }*/
//    //ld.raLocate(dm.getSklad(),new String[] {"CSKL"}, new String[] {this.getMasterSet().getString("CSKL")});
//    //vrzal = dm.getSklad().getString("VRZAL");
//    return true;
//  }
//
//  public void AfterSaveDetail(char mode) {
//    if (mode == 'N') {
//      rpc.EnabDisab(true);
//    }
//  }
//
//  public boolean ValDPEscapeDetail(char mode) {
//    if (mode == 'N' && rpcLostFocus) {
//      rpc.EnabDisab(true);
//      rpc.setCART();
//      eraseFields();
//      return false;
//    }
//    return true;
//  }
//
//  public boolean DeleteCheckDetail() {
//    oldRbr = this.getDetailSet().getShort("RBR");
//    return true;
//  }
//
//  public void AfterDeleteDetail() {
//    Aut.getAut().recalculateRbr(this, "RBR", oldRbr);
//  }
//
//  private void fillHeader() {
//   /* this.getDetailSet().setString("CSKL", this.getMasterSet().getString("CSKL"));
//    this.getDetailSet().setString("VRDOK", this.getMasterSet().getString("VRDOK"));
//    this.getDetailSet().setString("GOD", this.getMasterSet().getString("GOD"));
//    this.getDetailSet().setInt("BRDOK", this.getMasterSet().getInt("BRDOK")); */
//
//    // traži sljede\u0107i redni broj ovog dokumenta
//    this.getDetailSet().setShort("RBR", rbr.vrati_rbr("STDOKI",this.getMasterSet().getString("CSKL"),
//        this.getMasterSet().getString("VRDOK"),this.getMasterSet().getString("GOD"),
//        this.getMasterSet().getInt("BRDOK")));
//  }
//
//  private void updateCijene() {
//    // provjeri ima li artikla na stanju
//    if (!rpc.AST.findStanje(this.getMasterSet().getString("GOD"),
//         this.getMasterSet().getString("CSKL"),this.getDetailSet().getInt("CART"))) {
/*      JOptionPane.showMessageDialog(this.jpDetail,"Nema artikla na stanju!","Greška",
        JOptionPane.ERROR_MESSAGE);
      rpc.EnabDisab(true);
      rpc.setCART();*/
//      eraseFields();
//      Aut.getAut().handleRpcErr(rpc, "Nema artikla na stanju!");
//      return;
//    }
//    // provjeri nalazi li ve\u0107 u dokumentu isti artikl
//    if (artNotUnique(rpc.getCART())) {
/*      JOptionPane.showMessageDialog(this.jpDetail,"Artikl ve\u0107 u tablici za isti dokument!","Greška",
        JOptionPane.ERROR_MESSAGE);
      rpc.EnabDisab(true);
      rpc.setCART();*/
//      eraseFields();
//      Aut.getAut().handleRpcErr(rpc, "Artikl ve\u0107 u tablici za isti dokument!");
//      return;
//    }
//    enableKol();
//    vl.RezSet = null;
//    this.getDetailSet().setBigDecimal("NC", rpc.gettrenStanje().getBigDecimal("NC"));
//    this.getDetailSet().setBigDecimal("VC", rpc.gettrenStanje().getBigDecimal("VC"));
//    this.getDetailSet().setBigDecimal("MC", rpc.gettrenStanje().getBigDecimal("MC"));
//    this.getDetailSet().setBigDecimal("ZC", rpc.gettrenStanje().getBigDecimal("ZC"));
//    jraKol.requestFocus();
//  }
//
//  private void KolUpdated() {
//    if (this.getDetailSet().getBigDecimal("KOL").compareTo(ma.nul) > 0)
//      this.getDetailSet().setBigDecimal("KOL", this.getDetailSet().getBigDecimal("KOL").negate());
//    Aut.getAut().calcSkladFigures(this.getDetailSet(), false);
//  }
//
//  public void Funkcija_ispisa_master() {
//    Asql.createIzlazMaster(repQDS, this);
//    super.Funkcija_ispisa_master();
//  }
//
//  public void Funkcija_ispisa_detail() {
//    Asql.createIzlazDetail(repQDS, this);
//    super.Funkcija_ispisa_detail();
//  }
//
//  private void jbInit() throws Exception {
//
//    this.setMasterSet(dm.getZagPod());
//    this.setNaslovMaster("Povratnice kupca");
//    this.setVisibleColsMaster(new int[] {0,4,5,6});
//    this.setMasterKey(key);
//
//    this.setDetailSet(dm.getStPod());
//    this.setNaslovDetail("Artikli povratnice");
//    this.setVisibleColsDetail(new int[] {4,Aut.getAut().getCARTdependable(5,6,7),8,11,33,34});
//    this.setDetailKey(key);
//
//    this.setMasterDeleteMode(DELDETAIL);
//
//    xYLayout1.setWidth(650);
//    xYLayout1.setHeight(85);  // TODO
//    jpMaster.setLayout(xYLayout1);
//    jlPartner.setText("Poslovni partner");
//    jbSelPart.setText("...");
//    jlDatum.setText("Datum");
//
//    jlrPart.setColumnName("CPAR");
//    jlrPart.setTextFields(new JTextComponent[] {jlrNazPart});
//    jlrPart.setColNames(new String[] {"NAZPAR"});
//    jlrPart.setSearchMode(0);
//    jlrPart.setDataSet(this.getMasterSet());
//    jlrPart.setRaDataSet(dm.getPartneri());
//    jlrPart.setVisCols(new int[] {0,1});
//    jlrPart.setNavButton(jbSelPart);
//
//    jlrNazPart.setColumnName("NAZPAR");
//    jlrNazPart.setNavProperties(jlrPart);
//    jlrNazPart.setSearchMode(1);
//
//    jraDatum.setDataSet(this.getMasterSet());
//    jraDatum.setColumnName("DATDOK");
//
//    jpDetail.setLayout(xYLayout2);
//    xYLayout2.setWidth(645);
//    xYLayout2.setHeight(65);
//
//    jlKol.setText("Koli\u010Dina");
//    jlCijena.setText("Cijena");
//    jlCijena.setHorizontalAlignment(SwingConstants.TRAILING);
//    jlIznos.setText("Iznos");
//    jlIznos.setHorizontalAlignment(SwingConstants.TRAILING);
//
//    jraKol.setDataSet(this.getDetailSet());
//    jraKol.setColumnName("KOL");
//    jraCijena.setDataSet(this.getDetailSet());
//    jraCijena.setColumnName("ZC");
//    jraIznos.setDataSet(this.getDetailSet());
//    jraIznos.setColumnName("IRAZ");
//
//    jpMaster.add(jlPartner, new XYConstraints(15, 20, -1, -1));
//    jpMaster.add(jlrNazPart, new XYConstraints(255, 20, 350, -1));
//    jpMaster.add(jbSelPart, new XYConstraints(611, 20, 21, 21));
//    jpMaster.add(jlrPart, new XYConstraints(150, 20, 100, -1));
//    jpMaster.add(jlDatum, new XYConstraints(15, 45, -1, -1));
//    jpMaster.add(jraDatum, new XYConstraints(150, 45, 100, -1));
/*    jpMaster.add(jpr, new XYConstraints(0, 70, -1, -1));  // TODO*/
//
/*    jpr.setMode("I");*/
//
//    jpDetail.add(jlIznos, new XYConstraints(530, 8, 74, -1));
//    jpDetail.add(jlKol, new XYConstraints(15, 25, -1, -1));
//    jpDetail.add(jraKol, new XYConstraints(150, 25, 100, -1));
//    jpDetail.add(jraIznos, new XYConstraints(505, 25, 100, -1));
//    jpDetail.add(jraCijena, new XYConstraints(400, 25, 100, -1));
//    jpDetail.add(jlCijena, new XYConstraints(425, 8, 74, -1));
//
//    jpMasterHeader.setDataSet(this.getMasterSet());
//    jpMasterHeader.addBorder();
//
//    jpMaster.setBorder(BorderFactory.createEtchedBorder());
//    jpMasterMain.setLayout(new BorderLayout());
//    jpMasterMain.add(jpMasterHeader, BorderLayout.NORTH);
//    jpMasterMain.add(jpMaster, BorderLayout.CENTER);
//
//    jpDetailMain.setLayout(new BorderLayout());
//    jpDetail.setBorder(BorderFactory.createEtchedBorder());
//    jpDetailMain.add(rpc, BorderLayout.NORTH);
//    jpDetailMain.add(jpDetail, BorderLayout.CENTER);
//
//    this.setUserCheck(true);
//
//    this.setJPanelDetail(jpDetailMain);
//    this.setJPanelMaster(jpMasterMain);
//
//    this.raMaster.getRepRunner().addReport("hr.restart.robno.repPovratnicaOdobrenje",
//                                           "Ispis svih povratnica", 2);
//
//    this.raDetail.getRepRunner().addReport("hr.restart.robno.repPovratnicaOdobrenje",
//                                           "Ispis povratnice - odobrenja", 2);
//
/*    jpr.setMode("I");
    jpr.initSelector();*/
//
//    jraKol.addFocusListener(new FocusAdapter() {
//      public void focusLost(FocusEvent e) {
//        KolUpdated();
//      }
//    });
//  }
//
//  private void initRpcart() {
//    /*rpc.setGodina(hr.restart.util.Valid.getValid().findYear(dm.getDoku().getTimestamp("DATDOK")));
//    rpc.setCskl(dm.getStdoku().getString("CSKL"));*/
//    rpc.setTabela(this.getDetailSet());
//    rpc.setMode("DOH");
//    rpc.setBorder(BorderFactory.createEtchedBorder());
//    rpc.setParam(hr.restart.sisfun.frmParam.getParam("robno","indiCart"));
//    rpc.SetFocus(hr.restart.sisfun.frmParam.getParam("robno","focusCart"));
//    rpc.InitRaPanCart();
//    rpc.setnextFocusabile(this.jraKol);
//  }
//
//  private void initNewMaster() {
//    this.getPreSelect().copySelValues();
//    this.getMasterSet().setString("VRDOK", "POD");
/*    this.getMasterSet().setString("CSKL", jpSelectDoc.getJpSelectDoc().jrfCSKL.getText());*/
//    this.getMasterSet().setTimestamp("DATDOK", vl.getToday());
//    this.getMasterSet().setString("GOD",vl.findYear(getMasterSet().getTimestamp("DATDOK")));
//
//  }
//
//  private void getBrojDokumenta(com.borland.dx.dataset.DataSet ds) {
//    Integer Broj;
//    Broj=vl.findSeqInteger(ds.getString("CSKL")+ds.getString("VRDOK")+ds.getString("GOD"));
//    ds.setInt("BRDOK",Broj.intValue());
//  }
//}
