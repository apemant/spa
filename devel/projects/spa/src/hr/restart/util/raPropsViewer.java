/****license*****************************************************************
**   file: raPropsViewer.java
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
/*
 * PropsViewer.java
 *
 * Created on 2003. rujan 05, 13:39
 */

package hr.restart.util;

import hr.restart.swing.JraComboBox;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
/**
 *
 * @author  andrej
 */
public class raPropsViewer implements okFrame {

  private JPanel jpContent = new JPanel(new BorderLayout());
  private JPanel jpChooser = new JPanel(new BorderLayout());
  private JPanel jpBrowser = new JPanel(new BorderLayout());

  private JraComboBox jcbFileList;

  /** Creates a new instance of PropsViewer */
  public raPropsViewer() {
    initContent();
  }

  private void initContent() {
    jcbFileList = getFileCombo();
    JLabel jlFile = new JLabel("Datoteka");
    jlFile.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 50));
    JPanel jcbPan = new JPanel(new BorderLayout());
    jcbPan.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    jcbPan.add(jcbFileList);
    jpChooser.add(jlFile,BorderLayout.WEST);
    jpChooser.add(jcbPan,BorderLayout.CENTER);
    jpChooser.setBorder(BorderFactory.createEtchedBorder());
    jpContent.add(jpChooser,BorderLayout.NORTH);
    jpContent.add(jpBrowser,BorderLayout.CENTER);
    jcbFileList.setSelectedItem("System.properties");
  }

  private JraComboBox getFileCombo() {
    String[] pfs = new File(System.getProperty("user.dir")).list(
        new FilenameFilter() {
          public boolean accept(File dir,String name) {
            return name.endsWith(".properties");
          }
         });
    Arrays.sort(pfs);
    
    JraComboBox combo = new JraComboBox(pfs);
    combo.addItem("System.properties");
    combo.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent e) {
        filePicked();
      }
    });
    return combo;
  }

  private String currFile = "";
  private java.util.HashMap propsDialogMap = new java.util.HashMap();
  private void filePicked() {
    String pickedFile = jcbFileList.getSelectedItem().toString();
    if (pickedFile.equals(currFile)) return;
    propsDialog propsList = (propsDialog)propsDialogMap.get(pickedFile);
//    if (!currFile.equals(""))
//      jpBrowser.remove(((propsDialog)propsDialogMap.get(currFile)).getContentPane());
    jpBrowser.removeAll();
    if (propsList == null) {
      propsList = new propsDialog(pickedFile);
      propsList.getOKpanel().setVisible(false);
      propsDialogMap.put(pickedFile, propsList);
      propsList.jp.initKeyListener(propsList.getContentPane());
    } else
    currFile = pickedFile;
    jpBrowser.add(propsList.getContentPane());
    jpContent.revalidate();
    jpContent.repaint();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jpContent.revalidate();
        jpContent.repaint();
      }
    });
  }
  public void action_jBOK() {
    loopPDMap(true);
  }

  public void action_jPrekid() {
  }

  public boolean doSaving() {
    return loopPDMap(false);
  }

  public void reloadAll() {
    for (Iterator i = propsDialogMap.values().iterator(); i.hasNext(); )
      ((propsDialog) i.next()).reload();
  }
  
  public boolean loopPDMap(boolean action) {
    for (Iterator i = propsDialogMap.keySet().iterator(); i.hasNext(); ) {
      Object item = i.next();
      propsDialog pd = (propsDialog)propsDialogMap.get(item);
      if (pd.doSaving()) {
        if (action) pd.action_jBOK();
        else return true;
      }
    }
    return false;
  }

  public java.awt.Container getContentPane() {
    return jpContent;
  }

  public OKpanel getOKpanel() {
    return null;
  }

  public String getTitle() {
    return "Properties";
  }
}
