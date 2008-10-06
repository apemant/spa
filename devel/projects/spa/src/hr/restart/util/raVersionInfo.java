/****license*****************************************************************
**   file: raVersionInfo.java
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
package hr.restart.util;

public abstract class raVersionInfo {
  public static String java_version = System.getProperty("java.version");
  public static boolean isJava1_3() {
    return java_version.startsWith("1.3");
  }
  public static boolean isJava1_4() {
    return java_version.startsWith("1.4");
  }

}