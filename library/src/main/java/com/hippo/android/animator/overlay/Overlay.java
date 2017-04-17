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

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import java.util.ArrayList;

/**
 * ViewOverlay for {@code android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2}.
 */
public class Overlay {

  private final View host;
  private final ArrayList<Drawable> drawables = new ArrayList<>();

  public Overlay(View host) {
    this.host = host;
  }

  /**
   * Adds a {@link Drawable} to the overlay. The bounds of the drawable should be relative to
   * the host view. Any drawable added to the overlay should be removed when it is no longer
   * needed or no longer visible. Adding an already existing {@link Drawable}
   * is a no-op.
   */
  public void add(@NonNull Drawable drawable) {
    if (!drawables.contains(drawable)) {
      // Make each drawable unique in the overlay; can't add it more than once
      drawables.add(drawable);
      host.invalidate(drawable.getBounds());
      drawable.setCallback(host);
    }
  }

  /**
   * Removes the specified {@link Drawable} from the overlay. Removing a {@link Drawable} that was
   * not added with {@link #add(Drawable)} is a no-op.
   */
  public void remove(@NonNull Drawable drawable) {
    drawables.remove(drawable);
    host.invalidate(drawable.getBounds());
    drawable.setCallback(null);
  }

  /**
   * Removes all content from the overlay.
   */
  public void clear() {
    ArrayList<Drawable> drawables = this.drawables;
    for (int i = 0, n = drawables.size(); i < n; ++i) {
      drawables.get(i).setCallback(null);
    }
    drawables.clear();
  }

  /**
   * Call it in {@link View#verifyDrawable(Drawable)}.
   */
  public boolean verifyDrawable(Drawable who) {
    return drawables.contains(who);
  }

  /**
   * Call it in {@link View#dispatchDraw(Canvas)}.
   */
  public void draw(Canvas canvas) {
    ArrayList<Drawable> drawables = this.drawables;
    for (int i = 0, n = drawables.size(); i < n; ++i) {
      drawables.get(i).draw(canvas);
    }
  }
}
