/****license*****************************************************************
**   file: FixedWidthColumn.java
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


/**
 * @author andrej
 */
public class FixedWidthColumn extends Column implements IFixedWidthColumn {
  private int bindex;
  private int eindex;

  /* (non-Javadoc)
   * @see hr.restart.util.textconv.IFixedWidthColumn#getBeginIndex()
   */
  public int getBeginIndex() {
    return bindex;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.textconv.IFixedWidthColumn#setBeginIndex(int)
   */
  public void setBeginIndex(int b) {
    bindex = b;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.textconv.IFixedWidthColumn#getEndIndex()
   */
  public int getEndIndex() {
    return eindex;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.textconv.IFixedWidthColumn#setEndIndex(int)
   */
  public void setEndIndex(int e) {
    eindex = e;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.textconv.IColumn#parse(hr.restart.util.textconv.ILine)
   */
  public boolean parse(ILine line) {
    String cont = line.getContent().substring((bindex-1),eindex);
    Object val = getFormater().applyFormat(cont, getFormat(), getType());
    if (val == null) return false;
    setValue(val);
    return true;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.textconv.IColumn#cloneColumn()
   */
  public IColumn cloneColumn() {
    FixedWidthColumn col = new FixedWidthColumn();
    copyColumn(col);
    col.setBeginIndex(getBeginIndex());
    col.setEndIndex(getEndIndex());
    return col;
  }

}
