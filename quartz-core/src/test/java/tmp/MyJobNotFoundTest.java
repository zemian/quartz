package tmp;

import static org.quartz.impl.StdSchedulerFactory.PROP_SCHED_CLASS_LOAD_HELPER_CLASS;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CyclicBarrier;
import org.junit.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.integrations.tests.QuartzMemoryTestSupport;
import org.quartz.simpl.CascadingClassLoadHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyJobNotFoundTest extends QuartzMemoryTestSupport {

  private static Logger LOG = LoggerFactory.getLogger(MyJobNotFoundTest.class);

  @Override
  protected Properties createSchedulerProperties() {
    Properties props = TestUtils.postgresProps();
    props.put(PROP_SCHED_CLASS_LOAD_HELPER_CLASS, MyJobNotFoundClassLoadHelper.class.getName());
    return props;
  }

  @Override
  protected void afterSchedulerInit() throws Exception {
    LOG.info("Clear all scheduler data");
    scheduler.clear();

    // Setup Listeners
    scheduler.getListenerManager().addJobListener(new TestUtils.MyJobListener());
    scheduler.getListenerManager().addTriggerListener(new TestUtils.MyTriggerListener());
    scheduler.getListenerManager().addSchedulerListener(new TestUtils.MySchedulerListener());

    LOG.info("Start scheduler");
    scheduler.start();
  }

  @Test
  public void testPostgres() throws Exception {
    CyclicBarrier barrier = new CyclicBarrier(2);
    scheduler.getContext().put("barrier", barrier);

    JobDetail job1 = JobBuilder.newJob(TestUtils.MyJob.class).withIdentity("bad").
        //usingJobData("pauseTime", "30000").
        //usingJobData("createError", "Just a test").
            build();
    JobDetail job2 = JobBuilder.newJob(TestUtils.DisallowConcJob.class).withIdentity("good")
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

    //barrier.await(20, TimeUnit.SECONDS);
    Thread.sleep(30_000L);
    LOG.info("Testing is done.");
  }

  public static class MyJobNotFoundClassLoadHelper extends CascadingClassLoadHelper {

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
      if (TestUtils.MyJob.class.getName().equals(name)) {
        throw new ClassNotFoundException();
      } else {
        return super.loadClass(name);
      }
    }
  }
}
