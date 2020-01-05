package com.android.mycourse.address;

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
import java.util.List;

public class AddressAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private List<AddressBean> mAllAddresses;    // 所有网址
    private List<AddressBean> mAddresses;       // 显示的网址
    private AddressFilter mAddressFilter;       // 网址标题过滤器

    AddressAdapter(Context context, List<AddressBean> addresses) {
        this.mContext = context;
        this.mAllAddresses = addresses;
        this.mAddresses = addresses;
    }

    @Override
    public int getCount() {
        return mAddresses.size();
    }

    @Override
    public Object getItem(int i) {
        return mAddresses.get(i);
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
                    .inflate(R.layout.item_address, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = view.findViewById(R.id.tv_name);
            viewHolder.tvAddress = view.findViewById(R.id.tv_content);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvTitle.setText(mAddresses.get(i).getTitle());
        viewHolder.tvAddress.setText(mAddresses.get(i).getAddress());
        return view;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvAddress;
    }

    @Override
    public Filter getFilter() {
        if (null == mAddressFilter) {
            mAddressFilter = new AddressFilter();
        }
        return mAddressFilter;
    }

    /**
     * 自定义网址标题过滤器
     */
    class AddressFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String filter = charSequence.toString().trim().toLowerCase();   // 搜索词
            List<AddressBean> values;
            if (filter.equals("")) {    // 未输入搜索词
                values = mAllAddresses;     // 返回全部网址
            } else {
                values = new ArrayList<>();
                for (AddressBean address : mAllAddresses) {
                    if (address.getTitle().toLowerCase().contains(filter) ||
                            address.getAddress().toLowerCase().contains(filter)) {
                        values.add(address);   // 返回标题或网址含搜索词的网址
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
            mAddresses = (List<AddressBean>) filterResults.values;     // 获取用于显示的网址
            if (filterResults.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
