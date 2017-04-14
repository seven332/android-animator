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

public class PlaySequentiallyActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_play_sequentially);

    final View view = findViewById(R.id.view);
    final View frame = findViewById(R.id.frame);

    findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Animator animator1 = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0, frame.getWidth() - view.getWidth());
        animator1.setDuration(1000L);
        Animator animator2 = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0, frame.getHeight() - view.getHeight());
        animator2.setDuration(1000L);
        Animator animator3 = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, frame.getWidth() - view.getWidth(), 0);
        animator3.setDuration(1000L);
        Animator animator4 = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, frame.getHeight() - view.getHeight(), 0);
        animator4.setDuration(1000L);
        Animators.playSequentially(animator1, animator2, animator3, animator4).start();
      }
    });

    findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Animator animator1 = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0, frame.getWidth() - view.getWidth());
        animator1.setDuration(1000L);
        Animator animator2 = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0, frame.getHeight() - view.getHeight());
        animator2.setDuration(1000L);
        Animator animator3 = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, frame.getWidth() - view.getWidth(), 0);
        animator3.setDuration(1000L);
        Animator animator4 = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, frame.getHeight() - view.getHeight(), 0);
        animator4.setDuration(1000L);
        Animators.playSequentially(Arrays.asList(animator1, animator2, animator3, animator4)).start();
      }
    });
  }
}
