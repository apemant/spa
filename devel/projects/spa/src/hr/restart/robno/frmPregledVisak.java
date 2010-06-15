/****license*****************************************************************
**   file: frmPregledVisak.java
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
import hr.restart.util.LinkClass;
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

import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmPregledVisak extends raMasterDetail {
  _Main ma;
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  lookupData ld = lookupData.getlookupData();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();
  Rbr rbr = Rbr.getRbr();
  static frmPregledVisak vishak;


  private LinkClass lc = LinkClass.getLinkClass();
  private raKalkulBDStanje rKBD = new raKalkulBDStanje();
  private raFakeBDStanje stanje = new raFakeBDStanje();
  private raFakeBDStdoku stavka = new raFakeBDStdoku();
  private raFakeBDStdoku stavkaold = new raFakeBDStdoku();
  private String srcString;
  static frmOtpisRobe otpis;

  rajpBrDok jpMasterHeader = new rajpBrDok();
  JPanel jpMasterMain = new JPanel();
  JPanel jpMaster = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlSkl = new JLabel();
  JlrNavField jlrNazSkl = new JlrNavField() {
    public void after_lookUp() {
      jraDatum.requestFocus();
    }
  };
  JraButton jbSelSkl = new JraButton();
  JlrNavField jlrSkl = new JlrNavField() {
    public void after_lookUp() {
      jraDatum.requestFocus();
    }
  };
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

  boolean rpcLostFocus;

  String[] key = Util.mkey;
  short oldRbr;

  QueryDataSet repQDS = new QueryDataSet();

  public frmPregledVisak() {
    try {
      vishak = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static frmPregledVisak getInstance() {
    return vishak;
  }

  public QueryDataSet getrepQDS() {
    return repQDS;
  }

  private void enableKol() {
    rcc.setLabelLaF(jraKol, true);
  }

  public void beforeShowMaster() {
    this.getMasterSet().open();
    this.getDetailSet().open();
    initRpcart();
  }

  public void EntryPointMaster(char mode) {
    if (mode == 'N') {
      initNewMaster();
    }
    jpMasterHeader.SetDefTextDOK(mode);
    rcc.setLabelLaF(jlrSkl, false);
    rcc.setLabelLaF(jlrNazSkl, false);
    rcc.setLabelLaF(jbSelSkl, false);
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
      this.getPreSelect().copySelValues();
    } else
      jpMasterHeader.SetDefTextDOK(mode);
    if (mode != 'B') {
      jraDatum.requestFocus();
    }
  }

  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jlrSkl))
      return false;
    if (vl.isEmpty(jraDatum))
      return false;
    return true;
  }

  public void refilterDetailSet() {
    super.refilterDetailSet();
    rpc.setGodina(vl.findYear(this.getMasterSet().getTimestamp("DATDOK")));
    rpc.setCskl(this.getMasterSet().getString("CSKL"));
  }

  private void eraseFields() {
    rpcLostFocus = false;
    this.getDetailSet().setBigDecimal("KOL", _Main.nul);
    this.getDetailSet().setBigDecimal("ZC", _Main.nul);
    this.getDetailSet().setBigDecimal("IZAD", _Main.nul);
    rcc.EnabDisabAll(jpDetail, false);
  }

  public void EntryPointDetail(char mode) {
    if (mode == 'N') {
      eraseFields();
      rpc.setCART();
    } else if (mode == 'I') {
      rcc.setLabelLaF(jraCijena, false);
      rcc.setLabelLaF(jraIznos, false);
    }
  }

  public void SetFokusDetail(char mode) {
    if (mode == 'N') {
      eraseFields();
      rpc.setCART();
    } else if (mode == 'I') {
      lc.TransferFromDB2Class(getDetailSet(),stavkaold);
      jraKol.requestFocus();
    }
  }

  private QueryDataSet mjstanje ;
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
//    if (mode == 'N' && artNotUnique(rpc.getCART())) {
//      JOptionPane.showMessageDialog(this.jpDetail,"Artikl ve\u0107 promijenjen!","Greška",
//        JOptionPane.ERROR_MESSAGE);
//      rpc.EnabDisab(true);
//      rpc.setCART();
//      eraseFields();
//      return false;
//    }
    if (!rpc.AST.findStanje(this.getMasterSet().getString("GOD"),
                            this.getMasterSet().getString("CSKL"),this.getDetailSet().getInt("CART"))) {
      JOptionPane.showMessageDialog(this.jpDetail,"Nema artikla na stanju!","Greška",
                                    JOptionPane.ERROR_MESSAGE);
      rpc.EnabDisab(true);
      rpc.setCART();
      eraseFields();
      return false;
    }
    if (mode == 'N')
      fillHeader();

    Aut.getAut().calcSkladFigures(this.getDetailSet(), true);
    mjstanje= KalkulStanja(mode);
    return testStanje();
  }

  public boolean testStanje() {
    if (mjstanje== null) return false;
    if (mjstanje.getBigDecimal("KOL").doubleValue()<0) { // koji je ovo koruac????
      mjstanje = null;
      JOptionPane.showMessageDialog(this.jpDetail,"Nedovoljna koli\u010Dina na stanju!","Greška",
        JOptionPane.ERROR_MESSAGE);
      rpc.EnabDisab(true);
      rpc.setCART();
      eraseFields();
      return false;
    }
    else return true;
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

  public boolean DeleteCheckMaster() {
    srcString=getMasterSet().getString("CSKL")+getMasterSet().getString("VRDOK")+
              vl.findYear(getMasterSet().getTimestamp("DATDOK"));
    if (!rut.checkSeq(srcString,Integer.toString(getMasterSet().getInt("BRDOK")))) return false;

    /*if (!this.getDetailSet().isEmpty()) {
      javax.swing.JOptionPane.showConfirmDialog(null,"Nisu pobrisane stavke dokumenta !","Gre\u0161ka",
          javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.ERROR_MESSAGE);
      return false;
    }*/
    return true;
  }

  public boolean DeleteCheckDetail() {
//    System.out.println("Ulazim u DeleteCheckDetail");
    oldRbr = this.getDetailSet().getShort("RBR");
    lc.TransferFromDB2Class(getDetailSet(),stavkaold);
    mjstanje= KalkulStanja('B');
    return testStanje();
  }

  public void AfterDeleteDetail() {
//    vl.recountDataSet(raDetail, "RBR", oldRbr);
  }

  public void createUlazDetail(QueryDataSet keds) {
    keds.close();
    keds.closeStatement();
    keds.setQuery(new QueryDescriptor(dm.getDatabase1(),
      "SELECT * FROM doku,stdoku WHERE "+rut.getDoc("doku","stdoku")+
      " AND stdoku.cskl = '"+this.getMasterSet().getString("CSKL")+
      "' AND stdoku.vrdok = '"+this.getMasterSet().getString("VRDOK")+
      "' AND stdoku.god = '"+this.getMasterSet().getString("GOD")+
      "' AND stdoku.brdok = "+this.getMasterSet().getInt("BRDOK")
    ));
    keds.open();
  }

  public void Funkcija_ispisa_detail() {
    createUlazDetail(repQDS);
    super.Funkcija_ispisa_detail();
  }

  public void Funkcija_ispisa_master() {
    createUlazDetail(repQDS);
    super.Funkcija_ispisa_master();
  }

  private void fillHeader() {
    this.getDetailSet().setShort("RBR", rbr.vrati_rbr("STDOKU",this.getMasterSet().getString("CSKL"),
        this.getMasterSet().getString("VRDOK"),this.getMasterSet().getString("GOD"),
        this.getMasterSet().getInt("BRDOK")));
  }

  private void updateCijene() {
    if (rpc.AST.findStanje(this.getMasterSet().getString("GOD"),
                           this.getMasterSet().getString("CSKL"),this.getDetailSet().getInt("CART"))) {
      enableKol();
      vl.RezSet = null;

      this.getDetailSet().setBigDecimal("NC", rpc.gettrenStanje().getBigDecimal("NC"));
      this.getDetailSet().setBigDecimal("VC", rpc.gettrenStanje().getBigDecimal("VC"));
      this.getDetailSet().setBigDecimal("MC", rpc.gettrenStanje().getBigDecimal("MC"));
      this.getDetailSet().setBigDecimal("ZC", rpc.gettrenStanje().getBigDecimal("ZC"));
      jraKol.requestFocus();
    } else {
      /** @todo mini kalkulaciju kad artikla nema na stanju */
      eraseFields();
      Aut.getAut().handleRpcErr(rpc, "Nema artikla na stanju!");
      return;
    }
  }

  /*private void KolUpdated() {
    Aut.getAut().calcSkladFigures(this.getDetailSet(), true);
  }*/

  private void jbInit() throws Exception {

    this.setMasterDeleteMode(DELDETAIL);
    this.setMasterSet(dm.getDoku());
    this.setNaslovMaster("Inventurni višak");
    this.setVisibleColsMaster(new int[] {1,4,5});
    this.setMasterKey(key);

    this.setDetailSet(dm.getStdokuINV());
    this.setNaslovDetail("Stavke inventurnog viška");
    this.setVisibleColsDetail(new int[] {4,Aut.getAut().getCARTdependable(5,6,7),8,10,32,33});
    this.setDetailKey(Util.dkey);

    this.raMaster.getRepRunner().addReport("hr.restart.robno.repPregledVisak","Ispis inventurnih viškova",2);
    this.raMaster.getRepRunner().addReport("hr.restart.robno.repPregledVisakExtendedVersion","Ispis inventurnih viškova - vrijednosni",2);

    this.raDetail.getRepRunner().addReport("hr.restart.robno.repPregledVisak","Ispis inventurnih viškova",2);
    this.raDetail.getRepRunner().addReport("hr.restart.robno.repPregledVisakExtendedVersion","Ispis inventurnih viškova - vrijednosni",2);

    xYLayout1.setWidth(640);
    xYLayout1.setHeight(85);
    jpMaster.setLayout(xYLayout1);
    jlSkl.setText("Skladište");
    jbSelSkl.setText("...");
    jlDatum.setText("Datum");

    jlrSkl.setColumnName("CSKL");
    jlrSkl.setTextFields(new JTextComponent[] {jlrNazSkl});
    jlrSkl.setColNames(new String[] {"NAZSKL"});
    jlrSkl.setSearchMode(0);
    jlrSkl.setDataSet(this.getMasterSet());
    jlrSkl.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
    jlrSkl.setVisCols(new int[] {0,1});
    jlrSkl.setNavButton(jbSelSkl);

    jlrNazSkl.setColumnName("NAZSKL");
    jlrNazSkl.setNavProperties(jlrSkl);
    jlrNazSkl.setSearchMode(1);

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
    jraIznos.setColumnName("IZAD");

    jpMaster.add(jlSkl, new XYConstraints(15, 20, -1, -1));
    jpMaster.add(jlrNazSkl, new XYConstraints(260, 20, 335, -1));
    jpMaster.add(jbSelSkl, new XYConstraints(600, 20, 21, 21));
    jpMaster.add(jlrSkl, new XYConstraints(150, 20, 100, -1));
    jpMaster.add(jlDatum, new XYConstraints(15, 45, -1, -1));
    jpMaster.add(jraDatum, new XYConstraints(150, 45, 100, -1));

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
    });*/
  }

  private void initRpcart() {
    rpc.setTabela(this.getDetailSet());
    rpc.setMode("DOH");
    rpc.setBorder(BorderFactory.createEtchedBorder());
    rpc.setDefParam();
    rpc.InitRaPanCart();
    rpc.setnextFocusabile(this.jraKol);
  }

  private void initNewMaster() {
    this.getPreSelect().copySelValues();
    this.getMasterSet().setString("VRDOK", "INV");
    this.getMasterSet().setTimestamp("DATDOK", vl.getToday());
    this.getMasterSet().setString("GOD",vl.findYear(getMasterSet().getTimestamp("DATDOK")));
  }

  public boolean doBeforeSaveMaster(char mode) {
    if (mode == 'N') hr.restart.robno.Util.getUtil().getBrojDokumenta(getMasterSet());
    return true;
  }

  public boolean doWithSaveMaster (char mode){
//    System.out.println("doWithSaveMaster");
    if (mode == 'B') { // Brisanje mastera
      try {
        hr.restart.robno.Util.getUtil().delSeq(srcString, true);
      }
      catch (Exception ex) {
        ex.printStackTrace();
        return false;
      }
    }
    return true;
  }
  public boolean doWithSaveDetail(char mode){
    if (mode=='B' && !raDetail.isDeletingAll()) {
      try {
        vl.recountDataSet(raDetail, "RBR", oldRbr, false);
        raTransaction.saveChanges(getDetailSet());
      }
      catch (Exception ex) {
        ex.printStackTrace();
        return false;
      }
    }
    try {
      raTransaction.saveChanges(mjstanje);
      return true;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

  public QueryDataSet KalkulStanja(char mode) {
    QueryDataSet DBStanje = new QueryDataSet();
    DBStanje = hr.restart.util.Util.getNewQueryDataSet
    ("select * from stanje where cskl ='"+getDetailSet().getString("CSKL")+
    "' and god='"+getDetailSet().getString("GOD")+
    "' and cart="+getDetailSet().getInt("CART"),true);
    lc.TransferFromDB2Class(DBStanje,stanje);
    lc.TransferFromDB2Class(getDetailSet(),stavka);
    if (mode=='N') {
      stavkaold.Init();
    } else if (mode=='B') {
      stavka.Init();
    }
//    System.out.println("stavka.kol = " + stavka.kol);
//    System.out.println("stavkaold.kol = " + stavkaold.kol);
//    System.out.println("PRIJE stanje.kol = " + stanje.kol);
    rKBD.kalkStanjefromStdoku(stanje,stavka ,stavkaold,
                              getDetailSet().getString("VRDOK"));
//    System.out.println("POSLIJE stanje.kol = " + stanje.kol);
    lc.TransferFromClass2DB(DBStanje,stanje);
    return DBStanje;
  }
}

/*

    Pita mali Ivica tatu sto je to politika. Tata mu odgovori:
    - Evo sine, sad cu ja to tebi ljepo objasniti. Ja radim i dobivam novac za to. Zato sam ja kapitalist.
    Tvoja majka trosi moj novac i zato je ona Vlada. Nasa kucna pomocnica, teta Biba, je radnicka klasa.
    Svi imamo zajednicku jednu stvar - to je tvoja dobrobit. Jer, znas, ti si narod, a tvoj mali brat je nasa buducnost.
    Mali Ivica je toliko bio pod dojmom ovog odgovora mudrog oca da cijelu noc nije mogao zaspati.
    Kasno u noci njegov mali brat poceo plakati jer se pokakao u pelenu pa je mali Ivica otisao u spavacu
    sobu roditelja obavjestiti majku. No, majka je spavala u krevetu sama, tako cvrsto da nije niti cula
    najmla\u0111eg sincica kako place. Potom je mali Ivica otrcao u sobu kucne pomocnice, tete Bibe, i tamo ju
    nasao u zagrljaju s njegovim ocem. Teta Biba i mudri otac bili su toliko zaokupljeni svojim ''poslom''
    pa nisu ni vidjeli malog Ivicu koji je na kraju odlucio vratiti se u djecju sobu. Iduceg dana tata pita Ivicu:
    - I onda sine, jesi li naucio sto je to politika? Mali Ivica mu uzvrati:
    - Da! Kapitalizam iskoristava radnicku klasu dok Vlada spava. Nitko se ne osvrce na narod, a nasa buducnost
    lezi u govnima!

    To je politika!

*/