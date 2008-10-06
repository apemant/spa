/****license*****************************************************************
**   file: raTestis.java
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
package hr.restart.robno;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class raTestis extends JFrame {
  private JPanel jPanel1 = new JPanel();
  java.util.ArrayList podaci = new java.util.ArrayList();
  Object [][] tmp= {
    {"Label1", "hr.restart.robno.frmArtikli"},
    {"Label2", "hr.restart.robno.frmFranka"},
    {"Label3", "hr.restart.robno.frmNamjena"}
  };
  private JMenuBar jMenuBar1 = new JMenuBar();
  private JMenu jMenu1 = new JMenu("Prvi");
  private JMenuItem jMenuItem1 = new JMenuItem("Item 1");
  private JMenuItem jMenuItem2 = new JMenuItem("Item 2");
  private JButton jButton1 = new JButton();

  public raTestis() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public Font getFont() {
    return super.getFont();
  }
  private void jbInit() throws Exception {
    Ajtem p;
    jButton1.setText("jButton1");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton1_actionPerformed(e);
      }
    });
    this.getContentPane().add(jPanel1, BorderLayout.CENTER);
    jPanel1.add(jButton1, null);
    jMenuBar1.add(jMenu1);
    jMenu1.add(jMenuItem1);
    jMenu1.add(jMenuItem2);
    jMenu1.addSeparator();

    for (int x=0;x<tmp.length;x++) {
      podaci.add(p=new Ajtem((String) tmp[x][0], (String) tmp[x][1]));
      jMenu1.add(p.getMenu());
      p.getMenu().addActionListener(p.getListener());
    }

    this.setJMenuBar(jMenuBar1);
  }

  void jButton1_actionPerformed(ActionEvent e) {
    int i=2;
    do {
      System.out.println("i: "+i);
      i++;
    } while (i<1000);
  }
}

class Ajtem {
  static JMenuItem men;
  static String klasa;
  static ActionListener listener;
  public Ajtem(String str, String str2) {
    System.out.println("String: "+str+", "+str2);
    men = new JMenuItem(str);
    klasa = new String(str2);
    listener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.out.println("Klasa: "+klasa);
        _Main.getStartFrame().showFrame(klasa, klasa);
      }
    };
  }
  public static JMenuItem getMenu() {
    return men;
  }
  public static String getRunClass() {
    return klasa;
  }
  public static ActionListener getListener() {
    return listener;
  }
}
