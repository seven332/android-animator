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

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

/**
 * Implement this interface to make
 * {@link com.hippo.android.animator.Animators#crossFade(View, View, ViewGroup, boolean)}
 * work if {@code android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2}.
 * Please check {@link OverlayableFrameLayout} to know how to implement it.
 */
public interface Overlayable {

  /**
   * Returns the overlay for this view, creating it if it does not yet exist.
   */
  @NonNull
  Overlay getViewOverlay();
}
