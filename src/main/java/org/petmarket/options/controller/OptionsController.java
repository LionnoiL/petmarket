package org.petmarket.options.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Options", description = "the site options API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/options")
public class OptionsController {


}
