//给个 出一32 位的有符号整数，你需要将这个整数中每位上的数字进行反转。
//
// 示例 1:
//
// 输入: 123
//输出: 321
//
//
// 示例 2:
//
// 输入: -123
//输出: -321
//
//
// 示例 3:
//
// 输入: 120
//输出: 21
//
//
// 注意:
//
// 假设我们的环境只能存储得下 32 位的有符号整数，则其数值范围为 [−231, 231 − 1]。请根据这个假设，如果反转后整数溢出那么就返回 0。
// Related Topics 数学
// 👍 2363 👎 0


package leetcode.editor.cn;

public class ReverseInteger {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int reverse(int x) {
            String        str = String.valueOf(x);
            String[]      arr = str.split(""); // 用,分割        int[] chars = new int[];
            StringBuilder sb  = new StringBuilder();
            for (int i = arr.length - 1; i >= 0; i--) {
                if ("-".equals(arr[i])) {
                    sb.insert(0, "-");
                } else {
                    sb.append(arr[i]);
                }
            }
            return Integer.valueOf(String.valueOf(sb));
        }
    }
//leetcode submit region end(Prohibit modification and deletion)


    public static void main(String[] args) {
        Solution solution = new ReverseInteger().new Solution();
        int      reverse  = solution.reverse(-123);
        System.out.println(reverse);
    }

}
