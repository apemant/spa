/****license*****************************************************************
**   file: raGradientPainter.java
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
 * raPainter.java
 *
 * Created on 2003. listopad 30, 11:18
 */

package hr.restart.swing;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;

/**
 *
 * @author  andrej
 */
public class raGradientPainter {
  public Color col1;
  public Color col2;
  public Color col3;
  public Color col4;
  /** Creates a new instance of raPainter */
  public raGradientPainter() {
    col1 = new Color(255, 255, 255, 0);
    col2 = new Color(255, 255, 255, 128);
    col3 = new Color(0, 0, 0, 0);
    col4 = new Color(0, 0, 0, 64);    
  }
  public raGradientPainter(Color c1, Color c2, Color c3, Color c4) {
    col1 = c1;
    col2 = c2;
    col3 = c3;
    col4 = c4;
  }
  public raGradientPainter(Color baseColor1, Color baseColor2) {
    col1 = new Color(baseColor1.getRed(), baseColor1.getGreen(), baseColor1.getBlue(),0);
    col2 = new Color(baseColor1.getRed(), baseColor1.getGreen(), baseColor1.getBlue(),128);
    col3 = new Color(baseColor2.getRed(), baseColor2.getGreen(), baseColor2.getBlue(),0);
    col4 = new Color(baseColor2.getRed(), baseColor2.getGreen(), baseColor2.getBlue(),64);
  }
  public void paintGradient(Graphics g, JComponent c) {
    paintGradient(g, c, true, true);
  }
  public void paintGradient(Graphics g, JComponent c, boolean paintupper, boolean paintlower) {
    Graphics2D g2D = (Graphics2D) g;
    // paint upper gradient
    if (paintupper) {
      GradientPaint gradient1 = new GradientPaint(0.0f, (float) c.getHeight()/2, col1, 0.0f, 0.0f, col2);
      Rectangle rec1 = new Rectangle(0, 0, c.getWidth(), c.getHeight()/2);
      g2D.setPaint(gradient1);
      g2D.fill(rec1);
    }
    // paint lower gradient
    if (paintlower) {
      GradientPaint gradient2 = new GradientPaint(0.0f, (float) c.getHeight()/2, col3, 0.0f, (float) c.getHeight(), col4);
      Rectangle rec2 = new Rectangle(0, c.getHeight()/2, c.getWidth(), c.getHeight());
      g2D.setPaint(gradient2);
      g2D.fill(rec2);
    }
  }
}
