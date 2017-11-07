/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fullgram.messenger.exoplayer2.trackselection;

import org.fullgram.messenger.exoplayer2.ExoPlaybackException;
import org.fullgram.messenger.exoplayer2.RendererCapabilities;
import org.fullgram.messenger.exoplayer2.source.TrackGroupArray;

/** Selects tracks to be consumed by available renderers. */
public abstract class TrackSelector {

  /**
   * Notified when previous selections by a {@link TrackSelector} are no longer valid.
   */
  public interface InvalidationListener {

    /**
     * Called by a {@link TrackSelector} when previous selections are no longer valid.
     */
    void onTrackSelectionsInvalidated();

  }

  private InvalidationListener listener;

  /**
   * Initializes the selector.
   *
   * @param listener A listener for the selector.
   */
  public final void init(InvalidationListener listener) {
    this.listener = listener;
  }

  /**
   * Performs a track selection for renderers.
   *
   * @param rendererCapabilities The {@link RendererCapabilities} of the renderers for which tracks
   *     are to be selected.
   * @param trackGroups The available track groups.
   * @return A {@link TrackSelectorResult} describing the track selections.
   * @throws ExoPlaybackException If an error occurs selecting tracks.
   */
  public abstract TrackSelectorResult selectTracks(RendererCapabilities[] rendererCapabilities,
      TrackGroupArray trackGroups) throws ExoPlaybackException;

  /**
   * Called when a {@link TrackSelectorResult} previously generated by
   * {@link #selectTracks(RendererCapabilities[], TrackGroupArray)} is activated.
   *
   * @param info The value of {@link TrackSelectorResult#info} in the activated result.
   */
  public abstract void onSelectionActivated(Object info);

  /**
   * Invalidates all previously generated track selections.
   */
  protected final void invalidate() {
    if (listener != null) {
      listener.onTrackSelectionsInvalidated();
    }
  }

}
