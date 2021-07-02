package com.example.clbot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.clbot.pojo.BotList;

import java.util.List;

public interface IBotListService extends IService<BotList> {
    /**
     * 获取所有bot列表
     */
    List<BotList> getBotList();
}
