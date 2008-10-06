/****license*****************************************************************
**   file: ServerShell.java
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
package hr.restart.util.server;

import hr.restart.util.shell.Shell;
import hr.restart.util.shell.ShellEvent;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class ServerShell extends Shell {
  Server server;// = new Server();

  ShellEvent startServer = new ShellEvent() {
    public boolean eventAction(String shellInput) {
      if (server!=null) if (server.isRunning) {
        System.out.println("Server already running!");
        return false;
      }
      try {
        server = new Server(Integer.parseInt(shellInput));
        server.startServer();
        return true;
      }
      catch (Exception ex) {
        System.out.println("Exception "+ex);
        return false;
      }
    }
    public boolean inThread() {
      return false;
    }
    public String getInfo() {
      return "Starts server with port in argument";
    }
  };

  ShellEvent stopServer = new ShellEvent() {
    public boolean eventAction(String shellInput) {
      if (!server.isRunning) {
        System.out.println("Server allready down!");
        return false;
      }
      try {
        server.stopServer();
        return true;
      }
      catch (Exception ex) {
        System.out.println("Exception "+ex);
        return false;
      }
    }
    public boolean inThread() {
      return false;
    }
    public String getInfo() {
      return "stops server";
    }
  };

  ShellEvent whoServer = new ShellEvent() {
    public boolean eventAction(String shellInput) {
      System.out.println(server.who());
      return true;
    }
    public boolean inThread() {
      return false;
    }
    public String getInfo() {
      return "prints all clients connected";
    }
  };

  ShellEvent killClient = new ShellEvent() {
    public boolean eventAction(String shellInput) {
      server.kill(shellInput);
      return true;
    }
    public boolean inThread() {
      return false;
    }
    public String getInfo() {
      return "disconnects specified client";
    }
  };

  public ServerShell() {
    super("server>");
    setWelcomeMessage("Welcome to server shell! Type '?' for help.");
    defineInput("start",startServer);
    defineInput("shut",stopServer);
    defineInput("who",whoServer);
    defineInput("kill",killClient);
  }

  public boolean exitValidation() {
    if (server == null) return true;
    if (server.isRunning) {
      System.out.println("You can't exit while server is active. Shut the server first. '?' for help.");
      return false;
    } else {
      try {
        server.serversocket.close();
      }
      catch (Exception ex) {
      }
      return true;
    }
  }
  public static void main(String[] args) {
    try {
      new ServerShell().start();
    }
    catch (Exception ex) {

    }
  }

}
