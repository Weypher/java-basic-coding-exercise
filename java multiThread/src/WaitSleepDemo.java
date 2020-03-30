public class WaitSleepDemo {
    public static void main(String[] args) {
        final Object lock = new Object();// 创建一个锁
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread A is waiting to get lock"); // 1. thread A首先运行
                synchronized (lock) {
                    try {
                        System.out.println("thread A get lock and start to sleep 20 millis");// 2. thread A首先得到锁
                        Thread.sleep(20); //开始sleep 20ms
                        System.out.println("thread A do wait method"); // 4.睡眠完成，开始进行wait指令，释放了锁。
                        lock.wait(1000);// wait 1s
                        System.out.println("thread A is done"); // 7. 回到thread A 完成此处。
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        try { //这一串保证了thread A 必须先于thread B 执行。
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread B is waiting to get lock"); // 3. 在thread A开始睡眠20ms的时候，经过10ms，Thread B开始启动。
                synchronized (lock) { // 试图得到锁，但是第一次尝试时这个锁在Thread A手上。 //在4.之后lock被释放了，得到了lock。
                    try {
                        System.out.println("thread B get lock and start to sleep 10 millis");// 5. 得到了锁并且开始sleep 10ms
                        Thread.sleep(10);
                        System.out.println("thread B is done");// 6. sleep完成 synchronized的代码块完成，释放锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
