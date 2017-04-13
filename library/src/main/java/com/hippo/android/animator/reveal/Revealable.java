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

import android.graphics.Canvas;

public interface Revealable {

  /**
   * Controls whether revealing state of this view is enabled.
   */
  void setRevealEnabled(boolean enable);

  /**
   * Sets revealing state of this view.
   */
  void setReveal(int centerX, int centerY, float radius);

  /**
   * Draws actual content.
   * <p>
   * Usually it's {@code super.draw(canvas);}.
   */
  void drawContent(Canvas canvas);
}
