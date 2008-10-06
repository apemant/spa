/****license*****************************************************************
**   file: raTwoTableFrame.java
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

import java.awt.BorderLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public abstract class raTwoTableFrame extends raUpitLite {
  hr.restart.util.raTwoTableChooser ttc = new hr.restart.util.raTwoTableChooser(this);

  public raTwoTableFrame() {
    this(null);
  }
  public raTwoTableFrame(java.awt.Container owner) {
    super(raFrame.DIALOG, owner);
    getJdialog().setModal(true);
//    setModal(true);
    try {
      jInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jInit() throws Exception {

    this.getContentPane().add(ttc, BorderLayout.CENTER);
  }
  public void pack() {
    ttc.initialize();
    super.pack();
  }
  public hr.restart.util.raTwoTableChooser getTTC() {
    return ttc;
  }
}