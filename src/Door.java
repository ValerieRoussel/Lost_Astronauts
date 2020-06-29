public class Door extends Obj {
    char nextRoomCode;
    boolean vertical;

    public Door(int x, int y, int width, int height, String imgPath, boolean vert, char next) {
        super(x, y, width, height, imgPath);
        vertical = vert;
        if (!vertical) {
            this.width = 32;
            this.height = 16;
            updateRect();
        }
        nextRoomCode = next;
    }

}
