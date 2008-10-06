/****license*****************************************************************
**   file: raCommonClass.java
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
package hr.restart.util;

import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.jbcl.layout.XYConstraints;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class raCommonClass {

  private static raCommonClass myCC ;

  public static final int STANDARD=0;
  public static final int LABEL=1;
  public static int DEFENABMODE = LABEL;

  public raCommonClass() {
    myCC=this;
  }
/**
 *  * Enabla ili disabla sve komponente osim jTabbedPane u zadanom kontaineru sa zadanim modom
 */
  public void EnabDisabAll(java.awt.Container ContainerName,boolean onoff,int disabMode) {
//System.out.println(onoff+"-------ENAB DISAB ALL---------"+ContainerName.toString());
    EnabDisabAll2(ContainerName,onoff,disabMode);
  }
  /**
 *  * Enabla ili disabla sve komponente osim jTabbedPane u zadanom kontaineru sa zadanim modom
 * pomocu invokeLater()
 */
  public void EnabDisabAllLater(final Container ContainerName, final boolean onoff, final int disabMode) {
//System.out.println(onoff+"-------ENAB DISAB ALL---------"+ContainerName.toString());
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        EnabDisabAll2(ContainerName,onoff,disabMode);
      }
    });
  }
/**
 * Enabla ili disabla sve komponente osim jTabbedPane u zadanom kontaineru sa default modom = DEFENABMODE
 */
  public void EnabDisabAll(java.awt.Container ContainerName,boolean onoff) {
//System.out.println(onoff+"-------ENAB DISAB ALL---------"+ContainerName.toString());
    EnabDisabAll2(ContainerName,onoff,DEFENABMODE);
  }

  /**
   * Enabla ili disabla sve komponente osim jTabbedPane u zadanom kontaineru sa default modom = DEFENABMODE
   * pomocu invokeLater
   */
    public void EnabDisabAllLater(final Container ContainerName, final boolean onoff) {
//System.out.println(onoff+"-------ENAB DISAB ALL---------"+ContainerName.toString());
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          EnabDisabAll2(ContainerName,onoff,DEFENABMODE);
        }
      });
    }


  public void EnabDisabAll2(java.awt.Container ContainerName,boolean onoff, int disabMode) {
    EnabDisabAll2(ContainerName,onoff,disabMode,null);
  }

  public void EnabDisabAll2(java.awt.Container ContainerName,boolean onoff, int disabMode, java.awt.Container exclude) {
    excludeCont = exclude;
    java.util.LinkedList ll = this.getComponentTree(ContainerName);
    excludeCont = null;
    for (int i=0;i<ll.size();i++) {
      java.awt.Component cmp = (java.awt.Component)ll.get(i);
      if (!(cmp instanceof javax.swing.JTabbedPane)) {
          if (disabMode==STANDARD) cmp.setEnabled(onoff);
          if (disabMode==LABEL) setLabelLaF(cmp,onoff);
      }
    }
    if (!onoff) ContainerName.repaint();
//    ContainerName.validate();
  }
  
  public void addLabelText(JPanel c, JComponent t, int x, int y, int w, String lab, int a) {
  	JLabel l = new JLabel(lab);
  	l.setHorizontalAlignment(a);
  	c.add(t, new XYConstraints(x, y, w, -1));
  	c.add(l, new XYConstraints(x + 1, y - 18, w - 2, -1));
  }
  
  public void addLabelTextLeft(JPanel c, JComponent t, int x, int y, int w, String lab) {
  	addLabelText(c, t, x, y, w, lab, SwingConstants.LEADING);
  }
  
  public void addLabelTextRight(JPanel c, JComponent t, int x, int y, int w, String lab) {
  	addLabelText(c, t, x, y, w, lab, SwingConstants.TRAILING);
  }
  
  public void addLabelTextCenter(JPanel c, JComponent t, int x, int y, int w, String lab) {
  	addLabelText(c, t, x, y, w, lab, SwingConstants.CENTER);
  }
  
/**
 * Stvara linkedlist svih komponenti dodanim zadanom containeru i komponente njihovih komponenti
 */
  public java.util.LinkedList getComponentTree(java.awt.Container ContainerName) {
    java.util.LinkedList retList = new java.util.LinkedList();
    addCompTreeToList(ContainerName,retList);
    return retList;
  }

  private java.awt.Container excludeCont = null;

  private void addCompTreeToList(java.awt.Container ContainerName,java.util.LinkedList rList) {
    if (ContainerName.equals(excludeCont)) return;
    java.awt.Component sve_comp;
    int i=ContainerName.getComponentCount();
    int j=0;
    if (i != 0) {
      while (j<i) {
        sve_comp = ContainerName.getComponent(j);
        rList.add(sve_comp);
        try {
          addCompTreeToList((java.awt.Container) sve_comp,rList);
        } catch (java.lang.ClassCastException e) {}
        j++;
      }
    }
  }
/**
 *  jtfVRDOK.setOpaque(false);
    jtfVRDOK.setEnabled(false);
    jtfVRDOK.setEnablePopupMenu(false);
    jtfVRDOK.setDisabledTextColor(jtfVRDOK.getForeground());
    jtfVRDOK.setBorder(null);
 */
  public void setLabelLaF(java.awt.Component comp,boolean enabled) {
    /*if (System.getProperty("java.version").startsWith("1.5")) {
      comp.setEnabled(enabled);
      return;
    }*/
        
    //
//    if (comp.isEnabled() == enabled) return;//ako je enabled flag isti kao zadani smatrajmo da je labellaf vec postavljen
    if (comp instanceof hr.restart.util.raNavAction) {
      comp.setEnabled(enabled);
      return;
    }
    if (comp instanceof javax.swing.JLabel) return;
    if (comp instanceof javax.swing.JScrollBar) return;
    comp.setEnabled(enabled);
    if (comp instanceof javax.swing.JComponent) {
    	javax.swing.JComponent jcomp = (javax.swing.JComponent)comp;
      if (enabled) {
        // jos jedan workaround za jebeni basicComboboxui
        jcomp.setOpaque(true);
        if (!(comp instanceof hr.restart.util.raComboBox))
        {
// tu ne nacrta dobro na javi 5 ako je prije bilo offano
              jcomp.updateUI();
        }
      } else {
        jcomp.setOpaque(false);
//        jcomp.setBorder(null);
        if (jcomp instanceof hr.restart.swing.JraTextField) {
          hr.restart.swing.JraTextField jratextcomp = (hr.restart.swing.JraTextField)jcomp;
//          jratextcomp.setEnablePopupMenu(false);
          jratextcomp.setNormalColors();
        }
        if (jcomp instanceof javax.swing.text.JTextComponent) {
          javax.swing.text.JTextComponent jtextcomp = (javax.swing.text.JTextComponent)jcomp;
          jtextcomp.select(0, 0);
          jtextcomp.setDisabledTextColor(jtextcomp.getForeground());
        }

      }
    }
//System.out.println("....done");

  }

  public static raCommonClass getraCommonClass() {
    if (myCC == null) {
      myCC = new raCommonClass();
    }
    return myCC;
  }
  public void raPorukaGreske(java.lang.Exception e) {
    System.out.print("Greška "+e.getMessage());

  }

/*  public void Izlaz() {}
     raQueryDataSet.cancel();
  }
*/

}