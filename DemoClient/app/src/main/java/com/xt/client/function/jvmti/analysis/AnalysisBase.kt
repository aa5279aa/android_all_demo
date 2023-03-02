package com.xt.client.function.jvmti.analysis

abstract class AnalysisBase {


    fun findValueByKey(line: String, key: String): String {
        val keyIndex = line.indexOf(key)
        return line.substring(
            line.indexOf("{", keyIndex)+1,
            line.indexOf("}", keyIndex)
        );
    }


}