# PollFor

#### Java API for Synchronous and Asynchronous Polling

>**Polling** is the process where the computer or controlling device waits for an external device to check for its readiness or state, often with low-level hardware. For example, when a printer is connected via a parallel port, the computer waits until the printer has received the next character.
_Source [Wikipedia](https://en.wikipedia.org/wiki/Polling_(computer_science))_ 



## Getting Started

### Features

#### Polling
The basic polling includes polling for a single polling-job. The `Poll` API returns a single `PollDefinition` instance that is used for executing and initiating the poll. 

#### Asynchronous Polling
Asynchronous polling includes executing more than one polling-job in parallel. The `Poll` API returns a list or collection of the `PollDefinition` instances that are executed in parallel.

#### Polling Entry Criteria
The entry criteria is a condition that is evaluated to determine whether to initiate the polling-job or not. It functions like setting an initial delay to the polling-job (it's an alternative to initial-delay), however, rather than a fixed time wait, this criteria is considered a pre-polling-job which is iteratively executed at polling-intervals, similar to the primary-polling-job until the entry-criteria is met. 

_PS: The entry criteria also uses the same time-out value to avoid endless polling.
Also, the captured result of the pre-polling-job is not returned to the consumer_

```
holdUntil(Predicate<? super T> entryCriteria, Supplier<? super T> action)
```
#### Polling Exit Criteria
The exit-criteria is the condition that is evaluated by the primary polling-job (executed iteratively at polling-intervals) to determine when to exit the iterative process. 
If the exit-criteria is not met before the polling time-out is reached, the polling-job is aborted and is said to be **timed-out**.

```
until(Predicate<? super T> exitCriteria, Supplier<? super T> action)
```

#### Polling Time-Out
The maximum time until which the polling should continue unless an **exit-criteria** is not. The result returned has the `isTimedOut` flag as `true` in such a case.

_PS: This time-out won't be reached if the exit-criteria is met earlier_
```
pollFor(Integer timeOut, TimeUnit timeOutUnit)
```

#### Polling Iterations
Polling iterations is an alternative to Polling time-out, where instead of waiting for a maximum time-out, a specific number of iterations would be executed unless an **exit-criteria** is met.
```
pollFor(Integer pollIterations)
```

#### Polling Interval
The time-interval at which the job should be executed/queried to determine if it meets the **exit-criteria** or not. 

```
every(Integer timeInterval, TimeUnit timeIntervalUnit)
```

#### Polling Initial Delay
Polling initial delay is an initial wait before the 1st polling-job is executed/queried. This is an alternative to **entry-criteria**. 
```
holdFor(Integer intialDelay, TimeUnit intialDelayUnit)
```



## Usage

####Synchronous Polling

Poll using a 
