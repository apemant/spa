/****license*****************************************************************
**   file: raFrame.java
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

import javax.swing.event.InternalFrameEvent;

/**
 * Title:        raFrame
 * Description:  Wrapper koji glumi JFrame ili JInternalFrame ili JDialog ovisno o modu
 * Copyright:    Copyright (c) 2001
 * Company:      RA
 * @author AI
 * @version 1.0
 */

public class raFrame extends java.awt.Container implements raFrameInterface {
  protected Stopwatch initSW = Stopwatch.startInit(this);
//sysoutTEST ST= new sysoutTEST(false);
  private int frameMode=0;
  public static final int FRAME = 0;
  /**
   * deprecated izbaceno iz upotrebe i ne moze se nigdje odabrati
   */
  public static final int INTERNALFRAME = 1;

  public static final int DIALOG = 2;
  public static final int PANEL = 3;
  public static final int MAXIMIZED=99;
  private javax.swing.JFrame Jframe;
  private javax.swing.JInternalFrame Iframe;
  private javax.swing.JDialog Jdialog;
  private java.awt.Container frameOwner;
  private java.util.EventListener[] windowListeners;
  private java.util.EventListener[] keyListeners;
  private boolean raVisible=false;

  public raFrame() {
//    this(hr.restart.start.FRAME_MODE,null);
    this(hr.restart.start.FRAME_MODE,hr.restart.mainFrame.getMainFrame());
//    ST.prn("FRAME_MODE = ",hr.restart.start.FRAME_MODE);
  }
  public raFrame(int mode,java.awt.Container owner) {
    this.setFrameOwner(owner);
    setFrameMode(mode);
  }
  /**
   * <pre>
   * frame mode se definira varijablama u klasi raFrame a to su:
   * - INTERNALFRAME za JInternalFrame
   * - FRAME   za JFrame
   * </pre>
   */
  public void setFrameMode(int newFrameMode) {
    frameMode=newFrameMode;
    commonInit();
  }

  public int getFrameMode() {
    return frameMode;
  }

  public void setFrameOwner(java.awt.Container myowner) {
    frameOwner = myowner;
  }

  public java.awt.Container getFrameOwner() {
    return frameOwner;
  }

  public javax.swing.JFrame getJframe() throws java.lang.NullPointerException {
    if (Jframe == null) throw new java.lang.NullPointerException();
    return Jframe;
  }
  public javax.swing.JInternalFrame getIframe() throws java.lang.NullPointerException {
    if (Iframe == null) throw new java.lang.NullPointerException();
    return Iframe;
  }

  public javax.swing.JDialog getJdialog() throws java.lang.NullPointerException {
    if (Jdialog == null) throw new java.lang.NullPointerException();
    return Jdialog;
  }
  
  public java.awt.Window getWindow() {
    if (frameMode == FRAME) return getJframe();
    if (frameMode == DIALOG) return getJdialog();
    if (frameMode == INTERNALFRAME||frameMode == PANEL) {
      return hr.restart.mainFrame.getMainFrame();
    }
    return null;
  }
  public static java.awt.Frame getMainFr() {
    java.awt.Frame mainFr;
    if (hr.restart.start.isMainFrame()) mainFr = hr.restart.mainFrame.getMainFrame();
    else mainFr = raLLFrames.getRaLLFrames().getMsgStartFrame();
    return mainFr;
  }
  private void commonInit() {
    if (frameOwner==null) {
      frameOwner = getMainFr();
    }
/*
    if (frameMode==INTERNALFRAME) {
      Iframe = new javax.swing.JInternalFrame("",true,true,true,true);
      Iframe.setDefaultCloseOperation(Iframe.HIDE_ON_CLOSE);
      ((hr.restart.mainFrame)frameOwner).mainDesk.add(Iframe);

      Jframe=null;
      Jdialog=null;
    } else
*/
    if (frameMode==FRAME||frameMode==PANEL) {
      Jframe = new hr.restart.swing.JraFrame();
      Iframe = null;
      Jdialog = null;
    } else
    if (frameMode==DIALOG) {
      // ante 3.7.2002.  ako je owner Dialog
      if (frameOwner instanceof java.awt.Dialog) {
        Jdialog = new hr.restart.swing.JraDialog((java.awt.Dialog) frameOwner);
      } else if (frameOwner instanceof java.awt.Frame) {
        Jdialog = new hr.restart.swing.JraDialog((java.awt.Frame) frameOwner);
      } else {
        Jdialog = new hr.restart.swing.JraDialog(getMainFr());
      }
      Iframe = null;
      Jframe = null;
    }
    setIconImage(raImages.getImageIcon(raImages.IMGRAICON).getImage());
  }

//FUNKCIJE INTERFACEA
  public void setVisible(boolean visible) {
/*    if (frameMode==INTERNALFRAME) {
      Iframe.setVisible(visible);
    } else
    if (frameMode==FRAME) {
      Jframe.setVisible(visible);
    } else
    if (frameMode==DIALOG) {
      Jdialog.setVisible(visible);
    }*/
    if (visible) show(); else hide();
  }

  public boolean isVisible() {
/*    if (frameMode==INTERNALFRAME) {
      return Iframe.isVisible();
    } else
    if (frameMode==FRAME) {
      return Jframe.isVisible();
    } else
    if (frameMode==DIALOG) {
      return Jdialog.isVisible();
    }
    return false;*/
    return raVisible;
  }

  public void setTitle(String title) {
    if (frameMode==INTERNALFRAME) {
      Iframe.setTitle(title);
    } else
    if (frameMode==FRAME) {
      Jframe.setTitle(title);
    } else
    if (frameMode==DIALOG) {
      Jdialog.setTitle(title);
    } else
    if (frameMode==PANEL) {
      Jframe.setTitle(title);
      javax.swing.JTabbedPane mainTab = hr.restart.mainFrame.getMainFrame().mainTabs;
      int idx = mainTab.indexOfComponent(getContentPane());
      if (idx > -1)  mainTab.setTitleAt(idx,title);
    }
  }

  public String getTitle() {
    if (frameMode==INTERNALFRAME) {
      return Iframe.getTitle();
    } else
    if (frameMode==FRAME||frameMode==PANEL) {
      return Jframe.getTitle();
    } else
    if (frameMode==DIALOG) {
      return Jdialog.getTitle();
    }
    return "";
  }

  public void setState(int state) {
    if (frameMode==INTERNALFRAME) {
      if (state==java.awt.Frame.ICONIFIED) {
        try {Iframe.setIcon(true);} catch (Exception e) {}
      } else
      if (state==java.awt.Frame.NORMAL) {
        try {Iframe.setMaximum(false);} catch (Exception e) {}
      } else
      if (state==this.MAXIMIZED) {
        try {Iframe.setMaximum(true);} catch (Exception e) {}
      }
    } else
    if (frameMode==FRAME) {
      Jframe.setState(state);
    } else
    if (frameMode==DIALOG) {
// VODA??
    }
  }

  public int getState() {
    if (frameMode==INTERNALFRAME) {
      boolean max = Iframe.isMaximum();
      boolean ico = Iframe.isIcon();
      if (max) return MAXIMIZED;
      if (ico) return java.awt.Frame.ICONIFIED;
      if (!max) return java.awt.Frame.NORMAL;
    } else
    if (frameMode==FRAME) {
      return Jframe.getState();
    } else
    if (frameMode==DIALOG) {
// VODA??
    }
    return -1;
  }

  public void pack() {
    if (frameMode==INTERNALFRAME) {
      Iframe.pack();
    } else
    if (frameMode==FRAME||frameMode==PANEL) {
      Jframe.pack();
    } else
    if (frameMode==DIALOG) {
      Jdialog.pack();
    }
  }

  public void validate() {
    if (frameMode==INTERNALFRAME) {
      Iframe.validate();
    } else
    if (frameMode==FRAME||frameMode==PANEL) {
      Jframe.validate();
    } else
    if (frameMode==DIALOG) {
      Jdialog.validate();
    }
  }

  public void setSize(int width, int height) {
    if (frameMode==INTERNALFRAME) {
      Iframe.setSize(width,height);
    } else
    if (frameMode==FRAME||frameMode==PANEL) {
      Jframe.setSize(width,height);
    } else
    if (frameMode==DIALOG) {
      Jdialog.setSize(width,height);
    }
  }

  public java.awt.Dimension getSize() {
    if (frameMode==INTERNALFRAME) {
      return Iframe.getSize();
    } else
    if (frameMode==FRAME) {
      return Jframe.getSize();
    } else
    if (frameMode==DIALOG) {
      return Jdialog.getSize();
    } else
    if (frameMode==PANEL) {
      return hr.restart.mainFrame.getMainFrame().mainTabs.getSize();
    }
    return new java.awt.Dimension();
  }

  public void setLocation(int x,int y) {
    if (frameMode==INTERNALFRAME) {
      Iframe.setLocation(x,y);
    } else
    if (frameMode==FRAME) {
      Jframe.setLocation(x,y);
    } else
    if (frameMode==DIALOG) {
      Jdialog.setLocation(x,y);
    }
  }

  public java.awt.Point getLocation() {
    if (frameMode==INTERNALFRAME) {
      return Iframe.getLocation();
    } else
    if (frameMode==FRAME) {
      return Jframe.getLocation();
    } else
    if (frameMode==DIALOG) {
      return Jdialog.getLocation();
    }
    return new java.awt.Point(0,0);
  }

  public void show() {
    Valid.setApp(this.getClass());
    raVisible=true;
    if (frameMode==INTERNALFRAME) {
//ST.prn("iframe.show !!");
      Iframe.show();
    } else
    if (frameMode==FRAME) {
      Jframe.show();
    } else
    if (frameMode==DIALOG) {
      Jdialog.show();
    } else
    if (frameMode==PANEL) {
      showP();
    }
    setState(java.awt.Frame.NORMAL);
  }

  public void toFront() {
    getWindow().show();
  }

  public void hide() {
    raVisible=false;
    if (frameMode==INTERNALFRAME) {
      Iframe.setVisible(false);
    } else
    if (frameMode==FRAME) {
      Jframe.hide();
    } else
    if (frameMode==DIALOG) {
      Jdialog.hide();
    } else
    if (frameMode==PANEL) {
      hideP();
    }
  }

  private void showP() {
    javax.swing.JTabbedPane mainTab = hr.restart.mainFrame.getMainFrame().mainTabs;
    int idx = mainTab.indexOfComponent(getContentPane());
    if (idx==-1) {
      pack();
      mainTab.addTab(getTitle(),raImages.getImageIcon(raImages.IMGTAB),getContentPane(),getTitle());
      idx = mainTab.indexOfComponent(getContentPane());
      getContentPane().addComponentListener(panelHandler);
      mainTab.setSelectedIndex(idx);
      componentShownPanel();
    } else {
      mainTab.setSelectedIndex(idx);
    }
    mainTab.requestFocus();
  }

  java.awt.event.ComponentAdapter panelHandler = new java.awt.event.ComponentAdapter() {
    public void componentShown(java.awt.event.ComponentEvent e) {
      handleKeyListeners(true,true);
    }
    public void componentHidden(java.awt.event.ComponentEvent e) {
      handleKeyListeners(true,false);
    }
  };

  private void hideP() {
    hr.restart.mainFrame.getMainFrame().mainTabs.remove(getContentPane());
    getContentPane().removeComponentListener(panelHandler);
    componentHiddenPanel();
    panelHandler.componentHidden(null); //za svaki slucaj
  }

  private void componentShownPanel() {
    if (this instanceof hr.restart.util.raMatPodaci) return; // ovo je vec shandlano u raMatPodaci, ali trebalo bi sve handlati tu
    java.util.EventListener[] componentListeners = Jframe.getListeners(java.awt.event.ComponentListener.class);
    for (int i=0;i<componentListeners.length;i++) {
      java.awt.event.ComponentListener compListener = (java.awt.event.ComponentListener)componentListeners[i];
      compListener.componentShown(null);
    }
  }

  private void componentHiddenPanel() {
    if (this instanceof hr.restart.util.raMatPodaci) return; // ovo je vec shandlano u raMatPodaci, ali trebalo bi sve handlati tu
    java.util.EventListener[] componentListeners = Jframe.getListeners(java.awt.event.ComponentListener.class);
    for (int i=0;i<componentListeners.length;i++) {
      java.awt.event.ComponentListener compListener = (java.awt.event.ComponentListener)componentListeners[i];
      compListener.componentHidden(null);
    }
  }

  private void collectListeners() {
    windowListeners = Jframe.getListeners(java.awt.event.WindowListener.class);
    keyListeners = Jframe.getListeners(java.awt.event.KeyListener.class);
  }
  
  public java.util.EventListener[] getListeners(Class listenerType) {
    return getWindow().getListeners(listenerType);
  }
  
  private void handleKeyListeners(boolean toRemove,boolean toAdd) {
    if (this instanceof hr.restart.util.raMatPodaci) return; // ovo je vec shandlano u raMatPodaci, ali trebalo bi sve handlati tu
    if (keyListeners == null) collectListeners();
    hr.restart.mainFrame mainFrm = hr.restart.mainFrame.getMainFrame();
    for (int i=0;i<keyListeners.length;i++) {
      java.awt.event.KeyListener kyListener = (java.awt.event.KeyListener)keyListeners[i];
      if (toRemove) {
        mainFrm.removeKeyListener(kyListener);
        getContentPane().removeKeyListener(kyListener);
      }
      if (toAdd) {
        mainFrm.addKeyListener(kyListener);
        getContentPane().addKeyListener(kyListener);
      }
    }
  }

  public java.awt.Container getContentPane() {
    if (frameMode==INTERNALFRAME) {
      return Iframe.getContentPane();
    } else
    if (frameMode==FRAME||frameMode==PANEL) {
      return Jframe.getContentPane();
    } else
    if (frameMode==DIALOG) {
      return Jdialog.getContentPane();
    }
    return new java.awt.Container();
  }

  public void updateUI() {
    if (frameMode==INTERNALFRAME) {
      javax.swing.SwingUtilities.updateComponentTreeUI(Iframe);
    } else
    if (frameMode==FRAME||frameMode==PANEL) {
      javax.swing.SwingUtilities.updateComponentTreeUI(Jframe);
    } else
    if (frameMode==DIALOG) {
      javax.swing.SwingUtilities.updateComponentTreeUI(Jdialog);
    }
  }
// mozda dodati u interface
  public void setEnabled(boolean enabled) {
    if (frameMode==INTERNALFRAME) {
      Iframe.setEnabled(enabled);
    } else
    if (frameMode==FRAME) {
      Jframe.setEnabled(enabled);
    } else
    if (frameMode==DIALOG) {
      Jdialog.setEnabled(enabled);
    }
  }

  public boolean isEnabled() {
    if (frameMode==INTERNALFRAME) {
      return Iframe.isEnabled();
    } else
    if (frameMode==FRAME) {
      return Jframe.isEnabled();
    } else
    if (frameMode==DIALOG) {
      return Jdialog.isEnabled();
    }
    return false;
  }
  public boolean isShowing() {
    if (frameMode==INTERNALFRAME) {
      return Iframe.isShowing();
    } else
    if (frameMode==FRAME) {
      return Jframe.isShowing();
    } else
    if (frameMode==DIALOG) {
      return Jdialog.isShowing();
    } else
    if (frameMode==PANEL) {
      return (hr.restart.mainFrame.getMainFrame().mainTabs.indexOfComponent(getContentPane())>-1);
    }
    return false;
  }
  public java.awt.Dimension getPreferredSize() {
    if (frameMode==INTERNALFRAME) {
      return Iframe.getPreferredSize();
    } else
    if (frameMode==FRAME) {
      return Jframe.getPreferredSize();
    } else
    if (frameMode==DIALOG) {
      return Jdialog.getPreferredSize();
    }
    return null;
  }
  public void setIconImage(java.awt.Image im) {
    if (frameMode==INTERNALFRAME) {
      Iframe.setFrameIcon(new javax.swing.ImageIcon(im.getScaledInstance(20,-1,java.awt.Image.SCALE_FAST)));
    } else
    if (frameMode==FRAME) {
      Jframe.setIconImage(im);
    } else
    if (frameMode==DIALOG) {
      // od ownera
    }
  }
  public void requestFocus() {
    if (frameMode==INTERNALFRAME) {
      Iframe.requestFocus();
    } else
    if (frameMode==FRAME) {
      Jframe.requestFocus();
    } else
    if (frameMode==DIALOG) {
      Jdialog.requestFocus();
    }
  }
  public java.awt.Toolkit getToolkit() {
    if (frameMode==INTERNALFRAME) {
      return Iframe.getToolkit();
    } else
    if (frameMode==FRAME) {
      return Jframe.getToolkit();
    } else
    if (frameMode==DIALOG) {
      return Jdialog.getToolkit();
    }
    return null;
  }
  public void setJMenuBar(javax.swing.JMenuBar jmenubar) {
    if (frameMode==INTERNALFRAME) {
      Iframe.setJMenuBar(jmenubar);
    } else
    if (frameMode==FRAME) {
      Jframe.setJMenuBar(jmenubar);
    } else
    if (frameMode==DIALOG) {
      Jdialog.setJMenuBar(jmenubar);
    }
  }

  public javax.swing.JMenuBar getJMenuBar() {
    if (frameMode==INTERNALFRAME) {
      return Iframe.getJMenuBar();
    } else
    if (frameMode==FRAME) {
      return Jframe.getJMenuBar();
    } else
    if (frameMode==DIALOG) {
      return Jdialog.getJMenuBar();
    }
    return new javax.swing.JMenuBar();
  }

  public void addWindowListener(java.awt.event.WindowAdapter wlistener) {
    if (frameMode==INTERNALFRAME) {
      Iframe.addInternalFrameListener(translateAdapter(wlistener));
    } else
    if (frameMode==FRAME||frameMode==PANEL) {
      Jframe.addWindowListener(wlistener);
    } else
    if (frameMode==DIALOG) {
      Jdialog.addWindowListener(wlistener);
    }
  }

  public void removeWindowListener(java.awt.event.WindowAdapter wlistener) {
    if (frameMode==INTERNALFRAME) {
      Iframe.removeInternalFrameListener(translateAdapter(wlistener));
    } else
    if (frameMode==FRAME||frameMode==PANEL) {
      Jframe.removeWindowListener(wlistener);
    } else
    if (frameMode==DIALOG) {
      Jdialog.removeWindowListener(wlistener);
    }
  }

  public void addKeyListener(java.awt.event.KeyListener klistener) {
    if (frameMode==INTERNALFRAME) {
      Iframe.addKeyListener(klistener);
    } else
    if (frameMode==FRAME||frameMode==PANEL) {
      Jframe.addKeyListener(klistener);
    } else
    if (frameMode==DIALOG) {
      Jdialog.addKeyListener(klistener);
    }
  }

  public void removeKeyListener(java.awt.event.KeyListener klistener) {
    if (frameMode==INTERNALFRAME) {
      Iframe.removeKeyListener(klistener);
    } else
    if (frameMode==FRAME||frameMode==PANEL) {
      Jframe.removeKeyListener(klistener);
    } else
    if (frameMode==DIALOG) {
      Jdialog.removeKeyListener(klistener);
    }
  }

  public void addComponentListener(java.awt.event.ComponentAdapter clistener) {
    if (frameMode==INTERNALFRAME) {
      Iframe.addComponentListener(clistener);
    } else
    if (frameMode==FRAME||frameMode==PANEL) {
      Jframe.addComponentListener(clistener);
    } else
    if (frameMode==DIALOG) {
      Jdialog.addComponentListener(clistener);
    }
  }
  public void removeComponentListener(java.awt.event.ComponentAdapter clistener) {
    if (frameMode==INTERNALFRAME) {
      Iframe.removeComponentListener(clistener);
    } else
    if (frameMode==FRAME||frameMode==PANEL) {
      Jframe.removeComponentListener(clistener);
    } else
    if (frameMode==DIALOG) {
      Jdialog.removeComponentListener(clistener);
    }
  }

  public void setDefaultCloseOperation(int operation) {
    if (frameMode==INTERNALFRAME) {
      Iframe.setDefaultCloseOperation(operation);
    } else
    if (frameMode==FRAME) {
      Jframe.setDefaultCloseOperation(operation);
    } else
    if (frameMode==DIALOG) {
      Jdialog.setDefaultCloseOperation(operation);
    }
  }

  /**
   *   InternalFrameAdapter.internalFrameActivated(InternalFrameEvent);   ->   WindowAdapter.windowActivated(null);
   *   InternalFrameAdapter.internalFrameClosed(InternalFrameEvent);      ->   WindowAdapter.windowClosed(null);
   *   InternalFrameAdapter.internalFrameClosing(InternalFrameEvent);     ->   WindowAdapter.windowClosing(null);
   *   InternalFrameAdapter.internalFrameDeactivated(InternalFrameEvent); ->   WindowAdapter.windowDeactivated(null);
   *   InternalFrameAdapter.internalFrameDeiconified(InternalFrameEvent); ->   WindowAdapter.windowDeiconified(null);
   *   InternalFrameAdapter.internalFrameIconified(InternalFrameEvent);   ->   WindowAdapter.windowIconified(null);
   *   InternalFrameAdapter.internalFrameOpened(InternalFrameEvent);      ->   WindowAdapter.windowOpened(null);
   */
  private javax.swing.event.InternalFrameAdapter translateAdapter(final java.awt.event.WindowAdapter wAdapter) {
    return new javax.swing.event.InternalFrameAdapter() {
      public void internalFrameActivated(InternalFrameEvent e) {
        wAdapter.windowActivated(null);
      }
      public void internalFrameClosed(InternalFrameEvent e) {
        wAdapter.windowClosed(null);
      }
      public void internalFrameClosing(InternalFrameEvent e) {
        wAdapter.windowClosing(null);
      }
      public void internalFrameDeactivated(InternalFrameEvent e) {
        wAdapter.windowDeactivated(null);
      }
      public void internalFrameDeiconified(InternalFrameEvent e) {
        wAdapter.windowDeiconified(null);
      }
      public void internalFrameIconified(InternalFrameEvent e) {
        wAdapter.windowIconified(null);
      }
      public void internalFrameOpened(InternalFrameEvent e) {
        wAdapter.windowOpened(null);
      }
    };
  }
  public java.awt.Component getSource() {
    if (frameMode==INTERNALFRAME) {
      return Iframe;
    } else
    if (frameMode==FRAME) {
      return Jframe;
    } else
    if (frameMode==DIALOG) {
      return Jdialog;
    } else
    if (frameMode==PANEL) {
      return hr.restart.mainFrame.getMainFrame();
    }
    return null;
  }
}



