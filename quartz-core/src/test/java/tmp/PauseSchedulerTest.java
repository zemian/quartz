package tmp;

import java.util.HashSet;
import org.junit.Test;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public class PauseSchedulerTest extends TestUtils.PostgresTestSupport {
  @Override
  protected void beforeSchedulerStart() throws Exception {
    // Add scheduler listeners
    scheduler.getListenerManager().addJobListener(new TestUtils.MyJobListener());
    scheduler.getListenerManager().addTriggerListener(new TestUtils.MyTriggerListener());
    scheduler.getListenerManager().addSchedulerListener(new TestUtils.MySchedulerListener());
  }

  @Test
  public void testPostgres() throws Exception {
    JobDetail job1 = JobBuilder.newJob(TestUtils.MyJob.class).withIdentity("job1")
        //.usingJobData("pauseTime", "30000").
        .build();

    long now = System.currentTimeMillis();
    HashSet<Trigger> triggers = new HashSet<>();
    triggers.add(TriggerBuilder.newTrigger().forJob(job1)
        .withSchedule(CronScheduleBuilder.cronSchedule("*/3 * * * * ?"))
        .build());
    triggers.add(TriggerBuilder.newTrigger().forJob(job1)
        .withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?"))
        .build());

    scheduler.scheduleJob(job1, triggers, true);

    Thread.sleep(10_000L);
    log.info("Pausing scheduler");
    scheduler.standby();

    Thread.sleep(30_000L);
    log.info("Resume scheduler");
    // NOTE this is different than scheduler.pauseAll() on triggers only.
    scheduler.start();

    Thread.sleep(30_000L);
    log.info("Testing is done.");
  }
}
