/****license*****************************************************************
**   file: dlgCover.java
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
package hr.restart.sk;

import hr.restart.baza.dM;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.util.OKpanel;
import hr.restart.util.raCommonClass;
import hr.restart.util.startFrame;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
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

public class dlgCover extends JraDialog {
  raCommonClass rcc = raCommonClass.getraCommonClass();

  JPanel jp = new JPanel();
  XYLayout lay = new XYLayout();

  JLabel jlRac = new JLabel();
  JLabel jlRac2 = new JLabel();

  JLabel jlUpl = new JLabel();
  JLabel jlUpl2 = new JLabel();

  JLabel jlRacIS = new JLabel();
  JLabel jlUplIS = new JLabel();
  JLabel jlPok = new JLabel();

  JraTextField jraRacIznos = new JraTextField();
  JraTextField jraUplIznos = new JraTextField();

  JraTextField jraRacSaldo = new JraTextField() {
    public boolean isFocusTraversable() {
      return !first;
    }
    public void valueChanged() {
      chgRacun();
    }
  };
  JraTextField jraUplSaldo = new JraTextField() {
    public boolean isFocusTraversable() {
      return !first;
    }
    public void valueChanged() {
      chgUplata();
    }
  };
  JraTextField jraPok = new JraTextField() {
    public void valueChanged() {
      chgPok();
    }
  };

  BigDecimal origRS, origUS, origP, maxRS, maxUS;
  boolean first, racInv, uplInv;

  StorageDataSet fields = new StorageDataSet();

  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      OKPress();
    }
    public void jPrekid_actionPerformed() {
      CancelPress();
    }
  };

  private void init() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public dlgCover(Frame frame, String title) {
    super(frame, title, true);
    init();
  }

  public dlgCover(Dialog dlg, String title) {
    super(dlg, title, true);
    init();
  }

  public dlgCover(String title) {
    this((Frame) null, title);
  }

  public dlgCover() {
    this((Frame) null, "");
  }

  private String getVrdokName(String vr) {
    if (vr.equals("URN") || vr.equals("IRN")) return "Ra\u010Dun";
    else if (vr.equals("IPL") || vr.equals("UPL")) return "Uplata";
    else if (vr.equals("OKK") || vr.equals("OKD")) return "Knjižna obavijest";
    else return "Dokument";
  }

  public boolean changePok(ReadRow rac, ReadRow upl, DataSet pok) {
    String vrr = rac.getString("VRDOK");
    String vru = upl.getString("VRDOK");
    jlRac.setText(getVrdokName(vrr));
    jlUpl.setText(getVrdokName(vru));
    jlRac2.setText(rac.getString("OPIS"));
    jlUpl2.setText(upl.getString("OPIS"));
    if (raSaldaKonti.isDomVal(rac) || raSaldaKonti.isDomVal(upl))
      setTitle("djelomièno pokrivanje (" + hr.restart.zapod.Tecajevi.getDomOZNVAL() + ")");
    else setTitle("djelomièno pokrivanje (" + rac.getString("OZNVAL") + ")");

    origP = pok.getBigDecimal("IZNOS");
    String salCol = !raSaldaKonti.isSimple() && raSaldaKonti.isDomVal(rac) != 
        raSaldaKonti.isDomVal(upl) ? "SALDO" : raSaldaKonti.colSaldo();
    origRS = rac.getBigDecimal(salCol);
    origUS = upl.getBigDecimal(salCol);

    racInv = raVrdokMatcher.isRacunTip(rac) != 
        rac.getString("CSKSTAVKE").equals(pok.getString("CRACUNA"));
    uplInv = raVrdokMatcher.isUplataTip(upl) !=
        upl.getString("CSKSTAVKE").equals(pok.getString("CUPLATE"));

    /*racKob = raVrdokMatcher.isKob(rac);
    uplKob = raVrdokMatcher.isKob(upl);

    if (!racKob) maxRS = origRS.add(origP);
    else maxRS = origRS.subtract(origP);
    if (!uplKob) maxUS = origUS.add(origP);
    else maxUS = origUS.subtract(origP);

    maxPok = maxRS.abs().min(maxUS.abs());*/
    maxRS = racInv ? origRS.subtract(origP) : origRS.add(origP);
    maxUS = racInv ? origUS.subtract(origP) : origUS.add(origP);

    fields.setBigDecimal("RIZNOS", maxRS);
    fields.setBigDecimal("UIZNOS", maxUS);
    fields.setBigDecimal("RSALDO", origRS);
    fields.setBigDecimal("USALDO", origUS);
    fields.setBigDecimal("POK", origP);

    ok = false;
    first = true;
    super.show();

    if (ok) {
//      raSaldaKonti.setSaldo(rac, fields.getBigDecimal("RSALDO"));
//      raSaldaKonti.setSaldo(upl, fields.getBigDecimal("USALDO"));
//      rac.setBigDecimal("SALDO", fields.getBigDecimal("RSALDO"));
//      upl.setBigDecimal("SALDO", fields.getBigDecimal("USALDO"));
      pok.setBigDecimal("IZNOS", fields.getBigDecimal("POK"));
    }
    return ok;
  }

  boolean ok;
  private void OKPress() {
    chgPok();
    ok = true;
    this.dispose();
  }

  private void CancelPress() {
    this.dispose();
  }

  private void setFields() {
    fields.setColumns(new Column[] {
      dM.createBigDecimalColumn("RIZNOS"),
      dM.createBigDecimalColumn("RSALDO"),
      dM.createBigDecimalColumn("UIZNOS"),
      dM.createBigDecimalColumn("USALDO"),
      dM.createBigDecimalColumn("POK")
    });
    fields.open();
  }

  private void jbInit() throws Exception {
    setFields();
    jp.setLayout(lay);
    lay.setWidth(450);
    lay.setHeight(180);

    jlRac.setFont(jlRac.getFont().deriveFont(Font.BOLD));
    jlRac.setHorizontalAlignment(SwingConstants.CENTER);
    jlRac2.setHorizontalAlignment(SwingConstants.CENTER);
    jlUpl.setFont(jlRac.getFont().deriveFont(Font.BOLD));
    jlUpl.setHorizontalAlignment(SwingConstants.CENTER);
    jlUpl2.setHorizontalAlignment(SwingConstants.CENTER);

    jlRacIS.setText("Iznos / Saldo");
    jlRacIS.setHorizontalAlignment(SwingConstants.CENTER);
    jlUplIS.setText("Iznos / Saldo");
    jlUplIS.setHorizontalAlignment(SwingConstants.CENTER);
    jlPok.setText("Pokriveno");
    jlPok.setHorizontalAlignment(SwingConstants.CENTER);

    jraRacIznos.setDataSet(fields);
    jraRacIznos.setColumnName("RIZNOS");
    rcc.setLabelLaF(jraRacIznos, false);
    jraRacSaldo.setDataSet(fields);
    jraRacSaldo.setColumnName("RSALDO");
    jraUplIznos.setDataSet(fields);
    jraUplIznos.setColumnName("UIZNOS");
    rcc.setLabelLaF(jraUplIznos, false);
    jraUplSaldo.setDataSet(fields);
    jraUplSaldo.setColumnName("USALDO");
    jraPok.setDataSet(fields);
    jraPok.setColumnName("POK");

    jp.add(jlRac, new XYConstraints(10, 20, 180, -1));
    jp.add(jlUpl, new XYConstraints(260, 20, 180, -1));
    jp.add(jlRac2, new XYConstraints(10, 45, 180, -1));
    jp.add(jlUpl2, new XYConstraints(260, 45, 180, -1));

    jp.add(jlRacIS, new XYConstraints(50, 100, 100, -1));
    jp.add(jraRacIznos, new XYConstraints(50, 120, 100, -1));
    jp.add(jraRacSaldo, new XYConstraints(50, 145, 100, -1));
    jp.add(jlUplIS, new XYConstraints(300, 100, 100, -1));
    jp.add(jraUplIznos, new XYConstraints(300, 120, 100, -1));
    jp.add(jraUplSaldo, new XYConstraints(300, 145, 100, -1));
    jp.add(jlPok, new XYConstraints(175, 125, 100, -1));
    jp.add(jraPok, new XYConstraints(175, 145, 100, -1));

    addListeners();
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    getContentPane().add(jp, BorderLayout.CENTER);
    getContentPane().add(okp, BorderLayout.SOUTH);
    pack();

    okp.registerOKPanelKeys(this);
    startFrame.getStartFrame().centerFrame(this, 15, "Pokrivanje");
  }

  private void addListeners() {
    /*jraRacSaldo.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        chgRacun();
      }
    });
    jraUplSaldo.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        chgUplata();
      }
    });*/
    jraPok.addFocusListener(new FocusAdapter() {
      public void focusGained(FocusEvent e) {
        first = false;
      }
      /*public void focusLost(FocusEvent e) {
        chgPok();
      }*/
    });
  }

  /*private void ensure(String col, BigDecimal peak) {
    if (peak.signum() < 0) {
      if (fields.getBigDecimal(col).compareTo(peak) < 0)
        fields.setBigDecimal(col, peak);
      if (fields.getBigDecimal(col).signum() > 0)
        fields.setBigDecimal(col, raSaldaKonti.n0);
    } else {
      if (fields.getBigDecimal(col).compareTo(peak) > 0)
        fields.setBigDecimal(col, peak);
      if (fields.getBigDecimal(col).signum() < 0)
        fields.setBigDecimal(col, raSaldaKonti.n0);
    }
  }*/

  private void chgRacun() {
//    ensure("RSALDO", maxRS);
    fields.setBigDecimal("POK", racInv ?
        maxRS.subtract(fields.getBigDecimal("RSALDO")).negate() :
        maxRS.subtract(fields.getBigDecimal("RSALDO")));
    chgPok();
  }

  public void chgUplata() {
//    ensure("USALDO", maxUS);
    fields.setBigDecimal("POK", uplInv ?
        maxUS.subtract(fields.getBigDecimal("USALDO")).negate() :
        maxUS.subtract(fields.getBigDecimal("USALDO")));
    chgPok();
  }

  public void chgPok() {
//    ensure("POK", maxPok);
    BigDecimal pok = fields.getBigDecimal("POK");
    fields.setBigDecimal("RSALDO", racInv ? maxRS.add(pok) : maxRS.subtract(pok));
    fields.setBigDecimal("USALDO", uplInv ? maxUS.add(pok) : maxUS.subtract(pok));
  }
}