package hr.restart.robno;

import hr.restart.baza.Condition;
import hr.restart.baza.dM;
import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.JraSplitPane;
import hr.restart.swing.JraTextField;
import hr.restart.swing.KeyAction;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.Aus;
import hr.restart.util.lookupData;
import hr.restart.util.raFrame;
import hr.restart.util.raJPTableView;
import hr.restart.util.raMatPodaci;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;


public class dlgArtHelper extends raFrame {
  
  lookupData ld = lookupData.getlookupData();
  dM dm = dM.getDataModule();

  JPanel pan = new JPanel(new BorderLayout());
  JLabel lab = new JLabel();
  raJPTableView jptv = new raJPTableView(true) {
    public void mpTable_doubleClicked() {
    	if (jptv.getDataSet().rowCount() > 0 && jptv.getDataSet().getString("VRDOK").length() > 0)
    		Util.getUtil().showDocs(jptv, cart);
    }
    public void mpTable_killFocus(java.util.EventObject e) {};
  };
  
  
  JTabbedPane tpan = new JTabbedPane();
  raJPTableView jptvt1 = new raJPTableView(true) {
    public void mpTable_doubleClicked() {
        doubleClickIzlaz(jptvt1.getStorageDataSet());
      //Util.getUtil().showDocs(jptv2, cart);
    }
    public void mpTable_killFocus(java.util.EventObject e) {};
  };
  raJPTableView jptvt2 = new raJPTableView(true) {
    public void mpTable_doubleClicked() {
        doubleClickIzlaz(jptvt2.getStorageDataSet());
      //Util.getUtil().showDocs(jptv2, cart);
    }
    public void mpTable_killFocus(java.util.EventObject e) {};
  };
  
  /*JPanel pan2 = new JPanel(new BorderLayout());
  JLabel lab2 = new JLabel();
  raJPTableView jptv2 = new raJPTableView(true) {
    public void mpTable_doubleClicked() {
    	doubleClickIzlaz(jptv2.getStorageDataSet());
      //Util.getUtil().showDocs(jptv2, cart);
    }
    public void mpTable_killFocus(java.util.EventObject e) {};
  };*/
  
  
  
  JraSplitPane sp = new JraSplitPane(JSplitPane.VERTICAL_SPLIT);
  
  int cart;
  
  KeyAction actESC;
  JraTextField toFocus;
  raFrame thisOwner;
  
  public dlgArtHelper(raFrame owner, JraTextField focus) {
    super(raFrame.DIALOG, owner.getWindow());
    
    toFocus = focus;
    thisOwner = owner;
    
    lab.setText("Ulazi na skladištu");
    JPanel labp = new JPanel(new BorderLayout());
    labp.add(lab);
    labp.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
    pan.add(labp, BorderLayout.NORTH);
    pan.add(jptv);
    
    tpan.add("Izlazi partnera", jptvt1);
    tpan.add("Izlazi svih partnera", jptvt2);
    
    /*lab2.setText("Izlazi partnera");
    JPanel labp2 = new JPanel(new BorderLayout());
    labp2.add(lab2);
    labp2.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
    pan2.add(labp2, BorderLayout.NORTH);
    pan2.add(jptv2);*/
    
    sp.setResizeWeight(0.5);
    
    jptv.getColumnsBean().setSaveName(getClass().getName());
    jptvt1.getColumnsBean().setSaveName(getClass().getName()+"-1");
    jptvt2.getColumnsBean().setSaveName(getClass().getName()+"-2");
    
    jptvt2.addTableModifier(new raTableColumnModifier("CPAR", new String[] {"CPAR", "NAZPAR"}, dm.getPartneri()));
    this.getContentPane().add(pan);
    this.pack();
    getJdialog().addWindowListener(new WindowAdapter() {
      public void windowOpened(WindowEvent e) {
        toFocus.requestFocusLater();
      }
    });
  }
  
  public void doubleClickIzlaz(StorageDataSet ds) {
  	// za override
  }
  
  public void recalcMar() {
  	jptv.calc.set("fvc", ((raMatPodaci) thisOwner).getRaQueryDataSet().getBigDecimal("FVC"));
  	jptv.calc.set("kol", ((raMatPodaci) thisOwner).getRaQueryDataSet().getBigDecimal("KOL"));
    jptv.performAllRows("mar = fvc - NC;  PMAR = mar %% NC;  IMAR = mar * kol");
  }
  
  public void show(int cart, String cskl) {
    show(cart, cskl, -1);
  }
  
  public void show(int cart, String cskl, int cpar) {
/*    if (isShowing()) {
      jptv.getColumnsBean().saveSettings();
      hide();
    }
*/   

    
    this.cart = cart;
    Condition csklCond = cskl == null || cskl.length() == 0 ? Condition.none : Condition.equal("CSKL", cskl).qualified("doku");
    Condition acsklCond = cskl == null || cskl.length() == 0 ? Condition.none : Condition.equal("CSKLART", cskl).qualified("stdoku");
    Condition prkCond = Condition.equal("VRDOK", "PRK").qualified("doku");
    Condition kalCond = Condition.equal("VRDOK", "KAL").qualified("doku");
    Condition cartCond = Condition.equal("CART", cart).qualified("stdoku");
    StorageDataSet nabs = Aus.createSet("Doku.CSKL .VRDOK .GOD .BRDOK .DATDOK Stdoku.KOL {Stanje FLH}KOLFLH.3 .DC .PRAB .PZT .NC .PMAR .IMAR");
    Aus.q(nabs, "SELECT doku.cskl, doku.vrdok, doku.god, doku.brdok, doku.datdok, " +
    		" stdoku.kol, stdoku.kolflh, stdoku.dc, stdoku.prab, stdoku.pzt, stdoku.nc, stdoku.pmar " +
            " FROM doku INNER JOIN stdoku ON " + Aus.join("doku", "stdoku", Util.mkey) +
            " WHERE " +cartCond.and(prkCond.and(csklCond).or(kalCond.and(acsklCond))) + " ORDER BY doku.datdok");
    nabs.setTableName("doku_stdoku");    
    nabs.last();
    
    QueryDataSet st = Aus.q("SELECT * FROM stanje WHERE " + 
    		csklCond.and(Condition.equal("GOD", Aut.getAut().getKnjigodRobno())).and(cartCond).qualified("stanje"));
    if (st.rowCount() > 0) {
    	nabs.insertRow(false);
    	dM.copyColumns(st, nabs, new String[] {"CSKL", "GOD", "KOL", "NC"});
    	nabs.post();
    	nabs.last();
    }
    
    if (!ld.raLocate(dm.getArtikli(), "CART", Integer.toString(cart))) setTitle("Artikl " + cart); 
    else setTitle("Artikl " + Aut.getAut().getCARTdependable(dm.getArtikli()) + " - " + dm.getArtikli().getString("NAZART"));
    
    if (cskl == null || cskl.length() == 0 ) lab.setText("Ulazi na skladištu");
    else lab.setText("Ulazi na skladištu " + cskl);
    
    if (cpar < 0) {
     
      if (getContentPane().isAncestorOf(sp)) {
        getContentPane().removeAll();
        sp.removeAll();
        getContentPane().add(pan);
      }
      

      jptv.setKumTak(true);
      jptv.setDataSet(null);
      jptv.setStoZbrojiti(new String[] {});
      jptv.setKumTak(false);
      jptv.setDataSet(nabs);
      
      jptv.setVisibleCols(new int[] {4,10});
      jptv.getColumnsBean().initialize();
    } else {
      csklCond.qualified("doki");
      acsklCond.qualified("stdoki");
      cartCond.qualified("stdoki");
      Condition cparCond = Condition.equal("CPAR", cpar);
      Condition ojCond = Condition.in("VRDOK", "RAC PRD").or(Condition.equal("VRDOK", "PON").and(Condition.equal("PARAM", "OJ"))).qualified("doki");
      Condition sklCond = Condition.equal("VRDOK", "ROT").or(Condition.equal("VRDOK", "PON").and(Condition.diff("PARAM", "OJ"))).qualified("doki");
      StorageDataSet outs = Aus.createSet("doki.CSKL .VRDOK .GOD .BRDOK .DATDOK stdoki.KOL .FC .UPRAB .FVC .FMC");
      Aus.q(outs, "SELECT doki.cskl, doki.vrdok, doki.god, doki.brdok, doki.datdok, " +
          " stdoki.kol, stdoki.fc, stdoki.uprab, stdoki.fvc, stdoki.fmc " +
          " FROM doki INNER JOIN stdoki ON " + Aus.join("doki", "stdoki", Util.mkey) +
          " WHERE " +cartCond.and(cparCond).and(sklCond.and(csklCond).or(ojCond.and(acsklCond))));
      outs.setTableName("doki_stdoki");

      outs.setSort(new SortDescriptor(new String[] {"DATDOK"}));
      outs.last();
      
      StorageDataSet outsa = Aus.createSet("doki.CSKL .VRDOK .GOD .CPAR .BRDOK .DATDOK stdoki.KOL .FC .UPRAB .FVC .FMC");
      Aus.q(outsa, "SELECT doki.cskl, doki.vrdok, doki.god, doki.cpar, doki.brdok, doki.datdok, " +
          " stdoki.kol, stdoki.fc, stdoki.uprab, stdoki.fvc, stdoki.fmc " +
          " FROM doki INNER JOIN stdoki ON " + Aus.join("doki", "stdoki", Util.mkey) +
          " WHERE " +cartCond.and(sklCond.and(csklCond).or(ojCond.and(acsklCond))));
      outsa.setTableName("doki_stdoki");

      outsa.setSort(new SortDescriptor(new String[] {"DATDOK"}));
      outsa.last();
      
      tpan.setTitleAt(0, "Izlazi partnera " + cpar);
      //lab2.setText("Izlazi partnera " + cpar);
            
      if (!getContentPane().isAncestorOf(sp)) {
        getContentPane().remove(pan);
        sp.remove(pan);
        //sp.remove(pan2);
        sp.remove(tpan);
        
        sp.setTopComponent(pan);
        //sp.setBottomComponent(pan2);
        sp.setBottomComponent(tpan);
        getContentPane().add(sp);
      }
      
      jptv.setKumTak(true);
      jptv.setDataSet(null);
      jptv.setStoZbrojiti(new String[] {});
      jptv.setKumTak(false);
      jptv.setDataSet(nabs);
      
      jptv.setVisibleCols(new int[] {4,10});
      jptv.getColumnsBean().initialize();
      recalcMar();
      
      /*jptv2.setKumTak(true);
      jptv2.setDataSet(null);
      jptv2.setStoZbrojiti(new String[] {});
      jptv2.setKumTak(false);
      jptv2.setDataSet(outs);
      
      jptv2.setVisibleCols(new int[] {4,8});
      jptv2.getColumnsBean().initialize();*/
      
      jptvt1.setKumTak(true);
      jptvt1.setDataSet(null);
      jptvt1.setStoZbrojiti(new String[] {});
      jptvt1.setKumTak(false);
      jptvt1.setDataSet(outs);
      
      jptvt1.setVisibleCols(new int[] {4,8});
      jptvt1.getColumnsBean().initialize();
      
      jptvt2.setKumTak(true);
      jptvt2.setDataSet(null);
      jptvt2.setStoZbrojiti(new String[] {});
      jptvt2.setKumTak(false);
      jptvt2.setDataSet(outsa);
      
      jptvt2.setVisibleCols(new int[] {3,5,9});
      jptvt2.getColumnsBean().initialize();
      
    }
    if (!isShowing()) {
      pack();
      show();
      AWTKeyboard.registerKeyStroke(getWindow(), AWTKeyboard.ESC, actESC = new KeyAction() {
        public boolean actionPerformed() {
          if (isShowing()) hide();   
            return true;
          }
      });
    }
  }
  
  public void clear() {
    jptv.setDataSet(null);
    repaint();
  }
  
  public void hide() {
    super.hide();
    //jptv.setDataSet(null);
    AWTKeyboard.unregisterKeyStroke(getWindow(), AWTKeyboard.ESC, actESC);
  }

}
