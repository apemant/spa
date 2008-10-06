/****license*****************************************************************
**   file: repObrKamata2Template.java
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
 * Created on 2005.04.08
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.sk;

import hr.restart.util.reports.raReportElement;


/**
 * @author abf
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class repObrKamata2Template extends repObrKamataTemplate {

  private void switchNeighbors(raReportElement left, raReportElement right) {
    long oldleft = left.getLeft();
    left.setLeft(right.getLeft() + right.getWidth() - left.getWidth());
    right.setLeft(oldleft);
  }
  /**
   * 
   */
  public repObrKamata2Template() {
    switchNeighbors(LabelBroj_uplate.defaultAlterer(), LabelDospjeva.defaultAlterer());
    switchNeighbors(TextBROJUPL.defaultAlterer(), TextDATDOSP.defaultAlterer());
  }
}
