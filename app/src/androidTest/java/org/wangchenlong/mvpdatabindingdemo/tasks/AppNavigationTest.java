package org.wangchenlong.mvpdatabindingdemo.tasks;

import android.annotation.SuppressLint;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.Gravity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wangchenlong.mvpdatabindingdemo.MainActivity;
import org.wangchenlong.mvpdatabindingdemo.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.wangchenlong.mvpdatabindingdemo.custom.action.NavigationViewActions.navigateTo;

/**
 * 抽屉导航栏的测试, MainActivity的测试
 * <p>
 * Created by wangchenlong on 16/7/18.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AppNavigationTest {
    // 测试MainActivity
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    // 点击统计项
    @Test
    public void clickOnStatisticsNavigationItem_ShowStatisticsScreen() {
        // 点击抽屉, 从左侧弹出
        onView(withId(R.id.main_dl_drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(open());

        // 开始统计页面
        onView(withId(R.id.main_nv_nav_view))
                .perform(navigateTo(R.id.statistic_navi_menu_item));

        // 统计显示
        onView(withId(R.id.statistic_ll_content)).check(matches(isDisplayed()));
    }

    /**
     * 注意页面的Id, 不要写错.
     */
    @Test
    public void clickOnListNavigationItem_ShowListScreen() {
        onView(withId(R.id.main_dl_drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(open());

        onView(withId(R.id.main_nv_nav_view))
                .perform(navigateTo(R.id.statistic_navi_menu_item));

        onView(withId(R.id.statistic_dl_drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(open());

        onView(withId(R.id.statistic_nv_nav_view))
                .perform(navigateTo(R.id.list_navi_menu_item));

        onView(withId(R.id.tasks_rl_container)).check(matches(isDisplayed()));
    }

    /**
     * 点击Home的Icon
     */
    @SuppressLint("PrivateResource") @Test
    public void clickOnAndroidHomeIcon_OpensNavigation() {
        onView(withId(R.id.main_dl_drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)));

        // 点击Home键
        onView(withContentDescription(R.string.abc_action_bar_up_description))
                .perform(click());

        onView(withId(R.id.main_dl_drawer_layout))
                .check(matches(isOpen(Gravity.LEFT)));
    }
}
