
package zucc.com.myapplication;

/**
 * Created by admin on 2017/7/5.
 */

public class Hello_info {

    private String cnyrate;
    private Integer id;
    public Hello_info(){}

    public Hello_info(Integer id,String cnyrate) {
        this.id = id;
        this.cnyrate = cnyrate;

    }


    public Integer getId() {
        return id;
    }
    public String getCnyrate() {
        return cnyrate;
    }


    public void setId(Integer id){this.id = id;}
    public void setCnyrate(String cnyrate) {
        this.cnyrate = cnyrate;
    }


}