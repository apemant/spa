/****license*****************************************************************
**   file: statusHandler.java
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
package hr.restart.util;


public class statusHandler {
  raStatusBar status;
  public statusHandler() {
    findStatus();
  }
  public void next(String msg) {
    if (msg == null) status.next();
    else status.next(msg);
  }
  private void findStatus() {
    status = raStatusBar.getStatusBar();
/*
    if (hr.restart.start.isMainFrame()) {
      status = hr.restart.mainFrame.getMainFrame().getStatusBar();
    } else {
      status = raLLFrames.getRaLLFrames().getMsgStartFrame().getStatusBar();
    }
*/
  }
  public void finnishTask() {
    status.finnishTask();
  }
  public void finnishTask(String msg) {
    status.finnishTask();
    status.statusMSG(msg);
  }
  public void startTask(int steps,String msg) {
    status.startTask(steps,msg);
  }
}