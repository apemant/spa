/****license*****************************************************************
**   file: manipString.java
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
import javax.swing.SwingUtilities;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class manipString {

  static manipString mS ;

  static manipString getmanipString(){
    if (mS==null) mS = new manipString();
    return mS;
  }

  public String getString(String pero){
    return String.valueOf(getChars(pero,15,' '));
  }

  public String getString(String pero,boolean right){
    if (right) {
      return String.valueOf(getChars(pero,15,' ',SwingUtilities.RIGHT));
    }
    else {
      return String.valueOf(getChars(pero,15,' '));
    }
  }

  public char[] getChars(String pero){
    return getChars(pero,15,' ');
  }

  public String getString(String pero,int kvanto){
    return String.valueOf(getChars(pero,kvanto,' '));
  }

  public char[] getChars(String pero,int kvanto){
    return getChars(pero,kvanto,' ');
  }

  public String getString(String pero,int kvanto,char midle){
    return String.valueOf(getChars(pero,kvanto,midle));
  }

  public char[] getChars(String pero,int kvanto,char midle){
    return getChars(pero,kvanto,midle,SwingUtilities.LEFT);
  }

  public String getString(String pero,int kvanto,char midle,int justifaj){
    return String.valueOf(getChars(pero,kvanto,midle,justifaj));
  }

  public char[] getChars(String pero,int kvanto,char midle,int justifaj){

    char[] charsi = new char[kvanto];
    int duzina = pero.length();
    int counter = 0;
    char tmpChar;
    for (int i = 0 ; i<kvanto;i++){
      if (i<duzina) {
        if (justifaj == SwingUtilities.RIGHT){
          charsi[kvanto-1-i] = pero.charAt(duzina-1-i);
        }
        else {
          charsi[i] = pero.charAt(i);
        }
      }
      else {
        if (justifaj == SwingUtilities.RIGHT){
          charsi[kvanto-1-i]=midle;
        } else charsi[i]=midle;
      }
    }
    return charsi;
  }
  }