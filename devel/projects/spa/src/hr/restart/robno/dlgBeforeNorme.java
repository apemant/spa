/****license*****************************************************************
**   file: dlgBeforeNorme.java
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
package hr.restart.robno;

import hr.restart.swing.JraDialog;
import hr.restart.util.OKpanel;
import hr.restart.util.PreSelect;
import hr.restart.util.Valid;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import com.borland.dbswing.JdbTextField;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class dlgBeforeNorme extends JraDialog
{
    _Main main;
    BorderLayout borderLayout1 = new BorderLayout();
    private boolean selCanceled;
    Valid vl;
    hr.restart.util.raCommonClass rcc = hr.restart.util.raCommonClass.getraCommonClass();
    PreSelect pres = new PreSelect() {
      public void SetFokus() {
      }
        public boolean Validacija()
        {
//        this.pack();
        return true;
        }
    };
    hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();


    rapancart rpcart = new rapancart()
    {
      public void nextTofocus()
      {
      }
      public void metToDo_after_lookUp()
      {
      }
    };

    OKpanel okpanel = new OKpanel()
    {
      public void jBOK_actionPerformed()
      {
        okSelect();
      }
      public void jPrekid_actionPerformed()
      {
        cancelSelect();
      }
    };
  JPanel jpSel = new JPanel();
  XYLayout xYLayout1 = new XYLayout();
  JdbTextField jtfCART = new JdbTextField();

    public dlgBeforeNorme()
    {
      try
      {
        jbInit();
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    }

    private void okSelect()
    {
      if (!rpcart.getCART().equals(""))
      {
        pres.setSelDataSet(dm.getNorme());
        pres.setSelPanel(this.jpSel);
        pres.getSelRow().setInt("CART", new Integer(rpcart.getCART()).intValue());
        pres.getSelRow().post();
        pres.doSelect();
        this.hide();
        main.getStartFrame().showFrame("hr.restart.robno.frmNormativi", "Normativi");
      }
      else
        rpcart.jrfCART.requestFocus();
    }

    public void cancelSelect()
    {
      selCanceled=true;
      setVisible(false);
    }

    void this_keyPressed(KeyEvent e)
    {
      if (e.getKeyCode()==e.VK_F10)
      {
        okpanel.jBOK_actionPerformed();
        e.consume();
      }
      if (e.getKeyCode()==e.VK_ESCAPE)
      {
        rpcart.dm.getArtikli().open();
        if (rpcart.getCART().trim().equals(""))
        {
          okpanel.jPrekid_actionPerformed();
        }
        else
        {
          rcc.EnabDisabAll(this.rpcart, true);
          rpcart.jrfCART.requestFocus();
          rpcart.setCART();
        }
        e.consume();
      }
    }
  private void jbInit() throws Exception
  {
    this.addComponentListener(new java.awt.event.ComponentAdapter()
    {
      public void componentShown(ComponentEvent e)
      {
        this_componentShown(e);
      }
    }
    );
    this.getContentPane().setLayout(borderLayout1);
    this.addKeyListener(new java.awt.event.KeyAdapter()
    {
      public void keyPressed(KeyEvent e)
      {
        this_keyPressed(e);
      }
    });
    jpSel.setLayout(xYLayout1);
    jtfCART.setColumnName("CART");
    this.getContentPane().add(rpcart, BorderLayout.CENTER);
    this.getContentPane().add(okpanel, BorderLayout.SOUTH);
    jpSel.add(jtfCART,  new XYConstraints(15, 20, 100, -1));
    okpanel.setPreferredSize(new Dimension(138, 25));
    rpcart.setMode("DOH");
  }

  void this_componentShown(ComponentEvent e)
  {
    rpcart.setDefParam();
    rcc.EnabDisabAll(this.rpcart, true);
    rpcart.setCART();
  }
}
