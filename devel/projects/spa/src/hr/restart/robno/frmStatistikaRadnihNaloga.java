/****license*****************************************************************
**   file: frmStatistikaRadnihNaloga.java
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
import hr.restart.sisfun.Asql;
import hr.restart.swing.JraButton;
import hr.restart.swing.raTableColumnModifier;
import hr.restart.util.OKpanel;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.util.raFrame;
import hr.restart.util.raImages;
import hr.restart.util.raJPTableView;
import hr.restart.util.startFrame;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.borland.dx.sql.dataset.QueryDataSet;
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

class frmSRNDialog extends raFrame {
  raJPTableView jp = new raJPTableView() {
    public void mpTable_doubleClicked() {
      doubleClick();
    }
  };
  hr.restart.util.reports.JTablePrintRun TPRun =
      new hr.restart.util.reports.JTablePrintRun(getClass().getName());

  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      cancelPress();
    }
  };

  public frmSRNDialog(Container owner) {
    super(raFrame.DIALOG, owner);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(jp, BorderLayout.CENTER);
    this.getContentPane().add(okp, BorderLayout.SOUTH);
    okp.jBOK.setText("Ispis");
    okp.jBOK.setIcon(raImages.getImageIcon(raImages.IMGPRINT));
    jp.initKeyListener(this);

    this.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_F5) OKPress();
        if (e.getKeyCode() == e.VK_ESCAPE) cancelPress();
      }
    });
  }

  private void OKPress() {
    jp.getNavBar().getColBean().setSumRow(jp.getSumRow());
    TPRun.setInterTitle(getClass().getName());
    TPRun.setColB(jp.getNavBar().getColBean());
    TPRun.setRTitle(this.getTitle());
    TPRun.runIt();
  }
  private void cancelPress() {
    this.hide();
  }
  
  public void hide() {
    super.hide();
    jp.removeAllTableModifiers();
  }

  public void show() {
    jp.getNavBar().getColBean().initialize();
    super.show();
  }
  
  public void doubleClick() {
  }
}


public class frmStatistikaRadnihNaloga extends raFrame {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  Valid vl = Valid.getValid();
  dM dm = dM.getDataModule();

  JPanel jpDetail = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JLabel jlSubj = new JLabel();
  JLabel jlNaloga = new JLabel();
  JLabel jlMat = new JLabel();
  JLabel jlUsl = new JLabel();
  JLabel jlSati = new JLabel();
  JTextField jtSubj = new JTextField();
  JTextField jtNaloga = new JTextField();
  JTextField jtMat = new JTextField();
  JTextField jtUsl = new JTextField();
  JTextField jtSati = new JTextField();
  JraButton jbSubj = new JraButton();
  JraButton jbNaloga = new JraButton();
  JraButton jbMat = new JraButton();
  JraButton jbUsl = new JraButton();
  short cvrsubj;
  String qdat, qsub, qsublist, qradlist;

  frmSRNDialog srnsub, srnrn, srnm, srns;
  frmPregledRadnihNaloga prn;

  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      cancelPress();
    }
  };

  public frmStatistikaRadnihNaloga(Container owner) {
    super(raFrame.DIALOG, owner);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void setSelection(frmPregledRadnihNaloga prn, short cvrsubj, String queryDat, String querySub) {
    HashSet cradnal = new HashSet();
    HashSet csubrn = new HashSet();
    this.cvrsubj = cvrsubj;
    this.qdat = queryDat;
    this.qsub = querySub;
    this.prn = prn;
//    System.out.println(qdat + "  " + qsub);
    String query = "SELECT stdoki.vrdok, stdoki.jm, stdoki.kol FROM RN, stdoki "+
          "WHERE RN.cradnal = stdoki.cradnal AND RN.cvrsubj = " + cvrsubj + qdat + qsub;

    vl.execSQL(query);
    vl.RezSet.open();
//    sysoutTEST sys = new sysoutTEST(false);
//    sys.prn(vl.RezSet);
//    jtNaloga.setText("" + vl.getSetCount(vl.RezSet, 0));
//    jtSubj.setText("" + vl.getSetCount(vl.RezSet, 1));
    int mat = 0, usl = 0;
    BigDecimal b = new BigDecimal(0.);
    b = b.setScale(3);
    for (vl.RezSet.first(); vl.RezSet.inBounds(); vl.RezSet.next()) {
      if (vl.RezSet.getString("VRDOK").equals("IZD")) ++mat;
      else {
        ++usl;
        if (vl.RezSet.getString("JM").toUpperCase().equals("H") ||
            vl.RezSet.getString("JM").toUpperCase().equals("SAT"))
          b = b.add(vl.RezSet.getBigDecimal("KOL"));
      }
    }
    
    query = "SELECT RN.cradnal, RN.csubrn FROM RN WHERE RN.cvrsubj = " + 
        cvrsubj + qdat + qsub;
    vl.execSQL(query);
    vl.RezSet.open();
    for (vl.RezSet.first(); vl.RezSet.inBounds(); vl.RezSet.next()) {
      cradnal.add(vl.RezSet.getString("CRADNAL").toUpperCase());
      csubrn.add(vl.RezSet.getString("CSUBRN").toUpperCase());
    }
    
    jtNaloga.setText("" + cradnal.size());
    jtSubj.setText("" + csubrn.size());
    jtMat.setText("" + mat);
    jtUsl.setText("" + usl);
    jtSati.setText(b.setScale(0, b.ROUND_HALF_UP).toString());

    if (csubrn.size() <= 100) {
      VarStr sublist = new VarStr(128);
      Iterator si = csubrn.iterator();
      while (si.hasNext())
        sublist.append("'").append((String) si.next()).append("',");
      sublist.chop();
      qsublist = sublist.toString();
    } else
      qsublist = "SELECT RN.csubrn FROM RN WHERE RN.cvrsubj = " + cvrsubj + qdat + qsub;
    
    if (cradnal.size() <= 50) {
      VarStr radlist = new VarStr(128);
      Iterator ri = cradnal.iterator();
      while (ri.hasNext())
        radlist.append("'").append((String) ri.next()).append("',");
      radlist.chop();
      qradlist = radlist.toString();
    } else
      qradlist = "SELECT RN.cradnal FROM RN WHERE RN.cvrsubj = " + cvrsubj + qdat + qsub;
    
    System.out.println(qsublist);
    System.out.println(qradlist);
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(xYLayout1);
    xYLayout1.setWidth(350);
    xYLayout1.setHeight(160);

    jlSubj.setText("Subjekata");
    jlNaloga.setText("Radnih naloga");
    jlMat.setText("Stavki materijala");
    jlUsl.setText("Stavki usluga");
    jlSati.setText("Radnih sati (cca)");

    jbSubj.setText("...");
    jbNaloga.setText("...");
    jbMat.setText("...");
    jbUsl.setText("...");
    jtSubj.setHorizontalAlignment(SwingConstants.TRAILING);
    jtNaloga.setHorizontalAlignment(SwingConstants.TRAILING);
    jtMat.setHorizontalAlignment(SwingConstants.TRAILING);
    jtUsl.setHorizontalAlignment(SwingConstants.TRAILING);
    jtSati.setHorizontalAlignment(SwingConstants.TRAILING);
    rcc.setLabelLaF(jtSubj, false);
    rcc.setLabelLaF(jtNaloga, false);
    rcc.setLabelLaF(jtMat, false);
    rcc.setLabelLaF(jtUsl, false);
    rcc.setLabelLaF(jtSati, false);
    jpDetail.add(jlSubj, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlNaloga, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlMat, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlUsl, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jlSati, new XYConstraints(15, 120, -1, -1));
    jpDetail.add(jtSubj, new XYConstraints(200, 20, 100, -1));
    jpDetail.add(jtNaloga, new XYConstraints(200, 45, 100, -1));
    jpDetail.add(jtMat, new XYConstraints(200, 70, 100, -1));
    jpDetail.add(jtUsl, new XYConstraints(200, 95, 100, -1));
    jpDetail.add(jtSati, new XYConstraints(200, 120, 100, -1));
    jpDetail.add(jbSubj, new XYConstraints(310, 20, 21, 21));
    jpDetail.add(jbNaloga, new XYConstraints(310, 45, 21, 21));
    jpDetail.add(jbMat, new XYConstraints(310, 70, 21, 21));
    jpDetail.add(jbUsl, new XYConstraints(310, 95, 21, 21));

    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(jpDetail, BorderLayout.CENTER);
    this.getContentPane().add(okp, BorderLayout.SOUTH);

    okp.jBOK.setText("Ispis");
    okp.jBOK.setIcon(raImages.getImageIcon(raImages.IMGPRINT));
    srnsub = new frmSRNDialog(this.getWindow()) {
      public void doubleClick() {
        if (srnsub.jp.getDataSet().getRowCount() > 0)
          subDoubleClick();
      }
    };
    srnrn = new frmSRNDialog(this.getWindow()) {
      public void doubleClick() {
        if (srnrn.jp.getDataSet().getRowCount() > 0)
          rnDoubleClick();
      }
    };
    srnrn.jp.getColumnsBean().setSaveName("Statistika-RN");
    srnm = new frmSRNDialog(this.getWindow());
    srnm.jp.getColumnsBean().setSaveName("Statistika-MAT");
    srns = new frmSRNDialog(this.getWindow());
    srns.jp.getColumnsBean().setSaveName("Statistika-USL");

    jbSubj.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showSubjekti();
      }
    });

    jbNaloga.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showNalozi();
      }
    });

    jbMat.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showMat();
      }
    });

    jbUsl.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showUsl();
      }
    });
    this.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_F5) OKPress();
        if (e.getKeyCode() == e.VK_ESCAPE) cancelPress();
      }
    });
  }

  private void showSubjekti() {
    if (qsublist.equals("")) return;
//    vl.execSQL("SELECT * FROM RN_subjekt WHERE csubrn IN (" + qsublist + ")");
//    QueryDataSet sub = vl.RezSet;
//    sub.setColumns(dm.getRN_subjekt().cloneColumns());
//    sub.open();
    System.out.println(qsublist);

    srnsub.jp.setDataSet(Asql.getSubjektiDetail(cvrsubj, qsublist));
    int cols = srnsub.jp.getDataSet().getColumnCount();
    if (cols > 3) cols = 3 + ((int) Math.round(Math.sqrt(cols - 3)));
    int[] vc = new int[cols];
    for (int i = 0; i < cols; i++) vc[i] = i;
    srnsub.jp.setVisibleCols(vc);
    srnsub.setTitle("Subjekti radnih naloga");
    srnsub.setSize(Math.min(680, cols * 120), 300);
    srnsub.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 320, 240);
    srnsub.show();

    Toolkit.getDefaultToolkit().sync();
  }

  private void showNalozi() {
    if (qradlist.equals("")) return;
    vl.execSQL("SELECT * FROM RN WHERE cradnal IN (" + qradlist + ")");
    QueryDataSet rad = vl.RezSet;
    rad.setColumns(dm.getRN().cloneColumns());
    rad.open();

    String range = new String[] {"otvoreni", "obra\u010Dunati", "zatvoreni"}[prn.jcbPeriod.getSelectedIndex()];
    
    srnrn.hide();
    srnrn.jp.setDataSet(rad);
    srnrn.jp.setVisibleCols(new int[] {4, 5, 12, 13 + prn.jcbPeriod.getSelectedIndex()});
    srnrn.jp.addTableModifier(
      new raTableColumnModifier("CKUPAC", new String[] {"CKUPAC", "PREZIME", "IME"}, dm.getKupci())
    );
    srnrn.jp.addTableModifier(
      new raTableColumnModifier("CSUBRN", new String[] {"CSUBRN", "NAZVRSUBJ", "BROJ"}, dm.getRN_subjekt())
    );

    srnrn.setTitle("Radni nalozi " + range + " od " + prn.jraDatumDo.getText() + " do " +
          prn.jraDatumDo.getText());
    srnrn.setSize(640, 360);
    srnrn.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 320, 200);
    srnrn.show();
  }

  public void showMat() {
    if (qradlist.equals("")) return;
    vl.execSQL("SELECT * FROM stdoki WHERE vrdok = 'IZD' AND cradnal IN (" + qradlist + ") ORDER BY cradnal");
    QueryDataSet mat = vl.RezSet;
    mat.setColumns(dm.getStdoki().cloneColumns());
    mat.open();

    srnm.hide();
    srnm.jp.setDataSet(mat);
    srnm.jp.setVisibleCols(new int[] {0, Aut.getAut().getCARTdependable(5, 6, 7), 8, 11});
    srnm.jp.addTableModifier(
      new raTableColumnModifier("CSKL", new String[] {"CSKL", "NAZSKL"}, dm.getSklad())
    );

    srnm.setTitle("Utrošeni materijali radnih naloga");
    srnm.setSize(600, 400);
    srnm.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 320, 200);
    srnm.show();
    srnm.jp.removeAllTableModifiers();
  }

  public void showUsl() {
    if (qradlist.equals("")) return;
    vl.execSQL("SELECT * FROM stdoki WHERE vrdok = 'RNL' AND cradnal IN (" + qradlist + ") ORDER BY cradnal");
    QueryDataSet usl = vl.RezSet;
    usl.setColumns(dm.getStdoki().cloneColumns());
    usl.open();

    srns.hide();
    srns.jp.setDataSet(usl);
    srns.jp.setVisibleCols(new int[] {Aut.getAut().getCARTdependable(5, 6, 7), 8, 9, 11});

    srns.setTitle("Obavljeni poslovi radnih naloga");
    srns.setSize(600, 400);
    srns.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 320, 200);
    srns.show();
  }

  private void OKPress() {
    this.hide();
  }
  private void cancelPress() {
    this.hide();
  }
  
  void subDoubleClick() {
    showRnBySubject(srnsub.jp.getDataSet().getString("CSUBRN"));
  }
  
  void rnDoubleClick() {
    frmServis fs = (frmServis) startFrame.getStartFrame().
      showFrame("hr.restart.robno.frmServis", 15, "Servis", false);
    if (fs.raMaster.isShowing() && lookupData.getlookupData().raLookup(fs.getMasterSet(),
        "CRADNAL", srnrn.jp.getDataSet().getString("CRADNAL")) != null) {
      lookupData.getlookupData().raLocate(fs.getMasterSet(),
          "CRADNAL", srnrn.jp.getDataSet().getString("CRADNAL"));
      return;
    }
    
    showRnBySubject(srnrn.jp.getDataSet().getString("CSUBRN"));
  }
  
  void showRnBySubject(String csubrn) {
    frmServis fs = (frmServis) startFrame.getStartFrame().
      showFrame("hr.restart.robno.frmServis", 15, "Servis", false);
    fs.setPreSelect(fs.pres);
    fs.pres.getSelRow().setString("SERPR", "S");
    fs.pres.getSelRow().setString("CSKL", "");
    fs.jlrCorg.forceFocLost();
    fs.pres.getSelRow().setString("STATUS", "");
    fs.jcbStatus.setSelectedIndex(0);
    fs.pres.getSelRow().setTimestamp("DATDOK-from", prn.dummy.getTimestamp("DATUMO"));
//    this.getSelRow().setTimestamp("DATDOK-from",rut.
//      findFirstDayOfYear(Integer.valueOf(vl.findYear()).intValue()));
    fs.pres.getSelRow().setTimestamp("DATDOK-to", prn.dummy.getTimestamp("DATUMZ"));
    fs.pres.getSelRow().setString("CSUBRN", csubrn);
    fs.jlrPreSub.setText(csubrn);
    fs.pres.getSelRow().setShort("CVRSUBJ", cvrsubj);
    fs.jlrVrsub.setText(cvrsubj+"");
    fs.pres.doSelect();
    startFrame.getStartFrame().showFrame(fs);
  }
}
