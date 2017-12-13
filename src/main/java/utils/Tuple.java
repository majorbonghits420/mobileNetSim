package utils;

/**
 * Generic class for a two element tuple
 */
public class Tuple<L, R> {
    private final L left; /**< Left value of the tuple  */
    private final R right; /**< Right value of the tuple */

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
