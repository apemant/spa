package hr.restart.help;

import javax.swing.JPanel;

import com.borland.jbcl.layout.XYLayout;

import hr.restart.baza.Mesg;
import hr.restart.baza.dM;
import hr.restart.util.raMatPodaci;


public class frmMessages extends raMatPodaci {

  dM dm;
  JPanel jp = new JPanel();
  XYLayout lay = new XYLayout();
  
  
  public frmMessages() {
    super(2);
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void SetFokus(char mode) {
    // TODO Auto-generated method stub

  }

  public boolean Validacija(char mode) {
    // TODO Auto-generated method stub
    return false;
  }

  private void jbInit() throws Exception {

    dm = dM.getDataModule();

    setRaDetailPanel(jp);

    setRaQueryDataSet(Mesg.getDataModule().getFilteredDataSet("1=0"));

    setVisibleCols(new int[] {0,1,2,3,4});
  }
}
