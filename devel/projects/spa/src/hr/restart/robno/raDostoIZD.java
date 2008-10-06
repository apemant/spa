package hr.restart.robno;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.borland.dx.dataset.Column;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import hr.restart.swing.JraButton;
import hr.restart.swing.JraRadioButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raButtonGroup;
import hr.restart.util.JlrNavField;
import hr.restart.util.raCommonClass;
import hr.restart.util.raUpitLite;

public class raDostoIZD extends raUpitLite {
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
  hr.restart.util.Util ut = hr.restart.util.Util.getUtil();
  raCommonClass rcc = raCommonClass.getraCommonClass();

  QueryDataSet fieldSet = new QueryDataSet();

  Column datum = new Column();
  Column cskl = new Column();

  JPanel mainPanel = new JPanel();

  XYLayout xYLayout1 = new XYLayout();
  XYLayout xYLayout2 = new XYLayout();

  JraTextField jraDATUM = new JraTextField();
  JLabel jlDatum = new JLabel();

  JLabel jlCskl = new JLabel();
  JLabel jlaCskl = new JLabel();
  JLabel jlaNazskl = new JLabel();
  JraButton jbSelCskl = new JraButton();
  JlrNavField jlrCskl = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrNazskl = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  TitledBorder titledBorder1;

  String year, actionPerformed, qInsertToInventura, qUpdateRest, qDeleteInventura, ac;

  public raDostoIZD() {
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public boolean isIspis() {
    return false;
  }

  public boolean ispisNow() {
    return false;
  }
  
  public void componentShow() {
    
    
  }

  public void firstESC() {
    // TODO Auto-generated method stub
    
  }

  public void okPress() {
    DOStoIZD d2i = new DOStoIZD();
    d2i.setDatumDo(fieldSet.getTimestamp("DATUM"));
    d2i.setCskl(fieldSet.getString("CSKL"));
    d2i.start();
  }
  public void showMessage() {
    JOptionPane.showMessageDialog(this.getWindow(), "Obraèun uspješan!");
  }
  public boolean runFirstESC() {
    // TODO Auto-generated method stub
    return false;
  }

  private void jbInit() throws Exception {

    datum = dm.createTimestampColumn("DATUM");
    cskl = dm.createStringColumn("CSKL",0);

    fieldSet.setColumns(new Column[] {datum, cskl});
    fieldSet.open();

    jlDatum.setText("Datum");

    mainPanel.setLayout(xYLayout1);

    jlCskl.setText("Skladište");
    jlaCskl.setHorizontalAlignment(SwingConstants.CENTER);
    jlaCskl.setText("Šifra");
    jlaNazskl.setHorizontalAlignment(SwingConstants.CENTER);
    jlaNazskl.setText("Naziv");

    jraDATUM.setHorizontalAlignment(SwingConstants.CENTER);
    jraDATUM.setDataSet(fieldSet);
    jraDATUM.setColumnName("DATUM");

    jlrCskl.setColumnName("CSKL");
    jlrCskl.setColNames(new String[] {"NAZSKL"});
    jlrCskl.setDataSet(fieldSet);
    jlrCskl.setTextFields(new javax.swing.text.JTextComponent[] {jlrNazskl});
    jlrCskl.setVisCols(new int[] {0, 1});
    jlrCskl.setSearchMode(0);
    jlrCskl.setRaDataSet(Util.getSkladFromCorg());
    jlrCskl.setNavButton(jbSelCskl);

    jlrNazskl.setColumnName("NAZSKL");
    jlrNazskl.setNavProperties(jlrCskl);
    jlrNazskl.setSearchMode(1);

    this.setJPan(mainPanel);

    xYLayout1.setWidth(595);
    xYLayout1.setHeight(135);

    titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(139, 139, 139)),"");

    mainPanel.add(jlaCskl, new XYConstraints(151, 13, 98, -1));
    mainPanel.add(jlaNazskl, new XYConstraints(256, 13, 293, -1));
    mainPanel.add(jbSelCskl, new XYConstraints(555, 30, 21, 21));
    mainPanel.add(jlCskl, new XYConstraints(15, 30, -1, -1));
    mainPanel.add(jlrCskl, new XYConstraints(150, 30, 100, -1));
    mainPanel.add(jlrNazskl, new XYConstraints(255, 30, 295, -1));
    mainPanel.add(jlDatum,    new XYConstraints(15, 55, -1, -1));
    mainPanel.add(jraDATUM,    new XYConstraints(150, 55, 100, -1));



    hr.restart.zapod.OrgStr.getOrgStr().addKnjigChangeListener(new hr.restart.zapod.raKnjigChangeListener(){
      public void knjigChanged(String oldKnj, String newKnj){
        jlrCskl.setRaDataSet(Util.getSkladFromCorg());
      }
    });
  }
}
