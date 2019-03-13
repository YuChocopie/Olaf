/*
 * Copyright (C) 2017 The Android Open Source Project & Erik Huizinga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mashupgroup.weatherbear.ui.main

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View

/*
AppBarLayout의 동작에 문제가 있는 것 같아서, 동작을 수정한 Behavior를 AppBarLayout에 적용 (구글치니깐 나오네)
이것을 적용하면, 손가락을 튕겨서 맨 위/맨 아래로 스크롤 한 직후, 좌우 뷰페이져 스와이핑 등 inner 스크롤이 바로 작동되도록 할 수 있다.

손가락을 튕기면 fling이라는 동작이 스크롤뷰에 적용된 상태인데(관성으로 인해 스크롤이 이어지는 것),
fling 상태에는 이 스크롤뷰가 스크롤 포커스를 완전히 가지고있어서 스크롤이 완전히 멈추기 전 까지는 inner 스크롤이 안된다.
예를 들면 이 앱에서는, 상하 스크롤 관성이 남아있는 동안은 계속 상하스크롤만 되는 순간이라고 보면 된다.
그런데, 이게 최상단/최하단까지 스크롤 된 상태에도 관성이 남아있으면 이 관성이 없어질 때 까지 fling상태가 지속된다.
그래서 최상단에 도달해서 스크롤이 끝났음에도 좌우 inner스크롤이 잠시동안은 되지 않는 문제가 발생하게 된다.
이 문제를 수정해주는 behavior 코드이다.
stopNestedScrollIfNeeded() 메소드를 보면, 손가락이 떨어진 상태에서 스크롤이 최상단 또는 최하단에 도달했을 때
nestedScroll을 강제로 끝내는 코드가 들어있다. 스크롤을 강제로 끝내니 fling(관성)이 사라지고, 좌우 스와이핑이 즉시 가능해진다.
 */

/**
 * Workaround AppBarLayout.Behavior for https://issuetracker.google.com/66996774
 *
 *
 * See https://gist.github.com/chrisbanes/8391b5adb9ee42180893300850ed02f2 for
 * example usage.
 *
 * Kotlinised by Erik Huizinga (github: @erikhuizinga).
 */
class FixAppBarLayoutBehavior(context: Context?, attrs: AttributeSet?) :
        AppBarLayout.Behavior(context, attrs) {

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: AppBarLayout,
                                target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int,
                                dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
                dyUnconsumed, type)
        stopNestedScrollIfNeeded(dyUnconsumed, child, target, type)
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: AppBarLayout,
                                   target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        stopNestedScrollIfNeeded(dy, child, target, type)
    }

    private fun stopNestedScrollIfNeeded(dy: Int, child: AppBarLayout, target: View, type: Int) {
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            val currOffset = topAndBottomOffset
            if (dy < 0 && currOffset == 0 || dy > 0 && currOffset == -child.totalScrollRange) {
                ViewCompat.stopNestedScroll(target, ViewCompat.TYPE_NON_TOUCH)
            }
        }
    }
}