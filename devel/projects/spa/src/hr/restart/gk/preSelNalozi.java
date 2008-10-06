/****license*****************************************************************
**   file: preSelNalozi.java
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

import hr.restart.swing.JraTextField;
import hr.restart.util.PreSelect;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;

import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class preSelNalozi extends PreSelect {
  private static preSelNalozi presNal;
  JPanel jp = new JPanel();
  XYLayout xYLay = new XYLayout();
  JLabel jlDATKNJ = new JLabel();
  JraTextField jtDATUMOD = new JraTextField();
  JraTextField jtDATUMDO = new JraTextField();
  JraTextField jtKNJIG = new JraTextField();
  JLabel jlStatus = new JLabel();
  raComboBox rcbSTATUS = new raComboBox();
  jpGetVrsteNaloga jpGetVrnal = new jpGetVrsteNaloga();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  Util Ut = Util.getUtil();
  Valid Vl = Valid.getValid();
  private boolean _newinstance = false;
  public preSelNalozi() {
    try {
      jbInit();
      presNal = this;
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public preSelNalozi(boolean newinstance) {
    try {
      _newinstance = newinstance;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public static preSelNalozi getNewPreSelect() {
    return new preSelNalozi(true);
  }
  public static preSelNalozi getPreSelect() {
    if (presNal == null) presNal = new preSelNalozi();
    return presNal;
  }

  private void jbInit() throws Exception {
    jlDATKNJ.setText("Dat. knjiženja (od - do)");
    xYLay.setWidth(545);
    xYLay.setHeight(110);
    jp.setLayout(xYLay);
    jlStatus.setText("Status");
    jtDATUMOD.setColumnName("DATUMKNJ");
    jtDATUMOD.setDataSet(dm.getNalozi());
    jtDATUMOD.setHorizontalAlignment(SwingConstants.CENTER);
    jtDATUMDO.setColumnName("DATUMKNJ");
    jtDATUMDO.setDataSet(dm.getNalozi());
    jtDATUMDO.setHorizontalAlignment(SwingConstants.CENTER);
    jtKNJIG.setDataSet(dm.getNalozi());
    jtKNJIG.setColumnName("KNJIG");
    jtKNJIG.setVisible(false);
    jtKNJIG.setEnabled(false);
    rcbSTATUS.setRaColumn("STATUS");
    rcbSTATUS.setRaItems(new String[][] {
      {"Svi",""},
      {"Spremni","S"},
      {"Nespremni","N"},
      {"Knjiženi","K"}
    });
    jp.add(jtKNJIG, new XYConstraints(0,0,0,0));
    jp.add(jpGetVrnal,    new XYConstraints(0, 0, -1, -1));
    jp.add(jlDATKNJ,   new XYConstraints(15, 45, -1, -1));
    jp.add(jtDATUMOD,  new XYConstraints(150, 45, 100, -1));
    jp.add(jtDATUMDO,    new XYConstraints(255, 45, 100, -1));
    jp.add(jlStatus,    new XYConstraints(15, 70, -1, -1));
    jp.add(rcbSTATUS,       new XYConstraints(150, 70, 100, -1));
    addSelRange(jtDATUMOD,jtDATUMDO);
    installResetButton();
    setSelDataSet(dm.getNalozi());
    setSelPanel(jp);
  }

  boolean initialized;
  public void resetDefaults() {
    initialized = true;
    setDateInText(jtDATUMOD,Ut.getFirstDayOfMonth());
    setDateInText(jtDATUMDO,Ut.getLastDayOfMonth());
  }
  public void SetFokus() {
    if (!initialized) resetDefaults();
    getSelRow().setString("KNJIG",hr.restart.zapod.OrgStr.getKNJCORG());
    jpGetVrnal.jlrCVRNAL.requestFocus();
  }
  public boolean Validacija() {
    //if (Vl.isEmpty(jpGetVrnal.jlrCVRNAL)) return false;
    if (Vl.isEmpty(jtDATUMOD)) return false;
    if (Vl.isEmpty(jtDATUMDO)) return false;
    /*if (!Ut.isValidRange(getDateFromText(jtDATUMOD),getDateFromText(jtDATUMDO))) {
      JOptionPane.showMessageDialog(jtDATUMOD,"Datumski period nije ispravan","Greška",JOptionPane.ERROR_MESSAGE);
      return false;
    }*/
    vrnalEntered = !Vl.chkIsEmpty(jpGetVrnal.jlrCVRNAL);
    if (!_newinstance) checkVrnal(true);//samo neka upozori
    return true;
  }
  boolean checkVrnal(boolean msg) {
    if (!vrnalEntered) return true;
    dm.getZirorn().open();
//sysoutTEST ST = new sysoutTEST(false);
//ST.prn(dm.getZirorn());
//ST.prn(getSelRow());
    boolean existsZiro = lookupData.getlookupData().raLocate(
      dm.getZirorn(),
      new String[] {"CORG","CVRNAL"},
      new String[] {hr.restart.zapod.OrgStr.getKNJCORG(false),getSelRow().getString("CVRNAL")}
    );
    if (existsZiro) {
      if (msg) JOptionPane.showMessageDialog(getPreSelDialog(),"Odabrana vrsta naloga koristi se za izvode! Mogu\u0107 samo pregled!");
      return false;
    } else {
      return true;
    }
  }
  public boolean isPeriod(java.sql.Timestamp date) {
    return (date.after(getDateFromText(jtDATUMOD))||date.equals(getDateFromText(jtDATUMOD)))
            &&
           (date.before(getDateFromText(jtDATUMDO))||date.equals(getDateFromText(jtDATUMDO)));
  }
  private void setDateInText(JraTextField jtext,Timestamp date) {
    try {
      jtext.getDataSet().setTimestamp(jtext.getColumnName(),date);
    }
    catch (Exception ex) {
      System.out.println("setDateInText ex:"+ex);
    }
  }
  private Timestamp getDateFromText(JraTextField jtext) {
    try {
      return Ut.clearTime(jtext.getDataSet().getTimestamp(jtext.getColumnName()));
    }
    catch (Exception ex) {
      return null;
    }
  }
  public Timestamp getDefaultDate() {
    return getDateFromText(jtDATUMDO);
  }
  boolean vrnalEntered = false;
  public boolean isVrnalEntered() {
    return vrnalEntered;
  }
}