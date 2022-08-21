# android_all_demo
一直觉得研究各种技术，一个个demo的下载运行太费劲了，为什么不能有一个所有新技术的融合体demo呢？
这个项目就为此而创建。
项目包含几个部分：
# 1.DemoClient  
android工程，包含了应用于android的所有技术的集合。

为了避免各种异常的问题，建议如下配置：
## 1.project.build.gradle中使用tool的版本为7.2.0
classpath 'com.android.tools.build:gradle:7.2.0'
## 2.gradle-wrapper.properties中配置为：7.3.3
distributionUrl=https\://services.gradle.org/distributions/gradle-7.3.3-bin.zip
## 3.由于国内网络问题，建议jcenter,gooole等镜像地址改为阿里云的。可以实现科学上网的请忽略此条。
project.build.gradle做如下配置：
allprojects {  
    repositories {  
//        jcenter()  
//        google()  
        maven { url 'https://maven.aliyun.com/repository/google' }  
        maven { url 'https://maven.aliyun.com/repository/jcenter' }  
        maven { url 'http://maven.aliyun.com/nexus/content/groups/public' }   
        maven { url "https://jitpack.io" }  
    }  
}

# 后台项目暂未搭建 

# 4.DemoClient详细功能详见
<a href="https://github.com/aa5279aa/android_all_demo/tree/master/DemoClient#readme">DemoClient详细介绍</a>

