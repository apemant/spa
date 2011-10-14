/****license*****************************************************************
**   file: frmPjpar.java
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
import hr.restart.baza.Kosobe;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraLabel;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raMatPodaci;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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

public class frmPjpar extends raMatPodaci {
  JPanel jp = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  QueryDataSet paramQDS = new QueryDataSet();
  frmPartneri fP;
  int cpar;
  dM dm;
  Valid vl = Valid.getValid();

  JLabel jlPJ = new JLabel();
  JraLabel jtPJ = new JraLabel();
  JraTextField jtNAZPJ = new JraTextField();
  JLabel jlMJPJ = new JLabel();
  JraTextField jtMJPJ = new JraTextField();
  JLabel jlADRPJ = new JLabel();
  JraTextField jtADRPJ = new JraTextField();
  JraTextField jtPBRPJ = new JraTextField();
  JLabel jlTELPJ = new JLabel();
  JraTextField jtTELPJ = new JraTextField();
  JLabel jlTELFAXPJ = new JLabel();
  JraTextField jtTELFAXPJ = new JraTextField();
  JLabel jlKOPJ = new JLabel();
  JraTextField jtKOPJ = new JraTextField();
  JLabel jlREGIJA = new JLabel();
  JraTextField jtREGIJA = new JraTextField();
  JraCheckBox jcbAktiv = new JraCheckBox();
  
  JLabel jlKontaktOsoba = new JLabel("Kontakt osoba");
  JraButton jbKO = new JraButton();
  JlrNavField jrfKO = new JlrNavField();
  JlrNavField jrfNAZKO = new JlrNavField();
  
  JLabel jlAgent = new JLabel("Agent");
  JraButton jbAGENT = new JraButton();
  JlrNavField jrfCAGENT = new JlrNavField();
  JlrNavField jrfNAZAGENT = new JlrNavField();
  
  JLabel jlGLN = new JLabel();
  JraTextField jtGLN = new JraTextField();

  public frmPjpar(frmPartneri f, QueryDataSet tempParamQDS, int cpar) {
    try {
      fP = f;
      this.cpar=cpar;
      fP.setEnabled(false);
      paramQDS = tempParamQDS;
      paramQDS.open();
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public void ZatvoriOstalo() {
    fP.setEnabled(true);
    fP.toFront();
//    fP.show();
  }
  public JPanel getPjPanel() {
    return jp;
  }

  private void jbInit() throws Exception {
/*    int x= (this.getToolkit().getDefaultToolkit().getScreenSize().width)-555;
    int y= (this.getToolkit().getDefaultToolkit().getScreenSize().height)-330;
    this.setLocation((int)x/2,(int)y/2);
*/
    this.setTitle("Poslovne jedinice -  " + fP.getRaQueryDataSet().getString("NAZPAR"));
//    this.setSize(555, 330);
//    this.getJpTableView().setMinimumSize(new Dimension(555, 150));
    dm = dM.getDataModule();

    this.setRaQueryDataSet(paramQDS);
    this.setVisibleCols(new int[] {2,3,4});

//    paramQDS.setSort(new SortDescriptor(new String[]{"PJ"}));

    jlPJ.setText("Poslovna jedinica");
    jp.setLayout(xYLayout1);

    jcbAktiv.setColumnName("AKTIV");
    jcbAktiv.setDataSet(getRaQueryDataSet());
    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktiv.setSelectedDataValue("D");
    jcbAktiv.setText("Aktivan");
    jcbAktiv.setUnselectedDataValue("N");

    jtPJ.setHorizontalAlignment(SwingConstants.RIGHT);
    jtPJ.setText("1");
    jtPJ.setColumnName("PJ");
    jtPJ.setBorder(BorderFactory.createLoweredBevelBorder());
    jtPJ.setDataSet(getRaQueryDataSet());
    jtNAZPJ.setColumnName("NAZPJ");
    jtNAZPJ.setDataSet(getRaQueryDataSet());
    jlMJPJ.setText("PT broj i mjesto");
    jtMJPJ.setDataSet(getRaQueryDataSet());
    jtMJPJ.setColumnName("MJPJ");
    jlADRPJ.setText("Adresa");
    jtADRPJ.setColumnName("ADRPJ");
    jtADRPJ.setDataSet(getRaQueryDataSet());
    jtPBRPJ.setColumnName("PBRPJ");
    jtPBRPJ.setDataSet(getRaQueryDataSet());
    xYLayout1.setWidth(515);
    xYLayout1.setHeight(265);
    jlTELPJ.setText("Telefoni");
    jtTELPJ.setDataSet(getRaQueryDataSet());
    jtTELPJ.setColumnName("TELPJ");
    jlTELFAXPJ.setText("Telefax");
    jtTELFAXPJ.setDataSet(getRaQueryDataSet());
    jtTELFAXPJ.setColumnName("TELFAXPJ");
    jlKOPJ.setText("Kontakt osobe");
    jtKOPJ.setColumnName("KOPJ");
    jtKOPJ.setDataSet(getRaQueryDataSet());
    
    jlGLN.setText("GLN jedinice");
    jtGLN.setColumnName("GLN");
    jtGLN.setDataSet(getRaQueryDataSet());
    
    jlREGIJA.setText("Regija");
    jtREGIJA.setDataSet(getRaQueryDataSet());
    jtREGIJA.setColumnName("REGIJA");
    
    jrfKO.setColumnName("CKO");
    jrfKO.setDataSet(getRaQueryDataSet());
    jrfKO.setTextFields(new javax.swing.text.JTextComponent[] { jrfNAZKO });
    jrfKO.setColNames(new String[] { "IME" });
    jrfKO.setRaDataSet(Kosobe.getDataModule().getFilteredDataSet(
        Condition.equal("CPAR", cpar)));
    jrfKO.setVisCols(new int[] { 2, 3 });
    jrfKO.setNavButton(jbKO);

    jrfNAZKO.setSearchMode(1);
    jrfNAZKO.setColumnName("IME");
    jrfNAZKO.setNavProperties(jrfKO);
    
    jrfCAGENT.setColumnName("CAGENT");
    jrfCAGENT.setDataSet(getRaQueryDataSet());
    jrfCAGENT.setTextFields(new javax.swing.text.JTextComponent[] { jrfNAZAGENT });
    jrfCAGENT.setColNames(new String[] { "NAZAGENT" });
    jrfCAGENT.setRaDataSet(dm.getAgenti());
    jrfCAGENT.setVisCols(new int[] { 0, 1 });
    jrfCAGENT.setNavButton(jbAGENT);

    jrfNAZAGENT.setSearchMode(1);
    jrfNAZAGENT.setColumnName("NAZAGENT");
    jrfNAZAGENT.setNavProperties(jrfCAGENT);
    

    jp.add(jlPJ,   new XYConstraints(15, 30, -1, -1));
    jp.add(jtPJ,   new XYConstraints(150, 30, 30, -1));
    jp.add(jlMJPJ,   new XYConstraints(15, 80, -1, -1));
    jp.add(jtMJPJ,     new XYConstraints(255, 80, 230, -1));
    jp.add(jtPBRPJ,   new XYConstraints(150, 80, 100, -1));
    jp.add(jtNAZPJ,    new XYConstraints(185, 30, 300, -1));
    jp.add(jtADRPJ,    new XYConstraints(150, 55, 335, -1));
    jp.add(jlADRPJ,   new XYConstraints(15, 55, -1, -1));
    jp.add(jtTELPJ,    new XYConstraints(150, 105, 335, -1));
    jp.add(jlTELPJ,   new XYConstraints(15, 105, -1, -1));
    jp.add(jlTELFAXPJ,   new XYConstraints(15, 130, -1, -1));
    jp.add(jtTELFAXPJ,   new XYConstraints(150, 130, 100, -1));
    jp.add(jlKontaktOsoba, new XYConstraints(15, 180, -1, -1));
    jp.add(jrfKO, new XYConstraints(150, 180, 100, -1));
    jp.add(jrfNAZKO, new XYConstraints(255, 180, 230, -1));
    jp.add(jbKO, new XYConstraints(490, 180, 21, 21));
    jp.add(jlKOPJ, new XYConstraints(15, 205, -1, -1));
    jp.add(jtKOPJ, new XYConstraints(150, 205, 335, -1));
    /*jp.add(jlREGIJA, new XYConstraints(15, 180, -1, -1));
    jp.add(jtREGIJA, new XYConstraints(150, 180, 100, -1));*/
    jp.add(jcbAktiv, new XYConstraints(415, 7, 70, -1));
    
    jp.add(jlAgent, new XYConstraints(15, 155, -1, -1));
    jp.add(jrfCAGENT, new XYConstraints(150, 155, 100, -1));
    jp.add(jrfNAZAGENT, new XYConstraints(255, 155, 230, -1));
    jp.add(jbAGENT, new XYConstraints(490, 155, 21, 21));
    
    jp.add(jlGLN, new XYConstraints(15, 230, -1, -1));
    jp.add(jtGLN, new XYConstraints(150, 230, 120, -1));
    this.setRaDetailPanel(jp);
  }

  public void EntryPoint(char mode) {
    getRaQueryDataSet().setInt("CPAR", cpar);
  }

  public void ExitPoint(char mode) {
  }
  public boolean Validacija(char mode) {
    if (mode !='B') {
      frmKosobe.addKontakt(cpar,getRaQueryDataSet().getString("KOPJ"),"poslovna jedinica "+getRaQueryDataSet().getString("NAZPJ"));
    }
    return !vl.isEmpty(jtNAZPJ);
  }
  public void SetFokus(char mode) {
    if (mode == 'N') {
      getRaQueryDataSet().setInt("CPAR", cpar);
      getRaQueryDataSet().setInt("PJ", rdZapodUtil.getrdZPUtil().getPJ(cpar)+1);
      jtNAZPJ.requestFocus();
    }
    if(mode == 'I')
      jtNAZPJ.requestFocus();
  }

//  public void AfterSave(char mode)
//  {
//    paramQDS.refilter();
//  }
//  public void beforeShow()
//  {
//    this.getJpTableView().getDataSet().setSort(new SortDescriptor(new String[]{"PJ"}));
//  }
}
