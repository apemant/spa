/****license*****************************************************************
**   file: jpIniPrim.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraComboBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpIniPrim extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  HashMap NIVOI = new HashMap();
  frmIniPrim fIniPrim;
  JPanel jpDetail = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCvrp = new JLabel();
  JLabel jlIznos = new JLabel();
  JLabel jlKoef = new JLabel();
  JLabel jlRbr = new JLabel();
  JLabel jlSati = new JLabel();
  JraButton jbSelCvrp = new JraButton();
  JraCheckBox jcbAktiv = new JraCheckBox();
  JraTextField jraIznos = new JraTextField();
  JraTextField jraKoef = new JraTextField();
  JraTextField jraRbr = new JraTextField();
  JraTextField jraSati = new JraTextField();
  JlrNavField jlrNaziv = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCvrp = new JlrNavField() {
    public void after_lookUp() {
      sjUtil.getSjUtil().findFocusAfter(jlrCvrp.getText(), jraIznos, jraKoef,jraSati);
      if (dm.getVrsteprim().getString("REGRES").equals("D")) {
        dateOn(true);
        fIniPrim.getRaQueryDataSet().setTimestamp("IRAZOD", hr.restart.robno.Util.getUtil().findFirstDayOfYear());
        fIniPrim.getRaQueryDataSet().setTimestamp("IRAZDO", hr.restart.robno.Util.getUtil().findLastDayOfYear(2002));
      }
      else {
        dateOn(false);
      }
    }
  };
  JLabel jlOznaka = new JLabel();
  JLabel jlNaziv = new JLabel();
  private JLabel jlRazdoblje = new JLabel();
  private JraTextField jlIRAZDO = new JraTextField();
  private JraTextField jlIRAZOD = new JraTextField();
  raComboBox jcbSFond = new raComboBox();
  //nivo
  JraComboBox jcbNivo = new JraComboBox();
  JLabel jlNivo = new JLabel("Nivo");
  JlrNavField jlrNivo = new JlrNavField() {
    public void after_lookUp() {
      nivoAfterLookup();
    }
    public void setErrorColors() {
      //nemoj mi crveniti molim te
    }
  };
  JraButton jbSelNivo = new JraButton();
  //ovin
  public jpIniPrim(frmIniPrim f) {
    try {
      fIniPrim = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpDetail.setLayout(lay);
    lay.setWidth(596);
    lay.setHeight(145);

    dateOn(false);

    jbSelCvrp.setText("...");
    jlCvrp.setText("Vrsta primanja");
    jlIznos.setText("Iznos");
    jlKoef.setText("Koeficijent");
    jlRbr.setText("Redni broj");
    jlSati.setText("Sati");
    jcbAktiv.setColumnName("AKTIV");
    jcbAktiv.setDataSet(fIniPrim.getRaQueryDataSet());
    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktiv.setSelectedDataValue("D");
    jcbAktiv.setText("Aktivan");
    jcbAktiv.setUnselectedDataValue("N"); /**@todo: Definirati vrijednosti checkboxa */
    jraIznos.setColumnName("IZNOS");
    jraIznos.setDataSet(fIniPrim.getRaQueryDataSet());
    jraKoef.setColumnName("KOEF");
    jraKoef.setDataSet(fIniPrim.getRaQueryDataSet());
    jraRbr.setColumnName("RBR");
    jraRbr.setDataSet(fIniPrim.getRaQueryDataSet());
    jraSati.setColumnName("SATI");
    jraSati.setDataSet(fIniPrim.getRaQueryDataSet());

    jlrCvrp.setColumnName("CVRP");
    jlrCvrp.setDataSet(fIniPrim.getRaQueryDataSet());
    jlrCvrp.setColNames(new String[] {"NAZIV"});
    jlrCvrp.setTextFields(new JTextComponent[] {jlrNaziv});
    jlrCvrp.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCvrp.setSearchMode(0);
    jlrCvrp.setRaDataSet(dm.getVrsteprim());
    jlrCvrp.setNavButton(jbSelCvrp);

    jlrNaziv.setColumnName("NAZIV");
    jlrNaziv.setNavProperties(jlrCvrp);
    jlrNaziv.setSearchMode(1);

    jcbSFond.setRaColumn("SFOND");
    jcbSFond.setRaDataSet(fIniPrim.getRaQueryDataSet());
    jcbSFond.setRaItems(new String[][] {
      {"", "X"},
      {"Rada", "R"},
      {"Praznika","P"}
    });

    jlOznaka.setText("Oznaka");
    jlNaziv.setText("Naziv");
    jlRazdoblje.setText("Razdoblje (od-do)");
    jlIRAZDO.setDataSet(fIniPrim.getRaQueryDataSet());
    jlIRAZDO.setHorizontalAlignment(SwingConstants.CENTER);
    jlIRAZDO.setColumnName("IRAZDO");
    jlIRAZOD.setDataSet(fIniPrim.getRaQueryDataSet());
    jlIRAZOD.setHorizontalAlignment(SwingConstants.CENTER);
    jlIRAZOD.setColumnName("IRAZOD");
    
    //nivo
    NIVOI.put("Svima", new Object[] {"-","-"});
    NIVOI.put("-Org. jedinica", new Object[] {"CORG", dm.getOrgstruktura()});
    NIVOI.put("-Vrsta radnog odnosa", new Object[] {"CVRO", hr.restart.baza.Vrodn.getDataModule().copyDataSet()});
    NIVOI.put("-Radnik", new Object[] {"CRADNIK", dm.getRadnici()});
    NIVOI.put("Primanja 1", new Object[] {"INI1", null});
    NIVOI.put("Primanja 2", new Object[] {"INI2", null});
    NIVOI.put("Primanja 3", new Object[] {"INI3", null});
    NIVOI.put("Primanja 4", new Object[] {"INI4", null});
    NIVOI.put("Primanja 5", new Object[] {"INI5", null});
    jlrNivo.setColumnName("CNIVO");
    jlrNivo.setVisCols(new int[] {0,1});
    jlrNivo.setDataSet(fIniPrim.getRaQueryDataSet());
    jbSelNivo.setText("...");
    jlrNivo.setNavButton(jbSelNivo);
    for (Iterator i = new TreeSet(NIVOI.keySet()).iterator(); i.hasNext(); ) {
      String item = i.next().toString();
      jcbNivo.addItem("Nivo: "+item);
    }
    jcbNivo.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        nivoChanged();
      }
    });
    //ovin
    
    jpDetail.add(jbSelCvrp,  new XYConstraints(560, 35, 21, 21));
    jpDetail.add(jlCvrp,  new XYConstraints(15, 35, -1, -1));
    jpDetail.add(jlIznos,    new XYConstraints(15, 85, -1, -1));
    jpDetail.add(jlSati,   new XYConstraints(15, 60, -1, -1));
    jpDetail.add(jlrCvrp,  new XYConstraints(150, 35, 100, -1));
    jpDetail.add(jlrNaziv,  new XYConstraints(255, 35, 300, -1));
    jpDetail.add(jraIznos,    new XYConstraints(150, 85, 100, -1));
    //nivo
    jpDetail.add(jcbNivo,    new XYConstraints(255, 85, 195, -1));
    jpDetail.add(jlrNivo,    new XYConstraints(455, 85, 100, -1));
    jpDetail.add(jbSelNivo,    new XYConstraints(560, 85, 21, 21));
    //ovin
    jpDetail.add(jcbSFond,   new XYConstraints(150, 60, 100, -1));
    jpDetail.add(jraSati,   new XYConstraints(255, 60, 100, -1));
    jpDetail.add(jcbAktiv,   new XYConstraints(484, 10, 70, -1));
    jpDetail.add(jlKoef,    new XYConstraints(375, 60, -1, -1));
    jpDetail.add(jraKoef,     new XYConstraints(455, 60, 100, -1));
    jpDetail.add(jlOznaka,  new XYConstraints(150, 15, -1, -1));

    this.add(jpDetail, BorderLayout.CENTER);
    jpDetail.add(jlNaziv,  new XYConstraints(255, 15, -1, -1));
    jpDetail.add(jlRazdoblje, new XYConstraints(15, 110, -1, -1));
    jpDetail.add(jlIRAZOD, new XYConstraints(150, 110, 100, -1));
    jpDetail.add(jlIRAZDO, new XYConstraints(255, 110, 100, -1));

    jcbSFond.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        jcbSFond_itemStateChanged(e);
      }
    });
  }

  void dateOn(boolean ok) {
    if (ok) {
      jlRazdoblje.setVisible(true);
      jlIRAZDO.setVisible(true);
      jlIRAZOD.setVisible(true);
    }
    else {
      jlRazdoblje.setVisible(false);
      jlIRAZDO.setVisible(false);
      jlIRAZOD.setVisible(false);
    }
  }


  private void jcbSFond_itemStateChanged(ItemEvent e)
  {
    if(jcbSFond.getSelectedIndex()==0)
    {
      rcc.setLabelLaF(jraSati,true);
    }
    else
    {
      rcc.setLabelLaF(jraSati,false);
    }
  }
  //nivo
  private String getNivoSelectedItem() {
    StringTokenizer nizer = new StringTokenizer(jcbNivo.getSelectedItem().toString(),":");
    nizer.nextToken();
    return nizer.nextToken().trim();
  }
  private Object[] getNivoSelectedValues(String selected) {
    System.out.println("selected =-"+selected);
    return (Object[])NIVOI.get(selected);
  }
  private void selectJcbNivoFromData(String cnivo) {
    int idx = 0;
//    String cnivo = fIniPrim.getRaQueryDataSet().getString("CNIVO");
System.out.println("CNIVO = "+cnivo+"  ------------------");
    for (Iterator i = new TreeSet(NIVOI.keySet()).iterator(); i.hasNext(); ) {
      String colName = getNivoSelectedValues(i.next().toString())[0].toString();
System.out.println("colName = "+colName);
      if (cnivo.startsWith(colName+":")) {
        jcbNivo.setSelectedIndex(idx);
        String nivo;
        try {
          StringTokenizer cnivoizer = new StringTokenizer(cnivo, ":");
          cnivoizer.nextToken();
          nivo = cnivoizer.nextToken().trim();
System.out.println("nivo = "+nivo);
        } catch (Exception ex) {
//          ex.printStackTrace();
          nivo = "";
        }
        fIniPrim.getRaQueryDataSet().setString("CNIVO",nivo);
        nivoChanged();
        return;
      }
      idx++;
    }
    fIniPrim.getRaQueryDataSet().setString("CNIVO","");
    jcbNivo.setSelectedIndex(jcbNivo.getItemCount()-1);
    nivoChanged();
  }
  private void nivoChanged() {
    Object[] selVals = getNivoSelectedValues(getNivoSelectedItem());
    String colName = selVals[0].toString();
    if (colName.equals("-")) {
      jlrNivo.setVisible(false);
      jbSelNivo.setVisible(false);
    } else {
      if (selVals[1] != null) {
        jlrNivo.setRaDataSet((StorageDataSet)selVals[1]);
        jlrNivo.setNavColumnName(colName);
        jlrNivo.setColumnName("CNIVO");
        jlrNivo.setVisible(true);
        jbSelNivo.setVisible(true);
      } else {
        jlrNivo.setRaDataSet(null);
        jlrNivo.setNavColumnName("CNIVO");
        jlrNivo.setColumnName("CNIVO");
        jlrNivo.setVisible(false);
        jbSelNivo.setVisible(false);
      }
    }
  }
  
  private void nivoAfterLookup() {
  }
  
  void nivoBeforeSave() {
    Object[] selVals = getNivoSelectedValues(getNivoSelectedItem());
    String colName = selVals[0].toString();
    if (colName.equals("-")) {
      fIniPrim.getRaQueryDataSet().setString("CNIVO", "");
    } else {
      String _nivo = fIniPrim.getRaQueryDataSet().getString("CNIVO");
      if (colName.startsWith("INI")) _nivo = "";
      fIniPrim.getRaQueryDataSet().setString("CNIVO", colName+":"+_nivo);
    }
  }
  
  void nivoEntry(String cnivo) {
    selectJcbNivoFromData(cnivo);
  }
  //ovin
}
