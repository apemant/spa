/****license*****************************************************************
**   file: raDateRange.java
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
package hr.restart.swing;


public class raDateRange {
  raDatePopup from, to;

  private raDateRange() {
  }

  public raDateRange(JraTextField from, JraTextField to) {
    if (from.getRaPopup() != null && from.getRaPopup() instanceof raDatePopup) {
      this.from = (raDatePopup) from.getRaPopup();
      this.from.addToRange(to, true);
    } else this.from = new raDatePopup(from, to, true);

//    System.out.println("adding "+to.getColumnName());
    if (to.getRaPopup() != null && to.getRaPopup() instanceof raDatePopup) {
//      System.out.println("found existing");
      this.to = (raDatePopup) to.getRaPopup();
      this.to.addToRange(from, false);
    } else this.to = new raDatePopup(to, from, false);
  }

  public raDatePopup getFromPopup() {
    return from;
  }

  public raDatePopup getToPopup() {
    return to;
  }
}
