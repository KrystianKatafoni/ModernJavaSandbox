package org.example;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1, warmups = 1)
    public void collector() {
        //DoubleStream.iterate(0.0, n -> n < 100000000.0, n -> n + 1.0).mapToObj(Person::new).collect(Collectors.summingDouble(Person::getDebt));
        DoubleStream.iterate(0.0, n -> n < 1000000000.0, n -> n + 1.0).mapToObj(Person::new).collect(Collectors.counting());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1, warmups = 1)
    public void reduce() {
        //DoubleStream.iterate(0.0, n -> n < 100000000.0, n -> n + 1.0).mapToObj(Person::new).reduce(0.0, (sum, person) -> sum + person.getDebt(), (sum, otherSum) -> sum + otherSum);
        DoubleStream.iterate(0.0, n -> n < 1000000000.0, n -> n + 1.0).mapToObj(Person::new).count();
    }
}
