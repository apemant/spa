/****license*****************************************************************
**   file: ServerDialog.java
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

import hr.restart.swing.JraDialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class ServerDialog extends JraDialog implements okFrame, loadFrame {
  OKpanel okp = new OKpanel() {
    public void jPrekid_actionPerformed() {
      action_jPrekid();
    }
    public void jBOK_actionPerformed() {
      action_jBOK();
    }
  };
  private JPanel jp = new JPanel();
  private XYLayout xYLay = new XYLayout();
  private JLabel jlHost = new JLabel();
  private JTextField jtHost = new JTextField();
  private JLabel jlPort = new JLabel();
  private JTextField jtPort = new JTextField();
  private JLabel jlStatus = new JLabel();
  private JLabel jlStat = new JLabel();
  private JCheckBox jcbConnect = new JCheckBox("Spoji se na server");
  private String port;
  private String host;
  private boolean connectServer;
  private JButton jbCon = new JButton();
  public ServerDialog() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }

  }
  public void action_jPrekid() {

  }
  public void action_jBOK() {
    IntParam.setTag("host",jtHost.getText());
    IntParam.setTag("port",jtPort.getText());
    IntParam.setTag("connectServer", new Boolean(jcbConnect.isSelected()).toString());
  }
  public OKpanel getOKpanel() {
    return okp;
  }
  public boolean doSaving() {
    return (!(port.equals(jtPort.getText()) && host.equals(jtHost.getText()) && connectServer == jcbConnect.isSelected()));
  }
  public void reload() {
    host = IntParam.getTag("host");
    port = IntParam.getTag("port");
    connectServer = Boolean.valueOf(IntParam.getTag("connectServer")).booleanValue();
    jtHost.setText(host);
    jtPort.setText(port);
    jcbConnect.setSelected(connectServer);
    jcbConnect_changed(new ItemEvent(jcbConnect, ItemEvent.ITEM_STATE_CHANGED,jcbConnect,connectServer?ItemEvent.SELECTED:ItemEvent.DESELECTED));
    if (hr.restart.start.isClientConnected()) {
      jlStat.setText("Aktivan");
      jlStat.setForeground(Color.green.darker().darker().darker());
      jtHost.setText(hr.restart.start.getClient().getHost());
      jtPort.setText(hr.restart.start.getClient().getPort()+"");
      jbCon.setText("Stop CL");
    } else {
      jlStat.setText("Neaktivan");
      jlStat.setForeground(Color.red);
      jbCon.setText("Start CL");
    }
  }
  private void jbInit() throws Exception {
    setTitle("Postavke servera");
    jp.setLayout(xYLay);
    jtPort.addKeyListener(new hr.restart.swing.JraKeyListener());
    jtHost.addKeyListener(new hr.restart.swing.JraKeyListener());
    jlHost.setText("Server (host)");
    jlPort.setText("Port");
    jlStatus.setText("Status");
    jlStat.setFont(jlStat.getFont().deriveFont(Font.ITALIC|Font.BOLD,14));
    jlStat.setText("Aktivan");
    raMatPodaci.addCentered(jp,getContentPane());
    jbCon.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbCon_actionPerformed();
      }
    });
    jcbConnect.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent ev) {
        jcbConnect_changed(ev);
      }
    });
    xYLay.setWidth(365);
    xYLay.setHeight(140);
    jp.add(jcbConnect, new XYConstraints(15, 20,-1,-1));
    jp.add(jlHost,   new XYConstraints(15, 45, -1, -1));
    jp.add(jtHost,  new XYConstraints(150, 45, 200, -1));
    jp.add(jlPort,  new XYConstraints(15, 70, -1, -1));
    jp.add(jtPort,  new XYConstraints(150, 70, 100, -1));
    jp.add(jlStatus,    new XYConstraints(15, 105, -1, -1));
    jp.add(jlStat,    new XYConstraints(150, 102, -1, -1));
    jp.add(jbCon,        new XYConstraints(250, 102, 100, 21));
  }
  void jbCon_actionPerformed() {
    if (hr.restart.start.isClientConnected()) {
      hr.restart.start.stopClient();
    } else {
      hr.restart.start.startClient();
    }
    reload();
  }
  
  void jcbConnect_changed(ItemEvent ev) {
    boolean sel = ev.getStateChange() == ev.SELECTED;
    jtHost.setEnabled(sel);
    jtPort.setEnabled(sel);
    jbCon.setEnabled(sel);
  }
}
