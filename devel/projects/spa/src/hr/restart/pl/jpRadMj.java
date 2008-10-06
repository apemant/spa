/****license*****************************************************************
**   file: jpRadMj.java
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
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpRadMj extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmRadMj fRadMj;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlBodovi = new JLabel();
  JLabel jlCradmj = new JLabel();
  JLabel jlDodbod = new JLabel();
  JLabel jlKoef = new JLabel();
  JLabel jlaCradmj = new JLabel();
  JLabel jlaNazivrm = new JLabel();
  JraTextField jraBodovi = new JraTextField();
  JraTextField jraCradmj = new JraTextField();
  JraTextField jraDodbod = new JraTextField();
  JraTextField jraKoef = new JraTextField();
  JraTextField jraNazivrm = new JraTextField();
  JraCheckBox jcbAktiv = new JraCheckBox();

  public jpRadMj(frmRadMj f) {
    try {
      fRadMj = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(575);
    lay.setHeight(135);

    jlBodovi.setText("Bodovi");
    jlCradmj.setText("Radno mjesto");
    jlDodbod.setText("Dodatni bodovi");
    jlKoef.setText("Koeficijent");
    jlaCradmj.setHorizontalAlignment(SwingConstants.LEFT);
    jlaCradmj.setText("Oznaka");
    jlaNazivrm.setHorizontalAlignment(SwingConstants.LEFT);
    jlaNazivrm.setText("Naziv");
    jraBodovi.setColumnName("BODOVI");
    jraBodovi.setDataSet(fRadMj.getRaQueryDataSet());
    jraCradmj.setColumnName("CRADMJ");
    jraCradmj.setDataSet(fRadMj.getRaQueryDataSet());
    jraDodbod.setColumnName("DODBOD");
    jraDodbod.setDataSet(fRadMj.getRaQueryDataSet());
    jraKoef.setColumnName("KOEF");
    jraKoef.setDataSet(fRadMj.getRaQueryDataSet());
    jraNazivrm.setColumnName("NAZIVRM");
    jraNazivrm.setDataSet(fRadMj.getRaQueryDataSet());

    jcbAktiv.setColumnName("AKTIV");
    jcbAktiv.setDataSet(fRadMj.getRaQueryDataSet());
    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktiv.setSelectedDataValue("D");
    jcbAktiv.setText("Aktivan");
    jcbAktiv.setUnselectedDataValue("N");


    jpDetail.add(jcbAktiv,    new XYConstraints(490, 15, 70, -1));
    jpDetail.add(jlCradmj, new XYConstraints(15, 40, -1, -1));
    jpDetail.add(jlDodbod,  new XYConstraints(15, 90, -1, -1));
    jpDetail.add(jlKoef, new XYConstraints(15, 65, -1, -1));
    jpDetail.add(jlaCradmj, new XYConstraints(151, 23, 98, -1));
    jpDetail.add(jlaNazivrm, new XYConstraints(261, 23, 298, -1));
    jpDetail.add(jraCradmj, new XYConstraints(150, 40, 100, -1));
    jpDetail.add(jraDodbod,  new XYConstraints(150, 90, 100, -1));
    jpDetail.add(jraKoef,  new XYConstraints(150, 65, 100, -1));
    jpDetail.add(jraNazivrm, new XYConstraints(260, 40, 300, -1));
    jpDetail.add(jraBodovi,   new XYConstraints(460, 65, 100, -1));
    jpDetail.add(jlBodovi,   new XYConstraints(337, 65, -1, -1));

    this.add(jpDetail, BorderLayout.CENTER);
  }
}
