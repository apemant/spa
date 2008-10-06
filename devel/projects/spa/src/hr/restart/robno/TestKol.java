/****license*****************************************************************
**   file: TestKol.java
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

public class TestKol {
  String rezkol ="";
  private static TestKol TK;

  public static TestKol getTestKol(){
    if (TK == null) {
      TK = new TestKol();
    }
    return TK;
  }

  public boolean TestKol(int i) {
    rezkol=hr.restart.sisfun.frmParam.getParam("robno","rezkol");
    if(i == -1) {
        javax.swing.JOptionPane.showMessageDialog(null,
            "Koli\u010Dina je ve\u0107a nego koli\u010Dina na zalihi !",
            "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
        return false;
    }
    else if (i == -2 ){
      if (!rezkol.equals("N")) {
          javax.swing.JOptionPane.showMessageDialog(null,
              "Koristite rezervirane koli\u010Dine !",
              "Greška",javax.swing.JOptionPane.ERROR_MESSAGE);
          if (rezkol.equals("D")) return false;
      }
    }
    return true;
  }
}