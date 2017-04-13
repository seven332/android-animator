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

package com.hippo.android.animator.demo;

/*
 * Created by Hippo on 4/13/2017.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.hippo.android.animator.reveal.RevealableFrameLayout;

public class CircularRevealView extends RevealableFrameLayout {

  private float x;
  private float y;

  public CircularRevealView(Context context) {
    super(context);
  }

  public CircularRevealView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public CircularRevealView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    x = ev.getX();
    y = ev.getY();
    return super.dispatchTouchEvent(ev);
  }

  public float getTouchEventX() {
    return x;
  }

  public float getTouchEventY() {
    return y;
  }
}
