/****license*****************************************************************
**   file: frmMjIzv.java
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

import hr.restart.swing.JraTextField;
import hr.restart.util.lookupData;

import java.awt.event.FocusEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmMjIzv extends frmIzvjestajiPL {
  /** @todo ovo debagirat frst ting in d morning */
  hr.restart.util.Util ut = new hr.restart.util.Util();
  int mjOd, mjDo, godOd, godDo;

  QueryDataSet repPoCorg;
  QueryDataSet repPoCradMj;
  QueryDataSet repPoCorgUnutarCradMj;

  QueryDataSet fondoviSati;

  StorageDataSet sumPoCorg = new StorageDataSet();

  JPanel jPanel3 = new JPanel();
  public XYLayout xYLayout3 = new XYLayout();

  JLabel jlMjObrade = new JLabel();
  JraTextField jraMjObradeOd = new JraTextField() {
    public void focusLost(FocusEvent e) {
      super.focusLost(e);
      if (fieldSet.getShort("MJOBOD") > 12 || fieldSet.getShort("MJOBOD") < 1){
        fieldSet.setShort("MJOBOD", (short)0);
        jraMjObradeOd.requestFocus();
      }
      setPanel2();
    }
  };
  JraTextField jraMjObradeDo = new JraTextField(){
    public void focusLost(FocusEvent e) {
      super.focusLost(e);
      setPanel2();
    }
  };
  JraTextField jraGodObradeOd = new JraTextField(){
    public void focusLost(FocusEvent e) {
      super.focusLost(e);
      setPanel2();
    }
  };
  JraTextField jraGodObradeDo = new JraTextField(){
    public void focusLost(FocusEvent e) {
      super.focusLost(e);
      setPanel2();
    }
  };

  private static double STOPA;
  int brojObrada;
  int SUMFONDSATI;

  public frmMjIzv() {
    this('O');
  }

  public frmMjIzv(char mode) {
    super(mode);
    onStart();
    isArh = mode == 'A';
    if (!isArh) {
      frmi = this;
      addRep();
    }
  }

  static frmMjIzv frmi;
  static boolean isArh = false;

  public static frmMjIzv getFrmMjIzv() {
    if (isArh) return frmMjIzvArh.getFrmMjIzvArh();
    else return frmi;
  }

  Column colMjObrOD = new Column();
  Column colGodObrOD = new Column();
  Column colMjObrDO = new Column();
  Column colGodObrDO = new Column();

  protected void onStart(){
    if (this.getRepMode() == 'A'){

      colMjObrOD.setColumnName("MJOBOD");
      makeShortCol(colMjObrOD, "0");

      colGodObrOD.setColumnName("GDOBOD");
      makeShortCol(colGodObrOD, "0");

      colMjObrDO.setColumnName("MJOBDO");
      makeShortCol(colMjObrDO, "0");

      colGodObrDO.setColumnName("GDOBDO");
      makeShortCol(colGodObrDO, "0");

      fieldSet.close();

      fieldSet.addColumn(colMjObrOD);
      fieldSet.addColumn(colGodObrOD);
      fieldSet.addColumn(colMjObrDO);
      fieldSet.addColumn(colGodObrDO);

      fieldSet.open();

      jlMjObrade.setText("Mj. god. isplate (od - do)");

      jraMjObradeOd.setHorizontalAlignment(SwingConstants.CENTER);
      jraMjObradeOd.setDataSet(fieldSet);
      jraMjObradeOd.setColumnName("MJOBOD");

      jraMjObradeDo.setHorizontalAlignment(SwingConstants.CENTER);
      jraMjObradeDo.setDataSet(fieldSet);
      jraMjObradeDo.setColumnName("MJOBDO");

      jraGodObradeOd.setHorizontalAlignment(SwingConstants.CENTER);
      jraGodObradeOd.setDataSet(fieldSet);
      jraGodObradeOd.setColumnName("GDOBOD");

      jraGodObradeDo.setHorizontalAlignment(SwingConstants.CENTER);
      jraGodObradeDo.setDataSet(fieldSet);
      jraGodObradeDo.setColumnName("GDOBDO");

      jPanel3.setLayout(xYLayout3);
      xYLayout3.setWidth(590);
      xYLayout3.setHeight(25);
      rcc.EnabDisabAll(jPanel2, false);
      rcc.EnabDisabAll(jPanel2, false);
      jPanel3.add(jlMjObrade, new XYConstraints(15, 0, -1, -1));
      jPanel3.add(jraMjObradeOd, new XYConstraints(150, 0, 35, -1));
      jPanel3.add(jraGodObradeOd, new XYConstraints(190, 0, 60, -1));
      jPanel3.add(jraMjObradeDo, new XYConstraints(255, 0, 35, -1));
      jPanel3.add(jraGodObradeDo, new XYConstraints(295, 0, 60, -1));
      mainPanel.add(jPanel3, SwingConstants.CENTER);
    }
  }

  public void addRep() {
    this.addReport("hr.restart.pl.repMjIzvPoCorgUnutarCradmj", "Mjese\u010Dni izvještaj po radnim mjestima unutar organizacijskih jedinica", 2);
    this.addReport("hr.restart.pl.repMjIzvPoCorgu", "Mjese\u010Dni izvještaj po organizacijskim jedinicama", 2);
    this.addReport("hr.restart.pl.repMjIzvPoRadmj", "Mjese\u010Dni izvještaj po radnim mjestima", 2);
  }

  public void firstESC(){
    try {
      rcc.EnabDisabAll(jPanel1, true);
      if (getRepMode() == 'A'){
        rcc.EnabDisabAll(jPanel3, true);
      }
      jlrCorg.requestFocus();
      jlrCorg.selectAll();
      firstesc = false;
    }
    catch (Exception ex) {
    }
  }

  public void componentShow() {
    setDifolt();
    if (this.getRepMode() == 'A') {
      QueryDataSet mgb = ut.getNewQueryDataSet("SELECT max(rbrobr) as rbr, min(mjobr) as mj, min(godobr) as god FROM Kumulorgarh");
      fieldSet.setShort("MJOBOD", mgb.getShort("MJ"));
      fieldSet.setShort("GDOBOD", mgb.getShort("GOD"));
      fieldSet.setShort("RBRDO", mgb.getShort("RBR"));
      fieldSet.setShort("MJOBDO", Short.valueOf(ut.getMonth(vl.getToday())).shortValue());
      fieldSet.setShort("GDOBDO", Short.valueOf(ut.getYear(vl.getToday())).shortValue());
      setPanel2();
    }
    jlrCorg.requestFocus();
    jlrCorg.selectAll();
//    sysoutTEST syst = new sysoutTEST(false);
//    syst.prn(fieldSet);
  }

  private void getMaxRbr(){
  }

  private void setPanel2(){
    String mod, mdo;

    if (fieldSet.getShort("MJOBOD") < 10) mod = "0" + fieldSet.getShort("MJOBOD");
    else mod = String.valueOf(fieldSet.getShort("MJOBOD"));
    if (fieldSet.getShort("MJOBDO") < 10) mdo = "0" + fieldSet.getShort("MJOBDO");
    else mdo = String.valueOf(fieldSet.getShort("MJOBDO"));


    java.sql.Timestamp prviDan = java.sql.Timestamp.valueOf(fieldSet.getShort("GDOBOD")+
        "-" + mod + "-01 00:00:00.0");

    java.sql.Timestamp zadnjiDan = ut.getLastSecondOfDay(
        ut.getLastDayOfMonth(java.sql.Timestamp.valueOf(fieldSet.getShort("GDOBDO")+
        "-" + mdo + "-01 23:59:59.9")));

    String obrade = "SELECT max(mjobr) as maxMj, min(mjobr) as minMj, " +
                    "max(godobr) as maxGod, min(godobr) as minGod, " +
                    "max(rbrobr) as maxBr, min(rbrobr) as minBr " +
                    "FROM Kumulorgarh " +
                    "WHERE datumispl BETWEEN '" + prviDan + "' AND '" + zadnjiDan + "'";

    QueryDataSet xinimaxobrade = ut.getNewQueryDataSet(obrade);

    fieldSet.setShort("MJESECOD", xinimaxobrade.getShort("MINMJ"));
    fieldSet.setShort("GODINAOD", xinimaxobrade.getShort("MINGOD"));
    fieldSet.setShort("MJESECDO", xinimaxobrade.getShort("MAXMJ"));
    fieldSet.setShort("GODINADO", xinimaxobrade.getShort("MAXGOD"));
    fieldSet.setShort("RBROD", xinimaxobrade.getShort("MINBR"));
    fieldSet.setShort("RBRDO", xinimaxobrade.getShort("MAXBR"));
  }

  public void okPress(){
    isArh = getRepMode() == 'A';
    setInts();
    setDataSets();
  }

  public boolean Validacija(){
    if (!super.Validacija()) return false;
    rcc.EnabDisabAll(mainPanel, false);
    return true;
  }

  protected String getPoCorgString(){
    String poCorgString;
    if (getRepMode() == 'O'){
      poCorgString = "SELECT max(corg) as corg, " +
          "sum(sati) as sati, sum(bruto) as bruto, sum(doprinosi) as doprinosi, " +
          "sum(neto2) as neto, sum(poriprir) as poriprir, sum(naknade) as naknade, " +
          "sum(doprpod) as doprpod, cast ((sum(doprpod) / sum(bruto)) as double) as stopa " +
          "FROM Kumulorg "  +
          "WHERE " + getWhereQuery("kumulorg") +
          " group by kumulorg.corg";
    } else {
      poCorgString = "SELECT max(corg) as corg, max(mjobr) as mjobr, max(godobr) as godobr," +
          "max(datumispl) as datumispl," +
          "sum(sati) as sati, sum(bruto) as bruto, sum(doprinosi) as doprinosi, " +
          "sum(neto2) as neto, sum(poriprir) as poriprir, sum(naknade) as naknade," +
          "sum(doprpod) as doprpod, cast ((sum(doprpod) / sum(bruto)) as double) as stopa " +
          "FROM Kumulorgarh " +
          "WHERE " + getWhereQuery("kumulorgarh") +
          " GROUP BY kumulorgarh.corg";
    }
//    System.out.println(">>>=+=+=+=+=+=+=+=+=+=+=¤¤> " + poCorgString);
    return poCorgString;
  }

  protected String getPoCradmjString(){
    String poCradmjString;
    if (getRepMode() == 'O'){
      poCradmjString = "SELECT max(RadMJ.cradmj) as cradmj, max(RadMJ.nazivrm) as nazivrm, " +
          "max(radnici.corg) as corg, " +
          "sum(Kumulrad.sati) as sati, sum(Kumulrad.bruto) as bruto, sum(Kumulrad.doprinosi) as doprinosi, " +
          "sum(Kumulrad.neto2) as neto, sum(Kumulrad.poriprir) as poriprir, sum(Kumulrad.naknade) as naknade " +
          "FROM RadMJ, Kumulrad, Radnici, Radnicipl " +
          "WHERE kumulrad.cradnik = radnicipl.cradnik " +
          "AND radnicipl.cradnik = radnici.cradnik " +
          "AND radnicipl.cradmj = radmj.cradmj " +
          "AND " + getWhereQuery("radnicipl") +
          " GROUP BY RadMJ.cradmj";
    } else {
      poCradmjString = "SELECT max(RadMJ.cradmj) as cradmj, max(RadMJ.nazivrm) as nazivrm, " +
          "max(radnici.corg) as corg, " +
          "max(Kumulradarh.mjobr) as mjobr, max(Kumulradarh.godobr) as godobr, " +
          "sum(Kumulradarh.sati) as sati, sum(Kumulradarh.bruto) as bruto, sum(Kumulradarh.doprinosi) as doprinosi, " +
          "sum(Kumulradarh.neto2) as neto, sum(Kumulradarh.poriprir) as poriprir, sum(Kumulradarh.naknade) as naknade " +
          "FROM RadMJ, Kumulradarh, Radnici, Radnicipl " +
          "WHERE Kumulradarh.cradnik = radnicipl.cradnik " +
          "AND radnicipl.cradnik = radnici.cradnik " +
          "AND radnicipl.cradmj = radmj.cradmj " +
          "AND " + getWhereQuery("kumulradarh") +
          " GROUP BY RadMJ.cradmj";
    }
    return poCradmjString;
  }

  protected String getFondSatiString(){
    String fondS;
    if (this.getRepMode() == 'O'){
      dm.getOrgpl().open();
      fondS = "SELECT * FROM FondSati "+
                   "WHERE godina = " + dm.getOrgpl().getShort("GODOBR") + " "+
                   "AND mjesec = " + dm.getOrgpl().getShort("MJOBR") + " "+
                   "AND knjig = '" + hr.restart.zapod.OrgStr.getKNJCORG() + "'";
    } else {
      fondS = "SELECT * FROM FondSati "+
                   "WHERE godina BETWEEN " + fieldSet.getShort("GDOBOD") + " AND " + fieldSet.getShort("GDOBDO") + " "+
                   "AND mjesec BETWEEN " + fieldSet.getShort("MJOBOD") + " AND " + fieldSet.getShort("MJOBDO") + " "+
                   "AND knjig = '" + hr.restart.zapod.OrgStr.getKNJCORG() + "'";
    }
    return fondS;
  }

  protected String getPoCorgUnutarCradmjString(){
    String poCorgUnutarCradmjString;
    poCorgUnutarCradmjString = getPoCradmjString().concat(", radnicipl.corg");
    return poCorgUnutarCradmjString;
  }

  protected void setDataSets(){
    repPoCorg = ut.getNewQueryDataSet(getPoCorgString());
    repPoCradMj = ut.getNewQueryDataSet(getPoCradmjString());
    repPoCorgUnutarCradMj = ut.getNewQueryDataSet(getPoCorgUnutarCradmjString());
    fondoviSati = ut.getNewQueryDataSet(getFondSatiString());
    setSumSet();
  }

  private void makeBDcol(Column col, String difolt) {
    col.setDataType(com.borland.dx.dataset.Variant.BIGDECIMAL);
    col.setDefault(difolt);
  }

  Column zadPl = new Column();
  Column zadRc = new Column();
  Column brRad = new Column();

  protected void setSumSet(){
    try {
      zadPl.setColumnName("ZADPL");
      makeBDcol(zadPl, "0");

      zadRc.setColumnName("ZADRC");
      makeBDcol(zadRc, "0");

//      brRad.setColumnName("BRRAD");
//      brRad.setDataType(com.borland.dx.dataset.Variant.INT);
//      brRad.setDefault("0");

      brRad = dm.createIntColumn("BRRAD");

      sumPoCorg.setColumns(repPoCorg.cloneColumns());

      sumPoCorg.addColumn(zadPl);
      sumPoCorg.addColumn(zadRc);
      sumPoCorg.addColumn(brRad);

//      sumPoCorg.dropColumn("CORG");
//      sumPoCorg.dropColumn("STOPA");
//      if (this.getRepMode() == 'A'){
//        sumPoCorg.dropColumn("MJOBR");
//        sumPoCorg.dropColumn("GODOBR");
//        sumPoCorg.dropColumn("DATUMISPL");
//      }

      sumPoCorg.open();
    }
    catch (Exception ex) {
      sumPoCorg.deleteAllRows();
    }

    sumPoCorg.insertRow(false);
    repPoCorg.first();
    STOPA = repPoCorg.getDouble("STOPA");

    do{
      sumPoCorg.setBigDecimal("SATI", sumPoCorg.getBigDecimal("SATI").add(repPoCorg.getBigDecimal("SATI")));
      sumPoCorg.setBigDecimal("BRUTO", sumPoCorg.getBigDecimal("BRUTO").add(repPoCorg.getBigDecimal("BRUTO")));
      sumPoCorg.setBigDecimal("DOPRINOSI", sumPoCorg.getBigDecimal("DOPRINOSI").add(repPoCorg.getBigDecimal("DOPRINOSI")));
      sumPoCorg.setBigDecimal("NETO", sumPoCorg.getBigDecimal("NETO").add(repPoCorg.getBigDecimal("NETO")));
      sumPoCorg.setBigDecimal("PORIPRIR", sumPoCorg.getBigDecimal("PORIPRIR").add(repPoCorg.getBigDecimal("PORIPRIR")));
      sumPoCorg.setBigDecimal("NAKNADE", sumPoCorg.getBigDecimal("NAKNADE").add(repPoCorg.getBigDecimal("NAKNADE")));
      sumPoCorg.setBigDecimal("DOPRPOD", sumPoCorg.getBigDecimal("DOPRPOD").add(repPoCorg.getBigDecimal("DOPRPOD")));
      sumPoCorg.setBigDecimal("ZADPL", sumPoCorg.getBigDecimal("ZADPL").add(repPoCorg.getBigDecimal("BRUTO").add(repPoCorg.getBigDecimal("DOPRPOD"))));
      sumPoCorg.setBigDecimal("ZADRC", sumPoCorg.getBigDecimal("ZADRC").add(repPoCorg.getBigDecimal("BRUTO").add(repPoCorg.getBigDecimal("DOPRPOD"))).add(repPoCorg.getBigDecimal("NAKNADE")));
    } while (repPoCorg.next());
    if (this.getRepMode() == 'O') {
      sumPoCorg.setInt("BRRAD", (sumPoCorg.getBigDecimal("SATI").divide(this.getFondSatiRada(),0)).intValue());
    } else {
      QueryDataSet temporary = ut.getNewQueryDataSet("SELECT max(mjobr) as mjobr, max(godobr) as godobr, max(rbrobr) as rbrobr FROM Kumulorgarh "+
          "WHERE godobr BETWEEN " + fieldSet.getShort("GODINAOD") + " AND " + fieldSet.getShort("GODINADO") + " "+
          "AND mjobr BETWEEN " + fieldSet.getShort("MJESECOD") + " AND " + fieldSet.getShort("MJESECDO") + " "+" GROUP BY mjobr, godobr, rbrobr");
      temporary.first();
      do{
        lookupData.getlookupData().raLocate(fondoviSati, new String[] {"MJESEC", "GODINA"},
            new String[] {String.valueOf(temporary.getShort("MJOBR")), String.valueOf(temporary.getShort("GODOBR"))});
        if (temporary.getRow() == 0){
          SUMFONDSATI = fondoviSati.getBigDecimal("SATIRAD").intValue();
        } else {
          SUMFONDSATI = SUMFONDSATI + fondoviSati.getBigDecimal("SATIRAD").intValue();
        }
      } while (temporary.next());
    }
  }

  protected void setInts() {
    if(this.getRepMode() == 'A'){
      mjOd = (int)fieldSet.getShort("MJESECOD");
      mjDo = (int)fieldSet.getShort("MJESECDO");
      godOd =(int)fieldSet.getShort("GODINAOD");
      godDo =(int)fieldSet.getShort("GODINADO");
    }
  }

  public DataSet getRepPoCorg(){
    return repPoCorg;
  }

  public DataSet getSumPoCorg(){
    return sumPoCorg;
  }

  public DataSet getRepPoCradMj(){
    return repPoCradMj;
  }

  public DataSet getRepPoCorgUnutarCradmj(){
    return repPoCorgUnutarCradMj;
  }

  public double getStopa(){
    return STOPA;
  }

  public String getMjesecOd(){
      String mjod;
      if (fieldSet.getShort("MJOBOD") < 10) mjod = "0" + fieldSet.getShort("MJOBOD");
      else mjod = String.valueOf(fieldSet.getShort("MJOBOD"));
      return mjod;
  }

  public String getMjesecDo(){
      String mjedo;
      if (fieldSet.getShort("MJOBDO") < 10) mjedo = "0" + fieldSet.getShort("MJOBDO");
      else mjedo = String.valueOf(fieldSet.getShort("MJOBDO"));
      return mjedo;
  }

  public String getGodOd(){
    return String.valueOf(fieldSet.getShort("GDOBOD"));
  }

  public String getGodDo(){
    return String.valueOf(fieldSet.getShort("GDOBDO"));
  }

  public java.math.BigDecimal getFondSatiRada(){
    return fondoviSati.getBigDecimal("SATIRAD");
  }

  public java.math.BigDecimal getFondSatiRada(short god, short mj){
    lookupData.getlookupData().raLocate(fondoviSati, new String[] {"GODINA", "MJESEC"}, new String[] {String.valueOf(god), String.valueOf(mj)});
    return fondoviSati.getBigDecimal("SATIRAD");
  }

  public int getSumFondSati(){
    return SUMFONDSATI;
  }

  public double getDoprpod(double BRUTO){
    return BRUTO * STOPA;
  }
}