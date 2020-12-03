package proxy;

/**
 * @Description TODO
 * @Date 2020/11/25 16:54
 * @Author zsj
 */
public class Proxy{
    private SubjectImpl realsubject;
    public void Request() {
        if (realsubject == null) {
            realsubject = new SubjectImpl();
        }
        preRequest();
        realsubject.Request();
        postRequest();
    }
    public void preRequest() {
        System.out.println("访问真实主题之前的预处理。");
    }
    public void postRequest() {
        System.out.println("访问真实主题之后的后续处理。");
    }
}
