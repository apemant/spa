/****license*****************************************************************
**   file: raKontoCorgGroup.java
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
package hr.restart.gk;

import hr.restart.swing.JraButton;
import hr.restart.util.JlrNavField;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.raKonta;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
public class raKontoCorgGroup {
  private JComponent nextComponent;
  private raCommonClass rCC = raCommonClass.getraCommonClass();
  private raKonta rKon;
  private OrgStr Ojs = OrgStr.getOrgStr();
  private lookupData ld = lookupData.getlookupData();
  private raMatPodaci raMP;
  private com.borland.dx.dataset.StorageDataSet dataSet;
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();

  private JlrNavField jlrCORG;
  private JlrNavField jlrBROJKONTA;
  private JlrNavField jlrNAZIVKONTA;
  private JlrNavField jlrNAZIVORG;
  private JraButton jbGetCorg;
  private JraButton jbGetKonto;


  public raKontoCorgGroup(JlrNavField _jlrBROJKONTA, JlrNavField _jlrCORG, JComponent _nextComponent) {
    jlrCORG = _jlrCORG;
    jlrBROJKONTA = _jlrBROJKONTA;
    nextComponent = _nextComponent;
  }
  public raKontoCorgGroup(raMatPodaci _raMP) {
    this(_raMP,null);
  }
  public raKontoCorgGroup(raMatPodaci _raMP,JComponent _nextComponent)  {
    raMP = _raMP;
    dataSet = raMP.getRaQueryDataSet();
    nextComponent = _nextComponent;
    initComponents();
  }
  public raKontoCorgGroup(com.borland.dx.dataset.StorageDataSet _dataSet )  {
    raMP = null;
    dataSet = _dataSet;
    initComponents();
  }

  protected void initComponents() {
    jlrCORG = new JlrNavField() {
      public void after_lookUp() {
      }
    };
    jlrNAZIVORG = new JlrNavField();
    jbGetCorg = new JraButton();
    jbGetKonto = new JraButton();
    jlrBROJKONTA = new JlrNavField() {
/*
      protected void this_keyPressed(KeyEvent kev) {
        if (kev.getKeyCode() == kev.VK_F9) {
          setJlrBROJKONTA_searchMode(1);
        }
        super.this_keyPressed(kev);
      }
      public void focusGained(FocusEvent e) {
        super.focusGained(e);
        setJlrBROJKONTA_searchMode(0);
      }
*/
      public void after_lookUp() {
        if (getMode()!='B') aft_lookUpKonto(isLastLookSuccessfull());
        afterAfterLookupKonto();
      }
    };

    jlrNAZIVKONTA = new JlrNavField() {
      public void after_lookUp() {
        if (getMode()!='B') aft_lookUpKonto(isLastLookSuccessfull());
      }
    };

    jlrBROJKONTA.setColumnName("BROJKONTA");
    jlrBROJKONTA.setDataSet(dataSet);
    jlrBROJKONTA.setColNames(new String[] {"NAZIVKONTA"});
    jlrBROJKONTA.setVisCols(new int[] {0,1});
    jlrBROJKONTA.setTextFields(new javax.swing.text.JTextComponent[] {jlrNAZIVKONTA});
    jlrBROJKONTA.setRaDataSet(rKon.getAnalitickaKonta());
//    jlrBROJKONTA.setSearchMode(0);
    jlrBROJKONTA.setSearchMode(3);
    //test
    //jlrBROJKONTA.setLookupFrameWidth(1000);
    jlrNAZIVKONTA.setColumnName("NAZIVKONTA");
    jlrNAZIVKONTA.setSearchMode(1);
    jlrNAZIVKONTA.setNavProperties(jlrBROJKONTA);
    jlrBROJKONTA.setNavButton(jbGetKonto);

    jlrCORG.setColumnName("CORG");
    jlrCORG.setDataSet(dataSet);
    jlrCORG.setColNames(new String[] {"NAZIV"});
    jlrCORG.setVisCols(new int[] {0,1});
    jlrCORG.setTextFields(new javax.swing.text.JTextComponent[] {jlrNAZIVORG});
    jlrCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig()/*dm.getOrgstruktura()*/);
    jlrCORG.setSearchMode(0);
    jlrNAZIVORG.setColumnName("NAZIV");
    jlrNAZIVORG.setSearchMode(1);
    jlrNAZIVORG.setNavProperties(jlrCORG);
    jlrCORG.setNavButton(jbGetCorg);
    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnjig, String newKnjig) {
        jlrCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
        jlrNAZIVORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      };
    });
  }
  public char getMode() {
    if (raMP == null) return 'N';
    return raMP.getMode();
  }
  public void aft_lookUpKonto(boolean succ) {
    String brKonta = jlrBROJKONTA.getDataSet().getString("BROJKONTA");//fNalozi.getDetailSet().getString("BROJKONTA");
    if (brKonta.equals("")) {
      clrBROJKONTA();
      return;
    }
    if (!succ) {
      clrBROJKONTA();
      return;
    }
    setScrKonto(brKonta);
//    jlrBROJKONTA.getDataSet().getString("BROJKONTA")+
//        " - " + jlrBROJKONTA.getDataSet().getString("NAZIVKONTA");

//    if (raMP.equals(frmNalozi.getFrmNalozi().raDetail))
//    {
//      sysoutTEST ST = new sysoutTEST(false);
//      ST.prn(rKon.getAnalitickaKonta());
//      frmNalozi.getFrmNalozi().setKontoConcat(jlrBROJKONTA.getDataSet().getString("BROJKONTA"),
//       jlrBROJKONTA.getDataSet().getString("NAZIVKONTA") );
//    }
    reqFocusAfterKonto();
  }
  private void reqFocusAfterKonto() {
    if (rKon.isOrgStr(jlrBROJKONTA.getDataSet().getString("BROJKONTA"))) {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          jlrCORG.requestFocus();
        }
      });
    } else if (nextComponent!=null) {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          nextComponent.requestFocus();          
        }
      });
    }
  }

  public void clrBROJKONTA() {
    jlrBROJKONTA.emptyTextFields();
    jlrBROJKONTA.setText("");
    jlrBROJKONTA.maskCheck();
  }

  public void clrCORG() {
    jlrCORG.emptyTextFields();
    jlrCORG.setText("");
    jlrCORG.maskCheck();
  }

  public void setEnabledKonto(boolean enabled) {
    setEnabledNavGroup(jlrBROJKONTA,enabled);
  }
  void setEnabledNavGroup(JlrNavField field,boolean enabled) {
    rCC.setLabelLaF(field,enabled);
    for (int i=0;i<field.getTextFields().length;i++) {
      rCC.setLabelLaF(field.getTextFields()[i],enabled);
    }
    if (field.getNavButton()!=null) rCC.setLabelLaF(field.getNavButton(),enabled);
  }
  void setEnabledCorg(boolean enabled) {
    setEnabledNavGroup(jlrCORG,enabled);
    String strKnjig = Ojs.getKNJCORG(false); //fNalozi.getMasterSet().getString("KNJIG");
    if (enabled) {
      if (strKnjig.equals(jlrCORG.getText()) && !isKnjigWithOrgs_konto_orgstr()) {
        clrCORG();
      }
      if (isKnjigWithOrgs_konto_orgstr()) {
        setCORGDataSet(Ojs.getOrgstrAndCurrKnjig());
      } else {
        setCORGDataSet(Ojs.getOrgstrFromCurrKnjig());
      }
    } else {
      setCORGDataSet(dm.getOrgstruktura());
      jlrCORG.getDataSet().setString("CORG",strKnjig);
      jlrCORG.forceFocLost();
    }
  }
  
  public static boolean isKnjigWithOrgs_konto_orgstr() {
    return hr.restart.sisfun.frmParam.getParam("gk", "kontorgstr+knj", "N", "Ponuditi i knjigovodstvo za konta pridruzena org. strukturi (D/N)?")
            .equals("D");
  }
  
  public void setCORGDataSet(com.borland.dx.dataset.DataSet ds) {
    jlrCORG.setRaDataSet(ds);
    for (int i=0;i<jlrCORG.getTextFields().length;i++) {
      try {
        ((JlrNavField)jlrCORG.getTextFields()[i]).setRaDataSet(ds);
      }
      catch (Exception ex) {

      }
    }
  }
  /**
   * deklatacija ZADANIH komponente trebala bi izgledati ovako:
   * <pre>
   * JlrNavField jlrBROJKONTA = new JlrNavField() {
   *     protected void this_keyPressed(KeyEvent kev) {
   *       if (kev.getKeyCode() == kev.VK_F9) {
   *         kcGroup.setJlrBROJKONTA_searchMode(1);
   *       }
   *       super.this_keyPressed(kev);
   *     }
   *     public void focusGained(FocusEvent e) {
   *       super.focusGained(e);
   *       kcGroup.setJlrBROJKONTA_searchMode(0);
   *     }
   *     public void after_lookUp() {
   *       if (fNalozi.raDetail.getMode()!='B') kcGroup.aft_lookUpKonto();
   *     }
   * };
   * JlrNavField jlrNAZIVKONTA = new JlrNavField() {
   *     public void after_lookUp() {
   *       if (fNalozi.raDetail.getMode()!='B') kcGroup.aft_lookUpKonto();
   *     }
   * };
   * </pre>
   * @param sm
   */
  public void setJlrBROJKONTA_searchMode(int sm) {
    jlrBROJKONTA.setSearchMode(sm);

  }
  public void setScrKonto(String brKonta) {
    setEnabledCorg(rKon.isOrgStr(brKonta));
    setEnabledKonto(false);
  }

//geteri i seteri
  public JlrNavField getJlrCORG() {
    return jlrCORG;
  }
  public JlrNavField getJlrBROJKONTA() {
    return jlrBROJKONTA;
  }
  public JlrNavField getJlrNAZIVORG() {
    return jlrNAZIVORG;
  }
  public JlrNavField getJlrNAZIVKONTA() {
    return jlrNAZIVKONTA;
  }
  public JraButton getJbGetCorg() {
    return jbGetCorg;
  }
  public JraButton getJbGetKonto() {
    return jbGetKonto;
  }

  public void setNextComponent(JComponent _component) {
    nextComponent = _component;
  }
  public com.borland.dx.dataset.DataSet getKontaSet() {
    return jlrBROJKONTA.getRaDataSet();
  }
  public void afterAfterLookupKonto() {

  }
//  public String getKontoConcat()
//  {
//    return jlrBROJKONTA.getDataSet().getString("BROJKONTA")+
//        " - " + jlrBROJKONTA.getDataSet().getString("NAZIVKONTA");
//  }
}