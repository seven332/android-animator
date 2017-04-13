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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Revealable {@link View}.
 */
public class RevealableView extends View implements Revealable {

  private Reveal<RevealableView> reveal = new Reveal<>(this);

  public RevealableView(Context context) {
    super(context);
  }

  public RevealableView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public RevealableView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public void setRevealEnabled(boolean enable) {
    reveal.setRevealEnabled(enable);
  }

  @Override
  public void setReveal(int centerX, int centerY, float radius) {
    reveal.setReveal(centerX, centerY, radius);
  }

  @Override
  public void drawContent(Canvas canvas) {
    super.draw(canvas);
  }

  @SuppressLint("MissingSuperCall")
  @Override
  public void draw(Canvas canvas) {
    reveal.draw(canvas);
  }
}
