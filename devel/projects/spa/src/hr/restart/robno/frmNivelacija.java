/****license*****************************************************************
**   file: frmNivelacija.java
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
import hr.restart.baza.Stanje;
import hr.restart.baza.Stdoku;
import hr.restart.baza.dM;
import hr.restart.baza.stdoki;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: frmNivelacija</p>
 * <p>Description: Prozor za promjenu cijena artikala. Naslje\u0111uje raMasterDetail i ve\u0107inu
 * njegove funkcionalnosti.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: REST-ART</p>
 * @author abf
 * @version 1.0
 */

public class frmNivelacija extends raMasterDetail {
  // pomo\u0107ne klase
  raCommonClass rcc = raCommonClass.getraCommonClass();
  hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  lookupData ld = lookupData.getlookupData();
  Valid vl = Valid.getValid();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  Rbr rbr = Rbr.getRbr();
  _Main ma;
  dM dm;

  // GUI komponente DetailPanela za oba prozora.
  XYLayout xYLayout1 = new XYLayout();
  JPanel jpMasterMain = new JPanel();
  JPanel jpMaster = new JPanel();
  JLabel jlNaziv = new JLabel();
  JraButton jbSelSkl = new JraButton();
  JlrNavField jlrSklNaziv = new JlrNavField() {
    public void after_lookUp() {
      jraDatumDok.requestFocus();
    }
  };
  JLabel jlSkl = new JLabel();
  JLabel jlOznaka = new JLabel();
  JlrNavField jlrSkl = new JlrNavField() {
    public void after_lookUp() {
      jraDatumDok.requestFocus();
    }
  };
  JLabel jlDatumDok = new JLabel();
  JraTextField jraDatumDok = new JraTextField();
  JPanel jpDetail = new JPanel();
  JPanel jpDetailMain = new JPanel();
  XYLayout xYLayout2 = new XYLayout();
  TitledBorder titledBorder1;
  JPanel jPanel1 = new JPanel();
  XYLayout xYLayout3 = new XYLayout();
  JraTextField jraVPStara = new JraTextField();
  JLabel jlVPStara = new JLabel();
  JraTextField jraVPNova = new JraTextField() {
    public void valueChanged() {
      VPNovaUpdated();
    }
  };
  JLabel jlVPNova = new JLabel();
  JLabel jlPromjena = new JLabel();
  JraTextField jraPromjena = new JraTextField() {
    public void valueChanged() {
      if (isValueChanged()) PromjenaUpdated();
    }
  };
  JPanel jPanel2 = new JPanel();
  TitledBorder titledBorder2;
  JLabel jlMPStara = new JLabel();
  XYLayout xYLayout4 = new XYLayout();
  JLabel jlMPNova = new JLabel();
  JraTextField jraMPStara = new JraTextField();
  JraTextField jraMPNova = new JraTextField() {
    public void valueChanged() {
      MPNovaUpdated();
    }
  };
  JLabel jlPorez = new JLabel();
  JraTextField jraPorez = new JraTextField();
  JTextField jraOznakaPoreza = new JTextField();
  JLabel jlPosto1 = new JLabel();
  JLabel jlPosto2 = new JLabel();
  JLabel jlOznakaPoreza = new JLabel();
  raControlDocs rCD = new raControlDocs();   // dodao /.V radi provjera datuma i kalkulacija i bla bla
  private QueryDataSet stanjeSet = new QueryDataSet();
  // klasa koja definira i ispisuje broj dokumenta
  rajpBrDok jpMasterHeader = new rajpBrDok();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();

  /*
   * Klasa za dohva\u0107anje artikla. Overridana je metoda metToDo_after_lookUp koja
   * poziva metodu sa popunjavanje cijena (iz tablice stanja), i to samo ako
   * se unosi nova stavka dokumenta.
   */
  rapancart rpc = new rapancart() {
    public void metToDo_after_lookUp() {
       if (!rpcLostFocus && raDetail.getMode() == 'N') {
         rpcLostFocus = true;
         updateCijene();
/*         enableProm();
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

  /*
   * Flag koji služi da se updateCijene ne bi nepotrebno pozivao svaki put kad
   * rapancart izgubi fokus. Jednom je dovoljno.
   */
  boolean rpcLostFocus/*, cijeneUpdated*/;
  String vrzal;
  short oldRbr;

  // Stare vrijednosti poravnanja
  BigDecimal oldpormar, oldporpor, oldporav, oldsvc, oldsmc;
  int oldCart;
  String oldCskl, oldGod;

  /*
   * Dummy DataSet da bi se mogla postaviti maska ###,###,##0.00 na polje
   * Promjena cijene %, koje se ne nalazi niti u jednoj tablici.
   * - dodana je i kolona UKUPOR u isti dataset.
   */
  StorageDataSet pr = new StorageDataSet();
  Column prom = dM.createBigDecimalColumn("PROMJENA", "Promjena cijene");
  Column ukupor = dM.createBigDecimalColumn("UKUPOR", "Porez");

  // veza zaglavlja i stavki dokumenta
  //String[] key = util.mkey;

  dlgPromArt pa = new dlgPromArt(this.getJframe(), "Artikli");
  /*
   * DataSet za privremenu pohranu svih artikala iz tablice stanja, koji
   * pripadaju nekom skladištu/godini.
   */
  QueryDataSet allArt, repQDS = new QueryDataSet();

  static frmNivelacija frm;

  public frmNivelacija() {
    try {
      frm = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static frmNivelacija getInstance() {
    return frm;
  }

  public QueryDataSet getRepQDS() {
    return repQDS;
  }

  private void jbInit() throws Exception {
    dm = dM.getDataModule();
    /*
     * Podešavanje opcija raMasterDetail klase.
     * Za master dio potrebno je zadati odgovaraju\u0107i DataSet, zatim naslov,
     * stupce master DataSet-a koji \u0107e se prikazivati u tabli\u010Dnom prikazu master
     * dijela, te klju\u010D kojim se vežu master i detail DataSet.
     * VAŽNA NAPOMENA: DetailPanel master dijela se setira na kraju! Zato jer
     * je metoda setJPanelMaster jedino mjesto na kojem se poziva metoda pack()
     * za master frame, koja prilago\u0111ava dimenzije master framea tako da se podudara
     * sa dimenzijama DetailPanela mastera.
     * Za detail dio DetailPanel se može postaviti odmah jer se njegov pack() poziva
     * tek kasnije.
     */

    this.setMasterSet(dm.getDokuPOR());
    this.setNaslovMaster("Nivelacije");
    this.setVisibleColsMaster(new int[] {4,5});
    this.setMasterKey(Util.mkey);

    raMaster.getRepRunner().addReport("hr.restart.robno.repNivel", "Formatirani ispis", 2);
//    raMaster.getJpTableView().addTableModifier(
//        new raTableColumnModifier("CSKL", new String[] {"CSKL", "NAZSKL"}, dm.getSklad()));
    raMaster.installSelectionTracker("BRDOK");

    this.setDetailSet(dm.getStdokuPOR());
    this.setJPanelDetail(jpDetailMain);
    this.setNaslovDetail("Stavke promjene cijena");
    this.setVisibleColsDetail(new int[] {4,Aut.getAut().getCARTdependable(5,6,7),8,34,22,35,39});
    this.setDetailKey(Util.dkey);

    raDetail.getRepRunner().addReport("hr.restart.robno.repNivel", "Formatirani ispis", 2);

    this.setMasterDeleteMode(DELDETAIL);
    pr.setColumns(new Column[] {prom,ukupor});

    /*
     * podešavanje GUI komponenti DetailPanela i za master i za detail dio.
     */
    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,Color.gray)," Prodajna cijena bez poreza ");
    titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,Color.gray)," Prodajna cijena s porezom ");
    jpMaster.setLayout(xYLayout1);
    jlNaziv.setText("Naziv");
    jbSelSkl.setText("...");

    /*
     * Postavljanje parametara za jlrNavField-e za biranje skladišta.
     */
    jlrSkl.setColumnName("CSKL");
    jlrSkl.setTextFields(new JTextComponent[] {jlrSklNaziv});
    jlrSkl.setColNames(new String[] {"NAZSKL"});
    jlrSkl.setSearchMode(0);
    jlrSkl.setDataSet(this.getMasterSet());
    jlrSkl.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
    jlrSkl.setVisCols(new int[] {0,1});
    jlrSkl.setNavButton(jbSelSkl);

    jlrSklNaziv.setColumnName("NAZSKL");
    jlrSklNaziv.setNavProperties(jlrSkl);
    jlrSklNaziv.setSearchMode(1);

    jlSkl.setText("Skladište");
    jlOznaka.setToolTipText("");
    jlOznaka.setText("Šifra");

    xYLayout1.setWidth(645);
    xYLayout1.setHeight(115);
    jlDatumDok.setText("Datum Dokumenta");
    jraDatumDok.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatumDok.setColumnName("DATDOK");
    jraDatumDok.setDataSet(this.getMasterSet());
//    new raDatePopup(jraDatumDok);
    jpDetail.setLayout(xYLayout2);
    jPanel1.setBorder(titledBorder1);
    jPanel1.setLayout(xYLayout3);
    jlVPStara.setHorizontalAlignment(SwingConstants.TRAILING);
    jlVPStara.setText("Stara cijena");
    jlVPNova.setHorizontalAlignment(SwingConstants.TRAILING);
    jlVPNova.setText("Nova cijena");
    jraVPNova.setHorizontalAlignment(SwingConstants.TRAILING);
    jraVPNova.setDataSet(this.getDetailSet());
    jraVPNova.setColumnName("VC");

    jraVPStara.setHorizontalAlignment(SwingConstants.TRAILING);
    jraVPStara.setDataSet(this.getDetailSet());
    jraVPStara.setColumnName("SVC");
    jlPromjena.setText("Promjena cijene");
    jraPromjena.setHorizontalAlignment(SwingConstants.TRAILING);

    /*
     * Podešavanje polja za promjenu veleprd cijene. Povezivanje s
     * dummy DataSet-om radi definiranja odgovaraju\u0107e maske.
     * Isto se radi za ostala polja koja su bindana na dummy dataset.
     */
    jraPromjena.setColumnName("PROMJENA");
    jraPromjena.setDataSet(pr);
    jraPromjena.setPostOnRowPosted(false);

    jraPorez.setColumnName("UKUPOR");
    jraPorez.setDataSet(pr);
    jraPorez.setPostOnRowPosted(false);

    xYLayout2.setWidth(600);
    xYLayout2.setHeight(270);
    jPanel2.setBorder(titledBorder2);
    jPanel2.setLayout(xYLayout4);
    jlMPStara.setText("Stara cijena");
    jlMPStara.setHorizontalAlignment(SwingConstants.TRAILING);
    jlMPNova.setText("Nova cijena");
    jlMPNova.setHorizontalAlignment(SwingConstants.TRAILING);
    jraMPStara.setHorizontalAlignment(SwingConstants.TRAILING);
    jraMPStara.setDataSet(this.getDetailSet());
    jraMPStara.setColumnName("SMC");
    jraMPNova.setHorizontalAlignment(SwingConstants.TRAILING);
    jraMPNova.setDataSet(this.getDetailSet());
    jraMPNova.setColumnName("MC");

    jlPorez.setText("Porez");
    jlPosto1.setHorizontalAlignment(SwingConstants.TRAILING);
    jlPosto1.setText("(%)");
    jlPosto2.setText("(%)");
    jlPosto2.setHorizontalAlignment(SwingConstants.TRAILING);
    jlOznakaPoreza.setHorizontalAlignment(SwingConstants.TRAILING);
    jlOznakaPoreza.setText("Šifra");

    /*
     * jraOznakaPoreza i jraPorez nisu bindani na odgovaraju\u0107i dataset.
     * Stopa poreza se mora uzeti iz tablice Porezi, a oznaka poreza
     * iz tablice Artikli. Budu\u0107i da se ionako ru\u010Dno mora tražiti odgovaraju\u0107i
     * slog u tim tablicama, \u010Dini mi se da je bolje da se onda uop\u0107e ne
     * bindaju, nego da se ru\u010Dno pune.
     */
    jraOznakaPoreza.setHorizontalAlignment(SwingConstants.TRAILING);
    jraPorez.setHorizontalAlignment(SwingConstants.TRAILING);

    jpMaster.add(jlSkl, new XYConstraints(15, 40, -1, -1));
    jpMaster.add(jlrSkl, new XYConstraints(150, 40, 100, -1));
    jpMaster.add(jlrSklNaziv, new XYConstraints(255, 40, 350, -1));
    jpMaster.add(jbSelSkl, new XYConstraints(611, 40, 21, 21));
    jpMaster.add(jlOznaka, new XYConstraints(151, 20, -1, -1));
    jpMaster.add(jlNaziv, new XYConstraints(256, 20, -1, -1));
    jpMaster.add(jlDatumDok, new XYConstraints(15, 70, -1, -1));
    jpMaster.add(jraDatumDok, new XYConstraints(150, 70, 100, -1));
    jpDetail.add(jPanel1, new XYConstraints(20, 20, 605, 105));
    jPanel1.add(jraVPStara, new XYConstraints(475, 10, 100, -1));
    jPanel1.add(jlVPStara, new XYConstraints(350, 10, 95, -1));
    jPanel1.add(jraVPNova, new XYConstraints(475, 35, 100, -1));
    jPanel1.add(jlVPNova, new XYConstraints(350, 35, 95, -1));
    jPanel1.add(jlPromjena, new XYConstraints(15, 35, -1, -1));
    jPanel1.add(jraPromjena, new XYConstraints(240, 35, 75, -1));
    jPanel1.add(jlPosto1, new XYConstraints(265, 15, 49, -1));

    jpDetail.add(jPanel2, new XYConstraints(20, 140, 605, 105));
    jPanel2.add(jlMPStara, new XYConstraints(350, 10, 95, -1));
    jPanel2.add(jlMPNova, new XYConstraints(350, 35, 95, -1));
    jPanel2.add(jraMPStara, new XYConstraints(475, 10, 100, -1));
    jPanel2.add(jraMPNova, new XYConstraints(475, 35, 100, -1));
    jPanel2.add(jlPorez, new XYConstraints(15, 35, -1, -1));
    jPanel2.add(jraPorez, new XYConstraints(240, 35, 75, -1));
    jPanel2.add(jraOznakaPoreza, new XYConstraints(150, 35, 85, -1));
    jPanel2.add(jlPosto2, new XYConstraints(265, 15, 49, -1));
    jPanel2.add(jlOznakaPoreza, new XYConstraints(185, 14, 49, -1));

    jpDetailMain.setLayout(new BorderLayout());

    /*
     * dodavanje jpMasterHeader= new rajpBrDoc() koji se brine za ispis
     * broja dokumenta
     */
    jpMasterHeader.setDataSet(this.getMasterSet());
    jpMasterHeader.addBorder();

    jpMaster.setBorder(BorderFactory.createEtchedBorder());
    jpMasterMain.setLayout(new BorderLayout());
    jpMasterMain.add(jpMasterHeader, BorderLayout.NORTH);
    jpMasterMain.add(jpMaster, BorderLayout.CENTER);
    /*jpMaster.setPreferredSize(jpMaster.getMinimumSize());
    jpMasterHeader.setMinimumSize(jpMasterHeader.getPreferredSize());
    jpMasterMain.setMinimumSize(new Dimension(jpMaster.getMinimumSize().width,
      jpMaster.getMinimumSize().height+jpMasterHeader.getMinimumSize().height));
    jpMasterMain.setPreferredSize(jpMasterMain.getMinimumSize());*/

    // dodavanje rapancart-a, za dohvat artikla
    jpDetail.setBorder(BorderFactory.createEtchedBorder());
    jpDetailMain.add(rpc, BorderLayout.NORTH);
    jpDetailMain.add(jpDetail, BorderLayout.CENTER);

    this.setUserCheck(true);
    // ovdje se napokon postavlja DetailPanel za mastera
    this.setJPanelMaster(jpMasterMain);

    /*
     * Dodavanje listenera. Jedan actionlistener za dugme kojim se
     * dohva\u0107a skladište (u master dijelu), i focuslisteneri u detail
     * dijelu koji pozivaju odgovaraju\u0107e prora\u010Dune veleprodajnih i
     * maloprodajnih cijena na focusLost odre\u0111ene komponente.
     */
    /*jraVPNova.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        VPNovaUpdated();
      }
    });
    jraMPNova.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        MPNovaUpdated();
      }
    });
    jraPromjena.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        PromjenaUpdated();
      }
    });*/
    raDetail.addOption(new raNavAction("Artikli po grupama", raImages.IMGHISTORY, KeyEvent.VK_F7) {
      public void actionPerformed(ActionEvent e) {
        getMultiple();
      }
    }, 4, false);

    raDetail.addOption(new raNavAction("Promjena poreza", raImages.IMGMOVIE, KeyEvent.VK_F8, KeyEvent.SHIFT_MASK) {
      public void actionPerformed(ActionEvent e) {
        changePorez();
      }
    }, 5, false);
    
    raDetail.addOption(new raNavAction("Poravnavanje stanja", raImages.IMGPROPERTIES, KeyEvent.VK_F8) {
      public void actionPerformed(ActionEvent e) {
        changeMath();
      }
    }, 6, false);

//    raDetail.addKeyAction(new raKeyAction(java.awt.event.KeyEvent.VK_F7) {
//      public void keyAction() {
//        if (!rpcLostFocus && raDetail.getMode() == 'N')
//          getMultiple();
//      }
//    });
  }

  private void findQDS(boolean master) {
    Condition brdok = Condition.equal("BRDOK", getMasterSet().getInt("BRDOK"));
    if (master && raMaster.getSelectCondition() != null)
      brdok = raMaster.getSelectCondition();

    System.out.println(brdok);

    String qstr = "SELECT doku.cskl, doku.vrdok, doku.god, doku.brdok, doku.datdok, doku.corg, "+
                  "stdoku.rbr, stdoku.cart, stdoku.cart1, stdoku.bc, stdoku.nazart, "+
                  "stdoku.jm, stdoku.skol, stdoku.svc, stdoku.smc, stdoku.vc, stdoku.mc, "+
                  "stdoku.porav FROM doku,stdoku WHERE "+rut.getDoc("doku","stdoku")+
                  " AND doku.cskl='"+getMasterSet().getString("CSKL")+
                  "' AND doku.god='"+getMasterSet().getString("GOD")+"' AND doku.vrdok='POR'"+
                  " AND "+brdok.qualified("DOKU");
    repQDS.close();
    repQDS.closeStatement();
    repQDS.setQuery(new QueryDescriptor(dm.getDatabase1(), qstr));
    repQDS.open();
  }

  public void Funkcija_ispisa_master() {
    findQDS(true);
    super.Funkcija_ispisa_master();
  }

  public void Funkcija_ispisa_detail() {
    findQDS(false);
    super.Funkcija_ispisa_detail();
  }

  // inicijalizacija rapancarta. Mode = "DOH" postavlja mod na dohva\u0107anje (bez izmjene)
  private void initRpcart() {
    /*rpc.setGodina(hr.restart.util.Valid.getValid().findYear(dm.getDoku().getTimestamp("DATDOK")));
    rpc.setCskl(dm.getStdoku().getString("CSKL"));*/
    rpc.setTabela(this.getDetailSet());
    rpc.setMode("DOH");
    rpc.setMode("N");
    rpc.setPreferredSize(new Dimension(645, 100));
    rpc.setFocusCycleRoot(true);
    rpc.setBorder(BorderFactory.createEtchedBorder());
    rpc.setDefParam();
    rpc.InitRaPanCart();
    rpc.setnextFocusabile(this.jraPromjena);
  }

  private void initNewMaster() {
//    this.getMasterSet().setString("CUSER", hr.restart.sisfun.raLock.getRaLock().getUser());
    this.getMasterSet().setString("VRDOK", "POR");
    this.getMasterSet().setTimestamp("DATDOK", 
        vl.getPresToday(getPreSelect().getSelRow()));
//    this.getMasterSet().setString("GOD",vl.findYear(getMasterSet().getTimestamp("DATDOK")));
  }

  /*
   * Metoda za popunjavanje zaglavlja dokumenta. Automatski dodjeljuje sljede\u0107i
   * redni broj dokumenta za dano skladište, godinu i vrstu dokumenta.
   */
//  private void getBrojDokumenta(com.borland.dx.dataset.DataSet ds) {
//    Integer Broj;
//    Broj=vl.findSeqInteger(ds.getString("CSKL")+ds.getString("VRDOK")+ds.getString("GOD"), false);
//    ds.setInt("BRDOK",Broj.intValue());
//  }
  
  private void changeMath() {
  	ld.raLocate(dm.getSklad(), "CSKL", getMasterSet().getString("CSKL"));
  	if (!dm.getSklad().getString("VRZAL").equals("M")) {
  		JOptionPane.showMessageDialog(raDetail.getWindow(), "Pogrešna vrsta zalihe skladišta!", "Skladište", 
  				JOptionPane.WARNING_MESSAGE);
  		return;
  	}
  	
  	int opt = JOptionPane.showConfirmDialog(raDetail.getWindow(),
        "Poravnati matematiku stanja na svim artiklima?", "Poravnavanje",
        JOptionPane.OK_CANCEL_OPTION);
  	if (opt != JOptionPane.OK_OPTION) return;
  	
  	raProcess.runChild(raDetail.getWindow(), new Runnable() {
      public void run() {
        raProcess.setMessage("Dohvat artikala ...", false);
        fixDiff();
      }
    });
  	
  }
  
  private void changePorez() {
  	ld.raLocate(dm.getSklad(), "CSKL", getMasterSet().getString("CSKL"));
  	if (!dm.getSklad().getString("VRZAL").equals("M")) {
  		JOptionPane.showMessageDialog(raDetail.getWindow(), "Pogrešna vrsta zalihe skladišta!", "Skladište", 
  				JOptionPane.WARNING_MESSAGE);
  		return;
  	}
  	
    int opt = JOptionPane.showConfirmDialog(raDetail.getWindow(),
        "Zadržati iznos cijene s porezom?", "Naèin promjene",
        JOptionPane.YES_NO_CANCEL_OPTION);
    boolean mpc = true;
    if (opt == JOptionPane.YES_OPTION) mpc = true;
    else if (opt == JOptionPane.NO_OPTION) mpc = false;
    else return;
    
    final boolean mpcfix = mpc;
    
    raProcess.runChild(raDetail.getWindow(), new Runnable() {
      public void run() {
        raProcess.setMessage("Dohvat artikala ...", false);
        fixPorezAll(mpcfix);
      }
    });

  }
  
  void fixDiff() {
    QueryDataSet all = Aus.q("select stanje.cart, artikli.cart1, " +
        "artikli.bc, artikli.nazart, artikli.jm, artikli.cpor, stanje.vc, stanje.mc, stanje.kol "+ 
        "from stanje,artikli where stanje.cart = artikli.cart and " +
        "stanje.cskl = '" + this.getMasterSet().getString("CSKL") + "' and " +
        "stanje.god = '" + this.getMasterSet().getString("GOD") + "'");
    
    int count = 0, crbr = 0;
    DataSet cartds = Stdoku.getDataModule().getTempSet(
        Condition.whereAllEqual(Util.mkey, getMasterSet()));
    cartds.open();
    HashSet carts = new HashSet();
    for (cartds.first(); cartds.inBounds(); cartds.next()) {
      carts.add(new Integer(cartds.getInt("CART")));
      if (cartds.getShort("RBR") > count)
        count = cartds.getShort("RBR");
    }
    
    raDetail.getJpTableView().enableEvents(false);
    QueryDataSet st = Stanje.getDataModule().getTempSet(
        Condition.whereAllEqual(new String[] {"CSKL", "GOD"}, getMasterSet()));
    st.open();
    
    raProcess.setMessage("Rekalkulacija matematike stanja ...", false);
    
    BigDecimal min = new BigDecimal(1).movePointLeft(2);
    
    for (all.first(); all.inBounds(); all.next()) {
      if (carts.contains(new Integer(all.getInt("CART")))) {
        System.out.println(all.getInt("CART") + " postoji, preskocen..");
        continue;
      }
      
      ld.raLocate(st, "CART", all.getInt("CART")+"");
      
      BigDecimal diff = st.getBigDecimal("ZC").multiply(st.getBigDecimal("KOL"))
      									.setScale(2, BigDecimal.ROUND_HALF_UP).subtract(st.getBigDecimal("VRI"));
      
      if (diff.abs().compareTo(min) <= 0) continue;
      
      ld.raLocate(dm.getPorezi(), "CPOR", all.getString("CPOR"));
      BigDecimal ukpor = dm.getPorezi().getBigDecimal("UKUPOR");
      BigDecimal realpor = ukpor.movePointLeft(2).add(Aus.one0);
      BigDecimal mc = all.getBigDecimal("MC");
      BigDecimal vc = all.getBigDecimal("VC");
      BigDecimal kol = all.getBigDecimal("KOL");
      BigDecimal nmc = mc;
      BigDecimal nvc = vc;
      
      ++count;

      this.getDetailSet().insertRow(false);
      
//    popuni odgovaraju\u0107a polja u tablici stdoku
      fillHeader(crbr == 0);
      if (crbr == 0) crbr = getDetailSet().getShort("RBR");
      else {
        getDetailSet().setShort("RBR", (short) ++crbr);
        getDetailSet().setInt("RBSID", crbr);
      }
      Aut.getAut().copyArtFields(this.getDetailSet(), all);
      getDetailSet().setBigDecimal("SVC", vc);
      getDetailSet().setBigDecimal("SMC", mc);
      getDetailSet().setBigDecimal("SKOL", kol);
      getDetailSet().setBigDecimal("VC", nvc);
      getDetailSet().setBigDecimal("MC", nmc);
      
      getDetailSet().setBigDecimal("PORAV", diff);
      getDetailSet().setBigDecimal("DIOPORMAR", diff.divide(realpor, 2, BigDecimal.ROUND_HALF_UP));
      getDetailSet().setBigDecimal("DIOPORPOR", diff.subtract(getDetailSet().getBigDecimal("DIOPORMAR")));
     
      rut.updateStanje(ma.nul, ma.nul, ma.nul, ma.nul, ma.nul,
          ma.nul, ma.nul, ma.nul,
          'N', true, 'N', st, getDetailSet());
// rut.updateStanje(oldpormar, oldporpor, oldporav, 'N', true, 'N');

      getDetailSet().post();
      
    }
    
    raProcess.setMessage("Spremanje promjene ...", false);
    raTransaction.saveChangesInTransaction(
        new QueryDataSet[] {getDetailSet(), st});
    raDetail.getJpTableView().enableEvents(true);
    raDetail.jeprazno();
  }
  
  void fixPorezAll(boolean mpc) {
    QueryDataSet all = Aus.q("select stanje.cart, artikli.cart1, " +
    	"artikli.bc, artikli.nazart, artikli.jm, artikli.cpor, stanje.vc, stanje.mc, stanje.kol "+ 
        "from stanje,artikli where stanje.cart = artikli.cart and " +
        "stanje.cskl = '" + this.getMasterSet().getString("CSKL") + "' and " +
        "stanje.god = '" + this.getMasterSet().getString("GOD") + "'");
    
    int count = 0, crbr = 0;
    DataSet cartds = Stdoku.getDataModule().getTempSet(
        Condition.whereAllEqual(Util.mkey, getMasterSet()));
    cartds.open();
    HashSet carts = new HashSet();
    for (cartds.first(); cartds.inBounds(); cartds.next()) {
      carts.add(new Integer(cartds.getInt("CART")));
      if (cartds.getShort("RBR") > count)
        count = cartds.getShort("RBR");
    }
    
    raDetail.getJpTableView().enableEvents(false);
    QueryDataSet st = Stanje.getDataModule().getTempSet(
        Condition.whereAllEqual(new String[] {"CSKL", "GOD"}, getMasterSet()));
    st.open();
    
    raProcess.setMessage("Kalkulacija cijena ...", false);
    
    for (all.first(); all.inBounds(); all.next()) {
      if (carts.contains(new Integer(all.getInt("CART")))) {
        System.out.println(all.getInt("CART") + " postoji, preskocen..");
        continue;
      }

      ld.raLocate(dm.getPorezi(), "CPOR", all.getString("CPOR"));
      ld.raLocate(st, "CART", all.getInt("CART")+"");
      BigDecimal ukpor = dm.getPorezi().getBigDecimal("UKUPOR");
      BigDecimal realpor = ukpor.movePointLeft(2).add(Aus.one0);
      BigDecimal mc = all.getBigDecimal("MC");
      BigDecimal vc = all.getBigDecimal("VC");
      BigDecimal kol = all.getBigDecimal("KOL");
      if (vc.signum() > 0) {
        BigDecimal oldpor = mc.subtract(vc).divide(vc, 4, 
            BigDecimal.ROUND_HALF_UP).movePointRight(2);
        if (oldpor.add(oldpor).subtract(ukpor.add(ukpor)).abs().
            compareTo(Aus.one0) >= 0) {
          BigDecimal nmc = mc;
          BigDecimal nvc = vc;
          if (mpc) nvc = nmc.divide(realpor, 2, BigDecimal.ROUND_HALF_UP);
          else nmc = nvc.multiply(realpor).setScale(2, BigDecimal.ROUND_HALF_UP);
          
          ++count;

          this.getDetailSet().insertRow(false);

          // popuni odgovaraju\u0107a polja u tablici stdoku
          fillHeader(crbr == 0);
          if (crbr == 0) crbr = getDetailSet().getShort("RBR");
          else {
            getDetailSet().setShort("RBR", (short) ++crbr);
            getDetailSet().setInt("RBSID", crbr);
          }
          Aut.getAut().copyArtFields(this.getDetailSet(), all);
          getDetailSet().setBigDecimal("SVC", vc);
          getDetailSet().setBigDecimal("SMC", mc);
          getDetailSet().setBigDecimal("SKOL", kol);
          getDetailSet().setBigDecimal("VC", nvc);
          getDetailSet().setBigDecimal("MC", nmc);
          
          if (mpc) {
            getDetailSet().setBigDecimal("PORAV", Aus.zero2);
            BigDecimal pormar = nvc.subtract(vc).multiply(kol).
                  setScale(2, BigDecimal.ROUND_HALF_UP);
            getDetailSet().setBigDecimal("DIOPORMAR", pormar);
            getDetailSet().setBigDecimal("DIOPORPOR", pormar.negate());
          } else {
            BigDecimal porav = nmc.subtract(mc).multiply(kol).
                  setScale(2, BigDecimal.ROUND_HALF_UP);
            getDetailSet().setBigDecimal("PORAV", porav);
            getDetailSet().setBigDecimal("DIOPORMAR", Aus.zero2);
            getDetailSet().setBigDecimal("DIOPORPOR", porav);
          }
         
          rut.updateStanje(ma.nul, ma.nul, ma.nul, ma.nul, ma.nul,
              ma.nul, ma.nul, ma.nul,
              'N', true, 'N', st, getDetailSet());
//     rut.updateStanje(oldpormar, oldporpor, oldporav, 'N', true, 'N');

          getDetailSet().post();
        }
      }
    }
    
    raProcess.setMessage("Spremanje promjene ...", false);
    raTransaction.saveChangesInTransaction(
        new QueryDataSet[] {getDetailSet(), st});
    raDetail.getJpTableView().enableEvents(true);
    raDetail.jeprazno();
  }

  /**
   * Metoda koja otvara prozor za zadavanje kriterija za promjenu cijena više
   * artikala odjednom. Bira se grupa artikala ili svi artikli sa skladišta.
   */
  private int status = 0;
  private void getMultiple() {
    pa.show();
    if (pa.isOK() && pa.getPromjena().signum() != 0) {
      raProcess.runChild(raDetail.getWindow(), new Runnable() {
        public void run() {
          raProcess.setMessage("Dohvat artikala ...", false);
          if (getStanjeArt())
            status = updateAllArt();
          else status = 2;
        }
      });
      if (status != 0) {
        String[] errmes = {
          "Svim odabranim artiklima cijena je veæ promijenjena u ovom dokumentu!",
          "Nema artikala na stanju koji pripadaju odabranoj grupi!",
          "Transakcija nije uspjela!"};
        JOptionPane.showMessageDialog(jpDetail,errmes[status-1],"Greška",JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  /*
   * Metoda za dohva\u0107anje svih artikala koji pripadaju nekom skladištu/godini,
   * iz tablice stanja. Rezultiraju\u0107i dataset se \u010Duva u allArt.
   */
  private boolean getStanjeArt() {
    String grart = "";
    dm.getGrupart().open();
    if (!pa.isAll()) grart =
      Aus.getDataTreeList(pa.getGrupart(),dm.getGrupart(),"CGRART","CGRARTPRIP");
    if (grart == null) return false;
    else if (grart != "") grart = " and artikli." + grart;
    
    String q = "select stanje.cart, artikli.cart1, artikli.bc, artikli.nazart, artikli.jm, stanje.vc, stanje.mc "+
        "from stanje,artikli where stanje.cart = artikli.cart and " +
        "stanje.cskl = '" + this.getMasterSet().getString("CSKL") + "' and " +
        "stanje.god = '" + this.getMasterSet().getString("GOD") +"' "+grart;
    
    System.out.println(q);

    vl.execSQL(q);
    allArt = vl.RezSet;
    allArt.open();
    return (!allArt.isEmpty());
  }

  private int updateAllArt() {
    int count = 0, crbr = 0;
    String cart;
    raDetail.getJpTableView().enableEvents(false);
    dm.getStanje().refresh();
    // izra\u010Dunaj faktor kojim treba množiti cijene ( (promjena+100) / 100 )
    BigDecimal vcmul = (pa.getPromjena().add(new BigDecimal(100.))).divide(new BigDecimal(100.), 4, BigDecimal.ROUND_HALF_DOWN);

    // lociraj skladište i zgrabaj vrstu zalihe
    ld.raLocate(dm.getSklad(), "CSKL", this.getMasterSet().getString("CSKL"));
    vrzal = dm.getSklad().getString("VRZAL");
    oldpormar = oldporpor = oldporav = ma.nul;
    raProcess.setMessage("Kalkulacija cijena ...", false);
    // listaj jedan po jedan artikl iz dataseta allArt i provjeri je li mu cijena ve\u0107 promijenjena
    for (allArt.first(); allArt.inBounds(); allArt.next()) {
      cart = String.valueOf(allArt.getInt("CART"));
      if (!artNotUnique(cart)) {
        // ako artikl pripada odabranoj grupi, promijeni mu cijenu
        if (ld.raLocate(dm.getStanje(),new String[]{"cskl","god","cart"},new String[]{
          getMasterSet().getString("CSKL"),getMasterSet().getString("GOD"), cart})) {
          
            rCD.unosKalkulacije(getDetailSet(),dm.getStanje());
            
        ++count;

        this.getDetailSet().insertRow(false);

        // popuni odgovaraju\u0107a polja u tablici stdoku
        fillHeader(crbr == 0);
        if (crbr == 0) crbr = getDetailSet().getShort("RBR");
        else {
          getDetailSet().setShort("RBR", (short) ++crbr);
          getDetailSet().setInt("RBSID", crbr);
        }
        Aut.getAut().copyArtFields(this.getDetailSet(), allArt);
        ld.raLocate(dm.getPorezi(), "CPOR", dm.getArtikli().getString("CPOR"));
        this.getDetailSet().setBigDecimal("SVC",allArt.getBigDecimal("VC"));
        this.getDetailSet().setBigDecimal("SMC",allArt.getBigDecimal("MC"));
        this.getDetailSet().setBigDecimal("VC",allArt.getBigDecimal("VC").multiply(vcmul).setScale(2, BigDecimal.ROUND_HALF_UP));
        calcMPNova();
        calcPoravnanje(false);

        // ubaci promjene u tablicu stdoku
//        this.getDetailSet().saveChanges();

        // ažuriraj stanje artikla
        rut.updateStanje(ma.nul, ma.nul, ma.nul, ma.nul, ma.nul,
            oldpormar, oldporpor, oldporav,
            'N', true, 'N', dm.getStanje(), this.getDetailSet());
        
//        rut.updateStanje(oldpormar, oldporpor, oldporav, 'N', true, 'N');

        this.getDetailSet().post();


        
        }  else {
          System.out.println("stanje nema "+          getDetailSet().getString("CSKL")+"-"+getDetailSet().getString("GOD")+"-"+
          getDetailSet().getInt("CART"));
        }

        // ubaci novi red u tablicu stdoku
      }
    }

    vl.RezSet = null;
    raProcess.setMessage("Spremanje promjene ...", false);
    if (!raTransaction.saveChangesInTransaction(
        new QueryDataSet[] {getDetailSet(), dm.getStanje()})) count = 3;
    else count = count > 0 ? 0 : 1;
    raDetail.getJpTableView().enableEvents(true);
    raDetail.jeprazno();
    return count;
  }

  private void enableProm() {
    rcc.setLabelLaF(jraVPNova, true);
    rcc.setLabelLaF(jraPromjena, true);
    rcc.setLabelLaF(jraMPNova, true);
  }

  public void EntryPointMaster(char mode) {
    if (mode == 'N') {
      this.getPreSelect().copySelValues();
      initNewMaster();

//      this.getMasterSet().setString("CSKL", jpSelectDoc.getJpSelectDoc().jrfCSKL.getText());
//      jlrSkl.setText(jpSelectDoc.getJpSelectDoc().jrfCSKL.getText());
//      jlrSkl.forceFocLost();
    }
    rcc.setLabelLaF(jlrSkl, false);
    rcc.setLabelLaF(jlrSklNaziv, false);
    rcc.setLabelLaF(jbSelSkl, false);
    jpMasterHeader.SetDefTextDOK(mode);
  }
  /*
   * Metoda koju poziva parent (raMasterDetail) nakon prikazivanja DetailPanela
   * master dijela. Postavlja fokus na šifru skladišta i poziva ispis broja
   * dokumenta (osim ako je rije\u010D o novom dokumentu, mode == 'N')
   */
  public void SetFokusMaster(char mode) {
    rcc.setLabelLaF(jlrSkl, false);
    rcc.setLabelLaF(jlrSklNaziv, false);
    rcc.setLabelLaF(jbSelSkl, false);
    if (mode != 'N')
      jpMasterHeader.SetDefTextDOK(mode);
    if (mode != 'B')
      jraDatumDok.requestFocus();
  }

  /*
   * Metoda koju poziva parent (raMasterDetail) prije snimanja podataka sa
   * DetailPanela mastera. Provjerava jesu li popunjena polja sifre skladišta
   * i datuma dokumenta. Vra\u0107a false ako nisu (snimanje se ne\u0107e izvršiti).
   */
  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jlrSkl))
      return false;
    if (vl.isEmpty(jraDatumDok))
      return false;
//    if (mode == 'N') {
//      getBrojDokumenta(this.getMasterSet());
//    }
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
    delstr = rut.getSeqString(ds);
    return hr.restart.robno.Util.getUtil().checkSeq(delstr, String.valueOf(ds.getInt("BRDOK")));
  }

  /*
   * Metoda provjerava može li se obrisati zaglavlje dokumenta
   */
//  public boolean DeleteCheckMaster() {
//    return Aut.getAut().standardDeleteCheckMaster(this);
//  }

  public void beforeShowMaster() {
    this.getMasterSet().open();
    this.getDetailSet().open();
    /*String cskl = "";

    try {
    cskl = ((jpPreselectDoc) this.getPreSelect()).getSelRow().getString("CSKL");
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    System.out.println(cskl);
    if (ld.raLocate(dm.getSklad(), "CSKL", cskl)) {
      System.out.println(dm.getSklad().getString("VRZAL"));
      if (dm.getSklad().getString("VRZAL").equals("M"))
        this.setVisibleColsDetail(new int[] {4,Aut.getAut().getCARTdependable(5,6,7),8,34,26,36,39});
      else this.setVisibleColsDetail(new int[] {4,Aut.getAut().getCARTdependable(5,6,7),8,34,22,35,39});
      this.raDetail.getNavBar().getColBean().initialize();
    }*/
//    refilterDetailSet();
    initRpcart();
  }

  public boolean checkAccess() {

    if (getMasterSet().getString("STATKNJ").equalsIgnoreCase("K")
            || getMasterSet().getString("STATKNJ").equalsIgnoreCase("P")) {
        setUserCheckMsg(
                "Korisnik ne može promijeniti dokument jer je proknjižen !",
                false);
        return false;
    }
    if (isPrenesen()) {
        setUserCheckMsg(
                "Korisnik ne može promijeniti dokument jer je prenesen u ili iz druge baze !",
                false);
        return false;
    }
    if (isKPR()) {
        setUserCheckMsg(
                "Dokument je ušao u knjigu popisa i ne smije se mijenjati !!!",
                false);
        return false;
    }
    if (Aut.getAut().isWrongKnjigYear(this))
        return false;
    restoreUserCheckMessage();
    return true;
  }

  public boolean isPrenesen() {
    return getMasterSet().getString("STATUS").equalsIgnoreCase("P");
  }


  public boolean isKPR() {
    return getMasterSet().getString("STAT_KPR").equalsIgnoreCase("D");
  }
  
  public void refilterDetailSet() {
    super.refilterDetailSet();
    rpc.setGodina(vl.findYear(this.getMasterSet().getTimestamp("DATDOK")));
    rpc.setCskl(this.getMasterSet().getString("CSKL"));
  }

  /*
   * Metoda koja briše i disabla sva polja u donjem dijelu ekrana.
   * Pozvati nakon što rapancart dobije fokus.
   */
  private void eraseFields() {
    rpcLostFocus = false;
//    cijeneUpdated = false;
    /*jraVPStara.setText("");
    jraVPNova.setText("");
    jraMPStara.setText("");
    jraMPNova.setText("");
    jraPromjena.setText(""); */
    jraOznakaPoreza.setText("");
    jraPorez.setText("");
    pr.setBigDecimal("PROMJENA", _Main.nul);
    this.getDetailSet().setBigDecimal("SVC", _Main.nul);
    this.getDetailSet().setBigDecimal("SMC", _Main.nul);
    this.getDetailSet().setBigDecimal("VC", _Main.nul);
    this.getDetailSet().setBigDecimal("MC", _Main.nul);

    rcc.EnabDisabAll(jpDetail, false);
  }

  public void EntryPointDetail(char mode) {
    if (mode == 'N') {
      pr.setBigDecimal("PROMJENA", ma.nul);
      eraseFields();
      rpc.setCART();
    } else if (mode == 'I') {
      rcc.setLabelLaF(jraVPStara, false);
      rcc.setLabelLaF(jraMPStara, false);
      rcc.setLabelLaF(jraOznakaPoreza, false);
      rcc.setLabelLaF(jraPorez, false);
      setPorez();
      calcPromjena();
      oldpormar = this.getDetailSet().getBigDecimal("DIOPORMAR");
      oldporpor = this.getDetailSet().getBigDecimal("DIOPORPOR");
      oldporav = this.getDetailSet().getBigDecimal("PORAV");
      oldsvc = this.getDetailSet().getBigDecimal("SVC");
      oldsmc = this.getDetailSet().getBigDecimal("SMC");
    }
  }

  /*
   * Metoda koju poziva parent (raMasterDetail) nakon prikazivanja DetailPanela
   * detail dijela. Ako je rije\u010D o novoj stavci (mode == 'N') predaje fokus
   * rapancartu i prazni ostala polja na ekranu (i disabla ih).
   * U suprotnom disabla samo informativna polja, te postavlja fokus na
   * polje Promjena cijene.
   */
  public void SetFokusDetail(char mode) {
    if (mode == 'N') {
      pr.setBigDecimal("PROMJENA", ma.nul);
      eraseFields();
      rpc.setCART();
    } else if (mode == 'I') {
      jraPromjena.requestFocus();
    } else {
      setPorez();
      calcPromjena();
    }
  }

  /*
   * Metoda koja provjerava ima li ve\u0107 u dokumentu stavka s istim artiklom.
   */
  private boolean artNotUnique(String art) {
    return lookupData.getlookupData().raLocate(getDetailSet(), "CART", art);
  }

  private boolean artNotUniqueSQL(String art) {
    return stdoki.getDataModule().getRowCount(
        Condition.whereAllEqual(Util.mkey, getMasterSet()).
        and(Condition.equal("CART", art))) > 0;
  }

  boolean updateMC;
  public boolean checkArtCijene() {
    updateMC = false;
    if (frmParam.getParam("robno","dohMcPOS","AR",
      "Dohvat cijene na POS-u (AR,ST)").trim().equalsIgnoreCase("AR")) {
      
      if (ld.raLocate(dm.getArtikli(), "CART", getDetailSet().getInt("CART") + "") &&
          Aus.comp(dm.getArtikli(), "MC", getDetailSet()) != 0) {
        int opt = JOptionPane.showConfirmDialog(raDetail.getWindow(),
                "Cijena s porezom se razlikuje od cijene na artiklu. Ažurirati cijenu na artiklu?",
                "Promjena cijene", JOptionPane.YES_NO_CANCEL_OPTION);
        if (opt == JOptionPane.CANCEL_OPTION) return false;
        if (opt == JOptionPane.YES_OPTION) updateMC = true;
      }
    }

    return true;
  }

  /*
   * Metoda koju poziva parent (raMasterDetail) prije snimanja podataka sa
   * DetailPanela detail dijela. Onemogu\u0107uje snimanje ako nije odabran nikakav
   * artikl u rapancartu. Ako je rije\u010D o novoj stavci, prepisuje klju\u010D i tablice
   * zaglavlja dokumenta i dodaje odgovaraju\u0107i redni broj stavke. Zatim postavlja
   * cijenu zalihe i prora\u010Dunava poravnanje ovisno o vrsti zalihe doti\u010Dnog skladišta
   * (N = prosje\u010Dna nabavna, V = veleprodajna i M = maloprodajna cijena)
   */

  public boolean ValidacijaDetail(char mode) {

    // provjeri je li odabran artikl u rapancart-u
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
    if (mode == 'N' && artNotUniqueSQL(rpc.getCART())) {
      JOptionPane.showMessageDialog(this.jpDetail,"Artikl ve\u0107 promijenjen!","Greška",
        JOptionPane.ERROR_MESSAGE);
      rpc.EnabDisab(true);
      rpc.setCART();
      eraseFields();
      return false;
    }

    if (!rpc.AST.findStanje(this.getMasterSet().getString("GOD"),
         this.getMasterSet().getString("CSKL"),this.getDetailSet().getInt("CART"))) {
      JOptionPane.showMessageDialog(this.jpDetail,"Nema artikla na stanju!","Greška",
        JOptionPane.ERROR_MESSAGE);
      rpc.EnabDisab(true);
      rpc.setCART();
      eraseFields();
      return false;
    }

    if (this.getDetailSet().getBigDecimal("VC").signum() < 0) {
      jraVPNova.requestFocus();
      JOptionPane.showMessageDialog(this.jpDetail,"Cijena ne može biti negativna!","Greška",
        JOptionPane.ERROR_MESSAGE);
      return false;
    }
    // provjeri je li cijena uop\u0107e promijenjena
    if (getDetailSet().getBigDecimal("VC").compareTo(getDetailSet().getBigDecimal("SVC")) == 0) {
      jraPromjena.requestFocus();
      JOptionPane.showMessageDialog(this.jpDetail,"Obavezna promjena cijene!","Greška",
        JOptionPane.ERROR_MESSAGE);
      return false;
    }
    // ako se unosi nova stavka, prepiši klju\u010D iz zaglavlja
    if (mode == 'N')
      fillHeader();

    // zapamti stare vrijednosti poravnanja
    if (mode == 'N') {
      oldpormar = oldporpor = oldporav = ma.nul;
    } else {
      oldpormar = this.getDetailSet().getBigDecimal("DIOPORMAR");
      oldporpor = this.getDetailSet().getBigDecimal("DIOPORPOR");
      oldporav = this.getDetailSet().getBigDecimal("PORAV");
    }
    // lociraj skladište i zgrabaj vrstu zalihe
    ld.raLocate(dm.getSklad(), "CSKL", this.getMasterSet().getString("CSKL"));
    vrzal = dm.getSklad().getString("VRZAL");

    // zapamti kljuc za stanje
    oldCart = this.getDetailSet().getInt("CART");
    oldCskl = this.getDetailSet().getString("CSKL");
    oldGod = this.getDetailSet().getString("GOD");

//    System.out.println(oldCart + " " + oldCskl + " " + oldGod);
    // izra\u010Dunaj poravnanja
    calcPoravnanje(mode == 'I');

    findStanje();
    if (mode=='N') {
      if (stanjeSet.getRowCount()==0) return true;

// neistestirano

      if (!rCD.isDataKalkulforKalkulOK(getMasterSet().getTimestamp("DATDOK"),stanjeSet.getString("TKAL"))) {
        JOptionPane.showConfirmDialog(null,"Datum nivelacije je manji nego prethodne kalkulacije za ovaj artikl !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        return false;
      }

      if (!rCD.isDateIzlazOK(getMasterSet().getTimestamp("DATDOK"),stanjeSet.getString("TKAL"))) {
        JOptionPane.showConfirmDialog(null,"Datum je manji nego zadnji izlazni dokument za ovaj artikl !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    if (mode=='I') {
      if (!isUpdateOrDeletePossible()) {
        JOptionPane.showConfirmDialog(null,"Ispravak nije dozvoljen. Postoji promet koji je napravljen po ovom dokumentu !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    if (!checkArtCijene()) return false;
    //dm.getStanje().refresh();

    return true;
  }
  private void fillHeader() {
    fillHeader(true);
  }

  private void fillHeader(boolean setrbr) {
    this.getDetailSet().setString("CSKL", this.getMasterSet().getString("CSKL"));
    this.getDetailSet().setString("VRDOK", this.getMasterSet().getString("VRDOK"));
    this.getDetailSet().setString("GOD", this.getMasterSet().getString("GOD"));
    this.getDetailSet().setInt("BRDOK", this.getMasterSet().getInt("BRDOK"));

    // traži sljede\u0107i redni broj ovog dokumenta
    if (setrbr) {
      this.getDetailSet().setShort("RBR", rbr.vrati_rbr("STDOKU",this.getMasterSet().getString("CSKL"),
        this.getMasterSet().getString("VRDOK"),this.getMasterSet().getString("GOD"),
        this.getMasterSet().getInt("BRDOK")));
      getDetailSet().setInt("RBSID", getDetailSet().getShort("RBR"));
    }
  }

  /*
   * Metoda koja ra\u010Duna poravnanje ovisno o vrsti zalihe.
   */
  private void calcPoravnanje(boolean edit) {
    // zgrabaj najsvježije stanje artikla iz tablice Stanje
    if (!edit)
      rpc.AST.findStanje(this.getMasterSet().getString("GOD"),this.getMasterSet().getString("CSKL"),this.getDetailSet().getInt("CART"));
    BigDecimal kol = edit ? getDetailSet().getBigDecimal("SKOL") :
                     rpc.gettrenStanje().getBigDecimal("KOL");
    BigDecimal nc = rpc.gettrenStanje().getBigDecimal("NC");
    BigDecimal vc = rpc.gettrenStanje().getBigDecimal("VC");
    BigDecimal mc = rpc.gettrenStanje().getBigDecimal("MC");
//    System.out.println("kol " + kol + " cijene " + nc + "  " + vc + "  " + mc);
    this.getDetailSet().setBigDecimal("SKOL", kol);
    if (vrzal.equalsIgnoreCase("N")) {
      this.getDetailSet().setBigDecimal("ZC",nc);
    } else if (vrzal.equalsIgnoreCase("V")) {
      this.getDetailSet().setBigDecimal("ZC",this.getDetailSet().getBigDecimal("VC"));
      BigDecimal por = util.sumValue(oldporav, kol.multiply(this.getDetailSet().getBigDecimal("VC").subtract(vc)));
      this.getDetailSet().setBigDecimal("DIOPORMAR", por.setScale(2, BigDecimal.ROUND_HALF_UP));
      this.getDetailSet().setBigDecimal("PORAV", por.setScale(2, BigDecimal.ROUND_HALF_UP));
    } else if (vrzal.equalsIgnoreCase("M")) {
      System.out.println("MC "+this.getDetailSet().getBigDecimal("MC"));
      System.out.println("SMC "+this.getDetailSet().getBigDecimal("SMC"));
      System.out.println("stanje MC "+mc);
      this.getDetailSet().setBigDecimal("ZC",this.getDetailSet().getBigDecimal("MC"));
      BigDecimal ukupor = util.sumValue(oldporav, kol.multiply(this.getDetailSet().getBigDecimal("MC").subtract(mc)));
      BigDecimal marpor = ukupor.multiply(new BigDecimal(100. / (100. + pr.getBigDecimal("UKUPOR").doubleValue())));
      this.getDetailSet().setBigDecimal("PORAV", ukupor.setScale(2, BigDecimal.ROUND_HALF_UP));
//      this.getDetailSet().setBigDecimal("IZAD", this.getDetailSet().setBigDecimal("PORAV"));
      this.getDetailSet().setBigDecimal("DIOPORMAR", marpor.setScale(2, BigDecimal.ROUND_HALF_UP));
      this.getDetailSet().setBigDecimal("DIOPORPOR", ukupor.subtract(marpor).setScale(2, BigDecimal.ROUND_HALF_UP));
    }
  }

  /*
   * Metoda koja se poziva prije brisanja detail sloga. Treba vratiti false ako brisanje
   * nije dopušteno, ali za sada ne radi ništa sli\u010Dno. Pamti trenuta\u010Dne vrijednosti
   * poravnanja.
   */
  public boolean DeleteCheckDetail() {
    rCD.prepareFields(getDetailSet());
//    isFind=findSTANJE();
    findStanje();
    if (!isUpdateOrDeletePossible()) {
      JOptionPane.showConfirmDialog(null,"Brisanje nije dozvoljeno. Postoji promet koji je napravljen po ovom dokumentu !","Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }

    oldpormar = this.getDetailSet().getBigDecimal("DIOPORMAR");
    oldporpor = this.getDetailSet().getBigDecimal("DIOPORPOR");
    oldporav = this.getDetailSet().getBigDecimal("PORAV");
    oldsvc = this.getDetailSet().getBigDecimal("SVC");
    oldsmc = this.getDetailSet().getBigDecimal("SMC");
    oldRbr = this.getDetailSet().getShort("RBR");
    oldCart = this.getDetailSet().getInt("CART");
    oldCskl = this.getDetailSet().getString("CSKL");
    oldGod = this.getDetailSet().getString("GOD");
    return true;
  }

  public boolean doWithSaveDetail(char mode) {
    try {
      QueryDataSet stanje = Stanje.getDataModule().getTempSet(
          Condition.equal("CSKL", oldCskl).and(Condition.equal("GOD", oldGod)).
          and(Condition.equal("CART", oldCart)));
      stanje.open();
      boolean upd = stanje.rowCount() > 0;
      
      if (mode != 'B' && updateMC) try {
        if (ld.raLocate(dm.getArtikli(), "CART", getDetailSet().getInt("CART") + "")) {
          if (updateMC) Aus.set(dm.getArtikli(), "MC", getDetailSet());
          if (updateMC) Aus.set(dm.getArtikli(), "VC", getDetailSet());
          hr.restart.util.raTransaction.saveChanges(dm.getArtikli());
        }
      } finally {
        updateMC = false;
      }
      
      /*boolean upd = ld.raLocate(dm.getStanje(), new String[] {"CSKL", "GOD", "CART"},
                                new String[] {oldCskl, oldGod, String.valueOf(oldCart)});*/

      if (mode == 'B') {
        if (upd) {
          System.out.println(oldpormar);
          System.out.println(oldporpor);
          System.out.println(oldporav);
          this.getDetailSet().insertRow(true);
          this.getDetailSet().setInt("CART", oldCart);
          this.getDetailSet().setBigDecimal("SVC", oldsvc);
          this.getDetailSet().setBigDecimal("SMC", oldsmc);
          rut.updateStanje(ma.nul, ma.nul, ma.nul, ma.nul, ma.nul,
                           oldpormar, oldporpor, oldporav,
                           'B', true, 'N', /*dm.getStanje()*/ stanje, this.getDetailSet());
          this.getDetailSet().cancel();
          raTransaction.saveChanges(/*dm.getStanje()*/ stanje);
          stanjeSet.refresh();
          rCD.brisanjeKalkulacije(stanjeSet);
          raTransaction.saveChanges(stanjeSet);
        }
        vl.recountDataSet(raDetail, "RBR", oldRbr, false);
        raTransaction.saveChanges(getDetailSet());
      } else if (upd) {
        rut.updateStanje(ma.nul, ma.nul, ma.nul, ma.nul, ma.nul,
                 oldpormar, oldporpor, oldporav,
                 'N', true, 'N', /*dm.getStanje()*/ stanje, this.getDetailSet());
        raTransaction.saveChanges(/*dm.getStanje()*/ stanje);
        if (mode=='N'){
          findStanje();
          rCD.unosKalkulacije(getDetailSet(),stanjeSet);
//ST.prn(stanjeSet);
          hr.restart.util.raTransaction.saveChanges(getDetailSet());
          raTransaction.saveChanges(stanjeSet);

        }
        stanjeSet.setTimestamp("DATZK", 
            getMasterSet().getTimestamp("DATDOK"));
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /*
   * Metoda koja se poziva nakon brisanja detail sloga (stavka dokumenta).
   * Azurira tablicu stanje, koriste\u0107i ranije zapam\u0107ene vrijednosti poravnanja.
   * Tako\u0111er, sre\u0111uje redne brojeve stavki iza ove obrisane.
   */
//  public void AfterDeleteDetail() {
//    this.getDetailSet().insertRow(true);
//    System.out.println("updatiram stanje, check: oldpormar = " + oldpormar +
//      " oldporpor = " + oldporpor + " oldporav = " + oldporav);
//    System.out.println("novo stanje: diopormar = "+this.getDetailSet().getBigDecimal("DIOPORMAR") +
//      " dioporpor = " + this.getDetailSet().getBigDecimal("DIOPORPOR") +
//      " porav = " +this.getDetailSet().getBigDecimal("PORAV"));
//    this.getDetailSet().setInt("CART", oldCart);
//    rut.updateStanje(oldpormar, oldporpor, oldporav, 'B', true, 'N');
//    this.getDetailSet().cancel();
//    Aut.getAut().recalculateRbr(this, "RBR", oldRbr);
//  }

  /*
   * Metoda koja se poziva nakon snimanja detail sloga (stavka dokumenta)
   * Azurira tablicu Stanje.
   */
  public void AfterSaveDetail(char mode) {
//    System.out.println("updatiram stanje, check: oldpormar = " + oldpormar +
//      " oldporpor = " + oldporpor + " oldporav = " + oldporav);
//    System.out.println("novo stanje: diopormar = "+this.getDetailSet().getBigDecimal("DIOPORMAR") +
//      " dioporpor = " + this.getDetailSet().getBigDecimal("DIOPORPOR") +
//      " porav = " +this.getDetailSet().getBigDecimal("PORAV"));
//    rut.updateStanje(oldpormar, oldporpor, oldporav, 'N', true, 'N');

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

  /*
   * Metoda koja se poziva nakon što rapancart izgubi fokus (jedino prilikom unošenja
   * nove stavke). Popunjava odgovaraju\u0107a polja (stara veleprodajna cijena i stara
   * maloprodajna cijena) i omogu\u0107uje zadavanje novih cijena.
   */
  private void updateCijene() {
    // provjeri ima li artikla na stanju
    if (!rpc.AST.findStanje(this.getMasterSet().getString("GOD"),
         this.getMasterSet().getString("CSKL"),this.getDetailSet().getInt("CART"))) {
      /*JOptionPane.showMessageDialog(this.jpDetail,"Nema artikla na stanju!","Greška",
        JOptionPane.ERROR_MESSAGE);

      rpc.EnabDisab(true);
      rpc.setCART(); */
      eraseFields();
      Aut.getAut().handleRpcErr(rpc, "Nema artikla na stanju!");
      return;
    }
    // provjeri nalazi li ve\u0107 u dokumentu isti artikl
    if (artNotUniqueSQL(rpc.getCART())) {
      /*JOptionPane.showMessageDialog(this.jpDetail,"Artikl ve\u0107 promijenjen!","Greška",
        JOptionPane.ERROR_MESSAGE);
      rpc.EnabDisab(true);
      rpc.setCART(); */
      eraseFields();
      Aut.getAut().handleRpcErr(rpc, "Artikl ve\u0107 promijenjen!");
      return;
    }
    vl.RezSet = null;
    enableProm();

    updateStanje();
    this.getDetailSet().setBigDecimal("VC", this.getDetailSet().getBigDecimal("SVC"));
    this.getDetailSet().setBigDecimal("MC", this.getDetailSet().getBigDecimal("SMC"));
    calcPromjena();
    setPorez();
    jraPromjena.requestFocus();
  }

  /*
   * Metoda za punjenje podataka u polja oznaka i iznos poreza.
   */
  private void setPorez() {
    ld.raLocate(dm.getArtikli(), "CART", rpc.getCART());
    jraOznakaPoreza.setText(dm.getArtikli().getString("CPOR"));
    ld.raLocate(dm.getPorezi(), "CPOR", dm.getArtikli().getString("CPOR"));
    pr.setBigDecimal("UKUPOR",dm.getPorezi().getBigDecimal("UKUPOR"));
  }

  /*
   * Metoda ra\u010Duna postotnu promjenu cijene iz odnosa nove i stare veleprodajne cijene.
   */
  private void calcPromjena() {
    double diff;
    if (this.getDetailSet().getBigDecimal("SVC").compareTo(new BigDecimal(0.011)) <= 0)
      diff = 0;
    else
      diff = this.getDetailSet().getBigDecimal("VC").doubleValue() /
             this.getDetailSet().getBigDecimal("SVC").doubleValue() * 100 - 100;

    /*if (diff == 0.0) {
      jraPromjena.setText("");
    } else {*/
      pr.setBigDecimal("PROMJENA", new BigDecimal(diff).setScale(2, BigDecimal.ROUND_HALF_UP));
    //}
  }

  /*
   * Metoda ra\u010Duna novu veleprodajnu cijenu iz nove maloprodajne i stope poreza.
   */
  private void calcVPNova() {
    double vc = this.getDetailSet().getBigDecimal("MC").doubleValue() * 100 / (100 +
                dm.getPorezi().getBigDecimal("UKUPOR").doubleValue());
    this.getDetailSet().setBigDecimal("VC", new BigDecimal(vc).setScale(2, BigDecimal.ROUND_HALF_UP));
  }

  /*
   * Metoda ra\u010Duna novu maloprodajnu cijenu iz nove veleprodajne i stope poreza.
   */
  private void calcMPNova() {
    double mc = this.getDetailSet().getBigDecimal("VC").doubleValue() * (100 +
                dm.getPorezi().getBigDecimal("UKUPOR").doubleValue()) / 100;
    this.getDetailSet().setBigDecimal("MC", new BigDecimal(mc).setScale(2, BigDecimal.ROUND_HALF_UP));
  }

  /*
   * Sljede\u0107e tri metode se pozivaju na focus_lost odgovaraju\u0107eg polja i
   * ra\u010Dunaju vrijednosti za ostala polja na DetailPanelu detail dijela.
   */
  private void VPNovaUpdated() {
    updateStanje();
    calcPromjena();
    calcMPNova();
  }

  private void PromjenaUpdated() {
    updateStanje();
    if (jraPromjena.getText().equals("")) {
      this.getDetailSet().setBigDecimal("VC",this.getDetailSet().getBigDecimal("SVC"));
    } else {
      double diff = pr.getBigDecimal("PROMJENA").doubleValue();
      double oldvc = this.getDetailSet().getBigDecimal("SVC").doubleValue();
      this.getDetailSet().setBigDecimal("VC",new BigDecimal(oldvc * (diff + 100) / 100).setScale(2, BigDecimal.ROUND_HALF_UP));
    }
    calcMPNova();
  }
  private void MPNovaUpdated() {
    updateStanje();
    calcVPNova();
    calcPromjena();

  }

  /**
   * Metoda dohva\u0107a najsvježije stanje veleprodajne i maloprodajne cijene artikla.
   */
  private void updateStanje() {
    if (raDetail.getMode() == 'N') {
      this.getDetailSet().setBigDecimal("SVC",rpc.gettrenStanje().getBigDecimal("VC"));
      this.getDetailSet().setBigDecimal("SMC",rpc.gettrenStanje().getBigDecimal("MC"));
    }
  }


  public void findStanje(){
    allStanje.getallStanje().findStanjeUnconditional(getDetailSet().getString("GOD"),
        getDetailSet().getString("CSKL"), getDetailSet().getInt("CART"));
    stanjeSet = allStanje.getallStanje().gettrenSTANJE();
  }

  public boolean isUpdateOrDeletePossible(){
    stanjeSet.refresh();
    return rCD.testKalkulacije(getDetailSet(),stanjeSet);
  }


}
