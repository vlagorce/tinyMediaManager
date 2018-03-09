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

import org.tinymediamanager.scraper.entities.Certification;

/**
 * The interface IMediaInformation is used to provide an interface to common media related infos
 * 
 * @author Manuel Laggner
 */
public interface IMediaInformation {

  /**
   * gets the certification for this video file
   * 
   * @return the certification
   */
  public Certification getCertification();

  /**
   * gets the video format (e.g. 720p).
   * 
   * @return the video format
   */
  public String getMediaInfoVideoFormat();

  /**
   * get the video resolution (e.g. 1920x1080)
   *
   * @return the video resolution
   */
  public String getMediaInfoVideoResolution();

  /**
   * gets the aspect ratio (e.g. 1.33).
   * 
   * @return the aspect ratio
   */
  public float getMediaInfoAspectRatio();

  /**
   * gets the media info video codec (e.g. divx)
   * 
   * @return the video codec
   */
  public String getMediaInfoVideoCodec();

  /**
   * get the media info frame rate (e.g. 25.0)
   *
   * @return the frame rate
   */
  public double getMediaInfoFrameRate();

  /**
   * is the video in 3D
   * 
   * @return true if the video is in 3D
   */
  public boolean isVideoIn3D();

  /**
   * gets the audio codec (e.g. mp3)
   * 
   * @return the audio codec
   */
  public String getMediaInfoAudioCodec();

  /**
   * gets the audio channels (e.g, 6 at 5.1 sound)
   * 
   * @return the audio channels with a trailing ch
   */
  public String getMediaInfoAudioChannels();

  /**
   * gets the container format
   * 
   * @return the container format
   */
  public String getMediaInfoContainerFormat();

  /**
   * gets the media source
   * 
   * @return the media source
   */
  public MediaSource getMediaInfoSource();
}
