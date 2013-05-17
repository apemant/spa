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


public class jpDistkalDetail extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();

  frmDistkal fDistkal;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlBroj = new JLabel();
  JLabel jlDatisp = new JLabel();
  JLabel jlFlagadd = new JLabel();
  JraTextField jraBroj = new JraTextField();
  JraTextField jraDatisp = new JraTextField();
//  JraTextField jraFlagadd = new JraTextField();
  JraComboBox jcbFlagadd = new JraComboBox(new String[] {"Dodaj","Izuzmi"});
  
  public jpDistkalDetail(frmDistkal md) {
    try {
      fDistkal = md;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void BindComponents(DataSet ds) {
    jraBroj.setDataSet(ds);
    jraDatisp.setDataSet(ds);
//    jraFlagadd.setDataSet(ds);
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(285);
    lay.setHeight(110);

    jlBroj.setText("Broj");
    jlDatisp.setText("Datum");
    jlFlagadd.setText("Akcija");
    jraBroj.setColumnName("BROJ");
    jraDatisp.setColumnName("DATISP");
//    jraFlagadd.setColumnName("FLAGADD");

    jpDetail.add(jlBroj, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlDatisp, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlFlagadd, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jraBroj, new XYConstraints(150, 45, 120, -1));
    jpDetail.add(jraDatisp, new XYConstraints(150, 20, 120, -1));
    jpDetail.add(jcbFlagadd, new XYConstraints(150, 70, 120, -1));

    BindComponents(fDistkal.getDetailSet());
    /**@todo: Odkomentirati sljedecu liniju :) */
    this.add(jpDetail, BorderLayout.CENTER);
  }
}
