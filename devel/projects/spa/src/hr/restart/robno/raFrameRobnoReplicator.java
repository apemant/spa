/****license*****************************************************************
**   file: raFrameRobnoReplicator.java
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
package hr.restart.robno;

import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.OKpanel;
import hr.restart.util.raFrame;

import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class raFrameRobnoReplicator extends raFrame {

  private JPanel jpmain= new JPanel();
  private JPanel jpnorth= new JPanel();
  private JPanel jpsouth= new JPanel();
  private JPanel jpcentar= new JPanel();
  private RemoteStart rms;

  private OKpanel okp = new OKpanel(){
    public void jPrekid_actionPerformed(){
      prekid();
    }
    public void jBOK_actionPerformed(){
      ok();
    }
  };
  private JLabel lCmd = new JLabel("Dial-in program za konekciju");
  private JraTextField jtfCmd = new JraTextField();
  private JLabel lDcmd = new JLabel("Komanda za diskonekciju");
  private JraTextField jtfDcmd = new JraTextField();
//  private JLabel lPassword = new JLabel("Lozinka");
//  private JraTextField jtfPassword = new JraTextField();
//  private JLabel lPhonenumber = new JLabel("Broj telefona");
//  private JraTextField jtfPhonenumber = new JraTextField();
//  private JLabel lConnectioncommand = new JLabel("Vanjski program za kon.");
//  private JraTextField jtfConnectioncommand = new JraTextField();
  private JLabel lURLserver = new JLabel("URL server");
  private JraTextField jtfURLserver = new JraTextField();
  private JLabel lURLclient = new JLabel("URL client");
  private JraTextField jtfURLclient = new JraTextField();
  private ButtonGroup rgNacCon = new ButtonGroup();
  private JRadioButton rbDialUp = new JRadioButton("Dial-Up",false);
  private JRadioButton rbNetwork = new JRadioButton("Mreža",true);
  private ButtonGroup rgNacIzvod = new ButtonGroup();
  private JRadioButton rbServer = new JRadioButton("Server",false);
  private JRadioButton rbClient = new JRadioButton("Lokalno",true);
  private XYLayout xylayout = new XYLayout();
  private XYLayout xylayoutnort = new XYLayout();
  private XYLayout xylayoutsouth = new XYLayout();
  private StorageDataSet slogoreplikaciji ;
  private JraCheckBox jcbms2c = new JraCheckBox("Transfer mat. podataka sa servera na client");
  private JraCheckBox jcbmc2s = new JraCheckBox("Transfer mat. podataka sa clienta na server");
  private JraCheckBox jcbprom = new JraCheckBox("Transfer prometa");

  private void initSDS(){

    hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
    slogoreplikaciji = new StorageDataSet();
    Column cmd = dm.getDoki().getColumn("OPIS").cloneColumn();
//    cmd.setWidth(300);
    cmd.setColumnName("CMD");
    cmd.setCaption("Program za konekciju");
    Column dcmd = dm.getDoki().getColumn("OPIS").cloneColumn();
    dcmd.setColumnName("DCMD");
    dcmd.setCaption("Naredba za diskonekciju");
    Column URLserver = dm.getDoki().getColumn("OPIS").cloneColumn();
    URLserver.setColumnName("URLSERVER");
    Column URLclient = dm.getDoki().getColumn("OPIS").cloneColumn();
    URLclient.setColumnName("URLCLIENT");
    slogoreplikaciji.setColumns(new Column[]{cmd,dcmd,URLserver,URLclient});
    slogoreplikaciji.open();

  }

  public raFrameRobnoReplicator() {
    initSDS();
    jbInit();
  }

  private void jbInit() {

    rgNacCon.add(rbDialUp);
    rgNacCon.add(rbNetwork);
    rgNacIzvod.add(rbServer);
    rgNacIzvod.add(rbClient);
    jtfCmd.setColumnName("CMD");
    jtfCmd.setDataSet(slogoreplikaciji);
    jtfDcmd.setColumnName("DCMD");
    jtfDcmd.setDataSet(slogoreplikaciji);
    jtfURLserver.setColumnName("URLSERVER");
    jtfURLserver.setDataSet(slogoreplikaciji);
    jtfURLclient.setColumnName("URLCLIENT");
    jtfURLclient.setDataSet(slogoreplikaciji);

    xylayout.setWidth(470);
    xylayout.setHeight(195);
    this.getContentPane().setLayout(new BorderLayout());

//    jcbms2c.setHorizontalTextPosition(SwingUtilities.LEFT);


    jpmain.setLayout(xylayout);
    jpnorth.setLayout(xylayoutnort);
    jpnorth.setBorder(BorderFactory.createEtchedBorder());
    jpnorth.add(rbDialUp,new XYConstraints(15,5,135,-1));
    jpnorth.add(rbNetwork,new XYConstraints(370,5,70,-1));
    jpsouth.setLayout(xylayoutsouth);
    jpsouth.setBorder(BorderFactory.createEtchedBorder());
    jpsouth.add(rbServer,new XYConstraints(15,5,135,-1));
    jpsouth.add(rbClient,new XYConstraints(370,5,70,-1));
    jpmain.add(lCmd,new XYConstraints(15,15,185,-1));
    jpmain.add(jtfCmd,new XYConstraints(200,15,255,-1));
    jpmain.add(lDcmd,new XYConstraints(15,40,185,-1));
    jpmain.add(jtfDcmd,new XYConstraints(200,40,255,-1));
    jpmain.add(lURLserver,new XYConstraints(15,65,185,-1));
    jpmain.add(jtfURLserver,new XYConstraints(200,65,255,-1));
    jpmain.add(lURLclient,new XYConstraints(15,90,185,-1));
    jpmain.add(jtfURLclient,new XYConstraints(200,90,255,-1));
    jpmain.add(jcbms2c,new XYConstraints(15,115,-1,-1));
    jpmain.add(jcbmc2s,new XYConstraints(15,140,-1,-1));
    jpmain.add(jcbprom,new XYConstraints(15,165,-1,-1));

    jpcentar.setLayout(new BorderLayout());
    jpcentar.add(jpmain,BorderLayout.CENTER);
    jpcentar.add(jpnorth,BorderLayout.NORTH);
    jpcentar.add(jpsouth,BorderLayout.SOUTH);
    this.getContentPane().add(jpcentar,BorderLayout.CENTER);
    getContentPane().add(okp,BorderLayout.SOUTH);

  }
  void prekid() {
    if (rms.isBexit())  System.exit(0);
    this.hide();
  }

//  public void enable(boolean enable){
//    if (enable) {}else {}
//  }

  void ok() {
    if (rms !=null) {
      rms.gethmParametri().put("SERVER",rbServer.isSelected()?"D":"N");
      rms.gethmParametri().put("DIALUP",rbDialUp.isSelected()?"D":"N");
      rms.gethmParametri().put("CMD",slogoreplikaciji.getString("CMD"));
      rms.gethmParametri().put("DCMD",slogoreplikaciji.getString("DCMD"));
      rms.gethmParametri().put("MS2C",jcbms2c.isSelected()?"D":"N");
      rms.gethmParametri().put("MC2S",jcbmc2s.isSelected()?"D":"N");
      rms.gethmParametri().put("PRO",jcbprom.isSelected()?"D":"N");
      rms.go();
    }
  }

  public void setupslogoreplikaciji(HashMap hm) {
//    hm.get("GUI");
    slogoreplikaciji.setString("CMD",(String)hm.get("CMD"));
    slogoreplikaciji.setString("DCMD",(String)hm.get("DCMD"));
    rbDialUp.setSelected(((String)hm.get("DIALUP")).equalsIgnoreCase("D"));
    rbServer.setSelected(((String)hm.get("SERVER")).equalsIgnoreCase("D"));
    jcbms2c.setSelected(((String)hm.get("MS2C")).equalsIgnoreCase("D"));
    jcbmc2s.setSelected(((String)hm.get("MC2S")).equalsIgnoreCase("D"));
    jcbprom.setSelected(((String)hm.get("PRO")).equalsIgnoreCase("D"));
  }

  public void setupRemoteStart (RemoteStart rms) {
    this.rms = rms;
  }

  public void setURLs(String urlserver,String urlclient) {

    slogoreplikaciji.setString("URLSERVER",urlserver);
    slogoreplikaciji.setString("URLCLIENT",urlclient);

  }

  public void porukaError(String poruka) {
      JOptionPane.showConfirmDialog(this,poruka,"Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
  }

}