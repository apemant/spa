package hr.restart.util;

import com.borland.dx.dataset.AccessEvent;
import com.borland.dx.dataset.AccessListener;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.dataset.NavigationListener;


abstract public class NavigationAdapter implements NavigationListener, AccessListener {
  private boolean eventsEnabled = true;
  
  public NavigationAdapter() {
  }
  
  public void install(DataSet ds) {
    try {
      ds.addNavigationListener(this);
      ds.addAccessListener(this);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }
  
  public void uninstall(DataSet ds) {
    try {
      ds.removeNavigationListener(this);
      ds.removeAccessListener(this);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }

  public void navigated(NavigationEvent e) {
    if (eventsEnabled)
      navigated((DataSet) e.getSource());
  }
  
  public void accessChange(AccessEvent e) {
    if (e.getID() == 2 && e.getReason() == 8)
      eventsEnabled = false;
    else if (e.getID() == 1 && e.getReason() == 2)
      eventsEnabled = true;
  }
  
  abstract public void navigated(DataSet source);
}
