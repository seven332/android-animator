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

package com.hippo.android.animator.reveal;

/*
 * Created by Hippo on 3/30/2017.
 */

import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.view.View;

/**
 * A class to help building revealable view.
 */
public class Reveal<T extends View & Revealable> {

  private interface Clipper {
    void clip(Canvas canvas, int centerX, int centerY, float radius);
  }

  private static class ClipperBase implements Clipper {
    @Override
    public void clip(Canvas canvas, int centerX, int centerY, float radius) {
      canvas.clipRect(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
    }
  }

  // Canvas.clipPath() first supported API level is 18
  // http://developer.android.com/guide/topics/graphics/hardware-accel.html#unsupported
  private static class ClipperJellybeanMr2 implements Clipper {
    private Path path = new Path();

    @Override
    public void clip(Canvas canvas, int centerX, int centerY, float radius) {
      Path path = this.path;
      path.rewind();
      path.addCircle(centerX, centerY, radius, Path.Direction.CW);
      canvas.clipPath(path);
    }
  }

  private T view;
  private Clipper clipper;
  private boolean reveal;
  private int centerX;
  private int centerY;
  private float radius;

  public Reveal(T view) {
    this.view = view;
  }

  private void ensureClipper() {
    if (clipper == null) {
      int version = Build.VERSION.SDK_INT;
      if (version >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
        clipper = new ClipperJellybeanMr2();
      } else {
        clipper = new ClipperBase();
      }
    }
  }

  /**
   * Calls it in {@link Revealable#setRevealEnabled(boolean)}.
   */
  public void setRevealEnabled(boolean enable) {
    if (reveal != enable) {
      reveal = enable;
      if (enable) {
        ensureClipper();
      }
      view.invalidate();
    }
  }

  /**
   * Calls it in {@link Revealable#setReveal(int, int, float)}.
   */
  public void setReveal(int centerX, int centerY, float radius) {
    this.centerX = centerX;
    this.centerY = centerY;
    this.radius = radius;
    if (reveal) {
      view.invalidate();
    }
  }

  /**
   * Calls it in {@link View#draw(Canvas)}.
   */
  public void draw(Canvas canvas) {
    boolean reveal = this.reveal;
    int saveCount = 0;

    if (reveal) {
      saveCount = canvas.save();
      ensureClipper();
      clipper.clip(canvas, centerX, centerY, radius);
    }

    view.drawContent(canvas);

    if (reveal) {
      canvas.restoreToCount(saveCount);
    }
  }
}
