package com.acme.samples.mapping;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * @author Will Barrett
 */
@Component
public class OneToOne extends RouteBuilder {
        public static final String SCENARIO_7 = "SCENARIO_7";
        public static final String SCENARIO_8 = "SCENARIO_8";
        public static final String SCENARIO_9 = "SCENARIO_9";
        public static final String SCENARIO_10 = "SCENARIO_10";

        public static final String DIRECT_IN7 = "direct:" + SCENARIO_7 + "In";
        public static final String DIRECT_OUT7 = "direct:" + SCENARIO_7 + "Out";
        public static final String DIRECT_IN8 = "direct:" + SCENARIO_8 + "In";
        public static final String DIRECT_OUT8 = "direct:" + SCENARIO_8 + "Out";
        public static final String DIRECT_IN9 = "direct:" + SCENARIO_9 + "In";
        public static final String DIRECT_OUT9 = "direct:" + SCENARIO_9 + "Out";
        public static final String DIRECT_IN10 = "direct:" + SCENARIO_10 + "In";
        public static final String DIRECT_OUT10 = "direct:" + SCENARIO_10 + "Out";

        @Override
        public void configure() {
//Scenario Inputs
                from("jms:queue://{{in.mtprinted.start.queue}}?connectionFactory=#jmsConnectionFactory").log("${body}").to(DIRECT_IN7);
                from("file:{{web.mtprinted.upload.filepath}}").to(DIRECT_IN7);

                from("jms:queue://{{in.mtrentas.start.queue}}?connectionFactory=#jmsConnectionFactory").log("${body}").to(DIRECT_IN8);
                from("file:{{web.mtrentas.upload.filepath}}").to(DIRECT_IN8);

                from("jms:queue://{{in.mxprinted.start.queue}}?connectionFactory=#jmsConnectionFactory").log("${body}").to(DIRECT_IN9);
                from("file:{{web.mxprinted.upload.filepath}}").to(DIRECT_IN9);

                from("jms:queue://{{in.mxrentas.start.queue}}?connectionFactory=#jmsConnectionFactory").log("${body}").to(DIRECT_IN10);
                from("file:{{web.mxrentas.upload.filepath}}").to(DIRECT_IN10);
 
//Scenario Outputs
                from(DIRECT_IN7).id(SCENARIO_7)
                .to("txfrmr:com.alliance.swiftToPrinted/MT103ToPrinted")
                .log("----------Scenario 7 : After Transformed-----------------")
                .log("${body}")
                .log("---------------------------------------------------------")
                .wireTap("jms:queue://{{in.end.queue}}?connectionFactory=#jmsConnectionFactory")
                .to("file:{{web.output.filepath}}?fileName=${file:onlyname.noext}-mtprinted.out");

                from(DIRECT_IN8).id(SCENARIO_8)
                .to("txfrmr:com.alliance.swiftToRentas/MT103ToRentas")
                .log("----------Scenario 8 : After Transformed-----------------")
                .log("${body}")
                .log("---------------------------------------------------------")
                .wireTap("jms:queue://{{in.end.queue}}?connectionFactory=#jmsConnectionFactory")
                .to("file:{{web.output.filepath}}?fileName=${file:onlyname.noext}-mtrentas.out");

                from(DIRECT_IN9).id(SCENARIO_9)
                .to("txfrmr:com.alliance.pacsToPrinted/pacs08ToPrinted")
                .log("----------Scenario 9 : After Transformed-----------------")
                .log("${body}")
                .log("---------------------------------------------------------")
                .wireTap("jms:queue://{{in.end.queue}}?connectionFactory=#jmsConnectionFactory")
                .to("file:{{web.output.filepath}}?fileName=${file:onlyname.noext}-mxprinted.out");

                from(DIRECT_IN10).id(SCENARIO_10)
                .to("txfrmr:com.alliance.pacsToRentas/pacs08ToRentas")
                .log("----------Scenario 10 : After Transformed-----------------")
                .log("${body}")
                .log("---------------------------------------------------------")
                .wireTap("jms:queue://{{in.end.queue}}?connectionFactory=#jmsConnectionFactory")
                .to("file:{{web.output.filepath}}?fileName=${file:onlyname.noext}-mxrentas.out");

        }
}