package utils;

public class Tuple<L, R> {
    private final L left;
    private final R right;

    public Tuple(L l, R r) {
        left = l;
        right = r;
    }

    public L left(){
        return left;
    }

    public R right() {
        return right;
    }
}
