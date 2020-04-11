package io.nguyenhongphat0.crm.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

public class PdfUtil {
    public static byte[] generatePdf(SpringTemplateEngine engine, String template, Map<String, Object> model) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Context context = new Context();
            context.setVariables(model);
            String html = engine.process(template, context);
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new StringReader(html));
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public static void responsePdf(HttpServletResponse response, byte[] data, String name) {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "attachment; filename=" + name);
            response.setContentLength(data.length);
            response.getOutputStream().write(data);
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
