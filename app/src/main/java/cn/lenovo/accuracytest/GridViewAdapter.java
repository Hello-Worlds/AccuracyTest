package cn.lenovo.accuracytest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * GridView适配器
 * Created by 周超 on 2017/6/7.
 */
class GridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<GridItemInfo> list;
    private int h;//列高
    private int totalClickNum;//总次数

    GridViewAdapter(Context context, ArrayList<GridItemInfo> list, int h, int totalClickNum) {
        this.context = context;
        this.list = list;
        this.h = h;
        this.totalClickNum = totalClickNum;
    }

    void setData(ArrayList<GridItemInfo> list, int h, int totalClickNum) {
        this.list = list;
        this.h = h;
        this.totalClickNum = totalClickNum;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview_item, parent, false);
            convertView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h));
            vh = new ViewHolder();
            vh.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            vh.tv_percent = (TextView) convertView.findViewById(R.id.tv_percent);

            //设置字体大小
            if (h < 30) {
                vh.tv_count.setTextSize((float) h / 3);
                vh.tv_percent.setTextSize((float) h / 3);
            } else if (h < 60) {
                vh.tv_count.setTextSize((float) h / 4);
                vh.tv_percent.setTextSize((float) h / 4);
            } else if (h < 90) {
                vh.tv_count.setTextSize((float) h / 8);
                vh.tv_percent.setTextSize((float) h / 8);
            } else {
                vh.tv_count.setTextSize((float) h / 10);
                vh.tv_percent.setTextSize((float) h / 10);
            }

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        GridItemInfo info = list.get(position);
        vh.tv_count.setText(String.valueOf(info.getCount()));
        if (totalClickNum != 0) {
            vh.tv_percent.setText((info.getCount() * 100 / totalClickNum + "%"));
        }

        return convertView;
    }

    private class ViewHolder {
        TextView tv_count, tv_percent;
    }
}
