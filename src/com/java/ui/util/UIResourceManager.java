package com.java.ui.util;

import com.java.ui.componentc.ImageBorder;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class UIResourceManager {
    private static final Map<String, Border> borderMap = new HashMap<String, Border>();

    private static final Map<String, Color> colorMap = new HashMap<String, Color>();

    private static final Map<String, Icon> iconMap = new HashMap<String, Icon>();

    private static final Map<String, Image> imageMap = new HashMap<String, Image>();
    public static final String KEY_CALENDAR_BACKGROUND_IMAGE = "CalendarBackgroundImage";
    public static final String KEY_CALENDAR_CLOSE_IMAGE = "CalendarCloseImage";
    public static final String KEY_CALENDAR_CLOSE_PRESSED_IMAGE = "CalendarClosePressedImage";
    public static final String KEY_CALENDAR_CLOSE_ROLLOVER_IMAGE = "CalendarCloseRolloverImage";
    public static final String KEY_CALENDAR_COMBO_BOX_BUTTON_IMAGE = "CalendarComboBoxButtonImage";
    public static final String KEY_CALENDAR_COMBO_BOX_BUTTON_PRESSED_IMAGE = "CalendarComboBoxButtonPressedImage";
    public static final String KEY_CALENDAR_COMBO_BOX_BUTTON_ROLLOVER_IMAGE = "CalendarComboBoxButtonRolloverImage";
    public static final String KEY_CALENDAR_DAY_BACKGROUND = "CalendarDayBackground";
    public static final String KEY_CALENDAR_DAY_BORDER = "CalendarDayBorder";
    public static final String KEY_CALENDAR_DAY_SELECTED_BORDER = "CalendarTodaySelectedBorder";
    public static final String KEY_CALENDAR_NEXT_MONTH_IMAGE = "CalendarNextMonthImage";
    public static final String KEY_CALENDAR_NEXT_MONTH_PRESSED_IMAGE = "CalendarNextMonthPressedImage";
    public static final String KEY_CALENDAR_NEXT_MONTH_ROLLOVER_IMAGE = "CalendarNextMonthRolloverImage";
    public static final String KEY_CALENDAR_NEXT_YEAR_IMAGE = "CalendarNextYearImage";
    public static final String KEY_CALENDAR_NEXT_YEAR_PRESSED_IMAGE = "CalendarNextYearPressedImage";
    public static final String KEY_CALENDAR_NEXT_YEAR_ROLLOVER_IMAGE = "CalendarNextYearRolloverImage";
    public static final String KEY_CALENDAR_PREVIOUS_MONTH_IMAGE = "CalendarPreviousMonthImage";
    public static final String KEY_CALENDAR_PREVIOUS_MONTH_PRESSED_IMAGE = "CalendarPreviousMonthPressedImage";
    public static final String KEY_CALENDAR_PREVIOUS_MONTH_ROLLOVER_IMAGE = "CalendarPreviousMonthRolloverImage";
    public static final String KEY_CALENDAR_PREVIOUS_YEAR_IMAGE = "CalendarPreviousYearImage";
    public static final String KEY_CALENDAR_PREVIOUS_YEAR_PRESSED_IMAGE = "CalendarPreviousYearPressedImage";
    public static final String KEY_CALENDAR_PREVIOUS_YEAR_ROLLOVER_IMAGE = "CalendarPreviousYearRolloverImage";
    public static final String KEY_CALENDAR_WEEK_FOREGROUND = "CalendarWeekForeground";
    public static final String KEY_CALENDAR_WEEKEND_FOREGROUND = "CalendarWeekendForeground";
    public static final String KEY_CHECK_BOX_MENU_ITEM_SELECTED_ICON = "CheckBoxMenuItemSelectedIcon";
    public static final String KEY_COMBO_BOX_BUTTON_DISABLED_ICON = "ComboBoxButtonDisabledIcon";
    public static final String KEY_COMBO_BOX_BUTTON_ICON = "ComboBoxButtonIcon";
    public static final String KEY_COMBO_BOX_BUTTON_IMAGE = "ComboBoxButtonImage";
    public static final String KEY_COMBO_BOX_BUTTON_PRESSED_IMAGE = "ComboBoxButtonPressedImage";
    public static final String KEY_COMBO_BOX_BUTTON_ROLLOVER_IMAGE = "ComboBoxButtonRolloverImage";
    public static final String KEY_COMBO_BOX_EDITOR_BORDER = "ComboBoxEditorBorder";
    public static final String KEY_COMBO_BOX_POPUP_BORDER = "ComboBoxPopupBorder";
    public static final String KEY_COMBO_BOX_RENDERER_BORDER = "ComboBoxRendererBorder";
    public static final String KEY_COMBO_BOX_RENDERER_BORDER_DISABLED_IMAGE = "ComboBoxRendererBorderDisabledImage";
    public static final String KEY_COMBO_BOX_RENDERER_BORDER_IMAGE = "ComboBoxRendererBorderImage";
    public static final String KEY_COMBO_BOX_RENDERER_SELECTED_BORDER = "ComboBoxRendererSelectedBorder";
    public static final String KEY_COMPOUND_TEXT_DISABLED_BORDER = "CompoundTextDisabledBorder";
    public static final String KEY_COMPOUND_TEXT_NON_EDITABLE_BORDER = "CompoundTextNonEditableBorder";
    public static final String KEY_COMPOUND_TEXT_NON_EDITABLE_ROLLOVER_BORDER = "CompoundTextNonEditableRolloverBorder";
    public static final String KEY_COMPOUND_TEXT_ROLLOVER_BORDER = "CompoundTextRolloverBorder";
    public static final String KEY_HEADER_PANE_BACKGROUND_IMAGE = "HeaderPaneBackgroundImage";
    public static final String KEY_LIST_RENDERER_BORDER = "ListRendererBorder";
    public static final String KEY_MENU_ARROW_ICON = "MenuArrowIcon";
    public static final String KEY_MENU_BACKGROUND_IMAGE = "MenuBackgroundImage";
    public static final String KEY_MENU_ITEM_BACKGROUND_IMAGE = "MenuItemBackgroundImage";
    public static final String KEY_MESSAGE_BOX_ERROR_ICON = "MessageBoxErrorIcon";
    public static final String KEY_MESSAGE_BOX_INFORMATION_ICON = "MessageBoxInfomationIcon";
    public static final String KEY_MESSAGE_BOX_QUESTION_ICON = "MessageBoxQuestionIcon";
    public static final String KEY_MESSAGE_BOX_WARNING_ICON = "MessageBoxWarningIcon";
    public static final String KEY_SCROLL_BAR_BACKGROUND = "ScrollBarBackground";
    public static final String KEY_SCROLL_BAR_BORDER_COLOR = "ScrollBarBorderColor";
    public static final String KEY_SCROLL_BAR_DOWN_IMAGE = "ScrollBarDownImage";
    public static final String KEY_SCROLL_BAR_DOWN_PRESSED_IMAGE = "ScrollBarDownPressedImage";
    public static final String KEY_SCROLL_BAR_DOWN_ROLLOVER_IMAGE = "ScrollBarDownRolloverImage";
    public static final String KEY_SCROLL_BAR_H_IMAGE = "ScrollBarHImage";
    public static final String KEY_SCROLL_BAR_H_PRESSED_IMAGE = "ScrollBarHPressedImage";
    public static final String KEY_SCROLL_BAR_H_ROLLOVER_IMAGE = "ScrollBarHRolloverImage";
    public static final String KEY_SCROLL_BAR_LEFT_IMAGE = "ScrollBarLeftImage";
    public static final String KEY_SCROLL_BAR_LEFT_PRESSED_IMAGE = "ScrollBarLeftPressedImage";
    public static final String KEY_SCROLL_BAR_LEFT_ROLLOVER_IMAGE = "ScrollBarLeftRolloverImage";
    public static final String KEY_SCROLL_BAR_RIGHT_IMAGE = "ScrollBarRightImage";
    public static final String KEY_SCROLL_BAR_RIGHT_PRESSED_IMAGE = "ScrollBarRightPressedImage";
    public static final String KEY_SCROLL_BAR_RIGHT_ROLLOVER_IMAGE = "ScrollBarRightRolloverImage";
    public static final String KEY_SCROLL_BAR_UP_IMAGE = "ScrollBarUpImage";
    public static final String KEY_SCROLL_BAR_UP_PRESSED_IMAGE = "ScrollBarUpPressedImage";
    public static final String KEY_SCROLL_BAR_UP_ROLLOVER_IMAGE = "ScrollBarUpRolloverImage";
    public static final String KEY_SCROLL_BAR_V_IMAGE = "ScrollBarVImage";
    public static final String KEY_SCROLL_BAR_V_PRESSED_IMAGE = "ScrollBarVPressedImage";
    public static final String KEY_SCROLL_BAR_V_ROLLOVER_IMAGE = "ScrollBarVRolloverImage";
    public static final String KEY_SCROLL_PANE_LOWER_RIGHT_CORNER_ICON = "ScrollPaneLowerRightCornerIcon";
    public static final String KEY_SCROLL_TABLE_SHOW_MENU_BUTTON_IMAGE = "ScrollTableShowMenuButtonImage";
    public static final String KEY_SCROLL_TABLE_SHOW_MENU_BUTTON_PRESSED_IMAGE = "ScrollTableShowMenuButtonPressedImage";
    public static final String KEY_SCROLL_TABLE_SHOW_MENU_BUTTON_ROLLOVER_IMAGE = "ScrollTableShowMenuButtonRolloverImage";
    public static final String KEY_SCROLL_TEXT_BORDER = "ScrollTextBorder";
    public static final String KEY_SELECTED_ITEM_BACKGROUND_IMAGE = "SelectedItemBackgroundImage";
    public static final String KEY_SELECTED_ITEM_DISABLED_BACKGROUND_IMAGE = "SelectedItemDisabledBackgroundImage";
    public static final String KEY_SEPARATOR_IMAGE_H = "SeparatorImageH";
    public static final String KEY_SEPARATOR_IMAGE_V = "SeparatorImageV";
    public static final String KEY_SLIDER_BACKGROUND_IMAGE_H = "SliderBackgroundImageH";
    public static final String KEY_SLIDER_BACKGROUND_IMAGE_V = "SliderBackgroundImageV";
    public static final String KEY_SLIDER_DISABLED_TICK_COLOR = "SliderDisabledTickColor";
    public static final String KEY_SLIDER_INNER_COLOR_1 = "SliderInnerColor1";
    public static final String KEY_SLIDER_INNER_COLOR_2 = "SliderInnerColor2";
    public static final String KEY_SLIDER_MINI_BACKGROUND = "SliderMiniBackground";
    public static final String KEY_SLIDER_MINI_INNER_COLOR_1 = "SliderMiniInnerColor1";
    public static final String KEY_SLIDER_MINI_INNER_COLOR_2 = "SliderMiniInnerColor2";
    public static final String KEY_SLIDER_MINI_OUTER_COLOR = "SliderMiniOuterColor";
    public static final String KEY_SLIDER_MINI_THUMB_ICON_H = "SliderMiniThumbIconH";
    public static final String KEY_SLIDER_MINI_THUMB_ICON_V = "SliderMiniThumbIconV";
    public static final String KEY_SLIDER_OUTER_COLOR_1 = "SliderOuterColor1";
    public static final String KEY_SLIDER_OUTER_COLOR_2 = "SliderOuterColor2";
    public static final String KEY_SLIDER_THUMB_ICON_H = "SliderThumbIconH";
    public static final String KEY_SLIDER_THUMB_ICON_V = "SliderThumbIconV";
    public static final String KEY_SPINNER_EDITOR_BORDER = "SpinnerEditorBorder";
    public static final String KEY_SPINNER_EDITOR_DISABLED_BORDER = "SpinnerEditorDisabledBorder";
    public static final String KEY_SPINNER_NEXT_DISABLED_ICON = "SpinnerNextDisabledIcon";
    public static final String KEY_SPINNER_NEXT_DISABLED_IMAGE = "SpinnerNextDisabledImage";
    public static final String KEY_SPINNER_NEXT_ICON = "SpinnerNextIcon";
    public static final String KEY_SPINNER_NEXT_IMAGE = "SpinnerNextImage";
    public static final String KEY_SPINNER_NEXT_PRESSED_IMAGE = "SpinnerNextPressedImage";
    public static final String KEY_SPINNER_NEXT_ROLLOVER_IMAGE = "SpinnerNextRolloverImage";
    public static final String KEY_SPINNER_PREVIOUS_DISABLED_ICON = "SpinnerPreviousDisabledIcon";
    public static final String KEY_SPINNER_PREVIOUS_DISABLED_IMAGE = "SpinnerPreviousDisabledImage";
    public static final String KEY_SPINNER_PREVIOUS_ICON = "SpinnerPreviousIcon";
    public static final String KEY_SPINNER_PREVIOUS_IMAGE = "SpinnerPreviousImage";
    public static final String KEY_SPINNER_PREVIOUS_PRESSED_IMAGE = "SpinnerPreviousPressedImage";
    public static final String KEY_SPINNER_PREVIOUS_ROLLOVER_IMAGE = "SpinnerPreviousRolloverImage";
    public static final String KEY_TABBED_PANE_BACDGROUND_IMAGE = "TabbedPaneBackgroundImage";
    public static final String KEY_TABBED_PANE_NEXT_DISABLED_IMAGE = "TabbedPaneNextDisabledImage";
    public static final String KEY_TABBED_PANE_NEXT_IMAGE = "TabbedPaneNextImage";
    public static final String KEY_TABBED_PANE_NEXT_PRESSED_IMAGE = "TabbedPaneNextPressedImage";
    public static final String KEY_TABBED_PANE_NEXT_ROLLOVER_IMAGE = "TabbedPaneNextRolloverImage";
    public static final String KEY_TABBED_PANE_PRESSED_IMAGE = "TabbedPanePressedImage";
    public static final String KEY_TABBED_PANE_PREVIOUS_DISABLED_IMAGE = "TabbedPanePreviousDisabledImage";
    public static final String KEY_TABBED_PANE_PREVIOUS_IMAGE = "TabbedPanePreviousImage";
    public static final String KEY_TABBED_PANE_PREVIOUS_PRESSED_IMAGE = "TabbedPanePreviousPressedImage";
    public static final String KEY_TABBED_PANE_PREVIOUS_ROLLOVER_IMAGE = "TabbedPanePreviousRolloverImage";
    public static final String KEY_TABBED_PANE_ROLLOVER_IMAGE = "TabbedPaneRolloverImage";
    public static final String KEY_TABBED_PANE_SPLIT_H_IMAGE = "TabbedPaneSplitHImage";
    public static final String KEY_TABBED_PANE_SPLIT_V_IMAGE = "TabbedPaneSplitVImage";
    public static final String KEY_TABLE_EDITOR_BORDER = "TableEditorBorder";
    public static final String KEY_TABLE_HEADER_DEFAULT_IMAGE = "TableHeaderDefaultImage";
    public static final String KEY_TABLE_HEADER_DOWN_ICON = "TableHeaderDownIcon";
    public static final String KEY_TABLE_HEADER_PRESSED_IMAGE = "TableHeaderPressedImage";
    public static final String KEY_TABLE_HEADER_ROLLOVER_IMAGE = "TableHeaderRolloverImage";
    public static final String KEY_TABLE_HEADER_SPLIT_IMAGE = "TableHeaderSplitImage";
    public static final String KEY_TABLE_HEADER_UP_ICON = "TableHeaderUpIcon";
    public static final String KEY_TEXT_DISABLED_BACKGROUND = "TextDisabledBackground";
    public static final String KEY_TEXT_DISABLED_BORDER = "TextDisabledBorder";
    public static final String KEY_TEXT_NON_EDITABLE_BACKGROUND = "TextNonEditableBackground";
    public static final String KEY_TEXT_NON_EDITABLE_BORDER = "TextNonEditableBorder";
    public static final String KEY_TEXT_NON_EDITABLE_ROLLOVER_BORDER = "TextNonEditableRolloverBorder";
    public static final String KEY_TEXT_ROLLOVER_BORDER = "TextRolloverBorder";
    public static final String KEY_TEXT_SELECTION_COLOR = "TextSelectionColor";
    public static final String KEY_TEXT_SELECTION_FOREGROUND = "TextSelectionForeground";
    public static final String KEY_TREE_COLLAPSED_ICON = "TreeCollapsedIcon";
    public static final String KEY_TREE_COLUMN_EDITOR_BORDER = "TreeColumnEditorBorder";
    public static final String KEY_TREE_EDITOR_BORDER = "TreeEditorBorder";
    public static final String KEY_TREE_EXPANDED_ICON = "TreeExpandedIcon";
    public static final String KEY_TREE_LINE_COLOR = "TreeLineColor";
    public static final String KEY_TREE_NODE_DEFAULT_ICON = "TreeNodeDefaultIcon";
    public static final String KEY_TREE_RENDERER_BORDER = "TreeRendererBorder";
    public static final String KEY_WINDOW_BACKGROUND_IMAGE = "WindowBackgroundImage";
    public static final String KEY_WINDOW_CLOSE_IMAGE = "WindowCloseImage";
    public static final String KEY_WINDOW_CLOSE_PRESSED_IMAGE = "WindowClosePressedImage";
    public static final String KEY_WINDOW_CLOSE_ROLLOVER_IMAGE = "WindowCloseRolloverImage";
    public static final String KEY_WINDOW_MAX_IMAGE = "WindowMaxImage";
    public static final String KEY_WINDOW_MAX_PRESSED_IMAGE = "WindowMaxPressedImage";
    public static final String KEY_WINDOW_MAX_ROLLOVER_IMAGE = "WindowMaxRolloverImage";
    public static final String KEY_WINDOW_MIN_IMAGE = "WindowMinImage";
    public static final String KEY_WINDOW_MIN_PRESSED_IMAGE = "WindowMinPressedImage";
    public static final String KEY_WINDOW_MIN_ROLLOVER_IMAGE = "WindowMinRolloverImage";
    public static final String KEY_WINDOW_RESTORE_IMAGE = "WindowRestoreImage";
    public static final String KEY_WINDOW_RESTORE_PRESSED_IMAGE = "WindowRestorePressedImage";
    public static final String KEY_WINDOW_RESTORE_ROLLOVER_IMAGE = "WindowRestoreRolloverImage";
    public static final String KEY_WINDOW_TITLE_IMAGE = "WindowTitleImage";

    static {
        putColor("EmptyColor", new Color(0, 0, 0, 0));
        putColor("WhiteColor", new Color(254, 255, 255));
        putColor("TextDisabledBackground", new Color(250, 250, 249));
        putColor("TextSelectionForeground", getColor("WhiteColor"));
        putColor("TextSelectionColor", new Color(49, 106, 197));
        putColor("TextNonEditableBackground", new Color(235, 235, 235));
        putColor("ScrollBarBorderColor", new Color(226, 238, 243));
        putColor("ScrollBarBackground", new Color(235, 243, 246));
        putColor("SliderDisabledTickColor", Color.GRAY);
        putColor("TreeLineColor", new Color(172, 168, 153));
        putColor("SliderOuterColor1", new Color(157, 199, 240));
        putColor("SliderOuterColor2", new Color(154, 235, 171));
        putColor("SliderInnerColor1", new Color(76, 181, 237));
        putColor("SliderInnerColor2", new Color(53, 215, 89));
        putColor("SliderMiniOuterColor", new Color(78, 117, 160));
        putColor("SliderMiniBackground", Color.WHITE);
        putColor("SliderMiniInnerColor1", new Color(73, 239, 54));
        putColor("SliderMiniInnerColor2", new Color(238, 255, 71));
        putColor("CalendarDayBackground", new Color(168, 219, 255));
        putColor("CalendarWeekForeground", new Color(44, 74, 137));
        putColor("CalendarWeekendForeground", new Color(255, 128, 128));

        putBorder("ComboBoxRendererBorder", new EmptyBorder(0, 5, 0, 0));
        putBorder("ComboBoxRendererSelectedBorder", new EmptyBorder(0, 3, 0, 0));
        putBorder("ComboBoxEditorBorder", new EmptyBorder(2, 3, 0, 0));
        putBorder("ComboBoxPopupBorder", new CompoundBorder(new LineBorder(
                new Color(0, 147, 209)), new EmptyBorder(1, 1, 1, 1)));
        putBorder("ListRendererBorder", new EmptyBorder(0, 4, 0, 0));
        putBorder("TreeRendererBorder", new EmptyBorder(0, 3, 0, 1));
        putBorder("TreeEditorBorder", new CompoundBorder(new ImageBorder(
                getImageByName("border_rollover.png"), 2, 2, 2, 2),
                new EmptyBorder(0, 1, 0, 1)));
        putBorder("ScrollTextBorder", new EmptyBorder(2, 3, 1, 1));
        putBorder("SpinnerEditorBorder", new ImageBorder(
                getImageByName("border_editor_inner.png"), 2, 3, 0, 0));
        putBorder("SpinnerEditorDisabledBorder", new ImageBorder(
                getImageByName("border_editor_inner_disabled.png"), 2, 3, 0, 0));
        putBorder("TableEditorBorder",
                (Border) borderMap.get("TreeEditorBorder"));
        putBorder("TreeColumnEditorBorder", new CompoundBorder(new ImageBorder(
                getImageByName("border_rollover.png"), 2, 2, 2, 2),
                new EmptyBorder(0, 0, 0, 1)));
        putBorder("TextRolloverBorder", new ImageBorder(
                getImageByName("border_rollover.png"), 5, 6, 3, 4));
        putBorder("TextNonEditableBorder", new ImageBorder(
                getImageByName("border_noneditable.png"), 5, 6, 3, 4));
        putBorder("TextNonEditableRolloverBorder", new ImageBorder(
                getImageByName("border_noneditable_rollover.png"), 5, 6, 3, 4));
        putBorder("TextDisabledBorder", new ImageBorder(
                getImageByName("border_disabled.png"), 5, 6, 3, 4));
        putBorder("CompoundTextRolloverBorder", new ImageBorder(
                getImageByName("border_rollover.png"), 2, 2, 2, 2));
        putBorder("CompoundTextDisabledBorder", new ImageBorder(
                getImageByName("border_disabled.png"), 2, 2, 2, 2));
        putBorder("CompoundTextNonEditableBorder", new ImageBorder(
                getImageByName("border_noneditable.png"), 2, 2, 2, 2));
        putBorder("CompoundTextNonEditableRolloverBorder", new ImageBorder(
                getImageByName("border_noneditable_rollover.png"), 2, 2, 2, 2));
        putBorder("CalendarDayBorder", new EmptyBorder(2, 2, 2, 2));
        putBorder("CalendarTodaySelectedBorder", new LineBorder(new Color(21,
                88, 152), 2));

        putIcon("ComboBoxButtonIcon",
                getIconByName("combobox_arrow_button_icon.png"));
        putIcon("ComboBoxButtonDisabledIcon",
                getIconByName("combobox_arrow_button_disabled_icon.png"));
        putIcon("SliderThumbIconH", getIconByName("slider_thumb_h.png"));
        putIcon("SliderThumbIconV", getIconByName("slider_thumb_v.png"));
        putIcon("SliderMiniThumbIconH",
                getIconByName("slider_mini_thumb_h.png"));
        putIcon("SliderMiniThumbIconV",
                getIconByName("slider_mini_thumb_v.png"));
        putIcon("SpinnerPreviousIcon",
                getIconByName("spinner_previous_button_icon.png"));
        putIcon("SpinnerPreviousDisabledIcon",
                getIconByName("spinner_previous_button_disabled_icon.png"));
        putIcon("SpinnerNextIcon",
                getIconByName("spinner_next_button_icon.png"));
        putIcon("SpinnerNextDisabledIcon",
                getIconByName("spinner_next_button_disabled_icon.png"));
        putIcon("TableHeaderUpIcon", getIconByName("table_arrow_up.png"));
        putIcon("TableHeaderDownIcon", getIconByName("table_arrow_down.png"));
        putIcon("TreeNodeDefaultIcon", getIconByName("tree_node.png"));
        putIcon("TreeExpandedIcon", getIconByName("tree_expanded.png"));
        putIcon("TreeCollapsedIcon", getIconByName("tree_collapsed.png"));
        putIcon("ScrollPaneLowerRightCornerIcon",
                getIconByName("scrollpane_corner_l_r.png"));
        putIcon("CheckBoxMenuItemSelectedIcon",
                getIconByName("checkbox_menuitem_selected.png"));
        putIcon("MenuArrowIcon", getIconByName("menu_arrow.png"));
        putIcon("MessageBoxErrorIcon", getIconByName("messagebox_error.png"));
        putIcon("MessageBoxInfomationIcon",
                getIconByName("messagebox_info.png"));
        putIcon("MessageBoxWarningIcon",
                getIconByName("messagebox_warning.png"));
        putIcon("MessageBoxQuestionIcon",
                getIconByName("messagebox_question.png"));

        putImage("SelectedItemBackgroundImage",
                getImageByName("selected_item_background.png", true));
        putImage("SelectedItemDisabledBackgroundImage",
                getImageByName("selected_item_disabled_background.png", true));
        putImage("ComboBoxRendererBorderImage",
                getImageByName("border_editor_inner.png"));
        putImage("ComboBoxRendererBorderDisabledImage",
                getImageByName("border_editor_inner_disabled.png"));
        putImage("ComboBoxButtonImage",
                getImageByName("combobox_arrow_button_normal.png", true));
        putImage("ComboBoxButtonRolloverImage",
                getImageByName("combobox_arrow_button_rollover.png", true));
        putImage("ComboBoxButtonPressedImage",
                getImageByName("combobox_arrow_button_pressed.png", true));
        putImage("SeparatorImageH", getImageByName("separator_h.png"));
        putImage("SeparatorImageV", getImageByName("separator_v.png"));
        putImage("SliderBackgroundImageH", getImageByName("slider_bg_h.png"));
        putImage("SliderBackgroundImageV", getImageByName("slider_bg_v.png"));
        putImage("SpinnerPreviousImage",
                getImageByName("spinner_previous_button_normal.png", true));
        putImage("SpinnerPreviousPressedImage",
                getImageByName("spinner_previous_button_pressed.png", true));
        putImage("SpinnerPreviousRolloverImage",
                getImageByName("spinner_previous_button_rollover.png", true));
        putImage("SpinnerPreviousDisabledImage",
                getImageByName("spinner_previous_button_disabled.png", true));
        putImage("SpinnerNextImage",
                getImageByName("spinner_next_button_normal.png", true));
        putImage("SpinnerNextPressedImage",
                getImageByName("spinner_next_button_pressed.png", true));
        putImage("SpinnerNextRolloverImage",
                getImageByName("spinner_next_button_rollover.png", true));
        putImage("SpinnerNextDisabledImage",
                getImageByName("spinner_next_button_disabled.png", true));
        putImage("HeaderPaneBackgroundImage",
                getImageByName("table_header_default.png", true));
        putImage("TableHeaderDefaultImage",
                (Image) imageMap.get("HeaderPaneBackgroundImage"));
        putImage("TableHeaderPressedImage",
                getImageByName("table_header_press.png", true));
        putImage("TableHeaderRolloverImage",
                getImageByName("table_header_rollover.png", true));
        putImage("TableHeaderSplitImage",
                getImageByName("table_header_split.png", true));
        putImage("ScrollTableShowMenuButtonImage",
                (Image) imageMap.get("HeaderPaneBackgroundImage"));
        putImage("ScrollTableShowMenuButtonRolloverImage",
                (Image) imageMap.get("TableHeaderRolloverImage"));
        putImage("ScrollTableShowMenuButtonPressedImage",
                (Image) imageMap.get("TableHeaderPressedImage"));
        putImage("ScrollBarUpImage", getImageByName("scrollbar_up.png", true));
        putImage("ScrollBarUpRolloverImage",
                getImageByName("scrollbar_up_r.png", true));
        putImage("ScrollBarUpPressedImage",
                getImageByName("scrollbar_up_p.png", true));
        putImage("ScrollBarDownImage",
                getImageByName("scrollbar_down.png", true));
        putImage("ScrollBarDownRolloverImage",
                getImageByName("scrollbar_down_r.png", true));
        putImage("ScrollBarDownPressedImage",
                getImageByName("scrollbar_down_p.png", true));
        putImage("ScrollBarRightImage",
                getImageByName("scrollbar_right.png", true));
        putImage("ScrollBarRightRolloverImage",
                getImageByName("scrollbar_right_r.png", true));
        putImage("ScrollBarRightPressedImage",
                getImageByName("scrollbar_right_p.png", true));
        putImage("ScrollBarLeftImage",
                getImageByName("scrollbar_left.png", true));
        putImage("ScrollBarLeftRolloverImage",
                getImageByName("scrollbar_left_r.png", true));
        putImage("ScrollBarLeftPressedImage",
                getImageByName("scrollbar_left_p.png", true));
        putImage("ScrollBarVImage", getImageByName("scrollbar_v.png", true));
        putImage("ScrollBarVRolloverImage",
                getImageByName("scrollbar_v_r.png", true));
        putImage("ScrollBarVPressedImage",
                getImageByName("scrollbar_v_p.png", true));
        putImage("ScrollBarHImage", getImageByName("scrollbar_h.png", true));
        putImage("ScrollBarHRolloverImage",
                getImageByName("scrollbar_h_r.png", true));
        putImage("ScrollBarHPressedImage",
                getImageByName("scrollbar_h_p.png", true));
        putImage("TabbedPaneBackgroundImage",
                getImageByName("tabbedpane_background.png"));
        putImage("TabbedPaneRolloverImage",
                getImageByName("tabbedpane_rollover.png"));
        putImage("TabbedPanePressedImage",
                getImageByName("tabbedpane_pressed.png"));
        putImage("TabbedPaneSplitHImage",
                getImageByName("tabbedpane_seperator_h.png"));
        putImage("TabbedPaneSplitVImage",
                getImageByName("tabbedpane_seperator_v.png"));
        putImage("TabbedPanePreviousImage",
                getImageByName("tabbedpane_previous_button.png", true));
        putImage("TabbedPanePreviousRolloverImage",
                getImageByName("tabbedpane_previous_button_rollover.png", true));
        putImage("TabbedPanePreviousPressedImage",
                getImageByName("tabbedpane_previous_button_pressed.png", true));
        putImage("TabbedPanePreviousDisabledImage",
                getImageByName("tabbedpane_previous_button_disabled.png", true));
        putImage("TabbedPaneNextImage",
                getImageByName("tabbedpane_next_button.png", true));
        putImage("TabbedPaneNextRolloverImage",
                getImageByName("tabbedpane_next_button_rollover.png", true));
        putImage("TabbedPaneNextPressedImage",
                getImageByName("tabbedpane_next_button_pressed.png", true));
        putImage("TabbedPaneNextDisabledImage",
                getImageByName("tabbedpane_next_button_disabled.png", true));
        putImage("MenuBackgroundImage", getImageByName("menu_background.png"));
        putImage("MenuItemBackgroundImage",
                getImageByName("menu_item_background.png", true));
        putImage("CalendarBackgroundImage",
                getImageByName("calendar_background.png"));
        putImage("CalendarCloseRolloverImage",
                getImageByName("calendar_close_rollover.png", true));
        putImage("CalendarClosePressedImage",
                getImageByName("calendar_close_pressed.png", true));
        putImage("CalendarCloseImage",
                getImageByName("calendar_close_normal.png", true));
        putImage("CalendarNextMonthRolloverImage",
                getImageByName("calendar_nextmonth_rollover.png", true));
        putImage("CalendarNextMonthPressedImage",
                getImageByName("calendar_nextmonth_pressed.png", true));
        putImage("CalendarNextMonthImage",
                getImageByName("calendar_nextmonth_normal.png", true));
        putImage("CalendarNextYearRolloverImage",
                getImageByName("calendar_nextyear_rollover.png", true));
        putImage("CalendarNextYearPressedImage",
                getImageByName("calendar_nextyear_pressed.png", true));
        putImage("CalendarNextYearImage",
                getImageByName("calendar_nextyear_normal.png", true));
        putImage("CalendarPreviousMonthRolloverImage",
                getImageByName("calendar_premonth_rollover.png", true));
        putImage("CalendarPreviousMonthPressedImage",
                getImageByName("calendar_premonth_pressed.png", true));
        putImage("CalendarPreviousMonthImage",
                getImageByName("calendar_premonth_normal.png", true));
        putImage("CalendarPreviousYearRolloverImage",
                getImageByName("calendar_preyear_rollover.png", true));
        putImage("CalendarPreviousYearPressedImage",
                getImageByName("calendar_preyear_pressed.png", true));
        putImage("CalendarPreviousYearImage",
                getImageByName("calendar_preyear_normal.png", true));
        putImage("CalendarComboBoxButtonImage",
                getImageByName("calendar_button.png", true));
        putImage("CalendarComboBoxButtonRolloverImage",
                (Image) imageMap.get("CalendarComboBoxButtonImage"));
        putImage("CalendarComboBoxButtonPressedImage",
                getImageByName("calendar_button_pressed.png", true));
        putImage("WindowCloseImage",
                getImageByName("win_close_normal.png", true));
        putImage("WindowCloseRolloverImage",
                getImageByName("win_close_rollover.png", true));
        putImage("WindowClosePressedImage",
                getImageByName("win_close_pressed.png", true));
        putImage("WindowMaxImage", getImageByName("win_max_normal.png", true));
        putImage("WindowMaxRolloverImage",
                getImageByName("win_max_rollover.png", true));
        putImage("WindowMaxPressedImage",
                getImageByName("win_max_pressed.png", true));
        putImage("WindowRestoreImage",
                getImageByName("win_restore_normal.png", true));
        putImage("WindowRestoreRolloverImage",
                getImageByName("win_restore_rollover.png", true));
        putImage("WindowRestorePressedImage",
                getImageByName("win_restore_pressed.png", true));
        putImage("WindowMinImage", getImageByName("win_min_normal.png", true));
        putImage("WindowMinRolloverImage",
                getImageByName("win_min_rollover.png", true));
        putImage("WindowMinPressedImage",
                getImageByName("win_min_pressed.png", true));
        putImage("WindowBackgroundImage",
                getImageByName("window_background.png", true));
        putImage("WindowTitleImage", getImageByName("window_title.png"));
    }

    public static Map<String, Color> getAllColors() {
        return colorMap;
    }

    public static Map<String, Icon> getAllIcons() {
        return iconMap;
    }

    public static Map<String, Image> getAllImages() {
        return imageMap;
    }

    public static Border getBorder(String key) {
        return (Border) borderMap.get(key);
    }

    public static Color getColor(String key) {
        Color color = (Color) colorMap.get(key);
        return color == null ? (Color) colorMap.get("EmptyColor") : color;
    }

    public static Color getEmptyColor() {
        return getColor("EmptyColor");
    }

    public static Icon getIcon(String key) {
        return (Icon) iconMap.get(key);
    }

    public static ImageIcon getIconByName(String name) {
        return SwingResourceManager.getIcon(UIResourceManager.class,
                "/com/java/ui/images/" + name);
    }

    public static Image getImage(String key) {
        return (Image) imageMap.get(key);
    }

    public static Image getImageByName(String name) {
        return getImageByName(name, false);
    }

    public static Image getImageByName(String name, boolean createdByIcon) {
        if (createdByIcon) {
            return getIconByName(name).getImage();
        }

        return SwingResourceManager.getImage(UIResourceManager.class,
                "/com/java/ui/images/" + name);
    }

    public static Color getWhiteColor() {
        return getColor("WhiteColor");
    }

    public static void putBorder(String key, Border border) {
        borderMap.put(key, border);
    }

    public static void putColor(String key, Color color) {
        colorMap.put(key, color);
    }

    public static void putIcon(String key, Icon icon) {
        iconMap.put(key, icon);
    }

    public static void putImage(String key, Image image) {
        imageMap.put(key, image);
    }
}