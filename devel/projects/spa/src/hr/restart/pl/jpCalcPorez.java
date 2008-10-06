/****license*****************************************************************
**   file: jpCalcPorez.java
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
 * jpCalcPorez.java
 *
 * Created on 2003. prosinac 01, 17:43
 */

package hr.restart.pl;

import hr.restart.swing.raPropertiesBindedPanel;
import hr.restart.util.OKpanel;
import hr.restart.util.okFrame;

/**
 *  polja <read-only> 1-n - do 5 stavki
 *    bruto,    (koefsindrom31860)
 *    stdop1-n,
 *    maxosndop1-n,
 *    <iznosdop1-n>
 *    <ukdop>
 *    osnolak, koefolak
 *    premije1-n
 *    <sumolak>
 *    <iskolak>
 *    <porosn>
 *    stpor1-n,
 *    maxosnpor1-n,
 *    <iznospor1-n>
 *    <ukporez>
 *    stprir
 *    <iznosprir>
 *    <por+prir>
 *    neto
 * @author  andrej
 */
public class jpCalcPorez extends raPropertiesBindedPanel implements okFrame {
  
  frmCalcPorez fCalcPorez;
  OKpanel okp = new OKpanel() {
    public void jBOK_actionPerformed() {
      action_jBOK();
    }
    public void jPrekid_actionPerformed() {
       
    }
  };
  

  /** Creates a new instance of jpCalcPorez */
  // -- izbaciti ako ima nesto pl related i -> hr.restart.swing.raPropertiesBindedPanel
  // -- konstruktor s binderom
  public jpCalcPorez(frmCalcPorez f) {
    super(f.binder);
    fCalcPorez = f;
  }
  protected jpCalcPorez() {
  }
  
  public void action_jPrekid() {}
  public void action_jBOK() {
//    fCalcPorez.binder.store();
  }
  public java.awt.Container getContentPane() {
    return this;
  }
  public String getTitle() {
    return "";
  }
  public OKpanel getOKpanel() {
    return okp;
  }
  public boolean doSaving() {
    return false;
  }

}
