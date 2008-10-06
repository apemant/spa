/****license*****************************************************************
**   file: raTableUIModifier.java
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
package hr.restart.swing;
import java.awt.Color;
public class raTableUIModifier extends raTableModifier {
  private Color defColor;
  private Color altColor;
  private Color selColor;
  int step;
  public raTableUIModifier(
                  Color defaultColor,
                  Color alterColor,
                  Color selectedColor,
                  int stepdivider) {
    defColor = defaultColor;
    altColor = alterColor;
    selColor = selectedColor;
    step = stepdivider;
  }

  public raTableUIModifier(
                  Color defaultColor,
                  Color alterColor,
                  int stepdivider) {
    this(defaultColor,alterColor,null,stepdivider);
  }

  public raTableUIModifier(
                  Color alterColor,
                  int stepdivider) {
    this(null,alterColor,null,stepdivider);
  }

  public raTableUIModifier(Color alterColor) {
    this(null,alterColor,null,2);
  }

  public raTableUIModifier() {
    this(null,null,null,2);
  }

  public boolean doModify() {
    return true;
  }
  public void modify() {
    initColors();
    boolean isAlter = getRow()%step == 0;
    if (!isSelected()) {
      renderComponent.setBackground(isAlter?altColor:defColor);
    } else {
      renderComponent.setBackground(selColor);
    }
  }
  private void initColors() {
    if (defColor == null) defColor = getTable().getBackground();
    if (altColor == null) altColor = getModifiedBackground(0.9F);
    if (selColor == null) selColor = getTable().getSelectionBackground();
  }
  private Color getModifiedBackground(float factor) {
    Color bc = getTable().getBackground();
    float rgb[] = bc.getRGBColorComponents(null);
    float r = rgb[0]*factor;
    float g = rgb[1]*factor;
    float b = rgb[2]*factor;
    return new Color(r,g,b);
  }
}