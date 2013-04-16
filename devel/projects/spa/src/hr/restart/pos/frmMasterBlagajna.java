/****license*****************************************************************
**   file: frmMasterBlagajna.java
**   Copyright 2006 Rest Art
**
**   Licensed under the Apache License, Version 2.0 (the "License");
**   you may not use this file except in compliance with the License.
**   You may obtain a copy of the License at
**
**       http://www.apache.org/licenses/LICENSE-2.0
**
**   Unless required by applicable law or agreed to in writing, software
**   distributed under the License is distributed on an "AS IS" BASIS,
**   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
**   See the License for the specific language governing permissions and
**   limitations under the License.
**
****************************************************************************/
package hr.restart.pos;

import hr.apis_it.fin._2012.types.f73.PdvType;
import hr.apis_it.fin._2012.types.f73.PorezType;
import hr.apis_it.fin._2012.types.f73.RacunType;
import hr.apis_it.fin._2012.types.f73.RacunZahtjev;
import hr.restart.baza.Artikli;
import hr.restart.baza.Condition;
import hr.restart.baza.Pos;
import hr.restart.baza.Rate;
import hr.restart.baza.Refresher;
import hr.restart.baza.Stanje;
import hr.restart.baza.Stpos;
import hr.restart.baza.dM;
import hr.restart.baza.stdoki;
import hr.restart.robno.Aut;
import hr.restart.robno.Rbr;
import hr.restart.robno.Util;
import hr.restart.robno._Main;
import hr.restart.robno.allStanje;
import hr.restart.robno.dlgKupac;
import hr.restart.robno.frmPlacanje;
import hr.restart.robno.presPOS;
import hr.restart.robno.raVart;
import hr.restart.robno.repFISBIH;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.sisfun.raUser;
import hr.restart.sk.dlgSplitAmount;
import hr.restart.swing.JraTable2;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raColors;
import hr.restart.swing.raExtendedTable;
import hr.restart.swing.raOptionDialog;
import hr.restart.swing.raSelectTableModifier;
import hr.restart.swing.raStatusColorModifier;
import hr.restart.swing.raTableModifier;
import hr.restart.swing.raTextMask;
import hr.restart.util.*;
import hr.restart.zapod.OrgStr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.DataSetException;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class frmMasterBlagajna extends raMasterDetail {
  	java.math.BigDecimal kolNaStanju, kolRez;
	public static boolean allReadyRun=false;
	hr.restart.util.sysoutTEST st = new hr.restart.util.sysoutTEST(false);
  hr.restart.util.reports.mxReport rpos;
  short stavka;
  java.math.BigDecimal tmpIZNOS,tmpUKUPNO,tmpPOP;
  boolean newRacun=false;
  boolean delRacun=false;
  boolean isUsluga;
  boolean justExit;
  boolean makeNext=false;
  boolean allowEdit=true;
  static DataSet alterMaster, alterDetail;
  
  QueryDataSet qdsStPos = new QueryDataSet();
  QueryDataSet qdsRate= new QueryDataSet();
  StorageDataSet olds;
  BigDecimal pov;
  String cporez;
  int brojchek, brdoknew;
  short delStavka;      // redni broj stavke koja se briše
  Rbr rbr = Rbr.getRbr();
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  lookupData ld = lookupData.getlookupData();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();
  raNavAction navBEFEXIT = new raNavAction("Naplata",raImages.IMGEXPORT2,KeyEvent.VK_F7) {
    public void actionPerformed(ActionEvent e) {
    	if (allowEdit || raUser.getInstance().isSuper() || presBlag.isSuper)
    		pressF7('B');
    }
  };
  raNavAction navPOPUST = new raNavAction("Popust",raImages.IMGPREFERENCES,KeyEvent.VK_F12) {
    public void actionPerformed(ActionEvent e) {
      pressF12('B');
    }
  };
  raNavAction navKUPAC = new raNavAction("Unos kupca",raImages.IMGTIP,KeyEvent.VK_F11) {
    public void actionPerformed(ActionEvent e) {
      pressF11('B');
    }
  };
  
  raNavAction navREM = new raNavAction("Izbaci",raImages.IMGSENDMAIL,KeyEvent.VK_F3,KeyEvent.SHIFT_MASK) {
    public void actionPerformed(ActionEvent e) {
      transfer();
    }
  };
  
  raNavAction navSPLIT = new raNavAction("Razdijeli stavku",raImages.IMGPAUSE,KeyEvent.VK_F2,KeyEvent.SHIFT_MASK) {
    public void actionPerformed(ActionEvent e) {
      split();
    }
  };
  
  raNavAction navEXIT = new raNavAction("Spremanje raèuna",raImages.IMGHISTORY,KeyEvent.VK_F10) {
    public void actionPerformed(ActionEvent e) {
      pressF10('B');
    }
  };
  
  raNavAction navPRINT = new raNavAction("Ispis",raImages.IMGPAUSE,KeyEvent.VK_F5,KeyEvent.SHIFT_MASK) {
    public void actionPerformed(ActionEvent e) {
      racDest = hr.restart.sisfun.frmParam.getParam(
          "sisfun", "printerRMcmndAlt", "", "Radno mjesto (alt)", true);
      pressF5('B');
    }
  };
  
  raNavAction navZatvori = new raNavAction("Zatvori raèun", raImages.IMGDOWN, KeyEvent.VK_F8) {
    public void actionPerformed(ActionEvent e) {
      closeRac();
    }
  };

  raNavAction navPonisti = new raNavAction("Arhiviraj raèun", raImages.IMGSENDMAIL, KeyEvent.VK_F7) {
    public void actionPerformed(ActionEvent e) {
      arhRac();
    }
  };

  
  raOptionDialog dlgStol = new raOptionDialog();
  JPanel jpStol = new JPanel();
  JraTextField jraStol = new JraTextField() {
    public boolean maskCheck() {
      return true;
    }
  };
  
  raStatusColorModifier msc = new raStatusColorModifier("AKTIV", "D",
      raColors.green, Color.green.darker().darker());
  
  public static raTableModifier fsc = new raTableModifier() {
    Variant shared = new Variant();

    Color fcol = Color.green.darker().darker().darker();
    Color fbcol = Color.green;
    
    Color fncol = Color.red.darker().darker();
    Color fnbcol = Color.red;
    
    boolean succ = false;
    
    public boolean doModify() {
      if (getTable() instanceof JraTable2) {
        JraTable2 tab = (JraTable2) getTable();
        if (tab.getDataSet().getRowCount() > 0 && 
            tab.getDataSet().hasColumn("FOK") != null) {
          tab.getDataSet().getVariant("FOK", this.getRow(), shared);
          if (!shared.getString().equals("D")) return false;
          tab.getDataSet().getVariant("JIR", this.getRow(), shared);
          succ = shared.getString().length() > 0;
          return true;
        }
      }
      return false;
    }
    
    public void modify() {
      JComponent jRenderComp = (JComponent) renderComponent;
      if (isSelected()) {
        jRenderComp.setBackground(succ ? fbcol : fnbcol);
        jRenderComp.setForeground(Color.black);
      } else {
        //jRenderComp.setBackground(getTable().getBackground());
        jRenderComp.setForeground(succ ? fcol : fncol);
      }
    }
  };
  
  
  public static final String[] key = {"cskl","vrdok","god","brdok","cprodmj"};

  static frmMasterBlagajna frm;
  
  frmTableDataView viewReq = new frmTableDataView(false, false, true);
  frmTableDataView viewPlac = new frmTableDataView(false, false, true);
  frmTableDataView viewArt = new frmTableDataView(false, false, true);
  
  raExtendedTable totalTab = new raExtendedTable();
  StorageDataSet totalSet = new StorageDataSet();

  JPanel jpSelect = new JPanel();
  Column IZNOS = new Column();
  XYLayout xYLayout3 = new XYLayout();
  JLabel jLabel5 = new JLabel();
  dlgKupac dlgkup = null;
  jpDetBlagajna jpBl = new jpDetBlagajna() {
    public void afterCART() {
      if (getDetailSet().getString("BC").trim().equals("") && getDetailSet().getString("CART1").trim().equals("") && getDetailSet().getInt("CART")==0) 
      	return;
      if (raDetail.getMode()=='I') return;
      if (jpBl.jrfBC.isLastLookSuccessfull() || jpBl.jrfCART.isLastLookSuccessfull() || jpBl.jrfCART1.isLastLookSuccessfull()) {
      	System.out.println("findOtherThings");
      	findOtherThings();
      }
      else {
      	System.out.println("Malo san puka");
      	return;
      }
      if (isUsluga) {/// && jpBl.gotFocus) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            jrfNAZART.requestFocus();
          }
        });
      }
    }
    public void pressESC() {
      if (ValDPEscapeDetail(raDetail.getMode()))
        raDetail.getOKpanel().jPrekid_actionPerformed();
//      raDetail.rnvAdd_action();
    }
    public void pressENTER() {
    	System.out.println("pressEnter");
    	calcIZNOS(1);
    	raDetail.getOKpanel().jBOK_actionPerformed();
    }
    public void pressENTERPOP() {
    	System.out.println("pressEnterPop");
    	//calcIZNOS(1);
      raDetail.getOKpanel().requestFocus();
      this.rcc.setLabelLaF(jpBl.jtfPOP, false);
    }
    public void pressF8(String field) {
      String value = null; 
      if (field.equalsIgnoreCase("CART"))
        value = jrfCART.getText();
      else if (field.equalsIgnoreCase("CART1"))
        value = jrfCART1.getText();
      else if (field.equalsIgnoreCase("BC"))
        value = jrfBC.getText();
      else if (field.equalsIgnoreCase("NAZART"))
        value = jrfNAZART.getText();
      else return;
      
      String godina = getMasterSet().getString("GOD");
      // LOL, koji maloumni hack :) 
      /*if (frmParam.getParam("robno", "ugoart", "N", 
          "Samo artikli ugostiteljstva na POS-u (D,N)").equalsIgnoreCase("D"))
        godina = godina + "' AND artikli.vrart='A";*/
      
      String[] ret = Util.getUtil().showStanje(raDetail.getWindow(),
          getMasterSet().getString("CSKL"), godina, field, value);
      
      if (ret != null) {
        if (jpBl.tCartSifparam.equals("CART")) {
          jrfCART.setText(ret[1]);
          jrfCART.forceFocLost();
        } else if (jpBl.tCartSifparam.equals("CART1")) {
          jrfCART1.setText(ret[2]);
          jrfCART1.forceFocLost();
        } else if (jpBl.tCartSifparam.equals("BC")) {
          jrfBC.setText(ret[3]);
          jrfBC.forceFocLost();
        }
      }
    }
    public void focLost() {
      calcIZNOS(1);
    }
    public void focLostPOP() {
      frmMasterBlagajna.this.rcc.setLabelLaF(jpBl.jtfPOP, false);//
      globalPopust();
      //raDetail.rnvAdd_action();
    }
  };

  public frmMasterBlagajna() {
    super(1,3);
    try {
      frm = this;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static frmMasterBlagajna getInstance() {
    return frm;
  }

  String cskl, god, rm, cblag, stol;
  int cpar;
  boolean autonext;

  public void beforeShowMaster() {
  	cskl = getPreSelect().getSelRow().getString("CSKL");
    god = vl.findYear(getPreSelect().getSelRow().getTimestamp("DATDOK-to"));
    rm = getPreSelect().getSelRow().getString("CPRODMJ");
//    cblag="1";
    cblag = presBlag.getBlagajnik();
    String knjig = OrgStr.getKNJCORG(false);
    String cps = frmParam.getParam("zapod", "knjigCpar"+knjig, "",
        "Šifra partnera koji predstavlja knjigovodstvo "+knjig);
    cpar = Aus.getNumber(cps);
    
    autonext = frmParam.getParam("pos", "autonext", "D",
        "Kreirati novi pos raèun automatski (D,N)").equals("D");
    
    pov = Aus.getDecNumber(frmParam.getParam("robno", "iznosPov", "0.5",
        "Iznos povratne naknade"));
    
    
    
    if (raUser.getInstance().isSuper() && "D".equalsIgnoreCase(
        frmParam.getParam("pos", "vrijemeRac", "N",
        "Prikazati vrijeme na racunima POS (D,N)"))) { 
      getMasterSet().getColumn("DATDOK").setDisplayMask("dd-MM-yyyy  'u' HH:mm:ss");
      getMasterSet().getColumn("DATDOK").setWidth(24);
    } else { 
      getMasterSet().getColumn("DATDOK").setDisplayMask("dd-MM-yyyy");
      getMasterSet().getColumn("DATDOK").setWidth(12);
    }
    
    if (presBlag.stolovi) {
      getMasterSet().last();
      if (presBlag.stol == null || presBlag.stol.length() == 0)
        setNaslovMaster("Blagajna - svi raèuni");
      else setNaslovMaster("Blagajna - raèuni stola " + presBlag.stol);
      setNaslovDetail("Stavke raèuna (stol " + presBlag.stol + ")");
    
      String va = frmParam.getParam("pos", "csklVrart"+cskl, "",
          "Vrsta artikla na POS-u za skladište "+cskl);
      if (va.length() > 0) {
        Artikli.getDataModule().setFilter(
            (QueryDataSet) jpBl.jrfCART.getRaDataSet(),
            Condition.equal("VRART", va));
      } else
        Artikli.getDataModule().setFilter(
            (QueryDataSet) jpBl.jrfCART.getRaDataSet(),
            Condition.equal("KASA", "D"));
    }
    if (presBlag.isSkladOriented()) {
      updatePlac();
      setNaslovMaster("Blagajna - " + 
          ((presBlag) getPreSelect()).jrfNAZSKL.getText());
    }
    dm.getArtikli().open();
  }
  
  public boolean ValDPEscapeDetail(char mode) {
    /*if (getMasterSet().getBigDecimal("NETO").doubleValue()==0) {
      if (newRacun) {
        getDetailSet().refresh();
      }
    }*/
    if (mode == 'N' && jpBl.curMode == 'I') {
      DataSet empty = Stpos.getDataModule().getReadonlySet();
      empty.open();
      empty.insertRow(false);
      dM.copyColumns(empty, getDetailSet());
      SetFokusDetail('N');
      return false;
    }
//    return super.ValDPEscapeDetail(mode);
    return true;

  }
  
  public void AfterCancelDetail() {
  	System.out.println("getDetailSet().getInt: "+getDetailSet().getInt("CART"));
  	if (getDetailSet().getStatus()==com.borland.dx.dataset.RowStatus.INSERTED && getDetailSet().getInt("CART")==0) {

  		getDetailSet().deleteRow();

  	 }

  }
  
  public boolean checkAccess() {
    if (!getMasterSet().getString("STATUS").equals("N")) {
        setUserCheckMsg(
                "Korisnik ne može promijeniti dokument jer je prenesen u ili iz druge baze !",
                false);
        return false;
    }
    restoreUserCheckMessage();
    return true;
  }
  
  public boolean checkAddEnabled() {
    Timestamp upto = getPreSelect().getSelRow().getTimestamp("DATDOK-to");
    if (ut.getFirstSecondOfDay(vl.getToday()).after(upto)) {
      if (!vl.findYear(upto).equals(god)) {
        JOptionPane.showMessageDialog(raMaster.getWindow(),
            "Pogrešna godina na predselekciji !", "Greška",
            javax.swing.JOptionPane.ERROR_MESSAGE);
        return false;
      }
      int response = JOptionPane.showConfirmDialog(raMaster.getWindow(),
          "Napraviti raèun s datumom " + Aus.formatTimestamp(upto) + " ?",
          "Datum izvan perioda", JOptionPane.OK_CANCEL_OPTION);
      if (response != JOptionPane.OK_OPTION) return false;
    }
    
    if (presBlag.stolovi) {
      stol = presBlag.stol;
      if (stol == null || stol.length() == 0) {
        if (!dlgStol.show(raMaster.getWindow(), jpStol, "Odabir stola"))
          return false;
        stol = jraStol.getText();
        if (stol.length() == 0) return false;
      }
    }
    return true;
  }

  public void SetFokusMaster(char mode) {
    if (mode == 'N') {
      getMasterSet().setString("CSKL", cskl);
      getMasterSet().setString("GOD", god);
      getMasterSet().setString("VRDOK", "GRC");
      getMasterSet().setString("CPRODMJ", rm);
      getMasterSet().setString("CBLAGAJNIK", cblag);
      getMasterSet().setTimestamp("DATDOK", vl.getToday());
      Timestamp upto = getPreSelect().getSelRow().getTimestamp("DATDOK-to");
      if (ut.getFirstSecondOfDay(vl.getToday()).after(upto))
        getMasterSet().setTimestamp("DATDOK", upto);
      
      if (presBlag.stolovi) {
        getMasterSet().setString("STOL", stol);
      }
      getMasterSet().setTimestamp("SYSDAT",vl.findDate(true,0));

      onetimeSetNew = true;
      newRacun = true;
      raMaster.getOKpanel().jBOK_actionPerformed();
    }
    else if (mode=='I') {
      newRacun=false;
      this.jBStavke_actionPerformed(null);
    }
  }
  
  
  boolean onetimeSetNew = false;
  
  public void jBStavke_actionPerformed(ActionEvent e) {
    if (onetimeSetNew) {
      onetimeSetNew = false;
      newRacun = true;
      brdoknew = getMasterSet().getInt("BRDOK");
    } else newRacun = false;
    System.out.println("stavke performed");
    super.jBStavke_actionPerformed(e);
  }

  /*public void AfterDeleteMaster() {
    util.delSeq(delstr,false);
    dm.getRate().open();
    qdsStPos.close();
    qdsRate.close();
    qdsStPos.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from stpos order by BRDOK,RBR"));
    qdsRate.setQuery(new QueryDescriptor(dm.getDatabase1(),"select * from rate order by BRDOK,RBR"));

    qdsStPos.setMetaDataUpdate(MetaDataUpdate.TABLENAME+MetaDataUpdate.PRECISION+MetaDataUpdate.SCALE+MetaDataUpdate.SEARCHABLE);
    qdsRate.setMetaDataUpdate(MetaDataUpdate.TABLENAME+MetaDataUpdate.PRECISION+MetaDataUpdate.SCALE+MetaDataUpdate.SEARCHABLE);

    qdsStPos.setColumns(dm.getStpos().cloneColumns());
    qdsRate.setColumns(dm.getRate().cloneColumns());
    qdsStPos.open();
    qdsRate.open();

    vl.recountDataSet(dm.getPos(), "BRDOK", brojchek, false);
    vl.recountSortedDataSet(qdsRate, "BRDOK", brojchek, false);
    vl.recountSortedDataSet(qdsStPos, "BRDOK", brojchek, false);
    raTransaction.saveChangesInTransaction(new QueryDataSet[] {dm.getPos(),qdsRate,qdsStPos,dm.getSeq()});
    getDetailSet().refresh();
  }*/
  public void AfterDeleteDetail() {
    stavka--;
    try {
//    raTransaction.runSQL("delete from rate where brdok='"+brojchek+"'");
    } catch (java.lang.Exception e) {
      e.printStackTrace();
    }
    hr.restart.util.Valid.getValid().recountDataSet(getDetailSet(), "RBR", delStavka, false);
    getMasterSet().setBigDecimal("UKUPNO",getMasterSet().getBigDecimal("UKUPNO").
                                 subtract(tmpUKUPNO));
    getMasterSet().setBigDecimal("IZNOS",getMasterSet().getBigDecimal("IZNOS").
                                 subtract(tmpIZNOS));
    globalPopust();
  }
  public void AfterDeleteMaster() {
    if (presBlag.isSkladOriented()) {
      updatePlac();
    }
  }
  
  public void EntryPointMaster(char mode) {
    stavka=0;
    if (mode=='N') newRacun=true;
    else newRacun=false;
  }
 public void this_hideDetail() {
   raMaster.setEnabled(true);
   raMaster.requestFocus();
   raMaster.getOKpanel().jPrekid_actionPerformed();
   setMasterDetailMode(1);
   raDetail.hide();
 }
 
 
 
  public void refilterDetailSet() {
    super.refilterDetailSet();
    allowEdit = checkAccess();

    DataSet det = getDetailSet();
    DataSet mast = getMasterSet();
    boolean updMaster = mast.rowCount() > 0 && 
    		mast.getInt("BRDOK") == getDetailSet().getInt("BRDOK");
    
	  if (updMaster) {
	    Aus.clear(mast, "UKUPNO");
	    Aus.clear(mast, "IZNOS");
	    Aus.clear(mast, "NETO");
	    Aus.clear(mast, "UIRAC");
	  }
    det.enableDataSetEvents(false);
    olds = Stpos.getDataModule().getReadonlySet();
    olds.open();
    for (det.first(); det.inBounds(); det.next()) {
      olds.insertRow(false);
      det.copyTo(olds);
      olds.post();
      if (updMaster) {
	      Aus.add(mast, "UKUPNO", det);
	      Aus.add(mast, "IZNOS", det);
	      Aus.add(mast, "NETO", det);
	      Aus.add(mast, "UIRAC", det, "NETO");
      }
    }
    getDetailSet().first();
    getDetailSet().enableDataSetEvents(true);
    
    if (presBlag.stolovi) {
      setNaslovDetail("Stavke raèuna " +
          getMasterSet().getInt("BRDOK") +
            " (stol " + 
          getMasterSet().getString("STOL")+ ")");
    } else {
      setNaslovDetail("Stavke raèuna " +
          getMasterSet().getInt("BRDOK"));
    }
  }
  public void beforeShowDetail() {
  	System.out.println("Detail: "+getDetailSet().isOpen());
  	System.out.println("Master: "+getMasterSet().isOpen());
  	System.out.println("master mode " + raMaster.getMode());
  	allReadyRun=true;
    if (!newRacun) {
      	this.refilterDetailSet();
    	stavka=(short)(rbr.vrati_rbr("STPOS" ,getMasterSet().getString("CSKL"),getMasterSet().getString("VRDOK"),getMasterSet().getString("GOD"),getMasterSet().getInt("BRDOK"))-1);
    	if (presBlag.stolovi) {
    	  setNaslovDetail("Stavke raèuna " +
    	      getMasterSet().getInt("BRDOK") +
    	  		" (stol " + 
    	      getMasterSet().getString("STOL")+ ")");
    	} else {
    	  setNaslovDetail("Stavke raèuna " +
              getMasterSet().getInt("BRDOK"));
    	}
    }
    dm.getProd_mj().open();
    dm.getSklad().open();
    showLabel();
    String shljaker = presBlag.isUserOriented()?
        raUser.getInstance().getImeUsera():
          dm.getBlagajnici().getString("NAZBLAG");
    jpBl.jLabel11.setText(dm.getProd_mj().getString("NAZPRODMJ")+" / "+shljaker);
    jpBl.setDataSet(getMasterSet(), getDetailSet());
    getDetailSet().refresh();
  }
  public void EntryPointDetail(char mode) {
  	jpBl.disab('A', isUsluga);
    if (mode=='I') findMC();
  }
  public void AfterAfterSaveMaster(char mode) {
    super.AfterAfterSaveMaster(mode);
  }

  public void SetFokusDetail(char mode) {
    tmpIZNOS=getDetailSet().getBigDecimal("IZNOS");
    tmpUKUPNO=getDetailSet().getBigDecimal("UKUPNO");
    if (mode == 'N') {
      getDetailSet().setString("CSKL", getMasterSet().getString("CSKL"));
      getDetailSet().setString("GOD", getMasterSet().getString("GOD"));
      getDetailSet().setString("VRDOK", getMasterSet().getString("VRDOK"));
      getDetailSet().setInt("BRDOK", getMasterSet().getInt("BRDOK"));
      getDetailSet().setString("CPRODMJ", getMasterSet().getString("CPRODMJ"));
    }
    if (mode == 'N') {
      jpBl.disab('N', false);
      jpBl.fokus('N');
      //      jpBl.jrfCART.requestFocusLater();
    }
    else if (mode == 'I' && (raUser.getInstance().isSuper() || presBlag.isSuper)) {
      jpBl.disab('I', isUsluga);
      jpBl.jtfKOL.requestFocus();
      jpBl.jtfKOL.selectAll();
    }
    else {
        javax.swing.JOptionPane.showMessageDialog(raDetail,"Nemate prava na izmjenu dokumenta !","Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
        raDetail.getOKpanel().jBOK_actionPerformed();    
    }
  }
  public boolean ValidacijaDetail(char mode) {
    if (getDetailSet().getRowCount()==0) return false;
    if (vl.isEmpty(jpBl.jtfKOL))
      return false;
    if (vl.isEmpty(jpBl.jtfMC))
      return false;
    
    if (frmParam.getParam("robno","dohMcPOS","AR",
      "Dohvat cijene na POS-u (AR,ST)").trim().equalsIgnoreCase("ST") &&
        raVart.isStanje(getDetailSet().getInt("CART"))) {
      
      if (!checkKol()) return false;
    }
    /*if (frmParam.getParam("robno", "ugoart", "N", "Samo artikli ugostiteljstva").equalsIgnoreCase("N")) {
    	if (kolNaStanju.compareTo(getDetailSet().getBigDecimal("KOL"))<0) {
    		if (JOptionPane.showConfirmDialog(null, "Kolièina veæa od kolièine na zalihi,\nŽelite li nastaviti ?", "Upozorenje", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
    			return false;
    		}
    	}
    }*/
    if (mode=='N') {
      stavka++;
      getDetailSet().setShort("RBR", stavka);
    }
    return true;
  }

  String delstr;
  DataRow delRow;
  public boolean DeleteCheckMaster() {
  	if (raUser.getInstance().isSuper() || presBlag.isSuper) {
  	  if (!util.checkSeq("BL-"+cskl+"GRC"+god+"-"+rm, 
  	    Integer.toString(getMasterSet().getInt("BRDOK")))) return false;
  	  delRow = new DataRow(getMasterSet(), key);
  	  dM.copyColumns(getMasterSet(), delRow, key);
  	  	brojchek=getMasterSet().getInt("BRDOK");
  	    delstr = "BL-"+cskl+"GRC"+getMasterSet().getString("GOD")+"-"+getMasterSet().getString("CPRODMJ");
  	}
  	else {
        javax.swing.JOptionPane.showMessageDialog(raMaster,"Nemate prava na brisanje dokumenta !","Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
  		return false;
  	}
    return (getMasterSet().getString("STATUS").equalsIgnoreCase("N") && !getMasterSet().getString("FOK").equalsIgnoreCase("D"));
  }
  public boolean DeleteCheckDetail() {
  	if (raUser.getInstance().isSuper() || presBlag.isSuper) {
  	  	tmpIZNOS=getDetailSet().getBigDecimal("IZNOS");
  	    tmpUKUPNO=getDetailSet().getBigDecimal("UKUPNO");
  	    delStavka=getDetailSet().getShort("RBR");
  	}
  	else {
        javax.swing.JOptionPane.showMessageDialog(raDetail,"Nemate prava na brisanje stavaka !","Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
  		return false;  		
  	}
    return super.DeleteCheckDetail();
  }
  public void AfterSaveDetail(char mode) {
    if ((mode == 'N') || (mode== 'I')) {
      getMasterSet().setBigDecimal("UKUPNO",getMasterSet().getBigDecimal("UKUPNO").add(getDetailSet().getBigDecimal("UKUPNO")).subtract(tmpUKUPNO));
      getMasterSet().setBigDecimal("IZNOS",getMasterSet().getBigDecimal("IZNOS").add(getDetailSet().getBigDecimal("IZNOS")).subtract(tmpIZNOS));
      if (mode == 'N' && getDetailSet().rowCount() > 1) joinStavke();
    } else if (mode == 'B') {
      hr.restart.util.Valid.getValid().recountDataSet(getDetailSet(), "RBR", delStavka, false);
      getMasterSet().setBigDecimal("UKUPNO",getMasterSet().getBigDecimal("UKUPNO").subtract(tmpUKUPNO));
      getMasterSet().setBigDecimal("IZNOS",getMasterSet().getBigDecimal("IZNOS").subtract(tmpIZNOS));
    }
    globalPopust();
  }
  
  void joinAll() {
    if (!"R".equalsIgnoreCase(frmParam.getParam("pos", "joinArt", "N",
    "Spojiti stavke s istim artiklom i istim popustom (D,N,R)"))) return;
    
    String[] cols = {"CART", "PPOPUST1", "RBR", "KOL"};
    DataSet ds = getDetailSet();
    DataRow row = new DataRow(ds, cols);
    for (ds.first(); ds.inBounds(); ds.next()) {
      for (int i = 0; i < ds.rowCount(); i++) {
        ds.getDataRow(i, row);
        if ("RBR".equals(dM.compareColumns(row, ds, cols)))
          if (ds.getRow() > i) ds.setBigDecimal("KOL", Aus.zero0);
          else {
            Aus.add(ds, "KOL", row);
            ld.raLocate(dm.getArtikli(), "CART", Integer.toString(ds.getInt("CART")));
            cporez = dm.getArtikli().getString("CPOR");
            calcIZNOS(2);
          }
      }
    }
    for (ds.first(); ds.inBounds(); )
      if (ds.getBigDecimal("KOL").signum() != 0) ds.next();
      else ds.deleteRow();
    ds.saveChanges();
  }
  
  void joinStavke() {
    if (!"D".equalsIgnoreCase(frmParam.getParam("pos", "joinArt", "N",
        "Spojiti stavke s istim artiklom i istim popustom (D,N,R)"))) return;
    String[] cols = {"CART", "PPOPUST1", "RBR"};
    DataSet ds = getDetailSet();
    DataRow orig = new DataRow(ds, cols);
    DataRow row = new DataRow(ds, cols);
    ds.getDataRow(orig);
    for (int i = 0; i < ds.rowCount(); i++) {
      ds.getDataRow(i, row);
      if ("RBR".equals(dM.compareColumns(row, orig, cols))) {
        BigDecimal kol = ds.getBigDecimal("KOL");
        ds.emptyRow();
        ds.goToRow(i);
        Aus.add(ds, "KOL", kol);
        ds.post();
        calcIZNOS(1);
        break;
      }
    }
  }
  
  public boolean doBeforeSaveMaster(char mode) {
    if (mode == 'N') {
      Integer Broj = vl.findSeqInteger("BL-"+cskl+"GRC"+god+"-"+rm, true);
      getMasterSet().setInt("BRDOK",Broj.intValue());
    }
    return true;
  }
  
  public boolean doWithSaveMaster(char mode) {
    if (mode == 'N') {
      raTransaction.saveChanges(dm.getSeq());
    } else if (mode == 'B') {
      try {
      	util.delSeqCheck(delstr, true, brojchek);

        raTransaction.runSQL("delete from rate where "
            + Condition.whereAllEqual(key, delRow));
        raTransaction.runSQL("delete from stpos where "
            + Condition.whereAllEqual(key, delRow));
        raTransaction.saveChanges(getUpdatedStanje(delRow, null));

//        raTransaction.runSQL(posQuerys.recalcBrdok("POS",getMasterSet().getInt("BRDOK"),getMasterSet().getString("CSKL"),getMasterSet().getString("GOD")));
//        raTransaction.runSQL(posQuerys.recalcBrdok("STPOS",getMasterSet().getInt("BRDOK"),getMasterSet().getString("CSKL"),getMasterSet().getString("GOD")));
//        raTransaction.runSQL(posQuerys.recalcBrdok("RATE",getMasterSet().getInt("BRDOK"),getMasterSet().getString("CSKL"),getMasterSet().getString("GOD")));
      } catch (java.lang.Exception e) {
        e.printStackTrace();
      }
    }
    return true;
  }
  public boolean doWithSaveDetail(char mode) {
    System.out.println("dowithsave detail");
    return true;
  }

  public boolean ValidacijaPrijeIzlazaDetail() {
    if (justExit) return true;
    if (!newRacun) {
      //getDetailSet().close();
      //raMaster.getJpTableView().fireTableDataChanged();
//      getMasterSet().refresh();
      return true; // ako smo gledali stari raèun
    }
    if (exitQuestion()) {
      return false;
    }
    System.out.println("Validacija - checkRate: "+getMasterSet().getInt("BRDOK"));
    if (frmPlacanje.checkRate(this)) {
      /*calcGlobalPopust();
      raTransaction.saveChangesInTransaction(new QueryDataSet[] {getMasterSet(),getDetailSet()});*/
      newRacun=false;
  		System.out.println("check return true");
      return true;
    }
    else {
    	System.out.println("check return false");
    }
    System.out.println("Vraæam false");
    return false;
  }

  private void jbInit() throws Exception {
    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnjig, String newKnjig) {
        showLabel();
      };
    });

    dm.getPorezi().open();
    this.setMasterSet(dm.getPos());
    this.setNaslovMaster("Blagajna - raèuni");
    this.setVisibleColsMaster(new int[] {3,4,7,9});
    this.setMasterKey(key);
    this.setUserCheck(true);
    raMaster.setSoftDataLockEnabled(false);

    raMaster.setAutoFirstOnShow(false);
    raDetail.setSize(hr.restart.start.getSCREENSIZE());
    raDetail.addOption(navKUPAC,4,false);
    raDetail.addOption(navBEFEXIT,5,false);
    raDetail.addOption(navPOPUST,6);
    raDetail.addOption(navEXIT,7);
    if (hr.restart.sisfun.frmParam.getParam(
        "sisfun", "printerRMcmndAlt", "", "Radno mjesto (alt)", true).length()>0) {
      raDetail.addOption(navPRINT,7,false);
    }
    if (raUser.getInstance().isSuper()) {
      raDetail.addOption(navREM,4,false);
      raDetail.addOption(navSPLIT,5,false);
    }
    raDetail.setSaveChanges(false);
    raDetail.setSaveChangesMessage(null);
    raDetail.setDefaultSaveChangesAnsw(1);
//    raMaster.setSaveChanges(false);
//    raMaster.setSaveChangesMessage(null);
//    raMaster.setDefaultSaveChangesAnsw(1);
//    jpBl.setDataSet(getMasterSet(), getDetailSet());
    raDetail.addKeyAction(new hr.restart.util.raKeyAction(java.awt.event.KeyEvent.VK_F7) {
      public void keyAction() {
        if (allowEdit || raUser.getInstance().isSuper() || presBlag.isSuper) pressF7('U');
      }
    });
    raDetail.addKeyAction(new hr.restart.util.raKeyAction(java.awt.event.KeyEvent.VK_F11) {
      public void keyAction() {
        if (allowEdit) pressF11('U');
      }
    });
    raDetail.addKeyAction(new hr.restart.util.raKeyAction(java.awt.event.KeyEvent.VK_F10) {
      public void keyAction() {
        if (allowEdit) pressF10('U');
      }
    });
    raDetail.addKeyAction(new hr.restart.util.raKeyAction(java.awt.event.KeyEvent.VK_F12) {
      public void keyAction() {
        if (allowEdit) pressF12('U');
      }
    });
    raDetail.addKeyAction(new hr.restart.util.raKeyAction(java.awt.event.KeyEvent.VK_F5) {
      public void keyAction() {
        racDest = hr.restart.sisfun.frmParam.getParam(
            "sisfun", "printerRMcmnd", "1", "Radno mjesto", true);
        pressF5('U');
      }
    });
    raDetail.addKeyAction(new hr.restart.util.raKeyAction(java.awt.event.KeyEvent.VK_F5, KeyEvent.SHIFT_MASK) { 
      public void keyAction() {
        racDest = hr.restart.sisfun.frmParam.getParam(
          "sisfun", "printerRMcmndAlt", "", "Radno mjesto (alt)", true);
        pressF5('U');
      }
    });
    
    String np = frmParam.getParam("pos", "narPrint", "",
    "Definicije grupa artikala -> radno mjesto narudžbe", true);
    if (np.length() > 0) {
      raDetail.addOption(new raNavAction("Ispis narudžbe",
          raImages.IMGALIGNLEFT, KeyEvent.VK_F6) {
        public void actionPerformed(ActionEvent e) {
          printNar();
        }
      }, 8);
      raDetail.addKeyAction(new raKeyAction(KeyEvent.VK_F6) {
        public void keyAction() {
          checkUnos('U');
          printNar();
        }
      });
    }

    this.setMasterDeleteMode(DELDETAIL);
    //raDetail.setFastDelAll(true);
    
    raMaster.installSelectionTracker("BRDOK");

    this.setDetailSet(dm.getStpos());

//    this.setJPanelDetail(this.jpDet);
    this.setJPanelDetail(this.jpBl);

    this.setNaslovDetail("Stavke raèuna");
    this.setVisibleColsDetail(new int[] {4,5,8,10,11,12,20});
    this.setDetailKey((String[]) hr.restart.util.Util.getUtil().concatArrayStr(key, "RBR"));

    if (frmParam.getParam("pos", "posStolovi", "N",
      "Izbor stola na predselekciji blagajne (D,N)").equalsIgnoreCase("D")) {
      JPanel upper = new JPanel();
      upper.setLayout(new BoxLayout(upper, BoxLayout.X_AXIS));
      upper.add(new JLabel("Stol"));
      upper.add(Box.createHorizontalStrut(20));
      upper.add(jraStol);
      upper.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
      jraStol.setPreferredSize(new Dimension(100, 21));
      jraStol.setEnterDisabled(false);
      new raTextMask(jraStol, 10);
      
      jpStol.setLayout(new BorderLayout());
      jpStol.add(upper);
      jpStol.add(dlgStol.getOkPanel(), BorderLayout.SOUTH);
      dlgStol.getOkPanel().setEnterEnabled(true);
      if (false) {
        raMaster.addOption(navZatvori, 4);
        if (raUser.getInstance().isSuper()) {
          raMaster.addOption(navPonisti, 5);
        }
      }
      this.raMaster.addOption(new raNavAction("Pregled materijala", raImages.IMGMOVIE, KeyEvent.VK_F7, KeyEvent.SHIFT_MASK) {
        public void actionPerformed(java.awt.event.ActionEvent ev) {
          showRequirementsMaster();
        }
      },6,false);
      
      if (false) raMaster.getJpTableView().addTableModifier(msc);
    } else {
      if (presBlag.isSkladOriented()) {
        this.raMaster.addOption(new raNavAction("Naplata",raImages.IMGEXPORT2,KeyEvent.VK_F7) {
          public void actionPerformed(ActionEvent e) {
            if (allowEdit || raUser.getInstance().isSuper() || presBlag.isSuper)
              if (getMasterSet().rowCount() > 0 && 
                  getMasterSet().getBigDecimal("UIRAC").signum() != 0)
                keyNacinPlac();
          }
        },4,true);
        this.raMaster.addOption(new raNavAction("Pregled plaæanja", raImages.IMGMOVIE, KeyEvent.VK_F7, KeyEvent.SHIFT_MASK) {
          public void actionPerformed(java.awt.event.ActionEvent ev) {
            showTotalPlac();
          }
        },5,false);
        this.raMaster.addOption(new raNavAction("Pregled artikala", raImages.IMGHISTORY, KeyEvent.VK_F8, KeyEvent.SHIFT_MASK) {
          public void actionPerformed(java.awt.event.ActionEvent ev) {
            showTotalArt();
          }
        },6,false);
      } else {
        if (raUser.getInstance().isSuper()) {
          raMaster.addOption(navPonisti, 4);
        }
        this.raMaster.addOption(new raNavAction("Pregled materijala", raImages.IMGMOVIE, KeyEvent.VK_F7, KeyEvent.SHIFT_MASK) {
          public void actionPerformed(java.awt.event.ActionEvent ev) {
            showRequirementsMaster();
          }
        },5,false);
      }
    }
    
    /*if (presBlag.isFiskal()) */ raMaster.getJpTableView().addTableModifier(fsc);
    
    raDetail.getNavBar().removeStandardOption(raNavBar.ACTION_TOGGLE_TABLE);

    raDetail.setkum_tak(true);
    raDetail.setstozbrojiti(new String[] {"IZNOS"});
    
    raMaster.setkum_tak(true);
    raMaster.setstozbrojiti(new String[] {"UKUPNO","IZNOS","NETO","UIRAC"});
//    this.getOKpanel().setVisible(false);
    IZNOS.setCaption("IZNOS");
    IZNOS.setColumnName("IZNOS");
    IZNOS.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    IZNOS.setDisplayMask("###,###,##0.00");
    IZNOS.setDefault("0");
    IZNOS.setServerColumnName("NewColumn1");
    IZNOS.setSqlType(2);
//    jPanel2.setLayout(xYLayout3);
    xYLayout3.setHeight(40);
//    jPanel2.setBorder(BorderFactory.createEtchedBorder());
    jLabel5.setText("Kupac");
//    jpNorth.add(jPanel2,  BorderLayout.SOUTH);
//    jPanel2.add(jLabel5,    new XYConstraints(15, 15, -1, -1));
//    jPanel2.add(jrfCKUPAC, new XYConstraints(150, 10, 100, -1));
//    jPanel2.add(jrfNAZKUPAC, new XYConstraints(260, 10, 345, -1));
//    jPanel2.add(jbCKUPAC, new XYConstraints(609, 10, 21, 21));

    this.setJPanelMaster(new JPanel());
    
    raMaster.getTab().remove(1);
    raMaster.getRepRunner().addReport("hr.restart.robno.repRacunPOS", "Raèun");
    raMaster.getRepRunner().addJasper("hr.restart.robno.repRacunPOSj", "hr.restart.robno.repPosJas", "pos.jrxml", "Raèun jasper");
    if (repFISBIH.isFISBIH()) {
      raMaster.getRepRunner().addReport("hr.restart.robno.repFISBIHPos", "Fiskalni raèun");
      raDetail.getRepRunner().addReport("hr.restart.robno.repFISBIHPos", "Fiskalni raèun");
    }
    raDetail.getRepRunner().addReport("hr.restart.robno.repRacunPOS", "Raèun");
    if (np.length() > 0) {
      raDetail.getRepRunner().addReport("hr.restart.robno.repNarPOS", "Narudžba");
    }
    
    if (presBlag.isSkladOriented()) {
      totalSet.setColumns(new Column[] {
          dM.createStringColumn("NACPL", "Naèin plaæanja", 50),
          dM.createBigDecimalColumn("IRATA", "Iznos naplate")
      });
      totalSet.open();
      totalTab.setDataSet(totalSet);
      totalTab.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      totalTab.setCellSelectionEnabled(false);
      totalTab.clearSelection();
      totalTab.setAlternateColor(false);
      totalTab.setBackground(raMaster.getJpTableView().getBackground());
      
      raMaster.getJpTableView().installSummary(totalTab, 10, true);
    }
  }
  /**
   * Disejbliranje pojedinih panela na ekranu zavisno od moda rada
   * @param novi - da li je novi
   */
/*  void disab(boolean novi) {
    if (novi) {
      rcc.EnabDisabAll(this.rpcart, true);
      rcc.setLabelLaF(this.jtfKOL,      false);
      rcc.setLabelLaF(this.jtfPPOPUST,  false);
      rpcart.SetFocus(hr.restart.sisfun.frmParam.getParam("robno","focusCart"));
    }
    else {
      rcc.setLabelLaF(this.jtfKOL,      true);
      rcc.setLabelLaF(this.jtfPPOPUST,  false);
      rcc.EnabDisabAll(this.rpcart, false);
    }
  }*/
  /**
   * Pronazazenje cijena, poreza i iznosa
   */
  void findOtherThings() {
    if (raDetail.getMode()=='B') return;
//    lookupData.getlookupData().raLocate(dm.getPorezi(), "CPOR", getDetailSet().getString("CPOR"));

    //    dm.getPorezi().interactiveLocate(this.rpcart.getCPOR(), "CPOR", com.borland.dx.dataset.Locate.FIRST, false);
    getDetailSet().setBigDecimal("KOL", util.one);
    getDetailSet().setBigDecimal("MC", findMC());
    calcIZNOS(0);
    jpBl.disab('I', isUsluga);
  }

  /**
   * Pronalazak MC
   * @return MC sa stanja ili s artikla
   */

  java.math.BigDecimal findMC() {
    String vrc = frmParam.getParam("robno","dohMcPOS","AR",
      "Dohvat cijene na POS-u (AR,ST,CJ)").trim();
    
  	kolNaStanju = kolRez = Aus.zero3;
  	BigDecimal prva = Aus.zero2;
  	String str;
  	if (jpBl.tCartSifparam.equalsIgnoreCase("BC")) {
  		str="select MC,VRART,PPOP,CPOR from ARTIKLI where BC='"+getDetailSet().getString("BC")+"'";  		
  	}
  	else if (jpBl.tCartSifparam.equalsIgnoreCase("CART1")) {
  		str="select MC,VRART,PPOP,CPOR from ARTIKLI where CART1='"+getDetailSet().getString("CART1")+"'";  		
  	}
  	else {
  		str="select MC,VRART,PPOP,CPOR from ARTIKLI where CART="+getDetailSet().getInt("CART");  		
  	}
    vl.execSQL(str);
    vl.RezSet.open();
    isUsluga=raVart.isVarnaziv(vl.RezSet);
      //(vl.RezSet.getString("VRART").equals("U"));
    boolean nostanje = !raVart.isStanje(vl.RezSet);
    cporez=vl.RezSet.getString("CPOR");
    
    if (vrc.equals("CJ")) {
      DataSet cj = allStanje.getallStanje().getCijenik("GRC", cskl, cpar,
          getDetailSet().getInt("CART"));
      if (cj != null && cj.rowCount() > 0) {
        if (raDetail.getMode()=='N' && cj.getBigDecimal("POSTO").signum() > 0) {
          getDetailSet().setBigDecimal("PPOPUST1", cj.getBigDecimal("POSTO"));
        }
        return cj.getBigDecimal("MC");
      }
      vrc = "AR";
    }
    
    if (raDetail.getMode()=='N') {
      getDetailSet().setBigDecimal("PPOPUST1", vl.RezSet.getBigDecimal("PPOP"));
    }
  	prva=vl.RezSet.getBigDecimal("MC");
    if (nostanje || vrc.equals("AR")) {
      return prva;
    }
    
/*  	if (jpBl.tCartSifparam.equalsIgnoreCase("BC")) {
  		str="select STANJE.MC, STANJE.KOL from STANJE, ARTIKLI where ARTIKLI.BC='"+getDetailSet().getString("BC")+"' AND ARTIKLI.CART=STANJE.CART AND GOD='"+Aut.getAut().getKnjigodRobno()+"' AND CSKL='"+dm.getPos().getString("CSKL")+"'";
  	}
  	else if (jpBl.tCartSifparam.equalsIgnoreCase("CART1")) {
  		str="select STANJE.MC, STANJE.KOL from STANJE, ARTIKLI where ARTIKLI.CART1='"+getDetailSet().getString("CART1")+"' AND ARTIKLI.CART=STANJE.CART AND GOD='"+Aut.getAut().getKnjigodRobno()+"'AND CSKL='"+dm.getPos().getString("CSKL")+"'";
  	}
  	else {
  		str="select STANJE.MC, STANJE.KOL from STANJE, ARTIKLI where ARTIKLI.CART="+getDetailSet().getInt("CART")+" AND ARTIKLI.CART=STANJE.CART AND GOD='"+Aut.getAut().getKnjigodRobno()+"'AND CSKL='"+dm.getPos().getString("CSKL")+"'";
  	}*/
    
  	/*if (jpBl.tCartSifparam.equalsIgnoreCase("BC")) {
  		str="select STANJE.MC, STANJE.KOL from STANJE, ARTIKLI where ARTIKLI.BC='"+getDetailSet().getString("BC")+"' AND ARTIKLI.CART=STANJE.CART AND GOD='"+vl.getValid().findYear(getMasterSet().getTimestamp("DATDOK"))+"' AND CSKL='"+getMasterSet().getString("CSKL")+"'";
  	}
  	else if (jpBl.tCartSifparam.equalsIgnoreCase("CART1")) {
  		str="select STANJE.MC, STANJE.KOL from STANJE, ARTIKLI where ARTIKLI.CART1='"+getDetailSet().getString("CART1")+"' AND ARTIKLI.CART=STANJE.CART AND GOD='"+vl.getValid().findYear(getMasterSet().getTimestamp("DATDOK"))+"'AND CSKL='"+getMasterSet().getString("CSKL")+"'";
  	}
  	else {
  		str="select STANJE.MC, STANJE.KOL from STANJE, ARTIKLI where ARTIKLI.CART="+getDetailSet().getInt("CART")+" AND ARTIKLI.CART=STANJE.CART AND GOD='"+vl.getValid().findYear(getMasterSet().getTimestamp("DATDOK"))+"'AND CSKL='"+getMasterSet().getString("CSKL")+"'";
  	}
    System.out.println("cart: "+this.jpBl);
    System.out.println("str: "+str);
    vl.execSQL(str);
    vl.RezSet.open();
    
    System.out.println("vl.RezSet.rowCount()>0 - " + (vl.RezSet.rowCount()>0));*/
    
    DataSet sta = Stanje.getDataModule().getTempSet(Condition.whereAllEqual(
        new String[] {"CSKL", "GOD", "CART"}, getDetailSet()));
    sta.open();
    
    if (sta.rowCount()>0) {
    	kolNaStanju=sta.getBigDecimal("KOL");
    	kolRez=sta.getBigDecimal("KOLREZ");
    	System.out.println("stanje: " + kolNaStanju);
    	System.out.println("rezervirano: " + kolRez);
    	return sta.getBigDecimal("MC");
    }
    return prva;
/*    if (hr.restart.sisfun.frmParam.getParam("robno","dohMcPOS").trim().equals("ST")) {
//      isUsluga=(
      return util.getMC("STANJE", dm.getPos().getString("CSKL"), ""+getDetailSet().getInt("BRDOK"));
    }
    return util.getMC(getDetailSet().getInt("CART"));*/
  }

/*  public void masterSet_navigated(com.borland.dx.dataset.NavigationEvent e) {
    new Throwable().printStackTrace();
  }*/


  /**
   * Izracun ostalih vrijednosti na temelju KOL i MC
   * @param mod
   */



  public void calcIZNOS(int mod) {
    DataSet art = dm.getArtikli();
	  lookupData.getlookupData().raLocate(dm.getPorezi(), "CPOR", cporez);
	  lookupData.getlookupData().raLocate(art, "CART", getDetailSet().getInt("CART")+"");

    getDetailSet().setBigDecimal("UKUPNO", util.multiValue(getDetailSet().getBigDecimal("MC"), getDetailSet().getBigDecimal("KOL")));
    getDetailSet().setBigDecimal("IPOPUST1", util.multiValue(getDetailSet().getBigDecimal("UKUPNO"), getDetailSet().getBigDecimal("PPOPUST1").divide(util.sto,BigDecimal.ROUND_HALF_UP)));
    //getDetailSet().setBigDecimal("IPOPUST2", getDetailSet().getBigDecimal("MC").multiply(getDetailSet().getBigDecimal("PPOPUST2").divide(util.sto,1)));
    getDetailSet().setBigDecimal("IZNOS", getDetailSet().getBigDecimal("UKUPNO").subtract(getDetailSet().getBigDecimal("IPOPUST1")));
    if (mod==2) {
      getDetailSet().setBigDecimal("PPOPUST2", getMasterSet().getBigDecimal("UPPOPUST2"));
      getDetailSet().setBigDecimal("IPOPUST2", util.multiValue(getDetailSet().getBigDecimal("IZNOS"), util.divideValue(getDetailSet().getBigDecimal("PPOPUST2"), util.sto)));
      getDetailSet().setBigDecimal("NETO", util.negateValue(getDetailSet().getBigDecimal("IZNOS"), getDetailSet().getBigDecimal("IPOPUST2")));
    }
    BigDecimal sub = Aus.zero0;
    if ("D".equals(art.getString("POV")))
      sub = pov.multiply(getDetailSet().getBigDecimal("KOL")).setScale(2, BigDecimal.ROUND_HALF_UP);
    BigDecimal osnovica = new BigDecimal(getDetailSet().getBigDecimal("IZNOS").subtract(sub).doubleValue()/((100+dm.getPorezi().getBigDecimal("UKUPOR").doubleValue())/100));
//    getDetailSet().setBigDecimal("POR1", util.findIznos(getDetailSet().getBigDecimal("IZNOS"), dm.getPorezi().getBigDecimal("UNPOR1")));
//    getDetailSet().setBigDecimal("POR2", util.findIznos(getDetailSet().getBigDecimal("IZNOS"), dm.getPorezi().getBigDecimal("UNPOR2")));
//    getDetailSet().setBigDecimal("POR3", util.findIznos(getDetailSet().getBigDecimal("IZNOS"), dm.getPorezi().getBigDecimal("UNPOR3")));
    getDetailSet().setBigDecimal("PPOR1", dm.getPorezi().getBigDecimal("POR1"));
    getDetailSet().setBigDecimal("PPOR2", dm.getPorezi().getBigDecimal("POR2"));
    getDetailSet().setBigDecimal("PPOR3", dm.getPorezi().getBigDecimal("POR3"));
    getDetailSet().setBigDecimal("POR1", util.findIznos(osnovica, dm.getPorezi().getBigDecimal("POR1")));
    getDetailSet().setBigDecimal("POR2", util.findIznos(osnovica, dm.getPorezi().getBigDecimal("POR2")));
    getDetailSet().setBigDecimal("POR3", util.findIznos(osnovica, dm.getPorezi().getBigDecimal("POR3")));
//    System.out.println("Por2: "+);
    //    getDetailSet().setString("CART1", jpBl.jrfCART.getRaDataSet().getString("CART1"));
//    getDetailSet().setInt("CART", jpBl.jrfCART.getRaDataSet().getInt("CART"));
//    getDetailSet().setString("JM", jpBl.jrfCART.getRaDataSet().getString("JM"));
//    getDetailSet().setString("JM", jpBl.jrfJM.getText());
  }
  /**
   * Tipke u polju koli\u010Dina
   * - ENTER radi saveChanges()
   * - ESC ulazi na cancel()
   */
  void jtfKOL_keyPressed(KeyEvent e) {
    if (e.getKeyCode()==e.VK_TAB) {
      calcIZNOS(1);
      e.consume();
    }
  }
  /**
   * Pozivanje ekrana sa dodatnim podacima
   * @param mode - (U / B) - da li je pozvan sa unosa ili sa browsa
   */
  void pressF7(char mode) {
    checkUnos(mode);
    if (getMasterSet().getBigDecimal("NETO").doubleValue()==0) {
    }
    if (getDetailSet().getRowCount()>0) {
      getMasterSet().setString("CNACPL",frmParam.getParam("robno","gotNacPl"));
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          keyNacinPlac();
        }
      });
    }
    else {
      JOptionPane.showMessageDialog(raDetail.getWindow(),
          "Nema upisanih stavaka !", "Greška",JOptionPane.ERROR_MESSAGE);
    }
  }
  void pressF12(char mode) {
    checkUnos(mode);
    rcc.setLabelLaF(jpBl.jtfPOP, true);
    jpBl.jtfPOP.selectAll();
    jpBl.jtfPOP.requestFocusLater();
  }
  public void keyNacinPlac(){
    System.out.println("entryRate: "+getMasterSet().getInt("BRDOK"));
  	frmPlacanje.entryRate(this);
  	if (!newRacun) {
  	  getMasterSet().saveChanges();
  	  updatePlac();
  	  raMaster.getJpTableView().fireTableDataChanged();
  	}
  }
  void pressF11(char mode) {

    //_Main.getStartFrame().showFrame("hr.restart.robno.dlgKupac", "Unos kupca za R-1");
    if (dlgkup == null) {
      if (raDetail.getWindow() instanceof Frame)
        dlgkup = new dlgKupac((Frame) raDetail.getWindow(), getMasterSet());
      else if (raDetail.getWindow() instanceof Dialog)
        dlgkup = new dlgKupac((Dialog) raDetail.getWindow(), getMasterSet());
      else dlgkup = new dlgKupac((Frame) null, getMasterSet());
      dlgkup.pack();
      Aus.centerWindow(dlgkup);
    }
    
    dlgkup.setVisible(true);
    System.out.println("********* after dlgKupac");
    checkUnos(mode);
    //jpDetBlagajna.grabFocusPOS();
  }
  
  void arhRac() {
    if (getMasterSet().isEmpty()) return;
    if (getMasterSet().getString("STATUS").equalsIgnoreCase("P") &&
        getMasterSet().getString("RDOK").equalsIgnoreCase("arh")) {
      if (JOptionPane.showConfirmDialog(raMaster.getWindow(),
          "Želite li dearhivirati raèun?", "Dearhiviranje raèuna", 
          JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
      
      getMasterSet().setString("STATUS", "N");
      getMasterSet().setString("RDOK", "");
    } else {
      if (!getMasterSet().getString("STATUS").equalsIgnoreCase("N")) {
          JOptionPane.showMessageDialog(raMaster.getWindow(),
          "Raèun je veæ prenešen!", "Arhiviranje", JOptionPane.INFORMATION_MESSAGE);
       return;
      }
      if (JOptionPane.showConfirmDialog(raMaster.getWindow(),
        "Želite li arhivirati raèun?", "Arhiviranje raèuna", 
        JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
      getMasterSet().setString("STATUS", "P");
      getMasterSet().setString("RDOK", "arh");
    }
    
    getMasterSet().saveChanges();
    int row = getMasterSet().getRow();
    getMasterSet().refresh();
    getMasterSet().goToClosestRow(row);
    raMaster.getJpTableView().fireTableDataChanged();
    raMaster.jeprazno();
  }
  
  void closeRac() {
    if (getMasterSet().isEmpty()) return;
    if (!getMasterSet().getString("AKTIV").equalsIgnoreCase("D")) {
      JOptionPane.showMessageDialog(raMaster.getWindow(),
          "Raèun je veæ zakljuèen!", "Potvrda", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    
    if (JOptionPane.showConfirmDialog(raMaster.getWindow(),
        "Želite li zakljuèiti raèun za stol "+
        getMasterSet().getString("STOL")+" ?", "Potvrda raèuna", 
        JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
    
    getMasterSet().setString("AKTIV", "N");
    getMasterSet().saveChanges();
    int row = getMasterSet().getRow();
    getMasterSet().refresh();
    getMasterSet().goToClosestRow(row);
    raMaster.getJpTableView().fireTableDataChanged();
    raMaster.jeprazno();
  }
  
  boolean checkRate() {
    getMasterSet().setString("CNACPL",frmParam.getParam("robno","gotNacPl"));
      calcGlobalPopust();
      if (!frmPlacanje.checkRate(this)) return false;
      
      raTransaction.saveChangesInTransaction(
          new QueryDataSet[] {getMasterSet(), getDetailSet(),
              getUpdatedStanje(getMasterSet(), getDetailSet())});

//      raMaster.setEditEnabled(true);
      return true;
  }
  /**
   * Spremanje podataka sa defaultnim vrijednostima
   * @param mode - (U / B) - da li je pozvan sa unosa ili sa browsa
   */
  void pressF10(char mode) {
    checkUnos(mode);
    /*if (getMasterSet().getBigDecimal("NETO").doubleValue()==0) {
        System.out.println("getMaster: "+getMasterSet().getBigDecimal("NETO"));
      if (newRacun) getDetailSet().refresh();
    }*/
    if (getDetailSet().getRowCount()>0) {
      if (!checkRate()) return;
      if (presBlag.isFiskal(getMasterSet()) && !getMasterSet().getString("FOK").equals("D")) {
        if (JOptionPane.showConfirmDialog(raDetail, "Želite li ispisati predraèun?", "Ispis", JOptionPane.OK_CANCEL_OPTION) 
            == JOptionPane.OK_OPTION) {
          justPrintGRC();
        }
      }
      justExit=true;
      makeNext=newRacun;
      raDetail.rnvExit_action();
    }
    else {
    	System.out.println("Ich bin gecrk");
    	JOptionPane.showConfirmDialog(raDetail.getWindow(),"Nema upisanih stavaka !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      raDetail.rnvAdd_action();
    }
  }
  /**
   * Prekid unosa ako smo u modu unosa
   * @param mode
   */
  void checkUnos(char mode) {
    if (mode=='U') {
      jpBl.jtfKOL.requestFocus();
      raDetail.getOKpanel().jPrekid_actionPerformed();
    }
  }
  
  void split() {
    if (getDetailSet().rowCount() == 0) return;
    
    dlgSplitAmount dlg = null;
    if (raDetail.getWindow() instanceof Frame)
      dlg= new dlgSplitAmount((Frame) raDetail.getWindow());
    if (raDetail.getWindow() instanceof Dialog)
      dlg = new dlgSplitAmount((Dialog) raDetail.getWindow());
    BigDecimal result = dlg.performSplit("Stavka br. "+
        getDetailSet().getShort("RBR"), getDetailSet().getBigDecimal("KOL"));
    if (result != null && result.signum() > 0 && 
        result.compareTo(getDetailSet().getBigDecimal("KOL")) < 0) {
      cporez = Aus.q("SELECT * FROM artikli WHERE " + 
          Condition.equal("CART", getDetailSet())).getString("CPOR");
      DataRow copy = new DataRow(getDetailSet());
      getDetailSet().copyTo(copy);
      getDetailSet().setBigDecimal("KOL", getDetailSet().getBigDecimal("KOL").subtract(result));
      calcIZNOS(1);
      raDetail.getJpTableView().enableEvents(false);
      int rbs = 0;
      for (getDetailSet().first(); getDetailSet().inBounds(); getDetailSet().next())
        if (getDetailSet().getShort("RBR") > rbs)
          rbs = getDetailSet().getShort("RBR");
      getDetailSet().insertRow(false);
      copy.copyTo(getDetailSet());
      getDetailSet().setShort("RBR", (short) (rbs + 1));
      getDetailSet().setBigDecimal("KOL", result);
      calcIZNOS(1);
      calcGlobalPopust();
      try {
        getDetailSet().saveChanges();
      } catch (Exception e) {
        JOptionPane.showMessageDialog(raDetail.getWindow(), "Greška kod dodavanja stavke!",
            "Greška", JOptionPane.ERROR_MESSAGE);
      }
      getDetailSet().last();
      raDetail.getJpTableView().enableEvents(true);
    }
    
  }
  
  
  void transfer() {
    if (getDetailSet().rowCount() == 0) return;
    
    if (getDetailSet().rowCount() == 1) {
      JOptionPane.showMessageDialog(raDetail.getWindow(),
          "Zadnja stavka se ne može prebaciti !", "Greška", 
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    DataSet ds = Stpos.getDataModule().getTempSet(
        Condition.whereAllEqual(key, getMasterSet()));
    ds.open();
    DataRow dd = ld.raLookup(ds, new String[] 
             {"CART", "KOL", "NETO"}, getDetailSet());
    if (dd == null) {
      JOptionPane.showMessageDialog(raDetail.getWindow(),
          "Stavka nije snimljena !", "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    if (!this.raDetail.LegalDelete(false, true)) return;
    if (!checkRate()) {
      JOptionPane.showMessageDialog(raDetail.getWindow(),
          "Greška kod snimanja rata !", "Upozorenje", 
          JOptionPane.WARNING_MESSAGE);
    }
    
    DataSet mas = Pos.getDataModule().getReadonlySet();
    mas.insertRow(false);
    getMasterSet().copyTo(mas);
    mas.setString("CSKL", "#"+mas.getString("CSKL"));
    
    QueryDataSet zag = Pos.getDataModule().getTempSet(
        Condition.whereAllEqual(key, mas));
    zag.open();
    if (zag.getRowCount() == 0) {
      zag.insertRow(false);
      mas.copyTo(zag);
    }
    
    QueryDataSet det = Stpos.getDataModule().getTempSet(
        Condition.whereAllEqual(key, mas));
    det.open();
    int num = det.getRowCount() + 1;
    det.insertRow(false);
    dd.copyTo(det);
    det.setShort("RBR", (short) num);
    det.setString("CSKL", "#"+det.getString("CSKL"));
    
    Aus.clear(zag, "UKUPNO");
    Aus.clear(zag, "IZNOS");
    Aus.clear(zag, "NETO");
    Aus.clear(zag, "UIRAC");
    
    for (det.first(); det.inBounds(); det.next()) {
       Aus.add(zag, "UKUPNO", det);
       Aus.add(zag, "IZNOS", det);
       Aus.add(zag, "NETO", det);
       Aus.add(zag, "UIRAC", det, "NETO");
    }
    
    raTransaction.saveChangesInTransaction(new QueryDataSet[] {zag,det});  }
/*  boolean pressESC() {
    if (jpBl.jrfCART.getText().equals("")) {
      if (!exitQuestion()) {
        raDetail.rnvExit_action();
      }
    }
    else {
      jpBl.disab('N');
      dm.getStpos().setBigDecimal("KOL",util.nul);
      this.calcIZNOS(0);
    }
    return true;
  }*/
  /**
   * Pitanje kod izlaska
   * @return
   */
  boolean exitQuestion() {
    brojchek=getMasterSet().getInt("BRDOK");
    System.out.println("Exit question: "+brojchek+"  novi-"+newRacun);
    if (getDetailSet().rowCount()>0)  {
      if (!newRacun || getMasterSet().getInt("BRDOK") != brdoknew) return false; // ako smo gledali stari raèun
      if (JOptionPane.showConfirmDialog(null, "Želite li poništiti unos ?", "Izlaz", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
        getDetailSet().deleteAllRows();
        try {
          String str="delete from rate where " + 
                Condition.whereAllEqual(key, getMasterSet());
          vl.runSQL(str);
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
        delOtherTables();
        raMaster.getJpTableView().fireTableDataChanged();
        return false;
      }
      return true;
    }
    else {
      if (newRacun && getMasterSet().getInt("BRDOK") == brdoknew) delOtherTables();
    }
    return false;
  }
  
  QueryDataSet getUpdatedStanje(ReadRow master, DataSet detail) {
    if (!frmParam.getParam("pos", "rezKolPos", "D",
        "POS rezervira stanje artikala (D,N)").equalsIgnoreCase("D"))
      return Refresher.getDataModule().getTempSet(Condition.nil);
    
    List arts = new ArrayList();    
    for (olds.first(); olds.inBounds(); olds.next())
      if (raVart.isStanje(olds.getInt("CART")))
        arts.add(new Integer(olds.getInt("CART")));
    if (detail != null) {
      detail.enableDataSetEvents(false);
      for (detail.first(); detail.inBounds(); detail.next())
        if (raVart.isStanje(detail.getInt("CART")))
          arts.add(new Integer(detail.getInt("CART")));
    }
    
    QueryDataSet stanje = Stanje.getDataModule().getTempSet(
        Condition.equal("CSKL", master).
        and(Condition.equal("GOD", master)).
        and(Condition.in("CART", arts.toArray())));
    
    for (olds.first(); olds.inBounds(); olds.next())
      if (ld.raLocate(stanje, "CART", 
          Integer.toString(olds.getInt("CART")))) {
        Aus.sub(stanje, "KOLREZ", olds, "KOL");
        if (stanje.getBigDecimal("KOL").signum() < 0)
          stanje.setBigDecimal("KOLREZ", Aus.zero3);
      }
    
    if (detail != null) {
      for (detail.first(); detail.inBounds(); detail.next())
        if (ld.raLocate(stanje, "CART", 
            Integer.toString(detail.getInt("CART")))) {
          Aus.add(stanje, "KOLREZ", detail, "KOL");
          detail.setString("REZKOL", "D");
        }
      detail.enableDataSetEvents(false);
    }
    
    return stanje;
  }
  
  void delOtherTables() {
    delstr = "BL-"+cskl+"GRC"+getMasterSet().getString("GOD")+"-"+getMasterSet().getString("CPRODMJ");
    System.out.println("DEL: " + delstr);
    util.delSeqCheck(delstr, false, getMasterSet().getInt("BRDOK"));
    //raMaster.getJpTableView().fireTableDataChanged();
    QueryDataSet stanje = getUpdatedStanje(getMasterSet(), null);
    getMasterSet().deleteRow();
    
    raTransaction.saveChangesInTransaction(new QueryDataSet[] 
               {getMasterSet(),dm.getSeq(),getDetailSet(),stanje});
    raMaster.getJpTableView().fireTableDataChanged();
  }
  /**
   * Poslije pronalaska artikla
   */
  void myAfterCART() {
    jpBl.disab('I', isUsluga);
  }
  
  public void printNar() {
    if (getDetailSet().getRowCount() > 0) {
      if (allowEdit && !checkRate()) return;
    } else
      return;

    if (!printNar_impl(true, true))
      JOptionPane.showMessageDialog(raDetail.getWindow(),
          "Pogrešno zadane grupe artikala za ispis narudžbi!", "Greška",
          JOptionPane.ERROR_MESSAGE);
  }

  DataSet narSet;
  String narDest;

  public boolean printNar_impl(boolean saveChanges, boolean warn) {
    String np = frmParam.getParam("pos", "narPrint", "",
        "Definicije grupa artikala -> radno mjesto narudžbe");
    if (np.length() == 0) return false;
    String[] rmp = new VarStr(np).splitTrimmed('|');
    Map rms = new HashMap();
    for (int i = 0; i < rmp.length; i++) {
      String[] def = new VarStr(rmp[i]).splitTrimmed(':');
      if (def.length != 2) return false;
      rms.put(def[0], def[1]);
    }
    Map mgr = Aut.getAut().getPodgrupe(rms.keySet());
    QueryDataSet det = getDetailSet();
    
    boolean needSave = false;
    
    for (int ina = 0; ina < 2 && !needSave; ina++) {

    det.enableDataSetEvents(false);
    int orow = det.getRow();
    System.out.println("row: "+orow);
    for (Iterator i = rms.keySet().iterator(); i.hasNext(); ) {
      String gr = (String) i.next();
      System.out.println("Ispis za grupu "+gr+" : "+rms.get(gr));
      narSet = Stpos.getDataModule().getReadonlySet();
      for (det.first(); det.inBounds(); det.next()) {
        if ((ina == 1 || det.getString("AKTIV").equalsIgnoreCase("D")) &&
            ld.raLocate(dm.getArtikli(), "CART", Integer.toString(det.getInt("CART"))) 
            && gr.equals(mgr.get(dm.getArtikli().getString("CGRART")))) {
          narSet.insertRow(false);
          dM.copyDestColumns(det, narSet);
          det.setString("AKTIV", "N");
          narSet.setInt("BRDOK", getMasterSet().getInt("BRDOK"));
          narSet.post();
          System.out.println(narSet);
          needSave = true;
        }
      }
      if (narSet.rowCount() > 0) {
        narDest = (String) rms.get(gr);
        raDetail.getRepRunner().setOneTimeDirectReport("hr.restart.robno.repNarPOS");
        System.out.println("Zovem Funkcija_ispisa ... za destinaciju "+narDest);
        raDetail.Funkcija_ispisa();
        /*try {
          System.out.println("Spavam 250 ...");
          Thread.sleep(250);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }*/
      }
    }
    
    det.enableDataSetEvents(true);
    det.goToClosestRow(orow);
    
    if (!needSave && warn) {
      if (ina == 1)
        JOptionPane.showMessageDialog(raDetail.getWindow(), 
            "Nema stavaka za ispis narudžbe!", "Upozorenje",
            JOptionPane.WARNING_MESSAGE);
      else if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(
          raDetail.getWindow(), "Nema prethodno neispisanih stavaka za narudžbu!\n" +
            "Ispisati narudžbu iznova (oprez)?", "Narudžba",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE)) break;
    }
    
    }
    
    if (needSave&&saveChanges) det.saveChanges();
    return true;
  } 
  
  
  static String racDest;
  
//  public RacunType rtype = null;
  
  public static RacunType getRacType(DataSet ms) {
        
    lookupData ld = lookupData.getlookupData();
    dM dm = dM.getDataModule();
    
    DataSet ds = Stpos.getDataModule().getTempSet(Condition.whereAllEqual(key, ms));
    ds.open();

    //BigDecimal osnpdv = Aus.zero2;
    BigDecimal osnpnp = Aus.zero2;
    //BigDecimal izpdv = Aus.zero2;
    BigDecimal izpnp = Aus.zero2;
    BigDecimal tot = Aus.zero2;
    BigDecimal povn = Aus.zero2;
    BigDecimal povs = Aus.getDecNumber(frmParam.getParam("robno", "iznosPov", "0.5",
      "Iznos povratne naknade"));
    
    BigDecimal pdv25 = new BigDecimal("25").setScale(2);
    
    HashMap izmap = new HashMap();
    HashMap osnmap = new HashMap();
    
  
    for (ds.first(); ds.inBounds(); ds.next()) {
      BigDecimal por1 = ds.getBigDecimal("POR1");
      BigDecimal por2 = ds.getBigDecimal("POR2");
      BigDecimal osn = ds.getBigDecimal("NETO").subtract(por1).subtract(por2);
      ld.raLocate(dm.getArtikli(), new String[]{"CART"}, new String[] {String.valueOf(ds.getInt("CART"))});
      if ("D".equals(dm.getArtikli().getString("POV"))) {
        BigDecimal povni = povs.multiply(ds.getBigDecimal("KOL")).
          setScale(2, BigDecimal.ROUND_HALF_UP);
        povn = povn.add(povni);
        osn = osn.subtract(povni);
      }
      
      if (por1.signum() != 0) {
        BigDecimal post = ds.getBigDecimal("PPOR1").setScale(2);
        BigDecimal osnpdv = (BigDecimal) osnmap.get(post);
        BigDecimal izpdv = (BigDecimal) izmap.get(post);
        osnmap.put(post, osn.add(osnpdv != null ? osnpdv : Aus.zero0));
        izmap.put(post, por1.add(izpdv != null ? izpdv : Aus.zero0));
      }
      
      if (por2.signum() != 0) osnpnp = osnpnp.add(osn);
      izpnp = izpnp.add(por2);
      tot = tot.add(ds.getBigDecimal("NETO"));
      
      
    }
    
    System.out.println(tot);
    System.out.println(osnmap);
    System.out.println(izmap);
    System.out.println(osnpnp + " " + izpnp);
    System.out.println(povn);
    System.out.println(osnmap.get(pdv25));
    System.out.println(izmap.get(pdv25));
    
    ld.raLocate(dm.getLogotipovi(), "CORG", OrgStr.getKNJCORG(false));
    if (presBlag.isSkladOriented())
      if (!ld.raLocate(dm.getLogotipovi(), "CORG", ms.getString("CSKL")))
        ld.raLocate(dm.getLogotipovi(), "CORG", OrgStr.getKNJCORG(false));
    
    String oibf = dm.getLogotipovi().getString("OIB");
    System.out.println(dm.getLogotipovi());
    
    DataRow usr = ld.raLookup(dM.getDataModule().getUseri(),"CUSER", ms.getString("CUSER"));
    String oibu = usr.getString("OIB");
    
    if (!presBlag.isUserOriented()) {
      DataRow blag = ld.raLookup(dM.getDataModule().getBlagajnici(), "CBLAGAJNIK", ms.getString("CBLAGAJNIK"));
      if (blag != null && blag.getString("OIB").length() > 0) oibu = blag.getString("OIB");
    }
    
    String nacpl = ms.getString("CNACPL");
    DataSet rate = Rate.getDataModule().getTempSet(Condition.whereAllEqual(key, ms));
    rate.open();
    if (rate.getRowCount() == 1)
      nacpl = rate.getString("CNACPL");
    else if (rate.getRowCount() > 1)
      nacpl = "O";
    
    if (nacpl.length() == 0 || nacpl.equalsIgnoreCase("G") || nacpl.equalsIgnoreCase("N"))
      nacpl = "G";
    else if (nacpl.equalsIgnoreCase("C") || nacpl.equalsIgnoreCase("È"))
      nacpl = "C";
    else if (nacpl.equalsIgnoreCase("K") || nacpl.startsWith("K"))
      nacpl = "K";
    else if (nacpl.equalsIgnoreCase("T") || nacpl.startsWith("V"))
      nacpl = "T";
    else nacpl = "O";

    RacunType rac = presBlag.getFis(ms).createRacun(
        oibf, //oib firme (Rest Art) NE PREPISUJ!!
        presBlag.isFiskPDV(ms), //da li je obveznik pdv-a 
        ms.getTimestamp("DATDOK"), // datum i vrijeme kreiranja racuna
        presBlag.isFiskSep(ms) ? "N" : "P", // oznaka slijednosti
        ms.getInt("FBR"), // broj racuna 
        ms.getString("FPP"), // oznaka poslovne jedinice
        ms.getInt("FNU"), // oznaka naplatnog mjesta
//        presBlag.getFiskPP(ms), // oznaka poslovne jedinice
//        presBlag.isFiskGot(ms) ? presBlag.getFiskNapG(ms) : presBlag.getFiskNap(ms), // oznaka naplatnog mjesta
        new BigDecimal(25), //stopa pdv-a 
        (BigDecimal) osnmap.get(pdv25), //osnovica za pdv
        (BigDecimal) izmap.get(pdv25), //iznos pdv-a
        new BigDecimal(3), //stopa pnp-a
        osnpnp, //osnovica za pnp
        izpnp, //iznos pnp 
        null, //naziv naknade - defaults to 'Povradna naknada' 
        povn, //iznos naknade
        tot, //ukupan iznos racuna
        nacpl,//nacin placanja
        oibu,//oib prodavatelja (Ja) NE PREPISUJ!!
        false //da li je naknadna dostava
     );
    
    if (izmap.size() > 1 || (izmap.size() == 1 && izmap.containsKey(pdv25))) {
      for (Iterator i = izmap.keySet().iterator(); i.hasNext(); ) {
        BigDecimal post = (BigDecimal) i.next();
        if (post.compareTo(pdv25) == 0) continue;
        PorezType por = presBlag.getFis(ms).getFisFactory().createPorezType();
        por.setStopa(post);
        por.setOsnovica((BigDecimal) osnmap.get(post));
        por.setIznos((BigDecimal) izmap.get(post));
        if (rac.getPdv() == null)
          rac.setPdv(presBlag.getFis(ms).getFisFactory().createPdvType());

        rac.getPdv().getPorez().add(por);
        System.out.println(por);
      }
    }
    
    return rac;
  }
  
  public static void fisk(DataSet ms) {
    if (presBlag.isFiskal(ms) && (!ms.getString("FOK").equals("D") || ms.getString("JIR").length() == 0)) {
      
      try {
        if (!ms.getString("FOK").equals("D")) {
          String cOpis = presBlag.getSeqOpis(ms);          
          ms.setInt("FBR", Valid.getValid().findSeqInt(cOpis, true, false));
          ms.setString("FOK", "D");
          ms.setString("FPP", presBlag.getFiskPP(ms));
          ms.setInt("FNU", presBlag.isFiskGot(ms) ? presBlag.getFiskNapG(ms) : presBlag.getFiskNap(ms));
          String resetSysdat = frmParam.getParam("robno", "fiskDatumGRC", "N",
            "Postaviti datum kod fiskalizacije GRC (D,N)");
          if (resetSysdat.equals("D"))
            ms.setTimestamp("DATDOK", Valid.getValid().getToday());
          ms.saveChanges();
        }
        
   
  Timestamp datvri = new Timestamp(System.currentTimeMillis());
  RacunZahtjev zahtj = presBlag.getFis("GRC", ms.getString("CSKL")).createRacunZahtjev(
          presBlag.getFis("GRC", ms.getString("CSKL")).createZaglavlje(datvri, null), 
          getRacType(ms));
        
        String jir = presBlag.getFis("GRC", ms.getString("CSKL")).fiskaliziraj(zahtj);
        if (jir != null && jir.length() > 0 && !jir.startsWith("ZKI") && !jir.startsWith("false"))
          ms.setString("JIR", jir);
        else ms.setString("JIR", "");
        ms.saveChanges();
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }
  }
  
  public void pressF5(char mode) {
  	System.out.println("press F5");
  	checkUnos(mode);
/*    if (getMasterSet().getBigDecimal("UIRAC").doubleValue()==0) {
      JOptionPane.showConfirmDialog(null,"Nema upisanih stavaka !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      if (mode=='B') raDetail.rnvAdd_action();
      return;
    }*/
  	if (getDetailSet().getRowCount()>0) {
  	  if (allowEdit && !checkRate()) return;
  	  justExit=true;
  	  makeNext=newRacun;
  	} else return;
  	
  	joinAll();
  	
  	System.out.println("FISKALIZACIJA?");
  	fisk(getMasterSet());
  	
  	justPrintGRC();
    raDetail.rnvExit_action();
//    pressF10('B');
//    _Main.getStartFrame().showFrame("hr.restart.robno.dlgPrintPOS", "Spremanje i ispis raèuna");
/*
    if (getMasterSet().getInt("CKUPAC")==0) {
      rpos= new repRacunPOS();
    }
    else {
      rpos= new repRacunR1POS();
    }
    hr.restart.util.reports.mxRM mxrm = hr.restart.util.reports.mxRM.getDefaultMxRM();
//    mxrm.setPrintCommand("hrconv 2 4 < # > lpt1");


//    mxrm.setPrintCommand("notepad #");
    mxrm.setPrintCommand("");
    rpos.setRM(mxrm);
    rpos.makeReport();
//    rpos.print();
    dlgFileViewer d = new dlgFileViewer("ispis.txt", false);
    d.setStandAlone(true);
    d.setSize(400,500);
    d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    d.reload();
    d.show();
    pressF10('B');
//    if (mode=='U') pressF10(mode);
//    else if ((mode=='I') && (newRacun)) pressF10(mode);
 */
  }

  public void justPrintGRC() {
    int maxGrcF5 = Integer.parseInt(hr.restart.sisfun.frmParam.getParam("pos", "GRC_kom_int", "1", "Broj raèuna kod ispisa na F5 iz unosa POS-a",true));
    String oneTimeReportName = "hr.restart.robno.repRacunPOS";
    if (repFISBIH.isFISBIH() && frmParam.getParam("robno", "FISBIHdirPos", "N", "Koristiti BiH fiskalni raèun za direktni ispis sa POS-a", true).equalsIgnoreCase("D")) {
      oneTimeReportName = "hr.restart.robno.repFISBIHPos";
    }
      for (int print = 1; print <= maxGrcF5; print++) {
        raDetail.getRepRunner().setOneTimeDirectReport(oneTimeReportName);
        raDetail.Funkcija_ispisa();
        try {
          Thread.sleep(250);
        } catch (InterruptedException e) {
          // blah
        }
      }
  }
  void calcGlobalPopust() {
    tmpPOP=util.nul;
    getMasterSet().setBigDecimal("UIPOPUST2", util.multiValue(getMasterSet().getBigDecimal("IZNOS"), util.divideValue(getMasterSet().getBigDecimal("UPPOPUST2"), util.sto)));
    getMasterSet().setBigDecimal("NETO", util.negateValue(getMasterSet().getBigDecimal("IZNOS"), getMasterSet().getBigDecimal("UIPOPUST2")));
    getMasterSet().setBigDecimal("UIRAC", getMasterSet().getBigDecimal("NETO"));
    getMasterSet().setBigDecimal("UIPOPUST1", util.negateValue(getMasterSet().getBigDecimal("UKUPNO"), getMasterSet().getBigDecimal("IZNOS")));
    getDetailSet().first();
    do {
      ld.raLocate(dm.getArtikli(), "CART", Integer.toString(getDetailSet().getInt("CART")));
  	  ld.raLocate(dm.getPorezi(), "CPOR", dm.getArtikli().getString("CPOR"));
  	  getDetailSet().setBigDecimal("PPOPUST2", getMasterSet().getBigDecimal("UPPOPUST2"));
      getDetailSet().setBigDecimal("IPOPUST2", util.multiValue(getDetailSet().getBigDecimal("IZNOS"), util.divideValue(getDetailSet().getBigDecimal("PPOPUST2"), util.sto)));
      getDetailSet().setBigDecimal("NETO", util.negateValue(getDetailSet().getBigDecimal("IZNOS"), getDetailSet().getBigDecimal("IPOPUST2")));
      
      BigDecimal sub = Aus.zero0;
      if ("D".equals(dm.getArtikli().getString("POV")))
        sub = pov.multiply(getDetailSet().getBigDecimal("KOL")).setScale(2, BigDecimal.ROUND_HALF_UP);
      BigDecimal osnovica = new BigDecimal(getDetailSet().getBigDecimal("NETO").subtract(sub).doubleValue()/((100+dm.getPorezi().getBigDecimal("UKUPOR").doubleValue())/100));
//    getDetailSet().setBigDecimal("POR1", util.findIznos(getDetailSet().getBigDecimal("IZNOS"), dm.getPorezi().getBigDecimal("UNPOR1")));
//    getDetailSet().setBigDecimal("POR2", util.findIznos(getDetailSet().getBigDecimal("IZNOS"), dm.getPorezi().getBigDecimal("UNPOR2")));
//    getDetailSet().setBigDecimal("POR3", util.findIznos(getDetailSet().getBigDecimal("IZNOS"), dm.getPorezi().getBigDecimal("UNPOR3")));
    getDetailSet().setBigDecimal("PPOR1", dm.getPorezi().getBigDecimal("POR1"));
    getDetailSet().setBigDecimal("PPOR2", dm.getPorezi().getBigDecimal("POR2"));
    getDetailSet().setBigDecimal("PPOR3", dm.getPorezi().getBigDecimal("POR3"));
    getDetailSet().setBigDecimal("POR1", util.findIznos(osnovica, dm.getPorezi().getBigDecimal("POR1")));
    getDetailSet().setBigDecimal("POR2", util.findIznos(osnovica, dm.getPorezi().getBigDecimal("POR2")));
    getDetailSet().setBigDecimal("POR3", util.findIznos(osnovica, dm.getPorezi().getBigDecimal("POR3")));
      
      
      
      
/*      getDetailSet().setBigDecimal("POR1", util.findIznos(getDetailSet().getBigDecimal("NETO"), dm.getPorezi().getBigDecimal("UNPOR1")));
      getDetailSet().setBigDecimal("POR2", util.findIznos(getDetailSet().getBigDecimal("NETO"), dm.getPorezi().getBigDecimal("UNPOR2")));
      getDetailSet().setBigDecimal("POR3", util.findIznos(getDetailSet().getBigDecimal("NETO"), dm.getPorezi().getBigDecimal("UNPOR3")));
      getDetailSet().setBigDecimal("PPOR1", dm.getPorezi().getBigDecimal("POR1"));
      getDetailSet().setBigDecimal("PPOR2", dm.getPorezi().getBigDecimal("POR2"));
      getDetailSet().setBigDecimal("PPOR3", dm.getPorezi().getBigDecimal("POR3"));*/
//      System.out.println("CART: "+getDetailSet().getInt("CART")+" pravi: "+jpBl.jrfCART.getRaDataSet().getInt("CART"));
      //getDetailSet().setString("CART1", jpBl.jrfCART.getRaDataSet().getString("CART1"));
//      getDetailSet().setString("BC", jpBl.jrfCART.getRaDataSet().getString("BC"));
//      getDetailSet().setString("JM", jpBl.jrfCART.getRaDataSet().getString("JM"));

      tmpPOP=util.sumValue(tmpPOP, getDetailSet().getBigDecimal("IPOPUST2"));
      getDetailSet().next();
    } while (getDetailSet().inBounds());
    getDetailSet().first();
    getDetailSet().setBigDecimal("IPOPUST2", util.sumValue(getDetailSet().getBigDecimal("IPOPUST2"), util.negateValue(getMasterSet().getBigDecimal("UIPOPUST2"), tmpPOP)));
    getDetailSet().setBigDecimal("NETO", util.negateValue(getDetailSet().getBigDecimal("IZNOS"), getDetailSet().getBigDecimal("IPOPUST2")));
  }
  void globalPopust() {
    getMasterSet().setBigDecimal("UIPOPUST2", util.multiValue(getMasterSet().getBigDecimal("IZNOS").setScale(2), util.divideValue(getMasterSet().getBigDecimal("UPPOPUST2"), util.sto)));
    getMasterSet().setBigDecimal("NETO", util.negateValue(getMasterSet().getBigDecimal("IZNOS"), getMasterSet().getBigDecimal("UIPOPUST2")));
    getMasterSet().setBigDecimal("UIRAC", getMasterSet().getBigDecimal("NETO"));
    getMasterSet().setBigDecimal("UIPOPUST1", util.negateValue(getMasterSet().getBigDecimal("UKUPNO"), getMasterSet().getBigDecimal("IZNOS")));
    raMaster.getJpTableView().getMpTable().repaint();
  }

  public String PrepSql(String vrdok){
    String sqldodat= "";
      sqldodat="and cskl='"+getPreSelect().getSelRow().getString("CSKL")+
               "' and vrdok='GRC"+
               "' and brdok = '"+getMasterSet().getInt("BRDOK")+
               "' and god = '"+getMasterSet().getString("GOD")+"'";
    return sqldodat;
  }
  
  public DataSet getNarSet() {
    return narSet;
  }

  public String getDestination() {
    return narDest;
  }
  
  public static String getRacDestination() {
    return racDest;
  }
  
  public static void setRacDestination(String dest) {
    racDest = dest;
  }
  
  public static void setRacDestination() {
    racDest = hr.restart.sisfun.frmParam.getParam(
        "sisfun", "printerRMcmnd", "1", "Radno mjesto", true);
  }
  
  public static String getStol() {
    DataSet mast = frm == null ? alterMaster : frm.getMasterSet();
    try {
      DataSet ds = Aus.q("SELECT * FROM places WHERE id='" +
            mast.getString("STOL")+"'");
      if (ds.rowCount() > 0)
        return ds.getString("NAME");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return mast.getString("STOL");
  }


  public void Funkcija_ispisa_master(){

    if (raMaster.getSelectionTracker() != null && raMaster.getSelectionTracker().countSelected() > 1) {
      Integer[] brdoks = (Integer[]) raMaster.getSelectionTracker().getSelection();
      for (int i = 0; i < brdoks.length; i++) {
        ld.raLocate(getMasterSet(), "BRDOK", brdoks[i].toString());
        justPrintGRC();
      }
    } else
//    reportsQuerysCollector.getRQCModule().ReSql(PrepSql("GRC"),"GRC");
      super.Funkcija_ispisa_master();
  }

/*  public void Funkcija_ispisa_master(){
    pressF5('A');
  }*/
  public void Funkcija_ispisa_detail(){
    setRacDestination();
    pressF5('B');
    //super.Funkcija_ispisa_detail();
  }
  void showLabel() {
    String nazskl = " - ";
    //QueryDataSet mpskl = hr.restart.robno.Util.getMPSklDataset();
    /*if (mpskl.getRowCount() > 0) {
      nazskl += mpskl.getString("NAZSKL");
    } else*/ if (lookupData.getlookupData().raLocate(dm.getSklad(), "CSKL", cskl)) {
      nazskl += dm.getSklad().getString("NAZSKL");
    } else {
      nazskl = "";
    }
    
    jpBl.jLabel10.setText(hr.restart.zapod.OrgStr.getOrgStr().getKnjigovodstva().getString("NAZIV")+nazskl);
  }
  public void ZatvoriOstaloDetail() {
  	allReadyRun=false;
    if (autonext && makeNext && newRacun) {
      newRacun = false;
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          if (!presBlag.stolovi)
            raMaster.rnvAdd_action();          
        }
      });
    }
    else {
      newRacun = false;
      if (getMasterSet().rowCount() > 0 && getMasterSet().inBounds())
        try {
          getMasterSet().refetchRow(getMasterSet());
        } catch (DataSetException e) {
          e.printStackTrace();
          getMasterSet().refresh();
        }
        updatePlac();
      raMaster.getJpTableView().fireTableDataChanged();
    }
    makeNext=false;
    justExit = false;
//      }
//    });

  }
  
  boolean checkKol() {
    int cart = getDetailSet().getInt("CART");
    BigDecimal oldk = Aus.zero3;
    for (olds.first(); olds.inBounds(); olds.next())
      if (olds.getInt("CART") == cart)
          oldk = oldk.add(olds.getBigDecimal("KOL"));
    DataRow dr = new DataRow(getDetailSet(), new String[] {"CART", "KOL"});
    for (int i = 0; i < getDetailSet().rowCount(); i++)
      if (i != getDetailSet().getRow()) {
        getDetailSet().getDataRow(i, dr);
        if (dr.getInt("CART") == cart)
          oldk = oldk.subtract(dr.getBigDecimal("KOL"));
      }
    
    System.out.println("prethodno upisano: "+oldk);
    
    boolean rezkol = frmParam.getParam("pos", "rezKolPos", "D",
      "POS rezervira stanje artikala (D,N)").equalsIgnoreCase("D");
    BigDecimal kolAvail = rezkol ? kolNaStanju.subtract(kolRez) : kolNaStanju;
    System.out.println("avail: "+kolAvail);
    if (kolAvail.add(oldk).compareTo(getDetailSet().getBigDecimal("KOL")) < 0)
      if (JOptionPane.showConfirmDialog(raDetail.getWindow(), 
          "Kolièina veæa od kolièine artikla na zalihi,\n" +
          "Želite li nastaviti ?", "Upozorenje", JOptionPane.OK_CANCEL_OPTION, 
          JOptionPane.QUESTION_MESSAGE) != JOptionPane.OK_OPTION) 
          return false;

    return true;
  }
  
  public void ZatvoriOstaloMaster() {
  }
  
  void showTotalArt() {
  	if (getMasterSet().getRowCount() == 0) return;
  	
  	VarStr q = new VarStr(getMasterSet().getOriginalQueryString().toUpperCase());
    q.replace("* FROM POS", "stpos.cskl, stpos.cart1, stpos.nazart, stpos.kol, stpos.mc, stpos.neto from pos,stpos");
    q.replace(" WHERE ", " where " + Util.getUtil().getDoc("pos", "stpos") + " and ");
    DataSet ds = Aus.q(q.toString());
    ds.setSort(new SortDescriptor(new String[] {"CART1"}));
    
    StorageDataSet res = new StorageDataSet();
    res.setColumns(new Column[] {
    		dM.createStringColumn("CORG", "Dobavljaè", 12),
    		dM.createStringColumn("CART1", "Šifra", 20),
        dM.createStringColumn("NAZART", "Naziv artikla", 100),
        dM.createBigDecimalColumn("KOL", "Kolièina", 3),
        dM.createBigDecimalColumn("MC", "Cijena", 2),
        dM.createBigDecimalColumn("NETO", "Neto", 2)
    });
    res.open();
    String cart = "";
    for (ds.first(); ds.inBounds(); ds.next()) {
      if (!ds.getString("CART1").equals(cart)) {
      	cart = ds.getString("CART1");
        res.insertRow(false);
        res.setString("CORG", ds.getString("CSKL"));
        res.setString("CART1", cart);
        res.setString("NAZART", ds.getString("NAZART"));
        Aus.set(res, "MC", ds);
      }
      Aus.add(res, "KOL", ds);
      Aus.add(res, "NETO", ds);
    }
    
    viewArt.setDataSet(res);
    viewArt.setSums(new String[] {"NETO"});
    viewArt.setSaveName("Pregled-blag-art");
    viewArt.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    viewArt.setTitle("Prikaz prometa po artiklima  od " + 
              Aus.formatTimestamp(getPreSelect().getSelRow().getTimestamp("DATDOK-from")) + " do " +
              Aus.formatTimestamp(getPreSelect().getSelRow().getTimestamp("DATDOK-to")));
    viewArt.setVisibleCols(new int[] {1, 2, 3, 4, 5});
    raExtendedTable t = (raExtendedTable) viewArt.jp.getMpTable();
    t.setForcePage(true);
    t.addToGroup("CORG", true, new String[] {"#", "NAZIVLOG", "#\n", "ADRESA", "#,", "PBR", "MJESTO", "#, OIB", "OIB"}, 
    		dM.getDataModule().getLogotipovi(), true);
    viewArt.show();
  }
  
  void updatePlac() {
    if (!presBlag.isSkladOriented()) return;
    if (getMasterSet().getRowCount() == 0) {
      totalSet.empty();
      totalTab.fireTableDataChanged();
      return;
    }
    try {
      totalSet.enableDataSetEvents(false);
      totalTab.stopFire();
      VarStr q = new VarStr(getMasterSet().getOriginalQueryString().toUpperCase());
      q.replace("* FROM POS", "rate.cskl, rate.cnacpl, rate.cbanka, rate.irata from pos,rate");
      q.replace(" WHERE ", " where " + Util.getUtil().getDoc("pos", "rate") + " and ");
      DataSet ds = Aus.q(q.toString());
      ds.setSort(new SortDescriptor(new String[] {"CNACPL", "CBANKA"}));
      String cnacpl = "", cbanka = "";
      totalSet.first();
      int n = 0;
      for (ds.first(); ds.inBounds(); ds.next()) {
        if (!ds.getString("CNACPL").equals(cnacpl) ||
            !ds.getString("CBANKA").equals(cbanka)) {
          cnacpl = ds.getString("CNACPL");
          cbanka = ds.getString("CBANKA");
          if (++n > totalSet.rowCount()) totalSet.insertRow(false);
          else if (n > 1) totalSet.next();
          ld.raLocate(dm.getNacpl(), "CNACPL", cnacpl);
          String nac = cnacpl + " - " + dm.getNacpl().getString("NAZNACPL");
          if (cbanka.length() > 0) nac = nac + " - " + cbanka;
          totalSet.setString("NACPL", nac);
          Aus.clear(totalSet, "IRATA");
        }
        Aus.add(totalSet, "IRATA", ds);
      }
      while (totalSet.inBounds() && totalSet.rowCount() > n)
        totalSet.emptyRow();
      totalTab.clearSelection();
    } finally {
      totalSet.enableDataSetEvents(true);
      totalTab.startFire();
      totalTab.revalidate();
      totalTab.repaint();
    }
  }
  
  void showTotalPlac() {
    if (getMasterSet().getRowCount() == 0) return;
    
    VarStr q = new VarStr(getMasterSet().getOriginalQueryString().toLowerCase());
    q.replace("* from pos", "rate.cskl, rate.cnacpl, rate.cbanka, rate.irata from pos,rate");
    q.replace(" where ", " where " + Util.getUtil().getDoc("pos", "rate") + " and ");
    DataSet ds = Aus.q(q.toString());
    ds.setSort(new SortDescriptor(new String[] {"CNACPL", "CBANKA"}));
    
    StorageDataSet res = new StorageDataSet();
    res.setColumns(new Column[] {
    		dM.createStringColumn("CORG", "Dobavljaè", 12),
        dM.createStringColumn("NACPL", "Naèin plaæanja", 50),
        dM.createStringColumn("BANKA", "Kartièar", 50),
        dM.createBigDecimalColumn("IRATA", "Iznos naplate")
    });
    res.open();
    
    String cnacpl = "", cbanka = "";
    for (ds.first(); ds.inBounds(); ds.next()) {
      if (!ds.getString("CNACPL").equals(cnacpl) ||
          !ds.getString("CBANKA").equals(cbanka)) {
        cnacpl = ds.getString("CNACPL");
        cbanka = ds.getString("CBANKA");
        res.insertRow(false);
        ld.raLocate(dm.getNacpl(), "CNACPL", cnacpl);
        res.setString("NACPL", cnacpl + " - " + dm.getNacpl().getString("NAZNACPL"));
        res.setString("CORG", ds.getString("CSKL"));
        if (cbanka.length() > 0) {
          ld.raLocate(dm.getKartice(), "CBANKA", cbanka);
          res.setString("BANKA", cbanka + " - " + dm.getKartice().getString("NAZIV"));
        }
      }
      Aus.add(res, "IRATA", ds);
    }
    
    viewPlac.setDataSet(res);
    viewPlac.setSums(new String[] {"IRATA"});
    viewPlac.setSaveName("Pregled-blag-plac");
    viewPlac.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    viewPlac.setTitle("Prikaz prometa po vrsti naplate  od " + 
              Aus.formatTimestamp(getPreSelect().getSelRow().getTimestamp("DATDOK-from")) + " do " +
              Aus.formatTimestamp(getPreSelect().getSelRow().getTimestamp("DATDOK-to")));
    viewPlac.setVisibleCols(new int[] {1, 2, 3});
    raExtendedTable t = (raExtendedTable) viewPlac.jp.getMpTable();
    t.setForcePage(true);
    t.addToGroup("CORG", true, new String[] {"#", "NAZIVLOG", "#\n", "ADRESA", "#,", "PBR", "MJESTO", "#, OIB", "OIB"}, 
    		dM.getDataModule().getLogotipovi(), true);
    viewPlac.show();
  }
  
  String[] reqc = {"CART", "CART1", "BC", "NAZART", "JM", "KOL"};
  void showRequirementsMaster() {
    if (getMasterSet().getRowCount() == 0) return;
    
    final StorageDataSet reqs = stdoki.getDataModule().getScopedSet(
    "CSKL CART CART1 BC NAZART JM KOL NC INAB");
    reqs.open();
    
    raProcess.runChild(raMaster.getWindow(), new Runnable() {
      public void run() {
          int row = getMasterSet().getRow();
          raMaster.getJpTableView().enableEvents(false);

          for (getMasterSet().first(); getMasterSet().inBounds(); getMasterSet().next())
            if ("arh".equals(getMasterSet().getString("RDOK"))) {
              DataSet ds = Stpos.getDataModule().getTempSet(
                  Condition.whereAllEqual(key, getMasterSet()));
              raProcess.openScratchDataSet(ds);
              fillRequirements(reqs, ds);
              ds.close();
            }
          getMasterSet().goToRow(row);
          raMaster.getJpTableView().enableEvents(true);
        
        String[] cols = {"GOD", "CART"};
        String god = vl.getKnjigYear("robno");
        System.out.println(god);
        for (reqs.first(); reqs.inBounds(); reqs.next()) {
          raProcess.checkClosing();
          int cart = reqs.getInt("CART");
          DataSet st = Stanje.getDataModule().getTempSet(Condition.whereAllEqual(cols, 
              new Object[] {god, new Integer(cart)}));
          st.open();
          if (st.rowCount() > 0) {
            if (!ld.raLocate(st, "CSKL", reqs.getString("CSKL"))) st.first();
            reqs.setBigDecimal("NC", st.getBigDecimal("NC"));
          } else {
            DataSet art = Artikli.getDataModule().getTempSet(Condition.equal("CART", cart));
            art.open();
            reqs.setBigDecimal("NC", art.getBigDecimal("NC"));
          }
          reqs.setBigDecimal("INAB", util.multiValue(reqs.getBigDecimal("KOL"), 
              reqs.getBigDecimal("NC")));
        }
      }
    });
    
    if (!raProcess.isCompleted()) return;
    
    viewReq.setDataSet(reqs);
    viewReq.setSums(new String[] {"INAB"});
    viewReq.setSaveName("Pregled-arh");
    viewReq.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    viewReq.setTitle("Prikaz arhivirane potrošnje materijala  za odabrano razdoblje");
    viewReq.setVisibleCols(new int[] {0, Aut.getAut().getCARTdependable(1, 2, 3), 4, 5, 6, 7, 8});
    viewReq.show();
  }
  
  void fillRequirements(StorageDataSet store, DataSet ds) {
    for (ds.first(); ds.inBounds(); ds.next()) {
      DataSet exp = Aut.getAut().expandArt(ds, true);
      if (exp == null) addToExpanded(store, ds, ds.getString("CSKL"));
      else 
        for (exp.first(); exp.inBounds(); exp.next())
          addToExpanded(store, exp, ds.getString("CSKL"));
    }
  }
  
  void addToExpanded(StorageDataSet expanded, DataSet art, String cskl) {
    if (!ld.raLocate(expanded, "CART", Integer.toString(art.getInt("CART")))) {
      expanded.last();
      expanded.insertRow(false);
      dM.copyColumns(art, expanded, reqc);
    } else expanded.setBigDecimal("KOL", 
        expanded.getBigDecimal("KOL").add(art.getBigDecimal("KOL")));
    expanded.setString("CSKL", cskl);
    expanded.post();
  }
  
  public static DataSet getAlterMaster() {
    return alterMaster;
  }

  
  public static void setAlterMaster(DataSet alter) {
    alterMaster = alter;
  }

  
  public static DataSet getAlterDetail() {
    return alterDetail;
  }

  
  public static void setAlterDetail(DataSet alter) {
    alterDetail = alter;
  }

  /**
   * Spremanje globalnog popusta i njegovo rasporedjivanje po stavkama
   */

}
