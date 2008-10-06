/****license*****************************************************************
**   file: JraComboBox.java
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

import hr.restart.util.Aus;

import javax.swing.JComboBox;

public class JraComboBox extends JComboBox {  
  public JraComboBox() {
    super();    
  }
  public JraComboBox(Object[] items) {
    super(items);
  }
  public boolean isFocusTraversable() {
    return true;
  }
  
  public void addNotify() {
    super.addNotify();
    Aus.installComboPopupHideKey(this);
    Aus.installEnterRelease(this);
  }
  
  public void removeNotify() {
    super.removeNotify();
    AWTKeyboard.unregisterComponent(this);
  }
}
