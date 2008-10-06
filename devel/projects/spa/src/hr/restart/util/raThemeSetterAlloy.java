/****license*****************************************************************
**   file: raThemeSetterAlloy.java
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

public class raThemeSetterAlloy implements raThemeSetter {
  public void set(String laf, String theme) throws Throwable {
    Class c_theme = Class.forName(theme);
    Object o_theme = c_theme.newInstance();
//    com.incors.plaf.alloy.AlloyTheme a_theme = (com.incors.plaf.alloy.AlloyTheme)o_theme;
//    javax.swing.UIManager.setLookAndFeel(new com.incors.plaf.alloy.AlloyLookAndFeel(a_theme));
  }
}