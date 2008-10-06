/****license*****************************************************************
**   file: frmTeretOdob.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raTransaction;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmTeretOdob extends raMasterDetail {
  _Main ma;
  raCommonClass rcc = raCommonClass.getraCommonClass();
  lookupData ld = lookupData.getlookupData();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();
  Rbr rbr = Rbr.getRbr();

  rajpBrDok jpMasterHeader = new rajpBrDok();
  JPanel jpMasterMain = new JPanel();
  JPanel jpMaster = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlPart = new JLabel();
  JlrNavField jlrNazPart = new JlrNavField();
  JraButton jbSelSkl = new JraButton();
  JlrNavField jlrPart = new JlrNavField();
  JLabel jlNap = new JLabel();
  JlrNavField jlrNap = new JlrNavField();
  JlrNavField jlrNapText = new JlrNavField();
  JraButton jbSelNap = new JraButton();
  JLabel jlDatum = new JLabel();
  JraTextField jraDatum = new JraTextField();
  JPanel jpDetail = new JPanel();
  JPanel jpDetailMain = new JPanel();
  XYLayout xYLayout2 = new XYLayout();
  JLabel jlKol = new JLabel();
  JLabel jlCijena = new JLabel();
  JLabel jlIznos = new JLabel();
  JraTextField jraKol = new JraTextField();
  JraTextField jraCijena = new JraTextField();
  JraTextField jraIznos = new JraTextField();
  rapancart rpc = new rapancart() {
    public void metToDo_after_lookUp() {
       if (!rpcLostFocus && raDetail.getMode() == 'N') {
         rpcLostFocus = true;
         updateCijene();

       }
    }
  };

  hr.restart.robno.jpPreselectDoc presel;// tomo dodap
  boolean rpcLostFocus/*, cijeneUpdated*/;

  String[] key = Util.mkey;
  short oldRbr;

  public frmTeretOdob() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void enableKol() {
    rcc.setLabelLaF(jraKol, true);
    rcc.setLabelLaF(jraCijena, true);
  }

  public void beforeShowMaster() {
//    getMasterSet().refresh();
//    refilterDetailSet();
    getMasterSet().open();
    getDetailSet().open();
    initRpcart();
/* ovo je bespotrebno jerbo na\u010Dini\u0107emo 2 klase ekstra koj naslij\u0111uju ovu
    presel = null;
    presel = (jpPreselectDoc) presODB.getPresODB();
    if (presel == null) {
//    if (jpSelectDoc.getJpSelectDoc().getRaDokument().equals("TER")) {
      presel = (jpPreselectDoc) presTER.getPresTER();
      this.setNaslovMaster("Tere\u0107enja");
      this.setNaslovDetail("Stavke tere\u0107enja");
      this.raMaster.getRepRunner().clearAllReports();
      this.raMaster.getRepRunner().addReport("hr.restart.robno.repTerecenja", "Ispis svih tere\u0107enja", 2);
      this.raDetail.getRepRunner().clearAllReports();
      this.raDetail.getRepRunner().addReport("hr.restart.robno.repTerecenja", "Ispis tere\u0107enja", 2);
    } else {

      this.setNaslovMaster("Odobrenja");
      this.setNaslovDetail("Stavke odobrenja");
      this.raMaster.getRepRunner().clearAllReports();
      this.raMaster.getRepRunner().addReport("hr.restart.robno.repOdobrenja", "Ispis svih odobrenja", 2);
      this.raDetail.getRepRunner().clearAllReports();
      this.raDetail.getRepRunner().addReport("hr.restart.robno.repOdobrenja", "Ispis odobrenja", 2);
    }
*/
  }

  public void EntryPointMaster(char mode) {
    if (mode == 'N') {
      initNewMaster();
//      jlrPart.setText(jpSelectDoc.getJpSelectDoc().jrfCPAR.getText());
      jlrPart.setText(presel.jrfCPAR.getText());
      jlrPart.forceFocLost();
    } //else
      jpMasterHeader.SetDefTextDOK(mode);
    if (mode == 'I' || /*!jpSelectDoc.getJpSelectDoc().jrfCPAR.getText().trim().equals("")*/
                        !presel.jrfCPAR.getText().trim().equals("")) { // Tomo 22.07.2002
      rcc.setLabelLaF(jlrPart, false);
      rcc.setLabelLaF(jlrNazPart, false);
      rcc.setLabelLaF(jbSelSkl, false);
    }
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
/*      jlrPart.setText(jpSelectDoc.getJpSelectDoc().jrfCPAR.getText());
      jlrPart.forceFocLost(); */
//      if (jpSelectDoc.getJpSelectDoc().jrfCPAR.getText().trim().equals(""))
      if (presel.jrfCPAR.getText().trim().equals("")) // Tomo 22.07.2002
        jlrPart.requestFocus();
      else
        jlrNap.requestFocus();
    } else {
      jpMasterHeader.SetDefTextDOK(mode);
      if (mode != 'B') {
        jlrNap.requestFocus();
      }
    }
  }

  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jlrPart))
      return false;
    if (vl.isEmpty(jraDatum))
      return false;
    return true;
  }

  public boolean doBeforeSaveMaster(char mode) {
    if (mode == 'N') {
      hr.restart.robno.Util.getUtil().getBrojDokumenta(this.getMasterSet());
    }
    return true;
  }

  public boolean doWithSaveMaster(char mode) {
    if (mode == 'B') {
      hr.restart.robno.Util.getUtil().delSeq(delstr, true);
    }
    return true;
  }

  String delstr;
  public boolean DeleteCheckMaster() {
    DataSet ds = getMasterSet();
    delstr = hr.restart.robno.Util.getUtil().getSeqString(ds);
    return hr.restart.robno.Util.getUtil().checkSeq(delstr, String.valueOf(ds.getInt("BRDOK")));
  }

  public void refilterDetailSet() {
    super.refilterDetailSet();
    rpc.setGodina(vl.findYear(this.getMasterSet().getTimestamp("DATDOK")));
    rpc.setCskl(this.getMasterSet().getString("CSKL"));
  }


  private void eraseFields() {
    rpcLostFocus = false;
//    cijeneUpdated = false;
    /*jraKol.setText("");
    jraCijena.setText("");
    jraIznos.setText("");*/
    this.getDetailSet().setBigDecimal("KOL", _Main.nul);
    this.getDetailSet().setBigDecimal("ZC", _Main.nul);
    this.getDetailSet().setBigDecimal("IRAZ", _Main.nul);

    rcc.EnabDisabAll(jpDetail, false);
  }

  public void EntryPointDetail(char mode) {
    if (mode == 'N') {
      eraseFields();
//      rpc.setCART();
    } else if (mode == 'I') {
      rcc.setLabelLaF(jraIznos, false);
    }
  }

  public void SetFokusDetail(char mode) {
    if (mode == 'N') {
      eraseFields();
      rpc.setCART();
    } else if (mode == 'I') {
      jraKol.requestFocus();
    }
  }

  private boolean artNotUnique(String art) {
    return false;
//    vl.execSQL("select * from stdoki where " +
//               "cskl = '" + this.getMasterSet().getString("CSKL") + "' and " +
//               "vrdok = '" + this.getMasterSet().getString("VRDOK") + "' and " +
//               "god = '" + this.getMasterSet().getString("GOD") + "' and " +
//               "brdok = " + this.getMasterSet().getInt("BRDOK") +   " and cart = " + art);
//    vl.RezSet.open();
//    return (vl.RezSet.rowCount() > 0);
  }

  public boolean ValidacijaDetail(char mode) {

    if (rpc.getCART().equals("")) {
      eraseFields();
      JOptionPane.showMessageDialog(this.jpDetail,"Obavezan unos Artikla!","Greška",
        JOptionPane.ERROR_MESSAGE);
      rpc.EnabDisab(true);
      rpc.setCART();
      eraseFields();
      return false;
    }
//    if (!cijeneUpdated && mode == 'N') return false;
/*    if (artNotUnique(rpc.getCART())) {
      JOptionPane.showMessageDialog(this.jpDetail,"Artikl ve\u0107 promijenjen!","Greška",
        JOptionPane.ERROR_MESSAGE);
      rpc.EnabDisab(true);
      rpc.setCART();
      eraseFields();
      return false;
    } */
    if (mode == 'N')
      fillHeader();
    //ld.raLocate(dm.getSklad(),new String[] {"CSKL"}, new String[] {this.getMasterSet().getString("CSKL")});
    //vrzal = dm.getSklad().getString("VRZAL");
    Aut.getAut().calcSkladFigures(this.getDetailSet(), false);
    return true;
  }

  public void AfterSaveDetail(char mode) {
    if (mode == 'N') {
      rpc.EnabDisab(true);
    }
  }

  public boolean ValDPEscapeDetail(char mode) {
    if (mode == 'N' && rpcLostFocus) {
      rpc.EnabDisab(true);
      rpc.setCART();
      eraseFields();
      return false;
    }
    return true;
  }

  public boolean DeleteCheckDetail() {
    oldRbr = this.getDetailSet().getShort("RBR");
    return true;
  }

//  public void AfterDeleteDetail() {
//    Aut.getAut().recalculateRbr(this, "RBR", oldRbr);
//  }
  public boolean doWithSaveDetail(char mode) {
    if (mode == 'B') {
      vl.recountDataSet(this.raDetail, "RBR", oldRbr, false);
      raTransaction.saveChanges(getDetailSet());
    }
    return true;
  }

  public void Funkcija_ispisa_master(){
//    reportsQuerysCollector.getRQCModule().ReSql(PrepSql(false), jpSelectDoc.getJpSelectDoc().getRaDokument());
    reportsQuerysCollector.getRQCModule().ReSql(PrepSql(false), presel.getRaDokument());    // Tomo 22.07.2002
    super.Funkcija_ispisa_master();
  }

  public void Funkcija_ispisa_detail(){
//    reportsQuerysCollector.getRQCModule().ReSql(PrepSql(true), jpSelectDoc.getJpSelectDoc().getRaDokument());
    reportsQuerysCollector.getRQCModule().ReSql(PrepSql(true), presel.getRaDokument()); // Tomo 22.07.2002
    super.Funkcija_ispisa_detail();
  }

  public String PrepSql(boolean detail){
    String sqldodat= "";

    if (detail) {
         sqldodat="and cskl='"+jpSelectDoc.jpselectdoc.getSelRow().getString("CSKL")+"' "+
             "and vrdok='"+jpSelectDoc.jpselectdoc.getSelRow().getString("VRDOK")+"' " +
             "and brdok = "+getMasterSet().getInt("BRDOK");
    }
    else {
      if (!jpSelectDoc.jpselectdoc.getSelRow().getString("CSKL").equals(""))
        sqldodat= "and cskl='"+jpSelectDoc.jpselectdoc.getSelRow().getString("CSKL")+"' ";

      if (!jpSelectDoc.jpselectdoc.getSelRow().getString("VRDOK").equals(""))
        sqldodat= sqldodat+"and vrdok='"+jpSelectDoc.jpselectdoc.getSelRow().getString("VRDOK")+"' ";

      if (jpSelectDoc.jpselectdoc.getSelRow().getInt("CPAR")!=0)
        sqldodat= sqldodat+"and cpar="+jpSelectDoc.jpselectdoc.getSelRow().getInt("CPAR")+" ";

      if (!jpSelectDoc.jpselectdoc.getSelRow().getTimestamp("DATDOK-from").equals("")) {
        sqldodat= sqldodat+"and datdok >= '"+
        raDateUtil.getraDateUtil().PrepDate(jpSelectDoc.jpselectdoc.getSelRow().getTimestamp("DATDOK-from"),true)+"' ";
      }

      if (!jpSelectDoc.jpselectdoc.getSelRow().getTimestamp("DATDOK-to").equals("")) {
        sqldodat= sqldodat+"and datdok <= '"+
        raDateUtil.getraDateUtil().PrepDate(jpSelectDoc.jpselectdoc.getSelRow().getTimestamp("DATDOK-to"),false)+"' ";
      }
    }
    return sqldodat;
  }

  private void fillHeader() {
/*    this.getDetailSet().setString("CSKL", this.getMasterSet().getString("CSKL"));
    this.getDetailSet().setString("VRDOK", this.getMasterSet().getString("VRDOK"));
    this.getDetailSet().setString("GOD", this.getMasterSet().getString("GOD"));
    this.getDetailSet().setInt("BRDOK", this.getMasterSet().getInt("BRDOK"));
*/
    // traži sljede\u0107i redni broj ovog dokumenta
    this.getDetailSet().setShort("RBR", rbr.vrati_rbr("STDOKI",this.getMasterSet().getString("CSKL"),
        this.getMasterSet().getString("VRDOK"),this.getMasterSet().getString("GOD"),
        this.getMasterSet().getInt("BRDOK")));
  }

  private void updateCijene() {
    // provjeri nalazi li ve\u0107 u dokumentu isti artikl
/*    if (artNotUnique(rpc.getCART())) {
      /*JOptionPane.showMessageDialog(this.jpDetail,"Artikl ve\u0107 u tablici za isti dokument!","Greška",
        JOptionPane.ERROR_MESSAGE);
      rpc.EnabDisab(true);
      rpc.setCART();*/
   /*   eraseFields();
      Aut.getAut().handleRpcErr(rpc, "Artikl ve\u0107 u tablici za isti dokument!");
      return;
    }
    vl.RezSet = null; */
//    System.out.println("" + rpc.getBC() + "  " + rpc.isUsluga() + "  " + rpc.getNAZART());
    if (!rpc.isUsluga()) {
//      JOptionPane.showMessageDialog(this.jpDetail,"USLUGA obavezna! (glupi Ante ne kuži stvar, pa se strpite\n"+
//        "za bolju poruku","Greška",
//        JOptionPane.ERROR_MESSAGE);
//      rpc.EnabDisab(true);
//      rpc.setCART();
      eraseFields();
      Aut.getAut().handleRpcErr(rpc, "Artikl mora biti tipa Usluga!");
      return;
    }
    enableKol();
    jraKol.requestFocus();
  }

  /*private void KolUpdated() {
    Aut.getAut().calcSkladFigures(this.getDetailSet(), false);
  }*/

  private void jbInit() throws Exception {

    this.setMasterSet(dm.getDoki());
    this.setVisibleColsMaster(new int[] {0,4,5});
    this.setMasterKey(key);

    this.setDetailSet(dm.getStdoki());
    this.setVisibleColsDetail(new int[] {4,Aut.getAut().getCARTdependable(5,6,7),8,11,33,34});
    this.setDetailKey(Util.dkey);

    this.setMasterDeleteMode(DELDETAIL);

    xYLayout1.setWidth(645);
    xYLayout1.setHeight(110);
    jpMaster.setLayout(xYLayout1);
    jlPart.setText("Poslovni partner");
    jbSelSkl.setText("...");
    jlDatum.setText("Datum");
    jlNap.setText("Napomena");
    jbSelNap.setText("...");

    jlrPart.setColumnName("CPAR");
    jlrPart.setTextFields(new JTextComponent[] {jlrNazPart});
    jlrPart.setColNames(new String[] {"NAZPAR"});
    jlrPart.setSearchMode(0);
    jlrPart.setDataSet(this.getMasterSet());
    jlrPart.setRaDataSet(dm.getPartneri());
    jlrPart.setVisCols(new int[] {0,1});
    jlrPart.setNavButton(jbSelSkl);

    jlrNazPart.setColumnName("NAZPAR");
    jlrNazPart.setNavProperties(jlrPart);
    jlrNazPart.setSearchMode(1);

    jlrNap.setColumnName("CNAP");
    jlrNap.setTextFields(new JTextComponent[] {jlrNapText});
    jlrNap.setColNames(new String[] {"NAZNAP"});
    jlrNap.setSearchMode(0);
    jlrNap.setDataSet(this.getMasterSet());
    jlrNap.setRaDataSet(dm.getNapomene());
    jlrNap.setVisCols(new int[] {0,1});
    jlrNap.setNavButton(jbSelNap);

    jlrNapText.setColumnName("NAZNAP");
    jlrNapText.setNavProperties(jlrNap);
    jlrNapText.setSearchMode(1);

    jraDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatum.setDataSet(this.getMasterSet());
    jraDatum.setColumnName("DATDOK");

    jpDetail.setLayout(xYLayout2);
    xYLayout2.setWidth(645);
    xYLayout2.setHeight(65);

    jlKol.setText("Koli\u010Dina");
    jlCijena.setHorizontalAlignment(SwingConstants.TRAILING);
    jlCijena.setText("Cijena");
    jlIznos.setHorizontalAlignment(SwingConstants.TRAILING);
    jlIznos.setText("Iznos");

    jraKol.setDataSet(this.getDetailSet());
    jraKol.setColumnName("KOL");
    jraCijena.setDataSet(this.getDetailSet());
    jraCijena.setColumnName("ZC");
    jraIznos.setDataSet(this.getDetailSet());
    jraIznos.setColumnName("IRAZ");

    jpMaster.add(jlPart, new XYConstraints(15, 20, -1, -1));
    jpMaster.add(jlrNazPart, new XYConstraints(255, 20, 350, -1));
    jpMaster.add(jbSelSkl, new XYConstraints(611, 20, 21, 21));
    jpMaster.add(jlrPart, new XYConstraints(150, 20, 100, -1));
    jpMaster.add(jlDatum, new XYConstraints(15, 70, -1, -1));
    jpMaster.add(jraDatum, new XYConstraints(150, 70, 100, -1));
    jpMaster.add(jlNap, new XYConstraints(15, 45, -1, -1));
    jpMaster.add(jlrNap, new XYConstraints(150, 45, 100, -1));
    jpMaster.add(jlrNapText, new XYConstraints(255, 45, 350, -1));
    jpMaster.add(jbSelNap, new XYConstraints(611, 45, 21, 21));

    jpDetail.add(jlIznos, new XYConstraints(530, 8, 74, -1));
    jpDetail.add(jlKol, new XYConstraints(15, 25, -1, -1));
    jpDetail.add(jraKol, new XYConstraints(150, 25, 100, -1));
    jpDetail.add(jraIznos, new XYConstraints(505, 25, 100, -1));
    jpDetail.add(jraCijena, new XYConstraints(400, 25, 100, -1));
    jpDetail.add(jlCijena, new XYConstraints(425, 8, 74, -1));

    jpMasterHeader.setDataSet(this.getMasterSet());
    jpMasterHeader.addBorder();

    jpMaster.setBorder(BorderFactory.createEtchedBorder());
    jpMasterMain.setLayout(new BorderLayout());
    jpMasterMain.add(jpMasterHeader, BorderLayout.NORTH);
    jpMasterMain.add(jpMaster, BorderLayout.CENTER);

    jpDetailMain.setLayout(new BorderLayout());
    jpDetail.setBorder(BorderFactory.createEtchedBorder());
    jpDetailMain.add(rpc, BorderLayout.NORTH);
    jpDetailMain.add(jpDetail, BorderLayout.CENTER);

    this.setUserCheck(true);

    this.setJPanelDetail(jpDetailMain);
    this.setJPanelMaster(jpMasterMain);

/*    jraKol.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        KolUpdated();
      }
    });
    jraCijena.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        KolUpdated();
      }
    });*/
  }

  private void initRpcart() {
    /*rpc.setGodina(hr.restart.util.Valid.getValid().findYear(dm.getDoku().getTimestamp("DATDOK")));
    rpc.setCskl(dm.getStdoku().getString("CSKL"));*/
    rpc.setTabela(this.getDetailSet());
    rpc.setMode("DOH");
    rpc.setBorder(BorderFactory.createEtchedBorder());
    rpc.setDefParam();
    rpc.InitRaPanCart();
    rpc.setnextFocusabile(this.jraKol);
  }

  private void initNewMaster() {
/*  comment - Tomo 22.07.2002
    this.getMasterSet().setString("CSKL", jpSelectDoc.getJpSelectDoc().jrfCSKL.getText());
    this.getMasterSet().setString("VRDOK", jpSelectDoc.getJpSelectDoc().getRaDokument());
*/
    this.getMasterSet().setString("CSKL", presel.jrfCSKL.getText()); //Tomo 22.07.2002
    this.getMasterSet().setString("VRDOK", presel.getRaDokument()); //Tomo 22.07.2002
    this.getMasterSet().setTimestamp("DATDOK", vl.getToday());
//    this.getMasterSet().setString("GOD", vl.findYear(getMasterSet().getTimestamp("DATDOK")));

  }

//  private void getBrojDokumenta(com.borland.dx.dataset.DataSet ds) {
//    Integer Broj;
//    Broj=vl.findSeqInteger(ds.getString("CSKL")+ds.getString("VRDOK")+ds.getString("GOD"), false);
//    ds.setInt("BRDOK",Broj.intValue());
//  }
}
