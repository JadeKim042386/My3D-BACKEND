#!/bin/bash
PROJECT_NAME="my3d_backend"
JAR_PATH="/home/ubuntu/my3d_backend/build/libs/*.jar"
DEPLOY_PATH="/home/ubuntu/$PROJECT_NAME/"
DEPLOY_LOG_PATH="/home/ubuntu/$PROJECT_NAME/deploy.log"
DEPLOY_ERR_LOG_PATH="/home/ubuntu/$PROJECT_NAME/deploy_err.log"
APPLICATION_LOG_PATH="/home/ubuntu/$PROJECT_NAME/application.log"
BUILD_JAR=$(ls $JAR_PATH)
JAR_NAME=$(basename $BUILD_JAR)
STATIC_PATH="/home/ubuntu/$PROJECT_NAME/src/main/resources/static"

echo "===== 배포 시작 : $(date +%c) =====" >> $DEPLOY_LOG_PATH

echo "> build 파일명: $JAR_NAME" >> $DEPLOY_LOG_PATH
echo "> build 파일 복사" >> $DEPLOY_LOG_PATH
cp $BUILD_JAR $DEPLOY_PATH

# -- Deploy
IDLE_PROFILE="prod"
IDLE_APPLICATION_PATH=$DEPLOY_PATH$JAR_NAME

echo "> $IDLE_PROFILE 배포" >> $DEPLOY_LOG_PATH
nohup java -jar -DSpring.profiles.active=$IDLE_PROFILE $IDLE_APPLICATION_PATH >> $APPLICATION_LOG_PATH 2> $DEPLOY_ERR_LOG_PATH &

sleep 3

echo "> 배포 종료 : $(date +%c)" >> $DEPLOY_LOG_PATH
