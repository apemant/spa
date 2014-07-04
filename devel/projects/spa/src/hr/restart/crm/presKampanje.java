/****license*****************************************************************
**   file: presKampanje.java
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
package hr.restart.crm;

import hr.restart.baza.Condition;
import hr.restart.baza.Kampanje;
import hr.restart.baza.dM;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCorg;
import hr.restart.util.Aus;
import hr.restart.util.PreSelect;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class presKampanje extends PreSelect {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();

  JPanel jpDetail = new JPanel();

  jpCorg jpc = new jpCorg();

  JLabel jlDatum = new JLabel();
  JraTextField jraDatumOd = new JraTextField();
  JraTextField jraDatumDo = new JraTextField();
  
  public JraCheckBox jcbAktiv = new JraCheckBox();

  public presKampanje() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  public void resetDefaults() {
    jpc.setCorg(raUser.getInstance().getDefCorg());

    getSelRow().setTimestamp("DATPOC-from", ut.getFirstDayOfYear());
    getSelRow().setTimestamp("DATPOC-to", vl.getToday());
    if (isUserEnabled())
      setUserSelected(isUserSelected());
    
    jcbAktiv.setSelected(true);
    
  }

  boolean firstReset = false;
  public void SetFokus() {
    if (!firstReset) {
      firstReset = true;
      resetDefaults();
    }
    jraDatumOd.requestFocus();
  }

  public boolean Validacija() {
    Timestamp begDate = getSelRow().getTimestamp("DATPOC-from");
    Timestamp endDate = getSelRow().getTimestamp("DATPOC-to");
    if (endDate.before(Util.getUtil().getFirstSecondOfDay(begDate))) {
      jraDatumOd.requestFocus();
      JOptionPane.showMessageDialog(jraDatumOd.getTopLevelAncestor(),
        "Poèetni datum je iza završnog!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  private void jbInit() throws Exception {
    this.setSelDataSet(Kampanje.getDataModule().getQueryDataSet());
    Kampanje.getDataModule().setFilter(Condition.nil);

    jpDetail.setLayout(new XYLayout(640, 89));

    jpc.bind(getSelDataSet());

    jlDatum.setText("Datum (od - do)");
    jraDatumOd.setColumnName("DATPOC");
    jraDatumOd.setDataSet(getSelDataSet());
    jraDatumDo.setColumnName("DATPOC");
    jraDatumDo.setDataSet(getSelDataSet());
    
    jcbAktiv.setText(" Samo aktivne kampanje ");
    jcbAktiv.setHorizontalAlignment(JLabel.TRAILING);
    jcbAktiv.setHorizontalTextPosition(JLabel.LEADING);

    jpDetail.add(jpc, new XYConstraints(0, 20, -1, -1));
    jpDetail.add(jlDatum, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jraDatumOd, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jraDatumDo, new XYConstraints(255, 45, 100, -1));
    jpDetail.add(jcbAktiv, new XYConstraints(370, 45, 225, -1));
    
    this.addSelRange(jraDatumOd, jraDatumDo);
    this.setSelPanel(jpDetail);
  }
  
  protected String getMyDocText() {
    return "Moje kampanje";
  }
  
  protected String getAllDocText() {
    return "Sve kampanje";
  }
  
  public boolean applySQLFilter() {
    QueryDataSet selQDS = (QueryDataSet) getSelDataSet();    
    Aus.setFilter(selQDS, Valid.getValid().getNoWhereQuery(selQDS)+ " WHERE " + jpc.getAnyCondition().
        and(jcbAktiv.isSelected() ? Condition.equal("AKTIV", "D") : Condition.none).
        and(isUserQuery() ? Condition.equal("CUSER", raUser.getInstance().getUser()) : Condition.none).
        and(Condition.between("DATPOC", 
            getSelRow().getTimestamp("DATPOC-from"),
            getSelRow().getTimestamp("DATPOC-to"))));
    selQDS.open();
    return true;
  }
}
