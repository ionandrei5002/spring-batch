package io.spring.batch.chapter04;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobListenerFactoryBean;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@EnableBatchProcessing
@SpringBootApplication
public class Chapter04Application {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public CompositeJobParametersValidator validator() {
        CompositeJobParametersValidator validator = new CompositeJobParametersValidator();

        DefaultJobParametersValidator defaultJobParametersValidator = new DefaultJobParametersValidator(
            new String[] {"fileName"},
            new String[] {"name", "currentDate"}
        );

        defaultJobParametersValidator.afterPropertiesSet();

        validator.setValidators(
                Arrays.asList(new ParameterValidator(), defaultJobParametersValidator)
        );

        return validator;
    }

    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("basicJob")
                .start(step1())
                .validator(validator())
                .incrementer(new DailyJobTimestamperImpl())
//                .listener(new JobLoggerListenerImpl())
                .listener(JobListenerFactoryBean.getListener(
                        new JobLoggerListener()
                ))
                .build();
    }

    @Bean
    public Step step1() {
        return this.stepBuilderFactory.get("step1")
//                .tasklet(helloWorldTasklet(null, null))
                .tasklet(new HelloWorldImpl())
                .build();
    }

    @StepScope
    @Bean

    public Tasklet helloWorldTasklet(
            @Value("#{jobParameters['name']}") String name,
            @Value("#{jobParameters['fileName']}") String fileName
    ) {
            return (contribution, chunkContext) -> {
                System.out.println(String.format("Hello, %s", name));
                System.out.println(String.format("fileName = %s", fileName));
                return RepeatStatus.FINISHED;
            };

//        return (contribution, chunkContext) -> {
//            String name = (String) chunkContext.getStepContext()
//                    .getJobParameters()
//                    .get("name");
//            System.out.println(String.format("Hello, %s!", name));
//            return RepeatStatus.FINISHED;
//        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Chapter04Application.class, args);
    }

}
