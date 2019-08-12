package jakojaannos.test.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;
import java.util.stream.Stream;

public class Premise<TInput, TExpected> {
    private final Function<Premise<TInput, TExpected>, Stream<Expectation>> expectationFactory;

    public Premise(Function<Premise<TInput, TExpected>, Stream<Expectation>> expectationFactory) {
        this.expectationFactory = expectationFactory;
    }

    public Stream<Expectation> asStream() {
        return expectationFactory.apply(this);
    }

    public PremiseInput input(TInput input) {
        return new PremiseInput(input);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public class Expectation {
        @Getter private final TInput input;
        @Getter private final TExpected expected;
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public class PremiseInput {
        private final TInput input;

        public Expectation yields(TExpected expected) {
            return new Expectation(this.input, expected);
        }
    }
}
