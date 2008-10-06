/****license*****************************************************************
**   file: presPrimanja.java
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
package hr.restart.pl;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.util.JlrNavField;
import hr.restart.util.PreSelect;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raAdditionalLookupFilter;
import hr.restart.util.raCommonClass;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class presPrimanja extends PreSelect {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCorg = new JLabel();
  JLabel jlCradnik = new JLabel();
  JraButton jbSelCorg = new JraButton();
  JraButton jbSelCradnik = new JraButton();
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
      jlrCorg_after_lookUp();
    }
  };
  JlrNavField jlrIme = new JlrNavField() {
    public void after_lookUp() {
      jlrCradnik_after_lookup();
    }
  };
  JlrNavField jlrCradnik = new JlrNavField() {
    public void after_lookUp() {
      jlrCradnik_after_lookup();
    }
  };
  JlrNavField jlrPrezime = new JlrNavField() {
    public void after_lookUp() {
      jlrCradnik_after_lookup();
    }
  };
  JlrNavField jlrCorg = new JlrNavField() {
    public void after_lookUp() {
      jlrCorg_after_lookUp();
    }
  };
  raAdditionalLookupFilter radniciplLookupFilter = new raAdditionalLookupFilter() {
    public boolean isRow(com.borland.dx.dataset.ReadRow row) {
System.out.println("trazim za "+row.getString("CRADNIK"));
      boolean isRPL = lookupData.getlookupData().raLocate(
        dm.getRadnicipl(),new String[] {"CRADNIK"},new String[] {row.getString("CRADNIK")});
System.out.println("Nasao?..."+isRPL);
      return isRPL;
    }
  };
  public presPrimanja() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void SetFokus() {
    System.out.println("setFokus");
    getSelDataSet().setString("CRADNIK","");
    jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.forceFocLost();
    jlrCradnik.setRaDataSet(hr.restart.zapod.raRadnici.getRadniciFromKnjig(hr.restart.zapod.OrgStr.getKNJCORG()));
    jlrIme.setRaDataSet(hr.restart.zapod.raRadnici.getRadniciFromKnjig(hr.restart.zapod.OrgStr.getKNJCORG()));
    jlrPrezime.setRaDataSet(hr.restart.zapod.raRadnici.getRadniciFromKnjig(hr.restart.zapod.OrgStr.getKNJCORG()));
    jlrCorg.requestFocus();
  }

  public boolean Validacija() {
    if (vl.isEmpty(jlrCorg))
      return false;
    return true;
  }

  private void jbInit() throws Exception {
    this.setSelDataSet(dm.getRadnicipl());
    jpDetail.setLayout(lay);
    lay.setWidth(601);
    lay.setHeight(85);

    jbSelCorg.setText("...");
    jbSelCradnik.setText("...");
    jlCorg.setText("Org. jedinica");
    jlCradnik.setText("Radnik");

    jlrCradnik.setColumnName("CRADNIK");
    jlrCradnik.setDataSet(getSelDataSet());
    jlrCradnik.setColNames(new String[] {"IME", "PREZIME"});
    jlrCradnik.setTextFields(new JTextComponent[] {jlrIme, jlrPrezime});
    jlrCradnik.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCradnik.setSearchMode(0);
    jlrCradnik.setRaDataSet(hr.restart.zapod.raRadnici.getRadniciFromCurrentKnjig());
    jlrCradnik.setNavButton(jbSelCradnik);
//    jlrCradnik.setAdditionalLookupFilter(radniciplLookupFilter);

    jlrIme.setColumnName("IME");
    jlrIme.setNavProperties(jlrCradnik);
    jlrIme.setSearchMode(1);

    jlrPrezime.setColumnName("PREZIME");
    jlrPrezime.setNavProperties(jlrCradnik);
    jlrPrezime.setSearchMode(1);

    jlrCorg.setColumnName("CORG");
    jlrCorg.setDataSet(getSelDataSet());
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCorg.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.setNavButton(jbSelCorg);
    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(
        new hr.restart.zapod.raKnjigChangeListener() {
      public void knjigChanged(String novi, String stari) {
        System.out.println("knhjig chage listener");
        jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
        getSelDataSet().setString("CORG","");
        jlrCradnik.setRaDataSet(hr.restart.zapod.raRadnici.getRadniciFromKnjig(getSelDataSet().getString("CORG")));
        jlrIme.setRaDataSet(hr.restart.zapod.raRadnici.getRadniciFromKnjig(getSelDataSet().getString("CORG")));
        jlrPrezime.setRaDataSet(hr.restart.zapod.raRadnici.getRadniciFromKnjig(getSelDataSet().getString("CORG")));
      };
    }
        );

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCorg);
    jlrNaziv.setSearchMode(1);

    jpDetail.add(jbSelCorg, new XYConstraints(565, 20, 21, 21));
    jpDetail.add(jbSelCradnik, new XYConstraints(565, 45, 21, 21));
    jpDetail.add(jlCorg, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlCradnik, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlrCorg, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jlrCradnik, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jlrPrezime, new XYConstraints(255, 45, 200, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(255, 20, 305, -1));
    jpDetail.add(jlrIme, new XYConstraints(460, 45, 100, -1));

    this.setSelPanel(jpDetail);
  }
  public boolean applySQLFilter() {
    QueryDataSet myDataSet=(QueryDataSet) getSelDataSet();
    myDataSet.close();
    myDataSet.setQuery(new QueryDescriptor(myDataSet.getDatabase(), sjQuerys.selectRadniciPl(getSelRow().getString("CORG"), getSelRow().getString("CRADNIK"))));
    myDataSet.open();
    return true;
  }
  void jlrCorg_after_lookUp() {
    if (!doCorg_after_lookUp) {
      doCorg_after_lookUp = true;
      return;
    }
    jlrCradnik.getDataSet().setString(jlrCradnik.getColumnName(), "");
    jlrCradnik.forceFocLost();
/*    System.out.println("hr.restart.zapod.raRadnici.getRadniciFromKnjig("+getSelDataSet().getString("CORG"));
      jlrCradnik.setRaDataSet(hr.restart.zapod.raRadnici.getRadniciFromKnjig(getSelDataSet().getString("CORG")));
      jlrIme.setRaDataSet(hr.restart.zapod.raRadnici.getRadniciFromKnjig(getSelDataSet().getString("CORG")));
      jlrPrezime.setRaDataSet(hr.restart.zapod.raRadnici.getRadniciFromKnjig(getSelDataSet().getString("CORG")));*/
  }
  void jlrCradnik_after_lookup() {
    String _cradnik = jlrCradnik.getDataSet().getString(jlrCradnik.getColumnName());
    if (_cradnik.equals("")) return;
    doCorg_after_lookUp = false;
    if (lookupData.getlookupData().raLocate(dm.getRadnici(), "CRADNIK", _cradnik)) {
      jlrCorg.getDataSet().setString(jlrCorg.getColumnName(), dm.getRadnici().getString("CORG"));
    }
    jlrCorg.forceFocLost();
  }
  boolean doCorg_after_lookUp = true;
}
