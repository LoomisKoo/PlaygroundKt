### 概念
SPI全称是Service Provider Interface，服务提供方接口，服务通常是指一个接口或者一个抽象类，服务提供方是对这个接口或者抽象类的具体实现，由第三方来实现接口提供具体的服务。SPI提供了一种动态的对应用程序进行扩展的机制，通常用作框架服务的拓展或者可替换的服务组件，但是在Android中并没有广泛的使用。一般用于组件化

 `注意：服务具体的实现类必须有一个不带参数的构造方法`
	
### Android中使用SPI
1.创建spi接口
	
	public interface IAnimal {
		void run();
	}
	
2.接口实现

	public class Dog implements IAnimal {
	   @Override
	    public void run() {
	        Log.i("test", this.getClass().getName() + " == Dog run !");
	    }
	 }
	 
3.创建目录

	 main/resources/META-INF/services
	 
4.在上一步骤创建的文件夹下创建以接口命名（包括路径）的文件

	// 文件名
	com.example.spidemo.IAnimal
	// 文件内容（一行代表一个实现的接口），这里表示有两个接口实现
	com.example.spidemo.AnimalImplA
	com.example.spidemo.AnimalImplB
	
5.使用ServiceLoader动态查找接口的实现并使用

	    //通过ServiceLoader来动态加载Service,其中load有几个方法重载，可以尝试
        ServiceLoader<IAnimal> serviceLoader = ServiceLoader.load(IAnimal.class, ImageLoader.class.getClassLoader());

        Iterator<ImageLoader> it = serviceLoader.iterator();
        if (it.hasNext()) {
            it.next().displayImage();
        }
	
### 原理
应用程序调用ServiceLoader.load方法ServiceLoader.load方法内先创建一个新的ServiceLoader，并实例化该类中的成员变量，包括：

  - loader(ClassLoader类型，类加载器)
  - acc(AccessControlContext类型，访问控制器)
  - providers(LinkedHashMap<String,S>类型，用于缓存加载成功的类)
  - lookupIterator(实现迭代器功能)
  
应用程序通过迭代器接口获取对象实例。ServiceLoader先判断成员变量providers对象中(LinkedHashMap<String,S>类型)是否有缓存实例对象，如果有缓存，直接返回。如果没有缓存，执行类的装载，实现如下

  - 读取META-INF/services/下的配置文件，获得所有能被实例化的类的名称，值得注意的是，ServiceLoader可以跨越jar包获取META-INF下的配置文件
  - 通过反射方法Class.forName()加载类对象，并用instance()方法将类实例化
  - 把实例化后的类缓存到providers对象中，(LinkedHashMap<String,S>类型）
然后返回实例对象

### 优缺点
#### 优点

- 使用Java SPI机制的优势是实现解耦，使得第三方服务模块的装配控制的逻辑与调用者的业务代码分离，而不是耦合在一起。应用程序可以根据实际业务情况启用框架扩展或替换框架组件。
相比使用提供接口jar包，供第三方服务模块实现接口的方式，SPI的方式使得源框架，不必关心接口的实现类的路径

- 通过SPI的方式，第三方服务模块实现接口后，在第三方的项目代码的META-INF/services目录下的配置文件指定实现类的全路径名，源码框架即可找到实现类

#### 缺点

- 虽然ServiceLoader也算是使用的延迟加载，但是基本只能通过遍历全部获取，也就是接口的实现类全部加载并实例化一遍。如果你并不想用某些实现类，它也被加载并实例化了，这就造成了浪费。获取某个实现类的方式不够灵活，只能通过Iterator形式获取，不能根据某个参数来获取对应的实现类

- 多个并发多线程使用ServiceLoader类的实例是不安全的

- 加载不到实现类时抛出并不是真正原因的异常，错误很难定位