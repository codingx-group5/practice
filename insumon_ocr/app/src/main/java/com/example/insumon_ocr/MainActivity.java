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
    public opencv_core.Mat blurred;
    public ImageView imageView;

    public SeekBar skb;

    public opencv_core.Mat mat;

    Bitmap bitmap;
    Bitmap bitmap1;
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
        //skb = (SeekBar) findViewById(R.id.seekBar);

        imageView.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        bitmap = drawable.getBitmap();

        //opencv_core.IplImage iplImage = this.bitmapToIplImage(bitmap);

        converterToBitmap = new AndroidFrameConverter();
        converterToIplImage = new OpenCVFrameConverter.ToIplImage();
        converterToMat = new OpenCVFrameConverter.ToMat();


        Frame frame = converterToBitmap.convert(bitmap);

        roi = converterToIplImage.convertToMat(frame);
        cvtColor(roi, roi, opencv_imgproc.COLOR_RGB2GRAY);

        process(b,c);
    }

    public void process(int bsize,int c){
        //roi = opencv_imgcodecs.imread("C:/blood/demo_output/279_roi.jpg", CV_8UC1);


        //Gaussianblur
        blurred=new opencv_core.Mat(roi.rows(),roi.cols(),roi.type());
        opencv_imgproc.GaussianBlur(roi, blurred,new opencv_core.Size(15,15),0,0,0);
        //thresh
        opencv_core.Mat thresh= new opencv_core.Mat(roi.rows(), roi.cols(), roi.type());
        opencv_imgproc.adaptiveThreshold(blurred, thresh, 255, 1,0, bsize, c);

        //erode1  iter :2
        opencv_core.Mat erode1=thresh;
        opencv_core.Mat element_e1 = opencv_imgproc.getStructuringElement(0,new opencv_core.Size(3, 3));
        for (int i=0;i<20;i++) {
            opencv_imgproc.erode( erode1, erode1, element_e1);
        }
//		BufferedImage image = matToBufferedImage(erode1);

        //dialte1  iter:2
        opencv_core.Mat dialte1=erode1;
        opencv_core.Mat element_d1 = opencv_imgproc.getStructuringElement(0,new opencv_core.Size(5, 5));
        for (int i=0;i<2;i++) {
            opencv_imgproc.dilate(dialte1, dialte1, element_d1);
        }


        Frame frame1 = converterToMat.convert(dialte1);
        bitmap1 = converterToBitmap.convert(frame1);
//
//        imageView.setImageBitmap(bitmap1);


        imageView.setImageBitmap(bitmap1);


    }


    public Bitmap IplImageToBitmap(opencv_core.IplImage iplImage) {
        Bitmap bitmap = null;
        bitmap = Bitmap.createBitmap(iplImage.width(), iplImage.height(),
                Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(iplImage.getByteBuffer());
        return bitmap;
    }


    public opencv_core.IplImage bitmapToIplImage(Bitmap bitmap) {
        opencv_core.IplImage iplImage;
        iplImage = opencv_core.IplImage.create(bitmap.getWidth(), bitmap.getHeight(),
                IPL_DEPTH_8U, 4);
        bitmap.copyPixelsToBuffer(iplImage.getByteBuffer());
        return iplImage;
    }


    public void trans(View view) {
        b = b+50;
        process(b,c);
        System.out.println(String.valueOf(b));
//        if(!clicked){
//            process(515);
//            clicked = !clicked;
//        }else{
//            imageView.setImageBitmap(bitmap);
//            clicked = !clicked;
//        }
    }

    public void transc(View view) {
        c = c+5;
        process(b,c);
        System.out.println(">>"+String.valueOf(c));
    }
}
