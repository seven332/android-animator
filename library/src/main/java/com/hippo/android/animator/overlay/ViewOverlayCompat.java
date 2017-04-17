/*
 * Copyright 2017 Hippo Seven
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

package com.hippo.android.animator.overlay;

/*
 * Created by Hippo on 4/16/2017.
 */

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Compatible version of {@link android.view.ViewOverlay}.
 */
public abstract class ViewOverlayCompat {

  /**
   * Adds a {@link Drawable} to the overlay. The bounds of the drawable should be relative to
   * the host view. Any drawable added to the overlay should be removed when it is no longer
   * needed or no longer visible. Adding an already existing {@link Drawable}
   * is a no-op.
   */
  public abstract void add(@NonNull Drawable drawable);

  /**
   * Removes the specified {@link Drawable} from the overlay. Removing a {@link Drawable} that was
   * not added with {@link #add(Drawable)} is a no-op.
   */
  public abstract void remove(@NonNull Drawable drawable);

  /**
   * Removes all content from the overlay.
   */
  public abstract void clear();

  /**
   * Gets {@code ViewOverlayCompat} from a view.
   * <p>
   * {@code view} must implement {@link Overlayable} if
   * {@code Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2}.
   */
  @Nullable
  public static ViewOverlayCompat from(View view) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      return new ViewOverlayCompatJellybeanMr2(view.getOverlay());
    } else if (view instanceof Overlayable) {
      return new ViewOverlayCompatBase(((Overlayable) view).getViewOverlay());
    } else {
      return null;
    }
  }
}
