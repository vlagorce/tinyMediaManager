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
package org.tinymediamanager.ui.tvshows.actions;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ResourceBundle;

import org.tinymediamanager.core.tvshow.entities.TvShowSeason;
import org.tinymediamanager.ui.IconManager;
import org.tinymediamanager.ui.UTF8Control;
import org.tinymediamanager.ui.actions.TmmAction;
import org.tinymediamanager.ui.tvshows.TvShowUIModule;
import org.tinymediamanager.ui.tvshows.dialogs.TvShowSeasonEditorDialog;

/**
 * The Class TvShowChangeSeasonArtworkAction. To change the season artwork
 * 
 * @author Manuel Laggner
 */
public class TvShowChangeSeasonArtworkAction extends TmmAction {
  private static final long           serialVersionUID = 8356413227405772558L;
  private static final ResourceBundle BUNDLE           = ResourceBundle.getBundle("messages", new UTF8Control()); //$NON-NLS-1$

  public TvShowChangeSeasonArtworkAction() {
    putValue(NAME, BUNDLE.getString("tvshow.changeseasonartwork")); //$NON-NLS-1$
    putValue(LARGE_ICON_KEY, IconManager.EDIT);
    putValue(SMALL_ICON, IconManager.EDIT);
    putValue(SHORT_DESCRIPTION, BUNDLE.getString("tvshow.changeseasonartwork")); //$NON-NLS-1$
  }

  @Override
  protected void processAction(ActionEvent e) {
    List<Object> selectedObjects = TvShowUIModule.getInstance().getSelectionModel().getSelectedObjects();

    for (Object obj : selectedObjects) {
      // display image chooser
      if (obj instanceof TvShowSeason) {
        TvShowSeason season = (TvShowSeason) obj;
        TvShowSeasonEditorDialog editor = new TvShowSeasonEditorDialog(season, selectedObjects.size() > 1 ? true : false);
        if (!editor.showDialog()) {
          break;
        }
      }
    }
  }
}
