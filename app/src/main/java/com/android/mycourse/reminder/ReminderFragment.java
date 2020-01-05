package com.android.mycourse.reminder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mycourse.MainActivity;
import com.android.mycourse.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import top.wefor.circularanim.CircularAnim;


public class ReminderFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private List<Todos> todosList = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private TodoAdapter todoAdapter;
    private ItemTouchHelper itemTouchHelper;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);
        setHasOptionsMenu(true);
        initView(view);
        initEvent();
        initPermission();
        return view;
    }

    // 设置工具栏菜单
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();   // 清除工具栏中的原有的其他菜单项
    }

    //回调刷新
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setDBData();
        todoAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {

        recyclerView = view.findViewById(R.id.rv_reminder);
        fab = view.findViewById(R.id.fab_reminder_new);
        layoutManager = new LinearLayoutManager(getContext());
        todoAdapter = new TodoAdapter(todosList, getContext());
        ITHCallback callback = new ITHCallback(todoAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);

    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        recyclerView.setLayoutManager(layoutManager);
        setDBData();
        todoAdapter.setOnItemClickListener(new TodoAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View view, int pos) {
                Intent intent = new Intent(getContext(), ReminderEditActivity.class);
                intent.putExtra("title", todosList.get(pos).getTitle());
                intent.putExtra("dsc", todosList.get(pos).getDesc());
                intent.putExtra("date", todosList.get(pos).getDate());
                intent.putExtra("time", todosList.get(pos).getTime());
                startActivityForResult(intent, 1);
            }
        });
        recyclerView.setAdapter(todoAdapter);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转动画
                CircularAnim.fullActivity(getActivity(), view)
                        .go(new CircularAnim.OnAnimationEndListener() {
                            @Override
                            public void onAnimationEnd() {
                                Intent intent = new Intent(getContext(), ReminderAddActivity.class);
                                startActivityForResult(intent, 1);
                            }
                        });
            }
        });
    }

    /**
     * 动态权限申请
     */
    private void initPermission() {
        String[] permission = {Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECEIVE_BOOT_COMPLETED,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY
        };
        ArrayList<String> applyList = new ArrayList<>();

        for (String per : permission) {
            if (PackageManager.PERMISSION_GRANTED !=
                    ContextCompat.checkSelfPermission(MainActivity.INSTANCE, per)) {
                applyList.add(per);
            }
        }

        String[] tmpList = new String[applyList.size()];
        if (!applyList.isEmpty()) {
            ActivityCompat.requestPermissions(
                    MainActivity.INSTANCE, applyList.toArray(tmpList), 123);
        }
    }

    private void setDBData() {
        List<Todos> localTodo = ToDoUtils.getAllTodos(getContext());
        if (localTodo.size() > 0) {
            todosList.clear();
            todosList.addAll(localTodo);
            todoAdapter.notifyDataSetChanged();
        }
    }

}