/****license*****************************************************************
**   file: RemoteShellInvoker.java
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
package hr.restart.util.shell;
import hr.restart.util.server.Server;

import java.lang.reflect.Method;

public class RemoteShellInvoker {
  Server server;
  public RemoteShellInvoker() {
    server = Server.getServer();
  }

  public void invokeOnServer(String methd,hr.restart.util.shell.RemoteShell.remoteResult rshRes) {
    try {
      Object[] parms = rshRes.params;
      Class[] cprms = null;
      if (parms != null) {
        cprms = new Class[parms.length];
        for (int i = 0; i < cprms.length; i++) cprms[i] = parms[i].getClass();
      }
      Method meth = server.getClass().getMethod(methd,cprms);
      rshRes.res = meth.invoke(server,parms).toString();
    }
    catch (Exception ex) {
      rshRes.res = "invocation failed: "+ex+"\n";
      if (server==null) return;
      rshRes.res = rshRes.res.concat("Methods are: \n");
      Method[] mets = server.getClass().getMethods();
      for (int i = 0; i < mets.length; i++) {
        if (mets[i].getModifiers() == (mets[i].PUBLIC|mets[i].DECLARED)) {
          String parInfo = "";
          for (int ii = 0; ii < mets[i].getParameterTypes().length; ii++) {
            Class parTy = mets[i].getParameterTypes()[ii];
            if (!parTy.equals(java.lang.String.class)) {
              parInfo = "NoPrInT";
              break;
            }
            parInfo = parInfo.concat(parTy.getName()).concat(" ");
          }

          if (!parInfo.equals("NoPrInT")) {
            rshRes.res = rshRes.res.concat("  - ")
                     .concat(mets[i].getName()+" ")
                     .concat(parInfo)
                     .concat("\n");
          }
        }
      }
    }
  }
}