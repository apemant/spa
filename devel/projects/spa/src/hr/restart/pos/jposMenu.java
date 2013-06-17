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
import hr.restart.robno.raPOS;
import hr.restart.robno.repFISBIH;
import hr.restart.util.PreSelect;
import hr.restart.util.raLoader;
import hr.restart.util.raProcess;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.borland.dx.dataset.DataSet;

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
  public JMenuItem jmKPR = new JMenuItem();
  public JMenuItem jmPregledKPR = new JMenuItem();
  public JMenuItem jmFISBIH = new JMenuItem();

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
        raPOS.zakljucak();
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
    }
    if (repFISBIH.isFISBIH()) {
      addSeparator();
      add(jmFISBIH);
    }
  }

  public void jmFISBIH_actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
    SF.showFrame("hr.restart.robno.FISBIHIzvjestaji", jmFISBIH.getText());
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
}