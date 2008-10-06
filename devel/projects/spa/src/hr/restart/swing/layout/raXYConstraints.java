/****license*****************************************************************
**   file: raXYConstraints.java
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
package hr.restart.swing.layout;

public class raXYConstraints  implements Cloneable, java.io.Serializable
{

    public raXYConstraints()
    {
        this(0, 0, 0, 0);
    }

    public raXYConstraints(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public raXYConstraints(java.awt.Rectangle r) {
      x = r.x;
      y = r.y;
      width = r.width;
      height = r.height;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int hashCode()
    {
        return x ^ y * 37 ^ width * 43 ^ height * 47;
    }

    public boolean equals(Object that)
    {
        if(that instanceof raXYConstraints)
        {
            raXYConstraints other = (raXYConstraints)that;
            return other.x == x && other.y == y && other.width == width && other.height == height;
        } else
        {
            return false;
        }
    }

    public Object clone()
    {
        return new raXYConstraints(x, y, width, height);
    }

    public String toString()
    {
        return String.valueOf(String.valueOf((new StringBuffer("raXYConstraints[")).append(x).append(",").append(y).append(",").append(width).append(",").append(height).append("]")));
    }

    int x;
    int y;
    int width;
    int height;
}