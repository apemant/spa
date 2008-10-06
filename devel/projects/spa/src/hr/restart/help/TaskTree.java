package hr.restart.help;

import hr.restart.util.MenuTree;
import hr.restart.util.raImages;
import hr.restart.util.startFrame;
import hr.restart.util.MenuTree.treeLabel;
import hr.restart.util.MenuTree.treeOption;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.UIManager;

import com.l2fprod.common.swing.JTaskPane;
import com.l2fprod.common.swing.JTaskPaneGroup;

public class TaskTree extends JTaskPane implements raShortcutContainer {

	public TaskTree() {
		// TODO Auto-generated constructor stub
	}
	
	public void initFromTree(MenuTree tree) {
		for (int i = 0; i < tree.getRootnode().getChildCount(); i++) {
			addTask(tree.getRootnode().getChildAt(i), null);
		}
	}
	private void addTask(TreeNode child, JTaskPaneGroup tpg) {
		if (child.getChildCount() > 0) {
			JTaskPaneGroup jtpg = getJTaskPaneGroup(child);
			if (tpg == null) {
				add(jtpg);
			} else {
				tpg.add(jtpg);
			}
			for (int i = 0; i < child.getChildCount(); i++) {
				TreeNode ch = child.getChildAt(i);
//				JTaskPaneGroup ntpg = getJTaskPaneGroup(ch);
//				jtpg.add(ntpg);
				addTask(ch, jtpg);
			}
		} else {
			try {
				final treeOption trop = (treeOption)((DefaultMutableTreeNode)child).getUserObject();
				AbstractAction act;
				tpg.add(act = new AbstractAction() {
					public void actionPerformed(ActionEvent e) {
						trop.executeAction();
					}
				});
				act.putValue(Action.SMALL_ICON, raImages.getImageIcon(raImages.IMGOK));
				act.putValue(Action.NAME, child.toString());
			} catch (Exception e) {
				System.out.println("jebiga nije treeOption");
			}
		} 
		
	}
	
	private JTaskPaneGroup getJTaskPaneGroup(TreeNode child) {
		System.out.println("Dodajem nesto :: \n  class="+child.getClass().getName()
				+"\n cnt="+child.getChildCount());
		JTaskPaneGroup g = new JTaskPaneGroup();
		
		try {
			treeLabel jl = (treeLabel)((DefaultMutableTreeNode)child).getUserObject();
			g.setTitle(jl.getText());
			g.setIcon(jl.getIcon());
			jl.setComponentOrientation(g.getComponentOrientation());
		
		
			
		} catch (Exception e) {
			//e.printStackTrace();
			g.setTitle(child.toString());
		}
		g.setExpanded(false);
		return g;
	}


	
	
// ovo dolje je bezveze valjda-----------------------------------------------
	public void addMenuBar(JMenuBar bar, startFrame module) {
		JTaskPaneGroup jtpg = createJTaskPaneGroup(bar, module);
		add(jtpg);
		for (int i = 0; i < bar.getMenuCount(); i++) {
			addMenu(jtpg, bar.getMenu(i));
		}
	}
	
	private void addMenu(JTaskPaneGroup _jtpg, Component menucomponent) {
		JMenu menu = null;
		if (menucomponent instanceof JMenu) {
			menu = (JMenu) menucomponent;
		} else {
			System.out.println("UUUuuU nije Jmenu nego "+menucomponent);
		}
		if (menu != null && menu.getMenuComponentCount() > 0) {
			JTaskPaneGroup newgrp = new JTaskPaneGroup();
			newgrp.setTitle(menu.getToolTipText());
			newgrp.setExpanded(false);
			_jtpg.add(newgrp);
			for (int i = 0; i < menu.getMenuComponentCount(); i++) {
				addMenu(newgrp, menu.getMenuComponent(i));				
			}
			
		} else {
			JMenuItem jmenuitem = null;
			if (menucomponent instanceof JMenuItem) {
				jmenuitem = (JMenuItem) menucomponent;
			}
			if (jmenuitem == null) {
				System.out.println("******** Jegiba nekaj smo slejabi");
				System.out.println(menucomponent);
				return;
			}
			final JMenuItem fjmenuitem = jmenuitem;
			AbstractAction action = new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					((ActionListener)fjmenuitem.getListeners(ActionListener.class)[0]).actionPerformed(e);
				}
			};
		    action.putValue(Action.SMALL_ICON, raImages.getImageIcon(raImages.IMGTLEAF));
		    action.putValue(Action.NAME, jmenuitem.getText());
			_jtpg.add(action);
		}
		
	}

	private JTaskPaneGroup createJTaskPaneGroup(JMenuBar bar, startFrame module) {
		JTaskPaneGroup grp = new JTaskPaneGroup();
		grp.setTitle(module.getTitle());
		grp.setIcon(raImages.getModuleIcon(module));
		return grp;
	}

	public void addItem(raShortcutItem item) {
		// TODO Auto-generated method stub

	}

	public raShortcutItem getSelectedShortcutItem() {
		// TODO Auto-generated method stub
		return null;
	}

	public raShortcutContainer getShortcutTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isShortcutTarget() {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeItem(raShortcutItem item) {
		// TODO Auto-generated method stub

	}

}
