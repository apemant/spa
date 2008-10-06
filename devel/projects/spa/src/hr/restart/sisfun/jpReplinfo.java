/****license*****************************************************************
**   file: jpReplinfo.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpReplinfo extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmReplinfo fReplinfo;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlDatprom = new JLabel();
  JLabel jlImetab = new JLabel();
  JLabel jlKeytab = new JLabel();
  JLabel jlRbr_url = new JLabel();
  JLabel jlRep_flag = new JLabel();
  JraTextField jraDatprom = new JraTextField();
  JraTextField jraImetab = new JraTextField();
  JraTextField jraKeytab = new JraTextField();
  JraTextField jraRbr_url = new JraTextField();
  JraTextField jraRep_flag = new JraTextField();
  private JLabel jlTablica = new JLabel();

  public jpReplinfo(frmReplinfo f) {
    try {
      fReplinfo = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(455);
    lay.setHeight(160);

    jlDatprom.setText("Datum promjene");
    jlImetab.setText("Naziv");
    jlKeytab.setText("Klju\u010D");
    jlRbr_url.setText("Index remote URL - a");
    jlRep_flag.setText("Status replikacije");
    jraDatprom.setColumnName("DATPROM");
    jraDatprom.setDataSet(fReplinfo.getRaQueryDataSet());
    jraImetab.setColumnName("IMETAB");
    jraImetab.setDataSet(fReplinfo.getRaQueryDataSet());
    jraKeytab.setColumnName("KEYTAB");
    jraKeytab.setDataSet(fReplinfo.getRaQueryDataSet());
    jraRbr_url.setColumnName("RBR_URL");
    jraRbr_url.setDataSet(fReplinfo.getRaQueryDataSet());
    jraRep_flag.setColumnName("REP_FLAG");
    jraRep_flag.setDataSet(fReplinfo.getRaQueryDataSet());

    jlTablica.setText("Tablica");
    jpDetail.add(jlDatprom,  new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jlRbr_url,  new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlRep_flag,  new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jraDatprom,  new XYConstraints(150, 95, 100, -1));
    jpDetail.add(jraRbr_url,  new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jraRep_flag,  new XYConstraints(150, 70, 100, -1));
    jpDetail.add(jlImetab,   new XYConstraints(150, 3, -1, -1));
    jpDetail.add(jlTablica,  new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jraImetab,  new XYConstraints(150, 20, -1, -1));
    jpDetail.add(jraKeytab,   new XYConstraints(255, 20, 200, -1));
    jpDetail.add(jlKeytab, new XYConstraints(254, 2, -1, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
