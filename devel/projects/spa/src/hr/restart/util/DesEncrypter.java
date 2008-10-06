/****license*****************************************************************
**   file: DesEncrypter.java
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
/**
 */
package hr.restart.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * <pre>
 * Copy pasted from http://www.javaalmanac.com/egs/javax.crypto/DesFile.html?l=rel on Oct 5, 2005
 * Modified for use with plain DES encryption 
 * Here is an example that uses the class
 * try {
 *    // Create encrypter/decrypter class
 *    DesStringEncrypter encrypter = new DesEncrypter("My Pass Phrase!");
 *
 *    // Encrypt
 *    String encrypted = encrypter.encrypt("Don't tell anybody!");
 *
 *    // Decrypt
 *    String decrypted = encrypter.decrypt(encrypted);
 * } catch (Exception e) {
 * }
 * </pre>
 * @plagiator andrej
 * 
 */
public class DesEncrypter implements Encrypter {
  Cipher ecipher;
  Cipher dcipher;

  // 8-byte Salt
//1.4  byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
//1.4      (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03 };

  // Iteration count
//1.4  int iterationCount = 19;

  byte[] buf = new byte[1024];
  
  public DesEncrypter(String passPhrase) {
    try {
      //ai: adapt passPhrase
      VarStr apassPhrase = new VarStr(passPhrase).removeWhitespace();
      if (apassPhrase.length()<8) apassPhrase = apassPhrase.append("XXXXXXXX");
      // Create the key
      Security.addProvider(new com.sun.crypto.provider.SunJCE());//1.3
      KeySpec keySpec = new DESKeySpec(apassPhrase.toString().getBytes());//, salt,iterationCount);
 //1.4     SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
//1.4          .generateSecret(keySpec);
      SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
      ecipher = Cipher.getInstance(key.getAlgorithm());
      dcipher = Cipher.getInstance(key.getAlgorithm());

      // Prepare the parameter to the ciphers
//1.4      AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
//1.4          iterationCount);

      // Create the ciphers
      ecipher.init(Cipher.ENCRYPT_MODE, key);//1.4 , paramSpec);
      dcipher.init(Cipher.DECRYPT_MODE, key);//1.4 , paramSpec);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String encrypt(String str) {
    try {
      // Encode the string into bytes using utf-8
      byte[] utf8 = str.getBytes("UTF8");

      // Encrypt
      byte[] enc = ecipher.doFinal(utf8);

      // Encode bytes to base64 to get a string
      return new sun.misc.BASE64Encoder().encode(enc);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public String decrypt(String str) {
    try {
      // Decode base64 to get bytes
      byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

      // Decrypt
      byte[] utf8 = dcipher.doFinal(dec);

      // Decode using utf-8
      return new String(utf8, "UTF8");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void encrypt(InputStream in, OutputStream out) {
    try {
      // Bytes written to out will be encrypted
      out = new CipherOutputStream(out, ecipher);

      // Read in the cleartext bytes and write to out to encrypt
      int numRead = 0;
      while ((numRead = in.read(buf)) >= 0) {
        out.write(buf, 0, numRead);
      }
      out.flush();
      out.close();
      in.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void decrypt(InputStream in, OutputStream out) {
    try {
      // Bytes read from in will be decrypted
      in = new CipherInputStream(in, dcipher);

      // Read in the decrypted bytes and write the cleartext to out
      int numRead = 0;
      while ((numRead = in.read(buf)) >= 0) {
        out.write(buf, 0, numRead);
      }
      out.flush();
      out.close();
      in.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  private void enordecrypt(File in, File out, boolean encrypt) {
    try {
      FileInputStream fi = new FileInputStream(in);
      FileOutputStream fo = new FileOutputStream(out);
      if (encrypt) {
        encrypt(fi,fo);
      } else {
        decrypt(fi,fo);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }    
  }
  /* (non-Javadoc)
   * @see hr.restart.util.Encrypter#encrypt(java.io.File, java.io.File)
   */
  public void encrypt(File in, File out) {
    enordecrypt(in,out,true);
  }
  /* (non-Javadoc)
   * @see hr.restart.util.Encrypter#decrypt(java.io.File, java.io.File)
   */
  public void decrypt(File in, File out) {
    enordecrypt(in,out,false);
  }
  public static void main(String[] args) {
    try {
      String action = args[0];
      DesEncrypter de = new DesEncrypter(args[1]);
      if (action.equalsIgnoreCase("es")) {
        System.out.println(de.encrypt(args[2]));
      } else if (action.equalsIgnoreCase("ds")) {
        System.out.println(de.decrypt(args[2]));
      } else if (action.equalsIgnoreCase("ef")) {
        de.encrypt(new File(args[2]), new File(args[3]));
      } else if (action.equalsIgnoreCase("df")) {
        de.decrypt(new File(args[2]), new File(args[3]));
      } else {
        throw new IllegalArgumentException("invalid switch");
      }
    } catch (Exception e) {
      System.out.println(e);
      System.out.println("DesEncrypter: Encript or decript file or string");
      System.out.println("usage: DesEncrypter es|ds|ef|df passkey file|string [resultfile]");
      System.out.println("  es - encrypt string");
      System.out.println("  ds - decrypt string");
      System.out.println("  ef - encrypt file");
      System.out.println("  df - decrypt file");
      System.out.println();
      System.out.println("  passkey - key for encryption/decryption");
      System.out.println();
      System.out.println("  string - string to encrypt/decrypt");
      System.out.println("  file - file to encrypt/decrypt");
      System.out.println("  resultfile - encrypted/decrypted file");
      e.printStackTrace();
    }
  }
}

