/****license*****************************************************************
**   file: jpSalKonMaster.java
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

import hr.restart.baza.Vrshemek;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCorg;
import hr.restart.swing.jpCpar;
import hr.restart.util.JlrNavField;
import hr.restart.util.raCommonClass;
import hr.restart.util.raImages;
import hr.restart.util.raKeyAction;
import hr.restart.zapod.jpGetValute;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpSalKonMaster extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmSalKon fSalKon;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlBrojdok = new JLabel();
  JLabel jlCknjige = new JLabel();
//  JLabel jlCorg = new JLabel();
//  JLabel jlCpar = new JLabel();
  JLabel jlCskl = new JLabel();
  JLabel jlDatdok = new JLabel();
  JLabel jlIznos = new JLabel();
  JLabel jlOpis = new JLabel();
  JLabel jlaDatdok = new JLabel();
  JLabel jlaDatdosp = new JLabel();
  JLabel jlaDatknj = new JLabel();
  JraButton jbSelCknjige = new JraButton();
//  JraButton jbSelCorg = new JraButton();
//  JraButton jbSelCpar = new JraButton();
  JraButton jbSelCskl = new JraButton();
  JraTextField jraBrojdok = new JraTextField();
  JraTextField jraDatdok = new JraTextField() {
    public void valueChanged() {
      if (fSalKon != null)
        fSalKon.datdokChanged();
    }
  };
  JraTextField jraDatdosp = new JraTextField();
  JraTextField jraDatknj = new JraTextField();
  JraTextField jraExtbrojdok = new JraTextField();
  JraTextField jraIznos = new JraTextField();
  JraTextField jraOpis = new JraTextField();
  JlrNavField jlrCskl = new JlrNavField(){
    public void after_lookUp() {
      if (inedit) setKonto();
      
//      if (inedit) R2Handler.handleDatPrimitkaUI(jpSalKonMaster.this);
    }
  };
  JlrNavField jlrNazknjige = new JlrNavField() {
    public void after_lookUp() {      
      if (inedit) {
        fSalKon.afterKnjig();
        setKonto();        
        jpp.focusCparLater();
      }
    }
  };
  JlrNavField jlrCknjige = new JlrNavField() {
    public void after_lookUp() {      
      if (inedit) {
        fSalKon.afterKnjig();
        setKonto();        
        jpp.focusCparLater();
      }
    }
  };
  JlrNavField jlrShemaKnj = new JlrNavField();
  JlrNavField jlrKnj = new JlrNavField();
  jpCorg jpo = new jpCorg(350) {
    public void afterLookUp(boolean succ) {
      if (succ) jpp.focusCpar();
    }
  };
  jpCpar jpp = new jpCpar(350) {
    public void afterLookUp(boolean succ) {
      if (inedit) fSalKon.PartnerChangeD();
      if (succ) jraDatdok.requestFocus();
    }
  };
  JraCheckBox jcbKnj = new JraCheckBox(); 
//  JlrNavField jlrCpar = new JlrNavField() {
//    public void after_lookUp() {
//      if (inedit) fSalKon.PartnerChangeD();
//    }
//  };
//  JlrNavField jlrNazpar = new JlrNavField() {
//    public void after_lookUp() {
//      if (inedit) fSalKon.PartnerChangeD();
//    }
//  };
//  JlrNavField jlrNaziv = new JlrNavField() {
//    public void after_lookUp() {
//    }
//  };
//  JlrNavField jlrCorg = new JlrNavField() {
//    public void after_lookUp() {
//    }
//  };
  jpGetValute jpgetval = new jpGetValute();
  
  JLabel jlKonto = new JLabel();
  boolean supressFocus = false;
  JlrNavField jlrKonto = new JlrNavField() {
    public void after_lookUp() {      
      if (!supressFocus && isLastLookSuccessfull() && inedit) jraIznos.requestFocusLater();
    }
  };
  JlrNavField jlrNazKonto = new JlrNavField() {
    public void after_lookUp() {      
      if (!supressFocus && isLastLookSuccessfull()) jraIznos.requestFocusLater();
    }
  };
  
  JraButton jbSelKonto = new JraButton();
  public JraButton jbSelVirtual = new JraButton();
  
  QueryDataSet vrsk;
  boolean inedit;

  public jpSalKonMaster(frmSalKon md) {
    try {
      fSalKon = md;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void setEdit(boolean ed) {
    inedit = ed;
  }

  public void setVrsk(String vrdok) {
    Vrshemek.getDataModule().setFilter(vrsk, "app = 'sk' AND vrdok = '"+vrdok+"'");
    vrsk.open();
  }

  public void BindComponents(DataSet ds) {
    jraBrojdok.setDataSet(ds);
    jraDatdok.setDataSet(ds);
    jraDatdosp.setDataSet(ds);
    jraDatknj.setDataSet(ds);
    jraExtbrojdok.setDataSet(ds);
    jraIznos.setDataSet(ds);
    jraOpis.setDataSet(ds);
    jlrCskl.setDataSet(ds);
    jlrCknjige.setDataSet(ds);
    jcbKnj.setDataSet(ds);
//    jlrCpar.setDataSet(ds);
//    jlrCorg.setDataSet(ds);
    if (raSaldaKonti.isDirect()) jlrKonto.setDataSet(ds);
    jpp.bind(ds);
    jpo.bind(ds);
    jpgetval.setRaDataSet(ds);
  }

  private void jbInit() throws Exception {
   
    vrsk = Vrshemek.getDataModule().getFilteredDataSet("app = 'sk'");
    
    jpDetail.setLayout(lay);
    lay.setWidth(646);
    lay.setHeight(raSaldaKonti.isDirect() ? 310 : 285);

    jlBrojdok.setText("Broj dokumenta");
    jlCknjige.setText("Knjiga");
//    jlCorg.setText("Org. jedinica");
//    jlCpar.setText("Partner");
    jlCskl.setText("Shema knjiženja");
    jlDatdok.setText("Datum");
    jlIznos.setText("Iznos");
    jlOpis.setText("Opis");
    jlaDatdok.setHorizontalAlignment(SwingConstants.CENTER);
    jlaDatdok.setText("Dokumenta");
    jlaDatdosp.setHorizontalAlignment(SwingConstants.CENTER);
    jlaDatdosp.setText("Dospje\u0107a");
    jlaDatknj.setHorizontalAlignment(SwingConstants.CENTER);
    jlaDatknj.setText("Primitka");
    jraBrojdok.setColumnName("BROJDOK");
    jraDatdok.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatdok.setColumnName("DATDOK");

    jraDatdosp.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatdosp.setColumnName("DATDOSP");
    jraDatknj.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatknj.setColumnName("DATPRI");
    jraExtbrojdok.setColumnName("EXTBRDOK");

    jraOpis.setColumnName("OPIS");

    jlrCskl.setColumnName("CSKL");
    jlrCskl.setNavColumnName("CVRSK");

    jlrCskl.setVisCols(new int[] {0, 1});
    jlrCskl.setSearchMode(0);
    jlrCskl.setRaDataSet(vrsk);
    jlrCskl.setNavButton(jbSelCskl);

    jlrCknjige.setColumnName("CKNJIGE");
    jlrCknjige.setColNames(new String[] {"NAZKNJIGE", "CSKL", "KNJIZENJE"});
    jlrCknjige.setTextFields(new JTextComponent[] {jlrNazknjige, jlrShemaKnj, jlrKnj});
    jlrCknjige.setVisCols(new int[] {0, 4});
    jlrCknjige.setSearchMode(0);
    jlrCknjige.setRaDataSet(dm.getKnjigeUI());
    jlrCknjige.setNavButton(jbSelCknjige);

    jlrNazknjige.setColumnName("NAZKNJIGE");
    jlrNazknjige.setNavProperties(jlrCknjige);
    jlrNazknjige.setSearchMode(1);

    jlrShemaKnj.setColumnName("CSKL");
    jlrShemaKnj.setNavProperties(jlrCknjige);
    jlrShemaKnj.setVisible(false);
    jlrShemaKnj.setEnabled(false);
    
    jlrKnj.setColumnName("KNJIZENJE");
    jlrKnj.setNavProperties(jlrCknjige);
    jlrKnj.setVisible(false);
    jlrKnj.setEnabled(false);
    
    jcbKnj.setColumnName("CGKSTAVKE");
    jcbKnj.setSelectedDataValue("D");
    jcbKnj.setUnselectedDataValue("N");
    jcbKnj.setText(" Knjiženje raèuna u glavnu knjigu i salda konti ");    
    jcbKnj.setHorizontalTextPosition(JLabel.LEADING);
    jcbKnj.setHorizontalAlignment(JLabel.TRAILING);

//    jlrCpar.setColumnName("CPAR");
//    jlrCpar.setColNames(new String[] {"NAZPAR"});
//    jlrCpar.setTextFields(new JTextComponent[] {jlrNazpar});
//    jlrCpar.setVisCols(new int[] {0, 1});
//    jlrCpar.setSearchMode(0);
//    jlrCpar.setRaDataSet(dm.getPartneri());
//    jlrCpar.setNavButton(jbSelCpar);

//    jlrNazpar.setColumnName("NAZPAR");
//    jlrNazpar.setNavProperties(jlrCpar);
//    jlrNazpar.setSearchMode(1);

//    jlrCorg.setColumnName("CORG");
//    jlrCorg.setColNames(new String[] {"NAZIV"});
//    jlrCorg.setTextFields(new JTextComponent[] {jlrNaziv});
//    jlrCorg.setVisCols(new int[] {0, 1});
//    jlrCorg.setSearchMode(0);
//    jlrCorg.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
//    jlrCorg.setNavButton(jbSelCorg);

//    jlrNaziv.setColumnName("NAZIV");
//    jlrNaziv.setNavProperties(jlrCorg);
//    jlrNaziv.setSearchMode(1);
    
    if (raSaldaKonti.isDirect()) {
      jlKonto.setText("Konto");
      jlrKonto.setHorizontalAlignment(SwingConstants.TRAILING);
      jlrKonto.setColumnName("BROJKONTA");
      jlrKonto.setColNames(new String[] {"NAZIVKONTA"});
      jlrKonto.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazKonto});
      jlrKonto.setVisCols(new int[] {0, 1});
      jlrKonto.setSearchMode(3);
      jlrKonto.setRaDataSet(dm.getKontaAnalitic());
      jlrKonto.setNavButton(jbSelKonto);

      jlrNazKonto.setNavProperties(jlrKonto);
      jlrNazKonto.setColumnName("NAZIVKONTA");
      jlrNazKonto.setSearchMode(1);      
    }

    jpgetval.setTecajVisible(true);
    jpgetval.setTecajEditable(true);
    
    jbSelVirtual.setIcon(raImages.getImageIcon(raImages.IMGSENDMAIL));
    jbSelVirtual.setAutomaticFocusLost(false);
    jbSelVirtual.setToolTipText("Prijenos dokumenata iz urudžbenog");
    jbSelVirtual.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fSalKon.selectDoc();
      }
    });
    raKeyAction rkSelVirtual = new raKeyAction(KeyEvent.VK_F6, jbSelVirtual.getToolTipText()) {
      public void keyAction() {
        if (jbSelVirtual.isShowing() && jbSelVirtual.isEnabled()) fSalKon.selectDoc();
      }
    };
    rkSelVirtual.setIcon(jbSelVirtual.getIcon());
    if (fSalKon != null) fSalKon.raMaster.addKeyAction(rkSelVirtual);
    jpDetail.add(jpo, new XYConstraints(0, 45, -1, -1));
    jpDetail.add(jpp, new XYConstraints(0, 70, -1, -1));

    jpDetail.add(jbSelCknjige, new XYConstraints(610, 20, 21, 21));
//    jpDetail.add(jbSelCorg, new XYConstraints(610, 45, 21, 21));
//    jpDetail.add(jbSelCpar, new XYConstraints(610, 70, 21, 21));
    
    jpDetail.add(jlBrojdok, new XYConstraints(15, 140, -1, -1));
    jpDetail.add(jlCknjige, new XYConstraints(15, 20, -1, -1));
//    jpDetail.add(jlCorg, new XYConstraints(15, 45, -1, -1));
//    jpDetail.add(jlCpar, new XYConstraints(15, 70, -1, -1));    
    jpDetail.add(jlDatdok, new XYConstraints(15, 115, -1, -1));
    jpDetail.add(jlOpis, new XYConstraints(15, 210, -1, -1));
    jpDetail.add(jlaDatdok, new XYConstraints(151, 98, 98, -1));
    jpDetail.add(jlaDatdosp, new XYConstraints(256, 98, 98, -1));
    jpDetail.add(jlaDatknj, new XYConstraints(361, 98, 98, -1));
    jpDetail.add(jlrCknjige, new XYConstraints(150, 20, 100, -1));
//    jpDetail.add(jlrCorg, new XYConstraints(150, 45, 100, -1));
//    jpDetail.add(jlrCpar, new XYConstraints(150, 70, 100, -1));
//    jpDetail.add(jlrNaziv, new XYConstraints(255, 45, 350, -1));
    jpDetail.add(jlrNazknjige, new XYConstraints(255, 20, 350, -1));
//    jpDetail.add(jlrNazpar, new XYConstraints(255, 70, 350, -1));
    jpDetail.add(jraBrojdok, new XYConstraints(150, 140, 310, -1));
    jpDetail.add(jraDatdok, new XYConstraints(150, 115, 100, -1));
    jpDetail.add(jraDatdosp, new XYConstraints(255, 115, 100, -1));
    jpDetail.add(jraDatknj, new XYConstraints(360, 115, 100, -1));
    jpDetail.add(jbSelVirtual, new XYConstraints(465, 115, 21, 21));
    jpDetail.add(jraExtbrojdok, new XYConstraints(465, 140, 140, -1));    
    jpDetail.add(jraOpis, new XYConstraints(150, 210, 455, -1));
    jpDetail.add(jpgetval, new XYConstraints(0, 165, -1, 45));

    int vert = raSaldaKonti.isDirect() ? 260 : 235;
    jpDetail.add(jlCskl, new XYConstraints(15, vert, -1, -1));
    jpDetail.add(jbSelCskl, new XYConstraints(255, vert, 21, 21));
    jpDetail.add(jlIznos, new XYConstraints(415, vert, -1, -1));
    jpDetail.add(jraIznos, new XYConstraints(505, vert, 100, -1));
    jpDetail.add(jlrCskl, new XYConstraints(150, vert, 100, -1));
    jpDetail.add(jlrShemaKnj, new XYConstraints(300, vert, 5, -1));
    jpDetail.add(jlrKnj, new XYConstraints(310, vert, 5, -1));
    if (raSaldaKonti.isDirect()) {
      jpDetail.add(jlKonto, new XYConstraints(15, 235, -1, -1));
      jpDetail.add(jbSelKonto, new XYConstraints(610, 235, 21, 21));
      jpDetail.add(jlrKonto, new XYConstraints(150, 235, 100, -1));
      jpDetail.add(jlrNazKonto, new XYConstraints(255, 235, 350, -1));
    }
    jpDetail.add(jcbKnj, new XYConstraints(250, vert + 25, 355, -1));
    
    /*jraDatdok.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        if (fSalKon != null)
          fSalKon.datdokChanged();
      }
    });*/
    
//    OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
//      public void knjigChanged(String oldk, String newk) {
//        jlrCorg.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
//      }
//    });
    this.add(jpDetail, BorderLayout.CENTER);
  }
  
  void setKonto() {
    String konto = null;
    if (fSalKon.getBlagajna() != null) {
      konto = fSalKon.getBlagajna().getKontoURA();
    } 
    if (konto == null) {
      String sh = jlrCskl.getText();    
      if (sh == null || sh.length() == 0) sh = jlrShemaKnj.getText();    
      konto = raSaldaKonti.getKonto(fSalKon.vrdokSheme, sh, "1");
    }
    if (konto != null && konto.length() > 0) {
      jlrKonto.setText(konto);
      supressFocus = true;
      jlrKonto.forceFocLost(); //ai:ovo radi sranje jer fokusira u iznos kad stisnes novi
      supressFocus = false;
    }
  }  
}
