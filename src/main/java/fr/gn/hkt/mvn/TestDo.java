package fr.gn.hkt.mvn;

import fr.gn.hkt.mvn.data.Maybe;
import fr.gn.hkt.mvn.syntax.Do;

import java.time.LocalDate;

import static fr.gn.hkt.mvn.Maybes.Just;
import static fr.gn.hkt.mvn.Maybes.Nothing;

public final class TestDo {
    private TestDo() {}

    public static void main(String[] args) {
        final Maybe<Person> maybePers = Do
            .$(Maybe.instances(), Maybe::coerce

                , () -> getName("toto")

                , __ -> getAge("toto")

                , TestDo::getBirthDate

                , Person::new);

        final Maybe<Person> maybePers2 = Do
            .$(Maybe.instances(), Maybe::coerce

                , () -> getName("toto")

                , __ -> getAge("toto") // ignoring arg

                , __ -> getBirthDate("titi", __._2()) // ignoring the 2nd arg

                , __ -> new Person("toto", 35, __._3())); // ignoring all args but the 3rd
    }

    private static Maybe<String> getName(String id) {
        return id.equals("toto") ? Just("Toto Titi") : Nothing();
    }

    private static Maybe<Integer> getAge(String id) {
        return id.equals("toto") ? Just(36) : Nothing();
    }

    private static Maybe<LocalDate> getBirthDate(String name, Integer age) {
        return name.equals("Toto Titi") && age < 40 ? Just(LocalDate.now()) : Nothing();
    }

    private static final class Person {
        final String name;
        final Integer age;
        final LocalDate birth;

        Person(String name, Integer age, LocalDate birth) {
            this.name = name;
            this.age = age;
            this.birth = birth;
        }

        @Override
        public String toString() {
            return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", birth=" + birth +
                '}';
        }
    }
}
