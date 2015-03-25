package hr.restart.robno;

import java.awt.Point;

import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.sql.dataset.QueryDataSet;

import hr.restart.baza.Artikli;
import hr.restart.baza.Condition;
import hr.restart.baza.Doku;
import hr.restart.baza.Stdoku;
import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.KeyAction;
import hr.restart.util.Aus;
import hr.restart.util.lookupData;
import hr.restart.util.raFrame;
import hr.restart.util.raJPTableView;


public class dlgArtHelper extends raFrame {

  raJPTableView jptv = new raJPTableView(true) {

    public void mpTable_killFocus(java.util.EventObject e) {
      
    }

    public void mpTable_doubleClicked() {
      Util.getUtil().showDocs(jptv, cart);
    }    
  };
  
  int cart;
  
  KeyAction actESC;
  
  public dlgArtHelper(raFrame owner) {
    super(raFrame.DIALOG, owner.getWindow());
    
    jptv.getColumnsBean().setSaveName(getClass().getName());    
    this.getContentPane().add(jptv);
    this.pack();
  }
  
  public void show(int cart, String cskl) {
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
    
    QueryDataSet ds = Artikli.getDataModule().openTempSet(Condition.equal("CART", cart));
    if (ds.rowCount() == 0) setTitle("Ulazi artikla " + cart); 
    else setTitle("Ulazi artikla " + Aut.getAut().getCARTdependable(ds) + " - " + ds.getString("NAZART"));
    
    jptv.setKumTak(true);
    jptv.setDataSet(null);
    jptv.setStoZbrojiti(new String[] {});
    jptv.setKumTak(false);
    jptv.setDataSet(nabs);
    
    
      jptv.setVisibleCols(new int[] {4,10});
      jptv.getColumnsBean().initialize();
/*    Point cbpl = jptv.getNavBar().getColBean().getPreferredLocationOnScreen();
    if (cbpl != null) {
      pack();
      getWindow().setLocation(cbpl);
    }
*/  
    if (!isShowing()) {
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
