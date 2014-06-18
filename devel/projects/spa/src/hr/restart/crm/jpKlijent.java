package hr.restart.crm;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.HashMap;

import hr.restart.baza.KlijentStat;
import hr.restart.baza.Segmentacija;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.util.JlrNavField;
import hr.restart.util.raComboBox;

import javax.swing.FocusManager;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.DataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpKlijent extends JPanel {

  JraTextField jraNAZIV = new JraTextField();
  JraTextField jraADR = new JraTextField();
  
  JlrNavField jlrPBR = new JlrNavField();
  JlrNavField jlrMJ = new JlrNavField();
  JlrNavField jlrCMJ = new JlrNavField();
  JlrNavField jlrCZUP = new JlrNavField();
  JraTextField jraTEL = new JraTextField();
  JraTextField jraTELFAX = new JraTextField();
  JraTextField jraEMADR = new JraTextField();
  JraTextField jraWEBADR = new JraTextField();
  JraTextField jraMB = new JraTextField();
  JraTextField jraOIB = new JraTextField();
  
  JLabel dispCol = new JLabel();
  Color defColor = dispCol.getBackground();
  raComboBox rcbStatus = new raComboBox() {
    public void this_itemStateChanged() {
      super.this_itemStateChanged();
      setColor();
    }
  };
  raComboBox rcbSegment = new raComboBox();
  JraButton jbGetMj = new JraButton(); 
  
  HashMap cols = new HashMap();
  
  public jpKlijent() {
    setLayout(new XYLayout(725, 190));
    jraNAZIV.setColumnName("NAZIV");
    jraADR.setColumnName("ADR");
    jraMB.setColumnName("MB");
    jraOIB.setColumnName("OIB");
    jraTEL.setColumnName("TEL");
    jraTELFAX.setColumnName("TELFAX");
    jraEMADR.setColumnName("EMADR");
    jraWEBADR.setColumnName("WEBADR");
    
    dispCol.setOpaque(true);
    
    jlrPBR.setSearchMode(-1);
    jlrPBR.setColumnName("PBR");
    
    jlrPBR.setColNames(new String[] {"CMJESTA", "NAZMJESTA", "CZUP"});
    jlrPBR.setTextFields(new javax.swing.text.JTextComponent[] {jlrCMJ, jlrMJ, jlrCZUP});
    jlrPBR.setVisCols(new int[] {0, 1, 2});
    jlrPBR.setRaDataSet(dM.getDataModule().getMjesta());
    jlrPBR.setNavButton(jbGetMj);
    jlrPBR.setFocusLostOnShow(false);
    jlrPBR.setAfterLookUpOnClear(false);
    jlrPBR.setSearchMode(1);

    jlrMJ.setSearchMode(-1);
    jlrMJ.setColumnName("MJ");
    jlrMJ.setNavProperties(jlrPBR);
    jlrMJ.setNavColumnName("NAZMJESTA");
    
    jlrMJ.setFocusLostOnShow(false);
    jlrMJ.setAfterLookUpOnClear(false);
    jlrMJ.setSearchMode(1);

    jlrCMJ.setSearchMode(-1);
    jlrCMJ.setColumnName("CMJESTA");
    jlrCMJ.setNavProperties(jlrPBR);
    jlrCMJ.setVisible(false);
    jlrCMJ.setEnabled(false);
    jlrCMJ.setFocusLostOnShow(false);
    jlrCMJ.setAfterLookUpOnClear(false);

    jlrCZUP.setSearchMode(-1);
    jlrCZUP.setColumnName("CZUP");
    jlrCZUP.setNavProperties(jlrPBR);
    jlrCZUP.setVisible(false);
    jlrCZUP.setEnabled(false);
    jlrCZUP.setFocusLostOnShow(false);
    jlrCZUP.setAfterLookUpOnClear(false);
    
    DataSet ks = KlijentStat.getDataModule().getTempSet();
    rcbStatus.setRaColumn("SID");
    rcbStatus.setRaItems(ks, "SID", "NAZIV");
    
    rcbSegment.setRaColumn("CSEG");
    rcbSegment.setRaItems(Segmentacija.getDataModule().getTempSet(), "CSEG", "NAZIV");
    
    for (ks.first(); ks.inBounds(); ks.next())
      cols.put(ks.getString("SID"), findColor(ks.getString("BOJA")));
    
    add(new JLabel("Naziv"), new XYConstraints(15, 20, -1, -1));
    add(jraNAZIV, new XYConstraints(100, 20, 320, -1));
    add(new JLabel("Adresa"), new XYConstraints(15, 45, -1, -1));
    add(jraADR, new XYConstraints(100, 45, 320, -1));
    
    add(new JLabel("Mjesto"), new XYConstraints(15, 70, -1, -1));
    add(jlrPBR,   new XYConstraints(100, 70, 75, -1));
    add(jlrMJ,   new XYConstraints(180, 70, 200, -1));    
    add(jbGetMj,   new XYConstraints(385, 70, 21, 21));
    
    add(new JLabel("OIB"), new XYConstraints(15, 95, -1, -1));
    add(jraOIB, new XYConstraints(100, 95, 175, -1));
    add(new JLabel("Telefon"), new XYConstraints(15, 120, -1, -1));
    add(jraTEL, new XYConstraints(100, 120, 175, -1));
    add(new JLabel("E-mail"), new XYConstraints(15, 145, -1, -1));
    add(jraEMADR, new XYConstraints(100, 145, 175, -1));
    
    add(dispCol, new XYConstraints(565, 20, 150, 21));
    add(new JLabel("Status"), new XYConstraints(450, 45, -1, -1));
    add(rcbStatus, new XYConstraints(565, 45, 150, 21));
    add(new JLabel("Segmentacija"), new XYConstraints(450, 70, -1, -1));
    add(rcbSegment, new XYConstraints(565, 70, 150, 21));
    add(new JLabel("Matièni broj"), new XYConstraints(450, 95, -1, -1));
    add(jraMB, new XYConstraints(565, 95, 150, -1));
    add(new JLabel("Fax"), new XYConstraints(450, 120, -1, -1));
    add(jraTELFAX, new XYConstraints(565, 120, 150, -1));
    add(new JLabel("Web"), new XYConstraints(450, 145, -1, -1));
    add(jraWEBADR, new XYConstraints(565, 145, 150, -1));
  }
  
  public void BindComponents(DataSet ds) {
    jraNAZIV.setDataSet(ds);
    jraADR.setDataSet(ds);
    jraMB.setDataSet(ds);
    jraOIB.setDataSet(ds);
    jraTEL.setDataSet(ds);
    jraTELFAX.setDataSet(ds);
    jraEMADR.setDataSet(ds);
    jraWEBADR.setDataSet(ds);
    rcbStatus.setDataSet(ds);
    
    jlrPBR.setDataSet(ds);
    jlrMJ.setDataSet(ds);

  }

  
  public void setColor() {
    DataSet ds = rcbStatus.getDataSet();
    if (ds.rowCount() == 0 || ds.getString("SID").equals(""))
      dispCol.setBackground(defColor);
    else dispCol.setBackground((Color) cols.get(rcbStatus.getDataValue())); 
  }
  
  private Color findColor(String col) {
    if (col.startsWith("#") || col.startsWith("0x") || col.startsWith("0X") || Aus.isDigit(col))
      try {
        return Color.decode(col);
      } catch (Exception e) {
        //
      }
    else 
      try {
        Field f = Color.class.getDeclaredField(col);
        return (Color) f.get(null);
      } catch (Exception e) {
        System.out.println(e);
      }
    return defColor;
  }
}
