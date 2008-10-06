/****license*****************************************************************
**   file: raDBURLParser.java
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
 * raDBURLParser.java
 *
 * Created on 2004. lipanj 09, 12:54
 */

package hr.restart.util;
import java.util.StringTokenizer;
/**
 * Parsira url za jdbc konekciju na bazu i vraca dijelove
 * @author  andrej
 */
public class raDBURLParser {
  private String url;
  private String protocol;
  private String type;
  private String host;
  private String path;
  public raDBURLParser(String _url) {
    url = _url;
    StringTokenizer tokens = new StringTokenizer(url,":");
    protocol = tokens.nextToken();
    type = tokens.nextToken();
    String other = tokens.nextToken();
    while (tokens.hasMoreTokens()) other=other+":"+tokens.nextToken();
    StringTokenizer othertokens = new StringTokenizer(other,"/");
    host = othertokens.nextToken();
    path = othertokens.nextToken();
    while (othertokens.hasMoreTokens()) path=path+"/"+othertokens.nextToken();
  }
  
  public String toString() {
    return "dbURLparser("+url+"): \n"
          +"protocol = "+protocol+"\n"
          +"type = "+type+"\n"
          +"host = "+host+"\n"
          +"path = "+path+"\n";
  }
  
  /**
   * Getter for property url.
   * @return Value of property url.
   */
  public java.lang.String getUrl() {
    return url;
  }  
    
  /**
   * Getter for property protocol.
   * Prvi dio url-a npr. jdbc
   * @return Value of property protocol.
   */
  public java.lang.String getProtocol() {
    return protocol;
  }
    
  /**
   * Getter for property type.
   * Drugi dio url-a npr. interbase
   * @return Value of property type.
   */
  public java.lang.String getType() {
    return type;
  }
    
  /**
   * Getter for property host.
   * treci dio url-a npr. localhost ili 161.53.200.99
   * @return Value of property host.
   */
  public java.lang.String getHost() {
    return host;
  }
   
  /**
   * Getter for property path.
   * zadnji dio url-a npr. /home/interbase/bazara.gdb ili c:\restart\bazara.gdb
   * @return Value of property path.
   */
  public java.lang.String getPath() {
    return path;
  }
  
  
}
