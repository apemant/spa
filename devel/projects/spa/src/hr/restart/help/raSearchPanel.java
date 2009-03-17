/****license*****************************************************************
**   file: raSearchPanel.java
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
package hr.restart.help;

import hr.restart.swing.JraButton;
import hr.restart.util.Aus;
import hr.restart.util.raImages;
import hr.restart.util.raProcess;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class raSearchPanel extends JPanel {
  JPanel jpSearchInput = new JPanel();
  JPanel jpSearchRes = new JPanel();
//  JList jlSearchRes = new JList();
  raAbstractShortcutContainer searchRes;
  raShortcutNavBar bar = new raShortcutNavBar(new raAbstractShortcutContainer() {
    public boolean isShortcutTarget() {
      return false;
    }
  });
  
  JLabel jlSRTitle = new JLabel();
  private JTextField jtSearchString = new JTextField();
  private JraButton jrbSearch = new JraButton();
  private JraButton jrbSearchWeb = new JraButton();
  private JPanel jpSearchButtons = new JPanel();
  public raSearchPanel() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  void jbInit() throws Exception {
    setLayout(new BorderLayout());
    jpSearchInput.setLayout(new BorderLayout());
    jpSearchRes.setLayout(new BorderLayout());
    jlSRTitle.setFont(jlSRTitle.getFont().deriveFont(Font.BOLD));
    jlSRTitle.setText(" Rezutati");
    jpSearchRes.add(jlSRTitle,BorderLayout.NORTH);
//    jpSearchRes.add(jlSearchRes,BorderLayout.CENTER);
    initSearchText();
    initSearchButton();
    jpSearchInput.add(jtSearchString,BorderLayout.CENTER);
    jpSearchInput.add(jpSearchButtons ,BorderLayout.SOUTH);
    initSearchRes();
    add(jpSearchInput,BorderLayout.NORTH);
    add(jpSearchRes,BorderLayout.CENTER);
  }
  void initSearchText() {
    jtSearchString.addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode()==e.VK_ENTER) {
          startSearch();
          e.consume();
        }
      }
    });
    jtSearchString.addFocusListener(new FocusAdapter() {
      public void focusGained(FocusEvent e) {
        jtSearchString.selectAll();
      }
    });
    jtSearchString.setText("Text za pretraživanje");
  }
  public raShortcutNavBar getShortcutNavBar() {
    return bar;
  }
  void initSearchButton() {
    jrbSearch.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        startSearch();
      }
    });
    jrbSearch.setText("Traži u SPA");
//    jrbSearch.setIcon(raImages.getImageIcon(raImages.IMGSTICKER,21,false));
    jrbSearch.setHorizontalAlignment(SwingConstants.LEFT);
    jrbSearchWeb.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          raLiteBrowser.openSystemBrowser(
              new URL("http://www.google.hr/search?q="+
                  Aus.convertToURLFriendly(jtSearchString.getText())));
        } catch (MalformedURLException e1) {
          e1.printStackTrace();
        }
      }
    });
    jrbSearchWeb.setText("Traži na webu");
//    jrbSearchWeb.setIcon(raImages.getImageIcon(raImages.IMGPOGODAK,21,false));
    jrbSearchWeb.setHorizontalAlignment(SwingConstants.LEFT);
    GridLayout lay = new GridLayout(2, 1);
    jpSearchButtons.setLayout(lay);
    jpSearchButtons.add(jrbSearch);
    jpSearchButtons.add(jrbSearchWeb);
  }
  void initSearchRes() {
    try {
      if (searchRes != null) {
        bar.unregisterNavKeys(searchRes);
        jpSearchRes.remove(searchRes);
        searchRes = null;
      }
    } catch (Exception e) {
      System.out.println("raSearchPanel.initSearchRes ex :: "+e);
    }
//    jlSearchRes.setListData(new String[] {"Nema rezultata"});
  }
  void startSearch() {
    initSearchRes();
    raProcess.runChild(this, new spaSearch());
    getParent().requestFocus();
  }
  class spaSearch implements Runnable {
    public void run() {
      searchRes = raSearchSPA.search(jtSearchString.getText(), true, false, false);
      jpSearchRes.add(searchRes);
      bar.setShortcutContainer(searchRes);
      bar.registerNavKeys(searchRes);
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          raSearchPanel.this.validateTree();
        }
      });
      
    }
  }
  /*
  class fakeSearch extends Thread {
    public void run() {
      long now = System.currentTimeMillis();
      String dots = ".";
      jlSearchRes.setListData(new String[] {"Pretraživanje u tijeku"+dots});
      while (System.currentTimeMillis() < now+5000) {
        if (System.currentTimeMillis()%100 == 0) {
          dots = dots.concat(".");
          jlSearchRes.setListData(new String[] {"Pretraživanje u tijeku"+dots});
        }
      }
      jlSearchRes.setListData(new String[] {"Nema rezultata"});
    }
  };*/
}