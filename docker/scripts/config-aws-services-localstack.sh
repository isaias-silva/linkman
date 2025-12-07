#!/bin/sh

sleep 5
echo "[!] sqs config"

aws --endpoint-url=http://localstack:4566 sqs create-queue\
      --queue-name mail.fifo \
      --attributes FifoQueue=true;
      aws --endpoint-url=http://localstack:4566 sqs create-queue --queue-name metrics;
      echo 'queues created'

echo "[!] s3 bucket config"

aws --endpoint-url=http://localstack:4566 s3api create-bucket --bucket profile
