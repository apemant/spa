/****license*****************************************************************
**   file: raCronServerTrigger.java
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
 * raCronServerTrigger.java
 *
 * Created on 2004. svibanj 27, 11:58
 */

package hr.restart.db;
import hr.restart.util.raCron;
import hr.restart.util.raCronTask;
/**
 *
 * @author  andrej
 */
public class raCronServerTrigger extends raCronTask {
  raServerTrigger rST;
  /** Creates a new instance of raCronServerTrigger */
  public raCronServerTrigger(){
    rST = new raServerTrigger();
  }
  
  public long getPeriod() {
    return Integer.parseInt(raCron.getInstance().getProperty("servertrigger.minutes", "10"))*60*1000;
  }
  
  public long getDelay() {
    return Long.parseLong(raCron.getInstance().getProperty("servertrigger.delay", "10000"));
  }
  
  public boolean onException(Exception ex) {
    log(ex+":"+ex.getMessage());
    return true;
  }
  
  public void run() {
    rST.job();
  }
  
}
