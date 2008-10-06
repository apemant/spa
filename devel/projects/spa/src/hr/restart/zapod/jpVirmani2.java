/****license*****************************************************************
**   file: jpVirmani2.java
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
Human. It's like Sebacean. Except that we haven't conquered other worlds, so we just kick the crap out of each other.
*/
package hr.restart.zapod;

import hr.restart.baza.dM;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raButtonGroup;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class jpVirmani2 extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmVirmani fVirmani;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
//  JLabel jlApp = new JLabel();
  JLabel jlBrracnt = new JLabel();
  JLabel jlBrracuk = new JLabel();
//  JLabel jlCkey = new JLabel();
//  JLabel jlDatumpr = new JLabel();
//  JLabel jlJedzav = new JLabel();
//  JLabel jlKnjig = new JLabel();
  JLabel jlNateret = new JLabel();
//  JLabel jlPozOd = new JLabel();
  JLabel jlPozivZad = new JLabel();
//  JLabel jlRbr = new JLabel();
//  JLabel jlSif1 = new JLabel();
  JLabel jlUkorist = new JLabel();
  JraTextField jraApp = new JraTextField();
  JraTextField jraBrracnt = new JraTextField();
  JraTextField jraBrracuk = new JraTextField();
  JraTextField jraCkey = new JraTextField();
  JraTextField jraDatumizv = new JraTextField();
  JraTextField jraDatumpr = new JraTextField();
  JraTextField jraJedzav = new JraTextField();
  JraTextField jraKnjig = new JraTextField();
  JraTextField jraMjesto = new JraTextField();
  JraTextField jraNacizv = new JraTextField();
  hr.restart.swing.JraTextArea jraNateret = new hr.restart.swing.JraTextArea();
  JraTextField jraPnbo1 = new JraTextField();
  JraTextField jraPnbo2 = new JraTextField() {
    public void valueChanged() {
      jraPnbo2_focusLost(null);
    }
  };
  JraTextField jraPnbz1 = new JraTextField();
  JraTextField jraPnbz2 = new JraTextField();
  JraTextField jraRbr = new JraTextField();
  JraTextField jraSif1 = new JraTextField();
  JraTextField jraSif2 = new JraTextField();
  JraTextField jraSif3 = new JraTextField();
  hr.restart.swing.JraTextField jraSvrha = new hr.restart.swing.JraTextField();
  hr.restart.swing.JraTextArea jraUkorist = new hr.restart.swing.JraTextArea();

  JLabel jlNazAdrPlatitelj = new JLabel();
  JLabel jlNazAdrPrimatelj = new JLabel();
  JLabel jlModelPlatitelja = new JLabel();
  JLabel jlModelPrimatelja = new JLabel();
  JLabel jlPozivOdob = new JLabel();
  JLabel jlOpisPlacanja = new JLabel();
  JLabel jlDatumPodnosenja = new JLabel();
  JLabel jlStatObilj = new JLabel();
  JLabel jlSifraNacPl = new JLabel();
  JLabel jlHitnost = new JLabel();

  JraCheckBox jcbHitnost1 = new JraCheckBox();
  JraCheckBox jcbHitnost2 = new JraCheckBox();

  JraCheckBox jcbPrijenos = new JraCheckBox();
  JraCheckBox jcbUplata = new JraCheckBox();
  JraCheckBox jcbIsplata = new JraCheckBox();
  raButtonGroup pui = new raButtonGroup();

  JLabel jlNalogZaPlacanje = new JLabel();
  BorderLayout borderLayout1 = new BorderLayout();
  JLabel jlIznosTxt = new JLabel();
  JraTextField jraIznos = new JraTextField();
  JLabel jlKn = new JLabel();
  JLabel jlStatOb = new JLabel();
  JLabel jlOvjeraNalogodavca = new JLabel();
  JLabel jlOvjeraBanke = new JLabel();
  JLabel jlDatumPodnošenja = new JLabel();
  JLabel jlPotpisPrimatelja = new JLabel();

  public jpVirmani2(frmVirmani f) {
    try {
      fVirmani = f;
      jbInit();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(620);
    lay.setHeight(395);
    jraNateret.setColumns(7);
//    jlApp.setText("Aplikacija");
    jlBrracnt.setFont(new java.awt.Font("Dialog", 0, 9));
    jlBrracnt.setHorizontalAlignment(SwingConstants.CENTER);
    jlBrracnt.setText("Broj raèuna platitelja");
    jlBrracuk.setFont(new java.awt.Font("Dialog", 0, 9));
    jlBrracuk.setHorizontalAlignment(SwingConstants.CENTER);
    jlBrracuk.setText("Broj raèuna primatelja");
//    jlCkey.setText("Kljuè");
//    jlDatumpr.setFont(new java.awt.Font("Dialog", 0, 10));
//    jlDatumpr.setText("(datum predaje Zavodu)");
//    jlJedzav.setFont(new java.awt.Font("Dialog", 0, 10));
//    jlJedzav.setText("(Jedinica zavoda)");
//    jlKnjig.setText("Knjigovodstvo");
    jlNateret.setText("PLATITELJ:");
//    jlPozOd.setFont(new java.awt.Font("Dialog", 0, 9));
//    jlPozOd.setText("poziv");
    jlPozivZad.setFont(new java.awt.Font("Dialog", 0, 9));
    jlPozivZad.setText("Poziv na broj zaduženja");
//    jlRbr.setText("Rbr");
//    jlSif1.setFont(new java.awt.Font("Dialog", 0, 10));
//    jlSif1.setText("šifra");
    jlUkorist.setText("PRIMATELJ:");
    jraApp.setColumnName("APP");
    jraApp.setDataSet(fVirmani.getRaQueryDataSet());

    jraBrracnt.setColumnName("BRRACNT");
    jraBrracnt.setDataSet(fVirmani.getRaQueryDataSet());

    /*jraBrracnt.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jraBrracnt_focusLost(e);
      }
    });*/
    jraBrracnt.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        jraBrracnt_keyReleased(e);
      }
    });

    jraBrracuk.setColumnName("BRRACUK");
    jraBrracuk.setDataSet(fVirmani.getRaQueryDataSet());

    jraBrracuk.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        jraBrracuk_keyReleased(e);
      }
    });

    jraCkey.setColumnName("CKEY");
    jraCkey.setDataSet(fVirmani.getRaQueryDataSet());
    jraDatumizv.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatumizv.setColumnName("DATUMIZV");
    jraDatumizv.setDataSet(fVirmani.getRaQueryDataSet());
    jraDatumpr.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatumpr.setColumnName("DATUMPR");
    jraDatumpr.setDataSet(fVirmani.getRaQueryDataSet());
    jraJedzav.setColumnName("JEDZAV");
    jraJedzav.setDataSet(fVirmani.getRaQueryDataSet());
    jraKnjig.setColumnName("KNJIG");
    jraKnjig.setDataSet(fVirmani.getRaQueryDataSet());
    jraMjesto.setColumnName("MJESTO");
    jraMjesto.setDataSet(fVirmani.getRaQueryDataSet());
    jraNacizv.setColumnName("NACIZV");
    jraNacizv.setDataSet(fVirmani.getRaQueryDataSet());

    jraNateret.setFont(new java.awt.Font("Monospaced", 0, 12));
    jraNateret.setColumnName("NATERET");
    jraNateret.setDataSet(fVirmani.getRaQueryDataSet());
    jraNateret.setBorder(BorderFactory.createLineBorder(Color.black));
    jraNateret.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        jraNateret_keyPressed(e);
      }
    });

    jraPnbo1.setColumnName("PNBO1");
    jraPnbo1.setDataSet(fVirmani.getRaQueryDataSet());
    jraPnbo2.setColumnName("PNBO2");
    jraPnbo2.setDataSet(fVirmani.getRaQueryDataSet());

    /*jraPnbo2.addFocusListener(new java.awt.event.FocusAdapter(){
      public void focusLost(FocusEvent e) {
        jraPnbo2_focusLost(e);
      }
    });*/

    jraPnbz1.setColumnName("PNBZ1");
    jraPnbz1.setDataSet(fVirmani.getRaQueryDataSet());
    jraPnbz2.setColumnName("PNBZ2");
    jraPnbz2.setDataSet(fVirmani.getRaQueryDataSet());
    jraRbr.setColumnName("RBR");
    jraRbr.setDataSet(fVirmani.getRaQueryDataSet());
    jraSif1.setColumnName("SIF1");
    jraSif1.setDataSet(fVirmani.getRaQueryDataSet());
    jraSif2.setColumnName("SIF2");
    jraSif2.setDataSet(fVirmani.getRaQueryDataSet());
    jraSif3.setColumnName("SIF3");
    jraSif3.setDataSet(fVirmani.getRaQueryDataSet());

    jraSvrha.setColumnName("SVRHA");
    jraSvrha.setDataSet(fVirmani.getRaQueryDataSet());
    jraSvrha.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        jraSvrha_keyPressed(e);
      }
    });

    jraUkorist.setColumnName("UKORIST");
    jraUkorist.setDataSet(fVirmani.getRaQueryDataSet());
    jraUkorist.setBorder(BorderFactory.createLineBorder(Color.black));
    jraUkorist.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        jraUkorist_keyPressed(e);
      }
    });

    this.setLayout(borderLayout1);
    jlIznosTxt.setFont(new java.awt.Font("Dialog", 1, 12));
    jlIznosTxt.setText("IZNOS");

    jraIznos.setColumnName("IZNOS");
    jraIznos.setDataSet(fVirmani.getRaQueryDataSet());
    jraIznos.setNextFocusableComponent(jraBrracuk);

    jlKn.setBackground(Color.white);
    jlKn.setFont(new java.awt.Font("Dialog", 1, 12));
    jlKn.setBorder(BorderFactory.createLineBorder(Color.black));
    jlKn.setHorizontalAlignment(SwingConstants.CENTER);
    jlKn.setText("kn");
    jlStatOb.setBorder(BorderFactory.createLineBorder(Color.black));
    jlOvjeraNalogodavca.setFont(new java.awt.Font("Dialog", 0, 9));
    jlOvjeraNalogodavca.setBorder(BorderFactory.createLineBorder(Color.black));
    jlOvjeraNalogodavca.setHorizontalAlignment(SwingConstants.CENTER);
    jlOvjeraNalogodavca.setText("Ovjera nalogodavca");
    jlOvjeraBanke.setFont(new java.awt.Font("Dialog", 0, 9));
    jlOvjeraBanke.setBorder(BorderFactory.createLineBorder(Color.black));
    jlOvjeraBanke.setHorizontalAlignment(SwingConstants.CENTER);
    jlOvjeraBanke.setText("Ovjera banke");
    jlDatumPodnošenja.setFont(new java.awt.Font("Dialog", 0, 9));
    jlDatumPodnošenja.setHorizontalAlignment(SwingConstants.CENTER);
    jlDatumPodnošenja.setText("Datum podnošenja");
    jlPotpisPrimatelja.setFont(new java.awt.Font("Dialog", 0, 9));
    jlPotpisPrimatelja.setBorder(BorderFactory.createLineBorder(Color.black));
    jlPotpisPrimatelja.setHorizontalAlignment(SwingConstants.CENTER);
    jlPotpisPrimatelja.setText("Potpis primatelja");
    jlPotpisPrimatelja.setVerticalAlignment(SwingConstants.TOP);
    jlPotpisPrimatelja.setVerticalTextPosition(SwingConstants.TOP);
    jcbIsplata.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbIsplata.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbIsplata.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cbAction("I");
      }
    });
    jcbUplata.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbUplata.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cbAction("U");
      }
    });
    jcbPrijenos.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbPrijenos.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cbAction("P");
      }
    });
    jcbHitnost1.setFont(new java.awt.Font("Dialog", 0, 9));
    jcbHitnost1.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbHitnost1.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbHitnost1.setText("HITNOST");
    jcbHitnost1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cbAction("H1");
      }
    });
    jcbHitnost2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cbAction("H2");
      }
    });
    pui.add(jcbPrijenos);
    pui.add(jcbUplata);
    pui.add(jcbIsplata);
    jlNazAdrPlatitelj.setFont(new java.awt.Font("Dialog", 0, 9));
    jlNazAdrPlatitelj.setText("naziv (ime) i adresa");
    jlNazAdrPrimatelj.setText("naziv (ime) i adresa");
    jlNazAdrPrimatelj.setFont(new java.awt.Font("Dialog", 0, 9));
    jlModelPlatitelja.setFont(new java.awt.Font("Dialog", 0, 9));
    jlModelPlatitelja.setText("Model");
    jlModelPrimatelja.setText("Model");
    jlModelPrimatelja.setFont(new java.awt.Font("Dialog", 0, 9));
    jlPozivOdob.setText("Poziv na broj odobrenja");
    jlPozivOdob.setFont(new java.awt.Font("Dialog", 0, 9));
    jlOpisPlacanja.setFont(new java.awt.Font("Dialog", 0, 9));
    jlOpisPlacanja.setText("Opis plaæanja");
    jlDatumPodnosenja.setText("Datum valute/uplate/isplate");
    jlDatumPodnosenja.setFont(new java.awt.Font("Dialog", 0, 9));
    jlStatObilj.setText("Stat. ob.");
    jlStatObilj.setFont(new java.awt.Font("Dialog", 0, 9));
    jlSifraNacPl.setFont(new java.awt.Font("Dialog", 0, 9));
    jlSifraNacPl.setText("Šifra op. pl.");
    jlHitnost.setText("Hitnost");
    jlHitnost.setFont(new java.awt.Font("Dialog", 0, 9));
//    jlHica.setFont(new java.awt.Font("Dialog", 0, 9));
//    jlHica.setText("Hitnost");
    jcbPrijenos.setFont(new java.awt.Font("Dialog", 0, 9));
    jcbPrijenos.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbPrijenos.setText("PRIJENOS");
    jcbUplata.setFont(new java.awt.Font("Dialog", 0, 9));
    jcbUplata.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbUplata.setText("UPLATA");
    jcbIsplata.setFont(new java.awt.Font("Dialog", 0, 9));
    jcbIsplata.setText("ISPLATA");
    jlNalogZaPlacanje.setFont(new java.awt.Font("Dialog", 1, 9));
    jlNalogZaPlacanje.setHorizontalAlignment(SwingConstants.CENTER);
    jlNalogZaPlacanje.setText("NALOG ZA PLAÆANJE");
//    jpDetail.add(jlHica,    new XYConstraints(200, 30, -1, -1));
    jpDetail.add(jraSif1,    new XYConstraints(85, 253, 40, 25));
    jpDetail.add(jlStatObilj,    new XYConstraints(17, 240, -1, -1));
    jpDetail.add(jlSifraNacPl,     new XYConstraints(83, 240, -1, -1));

    jpDetail.add(jraDatumizv,         new XYConstraints(15, 292, 120, 25));

    jpDetail.add(jlDatumPodnosenja,  new XYConstraints(17, 279, -1, -1));
    jpDetail.add(jlOpisPlacanja,      new XYConstraints(142, 240, -1, -1));
    jpDetail.add(jraSvrha,                  new XYConstraints(140, 253, 460, 25));
    jpDetail.add(jlNateret,       new XYConstraints(15, 81, -1, -1));
    jpDetail.add(jraBrracuk,              new XYConstraints(285, 176, 315, 25));
    jpDetail.add(jlBrracnt,          new XYConstraints(287, 84, 272, -1));
    jpDetail.add(jlBrracuk,               new XYConstraints(287, 163, 272, -1));
    jpDetail.add(jraNateret,           new XYConstraints(15, 97, 200, 63));
    jpDetail.add(jlUkorist,     new XYConstraints(15, 160, -1, -1));
    jpDetail.add(jraUkorist,                  new XYConstraints(15, 176, 200, 63));
    jpDetail.add(jlNazAdrPlatitelj,    new XYConstraints(80, 84, -1, -1));
    jpDetail.add(jlNazAdrPrimatelj,    new XYConstraints(85, 163, -1, -1));
    jpDetail.add(jraBrracnt,         new XYConstraints(285, 97, 315, 25));
    jpDetail.add(jraPnbz2,            new XYConstraints(220, 135, 380, 25));
    jpDetail.add(jlPozivZad,    new XYConstraints(222, 122, -1, -1));
    jpDetail.add(jraPnbz1,     new XYConstraints(220, 97, 30, 25));
    jpDetail.add(jraPnbo1,       new XYConstraints(220, 176, 30, 25));
    jpDetail.add(jlModelPlatitelja,     new XYConstraints(222, 84, 30, -1));
    jpDetail.add(jlModelPrimatelja,      new XYConstraints(222, 163, -1, -1));
    jpDetail.add(jraPnbo2,             new XYConstraints(220, 214, 380, 25));
    jpDetail.add(jlPozivOdob,     new XYConstraints(222, 201, -1, -1));
    jpDetail.add(jraIznos,       new XYConstraints(340, 57, 260, 25));
    jpDetail.add(jlKn,          new XYConstraints(285, 59, 35, 21));
    jpDetail.add(jlIznosTxt,     new XYConstraints(236, 57, -1, 25));
    jpDetail.add(jlStatOb,            new XYConstraints(16, 254, 54, 23));
    jpDetail.add(jlOvjeraNalogodavca,              new XYConstraints(141, 282, 315, 86));
    jpDetail.add(jlOvjeraBanke,             new XYConstraints(455, 282, 144, 86));
    jpDetail.add(jlPotpisPrimatelja,    new XYConstraints(16, 354, 118, 14));
    jpDetail.add(jlNalogZaPlacanje,          new XYConstraints(380, 26, -1, 20));
    jpDetail.add(jcbIsplata, new XYConstraints(542, 26, 57, -1));
    jpDetail.add(jcbUplata, new XYConstraints(486, 26, 54, -1));
    jpDetail.add(jcbPrijenos, new XYConstraints(300, 26, 67, -1));
    jpDetail.add(jcbHitnost2, new XYConstraints(283, 26, 14, -1));
    jpDetail.add(jcbHitnost1, new XYConstraints(218, 26, 66, -1));
    jpDetail.add(jlDatumPodnošenja,     new XYConstraints(15, 315, 118, -1));
    jpDetail.add(jraDatumpr,   new XYConstraints(15, 327, 120, 25));

    // hendlanje fokusa zbog razbacanosti po panelu
//    jraNateret.setNextFocusableComponent(jraSvrha);
//    jraSvrha.setNextFocusableComponent(jraUkorist);
//    jraUkorist.setNextFocusableComponent(jraBrracnt);
//    jraPnbz2.setNextFocusableComponent(jraSif1);
//    jraJedzav.setNextFocusableComponent(jraNateret);
//    jraPnbo2.setNextFocusableComponent(jraDatumpr);
    jraNateret.setNextFocusableComponent(jraBrracnt);
    jraBrracnt.setNextFocusableComponent(jraPnbz1);
    jraPnbz1.setNextFocusableComponent(jraPnbz2);
    jraPnbz2.setNextFocusableComponent(jraUkorist);
    jraUkorist.setNextFocusableComponent(jraBrracuk);
    jraBrracuk.setNextFocusableComponent(jraPnbo1);
    jraPnbo1.setNextFocusableComponent(jraPnbo2);
    jraPnbo2.setNextFocusableComponent(jraSif1);
    jraSif1.setNextFocusableComponent(jraSvrha);
    jraSvrha.setNextFocusableComponent(jraIznos);
    jraIznos.setNextFocusableComponent(jraDatumizv);
    jraDatumizv.setNextFocusableComponent(jraDatumpr);
    jraDatumpr.setNextFocusableComponent(jraNateret);
    this.add(jpDetail, BorderLayout.CENTER);
  }

  void jraBrracnt_keyReleased(KeyEvent e) {
    jraBrracnt.setBackground(Color.white);
    if(e.getKeyCode()==e.VK_F9) {
      fVirmani.getRaQueryDataSet().setString("BRRACNT",fVirmani.getZiro(0));
      jraPnbz1.requestFocus();
    }
  }

  void jraBrracuk_keyReleased(KeyEvent e) {
    jraBrracuk.setBackground(Color.white);
    if(e.getKeyCode()==e.VK_F9) {
      fVirmani.getRaQueryDataSet().setString("BRRACUK",fVirmani.getZiroPar());
      jraPnbo1.requestFocus();
    }
  }

  void jraNateret_keyPressed(KeyEvent e) {
    if(e.getKeyCode()==e.VK_F9) {
      fVirmani.getRaQueryDataSet().setString("NATERET", fVirmani.getNaTeret(jraNateret.getText(),"F9"));
      if(jraNateret.getText().equals(""))
        jraNateret.requestFocus();
      else
        jraPnbz1.requestFocus();
    }
    else if (e.getKeyCode()==e.VK_F8) {
      fVirmani.getRaQueryDataSet().setString("NATERET", fVirmani.getNaTeret(jraNateret.getText(),"F8"));
//      if(jraNateret.getText().equals(""))
//        jraNateret.requestFocus();
//      else
        jraBrracnt.requestFocus();
    }
//    else if (e.getKeyCode()==e.VK_ENTER) {
//      System.out.println("nateret - enter");
//      if(jraNateret.getText().equals(""))
//        jraBrracnt.requestFocus();
//      else
//        jraPnbz1.requestFocus();
//    }
  }

  void jraUkorist_keyPressed(KeyEvent e) {
    String filter="";
    int filIdx=jraUkorist.getText().indexOf("\n");

    if(filIdx>0) {
      filter = jraUkorist.getText().substring(0,filIdx).trim();
    } else {
      filter = jraUkorist.getText().trim();
    }

    if(e.getKeyCode()==e.VK_F8) {
      String[] fromVir = fVirmani.getUKorist(filter,"F9");
      if (fromVir[3].equals("")) return;
      puniVirman(fromVir);
      if(jraUkorist.getText().equals(""))
        jraUkorist.requestFocus();
      else if(jraBrracnt.getText().equals(""))
        jraBrracnt.requestFocus();
      else
        jraIznos.requestFocus();
    } else if(e.getKeyCode()==e.VK_F9) {
      String[] fromVir = fVirmani.getUKorist(filter,"F8");
      if (fromVir[3].equals("")) return;
      fVirmani.getRaQueryDataSet().setString("UKORIST", fromVir[3]+"\n"+fromVir[5]+"\n"+fromVir[6]+" "+fromVir[4]);
//      fVirmani.getRaQueryDataSet().setString("UKORIST", fVirmani.getUKorist(filter,"F8"));
      if(jraUkorist.getText().equals(""))
        jraUkorist.requestFocus();
      else if(jraBrracnt.getText().equals(""))
        jraBrracnt.requestFocus();
      else
        jraPnbo1.requestFocus();
    }
  }

  void jraSvrha_keyPressed(KeyEvent e) {
    if(e.getKeyCode()== e.VK_F9) {
      String[] fromVir = fVirmani.getSvrha(jraSvrha.getText());
      if(fromVir == null) {
//        System.out.println("fVirmani.getSvrha(jraSvrha.getText()) je null!!!");
        jraSvrha.requestFocus();
      } else {
        puniVirman(fromVir);
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            jraIznos.requestFocus();
          }
        });
      }
    }
  }

  private void puniVirman(String[] bigDil) {
    fVirmani.getRaQueryDataSet().setString("SIF1",bigDil[14]);
    fVirmani.getRaQueryDataSet().setString("PNBZ1",bigDil[12]);
    fVirmani.getRaQueryDataSet().setString("PNBZ2",bigDil[13]);
    fVirmani.getRaQueryDataSet().setString("BRRACNT",bigDil[10]);
    fVirmani.getRaQueryDataSet().setString("NATERET",bigDil[7]);
    fVirmani.getRaQueryDataSet().setString("SVRHA", bigDil[8]);
    fVirmani.getRaQueryDataSet().setString("JEDZAV",bigDil[6]);
    fVirmani.getRaQueryDataSet().setString("UKORIST",bigDil[9]);
    fVirmani.getRaQueryDataSet().setString("BRRACUK",bigDil[17]);
    fVirmani.getRaQueryDataSet().setString("PNBO1",bigDil[18]);
    fVirmani.getRaQueryDataSet().setString("PNBO2",bigDil[19]);
    try {
      java.math.BigDecimal b = new java.math.BigDecimal(java.text.NumberFormat.getInstance().parse(bigDil[20].toString()).doubleValue()).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
      fVirmani.getRaQueryDataSet().setBigDecimal("IZNOS", b);
    }
    catch (Exception ex) {
      System.err.println("Puknuce na riplaejsevima ?? ");
      ex.printStackTrace();
    }
  }

  void jraBrracnt_focusLost(FocusEvent e) {
//    String jedZav = jraBrracnt.getText().substring(0, jraBrracnt.getText().indexOf("-"));
//    fVirmani.getRaQueryDataSet().setString("JEDZAV", jedZav);
  }

  void jraPnbo2_focusLost(FocusEvent e) {
    if (jraSvrha.getDataSet().getString("SVRHA").equals(""))
      jraSvrha.getDataSet().setString("SVRHA",jraPnbo2.getDataSet().getString("PNBO2"));
  }

  public void cbAction(String chbox){
    String parametar;
    if (!fVirmani.getRaQueryDataSet().getString("JEDZAV").equals(""))
      parametar = fVirmani.getRaQueryDataSet().getString("JEDZAV");
    else
      parametar = "NNDNN";
    if (chbox.equals("P")){
        parametar = parametar.substring(0,2)+"DNN";
    } else if (chbox.equals("U")){
        parametar = parametar.substring(0,2)+"NDN";
    } else if (chbox.equals("I")){
        parametar = parametar.substring(0,2)+"NND";
    } else if (chbox.equals("H1")){
      if (jcbHitnost1.isSelected()){
        parametar = "D"+parametar.substring(1,parametar.length());
      } else {
        parametar = "N"+parametar.substring(1,parametar.length());
      }
    } else if (chbox.equals("H2")){
      if (jcbHitnost2.isSelected()){
        parametar = parametar.substring(0,1)+"D"+parametar.substring(2,parametar.length());
      } else {
        parametar = parametar.substring(0,1)+"N"+parametar.substring(2,parametar.length());
      }
    }
    fVirmani.getRaQueryDataSet().setString("JEDZAV", parametar);
  }

  public void resetCheckboxes(){
    try {
      jcbHitnost1.setSelected(fVirmani.getRaQueryDataSet().getString("JEDZAV").substring(0,1).equals("D"));
      jcbHitnost2.setSelected(fVirmani.getRaQueryDataSet().getString("JEDZAV").substring(1,2).equals("D"));
      jcbPrijenos.setSelected(fVirmani.getRaQueryDataSet().getString("JEDZAV").substring(2,3).equals("D"));
      jcbUplata.setSelected(fVirmani.getRaQueryDataSet().getString("JEDZAV").substring(3,4).equals("D"));
      jcbIsplata.setSelected(fVirmani.getRaQueryDataSet().getString("JEDZAV").substring(4,5).equals("D"));
    }
    catch (Exception ex) {
      jcbPrijenos.setSelected(true);
    }
  }
}