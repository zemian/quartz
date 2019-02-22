package tmp;

import org.junit.Test;

public class ClearDatabaseTest extends TestUtils.PostgresTestSupport {

  @Override
  protected void beforeSchedulerStart() throws Exception {
    // Add scheduler listeners
    scheduler.getListenerManager().addJobListener(new TestUtils.MyJobListener());
    scheduler.getListenerManager().addTriggerListener(new TestUtils.MyTriggerListener());
    scheduler.getListenerManager().addSchedulerListener(new TestUtils.MySchedulerListener());
  }

  @Test
  public void testPostgres() throws Exception {
    log.info("Testing is done.");
  }
}
