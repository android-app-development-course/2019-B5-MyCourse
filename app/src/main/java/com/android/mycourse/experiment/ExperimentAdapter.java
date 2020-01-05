package com.android.mycourse.experiment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.mycourse.R;

import java.util.List;

public class ExperimentAdapter extends BaseAdapter {
    private Context mContext;
    private List<ExperimentBean> mExperiments;      // 显示的实验

    ExperimentAdapter(Context context, List<ExperimentBean> experiments) {
        this.mContext = context;
        this.mExperiments = experiments;
    }

    @Override
    public int getCount() {
        return mExperiments.size();
    }

    @Override
    public Object getItem(int i) {
        return mExperiments.get(i);
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
                    .inflate(R.layout.item_experiment, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTime = view.findViewById(R.id.tv_time);
            viewHolder.tvCourse = view.findViewById(R.id.tv_course);
            viewHolder.tvName = view.findViewById(R.id.tv_name);
            viewHolder.tvContent = view.findViewById(R.id.tv_content);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvTime.setText(mExperiments.get(i).getTime());
        viewHolder.tvCourse.setText(mExperiments.get(i).getCourse());
        viewHolder.tvName.setText(mExperiments.get(i).getName());
        viewHolder.tvContent.setText(mExperiments.get(i).getContent());
        return view;
    }

    class ViewHolder {
        TextView tvTime;
        TextView tvCourse;
        TextView tvName;
        TextView tvContent;
    }
}
