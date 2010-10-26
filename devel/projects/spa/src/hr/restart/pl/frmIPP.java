package hr.restart.pl;

import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.raImages;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.text.VariantFormatter;

public class frmIPP extends frmSPL {
  HashMap datarow = new HashMap();
  private static frmIPP fIPP;
  
  frmTableDataView view = new frmTableDataView(true, false, true) {
    protected void OKPress() {
      hide();
      convert();
      ok_action_thread();
      
    }
  };
  
  boolean manual;
  JButton mbut = new JButton("Ruèni unos");
  
  public frmIPP() {
    super();
    fIPP = this;
    mbut.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        manualEntry();
      }
    });
    JPanel down = new JPanel(new BorderLayout());
    this.getContentPane().remove(okp);
    down.add(mbut, BorderLayout.WEST);
    down.add(okp);
    view.okp.jBOK.setText("OK");
    view.okp.jBOK.setIcon(raImages.getImageIcon(raImages.IMGOK));

    this.getContentPane().add(down, BorderLayout.SOUTH);
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
    System.out.println("bd = "+bd);
    if (formatter == null) formatter = dM.createBigDecimalColumn("OGLEDNA").getFormatter();
    Variant v = new Variant();
    v.setBigDecimal(bd);
    System.out.println("formated "+formatter.format(v));
    return formatter.format(v);
  }
  void manualEntry() {
    if (!Validacija()) return;
    okPress();
    rcc.EnabDisabAllLater(getJPan(), true);
    manual = true;
    
    StorageDataSet data = new StorageDataSet();
    data.setColumns(new Column[] {
        dM.createIntColumn("RBR", "Rbr"),
        dM.createStringColumn("PRIM", "Vrsta primitka", 50),
        dM.createIntColumn("BROJ", "Broj stjecatelja"),
        dM.createBigDecimalColumn("OSN", "Osnovica", 2),
        dM.createBigDecimalColumn("POR", "Porez", 2)
    });
    data.getColumn("BROJ").setTableName("dummy");
    data.getColumn("OSN").setTableName("dummy");
    data.getColumn("POR").setTableName("dummy");
    data.open();
    String[] prim = {"Plaæa", "Mirovina", "Drugi dohodak", "Dividende"};
    String[][] cols = {
        {"PLBROJ", "PLOSN", "PLPOR"},
        {"MIRBROJ", "MIROSN", "MIRPOR"},
        {"DDBROJ", "DDOSN", "DDPOR"},
        {"DIVBROJ", "DIVOSN", "DIVPOR"}
    };
    for (int i = 0; i < 4; i++) {
      data.insertRow(false);
      data.setInt("RBR", i + 1);
      data.setString("PRIM", prim[i]);
      data.setUnassignedNull("BROJ");
      data.setUnassignedNull("OSN");
      data.setUnassignedNull("POR");
      String br = (String) datarow.get(cols[i][0]);
      String osn = (String) datarow.get(cols[i][1]);
      String por = (String) datarow.get(cols[i][2]);
      if (br != null && br.length() > 0)
        data.setInt("BROJ", Aus.getNumber(br));
      if (osn != null && osn.length() > 0)
        data.setBigDecimal("OSN", Aus.getDecNumber(osn));
      if (por != null && por.length() > 0)
        data.setBigDecimal("POR", Aus.getDecNumber(por));
    }
    view.setDataSet(data);
    view.setSums(new String[] {"BROJ", "OSN", "POR"});
    view.show();
    view.resizeLater();
  }
  
  void convert() {
    StorageDataSet data = view.jp.getStorageDataSet();

    String[][] cols = {
        {"PLBROJ", "PLOSN", "PLPOR"},
        {"MIRBROJ", "MIROSN", "MIRPOR"},
        {"DDBROJ", "DDOSN", "DDPOR"},
        {"DIVBROJ", "DIVOSN", "DIVPOR"}
    };
    
    for (data.first(); data.inBounds(); data.next()) {
      if (!data.isNull("BROJ"))
        datarow.put(cols[data.getInt("RBR")-1][0], 
            Integer.toString(data.getInt("BROJ")));
      if (!data.isNull("OSN"))
        datarow.put(cols[data.getInt("RBR")-1][1], 
            format(data.getBigDecimal("OSN")));
      if (!data.isNull("POR"))
        datarow.put(cols[data.getInt("RBR")-1][2], 
            format(data.getBigDecimal("POR")));
    }
    datarow.put("UKUBROJ", view.jp.getSum("BROJ").toString());
    datarow.put("UKUOSN", format(view.jp.getSum("OSN")));
    datarow.put("UKUPOR", format(view.jp.getSum("POR")));
  }
  
  public void afterReport() {
    super.afterReport();
    manual = false;
  }
  
  private void createReport() {
    if (manual) return;
    raIniciranje.getInstance().posOrgsPl(fieldSet.getString("CORG"));
    datumispl = dm.getOrgpl().getTimestamp("DATUMISPL");
    short month = new Short(Util.getUtil().getMonth(datumispl)).shortValue();
    short year = new Short(Util.getUtil().getYear(datumispl)).shortValue();
    String godmj = fieldSet.getShort("GODINAOD")+Valid.getValid().maskZeroInteger(new Integer((int)fieldSet.getShort("MJESECOD")), 2);
System.out.println("frmIPP.godmj = "+godmj);    
    BigDecimal[] vals = Harach.getHaracMj(godmj, null, fieldSet.getString("CORG"));
    //ako ima u radnima gonjaj
    if (fieldSet.getShort("MJESECOD") == month && fieldSet.getShort("GODINAOD") == year) {
      BigDecimal[] vals2 = Harach.getHaracMj(null, null, fieldSet.getString("CORG"));
      for (int i = 0; i < vals.length; i++) {
        vals[i] = vals[i].add(vals2[i]);
      }
    }
    //if (fieldSet.getString("")) 
    datarow.put("TITLE", "Izvješæe o posebnom porezu na plaæe, mirovine i druge primitke \n u "+fieldSet.getShort("MJESECOD")+
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
