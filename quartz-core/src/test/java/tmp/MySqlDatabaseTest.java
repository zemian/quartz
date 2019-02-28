package tmp;

import java.util.HashSet;
import org.junit.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public class MySqlDatabaseTest extends TestUtils.MySQLTestSupport {
  @Test
  public void testHello() throws Exception {
    JobDetail job1 = JobBuilder.newJob(TestUtils.MyJob.class).withIdentity("job1")
        .build();

    HashSet<Trigger> triggers = new HashSet<>();
    triggers.add(TriggerBuilder.newTrigger().forJob(job1)
        .build());
    triggers.add(TriggerBuilder.newTrigger().forJob(job1)
        .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(3))
        .build());

    scheduler.scheduleJob(job1, triggers, true);

    Thread.sleep(30_000L);
    log.info("Testing is done.");
  }
}
