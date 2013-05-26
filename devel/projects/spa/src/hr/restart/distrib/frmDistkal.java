package hr.restart.distrib;

import java.awt.Color;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;

import hr.restart.baza.Distkal;
import hr.restart.baza.StDistkal;
import hr.restart.baza.dM;
import hr.restart.robno.raDateUtil;
import hr.restart.robno.rdUtil;
import hr.restart.swing.JraTable2;
import hr.restart.swing.raTableModifier;
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

  raTableModifier danica = new raTableModifier() {
    
    public void modify() {
      Variant v = new Variant();
      ((JraTable2)getTable()).getDataSet().getVariant("DATISP",getRow(),v); //vrijednost iz dataseta u tom trenutku moze se dobiti jedino na ovaj nacin
      JComponent jRenderComp = (JComponent)renderComponent;
      Calendar cal = Calendar.getInstance();
      cal.setTimeInMillis(v.getTimestamp().getTime());
      int dow = cal.get(Calendar.DAY_OF_WEEK);
      int idxDan = dow == 1?8:dow;
      setComponentText(oDan[idxDan]+" "+raDateUtil.getraDateUtil().dataFormatter(v.getTimestamp()));
      if (isSelected()) {
        if (dow == Calendar.SATURDAY) {
          jRenderComp.setBackground(hr.restart.swing.raColors.green);
          jRenderComp.setForeground(Color.black);
        } else if (dow == Calendar.SUNDAY) {
          jRenderComp.setBackground(hr.restart.swing.raColors.red);
          jRenderComp.setForeground(Color.black);
        } else {
          jRenderComp.setBackground(getTable().getSelectionBackground());
          jRenderComp.setForeground(getTable().getSelectionForeground());
        }
      } else {
        if (dow == Calendar.SATURDAY) {
          jRenderComp.setForeground(Color.green.darker().darker());
        } else if (dow == Calendar.SUNDAY) {
          jRenderComp.setForeground(Color.red);
        } else {
          jRenderComp.setForeground(getTable().getForeground());
        }
      }      
    }
    
    public boolean doModify() {
      return ((JraTable2)getTable()).getDataSetColumn(getColumn()).getColumnName().equalsIgnoreCase("DATISP");
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
    initAA();
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
    raDetail.getJpTableView().addTableModifier(danica);
  }
  String[] oSvaki = new String[] {
      "svaki",
      "svaki drugi",
      "svaki prvi u mjesecu",
      "svaki drugi u mjesecu",
      "svaki treæi u mjesecu",
      "svaki èetvrti u mjesecu"
      };
  String[] oDan = {"radni dan","dan","ponedjeljak","utorak","srijeda","èetvrtak","petak","subota","nedjelja"};
  public void autoAdd(int svaki, int dan,
      int flag, long dod, long ddo, int broj) {
    getDetailSet().open();
    getDetailSet().deleteAllRows();
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(dod);
    int _br = broj;
    while (cal.getTimeInMillis() <= ddo) {
      if (isDateInRange(cal, svaki, dan)) {
        getDetailSet().insertRow(false);
        getDetailSet().setInt("CDISTKAL", getMasterSet().getInt("CDISTKAL"));
        getDetailSet().setTimestamp("DATISP", new Timestamp(cal.getTimeInMillis()));
        getDetailSet().setString("FLAGADD", flag==0?"D":"N");
        getDetailSet().setInt("BROJ", _br);
        getDetailSet().post();
        _br++;
      }
      cal.add(Calendar.DATE, 1);
    }
    getDetailSet().saveChanges();
    JOptionPane.showMessageDialog(jpMaster.getTopLevelAncestor(), "Datumi uspješno generirani!");
  }
  
  private boolean isDateInRange(Calendar cal, int svaki, int dan) {
    switch (svaki) {
    case 0: //svaki
      return isSvaki(cal, dan);

    case 1: 
      return isSvakiDrugi(cal, dan);
      
    case 2: 
      return isSvakiPrviUM(cal, dan);
      
    case 3: 
      return isSvakiDrugiUM(cal, dan);
      
    case 4: 
      return isSvakiTreciUM(cal, dan);

    case 5: 
      return isSvakiCetvrtiUM(cal, dan);

    default:
      break;
    }
    return false;
  }

  private boolean isSvakiCetvrtiUM(Calendar cal, int dan) {
    // TODO Auto-generated method stub
    return false;
  }

  private boolean isSvakiTreciUM(Calendar cal, int dan) {
    // TODO Auto-generated method stub
    return false;
  }

  private boolean isSvakiDrugiUM(Calendar cal, int dan) {
    // TODO Auto-generated method stub
    return false;
  }

  private boolean isSvakiPrviUM(Calendar cal, int dan) {
    // TODO Auto-generated method stub
    return false;
  }

  private boolean isSvakiDrugi(Calendar cal, int dan) {
    // TODO Auto-generated method stub
    return false;
  }

  private boolean isSvaki(Calendar cal, int dan) {
    int dow = cal.get(Calendar.DAY_OF_WEEK);
    switch (dan) {
    case 0: //svaki radni dan
      return !((dow == Calendar.SATURDAY) || (dow == Calendar.SUNDAY));
      
    case 1: //svaki dan
      return true;
      
    case 2: 
      return dow == Calendar.MONDAY;
      
    case 3: 
      return dow == Calendar.TUESDAY;

    case 4: 
      return dow == Calendar.WEDNESDAY;

    case 5: 
      return dow == Calendar.THURSDAY;
      
    case 6: 
      return dow == Calendar.FRIDAY;
      
    case 7: 
      return dow == Calendar.SATURDAY;

    case 8: 
      return dow == Calendar.SUNDAY;
      

    default:
      break;
    }
    return false;
  }
  
  private StorageDataSet _aaset;
  public StorageDataSet getAaSet() {
    if (_aaset == null) {
      _aaset = new StorageDataSet();
      _aaset.addColumn(dM.createIntColumn("BROJ"));
      _aaset.addColumn(dM.createTimestampColumn("DATUMFROM"));
      _aaset.addColumn(dM.createTimestampColumn("DATUMTO"));
    }
    return _aaset;
  }
  public void initAA() {
    getAaSet().open();
    getAaSet().deleteAllRows();
    getAaSet().insertRow(false);
    raDetail.addOption(navRECALC, 4, false);
  }
}
