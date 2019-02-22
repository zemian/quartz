package tmp;

import java.util.HashSet;
import org.junit.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public class PostgresDatabaseTest extends TestUtils.PostgresTestSupport {
  @Test
  public void testPostgres() throws Exception {
    JobDetail job1 = JobBuilder.newJob(TestUtils.MyJob.class).withIdentity("job1")
        .build();

    HashSet<Trigger> triggers = new HashSet<>();
    triggers.add(TriggerBuilder.newTrigger().forJob(job1)
        .build());
    triggers.add(TriggerBuilder.newTrigger().forJob(job1)
        .build());

    scheduler.scheduleJob(job1, triggers, true);

    Thread.sleep(3_000L);
    log.info("Testing is done.");
  }
}
