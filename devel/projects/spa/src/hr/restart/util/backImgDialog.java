/****license*****************************************************************
**   file: backImgDialog.java
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

import hr.restart.mainFrame;
import hr.restart.swing.JraDialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class backImgDialog  extends JraDialog implements okFrame, loadFrame {
  JFileChooser filechooser = new JFileChooser();
  OKpanel okp = new OKpanel() {
    public void jPrekid_actionPerformed() {
      action_jPrekid();
    }
    public void jBOK_actionPerformed() {
      action_jBOK();
    }
  };
  JPanel jp = new JPanel(new BorderLayout());
  ImageIcon previewImg = null;
  JPanel jpchoose = new JPanel(new BorderLayout());
  JLabel jlfile = new JLabel();
  JPanel jpfile = new JPanel(new BorderLayout());
  JButton jbclear = new JButton();
  JButton jbfile = new JButton();
  JPanel jpPreviewImage = new JPanel() {
    public void paint(java.awt.Graphics g) {
      super.paint(g);
      if (previewImg!=null) g.drawImage(previewImg.getImage(),0,0,getWidth(),getHeight(),null);
    }
  };

  public backImgDialog() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void action_jPrekid() {
  }

  public void action_jBOK() {
    hr.restart.mainFrame.getMainFrame().setBackgroundImage(jlfile.getText());
  }

  public OKpanel getOKpanel() {
    return okp;
  }
  private void jbInit() throws Exception {
    setTitle("Wallpaper");
    jlfile.setText("");
    jbfile.setText("...");
    jbfile.setPreferredSize(new Dimension(21,21));
    jbclear.setIcon(raImages.getImageIcon(raImages.IMGX));
    jbclear.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbclear_actionPerformed(e);
      }
    });
    jbclear.setPreferredSize(new Dimension(21,21));
    jbfile.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbfile_actionPerformed(e);
      }
    });
    this.getContentPane().add(jp, BorderLayout.CENTER);
    jpfile.add(jbfile,BorderLayout.WEST);
    jpfile.add(jbclear,BorderLayout.EAST);
    jp.add(jpchoose, BorderLayout.SOUTH);
    jpchoose.add(jlfile, BorderLayout.CENTER);
    jpchoose.add(jpfile,  BorderLayout.EAST);
//    filechooser.setAccessory(new fcPreviewArea(filechooser));
    filechooser.setAccessory(new FilePreviewer(filechooser));
    jp.add(jpPreviewImage, BorderLayout.CENTER);
  }
  public void reload() {
    previewImg = mainFrame.getMainFrame().imgBackground;
    jlfile.setText(IntParam.VratiSadrzajTaga("backimage"));
  }
  void jbfile_actionPerformed(ActionEvent e) {
    int result = filechooser.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      if (!setImage(filechooser.getSelectedFile().getAbsolutePath())) {
        JOptionPane.showMessageDialog(this,"Odabranu datoteku nije moguce prikazati kao sliku!");
      }
    }
  }
  void jbclear_actionPerformed(ActionEvent e) {
    setImage(null);
  }


  public boolean doSaving() {
    return !IntParam.VratiSadrzajTaga("backimage").equals(jlfile.getText());
  }

  boolean setImage(String filename) {
    try {
      if (filename == null) {
        previewImg = null;
        jlfile.setText("");
        jpPreviewImage.paint(jpPreviewImage.getGraphics());
        return true;
      }
      try {
        previewImg = new ImageIcon(filename);
      }
      catch (Exception ex) {
        System.out.println("previewImg = new ImageIcon(filename);");
        ex.printStackTrace();
      }
      if (previewImg.getImageLoadStatus() == MediaTracker.COMPLETE) {
        jlfile.setText(filename);
        jpPreviewImage.paint(jpPreviewImage.getGraphics());
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      return false;
    }
  }

  class fcPreviewArea extends JPanel implements java.beans.PropertyChangeListener {
    ImageIcon img = null;
    public fcPreviewArea(JFileChooser FC) {
      setPreferredSize(new Dimension(100, 50));
      FC.addPropertyChangeListener(this);
    }
    public void propertyChange(java.beans.PropertyChangeEvent ev) {
      String prop = ev.getPropertyName();
      if(prop == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) {
        String filep = ((java.io.File)ev.getNewValue()).getAbsolutePath();
        try {
          img = new ImageIcon(filep);
        }
        catch (Exception ex) {
          System.out.println("img = new ImageIcon(filep);");
          ex.printStackTrace();
        }
      }
    }
    public void paint(Graphics g) {
      super.paint(g);
      if (img!=null&&g!=null) {
        Image im = img.getImage().getScaledInstance(this.getWidth(),-1,Image.SCALE_FAST);
        g.drawImage(im,0,0,-1,-1,null);
      }
    }
  }

  class FilePreviewer extends JComponent implements java.beans.PropertyChangeListener {
    ImageIcon thumbnail = null;
    java.io.File f = null;

    public FilePreviewer(JFileChooser fc) {
      setPreferredSize(new Dimension(100, 50));
      fc.addPropertyChangeListener(this);
      setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
      loadImage();
    }

    public void loadImage() {
      if(f != null) {
        ImageIcon tmpIcon = null;
        try {
          tmpIcon = new ImageIcon(f.getPath());
        }
        catch (Exception ex) {
          System.out.println("tmpIcon = new ImageIcon(f.getPath());");
          ex.printStackTrace();
        }
        if(tmpIcon.getIconWidth() > 90) {
          try {
            thumbnail = new ImageIcon(
                tmpIcon.getImage().getScaledInstance(90, -1, Image.SCALE_DEFAULT));
          }
          catch (Exception ex) {
            System.out.println("thumbnail");
            ex.printStackTrace();
          }
        } else {
          thumbnail = tmpIcon;
        }
      }
    }

    public void propertyChange(java.beans.PropertyChangeEvent e) {
      String prop = e.getPropertyName();
      if(prop == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) {
        f = (java.io.File) e.getNewValue();
        if(isShowing()) {
          loadImage();
          repaint();
        }
      }
    }

    public void paint(Graphics g) {
      super.paint(g);
      if(thumbnail == null) {
        loadImage();
      }
      if(thumbnail != null) {
        int x = getWidth()/2 - thumbnail.getIconWidth()/2;
        int y = getHeight()/2 - thumbnail.getIconHeight()/2;
        if(y < 0) {
          y = 0;
        }

        if(x < 5) {
          x = 5;
        }
        thumbnail.paintIcon(this, g, x, y);
      }
    }
  }

}