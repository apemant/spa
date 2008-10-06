/****license*****************************************************************
**   file: raFrameInterface.java
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

/**
 * Title:        Utilitys
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      RA
 * @author AI
 * @version 1.0
 */

public interface raFrameInterface {
    public void setVisible(boolean visible);
    public boolean isVisible();
    public void setTitle(String title);
    public String getTitle();
    public void setState(int state);
    public int getState();
    public void pack();
    public void validate();
    public void setSize(int width, int height);
    public java.awt.Dimension getSize();
    public void setLocation(int x,int y);
    public void show();
    public void hide();
    public java.awt.Container getContentPane();
}