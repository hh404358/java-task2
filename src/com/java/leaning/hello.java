package com.java.leaning;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.java.leaning.Animal.ANIMALKIND;


enum animals{
    ChineseDog,Cat,Rabbit
}

abstract class Animal{
    protected String name;
    protected int age;
    protected boolean sex;//true:female false:man
    protected double price;
    protected static final int ANIMALKIND=3;

    public Animal() {
        this.name = "Animal";
        this.age = 10;
        this.sex = true;
        this.price = 0;
    }

    public Animal(String name, int age, boolean sex, double price) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public abstract String toString();
}

class ChineseDog extends Animal{
    public boolean isVaccineInjected;

    public ChineseDog() {
        super();
        this.name="ChineseDog";
        this.price=100;
    }

    public ChineseDog(String name, int age, boolean sex, double price ) {
        super(name, age, sex, price);
        isVaccineInjected=true;
    }

    @Override
    public String toString() {
        return "ChineseDog{" +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", price=" + price +
                "isVaccineInjected=" + isVaccineInjected +
                '}';
    }

}
class Cat extends Animal{
    public Cat() {
        super();
        this.name="Cat";
        this.price=200;
    }

    public Cat(String name, int age, boolean sex, double price) {
        super(name, age, sex, price);
    }

    @Override
    public String toString() {
        return "Cat{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", price=" + price +
                '}';
    }
}

class Rabbit extends Animal{
    public Rabbit() {
        super();
        this.name="Rabbit";
        this.price=150;
    }

    public Rabbit(String name, int age, boolean sex, double price) {
        super(name, age, sex, price);
    }

    @Override
    public String toString() {
        return "Rabbit{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", price=" + price +
                '}';
    }
}

class Customer{
    String name;
    int time;//到店次数
    LocalDateTime t;//最新到店时间，我用LocalDateTime具体到顾客到店时间
    Animal like;//顾客喜欢的动物，以便根据这个进行购买

    public Customer() {
        this.name = "customer";
        this.time = 0;
        this.t = LocalDateTime.of(2023,8,1,9,28,10);
        this.like = new Cat();
    }

    public Customer(String name, int time, LocalDateTime t, Animal like) {
        this.name = name;
        this.time = time;
        this.t = t;
        this.like = like;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", time=" + time +
                ", t=" + t +
                ", like='" + like + '\'' +
                '}';
    }
}
interface AnimalShop{
    void Buy(Animal x,int num);
    void WelcomeCustomer(Customer c);
    void Close();
}
class MyAnimalShop implements  AnimalShop{
    double res;
    ArrayList<Animal>animals;
    ArrayList<Customer>customers;
    boolean isOpen;

    public MyAnimalShop(double res, ArrayList<Animal> animals, ArrayList<Customer> customers, boolean isOpen) {
        this.res = res;
        this.animals = animals;
        this.customers = customers;
        this.isOpen = isOpen;
    }

    public boolean isOpen(LocalDateTime time){
        isOpen=false;
        switch(time.getDayOfWeek()){
            case MONDAY : case TUESDAY:case WEDNESDAY:case THURSDAY:case FRIDAY:
                if(time.getHour()>8&&time.getHour()<22)
                    isOpen=true;
                break;
            default:
                isOpen=false;
        }
        return isOpen;
    }

    //介于不可能买只小动物的，设置此函数~~~
    public void Buy(Animal x,int num){
        if(res-num*x.price<0)throw new InsufficientBalanceException();
        res-=num*x.price;
        animals.add(x);

    }

    public void BuyList(){
        while(true){
            System.out.println("请输入需要买的动物：");
            Scanner scanner=new Scanner(System.in);
            String animal=scanner.nextLine();
            System.out.println("请输入要买的数量：");
            int num=scanner.nextInt();
            boolean flag=false;
            while(flag){
                switch(animal){
                    case "ChineseDog":
                        ChineseDog chineseDog=new ChineseDog();
                        try{
                            Buy(chineseDog,num);
                        }
                        catch(InsufficientBalanceException e){
                            e.toString();
                        }
                        break;
                    case "Cat":
                        Cat cat=new Cat();
                        try{
                            Buy(cat,num);
                        }
                        catch(InsufficientBalanceException e){
                            e.toString();
                        }
                        break;
                    case "Rabbit":
                        Rabbit rabbit=new Rabbit();
                        try{
                            Buy(rabbit,num);
                        }
                        catch(InsufficientBalanceException e){
                            e.toString();
                        }
                        break;
                    default:
                        System.out.println("输入错误，请重新输入：");
                        flag=true;
                        break;
                }
            }


            System.out.println("是否继续购买（0：no  1:yes)");
            int choice=scanner.nextInt();
            if(choice==0)break;

        }

    }
    @Override
    public void WelcomeCustomer(Customer c) {
        if(!isOpen) {
            throw new StoreNotOpen();
        }
        if(animals.isEmpty()){
            throw new AnimalNotFoundException();
        }
        customers.add(c);

        boolean flag=false;
        for (Iterator<Animal> it=animals.iterator();it.hasNext();) {
            Animal animal=it.next();
            if(animal==c.like){
                flag=true;
                it.remove();
                break;
            }
        }
        System.out.println(c.like.toString());
        res+=c.like.price;
//        if(!animals.contains(c.like)){
//            System.out.println("无此顾客喜欢的动物！");
//            return;
//        }


        //remove的多线程错误ConcurrentModificationException注意！！！
        // animals.remove(c.like);


    }
    public void Close(){
        double sum=0.0;
        for (Customer customer:customers) {
            if(isOpen(customer.t)){
                System.out.println(customer.toString());
                sum+=customer.like.price;
            }
        }
        System.out.println("今天盈利："+sum);


    }

    @Override
    public String toString() {
        return "MyAnimalShop\n{" +
                "\nres=" + res +
                ",\n animals=" + animals +
                ",\n customers=" + customers +
                ",\n isOpen=" + isOpen +
                "\n}";
    }
}

//自定义异常类
//这里采用baseException，其他的异常类继承自他就好了
class BaseException extends RuntimeException{
    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
class AnimalNotFoundException extends BaseException{
    public AnimalNotFoundException() {
    }

    public AnimalNotFoundException(String message) {
        super(message);
    }

    public AnimalNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnimalNotFoundException(Throwable cause) {
        super(cause);
    }

    public AnimalNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String toString() {
        return "当前店铺中没有动物可卖";
    }
}

class NoWantedAnimal extends BaseException{

    public NoWantedAnimal() {
    }

    public NoWantedAnimal(String message) {
        super(message);
    }

    public NoWantedAnimal(String message, Throwable cause) {
        super(message, cause);
    }

    public NoWantedAnimal(Throwable cause) {
        super(cause);
    }

    public NoWantedAnimal(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String toString() {
        return "无该顾客所想要的动物！";
    }
}


class InsufficientBalanceException extends BaseException{
    public InsufficientBalanceException() {
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientBalanceException(Throwable cause) {
        super(cause);
    }

    public InsufficientBalanceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String toString() {
        return "当前店铺中余额不足！";
    }
}

class StoreNotOpen extends BaseException{
    public StoreNotOpen() {
    }

    public StoreNotOpen(String message) {
        super(message);
    }

    public StoreNotOpen(String message, Throwable cause) {
        super(message, cause);
    }

    public StoreNotOpen(Throwable cause) {
        super(cause);
    }

    public StoreNotOpen(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String toString() {
        return "当前店铺没有营业！";
    }
}

class Test{
    public void test(){
        //创建一个我的宠物店
        ArrayList<Animal>animals=new ArrayList<>();
        ChineseDog chineseDog=new ChineseDog();
        animals.add(chineseDog);
        Cat cat=new Cat();
        animals.add(cat);
        Rabbit rabbit=new Rabbit();
        animals.add(rabbit);
        ArrayList<Customer>customers=new ArrayList<>();
        MyAnimalShop myAnimalShop=new MyAnimalShop(100,animals,customers,true);
        System.out.println(myAnimalShop.toString());
        //test BuyList
        //myAnimalShop.BuyList();

        //test WelcomeCustomer
        while(true){
            System.out.println("开始迎客：");
            System.out.println("请输入顾客姓名：");
            Scanner scanner=new Scanner(System.in);
            String name=scanner.nextLine();
            System.out.println("请输入顾客到店次数：");
            int times= scanner.nextInt();
            System.out.println("请输入顾客最新到店时间：");
            int year=scanner.nextInt();
            int month=scanner.nextInt();
            int day=scanner.nextInt();
            int hour=scanner.nextInt();
            int minute=scanner.nextInt();
            int second=scanner.nextInt();
            LocalDateTime t= LocalDateTime.of(year,month,day,hour,minute,second);
            System.out.println("请输入顾客喜欢的动物：");
            String like=scanner.next();
            boolean flag=true;
            Customer c=new Customer();
            while(flag)
                switch(like){
                    case "ChineseDog":
                        ChineseDog chineseDog1=new ChineseDog();
                        c=new Customer(name,times,t,chineseDog1);
                        flag=false;
                        break;
                    case "Cat":
                        Cat cat1=new Cat();
                        c=new Customer(name,times,t,cat1);

                        flag=false;
                        break;
                    case "Rabbit":
                        Rabbit rabbit1=new Rabbit();
                        c=new Customer(name,times,t,rabbit1);
                        flag=false;
                        break;
                    default:
                        System.out.println("输入错误，请重新输入：");
                        break;
                }
            try{
                myAnimalShop.WelcomeCustomer(c);
            }
            catch(AnimalNotFoundException e){
                e.toString();
            }
            catch (StoreNotOpen e){
                e.toString();
            }

            System.out.println("是否停止营业：（0：yes  1：no)");
            int choice=scanner.nextInt();
            if(choice==0)break;
        }
        //test close
        myAnimalShop.Close();

    }
}

//bonus task
class Bonus{
    //bonus1:多线程输出
    public void test1(){
        final Object obj=new Object();
        int arr1[]={1,3,5,7,9};
        int arr2[]={2,4,6,8,0};
        /*//    方法一wait&notify
        //注意这里的synchronized不能使用this，因为这是在静态方法中，这里除了可以使用自定义的监视器，还可以使用类的Class对象。
        //无法正确保证是先输出1还是2，读者可自行添加CountDownLatch完成按指定顺序输出的功能。
        new Thread(()->{
            synchronized (obj){
                for (int num:arr1) {
                    System.out.println(num+" ");
                    try{
                        obj.notify();
                        obj.wait();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                obj.notify();
            }
        }).start();
        new Thread(()->{
            synchronized (obj){
                for (int num:arr2) {
                    System.out.println(num+" ");
                    try{
                        obj.notify();
                        obj.wait();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                obj.notify();
            }
        }).start();*/

        //方法二 ReentrantLock
        Lock lock=new ReentrantLock();
        Condition condition1= lock.newCondition();
        Condition condition2= lock.newCondition();
        new Thread(()->{
            lock.lock();
            try{
                for (int i = 0; i < arr1.length; i++) {
                    System.out.println(arr1[i]+" ");
                    condition2.signal();
                    condition1.await();
                }
                condition1.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                lock.unlock();
            }
        }).start();
        new Thread(()->{
            lock.lock();
            try{
                for (int i = 0; i < arr2.length; i++) {
                    System.out.println(arr2[i]+" ");
                    condition1.signal();
                    condition2.await();
                }
                condition2.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                lock.unlock();
            }
        }).start();
    }

    //bonus2:正则表达式检验邮箱
    public static boolean CheckBox(String s){
        /*  权当注释
        String ret1="\\d{0,10}\\@qq.com";//qq邮箱
        String ret2="\\d{0,10}\\@tom.com";//TOM
        String ret3="\\d{0,10}\\@gmail.com";//谷歌邮箱
        String ret4="\\d{0,10}\\@hotmail.com";//微软
        String ret5="\\d{0,10}\\@icloud.com";//苹果邮箱*/

        /*  第二种想法没有充分运用正则表达式
        String box[]={"qq","tom","gamil","hotmail","icloud"};
        String ret="";
        for(int i=0;i<box.length;i++)
        ret="\\d{0,10}\\@"+box[i]+"\\.com";*/

        String ret="\\d{0,10}@(qq|tom|gmail|hotmail|icloud).com";
        return s.matches(ret);
    }

    public boolean test2(){
        return (CheckBox("1234567890@qq.com"));
    }

}


public class hello {
    public static void main(String[] args) throws InterruptedException {
        //主任务：开宠物店
        //Test test=new Test();
        //test.test();


        Bonus bonus=new Bonus();
        bonus.test1();



    }
}
