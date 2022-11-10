# LTL模型检验算法实现文档

519030910313 陈晴

---

# 1. Parser

## 读入 Transition System

代码中，我们根据 transition system 的定义，通过一列 state，一列 action，一列 proposition，一个 transition mapping 和一个记录每个 state 所有 label 的 mapping 来表示一个 transition system （`src/TS/TS.java`，构造函数 `TS(Scanner scanner)` ）。

从 TS.txt 中读入的信息构造 transition system 是平凡的。

## 读入 LTL Formulas

（见类 Parser, `src/Parser/Parser.java`）

LTL Formus 的输入应放在`benchmark.txt`内。对于输入的字符串 `str`，我们首先去除其中的所有空格。现在假设 `str` 中不包含任何空格，接下来我们用递归的方式 parse `str`。

- 第一步：做一遍括号匹配，将 `str` 最外层的括号去除，得到 `str'`。
- 第一步：检查 `str'` 是否为长度为 1，且是小写字符。若是，我们为 `str'` 构造一个 atomic proposition。
- 若第一步不满足，则第二步：检查 `str'` 是否为二元运算。由于 `str'` 已经没有最外层括号，因此我们一边做括号匹配一边扫描 `str'` 中的每个字符。
    - 如果出现**不被任何括号所包含的** `/\`, `\/`, `U` 和 `->`，则我们分别递归 parse 该运算符的左侧和右侧，然后构造相应的二元运算，将两侧 parse 得到的 formula 作为该二元运算的 `lhs` 和 `rhs`。
    - 如果没有出现不被任何括号包含的上述二元匀速符，则进入第三步。
    - 值得注意的是，当二元运算为 `𝜑 \/ ψ` 时，我们生成等价的 `!(!𝜑 /\ !ψ)`，而不是直接生成一个析取运算。当二元运算为 `𝜑 -> ψ` 时，我们生成等价的 `!(𝜑 /\ (!(ψ)))`，而不是直接生成一个蕴含运算。**这确保了 parse 得到的 formula 中不包含析取运算符和蕴含运算符。**
- 若前两步均不满足，则第三步：检查 `str'` 是否为一元运算。由于 `str'` 已经没有最外层括号，因此我们检查 `str'` 的起始字符是否为 `!`, `X`, `F`, `G` 中的一个。如果满足，则我们递归 parse `str'` 除起始字符外的剩余部分，然后构造相应的一元运算，将递归 parse 得到的 formula 作为该一元运算的 `body`。
    - 值得注意的是，当一元运算为 `F(𝜑)` 时，我们生成等价的 `true U 𝜑` ，而不是直接生成一个 eventually 运算。当一元运算为 `G(𝜑)` 时，我们生成等价的 `!(true U (!(𝜑)))`，而不是直接生成一个 always 运算。**这确保了 parse 得到的 formula 中不包含 eventually 运算符和 always 运算符。**
- 若上述三步均不满足，说明输入的 `str` 不是一个合法的 formula 表示。

通过上述的 parse 算法，我们对于输入的 `str` 能够构造出对应的 formula，且该 formula 中仅包含 conjunction, until, negation 和 next 四种运算符。

# 2. 从 LTL Formula 到 GNBA 的等价构造

对于一个输入的 formula，我们按照教材中的算法构造其等价的 GNBA（`src/BA/GNBA.java`，GNBA 构造函数）

# 3. 从 GNBA 到 NBA 的等价转换

对于一个给定的 GNBA，我们按照教材中的算法构造其等价的 NBA（`src/BA/NBA.java`, NBA 构造函数）

# 4. TS 与 NBA 之间的 Product Construction

对于一个给定的 transition system 和 NBA，我们按照教材中的算法计算其 product transition system（`src/TS/TS.java`，构造函数 `TS(TS ts, NBA nba)`）

# 5. Nested Depth-First Search Algorithm

按照教材中的算法，实现在了 `src/TS/TS.java` 中的 `PersistenceChecking()` 函数中。



##### 所有GNBA、NBA、TS这些中间结果可用 IntelliJ IDEA 在单步调试的时候在`Main.java`的函数`process (TS ts, FormulaNode formula)`里看到，不再特意写输出。