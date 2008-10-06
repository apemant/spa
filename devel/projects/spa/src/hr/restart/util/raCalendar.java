/****license*****************************************************************
**   file: raCalendar.java
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

import hr.restart.swing.JraFrame;
import hr.restart.swing.JraScrollPane;

import java.awt.BorderLayout;

import javax.swing.JTable;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class raCalendar extends JraFrame {
//  hr.restart.util.dM dm;
/*  hr.restart.util.OKpanel ok = new hr.restart.util.OKpanel() {
    public void jBOK_actionPerformed() {
//      System.out.println(dm.findDate(false,0));
      System.out.println(tab.getSelectedColumn());
      System.out.println(tab.getSelectedRow());
    }
    public void jPrekid_actionPerformed() {
      System.out.println("prekid");
    }
  };*/
  String[] columnNames =  {"Pon", "Uto", "Sri", "\u010Cet", "Pet", "Sub", "Ned"};
  Object[] [] dataTable = { {" 1", " 2", " 3", " 4", " 5", " 6", " 7"},
                            {" 8", " 9", "10", "11", "12", "13", "14"},
                            {"15", "16", "17", "18", "19", "20", "21"},
                            {"22", "23", "24", "25", "26", "27", "28"},
                            {"29", "30", "31", "  ", "  ", "  ", "  "}
                          };
  JraScrollPane jScrollPane1 = new JraScrollPane();
  JTable tab = new JTable(dataTable, columnNames);

  public raCalendar() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
/*    tab.setBackground(Color.lightGray);
    tab.setBorder(BorderFactory.createEtchedBorder());
    tab.setGridColor(Color.black);
    tab.setPreferredScrollableViewportSize(new Dimension(250, 70));
    tab.setCellSelectionEnabled(false);*/
//    dm=hr.restart.util.dM.getDataModule();
    tab.setRowHeight(25);
//    this.getContentPane().add(ok, BorderLayout.SOUTH);
    this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(tab, null);
  }
}