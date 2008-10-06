/****license*****************************************************************
**   file: raCalcPorezDataGetter.java
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
 * raCalcPorezDataGetter.java
 *
 * Created on 2003. prosinac 08, 17:15
 */

package hr.restart.pl;

import hr.restart.util.OKpanel;

import java.math.BigDecimal;
/**
 * 
 * @author  andrej
 */
public abstract class raCalcPorezDataGetter implements hr.restart.util.okFrame {
  public OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      action_jBOK();
    }
    public void jPrekid_actionPerformed() {
      action_jPrekid();
    }
  };
  
  private frmCalcPorez fCalcPorez;
  
  public abstract javax.swing.ImageIcon getImageIcon();
  public abstract String getValue(String identifier);
  
  public void setFrmCalcPorez(frmCalcPorez f) {
    fCalcPorez = f;
  }
  
  public frmCalcPorez getFrmCalcPorez() {
    return fCalcPorez;
  }
  public void action_jPrekid() {
  }
  public boolean validateData() {
    return true;
  }

  public void action_jBOK() {
    if (!validateData()) return;
    java.util.Properties props = getFrmCalcPorez().binder.getProperties();
    for (java.util.Iterator i = props.keySet().iterator(); i.hasNext(); ) {
      String ident = i.next().toString();
      try {
//        String value = formatValue(getValue(ident));
//        props.setProperty(ident, value);
//        getFrmCalcPorez().binder.load(ident);
        try {
          BigDecimal bdval = new BigDecimal(getValue(ident));
          getFrmCalcPorez().binder.setBigDecimal(ident, bdval);
        } catch (NumberFormatException ex) {
          getFrmCalcPorez().binder.setText(ident, getValue(ident));
        }
        
      } catch (Exception ex) {
        System.out.println("failed to set value "+ident+". Ex: "+ex);
      }
    }
  }
  public OKpanel getOKpanel() {
    return okp;
  }
  public boolean doSaving() {
    return true;
  }

}
