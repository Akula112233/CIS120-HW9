package org.cis120.snake;

public abstract class EdibleBlock extends GameObj{

    public static int RandomNum(int min, int max){
        return (int)((Math.random() * (max - min)) + min);
    }

    private final int courtWidth;
    private final int courtHeight;

    /**
     * Constructor
     *
     * @param v
     * @param px
     * @param py
     * @param width
     * @param height
     * @param courtWidth
     * @param courtHeight
     */
    public EdibleBlock(int v, int px, int py, int width, int height, int courtWidth, int courtHeight) {
        super(v, px, py, width, height, courtWidth, courtHeight);
        this.courtWidth = courtWidth;
        this.courtHeight = courtHeight;
    }

    public abstract void eatInteraction (Snake snake);

    protected void randomizeMove() {
        this.setPx(RandomNum(getWidth() / 2, this.courtWidth + this.getWidth()));
        this.setPy(RandomNum(getHeight() / 2, this.courtHeight + this.getHeight()));
    }
}
