package hr.restart.swing;

import hr.restart.util.OKpanel;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;


public class raOptionDialog {
  protected JraDialog win;
  
  protected OKpanel okp = new OKpanel() {  
    public void jBOK_actionPerformed() {
      if (checkOk()) okPress();
    }
  
    public void jPrekid_actionPerformed() {
      cancelPress();
    }
  };
  
  private boolean accept;

  protected void okPress() {
    accept = true;
    cancelPress();
  }

  protected void cancelPress() {
    if (win != null) {
      win.dispose();
      win = null;
    }
  }

  public OKpanel getOkPanel() {
    return okp;
  }
  
  public boolean show(Container parent, JPanel content, String title) {
    Container realparent = null;

    if (parent instanceof JComponent)
      realparent = ((JComponent) parent).getTopLevelAncestor();
    else if (parent instanceof Window)
      realparent = parent;

    if (realparent instanceof Dialog)
      win = new JraDialog((Dialog) realparent, title, true);
    else if (realparent instanceof Frame)
      win = new JraDialog((Frame) realparent, title, true);
    else win = new JraDialog((Frame) null, title, true);
    
    win.setDefaultCloseOperation(win.DO_NOTHING_ON_CLOSE);
    win.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        cancelPress();
      }
    });
    okp.registerOKPanelKeys(win);
    win.setContentPane(content);
    win.pack();
    win.setLocationRelativeTo(parent);
    beforeShow();
    accept = false;
    win.show();
    return accept;
  }
  
  public void center() {
    if (win != null) win.setLocationRelativeTo(null);
  }
  
  protected void beforeShow() {
    
  }
  
  protected boolean checkOk() {
    return true;
  }
}
