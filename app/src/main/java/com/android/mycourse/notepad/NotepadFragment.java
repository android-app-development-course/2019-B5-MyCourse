package com.android.mycourse.notepad;

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
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.android.mycourse.MainActivity;
import com.android.mycourse.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import top.wefor.circularanim.CircularAnim;

public class NotepadFragment extends Fragment {

    private SearchView svNote;
    private ListView lvNote;
    private FloatingActionButton fabNewNote;

    private NoteHelper mNoteHelper;
    private ArrayList<NoteBean> mNotes;
    private NoteAdapter mNoteAdapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notepad, container, false);
        setHasOptionsMenu(true);
        initView(view);     // 初始化控件
        initEvent();        // 初始化事件
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_notepad, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 清空备忘
        if (item.getItemId() == R.id.action_notepad_clear) {
            new AlertDialog.Builder(MainActivity.INSTANCE)
                    .setTitle(getString(R.string.dialog_title))
                    .setMessage(getString(R.string.notepad_clear_ask))
                    .setPositiveButton(getString(R.string.btn_yes),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(getContext(),
                                            getString(R.string.notepad_clear_finish),
                                            Toast.LENGTH_SHORT).show();
                                    mNoteHelper.deleteAllNotes();
                                    mNotes.clear();
                                    mNoteAdapter.notifyDataSetChanged();
                                }
                            })
                    .setNegativeButton(getString(R.string.btn_no), null)
                    .show();
            return true;
        } // if end
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected end

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        svNote.setQuery(null, true);  // 清空搜索框内容，显示全部备忘
        svNote.clearFocus();    // 清除搜索框焦点，避免弹出软键盘
        // 获取数据
        int index =
                data != null ? data.getIntExtra(NotepadConstants.EXTRA_INDEX, -1) : -1;
        NoteBean note =
                data != null ? (NoteBean) data.getParcelableExtra(NotepadConstants.EXTRA_NOTE) : null;
        if (requestCode == NotepadConstants.REQUEST_NEW) {     // 新建备忘
            if (resultCode == NotepadConstants.RESULT_INSERT) {    // 添加备忘数据
                if (note != null) {
                    mNotes.add(0, note);    // 添加备忘到列表顶端
                }
            }
        } else if (requestCode == NotepadConstants.REQUEST_VIEW) {     // 查看备忘
            if (resultCode == NotepadConstants.RESULT_UPDATE) {    // 更新备忘数据
                if (index >= 0 && index < mNotes.size() && note != null) {
                    mNotes.set(index, note);
                }
            } else if (resultCode == NotepadConstants.RESULT_DELETE) {     // 删除备忘数据
                if (index >= 0 && index < mNotes.size()) {
                    mNotes.remove(index);
                }
            }
        }
        mNoteAdapter.notifyDataSetChanged();    // 刷新备忘列表
    } // onActivityResult end

    /**
     * 初始化控件
     *
     * @param view 布局
     */
    private void initView(View view) {

        svNote = view.findViewById(R.id.sv_notepad);
        lvNote = view.findViewById(R.id.lv_notepad);
        fabNewNote = view.findViewById(R.id.fab_notepad_new);

        // 读取备忘数据
        mNoteHelper = new NoteHelper(getContext());
        mNotes = mNoteHelper.getAllNotes();
        mNoteAdapter = new NoteAdapter(getContext(), mNotes);
        lvNote.setAdapter(mNoteAdapter);

    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        // 监听搜索框内容变化
        svNote.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mNoteAdapter.getFilter().filter(newText);   // 显示搜索结果
                return true;
            }
        });
        // 点击备忘列表查看备忘
        lvNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                Intent intent = new Intent(getContext(), NoteViewActivity.class);
                intent.putExtra(NotepadConstants.EXTRA_INDEX, i);
                intent.putExtra(NotepadConstants.EXTRA_NOTE, mNotes.get(i));
                startActivityForResult(intent, NotepadConstants.REQUEST_VIEW);  // 进入查看备忘界面
            }
        });
        // 长按备忘列表删除备忘
        lvNote.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int index = i;
                // 询问是否删除备忘
                new AlertDialog.Builder(MainActivity.INSTANCE)
                        .setTitle(getString(R.string.dialog_title))
                        .setMessage(String.format(
                                getString(R.string.notepad_delete_ask), mNotes.get(index).getTitle()))
                        .setPositiveButton(getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getContext(), String.format(
                                        getString(R.string.notepad_delete_finish),
                                        mNotes.get(index).getTitle()), Toast.LENGTH_SHORT).show();
                                mNoteHelper.deleteNote(mNotes.get(index));
                                mNotes.remove(index);
                                mNoteAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(getString(R.string.btn_no), null)
                        .show();
                return true;
            }
        });
        // 点击圆形按钮新建备忘
        fabNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CircularAnim.fullActivity(getActivity(), view)
                        .go(new CircularAnim.OnAnimationEndListener() {
                            @Override
                            public void onAnimationEnd() {
                                Intent intent = new Intent(getContext(), NoteEditActivity.class);
                                intent.putExtra(NoteEditActivity.ARG_MODE, NoteEditActivity.VAL_MODE_NEW);
                                startActivityForResult(intent, NotepadConstants.REQUEST_NEW);  // 进入新建备忘界面
                            }
                        });
            }
        });
    } // initEvent end
}