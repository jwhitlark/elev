# elev

A toolkit for simulating elevator control systems.

Note that this toolkit models the newer elevators that require you to
enter your destination in order to call an elevator, as getting the
information earlier in the process allows more interesting strategies.

The problem description says the scheduling algorithm is the most
interesting part of the problem, but I'm not sure I agree. With a well
studied problem like this, the first step is to look up the research,
(which I did). The next step is to build a toolkit so that you can
explore how various strategies might work with your particular
constraints. Anyway, that's my 2 cents.

## Finding your way around

### Prod
* src/elev/data.clj - The main assembly area
* src/elev/abstract.clj - Protocols (interfaces) for the library
* src/elev/data/query.clj - Stand alone functions that answer questions about one or more elevators.
* src/elev/data/impl.clj - Low level implementation details for data.clj
* src/elev/strategy.clj - Several switchable strategies for the simulator
* src/elev/core.clj - Entry point when running stand-alone

### Dev
* dev/user.clj - Development namespace for experimentation. Will be the default namespace for any REPL session
* test/* - No tests yet, too fluid, all REPL work so far.

## Future fun things to add

* Callbacks on pickup/deliver
* More strategies
* Cargo elevators
* Return elevators to ground floor when not in use
* Metrics

## Usage

### Running an elevator script
    java -jar elev-0.1.0-SNAPSHOT-standalone.jar sample1.clj

(will print out the visualization of the script)

### Working with elev via the REPL

    lein repl

You'll be dropped into the user namespace, which is setup for experimentation. Try the following:



    (-> (make-elevator-sim (->ElevatorCfg s/closest-stopped-or-closest-moving-strategy 5 4))
           (pickup 1 4)
           (tick-view)
           (tick-view)
           (pickup 4 0)
           (tick-view)
           (pickup 0 1)
           (tick-view)
           (pickup 2 4)
           (tick-view)
           (pickup 4 3)
           (pickup 2 4)
           (tick-view-until-finished))

Which will print out something that looks like:


    Elevator 0 has queued a pickup on 1 and a drop off on 4.
    tick
    +--------+---+---+---+---+
    | t:   1 | 0 | 1 | 2 | 3 |
    +--------+---+---+---+---+
    | 4      |   |   |   |   |
    | 3      |   |   |   |   |
    | 2      |   |   |   |   |
    | 1      | ▲ |   |   |   |
    | 0      |   | ■ | ■ | ■ |
    +--------+---+---+---+---+
    Remaining pickups: #{}
    Remaining dropoffs: #{4}
    tick
    +--------+---+---+---+---+
    | t:   2 | 0 | 1 | 2 | 3 |
    +--------+---+---+---+---+
    | 4      |   |   |   |   |
    | 3      |   |   |   |   |
    | 2      | ▲ |   |   |   |
    | 1      |   |   |   |   |
    | 0      |   | ■ | ■ | ■ |
    +--------+---+---+---+---+
    Remaining pickups: #{}
    Remaining dropoffs: #{4}
    Elevator 2 has queued a pickup on 4 and a drop off on 0.
    tick
    +--------+---+---+---+---+
    | t:   3 | 0 | 1 | 2 | 3 |
    +--------+---+---+---+---+
    | 4      |   |   |   |   |
    | 3      | ▲ |   |   |   |
    | 2      |   |   |   |   |
    | 1      |   |   | ▲ |   |
    | 0      |   | ■ |   | ■ |
    +--------+---+---+---+---+
    Remaining pickups: #{4}
    Remaining dropoffs: #{0 4}
    ...


## License

Copyright © 2015 Jason Whitlark

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
