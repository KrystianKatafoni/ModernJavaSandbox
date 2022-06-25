package org.example;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


class AppTest {

    @Test
    void slicingElements() {
        //takeWhile
        List<Person> personList = getPersonList();
        List<Person> result = personList.stream().sorted(Comparator.comparing(Person::getDebt)).takeWhile(person -> person.getDebt() < 3000).collect(Collectors.toList());
        assertEquals(4, result.size());

        //dropWhile
        List<Person> result2 = personList.stream().sorted(Comparator.comparing(Person::getDebt)).dropWhile(person -> person.getDebt() < 3600).collect(Collectors.toList());
        assertEquals(2, result2.size());

        //skip
        List<Person> result3 = personList.stream().skip(1).collect(Collectors.toList());
        assertEquals(7, result3.size());
    }

    @Test
    void flatMapping() {

        //flatmap
        List<Person> personList = getPersonList();

        //Wyswietl wszystkie nazwy kupionych produktow
        List<String> productNames = personList.stream()
                .map(Person::getPurchases)
                .flatMap(Collection::stream)
                .map(Purchase::getProductName)
                .distinct().collect(Collectors.toList());
        System.out.println(productNames);
    }

    @Test
    void findingAndMatching() {
        List<Person> personList = getPersonList();

        Person person = personList.stream().findAny().orElseThrow(() -> new IllegalArgumentException());
        System.out.println(person);
    }


    @Test
    void reducing() {
        List<Person> personList = getPersonList();

        // tylko akumulator
        Double sum = personList.stream().map(Person::getDebt).reduce(Double::sum).get();
        //uzycie inicjalizacji + akumulator
        double sum2 = personList.stream().mapToDouble(Person::getDebt).reduce(0.0, (a,b) -> a+b);
        //uzycie inicjalizacji + akumulator + combiner
        Double sum3 = personList.stream().reduce(0.0, (result, p2) -> (result + p2.getDebt()), (result, otherResult) -> result + otherResult);

        assertEquals(sum, sum2);
        assertEquals(sum, sum3);

    }

    @Test
    void numericStreams() {
        List<Person> personList = getPersonList();
        //mapToInt zamienia stream na numeric stream IntStream
        int sum = personList.stream()
                .mapToInt(Person::getNumberOfChilds)
                .sum();
        //max() zwraca OptionalInt
        int max = personList.stream()
                .mapToInt(Person::getNumberOfChilds)
                .max().getAsInt();

        //generowanie zakresu liczbowego
        IntStream intStream = IntStream.rangeClosed(1, 100)
                .filter(n -> n % 3 == 0);

    }

    @Test
    void buildingStreams() {
        //Strumień z wartości
        Stream<String> stream = Stream.of("AA", "BB", "CC");

        //Stream z wartości, która może być nullem
        String variable = "AA";
        Stream<String> stream2 = variable == null ? Stream.empty() : Stream.of(variable);

        //lub od Javy 9
        String variable2 = null;
        Stream<String> stream3 = Stream.ofNullable(variable2);
        stream3.forEach(System.out::println);

        //Strumienie z tablic
        int[] numbers = {2,3,5};
        int sum = Arrays.stream(numbers).sum();

        //wyszukanie w linijkach pliku unikalnych słów
        List<String> phrases = List.of("Ala ma kota", "i kot ma Alę", "Alę jest kot");
        long count = phrases.stream().flatMap(phrase -> Arrays.stream(phrase.split(" "))).distinct().count();
        assertEquals(7, count);

        //Generowanie strumenia z iteracji - java 9, generowanie liczb co 2 do momentu gdy są mniejsze od 30
        Stream.iterate(0, n -> n < 30,n -> n+2).forEach(System.out::println);

        //Generowanie strumenia z iteracji - z limitem elementów - 7, java 8
        Stream.iterate(1, n-> n*3).limit(7).forEach(System.out::println);

        //Generate
        long count2 = Stream.generate(() -> 2).limit(10).mapToInt(Integer::intValue).sum();
        assertEquals(20, count2);
    }

    @Test
    void collectingDataWithStreams() {
        List<Person> personList = getPersonList();
        long count = personList.stream().collect(Collectors.counting());
        System.out.println(count);

        Double avgNumOfChild = personList.stream().collect(Collectors.averagingInt(Person::getNumberOfChilds));
        System.out.println(avgNumOfChild);

        double debtSum = personList.stream().collect(Collectors.summingDouble(p -> p.getDebt()));
        personList.stream().map(Person::getName).collect(Collectors.joining(", "));
        personList.stream().collect(Collectors.reducing(0.0, Person::getDebt, Double::sum));
        personList.stream().reduce(0.0, (a , b) -> a + b.getDebt(), Double::sum);
    }

    @Test
    void testPerformance() {
        long start = System.currentTimeMillis();
        Long count = DoubleStream.iterate(0.0, n -> n < 100000000.0, n -> n + 1.0).mapToObj(Double::valueOf).collect(Collectors.counting());
        long end = System.currentTimeMillis();
        double seconds = ((double)end-start)/1000.0;
        System.out.println("Collector " + seconds + ", count: " + count);

        long start2 = System.currentTimeMillis();
        Long count2 = DoubleStream.iterate(0.0, n -> n < 100000000.0, n -> n + 1.0).mapToObj(Double::valueOf).count();
        long end2 = System.currentTimeMillis();
        double seconds2 = ((double)end2-start2)/1000.0;
        System.out.println("Reduce " + seconds2 + ", count: " + count2);
    }

    @Test
    void testGrouping() {
        List<Person> personList = getPersonList();
        Map<Sex, List<Person>> peopleBySex = personList.stream().collect(groupingBy(Person::getSex));
        System.out.println(peopleBySex);

        // Grupowanie po płci i filtrowanie po debt
        Map<Sex, List<Person>> peopleBySexAndFilteredByDebt = personList.stream().collect(groupingBy(Person::getSex, filtering(person -> person.getDebt() > 1000, toList())));
        System.out.println(peopleBySexAndFilteredByDebt);

        Map<Sex, List<String>> peopleNameBySexFilteredByDebt = personList.stream().collect(groupingBy(Person::getSex, filtering(person -> person.getDebt() > 1000, mapping(Person::getName, toList()))));
        System.out.println(peopleNameBySexFilteredByDebt);

        //ilosc kobiet i mężczyzn
        Map<Sex, Long> sexAmount = personList.stream().collect(groupingBy(Person::getSex, counting()));
        System.out.println(sexAmount);

        //znalezienie kobiety, której suma zakupów jest największa - wykorzystanie collectora
        Optional<Person> woman = personList.stream()
                .collect(filtering(person -> person.getSex().equals(Sex.FEMALE), maxBy(Comparator.comparing(person -> person.getPurchases().stream().mapToDouble(purchase -> purchase.getQuantity() * purchase.getPrice().doubleValue()).sum()))));
        System.out.println(woman.get());
    }


    private List<Person> getPersonList() {
        //Address
        Address address1 = new Address("Gornicza 12/8", "Konin", "62-510");
        Address address2 = new Address("Warszawska 22", "Poznan", "61-560");
        Address address3 = new Address("Poznanska 26", "Warszawa", "10-222");
        Address address4 = new Address("Kwiatowa 33/9", "Poznan", "61-222");
        Address address5 = new Address("Kwiatowa 33/7", "Poznan", "61-222");
        Address address6 = new Address("Margaretkowa", "Kutno", "63-666");
        //Purchase
        Purchase purchase0 = new Purchase("Mydlo", 3, BigDecimal.valueOf(19.22));
        Purchase purchase1 = new Purchase("Recznik", 2, BigDecimal.valueOf(50.66));
        Purchase purchase2 = new Purchase("Maslo", 2, BigDecimal.valueOf(10.65));
        Purchase purchase3 = new Purchase("Chleb", 2, BigDecimal.valueOf(8.65));
        Purchase purchase4 = new Purchase("Kosa spalinowa", 1, BigDecimal.valueOf(500.22));
        Purchase purchase5 = new Purchase("Szynka", 1, BigDecimal.valueOf(8.92));
        Purchase purchase6 = new Purchase("Samochodzik", 1, BigDecimal.valueOf(16.9));
        Purchase purchase7 = new Purchase("Lody", 1, BigDecimal.valueOf(20.44));
        Purchase purchase8 = new Purchase("Sos", 1, BigDecimal.valueOf(4.44));
        Purchase purchase9 = new Purchase("Woda", 6, BigDecimal.valueOf(15.67));
        //Person
        Person person1 = new Person("Karol", "Lewandowski", LocalDate.of(1992, 1, 22), Sex.MALE, 2, 3400, true, address1, List.of(purchase0, purchase3, purchase7));
        Person person2 = new Person("Ala", "Lewandowski", LocalDate.of(2002, 3, 25), Sex.FEMALE, 2, 3400, true, address1, List.of(purchase1, purchase8, purchase9));
        Person person3 = new Person("Maria", "Grzelak", LocalDate.of(1986, 1, 22), Sex.FEMALE, 0, 1560, true, address2, List.of(purchase4, purchase5, purchase1));
        Person person4 = new Person("Kamil", "Sosnowski", LocalDate.of(1987, 1, 22), Sex.MALE, 5, 2200, false, address3, List.of(purchase2, purchase6, purchase3));
        Person person5 = new Person("Kinga", "Katafoni", LocalDate.of(2001, 1, 22), Sex.FEMALE, 1, 16500, true, address4, List.of(purchase6, purchase4, purchase1));
        Person person6 = new Person("Zuzanna", "Mankowska", LocalDate.of(1956, 1, 22), Sex.FEMALE, 3, 0, false, address5, List.of(purchase8, purchase6, purchase0));
        Person person7 = new Person("Mikolaj", "Grobelny", LocalDate.of(1962, 1, 22), Sex.MALE, 1, 230000, false, address6, List.of(purchase5, purchase2, purchase9));
        Person person8 = new Person("Marcin", "Stuleja", LocalDate.of(2010, 1, 22), Sex.MALE, 0, 560, true, address4, List.of(purchase7, purchase4, purchase0));

        return List.of(person1, person2, person3, person4, person5, person6, person7, person8);
    }
}
