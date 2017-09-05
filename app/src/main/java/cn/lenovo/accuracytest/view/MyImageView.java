/**
 *
 */
package cn.lenovo.accuracytest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * 计算偏移量Activity 自定义view
 */
public class MyImageView extends android.support.v7.widget.AppCompatImageView {
    private final static String TAG = MyImageView.class.getSimpleName();
    private Paint line_Paint;
    private Paint h_cir_Paint;
    private Paint l_cir_Paint;
    private int width;
    private int height;
    private int w_divide;
    private int h_divide;
    private int cir_Site_Num = 0;
    private static final int CIR_COUNT = 81;
    private static final int LINE_ROW_COUNT = 9;
    private static String[][] offset;
    public static String line_rowString;
    private static final int PADDING = 20;
    private static final int radiu = 20;
    private static OnOffsetListener mListener;

    private boolean isTouch = true;

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        initPaint();
        initData();
    }

    private void initData() {
        line_rowString = LINE_ROW_COUNT + "×" + LINE_ROW_COUNT + "偏移量数据：\n\n";
        offset = new String[LINE_ROW_COUNT][LINE_ROW_COUNT];
    }

    private void initPaint() {
        line_Paint = new Paint();
        line_Paint.setAntiAlias(true);
        line_Paint.setColor(Color.WHITE);
        line_Paint.setTextSize(20);

        h_cir_Paint = new Paint();
        h_cir_Paint.setAntiAlias(true);
        h_cir_Paint.setColor(Color.RED);
        h_cir_Paint.setTextSize(20);

        l_cir_Paint = new Paint();
        l_cir_Paint.setAntiAlias(true);
        l_cir_Paint.setColor(Color.parseColor("#0000ff"));
        l_cir_Paint.setTextSize(20);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        w_divide = (width - 2 * PADDING) / 8;
        h_divide = (height - 2 * PADDING) / 8;
        Log.e(TAG, "width = " + width + "  w_divide = " + w_divide);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "width = " + width + "  w_divide = " + w_divide);
        // 画线
        for (int i = 0; i < 9; i++) {
            canvas.drawLine(PADDING, h_divide * i + PADDING, width - PADDING, h_divide * i + PADDING, line_Paint);
            canvas.drawLine(w_divide * i + PADDING, PADDING, w_divide * i + PADDING, height - PADDING, line_Paint);
        }

        // 画圆
        for (int j = 0; j < 9; j++) {
            for (int k = 0; k < 9; k++) {
                if ((9 * j + k) == cir_Site_Num) {
                    canvas.drawCircle((w_divide * k + PADDING), (h_divide * j + PADDING), radiu, h_cir_Paint);
                } else {
                    canvas.drawCircle((w_divide * k + PADDING), (h_divide * j + PADDING), radiu, l_cir_Paint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isTouch) {
            int downX = (int) event.getX();
            int downY = (int) event.getY();
            if (cir_Site_Num < CIR_COUNT) {
                int x = cir_Site_Num / LINE_ROW_COUNT;
                int y = cir_Site_Num % LINE_ROW_COUNT;
                int offX = (y * w_divide + PADDING) - downX;
                int offY = (x * h_divide + PADDING) - downY;
                Log.d(TAG, "offX = " + offX + "  offY = " + offY);

                double d = Math.sqrt(Math.abs(offX) * Math.abs(offX) + Math.abs(offY) * Math.abs(offY));
                offset[x][y] = new java.text.DecimalFormat("#.00").format(d);

                cir_Site_Num++;
                invalidate();
                if (cir_Site_Num == CIR_COUNT) {
                    mListener.saveOffset(offset);
                }
                isTouch = false;
                touchHandler.sendEmptyMessageDelayed(0, 1000);
            }
        }
        return super.onTouchEvent(event);
    }

    private Handler touchHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            isTouch = true;
            return false;
        }
    });


    // 接收设置接口，方法回调数据
    public void setOnOffsetListener(OnOffsetListener listener) {
        mListener = listener;
    }

    // 注册回调接收
    public interface OnOffsetListener {
        void saveOffset(String[][] offsetArray);
    }
}
