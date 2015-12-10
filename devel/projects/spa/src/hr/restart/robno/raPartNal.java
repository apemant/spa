package hr.restart.robno;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import hr.restart.baza.Partneri;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitFat;


public class raPartNal extends raUpitFat {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  private Valid vl = Valid.getValid();
  lookupData ld = lookupData.getlookupData();
  private TableDataSet tds = new TableDataSet();
  JPanel jp = new JPanel();
  XYLayout myXyLayout = new XYLayout();
  
  private JraTextField jraGod = new JraTextField();
  
  private static raPartNal inst;

  public raPartNal() {
    try {
      init();
      inst = this;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static raPartNal getInstance() {
    return inst;
  }
  
  public com.borland.dx.dataset.DataSet getQds() {
    return getJPTV().getMpTable().getDataSet();
  }
  
  private void init() throws Exception {
    tds.setColumns(new Column[] {
        dm.createStringColumn("GOD", "Godina", 4)
      }
    );
    
    tds.open();
    
    jp.setLayout(myXyLayout);

    jraGod.setColumnName("GOD");
    jraGod.setDataSet(tds);
    
    myXyLayout.setWidth(640);
    myXyLayout.setHeight(45);
    
    jp.add(new JLabel("Poèetna godina za popis kupaca "),  new XYConstraints(15, 12, 250, -1));
    jp.add(jraGod,  new XYConstraints(300, 10, 75, -1));
    
    this.setJPan(jp);
   
    tds.setString("GOD", vl.findYear());

  }
  
  public void componentShow() {
    removeNav();
    setDataSet(null);
    jraGod.requestFocusLater();
  }


  public String navDoubleClickActionName() {
    return null;
  }

  public int[] navVisibleColumns() {
    return new int[] {0,1,2,3,4,5};
  }

  public void firstESC() {
    this.getJPTV().clearDataSet();
    removeNav();
    rcc.EnabDisabAll(jp, true);
  }
  
  public void okPress() {
    StorageDataSet ds = Partneri.getDataModule().getTempSet("CPAR NAZPAR OIB ADR PBR MJ", "EXISTS " +
    		"(SELECT * FROM doki WHERE doki.cpar = partneri.cpar and doki.god >= '" + tds.getString("GOD") + "' AND doki.vrdok='ROT')");
    ds.open();
    setDataSet(ds);
    addReport("hr.restart.robno.repPartNal", "hr.restart.robno.repPartNal",
        "PartneriNap", "Nalijepnice za partnere 3x8");
  }

  public boolean runFirstESC() {
    return this.getJPTV().getDataSet() != null;
  }
}
