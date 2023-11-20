package org.petmarket.files;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class FileStorageName {

    private String shortName;
    private String fullName;
}
