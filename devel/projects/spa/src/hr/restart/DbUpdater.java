package hr.restart;

import javax.swing.JOptionPane;

import com.borland.dx.sql.dataset.QueryDataSet;

import hr.restart.baza.ConsoleCreator;
import hr.restart.baza.Verinfo;
import hr.restart.util.Util;
import hr.restart.util.raDbaseCreator;
import hr.restart.util.raProcess;

public class DbUpdater {
  public DbUpdater() {
    Util.redirectSystemOut();
    int answ = JOptionPane.showConfirmDialog(null, "Ažuriranti bazu?", "Pitanje", JOptionPane.YES_NO_OPTION);
    if (answ != 0) {// nije da
      return;
    }
    azuriraj();
    JOptionPane.showMessageDialog(null, "Ažuriranje baze završeno","Info",JOptionPane.INFORMATION_MESSAGE);
  }

  private void azuriraj() {
    raProcess.runChild("Proces","Ažuriranje baze u tijeku",new Runnable() {
      public void run() {
        ConsoleCreator.updateEverything();
        raProcess.setMessage("Azuriranje database verzije", true);
        updateDBVersion();
      }
    });
  }

  public static void updateDBVersion() {
    QueryDataSet vi = Verinfo.getDataModule().getQueryDataSet();
    vi.open(); vi.first();
    vi.setString("azversion", raSplashWorker.verMF);
    vi.saveChanges();
  }
}
