import com.liuyun.common.Token;
import org.junit.Test;

/**
 * @PackageName PACKAGE_NAME
 * @Description TODO
 * @Author Ren
 * @Date 2024/4/6 18:51
 * @Version 1.0
 */

public class test {

    public static void main(String[] args) {
        Token token = new Token();
        token.resolveToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyTmFtZSI6IuS7u-m5j-a2myIsImV4cCI6MTcxMjQwMzYwMX0.imP-DOqnkayx-AtzZtE-IUvzh9QgIa3ZrO5vhY2B2vY");
    }
    @Test
    public void test2(){


        int[] nums1=new int[]{1,3};
       int[] nums2=new int[]{2,4,6,9};
        int m=nums1.length;
        int n=nums2.length;
        int[] nums3=new int[n+m];
        int p1=0,p2=0;
        int i=0;
        for(;p1<m&&p2<n;i++){
            if(nums1[p1]<nums2[p2]){
                nums3[i]=nums1[p1];
                p1++;
            }else {
                nums3[i]=nums2[p2];
                p2++;
            }
        }
        for(;i<n+m;i++){
            if(p1<m){
                nums3[i]=nums1[p1];
                p1++;
            }
            if(p2<n){
                nums3[i]=nums2[p2];
                p2++;
            }
        }
            if ((n + m) % 2 == 0) {
                System.out.println((nums3[(n + m) / 2] + nums3[(n + m - 1) / 2]) / 2.0);
            } else {
                System.out.println(nums3[(n + m) / 2]);
            }

        }
        @Test
        int t3(){
        }
}


