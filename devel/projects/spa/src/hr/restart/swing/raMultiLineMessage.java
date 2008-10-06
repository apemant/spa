/****license*****************************************************************
**   file: raMultiLineMessage.java
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
package hr.restart.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class raMultiLineMessage extends JPanel {
  public static final int DEF_WRAP = 100;
  private static final int DEF_ALIGN = SwingConstants.CENTER;

  private raMultiLineMessage() {
  }

  public raMultiLineMessage(String mess) {
    renderLines(createLines(mess, DEF_WRAP), DEF_ALIGN);
  }

  public raMultiLineMessage(String mess, int align) {
    renderLines(createLines(mess, DEF_WRAP), align);
  }

  public raMultiLineMessage(String mess, int align, int wrapAt) {
    renderLines(createLines(mess, wrapAt), align);
  }

  public raMultiLineMessage(String[] mess) {
    renderLines(mess, DEF_ALIGN);
  }

  public raMultiLineMessage(String[] mess, int align) {
    renderLines(mess, align);
  }

  private void renderLines(String[] lines, int align) {
    this.setLayout(new GridLayout(lines.length, 1));
    for (int i = 0; i < lines.length; i++)
      this.add(new JLabel(lines[i].trim(), align) {
        public Font getFont() {
          return raMultiLineMessage.this == null ? super.getFont() :
                 raMultiLineMessage.this.getFont();
        }
        public Color getForeground() {
          return raMultiLineMessage.this == null ? super.getForeground() :
                 raMultiLineMessage.this.getForeground();
        }
      });
  }

  private String[] createLines(String mess, int wrapAt) {
    char c;
    int last = 0, beg = 0;
    LinkedList lines = new LinkedList();
    for (int i = 0; i < mess.length(); i++) {
      c = mess.charAt(i);
      if (c == '\n') {
        lines.add(mess.substring(beg, i));
        last = beg = i + 1;
      } else if (i - beg > wrapAt) {
        if (last <= beg) last = i;
        lines.add(mess.substring(beg, last));
        beg = last;
      } else if (c == ' ') last = i;
    }
    lines.add(mess.substring(beg, mess.length()));
    return (String[]) lines.toArray(new String[lines.size()]);
  }
}
