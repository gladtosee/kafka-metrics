/*
 * Copyright 2015 Michal Harish, michal.harish@gmail.com
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.amient.kafka.metrics;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ProducerPublisher implements MeasurementPublisher {

    private final KafkaProducer producer;
    private final String topic;

    public ProducerPublisher(final String kafkaBootstrapServers, final String topic) {
        this.topic = topic;
        this.producer = new KafkaProducer<String, Object>(new Properties() {{
            put("bootstrap.servers", kafkaBootstrapServers);
            put("compression.type", "gzip");
            put("batch.size", "250");
            put("linger.ms", "1000");
            put("key.serializer", org.apache.kafka.common.serialization.StringSerializer.class);
            put("value.serializer", io.amient.kafka.metrics.MeasurementSerializer.class);
        }});
    }

    @Override
    public void publish(MeasurementV1 m) {
        producer.send(new ProducerRecord<String, Object>(topic, m.getName().toString(), m));
    }

    @Override
    public void close() {
        producer.close();
    }
}