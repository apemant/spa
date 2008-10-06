/****license*****************************************************************
**   file: skladMenuIzv.java
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
/*
 * Created on 2005.04.18
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.sklad;

import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * @author Racunalo4test
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class skladMenuIzv extends JMenu {
	  JMenuItem jmPregledKol = new JMenuItem();
	  JMenuItem jmRezKol = new JMenuItem();
	  JMenuItem jmPregledNarudzbi = new JMenuItem();
	  JMenuItem jmPregledNerDost = new JMenuItem();
	  JMenuItem jmOstaliRep = new JMenuItem();
	  hr.restart.util.startFrame SF;
	  
	  public skladMenuIzv(hr.restart.util.startFrame startframe) {
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
	    this.setText("Pregledi");
	    jmPregledKol.setText("Pregled signalnih i minimalnih kolièina");
	    jmPregledKol.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmPregledKol_actionPerformed(e);
	      }
	    });
	    jmRezKol.setText("Pregled rezervacija po artiklima");
	    jmRezKol.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmRezKol_actionPerformed(e);
	      }
	    });
	    jmPregledNarudzbi.setText("Pregled neisporuèenih narudžbi");
	    jmPregledNarudzbi.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmNarudzbe_actionPerformed(e);
	      }
	    });
	    jmPregledNerDost.setText("Pregled nerealiziranih naloga");
	    jmPregledNerDost.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmNerDost_actionPerformed(e);
	      }
	    });
	    jmOstaliRep.setText("Ostali izvještaji");
	    jmOstaliRep.addActionListener(new java.awt.event.ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        jmOstaliRep_actionPerformed(e);
	      }
	    });
	    this.add(jmPregledKol);
	    this.add(jmRezKol);
	    this.add(jmPregledNarudzbi);
	    this.add(jmPregledNerDost);
	    this.addSeparator();
	    this.add(jmOstaliRep);
	  }
	  void jmOstaliRep_actionPerformed(ActionEvent e) {
	    SF.showFrame("hr.restart.robno.frmRobnoReportList", "Ostali izvještaji");
	  }
	  void jmPregledKol_actionPerformed(ActionEvent e) {
	    SF.showFrame("hr.restart.robno.upPregledKol", jmPregledKol.getText());
	  }
	  void jmRezKol_actionPerformed(ActionEvent e) {
	    SF.showFrame("hr.restart.robno.upRezKol", jmRezKol.getText());
	  }
	  void jmNarudzbe_actionPerformed(ActionEvent e) {
	    SF.showFrame("hr.restart.robno.FrmNeisporuceneNarudzbe", jmPregledNarudzbi.getText());
	  }
	  void jmNerDost_actionPerformed(ActionEvent e) {
	    SF.showFrame("hr.restart.robno.FrmNerealiziraneDostavnice", jmPregledNerDost.getText());
	  }

}
