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
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import com.hippo.android.animator.reveal.Revealable;
import com.hippo.android.animator.util.FloatProperty;

final class AnimatorsBase {
  private AnimatorsBase() {}

  static Animator playTogether(Animator... animators) {
    if (animators == null || animators.length == 0) {
      return null;
    }

    Animator first = null;
    AnimatorSet set = null;
    AnimatorSet.Builder builder = null;
    for (Animator animator : animators) {
      if (animator == null) {
        continue;
      }
      if (first == null) {
        // Save first non-null animator
        first = animator;
      } else {
        if (builder == null) {
          // Get second non-null animator
          // It's time to create a AnimatorSet
          set = new AnimatorSet();
          builder = set.play(first);
        }
        builder.with(animator);
      }
    }
    return set == null ? first : set;
  }

  private static class RevealProperty extends FloatProperty<Revealable> {

    private final int x;
    private final int y;

    public RevealProperty(int x, int y) {
      this.x = x;
      this.y = y;
    }

    @Override
    public void setValue(Revealable object, float value) {
      object.setReveal(x, y, value);
    }

    @Override
    public Float get(Revealable object) {
      // Ignore
      return null;
    }
  }

  private static class RevealAnimatorListener extends AnimatorListenerAdapter {
    private Revealable revealable;

    public RevealAnimatorListener(Revealable revealable) {
      this.revealable = revealable;
    }

    @Override
    public void onAnimationStart(Animator animation) {
      revealable.setRevealEnabled(true);
    }

    @Override
    public void onAnimationEnd(Animator animation) {
      revealable.setRevealEnabled(false);
    }
  }

  static Animator circularReveal(
      View view, int centerX, int centerY, float startRadius, float endRadius) {
    if (view instanceof Revealable && startRadius != endRadius){
      Animator animator = ObjectAnimator.ofFloat((Revealable) view,
          new RevealProperty(centerX, centerY), startRadius, endRadius);
      animator.addListener(new RevealAnimatorListener((Revealable) view));
      return animator;
    } else {
      return null;
    }
  }
}
