/****license*****************************************************************
**   file: jpReplurl.java
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
import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.IntParam;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpReplurl extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  IntParam param = new IntParam();

  frmReplurl fReplurl;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlDriver = new JLabel();
  JLabel jlPass = new JLabel();
  JLabel jlRbr_url = new JLabel();
  JLabel jlUrl = new JLabel();
  JLabel jlUsr = new JLabel();
  public JraComboBox jcbDriver = new JraComboBox();
  JraTextField jraPass = new JraTextField();
  JraTextField jraRbr_url = new JraTextField();
  JraComboBox jcbUrl = new JraComboBox();
  JraTextField jraUsr = new JraTextField();

  public jpReplurl(frmReplurl f) {
    try {
      fReplurl = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(525);
    lay.setHeight(160);

    jlDriver.setText("Driver");
    jlPass.setText("Password");
    jlRbr_url.setText("Index");
    jlUrl.setText("URL");
    jlUsr.setText("User");
//    rcbDriver.setRaColumn("DRIVER");
//    rcbDriver.setRaDataSet(fReplurl.getRaQueryDataSet());
    jraPass.setColumnName("PASS");
    jraPass.setDataSet(fReplurl.getRaQueryDataSet());

    jraRbr_url.setColumnName("RBR_URL");
    jraRbr_url.setDataSet(fReplurl.getRaQueryDataSet());
//    rcbUrl.setRaColumn("URL");
//    rcbUrl.setRaDataSet(fReplurl.getRaQueryDataSet());
    jraUsr.setColumnName("USR");
    jraUsr.setDataSet(fReplurl.getRaQueryDataSet());


    jpDetail.add(jlDriver, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlPass, new XYConstraints(15, 120, -1, -1));
    jpDetail.add(jlRbr_url, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlUrl, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlUsr, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jcbDriver,     new XYConstraints(150, 70, 360, -1));
    jpDetail.add(jraPass,     new XYConstraints(150, 120, 360, -1));
    jpDetail.add(jraRbr_url, new XYConstraints(150, 20, 50, -1));
    jpDetail.add(jcbUrl,     new XYConstraints(150, 45, 360, -1));
    jpDetail.add(jraUsr,     new XYConstraints(150, 95, 360, -1));

    jcbUrl.insertItemAt(param.URL,0);
    int i=1;
    String Sadrzaj="-1";
    String[][]url = new String[][]{};
    while (param.VratiSadrzajTaga("url"+i) != "")
    {
      Sadrzaj = param.VratiSadrzajTaga("url"+i);
      if (!Sadrzaj.equals(param.URL))
        jcbUrl.addItem(Sadrzaj);
      i++;
    }
    jcbUrl.setSelectedIndex(0);

    jcbDriver.insertItemAt(param.TIP,0);
    /// Tip Drivera
    i=1 ;
    Sadrzaj="-1";

   do {
      Sadrzaj = param.VratiSadrzajTaga("tip"+i);
      if (!Sadrzaj.equals(param.TIP)){
        jcbDriver.addItem(Sadrzaj);
        }
      i++;
    }while (param.VratiSadrzajTaga("tip"+i) != "");

    jcbDriver.setSelectedIndex(0);


    jcbDriver.setEditable(true);
    jcbUrl.setEditable(true);

    this.add(jpDetail, BorderLayout.CENTER);
  }




}
