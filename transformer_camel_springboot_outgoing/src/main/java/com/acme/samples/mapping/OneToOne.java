package com.acme.samples.mapping;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class OneToOne extends RouteBuilder {

        public static final String SCENARIO_1 = "SCENARIO_1";
        public static final String SCENARIO_2 = "SCENARIO_2";
        public static final String SCENARIO_3 = "SCENARIO_3";
        public static final String SCENARIO_4 = "SCENARIO_4";
        public static final String SCENARIO_5 = "SCENARIO_5";
        public static final String SCENARIO_6 = "SCENARIO_6";

        public static final String DIRECT_IN1 = "direct:" + SCENARIO_1 + "In";
        public static final String DIRECT_OUT1 = "direct:" + SCENARIO_1 + "Out";
        public static final String DIRECT_IN2 = "direct:" + SCENARIO_2 + "In";
        public static final String DIRECT_OUT2 = "direct:" + SCENARIO_2 + "Out";
        public static final String DIRECT_IN3 = "direct:" + SCENARIO_3 + "In";
        public static final String DIRECT_OUT3 = "direct:" + SCENARIO_3 + "Out";
        public static final String DIRECT_IN4 = "direct:" + SCENARIO_4 + "In";
        public static final String DIRECT_OUT4 = "direct:" + SCENARIO_4 + "Out";
        public static final String DIRECT_IN5 = "direct:" + SCENARIO_5 + "In";
        public static final String DIRECT_OUT5 = "direct:" + SCENARIO_5 + "Out";
        public static final String DIRECT_IN6 = "direct:" + SCENARIO_6 + "In";
        public static final String DIRECT_OUT6 = "direct:" + SCENARIO_6 + "Out";

        @Override
        public void configure() {
//Scenario Inputs
                from("jms:queue://{{out.bds.start.queue}}?connectionFactory=#jmsConnectionFactory").log("${body}")
                        .wireTap(DIRECT_IN1)
                        .to(DIRECT_IN2);
                from("file:{{web.bds.upload.filepath}}")
                        .wireTap(DIRECT_IN1)
                        .to(DIRECT_IN2);

                //from("jms:queue://{{out.2.start.queue}}?connectionFactory=#jmsConnectionFactory").log("${body}").to(DIRECT_IN2);
                //from("file:{{out.2.start.path}}").to(DIRECT_IN2);

                from("jms:queue://{{out.murex.start.queue}}?connectionFactory=#jmsConnectionFactory").log("${body}")
                        .wireTap(DIRECT_IN3)
                        .to(DIRECT_IN4);
                from("file:{{web.murex.upload.filepath}}")
                        .wireTap(DIRECT_IN3)
                        .to(DIRECT_IN4);

                //from("jms:queue://{{out.4.start.queue}}?connectionFactory=#jmsConnectionFactory").log("${body}").to(DIRECT_IN4);
                //from("file:{{out.4.start.path}}").to(DIRECT_IN4);

                //from("jms:queue://{{out.5.start.queue}}?connectionFactory=#jmsConnectionFactory").log("${body}").to(DIRECT_IN5);
                //from("file:{{out.5.start.path}}").to(DIRECT_IN5);

                //from("jms:queue://{{out.6.start.queue}}?connectionFactory=#jmsConnectionFactory").log("${body}").to(DIRECT_IN6);
                //from("file:{{out.6.start.path}}").to(DIRECT_IN6);
 
//Scenario Outputs
                from(DIRECT_IN1).id(SCENARIO_1)
                .to("txfrmr:com.alliance.rentasToSwift/rentasOutwardToMT103")
                .log("----------Scenario 1 : After Transformed-----------------")
                .log("${body}")
                .log("---------------------------------------------------------")
                .wireTap("jms:queue://{{out.end.queue}}?connectionFactory=#jmsConnectionFactory")
                .to("file:{{web.output.filepath}}?fileName=${file:onlyname.noext}-MT103.out");

                from(DIRECT_IN2).id(SCENARIO_2)
                .to("txfrmr:com.alliance.rentasToPacs/rentasOutwardToPacs08")
                .log("----------Scenario 2 : After Transformed-----------------")
                .log("${body}")
                .log("---------------------------------------------------------")
                .convertBodyTo(String.class)
                .wireTap("jms:queue://{{out.end.queue}}?connectionFactory=#jmsConnectionFactory")
                .to("file:{{web.output.filepath}}?fileName=${file:onlyname.noext}-MX008.out");
                
                from(DIRECT_IN3).id(SCENARIO_3)
                .to("txfrmr:com.alliance.rentasToSwift/rentasOutgoingToSwift")
                .log("----------Scenario 3/5 : After Transformed-----------------")
                .log("${body}")
                .log("---------------------------------------------------------")
                .wireTap("jms:queue://{{out.end.queue}}?connectionFactory=#jmsConnectionFactory")
                .to("file:{{web.output.filepath}}?fileName=${file:onlyname.noext}-MT.out");

                from(DIRECT_IN4).id(SCENARIO_4)
                .to("txfrmr:com.alliance.rentasToPacs/rentasOutgoingToPacs")
                .log("----------Scenario 4/6 : After Transformed-----------------")
                .log("${body}")
                .log("---------------------------------------------------------")
                .wireTap("jms:queue://{{out.end.queue}}?connectionFactory=#jmsConnectionFactory")
                .to("file:{{web.output.filepath}}?fileName=${file:onlyname.noext}-MX.out");

                /*
                from(DIRECT_IN5).id(SCENARIO_5)
                .to("txfrmr:com.alliance.rentasToSwift/rentasOutgoingToSwift")
                .log("----------Scenario 5/3 : After Transformed-----------------")
                .log("${body}")
                .log("---------------------------------------------------------")
                .wireTap("jms:queue://{{out.5.end.queue}}?connectionFactory=#jmsConnectionFactory")
                .to("file:{{out.5.end.path}}?fileName=${file:onlyname.noext}.out");

                from(DIRECT_IN6).id(SCENARIO_6)
                .to("txfrmr:com.alliance.rentasToPacs/rentasOutgoingToPacs")
                .log("----------Scenario 6/4 : After Transformed-----------------")
                .log("${body}")
                .log("---------------------------------------------------------")
                .wireTap("jms:queue://{{out.6.end.queue}}?connectionFactory=#jmsConnectionFactory")
                .to("file:{{out.6.end.path}}?fileName=${file:onlyname.noext}.out");
                */
        }
}