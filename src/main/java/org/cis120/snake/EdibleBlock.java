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
     * @param size
     * @param courtWidth
     * @param courtHeight
     */
    public EdibleBlock(int v, int px, int py, int size, int courtWidth, int courtHeight) {
        super(v, px, py, size, size, courtWidth, courtHeight);
        this.courtWidth = courtWidth;
        this.courtHeight = courtHeight;
    }

    public abstract void eatInteraction (Snake snake);

    @Override
    public String fileSaveInfo() {
        return getPx() + "," + getPy();
    }

    protected void randomizeMove() {
        this.setPx(RandomNum(getWidth() / 2 + 10, this.courtWidth - this.getWidth()) - 10);
        this.setPy(RandomNum(getHeight() / 2 + 10, this.courtHeight - this.getHeight() - 10));
    }
}
