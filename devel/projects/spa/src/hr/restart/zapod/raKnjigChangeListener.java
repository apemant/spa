/****license*****************************************************************
**   file: raKnjigChangeListener.java
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
package hr.restart.zapod;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public abstract class raKnjigChangeListener implements PropertyChangeListener {

  public raKnjigChangeListener() {
  }
  public void propertyChange(PropertyChangeEvent evt) {
    if (!evt.getPropertyName().equals("knjig")) return;
    String newVal = (evt.getNewValue() != null)?evt.getNewValue().toString():"";
    String oldVal = (evt.getOldValue() != null)?evt.getOldValue().toString():"";
    if (newVal.equals(oldVal)) return;
    knjigChanged(oldVal,newVal);
  }

  /**
   * Ta metoda se overrida i u nju upise svoj kod kojim popracamo promjenu knjigovodstva
   * @param oldKnjig stara oznaka knjigovodstva
   * @param newKnjig nova oznaka knjigovodstva
   */
  public abstract void knjigChanged(String oldKnjig, String newKnjig);
}