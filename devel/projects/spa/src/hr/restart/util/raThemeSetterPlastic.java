/****license*****************************************************************
**   file: raThemeSetterPlastic.java
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

public class raThemeSetterPlastic implements raThemeSetter {
  public void set(String laf,String theme) throws Throwable {
    Class c_theme = Class.forName(theme);
    Object o_theme = c_theme.newInstance();
    Class c_laf = Class.forName(laf);
    Object o_laf =  c_laf.newInstance();
    java.lang.reflect.Method lfmet = o_laf.getClass()
                                   .getMethod("setMyCurrentTheme",new Class[] {Class.forName("com.jgoodies.plaf.plastic.PlasticTheme")});
    lfmet.invoke(o_laf,new Object[] {o_theme});
    javax.swing.UIManager.setLookAndFeel((javax.swing.LookAndFeel)o_laf);
    //my shit
  //  com.jgoodies.clearlook.ClearLookManager.setPolicy(new com.jgoodies.clearlook.DefaultClearLookPolicy());
//    com.jgoodies.clearlook.ClearLookManager.setMode(com.jgoodies.clearlook.ClearLookMode.ON);
  }
}