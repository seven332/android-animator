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
 * Created by Hippo on 4/14/2017.
 */

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.hippo.android.animator.Animators;
import java.util.Arrays;

public class PlayTogetherActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_play_together);

    final View view1 = findViewById(R.id.view1);
    final View view2 = findViewById(R.id.view2);
    final View frame = findViewById(R.id.frame);

    findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Animator animator1 = ObjectAnimator.ofFloat(view1, View.TRANSLATION_X, 0, frame.getWidth() - view1.getWidth());
        animator1.setDuration(1000L);
        Animator animator2 = ObjectAnimator.ofFloat(view2, View.TRANSLATION_Y, 0, frame.getHeight() - view1.getHeight());
        animator2.setDuration(1000L);
        Animators.playTogether(animator1, animator2).start();
      }
    });

    findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Animator animator1 = ObjectAnimator.ofFloat(view1, View.TRANSLATION_X, 0, frame.getWidth() - view1.getWidth());
        animator1.setDuration(1000L);
        Animator animator2 = ObjectAnimator.ofFloat(view2, View.TRANSLATION_Y, 0, frame.getHeight() - view1.getHeight());
        animator2.setDuration(1000L);
        Animators.playTogether(Arrays.asList(animator1, animator2)).start();
      }
    });
  }
}
