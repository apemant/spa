package hr.restart.pos;

import hr.restart.baza.dM;
import hr.restart.robno.frmFormKPR;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raDateMask;
import hr.restart.swing.raInputDialog;
import hr.restart.util.Aus;
import hr.restart.util.ProcessInterruptException;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raLoader;
import hr.restart.util.raProcess;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class frmAllKpr {

  static String oldpj = "1";
  static StorageDataSet tds = new StorageDataSet();
  public static void show() {
    JPanel pan = new JPanel(new XYLayout(415, 75));
    tds = new StorageDataSet();
        tds.setColumns(new Column[] {
                dM.createTimestampColumn("DATDOK")
        });
        tds.open();
        tds.setTimestamp("DATDOK", Valid.getValid().getToday());
    JraTextField dat = new JraTextField();
    dat.setColumnName("DATDOK");
    dat.setDataSet(tds);
    raComboBox pj = new raComboBox() {
        public void this_itemStateChanged() {
            oldpj =  getDataValue();
        }
    };
    pj.setRaItems(new String[][] {
            {"Robna kuæa \"Vesna\"", "1"},
            {"Robna kuæa \"Tena\"", "2"},
            {"Robna kuæa \"Pierre\"", "3"},
            {"Robna kuæa \"Tena\" - higijena", "5"}
    });
    dat.setHorizontalAlignment(JLabel.CENTER);
    new raDateMask(dat);
    pan.add(new JLabel("Do datuma"), new XYConstraints(15,15,-1,-1));
    pan.add(dat, new XYConstraints(300, 15, 100, -1));
    pan.add(new JLabel("Prodajno mjesto"), new XYConstraints(15,40,-1,-1));
    pan.add(pj, new XYConstraints(190, 40, 210, -1));
    pj.setDataValue(oldpj);
    
    raInputDialog od = new raInputDialog();
    if (!od.show(null, pan, "Formiranje KPR")) return;
    
    final frmFormKPR fk = (frmFormKPR) raLoader.load("hr.restart.robno.frmFormKPR");
    fk.getTDS().setTimestamp("zavDatum", tds.getTimestamp("DATDOK")); 
    final DataSet skl = dM.getDataModule().getSklad();
    skl.open();
    for (skl.first(); skl.inBounds(); skl.next()) {
      if (!skl.getString("CSKL").startsWith(oldpj) ||
          skl.getString("CSKL").length() != 4) continue;
      raProcess.runChild(new Runnable() {
        public void run() {
          fk.getTDS().setString("CSKL", skl.getString("CSKL"));
          try {
            fk.okPress();
//          enableEvents(true);
          } catch (ProcessInterruptException re) {
            throw (ProcessInterruptException) re.fillInStackTrace();
          } catch (Exception ex) {
            raProcess.fail();
          }
        }
      });
      fk.externalSave();
    }
  }
}
