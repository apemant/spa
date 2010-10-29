package hr.restart.help;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JEditorPane;

import com.borland.dx.dataset.NavigationEvent;
import com.borland.jbcl.layout.XYLayout;

import hr.restart.baza.Mesg;
import hr.restart.baza.dM;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraScrollPane;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;
import hr.restart.util.raNavBar;


public class frmMessages extends raMatPodaci {

	static frmMessages instance;
	
  XYLayout lay = new XYLayout();
  
  JraScrollPane vp = new JraScrollPane();
  
  JEditorPane msg = new JEditorPane() {
    public boolean getScrollableTracksViewportWidth() {
      return true;
    }
 };
 
 	raNavAction rnvRead = new raNavAction("Oznaèi", raImages.IMGDELETE, KeyEvent.VK_F3) {
 	  public void actionPerformed(ActionEvent e) {
 	    mark();
 	  }
 	};
 	raNavAction rnvReadAll = new raNavAction("Oznaèi sve", raImages.IMGDELALL, KeyEvent.VK_F3, KeyEvent.SHIFT_MASK) {
 	   public void actionPerformed(ActionEvent e) {
 	     markAll();
 	   }
 	};
  
  
  public frmMessages() {
    super(2);
    try {
    	instance = this;
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static frmMessages getInstance() {
  	return instance;
  }
  
  public void SetFokus(char mode) {
    // TODO Auto-generated method stub

  }

  public boolean Validacija(char mode) {
    // TODO Auto-generated method stub
    return false;
  }
  
  public void switchPanel(boolean prvi,boolean drugi){
  	super.switchPanel(prvi, true);
  }
  
  public boolean isNormal() {
  	return false;
  }
  
  void mark() {
  	if (getRaQueryDataSet().rowCount() == 0) return;
  	if (!getRaQueryDataSet().getString("NOVA").equals("D")) return;
  	
  	getRaQueryDataSet().setString("NOVA", "N");
  	getRaQueryDataSet().saveChanges();
  	getRaQueryDataSet().emptyRow();
  	getJpTableView().fireTableDataChanged();
  	MsgDispatcher.refresh();
  	jeprazno();
  }
  
  void markAll() {
  	if (getRaQueryDataSet().rowCount() == 0) return;
  	Valid.getValid().runSQL("UPDATE mesg SET nova='N' WHERE dest='" + raUser.getInstance().getUser() + "' AND nova='D'");
  	getRaQueryDataSet().refresh();
  	getJpTableView().fireTableDataChanged();
  	MsgDispatcher.refresh();
  	jeprazno();
  }
  
  public void beforeShow() {
  	Aus.setFilter(getRaQueryDataSet(), "SELECT * FROM mesg WHERE dest='" + raUser.getInstance().getUser() + "' AND nova='D'");
  	getRaQueryDataSet().open();
  	setSort(new String[] {"DATUM"});
  }
  
  public void raQueryDataSet_navigated(NavigationEvent e) {
  	if (getRaQueryDataSet().rowCount() == 0) msg.setText("");
  	else msg.setText(getRaQueryDataSet().getString("MTEXT"));
  }

  private void jbInit() throws Exception {

    msg.setEditable(false);

    setRaQueryDataSet(Mesg.getDataModule().getFilteredDataSet("1=0"));
    getRaQueryDataSet().getColumn("DATUM").setDisplayMask("dd-MM-yyyy  'u' hh:mm:ss");
    getRaQueryDataSet().getColumn("DATUM").setWidth(20);
    
    removeRnvCopyCurr();
    getNavBar().removeStandardOptions(new int[] {raNavBar.ACTION_ADD, raNavBar.ACTION_DELETE, 
    		raNavBar.ACTION_UPDATE, raNavBar.ACTION_PRINT, raNavBar.ACTION_TOGGLE_TABLE});
    
    addOption(rnvRead, 0, true);
    addOption(rnvReadAll, 1, true);

    setVisibleCols(new int[] {0,1,2,3,4});
    vp.setPreferredSize(new Dimension(600, 250));
    vp.setViewportView(msg);
    jpDetailView.add(vp);
  }
}
