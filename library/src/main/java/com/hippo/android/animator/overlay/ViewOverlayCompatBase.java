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

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

class ViewOverlayCompatBase extends ViewOverlayCompat {

  private Overlay overlay;

  public ViewOverlayCompatBase(Overlay overlay) {
    this.overlay = overlay;
  }

  @Override
  public void add(@NonNull Drawable drawable) {
    overlay.add(drawable);
  }

  @Override
  public void remove(@NonNull Drawable drawable) {
    overlay.remove(drawable);
  }

  @Override
  public void clear() {
    overlay.clear();
  }
}
