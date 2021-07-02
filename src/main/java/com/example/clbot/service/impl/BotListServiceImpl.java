package com.example.clbot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.clbot.mapper.BotListMapper;
import com.example.clbot.pojo.BotList;
import com.example.clbot.service.IBotListService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BotListServiceImpl extends ServiceImpl<BotListMapper, BotList> implements IBotListService {
    @Override
    public List<BotList> getBotList() {
        return list();
    }
}
