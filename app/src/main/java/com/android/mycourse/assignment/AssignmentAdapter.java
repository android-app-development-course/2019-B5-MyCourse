package com.android.mycourse.assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.mycourse.R;

import java.util.List;

public class AssignmentAdapter extends BaseAdapter {
    private Context mContext;
    private List<AssignmentBean> mAssignments;      // 显示的课程

    AssignmentAdapter(Context context, List<AssignmentBean> assignments) {
        this.mContext = context;
        this.mAssignments = assignments;
    }

    @Override
    public int getCount() {
        return mAssignments.size();
    }

    @Override
    public Object getItem(int i) {
        return mAssignments.get(i);
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
                    .inflate(R.layout.item_assignment, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTime = view.findViewById(R.id.tv_time);
            viewHolder.tvCourse = view.findViewById(R.id.tv_course);
            viewHolder.tvTitle = view.findViewById(R.id.tv_title);
            viewHolder.tvContent = view.findViewById(R.id.tv_content);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvTime.setText(mAssignments.get(i).getTime());
        viewHolder.tvCourse.setText(mAssignments.get(i).getCourse());
        viewHolder.tvTitle.setText(mAssignments.get(i).getTitle());
        viewHolder.tvContent.setText(mAssignments.get(i).getContent());
        return view;
    }

    class ViewHolder {
        TextView tvTime;
        TextView tvCourse;
        TextView tvTitle;
        TextView tvContent;
    }
}
