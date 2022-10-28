# DemoClient：一个安卓源码研究，各种新技术尝鲜的demo项目

## 项目介绍

一个安卓源码研究，各种新技术尝鲜的demo项目。 包含

* 插件化
* JNI使用/动态注册等等
* AIDL研究
* 字节码插桩
* WCDB数据库研究
* 子线程刷新UI尝试
* flutter混合开发
* compose基础开发
* compose+MVI架构
* 内存共享传输
* 性能优化
* Retrofit源码探究
* View原理探究
* MMKV原理探究
* 绘制性能监控
* APT技术

## 项目结构介绍

* annotationLibrary:注解声明库，app和reouter_processor以来其注解的声明
* app:
* applibrary:
* appplugin:
* composeapp:
* DemoLint:
* gradle_demo
* gradleplugin
* keystore:
* repo:
* router_processor：APT技术的使用。编译过程中对注解进行处理。
* routerapi
* jvmti:一个虚拟机监控框架 <a href="https://github.com/aa5279aa/android_all_demo/tree/master/DemoClient/READMELIST#README_JVMTI">JVMTI详细介绍</a>

## 目录

* 插件化：app/src/com/xt/client/fragment/DynamicFragment
* JNI使用/动态注册等等
* AIDL研究
* 字节码插桩
* WCDB数据库研究
* 子线程刷新UI尝试
* flutter混合开发
* compose基础开发
* compose+MVI架构
* 内存共享传输
* 性能优化
* Retrofit源码探究
* View原理探究
* MMKV原理探究
* 绘制性能监控
* APT技术:APT生成配置文件，然后利用ServiceLoader，启动时加载实现某个接口的所有类。


## 备注

欢迎fork和PR，按照原有结构提交即可。

## 更新记录；
2022.07.19:更新readme
2022.08.19:添加APT功能1，自动生成resources/META-INF/配置文件
2022.08.21:添加APT功能2，自动生成java class文件，进行相关路由配置。
  


