/****license*****************************************************************
**   file: FileParser.java
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
 * Created on Dec 28, 2004
 */
package hr.restart.util.textconv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

/**
 * @author andrej
 * Parses file against given ILine definitions 
 */
public class FileParser {
  private File file;
  private List linedefs = new LinkedList();
  private TreeMap linesparsed = new TreeMap();
  private String charset = null;
  public FileParser() {
  }
  
  public FileParser(File _file) {
    setFile(_file);
  }
  
  public void addLineDef(ILine line) {
    linedefs.add(line);
  }
  public void setLineDefs(List ldefs) {
    linedefs = ldefs;
  }
  public File getFile() {
    return file;
  }
  
  public void setFile(File file) {
    this.file = file;
  }
  
  public void parse() {
    try {
      InputStreamReader isr;
      if (getCharset() == null) {
        isr = new InputStreamReader(new FileInputStream(file));
      } else {
        isr = new InputStreamReader(new FileInputStream(file),getCharset());
      }
      BufferedReader reader = new BufferedReader(isr);
      String fileline = reader.readLine();
      int linenum = 0;
      while (fileline != null) {
        for (Iterator iter = linedefs.iterator(); iter.hasNext();) {
          ILine line = (ILine) iter.next();
          line.setContent(fileline);
          if (line.verify()) {
            ILine parsedline = line.parse();
            linenum++;
            parsedline.setLineNum(linenum);
            linesparsed.put(new Integer(parsedline.getLineNum()),parsedline);
            break;
          }
        }
        fileline = reader.readLine();
      }      
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public TreeMap getLinesparsed() {
    return linesparsed;
  }

  public String getCharset() {
    return charset;
  }

  public void setCharset(String charset) {
    this.charset = charset;
  }

}
