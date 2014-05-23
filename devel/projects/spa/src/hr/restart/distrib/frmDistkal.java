package hr.restart.distrib;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashSet;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

import hr.restart.baza.Condition;
import hr.restart.baza.Distkal;
import hr.restart.baza.StDistkal;
import hr.restart.baza.dM;
import hr.restart.robno.raDateUtil;
import hr.restart.robno.rdUtil;
import hr.restart.swing.JraTable2;
import hr.restart.swing.raTableModifier;
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
  
  
  raNavAction navRECALC = new raNavAction("Rekalkulacija brojeva",raImages.IMGMOVIE,KeyEvent.VK_F7) {
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

  boolean generate = false;
  public void AfterAfterSaveMaster(char mode) {
    if (generate) try {
      jpMaster.autoAdd();
    } finally {
      generate = false;
    }
    super.AfterAfterSaveMaster(mode);
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
  
  public boolean isNewDetailNeeded() {
    return false;
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
  String[] oDan = {"dan","radni dan","ponedjeljak","utorak","srijeda","èetvrtak","petak","subota","nedjelja"};
  public void autoAdd(int svaki, int dan,
      int flag, Timestamp from, Timestamp to, int broj) {
    
    QueryDataSet old = StDistkal.getDataModule().getTempSet(Condition.equal("CDISTKAL", getMasterSet()));
    old.open();
    
    HashSet dats = new HashSet();
    for (old.first(); old.inBounds(); old.next()) {
        dats.add(old.getTimestamp("DATISP").toString().substring(0, 10));
    }
    
    
    from = Util.getUtil().getFirstSecondOfDay(from);
    to = Util.getUtil().getLastSecondOfDay(to);
    
    Calendar cal = Calendar.getInstance();
    cal.setTime(from);
    
    boolean even = false;
    for (cal.setTime(from); !cal.getTime().after(to); cal.set(cal.DATE, cal.get(cal.DATE) + 1)) {   
        
        int dw = cal.get(cal.DAY_OF_WEEK);
        int md = cal.get(cal.DAY_OF_MONTH);
        
        if (svaki == 0 || svaki == 1) {
            if (dan == 1 && (dw == cal.SUNDAY || dw == cal.SATURDAY)) continue;
            if (dan == 2 && dw != cal.MONDAY ||
                dan == 3 && dw != cal.TUESDAY ||
                dan == 4 && dw != cal.WEDNESDAY ||
                dan == 5 && dw != cal.THURSDAY ||
                dan == 6 && dw != cal.FRIDAY ||
                dan == 7 && dw != cal.SATURDAY ||
                dan == 8 && dw != cal.SUNDAY) continue;
            even = !even;
            if (svaki == 1 && !even) continue;
        } else if (svaki == 2) {
            if (md != 1) continue;
        } else if (svaki == 3) {
            if (md != 2) continue;
        } else if (svaki == 4) {
            if (md != 3) continue;
        } else if (svaki == 5) {
            if (md != 4) continue;
        } 
        
        Timestamp dat = new Timestamp(cal.getTime().getTime());
        if (dats.contains(dat.toString().substring(0, 10))) continue;
        
        old.insertRow(false);
        old.setInt("CDISTKAL", getMasterSet().getInt("CDISTKAL"));
        old.setTimestamp("DATISP", dat);
        old.setString("FLAGADD", flag == 0 ? "D" : "N");
    }
    
    int pocbr = broj;
    old.setSort(new SortDescriptor(new String[] {"DATISP"}));
    for (old.first(); old.inBounds(); old.next()) {
        old.setInt("BROJ", broj++);
    }
    
    old.saveChanges();
    getDetailSet().refresh();
    getDetailSet().last();
    JOptionPane.showMessageDialog(raMaster.getWindow(), "Dodani brojevi od " + pocbr + ". do " + (broj-1) + ".",
            "Generiranje gotovo", JOptionPane.INFORMATION_MESSAGE);

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
