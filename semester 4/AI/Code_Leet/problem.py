
class Solution:
    def candy(self, ratings: list[int]) -> int:
        candidates = [1] * len(ratings)

        for index in range(1, len(ratings)):
            if ratings[index] > ratings[index - 1]:
                candidates[index] = candidates[index - 1] + 1

        for index in range(len(ratings) - 2, -1, -1):
            if ratings[index] > ratings[index + 1]:
                candidates[index] = max(candidates[index], candidates[index + 1] + 1)

        print(candidates)
        return sum(candidates)
    
sol = Solution()
print(sol.candy([1,0,2]))
            