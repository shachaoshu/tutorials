package com.mycompany.tutorial.service.impl;

import com.mycompany.tutorial.service.TaskService;
import org.springframework.stereotype.Service;

/**
 * Created by scs on 14-7-29.
 */
@Service
public class TaskServiceImpl implements TaskService {
    @Override
    public String execute(String task) {
        String result = "Result of task: " + task;
        return result;
    }
}
