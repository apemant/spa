package hr.restart.pos;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import hr.restart.baza.dM;
import hr.restart.robno.rapancart;
import hr.restart.robno.rapancskl;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.lookupData;
import hr.restart.util.raUpitLite;


public class upKarticaPos extends raUpitLite {

  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  dM dm = hr.restart.baza.dM.getDataModule();
  lookupData ld = lookupData.getlookupData();
  
  TableDataSet tds = new TableDataSet();
  
  JPanel mainPanel = new JPanel();
  XYLayout mainXYLayout = new XYLayout();
  
  rapancskl rpcskl = new rapancskl() {
    public void findFocusAfter() {
      rpcart.setCskl(rpcskl.getCSKL());
      rpcart.setGodina(vl.findYear(tds.getTimestamp("pocDatum")));
      if (rpcart.getCART().length() == 0) {
        rpcart.setDefParam();
        rpcart.setCART();
      }
    }
  };
  
  rapancart rpcart = new rapancart() {
    public void nextTofocus(){
      jtfPocDatum.requestFocus();
    }
  };
  JraTextField jtfPocDatum = new JraTextField();
  JraTextField jtfZavDatum = new JraTextField();
  JLabel jlDatum = new JLabel();
  
  frmTableDataView ret;
  
  
  public upKarticaPos() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  private void jbInit() throws Exception {
    mainXYLayout.setWidth(640);
    mainXYLayout.setHeight(175);
    this.setJPan(mainPanel);
    mainPanel.setLayout(mainXYLayout);
    
    tds.setColumns(new Column[] {dm.createStringColumn("CSKL","Prodajno mjesto",12),
        dm.createTimestampColumn("pocDatum", "Poèetni datum"),
        dm.createTimestampColumn("zavDatum", "Krajnji datum"),
        });
    tds.open();
    
    jlDatum.setText("Datum (od-do)");
    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setDataSet(tds);

    jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfZavDatum.setColumnName("zavDatum");
    jtfZavDatum.setDataSet(tds);
    
    rpcart.setMode("DOH");
    rpcart.setBorder(null);
    rpcskl.setRaMode('S');
    
    mainPanel.add(rpcskl, new XYConstraints(0, 0, -1, -1));
    mainPanel.add(rpcart, new XYConstraints(0, 50, -1, -1));
    mainPanel.add(jlDatum,   new XYConstraints(15, 140, -1, -1));
    mainPanel.add(jtfPocDatum, new XYConstraints(150, 140, 100, -1));
    mainPanel.add(jtfZavDatum, new XYConstraints(255, 140, 100, -1));
  }
  
  public void componentShow() {
    tds.open();
    tds.setTimestamp("pocDatum", vl.getToday());
    tds.setTimestamp("zavDatum", vl.getToday());
    rpcart.EnabDisab(true);
    rpcart.clearFields();
    rpcskl.setDisab('N');
    rpcskl.setCSKL("");
  }

  public void firstESC() {
    if (rpcart.getCART().length()>0) {
      rpcart.EnabDisab(true);
      rpcart.setCART();
      return;
    }
    rpcart.clearFields();
    rpcskl.setDisab('N');
    rpcskl.setCSKL("");
  }
  
  public boolean Validacija() {
    if (rpcskl.getCSKL().length() == 0) {
      rpcskl.setCSKL("");
      JOptionPane.showConfirmDialog(getWindow(),"Obavezan unos skladišta !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (rpcart.getCART().length() == 0) {
      rpcart.setCART();
      JOptionPane.showConfirmDialog(getWindow(),"Obavezan unos artikla !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (!Aus.checkDateRange(jtfPocDatum, jtfZavDatum)) return false;
    return true; 
  }
  
  protected void upitCompleted() {
    if (ret != null) ret.show();
    ret = null;
  }

  public void afterOKPress() {
    rcc.EnabDisabAll(mainPanel, true);
  }

  public void okPress() {
    

  }
  
  public boolean isIspis() {
    return false;
  }

  public void ispis() {
    //
  }

  public boolean ispisNow() {
    return false;
  }

  public boolean runFirstESC() {
    return rpcskl.getCSKL().length()>0;
  }

}
