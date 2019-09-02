package com.example.insumon_ocr;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import org.bytedeco.javacpp.RealSense;
import org.bytedeco.tesseract.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TessOCR {

//
//    /**
//     把训练数据放到手机内存
//     * @param language "chi_sim" ,"eng"
//     */
//    private void copyFiles(String language) {
//        try {
//            String filepath = datapath + "/tessdata/" + language + ".traineddata";
//            AssetManager assetManager = getAssets();
//            InputStream instream = assetManager.open("tessdata/" + language + ".traineddata");
//            OutputStream outstream = new FileOutputStream(filepath);
//            byte[] buffer = new byte[1024];
//            int read;
//            while ((read = instream.read(buffer)) != -1) {
//                outstream.write(buffer, 0, read);
//            }
//            outstream.flush();
//            outstream.close();
//            instream.close();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
