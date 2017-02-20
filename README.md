## open-web-demo


###20151019

配置简化

在spring配置xml文件中，在beans的attribute中添加：
```
xmlns:mvc="http://www.dianping.com/schema/mvc"
```
在attribute的xsi:schemaLocation中，添加：
```
http://www.dianping.com/schema/mvc http://www.dianping.com/schema/mvc/web-mvc-1.0.xsd
```
在beans里面，使用：
```
<mvc:config templatePath="/WEB-INF/pages"/> 
```
即可完成配置。
其中，templatePath为模板存放路径，默认即为/WEB-INF/pages

详情见open-web-demo的xml文件配置：
http://code.dianpingoa.com/open/open-web-demo/blob/master/src/main/resources/config/spring/local/applicationContext-mvc.xml
