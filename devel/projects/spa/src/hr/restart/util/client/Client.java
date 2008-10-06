/****license*****************************************************************
**   file: Client.java
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

import hr.restart.util.server.commObject;
import hr.restart.util.server.commObjectServerInvoke;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
  clientThread cliThr;
//
  private java.net.Socket clientsocket;
  private Object result;
  private ObjectOutputStream toserver;
  private ObjectInputStream reader;
//  private volatile commObject serverCommObj;
  private ClConnection con = new ClConnection();
  private int port;
  private String host;

  public Client() {
    this("localhost",666);
  }
  public Client(String host_,int port_) {
    setPort(port_);
    setHost(host_);
  }
  public void setPort(int p) {
    port = p;
  }
  public ObjectOutputStream getOutputStream() {
    return toserver;
  }
  public ObjectInputStream getInputStream() {
    return reader;
  }
  public int getPort() {
    return port;
  }
  public void setHost(String h) {
    host = h;
  }
  public String getHost() {
    return host;
  }
  public Socket getSocket() {
    return clientsocket;
  }
  public ClConnection getConnection() {
    return con;
  }
  public void startClient() {
    cliThr = new clientThread();
    cliThr.start();
  }
/*
  public Object getResult() {
    while (true) {
      if (result != null) {
        return result;
      }
    }
  }
*/
  public synchronized commObject getCommObject() {
    /*
    while (true) {
      if (serverCommObj != null) {
        return serverCommObj;
      }
      try {
        Thread.currentThread().sleep(10);
        Thread.currentThread().yield();
      }
      catch (Exception ex) {
      }
    }
    */
    return cliThr.getServerComObj();
  }

  public synchronized void sendCommObject(commObject cobj) {
    while (true) {
      if (toserver!=null) break;
      try {
        Thread.currentThread().sleep(10);
        Thread.currentThread().yield();
      }
      catch (Exception ex) {
      }
    }
    try {
      toserver.writeObject(cobj);
    }
    catch (Exception ex2) {
      System.out.println("failed to send object");
      ex2.printStackTrace();
    }
  }

  public synchronized Object remoteInvoke(commObject cobj) {
    cliThr.setServerCommObjectNull();
    sendCommObject(cobj);
    return getCommObject().getInvoker();
  }

  /**
   * primjer koristenja:
   * <pre>
   * Potrebno je pozvati metodu makeNesto u klasi raNesto koja ima 4000 linija
   *
   * a) komunikator
   * public class raNestoComunicator implements java.io.Serializable {//mora biti Serializable da bi putovala
   *    public String param1
   *    public Integer param2
   *    public Boolean result
   * }
   *
   * b) invoker - instancira se na serveru
   * public class raNestoInvoker {
   *    public void invokeNesto(raNestoComunicator comm) {
   *      new raNesto();
   *      raNesto.setParam1(comm.param1); //opcionalno
   *      raNesto.setParam2(comm.param2); //opcionalno
   *      boolean rez = raNesto.makeNesto(); //poziv zadane metode
   *      comm.result = new Boolean(nesto); //opcionalno
   *    }
   * }
   *
   * c) poziv s clienta u bilo kojoj metodi
   * void biloKojaMetoda() {
   *   if (!hr.restart.start.isClientConnected) return;
   *   raNestoComunicator komunike = new raNestoComunicator();
   *   komunike.param1 = ... ; //opcionalno
   *   komunike.param2 = new Integer(...); //opcionalno
   *   Object vratio = hr.restart.start.getClient.remoteInvoke(komunike,"invokeNesto","raNestoInvoker",new Object[]{komunike});
   *   komunike = (raNestoComunicator)vratio;
   *   System.out.println("Operacija "+komunike.result.booleanValue()?"je":"nije"+" uspjela!");
   * }
   * </pre>
   * @param invokerI  komunikacijska klasa koja sadrzi podatke za invoke i rezultat. Ona putuje amo-tamo
   * @param methodI  naziv metode u klasi serverInvokerI koja se izvrsava na serveru
   * @param serverInvokerI  naziv klasa koja sadrzi metodu methodI koja se instancira na serveru i invoka tu metodu
   * @param parametersI  parametri metode methodI - najbolje da je komunikacijski objekt (invokerI)
   */
  public synchronized Object remoteInvoke(Object invokerI,String methodI,String serverInvokerI,Object[] parametersI) {
    commObjectServerInvoke cobjs = new commObjectServerInvoke();
    cobjs.setInvoker(invokerI);
    cobjs.setMethodToInvoke(methodI);
    cobjs.setServerInvokerClassName(serverInvokerI);
    cobjs.setParameters(parametersI);
    return remoteInvoke(cobjs);
  }

  private void sendDisconnectInformation() {
    clientDisconnectCommObject cdco = new clientDisconnectCommObject(con);
    sendCommObject(cdco);
  }


  class clientThread extends Thread {
    private volatile commObject serverCommObj;
    public void run() {
      try {
        System.out.println("conecting to server "+ host +" at port "+port+"...");
        clientsocket = new Socket(host,port);
        con.connect();
        reader = new ObjectInputStream(clientsocket.getInputStream());
        toserver = new ObjectOutputStream(getSocket().getOutputStream());

        sendDisconnectInformation();
        while (con.isConnected()) {
          try {
            serverCommObj = (commObject)reader.readObject();
          } catch (Exception readerex) {
            con.disconnect();
            break;
          }
          handleServerCommObj();
          yield();
        }
      }
      catch (Exception ex) {
        System.out.println("failed");
        ex.printStackTrace();
      }
    }
    public synchronized void handleServerCommObj() throws Exception {
      if (serverCommObj instanceof hr.restart.util.server.commObjectClientInvoke) {
        result = serverCommObj.invoke();
      }
      if (serverCommObj.getInvoker() instanceof ClConnection) {
        con = (ClConnection)serverCommObj.getInvoker();
      }
    }
    public synchronized commObject getServerComObj() {
      while (serverCommObj == null) {
        try {
          sleep(10);
//          yield();
        }
        catch (Exception ex) {

        }
      }
      return serverCommObj;
    }
    public void setServerCommObjectNull() {
      serverCommObj = null;
    }
  };

  public static void main(String[] args) {
    String host = "localhost";
    int port = 666;
    new Client().startClient();
  }

}
