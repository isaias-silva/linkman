#!/bin/sh

awslocal sqs list-queues >/dev/null 2>&1
if [ $? -ne 0 ]; then
  echo "SQS not ready"
  exit 1
fi

awslocal s3 ls >/dev/null 2>&1
if [ $? -ne 0 ]; then
  echo "S3 not ready"
  exit 1
fi

echo "OK"
exit 0