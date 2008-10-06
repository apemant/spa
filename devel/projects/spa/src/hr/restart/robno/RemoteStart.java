/****license*****************************************************************
**   file: RemoteStart.java
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
import hr.restart.db.raReplicatePrepare;
import hr.restart.db.raReplicateRobno;
import hr.restart.util.raProcess;

import java.util.HashMap;

//username=restart password=restart  entryname="Jazavcar over modem " os="win2000"
public class RemoteStart {

//  private RemoteConnection rRC;
  private HashMap hmParametri = new HashMap();
  private String localURL;
  private String serverURL;
  private String newAddressInParam;
  private int timeConnection=10;
  private raFrameRobnoReplicator rFRR;
  private String remoteporuka;
  private boolean bexit = true;
  private raReplicatePrepare rRP = new raReplicatePrepare();
  private String workProgress = new String();

  public RemoteStart(){

    hmParametri.put("GUI","D");
    hmParametri.put("SERVER","N");
    hmParametri.put("DIALUP","N");
    hmParametri.put("CMD","rasdial entryname [username [password|*]] [/DOMAIN:domain] [/PHONE:phonenumber] [/CALLBACK:callbacknumber] [/PHONEBOOK:phonebookfile] [/PREFIXSUFFIX]");
    hmParametri.put("DCMD","rasdial [entryname] /DISCONNECT");
    hmParametri.put("MS2C","N");
    hmParametri.put("MC2S","N");
    hmParametri.put("PRO","D");

  }

  public HashMap gethmParametri() {
    return hmParametri;
  }

  public boolean setupConnection() {

    hr.restart.start.startClient();
    int broj_sec = 0;
    boolean uspjesnostspajanja = false;

    while (broj_sec<timeConnection) {
      try {
        Thread.currentThread().sleep(1000);
      }
      catch (InterruptedException ex) {
      }
      if (hr.restart.start.isClientConnected()) {
        uspjesnostspajanja = true;
        localURL = hr.restart.start.getClient().getSocket().getLocalAddress().getHostAddress();
        serverURL = hr.restart.start.getClient().getSocket().getInetAddress().getHostAddress();
        break;
      }
    }

    if (!uspjesnostspajanja) {
      porukaHandle("Neuspjesno spajanje u roku od "+timeConnection+" sekundi.");
      gasiStvar();
    }
    return uspjesnostspajanja;
  }

  public boolean setupURLs() {

    if (isServer()) {
      boolean forreturn = remoteUrls();
      if (!forreturn) porukaHandle(remoteporuka);
      return forreturn;
    } else {
      raSetupUrls rSU = new raSetupUrls();
      rSU.localURL = localURL;
      rSU.serverURL = serverURL;
      if (rSU.setupURLs()) {
//        if (isGUI()) {
//          rFRR.setURLs(rSU.reteserverURL,rSU.retlocalURL);
//        }
        return true;
      } else {
        porukaHandle(rSU.poruka);
        return false;
      }
    }
  }

  public boolean remoteStart(final String metoda) {

    if (metoda== null || metoda.equalsIgnoreCase("")) return false;
    Comunicator4Replicator komunike = new Comunicator4Replicator();
    Comunicator4Replicator downKomunike = (Comunicator4Replicator) hr.restart.start.getClient().remoteInvoke(komunike,
        metoda,"hr.restart.robno.Invoker4Replicator",new Object[]{komunike});
    return downKomunike.result.booleanValue();
  }

  public boolean remoteUrls() {

    remoteporuka = "";
    Comunicator4Replicator komunike = new Comunicator4Replicator();

    komunike.localURL = localURL;   // setiranje parametar
    komunike.serverURL= serverURL;
    Comunicator4Replicator downKomunike = (Comunicator4Replicator) hr.restart.start.getClient().remoteInvoke(komunike,
            "invoke_setupURLs","hr.restart.robno.Invoker4Replicator",new Object[]{komunike});
    if (!downKomunike.result.booleanValue()) {
      remoteporuka = komunike.poruka;
    }
//    if (isGUI()) {
//      rFRR.setURLs(downKomunike.reteserverURL,downKomunike.retlocalURL);
//    }

    return downKomunike.result.booleanValue();
  }


  public static void main(String[] args) {
    RemoteStart remoteStart1 = new RemoteStart();
    remoteStart1.parseArgs(args);
  }

/*

  važe\u0107i argumenti su :
  [/GUI:D|N]                 ---- prikaz ekrana default D
  [/Server:D|N]              ---- izvo\u0111enje na serveru nakon konekcije  default N
  [/Dialup:D|N]              ---- konekcija putem DialUp-a ili mrezom default N sto znaci veza mrežom
  [/cmd:"DIALUPCOMMAND"]     ---- komanda za pokretanje Dial-Up-a
  [/dcmd:"DIALUPCOMMAND"]    ---- komanda za prekid konekcije Dial-Up-a

*/


  public boolean tokeniziranje(String ss) {
    java.util.StringTokenizer st1 = new java.util.StringTokenizer(ss,"/",false);
    if (st1.countTokens()<2) {
      java.util.StringTokenizer st2 = new java.util.StringTokenizer(st1.nextToken(),":",false);
      if (st2.countTokens()==2) {
        String finder = st2.nextToken().toUpperCase().trim();
        if (hmParametri.containsKey(finder)) {
          hmParametri.put(finder,st2.nextToken().trim());
        } else {
          System.out.println("NEPOSTOJECI PARAMETAR "+finder);
          return false;
        }
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  public void help() {
    System.out.println("važe\u0107i argumenti su :");
    System.out.println("                     [/GUI:D|N]                 ---- prikaz ekrana default D");
    System.out.println("                     [/Server:D|N]              ---- izvo\u0111enje na serveru nakon konekcije  default N");
    System.out.println("                     [/Dialup:D|N]              ---- konekcija putem DialUp-a ili mrezom default N sto znaci veza mrežom");
    System.out.println("                     [/cmd:\"DIALUPCOMMAND\"]     ---- komanda za pokretanje Dial-Up-a defaultna je rasdial");
    System.out.println("                     [/dcmd:\"DIALUPCOMMAND\"]    ---- komanda za prekid konekcije Dial-Up-a default rasdial / DISCONECT");
    System.out.println("                     [/mc2s:D|N]                  ---- transfer mat. podataka s clijenta na server default N");
    System.out.println("                     [/ms2c:D|N]                  ---- transfer mat. podataka s servera na client default N");
    System.out.println("                     [/pro:D|N]                   ---- transfer prometa s clienta na server default D");
    System.out.println("[] zna\u010Di da je parametar opcionalan");
  }

  /**
   * parseArgs (String[] args)
   * @param args
   */
  public void parseArgs(String[] args){

    if (args.length==1 && (args[0].equalsIgnoreCase("/H") || args[0].equalsIgnoreCase("/ H"))) {
      help();
      izlaz();
    }
    for (int i=0 ;i< args.length;i++) {
      if (!tokeniziranje(args[i])) {
        System.out.println("Neispravni parametri !!!");
        help();
        izlaz();
      }
    }
    start();

//    remoteStart();
  }

  public void start(){

    String gui = (String) hmParametri.get("GUI");
    if (gui.equalsIgnoreCase("D")) {
      GUIStart(true);
    } else {
      go();
    }
  }

  public void go() {
    boolean prijenosOK = true;
    if (!modemConnection()) izlaz();
    if (isServer()) {
      if (!setupConnection()) izlaz();
      if (!setupURLs()) izlaz();
      if (isMCS2S()) {
        if (!remoteStart("invoke_robMatClientToServer")) {
          IzlazGasiPoruka("Greška u REMOTE prijenosu (robMatClientToServer)");
          prijenosOK = false;
        }
      }
      if (isMS2C()) {
        if (!remoteStart("invoke_robMatServerToClient")) {
          IzlazGasiPoruka("Greška u REMOTE prijenosu (robMatServerToClient)");
          prijenosOK = false;
        }
      }
      if (isPRO()) {
        prepare("robDocClientToServer");
        if (!remoteStart("invoke_robDocClientToServer")) {
          IzlazGasiPoruka("Greška u REMOTE prijenosu (robDocClientToServer)");
          prijenosOK = false;
        }
      }
    } else  {
      if (isMCS2S()) {
        if (!raReplicateRobno.robLocalMatClientToServer()) {
          IzlazGasiPoruka("Greška u LOCAL prijenosu (robMatClientToServer)");
          prijenosOK = false;
        }
      }
      if (isMS2C()) {
        if (!raReplicateRobno.robLocalMatServerToClient()) {
          IzlazGasiPoruka("Greška u LOCAL prijenosu (robMatServerToClient)");
          prijenosOK = false;
        }
      }
      if (isPRO()) {
        prepare("robDocClientToServer");
        if (!raReplicateRobno.robLocalDocClientToServer()) {
          IzlazGasiPoruka("Greška u LOCAL prijenosu (robDocClientToServer)");
          prijenosOK = false;
        }
      }
    }
    if (prijenosOK) IzlazGasiPoruka("Prijenos je uspješno obavljen !!!");
  }

  private void setMessageExtern(final String msg) {
    raProcess.runChild(null,new Runnable() {
      public void run() {
            raProcess.setMessage(msg, false);
      }});
  }

  public void IzlazGasiPoruka(String poruka) {
    porukaHandle(poruka);
    gasiStvar();
    izlaz();
  }

  public boolean isGUI() {
    return ((String) hmParametri.get("GUI")).equalsIgnoreCase("D");
  }

  public boolean isServer() {
    return ((String) hmParametri.get("SERVER")).equalsIgnoreCase("D");
  }

  public boolean isMS2C() {
    return ((String) hmParametri.get("MS2C")).equalsIgnoreCase("D");
  }

  public boolean isMCS2S() {
    return ((String) hmParametri.get("MC2S")).equalsIgnoreCase("D");
  }

  public boolean isPRO() {
    return ((String) hmParametri.get("PRO")).equalsIgnoreCase("D");
  }

  public boolean isDialUp() {
    return ((String) hmParametri.get("DIALUP")).equalsIgnoreCase("D");
  }

  private void porukaHandle(String poruka) {
    if (isGUI()) {
      rFRR.porukaError(poruka);
    } else {
      System.out.println(poruka);
    }
  }

  public void GUIStart(boolean setup){
  }


  public void GUIStart_allone(boolean setup){
    hr.restart.util.startFrame.raLookAndFeel();
    rFRR = (raFrameRobnoReplicator)
            hr.restart.util.startFrame.getStartFrame().showFrame("hr.restart.robno.raFrameRobnoReplicator"
                     ,0,"Replikacija",true);

    if (setup) {
      rFRR.setupslogoreplikaciji(hmParametri);
      rFRR.setupRemoteStart(this);
    }
  }

  public void gasiStvar() {
    if (!isDialUp()) return;
    try {
      Process p = Runtime.getRuntime().exec("");
    }
    catch (Exception ex) {
      ex.printStackTrace();
      porukaHandle("Neuspjesno automatsko spustanje konekcije molimo spustite je ru\u010Dno !");
    }
  }

  public boolean modemConnection(){

    if (!isDialUp()) return true;

    try {
      String prigramcommand = (String) hmParametri.get("CMD");
      Process p = Runtime.getRuntime().exec(prigramcommand);
      p.waitFor();
      return true;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      porukaHandle("Konekcija modemom nije uspjela provjerite program za spajanje");
      return false;
    }
  }

  public void izlaz() {
    if (isBexit()) System.exit(0);
  }
  public boolean isBexit() {
    return bexit;
  }
  public void setBexit(boolean bexit) {
    this.bexit = bexit;
  }

  public void prepare(final String batch) {
    raProcess.runChild(new Runnable(){
      public void run(){
        rRP.prepareReplInf(batch);
      }
    });
  }
}