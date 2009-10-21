package hr.restart.robno;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import hr.restart.baza.Condition;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raDateRange;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raUpitLite;


public class frmIzvHCCP extends raUpitLite {
  
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  Valid vl = Valid.getValid();
  
  JPanel jp = new JPanel();
  XYLayout lay = new XYLayout();
  
  JLabel jlCSKL = new JLabel();
  JlrNavField jrfCSKL = new JlrNavField();
  JlrNavField jrfNAZSKL = new JlrNavField();
  JraButton jbCSKL = new JraButton();
  JLabel jlDatum = new JLabel();
  JraTextField jtfDATFROM = new JraTextField();
  JraTextField jtfDATTO = new JraTextField();
  TableDataSet tds = new TableDataSet();
  
  static frmIzvHCCP fD;
  
  public static frmIzvHCCP getInstance() {
    return fD;
  }
  
  public frmIzvHCCP() {
    try {
      fD = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  public void componentShow() {
    jrfCSKL.setText(hr.restart.sisfun.raUser.getInstance().getDefSklad());
    jrfCSKL.forceFocLost();
    tds.setTimestamp("DATFROM", ut.getFirstDayOfMonth());
    tds.setTimestamp("DATTO", vl.getToday());
    if (jrfCSKL.getText().length() == 0)
      jrfCSKL.requestFocusLater();
    else jtfDATFROM.requestFocusLater();
  }

  public void firstESC() {
    rcc.EnabDisabAllLater(getJPan(), true);
    if (jrfCSKL.getText().length() == 0)
      jrfCSKL.requestFocusLater();
    else jtfDATFROM.requestFocusLater();
  }
  
  private QueryDataSet repSet; 
  
  public QueryDataSet getTds(){
    return repSet;
  }

  public void okPress() {
    
    String q = 
     "SELECT doku.datdok, doku.brdok, stdoku.nazart, stdoku.kol,"+
      "stdoku.datpro, stdoku.lot, stdoku.creg, stdoku.rbr, partneri.nazpar"+
      " FROM doku,stdoku,partneri WHERE "+rut.getDoc("doku", "stdoku")+
      " AND doku.cpar=partneri.cpar AND doku.vrdok='PRI' AND "+
      jrfCSKL.getCondition().and(Condition.between("DATDOK", 
          tds, "DATFROM", "DATTO")).qualified("doku");
    
    QueryDataSet ds = ut.getNewQueryDataSet(q, false);
    openScratchDataSet(ds);
    if (ds.getRowCount() == 0) setNoDataAndReturnImmediately();

    repSet = ds;
    ds.setSort(new SortDescriptor(new String[] {"DATDOK", "BRDOK", "RBR"}));
  }
  
  public boolean Validacija() {
    return Aus.checkDateRange(jtfDATFROM, jtfDATTO);
  }

  public boolean isIspis() {
    return false;
  }
  
  public boolean ispisNow() {
    return true;
  }
  
  public boolean runFirstESC() {
    // TODO Auto-generated method stub
    if (jrfCSKL.getText().length() == 0) return false;
    jrfCSKL.setText("");
    jrfCSKL.forceFocLost();
    return true;
  }
  
  private void jbInit() throws Exception {
    tds.setColumns(new Column[] {
        dM.createStringColumn("CSKL", "Skladište", 12),
        dM.createTimestampColumn("DATFROM", "Datum od"),
        dM.createTimestampColumn("DATTO", "Datum do")
    });
    tds.open();
    
    jlDatum.setText("Datum");
    jlCSKL.setText("Skladište");
    lay.setWidth(555);
    lay.setHeight(80);
    jp.setLayout(lay);
    jrfCSKL.setColumnName("CSKL");
    jrfCSKL.setDataSet(tds);
    jrfCSKL.setColNames(new String[] {"NAZSKL"});
    jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZSKL});
    jrfCSKL.setVisCols(new int[] {0,1});
    jrfCSKL.setSearchMode(0);
    jrfCSKL.setRaDataSet(rut.getSkladFromCorg());
    jrfCSKL.setNavButton(jbCSKL);

    jrfNAZSKL.setColumnName("NAZSKL");
    jrfNAZSKL.setNavProperties(jrfCSKL);
    jrfNAZSKL.setSearchMode(1);
    
    jtfDATFROM.setDataSet(tds);
    jtfDATFROM.setColumnName("DATFROM");
    jtfDATTO.setDataSet(tds);
    jtfDATTO.setColumnName("DATTO");
    
    new raDateRange(jtfDATFROM, jtfDATTO);
    
    jp.add(jlCSKL, new XYConstraints(15, 20, -1, -1));
    jp.add(jlDatum, new XYConstraints(15, 45, -1, -1));
    jp.add(jrfCSKL, new XYConstraints(150, 20, 100, -1));
    jp.add(jrfNAZSKL, new XYConstraints(260, 20, 255, -1));
    jp.add(jbCSKL, new XYConstraints(519, 20, 21, 21));
    jp.add(jtfDATFROM, new XYConstraints(150, 45, 100, -1));
    jp.add(jtfDATTO, new XYConstraints(255, 45, 100, -1));

    setJPan(jp);
    
    this.addJasper("hr.restart.robno.repHCCPulaz","hr.restart.robno.repHCCPulaz","hccpul.jrxml","Ispis evidencije");
  }
}
