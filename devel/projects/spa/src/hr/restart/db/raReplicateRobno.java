/****license*****************************************************************
**   file: raReplicateRobno.java
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

import hr.restart.util.raProcess;

abstract public class raReplicateRobno extends raReplicate{

  raRobnoReplicator rR = new raRobnoReplicator();
  private static boolean  bisProcess = false;
  static raReplicateRobno rRR ;
  public static boolean isProcess() {
    return bisProcess;
  }
  public static void setProcess(boolean bprocess) {
    bisProcess= bprocess;
  }

  public raReplicateRobno(String batch_idx) {
    super(batch_idx);
    setReplicator(rR);
  }

  public raReplicateRobno(String batch_idx, boolean _useflags) {
    super(batch_idx,_useflags);
    setReplicator(rR);
  }

  public static boolean robMatServerToClient() {
    return remoteCall("robMatServerToClient",false);
  }

  public static boolean robLocalMatServerToClient() {
    initraReplicateRobno("robMatServerToClient",false);
    raProcess.runChild(rRR.getNewReplThread());
    return rRR.isSuccess();
  }

  public static boolean robMatClientToServer() {
    return remoteCall("robMatClientToServer",false);
  }

  public static boolean robLocalMatClientToServer() {
    System.out.println("replSveUSve");
    initraReplicateRobno("replSveUSve",false);

    raProcess.runChild(rRR.getNewReplThread());
    return rRR.isSuccess();
  }


  public static boolean robDocClientToServer() {
    return remoteCall("robDocClientToServer",true);
  }

  public static boolean robLocalDocClientToServer() {
    initraReplicateRobno("robDocClientToServer",true);
    raProcess.runChild(rRR.getNewReplThread());
    return rRR.isSuccess();
  }

  public static boolean remoteCall(String name,boolean relinfo) {
    rRR = null;
    rRR = new raReplicateRobno(name,relinfo) {
      public boolean validacija() {
        return true;
      }
    };
    rRR.setProcess(false);
    return rRR.start();
  }

  public static void initraReplicateRobno(final String what,final boolean repldef) {
    rRR = null;
    raProcess.runChild(new Runnable() {
      public void run() {
        rRR = new raReplicateRobno(what,repldef) {
          public void rRRinit() throws Exception {}
          public boolean validacija() {
            return rR.saveAll();
          }
        };
        rRR.setProcess(true);
    }});
  }
}