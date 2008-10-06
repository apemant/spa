/****license*****************************************************************
**   file: raLiteFrameRobnoReplicator.java
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
import hr.restart.baza.dM;
import hr.restart.db.raReplicate;
import hr.restart.db.raReplicatePrepare;
import hr.restart.db.raReplicateRobno;
import hr.restart.db.raRobnoTransferMaintance;
import hr.restart.db.raServerSideRobnoTransfer;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.util.OKpanel;
import hr.restart.util.Valid;
import hr.restart.util.raCommonClass;
import hr.restart.util.raFrame;
import hr.restart.util.raProcess;
import hr.restart.util.raTransaction;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class raLiteFrameRobnoReplicator extends raFrame {

  private hr.restart.db.raRobnoTransferMaintance rRTM = new raRobnoTransferMaintance();
  private raServerSideRobnoTransfer rSSRT = new raServerSideRobnoTransfer();
  private String param_replcmddialUp;
  private boolean is2PartTransfer = "D".equalsIgnoreCase(hr.restart.sisfun.frmParam.getParam("robno","2PartTransfer","D","Ako je transfer podijeljen na clijent i server aplikaciju"));;
  private String param_replcmddialDis;
  private String param_replServer;
  private RemoteConnection RC = new RemoteConnection();
  private raReplicatePrepare rRP = new raReplicatePrepare();
  private raCommonClass rcc =raCommonClass.getraCommonClass();
  private StorageDataSet sss = new StorageDataSet();

  private void initSSS(){
    Column col= dM.getDataModule().getDoki().getColumn("DATDOK").cloneColumn();
    col.setColumnName("DATUM");
    sss.addColumn(col);
    sss.open();
  }

  private JPanel jpmain= new JPanel();

  private OKpanel okp = new OKpanel(){
    public void jPrekid_actionPerformed(){
      prekid();
    }
    public void jBOK_actionPerformed(){
      ok();
    }
  };
  private JLabel lCmd = new JLabel("Naèin spajanja ");
  private JraTextField jtfCmd = new JraTextField();
  private JLabel lDcmd = new JLabel("Izvoðenje replikacije na ");
  private JraTextField jtfDcmd = new JraTextField();
  private JLabel lDatum = new JLabel("Prijenos do  ");
  private JraTextField jtfDatum = new JraTextField();

  private XYLayout xylayout = new XYLayout();
  private JraCheckBox jcbms2c = new JraCheckBox("Transfer mat. podataka sa servera na client");
  private JraCheckBox jcbmc2s = new JraCheckBox("Transfer mat. podataka sa clienta na server");
  private JraCheckBox jcbprom = new JraCheckBox("Transfer prometa");

  public raLiteFrameRobnoReplicator() {
    initSSS();
    jbInit();
  }

  public void setupParametri() {
//    is2PartTransfer = "D".equalsIgnoreCase(hr.restart.sisfun.frmParam.getParam("robno","2PartTransfer","D","Ako je transfer podijeljen na clijent i server aplikaciju"));
    param_replcmddialUp = hr.restart.sisfun.frmParam.getParam("robno","replcmddialUp","LAN","Dial up komanda za konekciju (za prenos mrezom upisati LAN)");
    param_replcmddialDis = hr.restart.sisfun.frmParam.getParam("robno","replcmddialDis","","Naredba OS-u za prekid konekcije ako je odabran dialup != 'LAN'");
    hr.restart.db.raRobnoReplicator.bFastTransfer=
        hr.restart.sisfun.frmParam.getParam("robno","replFast","D","Ako je D radi se brzi transfer ali jako optereæuje memoriju").equalsIgnoreCase("D");
    param_replServer = hr.restart.sisfun.frmParam.getParam("robno","replServer","N","Izvodjenje na serveru D/N");
    jcbms2c.setSelected(false);
    jcbmc2s.setSelected(false);
    jcbprom.setSelected(true);
    jtfCmd.setText(param_replcmddialUp.equalsIgnoreCase("LAN")?"Mreža":"Dial-up "+param_replcmddialUp);
    jtfDcmd.setText(param_replServer.equalsIgnoreCase("D")?"Server":"Client");
  }

  private void jbInit() {

    okp.registerOKPanelKeys(this);
    this.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentShown(java.awt.event.ComponentEvent e) {
        setupParametri();
        if (is2PartTransfer) {
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              jtfDatum.requestFocus();
            }
          });
        }
      }
    });
    jtfDatum.setColumnName("DATUM");
    jtfDatum.setDataSet(sss);

    this.getContentPane().setLayout(new BorderLayout());
    jpmain.setLayout(xylayout);
    jpmain.setBorder(BorderFactory.createEtchedBorder());
    jpmain.add(lCmd,new XYConstraints(15,15,185,-1));
    jpmain.add(jtfCmd,new XYConstraints(200,15,255,-1));
    jpmain.add(lDcmd,new XYConstraints(15,40,185,-1));
    jpmain.add(jtfDcmd,new XYConstraints(200,40,255,-1));
    if (is2PartTransfer) {
      sss.setTimestamp("DATUM",hr.restart.util.Valid.getValid().getToday());
      jpmain.add(lDatum,new XYConstraints(15,65,185,-1));
      jpmain.add(jtfDatum,new XYConstraints(200,65,100,-1));
      xylayout.setWidth(470);
      xylayout.setHeight(90);
    } else {
      xylayout.setWidth(470);
      xylayout.setHeight(65);
    }
//    jpmain.add(jcbms2c,new XYConstraints(15,65,-1,-1));
//    jpmain.add(jcbmc2s,new XYConstraints(15,90,-1,-1));
//    jpmain.add(jcbprom,new XYConstraints(15,115,-1,-1));
    this.getContentPane().add(jpmain,BorderLayout.CENTER);
    getContentPane().add(okp,BorderLayout.SOUTH);
    rcc.setLabelLaF(jtfCmd,false);
    rcc.setLabelLaF(jtfDcmd,false);
  }

  void prekid() {
    this.hide();
  }

//  public void enable(boolean enable){
//    if (enable) {}else {}
//  }

  boolean bjcbms2c = false;
  boolean bjcbmc2s = false;
  boolean bjcbprom = false;
  boolean isDialUp = false;
  boolean isServer = false;

  public boolean remoteSetupUrl() {
    try {
      Comunicator4Replicator komunike = new Comunicator4Replicator();
      komunike.localURL = RC.getLocalURL();   // setiranje parametar
//System.out.println(RC.getLocalURL());
      komunike.serverURL= RC.getServerURL();
//System.out.println(RC.getServerURL());

Object obj = hr.restart.start.getClient().remoteInvoke(komunike,
          "invoke_setupURLs","hr.restart.robno.Invoker4Replicator",new Object[]{komunike});

      Comunicator4Replicator downKomunike =
          (Comunicator4Replicator) obj;
      return downKomunike.result.booleanValue();
//return true;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }

  }

  public boolean doDialUP() {
    if (!RC.makeDialupConnection(param_replcmddialUp)) {
      porukaError("Neuspjelo spajanje na server");
      RC.DialUpdisconnect(param_replcmddialDis);
      return false;
    };

    if (is2PartTransfer){
/*
      if (!RC.setupConnection(10)) {
        porukaError("Neuspjelo podizanje server strane");
        RC.DialUpdisconnect(param_replcmddialDis);
        return false;
      }
*/
      boolean forreturn = ProccesTwoPartTransfer();
      if (forreturn) {
        JOptionPane.showConfirmDialog(this,"Prijenos uspješno zavrsen !","Poruka",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
      } else {
        porukaError(poruka);
      }
      RC.DialUpdisconnect(param_replcmddialDis);
      return forreturn;
//      return TwoPartTransfer();
    }


    if (isServer) {
      if (!RC.setupConnection(10)) {
        porukaError("Neuspjelo podizanje server strane");
        RC.DialUpdisconnect(param_replcmddialDis);
        return false;
      }
      if (!remoteSetupUrl()) {
        porukaError("Neuspjelo podesavanje adresa baza za replikaciju");
        RC.DialUpdisconnect(param_replcmddialDis);
        return false;
      }
        procserverTransfer(bjcbms2c,bjcbmc2s,bjcbprom);
      if (serverTransfer(bjcbms2c,bjcbmc2s,bjcbprom)) {
        RC.DialUpdisconnect(param_replcmddialDis);
        JOptionPane.showConfirmDialog(this,"Prijenos uspješno zavrsen !","Poruka",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
      } else {
        RC.DialUpdisconnect(param_replcmddialDis);
        porukaError("Prijenos nije uspio !!!");
        return false;
      }
    } else {
//      raSetupUrls rSU = new raSetupUrls();
//      rSU.localURL = RC.getLocalURL();
//      rSU.serverURL = RC.getServerURL();
//      if (!rSU.setupURLs()) {
//        porukaError("Neuspjelo podesavanje adresa baza za replikaciju");
//        RC.DialUpdisconnect(param_replcmddialDis);
//        return false;
//      }
      if (clientTransfer(bjcbms2c,bjcbmc2s,bjcbprom)){

        RC.DialUpdisconnect(param_replcmddialDis);
        JOptionPane.showConfirmDialog(this,"Prijenos uspješno zavrsen !","Poruka",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
      }else {
        RC.DialUpdisconnect(param_replcmddialDis);
        porukaError("Prijenos nije uspio !!!");
        return false;
      }
    }
    return true;
  }
  public boolean doLanConnection(){
    if (is2PartTransfer){
//      return TwoPartTransfer();
      boolean forreturn = ProccesTwoPartTransfer();
      if (forreturn) {
        JOptionPane.showConfirmDialog(this,"Prijenos uspješno zavrsen !","Poruka",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
      } else {
        porukaError(poruka);
      }
      return forreturn;
    }
//clientTransfer(boolean bjcbms2c,boolean bjcbmc2s,boolean bjcbprom)
    if (isServer) {
      if (!remoteSetupUrl()) {
        porukaError("Neuspjelo podesavanje adresa baza za replikaciju");
        return false;
      }
      procserverTransfer(bjcbms2c,bjcbmc2s,bjcbprom);
      if (serverTransfer(bjcbms2c,bjcbmc2s,bjcbprom)) {
        JOptionPane.showConfirmDialog(this,"Prijenos uspješno zavrsen !","Poruka",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
      } else {
        porukaError("Prijenos nije uspio !!!");
        return false;
      }
    } else {
//      raSetupUrls rSU = new raSetupUrls();
//      rSU.localURL = RC.getLocalURL();
//      rSU.serverURL = RC.getServerURL();
//      if (!rSU.setupURLs()) {
//        porukaError("Neuspjelo podesavanje adresa baza za replikaciju");
//        return false;
//      }
      if (clientTransfer(bjcbms2c,bjcbmc2s,bjcbprom)){
        JOptionPane.showConfirmDialog(this,"Prijenos uspješno zavrsen !","Poruka",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
      }else {
        porukaError("Prijenos nije uspio !!!");
        return false;
      }
    }
    return true;
  }


  void ok() {

    isDialUp = !"LAN".equalsIgnoreCase(param_replcmddialUp);
    isServer = "D".equalsIgnoreCase(param_replServer);
    bjcbms2c = jcbms2c.isSelected();
    bjcbmc2s = jcbmc2s.isSelected();
    bjcbprom = jcbprom.isSelected();
    if (bjcbprom && ! is2PartTransfer) {
          prepare("robDocClientToServer");
    }
    if (isDialUp) {
      doDialUP();


    } else {
      doLanConnection();
    }
    this.hide();
  }

  public boolean clientTransfer(boolean bjcbms2c,boolean bjcbmc2s,boolean bjcbprom){
    if (bjcbms2c) {
      if (!raReplicateRobno.robLocalMatServerToClient()) {
        porukaError("Greška u LOCAL prijenosu (robMatServerToClient)");
        return false;
      }
    }
    if (bjcbmc2s) {
      if (!raReplicateRobno.robLocalMatClientToServer()) {
        porukaError("Greška u LOCAL prijenosu (robMatClientToServer)");
        return false;
      }
    }

    if (bjcbprom) {
      prepare("robDocClientToServer");
      if (!raReplicateRobno.robLocalDocClientToServer()) {
        porukaError("Greška u LOCAL prijenosu (robDocClientToServer)");
        return false;
      }
    }
    return true;
  }

  boolean bserverRet =true;
  public void procserverTransfer(final boolean bjcbms2c,final boolean bjcbmc2s,final boolean bjcbprom) {
    raProcess.runChild(new Runnable() {
      public void run() {
        bserverRet = serverTransfer(bjcbms2c,bjcbmc2s,bjcbprom);
      }
    });
  }


  public boolean serverTransfer(boolean bjcbms2c,boolean bjcbmc2s,boolean bjcbprom) {
    if (bjcbmc2s) {
      if (!remoteStart("invoke_robMatClientToServer")) {
        porukaError("Greška u REMOTE prijenosu (robMatClientToServer)");
        return false;
      }
    }
    if (bjcbms2c) {
      if (!remoteStart("invoke_robMatServerToClient")) {
        porukaError("Greška u REMOTE prijenosu (robMatServerToClient)");
        return false;
      }
    }
    if (bjcbprom) {
      if (!remoteStart("invoke_robDocClientToServer")) {
        porukaError("Greška u REMOTE prijenosu (robDocClientToServer)");
        return false;
      }
    }
    return true;
  }

  public boolean remoteStart(final String metoda) {
    if (metoda== null || metoda.equalsIgnoreCase("")) return false;

    Comunicator4Replicator komunike = new Comunicator4Replicator();
    Comunicator4Replicator downKomunike = (Comunicator4Replicator) hr.restart.start.getClient().remoteInvoke(komunike,
        metoda,"hr.restart.robno.Invoker4Replicator",new Object[]{komunike});
    return downKomunike.result.booleanValue();
  }


  public void porukaError(String poruka) {
      JOptionPane.showConfirmDialog(this,poruka,"Gre\u0161ka",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
  }

  public void prepare(final String batch) {
    raProcess.runChild("Server replikacija","Replikacija u tijeku ..",new Runnable(){
      public void run(){
        rRP.prepareReplInf(batch);
      }
    });
  }
  private boolean pero = false;
  private String poruka ="";
  public boolean ProccesTwoPartTransfer(){
    raProcess.runChild(this,new Runnable(){
      public void run(){
        pero = TwoPartTransfer();
      }
     });
   return pero;
  }

  public void brbljaj(String text,boolean nesto){
    raProcess.setMessage(text, nesto);
  }

  public boolean TwoPartTransfer(){

    brbljaj("Prebacivanje potvrdne datoteke ...", false);
    String localfolder = hr.restart.sisfun.frmParam.getParam
                                  ("robno","replDirLocal","c:/restart/trans",
                              "Folder na klijentu za prihvat potvrde transfera");

    String dir = (new File(localfolder)).getAbsolutePath();

    String[] files = rRTM.getFileNamesForReturn();

    ArrayList al = new ArrayList();
    if (files != null) {
      for (int i = 0; i<files.length;i++) {
        if (rRTM.getFileFromServer(files[i],dir)) {
            al.add(files[i]);
        }
      }
    }
    /*
       sad treba raspakirati zipove
    */

    brbljaj("Dekompresija potvrdne datoteke  ...", false);

    if (al.size()>0) {
      String[] imefajli = Valid.ArrayList2StringArray(al);
      rSSRT.decompressZipFile(imefajli,dir);
      File dirA = new File(dir);
      brbljaj("Ažuriranje replinfo s kljucevima iz potvrdne datoteke  ...", false);
      for (int i = 0;i< imefajli.length;i++) {
        rRTM.azuriranjeReplInfoPovratnimFileom(dirA.getAbsolutePath()+
                      File.separator+imefajli[i].substring(0,imefajli[i].length()-4)+
                      File.separator+"goodList.dat","D");
        rRTM.azuriranjeReplInfoPovratnimFileom(dirA.getAbsolutePath()+
                      File.separator+imefajli[i].substring(0,imefajli[i].length()-4)+
                      File.separator+"badList.dat","N");
      }

      brbljaj("Ažuriranje TransHistory -a  ...", false);
      for (int i = 0;i<imefajli.length;i++) {
        try {
          String sqlup = "UPDATE TransHistory set status='D' , datpotvrde='"+
                   Valid.getValid().getToday()+"' where filenameserver='"+imefajli[i]+"'";
          raTransaction.runSQL(sqlup);
        }
        catch (Exception ex) {
          return false;
        }
      }
    }

    brbljaj("Priprema podataka za prijenos  ...", false);
    rRTM.setDatumdo(sss.getTimestamp("DATUM"));

    Thread tr = new Thread(new Runnable(){
      public void run(){
        pero = rRTM.prepareData("robDocClientToServer");
      }
    });
    tr.start();
    try {
      tr.join();
    }
    catch (InterruptedException ex) {
      ex.printStackTrace();
    }

    if (!pero) {
      poruka="Nema podataka za prijenos !!";
      return false;
    }
//    if (rRTM.prepareData("robDocClientToServer")) {
//      poruka="Nema podataka za prijenos !!";
//      return false;
//    }
    if (rRTM.getFileToSend()==null || rRTM.getFileToSend().equalsIgnoreCase("")) {
      poruka = "rRTM.getFileToSend()"+rRTM.getFileToSend();
      return false;
    }
    brbljaj("Prijenos podataka  ...", false);
    if (rRTM.fileToDestination()) {
      try {
        raTransaction.runSQL("INSERT INTO TransHistory (filenameclient,filenameserver,status,datprijenos) values ('"+
                             rRTM.getFileToSend()+"','"+"ret_"+(new File(rRTM.getFileToSend())).getName()+"','N','"+Valid.getValid().getToday()+"')");
        raTransaction.runSQL("UPDATE doki set status='P' where "+raReplicate.getKeyTab("doki")+"in ("+
                           "select keytab from replinfo where imetab='doki')");
        raTransaction.runSQL("UPDATE doku set status='P' where "+raReplicate.getKeyTab("doku")+"in ("+
                           "select keytab from replinfo where imetab='doku')");
        raTransaction.runSQL("UPDATE meskla set status='P' where "+raReplicate.getKeyTab("meskla")+"in ("+
                           "select keytab from replinfo where imetab='meskla')");
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }

//      raTransaction.runSQL("UPDATE doki set status='P' where "+raReplicate.getKeyTab("doki")+"in ("+
//                           "select keytab from replinfo where imetab='doki')");


      return true;
    } else {
      poruka="Neuspješan prijenos !!";
      return false;
    }
  }
  public String getPoruka() {
    return poruka;
  }
}

