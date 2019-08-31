package com.example.insumon_ocr;


import android.graphics.Bitmap;
import android.widget.ImageView;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.*;

import static org.bytedeco.javacpp.RealSense.any;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_imgproc.GaussianBlur;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.COLOR_RGB2GRAY;

import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;


public class Process {

    private Mat mardata;
    private Bitmap bitmapdata;

    public AndroidFrameConverter converterToBitmap = new AndroidFrameConverter();
    public OpenCVFrameConverter.ToIplImage converterToIplImage = new OpenCVFrameConverter.ToIplImage();
    public OpenCVFrameConverter.ToMat converterToMat =  new OpenCVFrameConverter.ToMat();


    public Process(Mat getMat){
        mardata = getMat;
    }

    public void compress(int w, int h){
        Mat resizeimage = new Mat();
        resize(mardata, resizeimage, new Size(w,h));
        mardata = resizeimage;
    }

    public void process(int bsize, int c) {

        //gray
        Mat gray = new Mat(mardata.rows(), mardata.cols(), mardata.type());
        cvtColor(mardata, gray, COLOR_RGB2GRAY);
        //Gaussianblur
        Mat blurred = new Mat(mardata.rows(), mardata.cols(), mardata.type());
        GaussianBlur(gray, blurred, new Size(15, 15), 0, 0, 0);
        //thresh
        Mat thresh = new Mat(mardata.rows(), mardata.cols(), mardata.type());
        adaptiveThreshold(blurred, thresh, 255, 1, 0, bsize, c);

        //erode1  iter :2
        Mat erode1 = thresh;
        Mat element_e1 = getStructuringElement(0, new Size(3, 3));
        for (int i = 0; i < 20; i++) {
            erode(erode1, erode1, element_e1);
        }


        //dialte1  iter:2
        mardata = erode1;
        Mat element_d1 = getStructuringElement(0, new Size(5, 5));
        for (int i = 0; i < 2; i++) {
            dilate(mardata, mardata, element_d1);
        }
    }

    public Mat getMat(){
        return  mardata;
    }

    public Bitmap getBitmap(){
        Frame frame_after = converterToMat.convert(mardata);
        Bitmap bitAfter = converterToBitmap.convert(frame_after);
        return  bitAfter;
    }

}
