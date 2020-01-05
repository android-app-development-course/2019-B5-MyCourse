package com.android.mycourse.experiment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mycourse.R;

public class ExperimentViewActivity extends AppCompatActivity {

    private int mIndex;
    private ExperimentBean mExperiment;

    private TextView tvSemester;
    private TextView tvCourse;
    private TextView tvName;
    private TextView tvTime;
    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment_view);
        initView();     // 初始化控件
        updateView();   // 控件装入数据
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_experiment_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // 返回主界面
            case android.R.id.home:
                finish();
                break;
            // 编辑实验
            case R.id.action_experiment_view_edit:
                Intent intent = new Intent(this, ExperimentEditActivity.class);
                intent.putExtra(ExperimentEditActivity.ARG_MODE, ExperimentEditActivity.VAL_MODE_EDIT);
                intent.putExtra(ExperimentConstants.EXTRA_EXPERIMENT, mExperiment);
                startActivityForResult(intent, ExperimentConstants.REQUEST_EDIT);  // 进入编辑界面
                break;
            // 删除实验
            case R.id.action_experiment_view_delete:
                // 询问是否删除实验
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.dialog_title))
                        .setMessage(String.format(getString(R.string.experiment_delete_ask),
                                mExperiment.getName()))
                        .setPositiveButton(getString(R.string.btn_yes),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ExperimentHelper experimentHelper =
                                                new ExperimentHelper(ExperimentViewActivity.this);
                                        experimentHelper.deleteExperiment(mExperiment);
                                        Intent intent = new Intent();
                                        intent.putExtra(ExperimentConstants.EXTRA_INDEX, mIndex);
                                        setResult(ExperimentConstants.RESULT_DELETE, intent);
                                        Toast.makeText(ExperimentViewActivity.this, String.format(
                                                getString(R.string.experiment_delete_finish),
                                                mExperiment.getName()), Toast.LENGTH_SHORT).show();
                                        finish();   // 返回删除主界面的实验
                                    }
                                })
                        .setNegativeButton(getString(R.string.btn_no), null)
                        .show();
                break;
        } // switch end
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected end

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ExperimentConstants.REQUEST_EDIT) {    // 编辑实验
            if (resultCode == ExperimentConstants.RESULT_UPDATE) {   // 更新实验
                mExperiment = data != null ? (ExperimentBean)
                        data.getParcelableExtra(ExperimentConstants.EXTRA_EXPERIMENT) : null;
                if (mExperiment != null) {
                    updateView();   // 更新控件数据
                    // 回传数据
                    Intent intent = new Intent();
                    intent.putExtra(ExperimentConstants.EXTRA_INDEX, mIndex);
                    intent.putExtra(ExperimentConstants.EXTRA_EXPERIMENT, mExperiment);
                    setResult(ExperimentConstants.RESULT_UPDATE, intent);
                }
            }
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {

        Intent intent = getIntent();
        mIndex = intent != null ?
                intent.getIntExtra(ExperimentConstants.EXTRA_INDEX, -1) : -1;
        mExperiment = intent != null ? (ExperimentBean)
                intent.getParcelableExtra(ExperimentConstants.EXTRA_EXPERIMENT) : null;

        tvSemester = findViewById(R.id.tv_experiment_view_semester);
        tvCourse = findViewById(R.id.tv_experiment_view_course);
        tvName = findViewById(R.id.tv_experiment_view_name);
        tvTime = findViewById(R.id.tv_experiment_view_time);
        tvContent = findViewById(R.id.tv_experiment_view_content);
        // 工具栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.experiment_view_title));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    /**
     * 更新控件数据
     */
    private void updateView() {
        if (mExperiment != null) {
            tvSemester.setText(mExperiment.getSemester());
            tvCourse.setText(mExperiment.getCourse());
            tvTime.setText(mExperiment.getTime());
            tvName.setText(mExperiment.getName());
            tvContent.setText(mExperiment.getContent());
        }
    }
}
