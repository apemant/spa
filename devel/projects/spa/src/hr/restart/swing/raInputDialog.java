package hr.restart.swing;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JPanel;


public class raInputDialog extends raOptionDialog {
	private JPanel content;
	private String title;
	private Object ret;
	public raInputDialog() {
		init();
	}
	public boolean show(Container parent) {
		return show(parent, content, title);
	}
  public boolean show(Container parent, JPanel content, String title) {
    JPanel main = new JPanel(new BorderLayout());
    main.add(content);
    main.add(okp, BorderLayout.SOUTH);
    return super.show(parent, main, title);
  }
  public Object getValue() {
  	return ret;
  }
  protected void init() {
		//
	}
  public void setContent(JPanel content) {
  	this.content = content;
  }
  public void setTitle(String title) {
  	this.title = title;
  	if (win != null) win.setTitle(title);
  }
  public void setValue(Object val) {
  	ret = val;
  }
  public void setParams(String title, JPanel content, Object val) {
  	this.title = title;
  	this.content = content;
  	ret = val;
  }
  
  protected boolean checkOk() {
    return super.checkOk();
  }
}
