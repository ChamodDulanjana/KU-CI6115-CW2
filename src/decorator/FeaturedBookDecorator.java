package decorator;

public class FeaturedBookDecorator extends BookDecorator {
    public FeaturedBookDecorator(BookComponent inner) {
        super(inner);
    }

    @Override
    public String getDescription() {
        return inner.getDescription() + " [FEATURED]";
    }
}
