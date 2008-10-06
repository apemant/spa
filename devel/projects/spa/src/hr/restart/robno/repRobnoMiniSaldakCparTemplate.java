/****license*****************************************************************
**   file: repRobnoMiniSaldakCparTemplate.java
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

import hr.restart.util.reports.RaRobnoMiniSaldakDetail;
import hr.restart.util.reports.RaRobnoMiniSaldakSF0;
import hr.restart.util.reports.RaRobnoMiniSaldakSH0;
import hr.restart.util.reports.RaRobnoMiniSaldakSH1;
import hr.restart.util.reports.ReportModifier;
import hr.restart.util.reports.raReportSection;

/*
 * Created on 2004.11.08
 */

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class repRobnoMiniSaldakCparTemplate extends repRobnoMiniSaldakCparOrigTemplate {

  public raReportSection createSectionHeader0() {
    // TODO Auto-generated method stub
    return new RaRobnoMiniSaldakSH0(this);
  }
  public raReportSection createDetail() {
    // TODO Auto-generated method stub
    return new RaRobnoMiniSaldakDetail(this);
  }
  public raReportSection createSectionFooter0() {
    // TODO Auto-generated method stub
    return new RaRobnoMiniSaldakSF0(this);
  }
  public raReportSection createSectionHeader1() {
    // TODO Auto-generated method stub
    return new RaRobnoMiniSaldakSH1(this);
  }
  /**
   * 
   */
  public repRobnoMiniSaldakCparTemplate() {
    System.out.println("TEMPLATE KLASA _ repRobnoMiniSaldakCparTemplate");

    this.addReportModifier(new ReportModifier() {
      public void modify() {
        System.out.println("ja bi sad nesto modificira....");
        modifyThis();
      }
    });
  }
  /* (non-Javadoc)
   * @see hr.restart.robno.repRobnoMiniSaldakCparOrigTemplate#createReportHeader()
   */
  public raReportSection createReportHeader() {    
    // {CAPTION, FORCE_NEW, NEW_ROWCOL, KEEP_TOGETHER, VISIBLE, GROW, SHRINK, HEIGHT}
    return  new raReportSection(template.getModel(ep.REPORT_HEADER), new String[] {"Report Header", "", "", 
        "","","","","0"});
  }

  /* (non-Javadoc)
   * @see hr.restart.robno.repRobnoMiniSaldakCparOrigTemplate#createPageHeader()
   */
  public raReportSection createPageHeader() {
    // TODO Auto-generated method stub
    return new raReportSection(template.getModel(ep.PAGE_HEADER), new String[] {"", "", "0"});
  }

  /* (non-Javadoc)
   * @see hr.restart.robno.repRobnoMiniSaldakCparOrigTemplate#createPageFooter()
   */
  public raReportSection createPageFooter() {
    // TODO Auto-generated method stub
    return new raReportSection(template.getModel(ep.PAGE_FOOTER), new String[] {"", "", "0"});
  }

  /* (non-Javadoc)
   * @see hr.restart.robno.repRobnoMiniSaldakCparOrigTemplate#createReportFooter()
   */
  public raReportSection createReportFooter() {
    // TODO Auto-generated method stub
    return  new raReportSection(template.getModel(ep.REPORT_FOOTER), new String[] {"Report Header", "", "", 
        "","","","","0"});
  }

  private void modifyThis() {
    /*
      if ("L".equalsIgnoreCase(hr.restart.sisfun.frmParam.getParam("robno", "ispProzor"))) 
        sectHead0.restoreDefaults();
      else {
        raReportSection left = sectHead0.getView(LabelDuznik, Rectangle2);
        raReportSection rigt = sectHead0.getView(LabelVjerovnik, TextThirdLine);
        left.moveRightCm(9.5);
        rigt.moveLeftCm(9.5);
      }
    */
  }

}
