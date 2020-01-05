package com.android.mycourse.assignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.mycourse.MainActivity;
import com.android.mycourse.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AssignmentFragment extends Fragment {

    private ArrayList<AssignmentBean> mAssignments;

    private ListView lvAssignment;
    private FloatingActionButton fabNewAssignment;
    private AssignmentHelper assignmentHelper;
    private AssignmentAdapter assignmentAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assignment, container, false);
        setHasOptionsMenu(true);
        initView(view);
        initEvent();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
    }

    // 处理添加或修改结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int index = data != null ?
                data.getIntExtra(AssignmentConstants.EXTRA_INDEX, -1) : -1;
        AssignmentBean assignment = data != null ?
                (AssignmentBean) data.getParcelableExtra(AssignmentConstants.EXTRA_ASSIGNMENT) : null;
        if (requestCode == AssignmentConstants.REQUEST_NEW) {   // 新建作业
            if (resultCode == AssignmentConstants.RESULT_INSERT) {  // 添加作业信息
                if (assignment != null) {
                    mAssignments.add(0, assignment);
                }
            }
        } else if (requestCode == AssignmentConstants.REQUEST_VIEW) {   // 查看作业
            if (resultCode == AssignmentConstants.RESULT_UPDATE) {  // 更新作业信息
                if (index >= 0 && index < mAssignments.size() && assignment != null) {
                    mAssignments.set(index, assignment);
                }
            } else if (resultCode == AssignmentConstants.RESULT_DELETE) {     // 删除作业信息
                if (index >= 0 && index < mAssignments.size()) {
                    mAssignments.remove(index);
                }
            }
        }
        assignmentAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化控件
     *
     * @param view 布局
     */
    private void initView(View view) {

        lvAssignment = view.findViewById(R.id.lv_assignment);
        fabNewAssignment = view.findViewById(R.id.fab_assignment_new);

        assignmentHelper = new AssignmentHelper(getContext());
        mAssignments = assignmentHelper.getAllAssignments();
        assignmentAdapter = new AssignmentAdapter(getContext(), mAssignments);
        lvAssignment.setAdapter(assignmentAdapter);

    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        // 点击作业列表项进入查看作业界面
        lvAssignment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), AssignmentViewActivity.class);
                intent.putExtra(AssignmentConstants.EXTRA_INDEX, i);
                intent.putExtra(AssignmentConstants.EXTRA_ASSIGNMENT, mAssignments.get(i));
                startActivityForResult(intent, AssignmentConstants.REQUEST_VIEW);
            }
        });
        // 长按作业列表删除作业
        lvAssignment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int index = i;
                // 询问是否删除作业
                new AlertDialog.Builder(MainActivity.INSTANCE)
                        .setTitle(getString(R.string.dialog_title))
                        .setMessage(String.format(
                                getString(R.string.assignment_delete_ask), mAssignments.get(index).getTitle()))
                        .setPositiveButton(getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getContext(), String.format(
                                        getString(R.string.assignment_delete_finish),
                                        mAssignments.get(index).getTitle()), Toast.LENGTH_SHORT).show();
                                assignmentHelper.deleteAssignment(mAssignments.get(index));
                                mAssignments.remove(index);
                                assignmentAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(getString(R.string.btn_no), null)
                        .show();
                return true;
            }
        });
        // 点击圆形按钮添加作业
        fabNewAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.INSTANCE, AssignmentEditActivity.class);
                intent.putExtra(AssignmentEditActivity.ARG_MODE, AssignmentEditActivity.VAL_MODE_NEW);
                startActivityForResult(intent, AssignmentConstants.REQUEST_NEW);
            }
        });
    }
}