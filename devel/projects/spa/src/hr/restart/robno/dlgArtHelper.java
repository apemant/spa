package hr.restart.robno;

import hr.restart.baza.Condition;
import hr.restart.baza.Doku;
import hr.restart.baza.Stdoku;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.JraSplitPane;
import hr.restart.swing.JraTextField;
import hr.restart.swing.KeyAction;
import hr.restart.util.Aus;
import hr.restart.util.lookupData;
import hr.restart.util.raFrame;
import hr.restart.util.raJPTableView;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.sql.dataset.QueryDataSet;


public class dlgArtHelper extends raFrame {
  
  lookupData ld = lookupData.getlookupData();
  dM dm = dM.getDataModule();

  JPanel pan = new JPanel(new BorderLayout());
  JLabel lab = new JLabel();
  raJPTableView jptv = new raJPTableView(true) {
    public void mpTable_doubleClicked() {
      Util.getUtil().showDocs(jptv, cart);
    }
    public void mpTable_killFocus(java.util.EventObject e) {};
  };
  
  JPanel pan2 = new JPanel(new BorderLayout());
  JLabel lab2 = new JLabel();
  raJPTableView jptv2 = new raJPTableView(true) {
    public void mpTable_doubleClicked() {
      Util.getUtil().showDocs(jptv2, cart);
    }
    public void mpTable_killFocus(java.util.EventObject e) {};
  };
  
  JraSplitPane sp = new JraSplitPane(JSplitPane.VERTICAL_SPLIT);
  
  int cart;
  
  KeyAction actESC;
  JraTextField toFocus;
  
  public dlgArtHelper(raFrame owner, JraTextField focus) {
    super(raFrame.DIALOG, owner.getWindow());
    
    toFocus = focus;
    
    lab.setText("Ulazi na skladištu");
    JPanel labp = new JPanel(new BorderLayout());
    labp.add(lab);
    labp.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
    pan.add(labp, BorderLayout.NORTH);
    pan.add(jptv);
    
    lab2.setText("Izlazi partnera");
    JPanel labp2 = new JPanel(new BorderLayout());
    labp2.add(lab2);
    labp2.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
    pan2.add(labp2, BorderLayout.NORTH);
    pan2.add(jptv2);
    
    sp.setResizeWeight(0.5);
    
    jptv.getColumnsBean().setSaveName(getClass().getName());
    jptv2.getColumnsBean().setSaveName(getClass().getName());
    this.getContentPane().add(pan);
    this.pack();
    getJdialog().addWindowListener(new WindowAdapter() {
      public void windowOpened(WindowEvent e) {
        toFocus.requestFocusLater();
      }
    });
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
    QueryDataSet nabs = Aus.q("SELECT doku.cskl, doku.vrdok, doku.god, doku.brdok, doku.datdok, " +
    		" stdoku.kol, stdoku.kolflh, stdoku.dc, stdoku.prab, stdoku.pzt, stdoku.nc " +
            " FROM doku INNER JOIN stdoku ON " + Aus.join("doku", "stdoku", Util.mkey) +
            " WHERE " +cartCond.and(prkCond.and(csklCond).or(kalCond.and(acsklCond))));
    nabs.setTableName("doku_stdoku");
    Aus.setupColumn(nabs.getColumn("CSKL"), Doku.getDataModule());
    Aus.setupColumn(nabs.getColumn("VRDOK"), Doku.getDataModule());
    Aus.setupColumn(nabs.getColumn("GOD"), Doku.getDataModule());
    Aus.setupColumn(nabs.getColumn("BRDOK"), Doku.getDataModule());
    Aus.setupColumn(nabs.getColumn("DATDOK"), Doku.getDataModule());
    Aus.setupColumn(nabs.getColumn("KOL"), Stdoku.getDataModule());
    Aus.setupColumn(nabs.getColumn("KOLFLH"), Stdoku.getDataModule().getColumn("KOL"));
    nabs.getColumn("KOLFLH").setCaption("Stanje FLH");
    Aus.setupColumn(nabs.getColumn("DC"), Stdoku.getDataModule());
    Aus.setupColumn(nabs.getColumn("PRAB"), Stdoku.getDataModule());
    Aus.setupColumn(nabs.getColumn("PZT"), Stdoku.getDataModule());
    Aus.setupColumn(nabs.getColumn("NC"), Stdoku.getDataModule());
    
    nabs.setSort(new SortDescriptor(new String[] {"DATDOK"}));
    nabs.last();
    
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
      QueryDataSet outs = Aus.q("SELECT doki.cskl, doki.vrdok, doki.god, doki.brdok, doki.datdok, " +
          " stdoki.kol, stdoki.fc, stdoki.uprab, stdoki.fvc, stdoki.fmc " +
          " FROM doki INNER JOIN stdoki ON " + Aus.join("doki", "stdoki", Util.mkey) +
          " WHERE " +cartCond.and(cparCond).and(sklCond.and(csklCond).or(ojCond.and(acsklCond))));
      outs.setTableName("doki_stdoki");
      Aus.setupColumn(outs.getColumn("CSKL"), doki.getDataModule());
      Aus.setupColumn(outs.getColumn("VRDOK"), doki.getDataModule());
      Aus.setupColumn(outs.getColumn("GOD"), doki.getDataModule());
      Aus.setupColumn(outs.getColumn("BRDOK"), doki.getDataModule());
      Aus.setupColumn(outs.getColumn("DATDOK"), doki.getDataModule());
      Aus.setupColumn(outs.getColumn("KOL"), stdoki.getDataModule());
      Aus.setupColumn(outs.getColumn("FC"), stdoki.getDataModule());
      Aus.setupColumn(outs.getColumn("UPRAB"), stdoki.getDataModule());
      Aus.setupColumn(outs.getColumn("FVC"), stdoki.getDataModule());
      Aus.setupColumn(outs.getColumn("FMC"), stdoki.getDataModule());
      
      outs.setSort(new SortDescriptor(new String[] {"DATDOK"}));
      outs.last();
      
      lab2.setText("Izlazi partnera " + cpar);
            
      if (!getContentPane().isAncestorOf(sp)) {
        getContentPane().remove(pan);
        sp.remove(pan);
        sp.remove(pan2);
        
        sp.setTopComponent(pan);
        sp.setBottomComponent(pan2);
        getContentPane().add(sp);
      }
      
      jptv.setKumTak(true);
      jptv.setDataSet(null);
      jptv.setStoZbrojiti(new String[] {});
      jptv.setKumTak(false);
      jptv.setDataSet(nabs);
      
      jptv.setVisibleCols(new int[] {4,10});
      jptv.getColumnsBean().initialize();
      
      jptv2.setKumTak(true);
      jptv2.setDataSet(null);
      jptv2.setStoZbrojiti(new String[] {});
      jptv2.setKumTak(false);
      jptv2.setDataSet(outs);
      
      jptv2.setVisibleCols(new int[] {4,8});
      jptv2.getColumnsBean().initialize();
      
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
