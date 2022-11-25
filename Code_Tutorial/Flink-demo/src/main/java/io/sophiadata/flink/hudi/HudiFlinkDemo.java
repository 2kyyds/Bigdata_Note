package io.sophiadata.flink.hudi;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import io.sophiadata.flink.base.BaseSql;

/** (@SophiaData) (@date 2022/10/30 16:28). */
public class HudiFlinkDemo extends BaseSql {

    public static void main(String[] args) {
        //
        new HudiFlinkDemo().init(args, "HudiFlinkDemo", false, true);
    }

    @Override
    public void handle(
            StreamExecutionEnvironment env, StreamTableEnvironment tEnv, ParameterTool params) {
        tEnv.executeSql(
                "CREATE TABLE sourceT (\n"
                        + "  uuid varchar(20),\n"
                        + "  name varchar(10),\n"
                        + "  age int,\n"
                        + "  ts timestamp(3),\n"
                        + "  `partition` varchar(20)\n"
                        + ") WITH (\n"
                        + "  'connector' = 'datagen',\n"
                        + "  'rows-per-second' = '1'\n"
                        + ")");

        tEnv.executeSql(
                "create table t2(\n"
                        + "  uuid varchar(20),\n"
                        + "  name varchar(10),\n"
                        + "  age int,\n"
                        + "  ts timestamp(3),\n"
                        + "  `partition` varchar(20)\n"
                        + ")\n"
                        + "with (\n"
                        + "  'connector' = 'hudi',\n"
                        + "  'path' = '/tmp/hudi_flink/t2',\n"
                        + "  'table.type' = 'MERGE_ON_READ'\n"
                        + ")");

        tEnv.executeSql("insert into t2 select * from sourceT");
    }
}