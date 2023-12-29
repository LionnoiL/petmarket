package org.petmarket.images;

import java.io.File;

public interface ImageConverter {

    File convert(File file);

    String generateImageFileName(String suffix, long id);
}
