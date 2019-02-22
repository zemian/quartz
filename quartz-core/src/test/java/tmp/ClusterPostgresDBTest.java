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

public class ClusterPostgresDBTest extends QuartzMemoryTestSupport {

  private static Logger LOG = LoggerFactory.getLogger(ClusterPostgresDBTest.class);

  @Override
  protected Properties createSchedulerProperties() {
    Properties props = TestUtils.postgresProps();
    props.put("org.quartz.scheduler.instanceName", "MyClusterPostgresTmpTestScheduler");
    props.put("org.quartz.jobStore.isClustered", "true");
    return props;
  }

  @Override
  protected void afterSchedulerInit() throws Exception {
    LOG.info("Clear all scheduler data");
    scheduler.clear();

//    // Add scheduler listeners
//    scheduler.getListenerManager().addJobListener(new TestUtils.MyJobListener());
//    scheduler.getListenerManager().addTriggerListener(new TestUtils.MyTriggerListener());
//    scheduler.getListenerManager().addSchedulerListener(new TestUtils.MySchedulerListener());

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
