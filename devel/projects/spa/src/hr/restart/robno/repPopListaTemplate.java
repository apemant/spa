/****license*****************************************************************
**   file: repPopListaTemplate.java
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

import hr.restart.util.reports.raPopListaDetail;
import hr.restart.util.reports.raPopListaSectionHeader1;
import hr.restart.util.reports.raReportSection;

public class repPopListaTemplate extends repPopListaOrigTemplate {

  public repPopListaTemplate() {
    System.out.println("template");
  }

  public raReportSection createPageHeader() {
    return new raReportSection(template.getModel(ep.PAGE_HEADER), new String[] {"", "", "0"});
  }

  public raReportSection createSectionHeader1(){
    return new raPopListaSectionHeader1(this);
  }

  public raReportSection createDetail(){
    return new raPopListaDetail(this);
  }
}