package org.petmarket.files;

import java.io.File;

public interface StorageService {

    FileStorageName sendFile(File file, String pathName, String fileName);

    byte[] getFile(String pathName, String fileName);

    void deleteFile(String pathName, String fileName);
}
