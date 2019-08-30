package com.example.insumon_ocr;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;

import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;

public class MainActivity extends AppCompatActivity {
    public opencv_core.Mat roi;
    public ImageView imageView;



    Bitmap bitmap;
    Bitmap bitmap_after;
    boolean clicked = false;

    int b = 1351;
    int c = 15;

    AndroidFrameConverter converterToBitmap;
    OpenCVFrameConverter.ToIplImage converterToIplImage;
    OpenCVFrameConverter.ToMat converterToMat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        imageView = (ImageView) findViewById(R.id.imgV);


        imageView.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        bitmap = drawable.getBitmap();




        converterToBitmap = new AndroidFrameConverter();
        converterToIplImage = new OpenCVFrameConverter.ToIplImage();
        converterToMat = new OpenCVFrameConverter.ToMat();


        Frame frame = converterToBitmap.convert(bitmap);

        roi = converterToIplImage.convertToMat(frame);


        process(b,c);
    }

    public void process(int bsize,int c){

        //gray
        opencv_core.Mat gray=new opencv_core.Mat(roi.rows(),roi.cols(),roi.type());
        cvtColor(roi, gray, opencv_imgproc.COLOR_RGB2GRAY);
        //Gaussianblur
        opencv_core.Mat blurred=new opencv_core.Mat(roi.rows(),roi.cols(),roi.type());
        opencv_imgproc.GaussianBlur(gray, blurred,new opencv_core.Size(15,15),0,0,0);
        //thresh
        opencv_core.Mat thresh= new opencv_core.Mat(roi.rows(), roi.cols(), roi.type());
        opencv_imgproc.adaptiveThreshold(blurred, thresh, 255, 1,0, bsize, c);

        //erode1  iter :2
        opencv_core.Mat erode1=thresh;
        opencv_core.Mat element_e1 = opencv_imgproc.getStructuringElement(0,new opencv_core.Size(3, 3));
        for (int i=0;i<20;i++) {
            opencv_imgproc.erode( erode1, erode1, element_e1);
        }


        //dialte1  iter:2
        opencv_core.Mat dialte1=erode1;
        opencv_core.Mat element_d1 = opencv_imgproc.getStructuringElement(0,new opencv_core.Size(5, 5));
        for (int i=0;i<2;i++) {
            opencv_imgproc.dilate(dialte1, dialte1, element_d1);
        }






        Frame frame_after = converterToMat.convert(dialte1);
        bitmap_after = converterToBitmap.convert(frame_after);

        imageView.setImageBitmap(bitmap_after);


    }




    public void trans(View view) {
        b = b+50;
        process(b,c);
        System.out.println(String.valueOf(b));
    }

    public void transc(View view) {
        c = c+5;
        process(b,c);
        System.out.println(">>"+String.valueOf(c));
    }
}
