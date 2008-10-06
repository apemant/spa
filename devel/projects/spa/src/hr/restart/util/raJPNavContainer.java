/****license*****************************************************************
**   file: raJPNavContainer.java
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

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class raJPNavContainer extends JPanel {
  GridLayout navLayout = new GridLayout();
  private int navActionCount = 0;
  public raJPNavContainer() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    setLayout(navLayout);
  }

  public void registerNavKeys(Component comp) {
    for (int i=0;i<getComponentCount();i++) {
      if (getComponent(i) instanceof hr.restart.util.raNavAction) {
        ((raNavAction)getComponent(i)).registerNavKey(comp);
      }
    }
  }

  public void unregisterNavKeys(Component comp) {
    for (int i=0;i<getComponentCount();i++) {
      if (getComponent(i) instanceof hr.restart.util.raNavAction) {
        ((raNavAction)getComponent(i)).unregisterNavKey(comp);
      }
    }
  }
  private void countNavActions() {
    navActionCount = 0;
    for (int i=0;i<getComponentCount();i++) {
      if (getComponent(i) instanceof hr.restart.util.raNavAction) {
        navActionCount = navActionCount + 1;
      }
    }
  }
  public raNavAction[] getNavActions() {
    countNavActions();
    raNavAction[] rnvActs = new raNavAction[navActionCount];
//System.out.println("navActionCount = "+navActionCount);
    for (int i=0;i<getComponentCount();i++) {
      if (getComponent(i) instanceof hr.restart.util.raNavAction) {
        rnvActs[i] = (raNavAction)getComponent(i);
      }
    }
    return rnvActs;
  }

  public boolean contains(raNavAction act) {
    for (int i=0;i<getComponentCount();i++) {
      if (getComponent(i).equals(act)) return true;
    }
    return false;
  }
/*
  public java.awt.Component add(java.awt.Component c) {
    if (c instanceof hr.restart.util.raNavAction) navActionCount++;
    return super.add(c);
  }
*/
  public void addOption(raNavAction act) {
    add(act);    
  }

  public void addOption(raNavAction act, int pos) {
    add(act,pos);
  }

  public void addOption(raNavAction act, com.borland.jbcl.layout.XYConstraints xyc) {
    add(act);    
    clcSize(xyc);
  }

  public void addOption(raNavAction act, int pos, com.borland.jbcl.layout.XYConstraints xyc) {
    add(act,pos);
    clcSize(xyc);
  }
  
  void clcSize(com.borland.jbcl.layout.XYConstraints xyc) {
    int nnW = getComponentCount()*raNavAction.ACTSIZE;
    setBounds(0,0,nnW,raNavAction.ACTSIZE);
    xyc.setX(nnW);
  }
}