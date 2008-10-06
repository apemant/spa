package hr.restart.util;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingUtilities;

/**
 * Ne radi bash jer OSX drzi negdje referencu na menubar prikazan u defaultnom i ne zeli ga promijeniti
 * @author andrej
 *
 */
public class MenuBarWinListener extends WindowAdapter {
	raFrame fr;
	startFrame stolenBarFrame;
	public MenuBarWinListener(raFrame _fr) {
		fr = _fr;
	}
    public void windowActivated(WindowEvent e) {
    	
    		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
		    		stolenBarFrame = raLLFrames.getRaLLFrames().getMsgStartFrame();
		    		fr.setJMenuBar(stolenBarFrame.getJMenuBar());
		    		stolenBarFrame.setJMenuBar(null);
			}
    		});
    }
    public void windowDeactivated(WindowEvent e) {
    		if (stolenBarFrame == null) return;
		SwingUtilities.invokeLater(new Runnable() {
		public void run() {
			stolenBarFrame.setJMenuBar(fr.getJMenuBar());
	    		fr.setJMenuBar(null);
		}
		});
    }

}
