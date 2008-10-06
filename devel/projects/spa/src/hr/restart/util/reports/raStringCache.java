/****license*****************************************************************
**   file: raStringCache.java
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
 * Created on Sep 30, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.util.reports;

import java.util.HashMap;


/**
 * @author abf
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class raStringCache {
  private HashMap cache;
  
  private String lastKey, lastDep;

  public raStringCache() {
    cache = new HashMap();
  }
  
  public void clear() {
    cache.clear();
  }
  
  public String getValue(String key, String dependency) {    
    lastKey = key;
    lastDep = dependency;
    StringPair sp = (StringPair) cache.get(key);
    if (sp == null || !sp.dep.equals(dependency)) return null;
    return sp.val;
  }
  
  public String putValue(String key, String dependency, String value) {
    cache.put(key, new StringPair(value, dependency));
    return value;
  }
  
  public String returnValue(String value) {
    return putValue(lastKey, lastDep, value);
  }
}

class StringPair {
  public String val, dep;
  public StringPair(String value, String dependency) {
    val = value;
    dep = dependency;
  }
}
