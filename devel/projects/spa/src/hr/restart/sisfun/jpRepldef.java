/****license*****************************************************************
**   file: jpRepldef.java
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
package hr.restart.sisfun;

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpRepldef extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmRepldef fRepldef;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlBatch_index = new JLabel();
  JLabel jlImetab = new JLabel();
  JLabel jlNacinrep = new JLabel();
  JLabel jlRbr_url = new JLabel();
  JLabel jlRbr_url2 = new JLabel();
  JLabel jlSlijed = new JLabel();
  JLabel jlaRbr_url = new JLabel();
  JLabel jlaUrl = new JLabel();
  JraButton jbSelRbr_url = new JraButton();
  JraButton jbSelRbr_url2 = new JraButton();
  JraTextField jraBatch_index = new JraTextField();
  JraTextField jraImetab = new JraTextField();
  raComboBox rcbNacinrep = new raComboBox();
  JraTextField jraSlijed = new JraTextField();
  JlrNavField jlrUrl2 = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrUrl = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrRbr_url = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrRbr_url2 = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpRepldef(frmRepldef f) {
    try {
      fRepldef = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(555);
    lay.setHeight(165);

    jbSelRbr_url.setText("...");
    jbSelRbr_url2.setText("...");
    jlBatch_index.setText("Identifikator");
    jlImetab.setText("Naziv tablice");
    jlNacinrep.setText("Na\u010Din rep.");
    jlRbr_url.setText("URL iz");
    jlRbr_url2.setText("URL u");
    jlSlijed.setText("Redoslijed");
    jlaRbr_url.setHorizontalAlignment(SwingConstants.LEFT);
    jlaRbr_url.setText("Index");
    jlaUrl.setHorizontalAlignment(SwingConstants.LEFT);
    jlaUrl.setText("Url");
    jraBatch_index.setColumnName("BATCH_INDEX");
    jraBatch_index.setDataSet(fRepldef.getRaQueryDataSet());
    jraImetab.setColumnName("IMETAB");
    jraImetab.setDataSet(fRepldef.getRaQueryDataSet());
    rcbNacinrep.setRaColumn("NACINREP");
    rcbNacinrep.setRaDataSet(fRepldef.getRaQueryDataSet());
    rcbNacinrep.setRaItems(new String[][]{
      {"Rijetko promjenjivi mat. pod","1"},
      {"\u010Cesto promjenjivi mat. pod","2"},
      {"Prometni podaci","3"},
    });
    jraSlijed.setColumnName("SLIJED");
    jraSlijed.setDataSet(fRepldef.getRaQueryDataSet());

    jlrRbr_url.setColumnName("RBR_URL");

    jlrRbr_url.setDataSet(fRepldef.getRaQueryDataSet());
    jlrRbr_url.setColNames(new String[] {"URL"});
    jlrRbr_url.setTextFields(new JTextComponent[] {jlrUrl});
    jlrRbr_url.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrRbr_url.setSearchMode(0);
    jlrRbr_url.setRaDataSet(dm.getReplurl());
    jlrRbr_url.setNavButton(jbSelRbr_url);

    jlrUrl.setColumnName("URL");
    jlrUrl.setNavProperties(jlrRbr_url);
    jlrUrl.setSearchMode(1);

    jlrRbr_url2.setColumnName("RBR_URL_U");
    jlrRbr_url2.setNavColumnName("RBR_URL");
    jlrRbr_url2.setDataSet(fRepldef.getRaQueryDataSet());
    jlrRbr_url2.setColNames(new String[] {"URL"});
    jlrRbr_url2.setTextFields(new JTextComponent[] {jlrUrl2});
    jlrRbr_url2.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrRbr_url2.setSearchMode(0);
    jlrRbr_url2.setRaDataSet(dm.getReplurl());
    jlrRbr_url2.setNavButton(jbSelRbr_url2);

    jlrUrl2.setColumnName("URL");
    jlrUrl2.setNavProperties(jlrRbr_url2);
    jlrUrl2.setSearchMode(1);

    jpDetail.add(jbSelRbr_url,   new XYConstraints(510, 65, 21, 21));
    jpDetail.add(jbSelRbr_url2,    new XYConstraints(510, 90, 21, 21));
    jpDetail.add(jlBatch_index, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlRbr_url,  new XYConstraints(15, 65, -1, -1));
    jpDetail.add(jlRbr_url2,    new XYConstraints(15, 90, -1, -1));
    jpDetail.add(jlSlijed,    new XYConstraints(15, 115, -1, -1));
    jpDetail.add(jlaRbr_url,   new XYConstraints(151, 43, 98, -1));
    jpDetail.add(jlaUrl,   new XYConstraints(235, 43, 298, -1));
    jpDetail.add(jlrRbr_url,    new XYConstraints(150, 65, 80, -1));
    jpDetail.add(jlrRbr_url2,    new XYConstraints(150, 90, 80, -1));
    jpDetail.add(jlrUrl,     new XYConstraints(235, 65, 270, -1));
    jpDetail.add(jlrUrl2,       new XYConstraints(235, 90, 270, -1));
    jpDetail.add(jraBatch_index,  new XYConstraints(150, 20, 80, -1));
    jpDetail.add(jraSlijed,      new XYConstraints(150, 115, 80, -1));
    jpDetail.add(jraImetab,  new XYConstraints(405, 20, -1, -1));
    jpDetail.add(jlImetab,  new XYConstraints(315, 20, -1, -1));
    jpDetail.add(rcbNacinrep,       new XYConstraints(310, 117, 195, -1));
    jpDetail.add(jlNacinrep,    new XYConstraints(235, 120, -1, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
