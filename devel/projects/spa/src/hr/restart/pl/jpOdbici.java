/****license*****************************************************************
**   file: jpOdbici.java
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
package hr.restart.pl;

import hr.restart.util.raMatPodaci;

import java.awt.BorderLayout;

import javax.swing.JPanel;


public class jpOdbici extends JPanel {

  jpOdbici2 jp2;

   public jpOdbici(raMatPodaci f) {
    try {
      jp2 = new jpOdbici2(f);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
//  public jpOdbici(frmOdbici f) {
//    try {
//      jp2 = new jpOdbici2(f);
//      jbInit();
//    }
//    catch(Exception e) {
//      e.printStackTrace();
//    }
//  }

  private void jbInit() throws Exception {
    this.setLayout(new BorderLayout());
    this.add(jp2, BorderLayout.SOUTH);
  }
}