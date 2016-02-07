package com.tim.smartparking;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * Created by aleksei on 17.01.16.
 */
public class Fragment2 extends Fragment {

    public Fragment2() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.qr_code, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        ImageView iv = (ImageView) getView().findViewById(R.id.qr);
        iv.setImageBitmap(genQRcode("hello", 0, 0));
    }

    public static Bitmap genQRcode(String content, int width, int height) {
        // Возвращает Bitmap по контенту, бросает null, если что-то пошло не так. Если не указать width и height, то они будут 512
        QRCodeWriter writer = new QRCodeWriter();
        if (width == 0) width = 512;
        if (height == 0) height = 512;
        Bitmap bmp = null;
        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height);
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bmp;
    }
}
