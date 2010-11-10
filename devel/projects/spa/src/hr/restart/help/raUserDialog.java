/****license*****************************************************************
**   file: raUserDialog.java
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

import hr.restart.start;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraFrame;
import hr.restart.util.Aus;
import hr.restart.util.IntParam;
import hr.restart.util.raImages;
import hr.restart.util.raScreenHandler;
import hr.restart.util.startFrame;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

public class raUserDialog extends JraFrame {
  //OSTAVI JFrame, ODJEBI JraFrame
  // sorry but jegiba, opet je JraFrame (debagiran)
  private raUserPanel usrp = new raUserPanel();
  private static raUserDialog instance;
  public raUserDialog(Frame frame, String title, boolean modal) {
    //super(frame, title, modal);
    super(title);
    try {
      instance = this;
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  private boolean useCache = false;
  private boolean getCache = false;
  private Image cachedImage = null;
  private int cachedW, cachedH;
  public void paint(Graphics g) {
    if (getCache) {
      if (cachedImage == null || cachedW != getWidth() || cachedH != getHeight())
        cachedImage = createImage(cachedW = getWidth(), cachedH = getHeight());
      super.paint(cachedImage.getGraphics());
      getCache = false;
      useCache = true;
    }
    if (useCache) g.drawImage(cachedImage, 0, 0, this);
    else super.paint(g);
  }

  public static raUserDialog getInstance() {
    return instance;
  }

  public raUserDialog(Frame frame) {
    this(frame, "Poslovne aplikacije - izbornik", false);
  }
  public raUserDialog() {
    this(null, "Poslovne aplikacije - izbornik", false);
  }
  public void pack() {
//    super.pack();
  }
  public void calcSizeAndPosition(boolean left) {
    int maxDlgW = 250;
    int scrW = Toolkit.getDefaultToolkit().getScreenSize().width;
    int scrH = Toolkit.getDefaultToolkit().getScreenSize().height;
    int dlgW = scrW/4>maxDlgW?maxDlgW:scrW/4;
    setSize(dlgW,scrH-(raScreenHandler.getSFRy(0)==0?50:raScreenHandler.getSFRy(0)));
    if (left) {
      setLocation(0,raScreenHandler.getSFRy(0));
    } else {
      setLocation(scrW-dlgW,raScreenHandler.getSFRy(0));
    }
  }
  void jbInit() throws Exception {
    setIconImage(raImages.getImageIcon(raImages.IMGRAICON).getImage());
    getContentPane().add(usrp);
    setJMenuBar(usrp.getJMenuBar());
    start.invokeAppleUtilMethod("userDialogInit",new raUserDialog[] {this}, new Class[] {raUserDialog.class});
/*    addComponentListener(new ComponentAdapter() {
    	public void componentShown(ComponentEvent e) {
    		getUserPanel().getTaskTree().initFromTree(getUserPanel().getMenuTree());
    	}
    });*/
    mesNorm = jMes.getBackground();
    mesRed = Aus.halfTone(mesNorm, Color.red, 0.3f); 
    jMes.setBackground(Aus.halfTone(jMes.getBackground(), Color.red, 0.3f));
    jMes.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stopMessageFlash();
        startFrame.getStartFrame().showFrame("hr.restart.help.frmMessages", "Poruke");
      }
    });
  }
  
  JraButton jMes = new JraButton();
  Color mesRed, mesNorm; 
  Timer flasher = new Timer(1500, new ActionListener() {
    boolean on;
    public void actionPerformed(ActionEvent e) {
      jMes.setBackground(on ? mesRed : mesNorm);
      on = !on;
      flasher.setDelay(on ? 1500 : 300);
    }
  });
  
  public void stopMessageFlash() {
    flasher.stop();
    jMes.setBackground(mesNorm);
  }
  
  public void startMessageFlash() {
    jMes.setBackground(mesRed);
    flasher.setDelay(1500);
    flasher.start();
  }
  
  public void updateMessageButton(final boolean refresh) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        int num = MsgDispatcher.getUnread();
        boolean memb = getContentPane().isAncestorOf(jMes);
        if (memb && num == 0) {
          stopMessageFlash();
          getContentPane().remove(jMes);
          ((JPanel) getContentPane()).revalidate();
        }
        if (num > 0) jMes.setText(Aus.getNum(num, 
               "nova poruka", "nove poruke", "novih poruka"));
        if (!memb && num > 0) {
          startMessageFlash();
          getContentPane().add(jMes, BorderLayout.NORTH);
          ((JPanel) getContentPane()).revalidate();
        }
        getContentPane().repaint();
        frmMessages mes = frmMessages.getInstance();
        if (refresh && mes != null && mes.isShowing()) {
        	mes.getRaQueryDataSet().refresh();
        	mes.getRaQueryDataSet().last();
        	mes.jeprazno();
        	mes.getJpTableView().fireTableDataChanged();
        } else if (num > 0) startMessageFlash();
      }
    });
  }

  private boolean smoothHidden = false;
  private boolean smoothShown = true;
  private SmoothShowListener sslistener = new SmoothShowListener();
  void startSmoothShow() {
    Toolkit.getDefaultToolkit().removeAWTEventListener(sslistener);
    Toolkit.getDefaultToolkit().addAWTEventListener(sslistener, AWTEvent.MOUSE_MOTION_EVENT_MASK);
  }
  void stopSmoothShow() {
    Toolkit.getDefaultToolkit().removeAWTEventListener(sslistener);
  }

  private int smoothShowingStep = -1;
  private int getSmoothShowingStep() {
    if (smoothShowingStep>=0) return smoothShowingStep;
    String _sss = IntParam.getTag("smoothShowingStepUSRD");
    if (_sss.equals("")) {
      IntParam.setTag("smoothShowingStepUSRD","20");
      smoothShowingStep = 20;
    } else {
      try {
        smoothShowingStep = Integer.parseInt(_sss);
      } catch (Exception e) {
        smoothShowingStep = 20;
      }
    }
    return smoothShowingStep;
  }

  private int smoothShowingSpeed = -1;
  private int getSmoothShowingSpeed() {
    if (smoothShowingSpeed>=0) return smoothShowingSpeed;
    String _sss = IntParam.getTag("smoothShowingSpeedUSRD");
    if (_sss.equals("")) {
      IntParam.setTag("smoothShowingSpeedUSRD","10");
      smoothShowingSpeed = 10;
    } else {
      try {
        smoothShowingSpeed = Integer.parseInt(_sss);
      } catch (Exception e) {
        smoothShowingSpeed = 10;
      }
    }
    return smoothShowingSpeed;
  }

  boolean hiding;

  javax.swing.Timer hideTimer = new javax.swing.Timer(10, new ActionListener() {
    public void actionPerformed(ActionEvent e) {
      if (!isShowing()) {
        hideTimer.stop();
        return;
      }
      if (getSmoothShowingStep() > 0) {
        if (hiding) {
          if ((Toolkit.getDefaultToolkit().getScreenSize().width-getLocationOnScreen().x) > 10) {
            setLocation(Math.min(Toolkit.getDefaultToolkit().getScreenSize().width - 10,
              getLocation().x + getSmoothShowingStep()), getLocation().y);
          } else {
            smoothHidden = true;
            hideTimer.stop();
            useCache = false;
          }
        } else {
          if ((Toolkit.getDefaultToolkit().getScreenSize().width-getLocationOnScreen().x) < getWidth()) {
            setLocation(Math.max(Toolkit.getDefaultToolkit().getScreenSize().width-getWidth(),
              getLocation().x - getSmoothShowingStep()), getLocation().y);
          } else {
            smoothShown = true;
            hideTimer.stop();
            useCache = false;
          }
        }
      } else {
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width-
                    (hiding ? 10 : getWidth()), getLocation().y);
        hideTimer.stop();
        useCache = false;
      }
    }
  });

  private void smoothShow() {
    if (!isShowing()) {
      setLocation(Toolkit.getDefaultToolkit().getScreenSize().width-getWidth(),0);
      raScreenHandler.disableHandler();
      show();
      raScreenHandler.enableHandler();
    }
    toFront();
    smoothHidden = false;
    hiding = false;
    getCache = true;
    if (!hideTimer.isRunning()) {
      hideTimer.setDelay(getSmoothShowingSpeed());
      hideTimer.setCoalesce(true);
      hideTimer.setRepeats(true);
      hideTimer.start();
    }
  }

  private void smoothHide() {
    if (!isShowing()) return;
    smoothShown = false;
    hiding = true;
    getCache = true;
    if (!hideTimer.isRunning()) {
      hideTimer.setDelay(getSmoothShowingSpeed());
      hideTimer.setCoalesce(true);
      hideTimer.setRepeats(true);
      hideTimer.start();
    }
  }

  class SmoothShowListener implements AWTEventListener {
		public void eventDispatched(AWTEvent event) {
      //System.out.println("event "+event);
      if (!(event instanceof MouseEvent)) {
        System.out.println("not MouseEnvent - "+event.getClass());
        return;
      }
      MouseEvent me = (MouseEvent)event;
      Window mew = null;
      if (!(me.getSource() instanceof JComponent)) {
        if ((me.getSource() instanceof Window)) {
          mew = (Window)me.getSource();
        } else {
          System.out.println("source not JComponent - "+me.getSource().getClass());
          return;
        }
      }
      if (mew == null) {
        JComponent mec = (JComponent)me.getSource();
        if (!(mec.getTopLevelAncestor() instanceof Window)) {
          System.out.println("toplevelAncestor not Window - "+mec.getTopLevelAncestor().getClass());
          return;
        }
        mew = (Window)mec.getTopLevelAncestor();
      }
      if (mew != null) {
        if (mew instanceof raUserDialog && smoothHidden) {
          smoothShow();
        } else if (!(mew instanceof raUserDialog)) {
          if (smoothShown) smoothHide();
        }
/*        int X = mew.getLocationOnScreen().x + me.getX();
        int Y = mew.getLocationOnScreen().y + me.getY();
        if (!(mew instanceof raUserDialog) && (Toolkit.getDefaultToolkit().getScreenSize().width-X)<100) {
          if (!raUserDialog.this.isShowing()) smoothShow();
        } else if ((mew instanceof raUserDialog) && smoothHidden) {
System.out.println("mew instanceof raUserDialog && smoothHidden");
          smoothShow();
        } else if ((Toolkit.getDefaultToolkit().getScreenSize().width-X)>200 &&
              !(mew instanceof raUserDialog
                || mew instanceof hr.restart.sisfun.frmPassword
                || mew instanceof hr.restart.dlgExit)) {
          if (raUserDialog.this.isShowing()) smoothHide();
        }
        System.out.println("mouse on "+mew.getClass().getName()+"("+X+","+Y+") \n  smoothHidden = "+smoothHidden+"\n  smoothShown = "+smoothShown);
*/

      } else {
        System.out.println("window not determined");
      }
    }
  }
  public raUserPanel getUserPanel() {
    return usrp;
  }
  private boolean shortcutPanelsLoaded = false;
  public void show() {
    hr.restart.util.raScreenHandler.showingUserDialog(this);
    if (!shortcutPanelsLoaded) {
      usrp.loadShortcutPanels();
      shortcutPanelsLoaded = true;
    }
    super.show();
    if (raUserPanel.getAutoHideOption()) startSmoothShow();
  }
  public void hide() {
    if (hr.restart.util.raScreenHandler.hidingUserDialog(this)) {
      stopSmoothShow();
      super.hide();
    }
  }
  public boolean autoHide() {
    if (raUserPanel.getAutoHideOption()) {
      //super.hide();
      smoothHide();
      return true;
    }
    return false;
  }
  public boolean autoShow() {
    if (raUserPanel.getAutoHideOption()) {
      smoothShow();
      return true;
    }
    return false;
  }
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      hr.restart.util.startFrame.getStartFrame();
      raUserDialog rud = new raUserDialog();
      rud.pack();
      rud.show();
    }
    catch (Exception ex) {

    }
  }

}