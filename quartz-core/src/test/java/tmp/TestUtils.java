package tmp;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.SchedulerException;
import org.quartz.SchedulerListener;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.TriggerListener;
import org.quartz.integrations.tests.QuartzMemoryTestSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JobUtils
 */
public class TestUtils {
  public static class PostgresTestSupport extends QuartzMemoryTestSupport {
    protected Logger log = LoggerFactory.getLogger(getClass());

    @Override
    protected Properties createSchedulerProperties() {
      Properties props = TestUtils.postgresProps();
      return props;
    }

    @Override
    protected void afterSchedulerInit() throws Exception {
      log.info("Clear all scheduler data");
      scheduler.clear();

      beforeSchedulerStart();

      log.info("Start scheduler");
      scheduler.start();
    }

    protected void beforeSchedulerStart() throws Exception {
    }
  }

  public static Properties createSchedulerPropertiesFromFile() {
    Properties props = new Properties();
    File file = new File(
        "C:\\Users\\zde\\src\\zemian\\quartz-starter\\src\\main\\resources\\zemian\\quartzstarter\\postgres.properties");
    try (FileInputStream ins = new FileInputStream(file)) {
      props.load(ins);
    } catch (Exception e) {
      throw new RuntimeException("Failed to read props " + file);
    }
    return props;
  }

  public static Properties postgresProps() {
    Properties props = new Properties();
    props.put("org.quartz.scheduler.instanceName", "PostgresTmpTestScheduler");
    props.put("org.quartz.scheduler.instanceId", "AUTO");
    props.put("org.quartz.scheduler.skipUpdateCheck", "true");
    props.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
    props.put("org.quartz.threadPool.threadCount", "12");
    props.put("org.quartz.threadPool.threadPriority", "5");
    props.put("org.quartz.jobStore.misfireThreshold", "10000");
    props.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
    props.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");
    props.put("org.quartz.jobStore.dataSource", "myDS");
    props.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
    props.put("org.quartz.jobStore.isClustered", "false");
    props.put("org.quartz.dataSource.myDS.driver", "org.postgresql.Driver");
    props.put("org.quartz.dataSource.myDS.URL", "jdbc:postgresql://localhost:5432/quartz");
    props.put("org.quartz.dataSource.myDS.user", "quartz");
    props.put("org.quartz.dataSource.myDS.password", "quartz123");
    props.put("org.quartz.dataSource.myDS.maxConnections", "5");
    return props;
  }


  public static void executeSampleJob(Logger log, JobExecutionContext context)
      throws JobExecutionException {
    try {
      JobDetail job = context.getJobDetail();
      Trigger trigger = context.getTrigger();
      JobDataMap jobMergedData = context.getMergedJobDataMap();
      JobKey jobKey = job.getKey();
      log.info("Job {} with trigger {} is running.", jobKey, trigger.getKey());
      log.info("jobMergedData={}", new HashMap(jobMergedData));
      log.debug("Job data={} ", job.getJobDataMap());
      log.debug("Trigger data={} ", trigger.getJobDataMap());

      // Sync job
      CyclicBarrier barrier = (CyclicBarrier) context.getScheduler().getContext().get("barrier");
      if (barrier != null) {
        log.info("Testing barrier wait of 20 secs wait max");
        barrier.await(20, TimeUnit.SECONDS);
      }

      // Pause job
      if (jobMergedData.containsKey("pauseTime")) {
        Long pauseTime = context.getMergedJobDataMap().getLongValueFromString("pauseTime");
        log.info("Pausing job on purpose: {} ms", pauseTime);
        Thread.sleep(pauseTime);
      }

      // Simulate exception job
      if (jobMergedData.containsKey("createError")) {
        throw new RuntimeException(
            "Job with error: " + context.getMergedJobDataMap().get("createError"));
      }
    } catch (Exception ex) {
      throw new JobExecutionException(ex);
    }
  }

  /**
   * MyJob
   */
  public static class MyJob implements Job {
    private static Logger LOG = LoggerFactory.getLogger(MyJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
      executeSampleJob(LOG, context);
    }
  }

  /**
   * DisallowConcJob
   */
  @DisallowConcurrentExecution
  @PersistJobDataAfterExecution
  public static class DisallowConcJob implements Job {

    private static Logger LOG = LoggerFactory.getLogger(DisallowConcJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
      executeSampleJob(LOG, context);

      JobDataMap jobData = context.getJobDetail().getJobDataMap();
      if (jobData.containsKey("updateCount")) {
        int from = jobData.getInt("updateCount");
        int to = from + 1;
        LOG.info("Update job data 'updateCount' from {} to {}", from, to);
        jobData.put("updateCount", to);
      }
    }
  }

  public static class MyJobListener implements JobListener {

    private static Logger LOG = LoggerFactory.getLogger(MyJobListener.class);

    @Override
    public String getName() {
      return "MyListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
      LOG.info("jobToBeExecuted: {}", context);
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
      LOG.info("jobExecutionVetoed: {}", context);
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
      LOG.info("jobWasExecuted: {}, {}", context, jobException);
    }
  }

  public static class MyTriggerListener implements TriggerListener {

    private static Logger log = LoggerFactory.getLogger(MyTriggerListener.class);

    @Override
    public String getName() {
      return "MyListener";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
      log.info("triggerFired: {}, {}", context, trigger);
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
      log.info("vetoJobExecution: {}, {}", context, trigger);
      return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
      log.info("triggerMisfired: {}", trigger);
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context,
        Trigger.CompletedExecutionInstruction triggerInstructionCode) {
      log.info("vetoJobExecution: {}, {}, {}", context, trigger, triggerInstructionCode);
    }
  }

  public static class MySchedulerListener implements SchedulerListener {

    private static Logger log = LoggerFactory.getLogger(MySchedulerListener.class);

    @Override
    public void jobScheduled(Trigger trigger) {
      log.info("jobScheduled: {}", trigger);
    }

    @Override
    public void jobUnscheduled(TriggerKey triggerKey) {
      log.info("jobUnscheduled: {}", triggerKey);
    }

    @Override
    public void triggerFinalized(Trigger trigger) {
      log.info("triggerFinalized: {}", trigger);
    }

    @Override
    public void triggerPaused(TriggerKey triggerKey) {
      log.info("triggerPaused: {}", triggerKey);
    }

    @Override
    public void triggersPaused(String triggerGroup) {
      log.info("triggersPaused: {}", triggerGroup);
    }

    @Override
    public void triggerResumed(TriggerKey triggerKey) {
      log.info("triggerResumed: {}", triggerKey);
    }

    @Override
    public void triggersResumed(String triggerGroup) {
      log.info("triggersResumed: {}", triggerGroup);
    }

    @Override
    public void jobAdded(JobDetail jobDetail) {
      log.info("jobAdded: {}", jobDetail);
    }

    @Override
    public void jobDeleted(JobKey jobKey) {
      log.info("jobDeleted: {}", jobKey);
    }

    @Override
    public void jobPaused(JobKey jobKey) {
      log.info("jobPaused: {}", jobKey);
    }

    @Override
    public void jobsPaused(String jobGroup) {
      log.info("jobsPaused: {}", jobGroup);
    }

    @Override
    public void jobResumed(JobKey jobKey) {
      log.info("jobResumed: {}", jobKey);
    }

    @Override
    public void jobsResumed(String jobGroup) {
      log.info("jobsResumed: {}", jobGroup);
    }

    @Override
    public void schedulerError(String msg, SchedulerException cause) {
      log.info("schedulerError: {}, {}", msg, cause);
    }

    @Override
    public void schedulerInStandbyMode() {
      log.info("schedulerInStandbyMode");
    }

    @Override
    public void schedulerStarted() {
      log.info("schedulerStarted");
    }

    @Override
    public void schedulerStarting() {
      log.info("schedulerStarting");
    }

    @Override
    public void schedulerShutdown() {
      log.info("schedulerShutdown");
    }

    @Override
    public void schedulerShuttingdown() {
      log.info("schedulerShuttingdown");
    }

    @Override
    public void schedulingDataCleared() {
      log.info("schedulingDataCleared");
    }
  }
}
