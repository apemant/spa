package hr.restart.pl;

import java.awt.Dimension;

import com.borland.dx.sql.dataset.QueryDataSet;

import hr.restart.baza.Condition;
import hr.restart.baza.Radplsifre;
import hr.restart.sk.frmPDV2;
import hr.restart.swing.JraDialog;
import hr.restart.util.startFrame;

public class DlgRadplSifre {

  protected DlgRadplSifre() {
    // TODO Auto-generated constructor stub
  }
  public static void showDialog(String cr) {
    JraDialog dlg = getDialog(cr);
    dlg.setMinimumSize(new Dimension(600, 150));
    startFrame.getStartFrame().centerFrame(dlg, 0, "Šifre za JOPPD");
    dlg.show();
  }
  public static JraDialog getDialog(String cr) {
    final QueryDataSet set = Radplsifre.getDataModule().getFilteredDataSet(Condition.equal("CRADNIK", cr));
    set.open();
    if (set.rowCount() == 0) {
      set.insertRow(false);
      set.setString("CRADNIK", cr);
    } else {
      set.first();
    }
    return frmPDV2.getInstance().getJOPPD().getDialogForSet(set, false, new Runnable() {
      
      public void run() {
        //ok
        set.saveChanges();
      }
    }, new Runnable() {
      
      public void run() {
        
        //cancel
      }
    });
  }
}
