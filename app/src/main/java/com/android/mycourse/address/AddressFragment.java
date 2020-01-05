package com.android.mycourse.address;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
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

public class AddressFragment extends Fragment {

    private SearchView svAddress;
    private ListView lvAddress;
    private FloatingActionButton fabNewAddress;

    private AddressHelper mAddressHelper;
    private ArrayList<AddressBean> mAddresses;
    private AddressAdapter mAddressAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address, container, false);
        setHasOptionsMenu(true);
        initView(view);     // 初始化控件
        initEvent();        // 初始化事件
        return view;
    }

    // 设置工具栏菜单
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();   // 清除工具栏中的原有的其他菜单项
        inflater.inflate(R.menu.menu_address, menu);
    }

    // 选择菜单项
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 清空网址
        if (item.getItemId() == R.id.action_address_clear) {
            new AlertDialog.Builder(MainActivity.INSTANCE)
                    .setTitle(getString(R.string.dialog_title))
                    .setMessage(getString(R.string.address_clear_ask))
                    .setPositiveButton(getString(R.string.btn_yes),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(getContext(),
                                            getString(R.string.address_clear_finish),
                                            Toast.LENGTH_SHORT).show();
                                    mAddressHelper.deleteAllAddresses();
                                    mAddresses.clear();
                                    mAddressAdapter.notifyDataSetChanged();
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
        // 获取数据
        int index =
                data != null ? data.getIntExtra(AddressConstants.EXTRA_INDEX, -1) : -1;
        AddressBean address =
                data != null ? (AddressBean) data.getParcelableExtra(AddressConstants.EXTRA_ADDRESS) : null;
        if (requestCode == AddressConstants.REQUEST_NEW) {     // 新建网址
            if (resultCode == AddressConstants.RESULT_INSERT) {    // 添加网址
                if (address != null) {
                    mAddresses.add(0, address);    // 添加网址到列表顶端
                }
            }
        } else if (requestCode == AddressConstants.REQUEST_EDIT) {  // 编辑网址
            if (resultCode == AddressConstants.RESULT_UPDATE) {     // 更新网址
                if (index >= 0 && index < mAddresses.size() && address != null) {
                    mAddresses.set(index, address);
                }
            }
        }
        mAddressAdapter.notifyDataSetChanged();    // 刷新网址列表
    } // onActivityResult end

    /**
     * 初始化控件
     */
    private void initView(View view) {

        svAddress = view.findViewById(R.id.sv_address);
        lvAddress = view.findViewById(R.id.lv_address);
        fabNewAddress = view.findViewById(R.id.fab_address_new);

        mAddressHelper = new AddressHelper(getContext());
        mAddresses = mAddressHelper.getAllAddresses();
        mAddressAdapter = new AddressAdapter(getContext(), mAddresses);
        lvAddress.setAdapter(mAddressAdapter);

    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        // 监听搜索框内容变化
        svAddress.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAddressAdapter.getFilter().filter(newText);   // 显示搜索结果
                return true;
            }
        });
        // 点击网址列表项弹出底部菜单，选择“打开网页”、“编辑网址”、“删除网址”
        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, long l) {
                PopupMenu popupMenu = new PopupMenu(getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_address_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Intent intent;
                        switch (menuItem.getItemId()) {
                            // 打开网页
                            case R.id.popup_address_open:
                                String address = mAddresses.get(i).getAddress();
                                if (!address.contains("//")) {
                                    address = "http://" + address;
                                }
                                Uri uri = Uri.parse(address);
                                intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                                break;
                            // 编辑网址
                            case R.id.popup_address_edit:
                                intent = new Intent(MainActivity.INSTANCE, AddressEditActivity.class);
                                intent.putExtra(AddressEditActivity.ARG_MODE, AddressEditActivity.VAL_MODE_EDIT);
                                intent.putExtra(AddressConstants.EXTRA_INDEX, i);
                                intent.putExtra(AddressConstants.EXTRA_ADDRESS, mAddresses.get(i));
                                startActivityForResult(intent, AddressConstants.REQUEST_EDIT);
                                break;
                            // 删除网址
                            case R.id.popup_address_delete:
                                final int index = i;
                                new AlertDialog.Builder(MainActivity.INSTANCE)
                                        .setTitle(getString(R.string.dialog_title))
                                        .setMessage(String.format(
                                                getString(R.string.address_delete_ask),
                                                mAddresses.get(index).getTitle()))
                                        .setPositiveButton(getString(R.string.btn_yes),
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Toast.makeText(getContext(), String.format(
                                                                getString(R.string.address_delete_finish),
                                                                mAddresses.get(index).getTitle()),
                                                                Toast.LENGTH_SHORT).show();
                                                        mAddressHelper.deleteAddress(mAddresses.get(index));
                                                        mAddresses.remove(index);
                                                        mAddressAdapter.notifyDataSetChanged();
                                                    }
                                                })
                                        .setNegativeButton(getString(R.string.btn_no), null)
                                        .show();
                                break;
                        } // switch end
                        return true;
                    } // onMenuItemClick end
                }); // popupMenu.setOnMenuItemClickListener end
                popupMenu.show();
            } // onItemClick end
        }); // lvAddress.setOnItemClickListener end
        // 点击圆形按钮新建网址
        fabNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CircularAnim.fullActivity(getActivity(), view)
                        .go(new CircularAnim.OnAnimationEndListener() {
                            @Override
                            public void onAnimationEnd() {
                                // 进入新建网址界面
                                Intent intent = new Intent(getContext(), AddressEditActivity.class);
                                intent.putExtra(AddressEditActivity.ARG_MODE,
                                        AddressEditActivity.VAL_MODE_NEW);
                                startActivityForResult(intent, AddressConstants.REQUEST_NEW);
                            }
                        });
            }
        }); // fabNewAddress.setOnClickListener end

    } // initEvent end
}