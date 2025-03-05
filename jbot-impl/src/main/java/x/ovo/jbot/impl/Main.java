package x.ovo.jbot.impl;

import lombok.extern.slf4j.Slf4j;
import x.ovo.jbot.impl.config.Banner;

@Slf4j
public class Main {

    public static void main(String[] args) {
        Banner.print();

        try {
            var bot = new JBotImpl();
            bot.mkdir().await();
            bot.initManager().await();
            bot.start()
                    .onSuccess(v -> log.info("JBot 启动成功"))
                    .onFailure(e -> log.error("JBot 启动失败：{}", e.getMessage(), e));
        } catch (Exception e) {
            log.error("JBot 启动失败：{}", e.getMessage(), e);
            System.exit(1);
        }
    }

}
