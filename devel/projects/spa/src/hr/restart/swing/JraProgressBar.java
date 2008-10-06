/****license*****************************************************************
**   file: JraProgressBar.java
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
package hr.restart.swing;

import hr.restart.util.sysoutTEST;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JProgressBar;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class JraProgressBar extends JProgressBar {
sysoutTEST ST = new sysoutTEST(false);
  private int delay = 30;
  private javax.swing.Timer t = new javax.swing.Timer(delay,new ActionListener() {
    int oldVal=0;
    public void actionPerformed(ActionEvent e) {
      if (oldVal==getValue()) {
        oldVal++;
        setValue(oldVal);
        getValue();
      } else {
        oldVal=getValue();
      }
      if (getPercentComplete()>=1.0) {
        t.stop();
      }
    }
  });
  public JraProgressBar() {
    super(0,10);
  }
  public JraProgressBar(int min, int max) {
    super(min,max);
  }
  public void startTimer() {
    if (!t.isRunning()) t.start();
  }
  public void stopTimer() {
    if (t.isRunning()) t.stop();
  }
  public void setDelay(int newDelay) {
    delay = newDelay;
    t.setDelay(delay);
  }
}