/****license*****************************************************************
**   file: ClientConnected.java
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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnected {
  private String clID;
  private Socket socket;
  private hr.restart.util.client.ClConnection clientInfo;
  private ObjectOutputStream clientOut;
  private ObjectInputStream clientIn;

  public ClientConnected() {
  }

  public ClientConnected(String id, Socket sock) {
    setID(id);
    setSocket(sock);
  }

  public void setSocket(Socket sockt) {
    socket = sockt;
    try {
      clientOut = new ObjectOutputStream(socket.getOutputStream());
      clientIn = new ObjectInputStream(socket.getInputStream());
    }
    catch (Exception ex) {
      System.out.println("client io creatiion failed");
      ex.printStackTrace();
    }
  }
  public Socket getSocket() {
    return socket;
  }
  public ObjectOutputStream getOutput() {
    return clientOut;
  }
  public ObjectInputStream getInput() {
    return clientIn;
  }

  public void setID(String clid) {
    clID = clid;
  }
  public String getID() {
    return clID;
  }

  public void setClientInfo(hr.restart.util.client.ClConnection clnfo) {
    clientInfo = clnfo;
  }
  public hr.restart.util.client.ClConnection getClientInfo() {
    return clientInfo;
  }
  public String toString() {
    return clID + "  " + socket.getInetAddress().toString();
  }

  public void finalize() throws Throwable {
    socket = null;
    clientIn = null;
    clientOut = null;
    super.finalize();
  }
}
