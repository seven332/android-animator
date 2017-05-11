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
 * Created by Hippo on 3/30/2017.
 */

import android.animation.Animator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.hippo.android.animator.Animators;

public class RecolorTextActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recolor_text);

    final TextView text = (TextView) findViewById(R.id.text);
    text.setOnClickListener(new View.OnClickListener() {
      private boolean toggle;
      @Override
      public void onClick(View v) {
        toggle = !toggle;
        Animator animator = Animators.recolorText(text,
            toggle ? Color.RED : Color.BLUE,
            toggle ? Color.BLUE : Color.RED);
        if (animator != null) {
          animator.setDuration(1000);
          animator.start();
        }
      }
    });
  }
}
