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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinymediamanager.core.Constants;
import org.tinymediamanager.core.ImageCache;
import org.tinymediamanager.core.MediaFileType;
import org.tinymediamanager.core.Message;
import org.tinymediamanager.core.Message.MessageLevel;
import org.tinymediamanager.core.MessageManager;
import org.tinymediamanager.core.Utils;
import org.tinymediamanager.core.entities.MediaEntity;
import org.tinymediamanager.core.entities.MediaFile;
import org.tinymediamanager.scraper.entities.MediaArtwork.MediaArtworkType;
import org.tinymediamanager.scraper.http.Url;

/**
 * The Class MediaEntityImageFetcherTask.
 * 
 * @author Manuel Laggner
 */
public class MediaEntityImageFetcherTask implements Runnable {
  private final static Logger LOGGER = LoggerFactory.getLogger(MediaEntityImageFetcherTask.class);

  private MediaEntity         entity;
  private String              url;
  private MediaArtworkType    type;
  private String              filename;
  private boolean             firstImage;

  public MediaEntityImageFetcherTask(MediaEntity entity, String url, MediaArtworkType type, String filename, boolean firstImage) {
    this.entity = entity;
    this.url = url;
    this.type = type;
    this.filename = filename;
    this.firstImage = firstImage;
  }

  @Override
  public void run() {
    long timestamp = System.currentTimeMillis(); // multi episode same file
    try {
      if (StringUtils.isBlank(filename)) {
        return;
      }

      // don't write jpeg -> write jpg
      if (FilenameUtils.getExtension(filename).equalsIgnoreCase("JPEG")) {
        filename = FilenameUtils.getBaseName(filename) + ".jpg";
      }

      String oldFilename = null;
      Path tempFile = null;
      try {
        // store old filename at the first image
        if (firstImage) {
          switch (type) {
            case POSTER:
            case BACKGROUND:
            case BANNER:
            case THUMB:
            case CLEARART:
            case DISC:
            case LOGO:
            case CLEARLOGO:
              oldFilename = entity.getArtworkFilename(MediaFileType.getMediaFileType(type));
              entity.removeAllMediaFiles(MediaFileType.getMediaFileType(type));
              break;

            default:
              return;
          }
        }

        // debug message
        LOGGER.debug("writing " + type + " " + filename);
        Path destFile = entity.getPathNIO().resolve(filename);
        try {
          // create a temp file/folder inside the tmm folder
          Path tempFolder = Paths.get(Constants.TEMP_FOLDER);
          if (!Files.exists(tempFolder)) {
            Files.createDirectory(tempFolder);
          }
          tempFile = tempFolder.resolve(filename + "." + timestamp + ".part"); // multi episode same file
        }
        catch (Exception e) {
          // could not create the temp folder somehow - put the files into the entity dir
          tempFile = entity.getPathNIO().resolve(filename + "." + timestamp + ".part"); // multi episode same file
        }

        // check if old and new file are the same (possible if you select it in the imagechooser)
        boolean sameFile = false;
        if (url.startsWith("file:")) {
          String newUrl = url.replace("file:/", "");
          Path file = Paths.get(newUrl);
          if (file.equals(destFile)) {
            sameFile = true;
          }
        }

        // fetch and store images
        if (!sameFile) {
          Url url1 = new Url(url);
          FileOutputStream outputStream = new FileOutputStream(tempFile.toFile());
          InputStream is = url1.getInputStream();
          if (is == null) {
            // 404 et all
            IOUtils.closeQuietly(outputStream);
            throw new FileNotFoundException("Error accessing url: " + url1.getStatusLine());
          }
          IOUtils.copy(is, outputStream);
          outputStream.flush();
          try {
            outputStream.getFD().sync(); // wait until file has been completely written
            // give it a few milliseconds
            Thread.sleep(150);
          }
          catch (Exception e) {
            // empty here -> just not let the thread crash
          }
          IOUtils.closeQuietly(outputStream);
          IOUtils.closeQuietly(is);

          // check if the file has been downloaded
          if (!Files.exists(tempFile) || Files.size(tempFile) == 0) {
            // cleanup the file
            FileUtils.deleteQuietly(tempFile.toFile());
            throw new Exception("0byte file downloaded: " + filename);
          }

          // delete the old one if exisiting
          if (StringUtils.isNotBlank(oldFilename)) {
            Path oldFile = entity.getPathNIO().resolve(oldFilename);
            Utils.deleteFileSafely(oldFile);
          }

          // delete new destination if existing
          Utils.deleteFileSafely(destFile);

          // move the temp file to the expected filename
          if (!Utils.moveFileSafe(tempFile, destFile)) {
            throw new Exception("renaming temp file failed: " + filename);
          }
        }

        // has tmm been shut down?
        if (Thread.interrupted()) {
          return;
        }

        // set the new image if its the first image
        if (firstImage) {
          LOGGER.debug("set " + type + " " + FilenameUtils.getName(filename));
          ImageCache.invalidateCachedImage(entity.getPathNIO().resolve(filename));
          switch (type) {
            case POSTER:
            case BACKGROUND:
            case BANNER:
            case THUMB:
            case CLEARART:
            case DISC:
            case LOGO:
            case CLEARLOGO:
              entity.setArtwork(destFile, MediaFileType.getMediaFileType(type));
              entity.callbackForWrittenArtwork(type);
              entity.saveToDb();
              break;

            default:
              return;
          }
        }
        else {
          MediaFile artwork = new MediaFile(destFile, MediaFileType.getMediaFileType(type));
          artwork.gatherMediaInformation();
          entity.addToMediaFiles(artwork);
        }
      }

      catch (Exception e) {
        if (e instanceof InterruptedException) {
          // only warning
          LOGGER.warn("interrupted image download");
        }
        else if (e instanceof FileNotFoundException) {
          // only warning
          LOGGER.warn(e.getMessage());
        }
        else {
          LOGGER.error("fetch image", e);
        }

        // fallback
        if (firstImage && StringUtils.isNotBlank(oldFilename)) {
          switch (type) {
            case POSTER:
            case BACKGROUND:
            case BANNER:
            case THUMB:
            case CLEARART:
            case DISC:
            case LOGO:
            case CLEARLOGO:
              entity.setArtwork(Paths.get(oldFilename), MediaFileType.getMediaFileType(type));
              entity.callbackForWrittenArtwork(type);
              entity.saveToDb();
              break;

            default:
              return;
          }
        }

        MessageManager.instance.pushMessage(
            new Message(MessageLevel.ERROR, "ArtworkDownload", "message.artwork.threadcrashed", new String[] { ":", e.getLocalizedMessage() }));
      }
      finally {
        // remove temp file
        // Path tempFile = entity.getPathNIO().resolve(filename + "." + timestamp + ".part"); // multi episode same file
        if (tempFile != null && Files.exists(tempFile)) {
          Utils.deleteFileSafely(tempFile);
        }
      }

    }
    catch (Exception e) {
      LOGGER.error("crashed thread: ", e);
    }
  }
}
