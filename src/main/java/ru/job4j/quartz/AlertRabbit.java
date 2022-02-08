package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class AlertRabbit {

    /**
     *читаем файл конфигурации
     */
    private static Properties init(String properties) {
        Properties config = new Properties();
        try (InputStream in = AlertRabbit.class.getClassLoader()
                .getResourceAsStream(properties)) {
            config.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }

    /**
     * подключаемся к БД
     * @param config - контейнер с настройками
     * @return - конект к базе
     * */
    private static Connection connection(Properties config)
            throws ClassNotFoundException, SQLException {

        Class.forName(config.getProperty("driver-class-name"));
        Connection cn = DriverManager.getConnection(
                config.getProperty("url"),
                config.getProperty("username"),
                config.getProperty("password")
        );
        return cn;
    }

    public static void main(String[] args) {

        Properties properties = init("rabbit.properties");
        try (Connection connect = connection(properties)) {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("connect", connect);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(propertiesInfo())
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * читаем настройки для интервала
     * @return интервал
     */
    public static int propertiesInfo() {
        int rabbitInterval = 0;
        try (InputStream in = AlertRabbit.class.getClassLoader()
                .getResourceAsStream(
                        "rabbit.properties")) {
            Properties properties = new Properties();
            properties.load(in);
            rabbitInterval = Integer.parseInt(properties.getProperty("rabbit.interval"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rabbitInterval;
    }

    public static class Rabbit implements Job {
        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
            Connection cn = (Connection) context.getJobDetail().getJobDataMap().get("connect");
        }
    }
}

