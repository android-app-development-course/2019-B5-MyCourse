package com.android.mycourse.course;

import android.content.Context;

import com.android.mycourse.R;

public class CourseUtils {

    /**
     * 获取星期
     * @param context 上下文
     * @return 星期数组
     */
    public static String[] getDays(Context context) {
        return new String[]{
                context.getString(R.string.dialog_course_time_monday),
                context.getString(R.string.dialog_course_time_tuesday),
                context.getString(R.string.dialog_course_time_wednesday),
                context.getString(R.string.dialog_course_time_thursday),
                context.getString(R.string.dialog_course_time_friday),
                context.getString(R.string.dialog_course_time_saturday),
                context.getString(R.string.dialog_course_time_sunday)};
    }

    /**
     * 将上课时间转换成固定的格式
     * @param context 上下文
     * @param i 星期
     * @param first 第一节
     * @param last 最后一节
     * @return 上课时间
     */
    public static String formatTime(Context context, int i, int first, int last) {
        String[] days = getDays(context);
        return days[i] + " " + String.format(context.getString(R.string.dialog_course_time_time),
                "" + (first == last ? first : first + "-" + last));
    }

    /**
     * 解析上课时间
     * @param context 上下文
     * @param time 上课时间
     * @return 星期、第一节、最后一节
     */
    public static int[] parseTime(Context context, String time) {
        int[] results = new int[]{-1, -1, -1};
        String[] days = getDays(context);
        String[] pars = time.split(" ");
        for (int i = 0; i < days.length; i++) {
            if (days[i].equals(pars[0])) {
                results[0] = i + 1;
                break;
            }
        }
        if (results[0] >= 0 && results[0] < days.length) {
            try {
                String[] classes = pars[2].split("-");
                results[1] = Integer.valueOf(classes[0]);
                if (classes.length == 1) {
                    results[2] = Integer.valueOf(classes[0]);
                } else {
                    results[2] = Integer.valueOf(classes[1]);
                }
            } catch (Exception ignored) {
            }
        }
        return results;
    }
}
