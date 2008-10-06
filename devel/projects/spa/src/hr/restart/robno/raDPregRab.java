/****license*****************************************************************
**   file: raDPregRab.java
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
package hr.restart.robno;

import hr.restart.swing.JraDialog;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Point;

import javax.swing.JPanel;

import com.borland.jbcl.layout.XYLayout;

public class raDPregRab extends JraDialog {

  JPanel panel1 = new JPanel();
  JPanel panel2 = new JPanel();
  XYLayout xyl = new XYLayout();
  raFilteriRazni rFR = new raFilteriRazni();
  raFilteriRazni.fVshrab_rab filter;
  raExtraDBManipulation rEDBN = new raExtraDBManipulation();
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  com.borland.dx.dataset.DataSetView dsw ;

  hr.restart.util.OKpanel okp = new hr.restart.util.OKpanel(){
  public void jBOK_actionPerformed(){
    pressOK();
  }
  public void jPrekid_actionPerformed(){
    pressCancel();
  }
  };
  public void pressOK(){
    dm.getvshrab_rab().removeRowFilterListener(filter);
    this.hide();
  }
  public void pressCancel(){
      dm.getvshrab_rab().removeRowFilterListener(filter);
    this.hide();
  }
  BorderLayout borderLayout1 = new BorderLayout();
  hr.restart.util.raJPTableView rjp = new hr.restart.util.raJPTableView();

  public raDPregRab(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    filter = rFR.getfVshrab_rab();
    try {
    dm.getVtrabat().open();
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public raDPregRab() {
    this(null, "", false);
  }

  void jbInit() throws Exception {

    dm.getvshrab_rab().open();
    rjp.setDataSet(dm.getvshrab_rab());
    rjp.setVisibleCols(new int[] {0,1,3,4});
    rjp.initKeyListener(this);
    panel1.setLayout(borderLayout1);
    getContentPane().add(panel1);
    panel1.add(rjp,BorderLayout.CENTER);
    panel1.add(okp,BorderLayout.SOUTH);
  }
  public void setUvjet(String pero) {

    filter.setCriteria(pero);
    try {
      dm.getvshrab_rab().removeRowFilterListener(filter);
      dm.getvshrab_rab().addRowFilterListener(filter);
    } catch (Exception e) {}
    dm.getvshrab_rab().refilter();

  }

  public void show() {
    Point rv = new Point();
    this.getParent().getLocation(rv);
    rv.x=(int )rv.getX()+this.getParent().getHeight();
    rv.y=(int )rv.getY()+50;
    this.setLocation(rv);
    rjp.getColumnsBean().eventInit();
    ((hr.restart.swing.JraTable2)rjp.getMpTable()).fireTableDataChanged(); /// da nema praznih kolona
    super.show();
  }
}