package com.kob.botrunningsystem.service.impl.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chenjikun
 * @version 1.0.0
 * @description TODO
 * @date 2023/3/24 20:05
 */
public class BotPool extends Thread{

    //互斥锁
    private final ReentrantLock lock = new ReentrantLock();
    //同步锁 用来锁住和唤醒消息队列 生产者消费者模型
    private final Condition condition = lock.newCondition();
    //bot 消息队列
    private final Queue<Bot> bots = new LinkedList<>();

    public void addBot(Integer userId, String botCode , String input){
        lock.lock();
        try{
            bots.add(new Bot(userId,botCode,input));
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }
    //消耗bot 执行逻辑
    private void consume(Bot bot){
        //这里使用joor 只能识别java代码 如果想编译其他语言的代码 可以使用Docker容器
        //开启一个joor线程 处理bot传过来的代码 返回移动结果
        Consumer consumer = new Consumer();
        consumer.startTimeOut(2000,bot); //总共等待五秒 若都使用bot则每个bot最多等待2s
    }

    @Override
    public void run() {
        while(true){
            lock.lock();
            if(bots.isEmpty()){
                try {
                    condition.await(); //阻塞线程 直到被唤醒才继续执行 自动释放锁
                } catch (InterruptedException e) {
                    lock.unlock();
                    e.printStackTrace();
                    break;
                }
            }
            else {
                Bot bot = bots.remove();
                lock.unlock(); //先解锁
                consume(bot);
            }
        }
    }
}
