/****license*****************************************************************
**   file: frmLogos.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Logodat;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raTransaction;
import hr.restart.util.reports.raSectionDesigner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class frmLogos extends raMatPodaci {
  static frmLogos flogos;
  JPanel jp = new JPanel();
  raCommonClass rcc = new raCommonClass();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlCORG = new JLabel();
  JlrNavField jtCORG = new JlrNavField() {
    public void after_lookUp() {
      jtCORG_after_lookUp();
      jcbAKTIV.setNextFocusableComponent(jtMATBROJ);
    }
  };
  dM dm;
  Valid vl;
  JLabel jlZIRO = new JLabel();
  JlrNavField jlrZIRO = new JlrNavField();
  JraButton jbZIRO = new JraButton();
  JLabel jlNAZIVLOG = new JLabel();
  JraTextField jtNAZIVLOG = new JraTextField();
  JLabel jlADRESA = new JLabel();
  JraTextField jtADRESA = new JraTextField();
  JLabel jlPTMJESTO = new JLabel();
  JlrNavField jtPBR = new JlrNavField();
  JlrNavField jtMJESTO = new JlrNavField();
  JraButton jbSelMj = new JraButton();
  JLabel jlMATBROJ = new JLabel();
  JraTextField jtMATBROJ = new JraTextField();
  JLabel jlSIFDJEL = new JLabel();
  JraTextField jtSIFDJEL = new JraTextField();
  JLabel jlPORISP = new JLabel();
  JraTextField jtPORISP = new JraTextField();
  JLabel jlOIB = new JLabel("OIB/GLN");
  JraTextField jtOIB = new JraTextField();
  JraTextField jtGLN = new JraTextField();
  JLabel jlTELEFONI = new JLabel();
  JraTextField jtTEL1 = new JraTextField();
  JraTextField jtTEL2 = new JraTextField();
  JraTextField jtFAX = new JraTextField();
  JraButton jbGetKnjig = new JraButton();
  JraCheckBox jcbAKTIV = new JraCheckBox();
  JraTextField jtEMAIL = new JraTextField();
  JraTextField jtURL = new JraTextField();
//  JLabel jLabel1 = new JLabel();
//  JraTextField jtfFOOTER1 = new JraTextField();
//  JraTextField jtfFOOTER2 = new JraTextField();
//  JLabel jlMARGIN = new JLabel();
//  JraTextField jtfTOPMARGIN = new JraTextField();
//  JLabel jlTOPMARGIN = new JLabel();
//  JLabel jlBOTTOMMARGIN = new JLabel();
//  JraTextField jtfBOTTOMMARGIN = new JraTextField();
//  char currMode;

  JLabel jlIzdok = new JLabel();
  JraButton jbHeader = new JraButton();
  JraButton jbFooter = new JraButton();
  JraButton jbVrdoks = new JraButton();

  QueryDataSet header, footer;
  String fontRep = null;

  public frmLogos() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {

    dm = hr.restart.baza.dM.getDataModule();
    vl = hr.restart.util.Valid.getValid();

    this.setRaQueryDataSet(dm.getAllLogotipovi());
    this.setVisibleCols(new int[] {0,1});
    this.setRaDetailPanel(jp);

    header = Logodat.getDataModule().getFilteredDataSet("1=0");
    footer = Logodat.getDataModule().getFilteredDataSet("1=0");

    jlCORG.setText("Org. jedinica");
    jtCORG.setDataSet(getRaQueryDataSet());
    jtCORG.setColumnName("CORG");
    jtCORG.setRaDataSet(dm.getOrgstruktura());
    jtCORG.setVisCols(new int[] {0,1});
    jtCORG.setSearchMode(0);
    jtCORG.setNavButton(jbGetKnjig);
//    jtCORG.setNextFocusableComponent(jtMATBROJ);
    jlZIRO.setText("éiro ra\u010Dun");
    jlrZIRO.setColumnName("ZIRO");
    jlrZIRO.setDataSet(getRaQueryDataSet());
    jlrZIRO.setVisCols(new int[] {0,1});
    jlrZIRO.setRaDataSet(dm.getZirorn());
    jlrZIRO.getRaDataSet().open();
    jlrZIRO.setSearchMode(0);
    jlrZIRO.setNavButton(jbZIRO);

//    OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
//      public void knjigChanged(String oldk, String newk) {
//        jtCORG.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
//        jlrZIRO.setRaDataSet(OrgStr.getOrgStr().getKnjigziro(newk));
//        jlrZIRO.getRaDataSet().open();
//      }
//    });

    jp.setLayout(xYLayout1);
    jlNAZIVLOG.setText("Naziv");
    jtNAZIVLOG.setColumnName("NAZIVLOG");
    jtNAZIVLOG.setDataSet(getRaQueryDataSet());
    xYLayout1.setWidth(485);
    xYLayout1.setHeight(315);
    jlADRESA.setText("Adresa");
    jtADRESA.setDataSet(getRaQueryDataSet());
    jtADRESA.setColumnName("ADRESA");
    jlPTMJESTO.setText("PT broj i mjesto");
    jtPBR.setColumnName("PBR");
    jtPBR.setDataSet(getRaQueryDataSet());
    jtPBR.setRaDataSet(dm.getMjesta());
    jtPBR.setTextFields(new JTextComponent[] {jtMJESTO});
    jtPBR.setColNames(new String[] {"NAZMJESTA"});
    jtPBR.setVisCols(new int[] {2,1});
    jtPBR.setSearchMode(3);
    jtPBR.setFocusLostOnShow(false);
    jtPBR.setNavButton(jbSelMj);
    jtMJESTO.setNavProperties(jtPBR);
    jtMJESTO.setDataSet(getRaQueryDataSet());
    jtMJESTO.setColumnName("MJESTO");
    jtMJESTO.setFocusLostOnShow(false);
    jtMJESTO.setSearchMode(1);
    jlMATBROJ.setText("Mati\u010Dni broj / e-mail");
    jtMATBROJ.setDataSet(getRaQueryDataSet());
    jtMATBROJ.setColumnName("MATBROJ");
    jlSIFDJEL.setText("äifra djelatnosti / URL");
    jtSIFDJEL.setColumnName("SIFDJEL");
    jtSIFDJEL.setDataSet(getRaQueryDataSet());
    jlPORISP.setText("Porezna ispostava");
    jtPORISP.setColumnName("PORISP");
    jtPORISP.setDataSet(getRaQueryDataSet());
    jtOIB.setColumnName("OIB");
    jtOIB.setDataSet(getRaQueryDataSet());
    jtGLN.setColumnName("GLN");
    jtGLN.setDataSet(getRaQueryDataSet());
    jlTELEFONI.setText("Telefoni / fax");
    jtTEL1.setDataSet(getRaQueryDataSet());
    jtTEL1.setColumnName("TEL1");
    jtTEL2.setColumnName("TEL2");
    jtTEL2.setDataSet(getRaQueryDataSet());
    jtFAX.setDataSet(getRaQueryDataSet());
    jtFAX.setColumnName("FAX");
    jcbAKTIV.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAKTIV.setHorizontalTextPosition(SwingConstants.LEFT);
    jcbAKTIV.setText("Aktivan");
    jcbAKTIV.setColumnName("AKTIV");
    jcbAKTIV.setDataSet(getRaQueryDataSet());
    jcbAKTIV.setSelectedDataValue("D");
    jcbAKTIV.setUnselectedDataValue("N");
    jtEMAIL.setColumnName("EMAIL");
    jtEMAIL.setDataSet(getRaQueryDataSet());
    jtURL.setColumnName("URL");
    jtURL.setDataSet(getRaQueryDataSet());
//    jLabel1.setText("Footer");
//    jtfFOOTER1.setColumnName("FOOTER1");
//    jtfFOOTER1.setDataSet(getRaQueryDataSet());
//    jtfFOOTER2.setColumnName("FOOTER2");
//    jtfFOOTER2.setDataSet(getRaQueryDataSet());
//    jlMARGIN.setText("Margine");
//    jtfTOPMARGIN.setDataSet(getRaQueryDataSet());
//    jtfTOPMARGIN.setColumnName("TOPMARGIN");
//    jlTOPMARGIN.setText("Gornja");
//    jlBOTTOMMARGIN.setText("Donja");
//    jtfBOTTOMMARGIN.setColumnName("BOTTOMMARGIN");
//    jtfBOTTOMMARGIN.setDataSet(getRaQueryDataSet());
//    jtfBOTTOMMARGIN.setNextFocusableComponent(this.jcbAKTIV);

    jlIzdok.setText("Izlazni dokumenti");
    jbHeader.setText("Logo zaglavlje");
    jbHeader.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        header.open();
        if (header.rowCount() == 0) createDefaultHeader();
        raSectionDesigner.setKey(getRaQueryDataSet().getString("CORG"), "H", "PH", "I");
        raSectionDesigner.show(getWindow(), "Zaglavlje logotipa izlaznih dokumenata", header);
      }
    });

    jbFooter.setText("Logo podnoûje");
    jbFooter.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        footer.open();
        if (footer.rowCount() == 0) createDefaultFooter();
        raSectionDesigner.setKey(getRaQueryDataSet().getString("CORG"), "F", "PF", "I");
        raSectionDesigner.show(getWindow(), "Podnoûje logotipa izlaznih dokumenata", footer);
      }
    });

    jbVrdoks.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        alterIzlazVrdoks();
      }
    });

    jbVrdoks.setText("...");

    jp.add(jlCORG, new XYConstraints(15, 20, -1, -1));
    jp.add(jtCORG, new XYConstraints(150, 20, 100, -1));
    jp.add(jlZIRO, new XYConstraints(15, 45, -1, -1));
    jp.add(jlrZIRO, new XYConstraints(150, 45, 300, -1));
    jp.add(jbZIRO, new XYConstraints(455, 45, 21, 21));

    jp.add(jlNAZIVLOG, new XYConstraints(15, 70, -1, -1));
    jp.add(jtNAZIVLOG, new XYConstraints(150, 70, 300, -1));
    jp.add(jlADRESA, new XYConstraints(15, 95, -1, -1));
    jp.add(jtADRESA, new XYConstraints(150, 95, 300, -1));
    jp.add(jlPTMJESTO, new XYConstraints(15, 120, -1, -1));
    jp.add(jtPBR, new XYConstraints(150, 120, 95, -1));
    jp.add(jtMJESTO, new XYConstraints(250, 120, 200, -1));
    jp.add(jbSelMj, new XYConstraints(455, 120, 21, 21));
    
    jp.add(jlMATBROJ, new XYConstraints(15, 145, -1, -1));
    jp.add(jtMATBROJ, new XYConstraints(150, 145, 95, -1));
    jp.add(jlSIFDJEL, new XYConstraints(15, 170, -1, -1));
    jp.add(jtSIFDJEL, new XYConstraints(150, 170, 95, -1));
    jp.add(jlPORISP, new XYConstraints(15, 195, -1, -1));
    jp.add(jtPORISP, new XYConstraints(150, 195, 300, -1));
    jp.add(jlTELEFONI, new XYConstraints(15, 220, -1, -1));
    jp.add(jtTEL1, new XYConstraints(150, 220, 95, -1));
    jp.add(jtTEL2, new XYConstraints(250, 220, 95, -1));
    jp.add(jtFAX, new XYConstraints(350, 220, 100, -1));
    jp.add(jbGetKnjig, new XYConstraints(255, 20, 21, 21));
    jp.add(jcbAKTIV, new XYConstraints(350, 20, 100, 21));
    jp.add(jtEMAIL, new XYConstraints(250, 145, 200, -1));
    jp.add(jtURL, new XYConstraints(250, 170, 200, -1));
    jp.add(jlOIB, new XYConstraints(15, 245, -1, -1));
    jp.add(jtOIB, new XYConstraints(150, 245, 145, -1));
    jp.add(jtGLN, new XYConstraints(300, 245, 150, -1));
    jp.add(jlIzdok, new XYConstraints(15, 275, -1, -1));
    jp.add(jbHeader, new XYConstraints(150, 275, 150, 21));
    jp.add(jbFooter, new XYConstraints(305, 275, 145, 21));
    jp.add(jbVrdoks, new XYConstraints(455, 275, 21, 21));
//    jp.add(jLabel1,    new XYConstraints(15, 230, -1, -1));
//    jp.add(jtfFOOTER1,    new XYConstraints(150, 230, 300, -1));
//    jp.add(jtfFOOTER2,   new XYConstraints(150, 255, 300, -1));
//    jp.add(jlMARGIN,      new XYConstraints(16, 280, -1, -1));
//    jp.add(jtfTOPMARGIN,          new XYConstraints(220, 280, 40, -1));
//    jp.add(jlTOPMARGIN,  new XYConstraints(150, 280, -1, -1));
//    jp.add(jtfBOTTOMMARGIN,   new XYConstraints(410, 280, 40, -1));
//    jp.add(jlBOTTOMMARGIN,  new XYConstraints(340, 280, -1, -1));
  }
  
  public void beforeShow() {
    fontRep = frmParam.getParam("sisfun", "globalFont", "", 
        "Ime fonta koji zamjenjuje Lucida Bright");
  }

  private void setSectionFilters() {
    Logodat.getDataModule().setFilter(header,
        Condition.equal("CORG", getRaQueryDataSet().getString("CORG")).and(
        Condition.equal("VRSTA", "H")));
      Logodat.getDataModule().setFilter(footer,
        Condition.equal("CORG", getRaQueryDataSet().getString("CORG")).and(
        Condition.equal("VRSTA", "F")));
  }

  public void EntryPoint(char mode) {
    if (mode == 'N') {
      rcc.setLabelLaF(jtCORG, true);
      rcc.setLabelLaF(jbGetKnjig, true);
      rcc.setLabelLaF(jbHeader, false);
      rcc.setLabelLaF(jbFooter, false);
    } else {
      rcc.setLabelLaF(jtCORG, false);
      rcc.setLabelLaF(jbGetKnjig, false);
      rcc.setLabelLaF(jbHeader, true);
      rcc.setLabelLaF(jbFooter, true);
      setSectionFilters();
    }
  }

  public void SetFokus(char mode) {
    if (mode == 'N') {

      //      jcbAKTIV.setNextFocusableComponent(this.jtNAZIVLOG);
//      jtCORG.setEnabled(true);
//      jbGetKnjig.setEnabled(true);
      jtCORG.requestFocus();
    } else if (mode == 'I') {
//      jcbAKTIV.setNextFocusableComponent(this.jtMATBROJ);

//      jtCORG.setEnabled(false);
//      jbGetKnjig.setEnabled(false);
      jtNAZIVLOG.requestFocus();

    }
  }
  public boolean Validacija(char mode) {
    if (mode == 'N') {
      if (vl.notUnique(jtCORG)) return false;
    }
    if (vl.isEmpty(jtNAZIVLOG)) return false;
    if (vl.isEmpty(jtMATBROJ)) return false;
    return true;
  }

  public boolean doWithSave(char mode) {
    try {
      if (mode == 'B') {
//        setSectionFilters();
        header.open();
        footer.open();
        header.deleteAllRows();
        footer.deleteAllRows();
        raTransaction.saveChanges(header);
        raTransaction.saveChanges(footer);
      }
      if (mode == 'I') {
        if (header.isOpen()) raTransaction.saveChanges(header);
        if (footer.isOpen()) raTransaction.saveChanges(footer);
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean DeleteCheck(){
    setSectionFilters();
    return true;
  }

  public void AfterSave(char mode) {
    dm.getLogodat().refresh();
  }

  public static frmLogos getFrmLogos() {
    if (flogos==null) flogos = new frmLogos();
    return flogos;
  }

  public void jtCORG_after_lookUp() {
    if (jtCORG.getText().length() == 0) {
      jlrZIRO.setRaDataSet(dm.getZirorn());
    } else {
      jlrZIRO.setRaDataSet(OrgStr.getOrgStr().getKnjigziro(
          OrgStr.getOrgStr().getPripKnjig(jtCORG.getText())));
    }
    jlrZIRO.getRaDataSet().open();
    jlrZIRO.forceFocLost();
    if (!jtNAZIVLOG.getText().equals("")) return;
    if (lookupData.getlookupData().raLocate(
                                    dm.getOrgstruktura(),
                                    new String[] {"CORG"},
                                    new String[] {jtCORG.getText()}
                                  )) {
      if (jtNAZIVLOG.getText().equals("")) {
        getRaQueryDataSet().setString("NAZIVLOG",dm.getOrgstruktura().getString("NAZIV"));
      }
      if (jtMJESTO.getText().equals("")) {
        getRaQueryDataSet().setString("MJESTO",dm.getOrgstruktura().getString("MJESTO"));
      }
      if (jtPBR.getText().equals("")) {
        try {
          getRaQueryDataSet().setInt("PBR",Integer.parseInt(dm.getOrgstruktura().getString("HPBROJ")));
        } catch (Exception e) {
          getRaQueryDataSet().setInt("PBR",0);
        }
      }
      if (jlrZIRO.getText().equals("")) {
        if (jlrZIRO.getRaDataSet().rowCount() > 0) {
          jlrZIRO.getRaDataSet().first();
          jlrZIRO.setText(jlrZIRO.getRaDataSet().getString("ZIRO"));
        }
      }
      if (jtADRESA.getText().equals("")) {
        getRaQueryDataSet().setString("ADRESA",dm.getOrgstruktura().getString("ADRESA"));
      }
    }
  }

  private int rbr;

  void fillCommon(DataSet ds, int x, int y, int w, int h) {
    String vrsta = ds.getString("VRSTA"), vrsec = ds.getString("VRSEC");
    ds.insertRow(false);
    ds.setShort("RBR", (short) ++rbr);
    ds.setString("CORG", getRaQueryDataSet().getString("CORG"));
    ds.setString("VRSTA", vrsta);
    ds.setString("VRSEC", vrsec);
    ds.setString("VRDOK", "I");
    ds.setInt("HPOS", x);
    ds.setInt("VPOS", y);
    ds.setInt("SIRINA", w);
    ds.setInt("VISINA", h);
  }

  void fillText(DataSet ds, String text, int x, int y, int w, int h, String font, String align) {
    fillCommon(ds, x, y, w, h);
    
    if (fontRep != null && fontRep.length() > 0) 
      font = new VarStr(font).replace("Lucida Bright", fontRep).toString();
    
    ds.setString("TEKST", text);
    ds.setString("TIP", "T");
    ds.setString("FONT", font);
    ds.setString("ALIGN", align);
  }

  void fillLine(DataSet ds, int x, int y, int w, int h) {
    fillCommon(ds, x, y, w, h);
    ds.setString("TIP", w > h ? "H" : "V");
  }

  void createDefaultHeader() {
    DataSet ds = this.getRaQueryDataSet(), h = header;
    h.insertRow(false);
    h.setShort("RBR", (short) (rbr = 0));
    h.setInt("SIRINA", 0);
    h.setInt("VISINA", 93);
    h.setString("CORG", ds.getString("CORG"));
    h.setString("VRSTA", "H");
    h.setString("VRSEC", "PH");
    h.setString("VRDOK", "I");
    fillText(h, ds.getString("NAZIVLOG"), 1, 1, 520, 21, "Lucida Bright;18;B;N", "L");
    fillText(h,getAdrPb(ds.getString("ADRESA"),ds.getInt("PBR"),ds.getString("MJESTO")),
             2, 20, 520, 14, "Lucida Bright;11", "L");
    /*ds.getString("ADRESA") + ", " + ds.getInt("PBR") + " " + ds.getString("MJESTO")*/

    fillText(h, getTelFax(ds.getString("TEL1"),ds.getString("TEL2"),ds.getString("FAX")),
             2,34,322,14,"Lucida Bright;10","L");
    fillText(h, "\néiro ra\u010Dun ", 2, 36, 63, 24, "Lucida Bright;10", "L");
    fillText(h, ds.getString("ZIRO"), 65, 47, 140, 14, "Lucida Bright;10;B;N", "L");
    fillText(h, ", MB ", 191, 47, 27, 14, "Lucida Bright;10", "L");
    fillText(h, ds.getString("MATBROJ"), 217, 47, 169, 14, "Lucida Bright;10", "L");


    /*
    fillText(h, "Tel :", 2, 34, 28, 14, "Lucida Bright;10", "L");
    fillText(h, "Fax :", 322, 34, 31, 14, "Lucida Bright;10", "L");
    fillText(h, ds.getString("TEL1") + "   " + ds.getString("TEL2"),
             49, 34, 251, 14, "Lucida Bright;10", "L");
    fillText(h, ds.getString("FAX"), 356, 34, 144, 14, "Lucida Bright;10", "L");
    fillText(h, "\néiro ra\u010Dun :", 2, 36, 71, 24, "Lucida Bright;10", "L");
    fillText(h, "MB :", 322, 47, 31, 14, "Lucida Bright;10", "L");
    fillText(h, ds.getString("ZIRO"), 75, 47, 225, 14, "Lucida Bright;10", "L");
    fillText(h, ds.getString("MATBROJ"), 356, 47, 144, 14, "Lucida Bright;10", "L");
    fillLine(h, 0, 61, 537, 1);
    */
    h.post();
  }

  private String getAdrPb(String ad, int pbr, String mj){
    String adpb = "";
    if (!ad.trim().equals("")){
      adpb = ad+", ";
    }
    if (pbr != 0){
      adpb = adpb+pbr+" "+mj;
    } else {
      adpb = adpb+" "+mj;
    }
    return adpb;
  }

  private String getTelFax(String t1, String t2, String fx){
    String telNfax = "";
    boolean imaTelefon = false;
    if (!t1.equals("") || !t2.equals("")) {
      telNfax = "Tel ";
      imaTelefon = true;
      if (!t1.equals("")) telNfax = telNfax+t1;
      if (!t2.equals("")){
        if (!t1.equals("")){
          telNfax = telNfax+", "+t2;
        } else {
          telNfax = telNfax+t2;
        }
      }
    }
    if (!fx.equals("")) {
      if (imaTelefon){
        telNfax = telNfax+", Fax "+fx;
      } else {
        telNfax = "Fax "+fx;
      }
    }
    return telNfax;
  }

  void createDefaultFooter() {
    DataSet ds = this.getRaQueryDataSet(), f = footer;
    f.insertRow(false);
    f.setShort("RBR", (short) (rbr = 0));
    f.setInt("SIRINA", 0);
    f.setInt("VISINA", 30);
    f.setString("CORG", ds.getString("CORG"));
    f.setString("VRSTA", "F");
    f.setString("VRSEC", "PF");
    f.setString("VRDOK", "I");
    fillLine(f, 0, 5, 537, 1);
    fillText(f, "Stranica $PAGE of $PAGES", 406, 10, 131, 12, "Lucida Bright;8", "R");
    f.post();
  }

  void alterIzlazVrdoks() {
    dlgLogoVrdok.show(this.getWindow());
  }
}
