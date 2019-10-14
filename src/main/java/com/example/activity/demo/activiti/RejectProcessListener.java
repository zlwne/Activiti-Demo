package com.example.activity.demo.activiti;


import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

/**
 * 拒绝处理监听
 *
 */
@Component
public class RejectProcessListener implements ExecutionListener {


    @Override
    public void notify(DelegateExecution delegateExecution) {
        // 处理审批通过后的流程  驳回时候会触发此方法
        System.out.println("处理审批通过后的流程--拒绝处理");
    }


}
