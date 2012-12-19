package hr.restart.robno;

import java.sql.Timestamp;

import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raUpitLite;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class FISBIHIzvjestaji extends raUpitLite {
  private static FISBIHIzvjestaji _inst;
  StorageDataSet fldset = new StorageDataSet();
  public FISBIHIzvjestaji() {
    super();
    setJPan(createJP());
    _inst = this;
  }
  
  public static Timestamp getOD() {
    return _inst.fldset.getTimestamp("OD");
  }
  public static Timestamp getDO() {
    return _inst.fldset.getTimestamp("DO");
  }
  JraTextField datumOD = new JraTextField();
  JraTextField datumDO = new JraTextField();
  
  private JPanel createJP() {
    fldset.addColumn(dM.createTimestampColumn("OD"));
    fldset.addColumn(dM.createTimestampColumn("DO"));
    datumOD.setColumnName("OD");
    datumDO.setColumnName("DO");
    datumOD.setDataSet(fldset);
    datumDO.setDataSet(fldset);
    JPanel jp = new JPanel();
    jp.setLayout(new XYLayout(375, 35));
    
    jp.add(new JLabel("Period "), new XYConstraints(20, 5, -1, -1));
    jp.add(datumOD, new XYConstraints(150, 5, 100, -1));
    jp.add(datumDO, new XYConstraints(260, 5, 100, -1));

    return jp;
  }
  public void okPress() {

  }

  public boolean runFirstESC() {
    return false;
  }

  public void firstESC() {
  }

  public void componentShow() {
    fldset.open();
    fldset.emptyAllRows();
    fldset.insertRow(false);
    Valid.getValid().getCommonRange(datumOD, datumDO);
    killAllReports();
    addReport("hr.restart.robno.repFISBIHizvjZ", "Z - Dnevni fiskalni izvještaj");
    addReport("hr.restart.robno.repFISBIHizvjX", "X - Presjek stanja");
    addReport("hr.restart.robno.repFISBIHizvjR", "R - Periodièni izvještaj");
  }

}
