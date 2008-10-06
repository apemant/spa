/****license*****************************************************************
**   file: MenuValue.java
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
 * Created on Nov 11, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.util.menus;

/**
 * @author andrej
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MenuValue {
  private String cmenu;
  private int sort;
  private String parentcmenu;
  private String menutype;
  private String method;
  private String text;
  /**
   * 
   */
  public MenuValue() {
  }

  /**
   * @return Returns the cmenu.
   */
  public String getCmenu() {
    return cmenu;
  }
  /**
   * Also sets text from MenuFactory.getMenuProperties();
   * @param cmenu The cmenu to set.
   */
  public void setCmenu(String cmenu) {
    this.cmenu = cmenu;
    setText(MenuFactory.getMenuProperties().getProperty(cmenu, cmenu));
  }
  /**
   * @return Returns the menutype.
   */
  public String getMenutype() {
    return menutype;
  }
  /**
   * @param menutype The menutype to set.
   */
  public void setMenutype(String menutype) {
    this.menutype = menutype;
  }
  /**
   * @return Returns the method.
   */
  public String getMethod() {
    return method;
  }
  /**
   * @param method The method to set.
   */
  public void setMethod(String method) {
    this.method = method;
  }
  /**
   * @return Returns the parentcmenu.
   */
  public String getParentcmenu() {
    return parentcmenu;
  }
  /**
   * @param parentcmenu The parentcmenu to set.
   */
  public void setParentcmenu(String parentcmenu) {
    this.parentcmenu = parentcmenu;
  }
  /**
   * @return Returns the sort.
   */
  public int getSort() {
    return sort;
  }
  /**
   * @param sort The sort to set.
   */
  public void setSort(int sort) {
    this.sort = sort;
  }

  /**
   * @return Returns the text.
   */
  public String getText() {
    return text;
  }
  /**
   * @param text The text to set.
   */
  public void setText(String text) {
    this.text = text;
  }
  
  public String toString() {
    return "text:"+getText()+" cmenu:"+getCmenu()+" sort:"+getSort()+" parentcmenu:"+getParentcmenu()+" menutype:"+getMenutype()+" method:"+getMethod();
  }
}
