package com.vishwanathlokare.VendorHelper.PdfUtills;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import java.io.IOException;

public class Common {
    public static  void addNemItem(Document document, String text , int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text,font);
        Paragraph p = new Paragraph(chunk);
        p.setAlignment(align);
        document.add(p);
    }

    public static void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    public static void addNewItemWithLeftAndRight(Document document,String leftText,String rightText,Font LeftFont,Font rightFont
    ) throws DocumentException {
        Chunk LeftChuck = new Chunk(leftText,LeftFont);
        Chunk rightChunk = new Chunk(rightText,rightFont);
        Paragraph p = new Paragraph(LeftChuck);
        p.add(new Chunk(new VerticalPositionMark()));
        p.add(rightChunk);
        document.add(p);
    }

    public static void addLineSeparator(Document document) throws DocumentException {
        LineSeparator l = new LineSeparator();
        l.setLineColor(new BaseColor(0,0,0,68));
        addLineSpace(document);
        document.add(new Chunk(l));
        addLineSpace(document);
    }
    public static void addSignature(Document document,String path) throws DocumentException, IOException {

        Image sign = Image.getInstance(path);
        sign.scaleAbsolute(180,
                100);
        sign.setAlignment(Element.ALIGN_RIGHT);
        //Chunk left = new Chunk(sign,0,0,);

        document.add(sign);
    }
}
