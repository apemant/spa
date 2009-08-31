package hr.restart.pl;

import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.raNumberMask;

import java.math.BigDecimal;
import java.util.HashMap;

import com.borland.dx.dataset.Variant;
import com.borland.dx.text.InvalidFormatException;
import com.borland.dx.text.VariantFormatter;

public class frmIPP extends frmSPL {
  HashMap datarow = new HashMap();
  private static frmIPP fIPP;
  public frmIPP() {
    super();
    fIPP = this;
  }
  
  public static frmIPP getFrmIPP() {
    return fIPP;
  }

  public boolean Validacija() {
     if (vl.isEmpty(jlrCorg)) return false;
     //createReport();
     return true;
  }
  public void okPress() {
    super.okPress();
    createReport();
  }
  VariantFormatter formatter;
  private String format(BigDecimal bd) {
    if (formatter == null) formatter = dM.createBigDecimalColumn("OGLEDNA").getFormatter();
    Variant v = new Variant();
    v.setBigDecimal(bd);
    return formatter.format(v);
  }
  private void createReport() {
    BigDecimal[] vals = Harach.getHaracMj(null, null, fieldSet.getString("CORG"));
    datarow.put("TITLE", "Izvješæe o posebnom porezu na plaæe, mirovine i druge primitke u "+fieldSet.getShort("MJESECOD")+
        ". mjesecu "+fieldSet.getShort("GODINAOD")+". godine");
    
    datarow.put("NAZIV", getKnjNaziv());
    datarow.put("ADRESA", getKnjAdresa()+", "+getKnjMjesto());
    datarow.put("MB", getKnjMatbroj());
    String wrow = frmParam.getParam("robno", "IPPred","1","U koji red u tocki II. se upisuju iznosi na IPP-u");
    try {
      datarow.put("PLOSN", (wrow.equals("1"))?format(vals[0]):"");
      datarow.put("PLPOR", (wrow.equals("1"))?format(vals[1]):"");
      datarow.put("PLBROJ", (wrow.equals("1"))?vals[2].intValue()+"":"");

      datarow.put("MIROSN", (wrow.equals("2"))?format(vals[0]):"");
      datarow.put("MIRPOR", (wrow.equals("2"))?format(vals[1]):"");
      datarow.put("MIRBROJ", (wrow.equals("2"))?vals[2].intValue()+"":"");

      datarow.put("DDOSN", (wrow.equals("3"))?format(vals[0]):"");
      datarow.put("DDPOR", (wrow.equals("3"))?format(vals[1]):"");
      datarow.put("DDBROJ", (wrow.equals("3"))?vals[2].intValue()+"":"");

      datarow.put("DIVOSN", (wrow.equals("4"))?format(vals[0]):"");
      datarow.put("DIVPOR", (wrow.equals("4"))?format(vals[1]):"");
      datarow.put("DIVBROJ", (wrow.equals("4"))?vals[2].intValue()+"":"");


      datarow.put("UKUOSN", format(vals[0]));
      datarow.put("UKUPOR", format(vals[1]));
      datarow.put("UKUBROJ", vals[2].intValue()+"");
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  protected void setReportProviders() {
    addJasper("repIPP", "hr.restart.pl.repIPP", "ipp.jrxml", "IPP Obrazac");
  }

  public HashMap getDatarow() {
    return datarow;
  }
}
