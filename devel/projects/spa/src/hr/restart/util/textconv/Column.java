/****license*****************************************************************
**   file: Column.java
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
 * 
 */
public abstract class Column implements IColumn {
  
  private String id;
  private int type;
  private String format;
  private Object value;
  private IColumnFormater formater;
  /* (non-Javadoc)
   * @see hr.restart.util.textconv.IColumn#getId()
   */
  public String getId() {
     return id;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.textconv.IColumn#setId(java.lang.String)
   */
  public void setId(String _id) {
    id = _id;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.textconv.IColumn#getType()
   */
  public int getType() {
    return type;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.textconv.IColumn#setType(int)
   */
  public void setType(int _type) {
    if (ColumnTypes.validateType(_type)) {
      type = _type;
    }
  }

  public void setType(String _type) {
    if (ColumnTypes.validateType(_type)) {
      type = ColumnTypes.getType(_type);
    }
  }

  
  /* (non-Javadoc)
   * @see hr.restart.util.textconv.IColumn#setFormat(java.lang.String)
   */
  public void setFormat(String _format) {
    format = _format;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.textconv.IColumn#getFormat()
   */
  public String getFormat() {
    return format;
  }

  /* (non-Javadoc)
   * @see hr.restart.util.textconv.IColumn#getValue()
   */
  public Object getValue() {
    return value;
  }

  public void setValue(Object v) {
    value = v;
  }
  
  public IColumnFormater getFormater() {
    if (formater == null) {
      formater = DefaultColumnFormater.getDefaultFormater();
    }
    return formater;
  }
  
  public void setFormater(IColumnFormater formater) {
    this.formater = formater;
  }
  
  public void copyColumn(Column col) {
    col.setFormat(getFormat());
    col.setFormater(getFormater());
    col.setId(getId());
    col.setType(getType());
    col.setValue(getValue());
  }
  
  public String toString() {
    return "id = "+getId()+"; type = "+getType()+"; format = "+getFormat()+"; value = "+getValue();    
  }
}
