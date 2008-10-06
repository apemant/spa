/****license*****************************************************************
**   file: TimeTrack.java
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

/**
 * Title:        Utilitys
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      RA
 * @author AI
 * @version 1.0
 */
public class TimeTrack {
  static long lnTime=0;
  boolean jelshuti=false;
  public TimeTrack(boolean cjelshuti) {
    jelshuti=cjelshuti;
  }
  public void Start() {
    if (jelshuti) return;
    lnTime=System.currentTimeMillis();
  }
  public void Start(String str) {
    if (jelshuti) return;
    System.out.println(str);
    lnTime=System.currentTimeMillis();
  }
  public void ReStart(String str) {
    if (jelshuti) return;
    Stop();
    System.out.println(str);
    lnTime=System.currentTimeMillis();
  }
  public void Stop() {
    if (jelshuti) return;
    System.out.print("Gotovo za ");
    System.out.print(System.currentTimeMillis()-lnTime);
    System.out.println(" milisekundi");
  }
}
