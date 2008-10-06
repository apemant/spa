/****license*****************************************************************
**   file: MemDialog.java
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

import hr.restart.swing.JraDialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class MemDialog extends JraDialog implements okFrame {
  OKpanel okp = new OKpanel() {
    public void jPrekid_actionPerformed() {
      action_jPrekid();
    }
    public void jBOK_actionPerformed() {
      action_jBOK();
    }
  };
  JPanel jPnl = new JPanel();
  JPanel jMainPane = new JPanel();
  XYLayout xYLay = new XYLayout();
  JLabel jLTotal = new JLabel();
  JLabel jLFree = new JLabel();
  JLabel jLUsed = new JLabel();
  JButton jBrefresh = new JButton();
  JButton jBGC = new JButton();
  JLabel jlbTotal = new JLabel();
  JLabel jlbFree = new JLabel();
  JLabel jlbUsed = new JLabel();

  public MemDialog() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void action_jPrekid() {
  }

  public void action_jBOK() {
  }

  public OKpanel getOKpanel() {
    return okp;
  }
  private void jbInit() throws Exception {
    setTitle("Memorija");
    jPnl.setLayout(xYLay);
    jLTotal.setText("Ukupno:");
    jLFree.setText("Slobodno:");
    jLUsed.setText("Iskorišteno:");
    jBrefresh.setText("Prikaži");
    jBrefresh.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jBrefresh_actionPerformed(e);
      }
    });
    jBGC.setText("GC");
    jBGC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jBGC_actionPerformed(e);
      }
    });
    jlbTotal.setHorizontalAlignment(SwingConstants.RIGHT);
    jlbTotal.setText("0 MB");
    jlbFree.setText("0 MB");
    jlbFree.setHorizontalAlignment(SwingConstants.RIGHT);
    jlbUsed.setHorizontalAlignment(SwingConstants.RIGHT);
    jlbUsed.setText("0 MB");
    xYLay.setWidth(215);
    xYLay.setHeight(145);
    jPnl.add(jLTotal, new XYConstraints(20, 15, -1, -1));
    jPnl.add(jLFree, new XYConstraints(20, 40, -1, -1));
    jPnl.add(jLUsed, new XYConstraints(20, 65, -1, -1));
    jPnl.add(jBrefresh, new XYConstraints(20, 100, 70, 21));
    jPnl.add(jBGC, new XYConstraints(120, 100, 70, 21));
    jPnl.add(jlbTotal, new XYConstraints(100, 15, 90, -1));
    jPnl.add(jlbFree, new XYConstraints(100, 40, 90, -1));
    jPnl.add(jlbUsed, new XYConstraints(100, 65, 90, -1));
    jPnl.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
//    jMainPane.setLayout(new GridBagLayout());
//    jMainPane.add(jPnl);
    raMatPodaci.addCentered(jPnl,jMainPane);
    getContentPane().add(jMainPane, BorderLayout.CENTER);
    refresh();
  }
  void refresh() {
    Runtime curRt = Runtime.getRuntime();
    long ltotal = curRt.totalMemory();
    long lfree = curRt.freeMemory();
    long lused = ltotal - lfree;
    jlbTotal.setText(dubToString(ltotal));
    jlbFree.setText(dubToString(lfree));
    jlbUsed.setText(dubToString(lused));
  }
  String dubToString(long lng) {
    java.math.BigDecimal bd = java.math.BigDecimal.valueOf(lng,1);
    java.math.BigDecimal bdd = java.math.BigDecimal.valueOf(1048576,1);
    bd = bd.divide(bdd,bd.ROUND_UP);
    return bd.toString()+" MB";
  }
  void jBrefresh_actionPerformed(ActionEvent e) {
    refresh();
  }

  public boolean doSaving() {
    return false;
  }

  void jBGC_actionPerformed(ActionEvent e) {
    System.gc();
    refresh();
  }
}