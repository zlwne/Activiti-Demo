package com.example.activity.demo;

import com.alibaba.fastjson.JSONObject;
import com.example.activity.demo.model.TaskVO;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {


    @Test
    public void contextLoads() {
        /**
         * demo中包含三种角色：运营  Operator       部门经理  Manager        财务  FinanceStaff
         * 一级审核直接成功
         * 二级审核走：财务
         * 三级审核走：运营 经理 财务
         */
    }

    /**
     * 导入流程
     * 注意：.bpmn和.bpmn20.xml两种格式都可以进行部署
     */
    @Test
    public void createTask(){
        InputStream bpmnStream = this.getClass().getClassLoader().getResourceAsStream("coin_withdraw_apply.bpmn20.xml");
        //得到流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getRepositoryService()
                .createDeployment()
                .addInputStream("coin_withdraw_apply.bpmn20.xml", bpmnStream)
                .deploy();
        System.out.println("流程导入成功");
    }

    /**
     * 开始流程
     */
    @Test
    public void startProcess(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //随机生成流程业务ID
        String bussinessKey = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println(bussinessKey);
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().processDefinitionKey("coin_withdraw_apply");
        if (processDefinitionQuery==null){
            System.out.println("当前流程Key不存在,请先部署流程");
        }
        Map<String,Object> processVar = new HashMap<>();
        //用户参数
        processVar.put("userId",1L);
        //审核金额
        processVar.put("amount", new BigDecimal("100"));
        //一级审核条件
        processVar.put("firstReviewAmount", new BigDecimal("10"));
        //二级审核条件
        processVar.put("secondReviewAmount", new BigDecimal("50"));
        //三级审核条件
        processVar.put("thirdReviewAmount", new BigDecimal("80"));
        processEngine.getRuntimeService().startProcessInstanceByKey("apply",bussinessKey,processVar);
        System.out.println("流程开启");
    }

    /**
     * 处理流程任务
     */
    @Test
    public void completeProcess(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        String taskId = "85004";
        Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
        //利用任务对象，获取流程实例id
        String processInstancesId=task.getProcessInstanceId();
        Map<String,Object> processVar = new HashMap<>();
        // 审批状态
        processVar.put("approveStatus", "pass");
        //processVar.put("approveStatus", "reject");
        String comments = "通过";
        //String comments = "不通过";
        //设置审批意见
        taskService.addComment(taskId, processInstancesId, comments);
        taskService.complete(taskId,processVar);
        System.out.println("流程处理完成");
    }

    /**
     * 根据流程任务ID获得参数
     */
    @Test
    public void findVariableByTaskId(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        Task task=processEngine.getTaskService().createTaskQuery().taskId("85004").singleResult();
        Map<String, Object> map = processEngine.getRuntimeService().getVariables(task.getProcessInstanceId());
        Long userId = Long.valueOf(map.get("userId").toString());
        System.out.println("获得流程参数："+userId);
    }


    /**
     * 查询某个组下面的流程
     */
    @Test
    public void findAllTaskByGroup(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Task> list = processEngine.getTaskService().createTaskQuery().taskCandidateGroup("manager").list();
        List<TaskVO> userTaskList  =new ArrayList<>();

        list.forEach(item -> {
            TaskVO userTask =  new TaskVO();
            BeanUtils.copyProperties(item,userTask);
            userTaskList.add(userTask);
        });
        System.out.println(JSONObject.toJSON(userTaskList));

    }

    /**
     * 查询某个人下面的流程
     */
    @Test
    public void findAllTask(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Task> list = processEngine.getTaskService().createTaskQuery().taskAssignee("manager").list();
        List<TaskVO> userTaskList  =new ArrayList<>();

        list.forEach(item -> {
            TaskVO userTask =  new TaskVO();
            BeanUtils.copyProperties(item,userTask);
            userTaskList.add(userTask);
        });
        System.out.println(JSONObject.toJSON(userTaskList));

    }


    /**
     * 根据piid查询任务
     */
    @Test
    public void testQueryTaskByPIID() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Task> list = processEngine.getTaskService().createTaskQuery().taskId("40004").list();
        List<TaskVO> userTaskList = new ArrayList<>();
        list.forEach(item -> {
            TaskVO userTask = new TaskVO();
            BeanUtils.copyProperties(item, userTask);
            userTaskList.add(userTask);
        });
        System.out.println(JSONObject.toJSON(userTaskList));
    }



    /**
     * 查看已经完成的任务和当前在执行的任务
     */
    @Test
    public void findHistoryTask() {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        //如果只想获取到已经执行完成的，那么就要加入completed这个过滤条件
        List<HistoricTaskInstance> historicTaskInstances1 = defaultProcessEngine.getHistoryService()
                .createHistoricTaskInstanceQuery()
                .finished()
                .list();
        //如果只想获取到已经执行完成的，那么就要加入completed这个过滤条件
        List<HistoricTaskInstance> historicTaskInstances2 = defaultProcessEngine.getHistoryService()
                .createHistoricTaskInstanceQuery()
                .list();
        System.out.println("执行完成的任务：" + historicTaskInstances1.size());
        System.out.println("所有的总任务数（执行完和当前未执行完）：" + historicTaskInstances2.size());
    }


    /**
     * 查询所有的部署流程
     */
    @Test
    public void queryAllDeplyoment() {
        //得到流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Deployment> lists = processEngine.getRepositoryService()
                .createDeploymentQuery()
                .orderByDeploymenTime()//按照部署时间排序
                .desc()//按照降序排序
                .list();
        for (Deployment deployment : lists) {
            System.out.println(deployment.getId() + "    部署ID" + deployment.getId());
        }
    }


    /**
     * 删除流程
     */
    @Test
    public void deleteDeployment(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getRepositoryService().deleteDeployment("62501",true);
    }




}
