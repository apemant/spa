/****license*****************************************************************
**   file: raElixirProperties.java
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

public interface raElixirProperties {
  String CHILD_COUNTER = "Child Counter";
  String CHILD_NUMBER = "Child Number";
  
  String MODEL_NAME = "Model Name";
  String NAME = "Name";
  String CAPTION = "Caption";
  String LOCALE = "Locale";
  String RECORD_SOURCE = "Record Source";
  String PAGEHEADER = "Page Header";
  String PAGEFOOTER = "Page Footer";
  String WIDTH = "Width";
  String GRID_ON = "Grid On";
  String SNAP = "Snap to grid";
  String GRID_X = "Grid X";
  String GRID_Y = "Grid Y";
  String VERSION = "Version";
  String MODIFIED = "Last Modified";
  String TEMPLATE_PATH = "Template Path";
  String TEXT_COUNT = "Text_Count";
  String LABEL_COUNT = "Label_Count";
  String IMAGE_COUNT = "Image_Count";
  String PAGE_BREAK_COUNT = "Page_Break_Count";
  String LINE_COUNT = "Line_Count";
  String CUSTOM_COUNT = "Custom_Component_Count";
  String RECTANGLE_COUNT = "Rectangle_Count";
  String SVG_COUNT = "Svg_Count";
  String SUBREPORT_COUNT = "SubReport_Count";
  String CHART_COUNT = "Chart_Count";
  String PARAMETER_COUNT = "Parameter_Count";
  String COMPONENT_COUNT = "Cmponent_Count";
  String TOP = "Top";
  String BOTTOM = "Bottom";
  String LEFT = "Left";
  String RIGHT = "Right";
  String ORIENTATION = "Orientation";
  String SIZE = "Size";
  String HEIGHT = "Height";
  String SOURCE = "Source";
  String COLUMNS = "Number of Columns";
  String ROW_SPACING = "Row Spacing";
  String COLUMN_SPACING = "Column Spacing";
  String COLUMN_LAYOUT = "Column Layout";
  String FORCE_NEW = "Force New Page";
  String NEW_ROWCOL = "New Row Or Col";
  String KEEP_TOGETHER = "Keep Together";
  String VISIBLE = "Visible";
  String GROW = "Can Grow";
  String SHRINK = "Can Shrink";
  String REPEAT = "Repeat Section";
  String CONTROL_SOURCE = "Control Source";
  String RUNNING_SUM = "Running Sum";
  String FORMAT = "Format";
  String HIDE_DUPLICATES = "Hide Duplicates";
  String BACK_STYLE = "Back Style";
  String BACK_COLOR = "Back Color";
  String BORDER_STYLE = "Border Style";
  String BORDER_COLOR = "Border Color";
  String BORDER_WIDTH = "Border Width";
  String FONT_COLOR = "Font Color";
  String FONT_NAME = "Font Name";
  String FONT_SIZE = "Font Size";
  String FONT_WEIGHT = "Font Weight";
  String ITALIC = "Font Italic";
  String UNDERLINE = "Font Underline";
  String ALIGN = "Text Align";
  String LOCK = "Lock";
  String WRAP = "Auto Wrap";
  String LINE_SLANT = "Line Slant";
  String FIELD = "Field";
  String SORT_ORDER = "Sort Order";
  String GROUP_HEADER = "Group Header";
  String GROUP_FOOTER = "Group Footer";
  String GROUP_ON = "Group On";
  String GROUP_INTERVAL = "Group Interval";
  String PICTURE = "Picture";
  String PICTURE_TYPE = "Picture Type";
  String SIZE_MODE = "Size Mode";
  String ALIGNMENT = "Picture Alignment";

  String[] REPORT_TEMPLATE_PROPS = {CAPTION, LOCALE, RECORD_SOURCE, PAGEHEADER,
           PAGEFOOTER, WIDTH, GRID_ON, SNAP, GRID_X, GRID_Y, VERSION};

  String[] REPORT_TEMPLATE_HPROPS = {TEMPLATE_PATH, TEXT_COUNT, LABEL_COUNT, IMAGE_COUNT,
           PAGE_BREAK_COUNT, LINE_COUNT, CUSTOM_COUNT, RECTANGLE_COUNT, SVG_COUNT,
           SUBREPORT_COUNT, CHART_COUNT, PARAMETER_COUNT, COMPONENT_COUNT};

  String[] PAGE_SETUP_PROPS = {TOP, BOTTOM, LEFT, RIGHT, ORIENTATION, SIZE, WIDTH,
           HEIGHT, SOURCE, COLUMNS, ROW_SPACING, COLUMN_SPACING, COLUMN_LAYOUT};

  String[] PARAMETERS_PROPS = {};

  String[] WATERMARK_PROPS = {PICTURE, PICTURE_TYPE, SIZE_MODE, ALIGNMENT, VISIBLE,
           LEFT, TOP, WIDTH, HEIGHT, BORDER_STYLE, BORDER_COLOR, BORDER_WIDTH};

  String[] SECTIONS_PROPS = {};

  String[] SECTION_PROPS = {FIELD, SORT_ORDER, GROUP_HEADER, GROUP_FOOTER,
           GROUP_ON, GROUP_INTERVAL, KEEP_TOGETHER};

  String[] REPORT_HEADER_PROPS = {CAPTION, FORCE_NEW, NEW_ROWCOL, KEEP_TOGETHER,
           VISIBLE, GROW, SHRINK, HEIGHT};

  String[] PAGE_HEADER_PROPS = {CAPTION, VISIBLE, HEIGHT};

  String[] SECTION_HEADER_PROPS = {CAPTION, FORCE_NEW, NEW_ROWCOL, KEEP_TOGETHER,
           VISIBLE, GROW, SHRINK, REPEAT, HEIGHT};

  String[] DETAIL_PROPS = {CAPTION, FORCE_NEW, NEW_ROWCOL, KEEP_TOGETHER,
           VISIBLE, GROW, SHRINK, HEIGHT};

  String[] SECTION_FOOTER_PROPS = {CAPTION, FORCE_NEW, NEW_ROWCOL, KEEP_TOGETHER,
           VISIBLE, GROW, SHRINK, HEIGHT};

  String[] PAGE_FOOTER_PROPS = {CAPTION, VISIBLE, HEIGHT};

  String[] REPORT_FOOTER_PROPS = {CAPTION, FORCE_NEW, NEW_ROWCOL, KEEP_TOGETHER,
           VISIBLE, GROW, SHRINK, HEIGHT};

  String[] TEXT_PROPS = {CONTROL_SOURCE, RUNNING_SUM, LOCALE, FORMAT, VISIBLE,
           HIDE_DUPLICATES, GROW, SHRINK, LEFT, TOP, WIDTH, HEIGHT, BACK_STYLE,
           BACK_COLOR, BORDER_STYLE, BORDER_COLOR, BORDER_WIDTH, FONT_COLOR,
           FONT_NAME, FONT_SIZE, FONT_WEIGHT, ITALIC, UNDERLINE, ALIGN, WRAP};

  String[] LABEL_PROPS = {CAPTION, VISIBLE, LEFT, TOP, WIDTH, HEIGHT, BACK_STYLE,
           BACK_COLOR, BORDER_STYLE, BORDER_COLOR, BORDER_WIDTH, FONT_COLOR,
           FONT_NAME, FONT_SIZE, FONT_WEIGHT, ITALIC, UNDERLINE, ALIGN};

  String[] LINE_PROPS = {LINE_SLANT, VISIBLE, LEFT, TOP, WIDTH, HEIGHT,
           BORDER_STYLE, BORDER_COLOR, BORDER_WIDTH};

  String[] RECTANGLE_PROPS = {VISIBLE, LEFT, TOP, WIDTH, HEIGHT, BACK_STYLE,
           BACK_COLOR, BORDER_STYLE, BORDER_COLOR, BORDER_WIDTH};

  String[] IMAGE_PROPS = {PICTURE, PICTURE_TYPE, SIZE_MODE, ALIGNMENT, VISIBLE,
           LEFT, TOP, WIDTH, HEIGHT, BORDER_STYLE, BORDER_COLOR, BORDER_WIDTH};

  String REPORT_TEMPLATE = "Report Template";
  String PAGE_SETUP = "Page Setup";
  String PARAMETERS = "Parameters";
  String WATERMARK = "Watermark";
  String SECTIONS = "Sections";
  String SECTION = "Section";
  String REPORT_HEADER = "Report Header";
  String PAGE_HEADER = "Page Header";
  String SECTION_HEADER = "Section Header";
  String DETAIL = "Detail";
  String SECTION_FOOTER = "Section Footer";
  String PAGE_FOOTER = "Page Footer";
  String REPORT_FOOTER = "Report Footer";
  String TEXT = "Text";
  String LABEL = "Label";
  String LINE = "Line";
  String RECTANGLE = "Rectangle";
  String IMAGE = "Image";

  String[] MODELS = {REPORT_TEMPLATE, PAGE_SETUP, PARAMETERS, WATERMARK, SECTIONS, SECTION,
           REPORT_HEADER, PAGE_HEADER, SECTION_HEADER, DETAIL, SECTION_FOOTER,
           PAGE_FOOTER, REPORT_FOOTER, TEXT, LABEL, LINE, RECTANGLE, IMAGE};

  String[] MODEL_NAMES = {"REPORT_TEMPLATE", "PAGE_SETUP", "PARAMETERS", "WATERMARK", "SECTIONS",
           "SECTION", "REPORT_HEADER", "PAGE_HEADER", "SECTION_HEADER", "DETAIL", "SECTION_FOOTER",
           "PAGE_FOOTER", "REPORT_FOOTER", "TEXT", "LABEL", "LINE", "RECTANGLE", "IMAGE"};

  String[][] MODEL_PROPS = {REPORT_TEMPLATE_PROPS, PAGE_SETUP_PROPS, PARAMETERS_PROPS,
             WATERMARK_PROPS, SECTIONS_PROPS, SECTION_PROPS, REPORT_HEADER_PROPS, PAGE_HEADER_PROPS,
             SECTION_HEADER_PROPS, DETAIL_PROPS, SECTION_FOOTER_PROPS, PAGE_FOOTER_PROPS,
             REPORT_FOOTER_PROPS, TEXT_PROPS, LABEL_PROPS, LINE_PROPS, RECTANGLE_PROPS,
             IMAGE_PROPS};
}
