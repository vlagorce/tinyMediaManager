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
package org.tinymediamanager.core.tasks;

import static org.tinymediamanager.core.Constants.MEDIA_INFORMATION;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinymediamanager.core.Message;
import org.tinymediamanager.core.Message.MessageLevel;
import org.tinymediamanager.core.MessageManager;
import org.tinymediamanager.core.entities.MediaEntity;
import org.tinymediamanager.core.entities.MediaFile;
import org.tinymediamanager.core.movie.entities.Movie;
import org.tinymediamanager.core.threading.TmmTaskManager;
import org.tinymediamanager.core.tvshow.entities.TvShowEpisode;

/**
 * The Class MediaFileInformationFetcherTask.
 * 
 * @author Manuel Laggner
 */
public class MediaFileInformationFetcherTask implements Callable<Object> {
  private final static Logger LOGGER = LoggerFactory.getLogger(MediaFileInformationFetcherTask.class);

  private List<MediaFile>     mediaFiles;
  private MediaEntity         mediaEntity;
  private long                uniqueId;
  private boolean             forceUpdate;

  /**
   * Instantiates a new media file information fetcher task.
   * 
   * @param mediaFile
   *          the media files
   * @param mediaEntity
   *          the media entity
   * @param forceUpdate
   *          force an update
   */
  public MediaFileInformationFetcherTask(MediaFile mediaFile, MediaEntity mediaEntity, boolean forceUpdate) {
    this.mediaFiles = new ArrayList<>();
    this.mediaFiles.add(mediaFile);
    this.mediaEntity = mediaEntity;
    this.forceUpdate = forceUpdate;
    this.uniqueId = TmmTaskManager.getInstance().GLOB_THRD_CNT.incrementAndGet();
  }

  /**
   * Instantiates ONE new media file information fetcher task for ALL files<br>
   * better to submit one file after another.. for status bar et all
   * 
   * @param mediaFiles
   *          the media files
   * @param mediaEntity
   *          the media entity
   * @param forceUpdate
   *          force an update
   */
  @Deprecated
  public MediaFileInformationFetcherTask(List<MediaFile> mediaFiles, MediaEntity mediaEntity, boolean forceUpdate) {
    this.mediaFiles = mediaFiles;
    this.mediaEntity = mediaEntity;
    this.forceUpdate = forceUpdate;
    this.uniqueId = TmmTaskManager.getInstance().GLOB_THRD_CNT.incrementAndGet();
  }

  @Override
  public String call() {
    // try/catch block in the root of the thread to log crashes
    try {
      String name = Thread.currentThread().getName();
      if (!name.contains("-G")) {
        name = name + "-G0";
      }
      name = name.replaceAll("\\-G\\d+", "-G" + uniqueId);
      Thread.currentThread().setName(name);

      for (MediaFile mediaFile : mediaFiles) {
        mediaFile.gatherMediaInformation(forceUpdate);
        if (mediaEntity != null && mediaEntity instanceof Movie && mediaFile.hasSubtitles()) {
          Movie movie = (Movie) mediaEntity;
          movie.setSubtitles(true);
        }
        if (mediaEntity != null && mediaEntity instanceof TvShowEpisode && mediaFile.hasSubtitles()) {
          TvShowEpisode episode = (TvShowEpisode) mediaEntity;
          episode.setSubtitles(true);
        }
      }
    }
    catch (Exception e) {
      LOGGER.error("Thread crashed: ", e);
      MessageManager.instance.pushMessage(
          new Message(MessageLevel.ERROR, "MediaInformation", "message.mediainfo.threadcrashed", new String[] { ":", e.getLocalizedMessage() }));
    }

    if (mediaEntity != null) {
      mediaEntity.saveToDb();
      mediaEntity.firePropertyChange(MEDIA_INFORMATION, false, true);
      return "getting MediaInfo from " + mediaEntity.getTitle();
    }

    return "getting MediaInfo";
  }
}
