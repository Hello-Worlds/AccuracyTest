package cn.lenovo.accuracytest.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.lenovo.accuracytest.R;
import cn.lenovo.accuracytest.view.MyImageView;

import static cn.lenovo.accuracytest.view.MyImageView.line_rowString;

/**
 * 计算偏移量Activity
 */
public class OffsetActivity extends AppCompatActivity implements MyImageView.OnOffsetListener {
    private static final String TAG = OffsetActivity.class.getSimpleName();
    private String path = Environment.getExternalStorageDirectory().getPath() + "/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_offset);

        deleteFile();
        MyImageView imageView = new MyImageView(this);
        imageView.setOnOffsetListener(this);
    }

    @Override
    public void saveOffset(final String[][] offsetArray) {
        new AlertDialog.Builder(this).setTitle("保存提示").setMessage("请确认是否保存数据！").setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                writeSDCard(line_rowString, offsetArray);
            }
        }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).show();
    }

    private void deleteFile() {
        File fileDir = new File(path);
        try {
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        File file = new File(path, "offsetSite_new.txt");
        file.delete();
    }

    private void writeSDCard(String line_row_count, String[][] offsetArray) {
        String fileName = "offsetSite_new.txt";

        StringBuilder sb = new StringBuilder();
        sb.append(line_row_count);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sb.append(offsetArray[i][j]).append("\t");
            }
            sb.append("\n");
        }

        //判断有没有SD卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());

                Toast.makeText(this, "保存成功，位置：\n" + path + "offsetSite_new.txt", Toast.LENGTH_LONG).show();
                Log.e(TAG, "保存位置：" + path + "offsetSite_new.txt");
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                finish();
            }
        }
    }
}
