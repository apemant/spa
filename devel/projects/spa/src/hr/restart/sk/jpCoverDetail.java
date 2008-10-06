/****license*****************************************************************
**   file: jpCoverDetail.java
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
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
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


public class jpCoverDetail extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlBrojdok = new JLabel();
  JLabel jlCorg = new JLabel();
  JLabel jlCpar = new JLabel();
  JLabel jlDatdok = new JLabel();
  JLabel jlOpis = new JLabel();
  JLabel jlSaldo = new JLabel();
  JLabel jlaDatdok = new JLabel();
  JLabel jlaDatdosp = new JLabel();
  JLabel jlaDatknj = new JLabel();
  JLabel jlaRsaldo = new JLabel();
  JLabel jlaSaldo = new JLabel();
  JLabel jlaSsaldo = new JLabel();
  JraButton jbSelCorg = new JraButton();
  JraButton jbSelCpar = new JraButton();
  JraTextField jraBrojdok = new JraTextField();
  JraTextField jraDatdok = new JraTextField();
  JraTextField jraDatdosp = new JraTextField();
  JraTextField jraDatknj = new JraTextField();
  JraTextField jraExtbrojdok = new JraTextField();
  JraTextField jraOpis = new JraTextField();
  JraTextField jraRsaldo = new JraTextField();
  JraTextField jraSaldo = new JraTextField();
  JraTextField jraSsaldo = new JraTextField();
  JlrNavField jlrCpar = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazpar = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCorg = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpCoverDetail() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void BindComponents(DataSet ds) {
    jraBrojdok.setDataSet(ds);
    jraDatdok.setDataSet(ds);
    jraDatdosp.setDataSet(ds);
    jraDatknj.setDataSet(ds);
    jraExtbrojdok.setDataSet(ds);
    jraOpis.setDataSet(ds);
    jraRsaldo.setDataSet(ds);
    jraSaldo.setDataSet(ds);
    jraSsaldo.setDataSet(ds);
    jlrCpar.setDataSet(ds);
    jlrCorg.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(646);
    lay.setHeight(245);

    jbSelCorg.setText("...");
    jbSelCpar.setText("...");
    jlBrojdok.setText("Broj dokumenta");
    jlCorg.setText("Org. jedinica");
    jlCpar.setText("Partner");
    jlDatdok.setText("Datum");
    jlOpis.setText("Opis");
    jlSaldo.setText("Iznos");
    jlaDatdok.setHorizontalAlignment(SwingConstants.CENTER);
    jlaDatdok.setText("Dokumenta");
    jlaDatdosp.setHorizontalAlignment(SwingConstants.CENTER);
    jlaDatdosp.setText("Dospje\u0107a");
    jlaDatknj.setHorizontalAlignment(SwingConstants.CENTER);
    jlaDatknj.setText("Primitka");
    jlaRsaldo.setHorizontalAlignment(SwingConstants.TRAILING);
    jlaRsaldo.setText("Pokriveno");
    jlaSaldo.setHorizontalAlignment(SwingConstants.TRAILING);
    jlaSaldo.setText("Saldo");
    jlaSsaldo.setHorizontalAlignment(SwingConstants.TRAILING);
    jlaSsaldo.setText("Nepokriveno");
    jraBrojdok.setColumnName("BROJDOK");
    jraDatdok.setColumnName("DATDOK");
    jraDatdosp.setColumnName("DATDOSP");
    jraDatknj.setColumnName("DATKNJ");
    jraExtbrojdok.setColumnName("EXTBROJDOK");
    jraOpis.setColumnName("OPIS");
    jraRsaldo.setColumnName("RSALDO");
    jraSaldo.setColumnName("SALDO");
    jraSsaldo.setColumnName("SSALDO");

    jlrCpar.setColumnName("CPAR");
    jlrCpar.setColNames(new String[] {"NAZPAR"});
    jlrCpar.setTextFields(new JTextComponent[] {jlrNazpar});
    jlrCpar.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCpar.setSearchMode(0);
    jlrCpar.setRaDataSet(dm.getPartneri());
    jlrCpar.setNavButton(jbSelCpar);

    jlrNazpar.setColumnName("NAZPAR");
    jlrNazpar.setNavProperties(jlrCpar);
    jlrNazpar.setSearchMode(1);

    jlrCorg.setColumnName("CORG");
    jlrCorg.setColNames(new String[] {"NAZIV"});
    jlrCorg.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCorg.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCorg.setSearchMode(0);
    jlrCorg.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jlrCorg.setNavButton(jbSelCorg);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCorg);
    jlrNaziv.setSearchMode(1);

    jpDetail.add(jbSelCorg, new XYConstraints(610, 20, 21, 21));
    jpDetail.add(jbSelCpar, new XYConstraints(610, 45, 21, 21));
    jpDetail.add(jlBrojdok, new XYConstraints(15, 115, -1, -1));
    jpDetail.add(jlCorg, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlCpar, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlDatdok, new XYConstraints(15, 90, -1, -1));
    jpDetail.add(jlOpis, new XYConstraints(15, 160, -1, -1));
    jpDetail.add(jlSaldo, new XYConstraints(15, 205, -1, -1));
    jpDetail.add(jlaDatdok, new XYConstraints(151, 73, 98, -1));
    jpDetail.add(jlaDatdosp, new XYConstraints(361, 73, 98, -1));
    jpDetail.add(jlaDatknj, new XYConstraints(256, 73, 98, -1));
    jpDetail.add(jlaRsaldo, new XYConstraints(256, 188, 98, -1));
    jpDetail.add(jlaSaldo, new XYConstraints(151, 188, 98, -1));
    jpDetail.add(jlaSsaldo, new XYConstraints(361, 188, 98, -1));
    jpDetail.add(jlrCorg, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jlrCpar, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(255, 20, 350, -1));
    jpDetail.add(jlrNazpar, new XYConstraints(255, 45, 350, -1));
    jpDetail.add(jraBrojdok, new XYConstraints(150, 115, 310, -1));
    jpDetail.add(jraDatdok, new XYConstraints(150, 90, 100, -1));
    jpDetail.add(jraDatdosp, new XYConstraints(360, 90, 100, -1));
    jpDetail.add(jraDatknj, new XYConstraints(255, 90, 100, -1));
    jpDetail.add(jraExtbrojdok, new XYConstraints(465, 115, 140, -1));
    jpDetail.add(jraOpis, new XYConstraints(150, 160, 455, -1));
    jpDetail.add(jraRsaldo, new XYConstraints(255, 205, 100, -1));
    jpDetail.add(jraSaldo, new XYConstraints(150, 205, 100, -1));
    jpDetail.add(jraSsaldo, new XYConstraints(360, 205, 100, -1));

    OrgStr.getOrgStr().addKnjigChangeListener(new raKnjigChangeListener() {
      public void knjigChanged(String oldk, String newk) {
        jlrCorg.setRaDataSet(OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
      }
    });
//    BindComponents(fCover.getDetailSet());
    /**@todo: Odkomentirati sljedeæu liniju :) */
    this.add(jpDetail, BorderLayout.CENTER);
  }
}
