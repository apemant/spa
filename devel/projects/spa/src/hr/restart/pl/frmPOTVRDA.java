/****license*****************************************************************
**   file: frmPOTVRDA.java
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

import hr.restart.baza.Radnici;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitLite;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmPOTVRDA extends raUpitLite {

  dM dm = dM.getDataModule();
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
//  hr.restart.zapod.OrgStr orgs;
  Util ut = Util.getUtil();

  JPanel mainPanel = new JPanel();
  private JPanel jPanel1 = new JPanel();
  private XYLayout xYLayout1 = new XYLayout();
  private BorderLayout borderLayout1 = new BorderLayout();

  StorageDataSet fieldSet = new StorageDataSet();
  QueryDataSet repSet = new QueryDataSet();
  QueryDataSet knjigovodstvo;
  QueryDataSet corgradnici = new QueryDataSet();
  String oldcorg = "";

  JLabel jlCorg = new JLabel();
  JLabel jlGodina = new JLabel();
  JLabel jlCvro = new JLabel();
  JLabel jlRadnik = new JLabel();

  JlrNavField jlrCorg = new JlrNavField() {
    public void after_lookUp() {aft_lookCorg();}
  };
  JlrNavField jlrNazorg = new JlrNavField();
  JlrNavField jlrCvro = new JlrNavField();
  JlrNavField jlrNazvro = new JlrNavField();
  JraTextField jraGodina = new JraTextField();
  JlrNavField jlrCradnik = new JlrNavField();
  JlrNavField jlrPrezime = new JlrNavField();
  JlrNavField jlrIme = new JlrNavField();

  JraButton jbSelCorg = new JraButton();
  JraButton jbSelVrod = new JraButton();
  JraButton jbSelCradnik = new JraButton();

  static frmPOTVRDA instanceOfMe;

  public frmPOTVRDA() {
    try {
      jbInit();
      instanceOfMe = this;
    }
    catch (Exception ex) {

    }
  }

  public static frmPOTVRDA getInstance(){
    return instanceOfMe;
  }

  public void componentShow() {
    fieldSet.emptyAllRows();

    try {
      fieldSet.setString("CORG", hr.restart.zapod.OrgStr.getKNJCORG());
      jlrCorg.forceFocLost();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    fieldSet.setString("GODINA",(Integer.parseInt(ut.getYear(vl.getToday()))-1)+"");

    jlrCorg.requestFocus();
    jlrCorg.selectAll();
  }

  public void okPress() {
    System.out.println("OK");
    repSet = ut.getNewQueryDataSet(getQstr(fieldSet.getString("CORG"),
                                           fieldSet.getString("GODINA"),
                                           fieldSet.getString("CVRO"),
                                           fieldSet.getString("CRADNIK"))
    );
  }

  public boolean isIspis() {
    return false;
  }
  public boolean ispisNow() {
    return true;
  }

  public void firstESC() {
    rcc.EnabDisabAll(jPanel1, true);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jlrCorg.requestFocus();
      }
    });
  }

  public boolean runFirstESC() {
//    if (!fieldSet.getString("CRADNIK").equals("")) return true;
    return false;
  }

  public String getQstr(String CORG, String GODINA, String CVRO, String CRADNIK){
    String cvro /*= "";
    if (!CVRO.equals("")) cvro */= "and kumulradarh.cvro = '"+CVRO+"' ";

    String qds = "SELECT "+

    "kumulorgarh.datumispl, "+
                 "kumulradarh.godobr, "+
                 "kumulradarh.cradnik, "+
                 "kumulradarh.cvro, "+
                 "kumulradarh.bruto, "+
                 "kumulradarh.poruk, "+
                 "kumulradarh.prir, "+
                 "kumulradarh.poriprir, "+
                 "kumulradarh.doprinosi, "+
                 "kumulradarh.netopk "+

    "FROM Kumulorgarh, Kumulradarh "+

    "WHERE kumulorgarh.godobr = kumulradarh.godobr "+
                 "AND kumulorgarh.mjobr = kumulradarh.mjobr "+
                 "AND kumulorgarh.rbrobr = kumulradarh.rbrobr "+
                 "AND kumulradarh.cvro = kumulorgarh.cvro "+
                 "AND kumulradarh.corg = kumulorgarh.corg "+
                 "and kumulradarh.corg = '"+CORG+"' "+
                 cvro+ //"and kumulradarh.cvro = '"+CVRO+"' "+
                 "and kumulradarh.cradnik = '"+CRADNIK+"' "+
                 "and kumulradarh.godobr = '"+GODINA+"' "+

    " order by cradnik, datumispl";

    System.out.println("\n\n"+qds+"\n\n");

    return qds;
  }

  public QueryDataSet getRepSet(){
    return repSet;
  }

  public String getCorg(){
    return fieldSet.getString("CORG");
  }

  private void jbInit() throws Exception {
    this.addReport("hr.restart.pl.repPorezPotvrda", "hr.restart.pl.repPorezPotvrda", "PorezPotvrda", "Potvrda");
    try {
      fieldSet.setColumns(new Column[] {
        dm.createStringColumn("CORG","Org. jedinica",5),
        dm.createStringColumn("GODINA","Godina",4),
        dm.createStringColumn("CVRO","Vrsta radnog odnosa",5),
        dm.createStringColumn("CRADNIK","Radnik",5)
      });
      fieldSet.open();
    }
    catch (Exception ex) {
    }

    jlCorg.setText("Org. jedinica");
    jlGodina.setText("Godina");
    jlCvro.setText("Vrsta radnog odnosa");
    jlRadnik.setText("Radnik");
/**/
    jlrCradnik.setHorizontalAlignment(SwingConstants.LEFT);
    jlrCradnik.setColumnName("CRADNIK");
    jlrCradnik.setDataSet(fieldSet);
    jlrCradnik.setColNames(new String[] {"IME", "PREZIME"});
    jlrCradnik.setTextFields(new javax.swing.text.JTextComponent[] {jlrIme, jlrPrezime});
    jlrCradnik.setVisCols(new int[] {0, 1, 2});
    jlrCradnik.setSearchMode(0);
    jlrCradnik.setRaDataSet(corgradnici);
    jlrCradnik.setNavButton(jbSelCradnik);

    jlrIme.setSearchMode(1);
    jlrIme.setNavProperties(jlrCradnik);
    jlrIme.setColumnName("IME");

    jlrPrezime.setSearchMode(1);
    jlrPrezime.setNavProperties(jlrCradnik);
    jlrPrezime.setColumnName("PREZIME");/**/

    jlrCorg.setColumnName("CORG");
    jlrCorg.setDataSet(fieldSet);
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazorg});
    jlrCorg.setVisCols(new int[] {0, 1, 2});
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.setNavButton(jbSelCorg);

    jlrNazorg.setSearchMode(1);
    jlrNazorg.setNavProperties(jlrCorg);
    jlrNazorg.setColumnName("NAZIV");

    jlrCvro.setColumnName("CVRO");
    jlrCvro.setDataSet(fieldSet);
    jlrCvro.setColNames(new String[] {"NAZIVRO"});
    jlrCvro.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazvro});
    jlrCvro.setVisCols(new int[] {0,1});
    jlrCvro.setRaDataSet(dm.getVrodn());
    jlrCvro.setNavButton(jbSelVrod);

    jlrNazvro.setSearchMode(1);
    jlrNazvro.setNavProperties(jlrCvro);
    jlrNazvro.setColumnName("NAZIVRO");

    jraGodina.setDataSet(fieldSet);
    jraGodina.setColumnName("GODINA");
    jraGodina.setHorizontalAlignment(SwingConstants.CENTER);

    mainPanel.setLayout(borderLayout1);
    this.setJPan(mainPanel);
    jPanel1.setLayout(xYLayout1);
    xYLayout1.setHeight(125);
    xYLayout1.setWidth(600);
    mainPanel.add(jPanel1, BorderLayout.CENTER);


    jPanel1.add(jlCorg, new XYConstraints(15,15,-1,-1));
    jPanel1.add(jlrCorg, new XYConstraints(150, 15, 100, -1));
    jPanel1.add(jlrNazorg, new XYConstraints(255, 15, 300, -1));
    jPanel1.add(jbSelCorg, new XYConstraints(560, 15, 21, 21));

    jPanel1.add(jlGodina, new XYConstraints(15,90,-1,-1));
    jPanel1.add(jraGodina, new XYConstraints(150, 90, 100, -1));

    jPanel1.add(jlCvro, new XYConstraints(15,40,-1,-1));
    jPanel1.add(jlrCvro, new XYConstraints(150, 40, 100, -1));
    jPanel1.add(jlrNazvro, new XYConstraints(255, 40, 300, -1));
    jPanel1.add(jbSelVrod, new XYConstraints(560, 40, 21, 21));

    jPanel1.add(jlRadnik, new XYConstraints(15,65,-1,-1));
    jPanel1.add(jlrCradnik, new XYConstraints(150, 65, 100, -1));
    jPanel1.add(jlrIme, new XYConstraints(410,65,145,-1));
    jPanel1.add(jlrPrezime, new XYConstraints(255,65,150,-1));
    jPanel1.add(jbSelCradnik, new XYConstraints(560, 65, 21, 21));

    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnj, String newKnj){
        jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      }
    });
  }

  public boolean Validacija(){
    return (!hr.restart.util.Valid.getValid().isEmpty(jlrCorg) &&
            !hr.restart.util.Valid.getValid().isEmpty(jlrCvro) &&
            !hr.restart.util.Valid.getValid().isEmpty(jlrCradnik) &&
            !hr.restart.util.Valid.getValid().isEmpty(jraGodina));
  }

  public void aft_lookCorg() {
    if (!fieldSet.getString("CORG").equals(oldcorg)) {
      jlrCradnik.setText("");
      jlrCradnik.emptyTextFields();
      oldcorg = fieldSet.getString("CORG");
      corgradnici.close();
      Radnici.getDataModule().setFilter(corgradnici,
        "corg in" + hr.restart.zapod.OrgStr.getOrgStr().getInQuery(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(oldcorg))
      );
      corgradnici.open();
    }
  }
}