/****license*****************************************************************
**   file: TextFile.java
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
package hr.restart.sisfun;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class TextFile {
  public static final int READ = 1;
  public static final int WRITE = 2;
  public static final int APPEND = 2;

  private static String enc;
  static {
    setSystemEncoding();
  }

  public static void setEncoding(String encoding) {
    enc = encoding;
  }

  public static void setSystemEncoding() {
    enc = System.getProperty("file.encoding");
  }

  public static String getEncoding() {
    return enc;
  }

  private BufferedReader reader;
  private BufferedWriter writer;

  private TextFile(BufferedReader r, BufferedWriter w) {
    reader = r;
    writer = w;
  }

  public static TextFile open(String fname, int mode) {
    if (mode == READ)
      return read(fname);
    else if (mode == WRITE)
      return write(fname);
    else if (mode == APPEND)
      return append(fname);
    else return null;
  }

  public static TextFile read(InputStream is) {
    try {
      BufferedReader r = new BufferedReader(new InputStreamReader(is, enc));
      return new TextFile(r, null);
    } catch (Exception e) {
      return null;
    }
  }

  public static TextFile read(String fname) {
    try {
      BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(fname), enc));
      return new TextFile(r, null);
    } catch (Exception e) {
      return null;
    }
  }

  public static TextFile write(String fname) {
    try {
      BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fname), enc));
      return new TextFile(null, w);
    } catch (Exception e) {
      return null;
    }
  }

  public static TextFile append(String fname) {
    try {
      BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fname, true), enc));
      return new TextFile(null, w);
    } catch (Exception e) {
      return null;
    }
  }

  public static TextFile read(File fn) {
    try {
      BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(fn), enc));
      return new TextFile(r, null);
    } catch (Exception e) {
      System.out.println(e);
      return null;
    }
  }

  public static TextFile write(File fn) {
    try {
      BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fn), enc));
      return new TextFile(null, w);
    } catch (Exception e) {
      return null;
    }
  }
  
  public LinkedList lines() {
  	LinkedList ret = new LinkedList();
  	String line;
  	while ((line = in()) != null)
  		ret.add(line);
  	close();
  	return ret;
  }

  public String in() {
    try {
      return reader.readLine();
    } catch (IOException e) {
      return null;
    }
  }

  public TextFile out(String line) {
    if (line == null) return this;
    try {
      writer.write(line);
      writer.newLine();
      return this;
    } catch (IOException e) {
      return null;
    }
  }
  
  public TextFile out(LinkedList lines) {
  	if (lines == null || lines.size() == 0) return this;
  	try {
      for (Iterator i = lines.iterator(); i.hasNext(); ) {
        writer.write((String) i.next());
        writer.newLine();
      }
      return this;
    } catch (IOException e) {
      return null;
    }
  }

  public TextFile out(String[] lines) {
    if (lines == null) return this;
    try {
      for (int i = 0; i < lines.length; i++) {
        writer.write(lines[i]);
        writer.newLine();
      }
      return this;
    } catch (IOException e) {
      return null;
    }
  }

  public TextFile out(List lines) {
    if (lines == null) return this;
    try {
      Iterator i = lines.iterator();
      while (i.hasNext()) {
        writer.write((String) i.next());
        writer.newLine();
      }
      return this;
    } catch (IOException e) {
      return null;
    }
  }

  public TextFile check() {
    return this;
  }

  public void close() {
    try {
      if (reader != null) reader.close();
      if (writer != null) writer.close();
    } catch (IOException e) {}
  }
}
