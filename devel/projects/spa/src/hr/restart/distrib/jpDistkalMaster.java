package hr.restart.distrib;

import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashSet;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import com.borland.jbcl.layout.*;
import com.borland.dx.dataset.*;
import com.borland.dx.sql.dataset.*;
import hr.restart.swing.*;
import hr.restart.baza.*;
import hr.restart.util.*;


public class jpDistkalMaster extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();

  StorageDataSet aaset;
  frmDistkal fDistkal;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCdistkal = new JLabel();
  JLabel jlCinhdistkal = new JLabel();
  JLabel jlOpis = new JLabel();
  JraButton jbSelCinheritdistkal = new JraButton();
  JraTextField jraCdistkal = new JraTextField();
  JraTextField jraOpis = new JraTextField();
  JlrNavField jlrCinheritdistkal = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrOpis = new JlrNavField() {
    public void after_lookUp() {
    }
  };

  JPanel jpAutoAdd = new JPanel();
  XYLayout aalay = new XYLayout();
  
  JraTextField jraDatumfrom = new JraTextField();
  JraTextField jraDatumto = new JraTextField();
  
  JLabel jlDatum = new JLabel("U periodu");
  JLabel jlDodaj = new JLabel("Dodaj");
  JraComboBox jcbSvaki = new JraComboBox(new String[] {
      "svaki",
      "svaki drugi",
      "svaki prvi u mjesecu",
      "svaki drugi u mjesecu",
      "svaki treæi u mjesecu",
      "svaki èetvrti u mjesecu"
      });
  String[] oDan = {"dan","radni dan","ponedjeljak","utorak","srijeda","èetvrtak","petak","subota","nedjelja"};
  JraComboBox jcbDan = new JraComboBox(oDan);
  JLabel JlBroj = new JLabel("Poèetni broj");
  JraTextField jraBroj = new JraTextField();
  JLabel jlAkcija = new JLabel("Akcija");
  JraComboBox jcbFLAGADD = new JraComboBox(new String[] {"Dodaj","Izuzmi"});
  JButton jbGen = new JButton("Generiraj datume");
  
  public jpDistkalMaster(frmDistkal md) {
    try {
      fDistkal = md;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void BindComponents(DataSet ds) {
    jraCdistkal.setDataSet(ds);
    jraOpis.setDataSet(ds);
    jlrCinheritdistkal.setDataSet(ds);
    jraDatumfrom.setDataSet(aaset);
    jraDatumto.setDataSet(aaset);
    jraBroj.setDataSet(aaset);

  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(546);
    lay.setHeight(300);

    jbSelCinheritdistkal.setText("...");
    jlCdistkal.setText("Oznaka");
    jlCinhdistkal.setText("Naslijeðen od");
    jlOpis.setText("Opis");
    jraCdistkal.setColumnName("CDISTKAL");
    jraOpis.setColumnName("OPIS");

    jlrCinheritdistkal.setColumnName("CINHERITDISTKAL");
    jlrCinheritdistkal.setNavColumnName("CDISTKAL");
    jlrCinheritdistkal.setColNames(new String[] {"OPIS"});
    jlrCinheritdistkal.setTextFields(new JTextComponent[] {jlrOpis});
    jlrCinheritdistkal.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCinheritdistkal.setSearchMode(0);
    jlrCinheritdistkal.setRaDataSet(Distkal.getDataModule().copyDataSet());
    jlrCinheritdistkal.setNavButton(jbSelCinheritdistkal);

    jlrOpis.setColumnName("OPIS");
    jlrOpis.setNavProperties(jlrCinheritdistkal);
    jlrOpis.setSearchMode(1);

    jpDetail.add(jbSelCinheritdistkal, new XYConstraints(510, 70, 21, 21));
    jpDetail.add(jlCdistkal, new XYConstraints(15, 20, -1, -1));
    jpDetail.add(jlCinhdistkal, new XYConstraints(15, 70, -1, -1));
    jpDetail.add(jlOpis, new XYConstraints(15, 45, -1, -1));
    jpDetail.add(jlrCinheritdistkal, new XYConstraints(150, 70, 75, -1));
    jpDetail.add(jlrOpis, new XYConstraints(230, 70, 275, -1));
    jpDetail.add(jraCdistkal, new XYConstraints(150, 20, 75, -1));
    jpDetail.add(jraOpis, new XYConstraints(150, 45, 355, -1));
    
    aaset = new StorageDataSet();
    aaset.addColumn(dM.createIntColumn("BROJ"));
    aaset.addColumn(dM.createTimestampColumn("DATUMFROM"));
    aaset.addColumn(dM.createTimestampColumn("DATUMTO"));
    jraDatumfrom.setColumnName("DATUMFROM");
    jraDatumto.setColumnName("DATUMTO");
    jraBroj.setColumnName("BROJ");
    jbGen.addActionListener(new ActionListener() {
      
      public void actionPerformed(ActionEvent e) {
        generate();
      }
    });
    
    jpAutoAdd.setLayout(aalay);
    aalay.setWidth(500);
    aalay.setHeight(110);
    jpAutoAdd.setBorder(BorderFactory.createTitledBorder("Generiranje datuma"));
    
    jpAutoAdd.add(jlDatum, new XYConstraints(15, 5, -1, -1));
    jpAutoAdd.add(jraDatumfrom, new XYConstraints(140, 5, 100, -1));
    jpAutoAdd.add(jraDatumto, new XYConstraints(245, 5, 100, -1));
    
    jpAutoAdd.add(jlDodaj, new XYConstraints(15, 30, -1, -1));
    jpAutoAdd.add(jcbSvaki, new XYConstraints(140, 30, 205, -1));
    jpAutoAdd.add(jcbDan, new XYConstraints(350, 30, 145, -1));
    jpAutoAdd.add(JlBroj, new XYConstraints(15, 65, -1, -1));
    jpAutoAdd.add(jraBroj, new XYConstraints(140, 65, 120, -1));
    jpAutoAdd.add(jlAkcija, new XYConstraints(300, 65, -1, -1));
    jpAutoAdd.add(jcbFLAGADD, new XYConstraints(350, 65, 145, -1));
    jpAutoAdd.add(jbGen, new XYConstraints(350, 95, 145, -1));
    jpDetail.add(jpAutoAdd, new XYConstraints(5, 100, 531, 155));

    BindComponents(fDistkal.getMasterSet());
    
    this.add(jpDetail, BorderLayout.CENTER);
  }

  protected void generate() {
    if (fDistkal.raMaster.getMode() != 'I') {
      JOptionPane.showMessageDialog(getTopLevelAncestor(), "Prvo morate dodati kalendar da bi generirali datume", "Greška", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    if (vl.isEmpty(jraDatumfrom) || vl.isEmpty(jraDatumto) || vl.isEmpty(jraBroj)) return;
    
    QueryDataSet old = StDistkal.getDataModule().getTempSet(Condition.equal("CDISTKAL", fDistkal.getMasterSet()));
    old.open();
    
    HashSet dats = new HashSet();
    for (old.first(); old.inBounds(); old.next()) {
    	dats.add(old.getTimestamp("DATISP").toString().substring(0, 10));
    }
    
    int svaki = jcbSvaki.getSelectedIndex();
    int dan = jcbDan.getSelectedIndex();
    int add = jcbFLAGADD.getSelectedIndex();
    
    Timestamp from = Util.getUtil().getFirstSecondOfDay(aaset.getTimestamp("DATUMFROM"));
    Timestamp to = Util.getUtil().getLastSecondOfDay(aaset.getTimestamp("DATUMTO"));
    
    Calendar cal = Calendar.getInstance();
    cal.setTime(from);
    

    // String[] oDan = {"dan","radni dan","ponedjeljak","utorak","srijeda","èetvrtak","petak","subota","nedjelja"};
    
    // "svaki", "svaki drugi",  "svaki prvi u mjesecu",  "svaki drugi u mjesecu",     "svaki treæi u mjesecu",    "svaki èetvrti u mjesecu"
    
    boolean even = false;
    for (cal.setTime(from); !cal.getTime().after(to); cal.set(cal.DATE, cal.get(cal.DATE) + 1)) {	
    	
    	int dw = cal.get(cal.DAY_OF_WEEK);
    	int md = cal.get(cal.DAY_OF_MONTH);
    	
    	if (svaki == 0 || svaki == 1) {
    		if (dan == 1 && (dw == cal.SUNDAY || dw == cal.SATURDAY)) continue;
    		if (dan == 2 && dw != cal.MONDAY ||
    			dan == 3 && dw != cal.TUESDAY ||
    			dan == 4 && dw != cal.WEDNESDAY ||
    			dan == 5 && dw != cal.THURSDAY ||
    			dan == 6 && dw != cal.FRIDAY ||
    			dan == 7 && dw != cal.SATURDAY ||
    			dan == 8 && dw != cal.SUNDAY) continue;
    		even = !even;
    		if (svaki == 1 && !even) continue;
    	} else if (svaki == 2) {
    		if (md != 1) continue;
    	} else if (svaki == 3) {
    		if (md != 2) continue;
    	} else if (svaki == 4) {
    		if (md != 3) continue;
    	} else if (svaki == 5) {
    		if (md != 4) continue;
    	} 
    	
    	Timestamp dat = new Timestamp(cal.getTime().getTime());
    	if (dats.contains(dat.toString().substring(0, 10))) continue;
    	
    	old.insertRow(false);
    	old.setInt("CDISTKAL", fDistkal.getMasterSet().getInt("CDISTKAL"));
    	old.setTimestamp("DATISP", dat);
    	old.setString("FLAGADD", add == 0 ? "D" : "N");
    }
    
    int broj = aaset.getInt("BROJ");
    int pocbr = broj;
    old.setSort(new SortDescriptor(new String[] {"DATISP"}));
    for (old.first(); old.inBounds(); old.next()) {
    	old.setInt("BROJ", broj++);
    }
    
    old.saveChanges();
    fDistkal.getDetailSet().refresh();
    fDistkal.getDetailSet().last();
    JOptionPane.showMessageDialog(fDistkal.raMaster.getWindow(), "Dodani brojevi od " + pocbr + ". do " + (broj-1) + ".",
    		"Generiranje gotovo", JOptionPane.INFORMATION_MESSAGE);
    
    
    /*JOptionPane.showMessageDialog(getTopLevelAncestor(), "Dodati svaki:"+svaki+" dan:"+dan+" add:"+add+
        " od: "+aaset.getTimestamp("DATUMFROM")+" do:"+aaset.getTimestamp("DATUMTO")+" od broja:"+aaset.getInt("BROJ"));*/
  }

  public void initAA() {
    aaset.open();
    aaset.deleteAllRows();
    aaset.insertRow(false);
  }
}
