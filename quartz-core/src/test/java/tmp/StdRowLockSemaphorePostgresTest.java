package tmp;

import java.util.HashSet;
import java.util.Properties;
import org.junit.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public class StdRowLockSemaphorePostgresTest extends TestUtils.PostgresTestSupport {

  @Override
  protected Properties createSchedulerProperties() {
    Properties props = super.createSchedulerProperties();
    props.setProperty("org.quartz.jobStore.lockHandler.class", "org.quartz.impl.jdbcjobstore.StdRowLockSemaphore");
    props.setProperty("org.quartz.jobStore.lockHandler.maxRetry", "7");
    props.setProperty("org.quartz.jobStore.lockHandler.retryPeriod", "3000");
    props.setProperty("org.quartz.jobStore.acquireTriggersWithinLock", "true");
    return props;
  }

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
