package com.waka.workspace.smalldianping.QRCODE;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

/**
 * Created by Administrator on 2016/1/14 0014.
 */
public class QRCodeImageCreater extends Thread {
    private String str;
    private Handler handler;
    private Bitmap bitmap;

    public QRCodeImageCreater(String str, Handler handler) {
        this.str = str;
        this.handler = handler;
    }

    public void run() {
        createQRCodeImage(str);
        Message message = Message.obtain(handler);
        message.what = 1;
        message.sendToTarget();
    }

    public void createQRCodeImage(String str) {

        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            int QR_WIDTH = 200, QR_HEIGHT = 200;
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(str, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            int x = bitmap.getWidth();
            int wh = x * 4 / 5;
            int ret = x / 10;
            bitmap = Bitmap.createBitmap(bitmap, ret, ret, wh, wh, null, false);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }
}
