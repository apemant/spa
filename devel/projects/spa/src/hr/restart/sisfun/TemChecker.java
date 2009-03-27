package hr.restart.sisfun;

import hr.restart.baza.Condition;
import hr.restart.gk.frmGK;
import hr.restart.gk.frmKnjRobno;
import hr.restart.gk.frmNalozi;
import hr.restart.util.DataSetComparator;
import hr.restart.util.Util;
import hr.restart.util.Valid;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.SwingUtilities;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;


public class TemChecker extends ComponentAdapter {

  int ui;
  String knjig, cvrnal, god;
  int rbr;
  frmKnjRobno instance;
  String[] key = {"KNJIG", "GOD", "CVRNAL", "RBR"};
  boolean cont = false;
  
  public TemChecker(String knjig, String cvrnal, String god, int ui, int rbr) {
    this.knjig = knjig;
    this.cvrnal = cvrnal;
    this.god = god;
    this.rbr = rbr;
    this.ui = ui;
  }
  
  public void next() {
    if (frmNalozi.getFrmNalozi().raMaster.isShowing()) 
      frmNalozi.getFrmNalozi().raMaster.hide();
    
    frmNalozi.getFrmNalozi().raMaster.addComponentListener(this);
    
    instance = (frmKnjRobno) frmGK.getFrmGK().showFrame(
        "hr.restart.gk.frmKnjRobno","Knjiženje iz robnog knjigovodstva");
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        String brnal = god.substring(2, 4) + cvrnal + 
               Valid.getValid().maskZeroInteger(new Integer(rbr), 4);
        instance.setKnjVals(ui, "77", brnal);
        System.out.println("Simulacija temeljnice "+brnal);
        instance.simulateStart();
      }
    });
  }
  
  public void componentShown(ComponentEvent e) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        compareTems();
      }
    });
  }
  
  public void componentHidden(ComponentEvent e) {
    frmNalozi.getFrmNalozi().raMaster.removeComponentListener(this);
    if (cont) {
      cont = false;
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          next();
        }
      });
    }
  }
  
  private void compareTems() {
    Condition cgkr = Condition.whereAllEqual(key, frmNalozi.getFrmNalozi().getMasterSet()); 
    Condition cgk = Condition.whereAllEqual(key, new String[] {knjig, god, cvrnal,
        Integer.toString(rbr++)});
    
    DataSet gkr = Util.getNewQueryDataSet(
        "SELECT brojkonta||'-'||corg as bc, sum(id) as id, sum(ip) as ip " +
        "FROM Gkstavkerad WHERE " + cgkr + " GROUP BY corg, brojkonta");
    System.out.println(gkr);
    if (gkr.hasColumn("BROJKONTA") != null)
      gkr.getColumn("BROJKONTA").setVisible(0);
    if (gkr.hasColumn("CORG") != null)
      gkr.getColumn("CORG").setVisible(0);
    
    DataSet gk = Util.getNewQueryDataSet(
        "SELECT brojkonta||'-'||corg as bc, sum(id) as id, sum(ip) as ip " +
        "FROM Gkstavke WHERE " + cgk + " GROUP BY corg, brojkonta");
    System.out.println(gk);
    if (gk.hasColumn("BROJKONTA") != null)
      gk.getColumn("BROJKONTA").setVisible(0);
    if (gk.hasColumn("CORG") != null)
      gk.getColumn("CORG").setVisible(0);

    System.out.println("Simulacija temeljnice "+cvrnal+(rbr-1));
    DataSetComparator dc = new DataSetComparator(true, false);
    StorageDataSet result = dc.compare(gk, gkr, "BC");
    
    if (result.rowCount() > 0) {     
      frmTableDataView view = new frmTableDataView();
      view.setDataSet(result);
      view.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
      view.show();
    } else if (gk.rowCount() > 0) {
      frmNalozi.getFrmNalozi().getMasterSet().last();
      frmNalozi.getFrmNalozi().raMaster.LegalDelete(false, false);
      cont = true;
      frmNalozi.getFrmNalozi().raMaster.hide();
    }
  }
}
