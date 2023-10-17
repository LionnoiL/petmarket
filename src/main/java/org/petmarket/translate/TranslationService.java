package org.petmarket.translate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.language.entity.Language;
import org.petmarket.pages.entity.SitePageTranslate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Set;

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

    public SitePageTranslate getTranslate(Set<SitePageTranslate> translations, Language language, Language defaultLanguage) {
        SitePageTranslate translate;
        translate = translations.stream()
                .filter(t -> t.getLanguage().equals(language))
                .findFirst().orElse(null);

        if (translate == null) {
            translate = translations.stream()
                    .filter(t -> t.getLanguage().equals(defaultLanguage))
                    .findFirst().orElseThrow(() -> new TranslateException("No translation"));
        }
        return translate;
    }
}
