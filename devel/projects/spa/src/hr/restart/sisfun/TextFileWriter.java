/****license*****************************************************************
**   file: TextFileWriter.java
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

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class TextFileWriter extends BufferedWriter {

  public TextFileWriter(String fname) throws FileNotFoundException {
    super(new OutputStreamWriter(new FileOutputStream(fname)));
  }

  public TextFileWriter(String fname, boolean append) throws FileNotFoundException {
    super(new OutputStreamWriter(new FileOutputStream(fname, append)));
  }

  public TextFileWriter(String fname, String enc)
      throws FileNotFoundException, UnsupportedEncodingException {
    super(new OutputStreamWriter(new FileOutputStream(fname), enc));
  }

  public TextFileWriter(String fname, boolean append, String enc)
      throws FileNotFoundException, UnsupportedEncodingException {
    super(new OutputStreamWriter(new FileOutputStream(fname, append), enc));
  }

  public TextFileWriter out(String line) {
    try {
      write(line);
      newLine();
      return this;
    } catch (IOException e) {
      return null;
    }
  }

  public TextFileWriter out(String[] lines) {
    try {
      for (int i = 0; i < lines.length; i++) {
        write(lines[i]);
        newLine();
      }
      return this;
    } catch (IOException e) {
      return null;
    }
  }

  public TextFileWriter out(List lines) {
    try {
      Iterator i = lines.iterator();
      while (i.hasNext()) {
        write((String) i.next());
        newLine();
      }
      return this;
    } catch (IOException e) {
      return null;
    }
  }

  public void close() {
    try {
      super.close();
    } catch (IOException e) {}
  }

  public TextFileWriter check() {
    return this;
  }
}

