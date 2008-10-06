/****license*****************************************************************
**   file: jpMxDocument.java
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
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpMxDocument extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmMxDocument fMxDocument;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCdoc = new JLabel();
  JLabel jlLinespacing = new JLabel();
  JLabel jlPagewidth = new JLabel();
  JLabel jlTopmargin_mm = new JLabel();
  JLabel jlUser1 = new JLabel();
  JLabel jlUser11 = new JLabel();
  JLabel jlUser12 = new JLabel();
  JLabel jlUser13 = new JLabel();
  JLabel jlUser2 = new JLabel();
  JLabel jlUser21 = new JLabel();
  JLabel jlUser22 = new JLabel();
  JLabel jlUser23 = new JLabel();
  JLabel jlaLeftmargin = new JLabel();
  JLabel jlaPagesize = new JLabel();
  JLabel jlaPagewidth = new JLabel();
  JLabel jlaTopmargin_mm = new JLabel();
  JraCheckBox jcbEjectpaper = new JraCheckBox();
  JraTextField jraCdoc = new JraTextField();
  JraTextField jraLeftmargin = new JraTextField();
  JraTextField jraLinespacing = new JraTextField();
  JraTextField jraOpis = new JraTextField();
  JraTextField jraPagesize = new JraTextField();
  JraTextField jraPagewidth = new JraTextField();
  JraTextField jraTopmargin_mm = new JraTextField();
  JraTextField jraUser1 = new JraTextField();
  JraTextField jraUser11 = new JraTextField();
  JraTextField jraUser12 = new JraTextField();
  JraTextField jraUser13 = new JraTextField();
  JraTextField jraUser2 = new JraTextField();
  JraTextField jraUser21 = new JraTextField();
  JraTextField jraUser22 = new JraTextField();
  JraTextField jraUser23 = new JraTextField();

  public jpMxDocument(frmMxDocument f) {
    try {
      fMxDocument = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void BindComponents(DataSet ds) {
    jcbEjectpaper.setDataSet(ds);
    jraCdoc.setDataSet(ds);
    jraLeftmargin.setDataSet(ds);
    jraLinespacing.setDataSet(ds);
    jraOpis.setDataSet(ds);
    jraPagesize.setDataSet(ds);
    jraPagewidth.setDataSet(ds);
    jraTopmargin_mm.setDataSet(ds);
    jraUser1.setDataSet(ds);
    jraUser11.setDataSet(ds);
    jraUser12.setDataSet(ds);
    jraUser13.setDataSet(ds);
    jraUser2.setDataSet(ds);
    jraUser21.setDataSet(ds);
    jraUser22.setDataSet(ds);
    jraUser23.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(615);
    lay.setHeight(230);

    jlCdoc.setText("Dokument");
    jlLinespacing.setText("Razmak redova");
    jlPagewidth.setText("Dimenzije");
    jlTopmargin_mm.setText("Margine");
    jlUser1.setText("Naredba 1");
    jlUser11.setText("Naredba 3");
    jlUser12.setText("Naredba 5");
    jlUser13.setText("Naredba 7");
    jlUser2.setText("Naredba 2");
    jlUser21.setText("Naredba 4");
    jlUser22.setText("Naredba 6");
    jlUser23.setText("Naredba 8");
    jlaLeftmargin.setHorizontalAlignment(SwingConstants.CENTER);
    jlaLeftmargin.setText("Lijeva");
    jlaPagesize.setHorizontalAlignment(SwingConstants.CENTER);
    jlaPagesize.setText("Dužina");
    jlaPagewidth.setHorizontalAlignment(SwingConstants.CENTER);
    jlaPagewidth.setText("Širina");
    jlaTopmargin_mm.setHorizontalAlignment(SwingConstants.CENTER);
    jlaTopmargin_mm.setText("Gornja u mm");
    jcbEjectpaper.setColumnName("EJECTPAPER");
    jcbEjectpaper.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbEjectpaper.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbEjectpaper.setSelectedDataValue("D");
    jcbEjectpaper.setText("Izbaci stranicu");
    jcbEjectpaper.setUnselectedDataValue("N"); /**@todo: Definirati vrijednosti checkboxa */
    jraCdoc.setColumnName("CDOC");
    jraLeftmargin.setColumnName("LEFTMARGIN");
    jraLinespacing.setColumnName("LINESPACING");
    jraOpis.setColumnName("OPIS");
    jraPagesize.setColumnName("PAGESIZE");
    jraPagewidth.setColumnName("PAGEWIDTH");
    jraTopmargin_mm.setColumnName("TOPMARGIN_MM");
    jraUser1.setColumnName("USER1");
    jraUser11.setColumnName("USER1");
    jraUser12.setColumnName("USER1");
    jraUser13.setColumnName("USER1");
    jraUser2.setColumnName("USER2");
    jraUser21.setColumnName("USER2");
    jraUser22.setColumnName("USER2");
    jraUser23.setColumnName("USER2");

    jpDetail.add(jcbEjectpaper, new XYConstraints(490, 90, 110, -1));
    jpDetail.add(jlCdoc, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlLinespacing, new XYConstraints(15, 90, -1, -1));
    jpDetail.add(jlPagewidth, new XYConstraints(15, 65, -1, -1));
    jpDetail.add(jlTopmargin_mm, new XYConstraints(355, 65, -1, -1));
    jpDetail.add(jlUser1, new XYConstraints(15, 115, -1, -1));
    jpDetail.add(jlUser11, new XYConstraints(15, 140, -1, -1));
    jpDetail.add(jlUser12, new XYConstraints(15, 165, -1, -1));
    jpDetail.add(jlUser13, new XYConstraints(15, 190, -1, -1));
    jpDetail.add(jlUser2, new XYConstraints(355, 115, -1, -1));
    jpDetail.add(jlUser21, new XYConstraints(355, 140, -1, -1));
    jpDetail.add(jlUser22, new XYConstraints(355, 165, -1, -1));
    jpDetail.add(jlUser23, new XYConstraints(355, 190, -1, -1));
    jpDetail.add(jlaLeftmargin, new XYConstraints(526, 48, 73, -1));
    jpDetail.add(jlaPagesize, new XYConstraints(231, 48, 73, -1));
    jpDetail.add(jlaPagewidth, new XYConstraints(151, 48, 73, -1));
    jpDetail.add(jlaTopmargin_mm, new XYConstraints(446, 48, 73, -1));
    jpDetail.add(jraCdoc, new XYConstraints(150, 20, 75, -1));
    jpDetail.add(jraLeftmargin, new XYConstraints(525, 65, 75, -1));
    jpDetail.add(jraLinespacing, new XYConstraints(150, 90, 155, -1));
    jpDetail.add(jraOpis, new XYConstraints(230, 20, 370, -1));
    jpDetail.add(jraPagesize, new XYConstraints(230, 65, 75, -1));
    jpDetail.add(jraPagewidth, new XYConstraints(150, 65, 75, -1));
    jpDetail.add(jraTopmargin_mm, new XYConstraints(445, 65, 75, -1));
    jpDetail.add(jraUser1, new XYConstraints(150, 115, 155, -1));
    jpDetail.add(jraUser11, new XYConstraints(150, 140, 155, -1));
    jpDetail.add(jraUser12, new XYConstraints(150, 165, 155, -1));
    jpDetail.add(jraUser13, new XYConstraints(150, 190, 155, -1));
    jpDetail.add(jraUser2, new XYConstraints(445, 115, 155, -1));
    jpDetail.add(jraUser21, new XYConstraints(445, 140, 155, -1));
    jpDetail.add(jraUser22, new XYConstraints(445, 165, 155, -1));
    jpDetail.add(jraUser23, new XYConstraints(445, 190, 155, -1));

    /**@todo: Odkomentirati sljedeæu liniju :) */
    this.add(jpDetail, BorderLayout.CENTER);
  }
}
