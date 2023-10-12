package org.petmarket.options.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.language.entity.Language;
import org.petmarket.options.entity.Options;
import org.petmarket.options.repository.OptionsRepository;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OptionsService {

    private final OptionsRepository optionsRepository;

    public Language getDefaultSiteLanguage() {
        Options options = optionsRepository.findById(1L).orElseThrow(() -> {
            throw new ItemNotFoundException("Options not found");
        });
        return options.getLanguage();
    }
}
