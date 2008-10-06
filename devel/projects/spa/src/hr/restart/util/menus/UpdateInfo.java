/****license*****************************************************************
**   file: UpdateInfo.java
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
 * Created on Dec 8, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.util.menus;

/**
 * @author andrej
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UpdateInfo {
  private String line;
  private String[] vals;
  private boolean updated;
  public UpdateInfo(String l, String[] v, boolean up) {
    line = l;
    vals = v;
    updated = up;
  }
  public String getLine() {
    return line;
  }
  public boolean isUpdated() {
    return updated;
  }
  public String[] getVals() {
    return vals;
  }
}
