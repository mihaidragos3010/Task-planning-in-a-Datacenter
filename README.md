# Task-planning-in-a-Datacenter

  The purpose of the homework was to understand parallel programming techniques in java. I used elementary notions such as: atomic, volatile, synchronized and synchronized data structures.

  In the realization of this theme, I had to create the classes myHost and myDispecer.

  Within the myDispecer class, I used the syncronized concept on the addTask function as a
synchronization method. This choice came after the observation of several generators that
could load and add tasks to the dispatcher at the same time and thus form a raise condition.
    Within the myHost class, we approached the following cases, which we synchronized as such:
adding more tasks to the queue, copying the classes to the own cache and the corresponding implementation
of the "shotdown" function of each host. As per the case that I treated was the one in which a new task
appears in the system, and at the same time a preemptible task is also added to the queue. In order to avoid
this unwanted event, I chose to use a PriorityBlockingQueue structure with its own comparison class to ensure
the necessary priorities. I have two variables nrTasks and nrWorkLeft that I chose to make volatile, because
I would have wanted to ensure equal visibility throughout the entire program when accessing them. I tried to avoid
the situation where thread-utile copies these values ​​into its own cache and modifies them there. As the
last synchronization method, I used timeToWork variables, which have the role of keeping my thread active in the
run() function until the main thread's decision to stop it through the "shutdown" function.
