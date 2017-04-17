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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Overlayable {@link LinearLayout}.
 */
public class OverlayableLinearLayout extends LinearLayout implements Overlayable {

  private Overlay overlay;

  public OverlayableLinearLayout(Context context) {
    super(context);
  }

  public OverlayableLinearLayout(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public OverlayableLinearLayout(
      Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected boolean verifyDrawable(@NonNull Drawable who) {
    return super.verifyDrawable(who) || (overlay != null && overlay.verifyDrawable(who));
  }

  @Override
  protected void dispatchDraw(Canvas canvas) {
    super.dispatchDraw(canvas);
    if (overlay != null) {
      overlay.draw(canvas);
    }
  }

  @NonNull
  @Override
  public Overlay getViewOverlay() {
    if (overlay == null) {
      overlay = new Overlay(this);
    }
    return overlay;
  }
}
