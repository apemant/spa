/****license*****************************************************************
**   file: frmPassword.java
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
package hr.restart.sisfun;

import hr.restart.baza.dM;
import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.ActionExecutor;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.KeyAction;
import hr.restart.util.Aus;
import hr.restart.util.FileHandler;
import hr.restart.util.OKpanel;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raImages;
import hr.restart.util.startFrame;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

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

public class frmPassword extends JraDialog {
//sysoutTEST ST = new sysoutTEST(false);
  String PROPFILENAME = "login.properties";
  JPanel jp = new JPanel();
  XYLayout xYLay = new XYLayout();
  JLabel jlCUSER = new JLabel();
  
  ActionExecutor execF9 = new ActionExecutor() {
	  public void run() {
		  jbGetUsers_actionPerformed();
	  }
  };
  
  JTextField jtCUSER = new JTextField() {
    public void addNotify() {
      super.addNotify();
      AWTKeyboard.registerKeyStroke(this, AWTKeyboard.F9, new KeyAction() {
        public boolean actionPerformed() {
          execF9.invokeLater();
          return true;
        }
      });
      Aus.installEnterRelease(this);
    }
    
    public void removeNotify() {
      super.removeNotify();
      AWTKeyboard.unregisterComponent(this);
    }
  };
  JLabel jlNAZIV = new JLabel();
  JLabel jlZAPORKA = new JLabel();
  JPasswordField jpswd = new JPasswordField() {
    public void addNotify() {
      super.addNotify();      
      Aus.installEnterRelease(this);
    }
    
    public void removeNotify() {
      super.removeNotify();
      AWTKeyboard.unregisterComponent(this);
    }
  };
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  lookupData lD = lookupData.getlookupData();
  private java.util.Properties props;
  int loginTry = 0;
  protected boolean loginFailed;
  OKpanel okp = new OKpanel() {
    public void jPrekid_actionPerformed() {
      prekid_act();
    }
    public void jBOK_actionPerformed() {
      ok_act();
    }
  };
  JraButton jbGetUsers = new JraButton();
  JraButton jbChangePsw = new JraButton();
  dlgUserList dlgUsers;
  dlgChangePassword dlgPassw;

  JPanel jpLock = new JPanel();
  JPasswordField jpfPass = new JPasswordField(5);
  JLabel jlLock1 = new JLabel();
  JLabel jlLock2 = new JLabel();
  private String forcedUsername;

  protected frmPassword(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    try {
      dm.getUseri().open();
      dm.getKljucevi().open();
    }
    catch (Exception ex) {
    }
  }

  public frmPassword() {
    this(null, "", true);
  }
  public boolean askLogin() {
    return askLogin(null);
  }
  public boolean askLogin(String username) {

//    if (hr.restart.util.startFrame.SFMain)
//      hr.restart.util.reports.raElixirLoader.load();
    forcedUsername = username;
    jtCUSER.setEditable(username == null);
    jbGetUsers.setEnabled(username == null);
    startFrame.getStartFrame().centerFrame(this,0,"Autorizacija");
    show();
    return !isLoginFailed();
  }

  void jbInit() throws Exception {
    jp.setLayout(xYLay);
    jlCUSER.setText("Korisni\u010Dko ime");
    jlNAZIV.setHorizontalAlignment(SwingConstants.CENTER);
    jlZAPORKA.setText("Zaporka");
    jtCUSER.setEditable(true);
    /*jtCUSER.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        jtCUSER_keyPressed(e);
      }
    });*/
//    raTextMask msk = new raTextMask(jtCUSER, 10);
//    msk.setAllowSpaces(false);
//    raDateMask msk = new raDateMask(jtCUSER);
//    jtCUSER.setHorizontalAlignment(SwingConstants.RIGHT);
//    raFieldMask msk = new raFieldMask(jtCUSER);
//    msk.setMask("###.###.##0,00%");
//    msk.setRightJustify(true);
//    raNumberMask msk = new raNumberMask(jtCUSER);
//    System.out.println(Aus.getFontHeightRatio("Lucida Bright", "Tahoma"));
    jtCUSER.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        if (!e.isTemporary()) jtCUSER_check();
      }
    });
    /*jtCUSER.addKeyListener(new hr.restart.swing.JraKeyListener());
    jpswd.addKeyListener(new hr.restart.swing.JraKeyListener());*/
    xYLay.setWidth(366);
    xYLay.setHeight(111);

    getContentPane().setLayout(new BorderLayout());
    jbGetUsers.setText("...");
    jbChangePsw.setText("Promjena zaporke");
    jbChangePsw.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbChangePsw_actionPerformed();
      }
    });
    jbChangePsw.setIcon(raImages.getImageIcon(raImages.IMGALIGNCENTER));
    okp.add(jbChangePsw,0);
    getContentPane().add(jp, BorderLayout.CENTER);
    getContentPane().add(okp, BorderLayout.SOUTH);
    jp.add(jlCUSER, new XYConstraints(15, 20, -1, -1));
    jp.add(jtCUSER, new XYConstraints(150, 20, 175, -1));
    jp.add(jlNAZIV, new XYConstraints(15, 45, 335, -1));
    jp.add(jlZAPORKA, new XYConstraints(15, 70, -1, -1));
    jp.add(jpswd, new XYConstraints(150, 70, 175, -1));
    jp.add(jbGetUsers, new XYConstraints(330, 20, 21, 21));
    props = FileHandler.getProperties(PROPFILENAME);
    okp.registerOKPanelKeys(this);
    jbGetUsers.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbGetUsers_actionPerformed();
      }
    });
    dlgUsers = new dlgUserList(this);
    dlgPassw = new dlgChangePassword(this);
    startFrame.getStartFrame().centerFrame(dlgUsers,0,"Korisnici");
    startFrame.getStartFrame().centerFrame(dlgPassw,0,"Promjena zaporke");

    jlLock1.setText("Korisnik je ve\u0107 prijavljen!");
    jlLock1.setHorizontalAlignment(SwingConstants.CENTER);
    jlLock2.setText("Unesite root lozinku za otklju\u010Davanje:");
    jlLock2.setHorizontalAlignment(SwingConstants.CENTER);
    jpLock.setLayout(new GridLayout(0, 1));
    jpLock.add(jlLock1);
    jpLock.add(jlLock2);
    jpLock.add(jpfPass);
    //setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        loginFailed = true;
        loginTry = 0;
      }
    });
    addComponentListener(new ComponentAdapter() {
      public void componentShown(ComponentEvent e) {
        jtCUSER_init();
      }
    });
  }

  private void prekid_act() {
    loginFailed = true;
    loginTry = 0;
    hide();
  }

  private void ok_act() {
    if (!validacija()) return;
    String last = raUser.getInstance().getUser();
    if (!last.equals("") && last.equals(jtCUSER.getText())) {
      loginFailed = false;
      loginTry = 0;
      hide();
      return;
    }
    raUser.getInstance().setUser(jtCUSER.getText());
    if (!jtCUSER.getText().equals("root") && !jtCUSER.getText().equals("test") &&
        !raUser.getInstance().lockRow(dm.getUseri(), new String[] {"CUSER"})) {
      jpswd.setText("");
      jtCUSER.requestFocus();
      if (JOptionPane.showConfirmDialog(this, "Korisnik ve\u0107 prijavljen! Otklju\u010Dati?",
          "Upozorenje", JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
//      int resp = JOptionPane.showConfirmDialog(this, jpLock, "Upozorenje", JOptionPane.OK_CANCEL_OPTION,
//          JOptionPane.PLAIN_MESSAGE);
//      if (resp == JOptionPane.CANCEL_OPTION ||
//          !getRootPassword().equals(new String(jpfPass.getPassword()))) {
//        if (resp == JOptionPane.OK_OPTION)
//          JOptionPane.showMessageDialog(this, "Lozinka neispravna!", "Greška", JOptionPane.ERROR_MESSAGE);
        raUser.getInstance().setUser(last);
        jtCUSER.setText("");
        jlNAZIV.setText("");
        return;
      }
//      JOptionPane.showMessageDialog(this, "Korisnik "+jtCUSER.getText()+" je ve\u0107 prijavljen!", "Greška", JOptionPane.ERROR_MESSAGE);
    }
    if (!last.equals(""))
      raUser.getInstance().unlockUser(last);
    loginFailed = false;
    loginTry = 0;
//    raUser.getInstance().setUser(jtCUSER.getText(), dm.getUseri().getString("CGRUPEUSERA"));
    String usr = raUser.getInstance().getUser();
    if (frmSistem.getFrmSistem() != null && !usr.equals("root") &&
    		!usr.equals("test") && !usr.equals("restart"))
      frmSistem.getFrmSistem().removeAdmin();
    props.setProperty(jtCUSER.getText(),"*");
    FileHandler.storeProperties(PROPFILENAME,props);
    hide();
  }
  void jtCUSER_init() {
    if (forcedUsername != null) {
      setJtCUSER_text(forcedUsername);
    } else if (props.keySet().size() == 1) {
      String s = props.keySet().toArray()[0].toString();
      setJtCUSER_text(s);      
    } else {
      jtCUSER.setText("");
      jtCUSER.requestFocus();
    }
    jpswd.setText("");
    jlNAZIV.setText("");
  }
  private void setJtCUSER_text(String s) {
    jtCUSER.setText(s);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jtCUSER_check();
      }
    });
  }

  void jtCUSER_check() {
//ST.prn("checking login...");
    if (jtCUSER.getText().equals("root")) {
      jlNAZIV.setText("Administrator");
      jpswd.requestFocus();
      return;
    }
    dm.getUseri().open();
    boolean located = lD.raLocate(dm.getUseri(), "CUSER", jtCUSER.getText());
    if (located) {
      jlNAZIV.setText(dm.getUseri().getString("NAZIV"));
      jpswd.requestFocus();
    } else {
      jlNAZIV.setText("");
      jtCUSER.setText("");
    }
  }

  public static boolean checkUserPassword(String user, String passwd) {
    if (user.equals("")) return false;
    boolean located = lookupData.getlookupData().raLocate(dM.getDataModule().getUseri(), "CUSER", user);
    if (!located) return false;
    if (!dM.getDataModule().getUseri().getString("ZAPORKA").equals(passwd)) return false;
    raUser.getInstance().setUser(user);
//    System.out.println(hr.restart.util.startFrame.SFMain);
//    System.out.println(hr.restart.util.reports.raElixirLoader);
//    if (hr.restart.util.startFrame.SFMain)
//      hr.restart.util.reports.raElixirLoader.load();

    if (!user.equals("root") && !user.equals("test") &&
        !raUser.getInstance().lockRow(dM.getDataModule().getUseri(), new String[] {"CUSER"}))
      return false;
    return true;
  }

  public boolean isLoginFailed() {
    return loginFailed;
  }

  public static String getRootPassword() {
    String date = Valid.getValid().getToday().toString();
    StringBuffer ps = new StringBuffer(6);
    ps.append(date.substring(8,10));
    ps.append(date.substring(5,7));
    return ps.reverse().toString();
  }

  public boolean validacija() {
    jtCUSER_check();//zbog F10
    if (jtCUSER.getText().equals("")) {
      Valid.getValid().showValidErrMsg(null,'E');
      jtCUSER.requestFocus();
      return false;
    }
    String pswd;
    String pswdTyped = new String(jpswd.getPassword());
    if (jtCUSER.getText().equals("root")) {
      pswd = getRootPassword();
    } else
      pswd = dm.getUseri().getString("ZAPORKA");
    if (pswd.equals("")) {
      dlgPassw.show();
      return dlgPassw.success;
    }

    if (!pswd.equals(pswdTyped)) {
      if (loginTry > 2) {
        JOptionPane.showMessageDialog(this,"Prekora\u010Den broj pokušaja! Autorizacija neuspjela!","Poruka",JOptionPane.WARNING_MESSAGE);
        loginFailed = true;
        hide();
      } else {
        JOptionPane.showMessageDialog(this,"Neispravna zaporka!","Poruka",JOptionPane.WARNING_MESSAGE);
        loginTry++;
      }
      return false;
    }
    return true;
  }

//  protected String encrypt(char[] chars) {
//  }
  void jbGetUsers_actionPerformed() {
    dlgUsers.show();
    if (dlgUsers.getValue() != null) {
      jtCUSER.setText(dlgUsers.getValue());
      jtCUSER_check();
    }
  }
/*
  void jtCUSER_keyPressed(KeyEvent e) {
    if (e.getKeyCode() == e.VK_F9) {
      jbGetUsers_actionPerformed();
      e.consume();
    }
  }*/

  void jbChangePsw_actionPerformed() {
    if (validacija() && !jtCUSER.getText().equals("root")) dlgPassw.show();
  }

  class dlgUserList extends JraDialog {
    JList jUserList = new JList();
    JraScrollPane jScrolList = new JraScrollPane(jUserList);
    private String strValue = null;
    OKpanel listokp = new OKpanel() {
      public void jPrekid_actionPerformed() {
        list_prekid();
      }
      public void jBOK_actionPerformed() {
        list_ok();
      }
    };

    dlgUserList(JDialog own) {
      super(own,"Pregled",true);
      jinit();
    }
    void jinit() {
      getContentPane().setLayout(new BorderLayout());
      getContentPane().add(jScrolList,BorderLayout.CENTER);
      getContentPane().add(listokp,BorderLayout.SOUTH);
      jUserList_init();
      listokp.registerOKPanelKeys(this);
      listokp.setEnterEnabled(true);
      this.addComponentListener(new ComponentAdapter() {
        public void componentShown(ComponentEvent e) {
          jUserList.requestFocus();
        }
      });
      jUserList.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          if (e.getClickCount() == 2 && (e.getModifiers() & MouseEvent.BUTTON1_MASK) > 0)
            list_ok();
        }
      });
    }

    private void jUserList_init() {
      jUserList.setListData(props.keySet().toArray());
      jUserList.setSelectedIndex(0);
      jUserList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    void list_ok() {
      strValue = jUserList.getSelectedValue().toString();
      hide();
    }

    void list_prekid() {
      strValue = null;
      hide();
    }

    String getValue() {
      return strValue;
    }
  }

  class dlgChangePassword extends JraDialog {
    JPanel jpcp = new JPanel();
    XYLayout xYLay = new XYLayout();
    JLabel jlNewPass = new JLabel();
    JLabel jlNewPass2 = new JLabel();
    JPasswordField jtNewPass = new JPasswordField();
    JPasswordField jtNewPass2 = new JPasswordField();
    boolean success = false;
    private String strNewPass = null;
    OKpanel cpokp = new OKpanel() {
      public void jPrekid_actionPerformed() {
        cp_prekid();
      }
      public void jBOK_actionPerformed() {
        cp_ok();
      }
    };

    public dlgChangePassword(JDialog own) {
      super(own,"Promjena zaporke",true);
      try {
        jInit();
      }
      catch(Exception e) {
        e.printStackTrace();
      }
    }

    private void jInit() throws Exception {
      jlNewPass.setText("Nova zaporka");
      jpcp.setLayout(xYLay);
      jlNewPass2.setText("Još jednom nova zaporka");
      xYLay.setWidth(410);
      xYLay.setHeight(85);
      jpcp.add(jlNewPass, new XYConstraints(15, 20, -1, -1));
      jpcp.add(jlNewPass2, new XYConstraints(15, 45, -1, -1));
      jpcp.add(jtNewPass, new XYConstraints(200, 20, 200, -1));
      jpcp.add(jtNewPass2, new XYConstraints(200, 45, 200, -1));
      getContentPane().setLayout(new BorderLayout());
      getContentPane().add(jpcp,BorderLayout.CENTER);
      getContentPane().add(cpokp,BorderLayout.SOUTH);
      addComponentListener(new ComponentAdapter() {
        public void componentShown(ComponentEvent e) {
          jtNewPass.requestFocus();
        }
      });
    }

    void cp_ok() {
      String s1 = new String(jtNewPass.getPassword());
      String s2 = new String(jtNewPass2.getPassword());
      if (!s1.equals(s2)) {
        JOptionPane.showMessageDialog(this,"Unešene zaporke se razlikuju! Zaporka nije promijenjena!","Poruka",JOptionPane.WARNING_MESSAGE);
        jtNewPass.setText("");
        jtNewPass2.setText("");
        jtNewPass.requestFocus();
        return;
      }
      strNewPass = new String(jtNewPass.getPassword());
      dm.getUseri().setString("ZAPORKA",s1);
      dm.getUseri().post();
      dm.getUseri().saveChanges();
      success = true;
      hide();
    }

    void cp_prekid() {
      success = false;
      hide();
    }

    public void show() {
      jtNewPass.setText("");
      jtNewPass2.setText("");
      success = false;
      super.show();
    }
  }
}

