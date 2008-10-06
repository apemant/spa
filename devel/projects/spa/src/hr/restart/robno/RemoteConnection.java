/****license*****************************************************************
**   file: RemoteConnection.java
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
import java.io.IOException;

public class RemoteConnection {
  /*
   rasdial entryname [username [password|*]] [/DOMAIN:domain]
           [/PHONE:phonenumber] [/CALLBACK:callbacknumber]
           [/PHONEBOOK:phonebookfile] [/PREFIXSUFFIX]

   rasdial [entryname] /DISCONNECT

          rasdial

  */

  private String entryname;
  private String username ="restart";
  private String password ="restart";
  private String phonenumber;
  private String os = "";
  private String connectioncommand;
  private String winconnectioncommand = "rasdial";
  private String linuxconnectioncommand = "";
  private boolean okParams = false;


  public RemoteConnection(){}
  public RemoteConnection(String [] args) throws Exception {
      for (int i=0;i<args.length;i++) {
        parsajString(args[i]);
      }
      if (entryname==null || entryname.equalsIgnoreCase("")) {
        System.out.println("Obavezan parametar entryname");
        okParams = false;
      } else if (os==null || os.equalsIgnoreCase("")) {
        System.out.println("Obavezan parametar os !!!");
        okParams = false;
      }

System.out.println("okPars" + okParams);
  }

  private void checkParams(String name_param,String value) throws Exception {
System.out.println(name_param+" = "+value);

    if (name_param.equalsIgnoreCase("entryname")) {
      if (value==null || value.equalsIgnoreCase("")) {
        okParams = false;
        throw new Exception("Entryname je null ili je prazan string");
      } else {
        this.entryname=value;
      }
    }
    else if  (name_param.equalsIgnoreCase("username")) {
      if (value!=null || !value.equalsIgnoreCase("")) {
        this.username=value;
      }
    }
    else if  (name_param.equalsIgnoreCase("password")) {
      if (value!=null || !value.equalsIgnoreCase("")) {
        this.password=value;
      }
    }
    else if  (name_param.equalsIgnoreCase("phonenumber")) {
      this.phonenumber = value;
    }
    else if  (name_param.equalsIgnoreCase("os")) {
      if (value!=null || !value.equalsIgnoreCase("")) {
        if (value.equalsIgnoreCase("WIN2000")) {
          this.os = value;
          this.connectioncommand=this.winconnectioncommand;
        }
        else if (value.equalsIgnoreCase("LINUX")) {
          this.os = value;
          this.connectioncommand=this.linuxconnectioncommand;
        } else {
          okParams = false;
          throw new Exception("Dozvoljeni parametri za os su WIN2000 ili LINUX");
        }
      } else {
        okParams = false;
        throw new Exception("Obavezan parametar os !!!");
      }
    }
    else if  (name_param.equalsIgnoreCase("connectioncommand")) {
      if (value!=null || !value.equalsIgnoreCase("")) {
        this.connectioncommand=value;
      }
    }
    else {
      okParams = false;
      throw new Exception("Nepostoje\u0107i parametar "+name_param);
    }
    okParams = true;
  }



  public RemoteConnection(String entryname,String username,String password,
                          String phonenumber,String os,String connectioncommand) throws Exception {

    checkParams("entryname",entryname);
    checkParams("username",username);
    checkParams("password",password);
    checkParams("phonenumber",phonenumber);
    checkParams("os",os);
    checkParams("connectioncommand",connectioncommand);


  }

  public void shutdownConnection (String disconectparam)  throws Exception {

    if (disconectparam!=null || !disconectparam.equalsIgnoreCase("")) {
      disconectparam = "/DISCONNECT";
    }

    try {
      Process p = Runtime.getRuntime().exec("rasdial \""+entryname.trim()+"\" "+disconectparam);
    }
    catch (IOException ex) {
      ex.printStackTrace();
      throw new Exception("Diskonekcija nije uspjela !!!!");
    }
  }

  public void uspostaviKonekciju() throws Exception {

    if (!okParams) throw new Exception ("Neispravni parametri") ;
    String prigramcommand = connectioncommand+" \""+entryname.trim()+"\" ";
    if (username!=null && !username.equalsIgnoreCase("")) {
      if (password!=null && !password.equalsIgnoreCase("")) {
        prigramcommand = prigramcommand +username+" "+password;
      }
    }

    if (phonenumber!=null && !phonenumber.equalsIgnoreCase("")) {
      prigramcommand = prigramcommand+" /PHONE:"+phonenumber;
    }
System.out.println(prigramcommand);
    Process p = Runtime.getRuntime().exec(prigramcommand);
    p.waitFor();
  }

  private void parsajString(String ss){
    int posnav=0;
    for (int i =1;i< ss.length();i++) {
      if (ss.charAt(i)==':' || ss.charAt(i)=='=') {
        posnav=i;
        break;
      }
    }
    if (posnav!=0 || posnav<ss.length()){
      try {
        checkParams(ss.substring(0,posnav),ss.substring(posnav+1,ss.length()));
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  public String getLocalURL() {
    return hr.restart.start.getClient().getSocket().getLocalAddress().getHostAddress();
  }

  public String getServerURL() {
    return hr.restart.start.getClient().getSocket().getInetAddress().getHostAddress();
  }


  public boolean makeDialupConnection(String coonection_command){
    try {
      Process p = Runtime.getRuntime().exec(coonection_command);
      p.waitFor();
      return (p.exitValue()==0);
    }
    catch (Exception ex) {
      return false;
    }
  }

  public boolean DialUpdisconnect(String disconect_command){
    try {
      Process p = Runtime.getRuntime().exec(disconect_command);
    }
    catch (Exception ex) {
      return false;
    }
    return true;
  }

  public boolean setupConnection(int timeConnection) {

    hr.restart.start.startClient();
    int broj_sec = 0;
    boolean uspjesnostspajanja = false;
    while (broj_sec<timeConnection) {
      try {
        Thread.currentThread().sleep(1000);
      }
      catch (InterruptedException ex) {
      }
      if (hr.restart.start.isClientConnected()) {
        uspjesnostspajanja = true;
        break;
      }
    }
    return uspjesnostspajanja;
  }
}