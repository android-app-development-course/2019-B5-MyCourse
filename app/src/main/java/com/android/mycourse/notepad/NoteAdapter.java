package com.android.mycourse.notepad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.mycourse.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoteAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private List<NoteBean> mAllNotes;   // 所有备忘
    private List<NoteBean> mNotes;      // 显示的备忘
    private NoteFilter mNoteFilter;

    NoteAdapter(Context context, List<NoteBean> notes) {
        this.mContext = context;
        this.mAllNotes = notes;
        this.mNotes = notes;
    }

    @Override
    public int getCount() {
        return mNotes.size();
    }

    @Override
    public Object getItem(int i) {
        return mNotes.get(i);
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
                    .inflate(R.layout.item_notepad, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTime = view.findViewById(R.id.tv_time);
            viewHolder.tvTitle = view.findViewById(R.id.tv_name);
            viewHolder.tvContent = view.findViewById(R.id.tv_content);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        // 若备忘的创建时间为同一年，则只显示月日和时间，否则显示完整时间
        String year = String.format("%tY", new Date());
        String time = mNotes.get(i).getTime();
        viewHolder.tvTime.setText(time.substring(0, 4).equals(year) ? time.substring(5) : time);
        viewHolder.tvTitle.setText(mNotes.get(i).getTitle());
        viewHolder.tvContent.setText(mNotes.get(i).getContent());
        return view;
    }

    class ViewHolder {
        TextView tvTime;
        TextView tvTitle;
        TextView tvContent;
    }

    @Override
    public Filter getFilter() {
        if (null == mNoteFilter) {
            mNoteFilter = new NoteFilter();
        }
        return mNoteFilter;
    }

    /**
     * 自定义备忘过滤器
     */
    class NoteFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String filter = charSequence.toString().trim().toLowerCase();   // 搜索词
            List<NoteBean> values;
            if (filter.equals("")) {    // 未输入搜索词
                values = mAllNotes;     // 返回全部备忘
            } else {
                values = new ArrayList<>();
                for (NoteBean note : mAllNotes) {
                    if (note.getTitle().toLowerCase().contains(filter) ||
                            note.getContent().toLowerCase().contains(filter)) {
                        values.add(note);   // 返回标题或内容含搜索词的备忘
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = values;
            filterResults.count = values.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mNotes = (List<NoteBean>) filterResults.values;     // 获取用于显示的备忘
            if (filterResults.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
