/****license*****************************************************************
**   file: raUICol.java
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
import hr.restart.util.JlrNavField;

public class raUICol {
private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  private String caption;
  private com.borland.dx.dataset.DataSet dataSet;
  private com.borland.dx.dataset.DataSet lookupSet;
  private String columnName;
  private String lookupColumnName;
  private String[] lookupColumnNames;
  private String[] lookupColumnCaptions; //style!
  private int[] lookupSearchModes;
  private boolean lookup = false;
  private javax.swing.JComponent uicomponent;
  private hr.restart.swing.JraTextField uiTextComponent;
  private int _margin = 150;
  private raUIColInitializer initializer;
  private raUIColValidator validator;
  public raUICol() {

  }
  public raUICol(com.borland.dx.dataset.Column jbCol) {
    setCaption(jbCol.getCaption());
    setDataSet(jbCol.getDataSet());
    setColumnName(jbCol.getColumnName());
  }

  public boolean validate(Object param) {
    if (validator == null) return true;
    return validator.validate(this, param);
  }

  public void initialize(Object param) {
    if (initializer!=null) initializer.initialize(this, param);
  }

  public javax.swing.JComponent getUIComponent() {
    if (uicomponent == null) return getNewUIComponent();
    return uicomponent;
  }
  public javax.swing.JComponent getNewUIComponent() {
    int x = 15;
    uicomponent = new javax.swing.JPanel();
    raXYLayout layout = new raXYLayout();
    uicomponent.setLayout(layout);
    uicomponent.add(new javax.swing.JLabel(getCaption()),new raXYConstraints(x, 20, -1, -1));

    if (lookup) {
      uiTextComponent = new JlrNavField();
      int lw = adduiTextComponent(x);
      JlrNavField _jltc = (JlrNavField)uiTextComponent;
      if (lookupColumnName != null) _jltc.setNavColumnName(lookupColumnName);
      String[] colnames = new String[getLookupColumnNames().length+1];
      JlrNavField[] jlncs = new JlrNavField[getLookupColumnNames().length+1];
      colnames[0] = _jltc.getNavColumnName();
      jlncs[0] = _jltc;
      for (int i = 0; i < getLookupColumnNames().length; i++) {
        colnames[i+1]=getLookupColumnNames()[i];
        jlncs[i+1] = new JlrNavField();
      }

      ST.prn(colnames);
      _jltc.setColNames(colnames);
      _jltc.setTextFields(jlncs);
      _jltc.setRaDataSet(getLookupSet());
      _jltc.setSearchMode(0);
      for (int i = 0; i < getLookupColumnNames().length; i++) {
        int sm = 1;
        try {
          sm = getLookupSearchModes()[i];
        } catch (Exception ex) {}
        int _lw = addNavComponent(x,lw,getLookupColumnNames()[i],sm, jlncs[i+1]);
        lw = _lw;
      }
      hr.restart.swing.JraButton getBut = new hr.restart.swing.JraButton();
      getBut.setText("...");
      _jltc.setNavButton(getBut);
      uicomponent.add(getBut,new raXYConstraints(_margin+lw+5,20,21,21));
    } else {
      uiTextComponent = new hr.restart.swing.JraTextField();
      adduiTextComponent(x);
    }
    layout.setMargins(0,0);
    return uicomponent;
  }

  private int adduiTextComponent(int x) {
    uiTextComponent.setDataSet(getDataSet());
    uiTextComponent.setColumnName(getColumnName());
    com.borland.dx.dataset.Column cl = getDataSet().getColumn(getColumnName());
/*if (lookup) {
  System.out.println("adduiTextComponent");
  System.out.println("cl = "+cl);
  System.out.println("cl.getDataType() = "+cl.getDataType());
  System.out.println("cl.getWidth() = "+cl.getWidth());
  System.out.println("cl.getPrec() = "+cl.getPrecision());
}*/
    int w = raPanelFactory.getDefWidth(cl.getDataType(),cl.getWidth());
/*if (lookup) {
  System.out.println("w = "+w);
}*/
    uicomponent.add(uiTextComponent,new raXYConstraints(x+_margin,20,w, -1));
    testGetFocus(uiTextComponent);
    return w;
  }
  private int addNavComponent(int x, int lastw, String colName, int searchMode, JlrNavField jlnc) {
    System.out.println("lastw "+lastw);
//    JlrNavField jlnc = new JlrNavField();
    jlnc.setColumnName(colName);
    jlnc.setSearchMode(searchMode);

//System.out.println("((JlrNavField)uiTextComponent).getColumnName() = "+((JlrNavField)uiTextComponent).getColumnName());
//System.out.println("((JlrNavField)uiTextComponent).getNavColumnName() = "+((JlrNavField)uiTextComponent).getNavColumnName());
//System.out.println("((JlrNavField)uiTextComponent).getSearchMode() = "+((JlrNavField)uiTextComponent).getSearchMode());
//System.out.println("((JlrNavField)uiTextComponent).getColumnNames() = ");
//ST.prn(((JlrNavField)uiTextComponent).getColNames());

    jlnc.setNavProperties((JlrNavField)uiTextComponent);
    com.borland.dx.dataset.Column cl = getLookupSet().getColumn(colName);
//System.out.println("addNavComponent");
//System.out.println("cl = "+cl);
//System.out.println("cl.getDataType() = "+cl.getDataType());
//System.out.println("cl.getWidth() = "+cl.getWidth());
//System.out.println("cl.getPrec() = "+cl.getPrecision());
    int _x = x + _margin + lastw + 5;
    int w = raPanelFactory.getDefWidth(cl.getDataType(),cl.getPrecision());
//Sytem.out.println("w = "+w);
    uicomponent.add(jlnc,new raXYConstraints(_x,20,w,-1));
    testGetFocus(jlnc);
//System.out.println("return "+(lastw + w + 5+x));
    return lastw + w + 5 + x;
  }
  //getteri & setteri
  public String getCaption() {
    return caption;
  }
  public String getColumnName() {
    return columnName;
  }
  public com.borland.dx.dataset.DataSet getDataSet() {
    return dataSet;
  }
  public boolean isLookup() {
    return lookup;
  }
  public String[] getLookupColumnNames() {
    return lookupColumnNames;
  }
  public int[] getLookupSearchModes() {
    return lookupSearchModes;
  }
  public com.borland.dx.dataset.DataSet getLookupSet() {
    return lookupSet;
  }
  public void setCaption(String caption) {
    this.caption = caption;
  }
  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }
  public void setDataSet(com.borland.dx.dataset.DataSet dataSet) {
    this.dataSet = dataSet;
  }
  public void setLookup(boolean lookup) {
    this.lookup = lookup;
  }
  public void setLookupColumnNames(String[] lookupColumnNames) {
    this.lookupColumnNames = lookupColumnNames;
  }
  public void setLookupSearchModes(int[] lookupSearchModes) {
    this.lookupSearchModes = lookupSearchModes;
  }
  public void setLookupSet(com.borland.dx.dataset.DataSet lookupSet) {
    this.lookupSet = lookupSet;
  }
  public String[] getLookupColumnCaptions() {
    return lookupColumnCaptions;
  }
  public void setLookupColumnCaptions(String[] lookupColumnCaptions) {
    this.lookupColumnCaptions = lookupColumnCaptions;
  }
  public hr.restart.swing.JraTextField getUiTextComponent() {
    return uiTextComponent;
  }
  public void setLookupColumnName(String lookupColumnName) {
    this.lookupColumnName = lookupColumnName;
  }
  public raUIColValidator getValidator() {
    return validator;
  }
  public void setValidator(raUIColValidator validator) {
    this.validator = validator;
  }
  public raUIColInitializer getInitializer() {
    return initializer;
  }
  public void setInitializer(raUIColInitializer initializer) {
    this.initializer = initializer;
  }
  //
  public void testGetFocus(final hr.restart.swing.JraTextField jtf) {
    jtf.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(java.awt.event.FocusEvent e) {
        System.out.println("jtf.getWidth() = "+jtf.getWidth());
      }

      public void focusLost(java.awt.event.FocusEvent e) {
      }
    });
  }
  //
}