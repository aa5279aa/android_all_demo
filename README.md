# android_all_demo
一直觉得研究各种技术，一个个demo的下载运行太费劲了，为什么不能有一个所有新技术的融合体demo呢？
这个项目就为此而创建。
项目包含几个部分：
# 1.DemoClient  
android工程，包含了应用于android的所有技术的集合。

为了避免各种异常的问题，建议如下配置：
## 1.project.build.gradle中使用tool的版本为3.2.0 
classpath 'com.android.tools.build:gradle:3.2.0'
## 2.gradle-wrapper.properties中配置为：4.6
distributionUrl=https\://services.gradle.org/distributions/gradle-4.6-all.zip
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

# 2.DemoServe   
java的服务，可以直接部署到tomcat。用于提供服务给客户端，比如提供protobuf的服务。
# 3.DemoDependent    
第三方项目的工程。维护protobuf契约文件的工程。
