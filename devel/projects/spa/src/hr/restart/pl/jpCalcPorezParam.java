/****license*****************************************************************
**   file: jpCalcPorezParam.java
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
 * jpCalcPorezParam.java
 *
 * Created on 2003. prosinac 03, 16:37
 */

package hr.restart.pl;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/** 
 *
 * @author  andrej
 */
public class jpCalcPorezParam extends jpCalcPorez {
  
  public jpCalcPorezParam(frmCalcPorez f) {
    super(f);
    jInit();
  }
  public void jInit() {
    builder.appendSeparator("Osnovice za doprinose");
    addTextFieldRow("Maksimalna za MIO 1. stup",true);
    addTextFieldRow("Maksimalna za MIO 2. stup",true);
    addTextFieldRow("Minimalna za doprinose",true);
    builder.appendSeparator("Olakšice");
    addTextFieldRow("Osnovna olakšica",true);
    builder.appendSeparator("Osnovice za poreze");
    addTextFieldRow("Porez 1",true);
    addTextFieldRow("Porez 2",true);
    addTextFieldRow("Porez 3",true);
    addAncestorListener(new AncestorListener() {
      public void ancestorAdded(AncestorEvent event) {
        fCalcPorez.fixOkp();
      }
      public void ancestorRemoved(AncestorEvent event) {
      }
      public void ancestorMoved(AncestorEvent event) {
      }
    });
  }
  public String getTitle() {
    return "Globalni parametri";
  }
}
