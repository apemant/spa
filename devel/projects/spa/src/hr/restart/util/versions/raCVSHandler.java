/****license*****************************************************************
**   file: raCVSHandler.java
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
/*
 * raCVSHandler.java
 *
 * Created on 2004. sijeèanj 15, 12:35
 */

package hr.restart.util.versions;

import hr.restart.util.IntParam;
import hr.restart.util.VarStr;

import java.io.File;
import java.sql.Timestamp;
/**
 * metode koje vracaju informacije o fileovima na cvs-u, uglavnom cvs log naredba
 * potrebni podaci
 * user, password, server IP, cvsroot, local cvsroot
 * @author  andrej
 */
public class raCVSHandler {
  private String cvscomm = IntParam.getTag("patchmaker.cvscommand");//"cvs -d :pserver:andrej:andrej@161.53.200.99:/sda1/cvsroot";
  private String localcvspath = IntParam.getTag("patchmaker.source");
  /** Creates a new instance of raCVSHandler */
  public raCVSHandler() {
  }
  public String getFullLog(String filenameAndPath) {
    try {
      StringBuffer buffer = new StringBuffer();
      File wd = new File(localcvspath);
      String fname = new VarStr(filenameAndPath).replaceIgnoreCase(localcvspath+"/","").toString();
      
      System.out.println(cvscomm+" log "+fname);
      Process proc = Runtime.getRuntime().exec(cvscomm+" log "+fname, null,wd);
      int ch;
      while ((ch = proc.getInputStream().read()) > -1) buffer.append((char)ch);
      return buffer.toString();
    } catch (Exception ex) {
      return "Sorry, change log not available :(";
    }
  }
  public String getLastRevision(String filenameAndPath) {
    return null;
  }
  
  public String getMessage(String filenameAndPath, String revision) {
    return null;
  }
  
  public String getLastMessage(String filenameAndPath) {
    return getMessage(filenameAndPath, getLastRevision(filenameAndPath));
  }
  
  public Timestamp getDate(String filenameAndPath, String revision) {
    return null;
  }
  
  public Timestamp getLastDate(String filenameAndPath) {
    return getDate(filenameAndPath, getLastRevision(filenameAndPath));
  }
  
  public static void main(String[] args) {
    try {
      //Runtime.getRuntime().exec("cvs -d :pserver:$1@161.53.200.99:/sda1/cvsroot login")
      File wd = new File("/proj/SPA");
      System.out.println("wd exists "+wd.exists());
      System.out.println("wd isDirectory "+wd.isDirectory());
      Process proc = Runtime.getRuntime().exec("cvs -d :pserver:andrej:andrej@161.53.200.99:/sda1/cvsroot log spa/hr/restart/util/Assert.java", null,wd);
      int ch;
      while ((ch = proc.getInputStream().read()) > -1) System.out.write(ch);
      //      Process proc = Runtime.getRuntime().exec("echo pero");
      if (proc.waitFor()!=0) {
        while ((ch = proc.getErrorStream().read()) > -1) System.out.write(ch);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
