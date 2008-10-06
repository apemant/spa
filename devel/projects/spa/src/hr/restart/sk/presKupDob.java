/****license*****************************************************************
**   file: presKupDob.java
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
package hr.restart.sk;



public class presKupDob extends presZbirnoGodina {
  public presKupDob() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  protected boolean isBoth() {
    return false;
  }
  
/*  protected boolean isDirect() {
    return false;
  } */

  protected void afterPartner(boolean succ) {
    if (succ) {
      jpzg.init();
      jpzg.focusNav();
    }
  }

  private void jbInit() throws Exception {
    jpp.setPartnerOboje();
    jpp.shiftLabel();
    jcbPS.setText(" Prikaz svih partnera ");
  }
}
