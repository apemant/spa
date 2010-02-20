package hr.restart.robno;

import com.borland.dx.dataset.DataSet;

import hr.restart.util.Aus;
import hr.restart.util.VarStr;
import hr.restart.util.reports.raReportData;


public class repIzlazniNorm extends repIzlazni {

  private String normNazart, normKol, normJm;

  public raReportData getRow(int i) {
    raReportData rd = super.getRow(i);
    findNorm();
    return rd;
  }
  
  private void findNorm() {
    normNazart = normKol = normJm = "";
    if (!raVart.isNorma(ds.getInt("CART")) || 
        raVart.isStanje(ds.getInt("CART"))) return;
    
    DataSet exp = Aut.getAut().expandArt(
              ds.getInt("CART"), ds.getBigDecimal("KOL"), false);
    if (exp.rowCount() == 0) return;

    VarStr naz = new VarStr();
    VarStr kol = new VarStr();
    VarStr jm = new VarStr();
    for (exp.first(); exp.inBounds(); exp.next()) {
      naz.append(exp.getString("NAZART")).append('\n');
      kol.append(Aus.formatBigDecimal(exp.getBigDecimal("KOL"))).append('\n');
      jm.append(exp.getString("JM")).append('\n');
    }
    normNazart = naz.toString();
    normKol = kol.toString();
    normJm = jm.toString();
  }
  
  public String getNORMNAZART() {
    return normNazart;
  }
  
  public String getNORMKOL() {
    return normKol;
  }
  
  public String getNORMJM() {
    return normJm;
  }
}
