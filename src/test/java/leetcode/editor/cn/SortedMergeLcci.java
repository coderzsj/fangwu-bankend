//给定两个排序后的数组 A 和 B，其中 A 的末端有足够的缓冲空间容纳 B。 编写一个方法，将 B 合并入 A 并排序。
//
// 初始化 A 和 B 的元素数量分别为 m 和 n。
//
// 示例:
//
// 输入:
//A = [1,2,3,0,0,0], m = 3
//B = [2,5,6],       n = 3
//
//输出: [1,2,2,3,5,6]
//
// 说明:
//
//
// A.length == n + m
//
// Related Topics 数组 双指针
// 👍 85 👎 0


package leetcode.editor.cn;

public class SortedMergeLcci {

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public void merge(int[] A, int m, int[] B, int n) {
            int round       = (int) Math.ceil((double) m / n); //进一法取A和B要比较的轮数
            int indexA      = n - 1;
            int indexB      = m - 1;
            int indexMerged = m + n - 1;
            while (round >= 1) {
                while (indexA >= 0 && indexB >= 0) {
                    if (A[indexA] >= B[indexB]) {
                        A[indexMerged--] = A[indexA--];
                    } else {
                        A[indexMerged--] = B[indexB--];
                    }
                }
                round--;
                indexA = n - 1;
            }
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

    public static void main(String[] args) {
        Solution solution = new SortedMergeLcci().new Solution();
    }
}
