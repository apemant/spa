package hr.restart.pos;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.DataSet;
import com.sun.glass.events.KeyEvent;
import com.sun.glass.ui.InvokeLaterDispatcher;

import hr.restart.baza.Artikli;
import hr.restart.baza.Condition;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraTextField;
import hr.restart.swing.XYPanel;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;


public class frmArtikliPonuda extends raMatPodaci {
  dM dm = dM.getDataModule();
  XYPanel pan;
  String cgrart;
  
  raNavAction rnvDelAll = new raNavAction("Poništavanje", raImages.IMGDELALL, KeyEvent.VK_F8) {
    public void actionPerformed(ActionEvent e) {
       delAll();
    }
  };
  
  public frmArtikliPonuda() {
    super(2);
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private void jbInit() throws Exception {
    cgrart = frmParam.getParam("pos", "grupaPonuda", "13", "Šifra grupe artikala za dnevnu ponudu");
    
    setRaQueryDataSet(Artikli.getDataModule().getTempSet(Condition.equal("CGRART", cgrart)));
    
    getRaQueryDataSet().getColumn("CPAR").setCaption("Šifra");
    
    pan = new XYPanel(getRaQueryDataSet()) {
      protected void changed(JraTextField tf) {
        updateNaz(tf);
      }
    };
    pan.label("Artikl", 100).nav("CPAR:CART NAZART", dm.getArtikli(), new int[] {0,3,4}, false).nl();
    pan.label("Opis", 100).text("NAZPRI", 455).nl().expand();
    
    dm.getArtikli().open();
    
    setRaDetailPanel(pan);
    setVisibleCols(new int[] {12,3,41});
    
    addOption(rnvDelAll, 4, false);
    
  }
  
  void updateNaz(JraTextField tf) {
    if (tf == null || !tf.getColumnName().equals("CPAR") || 
        getMode() == 'B' || getRaQueryDataSet().getString("NAZPRI").length() > 0) return;
    
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        if (lookupData.getlookupData().raLocate(dm.getArtikli(), "CART", Integer.toString(getRaQueryDataSet().getInt("CPAR"))))
          getRaQueryDataSet().setString("NAZPRI", dm.getArtikli().getString("NAZART"));
      }
    });    
  }


  public void SetFokus(char mode) {
    if (mode == 'N') {
      pan.getNav("CPAR").emptyTextFields();
      pan.getNav("CPAR").requestFocusLater();
    } else pan.getText("NAZPRI").requestFocusLater();
  }

  public boolean Validacija(char mode) {
    if (Valid.getValid().isEmpty(pan.getNav("CPAR"))) return false;
    if (Valid.getValid().isEmpty(pan.getText("NAZPRI"))) return false;
    
    if (mode == 'N') {
      Valid.getValid().execSQL("SELECT MAX(cart) AS maxc FROM artikli");
      Valid.getValid().RezSet.open();
      int maxc = Valid.getValid().RezSet.getInt("MAXC") + 1;
      Valid.getValid().RezSet.close();
      Valid.getValid().RezSet = null;
      
      DataSet old = Artikli.getDataModule().openTempSet(Condition.equal("CART", getRaQueryDataSet(), "CPAR"));
      dM.copyColumns(old, getRaQueryDataSet(), new String[] {"NAZART", "JM", "CPOR", "VC", "MC", "VRART", "TIPART", "CAN", "SORTKOL"});
      getRaQueryDataSet().setInt("CART",  maxc);
      getRaQueryDataSet().setString("CART1", Integer.toString(maxc));
      getRaQueryDataSet().setString("BC", Integer.toString(maxc));
      getRaQueryDataSet().setString("CGRART", cgrart);
    }
    
    return true;
  }
  
  void delAll() {
    if (getRaQueryDataSet().rowCount() == 0) return;
    
    if (JOptionPane.showConfirmDialog(this.getWindow(), "Želite li poništiti postojeæe artikle?", 
        "Poništavanje", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        
    Valid.getValid().runSQL("DELETE FROM artikli WHERE " + Condition.equal("CGRART", cgrart));
    getRaQueryDataSet().refresh();
    getJpTableView().fireTableDataChanged();
    jeprazno();
  }
}
