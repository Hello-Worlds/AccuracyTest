package cn.lenovo.accuracytest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import cn.lenovo.accuracytest.Utils.Utils;
import cn.lenovo.accuracytest.activity.AccuracyActivity;
import cn.lenovo.accuracytest.activity.OffsetActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_goto_accuracy, btn_goto_offset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        addListener();
    }

    private void initView() {
        btn_goto_accuracy = (Button) findViewById(R.id.btn_goto_accuracy);
        btn_goto_offset = (Button) findViewById(R.id.btn_goto_offset);
    }

    private void addListener() {
        btn_goto_accuracy.setOnClickListener(this);
        btn_goto_offset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_goto_accuracy:// 按键精度测试
                startActivity(new Intent(this, AccuracyActivity.class));
                break;
            case R.id.btn_goto_offset:// 触屏偏移量测试
                startActivity(new Intent(this, OffsetActivity.class));
                break;
        }
    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Utils.toast(this, "再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
