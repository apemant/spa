/****license*****************************************************************
**   file: PartnerCache.java
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
package hr.restart.sk;

import hr.restart.baza.dM;

import java.util.HashMap;
import java.util.Map;

import com.borland.dx.dataset.DataSet;

public class PartnerCache {
  Map cache = null;
  
  public PartnerCache() {
    createCache(dM.getDataModule().getPartneri());
  }
  
  public PartnerCache(boolean kup) {
    createCache(kup ? dM.getDataModule().getPartneriKup() : 
      dM.getDataModule().getPartneriDob());
  }
  
  private void createCache(DataSet ds) {
    cache = new HashMap();
    ds.open();
    for (ds.first(); ds.inBounds(); ds.next())
      cache.put(new Integer(ds.getInt("CPAR")), new Data(ds));
  }
  
  public boolean isCached(int cpar) {
    if (cache == null)
      throw new IllegalArgumentException("Cache is disposed");
    Integer p = new Integer(cpar);
    return cache.containsKey(p);
  }
  
  public String getName(int cpar) {
    if (cache == null)
      throw new IllegalArgumentException("Cache is disposed");
    Integer p = new Integer(cpar);
    if (!cache.containsKey(p)) return null;
    return ((Data) cache.get(p)).getName();
  }
  
  public String getNameNotNull(int cpar) {
    String name = getName(cpar);
    return name == null ? "" : name;
  }
  
  public Data getData(int cpar) {
    if (cache == null)
      throw new IllegalArgumentException("Cache is disposed");
    Integer p = new Integer(cpar);
    if (!cache.containsKey(p)) return null;
    return (Data) cache.get(p);
  }
  
  public void dispose() {
    cache.clear();
    cache = null;
  }
  
  public static class Data {
    private String naziv;
    private String mb;
    private String oib;
    private short zup;
    private int pbr;
    private int agent;
    public Data(DataSet ds) {
      naziv = ds.getString("NAZPAR");
      mb = ds.getString("MB");
      oib = ds.getString("OIB");
      zup = ds.getShort("CZUP");
      pbr = ds.getInt("PBR");
      agent = ds.getInt("CAGENT");
    }
    
    public String getName() {
      return naziv;
    }
    
    public short getZup() {
      return zup;
    }
    
    public int getPbr() {
      return pbr;
    }
    
    public int getAgent() {
      return agent;
    }
    
    public String getMB() {
      return mb;
    }
    
    public String getOIB() {
      return oib;
    }
  }
}
