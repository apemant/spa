/****license*****************************************************************
**   file: MailFrame.java
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
 * Created on May 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.util.mail.ui;

import hr.restart.util.OKpanel;
import hr.restart.util.VarStr;
import hr.restart.util.okFrame;
import hr.restart.util.raFrame;
import hr.restart.util.raLLFrames;
import hr.restart.util.startFrame;
import hr.restart.util.mail.Mailer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Properties;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * @author andrej
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MailFrame extends raFrame implements okFrame{
  private static MailFrame _this;
  private Mailer mailer;
  String recprefix = "Pošalji na ";
  String reckey = "mailrec";
  JPanel content = new JPanel();
  JPanel jpDesc = new JPanel();
  JComboBox jcbMailTo = new JComboBox();
  JTextArea jtDesc = new JTextArea();
  String description = null;
  DefaultComboBoxModel cbmodel;
  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      action_jBOK();
    }

    public void jPrekid_actionPerformed() {
      action_jPrekid();
    }
    
  };
  
  public static MailFrame getInstance() {
    if (_this == null) _this = new MailFrame();
    return _this;
  }

  protected MailFrame() {
    super(DIALOG, raLLFrames.getRaLLFrames().getMsgStartFrame());
    init();
  }
  /**
   * 
   */
  private void init() {
    getJdialog().setModal(true);
    setTitle("Izvješæe o grešci");
    content.setLayout(new BorderLayout());
    jpDesc.setLayout(new BorderLayout());
    jpDesc.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Kratak opis greške"));
    jtDesc.setBorder(BorderFactory.createLoweredBevelBorder());
    jtDesc.setPreferredSize(new Dimension(400,400));
    jpDesc.add(jtDesc,BorderLayout.CENTER);
    cbmodel = new DefaultComboBoxModel(getComboData());
    jcbMailTo.setModel(cbmodel);
    content.add(jcbMailTo,BorderLayout.NORTH);
    content.add(jpDesc, BorderLayout.CENTER);
    content.add(okp,BorderLayout.SOUTH);
    getContentPane().add(content,BorderLayout.CENTER);
  }
  /**
   * @return
   */
  private Vector getComboData() {
    Vector v = new Vector();
    Properties mp = Mailer.getMailProperties();
    v.add(recprefix+mp.getProperty(reckey));
    int i = 0;
    while (true) {
      i++;
      String d = mp.getProperty(reckey+i);
      if (d == null) {
        break;
      }
      v.add(recprefix+d);
    }
    return v;
  }
  public void fill(String rec, String msg) {
    jcbMailTo.setModel(new DefaultComboBoxModel(new String[] {recprefix+rec}));
    jtDesc.setText(msg);
  }
  /**
   * @param mailer 
   * @return
   */
  public static String getMessage(Mailer _mailer) {
    MailFrame mf = getInstance();
    mf.mailer = _mailer;
    startFrame sf = raLLFrames.getRaLLFrames().getMsgStartFrame();
    sf.centerFrame(mf,0,mf.getTitle());
    mf.jcbMailTo.setModel(mf.cbmodel);//bring back combo data
    mf.show();
    return mf.description;
  }
  /* (non-Javadoc)
   * @see hr.restart.util.okFrame#action_jPrekid()
   */
  public void action_jPrekid() {
    description = null;
    hide();
  }
  /* (non-Javadoc)
   * @see hr.restart.util.okFrame#action_jBOK()
   */
  public void action_jBOK() {
    description = jtDesc.getText();
    String rec = new VarStr(jcbMailTo.getSelectedItem().toString())
			.leftChop(recprefix.length()).toString();
    mailer.setRecipient(rec);
    hide();
  }
  /* (non-Javadoc)
   * @see hr.restart.util.okFrame#getOKpanel()
   */
  public OKpanel getOKpanel() {
    return okp;
  }
  /* (non-Javadoc)
   * @see hr.restart.util.okFrame#doSaving()
   */
  public boolean doSaving() {
    return false;
  }

  public Mailer getMailer() {
    return mailer;
  }

  public void setMailer(Mailer mailer) {
    this.mailer = mailer;
  }
}
