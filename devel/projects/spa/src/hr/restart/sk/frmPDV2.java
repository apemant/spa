package hr.restart.sk;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.andiv.print.VarStr;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import hr.porezna_uprava.e_porezna.sheme.zahtjevi.obrazacpdv.v7_0.SRazdoblje;
import hr.restart.baza.IzvjPDV;
import hr.restart.baza.dM;
import hr.restart.robno.raDateUtil;
import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.swing.raTableModifier;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raUpit;
import hr.restart.util.raUpitFat;
import hr.restart.util.sysoutTEST;

public class frmPDV2 extends raUpitFat {
  
  JPanel datePanel;
  JLabel jlPer = new JLabel("Razdoblje");
  JraTextField jraPoctDat = new JraTextField();
  JraTextField jraKrajDat = new JraTextField();
  JraComboBox jraObrazac = new JraComboBox(new String[] {"Obrazac PDV","Obrazac PDV-S","Obrazac ZP","Obrazac PDV-K"});
  XYLayout xYlay = new XYLayout();
  StorageDataSet stds = new StorageDataSet();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  Util ut = Util.getUtil();
  Valid vl = Valid.getValid();
  
  public frmPDV2() {
    try {
      datePanel = new JPanel();
      datePanel.setLayout(xYlay);
      xYlay.setWidth(700);
      xYlay.setHeight(50);
      stds.setColumns(new Column[] {
        dm.createTimestampColumn("DATUMOD"),
        dm.createTimestampColumn("DATUMDO")
      });

      jraPoctDat.setDataSet(stds);
      jraPoctDat.setColumnName("DATUMOD");

      jraKrajDat.setDataSet(stds);
      jraKrajDat.setColumnName("DATUMDO");
      datePanel.add(jlPer, new XYConstraints(15,15,-1,-1));
      datePanel.add(jraPoctDat, new XYConstraints(150, 15, 100, -1));
      datePanel.add(jraKrajDat, new XYConstraints(255, 15, 100, -1));
      datePanel.add(jraObrazac, new XYConstraints(360, 15, 150, -1));
      setJPan(datePanel);
      _this = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void okPress() {
    setDataSet(null);
    int pick = jraObrazac.getSelectedIndex();
    System.err.println("***** PICK:"+pick);
    switch (pick) {
    case 0:
      doPDV();
      break;
      
    case 1:
      doPDV_S();
      break;
      
    case 2:
      doZP();
      break;
      
    case 3:
      doPDV_K();
      break;
      
    default:
      break;
    }

  }

  public boolean runFirstESC() {
    // TODO Auto-generated method stub
    return false;
  }

  public void firstESC() {
    // TODO Auto-generated method stub

  }

  public void componentShow() {
    stds.setTimestamp("DATUMOD", ut.getFirstDayOfMonth(ut.addMonths(vl.getToday(), -1)));
    stds.setTimestamp("DATUMDO", ut.getLastDayOfMonth(ut.addMonths(vl.getToday(), -1)));
  }

  
  
  private void doPDV_K() {
    // TODO Auto-generated method stub
    
  }

  private void doZP() {
    // TODO Auto-generated method stub
    
  }

  private void doPDV_S() {
    // TODO Auto-generated method stub
    
  }
  QueryDataSet izvjpdv = IzvjPDV.getDataModule().getQueryDataSet();
  private void doPDV() {
    try {
      Object rPDVD = Class.forName("hr.restart.sk.repPDVDisk").newInstance();
      HashMap data = (HashMap)rPDVD.getClass().getMethod("tijeloData", null).invoke(rPDVD, null);
      StorageDataSet mset = new StorageDataSet();
      mset.addColumn(dM.createStringColumn("POZ","Pozicija",200));
      mset.addColumn(dM.createBigDecimalColumn("OSN", "Osnovica"));
      mset.addColumn(dM.createBigDecimalColumn("PDV", "PDV"));
      mset.open();
      for (Iterator iterator = data.keySet().iterator(); iterator.hasNext();) {
        String ciz = (String) iterator.next();
        String poz;
        boolean p = ciz.trim().endsWith("p");
        if (p) {
          poz = new VarStr(ciz).replaceLast("p", "o").toString();
        } else {
          poz = ciz;
        }
        if (!lookupData.getlookupData().raLocate(mset, "POZ", poz)) {
          mset.insertRow(false);
          mset.setString("POZ", poz);
        }
        mset.setBigDecimal(p?"PDV":"OSN", (BigDecimal)data.get(ciz));
        mset.post();
      }
      setDataSet(mset);
      getJPTV().removeAllTableModifiers();
      getJPTV().addTableModifier(
          new raTableColumnModifier("POZ", new String[] {"OPIS"}, new String[] {"POZ"}, new String[] {"CIZ"}, izvjpdv));
      addReport("hr.restart.sk.repPDVDisk", "Datoteka za e-poreznu");
      setTitle("Obrazac PDV za period "+raDateUtil.getraDateUtil().dataFormatter(getDatumOd())+" - "+raDateUtil.getraDateUtil().dataFormatter(getDatumDo()));
//      getJPTV().fireTableDataChanged();
    } catch (Exception e) {
      // TODO: handle exception
    }
  }
  private static frmPDV2 _this;
  public static frmPDV2 getInstance() {
    return _this;
  }
  public java.sql.Timestamp getDatumOd(){
    return stds.getTimestamp("DATUMOD");
  }

  public java.sql.Timestamp getDatumDo(){
    return stds.getTimestamp("DATUMDO");
  }

  public String navDoubleClickActionName() {
    // TODO Auto-generated method stub
    return null;
  }

  public int[] navVisibleColumns() {
    // TODO Auto-generated method stub
    return null;
  }
}
