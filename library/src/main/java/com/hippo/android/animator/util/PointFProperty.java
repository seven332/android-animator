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

package com.hippo.android.animator.util;

/*
 * Created by Hippo on 4/15/2017.
 */

import android.graphics.PointF;
import android.util.Property;

/**
 * An implementation of {@link android.util.Property} to be used specifically with fields of type
 * {@link PointF}.
 *
 * @param <T> The class on which the Property is declared.
 */
public abstract class PointFProperty<T> extends Property<T, PointF> {

  public PointFProperty() {
    super(PointF.class, null);
  }

  public PointFProperty(String name) {
    super(PointF.class, name);
  }
}
