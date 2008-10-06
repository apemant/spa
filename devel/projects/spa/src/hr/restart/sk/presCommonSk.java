/****license*****************************************************************
**   file: presCommonSk.java
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
package hr.restart.sk;

import hr.restart.baza.Condition;
import hr.restart.baza.Konta;
import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCorg;
import hr.restart.swing.jpCpar;
import hr.restart.util.Aus;
import hr.restart.util.PreSelect;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import javax.swing.JPanel;

import com.borland.dx.dataset.DataRow;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class presCommonSk extends PreSelect {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();

  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();

  JraTextField jraKnjig = new JraTextField();
  
  protected int dkAdd;

  public jpCorg jpc = new jpCorg(100, 290, true) {
    public void afterLookUp(boolean succ) {
      if (succ) jpp.focusCpar();
    }
  };
  public jpCpar jpp = createPartner();
  
  public jpSelKonto jpk = new jpSelKonto(100, 290, true) {
    public void afterLookUp(boolean succ) {
      afterKonto(succ);
    }
  };

  /*  StorageDataSet corgs = new StorageDataSet();
  QueryDataSet empty = new QueryDataSet() {
    public boolean saveChangesSupported() {
      return false;
    }
    public boolean open() {
      return true;
    }
    public void refresh() {
    }
  };*/

  public presCommonSk() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  protected boolean isBoth() {
    return true;
  }
  
  protected boolean isAll() {
    return false;
  }
  
  protected boolean isDirect() {
    return raSaldaKonti.isDirect();
  }
  
  protected jpCpar createPartner() {
    return new jpCpar(100, 290, isBoth(), isAll()) {
      public void afterLookUp(boolean succ) {
        afterPartner(succ);
      }
      protected void kupSelected() {
        super.kupSelected();
        if (isDirect()) checkKupDob(true);
      }
      protected void dobSelected() {
        super.dobSelected();
        if (isDirect()) checkKupDob(false);
      }
    };
  }

  public String getKonto() {
    return isDirect() ? jpk.getKonto() : null;
  }
  
  public String getCorg() {
    return jpc.getCorg();
  }
    
  private void jbInit() throws Exception {
    /*Skstavke.getDataModule().setFilter(empty, "");
    this.setSelDataSet(empty);
    corgs.setColumns(new Column[] {
      (Column) dm.getSkstavke().getColumn("CORG").clone()
    });
    corgs.getColumn("CORG").setColumnName("MYCORG");
    corgs.open();*/
    dkAdd = isDirect() ? 25 : 0;
    
    jpDetail.setLayout(lay);
    lay.setWidth(570);
    lay.setHeight(120 + dkAdd);

    jraKnjig.setColumnName("KNJIG");
    jraKnjig.setDataSet(getSelDataSet());
    jraKnjig.setVisible(false);
    jraKnjig.setEnabled(false);

    jpc.bind(getSelDataSet());
    jpp.bind(getSelDataSet());
    jpk.bind(getSelDataSet());
    jpk.setKontaSet(Konta.getDataModule().getFilteredDataSet(Condition.in("SALDAK", "K D")));

    jpDetail.add(jpc, new XYConstraints(0, 20, -1, -1));    
    jpDetail.add(jpp, new XYConstraints(0, 45 + dkAdd, -1, -1));
    jpDetail.add(jraKnjig, new XYConstraints(5, 70 + dkAdd, 5, -1));
    if (isDirect()) 
      jpDetail.add(jpk, new XYConstraints(0, 45, -1, -1));
    installResetButton();
  }
  
  public Condition getPresCondition() {
    return Condition.equal("KNJIG", getSelRow()).and(jpc.getOptCondition()).and(
        isDirect() ? jpk.getCondition() : Condition.none);
  }
  
  public boolean applySQLFilter() {
    QueryDataSet selQDS = (QueryDataSet) getSelDataSet();    
    Aus.setFilter(selQDS, Valid.getValid().getNoWhereQuery(selQDS)+ " WHERE " + getPresCondition());
    selQDS.open();
    return true;
  }

  protected void afterKonto(boolean succ) {
    DataRow konto = !succ ? null : jpk.getKontoRow();
    if (konto != null && isBoth()) {
      if (konto.getString("SALDAK").equalsIgnoreCase("D") && jpp.isKupci()) 
        jpp.setKupci(false);
      else if (konto.getString("SALDAK").equalsIgnoreCase("K") && !jpp.isKupci())
        jpp.setKupci(true);
    }
    if (succ) jpp.focusCparLater();
  }
  
  protected void afterPartner(boolean succ) {
    
  }
  
  void checkKupDob(boolean kupac) {
    DataRow konto = jpk.getKontoRow();
    if (konto != null) {
      if (konto.getString("SALDAK").equalsIgnoreCase("D") && jpp.isKupci())
        jpk.clear();
      else if (konto.getString("SALDAK").equalsIgnoreCase("K") && !jpp.isKupci())
        jpk.clear();
    }
  }

  /*protected void kupSelected() {
    jlrCpar.setRaDataSet(dm.getPartneriKup());
    jlrCpar.forceFocLost();
  }

  protected void dobSelected() {
    jlrCpar.setRaDataSet(dm.getPartneriDob());
    jlrCpar.forceFocLost();
  }*/
}
