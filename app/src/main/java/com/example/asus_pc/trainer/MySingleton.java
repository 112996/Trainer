package com.example.asus_pc.trainer;

/**
 * 练习单例模式
 */



//饿汉模式
public class MySingleton {
    //饿汉模式(在类加载时就完成初始化，类加载较慢，获取对象的速度快)
    private static MySingleton instance = new MySingleton(); //初始化

    private MySingleton(){
        //私有的构造函数
    }
    public static MySingleton getInstance(){  //静态，不需要同步
        return instance;
    }
}


//懒汉模式
class  LazySingleton{
    //在类加载时不创建实例，类加载速度快，运行时获取对象的速度较慢

    private static LazySingleton instance = null; //未初始化

    private LazySingleton(){
        //构造函数
    }

    public static synchronized LazySingleton getInstance(){  //静态，需要同步锁。
        // 当线程A需要调用的时候进行判断，若instance为空，则new LzaySingleton,并返回instance，并且A执有这个锁，那么B需要等待A执行完毕。
        // 当线程B执行同样操作的时候由于instance不为空了，就不需要再new
        if (instance == null){
            instance = new LazySingleton();  //再进行初始化
        }
        return instance;
    }
}

