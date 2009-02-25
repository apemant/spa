/****license*****************************************************************
**   file: raShortcutItem.java
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

import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.IntParam;
import hr.restart.util.VarStr;
import hr.restart.util.raImages;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.JLabel;


/**
 * @author Andrej
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class raShortcutItem extends JLabel implements Action {
  private String index = null;
  private boolean selected;
  private Color orgForeground = getForeground();
  /**
   * Constructor for raShortcutItem.
   */
  public raShortcutItem() {
    jInit();
  }
  public raShortcutItem(hr.restart.util.MenuTree.treeOption opt) {
    setIndex(raShortcutFileHandler.getOptionIndex(opt.getMenuItem()));
    jInit();
  }
  public raShortcutItem(String optIdx) {
    setIndex(optIdx);
    jInit();
  }

  public void paint(java.awt.Graphics g) {
    super.paint(Aus.forceAntiAlias(g));
  }
  public void setIcon(javax.swing.Icon ico) {
    setIcon(ico,false);
  }
  public void setIcon(javax.swing.Icon ico, boolean scaleMinor) {
    if (ico instanceof javax.swing.ImageIcon && (ico.getIconHeight()>35||scaleMinor)) {
      javax.swing.ImageIcon iico = (javax.swing.ImageIcon)ico;
      iico.setImage(iico.getImage().getScaledInstance(35, 35, java.awt.Image.SCALE_SMOOTH));
      super.setIcon(iico);
    } else super.setIcon(ico);
  }
  private void jInit() {
//    setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED)); //-> renderer
    //addMouseListener(new shortcutItemMouseListener());
    setIcon(raImages.getImageIcon(raImages.IMGTAB));
//    setFont(getFont().deriveFont(Font.BOLD,(float)(getFont().getSize()+2)));
//    setFont(getFont().deriveFont(Font.ITALIC|Font.BOLD,(float)(getFont().getSize()+2)));
//    setFont(getFont().deriveFont(Font.ITALIC));
    setFancyFont(this);
  }
  private static int fancyStyle = -999, fancySize = -999;
  public static void setFancyFont(javax.swing.JComponent comp) {
//    System.out.println("****setFancyFont("+comp);
//    if (System.getProperty("os.name").toLowerCase().equals("linux") &&
//        System.getProperty("java.version").startsWith("1.3")) {
//      comp.setFont(comp.getFont().deriveFont(Font.PLAIN,(float)(comp.getFont().getSize()+2)));
//    } else comp.setFont(comp.getFont().deriveFont(Font.ITALIC,(float)(comp.getFont().getSize()+2)));
    /*
    1 mjesto: 0 = PLAIN, 1 = BOLD, 2 = ITALIC, 3 = BOLD&ITALIC 
    2 mjesto: +1, +2, -1 etc.
    primjeri 0+0 :: plain bez izmjene velicine
             1+2 :: Bold i za 2 veci (default)
             3+2 :: Bold i Italic i za 2 veci
     */
    if (fancyStyle == -999 || fancySize == -999) {
      String param = IntParam.getTag("fancyFont");
      try {
        fancyStyle = Integer.parseInt(param.substring(0,1));
        fancySize = Integer.parseInt(param.replace('+', ' ').substring(1).trim());
      } catch (Exception e) {
        System.out.println("*** Error in setFancyFont::");
        e.printStackTrace();
        fancyStyle = 1;
        fancySize = 2;
        IntParam.setTag("fancyFont", "1+2");
      }
    }
//    comp.setFont(comp.getFont().deriveFont(Font.BOLD,(float)(comp.getFont().getSize()+2)));
    comp.setFont(comp.getFont().deriveFont(fancyStyle,(float)(comp.getFont().getSize()+fancySize)));
  }
  /**
   * @see javax.swing.Action#getValue(String)
   */
  public Object getValue(String arg0) {
    return null;
  }

  /**
   * @see javax.swing.Action#putValue(String, Object)
   */
  public void putValue(String arg0, Object arg1) {
  }

  /**
   * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
   */
  public void actionPerformed(ActionEvent arg0) {
  }

  public boolean equals(Object o) {
    if (getIndex() != null && o instanceof raShortcutItem) {
      raShortcutItem i = (raShortcutItem) o;
      return getIndex().equals(i.getIndex());
    } else {
      return super.equals(o);
    }
  }
  public String getIndex() {
    return index;
  }
  public void setIndex(String index) {
    this.index = index;
  }
  
  public String toString() {
    return index + ": " + getText();
  }

  class shortcutItemMouseListener extends MouseAdapter {
    /**
     * @see java.awt.event.MouseListener#mouseEntered(MouseEvent)
     */
    public void mouseEntered(MouseEvent arg0) {
//      setFont(getFont().deriveFont(Font.BOLD));
      setForeground(Color.yellow);
      getGraphics().drawLine(0,getHeight()-5,getWidth()-5,getHeight()-5);
    }


    /**
     * @see java.awt.event.MouseListener#mouseExited(MouseEvent)
     */
    public void mouseExited(MouseEvent arg0) {
//      setFont(getFont().deriveFont(Font.PLAIN));
      setForeground(orgForeground);
      getGraphics().clearRect(0,getHeight()-5,getWidth()-5,2);
    }
  }

}
