/****license*****************************************************************
**   file: Encrypter.java
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
 */
package hr.restart.util;

import java.io.File;

/**
 * @author andrej
 */
public interface Encrypter {
  /**
   * Encrypts string
   * @param str String to be encrypted
   * @return encrypted String
   */
  public abstract String encrypt(String str);

  /**
   * Decrypts string
   * @param str String to be decrypted
   * @return decrypted String
   */
  public abstract String decrypt(String str);

  /**
   * Encrypts file and writes to another file
   * @param in file to be encrypted
   * @param out encrypted file
   */
  public abstract void encrypt(File in, File out);

  /**
   * Decrypts file and writes to another file
   * @param in file to be decrypted
   * @param out decrypted file
   */
  public abstract void decrypt(File in, File out);
}