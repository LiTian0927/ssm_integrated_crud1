//1.页面加载完以后,直接发送一个ajax请求,要到分页数据
window.onload = function () {
    var vue = new Vue({
        el: "#app",
        data: {
            pageInformation: {},
            depts: [],
            formData: {empName: '', gender: "男", email: '', deptId: "1"},
            checkEmpName: -1,  //校验用户名的参数，-1 初始化，0 校验失败，1 校验成功，2 用户名已存在
            checkEmail: -1,//校验邮箱的参数，-1 初始化，0 校验失败，1 校验成功
            validationFailMsg_empName: '',
            validationFailMsg_email: '',
            currentEmpId: '',
            currentEmpName: '',
            checkedEmpNames:[],
            checkedEmpIds: []
        },
        mounted() {
            this.initPageInfo();
        },
        methods: {
            initPageInfo() {
                axios({
                    url: "/ssm/emps",
                    method: "GET",
                    params: {
                        pageNum: 1
                    }
                }).then(response => {
                    console.log(response.data)
                    vue.pageInformation = response.data.extension.pageInfo;
                }).catch(error => {
                    console.error(error)
                });
            },
            resetCheckParam() {
                vue.checkEmail = -1;
                vue.checkEmpName = -1;
            },
            resetCheckboxes() {
               let checkAllBox = this.$refs.checkAll;
               checkAllBox.checked = false;
               checkAllBox.dispatchEvent(new Event("change"));
            },
            onPageChanged(pageNum) {
                this.resetCheckboxes();
                this.resetCheckParam();
                axios({
                    url: "/ssm/emps",
                    method: "GET",
                    params: {
                        pageNum: pageNum
                    }
                }).then(response => {
                    console.log(response.data)
                    vue.pageInformation = response.data.extension.pageInfo;
                }).catch(error => {
                    console.error(error)
                });
            },
            /**
             * 查出所有的部门信息显示在下拉列表中
             */
            getDepts() {
                //打开按钮时重置表单
                let form = document.getElementsByTagName("form")[0];
                form.reset();
                vue.resetCheckParam();
                axios({
                    url: "/ssm/depts",
                    method: "GET"
                }).then(response => {
                    // console.log(response.data);
                    vue.depts = response.data.extension.depts;
                });
            },
            getGender() {
                vue.formData.gender = event.target.value;
            },
            getDeptId() {
                vue.formData.deptId = event.target.selectedOptions[0].value;
            },
            /**
             * 前端校验用户名
             * 正则表达式校验表单中的数据
             */
            validateEmpName() {
                vue.formData.empName = event.target.value;
                vue.checkEmpName = -1;
                let regExpEmpName = /(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5})/;//6到16位（字母，数字，下划线，减号）
                if (vue.formData.empName == '') {
                    vue.checkEmpName = -1;
                    return false;
                } else if (!regExpEmpName.test(vue.formData.empName)) {
                    //用户名不能通过校验
                    //alert("用户名必须是6-16位字母,数字,下划线,或者减号的组合");
                    vue.checkEmpName = 0;
                    vue.validationFailMsg_empName = "用户名必须是2-5位中文或者6-16位英文和数字的组合";
                    return false;
                } else {//发送异步请求，检查数据库中是否已经存在该用户名
                    axios({
                        url: '/ssm/checkEmpName',
                        method: "POST",
                        data: {empName: vue.formData.empName}
                    }).then(response => {
                        console.log(response.data);
                        if (response.data.code == 100) {
                            vue.checkEmpName = 1;
                            return true;
                        } else {
                            vue.checkEmpName = 0;
                            vue.validationFailMsg_empName = response.data.extension.validationFailMsg;
                            return false;
                        }
                    });
                }
            },
            /**
             * 前端校验邮箱
             * @returns {boolean}
             */
            validateEmail() {
                vue.formData.email = event.target.value;
                let regExpEmail = /([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/;
                if (vue.formData.email == '') {
                    vue.checkEmail = -1;
                    return false;
                } else if (!regExpEmail.test(vue.formData.email)) {
                    //email不能通过
                    //alert("邮箱格式不正确");
                    vue.checkEmail = 0;
                    vue.validationFailMsg_email = "邮箱格式不正确";
                    return false;
                } else {
                    vue.checkEmail = 1;
                    return true;
                }
            },
            /**
             * 保存按钮的点击事件,保存员工
             * 1.先对要提交给服务器的数据进行校验
             */
            saveEmp() {
                const emp_add_form = document.querySelector('#emp_add_model form');
                const add_form_data = Object.fromEntries(new FormData(emp_add_form));
                axios({
                    url: "/ssm/emps",
                    method: "POST",
                    data: add_form_data
                }).then(response => {
                    console.log(response.data);
                    if (response.data.code == 100) {//Msg.success
                        vue.resetCheckParam();
                        //1.关闭模态框
                        new bootstrap.Modal("#emp_add_model")._hideModal();
                        //2.来到最后一页,发送ajax请求显示的最后一页数据即可,直接传了一个非常大的页码,但是由于设置了reasonable参数,所以只会跳转到最后一页
                        vue.onPageChanged(vue.pageInformation.total + 1);
                    } else {
                        //显示失败信息
                        let errMap = response.data.extension.errMap;
                        if (undefined != errMap.empName) {
                            //显示用户名错误信息
                            vue.validationFailMsg_empName = errMap.empName;
                        }
                        if (undefined != errMap.email) {
                            //显示邮箱错误信息
                            vue.validationFailMsg_email = errMap.email;
                        }
                    }

                });
            },
            /**
             * 编辑按钮的点击事件
             * 发送异步请求实现数据回显
             */
            editEmp(empId) {
                vue.getDepts();//清空表单并获取depts数据
                vue.currentEmpId = empId;
                axios({
                    url: "/ssm/emps/" + empId,
                    method: "GET",
                }).then(response => {
                    // console.log(response.data);
                    let employee = response.data.extension.employee;
                    vue.formData.empName = employee.empName;
                    vue.formData.email = employee.email;
                    vue.formData.gender = employee.gender;
                    vue.formData.deptId = employee.deptId;
                });
            },
            /**
             * 更新按钮的点击事件
             * @param empId
             * @returns {boolean}
             */
            updateEmp() {
                //获取表单数据
                const emp_edit_form = document.querySelector('#emp_edit_model form');
                const edit_form_data = Object.fromEntries(new FormData(emp_edit_form));
                axios({
                    url: "/ssm/emps",
                    method: "PUT",
                    data: edit_form_data
                }).then(response => {
                    if (response.data.code == 200) {
                        //显示失败信息
                        let errMap = response.data.extension.errMap;
                        if (undefined != errMap.email) {
                            //显示邮箱错误信息
                            vue.validationFailMsg_email = errMap.email;
                        }
                    } else {
                        //成功
                        console.log(response.data);
                        //重置检查参数
                        vue.resetCheckParam();
                        //关闭模态框
                        new bootstrap.Modal("#emp_edit_model")._hideModal();
                        //更新信息
                        vue.onPageChanged(vue.pageInformation.pageNum);
                    }
                });
            },
            /**
             * 员工表格中删除按钮的点击事件
             */
            onDeleteBtnClicked(empId, empName) {
                //显示模态框
                const emp_delete_modal = new bootstrap.Modal("#emp_delete_model", {backdrop: false}).show("#emp_delete_model");
                //为模态框中确认删除的名字赋值
                vue.currentEmpName = empName;
                //将empId传入
                vue.currentEmpId = empId;
            },
            /**
             * 单个员工的确认删除
             */
            deleteEmp(empId) {
                axios({
                    url: "/ssm/emps/" + empId,
                    method: "DELETE"
                }).then(response => {
                    //关闭模态框
                    const emp_delete_modal = new bootstrap.Modal("#emp_delete_model")._hideModal();
                    //跳转到当前页面
                    vue.onPageChanged(vue.pageInformation.pageNum);
                });
            },
            /**
             * 批量删除按钮的点击事件
             */
            onDeleteBatchBtnClicked() {
                //显示模态框
                new bootstrap.Modal("#emp_delete_batch_model", {backdrop: false}).show("#emp_delete_batch_model");
            },
            /**
             * 确认批量删除按钮的点击事件
             */
            deleteEmpInBatch() {
                if (this.checkedEmpIds.length > 0 && this.checkedEmpNames.length > 0) {
                    axios({
                        url: "/ssm/emps",
                        method: "DELETE",
                        data: this.checkedEmpIds
                    }).then(response => {
                        if (response.data.code == 100) {
                            console.log(this.checkedEmpIds.length);
                            //关闭确认删除的模态框
                            new bootstrap.Modal("#emp_delete_batch_model")._hideModal();
                            //为删除成功的模态框绑定事件
                            const deleteInBatchResultModal = document.getElementById('deleteEmpInBatchResultModel');
                            //在关闭删除成功模态框时，再跳转页面
                            deleteInBatchResultModal.addEventListener('hide.bs.modal', () => this.onPageChanged(this.pageInformation.pageNum));
                            //弹出模态框
                            new bootstrap.Modal("#deleteEmpInBatchResultModel").show("#deleteEmpInBatchResultModel");
                        }
                    });
                }
            },
            /**
             * 点击表头checkbox，全选/全不选功能
             */
            checkAll() {
                //获取到复选框
                let checkEmps = document.getElementsByName("checkEmp");
                console.log("当前复选框数量为:" + checkEmps.length);
                if (this.$refs.checkAll.checked) {
                    //全选
                    checkEmps.forEach(checkItem => {
                        checkItem.checked = true;
                        checkItem.dispatchEvent(new Event('change'));//发起一个change事件，这是由于change事件只能由用户的交互发起，不能由程序的变更发起
                    });
                }else{
                    //全不选
                    checkEmps.forEach(checkItem => {
                        checkItem.checked = false;
                        checkItem.dispatchEvent(new Event('change'));//发起一个change事件
                    });
                }
            },
            /**
             * 每个emp的复选框的变化事件
             */
            checkOneEmp({target}) {//将事件对象的所有属性解构到单独的变量中
                let empId = target.getAttributeNode("emp-id").value;
                let empName = target.getAttributeNode("emp-name").value;
                let index = this.checkedEmpIds.indexOf(empId);
                if (!target.checked) {
                    //如果点击后没有选中，那么则没有全部选中，要将全选的复选框取消选中
                    this.$refs.checkAll.checked = false;
                    //若数组中存在该empId，则要删除掉
                    if (index > -1) {
                        this.checkedEmpIds.splice(index, 1);
                    }
                    //若数组中存在该empName，则要删除掉
                    index =  this.checkedEmpNames.indexOf(empName);
                    if (index > -1) {
                        this.checkedEmpNames.splice(index, 1);
                    }
                }else {//选中后，检查是否全部按钮都选中，若是，则需要将全选的复选框选中
                    let checkEmps = document.getElementsByName("checkEmp");
                    //Array.prototype.every() 方法对数组中的每个元素执行一次回调函数，直到它找到一个使回调函数返回 false（表示不匹配）的元素。如果没有这样的元素，则返回 true。
                    if (Array.prototype.every.call(checkEmps, checkEmp => checkEmp.checked)) {
                        //所有复选框都被选中
                        this.$refs.checkAll.checked = true;
                    }
                    //选中一个员工，则将该员工的id和name传入
                    index = this.checkedEmpIds.indexOf(empId);
                    if (index < 0) {
                        //数组中不存在该id,才将其保存到数组中
                        this.checkedEmpIds.push(empId);
                    }
                    index = this.checkedEmpNames.indexOf(empName);
                    if (index < 0) {
                        //数组中不存在该name,才将其保存到数组中
                        this.checkedEmpNames.push(empName);
                    }
                }
            },
        }
    });
};
