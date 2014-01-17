/*******************************************************************************
 * license** * file: presBlag.java * Copyright 2006 Rest Art * * Licensed under
 * the Apache License, Version 2.0 (the "License"); * you may not use this file
 * except in compliance with the License. * You may obtain a copy of the License
 * at * * http://www.apache.org/licenses/LICENSE-2.0 * * Unless required by
 * applicable law or agreed to in writing, software * distributed under the
 * License is distributed on an "AS IS" BASIS, * WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. * See the License for the
 * specific language governing permissions and * limitations under the License. *
 ******************************************************************************/
package hr.restart.pos;

import java.util.HashMap;

import hr.restart.baza.Condition;
import hr.restart.baza.Orgstruktura;
import hr.restart.baza.Sklad;
import hr.restart.baza.Smjene;
import hr.restart.baza.dM;
import hr.restart.robno.TypeDoc;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.FisUtil;
import hr.restart.util.JlrNavField;
import hr.restart.util.PreSelect;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.zapod.OrgStr;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class presBlag extends PreSelect {

  public static presBlag presblag;
  public static String blagajnik;
  public static String stol;
  public static boolean stolovi = false;
  
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();
  static boolean isSuper = false;
  JPanel jPanel1 = new JPanel();
  JlrNavField jrfNAZSKL = new JlrNavField();
  XYLayout xYLayout1 = new XYLayout();
  JPanel jp = new JPanel();
  JlrNavField jrfCSKL = new JlrNavField() {

    public void after_lookUp() {
      if (!isSkladOriented())
        setProd_mjLookup();
    }
  };
  JLabel jLabel4 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel1 = new JLabel();
  JraButton jbCSKL = new JraButton();
  JlrNavField jrfCPRODMJ = new JlrNavField();
  JlrNavField jrfNAZPRODMJ = new JlrNavField();
  JraButton jbCPRODMJ = new JraButton();

  JraButton jbCBLAGAJNIK = new JraButton();
  JlrNavField jrfCBLAGAJNIK = new JlrNavField();
  JlrNavField jrfNAZBLAGAJNIK = new JlrNavField();

  JLabel jlStol = new JLabel();
  JraTextField jraStol = new JraTextField();
  JraTextField jraVRDOK = new JraTextField();

  JLabel jlDatum = new JLabel();
  JraTextField jraDatumfrom = new JraTextField();
  JraTextField jraDatumto = new JraTextField();
  JPasswordField jpswd = new JPasswordField() {

    public void addNotify() {
      super.addNotify();
      Aus.installEnterRelease(this);
    }
  };
  JraCheckBox jcbAktiv = new JraCheckBox();
  
  raComboBox rcbSmjena = new raComboBox() {
    public void this_itemStateChanged() {}
  };

  public presBlag() {
    try {
      jbInit();
      dm.getPos().open();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static PreSelect getPres() {
    if (presblag == null) {
      presblag = new presBlag();

    }
    return presblag;
  }

  public void resetDefaults() {
    jpswd.setText("");
    dm.getSklad().open();
    jcbAktiv.setSelected(true);
    getSelRow().setTimestamp("DATDOK-from", vl.getToday());
    getSelRow().setTimestamp("DATDOK-to", vl.getToday());
    if (!isSkladOriented())
      getSelRow().setString("CSKL", raUser.getInstance().getDefSklad());
  }

  boolean firstTime = true;

  public void SetFokus() {
    rcc.setLabelLaF(this.getSelPanel(), true);

    if (firstTime) {
      firstTime = false;
      resetDefaults();
    }
    if (!getPRODMJ().trim().equals("")) {
      jrfCPRODMJ.getDataSet().setString("CPRODMJ", getPRODMJ());
      jrfCPRODMJ.forceFocLost();
    }
    /*
     * QueryDataSet qds = hr.restart.robno.Util.getMPSklDataset(); qds.open();
     * if (qds.rowCount()==0) { JOptionPane.showConfirmDialog(null,"Nema
     * definiranog maloprodajnog skladišta !",
     * "Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
     * this.cancelSelect(); }
     */
    // jrfCSKL.setText(qds.getString("CSKL"));
    // jrfNAZSKL.setText(qds.getString("NAZSKL"));
    jrfCSKL.forceFocLost();
    System.out.println("Nasao skladiste: " + dm.getSklad().getString("CSKL"));
    if (dm.getSklad().rowCount() == 1) {
      rcc.setLabelLaF(jrfCSKL, false);
      rcc.setLabelLaF(jrfNAZSKL, false);
      rcc.setLabelLaF(jbCSKL, false);
    }
    if (jrfCSKL.getText().length() == 0 || isSkladOriented()) {
      jrfCSKL.requestFocusLater();
    } else if (stolovi && jrfCPRODMJ.getText().length() > 0) {
      jraStol.requestFocusLater();
    } else {
      jrfCPRODMJ.requestFocusLater();
    }
    if (stolovi) jraVRDOK.getDataSet().setString("VRDOK", getVRDOK());
  }

  public boolean Validacija() {
    if (vl.isEmpty(jrfCSKL)) return false;
    if (vl.isEmpty(jrfCPRODMJ)) return false;
    // if (stolovi && vl.isEmpty(jraStol)) return false;
    if (isUserOriented()) {
      blagajnik = raUser.getInstance().getUser();
      isSuper = raUser.getInstance().isSuper();
    } else {
      dm.getBlagajnici().open();
      if (!lookupData.getlookupData().raLocate(dm.getBlagajnici(), "LOZINKA",
          String.valueOf(jpswd.getPassword()))) {
        JOptionPane.showMessageDialog(null, "Pogrešna lozinka!", "Greška",
            JOptionPane.ERROR_MESSAGE);
        return false;
      } else {
        blagajnik = dm.getBlagajnici().getString("CBLAGAJNIK");
        if (dm.getBlagajnici().getString("SUPER").equalsIgnoreCase("D")) {
          isSuper = true;
        } else {
          isSuper = false;
        }
      }
    }
    if (stolovi) stol = jraStol.getText();
    /*
     * String str="SELECT * FROM BLAGAJNICI WHERE
     * LOZINKA='"+String.valueOf(jpswd.getPassword())+"'"; QueryDataSet qds =
     * hr.restart.util.Util.getNewQueryDataSet(str); System.out.println("sql:
     * "+str); qds.open(); if (qds.rowCount()==0) {
     * JOptionPane.showMessageDialog(null, "Pogrešna lozinka!", "Greška",
     * JOptionPane.ERROR_MESSAGE); return false; } else {
     * 
     * blagajnik=qds.getString("CBLAGAJNIK"); if
     * (qds.getString("SUPER").equalsIgnoreCase("D")) {
     * System.out.println("Supeeeeeeeeeeeeeeeer"); isSuper=true; } else {
     * System.out.println("Nijeeeeeeeeeee Supeeeeeeeeeeeeeeeer"); isSuper=false; } }
     */
    // if (vl.isEmpty(jrfCBLAGAJNIK))
    // return false;
    if (frmMasterBlagajna.allReadyRun == true) {
      JOptionPane.showConfirmDialog(null, "Blagajna veæ pokrenuta !", "Greška",
          JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
      this.cancelSelect();
      return false;
    }
    return Aus.checkDateRange(jraDatumfrom, jraDatumto);
  }

  void jbInit() throws Exception {
    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(
        new hr.restart.zapod.raKnjigChangeListener() {

          public void knjigChanged(String oldKnjig, String newKnjig) {
            jrfCSKL.setRaDataSet(dm.getSklad());
          }
        });

    stolovi = frmParam.getParam("pos", "posStolovi", "N",
        "Izbor stola na predselekciji blagajne (D,N)").equalsIgnoreCase("D");

    jp.setLayout(xYLayout1);
    xYLayout1.setHeight(145);
    xYLayout1.setWidth(555);
    
    // qdsSklad.setQuery(new
    // com.borland.dx.sql.dataset.QueryDescriptor(dm.getDatabase1(), "select *
    // from sklad where vrzal=\'M\'", null, true, Load.ALL));
    jrfCSKL.setColumnName("CSKL");
    jrfCSKL.setColNames(new String[] {
      "NAZSKL"
    });
    jrfCSKL.setVisCols(new int[] {
        0, 1
    });
    jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] {
      jrfNAZSKL
    });
    jrfCSKL.setNavButton(jbCSKL);
    // jrfCSKL.setRaDataSet(qdsSklad);
    bindSklad();
    
    
    jrfNAZSKL.setNavProperties(jrfCSKL);
    jrfNAZSKL.setSearchMode(1);
    jrfNAZSKL.setColumnName("NAZSKL");
    jLabel4.setText("Smjena");
    jLabel3.setText("Zaporka");
    jLabel2.setText("Blagajna");
    jLabel1.setText(isSkladOriented() ? "Skladište" : "Prodajno mjesto");
    jrfCPRODMJ.setRaDataSet(dm.getProd_mj());
    jrfCPRODMJ.setTextFields(new javax.swing.text.JTextComponent[] {
      jrfNAZPRODMJ
    });
    jrfCPRODMJ.setVisCols(new int[] {
        0, 1
    });
    jrfCPRODMJ.setColNames(new String[] {
      "NAZPRODMJ"
    });
    jrfCPRODMJ.setColumnName("CPRODMJ");
    jrfCPRODMJ.setNavButton(jbCPRODMJ);
    jrfNAZPRODMJ.setColumnName("NAZPRODMJ");
    jrfNAZPRODMJ.setSearchMode(1);
    jrfNAZPRODMJ.setNavProperties(jrfCPRODMJ);

    jrfCBLAGAJNIK.setRaDataSet(dm.getBlagajnici());
    jrfCBLAGAJNIK.setTextFields(new javax.swing.text.JTextComponent[] {
      jrfNAZBLAGAJNIK
    });
    jrfCBLAGAJNIK.setVisCols(new int[] {
        0, 1
    });
    jrfCBLAGAJNIK.setColNames(new String[] {
      "NAZBLAG"
    });
    jrfCBLAGAJNIK.setColumnName("CBLAGAJNIK");
    jrfCBLAGAJNIK.setNavButton(jbCBLAGAJNIK);
    jrfNAZBLAGAJNIK.setColumnName("NAZBLAG");
    jrfNAZBLAGAJNIK.setSearchMode(1);
    jrfNAZBLAGAJNIK.setNavProperties(jrfCBLAGAJNIK);

    // jrfNAZPRODMJ.setEditable(false);
    jlDatum.setText("Datum (od - do)");
    jraDatumfrom.setColumnName("DATDOK");
    jraDatumto.setColumnName("DATDOK");
    
    DataSet sm = Smjene.getDataModule().getTempSet(Condition.equal("AKTIV", "D"));
    sm.open();
    sm.first();
    Variant v = new Variant();
    String[][] itm = new String[sm.rowCount() + 1][2];
    for (int i = 1; sm.inBounds(); sm.next(), i++) {
      sm.getVariant("NAZIV", v);
      itm[i][0] = v.toString();
      sm.getVariant("CSMJENA", v);
      itm[i][1] = v.toString();
    }
    itm[0][0] = "Konobar";
    itm[0][1] = "0";
    rcbSmjena.setRaItems(itm);

    if (stolovi) {
      jlStol.setText("Stol");
      jraStol.setColumnName("STOL");
      jraVRDOK.setColumnName("VRDOK");
      jp.add(jlStol, new XYConstraints(375, 70, -1, -1));
      jp.add(jraStol, new XYConstraints(415, 70, 100, 21));
      
    }
    //if (isFiskal()) {
      jcbAktiv.setText(" Samo raèuni ");
      jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
      jcbAktiv.setHorizontalAlignment(SwingConstants.TRAILING);
      jp.add(jcbAktiv, new XYConstraints(350, 120, 165, -1));
    //}
    
    int yc = 20;
    int yp = 45;
    
    if (isSkladOriented()) {
      yc = 45;
      yp = 20;
    }

    jp.add(jPanel1, new XYConstraints(0, 0, -1, -1));
    jp.add(jrfNAZSKL, new XYConstraints(260, yc, 255, -1));
    jp.add(jrfCSKL, new XYConstraints(150, yc, 100, -1));
    jp.add(jbCSKL, new XYConstraints(519, yc, 21, 21));
    jp.add(jLabel1, new XYConstraints(15, yc, -1, -1));
    jp.add(jLabel2, new XYConstraints(15, yp, -1, -1));
    jp.add(jrfCPRODMJ, new XYConstraints(150, yp, 100, -1));
    jp.add(jrfNAZPRODMJ, new XYConstraints(260, yp, 255, -1));
    jp.add(jbCPRODMJ, new XYConstraints(519, yp, 21, 21));

    jp.add(jraVRDOK, new XYConstraints(0, 0, 0, 0));
    jraVRDOK.setVisible(false);
    // jp.add(jrfCBLAGAJNIK, new XYConstraints(-1, -1, 100, -1));
    // jp.add(jrfNAZBLAGAJNIK, new XYConstraints(260, 70, 255, -1));
    // jp.add(jbCBLAGAJNIK, new XYConstraints(519, 70, 21, 21));

    jp.add(jlDatum, new XYConstraints(15, 95, -1, -1));
    jp.add(jraDatumfrom, new XYConstraints(150, 95, 100, -1));
    jp.add(jraDatumto, new XYConstraints(260, 95, 100, -1));
    if (isSmjena()) {
      jp.add(rcbSmjena, new XYConstraints(150, 120, 100, 21));
      jp.add(jLabel4, new XYConstraints(15, 120, -1, -1));
    }

    this.setSelDataSet(dm.getPos());
    this.addSelRange(jraDatumfrom, jraDatumto);
    this.setSelPanel(jp);
    installResetButton();
    if (!isUserOriented()) {
      jp.add(jLabel3, new XYConstraints(15, 70, -1, -1));
      jp.add(jpswd, new XYConstraints(150, 70, 100, -1));
    }
  }

  public String refineSQLFilter(String orig) {
    if (!jcbAktiv.isSelected() || !isFiskal("GRC", getSelRow().getString("CSKL"))) return orig;
    return orig + " AND pos.fok='D'";
  }
  
  void bindSklad() {
    String corg = frmParam.getParam("pos", "posCorg", OrgStr.getKNJCORG(false),
      "OJ za logotip na POS-u");
    if (corg == null || corg.length() == 0)
      corg = OrgStr.getKNJCORG(false);

    jrfCSKL.setRaDataSet(Sklad.getDataModule().getFilteredDataSet(
        Condition.in("CORG", OrgStr.getOrgStr().getOrgstrAndKnjig(corg))));
  }
  
  static DataSet myskl = Sklad.getDataModule().copyDataSet();
  static DataSet myorg = Orgstruktura.getDataModule().copyDataSet();
  
  public static DataSet findOJ(String vrdok, String cskl) {
    boolean sklad = TypeDoc.getTypeDoc().isCsklSklad(vrdok) || vrdok.equalsIgnoreCase("PRD") || 
          (isSkladOriented() && vrdok.equalsIgnoreCase("GRC"));
    String corg = cskl;
    if (sklad && lookupData.getlookupData().raLocate(myskl, "CSKL", cskl)) 
        corg = myskl.getString("CORG");
    
    lookupData.getlookupData().raLocate(myorg, "CORG", corg);
    while (myorg.getString("FISK").equalsIgnoreCase("X") && 
        !myorg.getString("PRIPADNOST").equals(myorg.getString("CORG")))
      lookupData.getlookupData().raLocate(myorg, "CORG", corg = myorg.getString("PRIPADNOST"));
      
    if (myorg.getString("FISK").equals("D") && myorg.getString("CCERT").trim().length() == 0) {
      DataRow dr = lookupData.getlookupData().raLookup(myorg, "CORG", myorg.getString("PRIPADNOST"));
      while (dr.getString("CCERT").trim().length() == 0 && !dr.getString("CORG").equals(dr.getString("PRIPADNOST")))
        dr = lookupData.getlookupData().raLookup(myorg, "CORG", dr.getString("PRIPADNOST"));
      
      myorg.setString("CCERT", dr.getString("CCERT"));
      myorg.setString("FPATH", dr.getString("FPATH"));
      myorg.setString("FKEY", dr.getString("FKEY"));
    }
    
    System.out.println(myorg);
    return myorg;
  }
  
  public static DataSet findOJ(DataSet ms) {
    return findOJ(ms.getString("VRDOK"), ms.getString("CSKL"));
  }
  
  private static int skladOriented = -1;

  public static boolean isSkladOriented() {
    if (skladOriented < 0) {
      skladOriented = frmParam.getParam("pos", "skladPos", "N",
          "Da li se POS vodi na nivou skladišta (D) ili prodajnog mjesta")
          .equalsIgnoreCase("D") ? 1 : 0;
    }
    return skladOriented == 1;
  }

  private static int userOriented = -1;

  public static boolean isUserOriented() {
    if (userOriented < 0) {
      userOriented = frmParam.getParam("pos", "posUser", "N",
          "Da li se POS vodi na nivou usera (D) ili blagajnika")
          .equalsIgnoreCase("D") ? 1 : 0;
    }
    return userOriented == 1;
  }
  
  private static int smjena = -1;
  
  
  public static boolean isFiskal(DataSet ms) {
    return isFiskal(ms.getString("VRDOK"), ms.getString("CSKL"));
  }
  
  public static boolean isFiskal(String vrdok, String cskl) {
    findOJ(vrdok, cskl);
    return myorg.getString("FISK").equalsIgnoreCase("D");
  }
  
  
  /*public static boolean isFiskal(String cskl) {
    if (isSkladOriented()) {
      return frmParam.getParam("robno", "fiskalizacija"+cskl, "N",
        "Radi li se fiskalizacija "+cskl+" (D,N)").equalsIgnoreCase("D");
    }
    return isFiskal();
  }
  
  private static int fiskal = -1;
  
  public static boolean isFiskal() {
    if (fiskal < 0) {
      fiskal = frmParam.getParam("robno", "fiskalizacija", "N",
          "Radi li se fiskalizacija (D,N)")
          .equalsIgnoreCase("D") ? 1 : 0;
    }
    return fiskal == 1;
  }
  
  private static int fiskpdv = -1;
  
  public static boolean isFiskPDV() {
    if (fiskpdv < 0) {
      fiskpdv = frmParam.getParam("robno", "fiskPDV", "D",
          "Je li korisnik u sustavu PDV-a (D,N)")
          .equalsIgnoreCase("D") ? 1 : 0;
    }
    return fiskpdv == 1;
  }
  */
  
  
  public static boolean isFiskPDV(DataSet ms) {
    findOJ(ms);
    return myorg.getString("FPDV").equalsIgnoreCase("D");
  }
  
  
  public static boolean isFiskSep(DataSet ms) {
    findOJ(ms);
    return !myorg.getString("FPOJED").equalsIgnoreCase("D");
  }
  
  public static boolean isFiskGot(DataSet ms) {
    findOJ(ms);
    return myorg.getString("FPOJED").equalsIgnoreCase("G");
  }
  
  public static String getFiskPP(DataSet ms) {
    findOJ(ms);
    return myorg.getString("FPP");
  }
  
  public static int getFiskNap(DataSet ms) {
    findOJ(ms);
    if (ms.getString("VRDOK").equals("IZD")) {
      String io = frmParam.getParam("robno", "fiskNapI-"+myorg.getString("FPP"), "", 
          "Oznaka naplatnog ureðaja za izdatnice PP " + myorg.getString("FPP") + " (lokalno)", true);
      if (io != null && io.length() > 0) Aus.getNumber(io);
    }
    String ur = frmParam.getParam("robno", "fiskNap-"+myorg.getString("FPP"), "1", 
        "Oznaka naplatnog ureðaja PP " + myorg.getString("FPP") + " (lokalno)", true);
    if (ur == null || ur.length() == 0) return 1;
    return Aus.getNumber(ur);
  }
  
  public static int getFiskNapG(DataSet ms) {
    findOJ(ms);
    String ur = frmParam.getParam("robno", "fiskNapG-"+myorg.getString("FPP"), "2", 
        "Oznaka naplatnog ureðaja za gotovinu PP " + myorg.getString("FPP") + " (lokalno)", true);
    if (ur == null || ur.length() == 0) return 1;
    return Aus.getNumber(ur);
  }
  
  public static String getSeqOpis(DataSet ms) {
    findOJ(ms);
    if (myorg.getString("FPOJED").equals("D")) 
      return "FISK-" + myorg.getString("CCERT") + "-" + myorg.getString("FPP") + "-" + ms.getString("GOD");
    
    if (!myorg.getString("FPOJED").equals("G") || "GRC|GOT|GRN".indexOf(ms.getString("VRDOK")) < 0) 
      return "FISK-" + myorg.getString("CCERT") + "-"  + myorg.getString("FPP") + "-" + getFiskNap(ms) + "-" + ms.getString("GOD");
    
    return "FISK-" + myorg.getString("CCERT") + "-" + myorg.getString("FPP") + "-" + getFiskNapG(ms) + "-" + ms.getString("GOD");
  }
  
  
  /*
  private static int fisksep = -1;
  
  public static boolean isFiskSep() {
    if (fisksep < 0) {
      fisksep = frmParam.getParam("robno", "fiskPojed", "N",
          "Brojaè fiskalizacije po naplatnom mjestu (D,N)")
          .equalsIgnoreCase("D") ? 1 : 0;
    }
    return fisksep == 1;
  }
  
  public static String getFiskPP(String cskl) {
    if (isSkladOriented()) {
      return frmParam.getParam("robno", "fiskPP"+cskl, "", "Oznaka poslovnog prostora " + cskl + " (lokalno)", true);
    }
    return getFiskPP();
  }
    
  public static String getFiskPP() {
    return frmParam.getParam("robno", "fiskPP", "", "Oznaka poslovnog prostora (lokalno)", true);
  }
  
  public static int getFiskNapG() {
    String ur = frmParam.getParam("robno", "fiskNapG", "2", "Oznaka naplatnog ureðaja za gotovinu (lokalno)", true);
    if (ur == null || ur.length() == 0) return 1;
    return Aus.getNumber(ur);
  }
  
  public static int getFiskNap(String cskl) {
    if (isSkladOriented()) {
      String ur = frmParam.getParam("robno", "fiskNap"+cskl, "1", "Oznaka naplatnog ureðaja " + cskl + " (lokalno)", true);
      if (ur == null || ur.length() == 0) return 1;
      return Aus.getNumber(ur);
    }
    return getFiskNap();
  }
  
  public static int getFiskNap() {
    
    String ur = frmParam.getParam("robno", "fiskNap", "1", "Oznaka naplatnog ureðaja (lokalno)", true);
    if (ur == null || ur.length() == 0) return 1;
    return Aus.getNumber(ur);
  }*/
  
  public static boolean isSmjena() {
    if (smjena < 0) {
      smjena = frmParam.getParam("pos", "posSmjena", "N",
          "Postoji li smjena na blagajni (D,N)")
          .equalsIgnoreCase("D") ? 1 : 0;
    }
    return smjena == 1;
  }

  public static String prodmj = null;

  public static String getPRODMJ() {
    if (prodmj == null) {
      prodmj = frmParam.getParam("pos", "prodmj", "",
          "Oznaka prodajnog mjesta za POS (per makina!)", true);
    }
    return prodmj;
  }

  public static String getBlagajnik() {
    return blagajnik;
  }

  public static boolean isSuper() {
    return isSuper;
  }

  /**
   * Override za NAR i sl.
   * 
   * @return
   */
  protected String getVRDOK() {
    return "GRC";
  }
  
  static FisUtil fis = null;
  
  static HashMap fisks = new HashMap();
  
  
  public static FisUtil getFis(DataSet ms) {
    return getFis(ms.getString("VRDOK"), ms.getString("CSKL"));
  }
  
  public static FisUtil getFis(String vrdok, String cskl) {
    FisUtil f = (FisUtil) fisks.get(cskl);
    if (f != null) return f;
    
    findOJ(vrdok, cskl);
    
    try {
      f = new FisUtil(Aus.findFileAnywhere(myorg.getString("FPATH")).getPath(), myorg.getString("FKEY"), null);
      fisks.put(cskl, f);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return f;
  }
  
  
  /*public static FisUtil getFis(String cskl) {
    if (isSkladOriented()) {
      FisUtil f = (FisUtil) fisks.get(cskl);
      if (f == null) {
        try {
          f = new FisUtil(frmParam.getParam("sisfun", "fiskey"+cskl, "", "Keystore za fiskalizaciju "+cskl),
              frmParam.getParam("sisfun", "fispass"+cskl, "1restart2", "Password za fiskalizaciju " + cskl), null);
          fisks.put(cskl, f);
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      return f;
    }
    return getFis();
  }
  
  
  public static FisUtil getFis() {    
    if (fis == null) {
      try {
        fis = new FisUtil(frmParam.getParam("sisfun", "fiskey", "", "Keystore za fiskalizaciju"),
            frmParam.getParam("sisfun", "fispass", "1restart2", "Password za fiskalizaciju"), null);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return fis;
  }*/

  private void setProd_mjLookup() {
    String cskl = jrfCSKL.getDataSet().getString(jrfCSKL.getColumnName());
    if (!cskl.equals("")) {
      QueryDataSet set = (QueryDataSet) jrfCPRODMJ.getRaDataSet();
      set.close();
      String q = "SELECT * FROM prod_mj WHERE cskl = '" + cskl + "'";
      System.out.println("setProd_mjLookup :: " + q);
      set.setQuery(new QueryDescriptor(dM.getDataModule().getDatabase1(), q,
          true));
      set.open();
      jrfCPRODMJ.forceFocLost();
    }
  }
}
