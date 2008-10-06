package hr.restart.pl;

import java.math.BigDecimal;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

import hr.restart.baza.Condition;
import hr.restart.baza.Logotipovi;
import hr.restart.robno.dlgKupac;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.reports.mxPrinter;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.repDisk;

public class repSBDisk_Splitska extends repDisk {
  StorageDataSet qds;
  frmBankSpec fBS = frmBankSpec.getInstance();
  hr.restart.baza.dM dm;
  BigDecimal naRukeUK = Aus.zero2;
  Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();
  int cBanke;
  String tipFile;
  String knjigovodstvo  = OrgStr.getKNJCORG();
  
  public repSBDisk_Splitska() {
    super(27);
    try {

    try {
      this.setPrinter(mxPrinter.getDefaultMxPrinter());
      //this.getPrinter().setNewline(System.getProperty("line.separator"));
      this.getPrinter().setNewline("\r\n");
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    qds = (StorageDataSet)fBS.getSpecBankDS(new String[] {"CISPLMJ","CORG","PREZIME","IME"});
    qds.open();
    this.setPrint("dohodak.dat");

    fill();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  public void fill() throws Exception {
    diskzap.open();
    qds.open();
    naRukeUK = Aus.zero2;
    for (qds.first(); qds.inBounds(); qds.next()) {
      naRukeUK = naRukeUK.add(qds.getBigDecimal("NARUKE"));
    }
    String naruu = getMasked(naRukeUK);
    String date = fBS.getDatumIspl().toString();
    String dd = date.substring(8,10);
    String mm = date.substring(5,7);
    String yyyy = date.substring(2,4);
    diskzap.insertRow(false);
    diskzap.setString("REDAK", "ST"+dd+mm+yyyy+naruu);
    diskzap.post();
    diskzap.insertRow(false);
    StorageDataSet logot = Logotipovi.getDataModule().getTempSet(Condition.equal("CORG", knjigovodstvo));
    logot.open();
    logot.first();
    diskzap.setString("REDAK", "ST"+logot.getString("NAZIVLOG"));
    diskzap.post();
    for (qds.first(); qds.inBounds(); qds.next()) {
      String ts = qds.getString("brojtek");
      String s = (ts.length()<11)?"0":"";
      s += ts;
      s += getMasked(qds.getBigDecimal("NARUKE"));
      s += (qds.getBigDecimal("NARUKE").signum()<0?"-":" ");
      diskzap.insertRow(false);
      diskzap.setString("REDAK", s);
      diskzap.post();
    }
    
  }

  private String getMasked(BigDecimal bd) {
    return vl.maskZeroInteger(new Integer(bd.abs().multiply(new BigDecimal(100)).intValue()), 15);
  }
  
  
}
