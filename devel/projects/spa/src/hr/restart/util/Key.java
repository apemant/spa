package hr.restart.util;


public class Key {
  
  public final String s1, s2;
  public final int i;

  public Key(String s1, String s2) {
    this.s1 = s1;
    this.s2 = s2;
    i = 0;
  }
  
  public Key(String s1, String s2, int i) {
    this.s1 = s1;
    this.s2 = s2;
    this.i = i;
  }
  
  public Key(String s1, int i) {
    this.s1 = s1;
    this.s2 = null;
    this.i = i;
  }
  
  public boolean equals(Object obj) {
    if (!(obj instanceof Key)) return false;
    Key k = (Key) obj;
    if (i != k.i) return false;
    if (!s1.equals(k.s1)) return false;
    if (s2 == null) return k.s2 == null;
    return s2.equals(k.s2);
  }
  
  public int hashCode() {
    if (s2 == null) return i * 31 + s1.hashCode();
    return (i * 31 + s1.hashCode()) * 31 + s2.hashCode();
  }
}
