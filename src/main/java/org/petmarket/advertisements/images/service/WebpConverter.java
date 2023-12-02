package org.petmarket.advertisements.images.service;

import com.luciad.imageio.webp.WebPWriteParam;
import org.petmarket.errorhandling.ImageConvertException;
import org.petmarket.utils.Helper;
import org.springframework.stereotype.Service;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class WebpConverter implements ImageConverter {

    @Override
    public File convert(File file) {
        BufferedImage image;
        File tmpFile;
        try {
            image = ImageIO.read(file);
            ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();

            WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSY_COMPRESSION]);
            writeParam.setCompressionQuality(0.8f);

            tmpFile = File.createTempFile("data", null);
            writer.setOutput(new FileImageOutputStream(tmpFile));
            writer.write(null, new IIOImage(image, null, null), writeParam);

        } catch (IOException e) {
            throw new ImageConvertException("Convert image failed!");
        }
        return tmpFile;
    }

    @Override
    public String generateImageFileName(String suffix, long id) {
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append("_");
        sb.append(Helper.getRandomString(5));
        sb.append("_");
        sb.append(suffix);
        sb.append(".webp");
        return sb.toString();
    }
}
