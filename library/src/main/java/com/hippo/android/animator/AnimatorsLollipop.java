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
 * Created by Hippo on 4/13/2017.
 */

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Property;
import android.view.View;
import android.view.ViewAnimationUtils;

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
final class AnimatorsLollipop {
  private AnimatorsLollipop() {}

  static <T> Animator ofPointF(T target, Property<T, PointF> property, Path path) {
    return ObjectAnimator.ofObject(target, property, null, path);
  }

  static Animator circularReveal(
      View view, int centerX, int centerY, float startRadius, float endRadius) {
    if (startRadius != endRadius) {
      return ViewAnimationUtils.createCircularReveal(
          view, centerX, centerY, startRadius, endRadius);
    } else {
      return null;
    }
  }
}
