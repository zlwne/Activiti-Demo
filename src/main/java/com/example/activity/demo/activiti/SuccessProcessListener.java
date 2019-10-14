package com.example.activity.demo.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 处理成功时处理
 */
@Component
public class SuccessProcessListener implements ExecutionListener {


    @Override
    public void notify(DelegateExecution delegateExecution) {
        // 处理审批通过后的流程，正常结束会执行此方法
        //获得流程中带过来的参数
        Long userId = delegateExecution.getVariable("userId", Long.class);
        Map<String, Object> map =  delegateExecution.getTransientVariables();
        System.out.println("处理审批通过后的流程--审批通过");
    }


}
