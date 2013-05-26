package hr.restart.distrib;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.sql.dataset.QueryDataSet;

import hr.restart.baza.Condition;
import hr.restart.baza.Distkal;
import hr.restart.baza.StDistkal;
import hr.restart.baza.dM;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raNavAction;


public class frmDistkal extends raMasterDetail {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  jpDistkalMaster jpMaster;
  jpDistkalDetail jpDetail;
  
  
  raNavAction navRECALC = new raNavAction("Reklakulacija brojeva",raImages.IMGMOVIE,KeyEvent.VK_F7) {
    public void actionPerformed(ActionEvent e) {
    	if (JOptionPane.showConfirmDialog(raDetail.getWindow(), "Rekalkulirati brojeve iza ovog datuma?",
    			"Rekalkulacija", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
    		recalc();
    }
  };


  public frmDistkal() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void SetFokusMaster(char mode) {
    jpMaster.initAA();
    if (mode == 'N') {
//      rcc.setLabelLaF(jpMaster.jraCdistkal, true);
      jpMaster.jraCdistkal.requestFocus();
      rcc.setLabelLaF(jpMaster.jpAutoAdd, false);
    } else if (mode == 'I') {
      rcc.setLabelLaF(jpMaster.jraCdistkal, false);
      jpMaster.jraOpis.requestFocus();
      /**@todo: Postaviti fokus za izmjenu sloga mastera */
    }
  }

  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jpMaster.jraCdistkal) || vl.isEmpty(jpMaster.jraOpis))
      return false;
    return true;
  }

  public void SetFokusDetail(char mode) {
    if (mode == 'N') {
      jpDetail.jraDatisp.requestFocus();
      jpDetail.jcbFlagadd.setSelectedIndex(0);
    } else if (mode == 'I') {
      jpDetail.jraBroj.requestFocus();
      jpDetail.jcbFlagadd.setSelectedIndex(getDetailSet().getString("FLAGADD").equals("D")?0:1);
    } else {
      jpDetail.jcbFlagadd.setSelectedIndex(getDetailSet().getString("FLAGADD").equals("D")?0:1);
    }
  }
  
  void recalc() {
  	Timestamp chg = Util.getUtil().getLastSecondOfDay(getDetailSet().getTimestamp("DATISP"));
  	
  	QueryDataSet ds = StDistkal.getDataModule().getTempSet(Condition.equal("CDISTKAL", getMasterSet()));
  	ds.open();
  	ds.setSort(new SortDescriptor(new String[] {"DATISP"}));
  	
  	int broj = 0;
  	for (ds.first(); ds.inBounds(); ds.next()) {
  		if (ds.getTimestamp("DATISP").before(chg)) broj = ds.getInt("BROJ");
  		else ds.setInt("BROJ", ++broj);
  	}
  	ds.saveChanges();
  	getDetailSet().refresh();
    getDetailSet().last();
  }

  public boolean ValidacijaDetail(char mode) {
    getDetailSet().setString("FLAGADD",jpDetail.jcbFlagadd.getSelectedIndex()==0?"D":"N");
    if (vl.isEmpty(jpDetail.jraDatisp) || vl.isEmpty(jpDetail.jraBroj))
      return false;
    if (mode == 'N' && notUnique()) /**@todo: Provjeriti jedinstvenost kljuca detaila */
      return false;
    return true;
  }

  public boolean notUnique() {
    return false;
//    return vl.notUnique(jpDetail.jraDatisp); //ne radi sa datumom
  }

  private void jbInit() throws Exception {
    this.setMasterSet(Distkal.getDataModule().getQueryDataSet());
    this.setNaslovMaster("Distribucijski kalendar"); /**@todo: Naslov mastera */
    this.setVisibleColsMaster(new int[] {1, 2, 3});
    this.setMasterKey(new String[] {"CDISTKAL"});
    this.setMasterDeleteMode(DELDETAIL);
    jpMaster = new jpDistkalMaster(this);
    this.setJPanelMaster(jpMaster);

    this.setDetailSet(StDistkal.getDataModule().getQueryDataSet());
    this.setNaslovDetail("Stavke distribucijskog kalendara"); /**@todo: Naslov detaila */
    this.setVisibleColsDetail(new int[] {2, 3});
    this.setDetailKey(new String[] {"CDISTKAL", "DATISP"});
    jpDetail = new jpDistkalDetail(this);
    this.setJPanelDetail(jpDetail);
    raDetail.addOption(navRECALC, 4, false);
  }
}
