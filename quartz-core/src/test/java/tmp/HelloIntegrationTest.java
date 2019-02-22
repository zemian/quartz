package tmp;

import static org.quartz.DateBuilder.futureDate;

import java.util.Date;
import org.junit.Test;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.integrations.tests.HelloJob;
import org.quartz.integrations.tests.QuartzDatabaseTestSupport;

public class HelloIntegrationTest extends QuartzDatabaseTestSupport {
  @Test
  public void testHello() throws Exception {
    JobDetail job1 = JobBuilder.newJob(HelloJob.class).withIdentity("job1").build();

//    Trigger tr = TriggerBuilder.newTrigger().forJob(job1)
//        .startAt(new Date(System.currentTimeMillis() + 5_000L))
//        .endAt(new Date(System.currentTimeMillis() + 8_000L))
//        .withSchedule(
//            SimpleScheduleBuilder
//                .repeatSecondlyForTotalCount(1)
//                .withMisfireHandlingInstructionNextWithRemainingCount()
//        )
//        .build();

    Trigger tr = TriggerBuilder.newTrigger().withIdentity("test")
        .startAt(futureDate(15, DateBuilder.IntervalUnit.MINUTE))
        .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(1).withIntervalInMinutes(1))
        .withPriority(1)
        .forJob(job1)
        .build();

    scheduler.scheduleJob(job1, tr);
    //scheduler.pauseTrigger(tr.getKey());
    //Thread.sleep(13_000L);
    //scheduler.resumeTrigger(tr.getKey());

    Thread.sleep(17_000L);
    LOG.info("Testing is done.");
  }
}
