/****license*****************************************************************
**   file: raLayUtil.java
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
package hr.restart.swing.layout;
import hr.restart.util.raCommonClass;

import java.awt.Container;
import java.util.LinkedList;

public class raLayUtil {

  protected raLayUtil() {
  }

  public static void parseXYLayout(Container target) {
//    if (target.getLayout() instanceof raXYLayout) return;
    LinkedList firstList = raCommonClass.getraCommonClass().getComponentTree(target);
    for (int i=0;i<firstList.size();i++) {
      if (firstList.get(i) instanceof java.awt.Container) {
        Container cont = (Container)firstList.get(i);
//        if (cont.getLayout() instanceof raXYLayout) return;
        parseXYLay(cont);
      }
    }
    parseXYLay(target);
  }
  private static void parseXYLay(Container cont) {
    if (cont.getLayout() instanceof com.borland.jbcl.layout.XYLayout ) {
//      com.borland.jbcl.layout.XYLayout jbxylay = (com.borland.jbcl.layout.XYLayout)cont.getLayout();
//    if (cont instanceof javax.swing.JPanel) {
      raXYLayout raXYLay = new raXYLayout();
      raXYLay.parseXYLayout(cont.getLayout(),cont);
      cont.setLayout(raXYLay);
    }
  }
  private static void parseNulLayout(Container target) {
  }
}