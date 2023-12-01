package org.petmarket.advertisements.images.service;

import java.io.File;

public interface ImageConverter {

    File convert(File file);

    String generateImageFileName(String suffix, long id);
}
