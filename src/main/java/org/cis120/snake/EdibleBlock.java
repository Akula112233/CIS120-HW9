package org.cis120.snake;

public abstract class EdibleBlock extends GameObj {

    public static int RandomNum(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
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

    public abstract void eatInteraction(Snake snake, boolean debug);

    @Override
    public String fileSaveInfo() {
        return getPx() + "," + getPy();
    }

    protected void randomizeMove(boolean debug) {
        int margin = 10;
        if (debug) {
            this.setPx(100 - getWidth());
            this.setPy(100 - getHeight());
            return;
        }
        this.setPx(RandomNum(getWidth() / 2 + margin, this.courtWidth - this.getWidth()) - margin);
        this.setPy(
                RandomNum(getHeight() / 2 + margin, this.courtHeight - this.getHeight() - margin)
        );
    }
}
