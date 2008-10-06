/****license*****************************************************************
**   file: JraSplitPane.java
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

import java.awt.Component;

import javax.swing.JSplitPane;

public class JraSplitPane extends JSplitPane {

  public JraSplitPane() {
    super();
    myInit();
  }
  public JraSplitPane(int newOrientation,
                    boolean newContinuousLayout,
                    Component newLeftComponent,
                    Component newRightComponent){
    super(newOrientation,newContinuousLayout,newLeftComponent,newRightComponent);
    myInit();
  }
  public JraSplitPane(int newOrientation,
                    boolean newContinuousLayout) {
    super(newOrientation,newContinuousLayout);
    myInit();
  }
  public JraSplitPane(int newOrientation,
                    Component newLeftComponent,
                    Component newRightComponent){
    super(newOrientation,newLeftComponent,newRightComponent);
    myInit();
  }

  public JraSplitPane(int newOrientation) {
    super(newOrientation);
    myInit();
  }

  private void myInit()  {
//    setContinuousLayout(true);
//    setOneTouchExpandable(true);
    hr.restart.util.Aus.removeSwingKey(this,java.awt.event.KeyEvent.VK_F8);
  }
}