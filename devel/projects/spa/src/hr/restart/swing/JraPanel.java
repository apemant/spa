/****license*****************************************************************
**   file: JraPanel.java
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
package hr.restart.swing;

import hr.restart.util.raMatPodaci;

import java.awt.LayoutManager;

import javax.swing.JPanel;


/**
 * <p>Title: JraPanel </p>
 * <p>Description: JPanel s podrskom za setFocusCycleRoot</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: REST-ART</p>
 * JPanel s podrskom za setFocusCycleRoot. Vjerojatno nepotreban u
 * 1.4.2 no nije ni stetan. Koristi se u raMatPodacima za detail
 * panel, i gdje vec se nadje za shodno.
 * @author ab.f
 * @version 1.0
 */

public class JraPanel extends JPanel {

  private boolean focusCycle;
  private raMatPodaci owner;

  public JraPanel() {
    super();
  }

  public JraPanel(boolean isDoubleBuffered) {
    super(isDoubleBuffered);
  }

  public JraPanel(LayoutManager layout) {
    super(layout);
  }

  public JraPanel(LayoutManager layout, boolean isDoubleBuffered) {
    super(layout, isDoubleBuffered);
  }

  public boolean isFocusCycleRoot() {
    return focusCycle;
  }

  public void setFocusCycleRoot(boolean focusCycle) {
    this.focusCycle = focusCycle;
  }
  
  public void setOwner(raMatPodaci mp) {
  	owner = mp;
  }
  
  public raMatPodaci getOwner() {
  	return owner;
  }
}