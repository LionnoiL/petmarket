package org.petmarket.advertisements.images.service;

import org.petmarket.errorhandling.ImageConvertException;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

@Service
public class ImageResizer {

    public void resize(InputStream input, Path target,
                       int width, int height) throws IOException {
        int newHeight = height;
        int newWidth = width;

        BufferedImage originalImage = ImageIO.read(input);
        double originalWidth = originalImage.getWidth();
        double originalHeight = originalImage.getHeight();

        if (originalHeight > originalWidth) {
            newWidth = (int) (newHeight * (originalWidth / originalHeight));
        }
        if (originalHeight < originalWidth) {
            newHeight = (int) (newWidth * (originalHeight / originalWidth));
        }

        Image newResizedImage = originalImage
                .getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        String s = target.getFileName().toString();
        String fileExtension = s.substring(s.lastIndexOf(".") + 1);

        boolean write = ImageIO.write(convertToBufferedImage(newResizedImage), fileExtension, target.toFile());
        if (!write) {
            throw new ImageConvertException("Image resize failed!");
        }
    }

    private BufferedImage convertToBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        BufferedImage bi = new BufferedImage(
                img.getWidth(null), img.getHeight(null),
                BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics2D = bi.createGraphics();
        graphics2D.drawImage(img, 0, 0, null);
        graphics2D.dispose();

        return bi;
    }
}
