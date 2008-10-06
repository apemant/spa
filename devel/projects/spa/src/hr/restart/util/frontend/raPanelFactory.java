/****license*****************************************************************
**   file: raPanelFactory.java
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
package hr.restart.util.frontend;
import hr.restart.swing.layout.raXYConstraints;
import hr.restart.swing.layout.raXYLayout;
import hr.restart.util.raMatPodaci;

import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.swing.JPanel;

import com.borland.dx.dataset.Variant;

public class raPanelFactory {
  private static HashMap defWidths;
  HashMap uiCols = new HashMap();
  private JPanel panel;

  public raPanelFactory() {
  }

  public void parseDataSet(com.borland.dx.dataset.DataSet ds) {
    uiCols.clear();
    for (int i = 0; i < ds.getColumnCount(); i++) {
      uiCols.put(new Integer(i),new raUICol(ds.getColumn(i)));
    }
  }

  public void addUICol(raUICol col) {
    int idx = uiCols.size();
    uiCols.put(new Integer(idx),col);
  }

  public raUICol getUICol(String colName) {
    for (Iterator i = uiCols.keySet().iterator(); i.hasNext(); ) {
      Object item = i.next();
      raUICol col = getUICol(item);
      if (col.getColumnName().equals(colName)) {
        return col;
      }
    }
    return null;
  }

  raUICol getUICol(Object idx) {
    return (raUICol)uiCols.get(idx);
  }

  public raUICol getUICol(int idx) {
    return getUICol(new Integer(idx));
  }

  public JPanel getPanel() {
    if (panel != null) return panel;
    int ph = 25;
    panel = new JPanel(new raXYLayout());
    int counter = 0;
    TreeSet keyset = new TreeSet(uiCols.keySet());
    for (Iterator i = keyset.iterator(); i.hasNext(); ) {
      Object item = i.next();
      raUICol col = getUICol(item);
      int y = counter*ph;
      panel.add(col.getUIComponent(),new raXYConstraints(0,y,-1,-1));
      counter++;
    }
    return panel;
  }

  public raMatPodaci getMatPodaci(com.borland.dx.sql.dataset.QueryDataSet qds) {
    raMatPodaci rmp = new raMatPodaci() {
      public boolean Validacija(char mode) {
        TreeSet keyset = new TreeSet(uiCols.keySet());
        for (Iterator i = keyset.iterator(); i.hasNext(); ) {
          Object item = i.next();
          raUICol col = getUICol(item);
          if (!col.validate(new Character(mode))) return false;
        }
        return true;
      }

      public void SetFokus(char mode) {
        TreeSet keyset = new TreeSet(uiCols.keySet());
        for (Iterator i = keyset.iterator(); i.hasNext(); ) {
          Object item = i.next();
          raUICol col = getUICol(item);
          col.initialize(new Character(mode));
        }
      }
    };
    rmp.setRaQueryDataSet(qds);
    JPanel jpDetail = getPanel();
    rmp.setRaDetailPanel(jpDetail);
    rmp.pack();
    return rmp;
  }


//statiranje
  private static HashMap getDefWidths() {
    if (defWidths != null) return defWidths;
    defWidths = new HashMap();
    putDef(Variant.TIMESTAMP,120);
    putDef(Variant.BIGDECIMAL,120);
    putDef(Variant.STRING,"10x");
    putDef(Variant.SHORT,100);
    putDef(Variant.INT,100);
    //.....
    return defWidths;
  }
  private static void putDef(int ty, int w) {
    defWidths.put(new Integer(ty),new Integer(w));
  }
  private static void putDef(int ty, String w) {
    defWidths.put(new Integer(ty),w);
  }
  static int getDefWidth(int ty, int w) {
    int maxWidth = 400;
    Object ow = getDefWidths().get(new Integer(ty));
    if (ow!=null && ow instanceof Integer) {
      return ((Integer)ow).intValue();
    } else {
      try {
        int precV = (new Integer(new StringTokenizer(ow.toString(),"x").nextToken()).intValue()*w);
        int retw = ((precV/25)+1)*25;//zaokruzi na 25
        //if (retw<50) retw=50;
        if (retw>400) retw=400;
        return retw;
      }
      catch (Exception ex) {
        System.out.println("getDefWidth ex :: "+ex);
        return (((9*w)/50)+1)*50;
      }
    }
  }

  public static void main(String[] args) {
//    System.out.println(getDefWidth(Variant.STRING,35));
    hr.restart.util.startFrame.raLookAndFeel();
//    JFrame frame = new JFrame("probica");
    raPanelFactory rapf = new raPanelFactory();
    rapf.parseDataSet(hr.restart.baza.dM.getDataModule().getRSPeriodobr());
    //
    rapf.getUICol("RSOO").setLookup(true);
    rapf.getUICol("RSOO").setLookupColumnNames(new String[] {"OPIS"});
    rapf.getUICol("RSOO").setLookupSet(hr.restart.zapod.Sifrarnici.getSifre("PLOO"));
    rapf.getUICol("RSOO").setLookupColumnName("CSIF");
    //
//    frame.getContentPane().add(rapf.getPanel());
//    ((hr.restart.util.JlrNavField)rapf.getUICol("RSOO").getUiTextComponent()).setNavColumnName("CSIF");
    hr.restart.util.startFrame.getStartFrame().showFrame(rapf.getMatPodaci(hr.restart.baza.dM.getDataModule().getRSPeriodobr()));
  }
}