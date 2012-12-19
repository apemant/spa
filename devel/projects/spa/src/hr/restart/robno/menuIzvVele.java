/****license*****************************************************************
**   file: menuIzvVele.java
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

import hr.restart.sisfun.frmParam;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class menuIzvVele extends JMenu {
  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");

  hr.restart.util.startFrame SF;
  JMenuItem jmPregledKol = new JMenuItem();
  JMenuItem jmRezKol = new JMenuItem();
  JMenuItem jmIspisCjenik = new JMenuItem();
  JMenuItem jmPorezList = new JMenuItem();
  JMenuItem jmPopustList = new JMenuItem();
  JMenuItem jmUlazVrsteDokumenata = new JMenuItem();
  JMenuItem jmOstaliRep = new JMenuItem();
  JMenuItem jmMiniSaldak = new JMenuItem();  
  JMenuItem jmPreglProdPoArt = new JMenuItem();
  JMenuItem jmPreglNeiskalkPrim = new JMenuItem();
  JMenuItem jmPregHCCP = new JMenuItem();
  JMenuItem jmPregHCCPu = new JMenuItem();
  
  JMenuItem jmPorezUgostiteljstvo = new JMenuItem();

  public JMenuItem jmFISBIH = new JMenuItem();
  
  public menuIzvVele(hr.restart.util.startFrame startframe) {
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
    
    jmFISBIH.setText("Fiskalni izvještaji");
    jmFISBIH.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmFISBIH_actionPerformed(e);
      }
    });
    
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
    jmIspisCjenik.setText("Ispis cjenika");
    jmIspisCjenik.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmIspisCjenik_actionPerformed(e);
      }
    });
    
    jmPorezList.setText("Pregled poreza");
    jmPorezList.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPorezList_actionPerformed(e);
      }
    });
    
    
    jmPorezUgostiteljstvo.setText("Pregled poreza - ugostiteljstvo");
    jmPorezUgostiteljstvo.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPorezUgostiteljstvo_actionPerformed(e);
      }
    });
    
    jmPopustList.setText("Pregled popusta");
    jmPopustList.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmPopustList_actionPerformed(e);
      }
    });
    jmUlazVrsteDokumenata.setText("Pregled ulaza po vrstama dokumenata");
    jmUlazVrsteDokumenata.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmUlazVrsteDokumenata_actionPerformed(e);
      }
    });
    jmMiniSaldak.setText("Pregled i ažuriranje naplate");
    jmMiniSaldak.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmMiniSaldak_actionPerformed(e);
      }
    });
    

/*    jmUIDok.setText("Pregled prodaje po dokumentima");
    jmUIDok.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmUIDok_actionPerformed(e);
      }
    });
    jmIzvArt.setText("Pregled prodaje po artiklima");
    jmIzvArt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmIzvArt_actionPerformed(e);
      }
    });
    jmKupStat.setText("Pregled prodaje po kupacima");
    jmKupStat.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmKupStat_actionPerformed(e);
      }
    });*/
    jmOstaliRep.setText("Ostali izvještaji");
    jmOstaliRep.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmOstaliRep_actionPerformed(e);
      }
    });
    

    jmPreglProdPoArt.setText("Pregled prodaje po artiklima");
    jmPreglProdPoArt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
      	jmPreglProdPoArt_actionPerformed(e);
      }
    });
    
    jmPreglNeiskalkPrim.setText("Pregled neiskalkuliranih primki");
    jmPreglNeiskalkPrim.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        jmPregledNeiskalkPrim_actionPerformed(ae);
      }
    });
    
    jmPregHCCP.setText("Pregled evidencije dopreme hrane");
    jmPregHCCP.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        jmPregHCCP_actionPerformed(ae);
      }
    });
    
    jmPregHCCPu.setText("Pregled evidencije otpreme hrane");
    jmPregHCCPu.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        jmPregHCCPu_actionPerformed(ae);
      }
    });
    
    this.add(jmPorezList);
    this.add(jmPorezUgostiteljstvo);
    this.add(jmPopustList);
    this.add(jmUlazVrsteDokumenata);
    this.addSeparator();
    this.add(jmPregledKol);
    this.add(jmRezKol);
//    this.addSeparator();
    this.add(jmMiniSaldak);    
//    this.addSeparator();
    this.add(jmPreglProdPoArt);
    this.addSeparator();
    this.add(jmPreglNeiskalkPrim);
    this.addSeparator();
    this.add(jmIspisCjenik);
    
    if (frmParam.getParam("robno", "ediUlaz", "N",
    "Panel za unos HCCP podataka na ulazu (D,N)").equals("D")) {
      this.addSeparator();
      this.add(jmPregHCCP);
      this.add(jmPregHCCPu);
    }
    if (repFISBIH.isFISBIH()) {
      addSeparator();
      add(jmFISBIH);
    }
    this.addSeparator();
    this.add(jmOstaliRep);
/*    this.add(jmUIDok);
    this.add(jmIzvArt);
    this.add(jmKupStat);*/

  }
/*  void jmUIDok_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upUlazIzlaz", res.getString("upFrmUlazIzlaz_title"));
  }
  void jmIzvArt_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmIzvArt", "Pregled prodaje po artiklima");
  }
  void jmKupStat_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.ispStatParDva", "Top lista kupaca");
  }*/
  void jmOstaliRep_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmRobnoReportList", "Ostali izvještaji");
  }
  void jmPorezList_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upPorezList", jmPorezList.getText());
  }
  
  void jmPorezUgostiteljstvo_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.RaUgostiteljstvoPorez", jmPorezUgostiteljstvo.getText());
  }
  
  void jmPopustList_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upPopustList", jmPopustList.getText());
  }
  void jmUlazVrsteDokumenata_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.UpUlaziZavTr", jmUlazVrsteDokumenata.getText());
  }
  void jmPregledKol_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upPregledKol", jmPregledKol.getText());
  }
  void jmRezKol_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.upRezKol", jmRezKol.getText());
  }
  void jmIspisCjenik_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.dlgPrintCjenik", jmIspisCjenik.getText());
  }
  void jmMiniSaldak_actionPerformed(ActionEvent e){
    SF.showFrame("hr.restart.robno.raRobnoMiniSaldak", jmMiniSaldak.getText());
  }
  void jmPreglProdPoArt_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmIzvArtVp", jmPreglProdPoArt.getText());
  }
  
  void jmPregHCCP_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmIzvHCCP", jmPregHCCP.getText());
  }
  
  void jmPregHCCPu_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.robno.frmIzvHCCPu", jmPregHCCPu.getText());
  }
  
  void jmPregledNeiskalkPrim_actionPerformed(ActionEvent ae) {
    SF.showFrame("hr.restart.robno.frmNeiskalkPrim", jmPreglNeiskalkPrim.getText());
  }
  public void jmFISBIH_actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
    SF.showFrame("hr.restart.robno.FISBIHIzvjestaji", jmFISBIH.getText());
  }

}
