package com.android.mycourse.course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.mycourse.R;

import java.util.List;

public class CourseAdapter extends BaseAdapter {
    private Context mContext;
    private List<CourseBean> mCourses;      // 显示的课程

    CourseAdapter(Context context, List<CourseBean> courses) {
        this.mContext = context;
        this.mCourses = courses;
    }

    @Override
    public int getCount() {
        return mCourses.size();
    }

    @Override
    public Object getItem(int i) {
        return mCourses.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_course, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = view.findViewById(R.id.tv_course_name);
            viewHolder.tvTeacher = view.findViewById(R.id.tv_course_teacher);
            viewHolder.tvType = view.findViewById(R.id.tv_course_type);
            viewHolder.tvWeek = view.findViewById(R.id.tv_course_week);
            viewHolder.tvTime = view.findViewById(R.id.tv_course_time);
            viewHolder.tvPlace = view.findViewById(R.id.tv_course_place);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvName.setText(mCourses.get(i).getName());
        viewHolder.tvTeacher.setText(mCourses.get(i).getClassTeacher());
        viewHolder.tvType.setText(mCourses.get(i).getType().equals("-") ? "" : mCourses.get(i).getType());
        viewHolder.tvWeek.setText(mCourses.get(i).getClassWeek());
        viewHolder.tvTime.setText(mCourses.get(i).getClassTime());
        viewHolder.tvPlace.setText(mCourses.get(i).getClassPlace());
        return view;
    }

    class ViewHolder {
        TextView tvName;
        TextView tvTeacher;
        TextView tvType;
        TextView tvWeek;
        TextView tvTime;
        TextView tvPlace;
    }
}
