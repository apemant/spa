/****license*****************************************************************
**   file: dlgUraIra.java
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
import hr.restart.swing.JraDialog;
import hr.restart.util.JlrNavField;
import hr.restart.util.OKpanel;
import hr.restart.util.raCommonClass;
import hr.restart.util.raMatPodaci;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class dlgUraIra {
  public static final int IRA = 1;
  public static final int URA = 2;

  static dlgUraIra dlg = new dlgUraIra();
  JDialog jd;

  JPanel main = new JPanel();
  JPanel center = new JPanel();
  XYLayout xy = new XYLayout();

  JLabel jlknj = new JLabel();
  JlrNavField cknjige = new JlrNavField();
  JlrNavField nazknjige = new JlrNavField();
  JraButton jbselknj = new JraButton();
  JLabel jlkol = new JLabel();
  JlrNavField ckolone = new JlrNavField();
  JlrNavField nazkolone = new JlrNavField();
  JraButton jbselkol = new JraButton();

  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      cancelPress();
    }
  };

  StorageDataSet fields = new StorageDataSet();
  boolean ok;

  private dlgUraIra() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void open(raMatPodaci owner) {
    dlg.show(owner);
  }

  private void show(raMatPodaci owner) {
    String[] cols = new String[] {"CKNJIGE", "CKOLONE"};
    if (owner.getFrameMode() == owner.DIALOG)
      jd = new JraDialog(owner.getJdialog(), "Podaci za knjigu URA/IRA", true);
    else jd = new JraDialog(owner.getJframe(), "Podaci za knjigu URA/IRA", true);
    jd.getContentPane().add(main);
    okp.registerOKPanelKeys(jd);
    jd.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    jd.addWindowListener(new WindowAdapter() {
      public void windowClosed(WindowEvent e) {
        jd.dispose();
      }
    });
//    fields.setString("CKNJIGE", owner.getRaQueryDataSet().getString("CKNJIGE"));
//    fields.setShort("CKOLONE", owner.getRaQueryDataSet().getShort("CKOLONE"));
//    if (!owner.getRaQueryDataSet().isUnassignedNull("CKOLONE"))
//      fields.setShort("CKOLONE", owner.getRaQueryDataSet().getShort("CKOLONE"));
//    else fields.setUnassignedNull("CKOLONE");
//    DataSet.copyTo(cols, owner.getRaQueryDataSet(), cols, fields);
    dM.copyColumns(owner.getRaQueryDataSet(), fields, cols);
    if (fields.getString("CKNJIGE") == null ||
        fields.getString("CKNJIGE").length() == 0)
      fields.setAssignedNull("CKOLONE");

    cknjige.forceFocLost();
    ckolone.forceFocLost();
    ok = false;
    jd.pack();
    jd.setLocationRelativeTo(jd.getOwner());
    jd.show();
    if (ok) {
//      DataSet.copyTo(cols, fields, cols, owner.getRaQueryDataSet());
//      owner.getRaQueryDataSet().setString("CKNJIGE", fields.getString("CKNJIGE"));
//      if (fields.isuna
//      owner.getRaQueryDataSet().setShort("CKOLONE", fields.getShort("CKOLONE"));
      dM.copyColumns(fields, owner.getRaQueryDataSet(), cols);
    }
  }

  public static void setEnabled(boolean yesno) {
    raCommonClass.getraCommonClass().EnabDisabAll(dlg.main, yesno);
    raCommonClass.getraCommonClass().setLabelLaF(dlg.okp.jPrekid, true);
  }

  public static void setUI(int ui) {
    if (ui == IRA) {
      dlg.cknjige.setRaDataSet(dM.getDataModule().getKnjigeI());
      dlg.ckolone.setRaDataSet(dM.getDataModule().getIzlazneKolone());
    } else {
      dlg.cknjige.setRaDataSet(dM.getDataModule().getKnjigeU());
      dlg.ckolone.setRaDataSet(dM.getDataModule().getUlazneKolone());
    }
  }

  private void OKPress() {
    ok = true;
    jd.dispose();
  }

  private void cancelPress() {
    jd.dispose();
  }

  private void jbInit() throws Exception {
    center.setLayout(xy);
    xy.setWidth(525);
    xy.setHeight(85);

    fields.setColumns(new Column[] {
      dM.createStringColumn("CKNJIGE", "Šifra knjige", 5),
      dM.createStringColumn("NAZKNJIGE", "Naziv knjige", 30),
      dM.createShortColumn("CKOLONE", "Broj kolone"),
      dM.createStringColumn("NAZIVKOLONE", "Naziv kolone", 50)
    });
    fields.open();

    jlknj.setText("Knjiga");
    cknjige.setColumnName("CKNJIGE");
    cknjige.setDataSet(fields);
    cknjige.setColNames(new String[] {"NAZKNJIGE"});
    cknjige.setTextFields(new javax.swing.text.JTextComponent[] {nazknjige});
    cknjige.setVisCols(new int[] {0,4});
    cknjige.setSearchMode(0);
    cknjige.setRaDataSet(dM.getDataModule().getKnjigeUI());
    cknjige.setNavButton(jbselknj);

    jlkol.setText("Kolona knjige");
    nazknjige.setNavProperties(cknjige);
    nazknjige.setDataSet(fields);
    nazknjige.setColumnName("NAZKNJIGE");
    nazknjige.setSearchMode(1);

    ckolone.setColumnName("CKOLONE");
    ckolone.setDataSet(fields);
    ckolone.setColNames(new String[] {"NAZIVKOLONE"});
    ckolone.setTextFields(new javax.swing.text.JTextComponent[] {nazkolone});
    ckolone.setVisCols(new int[] {0,1,2});
    ckolone.setSearchMode(0);
    ckolone.setRaDataSet(dM.getDataModule().getKoloneknjUI());
    ckolone.setNavButton(jbselkol);

    nazkolone.setNavProperties(ckolone);
    nazkolone.setDataSet(fields);
    nazkolone.setColumnName("NAZIVKOLONE");
    nazkolone.setSearchMode(1);

    center.add(jlknj, new XYConstraints(15, 20, -1, -1));
    center.add(cknjige, new XYConstraints(150, 20, 50, -1));
    center.add(nazknjige, new XYConstraints(205, 20, 275, -1));
    center.add(jbselknj, new XYConstraints(485, 20, 21, 21));

    center.add(jlkol, new XYConstraints(15, 45, -1, -1));
    center.add(ckolone, new XYConstraints(150, 45, 50, -1));
    center.add(nazkolone, new XYConstraints(205, 45, 275, -1));
    center.add(jbselkol, new XYConstraints(485, 45, 21, 21));

    main.setLayout(new BorderLayout());
    main.add(center, BorderLayout.CENTER);
    main.add(okp, BorderLayout.SOUTH);
  }
}

