package com.example.clbot.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "question")
public class Question {
    private int id;
    private long group_id;
    private String question;
    private String answer;
    private long last_edit_user;
}
