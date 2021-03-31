| 类 | 描述 |
| ---|---|
| TypeSpec |  用于生成类、接口、枚举对象的类 | 
| MethodSpec |  用于生成方法对象的类 | 
| ParameterSpec | 用于生成参数对象的类  | 
| AnnotationSpec | 用于生成注解对象的类  | 
| FieldSpec |  用于配置生成成员变量的类 | 
| ClassName |  通过包名和类名生成的对象，在JavaPoet中相当于为其指定Class | 
| ParameterizedTypeName |  通过MainClass和IncludeClass生成包含泛型的Class |
| JavaFile | 控制生成的Java文件的输出的类 | 

### 常用类
#### 修饰关键字
	addModifiers(Modifier... modifiers)
Modifier是一个枚举对象，枚举值为修饰关键字Public、Protected、Private、Static、Final等等。
所有在JavaPoet创建的对象都必须设置修饰符(包括方法、类、接口、枚举、参数、变量)。


#### 设置注解对象
	addAnnotation（AnnotationSpec annotationSpec）
	addAnnotation（ClassName annotation）
	addAnnotation(Class<?> annotation)
该方法即为类或方法或参数设置注解，参数即可以是AnnotationSpec，也可以是ClassName，还可以直接传递Class对象。
一般情况下，包含复杂属性的注解一般用AnnotationSpec，如果单纯添加基本注解，无其他附加属性可以直接使用ClassName或者Class即可

#### 设置注释
	addJavadoc（CodeBlock block）
	addJavadoc(String format, Object... args)
	
在编写类、方法、成员变量时，可以通过addJavadoc来设置注释，可以直接传入String对象，或者传入CodeBlock（代码块）

#### JavaPoet生成类、接口、枚举对象
在JavaPoet中生成类、接口、枚举，必须得通过TypeSpec生成，而classBuilder、interfaceBuilder、enumBuilder便是创建其关键的方法

	// 创建类：
	TypeSpec.classBuilder("类名“) 
	TypeSpec.classBuilder(ClassName className)
		
	// 创建接口：
	TypeSpec.interfaceBuilder("接口名称")
	TypeSpec.interfaceBuilder(ClassName className)
		
	// 创建枚举：
	TypeSpec.enumBuilder("枚举名称")
	TypeSpec.enumBuilder(ClassName className)


#### 继承、实现接口
	// 继承类：
	.superclass(ClassName className)
	
	// 实现接口
	.addSuperinterface(ClassName className)
	
继承存在泛型的父类

当继承父类存在泛型时，需要使用ParameterizedTypeName

	ParameterizedTypeName get(ClassName rawType, TypeName... typeArguments)
	
返回的ParameterizedTypeName对象，已经被添加泛型信息

#### 方法
	// 通过配置MethodSpec对象，使用addMethod方法将其添加进TypeSpec中
	addMethod(MethodSpec methodSpec)


#### 枚举
	// 通过addEnumConstan方法添加枚举值，参数为枚举值名称
	addEnumConstan(String enumValue)

#### JavaPoet生成成员变量
	builder(TypeName type, String name, Modifier... modifiers)
JavaPoet生成成员变量是通过FieldSpec的build方法生成。
只要传入TypeName(Class)、name（名称）、Modifier（修饰符），就可以生成一个基本的成员变量。

成员变量一般来说由注解（Annotation）、修饰符（Modifier）、Javadoc(注释)、initializer(实例化)

#### 注解
	addAnnotation(TypeName name) 
	
#### 修饰符
	addModifiers(Modifier ...modifier)
	
#### 注释
	addJavadoc(String format, Object... args)

#### 实例化
	initializer(String format, Object... args)
	
	  // public Activity mActivity = new Activity;
	  ClassName activity = ClassName.get("android.app", "Activity");
	  FieldSpec spec = FieldSpec.builder(activity, "mActivity")
	                .addModifiers(Modifier.PUBLIC)
	                .initializer("new $T", activity)
	                .build();	
  
### JavaPoet生成方法
avaPoet生成方法分为两种，第一种是构造方法，另一种为常规的方法

#### 构造方法
	MethodSpec.constructorBuilder()
	
#### 常规方法
		MethodSpec.methodBuilder(String name)
		
#### 方法参数
	addParameter(ParameterSpec parameterSpec)
	
#### 返回值
	returns(TypeName returnType)
设置方法的返回值，只需传入一个TypeName对象，而TypeName是ClassName，ParameterizedTypeName的基类

#### 方法体
在JavaPoet中，设置方法体内容有两个方法，分别是addCode和addStatement

	addCode()
	addStatement()
这两个本质上都是设置方法体内容，但是不同的是使用addStatement()方法时，你只需要专注于该段代码的内容，至于结尾的分号和换行它都会帮你做好。
而addCode（）添加的方法体内容就是一段无格式的代码片，需要开发者自己添加其格式

#### 方法体模板
在JavaPoet中，设置方法体使用模板是比较常见的，因为addCode和addStatement方法都存在这样的一个重载:

	addCode(String format, Object... args)
	addStatement(String format, Object... args)

### 特定占位符
#### $T
$T 在JavaPoet代指的是TypeName，该模板主要将Class抽象出来，用传入的TypeName指向的Class来代替

	// 等价于 ：Bundle bundle = new Bundle();
	ClassName bundle = ClassName.get("android.os", "Bundle");
	addStatement("$T bundle = new $T()",bundle)
	

#### $N
$N在JavaPoet中代指的是一个名称，例如调用的方法名称，变量名称，这一类存在意思的名称

	// 等价于 data.toString();
	addStatement("data.$N()",toString)
	
#### $S
$S在JavaPoet中就和String.format中%s一样,字符串的模板,将指定的字符串替换到$S的地方，需要注意的是替换后的内容，默认自带了双引号，如果不需要双引号包裹，需要使用$L

	// 将"name"字符串代替到$S的位置上
	.addStatement("return $S", “name”)
	
#### 抛出异常
	.addException(TypeName name)
设置方法抛出异常,可以使用addException方法,传入指定的异常的ClassName,即可为该方法设置其抛出该异常

### 生成方法参数
	 // JavaPoet生成有参方法时,需要填充参数,而生成参数则需要通过
	 // ParameterSpec这个类
	 addParameter(ParameterSpec parameterSpec)
	
#### 初始化ParameterSpec
	ParameterSpec.builder(TypeName type, String name, Modifier... modifiers)
	
给参数设置其Class,以及参数名称,和修饰符.
通常来说参数的构成包括:参数的类型(Class)、参数的名称（name）、修饰符（modifiers）、注解（Annotation）

### 生成注解
	不包含属性的注解可以直接通过
	.addAnnotation(TypeName name)
	
	// 如果使用的注解包含属性，并且不止一个时，这时候就需要生成AnnotationSpec
	// 初始化
	AnnotationSpec.builder(ClassName type)
	// 设置属性
	// name对应的就是属性名称，format的内容即属性体，同样方法体的格式化在这里也是适用
	addMember(String name, String format, Object... args)
	
### 生成代码
	// 负责生成的类是JavaFile
	JavaFile.builder(String packageName, TypeSpec typeSpec)
	// 生成的内容打印到控制台
	 javaFile.writeTo(System.out)
	 // 生成java文件
	 javaFile.writeTo（File file）
	
	



