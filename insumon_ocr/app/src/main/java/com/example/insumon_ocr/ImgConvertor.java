package com.example.insumon_ocr;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import org.bytedeco.javacpp.freenect2;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import org.bytedeco.javacv.OpenCVFrameConverter.ToMat;

//import org.opencv.core.*;

public class ImgConvertor {

    public AndroidFrameConverter converterToBitmap = new AndroidFrameConverter();
    public OpenCVFrameConverter.ToIplImage converterToIplImage = new OpenCVFrameConverter.ToIplImage();
    public OpenCVFrameConverter.ToMat converterToMat =  new OpenCVFrameConverter.ToMat();

//    public Mat oMat2bMat(org.opencv.core.Mat oMat){
//
//    }

    public Bitmap imagev2Bitmap(ImageView imgv){
        imgv.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        return bitmap;
    }

    //Frame frame = converterToBitmap.convert(bitmap);

    //roi = converterToIplImage.convertToMat(frame);

    public Mat imagev2Mat(ImageView imgv){
        Bitmap bi = imagev2Bitmap(imgv);
        Frame fr = converterToBitmap.convert(bi);
        Mat mat = converterToMat.convertToMat(fr);
        return mat;
    }


}
