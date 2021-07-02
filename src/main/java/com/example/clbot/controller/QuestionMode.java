package com.example.clbot.controller;

import com.example.clbot.pojo.Question;
import com.example.clbot.service.IQuestionService;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Controller
public class QuestionMode {
    @Autowired
    IQuestionService questionService;

    public List<Question> responseList() {
        List<Question> resultList = questionService.getQuestion();
        return resultList;
    }

    public void response(Bot bot) {
        AtomicReference<List<Question>> questions = new AtomicReference<>(responseList());
        bot.getEventChannel().subscribeAlways(GroupMessageEvent.class, (event) -> {
            // 每次退出设置时更新查找表
            if (event.getMessage().contentToString().equals("/quitquestionmode")){
                questions.set(responseList());
            }
            // 检查关键字
            for (Question q : questions.get()){
                if (event.getMessage().contentToString().contains(q.getQuestion())
                        && q.getGroup_id() == event.getGroup().getId()){
                    event.getSubject().sendMessage(q.getAnswer());
                }
            }
        });
    }
}
