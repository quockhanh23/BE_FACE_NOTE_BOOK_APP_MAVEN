//package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.service.pdf;
//
//import com.itextpdf.text.BaseColor;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.FontProvider;
//import com.itextpdf.text.pdf.BaseFont;
//
//public class CustomFontProvider implements FontProvider {
//
//    @Override
//    public boolean isRegistered(String fontName) {
//        return false;
//    }
//
//    @Override
//    public Font getFont(String fontName, String encoding, boolean embedded, float size, int style, BaseColor color) {
//        BaseFont unicode = CustomBase.getFontArial();
//        BaseColor color1 = new BaseColor(255, 0, 0);
//        Font font = new Font(unicode, 14);
//        font.setColor(color1);
//        return font;
//    }
//}
