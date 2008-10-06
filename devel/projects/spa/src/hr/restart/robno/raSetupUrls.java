/****license*****************************************************************
**   file: raSetupUrls.java
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
package hr.restart.robno;

public class raSetupUrls {

  public String localURL="tu je baba zakopala clienta";
  public String serverURL="tu je baba zakopala server";
  public String retlocalURL="tu je baba zakopala clienta";
  public String reteserverURL="tu je baba zakopala server";
  public String poruka="";

  public boolean setupURLs() {
    try {
        poruka="";
        hr.restart.baza.dM DM = hr.restart.baza.dM.getDataModule();
        DM.getReplurl().open();
        hr.restart.util.lookupData lD =  hr.restart.util.lookupData.getlookupData();
      if (lD.raLocate(DM.getReplurl(),new String[]{"RBR_URL"},new String[]{String.valueOf(3)})){
        DM.getReplurl().setString("URL",setupURL(DM.getReplurl().getString("URL"),localURL));
        DM.getReplurl().saveChanges();
        retlocalURL= DM.getReplurl().getString("URL");
      } else {
        poruka = "Ne valja !! Nema replurl gdje je RBR_URL = 3 tj. ne postoji URL client baze :((( ";
        return false;
      }

      if (lD.raLocate(DM.getReplurl(),new String[]{"RBR_URL"},new String[]{String.valueOf(4)})){
        DM.getReplurl().setString("URL",setupURL(DM.getReplurl().getString("URL"),serverURL));
        DM.getReplurl().saveChanges();
        reteserverURL= DM.getReplurl().getString("URL");
      } else {
        poruka="Ne valja !! Nema replurl gdje je RBR_URL = 4 tj. ne postoji URL server baze :((( ";
        return false;
      }
      return true;
    }
    catch (Exception ex) {
      poruka = "Nesto generalno ne valja !!!!!!!!!!";
      return false;
    }
  }

  private String  setupURL(String _oldAddressInParam,String _localURL) {
    int startSlash = 0;
    int stopSlash = 0;
    for (int i = 0;i< _oldAddressInParam.length();i++) {
      if (_oldAddressInParam.charAt(i)=='/' && _oldAddressInParam.charAt(i+1)=='/') {
        startSlash = i;
      } else   if (_oldAddressInParam.charAt(i)=='/' && _oldAddressInParam.charAt(i-1)!='/') {
        stopSlash = i;
        break;
      }
    }
    hr.restart.util.VarStr vs = new hr.restart.util.VarStr(
        _oldAddressInParam.substring(0,startSlash));
    vs.append("//").append(_localURL).append("/").append(_oldAddressInParam.substring(stopSlash+1,_oldAddressInParam.length()));

    return vs.toString();
}



}