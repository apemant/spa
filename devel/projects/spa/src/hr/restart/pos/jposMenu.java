/****license*****************************************************************
**   file: jposMenu.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Pos;
import hr.restart.baza.Stpos;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.robno.Util;
import hr.restart.robno.raControlDocs;
import hr.restart.robno.raPOS;
import hr.restart.robno.raPozivNaBroj;
import hr.restart.robno.repFISBIH;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.util.Aus;
import hr.restart.util.PreSelect;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raLoader;
import hr.restart.util.raProcess;
import hr.restart.util.raTransaction;
import hr.restart.util.startFrame;
import hr.restart.zapod.OrgStr;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author REST-ART development team
 * @version 1.0
 */

public class jposMenu extends JMenu {
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");
  hr.restart.util.startFrame SF;
  public JMenuItem jmBlagajna = new JMenuItem();
  public JMenuItem jmGOT = new JMenuItem();
  public JMenuItem jmRazduzenjePOSa = new JMenuItem();
  public JMenuItem jmRazduzenjePOS = new JMenuItem();
  public JMenuItem jmGRN = new JMenuItem();
  public JMenuItem jmRekapitulacijaPOS = new JMenuItem();
  public JMenuItem jmKartica = new JMenuItem();
  public JMenuItem jmStanje = new JMenuItem();
  public JMenuItem jmOdjava = new JMenuItem();
  public JMenuItem jmPregledArtikliRacuni = new JMenuItem();
  public JMenuItem jmZbroj = new JMenuItem();
  public JMenuItem jmZakljucak = new JMenuItem();
  public JMenuItem jmRacIspis = new JMenuItem();
  public JMenuItem jmKPR = new JMenuItem();
  public JMenuItem jmPregledKPR = new JMenuItem();
  public JMenuItem jmFISBIH = new JMenuItem();
  public JMenuItem jmArtPon = new JMenuItem();

  public jposMenu(hr.restart.util.startFrame startframe) {
    SF = startframe;
    jbInit();
    this.addAncestorListener(new javax.swing.event.AncestorListener() {
      public void ancestorAdded(javax.swing.event.AncestorEvent e) {
      }
      public void ancestorMoved(javax.swing.event.AncestorEvent e) {
      }
      public void ancestorRemoved(javax.swing.event.AncestorEvent e) {
      }
    });
  }

  private void jbInit() {
    this.setText("POS");
    jmBlagajna.setText("Blagajna");
    jmBlagajna.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmBlagajna_actionPerformed(e);
      }
    });
    jmGOT.setText("Gotovinski raèuni");
    jmGOT.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmGOT_actionPerformed(e);
      }
    });
    jmRazduzenjePOS.setText("Razduženja blagajni");
    jmRazduzenjePOS.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmRazduzenjePOS_actionPerformed(e);
      }
    });
    jmRazduzenjePOSa.setText("Razduženja blagajni");
    jmRazduzenjePOSa.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmRazduzenjePOSa_actionPerformed(e);
      }
    });

    jmGRN.setText("Gotovinski raèuni");
    jmGRN.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmGRN_actionPerformed(e);
      }
    });
    jmRekapitulacijaPOS.setText("Rekapitulacija uplata");
    jmRekapitulacijaPOS.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmRekapitulacijaPOS_actionPerformed(e);
      }
    });
    jmFISBIH.setText("Fiskalni izvještaji");
    jmFISBIH.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmFISBIH_actionPerformed(e);
      }
    });
    jmArtPon.setText("Dnevna ponuda");
    jmArtPon.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmArtPon_actionPerformed(e);
      }
    });
    jmKartica.setText("Kartica artikla");
    jmKartica.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmKartica_actionPerformed(e);
      }
    });
    jmStanje.setText("Stanje artikla");
    jmStanje.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmStanje_actionPerformed(e);
      }
    });
    jmOdjava.setText("Odjava komisije");
    jmOdjava.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
      	jmOdjava_actionPerformed(e);
      }
    });
    jmPregledArtikliRacuni.setText(presBlag.isSkladOriented() ? "Pregled prodaje" : "Pregled artikli raèuni");
    jmPregledArtikliRacuni.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPregledArtikliRacuni_actionPerformed(e);
      }
    });
    jmPregledKPR.setText("Knjiga popisa-KPR");
    jmPregledKPR.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPregledKPR_actionPerformed(e);
      }
    });
    jmZbroj.setText("Zbroj raèuna");
    jmZbroj.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        frmSumaPos.show();
      }
    });
    jmZakljucak.setText("Zakljuèak blagajne");
    jmZakljucak.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmZakljucak_actionPerformed(e);
      }
    });
    jmRacIspis.setText("Raèun za ispis");
    jmRacIspis.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmRacIspis_actionPerformed(e);
      }
    });
    jmKPR.setText("Formiranje KPR");
    jmKPR.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        frmAllKpr.show();
      }
    });
    this.add(jmBlagajna);
//    this.add(jmGOT);
//    this.add(jmRazduzenjePOSa);
    this.add(jmRazduzenjePOS);
//    this.add(jmGRN);
    this.addSeparator();
    this.add(jmRekapitulacijaPOS);
    this.add(jmPregledArtikliRacuni);
    if (presBlag.isSkladOriented()) {
        this.add(jmKartica);
        this.add(jmStanje);
        this.add(jmOdjava);
        this.add(jmPregledKPR);
    	this.addSeparator();
    	this.add(jmZakljucak);
    	this.add(jmKPR);
    	this.addSeparator();
      this.add(jmZbroj);
    } else {
      jmZakljucak.setText("Zakljuèak blagajne");
      this.add(jmZakljucak);
      this.add(jmRacIspis);
    }
    if (repFISBIH.isFISBIH()) {
      addSeparator();
      add(jmFISBIH);
    }
    if (frmParam.getParam("pos", "grupaPonuda", "", "Šifra grupe artikala za dnevnu ponudu").length() > 0) {
      addSeparator();
      add(jmArtPon);
    }
  }

  public void jmFISBIH_actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
    SF.showFrame("hr.restart.robno.FISBIHIzvjestaji", jmFISBIH.getText());
  }
  
  public void jmArtPon_actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
    SF.showFrame("hr.restart.pos.frmArtikliPonuda", jmArtPon.getText());
  }

  long lastTrans = 0;
  public void jmBlagajna_actionPerformed(ActionEvent e) {
    
    if (lastTrans == 0 || System.currentTimeMillis() > lastTrans + 1000 * 60 * 60 * 8)
    raProcess.runChild(SF, "Fiskalizacija", "Fiskalizacija nepotpuno zakljuèenih raèuna...", new Runnable() {    
      public void run() {
        lastTrans = System.currentTimeMillis();
        DataSet ms = Pos.getDataModule().getTempSet(Condition.equal("FOK", "D").and(Condition.emptyString("JIR").orNull()));
        ms.open();
        System.out.println("Fiskalizacija " + ms.rowCount() + " crvenih raèuna...");
        if (ms.rowCount() == 0) return;
        for (ms.first(); ms.inBounds(); ms.next()) {
          frmMasterBlagajna.fisk(ms);
        }
      }
    });
    
    PreSelect.showPreselect("hr.restart.pos.presBlag", "hr.restart.pos.frmMasterBlagajna",
                            jmBlagajna.getText(), false);
//    SF.showFrame("hr.restart.robno.dlgBeforePOS", res.getString("dlgBeforePOS_title"));
  }
  public void jmGOT_actionPerformed(ActionEvent e) {
    hr.restart.robno.raGOT ragot = (hr.restart.robno.raGOT)raLoader.load("hr.restart.robno.raGOT");
    hr.restart.robno.presGOT.getPres().showJpSelectDoc("GOT", ragot, true, jmGOT.getText());
  }
  public void jmRazduzenjePOS_actionPerformed(ActionEvent e) {
    hr.restart.robno.raPOS rapos = (hr.restart.robno.raPOS)raLoader.load("hr.restart.robno.raPOS");
    hr.restart.robno.presPOS.getPres().showJpSelectDoc("POS", rapos, true, jmRazduzenjePOS.getText());
  }
  public void jmRazduzenjePOSa_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmPos2POS", jmRazduzenjePOSa.getText());
  }
  public void jmGRN_actionPerformed(ActionEvent e) {
    hr.restart.robno.raGRN ragrn = (hr.restart.robno.raGRN)raLoader.load("hr.restart.robno.raGRN");
    hr.restart.robno.presGRN.getPres().showJpSelectDoc("GRN", ragrn, true, jmGRN.getText());
  }
  public void jmRekapitulacijaPOS_actionPerformed(ActionEvent e) {
//    SF.showFrame("hr.restart.robno.ispPOS_Total", jmRekapitulacijaPOS.getText());
    if (presBlag.isSkladOriented())
      SF.showFrame("hr.restart.pos.ispRekapNew", jmRekapitulacijaPOS.getText());
    else SF.showFrame("hr.restart.robno.ispRekapitulacijaRacunaPOS", jmRekapitulacijaPOS.getText());
  }
  public void jmKartica_actionPerformed(ActionEvent e){
    SF.showFrame("hr.restart.pos.upKarticaPos", jmKartica.getText());
  }
  public void jmStanje_actionPerformed(ActionEvent e){
    SF.showFrame("hr.restart.pos.upStanjePos", jmStanje.getText());
  }
  public void jmOdjava_actionPerformed(ActionEvent e){
    SF.showFrame("hr.restart.pos.upOdjava", jmOdjava.getText());
  }
  public void jmPregledArtikliRacuni_actionPerformed(ActionEvent e){
  	if (presBlag.isSkladOriented())
  		SF.showFrame("hr.restart.pos.upProdaja", jmPregledArtikliRacuni.getText());
  	else SF.showFrame("hr.restart.robno.upRacuniArtikliPOS", jmPregledArtikliRacuni.getText());
  }
  public void jmPregledKPR_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upKPR_NextGeneration", jmKPR.getText());
  }
  public void jmZakljucak_actionPerformed(ActionEvent e) {
    raPOS.zakljucak();
  }
  
  public void jmRacIspis_actionPerformed(ActionEvent e) {
    racunIzPOS();
  }
  
  public static void racunIzPOS() {    
    QueryDataSet pos = Pos.getDataModule().openTempSet(
        Condition.equal("CSKL", raUser.getInstance().getDefSklad()).and(
            Condition.equal("GOD", Valid.getValid().findYear(Valid.getValid().getToday()))));

    pos.setSort(new SortDescriptor(new String[] {"DATDOK"}));
    pos.last();
    
    if ("D".equalsIgnoreCase(frmParam.getParam("pos", "vrijemeRac", "N",
        "Prikazati vrijeme na racunima POS (D,N)"))) { 
      pos.getColumn("DATDOK").setDisplayMask("dd-MM-yyyy  'u' HH:mm:ss");
      pos.getColumn("DATDOK").setWidth(24);
    }
    
    lookupData.getlookupData().saveName = "dohvat-spapos-getgrn";
    lookupData.getlookupData().frameTitle = "Odabir raèuna";
    lookupData.getlookupData().setLookMode(lookupData.INDIRECT);
    try {
      String[] result = lookupData.getlookupData().lookUp(
          startFrame.getStartFrame(), pos,
            new String[] {"BRDOK", "STOL"}, 
            new String[] {"", ""}, new int[] {0,1,2,3,4,5});
      if (result == null) return;
      if (!lookupData.getlookupData().raLocate(pos, 
          new String[] {"BRDOK", "STOL"}, result)) return;
    } finally {
      lookupData.getlookupData().saveName = null;
      lookupData.getlookupData().modifiers = null;
      lookupData.getlookupData().lbutt = null;
      lookupData.getlookupData().frameTitle = "Dohvat";
    }
    
    createRAC(pos);
  }
  
  public static void createRAC(DataSet zag) {
    if (zag == null || zag.rowCount() == 0) return;
    
    String[] key = {"cskl","vrdok","god","brdok","cprodmj"};
    DataSet st = Stpos.getDataModule().openTempSet(Condition.whereAllEqual(key, zag));
    if (st == null || st.rowCount() == 0) return;
        
    QueryDataSet rzag = doki.getDataModule().openEmptySet();
    QueryDataSet rst = stdoki.getDataModule().openEmptySet();
    
    rzag.insertRow(false);
    String[] zcols = {"SYSDAT", "DATDOK", "UIRAC", "CNACPL", "CUSER", "FBR", "JIR", "FOK", "FPP", "FNU"};
    dM.copyColumns(zag, rzag, zcols);
    
    String[] scols = {"RBR", "CART", "CART1", "BC", "NAZART", "JM", "KOL", "POR1", "POR2", "POR3", "PPOR1", "PPOR2", "PPOR3"};
    
    lookupData ld = lookupData.getlookupData();
    if (ld.raLocate(dM.getDataModule().getSklad(), "CSKL", zag))
      rzag.setString("CSKL", dM.getDataModule().getSklad().getString("CORG"));
    else rzag.setString("CSKL", OrgStr.getKNJCORG(false));
    
    if (ld.raLocate(dM.getDataModule().getPartneri(), "CKUPAC", zag)) 
      rzag.setInt("CPAR", dM.getDataModule().getPartneri().getInt("CPAR"));
    
    rzag.setString("VRDOK", "RAC");
    
    rzag.setString("ZIRO", raPozivNaBroj.getraPozivNaBrojClass().getZiroRN(rzag));
    
    Util.getUtil().getBrojDokumenta(rzag, false);
    rzag.post();
    
    for (st.first(); st.inBounds(); st.next()) {
      rst.insertRow(false);
      dM.copyColumns(rzag, rst, Util.mkey);
      dM.copyColumns(st, rst, scols);
      
      Aus.set(rst, "IPRODSP", st, "NETO");
      Aus.set(rst, "IPRODBP", "IPRODSP");
      Aus.sub(rst, "IPRODBP", "POR1");
      Aus.sub(rst, "IPRODBP", "POR2");
      Aus.sub(rst, "IPRODBP", "POR3");
      Aus.set(rst, "INETO", "IPRODBP");
      Aus.base(rst, "INETO", "INETO", st.getBigDecimal("PPOPUST2").negate());
      Aus.base(rst, "INETO", "INETO", st.getBigDecimal("PPOPUST1").negate());
      
      Aus.div(rst, "FMC", "IPRODSP", "KOL");
      Aus.div(rst, "FVC", "IPRODBP", "KOL");
      Aus.div(rst, "FC", "INETO", "KOL");
      
      rst.setInt("RBSID", rst.getShort("RBR"));
      rst.setString("CSKLART", zag.getString("CSKL"));
      rst.setString("ID_STAVKA",
          raControlDocs.getKey(rst, new String[] { "cskl",
                  "vrdok", "god", "brdok", "rbsid" }, "stdoki"));
      rst.post();
    }
    
    String fiskForm = frmParam.getParam("robno", "fiskForm", "[FBR]-[FPP]-[FNU]",
        "Format fiskalnog broja izlaznog dokumenta na ispisu");
    rzag.setString("PNBZ2", Aus.formatBroj(rzag, fiskForm));
    
    if (raTransaction.saveChangesInTransaction(new QueryDataSet[] {rzag, rst, dM.getDataModule().getSeq()})) {
      Util.getUtil().showDocs(rzag.getString("CSKL"), "RAC", rzag.getTimestamp("DATDOK"), rzag.getTimestamp("DATDOK"));
    } else {
      JOptionPane.showMessageDialog(startFrame.getStartFrame(), "Neuspješno kreiranje raèuna!", "Greška", JOptionPane.ERROR_MESSAGE);
    }
  }
}