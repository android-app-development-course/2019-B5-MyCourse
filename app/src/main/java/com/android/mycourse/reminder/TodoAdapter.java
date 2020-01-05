package com.android.mycourse.reminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mycourse.R;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.Collections;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {
    private List<Todos> todosList;
    private Context context;
    private MaterialDialog dialog;
    private TodoDatabase dbHelper;
    private OnItemClickListener mOnItemClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView todo_title;
        TextView todo_desc;
        TextView todo_date;
        ImageView card_background;
        TextView isRepeat;
        TimelineView timelineView;

        ViewHolder(View itemView, final OnItemClickListener onClickListener) {
            super(itemView);
            todo_title = itemView.findViewById(R.id.todo_title);
            todo_desc = itemView.findViewById(R.id.todo_desc);
            todo_date = itemView.findViewById(R.id.todo_date);
            isRepeat = itemView.findViewById(R.id.is_Repeat);
            card_background = itemView.findViewById(R.id.card_background);
            timelineView = itemView.findViewById(R.id.time_marker);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClickListener != null) {
                        int position = getAdapterPosition();
                        //确保position值有效
                        if (position != RecyclerView.NO_POSITION) {
                            onClickListener.onItemClicked(view, position);
                        }
                    }
                }
            });
        }


    }

    TodoAdapter(List<Todos> todos, Context context) {
        this.todosList = todos;
        this.context = context;
    }

    void setOnItemClickListener(OnItemClickListener clickListener) {
        this.mOnItemClickListener = clickListener;
    }

    public interface OnItemClickListener {
        void onItemClicked(View view, int pos);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_todo, viewGroup, false);
        return new ViewHolder(v, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(TodoAdapter.ViewHolder ViewHolder, final int i) {
        Todos todo = todosList.get(i);
        ViewHolder.todo_title.setText(todo.getTitle());
        ViewHolder.todo_desc.setText(todo.getDesc());
        ViewHolder.todo_date.setText(todo.getDate() + " " + todo.getTime());
        ViewHolder.card_background.setImageBitmap(BitmapUtils.readBitMap(context, todo.getImgId()));
        if (todo.getIsRepeat() == 1) {
            ViewHolder.isRepeat.setText("重复");
            ViewHolder.isRepeat.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        } else {
            ViewHolder.isRepeat.setText("单次");
            ViewHolder.isRepeat.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        }
        if (todo.getRemindTime() <= System.currentTimeMillis()) {
            ViewHolder.timelineView.setMarker(context.getResources().getDrawable(R.drawable.ic_marker));
        } else {
            ViewHolder.timelineView.setMarker(context.getResources().getDrawable(R.drawable.round));
        }
    }

    @Override
    public int getItemCount() {
        return todosList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(todosList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        notifyItemRangeChanged(fromPosition, toPosition);
    }

    @Override
    public void onItemDissmiss(int position) {
        final int p = position;
        dialog = new MaterialDialog(context);
        dialog.setMessage("确定删除？")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Todos todos = todosList.get(p);
                        dbHelper = new TodoDatabase(context, "Data.db", null, 2);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete("Todo", "todotitle = ?",
                                new String[]{todos.getTitle()});
                        todosList.remove(p);
                        notifyItemRemoved(p);
                        notifyItemRangeChanged(p, todosList.size());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    public void onClick(View view) {
                        notifyItemChanged(p);
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }
}
