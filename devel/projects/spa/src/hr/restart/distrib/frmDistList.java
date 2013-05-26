package hr.restart.distrib;

import java.sql.Timestamp;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

import hr.restart.baza.Condition;
import hr.restart.baza.Distart;
import hr.restart.baza.Distkal;
import hr.restart.baza.Distlist;
import hr.restart.baza.StDistkal;
import hr.restart.baza.StDistlist;
import hr.restart.baza.dM;
import hr.restart.robno.SanityCheck;
import hr.restart.robno.Util;
import hr.restart.sisfun.raUser;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;
import hr.restart.util.raProcess;

public class frmDistList extends raMasterDetail {
	raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();
  
  jpDistListMaster jpMaster;
  jpDistListDetail jpDetail;

  public frmDistList() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  public boolean isNewDetailNeeded() {
    return false;
 }
  
  public void SetFokusMaster(char mode) {
  	if (mode == 'N') {
  		getPreSelect().copySelValues();
  		getMasterSet().setTimestamp("DATUM", vl.getPresToday(getPreSelect().getSelRow(), "DATUM"));
  	}
  	if (getMasterSet().getString("SIFDIST").length() == 0)
  		jpMaster.jlrDist.requestFocus();
  	else jpMaster.jtfDATUM.requestFocus();
  }

  public boolean ValidacijaMaster(char mode) {
    if (vl.isEmpty(jpMaster.jrfCSKL) || vl.isEmpty(jpMaster.jlrDist) || vl.isEmpty(jpMaster.jtfDATUM))
      return false;
    
    if (mode == 'N') 
      if (JOptionPane.showConfirmDialog(raMaster.getWindow(), "Kreirati distribucijsku listu za distributera " +
    		getMasterSet().getString("SIFDIST") + " za dan " + Aus.formatTimestamp(getMasterSet().getTimestamp("DATUM")) + "?",
    				"Kreiranje distribucijske liste", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return false;
    return true;
  }
  
  public boolean DeleteCheckMaster() {
  	seq = "DIST-" + getMasterSet().getString("GOD") + "-" + getMasterSet().getString("CSKL");
  	brdok = getMasterSet().getInt("BRDOK");
  	return Util.getUtil().checkSeq(seq, Integer.toString(getMasterSet().getInt("BRDOK")));
  }
  
  public boolean doBeforeSaveMaster(char mode) {
		if (mode == 'N') {
			cskl = getMasterSet().getString("CSKL");
			dat = getMasterSet().getTimestamp("DATUM");
			god = vl.findYear(dat);
			brdok = vl.findSeqInt("DIST-" + god + "-" + cskl);
			dist = getMasterSet().getString("SIFDIST");
			
			getMasterSet().setString("CUSER", raUser.getInstance().getUser());
			getMasterSet().setTimestamp("SYSDAT", vl.getToday());
			getMasterSet().setString("GOD", god);
			getMasterSet().setInt("BRDOK", brdok);
		}
		return true;
	}
  
  public boolean doWithSaveMaster(char mode) {
  	if (mode == 'B')
  	try {
  		Util.getUtil().delSeqCheck(seq, true, brdok); // / transakcija
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
  }
  
  
  public void AfterAfterSaveMaster(char mode) {
  	if (mode == 'N') {
  		raProcess.runChild(raMaster.getWindow(), new Runnable() {
				public void run() {
					generateDist();
				}
			});
  	}
  	super.AfterAfterSaveMaster(mode);
  }

  public void SetFokusDetail(char mode) {
    if (mode == 'N') {
    } else if (mode == 'I') {
    } else {
    }
  }

  public boolean ValidacijaDetail(char mode) {
    return true;
  }

  Timestamp dat;
  String cskl, god, dist, seq;
  int brdok;
  void generateDist() {
  	DataSet ds = Distart.getDataModule().getTempSet(Condition.equal("SIFDIST", dist).and(Condition.equal("AKTIV", "D")));
  	ds.open();
  	
  	QueryDataSet st = StDistlist.getDataModule().getTempSet();
  	st.open();
  	
  	DataSet kal = StDistkal.getDataModule().getTempSet(Condition.between("DATISP", dat, dat));
  	kal.open();
  	
  	DataSet zk = Distkal.getDataModule().getTempSet();
  	zk.open();
  	
  	String[] cc = {"CDISTART", "CPAR", "NAZIV", "MJ", "ADR", "PBR", "TEL", "TELFAX", "EMADR", "KOL"};
  	
  	short rbr = 0;
  	for (ds.first(); ds.inBounds(); ds.next()) {
  		int ckal = ds.getInt("CDISTKAL");
  		boolean skip = false;
  		while (!skip) {
  			skip = ld.raLocate(kal, new String[] {"CDISTKAL", "FLAGADD"}, new String[] {Integer.toString(ckal), "N"});
  			if (!skip) {
  				if (ld.raLocate(kal, new String[] {"CDISTKAL", "FLAGADD"}, new String[] {Integer.toString(ckal), "D"})) break;
  				if (!ld.raLocate(zk, "CDISTKAL", Integer.toString(ckal))) skip = true;
  				else if (zk.getInt("CINHERITDISTKAL") > 0 && zk.getInt("CINHERITDISTKAL") != ckal)
  					ckal = zk.getInt("CINHERITDISTKAL");
  				else skip = true;
  			}
  		}
  		if (!skip) {
  			st.insertRow(false);
  			st.setString("CSKL", cskl);
  			st.setString("GOD", god);
  			st.setInt("BRDOK", brdok);
  			st.setShort("RBR", ++rbr);
  			st.setString("SIFDIST", dist);
  			dM.copyColumns(ds, st, cc);
  		}
  	}
  	
  	st.saveChanges();
  	refilterDetailSet();
  }
  
  private void jbInit() throws Exception {
    this.setMasterSet(Distlist.getDataModule().getQueryDataSet());
    this.setNaslovMaster("Distribucijske liste");
    this.setVisibleColsMaster(new int[] {3, 4, 5});
    this.setMasterKey(new String[] {"CSKL", "GOD", "BRDOK"});
    jpMaster = new jpDistListMaster(this);
    this.setJPanelMaster(jpMaster);
    setMasterDeleteMode(DELDETAIL);

    this.setDetailSet(StDistlist.getDataModule().getQueryDataSet());
    this.setNaslovDetail("Stavke distribucijske liste"); /**@todo: Naslov detaila */
    this.setVisibleColsDetail(new int[] {3, 6, 8, 9, 7, 14});
    this.setDetailKey(new String[] {"CSKL", "GOD", "BRDOK", "RBR"});
    jpDetail = new jpDistListDetail(this);
    this.setJPanelDetail(jpDetail);
  }
}
