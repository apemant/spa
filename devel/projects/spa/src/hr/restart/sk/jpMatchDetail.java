/****license*****************************************************************
**   file: jpMatchDetail.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.raKnjigChangeListener;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpMatchDetail extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  DataSet bind;

  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlBrojizv = new JLabel();
  JLabel jlCgkstavke = new JLabel();
  JLabel jlCorg = new JLabel();
  JLabel jlDatdosp = new JLabel();
  JLabel jlDatumknj = new JLabel();
  JLabel jlOpis = new JLabel();
  JLabel jlVrdok = new JLabel();
  JLabel jlNacpl = new JLabel();
  JraTextField jraBrojdok = new JraTextField();
  JraTextField jraBrojizv = new JraTextField();
  JraTextField jraCgkstavke = new JraTextField();
  JraTextField jraDatdosp = new JraTextField();
  JraTextField jraDatumknj = new JraTextField();
  JraTextField jraExtbrojdok = new JraTextField();
  JraTextField jraNacpl = new JraTextField();
  JraTextField jraValuta = new JraTextField();
  JraTextField jraOpis = new JraTextField();
  JraTextField jraVrdok = new JraTextField();
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCorg = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpMatchDetail() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void setFieldValues() {
    if (bind.getInt("BROJIZV") == 0)
      jraBrojizv.setText("");
    else jraBrojizv.setText(Integer.toString(bind.getInt("BROJIZV")));
//    if (lookupData.getlookupData().raLocate(dm.getNacpl(), "CNACPL", bind.getString("CNACPL")))
//        jraNacpl.setText(dm.getNacpl().getString("NAZNACPL"));
//    else
//      jraNacpl.setText("");
    if (lookupData.getlookupData().raLocate(dm.getValute(), "OZNVAL", bind.getString("OZNVAL")))
      jraValuta.setText(dm.getValute().getString("NAZVAL"));
    else jraValuta.setText("");
    String vd = bind.getString("VRDOK"), ndok = "";
    if (vd.equalsIgnoreCase("IRN")) ndok = "Izlazni ra\u010Dun";
    else if (vd.equalsIgnoreCase("URN")) ndok = "Ulazni ra\u010Dun";
    else if (vd.equalsIgnoreCase("UPL")) ndok = "Uplata kupca";
    else if (vd.equalsIgnoreCase("IPL")) ndok = "Isplata dobavlja\u010Du";
    else if (vd.equalsIgnoreCase("OKK")) ndok = "Knjižna obavijest";
    else if (vd.equalsIgnoreCase("OKD")) ndok = "Knjižna obavijest";
    jraVrdok.setText(ndok);
  }

  public void BindComponents(DataSet ds) {
    bind = ds;
    jraBrojdok.setDataSet(ds);
//    jraBrojizv.setDataSet(ds);
    jraCgkstavke.setDataSet(ds);
    jraDatdosp.setDataSet(ds);
    jraDatumknj.setDataSet(ds);
    jraExtbrojdok.setDataSet(ds);
    jraNacpl.setDataSet(ds);
    jraOpis.setDataSet(ds);
//    jraVrdok.setDataSet(ds);
    jlrCorg.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(630);
    lay.setHeight(160);

    jlBrojizv.setText("Broj izvoda");
    jlCgkstavke.setText("Broj naloga");
    jlCorg.setText("Org. jedinica");
    jlDatdosp.setText("Datum dospje\u0107a");
    jlDatumknj.setText("Datum knjiženja");
    jlOpis.setText("Opis");
    jlVrdok.setText("Dokument");
    jlNacpl.setText("Iznos u valuti");
    jraBrojdok.setColumnName("BROJDOK");
    jraBrojizv.setColumnName("BROJIZV");
    jraDatdosp.setHorizontalAlignment(SwingConstants.TRAILING);
    jraCgkstavke.setColumnName("CGKSTAVKE");
    jraDatdosp.setColumnName("DATDOSP");
    jraDatdosp.setHorizontalAlignment(SwingConstants.CENTER);
    jraDatumknj.setColumnName("DATUMKNJ");
    jraDatumknj.setHorizontalAlignment(SwingConstants.CENTER);
    jraExtbrojdok.setColumnName("EXTBROJDOK");
    jraNacpl.setColumnName("PVSSALDO");
    jraOpis.setColumnName("OPIS");
    jraVrdok.setColumnName("VRDOK");

    jlrCorg.setColumnName("CORG");
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCorg.setVisCols(new int[] {0, 1});
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCorg);
    jlrNaziv.setSearchMode(1);
    jpDetail.add(jlNacpl, new XYConstraints(5, 112, -1, -1));
    jpDetail.add(jlBrojizv, new XYConstraints(5, 137, -1, -1));
    jpDetail.add(jlCgkstavke, new XYConstraints(5, 87, -1, -1));
    jpDetail.add(jlCorg, new XYConstraints(5, 12, -1, -1));
    jpDetail.add(jlDatdosp, new XYConstraints(400, 112, -1, -1));
    jpDetail.add(jlDatumknj, new XYConstraints(400, 87, -1, -1));
    jpDetail.add(jlOpis, new XYConstraints(5, 62, -1, -1));
    jpDetail.add(jlVrdok, new XYConstraints(5, 37, -1, -1));
    jpDetail.add(jlrCorg, new XYConstraints(140, 10, 110, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(255, 10, 370, -1));
    jpDetail.add(jraBrojdok, new XYConstraints(255, 35, 215, -1));
    jpDetail.add(jraBrojizv, new XYConstraints(140, 135, 50, -1));
    jpDetail.add(jraCgkstavke, new XYConstraints(140, 85, 225, -1));
    jpDetail.add(jraDatdosp, new XYConstraints(525, 110, 100, -1));
    jpDetail.add(jraDatumknj, new XYConstraints(525, 85, 100, -1));
    jpDetail.add(jraExtbrojdok, new XYConstraints(475, 35, 150, -1));
    jpDetail.add(jraNacpl, new XYConstraints(140, 110, 110, -1));
    jpDetail.add(jraValuta, new XYConstraints(255, 110, 110, -1));
    jpDetail.add(jraOpis, new XYConstraints(140, 60, 485, -1));
    jpDetail.add(jraVrdok, new XYConstraints(140, 35, 110, -1));

    OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
      public void knjigChanged(String oldk, String newk) {
        jlrCorg.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      }
    });
    /**@todo: Odkomentirati sljedeæu liniju :) */
    this.add(jpDetail, BorderLayout.CENTER);
  }
}
