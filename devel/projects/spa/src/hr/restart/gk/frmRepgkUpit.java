/****license*****************************************************************
**   file: frmRepgkUpit.java
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
/*
 * Created on Sep 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.gk;

import hr.restart.baza.Orgstruktura;
import hr.restart.swing.jpCorg;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitFat;

import javax.swing.JPanel;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * @author andrej
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class frmRepgkUpit extends raUpitFat {
  JPanel content = new JPanel();
  jpCorg jpc = new jpCorg(350,true);
  jpGkMonthRange jpr = new jpGkMonthRange();
  jpGetRepGK jpg = new jpGetRepGK();
  private StorageDataSet setjpc;
  private boolean esc1;
  public frmRepgkUpit() {
    dsInit();
    juInit();
  }
  /**
   * 
   */
  private void dsInit() {
    setjpc = new StorageDataSet();
    setjpc.addColumn(Orgstruktura.getDataModule().getColumn("CORG").cloneColumn());
    jpc.bind(setjpc);
  }
  /**
   * 
   */
  private void juInit() {
    XYLayout lay = new XYLayout();
    content.setLayout(lay);
    content.add(jpg, new XYConstraints(0,15,-1,-1));
    content.add(jpc, new XYConstraints(0,40,-1,-1));
    content.add(jpr, new XYConstraints(0,65,-1,-1));
    setJPan(content);
  }
  /* (non-Javadoc)
   * @see hr.restart.util.raUpitFat#navDoubleClickActionName()
   */
  public String navDoubleClickActionName() {
    // TODO Auto-generated method stub
    return "Izmjena";
  }

  /* (non-Javadoc)
   * @see hr.restart.util.raUpitFat#navVisibleColumns()
   */
  public int[] navVisibleColumns() {
    // TODO Auto-generated method stub
    return new int[] {1,2,3,4,5};
  }

  /* (non-Javadoc)
   * @see hr.restart.util.raUpitLite#okPress()
   */
  public void okPress() {
    RepgkEvaluator r = new RepgkEvaluator();
    r.setCrepgk(jpg.getData().getInt("CREPGK"));
    r.setRange(jpr.getData().getInt("MF"), jpr.getData().getInt("MT"), jpr.getData().getInt("Y"));
    setDataSet(r.evaluate());
    esc1 = true;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.raUpitLite#runFirstESC()
   */
  public boolean runFirstESC() {
    return esc1;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.raUpitLite#firstESC()
   */
  public void firstESC() {
    raCommonClass.getraCommonClass().EnabDisabAll(content,true);
    clearPanel();
    clearTable();
    initFocus();
    esc1 = false;
  }

  /**
   * 
   */
  private void initFocus() {
    jpg.crepgk.requestFocusLater();
  }
  /**
   * 
   */
  private void clearTable() {
    setDataSet(null);
  }
  /**
   * 
   */
  private void clearPanel() {
    setjpc.setString("CORG","");
    jpc.corg.forceFocLost();
    jpg.getData().empty();
    jpg.crepgk.forceFocLost();
    jpr.getData().empty();
  }
  /* (non-Javadoc)
   * @see hr.restart.util.raUpitLite#componentShow()
   */
  public void componentShow() {
    initFocus();
  }

}
