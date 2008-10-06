/****license*****************************************************************
**   file: jpVirmani.java
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
package hr.restart.zapod;



import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;





public class jpVirmani extends JPanel {

  raCommonClass rcc = raCommonClass.getraCommonClass();

  dM dm = dM.getDataModule();

  Valid vl = Valid.getValid();



  frmVirmani fVirmani;

  JPanel jpDetail = new JPanel();



  XYLayout lay = new XYLayout();

  JLabel jlApp = new JLabel();

  JLabel jlBrracnt = new JLabel();

  JLabel jlBrracuk = new JLabel();

  JLabel jlCkey = new JLabel();

  JLabel jlDatumpr = new JLabel();

  JLabel jlIznos = new JLabel();

  JLabel jlJedzav = new JLabel();

  JLabel jlKnjig = new JLabel();

  JLabel jlNateret = new JLabel();

  JLabel jlPozOd = new JLabel();

  JLabel jlPoziv = new JLabel();

  JLabel jlRbr = new JLabel();

  JLabel jlSif1 = new JLabel();

  JLabel jlSvrha = new JLabel();

  JLabel jlUkorist = new JLabel();

  JraTextField jraApp = new JraTextField();

  JraTextField jraBrracnt = new JraTextField() {
    public void valueChanged() {
      jraBrracnt_focusLost(null);
    }
  };

  JraTextField jraBrracuk = new JraTextField();

  JraTextField jraCkey = new JraTextField();

  JraTextField jraDatumizv = new JraTextField();

  JraTextField jraDatumpr = new JraTextField();

  JraTextField jraIznos = new JraTextField();

  JraTextField jraJedzav = new JraTextField();

  JraTextField jraKnjig = new JraTextField();

  JraTextField jraMjesto = new JraTextField();

  JraTextField jraNacizv = new JraTextField();

  hr.restart.swing.JraTextArea jraNateret = new hr.restart.swing.JraTextArea();

  JraTextField jraPnbo1 = new JraTextField();

  JraTextField jraPnbo2 = new JraTextField();

  JraTextField jraPnbz1 = new JraTextField();

  JraTextField jraPnbz2 = new JraTextField();

  JraTextField jraRbr = new JraTextField();

  JraTextField jraSif1 = new JraTextField();

  JraTextField jraSif2 = new JraTextField();

  JraTextField jraSif3 = new JraTextField();

  hr.restart.swing.JraTextArea jraSvrha = new hr.restart.swing.JraTextArea();

  hr.restart.swing.JraTextArea jraUkorist = new hr.restart.swing.JraTextArea();



  private JLabel jLabel1 = new JLabel();

  private JLabel jLabel2 = new JLabel();

  private JLabel jlMjestoIDat = new JLabel();

  JraTextField jtfVertical = new JraTextField();

  JraTextField jtfHorizontal = new JraTextField();

  private JLabel jlNalog = new JLabel();

  private JLabel jlPrim = new JLabel();

  private JLabel jlNac = new JLabel();

  private JLabel jlIzvr = new JLabel();

  private JLabel jlPoz2 = new JLabel();

  private JLabel jlPozZad = new JLabel();

  private JLabel jlPozNB2 = new JLabel();

  private JLabel jlPozOdob2 = new JLabel();

  private JLabel jlPecat = new JLabel();

  private JLabel jlZarez = new JLabel();



  public jpVirmani(frmVirmani f) {

    try {

      fVirmani = f;

      jbInit();

    }

    catch(Exception e) {

      e.printStackTrace();

    }

  }



  private void jbInit() throws Exception {

    jpDetail.setLayout(lay);

    lay.setWidth(620);

    lay.setHeight(395);



    jraNateret.setColumns(7);



    jlApp.setText("Aplikacija");

    jlBrracnt.setText("broj ra\u010Duna");

    jlBrracuk.setText("broj ra\u010Duna");

    jlCkey.setText("Kljuè");

    jlDatumpr.setFont(new java.awt.Font("Dialog", 0, 10));

    jlDatumpr.setText("(datum predaje Zavodu)");

    jlIznos.setFont(new java.awt.Font("Dialog", 0, 14));

    jlIznos.setText("kn");

    jlJedzav.setFont(new java.awt.Font("Dialog", 0, 10));

    jlJedzav.setText("(Jedinica zavoda)");

    jlKnjig.setText("Knjigovodstvo");

    jlNateret.setText("Knjižite na teret našeg ra\u010Duna");

    jlPozOd.setFont(new java.awt.Font("Dialog", 0, 9));

    jlPozOd.setText("poziv");

    jlPoziv.setFont(new java.awt.Font("Dialog", 0, 9));

    jlPoziv.setText("poziv");

    jlRbr.setText("Rbr");

    jlSif1.setFont(new java.awt.Font("Dialog", 0, 10));

    jlSif1.setText("šifra");

    jlSvrha.setText("Svrha doznake");

    jlUkorist.setText("U korist ra\u010Duna");

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

    jraIznos.setColumnName("IZNOS");

    jraIznos.setDataSet(fVirmani.getRaQueryDataSet());

    jraJedzav.setColumnName("JEDZAV");

    jraJedzav.setDataSet(fVirmani.getRaQueryDataSet());

    jraKnjig.setColumnName("KNJIG");

    jraKnjig.setDataSet(fVirmani.getRaQueryDataSet());

    jraMjesto.setColumnName("MJESTO");

    jraMjesto.setDataSet(fVirmani.getRaQueryDataSet());

    jraNacizv.setColumnName("NACIZV");

    jraNacizv.setDataSet(fVirmani.getRaQueryDataSet());



    jraNateret.setFont(new java.awt.Font("Monospaced", 0, 12));

//    jraNateret.setBorder(border2);

    jraNateret.setColumnName("NATERET");

    jraNateret.setDataSet(fVirmani.getRaQueryDataSet());

    jraNateret.addKeyListener(new java.awt.event.KeyAdapter() {

      public void keyPressed(KeyEvent e) {

        jraNateret_keyPressed(e);

      }

    });

    jraPnbo1.setColumnName("PNBO1");

    jraPnbo1.setDataSet(fVirmani.getRaQueryDataSet());

    jraPnbo2.setColumnName("PNBO2");

    jraPnbo2.setDataSet(fVirmani.getRaQueryDataSet());

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

//    jraSvrha.setBorder(border3);

    jraSvrha.setColumnName("SVRHA");

    jraSvrha.setDataSet(fVirmani.getRaQueryDataSet());

    jraSvrha.addKeyListener(new java.awt.event.KeyAdapter() {

      public void keyPressed(KeyEvent e) {

        jraSvrha_keyPressed(e);

      }

    });

//    jraUkorist.setBorder(border5);

    jraUkorist.setColumnName("UKORIST");

    jraUkorist.setDataSet(fVirmani.getRaQueryDataSet());

    jraUkorist.addKeyListener(new java.awt.event.KeyAdapter() {

      public void keyPressed(KeyEvent e) {

        jraUkorist_keyPressed(e);

      }

    });



    jLabel1.setText("ZAVOD ZA PLATNI PROMET");

    jLabel2.setText("POSEBAN NALOG ZA PRIJENOS");

    jlMjestoIDat.setFont(new java.awt.Font("Dialog", 0, 10));

    jlMjestoIDat.setText("(mjesto i datum izvršenja)");

    jtfVertical.setEnabled(false);

    jtfVertical.setBorder(BorderFactory.createLineBorder(Color.black));

    jtfVertical.setRequestFocusEnabled(false);

    jtfVertical.setEditable(false);

    jtfHorizontal.setBorder(BorderFactory.createLineBorder(Color.black));

    jlNalog.setFont(new java.awt.Font("Dialog", 0, 10));

    jlNalog.setText("(naziv i sjedište nalgodavca)");

    jlPrim.setFont(new java.awt.Font("Dialog", 0, 10));

    jlPrim.setText("(naziv i sjedište primatelja)");

    jlNac.setFont(new java.awt.Font("Dialog", 0, 10));

    jlNac.setText("na\u010Din");

    jlIzvr.setFont(new java.awt.Font("Dialog", 0, 10));

    jlIzvr.setText("izvrš.");

    jlPoz2.setFont(new java.awt.Font("Dialog", 0, 9));

    jlPoz2.setText("na broj");

    jlPozZad.setFont(new java.awt.Font("Dialog", 0, 9));

    jlPozZad.setText("(zaduž.)");

    jlPozNB2.setFont(new java.awt.Font("Dialog", 0, 9));

    jlPozNB2.setText("na broj");

    jlPozOdob2.setFont(new java.awt.Font("Dialog", 0, 9));

    jlPozOdob2.setText("(odobr.)");

    jlPecat.setFont(new java.awt.Font("Dialog", 0, 10));

    jlPecat.setText("(pe\u010Dat i potpis nalogodavca)");

    jlZarez.setText(",");



    jpDetail.add(jlBrracnt,     new XYConstraints(315, 70, -1, -1));

    jpDetail.add(jraBrracnt,          new XYConstraints(315, 90, 220, 25));

    jpDetail.add(jraJedzav,       new XYConstraints(15, 28, 200, 25));

    jpDetail.add(jraNateret,          new XYConstraints(15, 90, 270, 55));

    jpDetail.add(jLabel2,      new XYConstraints(315, 10, -1, -1));

    jpDetail.add(jLabel1,  new XYConstraints(15, 10, -1, -1));

    jpDetail.add(jlJedzav,        new XYConstraints(72, 53, -1, -1));

    jpDetail.add(jlNateret,      new XYConstraints(15, 70, -1, -1));

    jpDetail.add(jraPnbo2,             new XYConstraints(390, 290, 220, 25));

    jpDetail.add(jlBrracuk,            new XYConstraints(315, 240, -1, -1));

    jpDetail.add(jraBrracuk,             new XYConstraints(315, 260, 220, 25));

    jpDetail.add(jraPnbz2,          new XYConstraints(390, 125, 220, 25));

    jpDetail.add(jraIznos,          new XYConstraints(390, 200, 220, 25));

    jpDetail.add(jraPnbo1,              new XYConstraints(355, 290, 30, 25));

    jpDetail.add(jraUkorist,            new XYConstraints(15, 255, 270, 55));

    jpDetail.add(jtfVertical,              new XYConstraints(300, 70, 1, 255));

    jpDetail.add(jraNacizv,      new XYConstraints(580, 90, 30, 25));

    jpDetail.add(jraPnbz1,        new XYConstraints(355, 125, 30, 25));

    jpDetail.add(jraSif1,         new XYConstraints(485, 170, 40, 25));

    jpDetail.add(jraSif3,           new XYConstraints(570, 170, 40, 25));

    jpDetail.add(jraSif2,            new XYConstraints(530, 170, 40, 25));

    jpDetail.add(jlIznos,       new XYConstraints(369, 202, -1, -1));

    jpDetail.add(jlSif1,    new XYConstraints(452, 174, -1, -1));

    jpDetail.add(jraDatumpr,         new XYConstraints(250, 340, 100, 25));

    jpDetail.add(jraDatumizv,        new XYConstraints(510, 340, 100, 25));

    jpDetail.add(jlDatumpr,          new XYConstraints(244, 365, -1, -1));

    jpDetail.add(jlMjestoIDat,        new XYConstraints(425, 365, -1, -1));

    jpDetail.add(jraSvrha,      new XYConstraints(15, 180, 270, 55));

    jpDetail.add(jlSvrha,    new XYConstraints(15, 163, -1, -1));

    jpDetail.add(jlUkorist,   new XYConstraints(15, 238, -1, -1));

    jpDetail.add(jlNalog,   new XYConstraints(73, 145, -1, -1));

    jpDetail.add(jlPrim,      new XYConstraints(77, 310, -1, -1));











    jpDetail.add(jlNac,  new XYConstraints(548, 89, -1, -1));

    jpDetail.add(jlIzvr,  new XYConstraints(548, 101, -1, -1));

    jpDetail.add(jlPoziv,  new XYConstraints(315, 118, -1, -1));

    jpDetail.add(jlPoz2,    new XYConstraints(315, 128, -1, -1));

    jpDetail.add(jlPozZad,   new XYConstraints(315, 138, -1, -1));

    jpDetail.add(jlPozOd,   new XYConstraints(315, 288, -1, -1));

    jpDetail.add(jlPozNB2,   new XYConstraints(315, 298, -1, -1));

    jpDetail.add(jlPozOdob2,    new XYConstraints(315, 308, -1, -1));

    jpDetail.add(jraMjesto,       new XYConstraints(370, 340, 130, 25));

    jpDetail.add(jlPecat,  new XYConstraints(43, 365, -1, -1));





    jpDetail.add(jtfHorizontal,           new XYConstraints(15, 365, 190, 1));

    jpDetail.add(jlZarez,     new XYConstraints(503, 350, -1, -1));





    // hendlanje fokusa zbog razbacanosti po panelu

    jraNateret.setNextFocusableComponent(jraSvrha);

    jraSvrha.setNextFocusableComponent(jraUkorist);

    jraUkorist.setNextFocusableComponent(jraBrracnt);

    jraPnbz2.setNextFocusableComponent(jraSif1);

    jraIznos.setNextFocusableComponent(jraBrracuk);

    jraJedzav.setNextFocusableComponent(jraNateret);

    jraPnbo2.setNextFocusableComponent(jraDatumpr);

    this.add(jpDetail, BorderLayout.CENTER);

  }



  void jraBrracnt_keyReleased(KeyEvent e) {

    jraBrracnt.setBackground(Color.white);

    if(e.getKeyCode()==e.VK_F9)

    {

      fVirmani.getRaQueryDataSet().setString("BRRACNT",fVirmani.getZiro(0));

      jraNacizv.requestFocus();

    }

  }



  void jraBrracuk_keyReleased(KeyEvent e) {

    jraBrracuk.setBackground(Color.white);

    if(e.getKeyCode()==e.VK_F9)

    {

      fVirmani.getRaQueryDataSet().setString("BRRACUK",fVirmani.getZiroPar());

      jraPnbo1.requestFocus();

    }

  }



  void jraNateret_keyPressed(KeyEvent e) {

    if(e.getKeyCode()==e.VK_F9)

    {

      fVirmani.getRaQueryDataSet().setString("NATERET", fVirmani.getNaTeret(jraNateret.getText(),"F9"));

      if(jraNateret.getText().equals(""))

        jraNateret.requestFocus();

      else

        jraSvrha.requestFocus();

    }

    else if (e.getKeyCode()==e.VK_F8)

    {

      fVirmani.getRaQueryDataSet().setString("NATERET", fVirmani.getNaTeret(jraNateret.getText(),"F8"));

      if(jraNateret.getText().equals(""))

        jraNateret.requestFocus();

      else

        jraSvrha.requestFocus();

    }

  }



  void jraUkorist_keyPressed(KeyEvent e) {
    String filter="";
    int filIdx=jraUkorist.getText().indexOf("\n");

    if(filIdx>0) {
      filter = jraUkorist.getText().substring(0,filIdx).trim();
    } else {
      filter = jraUkorist.getText().trim();
    }

    if(e.getKeyCode()==e.VK_F9) {
      String[] fromVir = fVirmani.getUKorist(filter,"F9");
      puniVirman(fromVir);
      if(jraUkorist.getText().equals(""))
        jraUkorist.requestFocus();
      else if(jraBrracnt.getText().equals(""))
        jraBrracnt.requestFocus();
      else
        jraIznos.requestFocus();
    } else if(e.getKeyCode()==e.VK_F8) {
      String[] fromVir = fVirmani.getUKorist(filter,"F8");
      fVirmani.getRaQueryDataSet().setString("UKORIST", fromVir[3]+"\n"+fromVir[5]+",\n"+fromVir[6]+" "+fromVir[4]);
//      fVirmani.getRaQueryDataSet().setString("UKORIST", fVirmani.getUKorist(filter,"F8"));
      if(jraUkorist.getText().equals(""))
        jraUkorist.requestFocus();
      else if(jraBrracnt.getText().equals(""))
        jraBrracnt.requestFocus();
      else
        jraPnbo1.requestFocus();
    }

/*
    String filter="";

    int filIdx=jraUkorist.getText().indexOf("\n");

     if(filIdx>0)

       filter = jraUkorist.getText().substring(0,filIdx).trim();

     else

        filter = jraUkorist.getText().trim();

    if(e.getKeyCode()==e.VK_F9)

    {

//      fVirmani.getRaQueryDataSet().setString("UKORIST", fVirmani.getUKorist(filter,"F9"));

      if(jraUkorist.getText().equals(""))

        jraUkorist.requestFocus();

      else if(jraBrracnt.getText().equals(""))

        jraBrracnt.requestFocus();

      else

        jraNacizv.requestFocus();

    }

    else if(e.getKeyCode()==e.VK_F8)

    {

//      fVirmani.getRaQueryDataSet().setString("UKORIST", fVirmani.getUKorist(filter,"F8"));

      if(jraUkorist.getText().equals(""))

        jraUkorist.requestFocus();

      else if(jraBrracnt.getText().equals(""))

        jraBrracnt.requestFocus();

      else

        jraNacizv.requestFocus();

    }
*/
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
    fVirmani.getRaQueryDataSet().setBigDecimal("IZNOS",
    new java.math.BigDecimal(bigDil[20].replace(',','.')));
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

/*
    if(e.getKeyCode()== e.VK_F9)

    {

      fVirmani.getRaQueryDataSet().setString("SVRHA", fVirmani.getSvrha(jraSvrha.getText())[4]);

      if(jraSvrha.getText().equals(""))

      {

        jraSvrha.requestFocus();

      }

      else

      {

        jraUkorist.requestFocus();

      }

    }
*/
  }



  void jraBrracnt_focusLost(FocusEvent e) {

    String jedZav = jraBrracnt.getText().substring(0, jraBrracnt.getText().indexOf("-"));

    fVirmani.getRaQueryDataSet().setString("JEDZAV", jedZav);

  }

}

