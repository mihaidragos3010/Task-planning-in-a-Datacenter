/* Implement this class. */

import java.util.List;

public class MyDispatcher extends Dispatcher {

    volatile int idOfLastHostUsed;
    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
        idOfLastHostUsed = -1;
    }

//    Function is used to send toward host properly within the specific task algorithm. I used synchronized to
//  be sure that more generators threads are gonna to access this function simultaneously.
    @Override
    synchronized public void addTask(Task task) {

//            For Round Robin algorithm I used a variable the last host's index
            if (algorithm.equals(SchedulingAlgorithm.ROUND_ROBIN)) {
                int indexHost = (idOfLastHostUsed + 1) % hosts.size();
                Host host = hosts.get(indexHost);
                host.addTask(task);
                idOfLastHostUsed++;
            }

//            For Shortest Queue I select the host with the shortest queue
            if (algorithm.equals(SchedulingAlgorithm.SHORTEST_QUEUE)) {
                Host minHost = hosts.get(0);
                for(int i = 1; i < hosts.size(); i++){
                    Host checkedHost = hosts.get(i);
                    if(checkedHost.getQueueSize() < minHost.getQueueSize()){
                        minHost = checkedHost;
                    }
                }

                minHost.addTask(task);
            }

            if (algorithm.equals(SchedulingAlgorithm.SIZE_INTERVAL_TASK_ASSIGNMENT)) {

                if(task.getType() == TaskType.SHORT) hosts.get(0).addTask(task);
                if(task.getType() == TaskType.MEDIUM) hosts.get(1).addTask(task);
                if(task.getType() == TaskType.LONG) hosts.get(2).addTask(task);

            }

//            For Least Work Left I select the host with the shortest amount of work
            if (algorithm.equals(SchedulingAlgorithm.LEAST_WORK_LEFT)) {
                Host minHost = hosts.get(0);
                for(int i = 1; i < hosts.size(); i++){
                    Host checkedHost = hosts.get(i);
                    if(checkedHost.getWorkLeft() < minHost.getWorkLeft()){
                        minHost = checkedHost;
                    }
                }

                minHost.addTask(task);

            }
    }

}
