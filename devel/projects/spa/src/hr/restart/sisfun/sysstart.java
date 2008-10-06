/****license*****************************************************************
**   file: sysstart.java
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
package hr.restart.sisfun;


public class sysstart {
  frmSistem sisframe = new frmSistem() {
    public void hide() {
      System.exit(0);
    }
  };
  frmPassword fpass = new frmPassword();
  /**Construct the application*/
  public sysstart() {
//    sisframe.centerFrame(fpass,0,"Autorizacija");
//    fpass.show();
//    if (fpass.isLoginFailed()) {
//      System.exit(0);
//   } else {
      sisframe.ShowMe(false,"Sistemske funkcije");
//    }
  }
  /**Main method*/
  public static void main(String[] args) {
    hr.restart.util.startFrame.getStartFrame().raLookAndFeel();
//    hr.restart.util.raDataSetAdmin.configureDataSets(hr.restart.baza.dM.getDataModule());
    new sysstart();
//    hr.restart.util.raLoader.lazyLoad();
  }
}
