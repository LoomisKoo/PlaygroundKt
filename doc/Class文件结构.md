### demo
- 源代码

```
// Test.java
public class Test {
    private int num1 = 1;
    protected int num2 = 2;
    String str1 = "1";
    protected String str2 = "字符串2";

    public static void main(String args[]) {
        System.out.println("Hello World!");
        Test test = new Test();
        test.numPlus(10);
        test.stringPlus("str10");
    }

    int numPlus(int testArg) {
        return num1 + num2;
    }

    public void stringPlus(String testArg) {
        System.out.println("koo----- " + str1 + " + " + str2);
    }

    interface TestInterface {
    }
}

```


- 通过 javap -verbose 解析的Teat.class文件

```
  Last modified 2021年4月15日; size 1359 bytes
  SHA-256 checksum 70db6174513f6566e415741afcc53921740b77d0f635851136e51ce6a97d3c8a
  Compiled from "Test.java"
public class Test
  minor version: 0
  major version: 59
  flags: (0x0021) ACC_PUBLIC, ACC_SUPER
  this_class: #8                          // Test
  super_class: #2                         // java/lang/Object
  interfaces: 0, fields: 4, methods: 4, attributes: 4
Constant pool:
   #1 = Methodref          #2.#3          // java/lang/Object."<init>":()V
   #2 = Class              #4             // java/lang/Object
   #3 = NameAndType        #5:#6          // "<init>":()V
   #4 = Utf8               java/lang/Object
   #5 = Utf8               <init>
   #6 = Utf8               ()V
   #7 = Fieldref           #8.#9          // Test.num1:I
   #8 = Class              #10            // Test
   #9 = NameAndType        #11:#12        // num1:I
  #10 = Utf8               Test
  #11 = Utf8               num1
  #12 = Utf8               I
  #13 = Fieldref           #8.#14         // Test.num2:I
  #14 = NameAndType        #15:#12        // num2:I
  #15 = Utf8               num2
  #16 = String             #17            // 1
  #17 = Utf8               1
  #18 = Fieldref           #8.#19         // Test.str1:Ljava/lang/String;
  #19 = NameAndType        #20:#21        // str1:Ljava/lang/String;
  #20 = Utf8               str1
  #21 = Utf8               Ljava/lang/String;
  #22 = String             #23            // 字符串2
  #23 = Utf8               字符串2
  #24 = Fieldref           #8.#25         // Test.str2:Ljava/lang/String;
  #25 = NameAndType        #26:#21        // str2:Ljava/lang/String;
  #26 = Utf8               str2
  #27 = Fieldref           #28.#29        // java/lang/System.out:Ljava/io/PrintStream;
  #28 = Class              #30            // java/lang/System
  #29 = NameAndType        #31:#32        // out:Ljava/io/PrintStream;
  #30 = Utf8               java/lang/System
  #31 = Utf8               out
  #32 = Utf8               Ljava/io/PrintStream;
  #33 = String             #34            // Hello World!
  #34 = Utf8               Hello World!
  #35 = Methodref          #36.#37        // java/io/PrintStream.println:(Ljava/lang/String;)V
  #36 = Class              #38            // java/io/PrintStream
  #37 = NameAndType        #39:#40        // println:(Ljava/lang/String;)V
  #38 = Utf8               java/io/PrintStream
  #39 = Utf8               println
  #40 = Utf8               (Ljava/lang/String;)V
  #41 = Methodref          #8.#3          // Test."<init>":()V
  #42 = Methodref          #8.#43         // Test.numPlus:(I)I
  #43 = NameAndType        #44:#45        // numPlus:(I)I
  #44 = Utf8               numPlus
  #45 = Utf8               (I)I
  #46 = String             #47            // str10
  #47 = Utf8               str10
  #48 = Methodref          #8.#49         // Test.stringPlus:(Ljava/lang/String;)V
  #49 = NameAndType        #50:#40        // stringPlus:(Ljava/lang/String;)V
  #50 = Utf8               stringPlus
  #51 = InvokeDynamic      #0:#52         // #0:makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
  #52 = NameAndType        #53:#54        // makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
  #53 = Utf8               makeConcatWithConstants
  #54 = Utf8               (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
  #55 = Utf8               Code
  #56 = Utf8               LineNumberTable
  #57 = Utf8               main
  #58 = Utf8               ([Ljava/lang/String;)V
  #59 = Utf8               SourceFile
  #60 = Utf8               Test.java
  #61 = Utf8               NestMembers
  #62 = Class              #63            // Test$TestInterface
  #63 = Utf8               Test$TestInterface
  #64 = Utf8               BootstrapMethods
  #65 = MethodHandle       6:#66          // REF_invokeStatic java/lang/invoke/StringConcatFactory.makeConcatWithConstants:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;
  #66 = Methodref          #67.#68        // java/lang/invoke/StringConcatFactory.makeConcatWithConstants:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;
  #67 = Class              #69            // java/lang/invoke/StringConcatFactory
  #68 = NameAndType        #53:#70        // makeConcatWithConstants:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;
  #69 = Utf8               java/lang/invoke/StringConcatFactory
  #70 = Utf8               (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;
  #71 = String             #72            // koo----- \u0001 + \u0001
  #72 = Utf8               koo----- \u0001 + \u0001
  #73 = Utf8               InnerClasses
  #74 = Utf8               TestInterface
  #75 = Class              #76            // java/lang/invoke/MethodHandles$Lookup
  #76 = Utf8               java/lang/invoke/MethodHandles$Lookup
  #77 = Class              #78            // java/lang/invoke/MethodHandles
  #78 = Utf8               java/lang/invoke/MethodHandles
  #79 = Utf8               Lookup
{
  protected int num2;
    descriptor: I
    flags: (0x0004) ACC_PROTECTED

  java.lang.String str1;
    descriptor: Ljava/lang/String;
    flags: (0x0000)

  protected java.lang.String str2;
    descriptor: Ljava/lang/String;
    flags: (0x0004) ACC_PROTECTED

  public Test();
    descriptor: ()V
    flags: (0x0001) ACC_PUBLIC
    Code:
      stack=2, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: aload_0
         5: iconst_1
         6: putfield      #7                  // Field num1:I
         9: aload_0
        10: iconst_2
        11: putfield      #13                 // Field num2:I
        14: aload_0
        15: ldc           #16                 // String 1
        17: putfield      #18                 // Field str1:Ljava/lang/String;
        20: aload_0
        21: ldc           #22                 // String 字符串2
        23: putfield      #24                 // Field str2:Ljava/lang/String;
        26: return
      LineNumberTable:
        line 2: 0
        line 3: 4
        line 4: 9
        line 5: 14
        line 6: 20

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: (0x0009) ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=2, args_size=1
         0: getstatic     #27                 // Field java/lang/System.out:Ljava/io/PrintStream;
         3: ldc           #33                 // String Hello World!
         5: invokevirtual #35                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
         8: new           #8                  // class Test
        11: dup
        12: invokespecial #41                 // Method "<init>":()V
        15: astore_1
        16: aload_1
        17: bipush        10
        19: invokevirtual #42                 // Method numPlus:(I)I
        22: pop
        23: aload_1
        24: ldc           #46                 // String str10
        26: invokevirtual #48                 // Method stringPlus:(Ljava/lang/String;)V
        29: return
      LineNumberTable:
        line 9: 0
        line 10: 8
        line 11: 16
        line 12: 23
        line 13: 29

  int numPlus(int);
    descriptor: (I)I
    flags: (0x0000)
    Code:
      stack=2, locals=2, args_size=2
         0: aload_0
         1: getfield      #7                  // Field num1:I
         4: aload_0
         5: getfield      #13                 // Field num2:I
         8: iadd
         9: ireturn
      LineNumberTable:
        line 16: 0

  public void stringPlus(java.lang.String);
    descriptor: (Ljava/lang/String;)V
    flags: (0x0001) ACC_PUBLIC
    Code:
      stack=3, locals=2, args_size=2
         0: getstatic     #27                 // Field java/lang/System.out:Ljava/io/PrintStream;
         3: aload_0
         4: getfield      #18                 // Field str1:Ljava/lang/String;
         7: aload_0
         8: getfield      #24                 // Field str2:Ljava/lang/String;
        11: invokedynamic #51,  0             // InvokeDynamic #0:makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        16: invokevirtual #35                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
        19: return
      LineNumberTable:
        line 20: 0
        line 21: 19
}
SourceFile: "Test.java"
NestMembers:
  Test$TestInterface
BootstrapMethods:
  0: #65 REF_invokeStatic java/lang/invoke/StringConcatFactory.makeConcatWithConstants:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;
    Method arguments:
      #71 koo----- \u0001 + \u0001
InnerClasses:
  static #74= #62 of #8;                  // TestInterface=class Test$TestInterface of class Test
  public static final #79= #75 of #77;    // Lookup=class java/lang/invoke/MethodHandles$Lookup of class java/lang/invoke/MethodHandles
```

 - 通过javap -verbose 解析的TestInterface.class文件
 
```
 Classfile /Users/loomiskoo/Desktop/Test$TestInterface.class
  Last modified 2021年4月15日; size 179 bytes
  SHA-256 checksum 4557fce2867f12014e2cee419fd3947cb25aa114e2b383f30e2bac1dd0122507
  Compiled from "Test.java"
interface Test$TestInterface
  minor version: 0
  major version: 59
  flags: (0x0600) ACC_INTERFACE, ACC_ABSTRACT
  this_class: #1                          // Test$TestInterface
  super_class: #3                         // java/lang/Object
  interfaces: 0, fields: 0, methods: 0, attributes: 3
Constant pool:
   #1 = Class              #2             // Test$TestInterface
   #2 = Utf8               Test$TestInterface
   #3 = Class              #4             // java/lang/Object
   #4 = Utf8               java/lang/Object
   #5 = Utf8               SourceFile
   #6 = Utf8               Test.java
   #7 = Utf8               NestHost
   #8 = Class              #9             // Test
   #9 = Utf8               Test
  #10 = Utf8               InnerClasses
  #11 = Utf8               TestInterface
{
}
SourceFile: "Test.java"
NestHost: class Test
InnerClasses:
  static #11= #1 of #8;                   // TestInterface=class Test$TestInterface of class Test
```
 
 
[参考链接](https://www.jb51.net/article/35187.htm)