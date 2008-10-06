/****license*****************************************************************
**   file: Assert.java
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
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class Assert {

  private Assert() {
  }

  public static void is(boolean condition) {
    if (!condition)
      throw new AssertionNotTrueException();
  }

  public static void is(boolean condition, String message) {
    if (!condition)
      throw new AssertionNotTrueException(message);
  }

  public static void equal(Object o1, Object o2) {
    if (!o1.equals(o2))
      throw new AssertionNotEqualException();
  }

  public static void equal(Object o1, Object o2, String s) {
    if (!o1.equals(o2))
      throw new AssertionNotEqualException(s);
  }

  public static void equal(int i1, int i2) {
    if (i1 != i2)
      throw new AssertionNotEqualException();
  }

  public static void equal(int i1, int i2, String s) {
    if (i1 != i2)
      throw new AssertionNotEqualException(s);
  }

  public static void equal(short s1, short s2) {
    if (s1 != s2)
      throw new AssertionNotEqualException();
  }

  public static void equal(short s1, short s2, String s) {
    if (s1 != s2)
      throw new AssertionNotEqualException(s);
  }

  public static void equal(long l1, long l2) {
    if (l1 != l2)
      throw new AssertionNotEqualException();
  }

  public static void equal(long l1, long l2, String s) {
    if (l1 != l2)
      throw new AssertionNotEqualException(s);
  }

  public static void equal(float f1, float f2) {
    if (f1 != f2)
      throw new AssertionNotEqualException();
  }

  public static void equal(float f1, float f2, String s) {
    if (f1 != f2)
      throw new AssertionNotEqualException(s);
  }

  public static void equal(double d1, double d2) {
    if (d1 != d2)
      throw new AssertionNotEqualException();
  }

  public static void equal(double d1, double d2, String s) {
    if (d1 != d2)
      throw new AssertionNotEqualException(s);
  }

  public static void notNull(Object o) {
    if (o == null)
      throw new AssertionNullException();
  }

  public static void notNull(Object o, String s) {
    if (o == null)
      throw new AssertionNullException(s);
  }
}
