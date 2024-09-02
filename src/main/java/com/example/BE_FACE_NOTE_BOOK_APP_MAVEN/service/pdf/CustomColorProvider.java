//package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.pdf;
//
//import com.itextpdf.text.BaseColor;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.FontProvider;
//import com.itextpdf.text.pdf.BaseFont;
//
//import java.io.IOException;
//
//public class CustomColorProvider implements FontProvider {
//
//    @Override
//    public boolean isRegistered(String fontname) {
//        return false;
//    }
//
//    @Override
//    public Font getFont(String fontName, String encoding, boolean embedded, float size, int style, BaseColor color) {
//        BaseFont unicode;
//        try {
//            unicode = BaseFont.createFont("src\\main\\resources\\fonts\\ARIALUNI.ttf",
//                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//        } catch (DocumentException | IOException e) {
//            throw new RuntimeException(e);
//        }
//        BaseColor color1 = new BaseColor(255, 244, 11);
//        Font font = new Font(unicode, 10);
//        font.setColor(color1);
//        return font;
//    }
//}
