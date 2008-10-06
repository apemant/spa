/****license*****************************************************************
**   file: frmVerifyAmor.java
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
package hr.restart.os;

import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;
import hr.restart.util.raNavBar;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class frmVerifyAmor extends raMatPodaci {
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private JPanel jp = new JPanel();
  private XYLayout xYLayout1 = new XYLayout();
  private JLabel jLabel1 = new JLabel();
  private JraTextField jtfDATOD = new JraTextField();
  private JraTextField jtfDATDO = new JraTextField();
  private JlrNavField jrfNAZORG = new JlrNavField();
  private JLabel jlInvBr = new JLabel();
  private JlrNavField jrfCORG = new JlrNavField();

  public frmVerifyAmor() {
    super(2);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public void SetFokus(char parm1) {
  }
  public boolean Validacija(char parm1) {
    return true;
  }
  private void jbInit() throws Exception {
    this.setRaQueryDataSet(dm.getOS_Log());
    this.setRaDetailPanel(jp);
    this.setVisibleCols(new int[] {0,2,3,4});
    getJpTableView().getNavBar().removeStandardOptions(new int[] {raNavBar.ACTION_ADD, raNavBar.ACTION_UPDATE, raNavBar.ACTION_DELETE});
    getJpTableView().getNavBar().addOption(new raNavAction("Potvrda", raImages.IMGCOMPOSEMAIL, KeyEvent.VK_F6) {
      public void actionPerformed(ActionEvent e) {
        verifyMe();
      }
    });


    jLabel1.setText("Datum (od - do)");
    jp.setLayout(xYLayout1);

    jrfCORG.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndCurrKnjig());
    jrfCORG.setVisCols(new int[]{0,1});
    jrfCORG.setColNames(new String[] {"NAZIV"});
    jrfCORG.setTextFields(new javax.swing.text.JTextComponent[] {jrfNAZORG});
    jrfCORG.setDataSet(dm.getOS_Log());
    jrfCORG.setColumnName("CORG");
    jrfCORG.setNavColumnName("CORG");
    jrfNAZORG.setColumnName("NAZIV");
    jrfNAZORG.setNavProperties(jrfCORG);
    jrfNAZORG.setSearchMode(1);
    jlInvBr.setText("Org. jedinica");
    jtfDATOD.setDataSet(dm.getOS_Log());
    jtfDATOD.setColumnName("DATOD");
    jtfDATDO.setDataSet(dm.getOS_Log());
    jtfDATDO.setColumnName("DATDO");
    xYLayout1.setWidth(570);
    xYLayout1.setHeight(85);
    jp.add(jLabel1, new XYConstraints(15, 45, -1, -1));
    jp.add(jtfDATOD, new XYConstraints(150, 45, 100, -1));
    jp.add(jtfDATDO, new XYConstraints(255, 45, 100, -1));
    jp.add(jrfNAZORG,  new XYConstraints(255, 20, 300, -1));
    jp.add(jlInvBr,  new XYConstraints(15, 20, -1, -1));
    jp.add(jrfCORG,  new XYConstraints(150, 20, 100, -1));
  }
  private void verifyMe() {
    System.out.println("verifajmeeeeeeeeee");
    if (getRaQueryDataSet().getRowCount()==0) {
      JOptionPane.showConfirmDialog(jp,"Nema izvršenih obraèuna !", "Greška", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
      return;
    }
    else if (getRaQueryDataSet().getString("STATKNJ").equals("V")) {
      JOptionPane.showConfirmDialog(jp,"Obraèun je veæ potvrðen !", "Greška", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (JOptionPane.showConfirmDialog(jp, "Želite li potvrditi obraèun ?","Potvrda obraèuna",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {
      return;
    }
    getRaQueryDataSet().setString("STATKNJ", "V");
    getRaQueryDataSet().saveChanges();
    getRaQueryDataSet().refresh();
  }
}