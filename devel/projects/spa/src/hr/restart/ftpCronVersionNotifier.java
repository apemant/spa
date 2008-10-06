/****license*****************************************************************
**   file: ftpCronVersionNotifier.java
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
 * ftpCronVersionNotifier.java
 *
 * Created on 2004. svibanj 26, 13:30
 */

package hr.restart;
import hr.restart.util.raCron;
import hr.restart.util.raCronTask;

/**
 *
 * @author  andrej
 */
public class ftpCronVersionNotifier extends raCronTask {
  ftpVersionNotifier ftpVN;
  /** Creates a new instance of ftpCronVersionNotifier */
  public ftpCronVersionNotifier() {
    ftpVN = new ftpVersionNotifier(raCron.getInstance().getProperty("version.lib","lib"));
  }
  
  public long getPeriod() {
    return Integer.parseInt(raCron.getInstance().getProperty("version.period", "10000"));
  }
  
  public boolean onException(Exception ex) {
    log("Exception occured "+ex+":"+ex.getMessage()+"\n *** probably new version. F... it!");
    return true;
  }
  
  public void run() {
    if (ftpVN.runTimerTask()) {
      log("New versions file in "+ftpVN.dirName+" created!");
    }
  }
  
  public String logLine() {
    return null;
  }
}
