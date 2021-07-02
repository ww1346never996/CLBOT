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

    public Map<String, Question> responseMap() {
        Map<String, Question> resultMap = new HashMap<String, Question>();
        List<Question> resultList = questionService.getQuestion();
        for (Question question : resultList) {
            resultMap.put(question.getQuestion(), question);
        }
        return resultMap;
    }

    public List<String> questionList(){
        List<String> result = new ArrayList<String>();
        List<Question> resultList = questionService.getQuestion();
        for (Question question : resultList){
            result.add(question.getQuestion());
        }
        return result;
    }

    public void response(Bot bot) {
        AtomicReference<Map<String, Question>> resultMap = new AtomicReference<>(responseMap());
        AtomicReference<List<String>> questions = new AtomicReference<>(questionList());
        bot.getEventChannel().subscribeAlways(GroupMessageEvent.class, (event) -> {
            // 每次退出设置时更新查找表
            if (event.getMessage().contentToString().equals("/quitquestionmode")){
                resultMap.set(responseMap());
                questions.set(questionList());
            }
            // 检查关键字
            for (String q : questions.get()){
                if (event.getMessage().contentToString().contains(q)
                        && resultMap.get().get(q).getGroup_id() == event.getGroup().getId()){
                    event.getSubject().sendMessage(resultMap.get().get(q).getAnswer());
                }
            }
        });
    }
}
