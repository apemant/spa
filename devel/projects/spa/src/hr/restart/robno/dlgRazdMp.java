/****license*****************************************************************
**   file: dlgRazdMp.java
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
package hr.restart.robno;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.LinkClass;
import hr.restart.util.OKpanel;
import hr.restart.util.Valid;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.startFrame;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.MetaDataUpdate;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * Title:        Robno poslovanje
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      REST-ART
 * @author REST-ART development team
 * @version 1.0
 */

public class dlgRazdMp extends JraDialog {
//  static dlgRazdMp dRazdMp;
        raControlDocs rCD = new raControlDocs();
  com.borland.dx.sql.dataset.QueryDataSet tQds;
//  static raPOS fdi;
  Rbr rbr = Rbr.getRbr();
  hr.restart.util.sysoutTEST st = new hr.restart.util.sysoutTEST(false);
  hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
  hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
//  java.sql.Date dateP = null;
  JPanel panel1 = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JlrNavField jrfNAZSKL = new JlrNavField();
  JraButton jbCSKL = new JraButton();

  JlrNavField  jrfCPAR = new JlrNavField();
  JlrNavField jrfNAZPAR = new JlrNavField();
  JraButton jbCPAR = new JraButton();
  JLabel    jlCPAR = new JLabel("Zajedni\u010Dki partner");

  JLabel jLabel6 = new JLabel();
  JLabel jLabel5 = new JLabel();
  JlrNavField jrfCSKL = new JlrNavField();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JraTextField jtfPocDatum = new JraTextField();
  TableDataSet tds = new TableDataSet();
  Column column1 = new Column();
  dM dm;
  Valid vl;
  QueryDataSet stanjeforChange = new QueryDataSet();
  LinkClass lc = LinkClass.getLinkClass();
  raKalkulBDDoc rKD = new raKalkulBDDoc();

  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      pressOK();
    }
    public void jPrekid_actionPerformed() {
      pressCancel();
    }
  };
  JLabel jLabel3 = new JLabel();
  JraButton jbCSKL1 = new JraButton();
  JlrNavField jrfNAZSKL1 = new JlrNavField();
  JlrNavField jrfCSKL1 = new JlrNavField();
  public dlgRazdMp(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
//      dRazdMp=this;
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public dlgRazdMp() {
    this(_Main.getStartFrame(), "Razduženje maloprodaje", true);
  }
  /**
   * Statichki getter
   */
//  public static dlgRazdMp getdlgRadzMp(raPOS rpos) {
//    fdi=rpos;
//    if (dRazdMp == null) {
//      dRazdMp = new dlgRazdMp();
//    }
//    return dRazdMp;
//  }
  void jbInit() throws Exception {
    this.setModal(true);
    dm = hr.restart.baza.dM.getDataModule();
    vl = hr.restart.util.Valid.getValid();
    panel1.setLayout(xYLayout1);
    jrfNAZSKL.setEditable(false);
    jrfNAZSKL.setNavProperties(jrfCSKL);
    jrfNAZSKL.setSearchMode(1);
    jrfNAZSKL.setColumnName("NAZSKL");
    jbCSKL.setText(res.getString("jbKEYF9_text"));
    jLabel6.setText("Naziv");
    jLabel5.setText("Šifra");
    jrfCSKL.setColumnName("CSKL");
    jrfCSKL.setColNames(new String[] {"NAZSKL"});
    jrfCSKL.setVisCols(new int[]{0,1});
    jrfCSKL.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZSKL});
    jrfCSKL.setRaDataSet(hr.restart.robno.Util.getUtil().getMPSklDataset());
    jLabel1.setText("Skladište");
    jLabel2.setText("Datum");
// Zajedni\u010Dki partner

    jrfCPAR.setColumnName("CPAR");
    jrfCPAR.setColNames(new String[] {"NAZPAR"});
    jrfCPAR.setVisCols(new int[]{0,1});
    jrfCPAR.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZPAR});
    jrfCPAR.setRaDataSet(dm.getPartneri());
    jrfCPAR.setNavButton(jbCPAR);
    jbCPAR.setText("...");
    jrfNAZPAR.setEditable(false);
    jrfNAZPAR.setNavProperties(jrfCPAR);
    jrfNAZPAR.setSearchMode(1);
    jrfNAZPAR.setColumnName("NAZPAR");

// kraj tog ZP-a

    jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jtfPocDatum.setColumnName("pocDatum");
    jtfPocDatum.setDataSet(tds);
    column1.setColumnName("pocDatum");
    column1.setDataType(com.borland.dx.dataset.Variant.TIMESTAMP);
    column1.setDisplayMask("dd-MM-yyyy");
//    column1.setEditMask("dd-MM-yyyy");
    column1.setServerColumnName("NewColumn1");
    column1.setSqlType(0);
    tds.setColumns(new Column[] {column1});
    this.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        this_keyPressed(e);
      }
    });
    xYLayout1.setWidth(555);
    xYLayout1.setHeight(133);
    this.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        this_componentShown(e);
      }
    });
    jLabel3.setText("Prodajno mjesto");
    jbCSKL1.setText(res.getString("jbKEYF9_text"));
    jrfNAZSKL1.setColumnName("NAZSKL");
    jrfNAZSKL1.setSearchMode(1);
    jrfNAZSKL1.setNavProperties(jrfCSKL1);
    jrfNAZSKL1.setEditable(false);
    jrfCSKL1.setRaDataSet(hr.restart.robno.Util.getUtil().getMPSklDataset());
    jrfCSKL1.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZSKL1});
    jrfCSKL1.setVisCols(new int[]{0,1});
    jrfCSKL1.setColNames(new String[] {"NAZSKL"});
    jrfCSKL1.setColumnName("CSKL");
    this.getContentPane().add(panel1, BorderLayout.CENTER);
    this.getContentPane().add(okp, BorderLayout.SOUTH);

    panel1.add(jLabel5, new XYConstraints(150, 15, -1, -1));
    panel1.add(jLabel6, new XYConstraints(260, 15, -1, -1));


    panel1.add(jLabel1, new XYConstraints(15, 65, -1, -1));
    panel1.add(jlCPAR, new XYConstraints(15, 90, -1, -1));
    panel1.add(jLabel2, new XYConstraints(15, 115, -1, -1));
    panel1.add(jrfCSKL, new XYConstraints(150, 65, -1, -1));
    panel1.add(jrfCPAR, new XYConstraints(150, 90, -1, -1));
    panel1.add(jtfPocDatum, new XYConstraints(150, 115, -1, -1));
    panel1.add(jrfNAZSKL, new XYConstraints(260, 65, 255, -1));
    panel1.add(jrfNAZPAR, new XYConstraints(260, 90, 255, -1));
    panel1.add(jbCSKL, new XYConstraints(519, 65, 21, 21));
    panel1.add(jbCPAR, new XYConstraints(519, 90, 21, 21));
    panel1.add(jLabel3,   new XYConstraints(15, 40, -1, -1));
    panel1.add(jrfCSKL1,  new XYConstraints(150, 40, -1, -1));
    panel1.add(jbCSKL1, new XYConstraints(519, 40, 21, 21));
    panel1.add(jrfNAZSKL1, new XYConstraints(260, 40, 255, -1));
  }

  void this_keyPressed(KeyEvent e) {
    if (e.getKeyCode()==e.VK_F10) {
      pressOK();
    }
    else if (e.getKeyCode()==e.VK_ESCAPE) {
      pressCancel();
    }
  }
  void pressOK() {
    StatusAndPrikaz('S');
    String qStr;
    this.hide();
//    dateP = new java.sql.Date(dm.getDoki().getTimestamp("DatDok").getTime());
    qStr="select max(STPOS.CART) as CART, max(STPOS.NAZART) as NAZART, sum(STPOS.KOL) as KOL, sum(STPOS.IZNOS) as VRI, sum(STPOS.IPOPUST1)+sum(STPOS.IPOPUST2) as POPUST, sum(STPOS.POR1) as POR1, sum(STPOS.POR2) as POR2, sum(STPOS.POR3) as POR3, 0.00 as STANJE, 0.00 as ZALIHA, '   ' as JM "+
    "from STPOS, POS "+
    "where POS.CSKL='"+jrfCSKL.getText()+"' and POS.CSKL=STPOS.CSKL and POS.BRDOK=STPOS.BRDOK and POS.GOD=STPOS.GOD and POS.DATDOK<="+util.getTimestampValue(tds.getTimestamp("pocDatum"), util.NUM_LAST)+" and POS.STATUS='N' "+
    "group by STPOS.CART";
    System.out.println("Sql: "+qStr);
    vl.execSQL(qStr);
    vl.RezSet.setMetaDataUpdate(MetaDataUpdate.TABLENAME+MetaDataUpdate.PRECISION+MetaDataUpdate.SCALE+MetaDataUpdate.SEARCHABLE);
    vl.RezSet.setColumns(new Column[] {
      (Column) hr.restart.baza.dM.getDataModule().getStanje().getColumn("CART").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getArtikli().getColumn("NAZART").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getStanje().getColumn("KOL").clone(),
      (Column) hr.restart.baza.dM.getDataModule().getStanje().getColumn("VRI").clone()});
    vl.RezSet.open();
    tQds=vl.RezSet;
    tQds.open();
    if (vl.RezSet.rowCount()>0) {
      if (checkStanjeTG()) {
        if (copyPOS()) {
          StatusAndPrikaz('C');
          JOptionPane.showConfirmDialog(this,"Razduženje blagajne uspješno završeno !","Poruka",JOptionPane.DEFAULT_OPTION,JOptionPane.DEFAULT_OPTION);
        }
        else {
          StatusAndPrikaz('C');
          JOptionPane.showConfirmDialog(this,"Razduženje blagajne NE USPJEŠNO ! ! !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        }

//        qStr="update POS set STATUS='P' where POS.CSKL='"+jrfCSKL.getText()+"' and POS.DATDOK<="+util.getTimestampValue(tds.getTimestamp("pocDatum"), util.NUM_LAST)+" and POS.STATUS='N'";
//        vl.runSQL(qStr);
//        System.out.println("Sql: "+qStr);
      }
    }
    else {
      StatusAndPrikaz('C');

      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          porukaA();
        }
      });
    }
  }

  public void porukaA(){
          JOptionPane.showConfirmDialog(this,"Nema podataka za prijenos !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
  }


  void pressCancel() {
    this.hide();
  }
/*  boolean findPOS(frmDokIzlaz fdix) {
    fdi=fdix;
    this.pressOK();
//    this.show();
    return true;
  }*/

  void this_componentShown(ComponentEvent e) {
    jrfCPAR.setText("");
    jrfCPAR.emptyTextFields();
    jrfCSKL.setText("");
    jrfCSKL.emptyTextFields();
    jrfCSKL1.setText("");
    jrfCSKL1.emptyTextFields();
    tds.setTimestamp("pocDatum", vl.getToday());
    if (hr.restart.robno.Util.getUtil().getMPSklDataset().rowCount()==0) {
      JOptionPane.showConfirmDialog(null,"Nema definiranog maloprodajnog skladišta !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
      this.setVisible(false);
    }
    else if (hr.restart.robno.Util.getUtil().getMPSklDataset().rowCount()==1) {
      jrfCSKL.setText(hr.restart.robno.Util.getUtil().getMPSklDataset().getString("CSKL"));
      jrfNAZSKL.setText(hr.restart.robno.Util.getUtil().getMPSklDataset().getString("NAZSKL"));
      jrfCSKL1.setText(hr.restart.robno.Util.getUtil().getMPSklDataset().getString("CSKL"));
      jrfNAZSKL1.setText(hr.restart.robno.Util.getUtil().getMPSklDataset().getString("NAZSKL"));
      rcc.setLabelLaF(jrfCSKL,false);
      rcc.setLabelLaF(jrfNAZSKL,false);
      rcc.setLabelLaF(jbCSKL,false);
      rcc.setLabelLaF(jrfCSKL1,false);
      rcc.setLabelLaF(jrfNAZSKL1,false);
      rcc.setLabelLaF(jbCSKL1,false);
      jrfCPAR.requestFocus();
    }
    else {
      rcc.setLabelLaF(jrfCSKL,true);
      rcc.setLabelLaF(jrfNAZSKL,true);
      rcc.setLabelLaF(jbCSKL,true);
      jrfCSKL1.requestFocus();
    }
  }
/**
 * Provjera stanja i tarifne grupe za artikle koji se prenose u POS Otpremnicu
 */
  boolean checkStanjeTG() {
    String qStr;
    boolean lFirst=true;
    boolean lOk=true;
    tQds.first();
    do {
      if (!lFirst) {
        tQds.next();
      }
/**
 * Provjera da li ima artikl upisan na stanju
 */
      qStr="select STANJE.KOL as STANJE, STANJE.ZC as ZALIHA from STANJE where STANJE.CART="+tQds.getInt("CART")+" and STANJE.CSKL='"+jrfCSKL.getText()+"'";
      vl.execSQL(qStr);
      vl.RezSet.open();
      if (vl.RezSet.getRowCount()>0) {
        tQds.setBigDecimal("STANJE", vl.RezSet.getBigDecimal("STANJE"));
        tQds.setBigDecimal("ZALIHA", vl.RezSet.getBigDecimal("ZALIHA"));
        tQds.post();
/**
 * Provjera da li ima dovoljnu kolicinu na STANJU
 */
        if (tQds.getBigDecimal("KOL").doubleValue()<tQds.getBigDecimal("STANJE").doubleValue()) {
/**
 * Provjera da li postoji tarifna grupa za artikl
 */
          qStr="select POREZI.CPOR as CPOR, ARTIKLI.JM as JM from POREZI, ARTIKLI where ARTIKLI.CART="+tQds.getInt("CART")+" and POREZI.CPOR=ARTIKLI.CPOR";
          vl.execSQL(qStr);
          vl.RezSet.open();
          if (vl.RezSet.getRowCount()>0) {
            tQds.setString("JM", vl.RezSet.getString("JM"));
            tQds.post();
          }
          else {
            JOptionPane.showConfirmDialog(null,"Artikl: "+tQds.getString("NAZART")+" nema definirane porezne grupe !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
            lOk=false;
          }
        }
        else {
          System.out.println("isp: "+tQds.getBigDecimal("KOL")+", "+tQds.getBigDecimal("STANJE"));
          JOptionPane.showConfirmDialog(null,"Artikl: "+tQds.getString("NAZART")+" nema dovoljnu koli\u010Dinu na zalihi !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
          lOk=false;
        }
      }
      else {
        JOptionPane.showConfirmDialog(null,"Artikl: "+tQds.getString("NAZART")+" ne postoji na stanju !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        lOk=false;
      }
      lFirst=false;
    } while (!tQds.atLast());
    return lOk;
  }
/**
 * Kuporanje vrijednosti iz temporarnog dejtaseta u STDOKI
 */

  boolean copyPOS() {
//    dm.getStDokiPos().cancel();

    java.util.HashMap hmstanjekeys = new java.util.HashMap();

    for (tQds.first();tQds.inBounds();tQds.next()){
      hmstanjekeys.put(new Integer(tQds.getInt("CART")),new Integer(tQds.getInt("CART")));
    }

    java.util.ArrayList AL = new java.util.ArrayList(hmstanjekeys.values());

    hr.restart.util.VarStr myVarStr = new hr.restart.util.VarStr("in (");
    for (int i = 0;i<AL.size();i++) {
      myVarStr.append(((Integer)AL.get(i)).toString());
      myVarStr.append(",");
    }
    myVarStr.chopRight(1);
    myVarStr.append(")");

    String find_stanje = "select * from stanje where cskl='"+jrfCSKL.getText()+
                         "' and cart "+myVarStr.toString();

    stanjeforChange = hr.restart.util.Util.getNewQueryDataSet(find_stanje,true);

    short rbr = 0;

// dodaje zaglavlje
    dm.getZagPos().open();
    dm.getZagPos().insertRow(true);
    dm.getZagPos().setString("CSKL",jrfCSKL.getText());
    dm.getZagPos().setString("VRDOK","POS");
    dm.getZagPos().setString("GOD",hr.restart.util.Valid.getValid().findYear());
    int brdok =hr.restart.util.Valid.getValid().findSeqInt(jrfCSKL.getText()+
        "POS"+hr.restart.util.Valid.getValid().findYear(),false);
    dm.getZagPos().setInt("BRDOK",brdok);
    dm.getZagPos().setString("CUSER",hr.restart.sisfun.raUser.getInstance().getUser());
    dm.getZagPos().setTimestamp("DATDOK",tds.getTimestamp("pocDatum"));
    dm.getZagPos().setInt("CPAR",dm.getPartneri().getInt("CPAR"));
///

/// dodaje stavke

    dm.getStDokiPos().open();

    for (tQds.first();tQds.inBounds();tQds.next()){
      rbr++;
      rKD.stavkaold.Init();
      rKD.stanje.Init();
      rKD.stavka.Init();
      dm.getStDokiPos().insertRow(true);
      dm.getStDokiPos().setString("CSKL",jrfCSKL.getText());
      dm.getStDokiPos().setString("GOD",hr.restart.util.Valid.getValid().findYear());
      dm.getStDokiPos().setString("VRDOK","POS");
      dm.getStDokiPos().setInt("BRDOK",brdok);
      dm.getStDokiPos().setShort("RBR",rbr);
      dm.getStDokiPos().setInt("RBSID", rbr);
      dm.getStDokiPos().setInt("CART",tQds.getInt("CART"));
      dm.getStDokiPos().setString("NAZART",tQds.getString("NAZART"));
      if (hr.restart.util.lookupData.getlookupData().raLocate(dm.getArtikli(),new String[] {"CART"},
      new String[] {String.valueOf(tQds.getInt("CART"))})) {
        dm.getStDokiPos().setString("CART1",dm.getArtikli().getString("CART1"));
        dm.getStDokiPos().setString("BC",dm.getArtikli().getString("BC"));
        dm.getStDokiPos().setString("JM",dm.getArtikli().getString("JM"));
      }
      dm.getStDokiPos().setBigDecimal("KOL",tQds.getBigDecimal("KOL"));
      dm.getStDokiPos().setBigDecimal("IPRODSP",tQds.getBigDecimal("VRI"));
      dm.getStDokiPos().setBigDecimal("POR1",tQds.getBigDecimal("POR1"));
      dm.getStDokiPos().setBigDecimal("POR2",tQds.getBigDecimal("POR2"));
      dm.getStDokiPos().setBigDecimal("POR3",tQds.getBigDecimal("POR3"));
      dm.getStDokiPos().setBigDecimal("IPRODBP",
                                      tQds.getBigDecimal("VRI").add(
                                      tQds.getBigDecimal("POR1").
                                      add(tQds.getBigDecimal("POR2").
                                      add(tQds.getBigDecimal("POR3"))).negate()));
      dm.getStDokiPos().setBigDecimal("FC", dm.getStDokiPos().getBigDecimal("IPRODBP").divide(tQds.getBigDecimal("KOL"), 1));
      lc.TransferFromDB2Class(dm.getStDokiPos(),rKD.stavka);

      if (hr.restart.util.lookupData.getlookupData().raLocate(stanjeforChange,new String[] {"CART"},
      new String[] {String.valueOf(tQds.getInt("CART"))})) {
        lc.TransferFromDB2Class(stanjeforChange,rKD.stanje);
//          rKD.stanje.sVrSklad=DP.rpcart.AST.VrstaZaliha();
        rKD.stanje.sVrSklad="M";
        rKD.setWhat_kind_of_document("POS");
        rKD.kalkSkladPart();

        rCD.unosIzlaz(dm.getStDokiPos(),stanjeforChange);
        if (rKD.TestStanje() >0) {
          javax.swing.JOptionPane.showConfirmDialog(this,
              "Ne postoji dovoljna koli\u010Dina na zalihi za artikl "+tQds.getInt("CART")+"-"+
              tQds.getString("NAZART")+". Prenos nije uspio !","Greška !",
              javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.ERROR_MESSAGE);
          return false;
        }
        lc.TransferFromClass2DB(stanjeforChange,rKD.stanje);
        lc.TransferFromClass2DB(dm.getStDokiPos(),rKD.stavka);
      }
      else {
        javax.swing.JOptionPane.showConfirmDialog(this,
            "Ne postoji u stanje za artikl "+tQds.getInt("CART")+"-"+
            tQds.getString("NAZART")+". Prenos nije uspio !","Greška !",
            javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }

    return new raLocalTransaction(){
      public boolean transaction() throws Exception {
        try {
          saveChanges(dm.getSeq());
        } catch (Exception ex) {
          ex.printStackTrace();
          return false;
        }
        try {
          saveChanges(dm.getZagPos());
        } catch (Exception ex) {
          ex.printStackTrace();
          return false;
        }
        try {
          saveChanges(dm.getStDokiPos());
        } catch (Exception ex) {
          ex.printStackTrace();
          return false;
        }
        try {
          saveChanges(stanjeforChange);
        } catch (Exception ex) {
          ex.printStackTrace();
          return false;
        }

        try {
          String sqlupit = "select * from POS where POS.CSKL='"+jrfCSKL.getText()+
                    "' and POS.DATDOK<="+util.getTimestampValue(tds.getTimestamp("pocDatum"), util.NUM_LAST)+
              " and POS.STATUS='N'";
          QueryDataSet zastatus = hr.restart.util.Util.getNewQueryDataSet(sqlupit,true);
          for (zastatus.first();zastatus.inBounds();zastatus.next()){
            zastatus.setString("STATUS","P");
          }
          saveChanges(zastatus);
        }
        catch (Exception ex) {
          ex.printStackTrace();
          return false;
        }
        return true;
        }}.execTransaction();
//    return true;
  }

  public void StatusAndPrikaz(char what){

    if (what=='S') {
        new Thread() {
          public void run() {
            startFrame.getStartFrame().getStatusBar().startTask(10,"Razduženje blagajne u tijeku ...!");
          }
        }.start();
    }
    else if (what=='C') {
        new Thread() {
          public void run() {
            startFrame.getStartFrame().getStatusBar().finnishTask();
          }
        }.start();
    }


  }

}