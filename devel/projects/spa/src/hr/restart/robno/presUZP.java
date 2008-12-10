/****license*****************************************************************
**   file: presUZP.java
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
package hr.restart.robno;

public class presUZP extends jpPreselectDoc {
  static presUZP presuzp;

  public void defaultMatDocAllowed(){
    isMatDocAllowed = false;
  }

  public void defaultMatDocAllowedifObrac(){
    isMatDocifObracAllowed = false;
  }

  public presUZP(char cskl,char cpar) {
    super(cskl,cpar);
  }

  public presUZP() {
    super('N', 'D');
    presuzp=this;
  }
  
  public static jpPreselectDoc getPres() {
    if (presuzp==null) {
      presuzp=new presUZP();
    }
    return presuzp;
  }
}
