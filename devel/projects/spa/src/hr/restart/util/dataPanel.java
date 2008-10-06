/****license*****************************************************************
**   file: dataPanel.java
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

import javax.swing.JPanel;

import com.borland.jbcl.layout.XYLayout;

/**
 * Title:        Utilitys
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      RA
 * @author AI
 * @version 1.0
 */

public class dataPanel extends JPanel {
  XYLayout xYLay = new XYLayout();
  private com.borland.dx.dataset.DataSet raDataSet;

  public dataPanel() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  void jbInit() throws Exception {
    this.setLayout(xYLay);
  }
  public void setRaDataSet(com.borland.dx.dataset.DataSet newRaDataSet) {
    raDataSet = newRaDataSet;
  }
  public com.borland.dx.dataset.DataSet getRaDataSet() {
    return raDataSet;
  }
}