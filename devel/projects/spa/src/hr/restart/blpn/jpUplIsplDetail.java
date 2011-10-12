/****license*****************************************************************
**   file: jpUplIsplDetail.java
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
package hr.restart.blpn;

import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMasterDetail;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.raKnjigChangeListener;
import hr.restart.zapod.raKonta;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpUplIsplDetail extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  raMasterDetail mainClass;
  UplIsplAdvancedDetail advui = null;
//  frmUplIspl fuj;

  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCradnik = new JLabel();
  JLabel jlDatum = new JLabel();
  JLabel jlIzdatak = new JLabel();
  JLabel jlOpis = new JLabel();
  JLabel jlPrimitak = new JLabel();
  JLabel jlPvizdatak = new JLabel();
  JLabel jlPvprimitak = new JLabel();
  JLabel jlStavka = new JLabel();
  JLabel jlTecaj = new JLabel();
  JLabel jlTko = new JLabel();
  JraButton jbSelCradnik = new JraButton();
  JraButton jbSelStavka = new JraButton();
  JraButton jbURA = new JraButton();
  JraTextField jraDatum = new JraTextField(){
    public void valueChanged() {
      jraPrimitak_focusLost(null);
      jraIzdatak_focusLost(null);
      if (mainClass instanceof frmUplIspl)
        ((frmUplIspl)mainClass).getDetailSet().setBigDecimal("TECAJ", ((frmUplIspl)mainClass).ss.getTECAJ(((frmUplIspl)mainClass).getMasterSet().getString("OZNVAL"), ((frmUplIspl)mainClass).getDetailSet().getTimestamp("DATUM")));
      setDates();
    }
    /*public void focusLost(FocusEvent e) {
      super.focusLost(e);
        jraPrimitak_focusLost(e);
        jraIzdatak_focusLost(e);
        if (mainClass instanceof frmUplIspl)
          ((frmUplIspl)mainClass).getDetailSet().setBigDecimal("TECAJ", ((frmUplIspl)mainClass).ss.getTECAJ(((frmUplIspl)mainClass).getMasterSet().getString("OZNVAL"), ((frmUplIspl)mainClass).getDetailSet().getTimestamp("DATUM")));
        setDates();
    }*/
  };
  JraTextField jraIzdatak = new JraTextField(){
    public void valueChanged() {
      jraIzdatak_focusLost(null);
    }
    /*public void focusLost(FocusEvent e) {
      super.focusLost(e);
      jraIzdatak_focusLost(e);
    }*/
  };
  JraTextField jraOpis = new JraTextField();
  JraTextField jraPrimitak = new JraTextField(){
    public void valueChanged() {
      jraPrimitak_focusLost(null);
    }
    /*public void focusLost(FocusEvent e) {
      super.focusLost(e);
      jraPrimitak_focusLost(e);
    }*/
  };
  JraTextField jraPvizdatak = new JraTextField();
  JraTextField jraPvprimitak = new JraTextField();
  JraTextField jraTecaj = new JraTextField();
  JraTextField jraTko = new JraTextField();


  JLabel jlCorg = new JLabel();
  JlrNavField jlrCorg = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JraButton jbSelCorg = new JraButton();

  JlrNavField jlrStavka = new JlrNavField() {
    public void after_lookUp() {
      lookUpStavke();
    }
  };
  JlrNavField jlrOpisStavke = new JlrNavField() {
    public void after_lookUp() {
      lookUpStavke();
    }
  };
  JlrNavField jlrPrezime = new JlrNavField() {
    public void after_lookUp() {
      jlrCradnik.after_lookUp();
    }
  };
  JlrNavField jlrCradnik = new JlrNavField() {
    public void after_lookUp() {
      if (mainClass instanceof frmUplIspl)
        ((frmUplIspl)mainClass).getDetailSet().setString("TKO", ((frmUplIspl)mainClass).ss.getIme(jlrCradnik.getDataSet().getString("CRADNIK")));
      if (!jlrOpisStavke.getText().equals("")){
//        lookUpStavke();
      }
    }
  };

  public jpUplIsplDetail(raMasterDetail md) {
    try {
      mainClass = md;
      if (mainClass instanceof frmUplIspl) {
        
        jbInit();
      } else if (mainClass instanceof frmBlagIzv) {
        jbInit2();
      }
      OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
        public void knjigChanged(String oldKnjig, String newKnjig) {
          jlrCradnik.setRaDataSet(presPN.getRadnici());
          jlrPrezime.setRaDataSet(presPN.getRadnici());
          jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
          jlrNaziv.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());

        }
      });
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void setURAButton() {
    System.out.println("setURAButton ... ");
    jbURA.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ((frmUplIspl)mainClass).startURA();
      }
    });
    jbURA.setText("URA");
    jpDetail.add(jbURA, new XYConstraints(255, 20, 100, 21));
  }
  
  private void jbInit2() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(640);
    lay.setHeight(350);

    jlCradnik.setText("Radnik");
    jlDatum.setText("Datum stavke");
    jlCorg.setText("Org. jedinica");
    jlIzdatak.setText("Izdatak");
    jlOpis.setText("Opis");
    jlPrimitak.setText("Primitak");
    jlPvizdatak.setText("Izdatak u KN");
    jlPvprimitak.setText("Primitak u KN");
    jlStavka.setText("Vrsta troška");
    jlTecaj.setText("Te\u010Daj");
    jlTko.setText("Kome / Od koga");
    jraDatum.setColumnName("DATUM");
    jraDatum.setDataSet(((frmBlagIzv)mainClass).getDetailSet());
    jraDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jraIzdatak.setColumnName("IZDATAK");
    jraIzdatak.setDataSet(((frmBlagIzv)mainClass).getDetailSet());
    jraOpis.setColumnName("OPIS");
    jraOpis.setDataSet(((frmBlagIzv)mainClass).getDetailSet());

    jraPrimitak.setColumnName("PRIMITAK");
    jraPrimitak.setDataSet(((frmBlagIzv)mainClass).getDetailSet());
    jraPvizdatak.setColumnName("PVIZDATAK");
    jraPvizdatak.setDataSet(((frmBlagIzv)mainClass).getDetailSet());
    jraPvprimitak.setColumnName("PVPRIMITAK");
    jraPvprimitak.setDataSet(((frmBlagIzv)mainClass).getDetailSet());

    setStavka("1");

    jraTecaj.setColumnName("TECAJ");
    jraTecaj.setDataSet(((frmBlagIzv)mainClass).getDetailSet());
    jraTko.setColumnName("TKO");
    jraTko.setDataSet(((frmBlagIzv)mainClass).getDetailSet());

    jlrCradnik.setColumnName("CRADNIK");
    jlrCradnik.setDataSet(((frmBlagIzv)mainClass).getDetailSet());
    jlrCradnik.setColNames(new String[] {"PREZIME"});
    jlrCradnik.setTextFields(new JTextComponent[] {jlrPrezime});
    jlrCradnik.setVisCols(new int[] {0, 1});
    jlrCradnik.setSearchMode(0);
    jlrCradnik.setRaDataSet(presPN.getRadnici());
    jlrCradnik.setNavButton(jbSelCradnik);
    jlrPrezime.setColumnName("PREZIME");
    jlrPrezime.setNavProperties(jlrCradnik);
    jlrPrezime.setSearchMode(1);

    jlrCorg.setColumnName("CORG");
    jlrCorg.setDataSet(((frmBlagIzv)mainClass).getDetailSet());
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCorg.setVisCols(new int[] {0, 1});
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.setNavButton(jbSelCorg);
    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCorg);
    jlrNaziv.setSearchMode(1);
    this.add(jpDetail, BorderLayout.CENTER);
  }
  boolean advdetail=false, advdetnokonto=false;
  private void jbInit() throws Exception {
//    fuj = (frmUplIspl)mainClass;
    advdetail = frmParam.getParam("blpn","konparbl", "N", "Unos konta i partnera kroz stavku blagajne (D/N/M)").equals("D");
    advdetnokonto = frmParam.getParam("blpn","konparbl", "N", "Unos konta i partnera kroz stavku blagajne (D/N/M)").equals("M");

    jpDetail.setLayout(lay);
    lay.setWidth(640);
    lay.setHeight(350);

    jlCradnik.setText("Radnik");
    jlDatum.setText("Datum stavke");
    jlCorg.setText("Org. jedinica");
    jlIzdatak.setText("Izdatak");
    jlOpis.setText("Opis");
    jlPrimitak.setText("Primitak");
    jlPvizdatak.setText("Izdatak u KN");
    jlPvprimitak.setText("Primitak u KN");
    jlStavka.setText("Vrsta troška");
    jlTecaj.setText("Te\u010Daj");
    jlTko.setText("Kome / Od koga");
    jraDatum.setColumnName("DATUM");
    jraDatum.setDataSet(((frmUplIspl)mainClass).getDetailSet());
    jraDatum.setHorizontalAlignment(SwingConstants.CENTER);
    jraIzdatak.setColumnName("IZDATAK");
    jraIzdatak.setDataSet(((frmUplIspl)mainClass).getDetailSet());
    jraOpis.setColumnName("OPIS");
    jraOpis.setDataSet(((frmUplIspl)mainClass).getDetailSet());
    jraPrimitak.setColumnName("PRIMITAK");
    jraPrimitak.setDataSet(((frmUplIspl)mainClass).getDetailSet());
    jraPvizdatak.setColumnName("PVIZDATAK");
    jraPvizdatak.setDataSet(((frmUplIspl)mainClass).getDetailSet());
    jraPvprimitak.setColumnName("PVPRIMITAK");
    jraPvprimitak.setDataSet(((frmUplIspl)mainClass).getDetailSet());

    setStavka("1");

    jraTecaj.setColumnName("TECAJ");
    jraTecaj.setDataSet(((frmUplIspl)mainClass).getDetailSet());
    jraTko.setColumnName("TKO");
    jraTko.setDataSet(((frmUplIspl)mainClass).getDetailSet());

    jlrCradnik.setColumnName("CRADNIK");
    jlrCradnik.setDataSet(((frmUplIspl)mainClass).getDetailSet());
    jlrCradnik.setColNames(new String[] {"PREZIME"});
    jlrCradnik.setTextFields(new JTextComponent[] {jlrPrezime});
    jlrCradnik.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCradnik.setSearchMode(0);
    jlrCradnik.setRaDataSet(presPN.getRadnici());
    jlrCradnik.setNavButton(jbSelCradnik);

    jlrPrezime.setColumnName("PREZIME");
    jlrPrezime.setNavProperties(jlrCradnik);
    jlrPrezime.setSearchMode(1);

    jlrCorg.setColumnName("CORG");
    jlrCorg.setDataSet(((frmUplIspl)mainClass).getDetailSet());
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCorg.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.setNavButton(jbSelCorg);
        
    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCorg);
    jlrNaziv.setSearchMode(1);
    
    this.add(jpDetail, BorderLayout.CENTER);
  }

  public void setStavka(String cskl) {
    if (advui != null) return;
    jlrStavka.setColumnName("STAVKA");
    if (mainClass instanceof frmUplIspl)
    jlrStavka.setDataSet(((frmUplIspl)mainClass).getDetailSet());
    else
    jlrStavka.setDataSet(((frmBlagIzv)mainClass).getDetailSet());
    jlrStavka.setColNames(new String[] {"OPIS"});
    jlrStavka.setTextFields(new JTextComponent[] {jlrOpisStavke});
    jlrStavka.setRaDataSet(hr.restart.sisfun.Asql.getShkonta("blpn", cskl, "BL"));
    jlrStavka.getRaDataSet().refresh();
    jlrStavka.setVisCols(new int[] {2, 3});
    jlrStavka.setSearchMode(0);
    jlrStavka.setNavButton(jbSelStavka);

    jlrOpisStavke.setColumnName("OPIS");
    jlrOpisStavke.setNavProperties(jlrStavka);
    jlrOpisStavke.setSearchMode(1);


    jpDetail.remove(jlrStavka);
    jpDetail.remove(jbSelStavka);
    jpDetail.remove(jlrOpisStavke);


    jpDetail.add(jlrStavka, new XYConstraints(150, 120, 100, -1));
    jpDetail.add(jbSelStavka,   new XYConstraints(605, 120, 21, 21));
    jpDetail.add(jlrOpisStavke,  new XYConstraints(255, 120, 345, -1));
  }


  public void initDomesticVal() {
    jpDetail.removeAll();
    jlIzdatak.setText("Izdatak");
    jlPrimitak.setText("Primitak");

    jpDetail.add(jbSelCorg, new XYConstraints(605, 195, 21, 21));
    jpDetail.add(jbSelCradnik, new XYConstraints(605, 95, 21, 21));
    jpDetail.add(jlCorg, new XYConstraints(15, 195, -1, -1));
    jpDetail.add(jlCradnik, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jlDatum, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlIzdatak, new XYConstraints(360, 45, -1, -1));
    jpDetail.add(jlOpis, new XYConstraints(15, 170, -1, -1));
    jpDetail.add(jlPrimitak, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlStavka, new XYConstraints(15, 120, -1, -1));
    jpDetail.add(jlTko, new XYConstraints(15, 145, -1, -1));
    jpDetail.add(jlrCorg, new XYConstraints(150, 195, 100, -1));
    jpDetail.add(jlrCradnik, new XYConstraints(150, 95, 100, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(255, 195, 345, -1));
    jpDetail.add(jlrPrezime, new XYConstraints(255, 95, 345, -1));
    jpDetail.add(jraDatum, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jraIzdatak, new XYConstraints(500, 45, 100, -1));
    jpDetail.add(jraOpis, new XYConstraints(150, 170, 450, -1));
    jpDetail.add(jraPrimitak, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jlrStavka, new XYConstraints(150, 120, 100, -1));
    jpDetail.add(jbSelStavka,   new XYConstraints(605, 120, 21, 21));
    jpDetail.add(jlrOpisStavke,  new XYConstraints(255, 120, 345, -1));
    jpDetail.add(jraTko,   new XYConstraints(150, 145, 450, -1));
    setURAButton();
    setUplIsplAdvancedDetail();
  }

  public void initInoVal(String masterDetail) {
    jpDetail.removeAll();
    if (mainClass instanceof frmUplIspl){
      jlIzdatak.setText("Izdatak u " + ((frmUplIspl)mainClass).getMasterSet().getString("OZNVAL"));
      jlPrimitak.setText("Primitak u " + ((frmUplIspl)mainClass).getMasterSet().getString("OZNVAL"));
    } else if (mainClass instanceof frmBlagIzv){
      jlIzdatak.setText("Izdatak u " + ((frmBlagIzv)mainClass).getMasterSet().getString("OZNVAL"));
      jlPrimitak.setText("Primitak u " + ((frmBlagIzv)mainClass).getMasterSet().getString("OZNVAL"));
    }

    jpDetail.add(jbSelCorg, new XYConstraints(605, 195, 21, 21));
    jpDetail.add(jbSelCradnik, new XYConstraints(605, 95, 21, 21));
    jpDetail.add(jlCorg, new XYConstraints(15, 195, -1, -1));
    jpDetail.add(jlCradnik, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jlDatum, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlIzdatak, new XYConstraints(360, 45, -1, -1));
    jpDetail.add(jlOpis, new XYConstraints(15, 170, -1, -1));
    jpDetail.add(jlPrimitak, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlPvizdatak, new XYConstraints(360, 70, -1, -1));
    jpDetail.add(jlPvprimitak, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlStavka, new XYConstraints(15, 120, -1, -1));
    jpDetail.add(jlTecaj, new XYConstraints(360, 20, -1, -1));
    jpDetail.add(jlTko, new XYConstraints(15, 145, -1, -1));
    jpDetail.add(jlrCorg, new XYConstraints(150, 195, 100, -1));
    jpDetail.add(jlrCradnik, new XYConstraints(150, 95, 100, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(255, 195, 345, -1));
    jpDetail.add(jlrPrezime, new XYConstraints(255, 95, 345, -1));
    jpDetail.add(jraDatum, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jraIzdatak, new XYConstraints(500, 45, 100, -1));
    jpDetail.add(jraOpis, new XYConstraints(150, 170, 450, -1));
    jpDetail.add(jraPrimitak, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jraPvizdatak, new XYConstraints(500, 70, 100, -1));
    jpDetail.add(jraPvprimitak, new XYConstraints(150, 70, 100, -1));
    jpDetail.add(jlrStavka, new XYConstraints(150, 120, 100, -1));
    jpDetail.add(jbSelStavka,   new XYConstraints(605, 120, 21, 21));
    jpDetail.add(jlrOpisStavke,  new XYConstraints(255, 120, 345, -1));
    jpDetail.add(jraTecaj, new XYConstraints(500, 20, 100, -1));
    jpDetail.add(jraTko,   new XYConstraints(150, 145, 450, -1));
    setURAButton();
    setUplIsplAdvancedDetail();
  }

  void jraPrimitak_focusLost(FocusEvent e) {
    if(((frmUplIspl)mainClass).getDetailSet().getBigDecimal("PRIMITAK").compareTo(new java.math.BigDecimal(0)) >= 0) {
      ((frmUplIspl)mainClass).convertCurrency(((frmUplIspl)mainClass).getDetailSet().getTimestamp("DATUM"));
      if(((frmUplIspl)mainClass).getDetailSet().getBigDecimal("PRIMITAK").compareTo(new java.math.BigDecimal(0)) > 0) {
        ((frmUplIspl)mainClass).getDetailSet().setBigDecimal("IZDATAK", new java.math.BigDecimal(0));
      }
    }
  }

  void jraIzdatak_focusLost(FocusEvent e) {
    if(((frmUplIspl)mainClass).getDetailSet().getBigDecimal("IZDATAK").compareTo(new java.math.BigDecimal(0)) >= 0) {
      ((frmUplIspl)mainClass).convertCurrency(((frmUplIspl)mainClass).getDetailSet().getTimestamp("DATUM"));
      if(((frmUplIspl)mainClass).getDetailSet().getBigDecimal("IZDATAK").compareTo(new java.math.BigDecimal(0)) > 0) {
        ((frmUplIspl)mainClass).getDetailSet().setBigDecimal("PRIMITAK", new java.math.BigDecimal(0));
      }
    }
  }

  void lookUpStavke(){
    if (mainClass instanceof frmUplIspl) {
//      if(!fuj.putnal()){
//        if(jlrOpisStavke.getText().concat(" ").concat(((frmUplIspl)mainClass).ss.getIme(jlrCradnik.getText())).length() <= 50)
//          ((frmUplIspl)mainClass).getDetailSet().setString("OPIS", jlrOpisStavke.getText().concat(" ").concat(((frmUplIspl)mainClass).ss.getIme(jlrCradnik.getText())));
//        else
      if (advdetnokonto) {
        if (((frmUplIspl)mainClass).getSkStavkerad().getRowCount() > 0) {
          jlrStavka.setText("");
          jlrOpisStavke.setText("");
        } else {
          advui.kcg.getJlrBROJKONTA().getDataSet().setString("BROJKONTA", jlrStavka.getRaDataSet().getString("BROJKONTA"));
  //        advui.kcg.getJlrBROJKONTA().setText(jlrStavka.getRaDataSet().getString("BROJKONTA"));
          System.err.println("jpUplIsplDetail.lookUpStavke() :: jlrStavka.getRaDataSet() = "+jlrStavka.getRaDataSet());
          System.err.println("jpUplIsplDetail.lookUpStavke() :: advui.kcg.getJlrBROJKONTA().getDataSet() = "+advui.kcg.getJlrBROJKONTA().getDataSet());
          advui.kcg.aft_lookUpKonto(true);
          advui.kcg.afterAfterLookupKonto();
        }
      }
      if (((frmUplIspl)mainClass).getDetailSet().getString("OPIS").equals("")){
        ((frmUplIspl)mainClass).getDetailSet().setString("OPIS", jlrOpisStavke.getText());
        if (!jlrStavka.getText().equals("")) {
          ((frmUplIspl)mainClass).handleCORG();
        }
      }
//      }
    }
/*    if (mainClass.getDetailSet().getString("BROJKONTA").equals("")) {
      mainClass.getDetailSet().setString("BROJKONTA",jlrStavka.getRaDataSet().getString("BROJKONTA"));
      if (advui != null) advui.kcg.getJlrBROJKONTA().after_lookUp();
    }*/
  }

  private void setUplIsplAdvancedDetail() {
    if (advdetail||advdetnokonto) {
      advui = new UplIsplAdvancedDetail(mainClass.raDetail);
      jpDetail.remove(jbSelCorg);
      jpDetail.remove(jlCorg);
      jpDetail.remove(jlrCorg);
      jpDetail.remove(jlrNaziv);
      if (!advdetnokonto) {
        jpDetail.remove(jlStavka);
        jpDetail.remove(jlrStavka);
        jpDetail.remove(jbSelStavka);
        jpDetail.remove(jlrOpisStavke);
      } else {
        advui.kcg.getJlrBROJKONTA().setVisible(false);
        advui.kcg.getJbGetKonto().setVisible(false);
        advui.kcg.getJlrNAZIVKONTA().setVisible(false);
        advui.kontoLabel.setVisible(false);
      }
      jpDetail.add(advui, new XYConstraints(0,195,-1,-1));
    }
  }
  public void initAdvancedDetail(char mode) {
    if (advui == null) return;
    if (mode == 'I') {
      advui.kcg.setScrKonto(mainClass.getDetailSet().getString("BROJKONTA"));
      boolean isSK;
      try { isSK = raKonta.isSaldak(mainClass.getDetailSet().getString("BROJKONTA"));
      } catch (Exception e) { isSK = false; }
      advui.tsk.enable(isSK, mainClass.raDetail);
    } else if (mode == 'N') {
      advui.kcg.clrCORG();
      advui.kcg.clrBROJKONTA();
      advui.kcg.setEnabledKonto(true);
      advui.tsk.enable(false, mainClass.raDetail);
      //setDates();
    } else {
      advui.tsk.enable(false, mainClass.raDetail);
    }
    
  }
  public void setDates() {
    if (mainClass.getDetailSet().isNull("DATUM")) return;
    if (mainClass.getDetailSet().isNull("DATDOK"))
      mainClass.getDetailSet().setTimestamp("DATDOK", mainClass.getDetailSet().getTimestamp("DATUM"));
    if (mainClass.getDetailSet().isNull("DATDOSP"))
      mainClass.getDetailSet().setTimestamp("DATDOSP", mainClass.getDetailSet().getTimestamp("DATUM"));
  }
}