/****license*****************************************************************
**   file: frmPregledRadnihNaloga.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCustomAttrib;
import hr.restart.swing.raDateRange;
import hr.restart.util.JlrNavField;
import hr.restart.util.OKpanel;
import hr.restart.util.PreSelect;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;
import hr.restart.util.raFrame;
import hr.restart.util.raPreSelectAware;
import hr.restart.util.startFrame;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class frmPregledRadnihNaloga extends raFrame implements raPreSelectAware {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();
  hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();
  frmStatistikaRadnihNaloga stt;

  PreSelect pres;

  jpCustomAttrib jpPodaci = new jpCustomAttrib();
  JPanel jpDetail = new JPanel();

  XYLayout xYLayout1 = new XYLayout();
  JLabel jlPeriod = new JLabel();
  JLabel jlServiser = new JLabel();
  JlrNavField jlrSer = new JlrNavField();
  JlrNavField jlrSerIme = new JlrNavField();
  JlrNavField jlrSerPrezime = new JlrNavField();
  JraButton jbSelSer = new JraButton();
  JraTextField jraDatumOd = new JraTextField();
  JraTextField jraDatumDo = new JraTextField();
  JraTextField jraBroj = new JraTextField();
  raComboBox jcbPeriod = new raComboBox();
  JLabel jlSubj = new JLabel();
  JLabel jlVrsub = new JLabel();
  JLabel jlBroj = new JLabel();

  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      cancelPress();
    }
  };

  StorageDataSet dummy = new StorageDataSet();
  int emptyh;

  public frmPregledRadnihNaloga() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void OKPress() {
    new Thread() {
      public void run() {
        go();
      }
    }.start();
  }

  private void go() {
    VarStr where = new VarStr(32);

    hr.restart.sisfun.raDelayWindow load = hr.restart.sisfun.raDelayWindow.show(this.getContentPane(), 250);
    
    for (int i = 0; i < jpPodaci.getFieldCount(); i++)
      if (!jpPodaci.getFieldValue(i).equals("")) {
        where.append("(RN_znacsub.cznac = ").append(jpPodaci.getCznac(i));
        where.append(" AND RN_znacsub.vriznac != '").append(jpPodaci.getFieldValue(i)).append("') OR ");
      }
    where.chop(4);
    String sb = "";
    if (dummy.getString("BROJ").length() > 0)
      sb = " AND RN_subjekt.broj != '" + dummy.getString("BROJ") + "'"; 
    String csub = "";
    if (where.length() > 0 || sb.length() > 0) {
      if (where.length() > 0) where.insert(0, " AND (").append(")");
      csub = " AND RN.csubrn NOT IN (SELECT DISTINCT(RN_subjekt.csubrn) FROM RN_subjekt, RN_znacsub "+
             "WHERE RN_subjekt.csubrn = RN_znacsub.csubrn AND RN_subjekt.cvrsubj = '" +
             pres.getSelRow().getShort("CVRSUBJ") + "' " + where + sb+")";
    }

    String datum = new String[] {"DATDOK", "DATUMO", "DATUMZ"}[jcbPeriod.getSelectedIndex()];
    String range = " AND RN." + datum + " >= " + rut.getTimestampValue(dummy.getTimestamp("DATUMO"), rut.NUM_FIRST)+
              " AND RN." + datum + " <= " + rut.getTimestampValue(dummy.getTimestamp("DATUMZ"), rut.NUM_LAST);
    if (dummy.getString("CRADNIK").length() > 0)
      range = range + " AND RN.CRADNIK='" + dummy.getString("CRADNIK") + "'";

//    frmStatistikaRadnihNaloga results;
//    results = (frmStatistikaRadnihNaloga) startFrame.getStartFrame().showFrame("hr.restart.robno.frmStatistikaRadnihNaloga", 0, "Rezultati pretrage - " + jlVrsub.getText(), false);
//    results.setSelection(pres.getSelRow().getShort("CVRSUBJ"), range, csub);
    stt.setSelection(this, pres.getSelRow().getShort("CVRSUBJ"), range, csub);
//    stt.pack();
    stt.setTitle("Rezultati pretrage - " + jlVrsub.getText());

    load.close();
    stt.show();

//    results.show();

//    String rn = "SELECT * FROM RN, stdoki WHERE RN.cradnal = stdoki.cradnal AND " + range + csub;
//    System.out.println(rn);
//
//    vl.execSQL("SELECT DISTINCT(RN_subjekt.csubrn) FROM RN_subjekt, RN_znacsub "+
//               "WHERE RN_subjekt.csubrn = RN_znacsub.csubrn AND RN_subjekt.cvrsubj = '" +
//               pres.getSelRow().getShort("CVRSUBJ"));
  }

  private void cancelPress() {
    this.hide();
  }

  public void setPreSelect(PreSelect p) {
    pres = p;
  }

  public PreSelect getPreSelect() {
    return pres;
  }

  public void show() {
    jpPodaci.setFields(pres.getSelRow().getShort("CVRSUBJ"), false);
//    jpPodaci.clearDefaults();
//    System.out.println(jpPodaci.getFieldCount());
    lookupData.getlookupData().raLocate(dm.getRN_vrsub(), 
        "CVRSUBJ", Short.toString(jpPodaci.getCvrsubj()));
    jlBroj.setText(dm.getRN_vrsub().getString("NAZSERBR"));
    xYLayout1.setHeight(emptyh + jpPodaci.getFieldCount() * 25 +
         ((jpPodaci.getFieldCount() > 0) ? 10 : 0));
    jlVrsub.setText(jpPodaci.getVrsub());
    dummy.setTimestamp("DATUMO", hr.restart.util.Util.getUtil().getFirstDayOfYear());
    dummy.setTimestamp("DATUMZ", hr.restart.util.Valid.getValid().getToday());
    pack();
    startFrame.getStartFrame().centerFrame(this, 0, "Statistika radnih naloga");
    super.show();
  }


  private void jbInit() throws Exception {
    dM.getDataModule().loadModules();

    jpPodaci.setTables("RN_vrsub", "RN_znacajke", "RN_znacsub", "RN_sifznac");
    jpPodaci.setVrsubCols("CVRSUBJ", "NAZVRSUBJ", "CPRIP");
    jpPodaci.setAttrCols("CZNAC", "ZNACOPIS", "ZNACTIP", "ZNACREQ", "ZNACSIF");
    jpPodaci.setValueCols("CSUBRN", "VRIZNAC");
    jpPodaci.setSifDesc("OPIS");

    jpDetail.setLayout(xYLayout1);
    xYLayout1.setWidth(500);
    xYLayout1.setHeight(emptyh = 135);
    jlPeriod.setText("Datum (od - do)");
    jlSubj.setText("Vrsta subjekta");
    jlVrsub.setFont(jlVrsub.getFont().deriveFont(java.awt.Font.BOLD));
    jlBroj.setText("S/B");

    dummy.setColumns(new Column[] {
      (Column) dm.getRN().getColumn("DATUMO").clone(),
      (Column) dm.getRN().getColumn("DATUMZ").clone(),
      (Column) dm.getRN().getColumn("STATUS").clone(),
      (Column) dm.getRN_subjekt().getColumn("BROJ").clone(),
      dm.getRN().getColumn("CRADNIK").cloneColumn()
    });

    jcbPeriod.setRaColumn("STATUS");
    jcbPeriod.setRaDataSet(dummy);
    jcbPeriod.setRaItems(new String[][] {
      {"Otvaranja", "P"},
      {"Obra\u010Duna", "O"},
      {"Zatvaranja", "Z"}
    });

    jraDatumOd.setDataSet(dummy);
    jraDatumOd.setColumnName("DATUMO");
    jraDatumOd.setHorizontalAlignment(SwingConstants.CENTER);

    jraDatumDo.setDataSet(dummy);
    jraDatumDo.setColumnName("DATUMZ");
    jraDatumDo.setHorizontalAlignment(SwingConstants.CENTER);
    
    jraBroj.setDataSet(dummy);
    jraBroj.setColumnName("BROJ");
    
    jlServiser.setText("Serviser");
    jlrSer.setColumnName("CRADNIK");
    jlrSer.setDataSet(dummy);
    jlrSer.setRaDataSet(dm.getRadnici());
    jlrSer.setColNames(new String[] {"IME", "PREZIME"});
    jlrSer.setTextFields(new JTextComponent[] {jlrSerIme, jlrSerPrezime});
    jlrSer.setVisCols(new int[] {0, 1, 2});
    jlrSer.setSearchMode(0);
    jlrSer.setNavButton(jbSelSer);

    jlrSerIme.setColumnName("IME");
    jlrSerIme.setNavProperties(jlrSer);
    jlrSerIme.setSearchMode(1);
    jlrSerPrezime.setColumnName("PREZIME");
    jlrSerPrezime.setNavProperties(jlrSer);
    jlrSerPrezime.setSearchMode(1);

    jpDetail.add(jlPeriod, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jraDatumOd, new XYConstraints(150, 20, 100, -1));
    jpDetail.add(jraDatumDo, new XYConstraints(255, 20, 100, -1));
    jpDetail.add(jcbPeriod, new XYConstraints(360, 20, 100, -1));
    jpDetail.add(jlServiser, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlrSer, new XYConstraints(150, 45, 100, -1));
    jpDetail.add(jlrSerIme, new XYConstraints(255, 45, 100, -1));
    jpDetail.add(jlrSerPrezime, new XYConstraints(360, 45, 100, -1));
    jpDetail.add(jbSelSer, new XYConstraints(465, 45, 21, 21));
    
    jpDetail.add(jlSubj, new XYConstraints(15, 75, -1, -1));
    jpDetail.add(jlVrsub, new XYConstraints(150, 75, -1, -1));
    jpDetail.add(jlBroj, new XYConstraints(15, 100, -1, -1));
    jpDetail.add(jraBroj, new XYConstraints(150, 100, 205, -1));
    jpDetail.add(jpPodaci, new XYConstraints(0, 120, 500, -1));

    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(jpDetail, BorderLayout.CENTER);
    this.getContentPane().add(okp, BorderLayout.SOUTH);

    this.setTitle("Statistika radnih naloga");
    stt = new frmStatistikaRadnihNaloga(this.getWindow());
    stt.pack();
    startFrame.getStartFrame().centerFrame(stt, 0, "");
    new raDateRange(jraDatumOd, jraDatumDo);

    this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

    addComponentListener(new ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        jlrSer.requestFocusLater();
        //jpPodaci.SetFokus();
      }
    });
    this.okp.registerOKPanelKeys(this.getWindow());
//    this.addKeyListener(new KeyAdapter() {
//      public void keyPressed(KeyEvent e) {
//        if (e.getKeyCode() == e.VK_F10) OKPress();
//        if (e.getKeyCode() == e.VK_ESCAPE) cancelPress();
//      }
//    });    
  }
}
