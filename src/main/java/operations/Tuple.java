package operations;

/**
 * Helper-class that may holds two objects of different type.
 * 
 * @author Sebastian
 *
 * @param <L>
 *          Type of the 'left' object
 * @param <R>
 *          Type of the 'right' object
 */
public class Tuple<L, R>
{
  private L left = null;
  private R right = null;


  public Tuple(L left, R right)
  {
    this.left = left;
    this.right = right;
  }


  public L getLeft()
  {
    return left;
  }


  public void setLeft(L left)
  {
    this.left = left;
  }


  public R getRight()
  {
    return right;
  }


  public void setRight(R right)
  {
    this.right = right;
  }

}
