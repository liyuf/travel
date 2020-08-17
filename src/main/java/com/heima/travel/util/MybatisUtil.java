package com.heima.travel.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class MybatisUtil {
    //获取mybatis生产对象
    private static SqlSessionFactory sqlSessionFactory=null;

    static{
        try {
            //加载配置文件方式一
            String resource="mybatisConfig.xml";
            InputStream mybatisConfig = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(mybatisConfig);
//            加载配置文件方式二
//            String resource="mybatisConfig.xml";
//            InputStream resourceAsStream = MybatisUtil.class.getClassLoader().getResourceAsStream(resource);
//            sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SqlSession openSession(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession;
    }
    public static void close(SqlSession sqlSession){
        //提交事务
        sqlSession.commit();
        sqlSession.close();
    }

    //获取dao的代理对象
    public static <T> T getProxyObject(Class<T> interfaceClass){
        /*
        * 1、类加载器
        * 2、接口数组类型
        * 3、invokeHandler
        * */
        Class<?>[] interfaces={interfaceClass};
        //获取代理类型
        T proxyInstance= (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), interfaces, new InvocationHandler() {
            /*
            * 1、代理对象
            * 2、被代理对象的方法对象
            * 3、被代理对象的方法参数
            * */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //获取被代理对象
                //开启mybatis连接
                SqlSession sqlSession = sqlSessionFactory.openSession();
                T mapper = sqlSession.getMapper(interfaceClass);
                //对代理对象的方法进行增强
                Object result = method.invoke(mapper, args);
                //关闭连接
                close(sqlSession);
                return result;
            }
        });
                return proxyInstance;
    }
}
