/****license*****************************************************************
**   file: ILine.java
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

import java.util.Map;

/**
 * @author andrej
 * This interface represents line of formated text file
 */
public interface ILine {
  /**
   * Number of line in file.
   * @return number of line in file
   */
  public int getLineNum();
  /**
   * Number of line in file.
   * @param number number of line in file
   */
  public void setLineNum(int number);
  
  /**
   * Unparsed content of line
   * @return content of line
   */
  public String getContent();
  /**
   * Content of line
   * @param content content of line
   */
  public void setContent(String content);
  
  /**
   * Adds column definition
   * @param col column definition
   */
  public void addColumn(IColumn col);
  /**
   * sets list of columns (like iterating addColumn(IColumn) command) 
   * @param colMap
   */
  public void setColumns(Map colMap);
  
  /**
   * gets all colums defined in this line
   * @return
   */
  public Map getColumns();
  /**
   * Retrieves column definition
   * @param id String identifier of column
   * @return
   */
  public IColumn getColumn(String id);
  /**
   * Retrieves column value eg.
   * <code>return getColumn().getValue()</code>
   * @param id String identifier of column
   * @return value
   */
  public Object getColumnValue(String id);
  
  /**
   * Verifies line eg. first line of file is header line 
   * <code>return getLineNum() == 0</code> 
   * @return
   */
  public boolean verify();
  /**
   * Parses line content against columns, also clones this line and returns new ILine with parsed values
   * after this command <code>getColumnValue(String)</code> should return some meaningfull values 
   * @return success
   */
  public ILine parse();
  
}
