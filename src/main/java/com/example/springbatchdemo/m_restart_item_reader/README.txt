第一次启动的时候，要模拟错误，所以启动 local 环境，然后还要确保 restart_demo_file.txt 里面有一行的 Name 包含 wrong_name 来触发异常。

第二次启动的时候，要启动 local 环境 + stop-init 环境(stop-init.yml)，还要将 restart_demo_file.txt 中的 wrong_name 替换为其他的字符串。