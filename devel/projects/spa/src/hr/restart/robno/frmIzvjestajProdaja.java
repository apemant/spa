/****license*****************************************************************
**   file: frmIzvjestajProdaja.java
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
import hr.restart.baza.VTCparHist;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.sisfun.raUser;
import hr.restart.sk.PartnerCache;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raTextMask;
import hr.restart.util.Aus;
import hr.restart.util.IntParam;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raNavAction;
import hr.restart.util.raProcess;
import hr.restart.util.raUpitFat;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmIzvjestajProdaja extends raUpitFat {
  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
  raCommonClass rcc = raCommonClass.getraCommonClass();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();
  
  private int minMjKup = 0; //TODO parametar??
  private BigDecimal minUtr = Aus.zero2;
  private BigDecimal maxUtr = Aus.zero2;

  QueryDataSet mainData = null;
  
  //HashMap partneriMap = null;
  PartnerCache pc;
  HashMap dosadasnjiPrometi = null;
  HashMap dosadMje = null;
  HashMap dosadGod = null;
  boolean seg, totp;
  BigDecimal gm, gv;
  
  rapancskl1 rpc = new rapancskl1(350);
  private JraTextField minmjkupstva = new JraTextField();
  private JraTextField minUtrzak = new JraTextField();
  private JraTextField maxUtrzak = new JraTextField();
  private JraTextField od = new JraTextField();
  private JraTextField nadan = new JraTextField();

  JPanel jp = new JPanel();
  JPanel jPanel3 = new JPanel();
  StorageDataSet fdata = new StorageDataSet();
  
  JPanel jps = new JPanel();
  JraTextField mali = new JraTextField();
  JraTextField veliki = new JraTextField();
  raComboBox nacin = new raComboBox();
  JraCheckBox jseg = new JraCheckBox();
  
  BorderLayout localBorderLayout = new BorderLayout();
  XYLayout localXYLayout = new XYLayout();
  
  raNavAction rnvMark = new raNavAction("Oznaèi/poništi segmentaciju", 
      raImages.IMGPREFERENCES, java.awt.event.KeyEvent.VK_ENTER){
    public void actionPerformed(java.awt.event.ActionEvent e){
      toggle();
    }
  };
  
  raNavAction rnvUpdate = new raNavAction("Ažuriraj kupce u CRM-u", 
      raImages.IMGSENDMAIL, java.awt.event.KeyEvent.VK_F7){
    public void actionPerformed(java.awt.event.ActionEvent e){
      updateCRM();
    }
  };

  public frmIzvjestajProdaja() {
    try {
      initialize();
//      instanceOfMe = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public String navDoubleClickActionName() {
    return "";
  }
  
  void toggle() {
    if (!seg) return;
    
    jptv_doubleClick();
    getJPTV().getDataSet().next();
  }
  
  public void jptv_doubleClick() {
    if (!seg) return;
    
    if (getJPTV().getDataSet().getString("SEG").length() > 0) {
      getJPTV().getDataSet().setString("SEG", "");
    } else {
      String seg = getSeg(getJPTV().getDataSet());
      int segn = seg.equals("M") ? 0 : seg.equals("V") ? 2 : 1;
      String[] opts = {"Mali", "Srednji", "Veliki"};
      int n = JOptionPane.showOptionDialog(this.getWindow(),
          "Po po zadanim pravilima, ovaj kupac bi trebao biti oznaèen kao '" +
          opts[segn] + "'.\nKako želite da bude oznaèen?", "Segmentacija", 0,
          JOptionPane.QUESTION_MESSAGE, null, opts, opts[segn]);
      if (n >= 0)
        getJPTV().getDataSet().setString("SEG", opts[n].substring(0, 1));
    }
    getJPTV().fireTableDataChanged();
  }
  
  public void cancelPress() {
    super.cancelPress();
    IntParam.setTag("posl.defSeg",
        fdata.getBigDecimal("AVGM") + "|" +
        fdata.getBigDecimal("AVGV") + "|" +
        fdata.getBigDecimal("TOTM") + "|" +
        fdata.getBigDecimal("TOTV"));
  }

  protected void updateCRM() {
    if (!seg) return;
    
    if (!raUpdateCRM.allowedSklad(rpc.getCSKL())) {
      JOptionPane.showMessageDialog(this.getWindow(), 
          "Pogrešno skladište za ažuriranje CRM-a!",
          "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    if (JOptionPane.showConfirmDialog(getWindow(),
        "Ova naredba æe ažurirati segmentaciju svih oznaèenih kupaca " +
        "u CRM-u. Nastaviti?", "Potvrda", JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE) != JOptionPane.OK_OPTION) return;

    if (!dM.getDataModule().isCRM()) {
      JOptionPane.showMessageDialog(this.getWindow(), 
          "Nije definirana konekcija na CRM, akcija nemoguæa!",
          "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    raProcess.runChild(getWindow(), new Runnable() {
      public void run() {
        int row = getJPTV().getDataSet().getRow();
        getJPTV().enableEvents(false);
        try {
          raProcess.yield(raUpdateCRM.updateSegmentation(
              getJPTV().getDataSet(), pc));
        } finally {
          getJPTV().getDataSet().goToClosestRow(row);
          getJPTV().enableEvents(true);
        }
      }
    });
    if (raProcess.isInterrupted()) return;
    if (raProcess.isFailed() || raProcess.getReturnValue() == null) {
      JOptionPane.showMessageDialog(this.getWindow(), 
          "Greška prilikom ažuriranja segmentacije u CRM-u!",
          "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    StorageDataSet result = (StorageDataSet) raProcess.getReturnValue();
    frmTableDataView view = new frmTableDataView();
    view.setDataSet(result);
    view.setTitle("Rezultati ažuriranja segmentacije kupaca");
    view.setSize(800, 500);
    view.setLocation(50, 50);
    view.jp.getMpTable().setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    view.show();
  }

  public int[] navVisibleColumns() {
    if (!seg) return new int[]{0,1,2,3,4,5};
    return new int[]{0,1,2,3,4,5,6};
  }

  public void okPress() {
    pc = new PartnerCache();
    makeMainSet();
    mainData.first();
    setDataSetAndSums(mainData,new String[] {"SVEUKUPNO","PRPOMJ"});
  }

  public boolean runFirstESC() {
    if (this.getJPTV().getDataSet() != null) return true;
    return false;
  }

  public void firstESC() {
    rcc.EnabDisabAll(this.getJPan(),true);
    this.getJPTV().clearDataSet();
    removeNav();
    rpc.jrfCSKL.requestFocusLater();
  }

  public void componentShow() {
    this.getJPTV().clearDataSet();
    rpc.setCSKL(raUser.getInstance().getDefSklad());
    od.requestFocusLater();
    //partnersHash();
  }
  
  /*private void partnersHash(){
    QueryDataSet partneriSet = Partneri.getDataModule().getTempSet();
    partneriSet.open();
    partneriMap = new HashMap();
    for(partneriSet.first();partneriSet.inBounds();partneriSet.next())
      partneriMap.put(Integer.valueOf(partneriSet.getInt("CPAR")+""),partneriSet.getString("NAZPAR"));
  }*/
  
  private void initialize() throws Exception {
    fdata.setColumns(new Column[] {
        dM.createTimestampColumn("DATOD", "Od dana"),
        dM.createTimestampColumn("NADAN", "Na dan"),
        dM.createBigDecimalColumn("AVGM", "Mali avg", 2),
        dM.createBigDecimalColumn("AVGV", "Veliki avg", 2),
        dM.createBigDecimalColumn("TOTM", "Mali tot", 2),
        dM.createBigDecimalColumn("TOTV", "Veliki tot", 2),
        dM.createStringColumn("NACIN", "Nacin", 1)
    });
    fdata.open();
    jp.setLayout(localBorderLayout);

    jPanel3.setLayout(localXYLayout);
    localXYLayout.setWidth(655);
    localXYLayout.setHeight(160);

    jp.add(jPanel3, BorderLayout.CENTER);
    
    minmjkupstva.setHorizontalAlignment(JTextField.RIGHT);
    new raTextMask(minmjkupstva, 3, false, raTextMask.DIGITS);
    new raTextMask(minUtrzak, 10, false, raTextMask.DIGITS);
    new raTextMask(maxUtrzak, 10, false, raTextMask.DIGITS);
    
    od.setDataSet(fdata);
    od.setColumnName("DATOD");
    fdata.setTimestamp("DATOD", findFirstRac());
    nadan.setDataSet(fdata);
    nadan.setColumnName("NADAN");
    fdata.setTimestamp("NADAN", Valid.getValid().getToday());

    this.setJPan(jp);

    jPanel3.add(rpc, new XYConstraints(0,0,-1,-1));
    jPanel3.add(new JLabel("Datum (od - do)"), new XYConstraints(15,75,-1,-1));
    jPanel3.add(od, new XYConstraints(150,75,100,-1));
    jPanel3.add(nadan, new XYConstraints(255,75,100,-1));
    jPanel3.add(new JLabel("Minimalno mjeseci"), new XYConstraints(425,50,-1,-1));
    jPanel3.add(minmjkupstva, new XYConstraints(555,50,50,-1));
    jPanel3.add(new JLabel("Prosjeèno mj. (min/max)"), new XYConstraints(15,50,-1,-1));
    jPanel3.add(minUtrzak, new XYConstraints(150,50,100,-1));
    jPanel3.add(maxUtrzak, new XYConstraints(255,50,100,-1));
    
    JLabel jm = new JLabel("Mali do");
    jm.setHorizontalAlignment(SwingConstants.CENTER);
    JLabel jv = new JLabel("Veliki od");
    jv.setHorizontalAlignment(SwingConstants.CENTER);
    JLabel jn = new JLabel("Naèin raèunanja");
    jn.setHorizontalAlignment(SwingConstants.CENTER);
    XYLayout slay = new XYLayout();
    slay.setHeight(60);
    slay.setWidth(600);
    jps.setLayout(slay);
    jps.add(jn, new XYConstraints(375, 10, 200, -1));
    jps.add(nacin, new XYConstraints(375, 25, 200, -1));
    jps.add(jm, new XYConstraints(150, 10, 100, -1));
    jps.add(mali, new XYConstraints(150, 25, 100, -1));
    jps.add(jv, new XYConstraints(255, 10, 100, -1));
    jps.add(veliki, new XYConstraints(255, 25, 100, -1));
    //jps.setBorder(BorderFactory.createEtchedBorder());
    
    mali.setDataSet(fdata);
    veliki.setDataSet(fdata);
    nacin.setRaColumn("NACIN");
    nacin.setRaDataSet(fdata);
    nacin.setRaItems(new String[][] {
        {"Ukupni promet u periodu", "U"},
        {"Prosjeèni mjeseèni promet", "M"} 
    });
    nacin.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == e.SELECTED) {
          mali.setColumnName(nacin.getSelectedIndex() == 0 ? "TOTM" : "AVGM");
          veliki.setColumnName(nacin.getSelectedIndex() == 0 ? "TOTV" : "AVGV");
          
        }
      }
    });
    nacin.setSelectedIndex(1);
    nacin.setSelectedIndex(0);
    
    jseg.setText(" Segmentacija kupaca ");
    jseg.setHorizontalAlignment(SwingConstants.TRAILING);
    jseg.setHorizontalTextPosition(SwingConstants.LEADING);
    jPanel3.add(jseg, new XYConstraints(450,75,150,-1));
    jseg.setSelected(false);
    jseg.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == e.SELECTED) {
          if (!jPanel3.isAncestorOf(jps))
            jPanel3.add(jps, new XYConstraints(0, 100, -1, -1));
        } else jPanel3.remove(jps);
        jPanel3.revalidate();
        jPanel3.repaint();
      }
    });
    
    String seg = frmParam.getParam("posl", "defSeg", "500|2000|2000|50000",
        "Inicijalna segmentacija", true);
    String[] parts = new VarStr(seg).splitTrimmed('|');
    try {
      fdata.setBigDecimal("AVGM", Aus.getDecNumber(parts[0]));
      fdata.setBigDecimal("AVGV", Aus.getDecNumber(parts[1]));
      fdata.setBigDecimal("TOTM", Aus.getDecNumber(parts[2]));
      fdata.setBigDecimal("TOTV", Aus.getDecNumber(parts[3]));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void addNavBarOptions() {
    super.addNavBarOptions();
    getJPTV().getNavBar().removeOption(rnvDoubleClick);
    if (seg) {
      getJPTV().getNavBar().addOption(rnvMark, 0);
      getJPTV().getNavBar().addOption(rnvUpdate, 1);
    }
  }
  
  protected void navbarremoval() {
    super.navbarremoval();
    if (getJPTV().getNavBar().contains(rnvMark))
      this.getJPTV().getNavBar().removeOption(rnvMark);
    if (getJPTV().getNavBar().contains(rnvUpdate))
      this.getJPTV().getNavBar().removeOption(rnvUpdate);
  }
  
  protected String getPrometQueryUpit() {
    Condition nd = Condition.between("DATDOK", 
        fdata.getTimestamp("DATOD"), fdata.getTimestamp("NADAN"));
    String upit = "SELECT min (doki.datdok) as datpf, max(doki.cpar) as cpar, sum(stdoki.ineto) as ineto, sum(stdoki.iprodbp) as iprodbp, sum(stdoki.iprodsp) as iprodsp " +
            "FROM doki, stdoki WHERE doki.cskl = stdoki.cskl AND doki.vrdok = stdoki.vrdok " +
            "AND doki.god = stdoki.god AND doki.brdok = stdoki.brdok AND doki.vrdok in ('ROT','RAC','POD') AND " +nd+
            " AND  doki.cskl = '"+rpc.getCSKL()+"' group by doki.cpar";
    return upit;
  }
  
  private void makeMainSet(){
    QueryDataSet minSet = Aus.q("SELECT doki.cpar, min(doki.datdok) as datpf " +
    		"FROM doki WHERE doki.vrdok in ('ROT','RAC','POD') AND " +
    		"doki.cskl = '"+rpc.getCSKL()+"' group by doki.cpar");
    
    QueryDataSet prometiSet = ut.getNewQueryDataSet(getPrometQueryUpit());
    
    if (minSet.rowCount() < 1) setNoDataAndReturnImmediately();
    
    dosadasnjiPrometi = new HashMap();
    dosadGod = new HashMap();
    dosadMje = new HashMap();
    
    QueryDataSet dsp = VTCparHist.getDataModule().getTempSet();
    dsp.open();
    if (dsp.rowCount() > 0) {
      for (dsp.first(); dsp.inBounds(); dsp.next()) {
        dosadasnjiPrometi.put(dsp.getInt("CPAR")+"",dsp.getBigDecimal("UKUPROM"));
        dosadGod.put(dsp.getInt("CPAR") + "", vl.findYear(dsp.getTimestamp("DATPRF")));
        dosadMje.put(dsp.getInt("CPAR") + "", ut.getMonth(dsp.getTimestamp("DATPRF")));
      }
      dsp.emptyAllRows();
    }
    dsp.close();
    
    Column[] columns = new Column[] {
        dm.createIntColumn("CPAR","Oznaka"),
        dm.createStringColumn("NAZPAR","Kupac",150),
        dm.createBigDecimalColumn("SVEUKUPNO","Dosadašnji promet",2),
        dm.createStringColumn("KUPOD","Kupac od",8),
        dm.createIntColumn("BRMJ","Ukupno mjeseci"),
        dm.createBigDecimalColumn("PRPOMJ","Prosjeèni mjeseèni promet",2),
        dm.createStringColumn("SEG", "Segmentacija", 1)
    };
    
    
    mainData = new QueryDataSet();
    mainData.setColumns(columns);
    mainData.setRowId("CPAR",true);
    mainData.open();
    
    int brmj = 0;
    
    int tm = Integer.parseInt(ut.getMonth(fdata.getTimestamp("NADAN")));
    int tg = Integer.parseInt(vl.findYear(fdata.getTimestamp("NADAN")));
    
    for(minSet.first(); minSet.inBounds(); minSet.next()){
      String kupacod = "";
      int cpar = minSet.getInt("CPAR"); 
      String cps = Integer.toString(cpar);
      BigDecimal total = Aus.zero2;
      if (!dosadasnjiPrometi.containsKey(cps)) {
        Timestamp beg = minSet.getTimestamp("DATPF");
        kupacod = ut.getMonth(beg) + "." + vl.findYear(beg)+".";
        brmj = monthsOfRelation(Integer.parseInt(ut.getMonth(beg)),
              Integer.parseInt(vl.findYear(beg)), tm, tg);
      } else {
        String sm = dosadMje.get(cps).toString();
        String sg = dosadGod.get(cps).toString();
        kupacod = sm + "." + sg + ".";
        brmj = monthsOfRelation(Integer.parseInt(sm), 
                    Integer.parseInt(sg), tm, tg);
        total = new BigDecimal(dosadasnjiPrometi.get(cps).toString()); 
      }
      if (ld.raLocate(prometiSet, "CPAR", cps))
        total = total.add(prometiSet.getBigDecimal("IPRODBP"));
      BigDecimal pros = total.divide(new BigDecimal(brmj), 2, 
                                BigDecimal.ROUND_HALF_UP);
      // kontrole min/max. 
      // Ove tri linije zamjenjuju onih 7652 zakomentiranih dolje
      if (brmj < minMjKup) continue;
      if (minUtr.signum() != 0 && pros.compareTo(minUtr) < 0) continue;
      if (maxUtr.signum() != 0 && pros.compareTo(maxUtr) > 0) continue;
      
      mainData.insertRow(false);
      mainData.setInt("BRMJ", brmj);
      mainData.setInt("CPAR", cpar);
      mainData.setString("NAZPAR", pc.getNameNotNull(cpar));
      mainData.setString("KUPOD", kupacod);
      mainData.setBigDecimal("SVEUKUPNO", total);
      mainData.setBigDecimal("PRPOMJ",total.
          divide(new BigDecimal(brmj), 2, BigDecimal.ROUND_HALF_UP));
      if (seg) mainData.setString("SEG", getSeg(mainData));
    }
    
    /* 
      --> srkyjeva nebulozna skalamerija za izbacivanje redova koji
      ne odgovaraju kriterijima: 38 linija. Sažeto u 3, još bez 
      suvišnog dvostrukog prolaska kroz listu. <-- :rolleyes:
      
    mainData.first();
    
    do {
      if (minmjkupstva.getText().trim().length() > 0 && 
          mainData.getInt("BRMJ") < minMjKup) {
        mainData.deleteRow();
        if (mainData.getRow() != 0) {
          mainData.goToRow(mainData.getRow() - 1);
          continue;
        } else {
          mainData.next();
        }
      }

      if (minUtr.compareTo(Aus.zero2) != 0 || maxUtr.compareTo(Aus.zero2) != 0) {
        if (minUtr.compareTo(Aus.zero2) != 0 && maxUtr.compareTo(Aus.zero2) != 0) {
          if ((mainData.getBigDecimal("PRPOMJ").compareTo(minUtr) >= 0) && (mainData.getBigDecimal("PRPOMJ").compareTo(maxUtr) <= 0)) {
            mainData.next();
            continue;
          }
          mainData.deleteRow();
          if (mainData.getRow() != 0)
            mainData.goToRow(mainData.getRow() - 1);
          continue;

        } else {
          if ((minUtr.compareTo(Aus.zero2) != 0 && mainData.getBigDecimal("PRPOMJ").compareTo(minUtr) >= 0) || (maxUtr.compareTo(Aus.zero2) != 0 && mainData.getBigDecimal("PRPOMJ").compareTo(maxUtr) <= 0)) {
            mainData.next();
            continue;
          }
          mainData.deleteRow();
          if (mainData.getRow() != 0)
            mainData.goToRow(mainData.getRow() - 1);
          continue;
        }
      } else {
        mainData.next();
      }
    } while (mainData.inBounds());
    */
    mainData.setSort(new SortDescriptor(new String[] {"NAZPAR"}));
  }
  
  private int monthsOfRelation(int mpf, int gpf, int ms, int gs){
    int relation = 0;
    
    /*if (gpf < gs){
      int cacheG = 13 - mpf;
      if ((gs-gpf) > 1)
        for (int g = (gpf+1); g < gs; g++){
          if ((g) < gs) cacheG += 12;
        }
      relation = cacheG + ms; 
    } else {
      relation = ms - mpf;
    }  =>  */  relation = 12 * (gs - gpf) + ms - mpf;
    return relation <= 0 ? 1 : relation;
  }
  
  Timestamp findFirstRac() {
    vl.execSQL("SELECT min(DATDOK) as datdok from doki WHERE "+
        "doki.vrdok in ('ROT','RAC') and doki.cskl='"+
        raUser.getInstance().getDefSklad()+"'");
    vl.RezSet.open();
    return vl.RezSet.getTimestamp("DATDOK");
  }
  
  String getSeg(ReadRow row) {
    BigDecimal act = row.getBigDecimal(totp ? "SVEUKUPNO" : "PRPOMJ");
    if (act.compareTo(gm) < 0)
      return "M";
    else if (act.compareTo(gv) >= 0)
      return "V";
    return "S";
  }

  public boolean Validacija() {
    minMjKup = 0;
    minUtr = Aus.zero2;
    maxUtr = Aus.zero2;
    if (minmjkupstva.getText().trim().length() > 0)
    try {
      minMjKup = Integer.parseInt(minmjkupstva.getText().trim());
    } catch (NumberFormatException e) {
      return false;
    }
    if (minUtrzak.getText().trim().length() > 0)
    try {
      minUtr = new BigDecimal(minUtrzak.getText().trim());
    } catch (NumberFormatException e) {
      return false;
    }
    if (maxUtrzak.getText().trim().length() > 0)
    try {
      maxUtr = new BigDecimal(maxUtrzak.getText().trim());
    } catch (NumberFormatException e) {
      return false;
    }
    int by = Aus.getNumber(ut.getYear(fdata.getTimestamp("DATOD")));
    int bm = Aus.getNumber(ut.getMonth(fdata.getTimestamp("DATOD")));
    int ey = Aus.getNumber(ut.getYear(fdata.getTimestamp("NADAN")));
    int em = Aus.getNumber(ut.getMonth(fdata.getTimestamp("NADAN")));
    int mdiff = monthsOfRelation(bm, by, em, ey);
    /*if (mdiff < minMjKup) {
      JOptionPane.showMessageDialog(this.getWindow(), 
          "Datumski raspon je manji od zadang minimalnog broja mjeseci!",
          "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }*/
    if (seg = jseg.isSelected()) {
      totp = nacin.getSelectedIndex() == 0;
      gm = fdata.getBigDecimal(totp ? "TOTM" : "AVGM");
      gv = fdata.getBigDecimal(totp ? "TOTV" : "AVGV");
      if (gm.signum() < 0) {
        JOptionPane.showMessageDialog(this.getWindow(), 
            "Pogrešan iznos granice malog kupca!",
            "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
      if (gv.compareTo(gm) < 0) {
        JOptionPane.showMessageDialog(this.getWindow(), 
            "Pogrešan iznos granice velikog kupca!",
            "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }

    return super.Validacija();
  }
  
  

}














