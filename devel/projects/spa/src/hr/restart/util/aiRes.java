/****license*****************************************************************
**   file: aiRes.java
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


public class aiRes extends java.util.ListResourceBundle {
  static final Object[][] contents = new String[][]{
        {"jmiStartFrExit","Zatvori aplikaciju"},
	{ "jmStartFrWindow", "Prozori" },
	{ "jmiStartFrWinMin", "Minimiziraj" },
        { "jmiStartFrWinArng", "Uredi" },
        { "jmiStartFrWinCls", "Zatvori sve" },
        { "jmStartFr1menu","Opcije" },
        { "jmStartFrHelp","Pomo\u0107" },
        { "jmiStartFrHlpHelp","Upute" },
        { "jmiStartFrHlpAbout","O aplikaciji" },
        { "jmStartFrSys","Sistem"},
        { "jmiStartFrSysLnF","Izgled" },
        { "jmiStartFrSysMode","Na\u010Din rada"},
        { "jmiStartFrSysUsr","Korisni\u010Dki parametri"},
        { "jmiStartFrSysApp","Parametri aplikacije"},
        { "jmiStartFrSysFun","Sistemski alati"},
        { "jmiStartFrGetKnj","Odabir knjigovodstva za rad"}
        };
  public Object[][] getContents() {
    return contents;
  }
}