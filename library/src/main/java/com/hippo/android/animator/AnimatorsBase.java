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
import android.animation.TypeEvaluator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.util.Property;
import android.view.View;
import com.hippo.android.animator.reveal.Revealable;
import com.hippo.android.animator.util.FloatProperty;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

final class AnimatorsBase {
  private AnimatorsBase() {}

  ///////////////////////////////////////////////////////////////////////////
  // playTogether
  ///////////////////////////////////////////////////////////////////////////

  static Animator playTogether(Animator... animators) {
    if (animators == null || animators.length == 0) {
      return null;
    } else {
      return playTogether(Arrays.asList(animators));
    }
  }

  static Animator playTogether(Collection<Animator> animators) {
    if (animators == null || animators.size() == 0) {
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

  ///////////////////////////////////////////////////////////////////////////
  // playSequentially
  ///////////////////////////////////////////////////////////////////////////

  static Animator playSequentially(Animator... animators) {
    if (animators == null || animators.length == 0) {
      return null;
    } else {
      return playSequentially(Arrays.asList(animators));
    }
  }

  static Animator playSequentially(List<Animator> animators) {
    if (animators == null || animators.size() == 0) {
      return null;
    }

    AnimatorSet set = null;
    Animator previous = null;
    for (Animator animator : animators) {
      if (animator == null) {
        continue;
      }
      if (previous == null) {
        // Save first non-null animator as previous
        previous = animator;
      } else {
        if (set == null) {
          set = new AnimatorSet();
        }
        set.play(previous).before(animator);
        // Update previous
        previous = animator;
      }
    }
    return set == null ? previous : set;
  }

  ///////////////////////////////////////////////////////////////////////////
  // ofPointF
  ///////////////////////////////////////////////////////////////////////////

  private static class LinePointFEvaluator implements TypeEvaluator<PointF> {

    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private PointF point = new PointF();

    public LinePointFEvaluator(float startX, float startY, float endX, float endY) {
      this.startX = startX;
      this.startY = startY;
      this.endX = endX;
      this.endY = endY;
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
      point.x = startX + (fraction * (endX - startX));
      point.y = startY + (fraction * (endY - startY));
      return point;
    }
  }

  private static class PathPointFEvaluator implements TypeEvaluator<PointF> {

    private PathMeasure pathMeasure;
    private float pathLength;
    private float[] array = new float[2];
    private PointF point = new PointF();

    public PathPointFEvaluator(Path path) {
      pathMeasure = new PathMeasure(path, false);
      pathLength = pathMeasure.getLength();
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
      // Ensure fraction in [0, 1]
      if (fraction < 0) {
        fraction = 0;
      }
      if (fraction > 1) {
        fraction = 1;
      }
      pathMeasure.getPosTan(fraction * pathLength, array, null);
      point.set(array[0], array[1]);
      return point;
    }
  }

  // Avoid weird behavior
  private static final PointF DUMP_POINT_F = new PointF();

  static <T> Animator ofPointF(
      T target, Property<T, PointF> property, float startX, float startY, float endX, float endY) {
    TypeEvaluator<PointF> evaluator = new LinePointFEvaluator(startX, startY, endX, endY);
    return ObjectAnimator.ofObject(target, property, evaluator, DUMP_POINT_F, DUMP_POINT_F);
  }

  static <T> Animator ofPointF(T target, Property<T, PointF> property, Path path) {
    TypeEvaluator<PointF> evaluator = new PathPointFEvaluator(path);
    return ObjectAnimator.ofObject(target, property, evaluator, DUMP_POINT_F, DUMP_POINT_F);
  }

  ///////////////////////////////////////////////////////////////////////////
  // circularReveal
  ///////////////////////////////////////////////////////////////////////////

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
