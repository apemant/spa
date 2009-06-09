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

import hr.restart.util.PreSelect;
import hr.restart.util.raLoader;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

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
  public JMenuItem jmPregledArtikliRacuni = new JMenuItem();

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
    jmPregledArtikliRacuni.setText("Pregled artikli raèuni");
    jmPregledArtikliRacuni.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPregledArtikliRacuni_actionPerformed(e);
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
  }

  public void jmBlagajna_actionPerformed(ActionEvent e) {
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
    SF.showFrame("hr.restart.robno.ispRekapitulacijaRacunaPOS", jmRekapitulacijaPOS.getText());
  }
  public void jmPregledArtikliRacuni_actionPerformed(ActionEvent e){
    SF.showFrame("hr.restart.robno.upRacuniArtikliPOS", jmPregledArtikliRacuni.getText());
  }
}