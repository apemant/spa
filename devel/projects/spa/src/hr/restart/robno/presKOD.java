/****license*****************************************************************
**   file: presKOD.java
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
package hr.restart.robno;

import javax.swing.JOptionPane;

import hr.restart.util.lookupData;

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class presKOD extends jpPreselectDoc {
  static presKOD preskod;

  public void defaultMatDocAllowed(){
    isMatDocAllowed = false;
  }

  public void defaultMatDocAllowedifObrac(){
    isMatDocifObracAllowed = false;
  }



  public presKOD() {
    super('D', 'D');
    preskod=this;
  }
  public static jpPreselectDoc getPres() {
    if (preskod==null) {
      preskod=new presKOD();
    }
    return preskod;
  }
  
  public boolean Validacija() {
    if (!super.Validacija()) return false;
    
    if (!lookupData.getlookupData().raLocate(dm.getSklad(), "CSKL", getSelRow())) {
      jrfCSKL.requestFocus();
      JOptionPane.showMessageDialog(getPreSelDialog(), "Nepoznato skladište!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    
    if (!dm.getSklad().getString("VRSKL").equals("K")) {
      jrfCSKL.requestFocus();
      JOptionPane.showMessageDialog(getPreSelDialog(), "Odabrano skladište nije komisijsko!", "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }
}