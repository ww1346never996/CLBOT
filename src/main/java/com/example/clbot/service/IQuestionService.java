package com.example.clbot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.clbot.pojo.BotList;
import com.example.clbot.pojo.Question;

import java.util.List;

public interface IQuestionService extends IService<Question> {
    /**
     * 获取所有自动回复列表
     */
    List<Question> getQuestion();
    /**
     * 添加新的自动回复
     */
    boolean setQuestion(Question question);
}
