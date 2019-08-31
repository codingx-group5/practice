package com.example.insumon_ocr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;

import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.tesseract.TessBaseAPI;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import org.bytedeco.leptonica.global.lept;

import com.sun.jna.ptr.PointerByReference;
import org.bytedeco.leptonica.*;


import org.bytedeco.leptonica.PIX;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;


public class MainActivity extends AppCompatActivity {
    public Mat roi;
    public ImageView imageView;


    Bitmap bitmap;
    Bitmap bitmap_after;
    Mat Mat_after;

    PIX image2Scan;

    int b = 1351;
    int c = 15;

    //训练数据路径，必须包含tesseract文件夹
    static final String TESSBASE_PATH = "/storage/emulated/0/Download/tesseract/";
    //识别语言英文
    static final String DEFAULT_LANGUAGE = "eng";
    //识别语言简体中文
    static final String CHINESE_LANGUAGE = "chi_sim";

    AndroidFrameConverter converterToBitmap;
    OpenCVFrameConverter.ToIplImage converterToIplImage;
    OpenCVFrameConverter.ToMat converterToMat;

    public Process poss = new Process();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imageView = (ImageView) findViewById(R.id.imgV);


        imageView.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        bitmap = drawable.getBitmap();


        Frame frame = converterToBitmap.convert(bitmap);

        roi = converterToIplImage.convertToMat(frame);

        roi = poss.compress(roi);

        poss.process(roi,b,c);



        Mat_after = poss.getMat();
    }




    public void trans(View view) {
        b = b + 50;
        poss.process(roi,b, c);
        System.out.println(String.valueOf(b));
    }

    public void transc(View view) {
        c = c + 5;
        poss.process(roi,b, c);
        System.out.println(">>" + String.valueOf(c));
    }

//    public void setImage(Bitmap bmp) {
//        image2Scan = ReadFile.readBitmap(bmp);
//
//        if (image == null) {
//            throw new RuntimeException("Failed to read bitmap");
//        }
//
//        nativeSetImagePix(image.getNativePix());
//    }




//    public String EnglishOCR(){
//
//        final TessBaseAPI baseApi = new TessBaseAPI();
//        //初始化OCR的训练数据路径与语言
//
//        baseApi.Init(TESSBASE_PATH, DEFAULT_LANGUAGE);
//        //设置识别模式
//        baseApi.SetPageSegMode(7);
//        //设置要识别的图片
//        if (Mat_after.empty()) {
//            return "";
//        }
//
//
//
//        baseApi.SetImage(image2Scan);
//
//
//        english.setImageBitmap();
//        englishtext.setText(baseApi.getUTF8Text());
//        baseApi.clear();
//        baseApi.end();
//    }
//
//    public static PIX convertMatToPix(Mat mat) {
//        MatOfByte bytes = new MatOfByte();
//        Imgcodecs.imencode(".tif", mat, bytes);
//        ByteBuffer buff = ByteBuffer.wrap(bytes.toArray());
//
//        return lept.pixReadMem(buff, new NativeSize(buff.capacity()));
//
//    }
//
//    public static Mat convertPixToMat(PIX pix) {
//        PointerByReference pdata = new PointerByReference();
//        NativeSizeByReference psize = new NativeSizeByReference();
//        Leptonica1.pixWriteMem(pdata, psize, pix, ILeptonica.IFF_TIFF);
//        byte[] b = pdata.getValue().getByteArray(0, psize.getValue().intValue());
//        Leptonica1.lept_free(pdata.getValue());
//        return Imgcodecs.imdecode(new MatOfByte(b), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
//    }
}
