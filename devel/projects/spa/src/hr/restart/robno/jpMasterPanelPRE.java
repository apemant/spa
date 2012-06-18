/****license*****************************************************************
**   file: jpMasterPanelPRE.java
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
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class jpMasterPanelPRE extends JPanel {

  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
  private java.util.ResourceBundle res = java.util.ResourceBundle.getBundle("hr.restart.robno.Res");
  BorderLayout borderLayout1 = new BorderLayout();
  XYLayout xYLayout3 = new XYLayout();
  JPanel jpMasterCenter = new JPanel();
  rajpBrDok jpBRDOK = new rajpBrDok();
  JLabel jlOpis = new JLabel();
  private frmPRE master;

  JLabel jlDATDOK = new JLabel();
  JraTextField jtfDATDOK = new JraTextField();
  hr.restart.swing.JraTextArea jtfOPis = new hr.restart.swing.JraTextArea();

  jpRadniNalog jpRN = new jpRadniNalog(){
    public void after_Cancel(){
      master.afterCancel();
    }
    public void after_OK(){
      master.afterOK();
    }
  };

  JLabel jlRandman = new JLabel("Randman"); 
  JraTextField jraRandman = new JraTextField();

  JlrNavField jrfCORG = new JlrNavField(){
    public void after_lookUp(){
      Myafter_lookUp();
    }
  };
  JlrNavField jrfNAZORG = new JlrNavField();
  JraButton jbCORG = new JraButton();
  JraButton jbCRN = new JraButton();
  public void Myafter_lookUp(){
    jpRN.setDefaultCORG(jrfCORG.getText());
  }

  public jpMasterPanelPRE(frmPRE master) {
    this.master = master;
    try {
      jbInit();
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }

    void jbInit() throws Exception {

      jpMasterCenter.setBorder(BorderFactory.createEtchedBorder());
      jpMasterCenter.setLayout(xYLayout3);

      jpRN.setMode("P");      // za izdatnice, "R" za ra\u010Dune, "P" za primke
      this.setLayout(borderLayout1);

      jrfCORG.setColumnName("CORG");
      jrfCORG.setColNames(new String[] {"NAZIV"});
      jrfCORG.setVisCols(new int[]{0,1,2});
      jrfCORG.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZORG});
      jrfCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());

      jrfNAZORG.setColumnName("NAZIV");
      jrfNAZORG.setSearchMode(1);
      jrfNAZORG.setNavProperties(jrfCORG);
      
      jraRandman.setColumnName("RANDMAN");
      

      jbCORG.setText("...");
      jrfCORG.setNavButton(jbCORG);

      xYLayout3.setWidth(645);
      xYLayout3.setHeight(180);
      jtfDATDOK.setHorizontalAlignment(SwingConstants.CENTER);
      jtfDATDOK.setColumnName("DATDOK");
      jlDATDOK.setText(res.getString("jlDATDOK_text"));

      this.add(jpBRDOK, BorderLayout.NORTH);
      jpMasterCenter.add(new JLabel("Organizacijska jedinica "), new XYConstraints(15, 15, -1, -1));
      jpMasterCenter.add(jrfCORG, new XYConstraints(150, 15, 100, -1));
      jpMasterCenter.add(jrfNAZORG, new XYConstraints(255, 15, 345, -1));
      jpMasterCenter.add(jbCORG, new XYConstraints(605, 15, 21, 21));

      jpMasterCenter.add(jlDATDOK, new XYConstraints(15, 40, -1, -1));
      jpMasterCenter.add(jtfDATDOK, new XYConstraints(150, 40, 100, -1));

//      jpMasterCenter.add(new JLabel("Radni nalog"), new XYConstraints(15, 65, -1, -1));
//      jpMasterCenter.add(jrfRN, new XYConstraints(150, 65, 100, -1));
//      jpMasterCenter.add(jbCRN, new XYConstraints(255, 65, 21, 21));
      jpMasterCenter.add(jpRN, new XYConstraints(0, 65, -1, -1));
      
      if (frmParam.getParam("rn", "randman", "D", "Omoguæiti unos parametra RANDMAN (D,N)").equals("D")) {
	      jpMasterCenter.add(jlRandman, new XYConstraints(400, 65, -1, -1));
	      jpMasterCenter.add(jraRandman, new XYConstraints(500, 65, 100, -1));
      }

      jpMasterCenter.add(new JLabel("Opis"), new XYConstraints(15, 90, -1, -1));
      jpMasterCenter.add(jtfOPis, new XYConstraints(150, 90, 450, 80));

      this.add(jpMasterCenter, BorderLayout.CENTER);
    }

    public void initPanel(char mode) {
      jpBRDOK.SetDefTextDOK(mode);
      if (presPRE.getPres().jrfCPAR.getText().equals("")) {
        if (mode!='B') {
//          rcc.setLabelLaF(jrfCPAR, true);
//          rcc.setLabelLaF(jrfNAZPAR, true);
//          rcc.setLabelLaF(jbCPAR, true);
//          jrfCPAR.requestFocus();
        }
      }
      else {
//        rcc.setLabelLaF(jrfCPAR, false);
//        rcc.setLabelLaF(jrfNAZPAR, false);
//        rcc.setLabelLaF(jbCPAR, false);
//        jrfCPAR.forceFocLost();
        jtfDATDOK.requestFocus();
      }
    }

    void setDataSet(com.borland.dx.sql.dataset.QueryDataSet qds) {

      jtfDATDOK.setDataSet(qds);
      jrfCORG.setDataSet(qds);
      jpBRDOK.setDataSet(qds);
      jraRandman.setDataSet(qds);
    }


}
