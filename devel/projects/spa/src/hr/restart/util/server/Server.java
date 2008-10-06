/****license*****************************************************************
**   file: Server.java
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



import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;



public class Server {

  java.beans.PropertyChangeSupport propertyChangeSupport;

//propertiyji koje server ispucava



/**

 * kada se client spoji, server notificira propertychangelistenere sa ovim propertyName-om i kao novu vrijednost daje

 * clientov ID

 */

  public static String CL_CONNECTED = "cl_connected";



  /**

   * kada se client disconecta, server notificira propertychangelistenere sa ovim propertyName-om i kao novu vrijednost daje

   * clientov ID

   */

  public static String CL_DISCONNECTED = "cl_disconnected";



  /**

   * kada server dobije neki objekt od clienta, server notificira propertychangelistenere sa ovim propertyName-om

   * i kao novu vrijednost daje

   * clientov ID

   */

  public static String CL_GETOBJECT = "cl_getobject";



  /**

   *

   * kada server posalje neki objekt clientu, server notificira propertychangelistenere

   * sa ovim propertyName-om i kao novu vrijednost daje

   * clientov ID

   */

  public static String CL_SENDOBJECT = "cl_sendobject";

//

  java.net.ServerSocket serversocket;

  java.util.Hashtable connections = new java.util.Hashtable();

  private static Server srvr;

  boolean isRunning = false;

  int port;

  public Server() {

    this(666);

  }



  public Server(int serverport) {

    port = serverport;

    propertyChangeSupport = new PropertyChangeSupport(this);

    srvr = this;

  }



  public static Server getServer() {

    return srvr;

  }



  public void startServer() throws Exception {

    serversocket = new ServerSocket(port);

    isRunning = true;

    connectionListener.start();

    serverOutput("Server booted at port "+port+". Accepting requests...");

  }

  public static void serverOutput(String output) {

//    System.out.println(

//        new java.sql.Timestamp(System.currentTimeMillis()).toString().concat(": ")

//        .concat(output)

//      );

    System.out.println(output);

  }



  public void stopServer() throws Exception {

    Enumeration keys = connections.keys();

    while (keys.hasMoreElements()) {

      String key = keys.nextElement().toString();

      kill(key);

    }

    isRunning = false;

    serversocket.close();

  }



  public String kill(String clID) {

    commObjectClientInvoke cobjc = new commObjectClientInvoke();

    ClientConnected currClient = (ClientConnected)connections.get(clID);

    cobjc.setInvoker(currClient.getClientInfo());

    cobjc.setMethodToInvoke("disconnect");

    cobjc.setParameters(null);

    sendCommObject(cobjc,clID);

    removeConnection(clID);

    return clID+" disconnected!";

  }



  public String gc() {

    System.gc();

    return "garbage collection on server ... \n".concat(hr.restart.util.shell.Shell.printMemUsage());

  }



  public String mem() {

    return hr.restart.util.shell.Shell.printMemUsage();

  }

  private void removeConnection(String ID) {

    serverOutput("client "+ID+" left the party");

    firePropertyChange(CL_DISCONNECTED,ID);

    try {

      ClientConnected dead = (ClientConnected)connections.remove(ID);

      if (dead != null) dead.finalize();

    }

    catch (Throwable ex) {

      serverOutput("client "+ID+" finalization error ex = "+ex+" no big deal:)");

    }

  }

  public void sendCommObject(commObject cobj,String clID) {

    ClientConnected clientConn = (ClientConnected)connections.get(clID);

    try {

      clientConn.getOutput().writeObject(cobj);

      firePropertyChange(CL_SENDOBJECT,clID);

    }

    catch (Exception ex2) {

      serverOutput("failed to send object to client "

                   .concat(clID).concat(". object = ")

                   .concat(cobj.toString())

                  );

      ex2.printStackTrace();

    }

  }



  public String who() {

    Iterator iter = connections.entrySet().iterator();

    String ret = "";

    while (iter.hasNext()) {

      Map.Entry entry = (Map.Entry)iter.next();

      ClientConnected client = (ClientConnected)entry.getValue();

      ret = ret + client + "\n";

    }

    return ret;

  }



  public void addPropertyChangeListener(PropertyChangeListener lis) {

    propertyChangeSupport.addPropertyChangeListener(lis);

  }

  public void addPropertyChangeListener(String propName, PropertyChangeListener lis) {

    propertyChangeSupport.addPropertyChangeListener(propName,lis);

  }

  public void removePropertyChangeListener(PropertyChangeListener lis) {

    propertyChangeSupport.removePropertyChangeListener(lis);

  }

  public void removePropertyChangeListener(String propName, PropertyChangeListener lis) {

    propertyChangeSupport.removePropertyChangeListener(propName,lis);

  }

  public void firePropertyChange(PropertyChangeEvent evt) {

    propertyChangeSupport.firePropertyChange(evt);

  }

  public void firePropertyChange(String propName, Object oldValue, Object newValue) {

    propertyChangeSupport.firePropertyChange(propName, oldValue, newValue);

  }

  public void firePropertyChange(String propName, String newValue) {

    firePropertyChange(propName, "", newValue);

  }



  Thread connectionListener = new Thread() {

    public void run() {

      int i=1;

      do {

        try {

          String newClID = "cl"+i;

          Socket clsocket = serversocket.accept();

          serverOutput(newClID+" accepted ");

          ClientConnected clientConn = new hr.restart.util.server.ClientConnected(newClID,clsocket);

          clientListener clientLis = new clientListener(clientConn);

          clientLis.start();

          connections.put(newClID,clientConn);

          firePropertyChange(CL_CONNECTED,newClID);

          i++;

        }

        catch (Exception ex) {

          if (isRunning) {

            serverOutput("client acception failed");

            ex.printStackTrace();

          }

        }

      } while (isRunning);

      try {

        serversocket.close();

        serverOutput("Server is down");

      }

      catch (Exception ex2) {

        ex2.printStackTrace();

        serverOutput("server shutdown error... try Ctrl+C :)");

      }

    }

  };



  class clientListener extends Thread {

    ClientConnected clientConn;

    clientListener(ClientConnected clConn) {

      clientConn = clConn;

    }

    public void setID(hr.restart.util.client.ClConnection clc) {

      clc.setID(clientConn.getID());

    }

    public void run() {

      try {

        ObjectInputStream clIn = clientConn.getInput();

        ObjectOutputStream clOut = clientConn.getOutput();

        while (isRunning) {

          commObject cobj = (commObject)clIn.readObject();

          firePropertyChange(CL_GETOBJECT,clientConn.getID());

          if (cobj instanceof hr.restart.util.client.clientDisconnectCommObject) {
            if (cobj.getInvoker() instanceof hr.restart.util.client.ClConnection) {
              hr.restart.util.client.ClConnection clCon = (hr.restart.util.client.ClConnection)cobj.getInvoker();
              clCon.setID(clientConn.getID());
              clientConn.setClientInfo(clCon);
              sendCommObject(cobj,clientConn.getID());
            }
          }

          if (cobj instanceof commObjectServerInvoke) {

            try {

              commObjectServerInvoke cobjs = (commObjectServerInvoke)cobj;

              Object clientInvoker = cobjs.getInvoker();

              Object serverInvoker;

              if (cobjs.getServerInvokerClassName().equals("clientListener")) {

                serverInvoker = this;

              } else {

                serverInvoker = Class.forName(cobjs.getServerInvokerClassName()).newInstance();

              }

              cobj.setInvoker(serverInvoker);

              cobj.invoke();

              cobj.setInvoker(clientInvoker);

              sendCommObject(cobj,clientConn.getID());

            }

            catch (Exception ex) {

              ex.printStackTrace();

            }

          }

        }

      } catch (Exception ex) {

        try {

          clientConn.getSocket().close();

        }

        catch (Exception ex2) {

        }

        removeConnection(clientConn.getID());

      }

    }

  };

}

