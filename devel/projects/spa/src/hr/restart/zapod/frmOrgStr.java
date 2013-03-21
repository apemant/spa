/****license*****************************************************************
**   file: frmOrgStr.java
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
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraKeyListener;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raButtonGroup;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;



/**

 * Title:        Robno poslovanje

 * Description:

 * Copyright:    Copyright (c) 2000

 * Company:      REST-ART

 * @author REST-ART development team

 * @version 1.0

 */



public class frmOrgStr extends raMatPodaci {

  static frmOrgStr forgstr;

  Valid vl = Valid.getValid();

//  raCommonClass rcc = new raCommonClass();

  ResourceBundle zpRes = ResourceBundle.getBundle(hr.restart.zapod.frmZapod.RESBUNDLENAME);

  OrgStr ORS = OrgStr.getOrgStr();

  raCommonClass rCC = raCommonClass.getraCommonClass();

  String currentKNJIG=null;

  char orgmode;

  JPanel jp = new JPanel();

//  JraTextField jdbZIRO = new JraTextField();

  JlrNavField jdbZIRO = new JlrNavField();

  JlrNavField jdbHPBROJ = new JlrNavField();

  JlrNavField jdbMJESTO = new JlrNavField();
  JraButton jbSelMj = new JraButton();

  JraTextField jdbADRESA = new JraTextField();

  JraTextField jtfNAZIV = new JraTextField();

  JraTextField jtfCORG = new JraTextField();

  JLabel jLZiroOrg = new JLabel();

  JLabel jLPttBrojOrg = new JLabel();

  JLabel jLAdresaOrg = new JLabel();

  JLabel jLMjestoOrg = new JLabel();

  JLabel jLNazOrg = new JLabel();

  JLabel jLCorg = new JLabel();

  JraCheckBox jdbCBnalog = new JraCheckBox();

  XYLayout xYLayout4 = new XYLayout();

  XYLayout xYLayout3 = new XYLayout();

  dM dm;

  JLabel jLCorgPrip = new JLabel();

  JlrNavField jlrNFPRIPADNOST = new JlrNavField() {

    public void after_lookUp() {

      pripadnostAfter_lookUp();

    }

  };

  JraButton jBgetPrip = new JraButton();

  JlrNavField jlrNFNazivPrip = new JlrNavField(){

    public void after_lookUp() {

      pripadnostAfter_lookUp();

    }

  };

  JLabel jlPRIPADNOST = new JLabel();

  JLabel jlPRIPNAZIV = new JLabel();

  JraButton jBgetZiro = new JraButton();

  JLabel jlOJ = new JLabel();

  JLabel jladresa = new JLabel();
  
  
  raComboBox rcbFisk = new raComboBox() {
    public void this_itemStateChanged() {
      super.this_itemStateChanged();
      fiskChanged();
    }
  };

  JraTextField jraFPP = new JraTextField();
  raComboBox rcbFNU = new raComboBox();

  JraTextField jraCert = new JraTextField();
  JraTextField jraStore = new JraTextField();
  JraTextField jraPass = new JraTextField();
  
  JraCheckBox jcbPDV = new JraCheckBox();


  public frmOrgStr() {

    try {

      jbInit();

      forgstr = this;

    }

    catch(Exception e) {

      e.printStackTrace();

    }

  }

  private void jbInit() throws Exception {

    dm = dM.getDataModule();

    jp.setLayout(xYLayout3);



    this.setRaQueryDataSet(dm.getAllOrgstruktura());

    this.setVisibleCols(new int[] {0,1});





    jdbZIRO.setColumnName("ZIRO");

    jdbZIRO.setDataSet(dm.getAllOrgstruktura());

    jdbZIRO.setFocusLostOnShow(false);

    jdbZIRO.setColNames(new String[] {"ZIRO"});

    jdbZIRO.setTextFields(new javax.swing.text.JTextComponent[] {jdbZIRO});

    jdbZIRO.setVisCols(new int[] {1});

    jdbZIRO.setHandleError(false);

    jdbZIRO.setSearchMode(1);

    jdbZIRO.setNavButton(jBgetZiro);

    jdbHPBROJ.setColumnName("HPBROJ");
    jdbHPBROJ.setNavColumnName("PBR");
    jdbHPBROJ.setDataSet(getRaQueryDataSet());
    jdbHPBROJ.setRaDataSet(dm.getMjesta());
    jdbHPBROJ.setTextFields(new JTextComponent[] {jdbMJESTO});
    jdbHPBROJ.setColNames(new String[] {"NAZMJESTA"});
    jdbHPBROJ.setVisCols(new int[] {1,2});
    jdbHPBROJ.setFocusLostOnShow(false);
    jdbHPBROJ.setSearchMode(3);
    jdbHPBROJ.setNavButton(jbSelMj);
    
    jdbMJESTO.setColumnName("MJESTO");
    jdbMJESTO.setNavProperties(jdbHPBROJ);
    jdbMJESTO.setNavColumnName("NAZMJESTA");
    jdbMJESTO.setDataSet(getRaQueryDataSet());
    jdbMJESTO.setSearchMode(1);
    jdbMJESTO.setFocusLostOnShow(false);

    jdbADRESA.setColumnName("ADRESA");

    jdbADRESA.setDataSet(getRaQueryDataSet());

    jtfNAZIV.setColumnName("NAZIV");

    jtfNAZIV.setDataSet(getRaQueryDataSet());

    jtfCORG.setColumnName("CORG");

    jtfCORG.setDataSet(getRaQueryDataSet());

    jLZiroOrg.setText(zpRes.getString("jLZiroOrg_text"));

    jLPttBrojOrg.setText(zpRes.getString("jLPttBrojOrg_text"));

    jLAdresaOrg.setText(zpRes.getString("jLAdresaOrg_text"));

    jLMjestoOrg.setText(zpRes.getString("jLMjestoOrg_text"));

    jLNazOrg.setText(zpRes.getString("jLNazOrg_text"));

    jLCorg.setText(zpRes.getString("jLCorg_text"));

    jdbCBnalog.setText(zpRes.getString("jdbCBnalog_text"));

    jdbCBnalog.setColumnName("NALOG");

    jdbCBnalog.setDataSet(getRaQueryDataSet());

    jdbCBnalog.setSelectedDataValue("1");

    jdbCBnalog.setUnselectedDataValue("0");

    jdbCBnalog.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {

        jdbCBnalog_actionPerformed(e);

      }

    });



    xYLayout3.setWidth(580);

    xYLayout3.setHeight(331);
    
    
    rcbFisk.setRaColumn("FISK");
    rcbFisk.setRaDataSet(getRaQueryDataSet());
    rcbFisk.setRaItems(new String[][] {
        {"Nedefinirano / naslijeðeno s više razine","X"},
        {"Iskljuèeno na ovoj jedinici","N"},
        {"Ukljuèeno kao poslovni prostor","D"}
      });
    
    jraFPP.setDataSet(getRaQueryDataSet());
    jraFPP.setColumnName("FPP");
    
    rcbFNU.setRaColumn("FPOJED");
    rcbFNU.setRaDataSet(getRaQueryDataSet());
    rcbFNU.setRaItems(new String[][] {
        {"Brojaè po poslovnom prostoru","D"},
        {"Brojaè po naplatnom ureðaju","N"},
        {"Razdvojeni gotovinski raèuni","G"}
      });
    
    jraCert.setDataSet(getRaQueryDataSet());
    jraCert.setColumnName("CCERT");
    jraStore.setDataSet(getRaQueryDataSet());
    jraStore.setColumnName("FPATH");
    jraPass.setDataSet(getRaQueryDataSet());
    jraPass.setColumnName("FKEY");
    
    jcbPDV.setSelectedDataValue("D");
    jcbPDV.setUnselectedDataValue("N");
    jcbPDV.setDataSet(getRaQueryDataSet());
    jcbPDV.setColumnName("FPDV");
    jcbPDV.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbPDV.setHorizontalAlignment(SwingConstants.TRAILING);
    jcbPDV.setText(" Obveznik je u sustavu PDV-a ");


    jLCorgPrip.setText(zpRes.getString("jLCorgPrip_text"));



    jlrNFPRIPADNOST.setColumnName("CORG");

    jlrNFPRIPADNOST.setRaDataSet(ORS.getOrgstr2());

    jlrNFPRIPADNOST.setColNames(new String[] {"NAZIV"});

    jlrNFPRIPADNOST.setVisCols(new int[] {0,1});

    jlrNFPRIPADNOST.setTextFields(new javax.swing.text.JTextComponent[] {jlrNFNazivPrip});

    jlrNFPRIPADNOST.setSearchMode(0);

    jlrNFPRIPADNOST.setAfterLookUpOnClear(false);

    jlrNFPRIPADNOST.setNavButton(jBgetPrip);

    jlrNFNazivPrip.setColumnName("NAZIV");

    jlrNFNazivPrip.setSearchMode(1);

    jlrNFNazivPrip.setNavProperties(jlrNFPRIPADNOST);


    jlPRIPADNOST.setText("Oznaka");

    jlPRIPNAZIV.setText("Naziv");

    jlOJ.setText("Org. jedinica");

    jladresa.setText("Adresa");

    jp.add(jtfCORG, new XYConstraints(150, 20, 100, -1));

    jp.add(jtfNAZIV, new XYConstraints(255, 20, 285, -1));

    jp.add(jdbADRESA, new XYConstraints(150, 60, 180, -1));

    jp.add(jLCorg, new XYConstraints(150, 5, -1, -1));

    jp.add(jLNazOrg, new XYConstraints(255, 5, -1, -1));

    jp.add(jlOJ, new XYConstraints(15, 20, -1, -1));

    jp.add(jLAdresaOrg, new XYConstraints(150, 45, -1, -1));

    jp.add(jdbHPBROJ, new XYConstraints(335, 60, 60, -1));

    jp.add(jdbMJESTO, new XYConstraints(400, 60, 140, -1));
    jp.add(jbSelMj, new XYConstraints(545, 60, 21, 21));

    jp.add(jLPttBrojOrg, new XYConstraints(335, 45, -1, -1));

    jp.add(jLMjestoOrg, new XYConstraints(400, 45, -1, -1));

    jp.add(jladresa, new XYConstraints(15, 60, -1, -1));

    jp.add(jdbCBnalog,     new XYConstraints(15, 90, -1, 21));

    jp.add(jBgetPrip, new XYConstraints(545, 130, 21, 21));

    jp.add(jlPRIPADNOST, new XYConstraints(150, 115, -1, -1));

    jp.add(jlPRIPNAZIV, new XYConstraints(255, 115, -1, -1));

    jp.add(jLCorgPrip, new XYConstraints(15, 130, -1, -1));

    jp.add(jlrNFPRIPADNOST, new XYConstraints(150, 130, 100, -1));

    jp.add(jlrNFNazivPrip, new XYConstraints(255, 130, 285, -1));

    jp.add(jBgetZiro, new XYConstraints(545, 155, 21, 21));

    jp.add(jdbZIRO, new XYConstraints(150, 155, 390, -1));

    jp.add(jLZiroOrg, new XYConstraints(15, 155, -1, -1));
    
    jp.add(new JLabel("Fiskalizacija"), new XYConstraints(15, 200, -1, -1));
    jp.add(rcbFisk, new XYConstraints(150, 200, 300, -1));
    jp.add(new JLabel("Poslovni prostor"), new XYConstraints(15, 230, -1, -1));
    jp.add(jraFPP, new XYConstraints(150, 230, 150, -1));
    jp.add(rcbFNU, new XYConstraints(310, 230, 230, -1));
    
    jp.add(new JLabel("Šifra certifikata"), new XYConstraints(15, 260, -1, -1));
    jp.add(jraCert, new XYConstraints(150, 260, 150, -1));
    jp.add(new JLabel("Keystore datoteka"), new XYConstraints(15, 290, -1, -1));
    jp.add(jraStore, new XYConstraints(150, 290, 150, -1));
    
    jp.add(new JLabel("Šifra"), new XYConstraints(330, 290, -1, -1));
    jp.add(jraPass, new XYConstraints(390, 290, 150, -1));
    
    jp.add(jcbPDV, new XYConstraints(310, 260, 230, -1));
    

    this.setRaDetailPanel(jp);

    hr.restart.sisfun.raDataIntegrity.installFor(this);

  }



  public static frmOrgStr getFrmOrgStr() {

    if (forgstr==null) forgstr = new frmOrgStr();



    return forgstr;

  }



  public boolean Validacija(char mode) {

    if (mode=='N') {

      if (vl.notUnique(jtfCORG)) return false;

    }

    if (vl.isEmpty(jtfNAZIV)) return false;

    if (isKnjigovodstvo()) {

      if (vl.isEmpty(jdbZIRO)) return false;

    }

    if (vl.isEmpty(jlrNFPRIPADNOST)) return false;

   
    if (rcbFisk.getSelectedIndex() == 2) {
      boolean cert = getRaQueryDataSet().getString("CCERT").trim().length() > 0;
      boolean store = getRaQueryDataSet().getString("FPATH").trim().length() > 0;
      boolean key = getRaQueryDataSet().getString("FKEY").trim().length() > 0;
      
      if (store != key || cert != store) {
        if (!cert) jraCert.requestFocus();
        else if (!store) jraStore.requestFocus();
        else jraPass.requestFocus();
        JOptionPane.showMessageDialog(this, "Moraju biti unešena sva tri podatka, ili nijedan (za naslijeðivanje)!", 
            "Greška", JOptionPane.ERROR_MESSAGE);
        return false;
      }
    }
    
    if (!addZiroUI()) return false;

    return true;

  }



  private boolean addZiroUI() {
    if (isKnjigovodstvo()) {

      return ORS.addZiro(jtfCORG.getText(),jdbZIRO.getText());

    }

    return true;

  }



  public void SetFokus(char mode) {

    orgmode = mode;

    if (mode=='N') {

//      jlrNFPRIPADNOST.setFocusLostOnShow(false);
      rcbFisk.this_itemStateChanged();
      
      rcbFNU.this_itemStateChanged();
      
      jcbPDV.setSelected(true);

      clearJlrNFPRIPADNOST();

      rCC.setLabelLaF(jdbZIRO,false);

      rCC.setLabelLaF(jtfCORG, true);

//      jtfCORG.setEnabled(true);

      jtfCORG.requestFocus();

    }

    else {//if (mode=='I'){
      
      rcbFisk.this_itemStateChanged();
      rcbFNU.this_itemStateChanged();

//      jlrNFPRIPADNOST.setFocusLostOnShow(true);

      setJlrNFPRIPADNOST();

      if (mode=='I') {

//        jtfCORG.setEnabled(false);

        rCC.setLabelLaF(jtfCORG, false);

        jtfNAZIV.requestFocus();

        setCurrentKnjig();

        defineJdbZIRO();

      }

    }

    if (mode != 'B') enabPripPanel(!isKnjigovodstvo());

  }

  private void defineJdbZIRO() {

    if (orgmode == 'B') return;

    if (isKnjigovodstvo()) {

      ORS.getKnjigziro(currentKNJIG);

      ORS.getCurrentKnjigziro().open();

/*      int zircnt = ORS.getCurrentKnjigziro().getRowCount();

      if (zircnt>0) {

        getRaQueryDataSet().setString("ZIRO",ORS.getCurrentKnjigziro().getString("ZIRO"));

      } else if (zircnt==0) {

        jdbZIRO.setText("");

      }*/

      jdbZIRO.setRaDataSet(ORS.getCurrentKnjigziro());

    } else {

      jdbZIRO.setText("");

    }

    rCC.setLabelLaF(jdbZIRO,isKnjigovodstvo());

    rCC.setLabelLaF(jBgetZiro,isKnjigovodstvo());

  }

  private boolean isKnjigovodstvo() {

    return getRaQueryDataSet().getString("NALOG").equals("1");

  }

  private void setJlrNFPRIPADNOST() {

    jlrNFPRIPADNOST.setText(getRaQueryDataSet().getString("PRIPADNOST"));

    jlrNFPRIPADNOST.forceFocLost();

  }

  private void clearJlrNFPRIPADNOST() {

    jlrNFPRIPADNOST.setText("");

    jlrNFNazivPrip.setText("");

  }

  private void setPripPanel() {

//    if (jdbCBnalog.isSelected()) {

    if (isKnjigovodstvo()) {

      getRaQueryDataSet().setString("PRIPADNOST", jtfCORG.getText());

      jlrNFPRIPADNOST.setText(jtfCORG.getText());

      jlrNFNazivPrip.setText(jtfNAZIV.getText());

      setCurrentKnjig();

    } else {

      clearJlrNFPRIPADNOST();

    }

    defineJdbZIRO();

    enabPripPanel(!isKnjigovodstvo());

  }

  private void enabPripPanel(boolean kako) {

    rCC.setLabelLaF(jlrNFPRIPADNOST,kako);

    rCC.setLabelLaF(jlrNFNazivPrip,kako);

    rCC.setLabelLaF(jBgetPrip,kako);

  }



  void pripadnostAfter_lookUp() {//After lookup

    getRaQueryDataSet().setString("PRIPADNOST", jlrNFPRIPADNOST.getText());

    if (jlrNFPRIPADNOST.getText().equals(jtfCORG.getText())) {

      getRaQueryDataSet().setString("NALOG","1");

      setPripPanel();

    }

  }
  
  void fiskChanged() {
    if (rcbFisk.getSelectedIndex() == 2) {
      rCC.setLabelLaF(jraFPP, true);
      rCC.setLabelLaF(rcbFNU, true);
      rCC.setLabelLaF(jraCert, true);
      rCC.setLabelLaF(jraStore, true);
      rCC.setLabelLaF(jraPass, true);
    } else {
      jraFPP.setText("");
      jraCert.setText("");
      jraStore.setText("");
      jraPass.setText("");
      rCC.setLabelLaF(jraFPP, false);
      rCC.setLabelLaF(rcbFNU, false);
      rCC.setLabelLaF(jraCert, false);
      rCC.setLabelLaF(jraStore, false);
      rCC.setLabelLaF(jraPass, false);
    }
  }

  void setCurrentKnjig() {

    if (isKnjigovodstvo()) {

      currentKNJIG = jlrNFPRIPADNOST.getText();

    } else {

      currentKNJIG = ORS.getPripKnjig(jlrNFPRIPADNOST.getText());

    }

  }



  void jdbCBnalog_actionPerformed(ActionEvent e) {

    setPripPanel();

  }

}