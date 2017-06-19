package cn.lenovo.accuracytest;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnTouchListener, View.OnClickListener {
    private String TAG = "MainActivity";
    private Context mContext = MainActivity.this;

    private RelativeLayout rl_title;
    private TextView tv_choice, tv_current_px, tv_total_num, tv_reset, tv_current_square_percent, tv_current_square_count;
    private GridView gv_square;
    private GridViewAdapter adapter;
    private ArrayList<GridItemInfo> list = new ArrayList<>();
    private PopupWindow choicePopupWindow;

    private int statusHeight;//状态栏（通知栏）的高度
    private int screenWidth;//屏幕宽度：像素
    private int screenHeight;//屏幕高度：像素
    private int titleHeight;//标题栏高度：像素


    private int defColumnWidth = 100;//默认像素（列宽）
    private int defColumnHeight = 100;//默认像素（列高）
    private int w, h;//每个小方格的宽高
    private int startPx = 10;//选项框起始像素
    private int endPx = 120;//选项框结束像素
    private int step = 2;//像素渐变步幅
    private int totalClickNum;//屏幕点击总次数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        initView();
        addListener();
        initDefData();
        initDefAdapter();
    }

    private void initView() {
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        rl_title.measure(0, 0);
        tv_choice = (TextView) findViewById(R.id.tv_choice);
        tv_current_px = (TextView) findViewById(R.id.tv_current_px);
        tv_current_px.setText(defColumnWidth + " × " + defColumnHeight + " px");
        tv_total_num = (TextView) findViewById(R.id.tv_total_num);
        tv_reset = (TextView) findViewById(R.id.tv_reset);
        tv_current_square_percent = (TextView) findViewById(R.id.tv_current_square_percent);
        tv_current_square_count = (TextView) findViewById(R.id.tv_current_square_count);
        gv_square = (GridView) findViewById(R.id.gv_square);

        statusHeight = Utils.getStatusHeight(this);//状态栏（通知栏）的高度
        screenWidth = Utils.getScreenWidth(this);//屏幕宽度：像素
        screenHeight = Utils.getScreenHeight(this);//屏幕高度：像素
        titleHeight = rl_title.getMeasuredHeight();//标题栏高度：像素

        if (screenWidth > 1000) startPx = 62;
    }

    private void addListener() {
        tv_choice.setOnClickListener(this);
        tv_reset.setOnClickListener(this);
        gv_square.setOnItemClickListener(this);
        gv_square.setOnTouchListener(this);
    }

    private void initDefData() {
        w = defColumnWidth;
        h = defColumnHeight;
        int num = screenWidth * screenHeight / (defColumnWidth * defColumnHeight);//一屏能容下多少个小方格（粗略计算）
        gv_square.setColumnWidth(defColumnWidth);//设置列宽

        for (int i = 0; i < num; i++) {
            GridItemInfo info = new GridItemInfo();
            list.add(info);
        }
    }

    private void initDefAdapter() {
        adapter = new GridViewAdapter(mContext, list, defColumnHeight, totalClickNum);
        gv_square.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_choice://选择尺寸
                if (choicePopupWindow == null) {
                    initChoicePopupWindow();
                }
                showChoicePopupWindow(tv_choice);
                break;

            case R.id.tv_reset://重置
                totalClickNum = 0;
                tv_total_num.setText(String.valueOf(totalClickNum));
                tv_current_square_percent.setText("0%");
                tv_current_square_count.setText("0");
                changData(w, h);
                Utils.toast(mContext, "已重置");
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_choice://选择尺寸ListView点击事件
                choicePopupWindow.dismiss();
                totalClickNum = 0;
                tv_total_num.setText(String.valueOf(totalClickNum));
                tv_current_square_percent.setText("0%");
                tv_current_square_count.setText("0");
                tv_current_px.setText((startPx + position * step) + " × " + (startPx + position * step) + " px");
                changData(startPx + position * step, startPx + position * step);
                break;

            case R.id.gv_square://方格GridView点击事件
                totalClickNum++;
                tv_total_num.setText(String.valueOf(totalClickNum));
                list.get(position).setCount(list.get(position).getCount() + 1);
                tv_current_square_percent.setText((list.get(position).getCount() * 100 / totalClickNum + "%"));
                tv_current_square_count.setText(String.valueOf(list.get(position).getCount()));
                adapter.setData(list, h, totalClickNum);
                break;
        }
    }

    private void changData(int columnWidth, int columnHeight) {
        w = columnWidth;
        h = columnHeight;
        int num = screenWidth * screenHeight / (columnWidth * columnHeight);//一屏能容下多少个小方格（粗略计算）
        if (w >= 200) num += 10;//200px以上不准确，加上一个补全值

        gv_square.setColumnWidth(columnWidth);//设置列宽

        list.clear();
        for (int i = 0; i < num; i++) {
            GridItemInfo info = new GridItemInfo();
            list.add(info);
        }
        Log.e(TAG, "list.size() = " + list.size());

        adapter = new GridViewAdapter(mContext, list, columnHeight, totalClickNum);
        gv_square.setAdapter(adapter);
    }

    //初始化尺寸选择弹窗
    private void initChoicePopupWindow() {
        View view = getLayoutInflater().inflate(R.layout.popup_choice, null, false);
        view.measure(0, 0);//测量布局的大小，为了以后算偏移量（choicePopupWindow.getWidth()和choicePopupWindow.getHeight()）
        choicePopupWindow = new PopupWindow(view, Utils.dp_to_px(this, 120), screenHeight * 5 / 6, true);//这里可用ViewGroup.LayoutParams.WRAP_CONTENT或choicePopupWindow.getWidth()…
        choicePopupWindow.setBackgroundDrawable(new BitmapDrawable());
        choicePopupWindow.setAnimationStyle(R.style.mPopupStyle3);

        ArrayList<HashMap<String, Object>> lvList = new ArrayList<>();
        for (int i = startPx; i <= endPx; i += step) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("text", i + " × " + i + " px");
            lvList.add(map);
        }

        ListView lv = (ListView) view.findViewById(R.id.lv_choice);
        SimpleAdapter lv_adapter = new SimpleAdapter(this, lvList, R.layout.popup_sales_order_details_menu_listview_item, new String[]{"text"}, new int[]{R.id.mTextView});
        lv.setAdapter(lv_adapter);
        lv.setOnItemClickListener(this);
    }

    //显示尺寸选择弹窗
    private void showChoicePopupWindow(View v) {
        choicePopupWindow.showAtLocation(v, Gravity.START | Gravity.TOP, Utils.dp_to_px(this, 8), statusHeight + v.getHeight() - 10);//X轴偏移（8dp），Y轴偏移（通知栏高度+控件高度）
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return MotionEvent.ACTION_MOVE == event.getAction();//禁止滑动
    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Utils.toast(mContext, "再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
