package com.example.insumon_ocr;


import android.graphics.Bitmap;
import android.widget.ImageView;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_imgproc.GaussianBlur;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.COLOR_RGB2GRAY;

import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;


public class Process {

    private Mat dialte1;
    private Bitmap bitmap_after;

    private AndroidFrameConverter converterToBitmap;
    private OpenCVFrameConverter.ToIplImage converterToIplImage;
    private OpenCVFrameConverter.ToMat converterToMat;

    public Mat compress(Mat m, double scaleFactor){
        // compress img
        //out Mat
        return m;
    }

    public void process(Mat roi, int bsize, int c) {

        //gray
        Mat gray = new Mat(roi.rows(), roi.cols(), roi.type());
        cvtColor(roi, gray, COLOR_RGB2GRAY);
        //Gaussianblur
        Mat blurred = new Mat(roi.rows(), roi.cols(), roi.type());
        GaussianBlur(gray, blurred, new Size(15, 15), 0, 0, 0);
        //thresh
        Mat thresh = new Mat(roi.rows(), roi.cols(), roi.type());
        adaptiveThreshold(blurred, thresh, 255, 1, 0, bsize, c);

        //erode1  iter :2
        Mat erode1 = thresh;
        Mat element_e1 = getStructuringElement(0, new Size(3, 3));
        for (int i = 0; i < 20; i++) {
            erode(erode1, erode1, element_e1);
        }


        //dialte1  iter:2
        dialte1 = erode1;
        Mat element_d1 = getStructuringElement(0, new Size(5, 5));
        for (int i = 0; i < 2; i++) {
            dilate(dialte1, dialte1, element_d1);
        }
    }

    public Mat getMat(){
        return  dialte1;
    }

    public Bitmap getBitmap(){
        Frame frame_after = converterToMat.convert(dialte1);
        bitmap_after = converterToBitmap.convert(frame_after);
        return  bitmap_after;
    }
}
