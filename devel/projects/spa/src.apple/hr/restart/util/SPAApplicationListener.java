package hr.restart.util;

import hr.restart.start;

import com.apple.eawt.ApplicationEvent;
import com.apple.eawt.ApplicationListener;

public class SPAApplicationListener implements ApplicationListener {

	public void handleAbout(ApplicationEvent ev) {
		if (ev.isHandled()) return;
		raLLFrames.getRaLLFrames().getMsgStartFrame().About();
		ev.setHandled(true);
	}

	public void handleOpenApplication(ApplicationEvent arg0) {
		
		
	}

	public void handleOpenFile(ApplicationEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void handlePreferences(ApplicationEvent ev) {
		if (ev.isHandled()) return;
		raLLFrames.getRaLLFrames().getMsgStartFrame().SystemPar();
		ev.setHandled(true);
	}

	public void handlePrintFile(ApplicationEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void handleQuit(ApplicationEvent ev) {
		if (ev.isHandled()) return;
		start.exit();
		//ev.setHandled(true);
	}

	public void handleReOpenApplication(ApplicationEvent arg0) {
		// TODO Auto-generated method stub
		
	}
		

}
