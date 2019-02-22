package tmp;

import java.util.HashSet;
import java.util.Properties;
import org.junit.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.integrations.tests.QuartzMemoryTestSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostgresDatabaseTest extends QuartzMemoryTestSupport {

  private static Logger LOG = LoggerFactory.getLogger(PostgresDatabaseTest.class);

  @Override
  protected Properties createSchedulerProperties() {
    Properties props = TestUtils.postgresProps();
    return props;
  }

  @Override
  protected void afterSchedulerInit() throws Exception {
    LOG.info("Clear all scheduler data");
    scheduler.clear();

    LOG.info("Start scheduler");
    scheduler.start();
  }

  @Test
  public void testPostgres() throws Exception {
    JobDetail job1 = JobBuilder.newJob(TestUtils.MyJob.class).withIdentity("job1").
            build();

    HashSet<Trigger> triggers = new HashSet<>();
    triggers.add(TriggerBuilder.newTrigger().forJob(job1)
        .build());
    triggers.add(TriggerBuilder.newTrigger().forJob(job1)
        .build());

    scheduler.scheduleJob(job1, triggers, true);

    Thread.sleep(3_000L);
    LOG.info("Testing is done.");
  }
}
