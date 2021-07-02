package com.example.clbot.controller;

import com.example.clbot.pojo.Question;
import com.example.clbot.service.IQuestionService;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 用于管理会话内的command
 */
@Controller
public class CommandController {
    @Autowired
    IQuestionService questionService;

    public void helpMessage(Bot bot) {
        bot.getEventChannel().subscribeAlways(GroupMessageEvent.class, (event) -> {
            // 发送/help获取帮助列表
            if (event.getMessage().contentToString().equals("/help")) {
                event.getSubject().sendMessage("欢迎使用ChonglangBot\n" +
                        "下面是指令列表:\n" +
                        "输入/questionmodehelp 可查询自动回复设置的用法\n" +
                        "输入/gamehelp 可查看可用的游戏和相关的帮助\n" +
                        "输入/levelhelp 可查看群发言等级规则");
            }
            // 发送/questionmodehelp 获取自动回复设置信息
            if (event.getMessage().contentToString().equals("/questionmodehelp")) {
                event.getSubject().sendMessage("自动回复设置方式如下：\n" +
                        "首先输入/quesitonmode进入自动回复设置模式\n" +
                        "然后输入/q_+被回复的关键词或者句子\n" +
                        "指令示例如: /q_hello\n" +
                        "意为需要对hello这个关键词进行回复，" +
                        "然后输入/a_+回复的语句\n" +
                        "指令示例如：/a_hello\n" +
                        "意为对hello消息回复hello\n" +
                        "最后输入/quitquestionmode退出设置模式\n" +
                        "需要注意，一个关键词不可设置多个回复，且设置消息中不能出现_字符");
            }
        });
    }

    // 自动回复设置命令处理
    public void questionMode(Bot bot) {
        Question question = new Question();
        // 设置模式开关标志
        AtomicBoolean modeFlag = new AtomicBoolean(false);
        AtomicBoolean settingQuestionFlag = new AtomicBoolean(true);
        AtomicBoolean settingAnswerFlag = new AtomicBoolean(true);
        bot.getEventChannel().subscribeAlways(GroupMessageEvent.class, (event) -> {
            if (event.getMessage().contentToString().equals("/questionmode")) {
                // 打开设置模式
                modeFlag.set(true);
                // 记录自动回复设置人id
                question.setLast_edit_user(event.getSender().getId());
                // 记录自动回复所在的群组id
                question.setGroup_id(event.getGroup().getId());
                event.getSubject().sendMessage("您已进入自动回复设置模式，请按照指令格式输入指令，如需帮助请输入/questionmodehelp");
            }
        });
        bot.getEventChannel().subscribeAlways(GroupMessageEvent.class, (event) -> {
            if (event.getMessage().contentToString().equals("/te")) {
                event.getSubject().sendMessage(question.toString() + modeFlag.toString());
            }
        });
        // 进入设置模式处理流程
        bot.getEventChannel().subscribeAlways(GroupMessageEvent.class, (event) -> {
            if (modeFlag.get()) {
                if (event.getMessage().contentToString().contains("/q_")) {
                    String message = event.getMessage().contentToString();
                    String[] questionMessages = message.split("_");
                    if (questionMessages.length == 2) {
                        // 解析字符串
                        settingQuestionFlag.set(true);
                        question.setQuestion(questionMessages[1]);
                    } else {
                        settingQuestionFlag.set(false);
                        event.getSubject().sendMessage("输入有误，请依照格式输入正确的指令！");
                    }
                }
            }
            // 设置回答
            if (event.getMessage().contentToString().contains("/a_")) {
                if (modeFlag.get()) {
                    if (settingQuestionFlag.get()) {
                        String message = event.getMessage().contentToString();
                        String[] answerMessages = message.split("_");
                        if (answerMessages.length == 2) {
                            settingAnswerFlag.set(true);
                            question.setAnswer(answerMessages[1]);
                        } else {
                            settingAnswerFlag.set(false);
                            event.getSubject().sendMessage("输入有误，请依照格式输入正确的指令！");
                        }
                    } else {
                        event.getSubject().sendMessage("请先完成正确的问题设置！");
                    }
                }
            }
        });
        // 退出设置模式
        bot.getEventChannel().subscribeAlways(GroupMessageEvent.class, (event) -> {
            if (modeFlag.get() && settingAnswerFlag.get() && settingQuestionFlag.get()) {
                if (event.getMessage().contentToString().equals("/quitquestionmode")) {
                    if (questionService.setQuestion(question)) {
                        event.getSubject().sendMessage("设置成功！已退出设置模式");
                    } else {
                        event.getSubject().sendMessage("数据库异常，请联系bot管理员反馈！");
                    }
                } else if (!settingAnswerFlag.get() && !settingQuestionFlag.get()){
                    event.getSubject().sendMessage("未输入完整信息或信息有误，不保存！");
                }
            }
        });
    }
}
