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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.animation.Animator;
import android.animation.ValueAnimator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class AnimatorsTest {

  @Test
  public void testPlayTogether() {
    assertNull(Animators.playTogether());
    assertNull(Animators.playTogether((Animator[]) null));
    assertNull(Animators.playTogether(null, null, null));

    Animator animator = new ValueAnimator();
    assertEquals(animator, Animators.playTogether(null, animator, null));

    assertNotNull(Animators.playTogether(
        null,
        ValueAnimator.ofInt(0, 0),
        ValueAnimator.ofInt(0, 0),
        null
    ));
  }
}
