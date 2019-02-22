package tmp;

import java.util.HashSet;
import java.util.Properties;
import org.junit.Test;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public class PersistJobDataTest extends TestUtils.PostgresTestSupport {
  @Test
  public void testPostgres() throws Exception {
    JobDetail job1 = JobBuilder.newJob(TestUtils.DisallowConcJob.class).withIdentity("job1")
        .usingJobData("updateCount", 0)
        .build();

    HashSet<Trigger> triggers = new HashSet<>();
    triggers.add(TriggerBuilder.newTrigger().forJob(job1)
        .withSchedule(CronScheduleBuilder.cronSchedule("0/3 * * * * ?"))
        .build());

    scheduler.scheduleJob(job1, triggers, true);

    Thread.sleep(30_000L);
    log.info("Testing is done.");
  }
}
