/****license*****************************************************************
**   file: frmOtpisRobe.java
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
import hr.restart.baza.Vrdokum;
import hr.restart.baza.dM;
import hr.restart.sisfun.Asql;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.LinkClass;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raTransaction;

import java.awt.BorderLayout;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmOtpisRobe extends raMasterDetail {

  _Main ma;
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  lookupData ld = lookupData.getlookupData();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();
  Rbr rbr = Rbr.getRbr();
  private LinkClass lc = LinkClass.getLinkClass();
  private raKalkulBDStanje rKBD = new raKalkulBDStanje();
  private raFakeBDStanje stanje = new raFakeBDStanje();
  private raFakeBDStdoki stavka = new raFakeBDStdoki();
  private raFakeBDStdoki stavkaold = new raFakeBDStdoki();
  private BigDecimal generalBD = Aus.zero2;
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
  raComboBox jraTipKalkul = new raComboBox();
  rapancart rpc = new rapancart() {
    public void metToDo_after_lookUp() {
       if (!rpcLostFocus && raDetail.getMode() == 'N') {
         rpcLostFocus = true;
         updateCijene();
         findKolicinaforOtpis();
         
         /*
         enableKol();
         SwingUtilities.invokeLater(new Runnable() {
           public void run() {
             SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                 if (!cijeneUpdated && raDetail.getMode() == 'N') {
                   cijeneUpdated = true;
                   updateCijene();
                 }
               }
             });
           }
         }); */
       }
    }
  };

  boolean rpcLostFocus/*, cijeneUpdated*/;

  String[] key = Util.mkey;
  short oldRbr;

  QueryDataSet repQDS = new QueryDataSet();

  public frmOtpisRobe() {
    try {
      otpis = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static frmOtpisRobe getInstance() {
    return otpis;
  }

  public QueryDataSet getrepQDS() {
    return repQDS;
  }

  private void enableKol() {
    rcc.setLabelLaF(jraKol, true);
    rcc.setLabelLaF(jraTipKalkul, true);
    if (raIzlazTemplate.allowPriceChange()) rcc.setLabelLaF(jraCijena, true);
  }

  DataSet sdokiz;
  public void beforeShowMaster() {
//    getMasterSet().refresh();
//    refilterDetailSet();
//    this.getMasterSet().open();
//    this.getDetailSet().open();
    initRpcart();
    sdokiz = Vrdokum.getDataModule().getTempSet(
        Condition.in("APP", new String[] {"robno", "mp"}).
        and(Condition.in("TIPDOK", new String[] {"S", "SF", "FS"})).
        and(Condition.equal("VRSDOK", "I")).
        and(Condition.in("VRDOK", new String[] {"INM", "OTR"}).not()));
    sdokiz.open();
  }

  public void EntryPointMaster(char mode) {
    if (mode == 'N') {
      initNewMaster();
//      jlrSkl.setText(jpSelectDoc.getJpSelectDoc().jrfCSKL.getText());
//      jlrSkl.forceFocLost();
    }// else
    jpMasterHeader.SetDefTextDOK(mode);
    rcc.setLabelLaF(jlrSkl, false);
    rcc.setLabelLaF(jlrNazSkl, false);
    rcc.setLabelLaF(jbSelSkl, false);
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
      this.getPreSelect().copySelValues();
//      jlrSkl.setText(jpSelectDoc.getJpSelectDoc().jrfCSKL.getText());
//      jlrSkl.forceFocLost();
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

//  public boolean DeleteCheckMaster() {
//    return Aut.getAut().standardDeleteCheckMaster(this);
//  }

  public void refilterDetailSet() {
    super.refilterDetailSet();
    rpc.setGodina(vl.findYear(this.getMasterSet().getTimestamp("DATDOK")));
    rpc.setCskl(this.getMasterSet().getString("CSKL"));
  }

  private void eraseFields() {
    rpcLostFocus = false;
//    cijeneUpdated = false;
/*    jraKol.setText("");
    jraCijena.setText("");
    jraIznos.setText(""); */
    this.getDetailSet().setBigDecimal("KOL", _Main.nul);
    this.getDetailSet().setBigDecimal("ZC", _Main.nul);
    this.getDetailSet().setBigDecimal("IRAZ", _Main.nul);
    rcc.EnabDisabAll(jpDetail, false);
  }

  public void EntryPointDetail(char mode) {
    if (mode == 'N') {
      eraseFields();
      rpc.setCART();
    } else if (mode == 'I') {
//      rcc.setLabelLaF(jraCijena, false);
      rcc.setLabelLaF(jraCijena, raIzlazTemplate.allowPriceChange());
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
//    if (!cijeneUpdated && mode == 'N') return false;
    /*if (mode == 'N' && artNotUnique(rpc.getCART())) {
      JOptionPane.showMessageDialog(this.jpDetail,"Artikl ve\u0107 promijenjen!","Greška",
        JOptionPane.ERROR_MESSAGE);
      rpc.EnabDisab(true);
      rpc.setCART();
      eraseFields();
      return false;
    }*/
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
    //ld.raLocate(dm.getSklad(),new String[] {"CSKL"}, new String[] {this.getMasterSet().getString("CSKL")});
    //vrzal = dm.getSklad().getString("VRZAL");
    
    Aut.getAut().calcSkladFigures(this.getDetailSet(), false);
    mjstanje= KalkulStanja(mode);
    if (!testStanje()) return false;
    getDetailSet().setString("STATUS",jraTipKalkul.getDataValue());
    if (getDetailSet().getString("STATUS").equalsIgnoreCase("N")) return true;
  System.out.println("generalBD "+ generalBD);
  System.out.println("getDetailSet().getBigDecimal(KOL) "+ getDetailSet().getBigDecimal("KOL"));

    if (getDetailSet().getBigDecimal("KOL").compareTo(generalBD)>0){
        if (javax.swing.JOptionPane.showConfirmDialog(this, "Unesena kolièina je veæa nego dozvoljena kolièina za otpis \n"+
                "koju ste unijeli na artiklu.\nŽelite li nastaviti ?", "Upit",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.DEFAULT_OPTION)==0) {
            return true;
        } else {
            return false;
        }
    }
    return true;
  }

  public boolean testStanje() {
    if (mjstanje== null) return false;
    if (mjstanje.getBigDecimal("KOL").doubleValue()<0) {
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
    srcString=rut.getSeqString(getMasterSet());
    if (!rut.checkSeq(srcString,Integer.toString(getMasterSet().getInt("BRDOK")))) return false;

    if (!this.getDetailSet().isEmpty()) {
      javax.swing.JOptionPane.showConfirmDialog(null,"Nisu pobrisane stavke dokumenta !","Gre\u0161ka",
          javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  public boolean DeleteCheckDetail() {
System.out.println("Ulazim u DeleteCheckDetail");
    oldRbr = this.getDetailSet().getShort("RBR");
    lc.TransferFromDB2Class(getDetailSet(),stavkaold);
    mjstanje= KalkulStanja('B');
    return testStanje();
  }

  public void AfterDeleteDetail() {
//    Aut.getAut().recalculateRbr(this, "RBR", oldRbr);
  }

  public void Funkcija_ispisa_detail() {
    Asql.createIzlazDetail(repQDS, this);
    super.Funkcija_ispisa_detail();
  }

  public void Funkcija_ispisa_master() {
    Asql.createIzlazDetail(repQDS, this);
    super.Funkcija_ispisa_master();
  }

  private void fillHeader() {
   /* this.getDetailSet().setString("CSKL", this.getMasterSet().getString("CSKL"));
    this.getDetailSet().setString("VRDOK", this.getMasterSet().getString("VRDOK"));
    this.getDetailSet().setString("GOD", this.getMasterSet().getString("GOD"));
    this.getDetailSet().setInt("BRDOK", this.getMasterSet().getInt("BRDOK")); */

    // traži sljede\u0107i redni broj ovog dokumenta
    this.getDetailSet().setShort("RBR", rbr.vrati_rbr("STDOKI",this.getMasterSet().getString("CSKL"),
        this.getMasterSet().getString("VRDOK"),this.getMasterSet().getString("GOD"),
        this.getMasterSet().getInt("BRDOK")));
  }

  private void findKolicinaforOtpis(){
      
      
      
      BigDecimal bizlaz = Aus.zero2;
      BigDecimal bizlazotpsidosada = Aus.zero2;
      BigDecimal postotak = Aus.zero2;
      System.out.println(getDetailSet());
      Condition artstav = Condition.whereAllEqual(
          new String[] {"CSKL", "GOD", "CART"}, getDetailSet());
      
      DataSet ukupnoizaslo = 
              Aus.q("SELECT sum(KOL)as SUMICA from stdoki where "+
                  artstav.and(Condition.in("VRDOK", sdokiz)));
      
      DataSet ukupnodosadotpis = 
         Aus.q("SELECT sum(KOL)as SUMICA from stdoki where "+
             artstav.and(Condition.equal("VRDOK", "OTR")));
      
      int dec = 3;
      if (ld.raLocate(dm.getArtikli(), "CART", 
          Integer.toString(getDetailSet().getInt("CART")))) {

        postotak = dm.getArtikli().getBigDecimal("POSTOINV");
        String cg = dm.getArtikli().getString("CGRART");
        while (postotak.signum() <= 0 && cg != null &&
            cg.length() > 0 && ld.raLocate(dm.getGrupart(), "CGRART", cg)) {
          postotak = dm.getGrupart().getBigDecimal("POSTOINV");
          cg = dm.getGrupart().getString("CGRARTPRIP");
          if (dm.getGrupart().getString("CGRART").equals(cg)) cg = null;
        }
        if (ld.raLocate(dm.getJedmj(), "JM", getDetailSet().getString("JM")))
          dec = dm.getJedmj().getInt("ZNACDEC");
      }
      
      if (ukupnoizaslo.getRowCount()==1) {
          bizlaz =ukupnoizaslo.getBigDecimal("SUMICA"); 
      }
      if (ukupnodosadotpis.getRowCount()==1) {
          bizlazotpsidosada =ukupnodosadotpis.getBigDecimal("SUMICA"); 
      }
      System.out.println("Prije kalk bizlaz "+bizlaz);
      System.out.println("Prije kalk bizlazotpsidosada "+bizlazotpsidosada);
//      bizlaz = bizlaz.subtract(bizlazotpsidosada);
      bizlaz = bizlaz.multiply(postotak);
      bizlaz = bizlaz.divide(new BigDecimal(100), dec, BigDecimal.ROUND_HALF_UP);
      bizlaz = bizlaz.subtract(bizlazotpsidosada);
      System.out.println("Poslije bizlaz "+bizlaz);
      System.out.println(bizlazotpsidosada);
      System.out.println(bizlaz.compareTo(Aus.zero2)>0);


      if (bizlaz.compareTo(Aus.zero2)>0){
          getDetailSet().setBigDecimal("KOL",bizlaz);
          Aut.getAut().calcSkladFigures(this.getDetailSet(), false);
          generalBD = bizlaz;
      } else {
          generalBD = Aus.zero2;
      }
  }
  
  private void updateCijene() {
    // provjeri ima li artikla na stanju
    if (!rpc.AST.findStanje(this.getMasterSet().getString("GOD"),
         this.getMasterSet().getString("CSKL"),this.getDetailSet().getInt("CART"))) {
//      JOptionPane.showMessageDialog(this.jpDetail,"Nema artikla na stanju!","Greška",
//        JOptionPane.ERROR_MESSAGE);
//      rpc.EnabDisab(true);
//      rpc.setCART();
      eraseFields();
      Aut.getAut().handleRpcErr(rpc, "Nema artikla na stanju!");
      return;
    }
    // provjeri nalazi li ve\u0107 u dokumentu isti artikl
    /*if (artNotUnique(rpc.getCART())) {
//      JOptionPane.showMessageDialog(this.jpDetail,"Artikl ve\u0107 u tablici za isti dokument!","Greška",
//        JOptionPane.ERROR_MESSAGE);
//      rpc.EnabDisab(true);
//      rpc.setCART();
      eraseFields();
      Aut.getAut().handleRpcErr(rpc, "Artikl ve\u0107 u tablici za isti dokument!");
      return;
    }*/
    enableKol();
    vl.RezSet = null;
    this.getDetailSet().setBigDecimal("NC", rpc.gettrenStanje().getBigDecimal("NC"));
    this.getDetailSet().setBigDecimal("VC", rpc.gettrenStanje().getBigDecimal("VC"));
    this.getDetailSet().setBigDecimal("MC", rpc.gettrenStanje().getBigDecimal("MC"));
    this.getDetailSet().setBigDecimal("ZC", rpc.gettrenStanje().getBigDecimal("ZC"));
    jraKol.requestFocus();
  }

  /*private void KolUpdated() {
    Aut.getAut().calcSkladFigures(this.getDetailSet(), false);
  }*/

  private void jbInit() throws Exception {

    this.setMasterSet(dm.getZagOtp());
    this.setNaslovMaster("Otpis robe");
    this.setVisibleColsMaster(new int[] {1,4,5});
    this.setMasterKey(key);
    this.setDetailSet(dm.getStOtp());
    this.setNaslovDetail("Stavke otpisa robe");
    this.setVisibleColsDetail(new int[] {4,Aut.getAut().getCARTdependable(5,6,7),8,9,11,33,34});
    this.setDetailKey(Util.dkey);
//    this.setMasterDeleteMode(DELDETAIL);
    this.raMaster.getRepRunner().addReport("hr.restart.robno.repOtpisRobeExtendedVersion","Ispis svih otpisa", 2);
    this.raMaster.getRepRunner().addReport("hr.restart.robno.repOtpisRobe","Ispis svih otpisa - koli\u010Dinski",2);
    this.raMaster.getRepRunner().addReport("hr.restart.robno.repOtpisRobeMegablastVersion","Ispis svih otpisa - detaljno",2);
    this.raDetail.getRepRunner().addReport("hr.restart.robno.repOtpisRobeExtendedVersion","Ispis otpisa robe",2);
    this.raDetail.getRepRunner().addReport("hr.restart.robno.repOtpisRobe","Ispis otpisa robe - koli\u010Dinski",2);
    this.raDetail.getRepRunner().addReport("hr.restart.robno.repOtpisRobeMegablastVersion","Ispis svih otpisa - detaljno",2);
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
    xYLayout2.setHeight(85);

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
    
    
    QueryDataSet qds = new QueryDataSet();
    qds.setColumns(new Column[]{dM.createStringColumn("STATUS","Tip otpisa",1),
            dM.createStringColumn("OPIS","Vrsta otpis",50)});
    
    qds.open();
    qds.insertRow(false);
    qds.setString("STATUS","K");
    qds.setString("OPIS","Kalo");
    
    qds.insertRow(false);
    qds.setString("STATUS","R");
    qds.setString("OPIS","Rastur");
    
    qds.insertRow(false);
    qds.setString("STATUS","L");
    qds.setString("OPIS","Lom");
    
    qds.insertRow(false);
    qds.setString("STATUS","N");
    qds.setString("OPIS","Nedozvoljeni m.");

    jraTipKalkul.setRaDataSet(this.getDetailSet());
    jraTipKalkul.setRaColumn("STATUS");
    jraTipKalkul.setRaItems(qds, "STATUS", "OPIS");

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
    jpDetail.add(new JLabel("Vrsta otpisa"), new XYConstraints(15, 55, 74, -1));
    jpDetail.add(jraTipKalkul, new XYConstraints(150, 55, 150, -1));
    
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
    /*rpc.setGodina(hr.restart.util.Valid.getValid().findYear(dm.getDoku().getTimestamp("DATDOK")));
    rpc.setCskl(dm.getStdoku().getString("CSKL"));*/
    rpc.setTabela(this.getDetailSet());
    rpc.setMode("N");
    rpc.setBorder(BorderFactory.createEtchedBorder());
    rpc.setDefParam();
    rpc.InitRaPanCart();
    rpc.setnextFocusabile(this.jraKol);
  }

  private void initNewMaster() {
    this.getPreSelect().copySelValues();
    this.getMasterSet().setString("VRDOK", "OTR");
    this.getMasterSet().setTimestamp("DATDOK",
        vl.getPresToday(getPreSelect().getSelRow()));
    this.getMasterSet().setString("GOD",vl.findYear(getMasterSet().getTimestamp("DATDOK")));
  }


  public boolean doBeforeSaveMaster(char mode) {
    if (mode == 'N') hr.restart.robno.Util.getUtil().getBrojDokumenta(getMasterSet());
    return true;
  }

  public boolean doWithSaveMaster (char mode){
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
    if (mode == 'N') {
      getDetailSet().setInt("RBSID", rbr.getRbsID(getDetailSet()));
      getDetailSet().setString(
              "ID_STAVKA",
              raControlDocs.getKey(getDetailSet(), new String[] { "cskl",
                      "vrdok", "god", "brdok", "rbsid" }, "stdoku"));
      hr.restart.util.raTransaction.saveChanges(getDetailSet());
    }
    if (mode=='B') {
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
    rKBD.kalkStanjefromStdoki(stanje,stavka ,stavkaold,
                              getDetailSet().getString("VRDOK"));
    lc.TransferFromClass2DB(DBStanje,stanje);
    return DBStanje;
  }
}
