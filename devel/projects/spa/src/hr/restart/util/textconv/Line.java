/****license*****************************************************************
**   file: Line.java
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
 * Created on Dec 27, 2004
 */
package hr.restart.util.textconv;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author andrej
 * Implementation of ILine interface
 */
public class Line implements ILine {
  private static Logger log = Logger.getLogger(Line.class);
  private int linenum;
  private String content;
  private Map columns = new HashMap();
  private ILineVerifier lineVerifier;
  /* (non-Javadoc)
   * @see hr.restart.util.textconv.ILine#getLineNum()
   */
  public int getLineNum() {
    return linenum;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.textconv.ILine#setLineNum(int)
   */
  public void setLineNum(int number) {
    linenum = number;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.textconv.ILine#getContent()
   */
  public String getContent() {
    return content;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.textconv.ILine#setContent(java.lang.String)
   */
  public void setContent(String _content) {
    content = _content;
    if (log.isDebugEnabled()) {
      log.debug("Content set to "+_content);
    }
  }

  /* (non-Javadoc)
   * @see hr.restart.util.textconv.ILine#addColumn(hr.restart.util.textconv.IColumn)
   */
  public void addColumn(IColumn col) {
    columns.put(col.getId(),col);
  }
  /* (non-Javadoc)
   * @see hr.restart.util.textconv.ILine#setColumns(java.util.Map)
   */
  public void setColumns(Map colMap) {
    columns = colMap;
  }
  /* (non-Javadoc)
   * @see hr.restart.util.textconv.ILine#getColumn(java.lang.String)
   */
  public IColumn getColumn(String id) {
    return (IColumn)columns.get(id);
  }

  /* (non-Javadoc)
   * @see hr.restart.util.textconv.ILine#getColumnValue(java.lang.String)
   */
  public Object getColumnValue(String id) {
    try {
      return getColumn(id).getValue();
    } catch (Exception e) {
      throw new RuntimeException(getContent()+ " is wrong :"+e.getMessage());
    }
  }
  
  public ILineVerifier getLineVerifier() {
    return lineVerifier;
  }
  
  public void setLineVerifier(ILineVerifier lineVerifier) {
    this.lineVerifier = lineVerifier;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.textconv.ILine#verify()
   */
  public boolean verify() {
    if (getLineVerifier() != null) {
      return getLineVerifier().verify(this);
    } else return true;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.textconv.ILine#parse()
   */
  public ILine parse() {
    Line line = new Line();
    line.setContent(getContent());
    line.setLineNum(getLineNum());
    for (Iterator iter = columns.keySet().iterator(); iter.hasNext();) {
      String key = (String) iter.next();
      //if (!getColumn(key).parse(this)) return null;
      line.addColumn(getColumn(key).cloneColumn());
      if (!line.getColumn(key).parse(line)) return null;
    }
    return line;
  }
  public String toString() {
    String ret = "Line "+getLineNum()+":";
    for (Iterator iter = columns.keySet().iterator(); iter.hasNext();) {
      Object key = iter.next();
      ret = ret + "\n Column: key = "+key+"\n         column = "+columns.get(key);
    }
    return ret;
  }

  public Map getColumns() {
    return columns;
  }
}
