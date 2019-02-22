package tmp;

import java.util.Date;
import java.util.HashSet;
import org.junit.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tmp.TestUtils.MyJobListener;

public class VetoJobTest extends TestUtils.PostgresTestSupport {
  public static class JobVetoListener extends TestUtils.MyTriggerListener {

    private static Logger LOG = LoggerFactory.getLogger(MyJobListener.class);

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

  @Override
  protected void beforeSchedulerStart() throws Exception {
    // Add scheduler listeners
    scheduler.getListenerManager().addJobListener(new TestUtils.MyJobListener());
    scheduler.getListenerManager().addSchedulerListener(new TestUtils.MySchedulerListener());

    scheduler.getListenerManager().addTriggerListener(new JobVetoListener());
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
}
