/****license*****************************************************************
**   file: jpVrsteOdb.java
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
import hr.restart.swing.JraTextField;
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.raComboBox;
import hr.restart.util.raCommonClass;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class jpVrsteOdb extends JPanel {
  raCommonClass rcc = raCommonClass.getraCommonClass();
  dM dm = dM.getDataModule();
  Valid vl = Valid.getValid();
  StorageDataSet sDS = new StorageDataSet();
  Column nivo1 = new Column();
  Column nivo2 = new Column();
  Column nivo = new Column();
  frmVrsteOdb fVrsteOdb;
  JPanel jpNivo = new JPanel();

  XYLayout lay = new XYLayout();
  JLabel jlCpov = new JLabel();
  JLabel jlCvrodb = new JLabel();
  JLabel jlIznos = new JLabel();
  JLabel jlOpisvrodb = new JLabel();
  JLabel jlStopa = new JLabel();
  JraButton jbSelCpov = new JraButton();
  JraCheckBox jcbAktiv = new JraCheckBox();
  JraTextField jraCvrodb = new JraTextField();
  JraTextField jraIznos = new JraTextField();
  JraTextField jraOpisvrodb = new JraTextField();
  JraTextField jraStopa = new JraTextField();
  JlrNavField jlrNazpov = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  JlrNavField jlrCpov = new JlrNavField() {
    public void after_lookUp() {
    }
  };
  raComboBox jcbNivo1 = new raComboBox(){
    public void this_itemStateChanged() {
      super.this_itemStateChanged();
      jcbNivo1_selected();
    }
  };
  raComboBox jcbNivo2 = new raComboBox();
  raComboBox jcbTip = new raComboBox();
  raComboBox jcbVrOs = new raComboBox();
  raComboBox jcbOsOb = new raComboBox();
  JLabel jlNivo = new JLabel();
  JLabel jlTipOd = new JLabel();
  JLabel jlVrOs = new JLabel();
  JLabel jlOsOb = new JLabel();

  public int brojNivoa=1;
  raComboBox jcbNivo1Main = new raComboBox();
  JLabel jlNivo1 = new JLabel();
  JLabel jlNivo2 = new JLabel();
  JLabel jlVrOdbitka = new JLabel();


  public jpVrsteOdb(frmVrsteOdb f) {
    try {
      fVrsteOdb = f;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jpNivo.setLayout(lay);

    jbSelCpov.setText("...");
    jlCpov.setText("Povjerioc - virman");
    jlCvrodb.setText("Oznaka");
    jlIznos.setText("Iznos");
    jlOpisvrodb.setText("Opis");
    jlStopa.setText("Stopa");
    jcbAktiv.setColumnName("AKTIV");
    jcbAktiv.setDataSet(fVrsteOdb.getRaQueryDataSet());
    jcbAktiv.setHorizontalAlignment(SwingConstants.RIGHT);
    jcbAktiv.setHorizontalTextPosition(SwingConstants.LEADING);
    jcbAktiv.setSelectedDataValue("D");
    jcbAktiv.setText("Aktivan");
    jcbAktiv.setUnselectedDataValue("N"); /**@todo: Definirati vrijednosti checkboxa */
    jraCvrodb.setColumnName("CVRODB");
    jraCvrodb.setDataSet(fVrsteOdb.getRaQueryDataSet());
    jraIznos.setColumnName("IZNOS");
    jraIznos.setDataSet(fVrsteOdb.getRaQueryDataSet());
    jraOpisvrodb.setColumnName("OPISVRODB");
    jraOpisvrodb.setDataSet(fVrsteOdb.getRaQueryDataSet());
    jraStopa.setColumnName("STOPA");
    jraStopa.setDataSet(fVrsteOdb.getRaQueryDataSet());

    jlrCpov.setColumnName("CPOV");
    jlrCpov.setDataSet(fVrsteOdb.getRaQueryDataSet());
    jlrCpov.setColNames(new String[] {"NAZPOV"});
    jlrCpov.setTextFields(new JTextComponent[] {jlrNazpov});
    jlrCpov.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */
    jlrCpov.setSearchMode(0);
    jlrCpov.setRaDataSet(dm.getPovjerioci());
    jlrCpov.setNavButton(jbSelCpov);

    jlrNazpov.setColumnName("NAZPOV");
    jlrNazpov.setNavProperties(jlrCpov);
    jlrNazpov.setSearchMode(1);

    jlNivo.setText("Nivo odbitka");
    jlTipOd.setText("Tip odbitka");
    jlVrOs.setText("Vrsta osnovice");
    jlOsOb.setText("Osnovica za obra\u010Dun");
    lay.setWidth(575);
    lay.setHeight(235);

    jlNivo1.setText("Nivo 1");
    jlNivo2.setText("Nivo 2");
    jcbNivo1Main.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcbNivo1Main_actionPerformed(e);
      }
    });

    jlVrOdbitka.setText("Vrsta odbitka");


    jcbOsOb.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        jcbOsOb_itemStateChanged(e);
      }
    });
    jpNivo.add(jcbAktiv,   new XYConstraints(460, 20, 70, -1));
    jpNivo.add(jlCpov, new XYConstraints(15, 70, -1, -1));
    jpNivo.add(jlIznos,   new XYConstraints(15, 195, -1, -1));
    jpNivo.add(jlrCpov, new XYConstraints(150, 70, 50, -1));
    jpNivo.add(jlrNazpov,   new XYConstraints(205, 70, 325, -1));
    jpNivo.add(jraIznos,    new XYConstraints(150, 195, 125, -1));
    jpNivo.add(jcbNivo1,       new XYConstraints(405, 95, 125, -1));
    jpNivo.add(jcbTip,       new XYConstraints(150, 120, 125, -1));
    jpNivo.add(jcbVrOs,      new XYConstraints(150, 145, 125, -1));
    jpNivo.add(jraStopa,       new XYConstraints(405, 195, 125, -1));
    jpNivo.add(jcbOsOb,   new XYConstraints(150, 170, 125, -1));
    jpNivo.add(jlNivo,  new XYConstraints(15, 95, -1, -1));
    jpNivo.add(jlTipOd,  new XYConstraints(15, 120, -1, -1));
    jpNivo.add(jlVrOs,  new XYConstraints(15, 145, -1, -1));
    jpNivo.add(jlOsOb,   new XYConstraints(15, 170, -1, -1));
    jpNivo.add(jcbNivo2,             new XYConstraints(405, 120, 125, -1));
    jpNivo.add(jcbNivo1Main,     new XYConstraints(150, 95, 125, -1));

    nivo1.setColumnName("NIVO1");
    nivo1.setDataType(com.borland.dx.dataset.Variant.STRING);

    nivo2.setColumnName("NIVO2");
    nivo2.setDataType(com.borland.dx.dataset.Variant.STRING);

    nivo.setColumnName("NIVO");
    nivo.setDataType(com.borland.dx.dataset.Variant.STRING);

    sDS.setColumns(new Column[]{nivo1, nivo2, nivo});
    sDS.open();

    jcbNivo1.setRaColumn("NIVO1");
    jcbNivo1.setRaDataSet(sDS);
    jcbNivo1.setRaItems(new defaultItems().toRaItems());

    jcbNivo1Main.setRaDataSet(sDS);
    jcbNivo1Main.setRaColumn("NIVO");
    jcbNivo1Main.setRaItems(new String[][] {
      {"Primanja","ZA"},
      {"Radnici","RA"},
      {"Op\u0107ine","OP"},
      {"Org. jedinice","OJ"},
      {"Poduze\u0107e","PO"},
      {"Vrsta radnog odnosa","VR"},
      {"2 nivoa odbitka","NO"}
    });


    jcbNivo2.setRaDataSet(sDS);
    jcbNivo2.setRaColumn("NIVO2");
    jcbNivo2.setRaItems(new defaultItems().toRaItems());

    jcbTip.setRaColumn("TIPODB");
    jcbTip.setRaDataSet(fVrsteOdb.getRaQueryDataSet());
    jcbTip.setRaItems(new String[][] {
      {"Kalkulativni", "K"},
      {"Stvarni","S"},
      {"Razlika poreza","P"},
      {"Razlika prireza","R"}
    });

    jcbVrOs.setRaColumn("VRSTAOSN");
    jcbVrOs.setRaDataSet(fVrsteOdb.getRaQueryDataSet());
    jcbVrOs.setRaItems(new String[][] {
      {"Bruto", "1"},
      {"Porezna osnovica","2"},
      {"Neto","3"}
    });

    jcbOsOb.setRaColumn("OSNOVICA");
    jcbOsOb.setRaDataSet(fVrsteOdb.getRaQueryDataSet());
    jcbOsOb.setRaItems(new String[][] {
      {"Fiksni iznos", "0"},
      {"Bruto", "1"},
      {"Porezna osnovica","2"},
      {"Neto","3"},
      {"Zakonska","4"},
      {"Porez","5"}
    });

    jcbNivo1.setNextFocusableComponent(jcbNivo2);
    jcbNivo2.setNextFocusableComponent(this.jcbTip);
    jcbTip.setNextFocusableComponent(this.jcbVrOs);
    this.setNextFocusableComponent(this.jcbAktiv);

    this.add(jpNivo, BorderLayout.CENTER);
    jpNivo.add(jraOpisvrodb,  new XYConstraints(205, 46, 325, -1));
    jpNivo.add(jraCvrodb,  new XYConstraints(150, 45, 50, -1));
    jpNivo.add(jbSelCpov,  new XYConstraints(533, 70, 21, 21));
    jpNivo.add(jlNivo1,   new XYConstraints(352, 95, -1, -1));
    jpNivo.add(jlNivo2,   new XYConstraints(352, 120, -1, -1));
    jpNivo.add(jlStopa,   new XYConstraints(352, 195, -1, -1));
    jpNivo.add(jlCvrodb,  new XYConstraints(150, 28, -1, -1));
    jpNivo.add(jlOpisvrodb,  new XYConstraints(205, 28, -1, -1));
    jpNivo.add(jlVrOdbitka,   new XYConstraints(15, 45, -1, -1));
  }

  private void removeComboItem(int i)
  {
    switch (i) {
      case 1:
        if(jcbNivo1.getSelectedIndex()!=0)
        {
          jcbNivo2.removeItem(jcbNivo1.getSelectedItem());
        }
        break;
      case 0:
        if(jcbNivo2.getSelectedIndex()!=0)
        {
          jcbNivo1.removeItem(jcbNivo2.getSelectedItem());
        }
        break;
    }
  }


  void jcbNivo1Main_actionPerformed(ActionEvent e) {
    if(sDS.getString("NIVO").equals("NO"))
    {
      jcbNivo1.setVisible(true);
      jcbNivo2.setVisible(true);
      jlNivo1.setVisible(true);
      jlNivo2.setVisible(true);
    }
    else
    {
      jcbNivo1.setVisible(false);
      jcbNivo2.setVisible(false);
      jlNivo1.setVisible(false);
      jlNivo2.setVisible(false);
    }
  }

  public void jcbNivo1_selected()
  {
    jcbNivo2.setVisible(false);
    jcbNivo2.setRaItems(new defaultItems(sDS.getString("NIVO1")).toRaItems());
    jcbNivo2.setVisible(true);
  }

  class defaultItems extends java.util.TreeSet {
    String deletekey = null;
    public defaultItems() {
      this(null);
    }

    void put(String key, String val, int ord) {
      if (deletekey != null) {
        if (!key.equals(deletekey)) this.add(new tsValue(key,val,ord));
      } else this.add(new tsValue(key,val,ord));
    }

    public defaultItems(String _deletekey) {
      deletekey = _deletekey;
      put("","",0);
      put("ZA","Primanja",1);
      put("RA", "Radnici",2);
      put("OP","Op\u0107ine",3);
      put("OJ","Org. jedinice",4);
      put("PO","Poduze\u0107e",5);
      put("VR", "Vrsta radnog odnosa",6);
    }
    public String[][] toRaItems() {
      Object[] vals = toArray();
      String[][] raitems = new String[vals.length][2];
      for (int i = 0; i < vals.length; i++) {
        tsValue value = (tsValue)vals[i];
        raitems[i][0] = value.val;
        raitems[i][1] = value.key;
      }
      return raitems;
    }
    class tsValue implements Comparable {
      String key;
      String val;
      Integer ord;
      tsValue(String _key,String _val, int _ord) {
        key = _key;
        val = _val;
        ord = new Integer(_ord);
      }
      public int compareTo(Object o) {
        if (o instanceof tsValue) {
          return ord.compareTo(((tsValue)o).ord);
        } else return key.compareTo(o.toString());
      }
      public boolean equals(Object o) {
        if (o instanceof tsValue) {
          return key.equals(((tsValue)o).key);
        } else return key.equals(o);
      }
    }
  }

  void jcbOsOb_itemStateChanged(ItemEvent e) {
    if(jcbOsOb.getSelectedIndex()==0)
    {
      rcc.setLabelLaF(jraIznos, true);
      rcc.setLabelLaF(jraStopa, false);
    }
    else
    {
      rcc.setLabelLaF(jraIznos, false);
      rcc.setLabelLaF(jraStopa, true);
    }
  }
}
