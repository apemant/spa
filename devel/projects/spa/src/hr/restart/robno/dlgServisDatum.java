package hr.restart.robno;

import hr.restart.baza.dM;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.util.OKpanel;
import hr.restart.util.raCommonClass;
import hr.restart.util.startFrame;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class dlgServisDatum extends JraDialog {
  raCommonClass rcc = raCommonClass.getraCommonClass();

  JPanel jp = new JPanel();
  XYLayout lay = new XYLayout();
  
  JLabel datdok = new JLabel();
  JLabel datumo = new JLabel();
  JLabel datumz = new JLabel();
  JraTextField jraDatDok = new JraTextField();
  JraTextField jraDatumo = new JraTextField();
  JraTextField jraDatumz = new JraTextField();
  
  
  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      CancelPress();
    }
  };

  private void init() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public dlgServisDatum(Frame frame, String title) {
    super(frame, title, true);
    init();
  }

  public dlgServisDatum(Dialog dlg, String title) {
    super(dlg, title, true);
    init();
  }

  public dlgServisDatum(String title) {
    this((Frame) null, title);
  }

  public dlgServisDatum() {
    this((Frame) null, "");
  }
  
  String[] dats = {"DATDOK", "DATUMO", "DATUMZ"};
  public void changeDatum(DataSet ds) {
    bind(ds);
    ok = false;
    String stat = ds.getString("STATUS");
    boolean obr = !stat.equalsIgnoreCase("P");
    boolean zat = stat.equalsIgnoreCase("Z");
    rcc.setLabelLaF(jraDatumo, obr);
    rcc.setLabelLaF(jraDatumz, zat);
    DataRow prev = new DataRow(ds, dats);
    dM.copyColumns(ds, prev, dats);
    addComponentListener(new ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        jraDatDok.requestFocusLater();
      }
    });
    super.show();
    if (ok) ds.saveChanges();
    else dM.copyColumns(prev, ds, dats);
  }
  
  
  
  boolean ok;
  private void OKPress() {
    ok = true;
    this.dispose();
  }

  private void CancelPress() {
    this.dispose();
  }
  
  public void bind(DataSet ds) {
    jraDatDok.setDataSet(ds);
    jraDatumo.setDataSet(ds);
    jraDatumz.setDataSet(ds);
  }
  
  private void jbInit() throws Exception {
    jp.setLayout(lay);
    lay.setWidth(270);
    lay.setHeight(110);
    
    datdok.setText("Datum otvaranja");
    datumo.setText("Datum obraèuna");
    datumz.setText("Datum zavaranja");
    jraDatDok.setColumnName("DATDOK");
    jraDatumo.setColumnName("DATUMO");
    jraDatumz.setColumnName("DATUMZ");
    
    jp.add(datdok, new XYConstraints(15, 20, -1, -1));
    jp.add(datumo, new XYConstraints(15, 45, -1, -1));
    jp.add(datumz, new XYConstraints(15, 70, -1, -1));
    jp.add(jraDatDok, new XYConstraints(150, 20, 100, -1));
    jp.add(jraDatumo, new XYConstraints(150, 45, 100, -1));
    jp.add(jraDatumz, new XYConstraints(150, 70, 100, -1));
    
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    getContentPane().add(jp, BorderLayout.CENTER);
    getContentPane().add(okp, BorderLayout.SOUTH);
    pack();

    okp.registerOKPanelKeys(this);
    startFrame.getStartFrame().centerFrame(this, 15, "Promjena datuma naloga");
  }
}
