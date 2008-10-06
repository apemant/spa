/****license*****************************************************************
**   file: Int2.java
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
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Int2 {
  public int one, two;
  public Int2() {
    one = two = 0;
  }

  public Int2(int one, int two) {
    this.one = one;
    this.two = two;
  }

  public Int2 plus(Int2 other) {
    return new Int2(one + other.one, two + other.two);
  }

  public Int2 add(Int2 other) {
    one += other.one;
    two += other.two;
    return this;
  }

  public void clear() {
    one = two = 0;
  }

  public String toString() {
    return "Int2 ("+one+","+two+")";
  }
}

