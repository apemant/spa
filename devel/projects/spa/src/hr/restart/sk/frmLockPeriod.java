package hr.restart.sk;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

import javax.swing.JLabel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraPanel;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raUpitLite;
import hr.restart.zapod.OrgStr;


public class frmLockPeriod extends raUpitLite {
  
  JraPanel main = new JraPanel();
  XYLayout lay = new XYLayout(545, 90);
  
  JraButton jbGetOJ = new JraButton();
  JlrNavField jlrCORG = new JlrNavField();
  JlrNavField jlrNAZORG = new JlrNavField();
  JraTextField jraGodmj = new JraTextField();
  JraTextField jraGod = new JraTextField();
  JraTextField jraMj = new JraTextField();
  JraButton jbLeft = new JraButton();
  JraButton jbRight = new JraButton();
  StorageDataSet ds = new StorageDataSet();
  
  public frmLockPeriod() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    ds.setColumns(new Column[] {
        dM.getDataModule().getOrgstruktura().getColumn("CORG").cloneColumn(),
        dM.createIntColumn("GOD", "Godina"),
        dM.createIntColumn("MJ", "Mjesec"),
        dM.createStringColumn("GODMJ", "Godina i mjesec", 6)
    });
    ds.open();
    ds.insertRow(false);
    
    main.setLayout(lay);
    
    jlrCORG.setColumnName("CORG");
    jlrCORG.setDataSet(ds);
    jlrCORG.setVisCols(new int[] {0,1});
    jlrCORG.setColNames(new String[] {"NAZIV"});
    jlrCORG.setTextFields(new javax.swing.text.JTextComponent[] {jlrNAZORG});
    jlrCORG.setRaDataSet(OrgStr.getOrgStr().getKnjigovodstva());
    jlrCORG.setSearchMode(0);
    jlrCORG.setNavButton(jbGetOJ);
    jlrNAZORG.setColumnName("NAZIV");
    jlrNAZORG.setSearchMode(1);
    jlrNAZORG.setNavProperties(jlrCORG);
    
    jraGodmj.setDataSet(ds);
    jraGodmj.setColumnName("GODMJ");
    jraGod.setDataSet(ds);
    jraGod.setColumnName("GOD");
    jraMj.setDataSet(ds);
    jraMj.setColumnName("MJ");
    
    raCommonClass.getraCommonClass().setLabelLaF(jraGod, false);
    raCommonClass.getraCommonClass().setLabelLaF(jraMj, false);
    raCommonClass.getraCommonClass().setLabelLaF(jraGodmj, false);
    raCommonClass.getraCommonClass().setLabelLaF(jlrCORG, false);
    raCommonClass.getraCommonClass().setLabelLaF(jlrNAZORG, false);
    
    jbLeft.setIcon(raImages.getImageIcon(raImages.IMGBACK));
    jbRight.setIcon(raImages.getImageIcon(raImages.IMGFORWARD));
    
    jbLeft.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (ds.getInt("MJ") == 1) {
          ds.setInt("MJ", 12);
          ds.setInt("GOD", ds.getInt("GOD") - 1);
        } else
          ds.setInt("MJ", ds.getInt("MJ") - 1);
        
        ds.setString("GODMJ", Aus.leadzero(ds.getInt("GOD"), 4) + Aus.leadzero(ds.getInt("MJ"), 2));
      }
    });
    jbRight.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (ds.getInt("MJ") == 12) {
          ds.setInt("MJ", 1);
          ds.setInt("GOD", ds.getInt("GOD") + 1);
        } else
          ds.setInt("MJ", ds.getInt("MJ") + 1);
        
        ds.setString("GODMJ", Aus.leadzero(ds.getInt("GOD"), 4) + Aus.leadzero(ds.getInt("MJ"), 2));
      }
    });
    
    main.add(new JLabel("Knjigovodstvo"), new XYConstraints(15, 20, -1, -1));
    main.add(jlrCORG, new XYConstraints(150, 20, 100, -1));
    main.add(jlrNAZORG, new XYConstraints(255, 20, 250, -1));
    main.add(jbGetOJ, new XYConstraints(510, 20, 21, 21));
    main.add(new JLabel("Zakljuèati do (god/mj)"), new XYConstraints(15, 50, -1, -1));
    
    main.add(jbLeft, new XYConstraints(175, 50, 21, 21));
    main.add(jraGod, new XYConstraints(200, 50, 50, -1));
    main.add(jraMj, new XYConstraints(255, 50, 40, -1));
    main.add(jbRight, new XYConstraints(300, 50, 21, 21));
    main.add(jraGodmj, new XYConstraints(405, 50, 100, -1));
    
    setJPan(main);
  }
  
  public boolean Validacija() {
    if (!raSaldaKonti.checkUplate(jraGodmj)) return false;
    
    return true;
  }

  public void okPress() {
    // ništa
  }

  public boolean runFirstESC() {
    // TODO Auto-generated method stub
    return false;
  }

  public void firstESC() {
    // 
  }
  
  public void ok_action() {
    if (Validacija()) raSaldaKonti.lockTaxPeriod(jraGodmj);
  }
  
  public void componentShow() {
    ds.setString("CORG",  OrgStr.getKNJCORG(false));
    jlrCORG.forceFocLost();
    
    raCommonClass.getraCommonClass().setLabelLaF(jbGetOJ, false);
    raCommonClass.getraCommonClass().setLabelLaF(jraGod, false);
    raCommonClass.getraCommonClass().setLabelLaF(jraMj, false);
    raCommonClass.getraCommonClass().setLabelLaF(jraGodmj, false);
    raCommonClass.getraCommonClass().setLabelLaF(jlrCORG, false);
    raCommonClass.getraCommonClass().setLabelLaF(jlrNAZORG, false);
    
    String godmj = "";
    if (lookupData.getlookupData().raLocate(dM.getDataModule().getOrgstruktura(), "CORG", OrgStr.getKNJCORG(false))) 
      godmj = dM.getDataModule().getOrgstruktura().getString("UIGODMJ");
    
    if (godmj.length() < 6) {
      Timestamp now = Valid.getValid().getToday();
      now = Util.getUtil().addMonths(now, -1);
      String dat = now.toString();
      godmj = dat.substring(0, 4).concat(dat.substring(5, 7));
    }
    System.out.println(godmj);
    
    ds.setString("GODMJ", godmj);
    ds.setInt("GOD", Aus.getNumber(godmj.substring(0, 4)));
    ds.setInt("Mj", Aus.getNumber(godmj.substring(4, 6)));
  }
}
