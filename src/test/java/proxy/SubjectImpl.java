package proxy;

/**
 * @Description TODO
 * @Date 2020/11/25 16:54
 * @Author zsj
 */
public class SubjectImpl implements Subject {
    @Override
    public void Request() {
        int i = 1;
        System.out.println("访问真实主题方法..." + i);
    }
}
