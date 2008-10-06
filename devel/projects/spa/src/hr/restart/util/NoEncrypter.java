/****license*****************************************************************
**   file: NoEncrypter.java
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
 * Created on Oct 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author andrej
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NoEncrypter implements Encrypter {

  /* (non-Javadoc)
   * @see hr.restart.util.Encrypter#encrypt(java.lang.String)
   */
  public String encrypt(String str) {
    return str;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.Encrypter#decrypt(java.lang.String)
   */
  public String decrypt(String str) {
    return str;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.Encrypter#encrypt(java.io.File, java.io.File)
   */
  public void encrypt(File in, File out) {
    try {
      FileOutputStream fo = new FileOutputStream(out);
      FileInputStream fi = new FileInputStream(in);
      int c;
      while ((c = fi.read())!=-1) {
        fo.write(c);
      }
      fi.close();
      fo.flush();
      fo.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /* (non-Javadoc)
   * @see hr.restart.util.Encrypter#decrypt(java.io.File, java.io.File)
   */
  public void decrypt(File in, File out) {
    encrypt(in,out);
  }


}
