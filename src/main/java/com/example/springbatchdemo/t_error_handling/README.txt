启动的时候，要使用 local 和 stop-init 环境（application-stop-init.yml）。

启动两次（也就是测试完一次成功和一次失败）之后，任务会被标记为完成。

此时如果还想再测试，要先执行 delete-spring-batch-tables.sql 脚本来重置数据库，再继续执行。

或者，第一次模拟成功的时候，使用 local 环境。第二次模拟失败的时候，使用 local 和 stop-init 环境。