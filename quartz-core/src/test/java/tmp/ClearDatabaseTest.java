package tmp;

import java.util.Properties;
import org.junit.Test;
import org.quartz.integrations.tests.QuartzMemoryTestSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClearDatabaseTest extends QuartzMemoryTestSupport {

  private static Logger LOG = LoggerFactory.getLogger(ClearDatabaseTest.class);

  @Override
  protected Properties createSchedulerProperties() {
    Properties props = TestUtils.postgresProps();
    return props;
  }

  @Override
  protected void afterSchedulerInit() throws Exception {
    LOG.info("Clear all scheduler data");
    scheduler.clear();

    // Add scheduler listeners
    scheduler.getListenerManager().addJobListener(new TestUtils.MyJobListener());
    scheduler.getListenerManager().addTriggerListener(new TestUtils.MyTriggerListener());
    scheduler.getListenerManager().addSchedulerListener(new TestUtils.MySchedulerListener());

    LOG.info("Start scheduler");
    scheduler.start();
  }

  @Test
  public void testPostgres() throws Exception {
    LOG.info("Testing is done.");
  }
}
