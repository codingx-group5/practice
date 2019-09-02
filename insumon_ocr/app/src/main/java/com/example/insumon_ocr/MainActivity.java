package com.example.insumon_ocr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetManager;
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
import org.opencv.android.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import org.bytedeco.leptonica.global.lept;

import com.sun.jna.ptr.PointerByReference;
import org.bytedeco.leptonica.*;


import org.bytedeco.leptonica.PIX;
import org.opencv.android.Utils;
//import org.opencv.core;
import org.opencv.imgcodecs.Imgcodecs;


public class MainActivity extends AppCompatActivity {
    public Mat roi;
    //public Mat roi;
    public ImageView imageView;


    Bitmap bitmap;
    Bitmap bitmap_after;
    Mat Mat_after;


    int b = 1351;
    int c = 15;

    public AndroidFrameConverter converterToBitmap;
    public OpenCVFrameConverter.ToIplImage converterToIplImage;
    public OpenCVFrameConverter.ToMat converterToMat;

    public Process poss;
    public ImgConvertor conver = new ImgConvertor();


    public String datapath = getFilesDir()+ "/tesseract/";



    private TessBaseAPI detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imageView = (ImageView) findViewById(R.id.imgV);
        Bitmap bit = conver.imagev2Bitmap(imageView);

        roi = conver.imagev2Mat(imageView);

        poss = new Process(roi);

        poss.compress(roi.cols()/2,roi.rows()/2);
        //poss.process(b,c);

        poss.process(b,c);

        //bitmap = poss.getBitmap();

        imageView.setImageBitmap(poss.getBitmap());
        checkFile(new File(datapath + "tessdata/"), "letsgodigital");















    }




    public void trans(View view) {
        b = b + 50;
        poss.process(b, c);
        imageView.setImageBitmap(poss.getBitmap());
        System.out.println("B >> " + String.valueOf(b));
    }

    public void transc(View view) {
        c = c + 5;
        poss.process(b, c);
        imageView.setImageBitmap(poss.getBitmap());
        System.out.println("C >> " + String.valueOf(c));
    }






    private void checkFile(File dir, String language) {
        //如果目前不存在则创建方面，然后在判断训练数据文件是否存在
        if (!dir.exists() && dir.mkdirs()) {
            copyFiles(language);
        }
        if (dir.exists()) {
            String datafilepath = datapath + "/tessdata/" + language + ".traineddata";
            File datafile = new File(datafilepath);
            if (!datafile.exists()) {
                copyFiles(language);
            }
        }
    }




    /**
     把训练数据放到手机内存
     * @param language "chi_sim" ,"eng"
     */
    private void copyFiles(String language) {
        try {
            String filepath = datapath + "/tessdata/" + language + ".traineddata";
            AssetManager assetManager = getAssets();
            InputStream instream = assetManager.open("tessdata/" + language + ".traineddata");
            OutputStream outstream = new FileOutputStream(filepath);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
