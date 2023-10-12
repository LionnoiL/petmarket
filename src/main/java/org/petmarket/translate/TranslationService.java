package org.petmarket.translate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Slf4j
@RequiredArgsConstructor
@Service
public class TranslationService {

    public String getTranslate(TranslationMessages key, String langCode) {
        String result = "";
        Properties prop = new Properties();
        InputStream input = null;
        String fileName = "translations/" + langCode + "/messages.properties";

        try {
            input = TranslationService.class.getClassLoader().getResourceAsStream(fileName);
            prop.load(new InputStreamReader(input, StandardCharsets.UTF_8));
            result = prop.getProperty(key.toString());
        } catch (IOException ex) {
            log.info("failed get translate for {}:{}", langCode, key);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }
}
