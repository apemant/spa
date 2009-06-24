/****license*****************************************************************
**   file: jpSelectRevers.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.PreSelect;
import hr.restart.util.raMasterDetail;

import java.awt.Font;
import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.DataRow;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;
/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author REST-ART development team
 * @version 1.0
 */

//abstract public class jpPreselectDoc extends PreSelect {
public class jpSelectRevers extends PreSelect {

  hr.restart.util.Valid val = hr.restart.util.Valid.getValid();
  hr.restart.util.Util uut = hr.restart.util.Util.getUtil();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  rapancskl1 rpcskl = new rapancskl1(false,272);
  JLabel jlVRDOK = new JLabel();
  JLabel jlDatum = new JLabel();
  JraTextField jtfDATUMOD = new JraTextField();
  JraTextField jtfDATUMDO = new JraTextField();
  JraTextField jtfVRDOK = new JraTextField();
  XYLayout xYLayout1 = new XYLayout();
  JPanel jpSelDoc = new JPanel();
  String raDokument;
  JLabel jlBRDOK = new JLabel();
  JraTextField jtfBRDOK = new JraTextField();

/*
  JLabel jlCSKL = new JLabel("Skladište");
  JlrNavField jrfCSKL = new JlrNavField();
  JlrNavField jrfNAZSKL = new JlrNavField();
  JraButton jbCSKL = new JraButton();
*/

  JLabel jlCORG = new JLabel("Organizacijska jedinica");
  JlrNavField jrfCORG = new JlrNavField();
  JlrNavField jrfNAZORG = new JlrNavField();
  JraButton jbCORG = new JraButton();

  JLabel jlCPAR = new JLabel("Partner");
  JlrNavField jrfCPAR = new JlrNavField();
  JlrNavField jrfNAZPAR = new JlrNavField();
  JraButton jbCPAR = new JraButton();

  public jpSelectRevers() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {

    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener() {
      public void knjigChanged(String oldKnjig, String newKnjig) {
        rpcskl.jrfCSKL.setRaDataSet(hr.restart.robno.Util.getSkladFromCorg());
      }
    });

    jpSelDoc.setLayout(xYLayout1);
    xYLayout1.setHeight(185);
    xYLayout1.setWidth(565);
    jlDatum.setText("Datum  (od - do)");
    jlVRDOK.setText("Vrsta dokumenta");
    jtfVRDOK.setText(raDokument);
    jtfVRDOK.setColumnName("VRDOK");
    jtfVRDOK.setFont(jtfVRDOK.getFont().deriveFont(Font.BOLD));
    jtfVRDOK.setOpaque(false);
    jtfVRDOK.setEnabled(false);
    jtfVRDOK.setEnablePopupMenu(false);
    jtfVRDOK.setDisabledTextColor(jtfVRDOK.getForeground());
    jtfVRDOK.setBorder(null);
    jtfDATUMOD.setHorizontalAlignment(SwingConstants.CENTER);
    jtfDATUMOD.setColumnName("DATDOK");
    jtfDATUMDO.setHorizontalAlignment(SwingConstants.CENTER);
    jtfDATUMDO.setColumnName("DATDOK");
    jlBRDOK.setText("Broj dokumenta");
    jtfBRDOK.setColumnName("BRDOK");


/// CORG
    jrfCORG.setColumnName("CORG");
    jrfCORG.setColNames(new String[] {"NAZIV"});
    jrfCORG.setVisCols(new int[]{0,1,2});
    jrfCORG.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZORG});
    jrfCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener() {
      public void knjigChanged(String oldKnjig,String newKnjig) {
        jrfCORG.getRaDataSet().refresh();
        jrfNAZORG.getRaDataSet().refresh();
      }
    });
    jrfNAZORG.setColumnName("NAZIV");
    jrfNAZORG.setSearchMode(1);
    jrfNAZORG.setNavProperties(jrfCORG);
    jbCORG.setText("...");
    jrfCORG.setNavButton(jbCORG);

//  CPAR
    jrfCPAR.setColumnName("CPAR");
    jrfCPAR.setColNames(new String[] {"NAZPAR"});
    jrfCPAR.setVisCols(new int[]{0,1,2});
    jrfCPAR.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZPAR});
    jrfCPAR.setRaDataSet(dm.getPartneri());
    jrfNAZPAR.setColumnName("NAZPAR");
    jrfNAZPAR.setSearchMode(1);
    jrfNAZPAR.setNavProperties(jrfCPAR);
    jbCPAR.setText("...");
    jrfCPAR.setNavButton(jbCPAR);

    jpSelDoc.add(jlVRDOK, new XYConstraints(15, 20, -1, -1));
    jpSelDoc.add(jtfVRDOK, new XYConstraints(150, 20, 50, -1));
    jpSelDoc.add(rpcskl, new XYConstraints(0, 45, -1, -1));

    jpSelDoc.add(jlCPAR, new XYConstraints(15, 70, -1, -1));
    jpSelDoc.add(jrfCPAR, new XYConstraints(150, 70, 100, -1));
    jpSelDoc.add(jrfNAZPAR, new XYConstraints(255, 70, 275, -1));
    jpSelDoc.add(jbCPAR, new XYConstraints(532, 70, 21, 21));

    jpSelDoc.add(jlCORG, new XYConstraints(15, 95, -1, -1));
    jpSelDoc.add(jrfCORG, new XYConstraints(150, 95, 100, -1));
    jpSelDoc.add(jrfNAZORG, new XYConstraints(255, 95, 275, -1));
    jpSelDoc.add(jbCORG, new XYConstraints(532, 95, 21, 21));

    jpSelDoc.add(jlBRDOK, new XYConstraints(15, 120, -1, -1));
    jpSelDoc.add(jtfBRDOK, new XYConstraints(150, 120, 100, -1));

    jpSelDoc.add(jlDatum, new XYConstraints(15, 145 , -1, -1));
    jpSelDoc.add(jtfDATUMOD, new XYConstraints(150, 145, 100, -1));
    jpSelDoc.add(jtfDATUMDO, new XYConstraints(255, 145, 100, -1));

  }

  public void showJpSelectDoc(String raDocumentC, raMasterDetail owner, boolean showPres) {
    showJpSelectDoc(raDocumentC, owner, showPres, "", true);
  }
  public void showJpSelectDoc(String raDocumentC, raMasterDetail owner, boolean showPres, String tit) {
    showJpSelectDoc(raDocumentC, owner, showPres, tit, true);
  }
  public void showJpSelectDoc(String raDocumentC, raMasterDetail owner, boolean showPres, String tit, boolean imaPartner) {
    raDokument=raDocumentC;
    setSelDataSet(owner.getMasterSet());
    addSelRange(jtfDATUMOD, jtfDATUMDO);
    setSelPanel(jpSelDoc);
    if (showPres) {
      showPreselect(owner ,"Predselekcija - "+tit);
      this.getSelPanel().repaint();
    } else {
      doSelect();
      owner.go();
    }
  }

  boolean firstTime = true;
  com.borland.dx.dataset.DataRow preselData;
  
  public void SetFokus() {
    if (firstTime) {
      prepareRaDokument();
      oslobodi(true);
    } else {
      dM.copyColumns(preselData, getSelRow());
      rpcskl.jrfCSKL.forceFocLost();
      jrfCORG.forceFocLost();
      if (jrfCPAR.isVisible())
        jrfCPAR.forceFocLost();
    }
    
    rpcskl.jrfCSKL.requestFocus();

  }

  public boolean Validacija() {
    if (val.isEmpty(rpcskl.jrfCSKL)) return false;
    if (getSelRow().getInt("BRDOK")>0) {
        getSelRow().setTimestamp("DATDOK-from",hr.restart.robno.Util.getUtil().findFirstDayOfYear(Integer.parseInt(val.findYear())));
      }
    if (!hr.restart.util.Util.getUtil().isValidRange(getSelRow().getTimestamp("DATDOK-from"), getSelRow().getTimestamp("DATDOK-to")))
      return false;
    firstTime = false;
    preselData = new DataRow(getSelRow());
    dM.copyColumns(getSelRow(), preselData);
    
    return true;
  }
  public void oslobodi(boolean default1) {
      rpcskl.disabCSKL(true);
      rpcskl.Clear();
      rpcskl.setDefaultCSKL();
      rpcskl.jrfCSKL.forceFocLost();
  }

  void prepareRaDokument() {
    getSelRow().setString("VRDOK",raDokument);
    
    Timestamp day = val.getToday();
    if (!Aut.getAut().getKnjigodRobno().equals(val.findYear()) &&
        !dm.getKnjigod().getString("STATRADA").equalsIgnoreCase("D"))
      day = uut.getYearEnd(Aut.getAut().getKnjigodRobno());
    
    getSelRow().setTimestamp("DATDOK-from", day);
    getSelRow().setTimestamp("DATDOK-to", day);
    getSelRow().post();
  }
  
  public boolean isBRDOK() {
  	if (getSelRow().getInt("BRDOK")==0) return false;
  	return true;
  }
}
