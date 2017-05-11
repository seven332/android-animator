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
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import com.hippo.android.animator.overlay.ViewOverlayCompat;
import com.hippo.android.animator.reveal.Revealable;
import com.hippo.android.animator.util.ArcMotion;
import com.hippo.android.animator.util.FloatProperty;
import com.hippo.android.animator.util.IntProperty;
import com.hippo.android.animator.util.PointFProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

final class AnimatorsBase {
  private AnimatorsBase() {}

  private static final String LOG_TAG = AnimatorsBase.class.getSimpleName();

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

  ///////////////////////////////////////////////////////////////////////////
  // crossFade
  ///////////////////////////////////////////////////////////////////////////

  private static final int[] TEMP_LOCATION = new int[2];

  private static final IntProperty<Drawable> DRAWABLE_ALPHA_PROPERTY;
  private static final PointFProperty<Drawable> DRAWABLE_POSITION_PROPERTY;
  private static final ArcMotion ACR_PATH_MOTION;

  static {
    DRAWABLE_ALPHA_PROPERTY = new IntProperty<Drawable>() {
      @Override
      public void setValue(Drawable object, int value) {
        object.setAlpha(value);
      }

      @Override
      public Integer get(Drawable object) {
        return null;
      }
    };

    DRAWABLE_POSITION_PROPERTY = new PointFProperty<Drawable>() {
      private Rect bounds = new Rect();

      @Override
      public void set(Drawable object, PointF value) {
        Rect bounds = this.bounds;
        object.copyBounds(bounds);
        int width = bounds.width();
        int height = bounds.height();
        int x = Math.round(value.x) - width / 2;
        int y = Math.round(value.y) - height / 2;
        bounds.offsetTo(x, y);
        object.setBounds(bounds);
      }

      @Override
      public PointF get(Drawable object) {
        object.copyBounds(bounds);
        return new PointF(bounds.left, bounds.top);
      }
    };

    ACR_PATH_MOTION = new ArcMotion();
    ACR_PATH_MOTION.setMaximumAngle(90);
    ACR_PATH_MOTION.setMinimumHorizontalAngle(15);
    ACR_PATH_MOTION.setMinimumVerticalAngle(0);
  }

  static Animator crossFade(final View from, final View to, ViewGroup ancestor, final boolean toIsTop) {
    // Ensure views is laid out
    if (!ViewCompat.isLaidOut(from) || !ViewCompat.isLaidOut(to)) {
      Log.w(LOG_TAG, "From view and to view must be laid out before calling crossFade().");
      return null;
    }

    // Get overlay
    final ViewOverlayCompat overlay = ViewOverlayCompat.from(ancestor);
    if (overlay == null) {
      Log.w(LOG_TAG, "The ancestor in crossFade() must be able to create a ViewOverlay.");
      return null;
    }

    // Get the location of from view
    if (!Utils.getLocationInAncestor(from, ancestor, TEMP_LOCATION)) {
      Log.w(LOG_TAG, "From view must be in ancestor in crossFade().");
      return null;
    }
    int fromX = TEMP_LOCATION[0];
    int fromY = TEMP_LOCATION[1];

    // Get the location of to view
    if (!Utils.getLocationInAncestor(to, ancestor, TEMP_LOCATION)) {
      Log.w(LOG_TAG, "From view must be in ancestor in crossFade().");
      return null;
    }
    int toX = TEMP_LOCATION[0];
    int toY = TEMP_LOCATION[1];

    // Get the screenshot of from view
    final Bitmap fromBitmap = Utils.screenshot(from);
    if (fromBitmap == null) {
      Log.w(LOG_TAG, "Can't screenshot from view in crossFade().");
      return null;
    }

    // Get the screenshot of to view
    final Bitmap toBitmap = Utils.screenshot(to);
    if (toBitmap == null) {
      Log.w(LOG_TAG, "Can't screenshot to view in crossFade().");
      fromBitmap.recycle();
      return null;
    }

    // Create start drawables
    final Drawable fromDrawable = new BitmapDrawable(from.getContext().getResources(), fromBitmap);
    int fromWidth = fromBitmap.getWidth();
    int fromHeight = fromBitmap.getHeight();
    fromDrawable.setBounds(fromX, fromY, fromX + fromWidth, fromY + fromHeight);

    int fromCenterX = fromX + fromWidth / 2;
    int fromCenterY = fromY + fromHeight / 2;

    // Create end drawable
    final Drawable toDrawable = new BitmapDrawable(to.getContext().getResources(), toBitmap);
    int toWidth = toBitmap.getWidth();
    int toHeight = toBitmap.getHeight();
    int toStartX = fromCenterX - toWidth / 2;
    int toStartY = fromCenterY - toHeight / 2;
    toDrawable.setBounds(toStartX, toStartY, toStartX + toWidth, toStartY + toHeight);

    int toCenterX = toX + toWidth / 2;
    int toCenterY = toY + toHeight / 2;

    List<Animator> set = new ArrayList<>(4);

    // Create alpha animators
    Animator fromAlpha = ObjectAnimator.ofInt(fromDrawable, DRAWABLE_ALPHA_PROPERTY, 255, 0);
    Animator toAlpha = ObjectAnimator.ofInt(toDrawable, DRAWABLE_ALPHA_PROPERTY, 0, 255);
    set.add(fromAlpha);
    set.add(toAlpha);

    // Create position animators
    if (fromCenterX != toCenterX || fromCenterY != toCenterY) {
      Path path = ACR_PATH_MOTION.getPath(fromCenterX, fromCenterY, toCenterX, toCenterY);
      Animator fromPosition = Animators.ofPointF(fromDrawable, DRAWABLE_POSITION_PROPERTY, path);
      Animator toPosition = Animators.ofPointF(toDrawable, DRAWABLE_POSITION_PROPERTY, path);
      set.add(fromPosition);
      set.add(toPosition);
    }

    Animator animator = Animators.playTogether(set);
    animator.addListener(new AnimatorListenerAdapter() {
      float fromAlpha;
      float toAlpha;

      @Override
      public void onAnimationStart(Animator animation) {
        // Add drawables to overlay
        if (toIsTop) {
          overlay.add(fromDrawable);
          overlay.add(toDrawable);
        } else {
          overlay.add(toDrawable);
          overlay.add(fromDrawable);
        }
        // Hide from view and to view
        fromAlpha = from.getAlpha();
        toAlpha = to.getAlpha();
        from.setAlpha(0.0f);
        to.setAlpha(0.0f);
      }

      @Override
      public void onAnimationEnd(Animator animation) {
        // Remove drawables from overlay
        overlay.remove(fromDrawable);
        overlay.remove(toDrawable);
        // Show from view and to view
        from.setAlpha(fromAlpha);
        to.setAlpha(toAlpha);
        // Recycle bitmaps
        fromBitmap.recycle();
        toBitmap.recycle();
      }
    });

    return animator;
  }

  ///////////////////////////////////////////////////////////////////////////
  // Recolor background
  ///////////////////////////////////////////////////////////////////////////

  private static final IntProperty<ColorDrawable> COLOR_DRAWABLE_COLOR_PROPERTY;

  static {
    COLOR_DRAWABLE_COLOR_PROPERTY = new IntProperty<ColorDrawable>() {
      @Override
      public void setValue(ColorDrawable object, int value) {
        object.setColor(value);
      }

      @Override
      public Integer get(ColorDrawable object) {
        return object.getColor();
      }
    };
  }

  static Animator recolorBackground(View view, int color) {
    Drawable drawable = view.getBackground();
    if (drawable instanceof ColorDrawable) {
      return recolorBackground(view, ((ColorDrawable) drawable).getColor(), color);
    } else {
      return null;
    }
  }

  static Animator recolorBackground(View view, int startColor, int endColor) {
    if (startColor != endColor) {
      Drawable drawable = view.getBackground();
      if (drawable instanceof ColorDrawable) {
        ObjectAnimator animator = ObjectAnimator.ofInt((ColorDrawable) drawable,
            COLOR_DRAWABLE_COLOR_PROPERTY, startColor, endColor);
        animator.setEvaluator(new ArgbEvaluator());
        return animator;
      }
    }
    return null;
  }
}
