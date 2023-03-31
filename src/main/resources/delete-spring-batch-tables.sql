SET foreign_key_checks=0; -- 关闭外键检查
drop table if exists BATCH_JOB_INSTANCE;
drop table if exists BATCH_JOB_EXECUTION;
drop table if exists BATCH_JOB_EXECUTION_PARAMS;
drop table if exists BATCH_STEP_EXECUTION;
drop table if exists BATCH_STEP_EXECUTION_CONTEXT;
drop table if exists BATCH_JOB_EXECUTION_CONTEXT;
drop table if exists BATCH_STEP_EXECUTION_SEQ;
drop table if exists BATCH_JOB_EXECUTION_SEQ;
drop table if exists BATCH_JOB_SEQ;
SET foreign_key_checks=1; -- 开启外键检查