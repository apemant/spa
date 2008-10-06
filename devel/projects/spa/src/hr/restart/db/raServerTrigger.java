/****license*****************************************************************
**   file: raServerTrigger.java
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
package hr.restart.db;
import hr.restart.baza.dM;

import java.util.Timer;
import java.util.TimerTask;

public class raServerTrigger {

  private dM dm = dM.getDataModule();
  private hr.restart.db.raServerSideRobnoTransfer rSSRT = new hr.restart.db.raServerSideRobnoTransfer();
  private int minute = 10;
  private raReplicatePrepare rRP = new raReplicatePrepare();
  /**
   *@deprecated
   */
  public void start(){
    init();
    Timer timer = new Timer();

    timer.scheduleAtFixedRate(new TimerTask() {
      public void run() {
        job();
      }
    }, 1000,minute*60*1000);


  }
  /**
   * Radi aktualni posao. ai proglasio package visible da moze zvati iz raCronServerTrigger
   */
  void job(){

    String[] sss =  rSSRT.getZipFiles4Batch();
    if (sss==null) {
      System.out.println("nullll");
      return;
    }

    for (int i = 0;i<sss.length;i++){
      System.out.println(sss[i]);
      rSSRT.decompressZipFile(sss[i]);
      rSSRT.fromFileToDatabase();
      rSSRT.returnTransferKeys();
      rSSRT.makeReturnZipFile(sss[i]);
    }
    rSSRT.potvrdaTransHistory(sss);

//    startUpdateRealDB();
  }

  public void init(){
    dm.loadModules();
  }
  /**
   *@deprecated
   */
  public static void main(String[] args) {
    raServerTrigger rST = new raServerTrigger();
    if (args.length>0){
      try {
        rST.minute = Integer.parseInt(args[0]);
      }
      catch (NumberFormatException ex) {
        ex.printStackTrace();
      }
    }
    rST.start();
  }

  public void startUpdateRealDB(){
    raReplicatePrepare rRP = new raReplicatePrepare();
    rRP.prepareReplInf("robDocClientToServer",false);
    raReplicateRobno  rRR = new raReplicateRobno("robDocClientToServer",true) {
      public void rRRinit() throws Exception {}
      public boolean validacija() {
        return rR.saveAll();
      }
    };
    rRR.setProcess(false);
    rRR.start();
  }
}