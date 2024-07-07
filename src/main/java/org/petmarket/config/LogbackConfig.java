package org.petmarket.config;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.net.SyslogAppender;
import lombok.RequiredArgsConstructor;
import org.petmarket.options.entity.OptionsKey;
import org.petmarket.options.service.OptionsService;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogbackConfig implements ApplicationListener<ApplicationReadyEvent> {
    private final OptionsService optionsService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        SyslogAppender syslogAppender = new SyslogAppender();
        syslogAppender.setContext(context);
        syslogAppender.setSyslogHost(optionsService.getOptionsValueByKey(OptionsKey.PAPERTRAIL_HOST));
        syslogAppender.setPort(Integer.parseInt(optionsService.getOptionsValueByKey(OptionsKey.PAPERTRAIL_PORT)));
        syslogAppender.setFacility("USER");
        syslogAppender.setSuffixPattern("%logger{35} - %msg%n");

        PatternLayout layout = new PatternLayout();
        layout.setContext(context);
        layout.setPattern("%logger{35} - %msg%n");
        layout.start();

        syslogAppender.setLayout(layout);
        syslogAppender.start();

        context.getLogger("ROOT").addAppender(syslogAppender);
    }
}
