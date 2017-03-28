## 项目相关


### 一、编码规范
#### java相关
* 项目以业务模块分包，不允许出现不同功能模块的文件错位
* 驼峰命名法
* Activity文件以Activity结尾，Fragment，Adapter等依次类推
* 运算符两边必须加空格比如‘int a = 1;’
* 分隔符后必须加空格比如‘{a, b, c}’
* java代码中不允许出现中文字符串，必须在strings.xml中定义

#### xml相关
* 文件命名：列表以list_打头 activity以activity_打头 fragment以fragment_打头
其他布局以layout_打头
* id命名： TextView以tv_打头 ImageView以img_打头 其他layout都以layout_打头
其他特殊布局或特殊控件比如PullRefreshView，以prv打头，以此类推
* xml代码中不允许出现中文字符串，必须在strings.xml中定义

#### 文件位置
* 所有图片资源放在mipmap文件夹下，生成hdpi,xhdpi,xxhdpi三种规格，不允许放入drawable文件夹下
* 图片命名尽量不使用数字标记

### 二、Third Library
#### 安居客android架构：https://github.com/BaronZ88/MinimalistWeather
* 在该框架上进行修改，抛弃了dagger2的依赖注入框架
* 注意mvp设计模式的编码规范，不允许出现不同层级工作任务混淆的问题

#### 沉浸式状态栏 https://github.com/jgilfelt/SystemBarTint
* 注意style项目主题的设置

#### 动态权限管理 https://github.com/hongyangAndroid/MPermissions
* 注意manifest中的权限申请，否则不会调用解释方法
* 不能在library中引用库，否则会出现运行时错误

#### MaterialDialog弹框 https://github.com/drakeet/MaterialDialog
