/*
 * Created on 2005.03.11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.gk;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import hr.restart.baza.dM;
import hr.restart.sk.raSaldaKonti;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.util.OKpanel;
import hr.restart.util.raCommonClass;
import hr.restart.zapod.jpGetValute;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


/**
 * @author abf
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class dlgIzvodValuta extends JPanel {
  JDialog win;
  JPanel pan = new JPanel();
  jpGetValute jpv = new jpGetValute();
  JraTextField jraIznos = new JraTextField();
  JraTextField jraIznosVal = new JraTextField();
  JLabel jlIznos = new JLabel();
  JLabel jlIznosVal = new JLabel();
  XYLayout lay = new XYLayout();
  
  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      CancelPress();
    }
  };
  
  boolean ok;

  StorageDataSet data = new StorageDataSet();
  
  public dlgIzvodValuta() {
    try {
      init();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private void init() throws Exception {
    data.setColumns(new Column[] {
       dM.createStringColumn("OZNVAL", 3),
       dM.createBigDecimalColumn("TECAJ", 6),
       dM.createBigDecimalColumn("IZNOS", 2),
       dM.createBigDecimalColumn("IZNOSVAL", 2)
    });
    data.open();
    
    pan.setLayout(lay);
    lay.setWidth(640);
    lay.setHeight(80);
    
    jpv.setTecajVisible(true);
    jpv.setTecajEditable(true);
    jpv.setAlwaysSelected(true);
    jpv.setRaDataSet(data);
    jraIznos.setDataSet(data);
    jraIznos.setColumnName("IZNOS");
    jraIznosVal.setDataSet(data);
    jraIznos.setColumnName("IZNOSVAL");
    
    jlIznos.setText("Iznos");
    jlIznosVal.setText("Iznos u valuti");
    raCommonClass.getraCommonClass().setLabelLaF(jraIznos, false);
    
    pan.add(jpv, new XYConstraints(0, 0, -1, -1));
    pan.add(jlIznos, new XYConstraints(15, 50, -1, -1));
    pan.add(jlIznosVal, new XYConstraints(410, 50, -1, -1));
    pan.add(jraIznos, new XYConstraints(150, 50, 100, -1));
    pan.add(jraIznosVal, new XYConstraints(505, 50, 100, -1));
    
    setLayout(new BorderLayout());
    add(pan);
    add(okp, BorderLayout.SOUTH);
  }
  
  public void show(Container parent, DataSet bind, String idip) {
    Container realparent = null;
    String title = "Devizna uplata";

    if (parent instanceof JComponent)
      realparent = ((JComponent) parent).getTopLevelAncestor();
    else if (parent instanceof Window)
      realparent = parent;

    if (realparent instanceof Dialog)
      win = new JraDialog((Dialog) realparent, title, true);
    else if (realparent instanceof Frame)
      win = new JraDialog((Frame) realparent, title, true);
    else win = new JraDialog((Frame) null, title, true);
    
    win.setContentPane(this);
    win.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    win.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        CancelPress();
      }
    });
    win.pack();
    win.setLocationRelativeTo(realparent);
    okp.registerOKPanelKeys(win);
    data.setString("OZNVAL", bind.getString("OZNVAL"));
    data.setBigDecimal("TECAJ", bind.getBigDecimal("TECAJ"));
    data.setBigDecimal("IZNOS", bind.getBigDecimal(idip));
    data.setBigDecimal("IZNOSVAL", bind.getBigDecimal("DEV" + idip));
    jpv.initJP('I');
    jpv.disableDohvat();
    win.show();
    if (ok) {
      bind.setString("OZNVAL", data.getString("OZNVAL"));
      bind.setBigDecimal("TECAJ", data.getBigDecimal("TECAJ"));      
      bind.setBigDecimal("DEV" + idip, data.getBigDecimal("IZNOSVAL"));
      if (bind.getString("OZNVAL").length() == 0) {
        bind.setBigDecimal("TECAJ", raSaldaKonti.n0);      
        bind.setBigDecimal("DEVID", raSaldaKonti.n0);
        bind.setBigDecimal("DEVIP", raSaldaKonti.n0);
      }
    }
  }
  
  private void OKPress() {
    ok = true;
    kill();
  }

  private void CancelPress() {
    ok = false;
    kill();
  }
  
  private void kill() {
    jpv.disableDohvat();
    if (win != null) {
      win.dispose();
      win = null;
    }
  }
}
