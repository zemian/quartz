package tmp;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public class LongPauseJobTest extends TestUtils.PostgresTestSupport {
  private TestUtils.MySchedulerListener myListener = new TestUtils.MySchedulerListener();

  protected void beforeSchedulerStart() throws Exception {
    // Setup Listeners
    scheduler.getListenerManager().addJobListener(new TestUtils.MyJobListener());
    scheduler.getListenerManager().addTriggerListener(new TestUtils.MyTriggerListener());
    scheduler.getListenerManager().addSchedulerListener(new TestUtils.MySchedulerListener());
  }

  @Test
  public void testPostgres() throws Exception {
    JobDetail job1 = JobBuilder.newJob(TestUtils.MyJob.class).withIdentity("job1")
        .usingJobData("pauseTime", "30000")
        .usingJobData("createError", "Just a test")
        .build();
    JobDetail job2 = JobBuilder.newJob(TestUtils.DisallowConcJob.class).withIdentity("job2")
        .build();

    long now = System.currentTimeMillis();
    Map<JobDetail, Set<? extends Trigger>> toSchedule = new HashMap<>();
    toSchedule.put(job1, Collections.singleton(
        TriggerBuilder.newTrigger().forJob(job1)
            .startAt(new Date(now + 1))
            .build()));
    toSchedule.put(job2, Collections.singleton(
        TriggerBuilder.newTrigger().forJob(job2)
            .startAt(new Date(now))
            .build()));

    scheduler.scheduleJobs(toSchedule, true);

    Thread.sleep(60_000L);
    log.info("Testing is done.");
  }
}
