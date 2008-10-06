/****license*****************************************************************
**   file: presIzvodi.java
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

import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.PreSelect;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.zapod.OrgStr;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class presIzvodi extends PreSelect {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  JPanel jpDetail = new JPanel();
  XYLayout lay = new XYLayout();
  JLabel jlBrojizvfrom = new JLabel();
  JLabel jlDatumfrom = new JLabel();
  JLabel jlZiro = new JLabel();
  JraButton jbSelZiro = new JraButton();
  JraTextField jraBrojizvfrom = new JraTextField();
  JraTextField jraBrojizvto = new JraTextField();
  JraTextField jraDatumfrom = new JraTextField();
  JraTextField jraDatumto = new JraTextField();
  JlrNavField jlrZiro = new JlrNavField();
  JraTextField jtKNJIG = new JraTextField();

  public presIzvodi() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  boolean initialized;
  public void resetDefaults() {
    initialized = true;
    Calendar cal = Calendar.getInstance();
    cal.setTime(vl.getToday());
    String izvtodaydiff = frmParam.getParam("gk","izvtodaydiff","0","Koliko dana od danas se inace unose izvodi (0 danas,-1 dan prije)");
    cal.set(Calendar.DATE,cal.get(Calendar.DATE)+Integer.parseInt(izvtodaydiff));
    Timestamp today = new Timestamp(cal.getTime().getTime());
    //
    vl.getCommonRange(jraDatumfrom, jraDatumto,today);

    setDefBrojIzv();
  }

  public void SetFokus() {
    if (!initialized) resetDefaults();
    getSelRow().setString("KNJIG", hr.restart.zapod.OrgStr.getKNJCORG());
    //parametriziramo danas
    jlrZiro.requestFocus();
  }

  void setDefBrojIzv() {
    jraBrojizvfrom.getDataSet().setInt(jraBrojizvfrom.getColumnName(), 0);
    jraBrojizvto.getDataSet().setInt(jraBrojizvto.getColumnName(), 99999);
  }

  public boolean Validacija() {
    if (vl.isEmpty(jlrZiro))
      return false;
    if (!findCVRNAL())
      return false;
    return true;
  }

  boolean posZiro() {
    return lookupData.getlookupData().raLocate(
        dm.getZirorn(),
        new String[] { "CORG", "ZIRO" },
        new String[] { hr.restart.zapod.OrgStr.getKNJCORG(false),
            getSelRow().getString("ZIRO") });
  }

  boolean findCVRNAL() {
    boolean checkCVRNAL = false;
    boolean checkZIRO = posZiro();
    if (checkZIRO) {
      checkCVRNAL = lookupData.getlookupData().raLocate(dm.getVrstenaloga(),
          new String[] { "CVRNAL" },
          new String[] { dm.getZirorn().getString("CVRNAL") });
    }
    if (checkCVRNAL) {
      return true;
    } else {
      JOptionPane
          .showMessageDialog(getPreSelDialog(),
              "Žiro ra\u010Dun zadan u predselekciji nema upisanu ispravnu vrstu naloga!");
      return false;
    }
  }

  private void jbInit() throws Exception {
    setSelDataSet(dm.getIzvodi());
    jpDetail.setLayout(lay);
    lay.setWidth(391);
    lay.setHeight(110);
    jlBrojizvfrom.setText("Broj izvoda (od - do)");
    jlDatumfrom.setText("Datum (od - do)");
    jlZiro.setText("Žiro ra\u010Dun");
    jraBrojizvfrom.setColumnName("BROJIZV");
    jraBrojizvfrom.setDataSet(getSelDataSet());
    jraBrojizvto.setColumnName("BROJIZV");
    jraBrojizvto.setDataSet(getSelDataSet());
    jraDatumfrom.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatumfrom.setColumnName("DATUM");
    jraDatumfrom.setDataSet(getSelDataSet());
    jraDatumto.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatumto.setColumnName("DATUM");
    jraDatumto.setDataSet(getSelDataSet());
    jlrZiro.setColumnName("ZIRO");
    jlrZiro.setDataSet(getSelDataSet());
    jlrZiro.setVisCols(new int[] { 1 });
    jlrZiro.setSearchMode(3);
    //    jlrZiro.setRaDataSet(dm.getZirorn());
    jlrZiro.setRaDataSet(OrgStr.getOrgStr().getKnjigziro(
        OrgStr.getKNJCORG(false)));
    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(
        new hr.restart.zapod.raKnjigChangeListener() {
          public void knjigChanged(String oo, String no) {
            jlrZiro.setRaDataSet(OrgStr.getOrgStr().getKnjigziro(
                OrgStr.getKNJCORG(false)));
          }
        });
    jlrZiro.setNavButton(jbSelZiro);
    jtKNJIG.setColumnName("KNJIG");
    jtKNJIG.setDataSet(getSelDataSet());
    jtKNJIG.setVisible(false);
    jtKNJIG.setEnabled(false);
    jpDetail.add(jtKNJIG, new XYConstraints(0, 0, 0, 0));
    jpDetail.add(jbSelZiro, new XYConstraints(360, 20, 21, 21));
    jpDetail.add(jlBrojizvfrom, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlDatumfrom, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlZiro, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlrZiro, new XYConstraints(150, 20, 205, -1));
    jpDetail.add(jraBrojizvfrom, new XYConstraints(150, 70, 100, -1));
    jpDetail.add(jraBrojizvto, new XYConstraints(255, 70, 100, -1));
    jpDetail.add(jraDatumfrom, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jraDatumto, new XYConstraints(255, 45, 100, -1));
    addSelRange(jraDatumfrom, jraDatumto);
    addSelRange(jraBrojizvfrom, jraBrojizvto);
    setSelPanel(jpDetail);
    installResetButton();
  }
}