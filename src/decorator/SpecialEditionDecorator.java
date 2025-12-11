package decorator;

public class SpecialEditionDecorator extends  BookDecorator {
    private final String editionInfo;

    public SpecialEditionDecorator(BookComponent inner, String editionInfo) {
        super(inner);
        this.editionInfo = editionInfo;
    }

    @Override
    public String getDescription() {
        return inner.getDescription() + " [Special Edition: " + editionInfo + "]";
    }
}
