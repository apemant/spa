/****license*****************************************************************
**   file: FrmPartneriTelemarketeri.java
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
import hr.restart.baza.Telehist;
import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCpar;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raTransaction;

import java.sql.Timestamp;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class FrmPartneriTelemarketeri extends raMatPodaci {
  
  private jpCpar partnerPanel = new jpCpar() {
    public void afterLookUp(boolean succ) {
      if (succ) {
        System.out.println("uspjesno lukapan :)"); //XDEBUG delete when no more needed
      }
    }
  };
  private JraTextField jraDatumOd = new JraTextField();
  private JraTextField jraDatumDo = new JraTextField();
  
  JPanel jp = new JPanel();
  XYLayout myXyLayout = new XYLayout();
  
  dM dm;
  Valid vl;
  
  QueryDataSet telehist;
  int ctel;
  
  public FrmPartneriTelemarketeri(QueryDataSet telehist, int ctel){
    super(2);
    try {
      dm = dM.getDataModule();
      vl = Valid.getValid();
      this.telehist = telehist;
      this.ctel = ctel;
      initializer();
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public void SetFokus(char mode) {
    if (mode == 'N'){
      this.getRaQueryDataSet().setTimestamp("DATUMOD", vl.getToday());
      partnerPanel.cpar.emptyTextFields();
      partnerPanel.cpar.requestFocusLater();
      raCommonClass.getraCommonClass().setLabelLaF(jraDatumDo,false);
    } else if (mode == 'I'){
      raCommonClass.getraCommonClass().EnabDisabAll(partnerPanel,false);
      raCommonClass.getraCommonClass().setLabelLaF(jraDatumDo,false);
      jraDatumDo.requestFocusLater();
    }
  }
  
  private void initializer() throws Exception {
    this.setRaQueryDataSet(telehist);
    this.setVisibleCols(new int[] {1,2,3});

    jp.setLayout(myXyLayout);
    
    myXyLayout.setWidth(640);
    myXyLayout.setHeight(145);

    partnerPanel.bind(getRaQueryDataSet());

    jraDatumOd.setColumnName("DATUMOD");
    jraDatumOd.setDataSet(getRaQueryDataSet());
    
    jraDatumDo.setColumnName("DATUMDO");
    jraDatumDo.setDataSet(getRaQueryDataSet());
    
    jp.add(partnerPanel, new XYConstraints(0, 32, 630, 77));
    
    jp.add(new JLabel("Period"),  new XYConstraints(15, 57, 100, -1));
    jp.add(jraDatumOd,  new XYConstraints(150, 55, 100, -1));
    jp.add(jraDatumDo,  new XYConstraints(255, 55, 100, -1));
    
    this.setRaDetailPanel(jp);
    raTableColumnModifier TCM = new raTableColumnModifier(
        "CPAR",
        new String[] {"CPAR", "NAZPAR"}, 
        new String[] {"CPAR"},
        dm.getPartneri()
    ) {
      public int getMaxModifiedTextLength() {
        return 27;
      }
    };
    this.getJpTableView().addTableModifier(TCM);
  }
  
  public boolean Validacija(char mode) {
    if (mode == 'N'){
      if (vl.isEmpty(partnerPanel.cpar)) return false;
      int chist = Util.getUtil().getNewQueryDataSet("select max(CHIST) as chist from telehist").getInt("CHIST")+1;
      System.out.println("najveci broj u telehistu = "+chist); //XDEBUG delete when no more needed
      this.getRaQueryDataSet().setInt("CHIST",chist);
      this.getRaQueryDataSet().setInt("CTEL",ctel);
    }
    
    System.out.println(this.getRaQueryDataSet()); //XDEBUG delete when no more needed
    
    return checkAndUpdate(mode);
  }
  
  int delCpar;
  
  public boolean BeforeDelete() {
    delCpar = this.getRaQueryDataSet().getInt("CPAR");
    return true;
  }

  public void AfterDelete() {
    QueryDataSet ds = Telehist.getDataModule().getTempSet(Condition.equal("CPAR", delCpar));
    ds.open();
    boolean ima = (ds.rowCount() > 0);
    if (ima) {
      ds.setSort(new SortDescriptor(new String[] {"DATUMOD"}));
      ds.last();
      ds.setTimestamp("DATUMDO", null);
    }
    if (ima) raTransaction.saveChanges(ds);
  }


  public boolean checkAndUpdate(char mode) {
    QueryDataSet ds = Telehist.getDataModule().getTempSet(Condition.equal("CPAR", getRaQueryDataSet().getInt("CPAR")));
    ds.open();
    Timestamp now = Util.getUtil().getCurrentDatabaseTime();
    boolean ima = (ds.rowCount() > 0);
    if (ima) {
      ds.setSort(new SortDescriptor(new String[] {"DATUMOD"}));
      ds.last();
      ds.setTimestamp("DATUMDO", getRaQueryDataSet().getTimestamp("DATUMOD"));
    }
    if (ima) raTransaction.saveChanges(ds);
    return true;
  }
  
}
