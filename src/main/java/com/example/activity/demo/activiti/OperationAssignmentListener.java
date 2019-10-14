package com.example.activity.demo.activiti;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;


/**
 * 运营任务分配处理人监听
 *
 */
@Component
public class OperationAssignmentListener implements TaskListener {


    /**
     * 这里设置对应的任务处理者
     * @param delegateTask
     */
    @Override
    public void notify(DelegateTask delegateTask) {

        // 设置处理人
        //String nextHandler = "user";
        //delegateTask.setAssignee(nextHandler);
        System.out.println("配置运营处理人");

    }



}
