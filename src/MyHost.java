/* Implement this class. */

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

public class MyHost extends Host {
//    All variables below are volatile because I need to be modified by generators and visible by each one of them
//    Variable is used to keep run() until main thread shutdown the execution of this thread
    volatile boolean timeToWork;
//    Variable counts the number of tasks in queue and running
    volatile int nrTasks;
//    Variable counts the amount of work left
    volatile int nrWorkLeft;
//    Data structure send toward execution tasks into specific order
    PriorityBlockingQueue<Task> queue;
    MyHost(){
        queue = new PriorityBlockingQueue<Task>(100,new ComtaratorTasks());
        timeToWork = true;
        nrTasks = 0;
        nrWorkLeft = 0;
    }
    @Override
    public void run() {

        while(timeToWork) {
            while(!queue.isEmpty()) {
//                I'm sure the peek task will be the right one because I initialized queue with ComparatorTasks class
                Task task = queue.poll();

                if(task.isPreemptible()) processTaskPreemptible(task);
                if(!task.isPreemptible()) processNotTaskPreemptible(task);

            }
        }

    }

//    Function add a new task in queue and increase each variable used for counting
    @Override
    public void addTask(Task task) {
        queue.add(task);
        nrTasks++;
        nrWorkLeft += task.getDuration();
    }
//    Function returns the number of thread in queue and running
    @Override
    public int getQueueSize() { return nrTasks;}
//    Function returns the amount of work left
    @Override
    public long getWorkLeft() { return nrWorkLeft;}
//    Function make variable "timeToWork" false to make possible the release form first while in run() function.
    @Override
    public void shutdown() { timeToWork = false;}

//    Function process tasks that are preemptible. Firstly task while sleep for a second.
    void processTaskPreemptible(Task task){

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        task.setLeft(task.getLeft() - 1000L);
        nrWorkLeft -= 1000L;

//        If task doesn't have work left it will finish and decrease the number tasks variable. If task has
//      work left will be added again in queue
        if(task.getLeft() == 0){
            task.finish();
            nrTasks--;
        }else{
            queue.add(task);
        }

    }

//    Function process tasks that aren't preemptible. I used while to keep the task un running time and after whole
//  execution to continue with the next task
    void processNotTaskPreemptible(Task task){

        while(task.getLeft() > 0) {
            try {
                Thread.sleep(1000L);
                task.setLeft(task.getLeft() - 1000L);
                nrWorkLeft -= 1000L;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

        task.finish();
        nrTasks--;

    }

//    Class is used to order the task queue
    class ComtaratorTasks implements Comparator<Task>{

        public int compare(Task task1, Task task2){

            if (task1.getPriority() < task2.getPriority()) return 1;
            if (task1.getPriority() > task2.getPriority()) return -1;

            if (task1.getStart() > task2.getStart()) return 1;
            if (task1.getStart() < task2.getStart()) return -1;

            return 0;
        }

    }
}