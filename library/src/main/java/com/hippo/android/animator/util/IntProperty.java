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
 * Created by Hippo on 3/30/2017.
 */

import android.util.Property;

/**
 * An implementation of {@link android.util.Property} to be used specifically with fields of type
 * <code>int</code>. This type-specific subclass enables performance benefit by allowing
 * calls to a {@link #setValue(Object, int) setValue()} function that takes the primitive
 * <code>int</code> type and avoids autoboxing and other overhead associated with the
 * <code>Integer</code> class.
 *
 * @param <T> The class on which the Property is declared.
 */
public abstract class IntProperty<T> extends Property<T, Integer> {

  public IntProperty() {
    super(Integer.class, null);
  }

  public IntProperty(String name) {
    super(Integer.class, name);
  }

  /**
   * A type-specific variant of {@link #set(Object, Integer)} that is faster when dealing
   * with fields of type <code>int</code>.
   */
  public abstract void setValue(T object, int value);

  @Override
  public final void set(T object, Integer value) {
    setValue(object, value);
  }
}
