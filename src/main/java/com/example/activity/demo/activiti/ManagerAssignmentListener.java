package com.example.activity.demo.activiti;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;


/**
 * 部门经理审批设置处理人监听
 *
 */
@Component
public class ManagerAssignmentListener implements TaskListener {



    @Override
    public void notify(DelegateTask delegateTask) {

        // 设置处理人
        //String nextHandler = "manager";
        //delegateTask.setAssignee(nextHandler);
        System.out.println("配置部门经理处理人");


    }


}
