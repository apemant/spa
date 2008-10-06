/****license*****************************************************************
**   file: raCronTask.java
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
 * raCronTask.java
 *
 * Created on 2004. svibanj 25, 09:47
 */

package hr.restart.util;

import java.util.Date;
import java.util.TimerTask;

/**
 * Task used in raCron engine. It's name must contain string "Cron" (case sensitive) 
 * for faster searching.
 * @author  andrej
 */
public abstract class raCronTask extends TimerTask {
  
  /**
   * time in milliseconds between successive task execution
   * @return time in milliseconds if 0 then executes once at <CODE>getStartTime()</CODE>
   * @see #getStartTime
   */
  public abstract long getPeriod();
  /**
   * What happens if exception is thrown
   * @return true if you want to reexecute task
   * @param ex exception occured
   */  
  public abstract boolean onException(Exception ex);
  /**
   * delay in milliseconds before task is to be executed
   * NOTE: if getStartTime is defined (overriden) getDelay is ignored
   * @return delay in milliseconds
   */  
  public long getDelay() {
    return 0;
  }
  /**
   * First time at which task is to be executed.
   * If time is in the past, the task is scheduled for immediate execution
   * NOTE: if getStartTime is defined (overriden) getDelay is ignored
   * @return time at which task is to be executed
   * default is null (execute immediatly)
   */  
  public Date getStartTime() {
    return null;
  }
  /**
   * Line to write in log when task successfully executed
   * @return null if you don't want log line,
   * default is getClass().getName()+" executed succesfuly ..."
   */  
  public String logLine() {
    return this.getClass().getName()+" executed succesfuly ...";
  }
  
  /**
   * Writes <I>text</I> to raCron log file
   * @param text text to write in log
   * @see raCron#log(String)
   */  
  public void log(String text) {
    raCron.getInstance().log(getClass().getName()+": "+text);
  }

}
