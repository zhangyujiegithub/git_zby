-dontoptimize
-dontwarn com.baidu.**
-dontwarn com.tencent.**
-dontwarn org.apache.commons.logging.**
-ignorewarnings
-keep class * {
public private *;
}

 #华为推送混淆
-ignorewarning
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.hianalytics.android.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}

-keep class com.huawei.android.hms.agent.**{*;}
-keep class com.huawei.gamebox.plugin.gameservice.**{*;}

  #Vivo推送混淆
-dontwarn com.vivo.push.**
-keep class com.vivo.push.**{*; }
-keep class com.vivo.vms.**{*; }
-keep class com.biaozhunyuan.tianyi.pushService.vivoPush.VivoPushMessageReceiverImpl{*;}