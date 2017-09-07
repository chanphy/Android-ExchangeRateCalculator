
package zucc.com.myapplication;

/**
 * Created by admin on 2017/7/4.
 */

public class History_info {

    private String mAmount, fornumber, homnumber, mConverted;
        public History_info(){}

    public History_info(String mAmount, String fornumber, String homnumber, String mConverted) {
        this.mAmount = mAmount;
        this.fornumber = fornumber;
        this.homnumber = homnumber;
        this.mConverted = mConverted;
    }



        public String getmAmount() {
            return mAmount;
        }

        public String getFornumber() {
            return fornumber;
        }

        public String getHomnumber() {
            return homnumber;
        }

        public String getmConverted() {
            return mConverted;
        }




        public void setmAmount(String mAmount) {
            this.mAmount = mAmount;
        }

        public void setHomnumber(String homnumber) {
            this.homnumber = homnumber;
        }

        public void setFornumber(String fornumber) {
            this.fornumber = fornumber;
        }

        public void setmConverted(String mConverted) {
            this.mConverted = mConverted;
        }

}
