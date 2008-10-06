/****license*****************************************************************
**   file: RemoteShell.java
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
import hr.restart.util.client.Client;
import hr.restart.util.client.clientDisconnectCommObject;

public class RemoteShell extends Shell {
  Client client;
  private ShellEvent shellConnect = new ShellEvent() {
    public boolean eventAction(String inp) {
      return connect(inp);
    }
    public boolean inThread() {return false;}
    public String getInfo() {
      return "connects to <servername> <port>";
    }
  };

  private ShellEvent shellRemote = new ShellEvent() {
    public boolean eventAction(String inp) {
      if (!client.getConnection().isConnected()) return false;
      remoteResult rRes = new remoteResult();
      java.util.StringTokenizer tokens = new java.util.StringTokenizer(inp," ");
      String meth = null;
      if (tokens.countTokens() > 1) {
        Object[] pars = new Object[tokens.countTokens()-1];
        meth = tokens.nextToken();
        for (int i = 0; i < pars.length; i++) {
          pars[i] = tokens.nextToken();
        }
        rRes.params = pars;
      } else {
        meth = inp.trim();
      }
      remoteResult r = (remoteResult)client.remoteInvoke(rRes,"invokeOnServer","hr.restart.util.shell.RemoteShellInvoker",new Object[] {meth,rRes});
      System.out.println(r.res);
      return true;
    }
    public boolean inThread() {return false;}
    public String getInfo() {
      return "invokes command on server and returns result";
    }
  };

  private ShellEvent shellClose = new ShellEvent() {
    public boolean eventAction(String inp) {
      if (client != null) {
        if (!client.getConnection().isConnected()) return false;
        client.getConnection().disconnect();
        clientDisconnectCommObject c = new clientDisconnectCommObject(client.getConnection());
        client.sendCommObject(c);
        return true;
      } else return false;
    }
    public boolean inThread() {return false;}
    public String getInfo() {
      return "closes connection";
    }
  };

  public RemoteShell() {
    super("RemoteShell>");
    defineInput("open",shellConnect);
    defineInput("close",shellClose);
    defineInput("remote",shellRemote);
  }

  public boolean exitValidation() {
    return shellClose.eventAction("");
  }

  private boolean connect(String inp) {
    java.util.StringTokenizer toks = new java.util.StringTokenizer(inp," ");
    if (toks.countTokens() != 2) return false;
    String host = toks.nextToken().trim();
    int port = 0;
    try {
      port = Integer.parseInt(toks.nextToken().trim());
    }
    catch (NumberFormatException ex) {
      return false;
    }
    System.out.println("connecting to "+host+" at port "+port);
    client = new Client(host,port);
    client.startClient();
    return true;
  }

  public static void main(String[] args) {
    RemoteShell rsh = new RemoteShell();
    rsh.start();
    try {
      String inp = args[0].concat(" ").concat(args[1]);
      if (!rsh.connect(inp)) throw new Exception();
    }
    catch (Exception ex) {
      System.out.println("not conected");
    }
  }

  public class remoteResult implements java.io.Serializable {
    String res = "";
    Object[] params = null;
  }
}