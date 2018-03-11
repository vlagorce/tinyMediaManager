/*
 * Copyright 2012 - 2018 Manuel Laggner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinymediamanager.ui.settings;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinymediamanager.Globals;
import org.tinymediamanager.core.Message;
import org.tinymediamanager.core.Message.MessageLevel;
import org.tinymediamanager.core.MessageManager;
import org.tinymediamanager.core.Settings;
import org.tinymediamanager.core.Utils;
import org.tinymediamanager.ui.TmmFontHelper;
import org.tinymediamanager.ui.TmmUIHelper;
import org.tinymediamanager.ui.UTF8Control;
import org.tinymediamanager.ui.components.LinkLabel;
import org.tinymediamanager.ui.components.ReadOnlyTextArea;
import org.tinymediamanager.ui.components.TmmLabel;

import net.miginfocom.swing.MigLayout;

/**
 * The class UiSettingsPanel is used to display some UI related settings
 * 
 * @author Manuel Laggner
 */
public class UiSettingsPanel extends JPanel {
  private static final long           serialVersionUID   = 6409982195347794360L;

  /** @wbp.nls.resourceBundle messages */
  private static final ResourceBundle BUNDLE             = ResourceBundle.getBundle("messages", new UTF8Control()); //$NON-NLS-1$
  private static final Logger         LOGGER             = LoggerFactory.getLogger(UiSettingsPanel.class);
  private static final Integer[]      DEFAULT_FONT_SIZES = { 12, 14, 16, 18, 20, 22, 24, 26, 28 };

  private Settings                    settings           = Settings.getInstance();
  private List<LocaleComboBox>        locales            = new ArrayList<>();

  private JComboBox                   cbLanguage;
  private JLabel                      lblFontChangeHint;
  private LinkLabel                   lblLinkTransifex;
  private JComboBox                   cbFontSize;
  private JComboBox                   cbFontFamily;
  private JLabel                      lblLanguageChangeHint;
  private JCheckBox                   chckbxStoreWindowPreferences;
  private JComboBox                   cbTheme;
  private JLabel                      lblThemeHint;

  public UiSettingsPanel() {
    LocaleComboBox actualLocale = null;
    Locale settingsLang = Utils.getLocaleFromLanguage(Globals.settings.getLanguage());
    for (Locale l : Utils.getLanguages()) {
      LocaleComboBox localeComboBox = new LocaleComboBox(l);
      locales.add(localeComboBox);
      if (l.equals(settingsLang)) {
        actualLocale = localeComboBox;
      }
    }

    // ui init
    initComponents();
    initDataBindings();

    // data init
    if (actualLocale != null) {
      cbLanguage.setSelectedItem(actualLocale);
    }

    cbFontFamily.setSelectedItem(Globals.settings.getFontFamily());
    int index = cbFontFamily.getSelectedIndex();
    if (index < 0) {
      cbFontFamily.setSelectedItem("Dialog");
      index = cbFontFamily.getSelectedIndex();
    }
    if (index < 0) {
      cbFontFamily.setSelectedIndex(0);
    }
    cbFontSize.setSelectedItem(Globals.settings.getFontSize());
    index = cbFontSize.getSelectedIndex();
    if (index < 0) {
      cbFontSize.setSelectedIndex(0);
    }
    cbTheme.setSelectedItem(Globals.settings.getTheme());
    index = cbTheme.getSelectedIndex();
    if (index < 0) {
      cbTheme.setSelectedIndex(0);
    }

    lblLinkTransifex.addActionListener(arg0 -> {
      try {
        TmmUIHelper.browseUrl(lblLinkTransifex.getText());
      }
      catch (Exception e) {
        LOGGER.error(e.getMessage());
        MessageManager.instance.pushMessage(
            new Message(MessageLevel.ERROR, lblLinkTransifex.getText(), "message.erroropenurl", new String[] { ":", e.getLocalizedMessage() }));//$NON-NLS-2$
      }
    });

    ActionListener actionListener = e -> checkChanges();
    cbLanguage.addActionListener(actionListener);
    cbFontFamily.addActionListener(actionListener);
    cbFontSize.addActionListener(actionListener);
    cbTheme.addActionListener(actionListener);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private void initComponents() {
    setLayout(new MigLayout("", "[25lp][][400lp,grow]", "[][][][][][20lp][][][][20lp][][][][][][20lp][][]"));
    {
      final JLabel lblLanguageT = new TmmLabel(BUNDLE.getString("Settings.language"), 1.16667); //$NON-NLS-1$
      add(lblLanguageT, "cell 0 0 3 1");
    }
    {
      cbLanguage = new JComboBox(locales.toArray());
      add(cbLanguage, "cell 1 1 2 1");
    }
    {
      final JLabel lblLanguageHint = new JLabel(BUNDLE.getString("tmm.helptranslate")); //$NON-NLS-1$
      add(lblLanguageHint, "cell 1 2 2 1");
    }
    {
      lblLinkTransifex = new LinkLabel("https://forum.kodi.tv/showthread.php?tid=174987");
      add(lblLinkTransifex, "cell 1 3 2 1");
    }
    {
      lblLanguageChangeHint = new JLabel("");
      TmmFontHelper.changeFont(lblLanguageChangeHint, Font.BOLD);
      add(lblLanguageChangeHint, "cell 0 4 3 1");
    }
    {
      final JLabel lblTheme = new TmmLabel(BUNDLE.getString("Settings.uitheme"), 1.16667); //$NON-NLS-1$
      add(lblTheme, "cell 0 6 3 1");
    }
    {
      cbTheme = new JComboBox(new String[] { "Light", "Dark" });
      add(cbTheme, "cell 1 7 2 1");
    }
    {
      lblThemeHint = new JLabel("");
      TmmFontHelper.changeFont(lblThemeHint, Font.BOLD);
      add(lblThemeHint, "cell 0 8 3 1");
    }
    {
      final JLabel lblFontT = new TmmLabel(BUNDLE.getString("Settings.font"), 1.16667); //$NON-NLS-1$
      add(lblFontT, "cell 0 10 3 1");
    }
    {
      final JLabel lblFontFamilyT = new JLabel(BUNDLE.getString("Settings.fontfamily")); //$NON-NLS-1$
      add(lblFontFamilyT, "cell 1 11");
    }
    {
      GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
      cbFontFamily = new JComboBox(env.getAvailableFontFamilyNames());
      add(cbFontFamily, "cell 2 11");
    }
    {
      final JLabel lblFontSizeT = new JLabel(BUNDLE.getString("Settings.fontsize")); //$NON-NLS-1$
      add(lblFontSizeT, "cell 1 12");
    }
    {
      cbFontSize = new JComboBox(DEFAULT_FONT_SIZES);
      add(cbFontSize, "cell 2 12");
    }
    {
      final JTextArea tpFontHint = new ReadOnlyTextArea(BUNDLE.getString("Settings.fonts.hint")); //$NON-NLS-1$
      add(tpFontHint, "cell 1 13 2 1,growx");
    }
    {
      lblFontChangeHint = new JLabel("");
      TmmFontHelper.changeFont(lblFontChangeHint, Font.BOLD);
      add(lblFontChangeHint, "cell 0 14 3 1");
    }
    {
      final JLabel lblMiscT = new TmmLabel(BUNDLE.getString("Settings.misc"), 1.16667); //$NON-NLS-1$
      add(lblMiscT, "cell 0 16 3 1");
    }
    {
      chckbxStoreWindowPreferences = new JCheckBox(BUNDLE.getString("Settings.storewindowpreferences")); //$NON-NLS-1$
      add(chckbxStoreWindowPreferences, "cell 1 17 2 1");
    }
  }

  /**
   * Check changes.
   */
  private void checkChanges() {
    LocaleComboBox loc = (LocaleComboBox) cbLanguage.getSelectedItem();
    Locale locale = loc.loc;
    Locale actualLocale = Utils.getLocaleFromLanguage(Globals.settings.getLanguage());
    if (!locale.equals(actualLocale)) {
      Globals.settings.setLanguage(locale.toString());
      lblLanguageChangeHint.setText(BUNDLE.getString("Settings.languagehint")); //$NON-NLS-1$
    }

    // theme
    String theme = (String) cbTheme.getSelectedItem();
    if (!theme.equals(Globals.settings.getTheme())) {
      Globals.settings.setTheme(theme);
      lblThemeHint.setText(BUNDLE.getString("Settings.uitheme.hint")); //$NON-NLS-1$
    }

    // fonts
    Integer fontSize = (Integer) cbFontSize.getSelectedItem();
    if (fontSize != Globals.settings.getFontSize()) {
      Globals.settings.setFontSize(fontSize);
      lblFontChangeHint.setText(BUNDLE.getString("Settings.fontchangehint")); //$NON-NLS-1$
    }

    String fontFamily = (String) cbFontFamily.getSelectedItem();
    if (!fontFamily.equals(Globals.settings.getFontFamily())) {
      Globals.settings.setFontFamily(fontFamily);
      lblFontChangeHint.setText(BUNDLE.getString("Settings.fontchangehint")); //$NON-NLS-1$
    }
  }

  /**
   * Helper class for customized toString() method, to get the Name in localized language.
   */
  private class LocaleComboBox {
    private Locale       loc;
    private List<Locale> countries;

    LocaleComboBox(Locale loc) {
      this.loc = loc;
      countries = LocaleUtils.countriesByLanguage(loc.getLanguage().toLowerCase());
    }

    public Locale getLocale() {
      return loc;
    }

    @Override
    public String toString() {
      // display country name if needed
      // not needed when language == country
      if (loc.getLanguage().equalsIgnoreCase(loc.getCountry())) {
        return loc.getDisplayLanguage(loc);
      }

      // special exceptions (which do not have language == country)
      if (loc.toString().equals("en_US")) {
        return loc.getDisplayLanguage(loc);
      }

      // not needed, when this language is only in one country
      if (countries.size() == 1) {
        return loc.getDisplayLanguage(loc);
      }

      // output country if available
      if (StringUtils.isNotBlank(loc.getDisplayCountry(loc))) {
        return loc.getDisplayLanguage(loc) + " (" + loc.getDisplayCountry(loc) + ")";
      }

      return loc.getDisplayLanguage(loc);
    }
  }

  protected void initDataBindings() {
    BeanProperty<Settings, Boolean> settingsBeanProperty = BeanProperty.create("storeWindowPreferences");
    BeanProperty<JCheckBox, Boolean> jCheckBoxBeanProperty = BeanProperty.create("selected");
    AutoBinding<Settings, Boolean, JCheckBox, Boolean> autoBinding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, settings,
        settingsBeanProperty, chckbxStoreWindowPreferences, jCheckBoxBeanProperty);
    autoBinding.bind();
  }
}
