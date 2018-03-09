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
package org.tinymediamanager.core;

/**
 * The Class Constants.
 * 
 * @author Manuel Laggner
 */
public final class Constants {
  public final static String BACKUP_FOLDER          = ".deletedByTMM";
  public final static String TEMP_FOLDER            = "tmp";

  public final static String ACTORS                 = "actors";
  public final static String ADDED_EPISODE          = "addedEpisode";
  public final static String ADDED_MOVIE            = "addedMovie";
  public final static String ADDED_MOVIE_SET        = "addedMovieSet";
  public final static String ADDED_SEASON           = "addedSeason";
  public final static String ADDED_TV_SHOW          = "addedTvShow";
  public final static String AIRED_EPISODE          = "airedEpisode";
  public final static String AIRED_SEASON           = "airedSeason";
  public final static String AUDIO_CODEC            = "audioCodec";
  public final static String BANNER                 = "banner";
  public final static String BANNER_URL             = "bannerUrl";
  public final static String CAST                   = "cast";
  public final static String CHARACTER              = "character";
  public final static String CERTIFICATION          = "certification";
  public final static String COUNTRY                = "country";
  public final static String DATA_SOURCE            = "dataSource";
  public final static String DATE_ADDED             = "dateAdded";
  public final static String DATE_ADDED_AS_STRING   = "dateAddedAsString";
  public final static String DIRECTORS              = "directors";
  public final static String DIRECTORS_AS_STRING    = "directorsAsString";
  public final static String DVD_EPISODE            = "dvdEpisode";
  public final static String DVD_ORDER              = "dvdOrder";
  public final static String DVD_SEASON             = "dvdSeason";
  public final static String DISPLAY_EPISODE        = "displayEpisode";
  public final static String DISPLAY_SEASON         = "displaySeason";
  public final static String EDITION                = "edition";
  public final static String EDITION_AS_STRING      = "editionAsString";
  public final static String EPISODE                = "episode";
  public final static String EPISODE_COUNT          = "episodeCount";
  public final static String FANART                 = "fanart";
  public final static String FANART_URL             = "fanartUrl";
  public final static String FIRST_AIRED            = "firstAired";
  public final static String FIRST_AIRED_AS_STRING  = "firstAiredAsString";
  public final static String FRAME_RATE             = "frameRate";
  public final static String GENRE                  = "genre";
  public final static String GENRES_AS_STRING       = "genresAsString";
  public final static String HAS_IMAGES             = "hasImages";
  public final static String HAS_NFO_FILE           = "hasNfoFile";
  public final static String MEDIA_FILES            = "mediaFiles";
  public final static String MEDIA_INFORMATION      = "mediaInformation";
  public final static String MEDIA_SOURCE           = "mediaSource";
  public final static String MOVIESET               = "movieset";
  public final static String MOVIESET_TITLE         = "movieSetTitle";
  public final static String NAME                   = "name";
  public final static String NEWLY_ADDED            = "newlyAdded";
  public final static String NFO_FILENAME           = "nfoFilename";
  public final static String ORIGINAL_TITLE         = "originalTitle";
  public final static String PATH                   = "path";
  public final static String PLOT                   = "plot";
  public final static String POSTER                 = "poster";
  public final static String POSTER_URL             = "posterUrl";
  public final static String PRODUCERS              = "producers";
  public final static String PRODUCTION_COMPANY     = "productionCompany";
  public final static String RATING                 = "rating";
  public final static String RELEASE_DATE           = "releaseDate";
  public final static String RELEASE_DATE_AS_STRING = "releaseDateAsString";
  public final static String REMOVED_EPISODE        = "removedEpisode";
  public final static String REMOVED_MOVIE          = "removedMovie";
  public final static String REMOVED_MOVIE_SET      = "removedMovieSet";
  public final static String REMOVED_TV_SHOW        = "removedTvShow";
  public final static String ROLE                   = "role";
  public final static String RUNTIME                = "runtime";
  public final static String SCRAPED                = "scraped";
  public final static String SEASON                 = "season";
  public final static String SEASON_COUNT           = "seasonCount";
  public final static String SEASON_POSTER          = "seasonPoster";
  public final static String SEASON_BANNER          = "seasonBanner";
  public final static String SEASON_THUMB           = "seasonThumb";
  public final static String SORT_TITLE             = "sortTitle";
  public final static String SPOKEN_LANGUAGES       = "spokenLanguages";
  public final static String STATUS                 = "status";
  public final static String STUDIO                 = "studio";
  public final static String TAG                    = "tag";
  public final static String TAGS_AS_STRING         = "tagsAsString";
  public final static String THUMB                  = "thumb";
  public final static String THUMB_URL              = "thumbUrl";
  public final static String THUMB_PATH             = "thumbPath";
  public final static String TITLE                  = "title";
  public final static String TITLE_FOR_UI           = "titleForUi";
  public final static String TITLE_SORTABLE         = "titleSortable";
  public final static String TOP250                 = "top250";
  public final static String TRAILER                = "trailer";
  public final static String TV_SHOW                = "tvShow";
  public final static String TV_SHOW_COUNT          = "tvShowCount";
  public final static String TV_SHOWS               = "tvShows";
  public final static String VIDEO_CODEC            = "videoCodec";
  public final static String VIDEO_IN_3D            = "videoIn3D";
  public final static String VOTES                  = "votes";
  public final static String WATCHED                = "watched";
  public final static String WRITERS                = "writers";
  public final static String WRITERS_AS_STRING      = "writersAsString";
  public final static String YEAR                   = "year";

  // some hardcoded, well known meta data provider IDs
  // may add new ones in MediaEntity.setId()
  public final static String TMDB                   = "tmdb";
  public final static String IMDB                   = "imdb";
  public final static String TVDB                   = "tvdb";
  public final static String TRAKT                  = "trakt";
  public final static String FANART_TV              = "fanarttv";

  private Constants() {
  }
}
