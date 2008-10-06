/****license*****************************************************************
**   file: Invoker4Replicator.java
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
import hr.restart.db.raReplicateRobno;

public class Invoker4Replicator {

  public void invoke_robMatClientToServer(Comunicator4Replicator comm) {
    comm.result = new Boolean(raReplicateRobno.robMatClientToServer());
  }
  public void invoke_robMatServerToClient(Comunicator4Replicator comm) {
   comm.result = new Boolean(raReplicateRobno.robMatServerToClient());
  }
  public void invoke_robDocClientToServer(Comunicator4Replicator comm) {
    comm.result = new Boolean(raReplicateRobno.robDocClientToServer());
  }

  public void invoke_setupURLs(Comunicator4Replicator comm) {
    raSetupUrls rSU = new raSetupUrls();
    rSU.localURL = comm.localURL;
    rSU.serverURL = comm.serverURL;
    comm.result = new Boolean(rSU.setupURLs());
    comm.poruka = rSU.poruka;
    comm.retlocalURL = rSU.retlocalURL;
    comm.reteserverURL = rSU.reteserverURL;
  }
}

