/****license*****************************************************************
**   file: presOTP.java
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

/**
 * <p>Title: Robno poslovanje</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: REST-ART</p>
 * @author unascribed
 * @version 1.0
 */

public class presOTP extends jpPreselectDoc {
  static presOTP presotp;

  public void defaultMatDocAllowed(){
    isMatDocAllowed = true;
  }

  public void defaultMatDocAllowedifObrac(){
    isMatDocifObracAllowed = true;
  }


  public presOTP() {
    super('D', 'D');
    presotp=this;
  }
  public static jpPreselectDoc getPres() {
    if (presotp==null) {
      presotp=new presOTP();
    }
    return presotp;
  }
}