/****license*****************************************************************
**   file: presZbirno.java
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

import hr.restart.baza.Skstavke;
import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpZupGrad;

import java.math.BigDecimal;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;


public class presZbirno extends presCommonSk {

/*  JLabel jlMjesto = new JLabel();
  JLabel jlZup = new JLabel();
  JraTextField jraMjesto = new JraTextField() {
    public boolean isFocusTraversable() {
      return jpp.getCpar() == 0;
    }
  };

  JraButton jbSelZup = new JraButton();
  JlrNavField jlrZup = new JlrNavField() {
    public boolean isFocusTraversable() {
      return jraMjesto.getText().length() == 0 && jpp.getCpar() == 0;
    }
  };

  JlrNavField jlrNazivZup = new JlrNavField() {
    public boolean isFocusTraversable() {
      return jraMjesto.getText().length() == 0 && jpp.getCpar() == 0;
    }
  }; */

  JraTextField jraDatumfrom = new JraTextField();
  JraTextField jraDatumto = new JraTextField();

  jpZupGrad jpzg = new jpZupGrad(100, 290) {
    public void afterLookUp(boolean succ) {
      if (succ) {
        jpp.init();
        focusAfterZup();
      }
    }
  };
  
  boolean firstReset = false;

  StorageDataSet gmsz = new StorageDataSet();
  Column GOD;
  Column MJESTO;
  Column ZUP;
  Column AGENT;
  Column CGR;
  Column MIZ;

  public presZbirno() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  protected void focusAfterZup() {
    jraDatumfrom.requestFocus();
  }

  public void resetDefaults() {
    gmsz.setString("GOD", vl.findYear(vl.getToday()));
    jpzg.init();
    if (jpp.isCombo()) jpp.setKupci(true);
    jpk.setKontaAllow(false);
    jpp.init();
    jpc.init();
  }

  public void SetFokus() {
    getSelRow().setString("KNJIG", hr.restart.zapod.OrgStr.getKNJCORG(false));
    if (!firstReset) {
      firstReset = true;
      resetDefaults();
    }
    if (jpp.isCombo()) jpp.setKupci(jpp.isKupci());

/*    jlrCorg.setText(hr.restart.zapod.OrgStr.getKNJCORG(false));
    jlrCorg.forceFocLost(); */
    jpp.focusCombo();
//    jpp.setSkipCpar();
  }

  public String getMjesto() {
    return jpzg.getGrad();
  }

  public String getZup() {
    return jpzg.getZupanija();
  }
  
  public String getAgent() {
    return jpzg.getAgent();
  }
  
  public String getGrupa() {
    return jpzg.getGrupa();
  }

  public boolean checkPartner(ReadRow par) {
    return jpzg.checkPartner(par);
  }
  
  public boolean checkPartner(int agent, short zup, int pbr, String gr) {
    return jpzg.checkPartner(agent, zup, pbr, gr);
  }

  public String getGodina() {
    return gmsz.getString("GOD");
  }

  public BigDecimal getIznos() {
    return gmsz.getBigDecimal("MINIZNOS");
  }

  private void jbInit() throws Exception {
     setSelDataSet(Skstavke.getDataModule().getTempSet("1=0"));
//    jlMjesto.setText("Mjesto");
//    jlZup.setText("Županija")7

    GOD = (Column) dm.getDoku().getColumn("GOD").clone();
    MJESTO = (Column) dm.getPartneri().getColumn("PBR").clone();
    AGENT = (Column) dm.getPartneri().getColumn("CAGENT").clone();
    CGR = (Column) dm.getPartneri().getColumn("CGRPAR").clone();
    ZUP = (Column) dm.getZupanije().getColumn("CZUP").clone();
    MIZ = dM.createBigDecimalColumn("MINIZNOS", 2);    
    gmsz.setColumns(new Column[] {GOD, MJESTO, ZUP, MIZ, AGENT, CGR});

//    jraMjesto.setColumnName("MJ");
//    jraMjesto.setDataSet(gmsz);

    jraDatumfrom.setColumnName("DATUMKNJ");
    jraDatumfrom.setDataSet(getSelDataSet());
    jraDatumto.setColumnName("DATUMKNJ");
    jraDatumto.setDataSet(getSelDataSet());

    jpzg.bind(gmsz);

//    jlrZup.setColumnName("CZUP");
//    jlrZup.setDataSet(gmsz);
//    jlrZup.setColNames(new String[] {"NAZIVZUP"});
//    jlrZup.setTextFields(new JTextComponent[] {jlrNazivZup});
//    jlrZup.setVisCols(new int[] {0, 1});
//    jlrZup.setSearchMode(0);
//    jlrZup.setRaDataSet(dm.getZupanije());
//    jlrZup.setNavButton(jbSelZup);
//
//    jlrNazivZup.setColumnName("NAZIVZUP");
//    jlrNazivZup.setNavProperties(jlrZup);
//    jlrNazivZup.setSearchMode(1);

//    jpDetail.add(jlMjesto, new XYConstraints(15, 72, -1, -1));
//    jpDetail.add(jraMjesto, new XYConstraints(150, 70, 100, -1));
//    jpDetail.add(jlZup, new XYConstraints(275, 72, -1, -1));
//    jpDetail.add(jlrZup, new XYConstraints(340, 70, 50, -1));
//    jpDetail.add(jlrNazivZup, new XYConstraints(395, 70, 150, -1));
//    jpDetail.add(jbSelZup, new XYConstraints(550, 70, 21, 21));
    jpDetail.add(jpzg, new XYConstraints(0, 70 + dkAdd, -1, -1));
  }
  
  protected void afterPartner(boolean succ) {
    if (succ) {
      jpzg.init();
      jpzg.focusNav();
    }
  }

  public boolean isZup() {
    return jpzg.isZup();
  }

   public boolean isGrad() {
     return jpzg.isGrad();
   }
   
   public boolean isAgent() {
     return jpzg.isAgent();
   }
   
   public boolean isGrupa() {
     return jpzg.isGrupa();
   }
   
   public boolean applySQLFilter() {
     return true;
   }
}
