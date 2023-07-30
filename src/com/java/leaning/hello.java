package com.java.leaning;

import java.sql.SQLOutput;

class Booth {
    private long id;
    private String name;
    private int tota;
    private boolean isClosed;

    public Booth() {

    }
    public Booth(long id,String name,int tota,boolean isClosed){
        this.id=id;
        this.name=name;
        this.tota=tota;
        this.isClosed=isClosed;
    }

    public long getId() {return id; }

    public  void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public  void setName(String name) {
        this.name = name;
    }

    public int getTota() {
        return tota;
    }

    public  void setTota(int tota) {
        this.tota = tota;
    }

    public boolean isIsClosed() {
        return isClosed;
    }

    public void setIsClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    @Override
    public  String toString(){
        String s=" id:"+getId()+"\n name:"+getName()+"\n how many xigua is sole:"+getTota()+"\n is close:"+isIsClosed();
        return s;
    }
    public static void purchase(Booth t,int num){
        if(num<0||t.isIsClosed()||t.getTota()<num){
            System.out.println("can not buy!!!");
            return;
        }
        t.setTota(t.getTota()-num);
        System.out.println("buy success!");
        return;

    }
    public void restock(int in){
        if(in>200){
            System.out.println("too much!");
            return;
        }
        tota+=in;
        System.out.println("restock success!");
    }

    public static void closeBooths(Booth[] booths){
        for(int i=0;i< booths.length;i++){
            if(!booths[i].isIsClosed())
                booths[i].setIsClosed(true);
            else {
                String s=booths[i].toString();
                System.out.println(s);
            }
        }
    }

}

public class hello {
    //test
    public static void main(String[] args) {

        Booth[] booths=new Booth[6];
        //第一种实例化
//        Booth b1=new Booth(1,"haha1",101,true);
//        Booth b2=new Booth(2,"haha2",301,true);
//        Booth b3=new Booth(3,"haha3",156,false);
//        Booth b4=new Booth(4,"haha4",121,true);
//        Booth b5=new Booth(5,"haha5",391,false);
//        Booth b6=new Booth(6,"haha6",231,true);
//        booths[0]=b1;
//        booths[1]=b2;
//        booths[2]=b3;
//        booths[3]=b4;
//        booths[4]=b5;
//        booths[5]=b6;
        //第二种：偷懒式
        for(int i=0;i<6;i++)
            booths[i]=new Booth(i+1,"haha"+i,100*i,true);


        //toString
        String s=booths[2].toString();
        System.out.println(s);
        //restock
        booths[2].restock(10);
        s=booths[2].toString();
        System.out.println(s);
        //purchase
        Booth.purchase(booths[5],9);
        s=booths[5].toString();
        System.out.println(s);

        //closeBooths
        Booth.closeBooths(booths);



    }
}
