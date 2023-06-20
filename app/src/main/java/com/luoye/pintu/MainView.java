package com.luoye.pintu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import com.luoye.pintu.util.MLog;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zyw on 2017/10/18.
 */
public class MainView extends View {
    private static final String TAG = MainView.class.getSimpleName();
    private Context context;
    private Bitmap back;
    private Paint paint;
    private int tileWidth;
    private int tileHeight;
    private Bitmap[] bitmapTiles;
    private int[][] dataTiles;
    private Board tilesBoard;
    private final int COL;// = 3;
    private final int ROW;// = 3;
    private int[][] dir = {{-1, 0},//左
            {0, -1},//上
            {1, 0},//右
            {0, 1}//下
    };
    private boolean isSuccess;

    int steps = 0;

    public MainView(Context context, int block) {
        super(context);
        this.context = context;
        COL = ROW = block;
        paint = new Paint();
        paint.setAntiAlias(true);
        init(true);
        startGame();
        MLog.d(TAG, MainActivity.getScreenWidth() + "," + MainActivity.getScreenHeight());
    }

    /**
     * 初始化
     */
    private void init(boolean first) {
        //载入图像，并将图片切成块
        AssetManager assetManager = context.getAssets();
        try {
            String name = ((MainActivity) context).next_file(first ? 0 : 1);
            InputStream assetInputStream = assetManager.open(name);
            Bitmap bitmap = BitmapFactory.decodeStream(assetInputStream);
            back = Bitmap.createScaledBitmap(bitmap, MainActivity.getScreenWidth(), MainActivity.getScreenHeight(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        tileWidth = back.getWidth() / COL;
        tileHeight = back.getHeight() / ROW;
        bitmapTiles = new Bitmap[COL * ROW];
        int idx = 0;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                bitmapTiles[idx++] = Bitmap.createBitmap(back, j * tileWidth, i * tileHeight, tileWidth, tileHeight);
            }
        }
    }

    /**
     * 开始游戏
     */
    private void startGame() {
        tilesBoard = new Board();
        dataTiles = tilesBoard.createRandomBoard(ROW, COL);
        isSuccess = false;
        steps = 0;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int color = getResources().getColor(R.color.background);
        canvas.drawColor(color);//Color.GRAY);
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                int idx = dataTiles[i][j];
                if (idx == ROW * COL - 1 && !isSuccess) continue;
                canvas.drawBitmap(bitmapTiles[idx], j * tileWidth, i * tileHeight, paint);
            }
        }
    }

    /**
     * 将屏幕上的点转换成,对应拼图块的索引
     *
     * @param x
     * @param y
     * @return
     */
    private Point xyToIndex(int x, int y) {
        int extraX = x % tileWidth > 0 ? 1 : 0;
        int extraY = x % tileWidth > 0 ? 1 : 0;
        int col = x / tileWidth + extraX;
        int row = y / tileHeight + extraY;

        return new Point(col - 1, row - 1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Point point = xyToIndex((int) event.getX(), (int) event.getY());

            for (int i = 0; i < dir.length; i++) {
                int newX = point.getX() + dir[i][0];
                int newY = point.getY() + dir[i][1];

                if (newX >= 0 && newX < COL && newY >= 0 && newY < ROW) {
                    if (dataTiles[newY][newX] == COL * ROW - 1) {
                        steps++;
                        int temp = dataTiles[point.getY()][point.getX()];
                        dataTiles[point.getY()][point.getX()] = dataTiles[newY][newX];
                        dataTiles[newY][newX] = temp;
                        invalidate();
                        if (tilesBoard.isSuccess(dataTiles)) {
                            isSuccess = true;
                            invalidate();
                            String successText = String.format("Congratulations, you have moved %d times", steps);
                            new AlertDialog.Builder(context).setTitle("Challenge Success").setCancelable(false).setMessage(successText).setPositiveButton("restart.", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    init(false);
                                    startGame();
                                }
                            }).setNegativeButton("exit game?", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    System.exit(0);
                                }
                            }).create().show();
                        }
                    }
                }
            }
        }
        return true;
    }

    private void printArray(int[][] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                sb.append(arr[i][j] + ",");
            }
            sb.append("\n");
        }
        MLog.d(TAG, sb.toString());
    }

}
