/****license*****************************************************************
**   file: raElixirPropertyValues.java
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
package hr.restart.util.reports;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public interface raElixirPropertyValues {
  String YES = "Yes";
  String NO = "No";
  
  String PORTRAIT = "Portrait";
  String LANDSCAPE = "Landscape";

  String BLACK = "Black";
  String BLUE = "Blue";
  String CYAN = "Cyan";
  String DARK_GRAY = "Dark Gray";
  String GRAY = "Gray";
  String LIGHT_GRAY = "Light Gray";
  String GREEN = "Green";
  String MAGENTA = "Magenta";
  String ORANGE = "Orange";
  String PINK = "Pink";
  String RED = "Red";
  String WHITE = "White";
  String YELLOW = "Yellow";

  String NORMAL = "Normal";
  String SOLID = "Solid";
  String TRANSPARENT = "Transparent";

  String BOLD = "Bold";
  String PLAIN = "Plain";

  String LEFT = "Left";
  String CENTER = "Center";
  String RIGHT = "Right";

  String TOP_LEFT = "Top Left";
  String TOP_RIGHT = "Top Right";
  String BOTTOM_LEFT = "Bottom Left";
  String BOTTOM_RIGHT = "Bottom Right";

  String CLIP = "Clip";
  String ZOOM = "Zoom";
  String STRETCH = "Stretch";

  String EACH_VALUE = "Each Value";
  String DESCENDING = "Descending";

  String BEFORE = "Before";
  String AFTER = "After";
  
  String OVER_GROUP = "Over Group";

  String ARIAL = "Arial";
  String GEORGIA = "Georgia";
  String LUCIDA_BRIGHT = "Lucida Bright";
  String TIMES_NEW_ROMAN = "Times New Roman";

  String[] REPORT_TEMPLATE_DEFS = new String[] {"Report Template", "hr_HR", "", "All Pages",
     "All Pages", "8640", "No", "No", "5", "5", "1.50"};

  String[] PAGE_SETUP_DEFS = new String[] {"1440", "1440", "1440", "1440", "Portrait", "A4",
     "11894", "16834", "", "1", "0", "0", "Across, then Down"};

  String[] PARAMETERS_DEFS = new String[] {};

  String[] WATERMARK_DEFS = new String[] {"", "", "Clip", "Center", "No", "1440", "1440",
     "2880", "2880", "Transparent", "Black", "1 pt"};

  String[] SECTIONS_DEFS = new String[] {};

  String[] SECTION_DEFS = new String[] {"", "Ascending", "No", "No", "", "1", "No"};

  String[] REPORT_HEADER_DEFS = new String[] {"Report Header", "None", "None", "Yes", "No",
     "No", "No", "2880"};

  String[] PAGE_HEADER_DEFS = new String[] {"Page Header", "Yes", "1440"};

  String[] SECTION_HEADER_DEFS = new String[] {"Section Header0", "None", "None", "Yes",
     "No", "Yes", "No", "No", "720"};

  String[] DETAIL_DEFS = new String[] {"Detail", "None", "None", "Yes", "Yes", "Yes", "Yes",
     "2880"};

  String[] SECTION_FOOTER_DEFS = new String[] {"Section Footer0", "None", "None", "Yes",
     "No", "Yes", "No", "720"};

  String[] PAGE_FOOTER_DEFS = new String[] {"Page Footer", "Yes", "1440"};

  String[] REPORT_FOOTER_DEFS = new String[] {"Report Footer", "None", "None", "Yes", "No",
     "No", "No", "2880"};

  String[] TEXT_DEFS = new String[] {"", "No", "Template Locale", "None", "Yes", "No", "No",
     "No", "0", "0", "1440", "360", "Transparent", "White", "Transparent", "Black", "1 pt", "Black",
     "Serif", "10", "Plain", "No", "No", "Left", "Yes"};

  String[] LABEL_DEFS = new String[] {"", "Yes", "0", "0", "1440", "360", "Transparent",
     "White", "Transparent", "Black", "1 pt", "Black", "Serif", "10", "Plain", "No", "No", "Left"};

  String[] LINE_DEFS = new String[] {"\\", "Yes", "0", "144", "2880", "144", "Solid",
     "Black", "1 pt"};

  String[] RECTANGLE_DEFS = new String[] {"Yes", "360", "360", "1440", "1440", "Normal",
     "White", "Solid", "Black", "1 pt"};

  String[] IMAGE_DEFS = new String[] {"", "", "Clip", "Center", "Yes", "1440", "1440",
     "1440", "1440", "Transparent", "Black", "1 pt"};

  String[][] MODEL_DEFS = {REPORT_TEMPLATE_DEFS, PAGE_SETUP_DEFS, PARAMETERS_DEFS,
             WATERMARK_DEFS, SECTIONS_DEFS, SECTION_DEFS, REPORT_HEADER_DEFS, PAGE_HEADER_DEFS,
             SECTION_HEADER_DEFS, DETAIL_DEFS, SECTION_FOOTER_DEFS, PAGE_FOOTER_DEFS,
             REPORT_FOOTER_DEFS, TEXT_DEFS, LABEL_DEFS, LINE_DEFS, RECTANGLE_DEFS,
             IMAGE_DEFS};
}
