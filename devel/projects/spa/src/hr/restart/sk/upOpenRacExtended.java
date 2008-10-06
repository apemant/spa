package hr.restart.sk;

import java.awt.Toolkit;
import java.math.BigDecimal;
import java.util.Arrays;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;

import hr.restart.baza.dM;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;
import hr.restart.util.startFrame;
//showFrame("hr.restart.sk.upOpenRacExtended", "Dugovanja / potraživanja");
public class upOpenRacExtended extends upOpenRac {
  
  JraTextField jraPAll = new JraTextField();
  boolean setCreated = false;
  String[] pers;
  private boolean kumul = true;
  
  public upOpenRacExtended() {
    super();
    extInit();
  }
  public static upOpenRacExtended show(String defaults) {
    return show(defaults, true, null);
  }
  static String otpisi = null;
  public static upOpenRacExtended show(String defaults, boolean _kumul, String _otpis) {
    otpisi = _otpis;
    upOpenRacExtended up = new upOpenRacExtended();
    up.kumul = _kumul;
    up.jraPAll.setText(defaults);
    startFrame.getStartFrame().centerFrame(up,0,"Dugovanja / potraživanja");
    up.setVisible(true);
    return up;
  }
  
  public boolean ispisNow() {
    return true;
  }
  
  public void ispis(){
    //super.ispis();
    StorageDataSet sumset = new StorageDataSet();
    sumset.addColumn(dM.createStringColumn("OPIS", 100));
    sumset.addColumn(dM.createBigDecimalColumn("VRIJEDNOST"));
    sumset.addColumn(dM.createBigDecimalColumn("OTPIS"));
    sumset.addColumn(dM.createBigDecimalColumn("UKUPNO"));
    if (kumul) {
      BigDecimal tsal = Aus.zero2, totalu = Aus.zero2, total = Aus.zero2, ndosp = Aus.zero2;
      BigDecimal[] bdotp = null;
      if (otpisi != null) { 
        String[] otp = new VarStr(otpisi).split(',');
        bdotp = new BigDecimal[otp.length];
        for (int j = 0; j < bdotp.length; j++) {
          bdotp[j] = new BigDecimal(otp[j]);
        }
      }
      BigDecimal[] bdpers = new BigDecimal[pers.length+1];
      Arrays.fill(bdpers, Aus.zero2);
      for (repQDS.first(); repQDS.inBounds(); repQDS.next()) {
        tsal = tsal.add(repQDS.getBigDecimal("TSAL"));
        totalu = totalu.add(repQDS.getBigDecimal("TOTALU"));
        total = total.add(repQDS.getBigDecimal("TOTAL"));
        ndosp = ndosp.add(repQDS.getBigDecimal("NDOSP"));
        for (int i = 0; i < bdpers.length; i++) {
          bdpers[i] = bdpers[i].add(repQDS.getBigDecimal("PER"+(i+1)));
        }
      }
      sumset.open();
      addSumRecord(sumset, "Ukupno saldo", tsal);
      addSumRecord(sumset, "Otvorene uplate", totalu);
      addSumRecord(sumset, "Ukupno raèuni", total);
      addSumRecord(sumset, "Nedospjelo", ndosp);
      for (int i = 0; i < bdpers.length; i++) {
        if (bdotp == null) {
          addSumRecord(sumset, repQDS.getColumn("PER"+(i+1)).getCaption(), bdpers[i]);
        } else {
          addSumRecord(sumset, repQDS.getColumn("PER"+(i+1)).getCaption(), bdpers[i], bdotp[i]);
        }
      }
    }
    viewSet(kumul?sumset:repQDS);
    //debug
    //viewSet(repQDS);
  }
  private void viewSet(StorageDataSet set) {
    frmTableDataView view = new frmTableDataView();
    view.setTitle(getNaslov()+" "+getPodnaslov());
    view.setSize(640, 400);
    view.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 320, 200);
    view.setDataSet(set);
    if (kumul) {
      view.setSums(new String[] {"OTPIS","UKUPNO"});
    } else {
      view.setSums(new String[] {"TSAL", "TOTALU", "TOTAL", "NDOSP"});
    }
    view.setVisible(true);
  }
  private void addSumRecord(StorageDataSet s, String caption, BigDecimal val) {
    addSumRecord(s, caption, val, null);
  }
  private void addSumRecord(StorageDataSet s, String caption, BigDecimal val, BigDecimal botp) {
    s.insertRow(false);
    s.setString("OPIS", caption);
    s.setBigDecimal("VRIJEDNOST", val);
    if (botp != null) {
      s.setBigDecimal("OTPIS", val.multiply(botp));
      s.setBigDecimal("UKUPNO", val.multiply(Aus.one0.subtract(botp)));
    }
    s.post();
  }

  public boolean Validacija() {
    if (!super.Validacija()) return false;
    pers = new VarStr(jraPAll.getText()).split(',');
    checkPeriod(pers);
    remakeRepQDS(pers);
    return true;
  }
  /**
   * Kopipejstano iz super klase, nije mi se dalo modificirat i zvat onu sa JText..[] 
   * @param alls
   */
  private void checkPeriod(String[] alls) {
    int previous = 0;
    for (int i = 0; i < 4; i++) {
      int num = Aus.getNumber(alls[i]);
      if (num == 0 || previous < 0) num = -1;
      else if (num <= previous) num = previous + 1;
      previous = num;
      alls[i]=(num == -1 ? "" : String.valueOf(num));
    }
  }
  public void extInit() {
    pan.remove(jraP1);
    pan.remove(jraP2);
    pan.remove(jraP3);
    pan.remove(jraP4);
    pan.add(jraPAll,new XYConstraints(290, 170 + dkAdd, 255, -1));
  }

  
  public void initRepQDSColumns() {
 //nista dok ne odabere 
  }
  
  public void remakeRepQDS(String[] pers) {
    repQDS = new QueryDataSet();
    initRepQDSColumnsBasic();
    int _per = -1;
    for (int i = 0; i < pers.length; i++) {
      String caption = ((_per<=0)?0:_per+1)+" - "+pers[i]+" dana ";
      _per = Aus.getNumber(pers[i]);
      repQDS.addColumn(dM.createBigDecimalColumn("PER"+(i+1),caption));
    }
    
    repQDS.addColumn(dM.createBigDecimalColumn("PER"+(pers.length+1), "Preko "+_per+" dana"));
    repQDS.open();
  }  
  
  public void dispatchPeriods(BigDecimal sal, int kas) {
    if (!dospPer) kas = -kas;
    boolean inPeriod = false;
    for (int i = 0; i < pers.length; i++) {
      if ((kas <= Aus.getNumber(pers[i])) || (Aus.getNumber(pers[i]) == 0)) {
        repQDS.setBigDecimal("PER"+(i+1), repQDS.getBigDecimal("PER"+(i+1)).add(sal));
        inPeriod = true;
        break;
      }
    }
    //else
    if (!inPeriod) repQDS.setBigDecimal("PER"+(pers.length+1), repQDS.getBigDecimal("PER"+(pers.length+1)).add(sal));
  }
}
