package com.example.insumon_ocr;

import org.bytedeco.leptonica.PIX;
import org.bytedeco.tesseract.TessBaseAPI;

public class TesseractDetect {


    //////////////////////////////////////////

    // must contain "tesseract" folder
    static final String TESSBASE_PATH = "/storage/emulated/0/Download/tesseract/";
    static final String DEFAULT_LANGUAGE = "eng";
    static final String CHINESE_LANGUAGE = "chi_sim";

    //////////////////////////////////////////

    private TessBaseAPI detector;

    public TesseractDetect(){
        detector = new TessBaseAPI();
        detector.Init(TESSBASE_PATH, DEFAULT_LANGUAGE);
        detector.SetPageSegMode(7);
    }

    public void detectPixImg(PIX pixImg){
        detector.SetImage(pixImg);
        //detector.
    }


}
