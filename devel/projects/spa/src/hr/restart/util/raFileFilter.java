/****license*****************************************************************
**   file: raFileFilter.java
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
package hr.restart.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raFileFilter extends FileFilter {
  String desc;
  String[] ext;

  public raFileFilter(String desc) {
    this.desc = desc;
    parseDescription();
  }

  private void parseDescription() {
    int fo = desc.indexOf('('), lo = desc.lastIndexOf('(');
    int fc = desc.indexOf(')'), lc = desc.indexOf(')');
    if (fo != lo || fc != lc || fc < fo)
      throw new IllegalArgumentException("Invalid FileFilter description");
    String[] filts = new VarStr(desc.substring(fo + 1, fc)).splitTrimmed(',');
    if (filts.length < 1)
      throw new IllegalArgumentException("Invalid FileFilter description");
    for (int i = 0; i < filts.length; i++)
      if (filts[i].length() < 2 || !filts[i].startsWith("*.") ||
          filts[i].indexOf('.') != filts[i].lastIndexOf('.'))
        throw new IllegalArgumentException("Invalid FileFilter description");

    ext = new String[filts.length];
    for (int i = 0; i < filts.length; i++)
      ext[i] = filts[i].substring(1);
  }

  public boolean accept(File f) {
    if (f.isDirectory()) return true;
    for (int i = 0; i < ext.length; i++)
      if (f.getName().toLowerCase().endsWith(ext[i]))
        return true;
    return false;
  }

  public String getDescription() {
    return desc;
  }
}

