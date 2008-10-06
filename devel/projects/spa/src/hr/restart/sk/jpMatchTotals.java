/****license*****************************************************************
**   file: jpMatchTotals.java
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
import hr.restart.swing.JraTextField;

import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class jpMatchTotals extends JPanel {
  XYLayout lay = new XYLayout();

  JLabel jlUk = new JLabel("Ukupno");
  JLabel jlUkPv = new JLabel("Ukupno");
  JLabel jlTot = new JLabel("");
  JLabel jlPok = new JLabel("Pokriveno");
  JLabel jlOst = new JLabel("Saldo");
  JraTextField jraTot = new JraTextField();
  JraTextField jraPok = new JraTextField();
  JraTextField jraOst = new JraTextField();
  JraTextField jraTotPv = new JraTextField();
  JraTextField jraPokPv = new JraTextField();
  JraTextField jraOstPv = new JraTextField();
  StorageDataSet tots = new StorageDataSet();

  public jpMatchTotals() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setBaseText(String dok) {
    jlTot.setText(dok);
  }

  public void setValuta(String val, boolean force) {
    if (!force && (raSaldaKonti.isSimple() || val == null || 
        val.equalsIgnoreCase(hr.restart.zapod.Tecajevi.getDomOZNVAL()))) { 
      jlUk.setText("Ukupno");
      lay.setHeight(50);
    } else { 
      jlUk.setText("Ukupno/"+val);
      jlUkPv.setText("Ukupno/"+hr.restart.zapod.Tecajevi.getDomOZNVAL());
      lay.setHeight(80);
    }
  }
  
  public void initTotal(BigDecimal total) {
    tots.setBigDecimal("TOT", total);
    tots.setBigDecimal("POK", raSaldaKonti.n0);
    tots.setBigDecimal("SAL", total);
  }

  public void initTotalPv(BigDecimal totalPv) {
    tots.setBigDecimal("TOTPV", totalPv);
    tots.setBigDecimal("POKPV", raSaldaKonti.n0);
    tots.setBigDecimal("SALPv", totalPv);
  }

  public boolean isTotalDefined() {
    return tots.getBigDecimal("TOT").signum() != 0;
  }

  public void setPokriveno(BigDecimal pok) {
    tots.setBigDecimal("POK", pok);
    if (isTotalDefined())
      tots.setBigDecimal("SAL", tots.getBigDecimal("TOT").subtract(pok));
  }

  public void addPokriveno(BigDecimal pok) {
    setPokriveno(tots.getBigDecimal("POK").add(pok));
  }

  public BigDecimal getPokriveno() {
    return tots.getBigDecimal("POK");
  }

  public BigDecimal getTotal() {
    return tots.getBigDecimal("TOT");
  }

  public BigDecimal getSaldo() {
    return tots.getBigDecimal("SAL");
  }

  public void setPokrivenoPv(BigDecimal pokPv) {
    tots.setBigDecimal("POKPV", pokPv);
    if (isTotalDefined())
      tots.setBigDecimal("SALPV", tots.getBigDecimal("TOTPV").subtract(pokPv));
  }

  public void addPokrivenoPv(BigDecimal pokPv) {
    setPokrivenoPv(tots.getBigDecimal("POKPV").add(pokPv));
  }

  public BigDecimal getPokrivenoPv() {
    return tots.getBigDecimal("POKPV");
  }

  public BigDecimal getTotalPv() {
    return tots.getBigDecimal("TOTPV");
  }

  public BigDecimal getSaldoPv() {
    return tots.getBigDecimal("SALPV");
  }

  private void jbInit() throws Exception {
    tots.setColumns(new Column[] {
      dM.createBigDecimalColumn("TOT", 2),
      dM.createBigDecimalColumn("POK", 2),
      dM.createBigDecimalColumn("SAL", 2),
      dM.createBigDecimalColumn("TOTPV", 2),
      dM.createBigDecimalColumn("POKPV", 2),
      dM.createBigDecimalColumn("SALPV", 2)
    });
    tots.open();
    tots.insertRow(false);

    jraTot.setDataSet(tots);
    jraTot.setColumnName("TOT");
    jraPok.setDataSet(tots);
    jraPok.setColumnName("POK");
    jraOst.setDataSet(tots);
    jraOst.setColumnName("SAL");
    jlTot.setHorizontalAlignment(JLabel.CENTER);
    jlPok.setHorizontalAlignment(JLabel.CENTER);
    jlOst.setHorizontalAlignment(JLabel.CENTER);
    jraTotPv.setDataSet(tots);
    jraTotPv.setColumnName("TOTPV");
    jraPokPv.setDataSet(tots);
    jraPokPv.setColumnName("POKPV");
    jraOstPv.setDataSet(tots);
    jraOstPv.setColumnName("SALPV");
    setLayout(lay);
    lay.setWidth(475);
    lay.setHeight(80);
    add(jlUk, new XYConstraints(15, 20, -1, -1));
    add(jraTot, new XYConstraints(150, 20, 100, -1));
    add(jraPok, new XYConstraints(255, 20, 100, -1));
    add(jraOst, new XYConstraints(360, 20, 100, -1));
    add(jlTot, new XYConstraints(150, 4, 100, -1));
    add(jlPok, new XYConstraints(255, 4, 100, -1));
    add(jlOst, new XYConstraints(360, 4, 100, -1));
    add(jlUkPv, new XYConstraints(15, 50, -1, -1));
    add(jraTotPv, new XYConstraints(150, 50, 100, -1));
    add(jraPokPv, new XYConstraints(255, 50, 100, -1));
    add(jraOstPv, new XYConstraints(360, 50, 100, -1));
    setBorder(BorderFactory.createRaisedBevelBorder());
  }
}
