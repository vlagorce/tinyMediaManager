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

package org.tinymediamanager.core.tvshow.filenaming;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;
import org.tinymediamanager.core.tvshow.ITvShowSeasonFileNaming;
import org.tinymediamanager.core.tvshow.TvShowRenamer;
import org.tinymediamanager.core.tvshow.entities.TvShow;

/**
 * The Enum TvShowSeasonThumbNaming.
 * 
 * @author Manuel Laggner
 */
public enum TvShowSeasonThumbNaming implements ITvShowSeasonFileNaming {
  /** seasonXX-thumb.* */
  SEASON_THUMB {
    @Override
    public String getFilename(TvShow tvShow, int season, String extension) {
      if (season > 0) {
        return String.format("season%02d-thumb." + extension, season);
      }
      else {
        return "season-specials-thumb." + extension;
      }
    }
  },

  /** season_folder/seasonXX-thumb.* */
  SEASON_FOLDER {
    @Override
    public String getFilename(TvShow tvShow, int season, String extension) {
      String seasonFoldername = TvShowRenamer.getSeasonFoldername(tvShow, season);

      // check whether the season folder name exists or not; do not create it just for the artwork!
      if (StringUtils.isBlank(seasonFoldername) || !Files.exists(Paths.get(tvShow.getPath(), seasonFoldername))) {
        // does not exist - fall back to the the show base filename style
        return SEASON_THUMB.getFilename(tvShow, season, extension);
      }

      String filename = "";
      if (StringUtils.isNotBlank(seasonFoldername)) {
        filename = seasonFoldername + File.separator;
      }

      if (season > 0) {
        filename += String.format("season%02d-thumb", season);
      }
      else {
        filename += "season-specials-thumb";
      }
      return filename + "." + extension;
    }
  }
}
