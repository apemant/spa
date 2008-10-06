/****license*****************************************************************
**   file: frmPripChanger.java
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
package hr.restart.os;

import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class frmPripChanger extends frmStatTemplate {

  public frmPripChanger() {
    try {
      jbInit();
    }
    catch (Exception ex) {

    }

  }

  void jbInit() throws Exception
  {
    super.jbInit();
    this.addKeyListener(new java.awt.event.KeyAdapter() {
    public void keyPressed(KeyEvent e) {
      this_keyPressed(e);
    }
   });
   jtfDatum.addKeyListener(new java.awt.event.KeyAdapter() {
     public void keyPressed(KeyEvent e) {
       jtfDatum_keyPressed(e);
     }
    });
   jtfDatum.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        jtfDatum_focusLost(e);
      }
    });
    column3.setCaption("OS u pripremi");
    setRaTextFieldFocus(jtfDatum);
  }

  public void show()
  {
    tdsDate.setTimestamp("DATAKTIVIRANJA", hr.restart.util.Valid.getValid().findDate(false,0));
    this.prepareTDS("P", pres.getSelRow().getString("CORG"));
    this.setTitle("Promjena statusa pripreme");
    super.show();
  }

  void jtfDatum_keyPressed(KeyEvent e) {
    if(e.getKeyCode()==e.VK_ESCAPE)
      this.hide();
  }

  void this_keyPressed(KeyEvent e) {
    if(e.getKeyCode()==e.VK_ESCAPE)
    {
      if(tDS2.getRowCount()>0)
        prepareTDS("P", pres.getSelRow().getString("CORG"));
      jtfDatum.requestFocus();
    }
  }
  void jtfDatum_focusLost(FocusEvent e) {
    tDS.empty();
    tDS2.empty();
    tDS.emptyAllRows();
    tDS2.emptyAllRows();
     prepareTDS("P", pres.getSelRow().getString("CORG"));
    ttC.fireTableDataChanged();
  }
}