/****license*****************************************************************
**   file: ClientShell.java
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
package hr.restart.util.client;


import hr.restart.util.MathEvaluatorCommunicator;
import hr.restart.util.server.commObjectServerInvoke;
import hr.restart.util.shell.Shell;
import hr.restart.util.shell.ShellEvent;



public class ClientShell extends Shell {

  Client client;

  ClConnection serverCLC;

  commObjectServerInvoke cobGetID;

  ShellEvent serverCalc = new ShellEvent() {

    public boolean eventAction(String shellInput) {

      if (!checkConnect()) return false;

      MathEvaluatorCommunicator mac = new MathEvaluatorCommunicator();

      commObjectServerInvoke cobjs = new commObjectServerInvoke();

      cobjs.setInvoker(mac);

      cobjs.setMethodToInvoke("evaluate");

      cobjs.setServerInvokerClassName("hr.restart.util.MathEvaluatorInvoker");

      cobjs.setParameters(new Object[] {shellInput,mac});

      System.out.println(" = "+((MathEvaluatorCommunicator)client.remoteInvoke(cobjs)).getResult());

      return true;

    }

    public boolean inThread() {

      return false;

    }

    public String getInfo() {

      return "calculates given expression";

    }

  };

  ShellEvent enterPressed = new ShellEvent() {

    public boolean eventAction(String shellInput) {

      if (!checkConnect()) return false;

      if (!client.getConnection().isConnected()) {

        System.out.println("Client disconnected... exiting");

        stopShell();

      }

      return true;

    }

    public boolean inThread() {

      return false;

    }

    public String getInfo() {

      return "";

    }

  };

  ShellEvent serverQuery = new ShellEvent() {

    public boolean eventAction(String shellInput) {

      if (!checkConnect()) return false;

      hr.restart.util.DatasetCommunicator dsComm = new hr.restart.util.DatasetCommunicator();

      dsComm.query = shellInput;

      dsComm = (hr.restart.util.DatasetCommunicator)client.remoteInvoke(dsComm,"getDataSet","hr.restart.util.DatasetInvoker",new Object[] {dsComm});

      System.out.println(dsComm.dataset);

      return true;

    }

    public boolean inThread() {

      return false;

    }

    public String getInfo() {

      return "executes query on server and prints result";

    }

  };

  ShellEvent connectServer = new ShellEvent() {

    public boolean eventAction(String shellInput) {

      java.util.StringTokenizer tok = new java.util.StringTokenizer(shellInput.trim()," ");

      if (client != null) disconnect();

      try {

        String host = tok.nextToken();

        int port = Integer.parseInt(tok.nextToken());

        connect(host,port);

        return true;

      }

      catch (Exception ex) {

        System.out.println("Usage: connect host port");

        System.out.println("Example: connect 161.53.200.99 505");

        return false;

      }

    }

    public boolean inThread() {

      return true;

    }

    public String getInfo() {

      return "connects to server at host and port in parameter";

    }

  };

  public ClientShell() {

    this(null,0);

  }

  public ClientShell(String host,int port) {

    super("client>");

    setWelcomeMessage("Welcome to client shell! Type '?' for help.");

    defineInput("connect",connectServer);

    defineInput("calc",serverCalc);

    defineInput("query",serverQuery);

    defineInput("",enterPressed);

    if (host!=null) connect(host,port);

  }

  void connect(String host,int port) {

    System.out.println("starting client");

    client = new Client(host,port);

    client.startClient();

    System.out.println("retrieving client information");

    cobGetID = new commObjectServerInvoke();

    serverCLC = new ClConnection();

    serverCLC.connect(); // da ne sjebem konekciju, neznam zasto koristim istu klasu, valjda sam lijen i samo kompliciram

    cobGetID.setInvoker(serverCLC);

    cobGetID.setServerInvokerClassName("clientListener");

    cobGetID.setMethodToInvoke("setID");

    cobGetID.setParameters(new Object[] {serverCLC});

    serverCLC = (ClConnection)client.remoteInvoke(cobGetID);

    setPrompt(serverCLC.getID()+">");

    System.out.println();

  }

  boolean checkConnect() {

    if (client == null || serverCLC == null) {

      System.out.println("First you must connect to server. Use connect <host> <port>");

      return false;

    } else return true;

  }

  boolean disconnect() {

    //perversion

    if (client == null) return true;

    if (serverCLC == null) return true;

    serverCLC.disconnect();

    client.sendCommObject(cobGetID);//posalje na server pa on vrati pa trass

    setPrompt("client>");

    return true;

  }

  public boolean exitValidation() {

    return disconnect();

  }

  public static void main(String[] args) {

    if (args.length == 2) {

System.out.println("calling with args");

      new ClientShell(args[0],Integer.parseInt(args[1])).start();

    } else {

      new ClientShell().start();

    }

  }

}

