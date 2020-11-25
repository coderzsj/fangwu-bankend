package principle;

import lombok.Data;

/**
 * @Description TODO
 * @Date 2020/11/25 16:35
 * @Author zsj
 */
//经纪人
@Data
class Agent {
    private Star    star;
    private Fans    fans;
    private Company company;
    public void meeting() {
        System.out.println(fans.getName() + "与明星" + star.getName() + "见面了。");
    }

    public void business() {
        System.out.println(company.getName() + "与明星" + star.getName() + "洽淡业务。");
    }
}
