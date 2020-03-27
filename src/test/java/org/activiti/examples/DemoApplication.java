package org.activiti.examples;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	private Logger logger = LoggerFactory.getLogger(DemoApplication.class);

	@Autowired
	private TaskRuntime taskRuntime;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	HistoryService historyService;
	@Autowired
	RepositoryService repositoryService;

	@Autowired
	TaskService taskService;

	@Autowired
	private SecurityUtil securityUtil;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

	}

	@Override
	public void run(String... args) {

		// Using Security Util to simulate a logged in user
		securityUtil.logInAs("john");

		String processDefinitionKey = "dev-flow-1";

		Deployment deploy = repositoryService.createDeployment().name("dev-flow-1")
				.addClasspathResource("processes/test/dev-flow-1.bpmn").deploy();

		//ProcessInstance instance = runtimeService.startProcessInstanceByKey(processDefinitionKey);

		List<Task> list = taskService.createTaskQuery().taskCandidateOrAssigned("bob")// taskCandidateOrAssigned("")//
				// 创建任务查询对象
				.list();

//		List<Task> list = taskService.createNativeTaskQuery().sql(
//				" select distinct RES.* from ACT_RU_TASK RES left join ACT_RU_IDENTITYLINK I on I.TASK_ID_ = RES.ID_ WHERE (RES.ASSIGNEE_ = 'john' or ( I.TYPE_ = 'candidate' and I.USER_ID_ = 'john')) order by RES.ID_ asc LIMIT 2147483647 OFFSET 0  ")
//				// 创建任务查询对象
//				.list();

//		List<Task> list = taskService.createNativeTaskQuery().sql(
//				"INSERT INTO `test`.`act_hi_taskinst`(`ID_`, `PROC_DEF_ID_`, `TASK_DEF_KEY_`, `PROC_INST_ID_`, `EXECUTION_ID_`, `NAME_`, `PARENT_TASK_ID_`, `DESCRIPTION_`, `OWNER_`, `ASSIGNEE_`, `START_TIME_`, `CLAIM_TIME_`, `END_TIME_`, `DURATION_`, `DELETE_REASON_`, `PRIORITY_`, `DUE_DATE_`, `FORM_KEY_`, `CATEGORY_`, `TENANT_ID_`) VALUES ('1', 'Process_0vn28q1:1:6be72d6e-6f5f-11ea-abc7-00d86172bdc5', 'test_1', '6beb732f-6f5f-11ea-abc7-00d86172bdc5', '6bebc051-6f5f-11ea-abc7-00d86172bdc5', 'test1', NULL, NULL, NULL, NULL, '2020-03-26 20:43:41.162', NULL, NULL, NULL, NULL, 50, NULL, NULL, NULL, '')")
//				// 创建任务查询对象
//				.list();

//		List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
//				// .taskAssignee(assignee)
//				.finished().list();
//
//		System.out.println(JSON.toJSONString(list));

		if (list != null && list.size() > 0) {
			for (Task task : list) {
				System.out.println("任务ID:" + task.getId());
				System.out.println("任务名称:" + task.getName());
				System.out.println("任务的创建时间:" + task.getCreateTime());
				System.out.println("任务的办理人:" + task.getAssignee());
				System.out.println("流程实例ID：" + task.getProcessInstanceId());
				System.out.println("执行对象ID:" + task.getExecutionId());
				System.out.println("流程定义ID:" + task.getProcessDefinitionId());
				System.out.println("*****************************************************************************");

				Map<String, Object> variables = new HashMap<>();
				variables.put("day", 3);
				taskService.complete(task.getId(),variables);

			}
		}

	}

//	@Bean
//	public TaskRuntimeEventListener<TaskAssignedEvent> taskAssignedListener() {
//		return taskAssigned -> logger.info(">>> Task Assigned: '" + taskAssigned.getEntity().getName()
//				+ "' We can send a notification to the assginee: " + taskAssigned.getEntity().getAssignee());
//	}
//
//	@Bean
//	public TaskRuntimeEventListener<TaskCompletedEvent> taskCompletedListener() {
//		return taskCompleted -> logger.info(">>> Task Completed: '" + taskCompleted.getEntity().getName()
//				+ "' We can send a notification to the owner: " + taskCompleted.getEntity().getOwner());
//	}

}