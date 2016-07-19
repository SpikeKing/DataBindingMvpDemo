package org.wangchenlong.mvpdatabindingdemo.tasks;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wangchenlong.mvpdatabindingdemo.Injection;
import org.wangchenlong.mvpdatabindingdemo.MainActivity;
import org.wangchenlong.mvpdatabindingdemo.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.base.Preconditions.checkArgument;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

/**
 * 任务页面测试
 * Created by wangchenlong on 16/7/19.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainScreenTest {

    private final static String TITLE_1 = "TITLEX";
    private final static String DESCRIPTION = "DESCRIPTION";
    private final static String TITLE_2 = "TITLEY";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class) {
                @Override protected void beforeActivityLaunched() {
                    super.beforeActivityLaunched();
                    // 启动之前删除所有数据Tasks
                    Injection.provideTasksRepository(InstrumentationRegistry.getTargetContext())
                            .deleteAllTasks();
                }
            };

    /**
     * 匹配ListView的Item项
     *
     * @param itemText item匹配
     * @return 给定的匹配View
     */
    private Matcher<View> withItemText(final String itemText) {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty");
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View item) {
                return allOf(isDescendantOfA(isAssignableFrom(ListView.class)),
                        withText(itemText)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is isDescendantOfA LV with text " + itemText);
            }
        };
    }

    /**
     * 测试Fab按钮是否添加任务
     */
    @Test
    public void clickAddTaskButton_opensAddTaskUi() {
        // 点击Fab按钮
        onView(withId(R.id.main_fab_add_task)).perform(click());

        // 添加任务页面的标题是否显示
        onView(withId(R.id.task_add_et_title)).check(matches(isDisplayed()));
    }

    /**
     * 编辑任务
     *
     * @throws Exception
     */
    @Test
    public void editTask() throws Exception {
        createTask(TITLE_1, DESCRIPTION); // 添加任务

        Thread.sleep(5000);

        // 点击任务
        onView(withText(TITLE_1)).perform(click());

        // 修改任务
        onView(withId(R.id.task_detail_fab_edit_task)).perform(click());

        String editTaskTitle = TITLE_2;
        String editTaskDescription = "New Description";

        // 编辑任务标题
        onView(withId(R.id.task_add_et_title))
                .perform(replaceText(editTaskTitle), closeSoftKeyboard());

        // 编辑任务详情
        onView(withId(R.id.task_add_et_description))
                .perform(replaceText(editTaskDescription), closeSoftKeyboard());

        // 点击编辑完成任务
        onView(withId(R.id.task_add_fab_edit_task_done)).perform(click());

        // 检测新的标题是否正确
        onView(withItemText(editTaskTitle)).check(matches(isDisplayed()));

        // 检测Title1不存在
        onView(withItemText(TITLE_1)).check(doesNotExist());
    }

    /**
     * 创建任务, 使用TaskAdd页面
     *
     * @param title       标题
     * @param description 描述
     */
    private void createTask(String title, String description) {
        onView(withId(R.id.main_fab_add_task)).perform(click());

        // 写入Title
        onView(withId(R.id.task_add_et_title))
                .perform(typeText(title), closeSoftKeyboard());

        // 写入Description
        onView(withId(R.id.task_add_et_description))
                .perform(typeText(description), closeSoftKeyboard());

        // 点击完成任务
        onView(withId(R.id.task_add_fab_edit_task_done)).perform(click());
    }
}
