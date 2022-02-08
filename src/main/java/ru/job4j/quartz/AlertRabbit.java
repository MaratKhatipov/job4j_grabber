package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class AlertRabbit {
    public static void main(String[] args) {
        try {
            /**
             * 1. Конфигурирование.
             * Начало работы происходит с создания класса управляющего всеми работами.
             * В объект Scheduler мы будем добавлять задачи, которые хотим выполнять периодически.
             */
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            /**
             * 2. Создание задачи.
             * quartz каждый раз создает объект с типом org.quartz.Job,
             * нужно создать класс реализующий этот интерфейс.
             */
            JobDetail job = newJob(Rabbit.class).build();
            /**
             * 3. Создание расписания.
             * Конструкция выше настраивает периодичность запуска.
             * В нашем случае, мы будем запускать задачу через 10 секунд и делать это бесконечно.
             */
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(propertiesInfo())
                    .repeatForever();
            /**
             * 4. Задача выполняется через триггер.
             * Здесь можно указать, когда начинать запуск.
             * Мы хотим сделать это сразу.
             */
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            /**
             * 5. Загрузка задачи и триггера в планировщик
             *
             */
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

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

    /**
     * Внутри этого класса нужно описать требуемые действия.
     * В нашем случае - это вывод на консоль текста.
     */
    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
        }
    }
}

