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

package com.hippo.android.animator;

/*
 * Created by Hippo on 4/16/2017.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewParent;

final class Utils {
  private Utils() {}

  public static boolean getLocationInAncestor(View view, View ancestor, int[] location)
      throws IllegalArgumentException, NullPointerException {
    if (view == null) {
      throw new NullPointerException("view == null");
    }
    if (location == null || location.length < 2) {
      throw new IllegalArgumentException(
          "location must be an array of two integers");
    }

    boolean result = false;
    float[] position = new float[2];

    view.getMatrix().mapPoints(position);

    position[0] += view.getLeft();
    position[1] += view.getTop();

    ViewParent viewParent = view.getParent();
    while (viewParent instanceof View) {
      view = (View) viewParent;
      if (view == ancestor) {
        result = true;
        break;
      }

      position[0] -= view.getScrollX();
      position[1] -= view.getScrollY();

      view.getMatrix().mapPoints(position);

      position[0] += view.getLeft();
      position[1] += view.getTop();

      viewParent = view.getParent();
    }

    location[0] = (int) (position[0] + 0.5f);
    location[1] = (int) (position[1] + 0.5f);

    return result;
  }

  public static Bitmap screenshot(View v)
      throws OutOfMemoryError, IllegalArgumentException {
    int width = v.getWidth();
    int height = v.getHeight();
    if (width <= 0 || height <= 0) {
      width = v.getMeasuredWidth();
      height = v.getMeasuredHeight();
    }
    if (width <= 0 || height <= 0) {
      return null;
    }

    Bitmap bitmap;
    try {
      bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    } catch (OutOfMemoryError | IllegalArgumentException e) {
      return null;
    }
    Canvas canvas = new Canvas(bitmap);
    canvas.translate(-v.getScrollX(), -v.getScrollY());
    v.draw(canvas);
    return bitmap;
  }
}
