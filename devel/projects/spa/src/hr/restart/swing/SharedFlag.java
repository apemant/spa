package hr.restart.swing;


public class SharedFlag {

  private boolean flag;
  public SharedFlag() {
  }
  
  public synchronized boolean request() {
    if (flag) return false;
    return flag = true;
  }
    
  public synchronized void release() {
    flag = false;
  }
}
