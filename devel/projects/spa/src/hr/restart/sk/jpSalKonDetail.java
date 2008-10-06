/****license*****************************************************************
**   file: jpSalKonDetail.java
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
package hr.restart.sk;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.raCommonClass;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.raKnjigChangeListener;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpSalKonDetail extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmSalKon fSalKon;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCkolone = new JLabel();
  JLabel jlCorg = new JLabel();
  JLabel jlIznos = new JLabel();
  JLabel jlStavka = new JLabel();
  JLabel jlKonto = new JLabel();
  JLabel jlDug = new JLabel();
  JLabel jlPot = new JLabel();
  JraButton jbSelCkolone = new JraButton();
  JraButton jbSelCorg = new JraButton();
  JraButton jbSelStavka = new JraButton();
  JraTextField jraIznos = new JraTextField();
  JraTextField jraDug = new JraTextField();
  JraTextField jraPot = new JraTextField();
  
  JlrNavField jlrKonto = new JlrNavField() {
    public void after_lookUp() {
      if (inedit && raSaldaKonti.isDirect()) fSalKon.afterKonto();
    }
  };
  JlrNavField jlrNazKonto = new JlrNavField();
  JlrNavField jlrKarKonto = new JlrNavField();  
  JraButton jbSelKonto = new JraButton();
  
  JlrNavField jlrStavka = new JlrNavField() {
    public void after_lookUp() {
      if (inedit) fSalKon.afterStavka();
    }
  };
  JlrNavField jlrNazivkolone = new JlrNavField();
  JlrNavField jlrKarkolone = new JlrNavField();
  JlrNavField jlrNaziv = new JlrNavField();
  JlrNavField jlrOpis = new JlrNavField() {
    public void after_lookUp() {
      if (inedit) fSalKon.afterStavka();
    }
  };
  JlrNavField jlrCkolone = new JlrNavField() {
    public void after_lookUp() {
      if (inedit && raSaldaKonti.isDirect()) fSalKon.afterKolone();
    }
  };
  JlrNavField jlrCorg = new JlrNavField();
  JlrNavField jlrShCskl = new JlrNavField();
  JlrNavField jlrShKar = new JlrNavField();
  JlrNavField jlrShPolje = new JlrNavField();

  boolean inedit;

  public jpSalKonDetail(frmSalKon md) {
    try {
      fSalKon = md;      
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void setEdit(boolean ed) {
//    System.out.println("edit = "+ed);
    inedit = ed;
  }
  
  public void enableIznos(boolean id, boolean ip) {
    if (raSaldaKonti.isDirect()) {
      rcc.setLabelLaF(jraDug, id);   
      rcc.setLabelLaF(jraPot, ip);      
    }
  }  

  public void BindComponents(DataSet ds) {    
    if (raSaldaKonti.isDirect()) {
      jlrKonto.setDataSet(ds);
      jraDug.setDataSet(ds);
      jraPot.setDataSet(ds);
    } else {
      jlrStavka.setDataSet(ds);
      jraIznos.setDataSet(ds);
    }
    jlrCkolone.setDataSet(ds);
    jlrCorg.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(541);
    lay.setHeight(135);

    jbSelCkolone.setText("...");
    jbSelCorg.setText("...");
    jbSelStavka.setText("...");
    jlKonto.setText("Konto");
    jlCkolone.setText("Kolona");
    jlCorg.setText("Org. jedinica");
    jlIznos.setText("Iznos");
    jlDug.setText("Duguje");
    jlDug.setHorizontalAlignment(JLabel.CENTER);
    jlPot.setText("Potražuje");
    jlPot.setHorizontalAlignment(JLabel.CENTER);
    jlStavka.setText("Stavka sheme knjiženja");

    if (raSaldaKonti.isDirect()) {
      jlrKonto.setHorizontalAlignment(SwingConstants.TRAILING);
      jlrKonto.setColumnName("BROJKONTA");
      jlrKonto.setColNames(new String[] {"NAZIVKONTA", "KARAKTERISTIKA"});
      jlrKonto.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazKonto, jlrKarKonto});
      jlrKonto.setVisCols(new int[] {0, 1});
      jlrKonto.setSearchMode(3);
      jlrKonto.setRaDataSet(dm.getKontaAnalitic());
      jlrKonto.setNavButton(jbSelKonto);

      jlrNazKonto.setNavProperties(jlrKonto);
      jlrNazKonto.setColumnName("NAZIVKONTA");
      jlrNazKonto.setSearchMode(1);

      jlrKarKonto.setNavProperties(jlrKonto);
      jlrKarKonto.setColumnName("KARAKTERISTIKA");
      jlrKarKonto.setSearchMode(1);
      
      jraDug.setColumnName("ID");
      jraPot.setColumnName("IP");
    } else {
      jlrStavka.setColumnName("STAVKA");

      jlrStavka.setColNames(new String[] {"OPIS", "CSKL", "KARAKTERISTIKA", "POLJE"});
      jlrStavka.setTextFields(new JTextComponent[] {jlrOpis, jlrShCskl, jlrShKar, jlrShPolje});
      jlrStavka.setVisCols(new int[] {2, 3, 4, 5, 6});
      jlrStavka.setSearchMode(0);
      jlrStavka.setRaDataSet(dm.getShkonta());
      jlrStavka.setNavButton(jbSelStavka);

      jlrOpis.setColumnName("OPIS");
      jlrOpis.setNavProperties(jlrStavka);
      jlrOpis.setSearchMode(1);

      jlrShCskl.setColumnName("CSKL");
      jlrShCskl.setNavProperties(jlrStavka);

      jlrShKar.setColumnName("KARAKTERISTIKA");
      jlrShKar.setNavProperties(jlrStavka);

      jlrShPolje.setColumnName("POLJE");
      jlrShPolje.setNavProperties(jlrStavka);
    }

    jlrCkolone.setColumnName("CKOLONE");

    jlrCkolone.setColNames(new String[] {"NAZIVKOLONE", "DUGPOT"});
    jlrCkolone.setTextFields(new JTextComponent[] {jlrNazivkolone, jlrKarkolone});
    jlrCkolone.setVisCols(new int[] {0, 1});
    jlrCkolone.setSearchMode(0);
    jlrCkolone.setRaDataSet(dm.getKoloneknjUI());
    jlrCkolone.setNavButton(jbSelCkolone);

    jlrNazivkolone.setColumnName("NAZIVKOLONE");
    jlrNazivkolone.setNavProperties(jlrCkolone);
    jlrNazivkolone.setSearchMode(1);
    
    jlrKarkolone.setColumnName("DUGPOT");
    jlrKarkolone.setNavProperties(jlrCkolone);
    jlrKarkolone.setSearchMode(1);

    jlrCorg.setColumnName("CORG");
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCorg.setVisCols(new int[] {0, 1});
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.setNavButton(jbSelCorg);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCorg);
    jlrNaziv.setSearchMode(1);

    if (raSaldaKonti.isDirect()) {
      lay.setHeight(155);
      jpDetail.add(jlKonto, new XYConstraints(15, 20, -1, -1));
      jpDetail.add(jbSelKonto, new XYConstraints(505, 20, 21, 21));
      jpDetail.add(jlrKonto, new XYConstraints(150, 20, 100, -1));
      jpDetail.add(jlrNazKonto, new XYConstraints(255, 20, 245, -1));
      jpDetail.add(jlIznos, new XYConstraints(15, 115, -1, -1));
      jpDetail.add(jlDug, new XYConstraints(150, 100, 100, -1));
      jpDetail.add(jlPot, new XYConstraints(400, 100, 100, -1));
      jpDetail.add(jraDug, new XYConstraints(150, 115, 100, -1));
      jpDetail.add(jraPot, new XYConstraints(400, 115, 100, -1));
    } else {
      jpDetail.add(jlStavka, new XYConstraints(15, 20, -1, -1));
      jpDetail.add(jbSelStavka, new XYConstraints(505, 20, 21, 21));
      jpDetail.add(jlrStavka, new XYConstraints(150, 20, 100, -1));
      jpDetail.add(jlrOpis, new XYConstraints(255, 20, 245, -1));
      jpDetail.add(jlIznos, new XYConstraints(15, 95, -1, -1));
      jpDetail.add(jraIznos, new XYConstraints(150, 95, 100, -1));
    }
    jpDetail.add(jbSelCkolone, new XYConstraints(505, 70, 21, 21));
    jpDetail.add(jbSelCorg, new XYConstraints(505, 45, 21, 21));
    jpDetail.add(jlCkolone, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlCorg, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlrCkolone, new XYConstraints(150, 70, 100, -1));
    jpDetail.add(jlrCorg, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(255, 45, 245, -1));
    jpDetail.add(jlrNazivkolone, new XYConstraints(255, 70, 245, -1));    

    OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
      public void knjigChanged(String oldk, String newk) {
        jlrCorg.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      }
    });
    this.add(jpDetail, BorderLayout.CENTER);
  }
}
