/****license*****************************************************************
**   file: dlgExit.java
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
 * dlExit.java
 *
 * Created on 2003. listopad 20, 13:40
 */

package hr.restart;

import hr.restart.util.FileHandler;
import hr.restart.util.IntParam;
import hr.restart.util.raImages;
import hr.restart.util.raSkinDialog;
import hr.restart.util.raSwitchDialog;
import hr.restart.util.startFrame;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
/**
 *
 * @author  andrej
 */
public class dlgExit extends JWindow {
  private JPanel jpContent;
  //private int result = 0; //0 = povratak, 1=ch user, 2= ch knjig, 3=izlaz
  private static dlgExit _this;
  private int cursorPosition = 0;
  private JLabel[] labels;
  private JLabel jlTitle = new JLabel("Izlazne operacije");
  private JLabel jlMessage = new JLabel();
//colors  
  private static Color selBackground = hr.restart.help.raAbstractShortcutContainer.listSelectionBackground;
  private static Color selBorderColor = hr.restart.help.raAbstractShortcutContainer.listSelectionBorder;
  private static Color unselBackGround = Color.white;
  private static Color panelBorderColor = hr.restart.help.raAbstractShortcutContainer.listSelectionBackground;

  private AWTEventListener awtlistener = new AWTEventListener() {
    public void eventDispatched(AWTEvent event) {
      //System.out.println("getID = "+event.getID());
      if (event.getID() != 402) return;//402 je keyreleased
      if (event instanceof KeyEvent) {
        KeyEvent keyevent = (KeyEvent)event;
        if (keyevent.isConsumed()) return;
        if (keyevent.getKeyCode() == KeyEvent.VK_LEFT) {
          selectLeft();
        } else if (keyevent.getKeyCode() == KeyEvent.VK_RIGHT) {
          selectRight();
        } else if (keyevent.getKeyCode() == KeyEvent.VK_ENTER) {
          exitClicked(cursorPosition);
        } else if (keyevent.getKeyCode() == KeyEvent.VK_ESCAPE) {
          exitClicked(0);
        } else if (keyevent.getKeyCode() == KeyEvent.VK_T && keyevent.getModifiers() == KeyEvent.CTRL_MASK) {
          exitClicked(99); //Alati
        }
        keyevent.consume();
      }
      
    }
  };
  /** Creates a new instance of dlExit */
  protected dlgExit() {
    super(startFrame.getStartFrame());
    jInit();
  }
  private void jInit() {
    jpContent = new JPanel(new GridLayout(1, 4));
    jpContent.setBorder(BorderFactory.
      createMatteBorder(0,2,2,2,panelBorderColor));
      //createBevelBorder(javax.swing.border.BevelBorder.RAISED, Color.white, hr.restart.help.raAbstractShortcutContainer.listSelectionBackground));
    labels = new JLabel[4];
    labels[0] = getLabel(raImages.IMGDLGXBACK, "Povratak", 0, "Zatvara ovu poruku bez akcije");
    labels[1] = getLabel(raImages.IMGDLGXUSER, "Korisnik", 1, "Promjena korisnika");
    labels[2] = getLabel(raImages.IMGDLGXKNJIG, "Knjigovodstvo", 2, "Promjena knjigovodstva");
    labels[3] = getLabel(raImages.IMGDLGXEXIT, "Izlaz", 3, "Izlaz iz cijelog sustava");
    for (int i=0; i<labels.length; i++) jpContent.add(labels[i]);
    jlTitle.setBackground(selBackground);
    jlTitle.setOpaque(true);
    jlTitle.setBorder(BorderFactory.createMatteBorder(2,2,2,2,panelBorderColor));
    jlTitle.setFont(jlTitle.getFont().deriveFont(Font.BOLD|Font.ITALIC, jlTitle.getFont().getSize()+2));
    jlTitle.setHorizontalAlignment(JLabel.CENTER);
    jlMessage.setBackground(unselBackGround);
    jlMessage.setOpaque(true);
    jlMessage.setBorder(BorderFactory.createMatteBorder(0,2,2,2,panelBorderColor));
    jlMessage.setText(labels[0].getToolTipText());
    jlMessage.setHorizontalAlignment(JLabel.CENTER);
    addComponentListener(new ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        addKeyListeners();
      }
    });
    getContentPane().add(jpContent, BorderLayout.CENTER);
    getContentPane().add(jlTitle, BorderLayout.NORTH);
    getContentPane().add(jlMessage, BorderLayout.SOUTH);
  }
  
  private JLabel getLabel(String icondesc, String text, final int res, String toolTipText) {
    ImageIcon ic = raImages.getImageIcon(icondesc);
    final JLabel jl = new JLabel(text, ic, SwingConstants.CENTER) {
      public boolean isFocusTraversable() {
        return true;
      }
    };
    jl.setToolTipText(toolTipText);
    jl.setHorizontalTextPosition(SwingConstants.CENTER);
    jl.setVerticalTextPosition(SwingConstants.BOTTOM);
    jl.setOpaque(true);
    jl.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        exitClicked(res);
      }
      public void mouseEntered(MouseEvent e) {
        for (int i=0; i<labels.length; i++) setSelected(labels[i], i == res);
        setSelected(jl,true);
        cursorPosition = res;
      }
      public void mouseExited(MouseEvent e) {
        setSelected(jl,false);
      }
    });
    return jl;
  }
  
  public static void setSelectedUI(JLabel jl, boolean isSelected) {
    if (isSelected) {
      jl.setBackground(selBackground);
      jl.setBorder(BorderFactory.createLineBorder(selBorderColor,1));
    } else {
      jl.setBackground(unselBackGround);
      jl.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
    }
  }
  
  private void setSelected(JLabel jl, boolean isSelected) {
    setSelectedUI(jl, isSelected);
    if (isSelected) jlMessage.setText(jl.getToolTipText()+" - ENTER");
  }
  
  public void pack() {
    super.pack();
    this.setLocation((start.getSCREENSIZE().width-this.getSize().width)/2, (start.getSCREENSIZE().height-this.getSize().height)/2);
    for (int i=0; i<labels.length; i++) setSelected(labels[i], i == 0);
  }
  private void addKeyListeners() {
    Toolkit.getDefaultToolkit().addAWTEventListener(awtlistener, AWTEvent.KEY_EVENT_MASK);    
  }
  private void rmvKeyListeners() {
    Toolkit.getDefaultToolkit().removeAWTEventListener(awtlistener);
  }
  public static void showExitMessage() {
    if (_this == null) _this = new dlgExit();
    _this.pack();
    _this.show();
  }
  private void exitClicked(int res) {
    dlgExit.this.hide();
    rmvKeyListeners();
    if (res == 0) {
    } else if (res == 1) {
      startFrame.changeUser();
    } else if (res == 2) {
      hr.restart.zapod.dlgGetKnjig.showDlgGetKnjig();
    } else if (res == 3) {
      try {
        if (start.exiting()) System.exit(0);
      }
      catch (Exception ex) {
        ex.printStackTrace();
        try {
          JOptionPane.showMessageDialog(null, "Pojavila se greska pri izlasku iz aplikacije: "+ex, "ERROR", JOptionPane.ERROR_MESSAGE);
          System.exit(0);
        } catch (Exception ex2) {
          System.exit(0);
        }
      }
    } else if (res == 99) {
      final boolean orgSFMain = startFrame.SFMain;
      startFrame.SFMain = true;
      startFrame.getStartFrame().showFrame("hr.restart.util.optionsDialog",0,"Alati");
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          startFrame.SFMain = orgSFMain;    
        }
      });
      
    }
  }
  private void selectLeft() {
    setSelected(labels[cursorPosition], false);
    cursorPosition = cursorPosition>0?cursorPosition-1:3;
    setSelected(labels[cursorPosition], true);
  }
  private void selectRight() {
    setSelected(labels[cursorPosition], false);
    cursorPosition = cursorPosition<3?cursorPosition+1:0;
    setSelected(labels[cursorPosition], true);
  }

  public static void switchKnjig() {
    raSwitchDialog swKnjig = new raSwitchDialog() {
        public boolean doSwitch(Object oldvalue, Object newValue) {
          //System.out.println("mijenjam iz "+oldvalue+" u "+newValue);
          hr.restart.zapod.dlgGetKnjig.changeKnjig(newValue.toString(), false);
          return true;
        }
    };
    com.borland.dx.sql.dataset.QueryDataSet knjigovodstva = hr.restart.zapod.OrgStr.getOrgStr().getKnjigovodstva();
    knjigovodstva.setSort(new com.borland.dx.dataset.SortDescriptor(new String[] {"CORG"}));
    swKnjig.setSwitchSet(knjigovodstva);
    swKnjig.setSwColumnName("CORG");
    swKnjig.setSwPreviewCols(new String[] {"NAZIV","ADRESA","MJESTO","CORG"}); 
    swKnjig.setSwListcolumn("NAZIV");
    swKnjig.setSwImageDescs(raSwitchDialog.translateImageProperties("knjig_img.properties",knjigovodstva,"CORG"));
    //new String[] {"robno","pl","pl","sk","gk","sk"});
    swKnjig.setInitialValue(hr.restart.zapod.OrgStr.getKNJCORG(false));
    startFrame.getStartFrame().centerFrame(swKnjig, 0, "Odabir knjigovodstva za rad");
    swKnjig.show();
  }
  public static boolean isFancySwitch() {
    java.util.Properties mainprops = FileHandler.getProperties(IntParam.PROPSFILENAME);
    if (!mainprops.containsKey("fancySwitch")) {
      mainprops.setProperty("fancySwitch", "false");
      FileHandler.storeProperties(IntParam.PROPSFILENAME,mainprops);
    }
    return IntParam.getTag("fancySwitch").equals("true");
  }
  /** samo za testiranje switchKnjig 
   */
  public static void main(String[] args) {
    raSkinDialog.makeLookAndFeel();
    switchKnjig();
    System.exit(0);
  }
}
