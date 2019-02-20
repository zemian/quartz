# Quartz Job Scheduler Cookbook

The Quartz cookbook is a collection of succinct code examples of doing specific things with Quartz.

The examples assume you have used static imports of Quartz's DSL classes such as these:

```
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.CalendarIntervalScheduleBuilder.*;
import static org.quartz.JobKey.*;
import static org.quartz.TriggerKey.*;
import static org.quartz.DateBuilder.*;
import static org.quartz.impl.matchers.KeyMatcher.*;
import static org.quartz.impl.matchers.GroupMatcher.*;
import static org.quartz.impl.matchers.AndMatcher.*;
import static org.quartz.impl.matchers.OrMatcher.*;
import static org.quartz.impl.matchers.EverythingMatcher.*;
```

Choose from the following menu of How-Tos:

+ [Instantiating a Scheduler](CreateScheduler.md)
+ [Placing a Scheduler in Stand-by Mode](SchedulerStandby.md)
+ [Shutting Down a Scheduler](ShutdownScheduler.md)
+ [Initializing a Scheduler Within a Servlet Container](ServletInitScheduler.md)
+ [Utilizing Multiple (Non-Clustered) Scheduler Instances](ServletInitScheduler.md)
+ [Defining a Job](DefineJobWithData.md)
+ [Defining and Scheduling a Job](ScheduleJob.md)
+ [Unscheduling a Job](UnscheduleJob.md)
+ [Storing a Job For Later Scheduling](StoreJob.md)
+ [Scheduling an already stored Job](ScheduleStoreJob.md)
+ [Updating an existing Job](UpdateJob.md)
+ [Updating an existing Trigger](UpdateTrigger.md)
+ [Initializing a Scheduler With Job And Triggers Defined in an XML file](JobInitPlugin.md)
+ [Listing Jobs in the Scheduler](ListJobs.md)
+ [Listing Triggers in the Scheduler](ListTriggers.md)
+ [Finding Triggers of a Job](JobTriggers.md)
+ [Using JobListeners](JobListeners.md)
+ [Using TriggerListeners](TriggerListeners.md)
+ [Using SchedulerListeners](SchedulerListeners.md)
+ [Trigger That Fires Every 10 Seconds](TenSecTrigger.md)
+ [Trigger That Fires Every 90 Minutes](NintyMinTrigger.md)
+ [Trigger That Fires Every Day](DailyTrigger.md)
+ [Trigger That Fires Every 2 Days](BiDailyTrigger.md)
+ [Trigger That Fires Every Week](WeeklyTrigger.md)
+ [Trigger That Fires Every 2 Weeks](BiWeeklyTrigger.md)
+ [Trigger That Fires Every Month](MonthlyTrigger.md)
