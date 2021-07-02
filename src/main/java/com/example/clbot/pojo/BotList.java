package com.example.clbot.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigInteger;

@Data
@TableName(value = "botlist")
public class BotList{
    private int botNumber;
    private Long qqNumber;
    private String password;
}
