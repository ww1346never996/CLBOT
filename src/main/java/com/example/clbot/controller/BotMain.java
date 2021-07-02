package com.example.clbot.controller;

import com.example.clbot.pojo.BotList;
import com.example.clbot.service.IBotListService;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * bot进程主逻辑
 */
@Controller
public class BotMain {
    @Autowired
    IBotListService botListService;
    @Autowired
    CommandController commandController;
    @Autowired
    QuestionMode questionMode;

    public void run() {
        List<BotList> botList = botListService.getBotList();
        BotList selectBot = botList.get(0);
        Bot bot = BotFactory.INSTANCE.newBot(selectBot.getQqNumber(), selectBot.getPassword(), new BotConfiguration() {{
            setHeartbeatStrategy(HeartbeatStrategy.REGISTER);
            fileBasedDeviceInfo(); // 使用 device.json 存储设备信息
            setProtocol(MiraiProtocol.ANDROID_PHONE); // 切换协议
        }});
        bot.login();
        afterLogin(bot);
        commandController.helpMessage(bot);
        commandController.questionMode(bot);
        questionMode.response(bot);
    }

    public void afterLogin(Bot bot) {
        long yourQQNumber = 928650496;
        bot.getEventChannel().subscribeAlways(GroupMessageEvent.class, (event) -> {
            if (event.getGroup().getId() == yourQQNumber && event.getMessage().toString().contains("test")) {
                event.getSubject().sendMessage(Image.fromId("{5C3A30DA-32DB-89B0-74AA-2B9D02647B89}.jpg")
                );
            }
        });
    }

}
