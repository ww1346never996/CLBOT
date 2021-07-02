package com.example.clbot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.clbot.mapper.BotListMapper;
import com.example.clbot.mapper.QuestionMapper;
import com.example.clbot.pojo.BotList;
import com.example.clbot.pojo.Question;
import com.example.clbot.service.IQuestionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {
    @Override
    public List<Question> getQuestion() {
        return list();
    }

    @Override
    public boolean setQuestion(Question question) {
        return save(question);
    }

}
