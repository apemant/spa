/****license*****************************************************************
**   file: raColors.java
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
 * raColors.java
 *
 * Created on 2003. listopad 29, 10:00
 */

package hr.restart.swing;

import java.awt.Color;
/**
 *
 * @author  karsten
 */
public abstract class raColors {
  
  public static Color blue = new Color(195,212,232);
  
  public static Color green = new Color(200,222,200);
  
  public static Color red = new Color(222,200,200);
  
  public static Color grey = new Color(164,164,164);
  
  public static Color yellow = new Color(249,225,131);

  public static Color lightYellow = new Color(255,255,179);

  public static Color lightRed = new Color(255,191,191);
  
  
  public raColors() {
    initColors();
  }
  
  private void initColors() {
    /**@todo: iz colors.properties izvuci boje, a spremi ih preko tool ekrana sa colorchooserom */
  }
  
}
