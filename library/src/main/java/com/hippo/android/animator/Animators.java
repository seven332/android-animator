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
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;

public final class Animators {
  private Animators() {}

  private interface AnimatorsImpl {
    Animator circularReveal(
        View view, int centerX, int centerY, float startRadius, float endRadius);
  }

  private static class BaseAnimatorsImpl implements AnimatorsImpl {
    @Override
    public Animator circularReveal(
        View view, int centerX, int centerY, float startRadius, float endRadius) {
      return AnimatorsBase.circularReveal(view, centerX, centerY, startRadius, endRadius);
    }
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  private static class LollipopAnimatorsImpl extends BaseAnimatorsImpl {
    @Override
    public Animator circularReveal(
        View view, int centerX, int centerY, float startRadius, float endRadius) {
      return AnimatorsLollipop.circularReveal(view, centerX, centerY, startRadius, endRadius);
    }
  }

  private static final AnimatorsImpl IMPL;
  static {
    final int version = android.os.Build.VERSION.SDK_INT;
    if (version >= Build.VERSION_CODES.LOLLIPOP) {
      IMPL = new LollipopAnimatorsImpl();
    } else {
      IMPL = new BaseAnimatorsImpl();
    }
  }

  /**
   * Creates circular reveal animation.
   * <p>
   * Returns {@code null} if can't create it or no need to create it.
   */
  @Nullable
  public static Animator circularReveal(
      View view, int centerX, int centerY, float startRadius, float endRadius) {
    return IMPL.circularReveal(view, centerX, centerY, startRadius, endRadius);
  }
}
