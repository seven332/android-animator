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
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import java.util.Collection;
import java.util.List;

public final class Animators {
  private Animators() {}

  private interface AnimatorsImpl {
    Animator playTogether(Animator... animators);
    Animator playTogether(Collection<Animator> animators);
    Animator playSequentially(Animator... animators);
    Animator playSequentially(List<Animator> animators);
    <T> Animator ofPointF(
        T target, Property<T, PointF> property, float startX, float startY, float endX, float endY);
    <T> Animator ofPointF(T target, Property<T, PointF> property, Path path);
    Animator circularReveal(
        View view, int centerX, int centerY, float startRadius, float endRadius);
    Animator crossFade(View from, View to, ViewGroup ancestor, boolean toIsTop);
  }

  private static class BaseAnimatorsImpl implements AnimatorsImpl {

    @Override
    public Animator playTogether(Animator... animators) {
      return AnimatorsBase.playTogether(animators);
    }

    @Override
    public Animator playTogether(Collection<Animator> animators) {
      return AnimatorsBase.playTogether(animators);
    }

    @Override
    public Animator playSequentially(Animator... animators) {
      return AnimatorsBase.playSequentially(animators);
    }

    @Override
    public Animator playSequentially(List<Animator> animators) {
      return AnimatorsBase.playSequentially(animators);
    }

    @Override
    public <T> Animator ofPointF(
        T target, Property<T, PointF> property, float startX, float startY, float endX, float endY) {
      return AnimatorsBase.ofPointF(target, property, startX, startY, endX, endY);
    }

    @Override
    public <T> Animator ofPointF(T target, Property<T, PointF> property, Path path) {
      return AnimatorsBase.ofPointF(target, property, path);
    }

    @Override
    public Animator circularReveal(
        View view, int centerX, int centerY, float startRadius, float endRadius) {
      return AnimatorsBase.circularReveal(view, centerX, centerY, startRadius, endRadius);
    }

    @Override
    public Animator crossFade(View from, View to, ViewGroup ancestor, boolean toIsTop) {
      return AnimatorsBase.crossFade(from, to, ancestor, toIsTop);
    }
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  private static class LollipopAnimatorsImpl extends BaseAnimatorsImpl {
    @Override
    public <T> Animator ofPointF(T target, Property<T, PointF> property, Path path) {
      return AnimatorsLollipop.ofPointF(target, property, path);
    }

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
   * Creates an Animator to play all of the supplied animations at the same time.
   * Returns {@code null} if there is no valid animator.
   * Returns the first valid animator if there is only one valid animator.
   * Returns an AnimatorSet if there are more the one animators.
   */
  @Nullable
  public static Animator playTogether(Animator... animators) {
    return IMPL.playTogether(animators);
  }

  /**
   * Creates an Animator to play all of the supplied animations at the same time.
   * Returns {@code null} if there is no valid animator.
   * Returns the first valid animator if there is only one valid animator.
   * Returns an AnimatorSet if there are more the one animators.
   */
  @Nullable
  public static Animator playTogether(Collection<Animator> animators) {
    return IMPL.playTogether(animators);
  }

  /**
   * Creates an Animator to play each of the supplied animations when the previous animation ends.
   * Returns {@code null} if there is no valid animator.
   * Returns the first valid animator if there is only one valid animator.
   * Returns an AnimatorSet if there are more the one animators.
   */
  @Nullable
  public static Animator playSequentially(Animator... animators) {
    return IMPL.playSequentially(animators);
  }

  /**
   * Creates an Animator to play each of the supplied animations when the previous animation ends.
   * Returns {@code null} if there is no valid animator.
   * Returns the first valid animator if there is only one valid animator.
   * Returns an AnimatorSet if there are more the one animators.
   */
  @Nullable
  public static Animator playSequentially(List<Animator> animators) {
    return IMPL.playSequentially(animators);
  }

  /**
   * Creates an Animator that animates a property along a line
   * which is from start point to end point.
   * The animation moves in two dimensions, animating coordinates
   * {@code (x, y)} together to follow the line. This variant animates the coordinates
   * in a {@link PointF} to follow the line.
   */
  @NonNull
  public static <T> Animator ofPointF(
      @NonNull T target, @NonNull Property<T, PointF> property,
      float startX, float startY, float endX, float endY) {
    return IMPL.ofPointF(target, property, startX, startY, endX, endY);
  }

  /**
   * Creates an Animator that animates a property along a {@link Path}.
   * The animation moves in two dimensions, animating coordinates
   * {@code (x, y)} together to follow the line. This variant animates the coordinates
   * in a {@link PointF} to follow the {@code Path}.
   */
  @NonNull
  public static <T> Animator ofPointF(
      @NonNull T target, @NonNull Property<T, PointF> property, @NonNull Path path) {
    return IMPL.ofPointF(target, property, path);
  }

  /**
   * Creates a circular reveal animator.
   * Returns {@code null} if can't create it or no need to create it.
   * <p>
   * Make sure the {@code view} implement {@link com.hippo.android.animator.reveal.Revealable}
   * if {@code android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP}.
   */
  @Nullable
  public static Animator circularReveal(
      View view, int centerX, int centerY, float startRadius, float endRadius) {
    return IMPL.circularReveal(view, centerX, centerY, startRadius, endRadius);
  }

  /**
   * Creates a cross fade animator between from-view and to-view.
   * Returns {@code null} if from-view or to-view isn't laid out, or
   * {@code ancestor} is not the ancestor of any of from-view and to-view,
   * or can't create overlay from {@code ancestor}, or something wrong happened.
   * <p>
   * The alpha values of from-view and to-view will be set to 0 when the animator starts.
   * Instead screenshot of the views shows on the overlay.
   * When the animator ends, the alpha values of from-view and to-view will come back.
   * <p>
   * From-view and to-view should not be too large.
   *
   * @param ancestor the common ancestor of both from-view and to-view,
   *                 at the same time {@link com.hippo.android.animator.overlay.ViewOverlayCompat}
   *                 must can be created from it.
   * @param toIsTop {@code true} if the to-view is on the top of from-view.
   */
  @Nullable
  public static Animator crossFade(
      @Nullable View from, @Nullable View to, @Nullable ViewGroup ancestor, boolean toIsTop) {
    return IMPL.crossFade(from, to, ancestor, toIsTop);
  }
}
