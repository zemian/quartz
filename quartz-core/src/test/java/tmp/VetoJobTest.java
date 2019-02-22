package tmp;

import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import org.junit.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.integrations.tests.QuartzMemoryTestSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VetoJobTest extends QuartzMemoryTestSupport {

  private static Logger LOG = LoggerFactory.getLogger(VetoJobTest.class);

  @Override
  protected Properties createSchedulerProperties() {
    Properties props = TestUtils.postgresProps();
    return props;
  }

  @Override
  protected void afterSchedulerInit() throws Exception {
    LOG.info("Clear all scheduler data");
    scheduler.clear();

    // Add scheduler listeners
    scheduler.getListenerManager().addJobListener(new TestUtils.MyJobListener());
    scheduler.getListenerManager().addSchedulerListener(new TestUtils.MySchedulerListener());

    scheduler.getListenerManager().addTriggerListener(new JobVetoListener());

    LOG.info("Start scheduler");
    scheduler.start();
  }

  @Test
  public void testPostgres() throws Exception {
    JobDetail job1 = JobBuilder.newJob(TestUtils.MyJob.class).withIdentity("vetoJobTest1").
        usingJobData("pauseTime", "30000").
        build();

    long now = System.currentTimeMillis();
    HashSet<Trigger> triggers = new HashSet<>();
    triggers.add(TriggerBuilder.newTrigger().forJob(job1)
        .startAt(new Date(now + 1))
        .build());
    triggers.add(TriggerBuilder.newTrigger().forJob(job1)
        .startAt(new Date(now + 1))
        .build());

    scheduler.scheduleJob(job1, triggers, true);

    Thread.sleep(60_000L);
  }

  public static class JobVetoListener extends TestUtils.MyTriggerListener {

    // NOTE this is a trigger listener!
    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
      if (trigger.getKey().getName().equals("vetoJobTestTrigger2")) {
        LOG.info("Veto-ing trigger: " + trigger.getKey());
        return true;
      }
      return false;
    }
  }
}
