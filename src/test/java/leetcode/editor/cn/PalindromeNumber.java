//判断一个整数是否是回文数。回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。
//
// 示例 1:
//
// 输入: 121
//输出: true
//
//
// 示例 2:
//
// 输入: -121
//输出: false
//解释: 从左向右读, 为 -121 。 从右向左读, 为 121- 。因此它不是一个回文数。
//
//
// 示例 3:
//
// 输入: 10
//输出: false
//解释: 从右向左读, 为 01 。因此它不是一个回文数。
//
//
// 进阶:
//
// 你能不将整数转为字符串来解决这个问题吗？
// Related Topics 数学
// 👍 1319 👎 0


package leetcode.editor.cn;

public class PalindromeNumber {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public boolean isPalindrome(int x) {
            String        string  = String.valueOf(x);
            StringBuilder sb      = new StringBuilder(string);
            StringBuilder reverse = sb.reverse();
            if (string.equals(reverse.toString())) {
                return true;
            }
            return false;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)


    public static void main(String[] args) {
        Solution solution = new PalindromeNumber().new Solution();
        boolean  palindrome = solution.isPalindrome(121);
        System.out.println(palindrome);
    }
}
