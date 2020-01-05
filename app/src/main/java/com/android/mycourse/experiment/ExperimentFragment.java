package com.android.mycourse.experiment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class ExperimentFragment extends Fragment {

    private ArrayList<ExperimentBean> mExperiments;

    private ListView lvExperiments;
    private FloatingActionButton fabNewExperiment;
    private ExperimentHelper experimentHelper;
    private ExperimentAdapter experimentAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_experiment, container, false);
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
                data.getIntExtra(ExperimentConstants.EXTRA_INDEX, -1) : -1;
        ExperimentBean experiment = data != null ?
                (ExperimentBean) data.getParcelableExtra(ExperimentConstants.EXTRA_EXPERIMENT) : null;
        if (requestCode == ExperimentConstants.REQUEST_NEW) {   // 新建实验
            if (resultCode == ExperimentConstants.RESULT_INSERT) {  // 添加实验信息
                if (experiment != null) {
                    mExperiments.add(0, experiment);
                }
            }
        } else if (requestCode == ExperimentConstants.REQUEST_VIEW) {   // 查看实验
            if (resultCode == ExperimentConstants.RESULT_UPDATE) {  // 更新实验信息
                if (index >= 0 && index < mExperiments.size() && experiment != null) {
                    mExperiments.set(index, experiment);
                }
            } else if (resultCode == ExperimentConstants.RESULT_DELETE) {     // 删除实验信息
                if (index >= 0 && index < mExperiments.size()) {
                    mExperiments.remove(index);
                }
            }
        }
        experimentAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化控件
     *
     * @param view 布局
     */
    private void initView(View view) {

        lvExperiments = view.findViewById(R.id.lv_experiment);
        fabNewExperiment = view.findViewById(R.id.fab_experiment_new);

        experimentHelper = new ExperimentHelper(getContext());
        mExperiments = experimentHelper.getAllExperiments();
        experimentAdapter = new ExperimentAdapter(getContext(), mExperiments);
        lvExperiments.setAdapter(experimentAdapter);

    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        // 点击实验列表项进入查看实验界面
        lvExperiments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), ExperimentViewActivity.class);
                intent.putExtra(ExperimentConstants.EXTRA_INDEX, i);
                intent.putExtra(ExperimentConstants.EXTRA_EXPERIMENT, mExperiments.get(i));
                startActivityForResult(intent, ExperimentConstants.REQUEST_VIEW);
            }
        });
        // 长按实验列表删除实验
        lvExperiments.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int index = i;
                // 询问是否删除实验
                new AlertDialog.Builder(MainActivity.INSTANCE)
                        .setTitle(getString(R.string.dialog_title))
                        .setMessage(String.format(
                                getString(R.string.experiment_delete_ask), mExperiments.get(index).getName()))
                        .setPositiveButton(getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getContext(), String.format(
                                        getString(R.string.experiment_delete_finish),
                                        mExperiments.get(index).getName()), Toast.LENGTH_SHORT).show();
                                experimentHelper.deleteExperiment(mExperiments.get(index));
                                mExperiments.remove(index);
                                experimentAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(getString(R.string.btn_no), null)
                        .show();
                return true;
            }
        });
        // 点击圆形按钮添加实验
        fabNewExperiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.INSTANCE, ExperimentEditActivity.class);
                intent.putExtra(ExperimentEditActivity.ARG_MODE, ExperimentEditActivity.VAL_MODE_NEW);
                startActivityForResult(intent, ExperimentConstants.REQUEST_NEW);
            }
        });
    }
}