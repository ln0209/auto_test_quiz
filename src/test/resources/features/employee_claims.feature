# language: zh-CN
# 功能：员工报销申请管理

@employee_claims
功能: 创建并验证员工报销申请

  场景大纲: 成功创建并提交一条完整的员工报销申请
  假设 用户打开测试网站 "https://opensource-demo.orangehrmlive.com/"
  当 用户使用用户名 "<username>" 和密码 "<password>" 登录系统
  并且 用户导航到Claims模块
  并且 点击"Employee Claims"选项
  并且 点击"Assign Claims"按钮
  并且 在创建报销请求表单中填写如下信息：
  | Field          | Value             |
  | Employee Name  | <employee_name>   |
  | Event          | <event>           |
  | Currency       | <currency>        |
  并且 点击"Create"按钮
  那么 系统应显示成功消息 "<create_success_message>"
  并且 用户应被导航到Assign Claim详情页面
  并且 详情页面上的基础信息应与填写的内容一致：
  | Field          | Value             |
  | Employee Name  | <employee_name>   |
  | Event          | <event>           |
  | Currency       | <currency>        |
  当 在详情页面点击"Add Expenses"按钮
  并且 在添加费用表单中填写如下信息：
  | Field         | Value             |
  | Expense Type  | <expense_type>    |
  | Date          | <expense_date>    |
  | Amount        | <amount>          |
  并且 点击"Submit"按钮
  那么 系统应显示成功消息 "<expense_success_message>"
  并且 费用列表中的最新记录应与填写的信息一致
  当 用户点击"Back"按钮
  那么 用户应返回到Assign Claim列表页面
  并且 列表中应存在员工为"<employee_name>"的报销申请记录

    例子:
      | username | password | employee_name   | event              | currency | create_success_message      | expense_type   | expense_date | amount | expense_success_message   |
      | Admin    | admin123 | Amelia Brown    | Travel allowances  | Euro     | Claim assigned successfully | Accommodation  | 2024-06-01   | 250.50 | Expense added successfully|