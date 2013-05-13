package hr.restart.distrib;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import com.borland.jbcl.layout.*;
import com.borland.dx.dataset.*;
import com.borland.dx.sql.dataset.*;
import hr.restart.swing.*;
import hr.restart.baza.*;
import hr.restart.util.*;
import hr.restart.zapod.Sifrarnici;


public class jpDistart extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  frmDistart fDistart;
  JPanel jpDetail = new JPanel();
  
  private int lastcpar = -1;
  private QueryDataSet pjp;

  XYLayout lay = new XYLayout();
  JLabel jlAdr = new JLabel();
//  JLabel jlAktiv = new JLabel();
  JLabel jlCgrart = new JLabel();
  JLabel jlDatzisp = new JLabel();
  JLabel jlFax = new JLabel();
  JLabel jlKol = new JLabel();
  JLabel jlNaziv = new JLabel();
  JLabel jlPar = new JLabel();
  JLabel jlPj = new JLabel();
  JLabel jlSifdist = new JLabel();
  JLabel jlSaldo = new JLabel();
  JLabel jlaDatist = new JLabel();
  JLabel jlaDatzisp = new JLabel();
  JLabel jlaKol = new JLabel();
  JLabel jlaKoliz = new JLabel();
  JLabel jlaKolul = new JLabel();
  JraButton jbSelCgrart = new JraButton();
  JraButton jbSelCpar = new JraButton();
  JraButton jbSelPj = new JraButton();
  JraButton jbSelSifdist = new JraButton();
  JraButton jbSelDistkal = new JraButton();
  JraTextField jraSaldo = new JraTextField();
  JraTextField jraAdr = new JraTextField();
  JraCheckBox jraAktiv = new JraCheckBox();
  JraTextField jraDatist = new JraTextField();
  JraTextField jraDatzisp = new JraTextField();
  JraTextField jraEmadr = new JraTextField();
  JraTextField jraFax = new JraTextField();
  JraTextField jraKol = new JraTextField();
  JraTextField jraKoliz = new JraTextField() {
    public void focusLost(FocusEvent e) {
      super.focusLost(e);
      calcSaldo();
    };
  };
  JraTextField jraKolul = new JraTextField() {
    public void focusLost(FocusEvent e) {
      super.focusLost(e);
      calcSaldo();
    };
  };
  JraTextField jraMj = new JraTextField();
  JraTextField jraNaziv = new JraTextField();
  JraTextField jraPbr = new JraTextField();
  JraTextField jraTelfax = new JraTextField();
  JlrNavField jlrCpar = new JlrNavField() {
    public void after_lookUp() {
      filterPjpar(fDistart.getRaQueryDataSet().getInt("CPAR"));
      jlrPj.setRaDataSet(pjp);
      jlrNazpj.setRaDataSet(pjp);
    }
  };
  JlrNavField jlrNazpar = new JlrNavField() {
    public void after_lookUp() {
      jlrCpar.after_lookUp();
    }
  };
  JlrNavField jlrNazgrart = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCgrart = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazpj = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrPj = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrSifdist = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCdistkal = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrOpisdistkal = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  public jpDistart(frmDistart f) {
    try {
      fDistart = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void BindComponents(DataSet ds) {
    jraSaldo.setDataSet(getSaldoSet());
    jraAdr.setDataSet(ds);
    jraAktiv.setDataSet(ds);
    jraDatist.setDataSet(ds);
    jraDatzisp.setDataSet(ds);
    jraEmadr.setDataSet(ds);
    jraFax.setDataSet(ds);
    jraKol.setDataSet(ds);
    jraKoliz.setDataSet(ds);
    jraKolul.setDataSet(ds);
    jraMj.setDataSet(ds);
    jraNaziv.setDataSet(ds);
    jraPbr.setDataSet(ds);
    jraTelfax.setDataSet(ds);
    jlrCpar.setDataSet(ds);
    jlrCgrart.setDataSet(ds);
    jlrPj.setDataSet(ds);
    jlrSifdist.setDataSet(ds);
    jlrCdistkal.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(546);
    lay.setHeight(370);

    jbSelCgrart.setText("...");
    jbSelCpar.setText("...");
    jbSelPj.setText("...");
    jbSelSifdist.setText("...");
    jlAdr.setText("Adresa");
    jraAktiv.setText("Isporuka");
    jraAktiv.setHorizontalAlignment(SwingConstants.LEFT);
    jlCgrart.setText("Grupa");
    jlDatzisp.setText("Datum isporuke");
    jlFax.setText("Tel/fax/e-mail");
    jlKol.setText("Kolièine");
    jlNaziv.setText("Naziv");
    jlPar.setText("Partner");
    jlPj.setText("PJ");
    jlSifdist.setText("Distributer");
    jlSaldo.setHorizontalAlignment(SwingConstants.TRAILING);
    jlSaldo.setText("Saldo");
    jlaDatist.setHorizontalAlignment(SwingConstants.CENTER);
    jlaDatist.setText("Isteka");
    jlaDatzisp.setHorizontalAlignment(SwingConstants.CENTER);
    jlaDatzisp.setText("Prošle");
    jlaKol.setHorizontalAlignment(SwingConstants.TRAILING);
    jlaKol.setText("Po pošiljci");
    jlaKoliz.setHorizontalAlignment(SwingConstants.TRAILING);
    jlaKoliz.setText("Isporuèena");
    jlaKolul.setHorizontalAlignment(SwingConstants.TRAILING);
    jlaKolul.setText("Naruèena");
    jraSaldo.setColumnName("SALDO");
    rcc.setLabelLaF(jraSaldo, false);
    jraAdr.setColumnName("ADR");
    jraAktiv.setColumnName("AKTIV");
    jraAktiv.setSelectedDataValue("D");
    jraAktiv.setUnselectedDataValue("N");
    jraDatist.setColumnName("DATIST");
    jraDatzisp.setColumnName("DATZISP");
    jraEmadr.setColumnName("EMADR");
    jraFax.setColumnName("TEL");
    jraKol.setColumnName("KOL");
    jraKoliz.setColumnName("KOLIZ");
    jraKolul.setColumnName("KOLUL");
    jraMj.setColumnName("MJ");
    jraNaziv.setColumnName("NAZIV");
    jraPbr.setColumnName("PBR");
    jraTelfax.setColumnName("TELFAX");

    jlrCpar.setColumnName("CPAR");
    jlrCpar.setColNames(new String[] {"NAZPAR"});
    jlrCpar.setTextFields(new JTextComponent[] {jlrNazpar});
    jlrCpar.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCpar.setSearchMode(0);
    jlrCpar.setRaDataSet(dm.getPartneri());
    jlrCpar.setNavButton(jbSelCpar);

    jlrNazpar.setColumnName("NAZPAR");
    jlrNazpar.setNavProperties(jlrCpar);
    jlrNazpar.setSearchMode(1);

    jlrCgrart.setColumnName("CGRART");
    jlrCgrart.setColNames(new String[] {"NAZGRART"});
    jlrCgrart.setTextFields(new JTextComponent[] {jlrNazgrart});
    jlrCgrart.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCgrart.setSearchMode(0);
    jlrCgrart.setRaDataSet(dm.getGrupart());
    jlrCgrart.setNavButton(jbSelCgrart);

    jlrNazgrart.setColumnName("NAZGRART");
    jlrNazgrart.setNavProperties(jlrCgrart);
    jlrNazgrart.setSearchMode(1);

    jlrPj.setColumnName("PJ");
    jlrPj.setColNames(new String[] {"NAZPJ"});
    jlrPj.setTextFields(new JTextComponent[] {jlrNazpj});
    jlrPj.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrPj.setSearchMode(0);
    jlrPj.setRaDataSet(getPjpar());
    jlrPj.setNavButton(jbSelPj);

    jlrNazpj.setColumnName("NAZPJ");
    jlrNazpj.setNavProperties(jlrPj);
    jlrNazpj.setSearchMode(1);

    jlrSifdist.setColumnName("SIFDIST");
    jlrSifdist.setNavColumnName("CSIF");
    jlrSifdist.setColNames(new String[] {"NAZIV"});
    jlrSifdist.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrSifdist.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrSifdist.setSearchMode(0);
    jlrSifdist.setRaDataSet(Sifrarnici.getSifre("DIST"));
    jlrSifdist.setNavButton(jbSelSifdist);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrSifdist);
    jlrNaziv.setSearchMode(1);
//***************
    jlrCdistkal.setColumnName("CDISTKAL");
    jlrCdistkal.setColNames(new String[] {"OPIS"});
    jlrCdistkal.setTextFields(new JTextComponent[] {jlrOpisdistkal});
    jlrCdistkal.setVisCols(new int[] {0, 1, 2}); /**@todo: Dodati visible cols za lookup frame */
    jlrCdistkal.setSearchMode(0);
    jlrCdistkal.setRaDataSet(Distkal.getDataModule().getQueryDataSet());
    jlrCdistkal.setNavButton(jbSelDistkal);

    jlrOpisdistkal.setColumnName("OPIS");
    jlrOpisdistkal.setNavProperties(jlrCdistkal);
    jlrOpisdistkal.setSearchMode(1);

//***************
    jpDetail.add(jbSelCgrart, new XYConstraints(510, 215, 21, 21));
    jpDetail.add(jbSelCpar, new XYConstraints(510, 20, 21, 21));
    jpDetail.add(jbSelPj, new XYConstraints(510, 45, 21, 21));
    jpDetail.add(jbSelSifdist, new XYConstraints(510, 145, 21, 21));
    jpDetail.add(jlAdr, new XYConstraints(15, 95, -1, -1));
    jpDetail.add(jraAktiv, new XYConstraints(15, 327, -1, -1));
    jpDetail.add(jlCgrart, new XYConstraints(15, 215, -1, -1));
    jpDetail.add(jlDatzisp, new XYConstraints(15, 302, -1, -1));
    jpDetail.add(jlFax, new XYConstraints(15, 120, -1, -1));
    jpDetail.add(jlKol, new XYConstraints(15, 190, -1, -1));
    jpDetail.add(jlNaziv, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlPar, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlPj, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlSifdist, new XYConstraints(15, 145, -1, -1));
    jpDetail.add(jlSaldo, new XYConstraints(421, 173, 83, -1));
    jpDetail.add(jlaDatist, new XYConstraints(391, 285, 118, -1));
    jpDetail.add(jlaDatzisp, new XYConstraints(151, 285, 118, -1));
    jpDetail.add(jlaKol, new XYConstraints(151, 173, 83, -1));
    jpDetail.add(jlaKoliz, new XYConstraints(331, 173, 83, -1));
    jpDetail.add(jlaKolul, new XYConstraints(241, 173, 83, -1));
    jpDetail.add(jlrCgrart, new XYConstraints(150, 215, 75, -1));
    jpDetail.add(jlrCpar, new XYConstraints(150, 20, 75, -1));
    jpDetail.add(jlrNazgrart, new XYConstraints(230, 215, 275, -1));
    jpDetail.add(jlrNaziv, new XYConstraints(230, 145, 275, -1));
    jpDetail.add(jlrNazpar, new XYConstraints(230, 20, 275, -1));
    jpDetail.add(jlrNazpj, new XYConstraints(230, 45, 275, -1));
    jpDetail.add(jlrPj, new XYConstraints(150, 45, 75, -1));
    jpDetail.add(jlrSifdist, new XYConstraints(150, 145, 75, -1));
    jpDetail.add(jraSaldo, new XYConstraints(420, 190, 85, -1));
    jpDetail.add(jraAdr, new XYConstraints(150, 95, 200, -1));
    jpDetail.add(jraDatist, new XYConstraints(390, 302, 120, -1));
    jpDetail.add(jraDatzisp, new XYConstraints(150, 302, 120, -1));
    jpDetail.add(jraEmadr, new XYConstraints(360, 120, 145, -1));
    jpDetail.add(jraFax, new XYConstraints(150, 120, 100, -1));
    jpDetail.add(jraKol, new XYConstraints(150, 190, 85, -1));
    jpDetail.add(jraKoliz, new XYConstraints(330, 190, 85, -1));
    jpDetail.add(jraKolul, new XYConstraints(240, 190, 85, -1));
    jpDetail.add(jraMj, new XYConstraints(410, 95, 95, -1));
    jpDetail.add(jraNaziv, new XYConstraints(150, 70, 355, -1));
    jpDetail.add(jraPbr, new XYConstraints(355, 95, 50, -1));
    jpDetail.add(jraTelfax, new XYConstraints(255, 120, 100, -1));

    jpDetail.add(jlrCdistkal, new XYConstraints(150, 257, 75, -1));
    jpDetail.add(jlrOpisdistkal, new XYConstraints(230, 257, 275, -1));
    jpDetail.add(jbSelDistkal, new XYConstraints(510, 257, 21, 21));
    jpDetail.add(new JLabel("Distr. kalendar"), new XYConstraints(15, 257, -1, -1));
    
    this.add(jpDetail, BorderLayout.CENTER);
  }


  private DataSet getPjpar() {
    pjp = Pjpar.getDataModule().getFilteredDataSet(Condition.nil);
    return pjp;
  }
  private DataSet filterPjpar(int cpar) {
    if (pjp != null && lastcpar == cpar) {
      return pjp;
    } 
    pjp = Pjpar.getDataModule().getFilteredDataSet(Condition.equal("CPAR", cpar));
    pjp.open();
    lastcpar = cpar;
    return pjp;
  }
  
  StorageDataSet saldoset;
  public DataSet getSaldoSet() {
    if (saldoset == null) {
      saldoset = new StorageDataSet();
      saldoset.addColumn(dM.createBigDecimalColumn("SALDO"));
      saldoset.open();
    }
    return saldoset;
  }

  public void calcSaldo() {
    getSaldoSet().setBigDecimal("SALDO", fDistart.getRaQueryDataSet().getBigDecimal("KOLUL").subtract(fDistart.getRaQueryDataSet().getBigDecimal("KOLIZ")));
  }
}
